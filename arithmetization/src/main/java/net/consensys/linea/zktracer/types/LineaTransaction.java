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
import net.consensys.linea.zktracer.module.txndata.TxnData;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.evm.worldstate.WorldView;

@Accessors(fluent = true)
@Getter
public class LineaTransaction {
  final int relativeBlockNumber;

  final Transaction besuTransaction;
  final int relativeTransactionNumber;
  final Address coinbase;
  final long baseFee;

  final boolean isDeployment;
  final boolean requiresEvmExecution;
  final boolean copyTransactionCallData;

  final BigInteger initialBalance;

  final long dataCost;
  final long accessListCost;
  final long initiallyAvailableGas;

  final Address effectiveTo;

  final long effectiveGasPrice;

  @Setter long refundCounterMax;
  @Setter long refundEffective;
  @Setter long leftoverGas;
  @Setter boolean statusCode;

  public LineaTransaction(
      WorldView world,
      int relativeBlockNumber,
      Address coinbase,
      Wei baseFee,
      TxnData txnData,
      Transaction transaction) {
    this.relativeBlockNumber = relativeBlockNumber;
    this.coinbase = coinbase;
    this.baseFee = baseFee.toLong();

    this.besuTransaction = transaction;
    this.relativeTransactionNumber = txnData.currentBlock().getTxs().size() + 1;

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

    this.effectiveGasPrice = getEffectiveGasPrice();
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

  public void completeLineaTransaction(
      boolean statusCode, long leftoverGas, long refundCounterMax) {
    this.refundCounterMax = refundCounterMax;
    this.leftoverGas = leftoverGas;
    this.statusCode = statusCode;
    this.refundEffective = getRefundEffective();
  }

  private long getRefundEffective() {
    final long consumedGas = besuTransaction.getGasLimit() - leftoverGas;
    final long maxRefundableAmount = consumedGas / MAX_REFUND_QUOTIENT;
    return Math.min(maxRefundableAmount, refundCounterMax);
  }

  private long getEffectiveGasPrice() {

    final Transaction tx = besuTransaction;
    switch (tx.getType()) {
      case FRONTIER, ACCESS_LIST -> {
        return tx.getGasPrice().get().getAsBigInteger().longValueExact();
      }
      case EIP1559 -> {
        final long baseFee = baseFee();
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
}
