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
package net.consensys.linea.sequencer.txselection.selectors;

import static net.consensys.linea.sequencer.txselection.LineaTransactionSelectionResult.BLOCK_BYTES_SIZE_OVERFLOW;

import java.util.List;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.datatypes.PendingTransaction;
import org.hyperledger.besu.evm.log.Log;
import org.hyperledger.besu.plugin.data.TransactionProcessingResult;
import org.hyperledger.besu.plugin.data.TransactionSelectionResult;
import org.hyperledger.besu.plugin.services.txselection.PluginTransactionSelector;

@Getter
@Slf4j
public class MaxBlockSizeTransactionSelector implements PluginTransactionSelector {

  public static final int AVERAGE_TX_RLP_SIZE = 400;
  public static final int FROM_ADDRESS_SIZE = 20;
  public static final int EMPTY_L1_BLOCK_SIZE = 64 + 63 + 64 + 64;
  public static final int LOG_SIZE = 32;

  private int cumulativeBlockBytesSize = EMPTY_L1_BLOCK_SIZE;
  private final int maxBytesPerBlock;

  static final Cache<Hash, Integer> cache = CacheBuilder.newBuilder().maximumSize(1000).build();

  public MaxBlockSizeTransactionSelector(int maxBytesPerBlock) {
    this.maxBytesPerBlock = maxBytesPerBlock;
  }

  @Override
  public TransactionSelectionResult evaluateTransactionPreProcessing(
      PendingTransaction pendingTransaction) {
    return TransactionSelectionResult.SELECTED;
  }

  @Override
  public TransactionSelectionResult evaluateTransactionPostProcessing(
      final PendingTransaction pendingTransaction,
      final TransactionProcessingResult processingResult) {
    int transactionL1Size =
        computeL1TransactionSize(pendingTransaction, processingResult.getLogs());
    if (isTransactionExceedingL1BlockSizeLimit(transactionL1Size)) {
      log.atTrace()
          .setMessage(
              "tx {} size {} would cause block (cumulative size {}) to exceed max {} bytes, skipping tx")
          .addArgument(pendingTransaction.getTransaction()::getHash)
          .addArgument(() -> transactionL1Size)
          .addArgument(() -> cumulativeBlockBytesSize)
          .addArgument(maxBytesPerBlock)
          .log();
      return BLOCK_BYTES_SIZE_OVERFLOW;
    }
    return TransactionSelectionResult.SELECTED;
  }

  /**
   * Checks if the total size of all transactions in a block would exceed the maximum allowed size
   * if the given transaction were added.
   *
   * @param transactionL1Size the L1 Transaction Size in Bytes.
   * @return true if the total call data size would be too big, false otherwise.
   */
  private boolean isTransactionExceedingL1BlockSizeLimit(final int transactionL1Size) {
    try {
      return Math.addExact(cumulativeBlockBytesSize, transactionL1Size) > maxBytesPerBlock;
    } catch (final ArithmeticException e) {
      // Overflow won't occur as totalBlockSize won't exceed Integer.MAX_VALUE
      return true;
    }
  }

  /**
   * Updates the total size when a transaction is selected.
   *
   * @param pendingTransaction The selected transaction.
   */
  @Override
  public void onTransactionSelected(
      final PendingTransaction pendingTransaction, final TransactionProcessingResult result) {
    final int transactionL1Size = computeL1TransactionSize(pendingTransaction, result.getLogs());
    cumulativeBlockBytesSize = Math.addExact(cumulativeBlockBytesSize, transactionL1Size);
  }

  public int computeL1TransactionSize(final PendingTransaction tx, final List<Log> logs) {
    Hash transactionHash = tx.getTransaction().getHash();
    Integer transactionSize = cache.getIfPresent(transactionHash);
    if (transactionSize == null) {
      transactionSize = calculateTransactionSize(tx, logs);
      cache.put(transactionHash, transactionSize);
    }
    return transactionSize;
  }

  @VisibleForTesting
  public int calculateTransactionSize(final PendingTransaction tx, final List<Log> logs) {
    // TODO Replace with transaction actual size
    return AVERAGE_TX_RLP_SIZE + FROM_ADDRESS_SIZE + computeLogsSize(logs);
  }

  private static int computeLogsSize(final List<Log> logs) {
    return logs.size() * LOG_SIZE;
  }

  @Override
  public void onTransactionNotSelected(
      final PendingTransaction pendingTransaction,
      final TransactionSelectionResult transactionSelectionResult) {
    cache.invalidate(pendingTransaction.getTransaction().getHash());
  }
}
