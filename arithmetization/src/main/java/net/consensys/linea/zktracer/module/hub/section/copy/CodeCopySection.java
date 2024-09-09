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
import static net.consensys.linea.zktracer.module.hub.signals.Exceptions.OUT_OF_GAS_EXCEPTION;

import net.consensys.linea.zktracer.module.hub.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.DomSubStampsSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.MxpCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.mmu.MmuCall;
import net.consensys.linea.zktracer.module.hub.section.TraceSection;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import net.consensys.linea.zktracer.module.hub.signals.PureException;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.account.Account;
import org.hyperledger.besu.evm.frame.MessageFrame;

public class CodeCopySection extends TraceSection {

  public CodeCopySection(Hub hub) {
    super(hub, maxNumberOfRows(hub));

    // Miscellaneous row
    final ImcFragment imcFragment = ImcFragment.empty(hub);
    this.addStack(hub);
    this.addFragment(imcFragment);

    // triggerOob = false
    // triggerMxp = true
    final MxpCall mxpCall = new MxpCall(hub);
    imcFragment.callMxp(mxpCall);

    final short exceptions = hub.pch().exceptions();
    checkArgument(mxpCall.mxpx == Exceptions.memoryExpansionException(exceptions));

    // The MXPX case
    if (mxpCall.mxpx) {
      commonValues.setTracedException(PureException.MEMORY_EXPANSION_EXCEPTION);
      return;
    }

    // The OOGX case
    if (Exceptions.any(exceptions)) {
      checkArgument(exceptions == OUT_OF_GAS_EXCEPTION);
      commonValues.setTracedException(PureException.OUT_OF_GAS_EXCEPTION);
      return;
    }

    // the unexceptional case
    final ContextFragment contextFragment = ContextFragment.readCurrentContextData(hub);
    this.addFragment(contextFragment);

    // Account row
    final MessageFrame frame = hub.messageFrame();
    final Address codeAddress = frame.getContractAddress();
    final Account codeAccount = frame.getWorldUpdater().get(codeAddress);

    final boolean warmth = frame.isAddressWarm(codeAddress);
    checkArgument(warmth);

    final AccountSnapshot codeAccountSnapshot =
        AccountSnapshot.fromAccount(
            codeAccount,
            warmth,
            hub.deploymentNumberOf(codeAddress),
            hub.deploymentStatusOf(codeAddress));

    final DomSubStampsSubFragment doingDomSubStamps =
        DomSubStampsSubFragment.standardDomSubStamps(this.hubStamp(), 0); // Specifics for CODECOPY

    final AccountFragment accountReadingFragment =
        hub.factories()
            .accountFragment()
            .make(codeAccountSnapshot, codeAccountSnapshot, doingDomSubStamps);

    accountReadingFragment.requiresRomlex(true);

    this.addFragment(accountReadingFragment);

    final boolean triggerMmu = mxpCall.mayTriggerNontrivialMmuOperation;
    if (triggerMmu) {
      MmuCall mmuCall = MmuCall.codeCopy(hub);
      imcFragment.callMmu(mmuCall);
    }
  }

  // 4 = 1 (stack row) + 3 (up to 3 non-stack rows)
  private static short maxNumberOfRows(Hub hub) {
    return (short) (hub.opCode().numberOfStackRows() + 3);
  }
}
