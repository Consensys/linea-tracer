/*
 * Copyright ConsenSys AG.
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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.besu.datatypes.PendingTransaction;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.plugin.data.TransactionSelectionResult;

/**
 * This class extends PreProcessingTransactionSelector and provides a specific implementation for
 * evaluating transactions based on the size of the call data. It checks if the call data size of a
 * transaction or the total call data size of all transactions in a block exceeds the maximum
 * allowed size.
 */
@Slf4j
@RequiredArgsConstructor
public class MaxBlockCallDataTransactionSelector extends PreProcessingTransactionSelector {

  private final int maxBlockCallDataSize;
  private int blockCallDataSize;

  /**
   * Evaluates a transaction before processing. Checks if the call data size of the transaction or
   * the total call data size of all transactions in a block exceeds the maximum allowed size.
   *
   * @param pendingTransaction The transaction to evaluate.
   * @return INVALID if the call data size of the transaction is too big, BLOCK_FULL if the total
   *     call data size of all transactions in a block is too big, otherwise SELECTED.
   */
  @Override
  public TransactionSelectionResult evaluateTransactionPreProcessing(
      final PendingTransaction pendingTransaction) {

    final Transaction transaction = pendingTransaction.getTransaction();
    final int transactionCallDataSize = transaction.getPayload().size();

    if (isTransactionExceedingBlockCallDataSizeLimit(transactionCallDataSize)) {
      log.trace(
          "BlockCallData {} greater than {}, completing operation",
          transactionCallDataSize,
          maxBlockCallDataSize);
      return TransactionSelectionResult.BLOCK_FULL;
    }
    return TransactionSelectionResult.SELECTED;
  }

  /**
   * Checks if the total call data size of all transactions in a block would exceed the maximum
   * allowed size if the given transaction were added.
   *
   * @param transactionCallDataSize The call data size of the transaction.
   * @return true if the total call data size would be too big, false otherwise.
   */
  private boolean isTransactionExceedingBlockCallDataSizeLimit(int transactionCallDataSize) {
    try {
      return Math.addExact(blockCallDataSize, transactionCallDataSize) > maxBlockCallDataSize;
    } catch (final ArithmeticException e) {
      // Overflow won't occur as blockCallDataSize won't exceed Integer.MAX_VALUE
      return true;
    }
  }

  /**
   * Updates the total call data size of all transactions in a block when a transaction is selected.
   *
   * @param pendingTransaction The selected transaction.
   */
  @Override
  public void onTransactionSelected(PendingTransaction pendingTransaction) {
    final int transactionCallDataSize = pendingTransaction.getTransaction().getPayload().size();
    blockCallDataSize = Math.addExact(blockCallDataSize, transactionCallDataSize);
  }
}
