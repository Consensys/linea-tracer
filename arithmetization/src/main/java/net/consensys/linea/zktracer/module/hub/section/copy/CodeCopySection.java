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

import static net.consensys.linea.zktracer.module.hub.signals.Exceptions.outOfGasException;

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
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.account.Account;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class CodeCopySection implements PostTransactionDefer {
  CodeCopyCommonSection sectionPrequel;
  ImcFragment imcFragment;
  boolean triggerMmu;

  public CodeCopySection(Hub hub) {
    this.sectionPrequel = new CodeCopyCommonSection(hub);
    hub.addTraceSection(sectionPrequel);

    // Miscellaneous row
    imcFragment = ImcFragment.empty(hub);
    this.sectionPrequel.addFragmentsAndStack(hub, imcFragment);

    // triggerOob = false
    // triggerMxp = true
    MxpCall mxpCall = new MxpCall(hub);
    imcFragment.callMxp(mxpCall);
    boolean xahoy = mxpCall.mxpx || outOfGasException(hub.pch().exceptions());

    // Context row
    // TODO: use ContextFragment.readContextDataByContextNumber(hub, CN)
    ContextFragment contextFragment =
        xahoy
            ? ContextFragment.executionProvidesEmptyReturnData(hub)
            : ContextFragment.readCurrentContextData(hub);
    this.sectionPrequel.addFragment(contextFragment);

    // Account row
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

    if (mxpCall.mxpx) {
      AccountFragment accountReadingFragment =
          hub.factories()
              .accountFragment()
              .makeWithTrm(accountBefore, accountBefore, rawTargetAddress, doingDomSubStamps);

      this.sectionPrequel.addFragment(accountReadingFragment);
      return;
    }
    if (!xahoy) {
      // TODO: ?
    }

    triggerMmu = !xahoy && mxpCall.isMayTriggerNonTrivialMmuOperation();
    hub.defers().postTx(this);
  }

  @Override
  public void runPostTx(Hub hub, WorldView state, Transaction tx, boolean isSuccessful) {
    if (triggerMmu) {
      MmuCall mmuCall = MmuCall.codeCopy(hub);
      imcFragment.callMmu(mmuCall);
    }
  }

  public static class CodeCopyCommonSection extends TraceSection {
    public CodeCopyCommonSection(Hub hub) {
      super(hub);
      // TODO: do we need something else here?
    }
  }
}
