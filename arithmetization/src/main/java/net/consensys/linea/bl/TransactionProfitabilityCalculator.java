package net.consensys.linea.bl;

import java.math.BigDecimal;

import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.config.LineaTransactionSelectorConfiguration;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.datatypes.Wei;
import org.slf4j.spi.LoggingEventBuilder;

@Slf4j
public class TransactionProfitabilityCalculator {

  private final LineaTransactionSelectorConfiguration conf;
  private final double preComputedValue;

  public TransactionProfitabilityCalculator(final LineaTransactionSelectorConfiguration conf) {
    this.conf = conf;
    this.preComputedValue =
        conf.getEstimateGasMinMargin() * conf.getGasPriceRatio() * conf.getVerificationGasCost();
  }

  public Wei profitablePriorityFeePerGas(
      final Transaction transaction, final Wei minGasPrice, final long gas) {
    final double txSize = Math.max(0, transaction.getSize() + conf.getAdjustTxSize());
    final double compressedSerializedSize = txSize / conf.getTxCompressionRatio();

    final var profitAt =
        preComputedValue
            * txSize
            * minGasPrice.getAsBigInteger().doubleValue()
            / (gas * conf.getVerificationCapacity());

    final var profitAtWei = Wei.ofNumber(BigDecimal.valueOf(profitAt).toBigInteger());

    log.atDebug()
        .setMessage(
            "Estimated profitable priorityFeePerGas: {}; estimateGasMinMargin={}, gasPriceRatio={}, "
                + "verificationGasCost={}, txSize={}, compressedTxSize={}, minGasPrice={}, gas={}, verificationCapacity={}")
        .addArgument(profitAtWei::toHumanReadableString)
        .addArgument(conf.getEstimateGasMinMargin())
        .addArgument(conf.getGasPriceRatio())
        .addArgument(conf.getVerificationGasCost())
        .addArgument(txSize)
        .addArgument(compressedSerializedSize)
        .addArgument(minGasPrice::toHumanReadableString)
        .addArgument(gas)
        .addArgument(conf.getVerificationCapacity())
        .log();

    return Wei.ofNumber(BigDecimal.valueOf(profitAt).toBigInteger());
  }

  public boolean isProfitable(
      final String step,
      final Transaction transaction,
      final double minGasPrice,
      final double effectiveGasPrice,
      final long gas) {
    final double revenue = effectiveGasPrice * gas;

    final double l1GasPrice = minGasPrice * conf.getGasPriceRatio();
    final int serializedSize = Math.max(0, transaction.getSize() + conf.getAdjustTxSize());
    final double verificationGasCostSlice =
        (((double) serializedSize) / conf.getVerificationCapacity())
            * conf.getVerificationGasCost();
    final double cost = l1GasPrice * verificationGasCostSlice;

    final double margin = revenue / cost;

    if (margin < conf.getMinMargin()) {
      log(
          log.atDebug(),
          step,
          transaction,
          margin,
          effectiveGasPrice,
          gas,
          minGasPrice,
          l1GasPrice,
          serializedSize,
          conf.getAdjustTxSize());
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
          serializedSize,
          conf.getAdjustTxSize());
      return true;
    }
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
      final int serializedSize,
      final int adjustTxSize) {
    leb.setMessage(
            "Context {}. Transaction {} has a margin of {}, minMargin={}, verificationCapacity={}, "
                + "verificationGasCost={}, gasPriceRatio={}, effectiveGasPrice={}, gasUsed={}, minGasPrice={}, "
                + "l1GasPrice={}, serializedSize={}, adjustTxSize={}")
        .addArgument(context)
        .addArgument(transaction::getHash)
        .addArgument(margin)
        .addArgument(conf.getMinMargin())
        .addArgument(conf.getVerificationCapacity())
        .addArgument(conf.getVerificationGasCost())
        .addArgument(conf.getGasPriceRatio())
        .addArgument(effectiveGasPrice)
        .addArgument(gasUsed)
        .addArgument(minGasPrice)
        .addArgument(l1GasPrice)
        .addArgument(serializedSize)
        .addArgument(adjustTxSize)
        .log();
  }
}
