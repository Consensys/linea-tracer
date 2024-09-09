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

package net.consensys.linea.zktracer.module.hub.section.copy;

import static com.google.common.base.Preconditions.*;
import static net.consensys.linea.zktracer.module.hub.signals.Exceptions.MEMORY_EXPANSION_EXCEPTION;
import static net.consensys.linea.zktracer.module.hub.signals.Exceptions.OUT_OF_GAS_EXCEPTION;
import static net.consensys.linea.zktracer.module.hub.signals.Exceptions.outOfGasException;
import static net.consensys.linea.zktracer.types.AddressUtils.isAddressWarm;

import net.consensys.linea.zktracer.module.hub.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.defer.PostRollbackDefer;
import net.consensys.linea.zktracer.module.hub.fragment.DomSubStampsSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.MxpCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.mmu.MmuCall;
import net.consensys.linea.zktracer.module.hub.section.TraceSection;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.account.Account;
import org.hyperledger.besu.evm.frame.MessageFrame;

// TODO: implement
public class ExtCodeCopySection extends TraceSection implements PostRollbackDefer {

  final Bytes rawAddress;
  final Address address;
  final int incomingDeploymentNumber;
  final boolean incomingDeploymentStatus;
  final boolean incomingWarmth;

  AccountSnapshot accountBefore;
  AccountSnapshot accountAfter;

  public ExtCodeCopySection(Hub hub) {
    // 4 = 1 + 3
    super(hub, maxNumberOfRows(hub));

    final MessageFrame frame = hub.messageFrame();
    rawAddress = frame.getStackItem(0);
    address = Address.extract(Bytes32.leftPad(rawAddress));
    incomingDeploymentNumber = hub.deploymentNumberOf(address);
    incomingDeploymentStatus = hub.deploymentStatusOf(address);
    incomingWarmth = isAddressWarm(frame, address);
    final ImcFragment imcFragment = ImcFragment.empty(hub);

    this.addStack(hub);
    this.addFragment(imcFragment);

    // triggerExp = false
    // triggerOob = false
    // triggerStp = false
    // triggerMxp = true
    final MxpCall mxpCall = new MxpCall(hub);
    imcFragment.callMxp(mxpCall);

    final short exceptions = hub.pch().exceptions();
    checkArgument(mxpCall.mxpx == Exceptions.memoryExpansionException(exceptions));

    // The MXPX case
    if (mxpCall.mxpx) {
      commonValues.setTracedException(MEMORY_EXPANSION_EXCEPTION);
      return;
    }

    final Account foreignAccount = frame.getWorldUpdater().get(address);

    accountBefore =
        foreignAccount != null
            ? AccountSnapshot.canonical(hub, address)
            : AccountSnapshot.fromAddress(
                address, incomingWarmth, incomingDeploymentNumber, incomingDeploymentStatus);

    final DomSubStampsSubFragment doingDomSubStamps =
        DomSubStampsSubFragment.standardDomSubStamps(this.hubStamp(), 0);

    // The OOGX case
    if (outOfGasException(exceptions)) {
      // the last context row will be added automatically
      final AccountFragment accountReadingFragment =
          hub.factories()
              .accountFragment()
              .makeWithTrm(accountBefore, accountBefore, rawAddress, doingDomSubStamps);

      this.addFragment(accountReadingFragment);
      commonValues.setTracedException(OUT_OF_GAS_EXCEPTION);
      return;
    }

    // The unexceptional case
    checkArgument(Exceptions.none(exceptions));

    final boolean triggerMmu = mxpCall.mayTriggerNontrivialMmuOperation;
    if (triggerMmu) {
      final MmuCall mmuCall = MmuCall.extCodeCopy(hub);
      imcFragment.callMmu(mmuCall);
    }

    // TODO: make sure that hasCode returns false during deployments
    //  in particular: write tests for that scenario
    final boolean foreignAccountHasCode = foreignAccount != null && foreignAccount.hasCode();
    final boolean triggerCfi = triggerMmu && foreignAccountHasCode;

    accountAfter = accountBefore.deepCopy();
    accountAfter.turnOnWarmth();

    final AccountFragment accountDoingFragment =
        hub.factories()
            .accountFragment()
            .makeWithTrm(accountBefore, accountAfter, rawAddress, doingDomSubStamps);
    accountDoingFragment.requiresRomlex(triggerCfi);
    if (triggerCfi) {
      hub.romLex().callRomLex(hub.messageFrame());
    }
    this.addFragment(accountDoingFragment);

    // an EXTCODECOPY section is only scheduled
    // for rollback if it is unexceptional
    hub.defers().scheduleForPostRollback(this, hub.currentFrame());
  }

  @Override
  public void resolvePostRollback(Hub hub, MessageFrame messageFrame, CallFrame callFrame) {

    final AccountSnapshot accountPostRollback = AccountSnapshot.canonical(hub, address);

    final DomSubStampsSubFragment undoingDomSubStamps =
        DomSubStampsSubFragment.revertWithCurrentDomSubStamps(
            this.hubStamp(), callFrame.revertStamp(), 1);

    final AccountFragment undoingAccountFragment =
        hub.factories()
            .accountFragment()
            .make(accountAfter, accountPostRollback, undoingDomSubStamps);

    this.addFragment(undoingAccountFragment);
  }

  private static short maxNumberOfRows(Hub hub) {
    return (short) (hub.opCode().numberOfStackRows() + 3);
  }
}
