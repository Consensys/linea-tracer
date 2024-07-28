/*
 * Copyright Consensys Software Inc.
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
package net.consensys.linea.zktracer.module.hub.section.halt;

import static net.consensys.linea.zktracer.module.hub.fragment.scenario.ReturnScenarioFragment.ReturnScenario.*;
import static org.hyperledger.besu.evm.internal.Words.clampedToLong;

import com.google.common.base.Preconditions;
import lombok.Getter;
import net.consensys.linea.zktracer.module.hub.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.defer.PostRollbackDefer;
import net.consensys.linea.zktracer.module.hub.defer.PostTransactionDefer;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.DomSubStampsSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.MxpCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.mmu.MmuCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.oob.OobCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.oob.opcodes.XCallOobCall;
import net.consensys.linea.zktracer.module.hub.fragment.scenario.ReturnScenarioFragment;
import net.consensys.linea.zktracer.module.hub.section.TraceSection;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import net.consensys.linea.zktracer.types.Bytecode;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.worldstate.WorldView;

@Getter
public class ReturnSection extends TraceSection implements PostTransactionDefer, PostRollbackDefer {

  final int hubStamp;
  final CallFrame currentFrame;
  final short exceptions;
  final boolean returnFromDeployment;
  final boolean returnFromMessageCall; // not stricly speaking necessary
  final ReturnScenarioFragment returnScenarioFragment;
  final ImcFragment firstImcFragment;
  ImcFragment secondImcFragment;
  AccountFragment deploymentFragment;
  AccountFragment undoingDeploymentAccountFragment;
  MmuCall firstMmuCall;
  MmuCall secondMmuCall;

  ContextFragment squashParentContextReturnData;
  @Getter public boolean emptyDeployment;
  @Getter public boolean deploymentWasReverted = false;

  public ReturnSection(Hub hub) {
    // 5 = 1 + 4
    // 8 = 1 + 7
    super(hub, Exceptions.any(hub.pch().exceptions()) ? (short) 5 : (short) 8);
    hub.addTraceSection(this);

    hubStamp = hub.stamp();
    currentFrame = hub.currentFrame();
    exceptions = hub.pch().exceptions();
    returnFromMessageCall = hub.currentFrame().isMessageCall();
    returnFromDeployment = hub.currentFrame().isDeployment();
    firstImcFragment = ImcFragment.empty(hub);

    returnScenarioFragment = new ReturnScenarioFragment();
    final ContextFragment currentContextFragment = ContextFragment.readCurrentContextData(hub);
    final MxpCall mxpCall = new MxpCall(hub);
    firstImcFragment.callMxp(mxpCall);

    this.addStack(hub);
    this.addFragment(returnScenarioFragment);
    this.addFragment(currentContextFragment);
    this.addFragment(firstImcFragment);

    // Exceptional RETURN's
    ///////////////////////
    ///////////////////////

    if (Exceptions.any(exceptions)) {
      returnScenarioFragment.setScenario(RETURN_EXCEPTION);
    }

    // Memory expansion exception (MXPX)
    // Out of gas exception (OOGX)
    ////////////////////////////////////
    if (Exceptions.memoryExpansionException(exceptions)
        || Exceptions.outOfGasException(exceptions)) {
      // Note: the missing context fragment is added elsewhere
      return;
    }

    // Max code size exception (MAXCSX)
    ///////////////////////////////////
    boolean triggerOobForMaxCodeSizeException = Exceptions.codeSizeOverflow(exceptions);
    if (triggerOobForMaxCodeSizeException) {
      Preconditions.checkArgument(hub.currentFrame().isDeployment());
      OobCall oobCall = new XCallOobCall();
      firstImcFragment.callOob(oobCall);
      return;
    }

    // Invalid code prefix exception (ICPX)
    ///////////////////////////////////////
    final boolean triggerMmuForInvalidCodePrefix = Exceptions.invalidCodePrefix(exceptions);
    if (triggerMmuForInvalidCodePrefix) {
      Preconditions.checkArgument(hub.currentFrame().isDeployment());
      firstMmuCall = MmuCall.invalidCodePrefix(hub);
      hub.defers().scheduleForPostTransaction(this);
      return;
    }

    Preconditions.checkArgument(Exceptions.none(exceptions));

    // Unexceptional RETURN's
    // (we have exceptions ≡ ∅ by the checkArgument)
    ////////////////////////////////////////////////
    ////////////////////////////////////////////////

    final boolean nontrivialMmOperation = mxpCall.isMayTriggerNonTrivialMmuOperation();

    // RETURN_FROM_MESSAGE_CALL cases
    /////////////////////////////////
    /////////////////////////////////
    if (returnFromMessageCall) {
      final boolean messageCallReturnTouchesRam =
          !hub.currentFrame().isRoot()
              && nontrivialMmOperation // [size ≠ 0] ∧ ¬MXPX
              && !hub.currentFrame().parentReturnDataTarget().isEmpty(); // [r@c ≠ 0]

      returnScenarioFragment.setScenario(
          messageCallReturnTouchesRam
              ? RETURN_FROM_MESSAGE_CALL_WILL_TOUCH_RAM
              : RETURN_FROM_MESSAGE_CALL_WONT_TOUCH_RAM);

      if (messageCallReturnTouchesRam) {
        firstMmuCall = MmuCall.returnFromMessageCall(hub);
        Preconditions.checkArgument(!firstMmuCall.successBit());
        hub.defers().scheduleForPostTransaction(this);
      }
      // no need for the else case (and a nop) as per @François

      final ContextFragment updateCallerReturnData =
          ContextFragment.executionProvidesReturnData(
              hub,
              hub.callStack().getById(hub.currentFrame().parentFrameId()).contextNumber(),
              hub.currentFrame().contextNumber());
      this.addFragment(updateCallerReturnData);

      return;
    }

    // RETURN_FROM_DEPLOYMENT cases
    ///////////////////////////////
    ///////////////////////////////
    if (returnFromDeployment) {
      hub.defers().scheduleForPostRollback(this, hub.currentFrame());

      final long byteCodeSize = hub.messageFrame().getStackItem(1).toLong();
      final boolean emptyDeployment = byteCodeSize == 0;
      final boolean triggerOobForNonemptyDeployments = mxpCall.isMayTriggerNonTrivialMmuOperation();
      Preconditions.checkArgument(triggerOobForNonemptyDeployments == !emptyDeployment);

      final Address deploymentAddress = hub.messageFrame().getRecipientAddress();
      final AccountSnapshot accountBeforeDeployment =
          AccountSnapshot.canonical(hub, deploymentAddress);

      // Empty deployments
      ////////////////////
      if (emptyDeployment) {

        final AccountSnapshot accountAfterEmptyDeployment =
            accountBeforeDeployment.deployByteCode(Bytecode.EMPTY);
        final AccountFragment emptyDeploymentAccountFragment =
            hub.factories()
                .accountFragment()
                .make(
                    accountBeforeDeployment,
                    accountAfterEmptyDeployment,
                    DomSubStampsSubFragment.standardDomSubStamps(this.hubStamp(), 0));
        this.addFragment(emptyDeploymentAccountFragment);

        // Note:
        //  - triggerHASHINFO isn't required;
        //  - triggerROMLEX isn't required either;

        return;
      }

      // Nonempty deployments
      ///////////////////////
      hub.defers().scheduleForPostTransaction(this);

      firstMmuCall = MmuCall.invalidCodePrefix(hub);
      Preconditions.checkArgument(firstMmuCall.successBit());

      secondImcFragment = ImcFragment.empty(hub);
      secondMmuCall = MmuCall.returnFromDeployment(hub);
      this.addFragment(secondImcFragment);

      final long offset = clampedToLong(hub.messageFrame().getStackItem(0));
      final Bytecode deploymentCode =
          new Bytecode(hub.messageFrame().shadowReadMemory(offset, byteCodeSize));

      // TODO: we require the
      //  - triggerHashInfo stuff on the first stack row
      //  - triggerROMLEX on the deploymentAccountFragment row (done)
      final AccountFragment nonemptyDeploymentAccountFragment =
          hub.factories()
              .accountFragment()
              .make(
                  accountBeforeDeployment,
                  accountBeforeDeployment.deployByteCode(deploymentCode),
                  DomSubStampsSubFragment.standardDomSubStamps(this.hubStamp(), 0));
      nonemptyDeploymentAccountFragment.requiresRomlex(true);
      hub.romLex().callRomLex(hub.messageFrame());

      this.addFragment(nonemptyDeploymentAccountFragment);

      // TODO: we need to implement the mechanism that will append the
      //  context row which will squash the creator's return data after
      //  any and all account-rows.
      //
      // TODO: make sure this works if the current context is the root
      //  context (deployment transaction) in particular the following
      //  ``Either.left(callStack.parent().id())''
      squashParentContextReturnData = ContextFragment.executionProvidesEmptyReturnData(hub);
    }

    // returnFromDeployment =
  }

  @Override
  public void resolvePostRollback(Hub hub, MessageFrame messageFrame, CallFrame callFrame) {
    // TODO
    Preconditions.checkArgument(returnFromDeployment);
    returnScenarioFragment.setScenario(
        emptyDeployment
            ? RETURN_FROM_DEPLOYMENT_EMPTY_CODE_WILL_REVERT
            : RETURN_FROM_DEPLOYMENT_NONEMPTY_CODE_WILL_REVERT);

    // TODO: do we account for updates to
    //  - deploymentNumber and status ? Presumably, but if so by coincidence
    //  - MARKED_FOR_SELF_DESTRUCT(_NEW) ? No
    //  -
    undoingDeploymentAccountFragment =
        hub.factories()
            .accountFragment()
            .make(
                deploymentFragment.newState(),
                deploymentFragment.oldState(),
                DomSubStampsSubFragment.revertWithCurrentDomSubStamps(
                    hubStamp, hub.currentFrame().revertStamp(), 1));

    this.addFragment(undoingDeploymentAccountFragment);
    deploymentWasReverted = true;
  }

  @Override
  public void resolvePostTransaction(
      Hub hub, WorldView state, Transaction tx, boolean isSuccessful) {
    // TODO

    if (returnFromDeployment && !deploymentWasReverted) {
      returnScenarioFragment.setScenario(
          emptyDeployment
              ? RETURN_FROM_DEPLOYMENT_EMPTY_CODE_WONT_REVERT
              : RETURN_FROM_DEPLOYMENT_NONEMPTY_CODE_WONT_REVERT);
    }

    if (firstMmuCall != null) firstImcFragment.callMmu(firstMmuCall);
    if (secondMmuCall != null) secondImcFragment.callMmu(secondMmuCall);

    this.addFragment(squashParentContextReturnData);
  }
}
