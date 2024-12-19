/*
 * Copyright ConsenSys Inc.
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

import static com.google.common.base.Preconditions.*;

import lombok.Setter;
import net.consensys.linea.zktracer.module.hub.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.defer.PostTransactionDefer;
import net.consensys.linea.zktracer.module.hub.fragment.DomSubStampsSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.TransactionFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import net.consensys.linea.zktracer.module.hub.transients.DeploymentInfo;
import net.consensys.linea.zktracer.types.TransactionProcessingMetadata;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class TxFinalizationSection extends TraceSection implements PostTransactionDefer {
  private final TransactionProcessingMetadata txMetadata;

  private final AccountSnapshot senderTxFinalization;
  private @Setter AccountSnapshot senderTxFinalizationNew;

  private final AccountSnapshot recipientTxFinalization;
  private @Setter AccountSnapshot recipientTxFinalizationNew;

  private final AccountSnapshot coinbaseTxFinalization;
  private @Setter AccountSnapshot coinbaseTxFinalizationNew;

  public TxFinalizationSection(Hub hub, WorldView world, boolean exceptionOrRevert) {
    super(hub, (short) 4);

    txMetadata = hub.txStack().current();

    final Address senderAddress = txMetadata.getSender();
    final Address recipientAddress = txMetadata.getEffectiveRecipient();
    final Address coinbaseAddress = txMetadata.getCoinbase();

    senderTxFinalization =
        exceptionOrRevert
            ? hub.txStack().getInitializationSection().getSenderValueTransferNew()
            : AccountSnapshot.canonical(hub, world, senderAddress);
    recipientTxFinalization =
        exceptionOrRevert
            ? hub.txStack().getInitializationSection().getRecipientValueReceptionNew()
            : AccountSnapshot.canonical(hub, world, recipientAddress);
    coinbaseTxFinalization = AccountSnapshot.canonical(hub, world, coinbaseAddress);

    hub.defers().scheduleForPostTransaction(this);
  }

  @Override
  public void resolvePostTransaction(
      Hub hub, WorldView world, Transaction tx, boolean isSuccessful) {

    final boolean coinbaseWarmth = txMetadata.isCoinbaseWarmAtTransactionEnd();

    final Address senderAddress = senderTxFinalization.address();
    senderTxFinalizationNew =
        AccountSnapshot.canonical(hub, world, senderAddress)
            .turnOnWarmth(); // purely constraints based

    final Address recipientAddress = recipientTxFinalization.address();
    recipientTxFinalizationNew =
        AccountSnapshot.canonical(hub, world, recipientAddress)
            .turnOnWarmth(); // purely constraints based

    final Address coinbaseAddress = coinbaseTxFinalization.address();
    coinbaseTxFinalizationNew =
        AccountSnapshot.canonical(hub, world, coinbaseAddress)
            .setWarmthTo(coinbaseWarmth); // purely constraints based

    DeploymentInfo deploymentInfo = hub.transients().conflation().deploymentInfo();
    checkArgument(isSuccessful == txMetadata.statusCode());

    // TODO: do we switch off the deployment status at the end of a deployment ?
    // checkArgument(
    //     !deploymentInfo.getDeploymentStatus(senderAddress),
    //     "The sender may not be under deployment");
    // checkArgument(
    //     !deploymentInfo.getDeploymentStatus(recipientAddress),
    //     "The recipient may not be under deployment");
    checkArgument(
        !deploymentInfo.getDeploymentStatus(coinbaseAddress),
        "The coinbase may not be under deployment");

    if (isSuccessful) {
      successFinalization(hub);
    } else {
      failureFinalization(hub);
    }
  }

  private void successFinalization(Hub hub) {
    // TODO: are the assignments here correct?
    // ACC i+0 (sender)
    senderTxFinalizationNew =
        senderTxFinalization.deepCopy().incrementBalanceBy(txMetadata.getGasRefundInWei());

    final AccountFragment senderAccountFragment =
        hub.factories()
            .accountFragment()
            .make(
                senderTxFinalization,
                senderTxFinalizationNew,
                DomSubStampsSubFragment.standardDomSubStamps(hub.stamp(), 0));

    // ACC i+1 (coinbase) (depending on weather the sender is the coinbase or not)
    coinbaseTxFinalizationNew =
        !txMetadata.senderIsCoinbase()
            ? coinbaseTxFinalization.deepCopy().incrementBalanceBy(txMetadata.getCoinbaseReward())
            : coinbaseTxFinalization
                .deepCopy()
                .incrementBalanceBy(
                    txMetadata.getGasRefundInWei().add(txMetadata.getCoinbaseReward()));

    final AccountFragment coinbaseAccountFragment =
        hub.factories()
            .accountFragment()
            .make(
                senderTxFinalization,
                coinbaseTxFinalizationNew,
                DomSubStampsSubFragment.standardDomSubStamps(hub.stamp(), 1));

    this.addFragments(senderAccountFragment, coinbaseAccountFragment);

    // TXN i+2
    final TransactionFragment currentTransactionFragment =
        TransactionFragment.prepare(hub.txStack().current());
    this.addFragment(currentTransactionFragment);
  }

  private void failureFinalization(Hub hub) {
    if (txMetadata.noAddressCollisions()) {

      final AccountFragment senderAccountFragment =
          hub.factories()
              .accountFragment()
              .make(
                  senderTxFinalization,
                  senderTxFinalizationNew,
                  DomSubStampsSubFragment.standardDomSubStamps(hub.stamp(), 0));

      final AccountFragment recipientAccountFragment =
          hub.factories()
              .accountFragment()
              .make(
                  recipientTxFinalization,
                  recipientTxFinalizationNew,
                  DomSubStampsSubFragment.standardDomSubStamps(hub.stamp(), 1));

      final AccountFragment coinbaseAccountFragment =
          hub.factories()
              .accountFragment()
              .make(
                  coinbaseTxFinalization,
                  coinbaseTxFinalizationNew,
                  DomSubStampsSubFragment.standardDomSubStamps(hub.stamp(), 2));

      this.addFragments(senderAccountFragment, recipientAccountFragment, coinbaseAccountFragment);

    } else {
      // TODO: should we treat differently the sender != coinbase case from the sender == coinbase
      //  in the failure finalization case, too?

      final Wei transactionValue = (Wei) txMetadata.getBesuTransaction().getValue();

      // FIRST ROW
      final AccountSnapshot senderSnapshotAfterValueAndGasRefunds =
          senderTxFinalization
              .deepCopy()
              .incrementBalanceBy(transactionValue)
              .incrementBalanceBy(txMetadata.getGasRefundInWei());

      final AccountFragment senderAccountFragment =
          hub.factories()
              .accountFragment()
              .make(
                  senderTxFinalization,
                  senderSnapshotAfterValueAndGasRefunds,
                  DomSubStampsSubFragment.standardDomSubStamps(hub.stamp(), 0));

      // SECOND ROW
      final AccountSnapshot recipientSnapshotBeforeSecondRow =
          (txMetadata.senderIsRecipient())
              ? senderSnapshotAfterValueAndGasRefunds
              : recipientTxFinalization;

      final AccountSnapshot recipientSnapshotAfterSecondRow =
          recipientSnapshotBeforeSecondRow.deepCopy().decrementBalanceBy(transactionValue);

      final AccountFragment recipientAccountFragment =
          hub.factories()
              .accountFragment()
              .make(
                  recipientSnapshotBeforeSecondRow,
                  recipientSnapshotAfterSecondRow,
                  DomSubStampsSubFragment.standardDomSubStamps(hub.stamp(), 1));

      // THIRD ROW
      final AccountSnapshot coinbaseSnapshotBefore =
          coinbaseTxFinalizationNew.deepCopy().decrementBalanceBy(txMetadata.getCoinbaseReward());

      final AccountFragment coinbaseAccountFragment =
          hub.factories()
              .accountFragment()
              .make(
                  coinbaseSnapshotBefore,
                  coinbaseTxFinalizationNew,
                  DomSubStampsSubFragment.standardDomSubStamps(hub.stamp(), 2));

      // TODO: in the new specs it seems we only have 2 accounts rows
      //  how to change the failure finalization case?
      this.addFragments(senderAccountFragment, recipientAccountFragment, coinbaseAccountFragment);
    }
    final TransactionFragment currentTransactionFragment =
        TransactionFragment.prepare(hub.txStack().current());

    this.addFragment(currentTransactionFragment);
  }
}
