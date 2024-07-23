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

package net.consensys.linea.zktracer.module.hub.section;

import net.consensys.linea.zktracer.module.hub.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.defer.PostRollbackDefer;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.DomSubStampsSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.account.Account;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.internal.Words;

public class AccountSection extends TraceSection implements PostRollbackDefer {

  final short exceptions;
  final Bytes rawTargetAddress;
  final Address targetAddress;
  final boolean intialWarmth;
  final AccountSnapshot accountSnapshotBefore;
  final AccountSnapshot accountSnapshotAfter;

  public void appendToTrace(Hub hub) {

    hub.addTraceSection(this);
    hub.defers().scheduleForPostRollback(this, hub.currentFrame());

    if (hub.opCode().isAnyOf(OpCode.SELFBALANCE, OpCode.CODESIZE)) {
      this.addFragment(ContextFragment.readCurrentContextData(hub));
    }

    final DomSubStampsSubFragment doingDomSubStamps =
        DomSubStampsSubFragment.standardDomSubStamps(this.hubStamp(), 0);

    AccountFragment doingAccountFragment =
        hub.opCode().isAnyOf(OpCode.SELFBALANCE, OpCode.CODESIZE)
            ? hub.factories()
                .accountFragment()
                .make(this.accountSnapshotBefore, this.accountSnapshotAfter, doingDomSubStamps)
            : hub.factories()
                .accountFragment()
                .makeWithTrm(
                    this.accountSnapshotBefore,
                    this.accountSnapshotAfter,
                    this.rawTargetAddress,
                    doingDomSubStamps);

    this.addFragment(doingAccountFragment);
  }

  private static short maxNumberOfRows(Hub hub) {
    OpCode opCode = hub.opCode();

    if (opCode.isAnyOf(OpCode.BALANCE, OpCode.EXTCODESIZE, OpCode.EXTCODEHASH)) {
      return (short) (opCode.numberOfStackRows() + 3);
    }

    return (short) (opCode.numberOfStackRows() + (Exceptions.any(hub.pch().exceptions()) ? 1 : 2));
  }

  public AccountSection(Hub hub) {
    super(hub, maxNumberOfRows(hub));
    hub.addTraceSection(this);
    this.addStack(hub);

    exceptions = hub.pch().exceptions();

    final MessageFrame frame = hub.messageFrame();
    this.rawTargetAddress =
        switch (hub.opCode()) {
          case BALANCE, EXTCODESIZE, EXTCODEHASH -> frame.getStackItem(0);
          case SELFBALANCE -> frame.getRecipientAddress();
          case CODESIZE -> frame.getContractAddress();
          default -> throw new RuntimeException("invalid opcode");
        };

    this.targetAddress = Words.toAddress(this.rawTargetAddress);
    this.intialWarmth = frame.isAddressWarm(targetAddress);
    this.accountSnapshotBefore =
        AccountSnapshot.fromAccount(
            getTargetAccount(hub),
            this.intialWarmth,
            hub.transients().conflation().deploymentInfo().number(targetAddress),
            hub.transients().conflation().deploymentInfo().isDeploying(targetAddress));
    this.accountSnapshotAfter =
        AccountSnapshot.fromAccount(
            getTargetAccount(hub),
            true, // QUESTION: is this true even if we revert with the instruction ? ANSWER: yes.
            hub.transients().conflation().deploymentInfo().number(targetAddress),
            hub.transients().conflation().deploymentInfo().isDeploying(targetAddress));
  }

  private Account getTargetAccount(Hub hub) {
    return hub.messageFrame().getWorldUpdater().get(targetAddress);
  }

  // TODO: make sure the resolvePostRollback method
  //  gets called before the final context row is
  //  added; simplest solution: have that final context
  //  row in a separate list.
  public void resolvePostRollback(Hub hub, MessageFrame messageFrame, CallFrame callFrame) {

    final DomSubStampsSubFragment undoingDomSubStamps =
        DomSubStampsSubFragment.revertWithCurrentDomSubStamps(
            this.hubStamp(), hub.currentFrame().revertStamp(), 0);

    final int deploymentNumberAtRollback =
        hub.transients().conflation().deploymentInfo().number(targetAddress);
    final boolean deploymentStatusAtRollback =
        hub.transients().conflation().deploymentInfo().isDeploying(targetAddress);
    AccountSnapshot revertFrom =
        new AccountSnapshot(
            this.accountSnapshotAfter.address(),
            this.accountSnapshotAfter.nonce(), // TODO: this will blow up!
            this.accountSnapshotAfter.balance(),
            this.accountSnapshotAfter.isWarm(),
            this.accountSnapshotAfter.code(),
            deploymentNumberAtRollback,
            deploymentStatusAtRollback);

    AccountSnapshot revertTo =
        new AccountSnapshot(
            this.accountSnapshotBefore.address(),
            this.accountSnapshotBefore.nonce(), // TODO: this will blow up!
            this.accountSnapshotBefore.balance(),
            this.accountSnapshotBefore.isWarm(),
            this.accountSnapshotBefore.code(),
            deploymentNumberAtRollback,
            deploymentStatusAtRollback);

    this.addFragment(
        hub.factories().accountFragment().make(revertFrom, revertTo, undoingDomSubStamps));
  }
}
