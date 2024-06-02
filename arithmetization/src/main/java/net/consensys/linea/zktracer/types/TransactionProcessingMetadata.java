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
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.ZkTracer;
import net.consensys.linea.zktracer.module.hub.transients.Block;
import net.consensys.linea.zktracer.module.hub.transients.StorageInitialValues;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Transaction;
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

  @Accessors(fluent = true)
  final boolean requiresEvmExecution;

  @Accessors(fluent = true)
  final boolean copyTransactionCallData;

  final BigInteger initialBalance;

  final long dataCost;
  final long accessListCost;
  final long initiallyAvailableGas;

  final Address effectiveTo;

  final long effectiveGasPrice;

  @Setter long refundCounterMax = 0;
  @Setter long refundEffective = 0;
  @Setter long leftoverGas = 0;

  @Accessors(fluent = true)
  @Setter
  boolean statusCode = false;

  @Setter int hubStampTransactionEnd;

  @Accessors(fluent = true)
  @Setter
  boolean isSenderPreWarmed = false;

  @Accessors(fluent = true)
  @Setter
  boolean isReceiverPreWarmed = false;

  final StorageInitialValues storage = new StorageInitialValues();

  public TransactionProcessingMetadata(
      WorldView world,
      Transaction transaction,
      Block block,
      int relativeTransactionNumber,
      int absoluteTransactionNumber) {
    this.absoluteTransactionNumber = absoluteTransactionNumber;
    this.relativeBlockNumber = block.blockNumber();
    this.coinbase = block.minerAddress();
    this.baseFee = block.baseFee().toLong();

    this.besuTransaction = transaction;
    this.relativeTransactionNumber = relativeTransactionNumber;

    this.isDeployment = transaction.getTo().isEmpty();
    this.requiresEvmExecution = computeRequiresEvmExecution(world);
    this.copyTransactionCallData = computeCopyCallData();

    this.initialBalance = getInitialBalance(world);

    this.dataCost =
        ZkTracer.gasCalculator.transactionIntrinsicGasCost(
            besuTransaction.getPayload(), isDeployment);
    this.accessListCost =
        besuTransaction.getAccessList().map(ZkTracer.gasCalculator::accessListGasCost).orElse(0L);
    this.initiallyAvailableGas = getInitiallyAvailableGas();

    this.effectiveTo = effectiveToAddress(besuTransaction);

    this.effectiveGasPrice = computeEffectiveGasPrice();
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
    // Note: Besu's dataCost computation contains the 21_000 transaction cost
    return dataCost
        + (isDeployment ? GAS_CONST_G_CREATE : 0)
        + accessListCost;
  }

  public long getInitiallyAvailableGas() {
    return besuTransaction.getGasLimit() - getUpfrontGasCost();
  }

  public void completeLineaTransaction(
      boolean statusCode, long leftoverGas, long refundCounterMax, int hubStampTransactionEnd) {
    this.refundCounterMax = refundCounterMax;
    this.leftoverGas = leftoverGas;
    this.statusCode = statusCode;
    this.refundEffective = computeRefundEffective();
    this.hubStampTransactionEnd = hubStampTransactionEnd;
  }

  private long computeRefundEffective() {
    final long consumedGas = besuTransaction.getGasLimit() - leftoverGas;
    final long maxRefundableAmount = consumedGas / MAX_REFUND_QUOTIENT;
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
            tx.getMaxPriorityFeePerGas().get().getAsBigInteger().longValue();
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
}
