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
import static net.consensys.linea.zktracer.types.AddressUtils.getCreateAddress;

import net.consensys.linea.zktracer.module.hub.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.defer.PostExecDefer;
import net.consensys.linea.zktracer.module.hub.defer.PostTransactionDefer;
import net.consensys.linea.zktracer.module.hub.defer.ReEnterContextDefer;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.RlpAddrSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.MxpCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.StpCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.mmu.MmuCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.oob.OobCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.oob.opcodes.CreateOobCall;
import net.consensys.linea.zktracer.module.hub.fragment.scenario.ScenarioEnum;
import net.consensys.linea.zktracer.module.hub.signals.AbortingConditions;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.account.Account;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.operation.Operation;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class CreateSection implements PostExecDefer, ReEnterContextDefer, PostTransactionDefer {
  private FillCreateSection createSection;
  private boolean failure;

  private int creatorContextId;
  private boolean emptyInitCode;

  // Just before create
  private AccountSnapshot oldCreatorSnapshot;
  private AccountSnapshot oldCreatedSnapshot;

  // Just after create but before entering child frame
  private AccountSnapshot midCreatorSnapshot;
  private AccountSnapshot midCreatedSnapshot;

  // After return from child-context
  private AccountSnapshot newCreatorSnapshot;
  private AccountSnapshot newCreatedSnapshot;

  private RlpAddrSubFragment rlpAddrSubFragment;

  /* true if the CREATE was successful **/
  private boolean createSuccessful = false;

  /* true if the putatively created account already has code **/
  private boolean targetHasCode() {
    return !oldCreatedSnapshot.code().isEmpty();
  }

  // row i+2
  private final ImcFragment imcFragment;
  private MmuCall mmuCall;

  // row i+ ?
  private ContextFragment lastContextFragment;

  public CreateSection(Hub hub) {
    final short exception = hub.pch().exceptions();

    // row i +1
    final ContextFragment commonContext = ContextFragment.readCurrentContextData(hub);

    imcFragment = ImcFragment.empty(hub);

    if (Exceptions.staticFault(exception)) {
      new ExceptionalCreate(hub, commonContext, imcFragment);
      return;
    }

    final MxpCall mxpCall = new MxpCall(hub);
    imcFragment.callMxp(mxpCall);

    if (Exceptions.memoryExpansionException(exception)) {
      new ExceptionalCreate(hub, commonContext, imcFragment);
      return;
    }

    final StpCall stpCall = new StpCall(hub, mxpCall.getGasMxp());
    stpCall.stpCallForCreates(hub);
    imcFragment.callStp(stpCall);

    if (Exceptions.outOfGasException(exception)) {
      new ExceptionalCreate(hub, commonContext, imcFragment);
      return;
    }

    final OobCall oobCall = new CreateOobCall();
    imcFragment.callOob(oobCall);

    // We are now with unexceptional create
    final AbortingConditions aborts = hub.pch().abortingConditions().snapshot();

    final CallFrame callFrame = hub.currentFrame();
    final MessageFrame frame = callFrame.frame();

    final Address creatorAddress = callFrame.accountAddress();
    final Account creatorAccount = frame.getWorldUpdater().get(creatorAddress);
    oldCreatorSnapshot =
        AccountSnapshot.fromAccount(
            creatorAccount,
            frame.isAddressWarm(creatorAddress),
            hub.transients().conflation().deploymentInfo().number(creatorAddress),
            hub.transients().conflation().deploymentInfo().isDeploying(creatorAddress));

    if (aborts.any()) {
      new AbortCreate(hub, commonContext, imcFragment, oldCreatorSnapshot);
      return;
    }

    // We are now with unexceptional, non-aborting create
    hub.defers().schedulePostExecution(this);
    hub.defers().reEntry(this);
    hub.defers().schedulePostTransaction(this);

    this.creatorContextId = hub.currentFrame().id();

    final Address createdAddress = getCreateAddress(frame);
    final Account createdAccount = frame.getWorldUpdater().get(createdAddress);
    oldCreatedSnapshot =
        AccountSnapshot.fromAccount(
            createdAccount,
            frame.isAddressWarm(createdAddress),
            hub.transients().conflation().deploymentInfo().number(createdAddress),
            hub.transients().conflation().deploymentInfo().isDeploying(createdAddress));

    rlpAddrSubFragment = RlpAddrSubFragment.makeFragment(hub, createdAddress);

    failure = hub.pch().failureConditions().any();
    emptyInitCode = hub.transients().op().callDataSegment().isEmpty();

    if (failure || emptyInitCode) {
      final ScenarioEnum scenario =
          failure ? CREATE_FAILURE_CONDITION_WONT_REVERT : CREATE_EMPTY_INIT_CODE_WONT_REVERT;
      this.createSection = new FailureOrEmptyInitCreate(hub, scenario, commonContext, imcFragment);
      this.lastContextFragment = ContextFragment.nonExecutionEmptyReturnData(hub);
      if (hub.opCode() == OpCode.CREATE2 && !emptyInitCode) {
        this.mmuCall = MmuCall.create2(hub);
      }
      return;
    }

    // Finally, non-exceptional, non-aborting, non-failing, non-emptyInitCode create
    this.createSection = new NonEmptyInitCodeCreate(hub, commonContext, imcFragment);
  }

  @Override
  public void resolvePostExecution(
      Hub hub, MessageFrame frame, Operation.OperationResult operationResult) {
    final Address creatorAddress = oldCreatorSnapshot.address();
    this.midCreatorSnapshot =
        AccountSnapshot.fromAccount(
            frame.getWorldUpdater().get(creatorAddress),
            true,
            hub.transients().conflation().deploymentInfo().number(creatorAddress),
            hub.transients().conflation().deploymentInfo().isDeploying(creatorAddress));

    final Address createdAddress = oldCreatedSnapshot.address();
    this.midCreatedSnapshot =
        AccountSnapshot.fromAccount(
            frame.getWorldUpdater().get(createdAddress),
            true,
            hub.transients().conflation().deploymentInfo().number(createdAddress),
            hub.transients().conflation().deploymentInfo().isDeploying(createdAddress));

    // Pre-emptively set new* snapshots in case we never enter the child frame.
    // Will be overwritten if we enter the child frame and runNextContext is explicitly called by
    // the defer registry.
    this.resolveAtContextReEntry(hub, frame);
  }

  @Override
  public void resolveAtContextReEntry(Hub hub, MessageFrame frame) {
    this.createSuccessful = !frame.getStackItem(0).isZero(); // TODO: are we sure it's working ??

    final Address creatorAddress = oldCreatorSnapshot.address();
    this.newCreatorSnapshot =
        AccountSnapshot.fromAccount(
            frame.getWorldUpdater().get(creatorAddress),
            true,
            hub.transients().conflation().deploymentInfo().number(creatorAddress),
            hub.transients().conflation().deploymentInfo().isDeploying(creatorAddress));

    final Address createdAddress = oldCreatedSnapshot.address();
    this.newCreatedSnapshot =
        AccountSnapshot.fromAccount(
            frame.getWorldUpdater().get(createdAddress),
            true,
            hub.transients().conflation().deploymentInfo().number(createdAddress),
            hub.transients().conflation().deploymentInfo().isDeploying(createdAddress));
  }

  @Override
  public void resolvePostTransaction(
      Hub hub, WorldView state, Transaction tx, boolean isSuccessful) {

    if (this.mmuCall != null) {
      this.imcFragment.callMmu(this.mmuCall);
    }

    this.createSection.fillAccountFragment(
        hub,
        rlpAddrSubFragment,
        oldCreatorSnapshot,
        midCreatorSnapshot,
        newCreatorSnapshot,
        oldCreatedSnapshot,
        midCreatedSnapshot,
        newCreatedSnapshot);

    final CallFrame createCallFrame = hub.callStack().getById(this.creatorContextId);
    if (createCallFrame.hasReverted()) {
      final int childRevertStamp =
          createCallFrame.childFrames().isEmpty()
              ? 0
              : hub.callStack()
                  .getById(createCallFrame.childFrames().getFirst())
                  .revertStamp(); // TODO: not sure about this
      final int currentRevertStamp = createCallFrame.revertStamp();
      this.createSection.fillReverting(
          hub,
          childRevertStamp,
          currentRevertStamp,
          oldCreatorSnapshot,
          midCreatorSnapshot,
          newCreatorSnapshot,
          oldCreatedSnapshot,
          midCreatedSnapshot,
          newCreatedSnapshot);
    }

    this.createSection.fillContextFragment(this.lastContextFragment);
  }
}
