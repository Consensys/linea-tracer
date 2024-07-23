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

import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import lombok.Getter;
import net.consensys.linea.zktracer.module.hub.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.defer.PostRollbackDefer;
import net.consensys.linea.zktracer.module.hub.defer.PostTransactionDefer;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.DomSubStampsSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import net.consensys.linea.zktracer.module.hub.fragment.scenario.SelfdestructScenarioFragment;
import net.consensys.linea.zktracer.module.hub.section.TraceSection;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import net.consensys.linea.zktracer.types.TransactionProcessingMetadata.AddressDeploymentNumberKey;
import net.consensys.linea.zktracer.types.TransactionProcessingMetadata.HubStampCallFrameValue;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class SelfdestructSection extends TraceSection
    implements PostRollbackDefer, PostTransactionDefer {

  final int id;
  final int hubStamp;
  final short exceptions;

  SelfdestructScenarioFragment selfdestructScenarioFragment;

  final Address address;
  AccountSnapshot accountBefore;
  AccountSnapshot accountAfter;

  final Bytes recipientRawAddress;
  final Address recipientAddress;
  AccountFragment selfDestroyerFirstAccountFragment;
  AccountFragment recipientFirstAccountFragment;
  AccountSnapshot recipientAccountBefore;
  AccountSnapshot recipientAccountAfter;

  final boolean selfDestructTargetsItself;
  @Getter boolean selfDestructWasReverted = false;

  public SelfdestructSection(Hub hub) {
    // up to 8 = 1 + 7 rows
    super(hub, (short) 8);
    hub.addTraceSection(this);

    // Init
    this.id = hub.currentFrame().id();
    hubStamp = hub.stamp();
    this.exceptions = hub.pch().exceptions();

    final MessageFrame frame = hub.messageFrame();

    // Account
    this.address = frame.getSenderAddress();
    this.accountBefore = AccountSnapshot.canonical(hub, this.address);

    // Recipient
    this.recipientRawAddress = frame.getStackItem(0);
    this.recipientAddress = Address.extract((Bytes32) this.recipientRawAddress);

    this.selfDestructTargetsItself = this.address.equals(this.recipientAddress);

    this.recipientAccountBefore =
        selfDestructTargetsItself
            ? this.accountAfter.deepCopy()
            : AccountSnapshot.canonical(hub, this.recipientAddress);

    selfdestructScenarioFragment = new SelfdestructScenarioFragment();
    // SCN fragment
    this.addFragment(selfdestructScenarioFragment);
    if (Exceptions.any(exceptions)) {
      selfdestructScenarioFragment.setScenario(
          SelfdestructScenarioFragment.SelfdestructScenario.SELFDESTRUCT_EXCEPTION);
    }

    // CON fragment (1)
    ContextFragment contextFragment = ContextFragment.readCurrentContextData(hub);
    this.addFragment(contextFragment);

    // STATICX case
    if (Exceptions.staticFault(exceptions)) {
      return;
    }

    // OOGX case
    if (Exceptions.any(exceptions)) {
      Preconditions.checkArgument(exceptions == Exceptions.OUT_OF_GAS_EXCEPTION);

      selfDestroyerFirstAccountFragment =
          hub.factories()
              .accountFragment()
              .make(
                  this.accountBefore,
                  this.accountBefore,
                  DomSubStampsSubFragment.standardDomSubStamps(hub, 0));

      this.addFragment(selfDestroyerFirstAccountFragment);

      recipientFirstAccountFragment =
          hub.factories()
              .accountFragment()
              .makeWithTrm(
                  this.recipientAccountBefore,
                  this.recipientAccountBefore,
                  this.recipientRawAddress,
                  DomSubStampsSubFragment.standardDomSubStamps(hub, 1));

      this.addFragment(recipientFirstAccountFragment);

      return;
    }

    // Unexceptional case
    Map<AddressDeploymentNumberKey, List<HubStampCallFrameValue>> unexceptionalSelfDestructMap =
        hub.txStack().current().getUnexceptionalSelfDestructMap();

    AddressDeploymentNumberKey addressDeploymentNumberKey =
        new AddressDeploymentNumberKey(this.address, this.accountBefore.deploymentNumber());

    if (unexceptionalSelfDestructMap.containsKey(addressDeploymentNumberKey)) {
      List<HubStampCallFrameValue> hubStampCallFrameValues =
          unexceptionalSelfDestructMap.get(addressDeploymentNumberKey);
      hubStampCallFrameValues.add(new HubStampCallFrameValue(hubStamp, hub.currentFrame()));
      // TODO: double check that re-adding the list to the map is not necessary, as the list is a
      //  reference
      //  unexceptionalSelfDestructMap.put(addressDeploymentNumberKey, hubStampCallFrameValues);
    } else {
      unexceptionalSelfDestructMap.put(
          new AddressDeploymentNumberKey(this.address, this.accountBefore.deploymentNumber()),
          List.of(new HubStampCallFrameValue(hubStamp, hub.currentFrame())));
    }

    hub.defers().scheduleForPostRollback(this, hub.currentFrame());
    hub.defers().schedulePostTransaction(this);

    // Modify the current account and the recipient account
    // - The current account has its balance reduced to 0 (i+2)
    //   * selfDestroyerFirstAccountFragment
    // - The recipient account, if it is not the current account, receive that balance (+= balance),
    // otherwise remains 0 (i+3)
    //   * recipientFirstAccountFragment
    // - The recipient address will become warm (i+3)
    //   * recipientFirstAccountFragment

    this.accountAfter = this.accountBefore.debit(this.accountBefore.balance());
    selfDestroyerFirstAccountFragment =
        hub.factories()
            .accountFragment()
            .make(
                this.accountBefore,
                this.accountAfter,
                DomSubStampsSubFragment.selfdestructDomSubStamps(hub));
    this.addFragment(selfDestroyerFirstAccountFragment);

    this.recipientAccountAfter =
        this.selfDestructTargetsItself
            ? this.accountAfter.deepCopy()
            : this.recipientAccountBefore.credit(
                this.accountBefore
                    .balance()); // NOTE: in the second case account is equal to recipientAccount
    this.recipientAccountAfter = this.recipientAccountAfter.turnOnWarmth();
    recipientFirstAccountFragment =
        hub.factories()
            .accountFragment()
            .make(
                this.recipientAccountBefore,
                this.recipientAccountAfter,
                DomSubStampsSubFragment.selfdestructDomSubStamps(hub));
    this.addFragment(recipientFirstAccountFragment);
  }

  @Override
  public void resolvePostRollback(Hub hub, MessageFrame messageFrame, CallFrame callFrame) {
    // Undo the modifications we applied to selfDestroyerFirstAccountFragment and
    // recipientFirstAccountFragment
    final AccountFragment selfDestroyerUndoingAccountFragment =
        hub.factories()
            .accountFragment()
            .make(
                this.accountAfter,
                this.accountBefore,
                DomSubStampsSubFragment.revertWithCurrentDomSubStamps(
                    hubStamp, callFrame.revertStamp(), 2));
    this.addFragment(selfDestroyerUndoingAccountFragment);

    final AccountFragment recipientUndoingAccountFragment =
        hub.factories()
            .accountFragment()
            .make(
                this.recipientAccountAfter,
                this.recipientAccountBefore,
                DomSubStampsSubFragment.revertWithCurrentDomSubStamps(
                    hubStamp, callFrame.revertStamp(), 3));
    this.addFragment(recipientUndoingAccountFragment);

    ContextFragment squashParentContextReturnData =
        ContextFragment.executionProvidesEmptyReturnData(
            hub, hub.callStack().getParentContextNumberById(this.id));
    this.addFragment(squashParentContextReturnData);

    selfDestructWasReverted = true;

    selfdestructScenarioFragment.setScenario(
        SelfdestructScenarioFragment.SelfdestructScenario.SELFDESTRUCT_WILL_REVERT);
  }

  @Override
  public void resolvePostTransaction(
      Hub hub, WorldView state, Transaction tx, boolean isSuccessful) {
    // if selfDestructWasReverted = false then we are in the WONT_REVERT case
    // We still need to understand in which of the below cases we are:
    // selfdestructScenarioFragment.setScenario(
    //  SelfdestructScenarioFragment.SelfdestructScenario.SELFDESTRUCT_WONT_REVERT_NOT_YET_MARKED);
    // selfdestructScenarioFragment.setScenario(
    //  SelfdestructScenarioFragment.SelfdestructScenario.SELFDESTRUCT_WONT_REVERT_ALREADY_MARKED);

    // will not revert (subcases: already marked, not yet marked)
    // - not yet marked corresponds to when SELFDESTRUCT produces no exceptions, will not be
    // reverted
    // ,and it is the first time this account will be successful in self-destructing

    // - already marked corresponds to when SELFDESTRUCT produces no exceptions, will not be
    // reverted
    // ,and it is not the first time this account will be successful in self-destructing

    // mark for self-destructing is associated to an address and a deployment number
    // use a maps that keeps track of the (hub stamp, call frame) of all the unexceptional
    // SELFDESTRUCT for a given (address, deployment number)

    // we need the callFrame in order to tell us, when we walk through this map at the end of the
    // transaction (transactionEnd),
    // if the SELFDESTRUCT took place in a frame that will be reverted/roll back (in this case, we
    // can ignore it)
    // later this information will be used to filter out the reverted SELFDESTRUCT
    // once we have this filtered map, for every pair (address, deploymentNumber),
    // we can compute its selfDestructTime = the first hubStamp such that is successful and not
    // reverted
    // these SELFDESTRUCT will be finished at the end of the transaction by wiping the corresponding
    // accounts

    // at the end of the transaction we have that map

    // we analyse that map:
    // for every (address, deployment number) we walk through the list of [(hub stamp, call frame),
    // ...]

    // for every call frame we know if it was reverted or not

    // the first time (selfDestructTime) we find a call frame that has not been reverted, we
    // remember the hub stamp

    // this produces a new map (address, deployment number) -> selfDestructTime (of the first
    // successful and un-reverted
    // SELFDESTRUCT)

    // we now have a map of all (address, deployment number) that have been successful in
    // self-destructing
    // and the hub stamp in which it happened

    // for every account row in the entire trace, we can now decide what to write in the
    // MARKED_FOR_SELFDESTRUCT and MARKED_FOR_SELFDESTRUCT_NEW columns:
    // MARKED_FOR_SELFDESTRUCT = [hub stamp < selfDestructTime]
    // MARKED_FOR_SELFDESTRUCT_NEW = [hub stamp >= selfDestructTime]

    // This will only be triggered at the hub stamp = selfDestructTime
    // we wipe the entire account in the "not yet marked" case (precisely when not yet marked
    // transitions from false to true) (see row i+4 of not yet marked case)
    // in the already marked case we know that this action has already been scheduled for the future

    // every transaction should start with an empty map

    // TODO: review and comment the code below

    for (Map.Entry<AddressDeploymentNumberKey, List<HubStampCallFrameValue>> entry :
        hub.txStack().current().getUnexceptionalSelfDestructMap().entrySet()) {

      AddressDeploymentNumberKey addressDeploymentNumberKey = entry.getKey();
      List<HubStampCallFrameValue> hubStampCallFrameValues = entry.getValue();

      int selfDestructTime = -1;
      for (HubStampCallFrameValue hubStampCallFrameValue : hubStampCallFrameValues) {
        if (hubStampCallFrameValue.callFrame().revertStamp() == -1) {
          selfDestructTime = hubStampCallFrameValue.hubStamp();
          hub.txStack()
              .current()
              .getEffectiveSelfDestructMap()
              .put(addressDeploymentNumberKey, selfDestructTime);
          break;
        }
      }

      if (selfDestructTime != -1) {
        AccountFragment accountFragment =
            hub.factories()
                .accountFragment()
                .make(
                    this.accountAfter,
                    AccountSnapshot.empty(false, 0, false),
                    DomSubStampsSubFragment.selfdestructDomSubStamps(hub));
        this.addFragment(accountFragment);
      }
    }
  }
}
