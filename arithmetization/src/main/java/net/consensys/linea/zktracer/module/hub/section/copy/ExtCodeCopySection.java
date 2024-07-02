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

import static net.consensys.linea.zktracer.module.hub.signals.Exceptions.none;
import static net.consensys.linea.zktracer.module.hub.signals.Exceptions.outOfGasException;

import net.consensys.linea.zktracer.module.hub.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.DomSubStampsSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.MxpCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.mmu.MmuCall;
import net.consensys.linea.zktracer.module.hub.section.TraceSection;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.account.Account;
import org.hyperledger.besu.evm.frame.MessageFrame;

public class ExtCodeCopySection extends TraceSection {

  public static void appendToTrace(Hub hub) {

    final ExtCodeCopySection extCodeCopySection = new ExtCodeCopySection(hub);
    hub.addTraceSection(extCodeCopySection);

    ImcFragment imcFragment = ImcFragment.empty(hub);
    extCodeCopySection.addFragmentsAndStack(hub, imcFragment);

    // triggerOob = false
    // triggerMxp = true
    MxpCall mxpCall = new MxpCall(hub);
    imcFragment.callMxp(mxpCall);

    // The MXPX case
    if (mxpCall.mxpx) {
      // the last context row will be added automatically
      return;
    }

    final MessageFrame frame = hub.messageFrame();
    final Bytes rawTargetAddress = frame.getStackItem(0);
    final Address targetAddress = Address.extract((Bytes32) rawTargetAddress);
    final Account targetAccount = frame.getWorldUpdater().get(targetAddress);

    AccountSnapshot accountBefore =
        AccountSnapshot.fromAccount(
            targetAccount,
            frame.isAddressWarm(targetAddress),
            hub.transients().conflation().deploymentInfo().number(targetAddress),
            hub.transients().conflation().deploymentInfo().isDeploying(targetAddress));

    DomSubStampsSubFragment doingDomSubStamps =
        DomSubStampsSubFragment.standardDomSubStamps(hub, 0);

    final short exceptions = hub.pch().exceptions();

    // The OOGX case
    if (outOfGasException(exceptions)) {
      // the last context row will be added automatically
      AccountFragment accountReadingFragment =
          hub.factories()
              .accountFragment()
              .makeWithTrm(accountBefore, accountBefore, rawTargetAddress, doingDomSubStamps);

      extCodeCopySection.addFragment(accountReadingFragment);
      return;
    }

    final boolean triggerMmu = none(exceptions) && mxpCall.isMayTriggerNonTrivialMmuOperation();
    if (triggerMmu) {
      MmuCall mmuCall = MmuCall.extCodeCopy(hub);
      imcFragment.callMmu(mmuCall);
    }

    AccountSnapshot accountAfter =
        AccountSnapshot.fromAccount(
            targetAccount,
            true,
            hub.transients().conflation().deploymentInfo().number(targetAddress),
            hub.transients().conflation().deploymentInfo().isDeploying(targetAddress));

    AccountFragment accountDoingFragment =
        hub.factories()
            .accountFragment()
            .makeWithTrm(accountBefore, accountAfter, rawTargetAddress, doingDomSubStamps);

    extCodeCopySection.addFragment(accountDoingFragment);

    // affected by issue #785
    if (hub.callStack().current().willRevert()) {
      DomSubStampsSubFragment undoingDomSubStamps =
          DomSubStampsSubFragment.revertWithCurrentDomSubStamps(hub, 1);
      AccountFragment undoingAccountFragment =
          hub.factories().accountFragment().make(accountAfter, accountBefore, undoingDomSubStamps);
      extCodeCopySection.addFragment(undoingAccountFragment);
    }
  }

  public ExtCodeCopySection(Hub hub) {
    super(hub);
  }
}
