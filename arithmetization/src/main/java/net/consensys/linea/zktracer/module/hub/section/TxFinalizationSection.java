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

import static net.consensys.linea.zktracer.module.constants.GlobalConstants.MAX_REFUND_QUOTIENT;

import java.math.BigInteger;
import java.util.Optional;

import net.consensys.linea.zktracer.module.hub.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.TraceFragment;
import net.consensys.linea.zktracer.module.hub.fragment.TransactionFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import net.consensys.linea.zktracer.module.hub.transients.DeploymentInfo;
import net.consensys.linea.zktracer.types.TransactionProcessingMetadata;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.evm.account.Account;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class TxFinalizationSection extends TraceSection {
  public TxFinalizationSection(Hub hub, TraceFragment... fragments) {
    this.addFragmentsWithoutStack(hub, fragments);
  }

  public static TxFinalizationSection finalizeSuccessfulTransaction(
      final Hub hub, final WorldView worldView, final boolean coinbaseWarmth) {

    final TransactionProcessingMetadata tx = hub.txStack().current();

    final Address senderAddress = tx.getSender();
    final Address coinbaseAddress = tx.getCoinbase();

    final DeploymentInfo deploymentInfo = hub.transients().conflation().deploymentInfo();

    final long gasUsed = tx.getBesuTransaction().getGasLimit() - tx.getLeftoverGas();
    final long effectiveRefunds = Math.min(hub.accruedRefunds(), gasUsed / MAX_REFUND_QUOTIENT);
    final long senderGasRefund = tx.getLeftoverGas() + effectiveRefunds;
    final long coinbaseGasReward = tx.getBesuTransaction().getGasLimit() - senderGasRefund;

    final Wei coinbaseFee =
        Wei.of(
            BigInteger.valueOf(tx.getEffectiveGasPrice())
                .multiply(BigInteger.valueOf(coinbaseGasReward)));

    final AccountFragment.AccountFragmentFactory accountFragmentFactory =
        hub.factories().accountFragment();

    final Wei senderWeiRefund =
        Wei.of(
            BigInteger.valueOf(senderGasRefund)
                .multiply(BigInteger.valueOf(tx.getEffectiveGasPrice())));

    // SENDER account snapshots
    final Account senderAccount = worldView.get(senderAddress);
    final AccountSnapshot senderAccountSnapshotBeforeRefunds =
        AccountSnapshot.fromAccount(
            senderAccount,
            true,
            deploymentInfo.number(senderAddress),
            deploymentInfo.isDeploying(senderAddress));

    final AccountSnapshot senderAccountSnapshotAfterGasRefund =
        senderAccountSnapshotBeforeRefunds.credit(senderWeiRefund);

    final boolean coinbaseIsSender = coinbaseAddress.equals(senderAddress);

    // COINBASE account snapshots
    Optional<Account> optionalCoinbaseAccount = Optional.ofNullable(worldView.get(coinbaseAddress));
    final AccountSnapshot coinbaseSnapshotBeforeFeeCollection =
        coinbaseIsSender
            ? senderAccountSnapshotAfterGasRefund
            // WARNING!
            // worldView.get(address) returns null
            // if there is no account at that address
            // this is why we get a zero address
            : optionalCoinbaseAccount.isPresent()
                ? AccountSnapshot.fromAccount(
                    optionalCoinbaseAccount,
                    coinbaseWarmth,
                    deploymentInfo.number(coinbaseAddress),
                    deploymentInfo.isDeploying(coinbaseAddress))
                : AccountSnapshot.fromAddress(
                    coinbaseAddress,
                    coinbaseWarmth,
                    deploymentInfo.number(coinbaseAddress),
                    deploymentInfo.isDeploying(coinbaseAddress));

    final AccountSnapshot coinbaseSnapshotAfterFeeCollection =
        coinbaseSnapshotBeforeFeeCollection.credit(coinbaseFee, coinbaseWarmth);

    return new TxFinalizationSection(
        hub,
        accountFragmentFactory.make(
            senderAccountSnapshotBeforeRefunds, senderAccountSnapshotAfterGasRefund),
        accountFragmentFactory.make(
            coinbaseSnapshotBeforeFeeCollection, coinbaseSnapshotAfterFeeCollection),
        TransactionFragment.prepare(hub.txStack().current()));
  }

  public static TxFinalizationSection finalizeUnsuccessfulTransaction(
      final Hub hub, final WorldView worldView, final boolean coinbaseWarmth) {

    final TransactionProcessingMetadata tx = hub.txStack().current();

    final Address senderAddress = tx.getSender();
    final Address effectiveToAddress = tx.getEffectiveTo();
    final Address coinbaseAddress = tx.getCoinbase();

    final DeploymentInfo deploymentInfo = hub.transients().conflation().deploymentInfo();

    final Account senderAccount = worldView.get(senderAddress);
    final AccountSnapshot senderAccountSnapshotBeforeRefunds =
        AccountSnapshot.fromAccount(
            senderAccount,
            true,
            deploymentInfo.number(senderAddress),
            deploymentInfo.isDeploying(senderAddress));

    final long gasUsed = tx.getBesuTransaction().getGasLimit() - tx.getLeftoverGas();
    final long effectiveRefunds = Math.min(hub.accruedRefunds(), gasUsed / MAX_REFUND_QUOTIENT);
    final long senderGasRefund = tx.getLeftoverGas() + effectiveRefunds;

    final long coinbaseGasReward = tx.getBesuTransaction().getGasLimit() - senderGasRefund;
    final Wei coinbaseFee =
        Wei.of(
            BigInteger.valueOf(tx.getEffectiveGasPrice())
                .multiply(BigInteger.valueOf(coinbaseGasReward)));

    final AccountFragment.AccountFragmentFactory accountFragmentFactory =
        hub.factories().accountFragment();

    // SENDER account snapshots
    final Wei senderWeiRefund =
        Wei.of(
            BigInteger.valueOf(senderGasRefund)
                .multiply(BigInteger.valueOf(tx.getEffectiveGasPrice()))
                .add(tx.getBesuTransaction().getValue().getAsBigInteger()));
    final AccountSnapshot senderAccountSnapshotAfterGasAndBalanceRefund =
        senderAccountSnapshotBeforeRefunds.credit(senderWeiRefund);

    // RECIPIENT account snapshots
    final boolean recipientIsSender = effectiveToAddress.equals(senderAddress);
    final AccountSnapshot effectiveToAccountSnapshotBeforeReimbursingValue =
        recipientIsSender
            ? senderAccountSnapshotAfterGasAndBalanceRefund
            : AccountSnapshot.fromAccount(
                worldView.get(effectiveToAddress),
                true,
                deploymentInfo.number(effectiveToAddress),
                deploymentInfo.isDeploying(effectiveToAddress));

    final AccountSnapshot effectiveToAccountSnapshotAfterReimbursingValue =
        effectiveToAccountSnapshotBeforeReimbursingValue.debit(
            (Wei) tx.getBesuTransaction().getValue());

    // COINBASE account snapshots
    final boolean coinbaseIsRecipient = coinbaseAddress.equals(effectiveToAddress);
    final boolean coinbaseIsSender = coinbaseAddress.equals(senderAddress);

    AccountSnapshot coinbaseBeforeFeeCollection =
        coinbaseIsRecipient
            ? effectiveToAccountSnapshotAfterReimbursingValue
            : coinbaseIsSender
                ? senderAccountSnapshotAfterGasAndBalanceRefund
                : AccountSnapshot.fromAccount(
                    worldView.get(coinbaseAddress),
                    coinbaseWarmth,
                    deploymentInfo.number(coinbaseAddress),
                    deploymentInfo.isDeploying(coinbaseAddress));

    AccountSnapshot coinbaseAfterFeeCollection =
        coinbaseBeforeFeeCollection.credit(coinbaseFee, coinbaseWarmth);

    return new TxFinalizationSection(
        hub,
        accountFragmentFactory.make(
            senderAccountSnapshotBeforeRefunds, senderAccountSnapshotAfterGasAndBalanceRefund),
        accountFragmentFactory.make(
            effectiveToAccountSnapshotBeforeReimbursingValue,
            effectiveToAccountSnapshotAfterReimbursingValue),
        accountFragmentFactory.make(coinbaseBeforeFeeCollection, coinbaseAfterFeeCollection),
        TransactionFragment.prepare(hub.txStack().current()));
  }

  @Override
  public void seal(Hub hub) {}
}
