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

import com.google.common.base.Preconditions;
import net.consensys.linea.zktracer.module.hub.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.defer.PostRollbackDefer;
import net.consensys.linea.zktracer.module.hub.defer.PostTransactionDefer;
import net.consensys.linea.zktracer.module.hub.fragment.DomSubStampsSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.MxpCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.mmu.MmuCall;
import net.consensys.linea.zktracer.module.hub.section.TraceSection;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.account.Account;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class ExtCodeCopySection extends TraceSection
    implements PostRollbackDefer, PostTransactionDefer {

  final int hubStamp;
  final Bytes rawAddress;
  final Address address;
  final int incomingDeploymentNumber;
  final boolean incomingDeploymentStatus;
  final boolean incomingWarmth;
  final short exceptions;

  AccountSnapshot accountBefore;
  AccountSnapshot accountAfter;

  final ImcFragment miscFragment;
  boolean triggerMmu = false;
  MmuCall mmuCall;

  public ExtCodeCopySection(Hub hub) {
    // 4 = 1 + 3
    super(hub, (short) 4);
    hub.addTraceSection(this);

    final MessageFrame frame = hub.messageFrame();
    hubStamp = hub.stamp();
    rawAddress = frame.getStackItem(0);
    address = Address.extract((Bytes32) rawAddress);
    incomingDeploymentNumber = hub.transients().conflation().deploymentInfo().number(address);
    incomingDeploymentStatus = hub.transients().conflation().deploymentInfo().isDeploying(address);
    incomingWarmth = frame.isAddressWarm(address);
    exceptions = hub.pch().exceptions();
    miscFragment = ImcFragment.empty(hub);

    this.addStack(hub);
    this.addFragment(miscFragment);

    // triggerExp = false
    // triggerOob = false
    // triggerStp = false
    // triggerMxp = true
    final MxpCall mxpCall = new MxpCall(hub);
    miscFragment.callMxp(mxpCall);

    Preconditions.checkArgument(
        mxpCall.mxpx == Exceptions.memoryExpansionException(this.exceptions));

    // The MXPX case
    ////////////////
    if (mxpCall.mxpx) {
      return;
    }

    final Account foreignAccount = frame.getWorldUpdater().get(this.address);

    this.accountBefore =
        AccountSnapshot.fromAccount(
            foreignAccount, incomingWarmth, incomingDeploymentNumber, incomingDeploymentStatus);

    final DomSubStampsSubFragment doingDomSubStamps =
        DomSubStampsSubFragment.standardDomSubStamps(this.hubStamp(), 0);

    // The OOGX case
    ////////////////
    if (outOfGasException(exceptions)) {
      // the last context row will be added automatically
      final AccountFragment accountReadingFragment =
          hub.factories()
              .accountFragment()
              .makeWithTrm(accountBefore, accountBefore, rawAddress, doingDomSubStamps);

      this.addFragment(accountReadingFragment);
      return;
    }

    // The unexceptional case
    /////////////////////////
    triggerMmu = none(exceptions) && mxpCall.mayTriggerNonTrivialMmuOperation;
    if (triggerMmu) {
      mmuCall = MmuCall.extCodeCopy(hub);
      hub.defers().schedulePostTransaction(this);
    }

    // TODO: make sure that hasCode returns false during deployments
    //  in particular: write tests for that scenario
    final boolean triggerCfi = triggerMmu && foreignAccount.hasCode();

    this.accountAfter =
        AccountSnapshot.fromAccount(
            foreignAccount, true, incomingDeploymentNumber, incomingDeploymentStatus);

    final AccountFragment accountDoingFragment =
        hub.factories()
            .accountFragment()
            .makeWithTrm(accountBefore, accountAfter, rawAddress, doingDomSubStamps);
    accountDoingFragment.requiresRomlex(triggerCfi);
    this.addFragment(accountDoingFragment);
    hub.romLex().callRomLex(hub.messageFrame());

    // an EXTCODECOPY section is only scheduled
    // for rollback if it is unexceptional
    hub.defers().scheduleForPostRollback(this, hub.currentFrame());
  }

  @Override
  public void resolvePostRollback(Hub hub, MessageFrame messageFrame, CallFrame callFrame) {

    final int deploymentNumberAtRollback =
        hub.transients().conflation().deploymentInfo().number(address);
    final boolean deploymentStatusAtRollback =
        hub.transients().conflation().deploymentInfo().isDeploying(address);

    final AccountSnapshot revertFromSnapshot =
        new AccountSnapshot(
            accountAfter.address(),
            accountAfter.nonce(),
            accountAfter.balance(),
            accountAfter.isWarm(),
            accountAfter.code(),
            deploymentNumberAtRollback,
            deploymentStatusAtRollback);

    final AccountSnapshot revertToSnapshot =
        new AccountSnapshot(
            accountBefore.address(),
            accountBefore.nonce(),
            accountBefore.balance(),
            accountBefore.isWarm(),
            accountBefore.code(),
            deploymentNumberAtRollback,
            deploymentStatusAtRollback);

    final DomSubStampsSubFragment undoingDomSubStamps =
        DomSubStampsSubFragment.revertWithCurrentDomSubStamps(hubStamp, callFrame.revertStamp(), 1);
    final AccountFragment undoingAccountFragment =
        hub.factories()
            .accountFragment()
            .make(revertFromSnapshot, revertToSnapshot, undoingDomSubStamps);

    this.addFragment(undoingAccountFragment);
  }

  @Override
  public void resolvePostTransaction(
      Hub hub, WorldView state, Transaction tx, boolean isSuccessful) {
    if (triggerMmu) {
      miscFragment.callMmu(mmuCall);
    }
  }
}