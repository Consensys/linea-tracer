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

package net.consensys.linea.zktracer.module.hub.section.create;

import static net.consensys.linea.zktracer.module.hub.fragment.scenario.ScenarioEnum.CREATE_EMPTY_INIT_CODE_WONT_REVERT;
import static net.consensys.linea.zktracer.module.hub.fragment.scenario.ScenarioEnum.CREATE_FAILURE_CONDITION_WONT_REVERT;
import static net.consensys.linea.zktracer.types.AddressUtils.getDeploymentAddress;

import java.util.Optional;

import com.google.common.base.Preconditions;
import net.consensys.linea.zktracer.module.hub.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.defer.ChildContextEntryDefer;
import net.consensys.linea.zktracer.module.hub.defer.PostExecDefer;
import net.consensys.linea.zktracer.module.hub.defer.PostTransactionDefer;
import net.consensys.linea.zktracer.module.hub.defer.ReEnterContextDefer;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.RlpAddrSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.MxpCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.StpCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.mmu.MmuCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.oob.opcodes.CreateOobCall;
import net.consensys.linea.zktracer.module.hub.fragment.scenario.ScenarioEnum;
import net.consensys.linea.zktracer.module.hub.signals.AbortingConditions;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.account.Account;
import org.hyperledger.besu.evm.account.AccountState;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.operation.Operation;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class CreateSection
    implements PostExecDefer, ReEnterContextDefer, PostTransactionDefer, ChildContextEntryDefer {
  private FillCreateSection createSection;

  private int creatorContextId;

  // Just before create
  private AccountSnapshot preOpcodeCreatorSnapshot;
  private AccountSnapshot preOpcodeCreateeSnapshot;

  // Just after create but before entering child frame
  private AccountSnapshot midCreatorSnapshot;
  private AccountSnapshot midCreateeSnapshot;

  // After return from child-context
  private AccountSnapshot newCreatorSnapshot;
  private AccountSnapshot newCreateeSnapshot;

  private RlpAddrSubFragment rlpAddrSubFragment;

  /** true if the CREATE was successful */
  private boolean createSuccessful = false;

  // row i+2
  private final ImcFragment imcFragment;
  private MmuCall mmuCall;

  // row i+?
  private ContextFragment finalContextFragment;

  public CreateSection(Hub hub) {
    final short exceptions = hub.pch().exceptions();

    // row i + 1
    final ContextFragment currentContextFragment = ContextFragment.readCurrentContextData(hub);

    // row: i + 2
    // Note: in the static case this imc fragment remains empty

    // STATICX case
    imcFragment = ImcFragment.empty(hub);
    if (Exceptions.staticFault(exceptions)) {
      new ExceptionalCreate(hub, currentContextFragment, imcFragment);
      return;
    }

    final MxpCall mxpCall = new MxpCall(hub);
    imcFragment.callMxp(mxpCall);
    Preconditions.checkArgument(mxpCall.mxpx == Exceptions.memoryExpansionException(exceptions));

    // MXPX case
    if (Exceptions.memoryExpansionException(exceptions)) {
      new ExceptionalCreate(hub, currentContextFragment, imcFragment);
      return;
    }

    final StpCall stpCall = new StpCall(hub, mxpCall.getGasMxp());
    stpCall.stpCallForCreates(hub);
    imcFragment.callStp(stpCall);
    Preconditions.checkArgument(
        stpCall.outOfGasException() == Exceptions.outOfGasException(exceptions));

    // OOGX case
    if (Exceptions.outOfGasException(exceptions)) {
      new ExceptionalCreate(hub, currentContextFragment, imcFragment);
      return;
    }

    Preconditions.checkArgument(Exceptions.none(exceptions));

    final CreateOobCall oobCall = new CreateOobCall();
    imcFragment.callOob(oobCall);

    // The CREATE(2) is now unexceptional
    final AbortingConditions aborts = hub.pch().abortingConditions().snapshot();
    Preconditions.checkArgument(oobCall.isAbortingCondition() == aborts.any());

    final CallFrame callFrame = hub.currentFrame();
    final MessageFrame frame = callFrame.frame();

    final Address creatorAddress = callFrame.accountAddress();
    preOpcodeCreatorSnapshot = AccountSnapshot.canonical(hub, creatorAddress);

    if (aborts.any()) {
      new AbortCreate(hub, currentContextFragment, imcFragment, preOpcodeCreatorSnapshot);
      return;
    }

    // The CREATE(2) is now unexceptional and unaborted
    hub.defers().schedulePostExecution(this);
    hub.defers().scheduleForContextReEntry(this, hub.currentFrame());
    hub.defers().schedulePostTransaction(this);

    // Note: all future account rows will be added during the resolvePostTransaction

    this.creatorContextId = hub.currentFrame().id();

    final Address createeAddress = getDeploymentAddress(frame);
    preOpcodeCreateeSnapshot = AccountSnapshot.canonical(hub, createeAddress);

    rlpAddrSubFragment = RlpAddrSubFragment.makeFragment(hub, createeAddress);

    final Optional<Account> deploymentAccount =
        Optional.ofNullable(frame.getWorldUpdater().get(createeAddress));
    final boolean createdAddressHasNonZeroNonce =
        deploymentAccount.map(a -> a.getNonce() != 0).orElse(false);
    final boolean createdAddressHasNonEmptyCode =
        deploymentAccount.map(AccountState::hasCode).orElse(false);

    final boolean failureCondition = createdAddressHasNonZeroNonce || createdAddressHasNonEmptyCode;
    final boolean emptyInitCode = hub.transients().op().initCodeSegment().isEmpty();

    if (failureCondition || emptyInitCode) {
      final ScenarioEnum scenario =
          failureCondition
              ? CREATE_FAILURE_CONDITION_WONT_REVERT
              : CREATE_EMPTY_INIT_CODE_WONT_REVERT;
      this.createSection =
          new FailureOrEmptyInitCreate(hub, scenario, currentContextFragment, imcFragment);
      this.finalContextFragment = ContextFragment.nonExecutionProvidesEmptyReturnData(hub);

      if (hub.opCode() == OpCode.CREATE2 && !emptyInitCode) {
        this.mmuCall = MmuCall.create2(hub, failureCondition);
      }
      return;
    }

    // Finally, non-exceptional, non-aborting, non-failing, non-emptyInitCode create
    hub.defers().scheduleForImmediateContextEntry(this);
    this.createSection = new NonEmptyInitCodeCreate(hub, currentContextFragment, imcFragment);
    this.mmuCall =
        hub.opCode() == OpCode.CREATE2
            ? MmuCall.create2(hub, failureCondition)
            : MmuCall.create(hub);

    this.finalContextFragment = ContextFragment.initializeNewExecutionContext(hub);
  }

  @Override
  public void resolvePostExecution(
      Hub hub, MessageFrame frame, Operation.OperationResult operationResult) {
    final Address creatorAddress = preOpcodeCreatorSnapshot.address();
    this.midCreatorSnapshot =
        AccountSnapshot.fromAccount(
            frame.getWorldUpdater().get(creatorAddress),
            true,
            hub.transients().conflation().deploymentInfo().number(creatorAddress),
            hub.transients().conflation().deploymentInfo().isDeploying(creatorAddress));

    final Address createdAddress = preOpcodeCreateeSnapshot.address();
    this.midCreateeSnapshot =
        AccountSnapshot.fromAccount(
            frame.getWorldUpdater().get(createdAddress),
            true,
            hub.transients().conflation().deploymentInfo().number(createdAddress),
            hub.transients().conflation().deploymentInfo().isDeploying(createdAddress));

    // Pre-emptively set new* snapshots in case we never enter the child frame.
    // Will be overwritten if we enter the child frame and runNextContext is explicitly called by
    // the defer registry.
    // this.resolveAtContextReEntry(hub, frame);
  }

  // TODO: @François: there was something around triggering the ROM_LEX module at the right time
  @Override
  public void resolvePostTransaction(
      Hub hub, WorldView state, Transaction tx, boolean isSuccessful) {

    if (this.mmuCall != null) {
      this.imcFragment.callMmu(this.mmuCall);
    }

    final CallFrame createCallFrame = hub.callStack().getById(this.creatorContextId);
    final int childRevertStamp =
        createCallFrame.childFrames().isEmpty()
            ? 0
            : hub.callStack()
                .getById(createCallFrame.childFrames().getFirst())
                .revertStamp(); // TODO: not sure about this
    this.createSection.fillAccountFragment(
        hub,
        createSuccessful,
        childRevertStamp,
        rlpAddrSubFragment,
        preOpcodeCreatorSnapshot,
        midCreatorSnapshot,
        newCreatorSnapshot,
        preOpcodeCreateeSnapshot,
        midCreateeSnapshot,
        newCreateeSnapshot);

    if (createCallFrame.hasReverted()) {
      final int currentRevertStamp = createCallFrame.revertStamp();
      this.createSection.fillReverting(
          hub,
          createSuccessful,
          currentRevertStamp,
          preOpcodeCreatorSnapshot,
          midCreatorSnapshot,
          newCreatorSnapshot,
          preOpcodeCreateeSnapshot,
          midCreateeSnapshot,
          newCreateeSnapshot);
    }

    this.createSection.fillContextFragment(this.finalContextFragment);
  }

  // TODO: ensure with @Daniel.Lehrner that at contextEntry the accounts
  //  of both creator and createe were updated (and potentially created)
  //  otherwise do it later
  @Override
  public void resolveUponEnteringChildContext(Hub hub, MessageFrame frame) {}

  @Override
  public void resolveAtContextReEntry(Hub hub, CallFrame callFrame) {

    final MessageFrame frame = callFrame.frame();

    this.createSuccessful = !frame.getStackItem(0).isZero(); // TODO: are we sure it's working ??

    final Address creatorAddress = preOpcodeCreatorSnapshot.address();
    this.newCreatorSnapshot = AccountSnapshot.canonical(hub, creatorAddress);

    final Address createeAddress = preOpcodeCreateeSnapshot.address();
    this.newCreateeSnapshot = AccountSnapshot.canonical(hub, createeAddress);
  }
}
