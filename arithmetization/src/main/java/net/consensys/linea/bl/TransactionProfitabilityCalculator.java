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
package net.consensys.linea.bl;

import java.math.BigDecimal;

import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.config.LineaProfitabilityConfiguration;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.datatypes.Wei;
import org.slf4j.spi.LoggingEventBuilder;

@Slf4j
public class TransactionProfitabilityCalculator {

  private final LineaProfitabilityConfiguration profitabilityConf;
  private final double preComputedValue;

  public TransactionProfitabilityCalculator(
      final LineaProfitabilityConfiguration profitabilityConf) {
    this.profitabilityConf = profitabilityConf;
    this.preComputedValue =
        profitabilityConf.gasPriceRatio() * profitabilityConf.verificationGasCost();
  }

  public Wei profitablePriorityFeePerGas(
      final Transaction transaction,
      final double minMargin,
      final Wei minGasPrice,
      final long gas) {
    final double compressedTxSize = getCompressedTxSize(transaction);

    final var profitAt =
        preComputedValue
            * minMargin
            * compressedTxSize
            * minGasPrice.getAsBigInteger().doubleValue()
            / (gas * profitabilityConf.verificationCapacity());

    final var profitAtWei = Wei.ofNumber(BigDecimal.valueOf(profitAt).toBigInteger());

    log.atDebug()
        .setMessage(
            "Estimated profitable priorityFeePerGas: {}; estimateGasMinMargin={}, verificationCapacity={}, "
                + "verificationGasCost={}, gasPriceRatio={}, gas={}, minGasPrice={}, "
                + "l1GasPrice={}, txSize={}, compressedTxSize={}, adjustTxSize={}")
        .addArgument(profitAtWei::toHumanReadableString)
        .addArgument(profitabilityConf.estimateGasMinMargin())
        .addArgument(profitabilityConf.verificationCapacity())
        .addArgument(profitabilityConf.verificationGasCost())
        .addArgument(profitabilityConf.gasPriceRatio())
        .addArgument(gas)
        .addArgument(minGasPrice::toHumanReadableString)
        .addArgument(
            () -> minGasPrice.multiply(profitabilityConf.gasPriceRatio()).toHumanReadableString())
        .addArgument(transaction::getSize)
        .addArgument(compressedTxSize)
        .addArgument(profitabilityConf.adjustTxSize())
        .log();

    return profitAtWei;
  }

  public boolean isProfitable(
      final String step,
      final Transaction transaction,
      final double minMargin,
      final double minGasPrice,
      final double effectiveGasPrice,
      final long gas) {
    final double revenue = effectiveGasPrice * gas;

    final double l1GasPrice = minGasPrice * profitabilityConf.gasPriceRatio();
    final double compressedTxSize = getCompressedTxSize(transaction);
    final double verificationGasCostSlice =
        (compressedTxSize / profitabilityConf.verificationCapacity())
            * profitabilityConf.verificationGasCost();
    final double cost = l1GasPrice * verificationGasCostSlice;

    final double margin = revenue / cost;

    if (margin < minMargin) {
      log(
          log.atDebug(),
          step,
          transaction,
          margin,
          effectiveGasPrice,
          gas,
          minGasPrice,
          l1GasPrice,
          compressedTxSize,
          profitabilityConf.adjustTxSize());
      return false;
    } else {
      log(
          log.atTrace(),
          step,
          transaction,
          margin,
          effectiveGasPrice,
          gas,
          minGasPrice,
          l1GasPrice,
          compressedTxSize,
          profitabilityConf.adjustTxSize());
      return true;
    }
  }

  private double getCompressedTxSize(final Transaction transaction) {
    // this is just a temporary estimation, that will be replaced by gnarkCompression when available
    // at that point conf.txCompressionRatio and conf.adjustTxSize options can be removed
    final double adjustedTxSize =
        Math.max(0, transaction.getSize() + profitabilityConf.adjustTxSize());
    return adjustedTxSize / profitabilityConf.txCompressionRatio();
  }

  private void log(
      final LoggingEventBuilder leb,
      final String context,
      final Transaction transaction,
      final double margin,
      final double effectiveGasPrice,
      final long gasUsed,
      final double minGasPrice,
      final double l1GasPrice,
      final double compressedTxSize,
      final int adjustTxSize) {
    leb.setMessage(
            "Context {}. Transaction {} has a margin of {}, minMargin={}, verificationCapacity={}, "
                + "verificationGasCost={}, gasPriceRatio={}, effectiveGasPrice={}, gasUsed={}, minGasPrice={}, "
                + "l1GasPrice={}, txSize={}, compressedTxSize={}, adjustTxSize={}")
        .addArgument(context)
        .addArgument(transaction::getHash)
        .addArgument(margin)
        .addArgument(profitabilityConf.minMargin())
        .addArgument(profitabilityConf.verificationCapacity())
        .addArgument(profitabilityConf.verificationGasCost())
        .addArgument(profitabilityConf.gasPriceRatio())
        .addArgument(effectiveGasPrice)
        .addArgument(gasUsed)
        .addArgument(minGasPrice)
        .addArgument(l1GasPrice)
        .addArgument(transaction::getSize)
        .addArgument(compressedTxSize)
        .addArgument(adjustTxSize)
        .log();
  }
}
