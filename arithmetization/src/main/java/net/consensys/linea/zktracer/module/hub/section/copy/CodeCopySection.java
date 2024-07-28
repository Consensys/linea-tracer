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

import com.google.common.base.Preconditions;
import net.consensys.linea.zktracer.module.hub.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.defer.PostTransactionDefer;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.DomSubStampsSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.MxpCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.mmu.MmuCall;
import net.consensys.linea.zktracer.module.hub.section.TraceSection;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.account.Account;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class CodeCopySection extends TraceSection implements PostTransactionDefer {
  final ImcFragment imcFragment;
  boolean triggerMmu;
  final short exceptions;
  MmuCall mmuCall;

  public CodeCopySection(Hub hub) {
    // 4 = 1 (stack row) + 3 (up to 3 non-stack rows)
    super(hub, (short) 4);
    this.exceptions = hub.pch().exceptions();
    hub.addTraceSection(this);

    // Miscellaneous row
    imcFragment = ImcFragment.empty(hub);
    this.addStackAndFragments(hub, imcFragment);

    // triggerOob = false
    // triggerMxp = true
    final MxpCall mxpCall = new MxpCall(hub);
    imcFragment.callMxp(mxpCall);
    final boolean mxpx = mxpCall.mxpx;
    Preconditions.checkArgument(mxpx == Exceptions.memoryExpansionException(exceptions));

    // The MXPX case
    ////////////////
    if (mxpx) {
      return;
    }

    final boolean xahoy = Exceptions.any(exceptions);
    Preconditions.checkArgument(xahoy == (exceptions == Exceptions.OUT_OF_GAS_EXCEPTION));

    // The OOGX case
    ////////////////
    if (Exceptions.any(exceptions)) {
      return;
    }

    // Beyond this point we are in the xahoy = false case

    // Context row
    final ContextFragment contextFragment = ContextFragment.readCurrentContextData(hub);
    this.addFragment(contextFragment);

    // Account row
    final MessageFrame frame = hub.messageFrame();
    final Address codeAddress = frame.getContractAddress();
    final Account codeAccount = frame.getWorldUpdater().get(codeAddress);

    final boolean warmth = frame.isAddressWarm(codeAddress);
    Preconditions.checkArgument(warmth);

    final AccountSnapshot codeAccountSnapshot =
        AccountSnapshot.fromAccount(
            codeAccount,
            warmth,
            hub.transients().conflation().deploymentInfo().number(codeAddress),
            hub.transients().conflation().deploymentInfo().isDeploying(codeAddress));

    final DomSubStampsSubFragment doingDomSubStamps =
        DomSubStampsSubFragment.standardDomSubStamps(this.hubStamp(), 0); // Specifics for CODECOPY

    final AccountFragment accountReadingFragment =
        hub.factories()
            .accountFragment()
            .make(codeAccountSnapshot, codeAccountSnapshot, doingDomSubStamps);

    accountReadingFragment.requiresRomlex(true);

    this.addFragment(accountReadingFragment);

    triggerMmu = mxpCall.isMayTriggerNonTrivialMmuOperation();
    if (triggerMmu) {
      mmuCall = MmuCall.codeCopy(hub);
      hub.defers().scheduleForPostTransaction(this);
    }
  }

  @Override
  public void resolvePostTransaction(
      Hub hub, WorldView state, Transaction tx, boolean isSuccessful) {
    if (triggerMmu) {
      imcFragment.callMmu(mmuCall);
    }
  }
}
