/*
 * Copyright ConsenSys Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package net.consensys.linea.zktracer.module.hub.section.call;

import static net.consensys.linea.zktracer.module.hub.fragment.scenario.CallScenarioFragment.CallScenario.*;
import static net.consensys.linea.zktracer.types.AddressUtils.isPrecompile;
import static net.consensys.linea.zktracer.types.Conversions.bytesToBoolean;

import com.google.common.base.Preconditions;
import net.consensys.linea.zktracer.module.hub.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.Factories;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.defer.ContextExitDefer;
import net.consensys.linea.zktracer.module.hub.defer.PostExecDefer;
import net.consensys.linea.zktracer.module.hub.defer.PostRollbackDefer;
import net.consensys.linea.zktracer.module.hub.defer.PostTransactionDefer;
import net.consensys.linea.zktracer.module.hub.defer.ReEnterContextDefer;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.DomSubStampsSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.MxpCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.StpCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.oob.opcodes.CallOobCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.oob.opcodes.XCallOobCall;
import net.consensys.linea.zktracer.module.hub.fragment.scenario.CallScenarioFragment;
import net.consensys.linea.zktracer.module.hub.section.TraceSection;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import net.consensys.linea.zktracer.types.EWord;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.operation.Operation;
import org.hyperledger.besu.evm.worldstate.WorldUpdater;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class CallSection extends TraceSection
    implements PostExecDefer,
        PostRollbackDefer,
        PostTransactionDefer,
        ReEnterContextDefer,
        ContextExitDefer {

  // row i+0
  private final CallScenarioFragment scenarioFragment = new CallScenarioFragment();

  // row i+2
  private final ImcFragment firstImcFragment;

  // Just before call

  private ContextFragment finalContextFragment;
  private Bytes rawCalleeAddress;

  // Just before the CALL Opcode
  private AccountSnapshot preOpcodeCallerSnapshot;
  private AccountSnapshot preOpcodeCalleeSnapshot;

  // Just after the CALL Opcode
  private AccountSnapshot postOpcodeCallerSnapshot;
  private AccountSnapshot postOpcodeCalleeSnapshot;

  // Just before re-entry
  private AccountSnapshot preReEntryCallerSnapshot;
  private AccountSnapshot preReEntryCalleeSnapshot;

  // Just after re-entry
  private AccountSnapshot postReEntryCallerSnapshot;
  private AccountSnapshot postReEntryCalleeSnapshot;

  private boolean isSelfCall;
  private boolean callCanTransferValue;

  private Wei value;

  public CallSection(Hub hub) {
    super(hub, maxNumberOfLines(hub));
    hub.addTraceSection(this);
    final short exceptions = hub.pch().exceptions();

    // row i, i + 1 and i + 2
    final ContextFragment currentContextFragment = ContextFragment.readCurrentContextData(hub);
    this.firstImcFragment = ImcFragment.empty(hub);

    this.addFragmentsAndStack(hub, scenarioFragment, currentContextFragment, firstImcFragment);

    if (Exceptions.any(exceptions)) {
      scenarioFragment.setScenario(CALL_EXCEPTION);
      final XCallOobCall oobCall = new XCallOobCall();
      firstImcFragment.callOob(oobCall);
    }

    // STATICX cases
    if (Exceptions.staticFault(exceptions)) {
      return;
    }

    final MxpCall mxpCall = new MxpCall(hub);
    firstImcFragment.callMxp(mxpCall);
    Preconditions.checkArgument(mxpCall.mxpx == Exceptions.memoryExpansionException(exceptions));

    // MXPX case
    if (Exceptions.memoryExpansionException(exceptions)) {
      return;
    }

    final StpCall stpCall = new StpCall(hub, mxpCall.gasMxp);
    stpCall.stpCallForCalls(hub);
    firstImcFragment.callStp(stpCall);
    Preconditions.checkArgument(
        stpCall.outOfGasException() == Exceptions.outOfGasException(exceptions));

    final Address callerAddress = hub.currentFrame().callerAddress();
    this.preOpcodeCallerSnapshot = AccountSnapshot.canonical(hub, callerAddress);

    this.rawCalleeAddress = hub.currentFrame().frame().getStackItem(1);
    final Address calleeAddress = Address.extract(EWord.of(rawCalleeAddress)); // TODO check this
    this.preOpcodeCalleeSnapshot = AccountSnapshot.canonical(hub, calleeAddress);

    // OOGX case
    if (Exceptions.outOfGasException(exceptions)) {
      this.oogXCall(hub);
      return;
    }

    // The CALL is now unexceptional
    Preconditions.checkArgument(Exceptions.none(exceptions));

    final CallOobCall oobCall = new CallOobCall();
    firstImcFragment.callOob(oobCall);

    final boolean aborts = hub.pch().abortingConditions().any();
    Preconditions.checkArgument(oobCall.isAbortingCondition() == aborts);

    hub.defers().scheduleForPostExecution(this);
    hub.defers().scheduleForPostRollback(this, hub.currentFrame());
    hub.defers().scheduleForPostTransaction(this);

    if (aborts) {
      this.abortingCall(hub);
      return;
    }

    // The CALL is now unexceptional and un-aborted
    hub.defers().scheduleForContextExit(this, hub.currentFrame().id());
    hub.defers().scheduleForContextReEntry(this, hub.currentFrame());
    final WorldUpdater world = hub.messageFrame().getWorldUpdater();

    if (isPrecompile(calleeAddress)) {
      this.scenarioFragment.setScenario(CALL_PRC_UNDEFINED);
    } else {
      this.scenarioFragment.setScenario(
          world.get(calleeAddress).hasCode() ? CALL_SMC_UNDEFINED : CALL_EOA_SUCCESS_WONT_REVERT);

      // TODO is world == worldUpdater & what happen if get
      //  doesn't work ?
      // TODO: write a test where the recipient of the call does not exist in the state
    }

    this.callCanTransferValue = hub.currentFrame().opCode().callCanTransferValue();

    // TODO: lastContextFragment for PRC

    if (this.scenarioFragment.getScenario() == CALL_SMC_UNDEFINED) {
      this.finalContextFragment = ContextFragment.initializeNewExecutionContext(hub);
      this.isSelfCall = callerAddress.equals(calleeAddress);
    }

    if (this.scenarioFragment.getScenario() == CALL_EOA_SUCCESS_WONT_REVERT) {
      this.finalContextFragment = ContextFragment.nonExecutionProvidesEmptyReturnData(hub);
    }
  }

  private static short maxNumberOfLines(final Hub hub) {
    // 99 % of the time this number of rows will be sufficient
    if (Exceptions.any(hub.pch().exceptions())) {
      return 8;
    }
    if (hub.pch().abortingConditions().any()) {
      return 9;
    }
    return 12; // 12 = 2 (stack) + 5 (CALL prequel) + 5 (successful PRC, except BLAKE and MODEXP)
  }

  private void oogXCall(Hub hub) {

    final Factories factories = hub.factories();
    final AccountFragment callerAccountFragment =
        factories
            .accountFragment()
            .make(
                preOpcodeCallerSnapshot,
                preOpcodeCallerSnapshot,
                DomSubStampsSubFragment.standardDomSubStamps(this.hubStamp(), 0));

    final AccountFragment calleeAccountFragment =
        factories
            .accountFragment()
            .makeWithTrm(
                preOpcodeCalleeSnapshot,
                preOpcodeCalleeSnapshot,
                rawCalleeAddress,
                DomSubStampsSubFragment.standardDomSubStamps(this.hubStamp(), 1));

    this.addFragmentsWithoutStack(callerAccountFragment, calleeAccountFragment);
  }

  private void abortingCall(Hub hub) {
    this.scenarioFragment.setScenario(CALL_ABORT_WONT_REVERT);
    this.finalContextFragment = ContextFragment.nonExecutionProvidesEmptyReturnData(hub);
  }

  @Override
  public void resolvePostExecution(
      Hub hub, MessageFrame frame, Operation.OperationResult operationResult) {
    switch (this.scenarioFragment.getScenario()) {
      case CALL_EOA_SUCCESS_WONT_REVERT, CALL_SMC_UNDEFINED -> {
        this.postOpcodeCallerSnapshot =
            AccountSnapshot.canonical(hub, this.preOpcodeCallerSnapshot.address());
        this.postOpcodeCalleeSnapshot =
            AccountSnapshot.canonical(hub, this.preOpcodeCalleeSnapshot.address());

        if (isSelfCall && callCanTransferValue) {
          // In case of a self-call that transfers value, the balance of the caller
          // is decremented by the value transferred. This becomes the initial state
          // of the callee, which is then credited by that value. This can happen
          // only for the SMC case.
          value = Wei.of(frame.getStackItem(2).toUnsignedBigInteger());
          postOpcodeCallerSnapshot.decrementBalance(value);
          preOpcodeCalleeSnapshot.decrementBalance(value);
        }
      }
      case CALL_PRC_UNDEFINED -> {
        // TODO: implement
      }
    }

    final Factories factories = hub.factories();
    final AccountFragment firstCallerAccountFragment =
        factories
            .accountFragment()
            .make(
                preOpcodeCallerSnapshot,
                postOpcodeCallerSnapshot,
                DomSubStampsSubFragment.standardDomSubStamps(this.hubStamp(), 0));

    final AccountFragment firstCalleeAccountFragment =
        factories
            .accountFragment()
            .makeWithTrm(
                preOpcodeCalleeSnapshot,
                postOpcodeCalleeSnapshot,
                rawCalleeAddress,
                DomSubStampsSubFragment.standardDomSubStamps(this.hubStamp(), 1));

    this.addFragmentsWithoutStack(firstCallerAccountFragment, firstCalleeAccountFragment);
  }

  @Override
  public void resolvePostRollback(Hub hub, MessageFrame messageFrame, CallFrame callFrame) {
    final Factories factories = hub.factories();
    final AccountSnapshot postRollbackCalleeSnapshot =
        AccountSnapshot.canonical(hub, this.preOpcodeCalleeSnapshot.address());

    switch (this.scenarioFragment.getScenario()) {
      case CALL_ABORT_WONT_REVERT -> {
        this.scenarioFragment.setScenario(CALL_ABORT_WILL_REVERT);
        final AccountFragment undoingCalleeAccountFragment =
            factories
                .accountFragment()
                .make(
                    postOpcodeCalleeSnapshot,
                    postRollbackCalleeSnapshot,
                    DomSubStampsSubFragment.revertWithCurrentDomSubStamps(
                        this.commonValues.hubStamp,
                        this.commonValues.callFrame().revertStamp(),
                        0));
        this.addFragment(undoingCalleeAccountFragment);
      }

      case CALL_EOA_SUCCESS_WONT_REVERT -> {
        this.scenarioFragment.setScenario(CALL_EOA_SUCCESS_WILL_REVERT);
        final AccountSnapshot postRollbackCallerSnapshot =
            AccountSnapshot.canonical(hub, this.preOpcodeCallerSnapshot.address());

        final AccountFragment undoingCallerAccountFragment =
            factories
                .accountFragment()
                .make(
                    postOpcodeCallerSnapshot,
                    postRollbackCallerSnapshot,
                    DomSubStampsSubFragment.revertWithCurrentDomSubStamps(
                        this.hubStamp(), this.commonValues.callFrame().revertStamp(), 2));

        final AccountFragment undoingCalleeAccountFragment =
            factories
                .accountFragment()
                .make(
                    postOpcodeCalleeSnapshot,
                    postRollbackCalleeSnapshot,
                    DomSubStampsSubFragment.revertWithCurrentDomSubStamps(
                        this.hubStamp(), this.commonValues.callFrame().revertStamp(), 3));

        this.addFragmentsWithoutStack(undoingCallerAccountFragment, undoingCalleeAccountFragment);
      }
      default -> throw new IllegalArgumentException("What were you thinking ?");
    }
  }

  @Override
  public void resolvePostTransaction(
      Hub hub, WorldView state, Transaction tx, boolean isSuccessful) {

    this.addFragment(finalContextFragment);
  }

  @Override
  public void resolveAtContextReEntry(Hub hub, CallFrame frame) {
    switch (this.scenarioFragment.getScenario()) {
      case CALL_SMC_UNDEFINED -> {
        // TODO: what follows assumes that the caller's stack has been updated
        //  to contain the success bit of the call at traceContextReEntry.
        //  See issue #872.
        boolean successBit = bytesToBoolean(hub.messageFrame().getStackItem(0));
        if (successBit) {
          return;
        }

        scenarioFragment.setScenario(CALL_SMC_FAILURE_WONT_REVERT);

        preReEntryCallerSnapshot =
            AccountSnapshot.canonical(hub, preOpcodeCallerSnapshot.address());
        preReEntryCalleeSnapshot =
            AccountSnapshot.canonical(hub, preOpcodeCalleeSnapshot.address());

        postReEntryCallerSnapshot =
            AccountSnapshot.canonical(hub, preOpcodeCallerSnapshot.address());
        postReEntryCalleeSnapshot =
            AccountSnapshot.canonical(hub, preOpcodeCalleeSnapshot.address());

        if (isSelfCall && callCanTransferValue) {
          postReEntryCallerSnapshot.decrementBalance(value);
          postReEntryCalleeSnapshot.decrementBalance(value);
        } else {
          preReEntryCallerSnapshot.decrementBalance(value);
          preReEntryCalleeSnapshot.incrementBalance(value);
        }

        AccountFragment postReEntryCallerAccountFragment =
            hub.factories()
                .accountFragment()
                .make(
                    postOpcodeCallerSnapshot,
                    postReEntryCallerSnapshot,
                    DomSubStampsSubFragment.revertWithCurrentDomSubStamps(
                        this.hubStamp(), frame.revertStamp(), 2));

        AccountFragment postReEntryCalleeAccountFragment =
            hub.factories()
                .accountFragment()
                .make(
                    postOpcodeCalleeSnapshot,
                    postReEntryCalleeSnapshot,
                    DomSubStampsSubFragment.revertWithCurrentDomSubStamps(
                        this.hubStamp(), frame.revertStamp(), 3));

        this.addFragmentsWithoutStack(
            postReEntryCallerAccountFragment, postReEntryCalleeAccountFragment);
      }
      default -> {}
    }
  }

  @Override
  public void resolveUponExitingContext(Hub hub, CallFrame frame) {}
}
