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
import net.consensys.linea.zktracer.module.hub.fragment.DomSubStampsSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.MxpCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.mmu.MmuCall;
import net.consensys.linea.zktracer.module.hub.section.TraceSection;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import net.consensys.linea.zktracer.types.Bytecode;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.account.Account;
import org.hyperledger.besu.evm.frame.MessageFrame;

public class ExtCodeCopySection extends TraceSection implements PostRollbackDefer {

  final Bytes rawAddress;
  final Address address;
  final int incomingDeploymentNumber;
  final boolean incomingDeploymentStatus;
  final boolean incomingWarmth;
  final short exceptions;

  AccountSnapshot accountBefore;
  AccountSnapshot accountAfter;

  public ExtCodeCopySection(Hub hub) {
    super(hub);

    final MessageFrame frame = hub.messageFrame();
    this.rawAddress = frame.getStackItem(0);
    this.address = Address.extract((Bytes32) this.rawAddress);
    this.incomingDeploymentNumber = hub.transients().conflation().deploymentInfo().number(this.address);
    this.incomingDeploymentStatus = hub.transients().conflation().deploymentInfo().isDeploying(this.address);
    this.incomingWarmth = frame.isAddressWarm(this.address);
    this.exceptions = hub.pch().exceptions();
  }

  public void populateSection(Hub hub) {

    hub.addTraceSection(this);

    ImcFragment imcFragment = ImcFragment.empty(hub);
    this.addFragmentsAndStack(hub, imcFragment);

    // triggerOob = false
    // triggerMxp = true
    MxpCall mxpCall = new MxpCall(hub);
    imcFragment.callMxp(mxpCall);

    Preconditions.checkArgument(mxpCall.mxpx == Exceptions.memoryExpansionException(this.exceptions));

    // The MXPX case
    ////////////////
    if (mxpCall.mxpx) {
      return;
    }

    final MessageFrame frame = hub.messageFrame();
    final Account foreignAccount = frame.getWorldUpdater().get(this.address);

    this.accountBefore =
        AccountSnapshot.fromAccount(
            foreignAccount,
            this.incomingWarmth,
            this.incomingDeploymentNumber,
            this.incomingDeploymentStatus);

    DomSubStampsSubFragment doingDomSubStamps =
        DomSubStampsSubFragment.standardDomSubStamps(hub, 0);

    // The OOGX case
    ////////////////
    if (outOfGasException(this.exceptions)) {
      // the last context row will be added automatically
      AccountFragment accountReadingFragment =
          hub.factories()
              .accountFragment()
              .makeWithTrm(this.accountBefore, this.accountBefore, this.rawAddress, doingDomSubStamps);

      this.addFragment(accountReadingFragment);
      return;
    }

    // The unexceptional case
    /////////////////////////
    final boolean triggerMmu = none(this.exceptions) && mxpCall.isMayTriggerNonTrivialMmuOperation();
    if (triggerMmu) {
      MmuCall mmuCall = MmuCall.extCodeCopy(hub);
      imcFragment.callMmu(mmuCall);
    }

    this.accountAfter =
        AccountSnapshot.fromAccount(
            foreignAccount,
            true,
            this.incomingDeploymentNumber,
            this.incomingDeploymentStatus);

    AccountFragment accountDoingFragment =
        hub.factories().accountFragment()
            .makeWithTrm(this.accountBefore, this.accountAfter, this.rawAddress, doingDomSubStamps);

    this.addFragment(accountDoingFragment);
    hub.defers().scheduleForPostRollback(this, hub.currentFrame());
  }

  @Override
  public void resolvePostRollback(Hub hub, MessageFrame messageFrame, CallFrame callFrame) {

    Account revertFrom = messageFrame.getWorldUpdater().get(this.address);
    final int deploymentNumberAtRollback = hub.transients().conflation().deploymentInfo().number(this.address);
    final boolean deploymentStatusAtRollback = hub.transients().conflation().deploymentInfo().isDeploying(this.address);

    AccountSnapshot revertFromSnapshot = new AccountSnapshot(
            this.address,
            revertFrom.getNonce(),
            revertFrom.getBalance(),
            true,
            new Bytecode(revertFrom.getCode().copy()), // stolen from AccountSnapshot.java
            deploymentNumberAtRollback,
            deploymentStatusAtRollback
    );

    AccountSnapshot revertToSnapshot = new AccountSnapshot(
            this.address,
            revertFrom.getNonce(),
            revertFrom.getBalance(),
            this.incomingWarmth,
            new Bytecode(revertFrom.getCode().copy()), // stolen from AccountSnapshot.java
            deploymentNumberAtRollback,
            deploymentStatusAtRollback
    );


    DomSubStampsSubFragment undoingDomSubStamps = DomSubStampsSubFragment.revertWithCurrentDomSubStamps(hub, 1);
    AccountFragment undoingAccountFragment =
            hub.factories().accountFragment().make(
                    revertFromSnapshot,
                    revertToSnapshot,
                    undoingDomSubStamps);

    this.addFragment(undoingAccountFragment);
  }
}
