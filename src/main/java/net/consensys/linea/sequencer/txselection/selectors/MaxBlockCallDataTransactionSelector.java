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

import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.sequencer.LineaConfiguration;
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
public class MaxBlockCallDataTransactionSelector extends PreProcessingTransactionSelector {
  private final int maxBlockCallDataSize;
  private final int maxTxCallDataSize;
  private int blockCallDataSize;
  public static String CALL_DATA_TOO_BIG_INVALID_REASON = "CallData too big";

  /**
   * Constructor that initializes the maximum allowed call data size for a transaction and a block.
   *
   * @param lineaConfiguration The configuration to use.
   */
  public MaxBlockCallDataTransactionSelector(LineaConfiguration lineaConfiguration) {
    this.maxBlockCallDataSize = lineaConfiguration.maxBlockCallDataSize();
    this.maxTxCallDataSize = lineaConfiguration.maxTxCallDataSize();
  }

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

    if (isTransactionCallDataSizeTooBig(transactionCallDataSize)) {
      logTransactionCallDataSizeTooBig(transaction, transactionCallDataSize);
      return TransactionSelectionResult.invalid(CALL_DATA_TOO_BIG_INVALID_REASON);
    }

    if (isBlockCallDataSizeTooBig(transactionCallDataSize, transaction)) {
      logBlockCallDataSizeTooBig(transaction, transactionCallDataSize);
      return TransactionSelectionResult.BLOCK_FULL;
    }

    return TransactionSelectionResult.SELECTED;
  }

  /**
   * Checks if the call data size of a transaction exceeds the maximum allowed size.
   *
   * @param transactionCallDataSize The call data size of the transaction.
   * @return true if the call data size is too big, false otherwise.
   */
  private boolean isTransactionCallDataSizeTooBig(int transactionCallDataSize) {
    return transactionCallDataSize > maxTxCallDataSize;
  }

  /**
   * Checks if the total call data size of all transactions in a block would exceed the maximum
   * allowed size if the given transaction were added.
   *
   * @param transactionCallDataSize The call data size of the transaction.
   * @param transaction The transaction to be added.
   * @return true if the total call data size would be too big, false otherwise.
   */
  private boolean isBlockCallDataSizeTooBig(int transactionCallDataSize, Transaction transaction) {
    try {
      return Math.addExact(blockCallDataSize, transactionCallDataSize) > maxBlockCallDataSize;
    } catch (final ArithmeticException e) {
      // this should never happen
      log.warn(
          "Not adding transaction {} otherwise blockCallDataSum {} overflows",
          transaction,
          blockCallDataSize);
      return true;
    }
  }

  /**
   * Logs a warning message indicating that the call data size of a transaction is too big.
   *
   * @param transaction The transaction with the too big call data size.
   * @param transactionCallDataSize The call data size of the transaction.
   */
  private void logTransactionCallDataSizeTooBig(
      Transaction transaction, int transactionCallDataSize) {
    log.warn(
        "Not adding transaction {} because callData size {} is too big",
        transaction,
        transactionCallDataSize);
  }

  /**
   * Logs a trace message indicating that the total call data size of all transactions in a block
   * would be too big if the given transaction were added.
   *
   * @param transaction The transaction to be added.
   * @param transactionCallDataSize The call data size of the transaction.
   */
  private void logBlockCallDataSizeTooBig(Transaction transaction, int transactionCallDataSize) {
    log.trace(
        "BlockCallData {} greater than {}, completing operation",
        transactionCallDataSize,
        maxBlockCallDataSize);
  }

  /**
   * Updates the total call data size of all transactions in a block when a transaction is selected.
   *
   * @param pendingTransaction The selected transaction.
   */
  @Override
  public void onTransactionSelected(PendingTransaction pendingTransaction) {
    blockCallDataSize =
        Math.addExact(blockCallDataSize, pendingTransaction.getTransaction().getPayload().size());
  }
}
