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

  private AccountSnapshot senderFinalization;
  private AccountSnapshot senderFinalizationNew;

  private AccountSnapshot coinbaseFinalization;
  private AccountSnapshot coinbaseFinalizationNew;

  public TxFinalizationSection(Hub hub, WorldView world, boolean exceptionOrRevert) {
    super(hub, (short) 4);

    txMetadata = hub.txStack().current();

    final Address senderAddress = txMetadata.getSender();
    final Address coinbaseAddress = txMetadata.getCoinbase();

    senderFinalization =
        exceptionOrRevert
            ? hub.txStack().getInitializationSection().getSenderValueTransferNew()
            : AccountSnapshot.canonical(hub, world, senderAddress);
    coinbaseFinalization = AccountSnapshot.canonical(hub, world, coinbaseAddress);

    hub.defers().scheduleForPostTransaction(this);
  }

  @Override
  public void resolvePostTransaction(
      Hub hub, WorldView world, Transaction tx, boolean isSuccessful) {

    final boolean coinbaseWarmth = txMetadata.isCoinbaseWarmAtTransactionEnd();

    final Address senderAddress = senderFinalization.address();
    senderFinalizationNew =
        AccountSnapshot.canonical(hub, world, senderAddress)
            .turnOnWarmth(); // purely constraints based

    final Address coinbaseAddress = coinbaseFinalization.address();
    coinbaseFinalizationNew =
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
    senderFinalizationNew =
        senderFinalization.deepCopy().incrementBalanceBy(txMetadata.getGasRefundInWei());

    final AccountFragment senderAccountFragment =
        hub.factories()
            .accountFragment()
            .make(
                    senderFinalization,
                    senderFinalizationNew,
                DomSubStampsSubFragment.standardDomSubStamps(hub.stamp(), 0));

    // ACC i+1 (coinbase) (depending on weather the sender is the coinbase or not)
    coinbaseFinalizationNew =
        !txMetadata.senderIsCoinbase()
            ? coinbaseFinalization.deepCopy().incrementBalanceBy(txMetadata.getCoinbaseReward())
            : coinbaseFinalization
                .deepCopy()
                .incrementBalanceBy(
                    txMetadata.getGasRefundInWei().add(txMetadata.getCoinbaseReward()));

    final AccountFragment coinbaseAccountFragment =
        hub.factories()
            .accountFragment()
            .make(
                    senderFinalization,
                    coinbaseFinalizationNew,
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
                      senderFinalization,
                      senderFinalizationNew,
                  DomSubStampsSubFragment.standardDomSubStamps(hub.stamp(), 0));

      final AccountFragment coinbaseAccountFragment =
          hub.factories()
              .accountFragment()
              .make(
                      coinbaseFinalization,
                      coinbaseFinalizationNew,
                  DomSubStampsSubFragment.standardDomSubStamps(hub.stamp(), 2));

      this.addFragments(senderAccountFragment, coinbaseAccountFragment);

    } else {
      // TODO: should we treat differently the sender != coinbase case from the sender == coinbase
      //  in the failure finalization case, too?

      final Wei transactionValue = (Wei) txMetadata.getBesuTransaction().getValue();

      // FIRST ROW
      final AccountSnapshot senderSnapshotAfterValueAndGasRefunds =
          senderFinalization
              .deepCopy()
              .incrementBalanceBy(transactionValue)
              .incrementBalanceBy(txMetadata.getGasRefundInWei());

      final AccountFragment senderAccountFragment =
          hub.factories()
              .accountFragment()
              .make(
                      senderFinalization,
                  senderSnapshotAfterValueAndGasRefunds,
                  DomSubStampsSubFragment.standardDomSubStamps(hub.stamp(), 0));

      // THIRD ROW
      final AccountSnapshot coinbaseSnapshotBefore =
          coinbaseFinalizationNew.deepCopy().decrementBalanceBy(txMetadata.getCoinbaseReward());

      final AccountFragment coinbaseAccountFragment =
          hub.factories()
              .accountFragment()
              .make(
                  coinbaseSnapshotBefore,
                      coinbaseFinalizationNew,
                  DomSubStampsSubFragment.standardDomSubStamps(hub.stamp(), 2));

      this.addFragments(senderAccountFragment, coinbaseAccountFragment);
    }
    final TransactionFragment currentTransactionFragment =
        TransactionFragment.prepare(hub.txStack().current());

    this.addFragment(currentTransactionFragment);
  }
}
