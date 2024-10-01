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

package net.consensys.linea.zktracer.types;

import static net.consensys.linea.zktracer.module.constants.GlobalConstants.*;
import static net.consensys.linea.zktracer.types.AddressUtils.effectiveToAddress;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.ZkTracer;
import net.consensys.linea.zktracer.module.hub.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.transients.Block;
import net.consensys.linea.zktracer.module.hub.transients.StorageInitialValues;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.evm.log.Log;
import org.hyperledger.besu.evm.worldstate.WorldView;

@Getter
public class TransactionProcessingMetadata {

  final int absoluteTransactionNumber;
  final int relativeTransactionNumber;
  final int relativeBlockNumber;

  final Transaction besuTransaction;
  final Address coinbase;
  final long baseFee;

  final boolean isDeployment;
  final int recipientAddressDeploymentNumber;
  final boolean recipientAddressDeploymentStatus;

  @Accessors(fluent = true)
  final boolean requiresEvmExecution;

  @Accessors(fluent = true)
  final boolean copyTransactionCallData;

  final BigInteger initialBalance;

  final long dataCost;
  final long accessListCost;

  /* g in the EYP, defined by g = TG - g0 */
  final long initiallyAvailableGas;

  final Address effectiveRecipient;

  final long effectiveGasPrice;

  /* g' in the EYP*/
  @Setter long leftoverGas = -1;
  /*  Ar in the EYP*/
  @Setter long refundCounterMax = -1;
  /* g* - g' in the EYP*/
  @Setter long refundEffective = -1;
  /* Tg - g' in the EYP*/
  @Setter long gasUsed = -1;
  /* g* in the EYP */
  @Setter long gasRefunded = -1;
  /* Tg - g* in the EYP */
  @Setter long totalGasUsed = -1;

  @Accessors(fluent = true)
  @Setter
  boolean statusCode = false;

  @Setter int hubStampTransactionEnd;

  @Setter int accumulatedGasUsedInBlock = -1;

  @Accessors(fluent = true)
  @Setter
  boolean isSenderPreWarmed = false;

  @Accessors(fluent = true)
  @Setter
  boolean isRecipientPreWarmed = false;

  @Accessors(fluent = true)
  @Setter
  boolean isCoinbaseWarmAtTransactionEnd = false;

  @Setter List<Log> logs;

  final StorageInitialValues storage = new StorageInitialValues();

  @Setter int codeFragmentIndex = -1;

  @Setter Set<AccountSnapshot> destructedAccountsSnapshot = new HashSet<>();

  @Getter
  final Map<EphemeralAccount, List<AttemptedSelfDestruct>> unexceptionalSelfDestructMap =
      new HashMap<>();

  @Getter final Map<EphemeralAccount, Integer> effectiveSelfDestructMap = new HashMap<>();

  // Ephermeral accounts are both accounts that have been deployed on-chain
  // and accounts that live for a limited time
  public record EphemeralAccount(Address address, int deploymentNumber) {}

  public record AttemptedSelfDestruct(int hubStamp, CallFrame callFrame) {}

  public TransactionProcessingMetadata(
    final Hub hub,
      final WorldView world,
      final Transaction transaction,
      final Block block,
      final int relativeTransactionNumber,
      final int absoluteTransactionNumber) {
    this.absoluteTransactionNumber = absoluteTransactionNumber;
    relativeBlockNumber = block.blockNumber();
    coinbase = block.coinbaseAddress();
    baseFee = block.baseFee().toLong();

    besuTransaction = transaction;
    this.relativeTransactionNumber = relativeTransactionNumber;

    isDeployment = transaction.getTo().isEmpty();
    requiresEvmExecution = computeRequiresEvmExecution(world);
    copyTransactionCallData = computeCopyCallData();

    initialBalance = getInitialBalance(world);

    // Note: Besu's dataCost computation contains
    // - the 21_000 transaction cost (we deduce it)
    // - the contract creation cost in case of deployment (we set deployment to false to not add it)
    dataCost =
        ZkTracer.gasCalculator.transactionIntrinsicGasCost(besuTransaction.getPayload(), false)
            - GAS_CONST_G_TRANSACTION;
    accessListCost =
        besuTransaction.getAccessList().map(ZkTracer.gasCalculator::accessListGasCost).orElse(0L);
    initiallyAvailableGas = getInitiallyAvailableGas();

    effectiveRecipient = effectiveToAddress(besuTransaction);

    effectiveGasPrice = computeEffectiveGasPrice();

    recipientAddressDeploymentNumber = hub.deploymentNumberOf(effectiveRecipient);
    recipientAddressDeploymentStatus = hub.deploymentStatusOf(effectiveRecipient);

  }

  public void setPreFinalisationValues(
      final long leftOverGas,
      final long refundCounterMax,
      final boolean coinbaseIsWarmAtFinalisation,
      final int accumulatedGasUsedInBlockAtStartTx) {

    this.isCoinbaseWarmAtTransactionEnd(coinbaseIsWarmAtFinalisation);
    this.refundCounterMax = refundCounterMax;
    this.setLeftoverGas(leftOverGas);
    this.gasUsed = computeGasUsed();
    this.refundEffective = computeRefundEffective();
    this.gasRefunded = computeRefunded();
    this.totalGasUsed = computeTotalGasUsed();
    this.accumulatedGasUsedInBlock = (int) (accumulatedGasUsedInBlockAtStartTx + totalGasUsed);
  }

  public void completeLineaTransaction(
      Hub hub, final boolean statusCode, final List<Log> logs, final Set<Address> selfDestructs) {
    this.statusCode = statusCode;
    hubStampTransactionEnd = hub.stamp();
    this.logs = logs;
    for (Address address : selfDestructs) {
      hub.transients()
          .conflation()
          .deploymentInfo()
          .freshDeploymentNumberFinishingSelfdestruct(
              address); // depNum += 1 and depStatus <- false
      destructedAccountsSnapshot.add(
          AccountSnapshot.fromAddress(
              address, true, hub.deploymentNumberOf(address), hub.deploymentStatusOf(address)));

      // registering the "fresh new deployment number" (that doesn't correspond to any deployment)
      hub.romLex().callRomLexForSelfdestruct(address);
    }

    determineSelfDestructTimeStamp();
  }

  private boolean computeCopyCallData() {
    return requiresEvmExecution && !isDeployment && !besuTransaction.getData().get().isEmpty();
  }

  private boolean computeRequiresEvmExecution(WorldView world) {
    if (!isDeployment) {
      return Optional.ofNullable(world.get(this.besuTransaction.getTo().get()))
          .map(a -> !a.getCode().isEmpty())
          .orElse(false);
    }

    return !this.besuTransaction.getInit().get().isEmpty();
  }

  private BigInteger getInitialBalance(WorldView world) {
    final Address sender = besuTransaction.getSender();
    return world.get(sender).getBalance().getAsBigInteger();
  }

  public long getUpfrontGasCost() {
    return dataCost
        + (isDeployment ? GAS_CONST_G_CREATE : 0)
        + GAS_CONST_G_TRANSACTION
        + accessListCost;
  }

  public long getInitiallyAvailableGas() {
    return besuTransaction.getGasLimit() - getUpfrontGasCost();
  }

  private long computeRefundEffective() {
    final long maxRefundableAmount = this.getGasUsed() / MAX_REFUND_QUOTIENT;
    return Math.min(maxRefundableAmount, refundCounterMax);
  }

  private long computeEffectiveGasPrice() {
    final Transaction tx = besuTransaction;
    switch (tx.getType()) {
      case FRONTIER, ACCESS_LIST -> {
        return tx.getGasPrice().get().getAsBigInteger().longValueExact();
      }
      case EIP1559 -> {
        final long baseFee = this.baseFee;
        final long maxPriorityFee =
            tx.getMaxPriorityFeePerGas().get().getAsBigInteger().longValueExact();
        final long maxFeePerGas = tx.getMaxFeePerGas().get().getAsBigInteger().longValueExact();
        return Math.min(baseFee + maxPriorityFee, maxFeePerGas);
      }
      default -> throw new IllegalArgumentException("Transaction type not supported");
    }
  }

  public Address getSender() {
    return besuTransaction.getSender();
  }

  public boolean requiresPrewarming() {
    return requiresEvmExecution && (accessListCost != 0);
  }

  public boolean requiresCfiUpdate() {
    return requiresEvmExecution && isDeployment;
  }

  /* Tg - g' in the EYP*/
  public long computeGasUsed() {
    return besuTransaction.getGasLimit() - leftoverGas;
  }

  /* g* in the EYP */
  public long computeRefunded() {
    return leftoverGas + this.refundEffective;
  }

  /* Tg - g* in the EYP */
  public long computeTotalGasUsed() {
    return besuTransaction.getGasLimit() - getGasRefunded();
  }

  public long feeRateForCoinbase() {
    return switch (besuTransaction.getType()) {
      case FRONTIER, ACCESS_LIST, EIP1559 -> effectiveGasPrice - baseFee;
      default -> throw new IllegalStateException(
          "Transaction Type not supported: " + besuTransaction.getType());
    };
  }

  public Wei getCoinbaseReward() {
    return Wei.of(
        BigInteger.valueOf(totalGasUsed).multiply(BigInteger.valueOf(feeRateForCoinbase())));
  }

  public Wei getGasRefundInWei() {
    return Wei.of(BigInteger.valueOf(gasRefunded).multiply(BigInteger.valueOf(effectiveGasPrice)));
  }

  public int numberWarmedAddress() {
    return this.besuTransaction.getAccessList().isPresent()
        ? this.besuTransaction.getAccessList().get().size()
        : 0;
  }

  public int numberWarmedKey() {
    return this.besuTransaction.getAccessList().isPresent()
        ? this.besuTransaction.getAccessList().get().stream()
            .mapToInt(accessListEntry -> accessListEntry.storageKeys().size())
            .sum()
        : 0;
  }

  private void determineSelfDestructTimeStamp() {
    for (Map.Entry<EphemeralAccount, List<AttemptedSelfDestruct>> entry :
        unexceptionalSelfDestructMap.entrySet()) {

      final EphemeralAccount ephemeralAccount = entry.getKey();
      final List<AttemptedSelfDestruct> attemptedSelfDestructs = entry.getValue();

      // For each address, deployment number, we find selfDestructTime as
      // the time in which the first unexceptional and un-reverted SELFDESTRUCT occurs
      // Then we add this value in a new map
      for (AttemptedSelfDestruct attemptedSelfDestruct : attemptedSelfDestructs) {
        if (attemptedSelfDestruct.callFrame().revertStamp() == 0) {
          final int selfDestructTime = attemptedSelfDestruct.hubStamp();
          effectiveSelfDestructMap.put(ephemeralAccount, selfDestructTime);
          break;
        }
      }
    }
  }
}
