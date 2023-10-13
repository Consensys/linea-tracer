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

import static net.consensys.linea.sequencer.txselection.TransactionUtil.EMPTY_L1_BLOCK_SIZE;
import static net.consensys.linea.sequencer.txselection.TransactionUtil.calculateL1TransactionSize;

import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.sequencer.LineaConfiguration;
import org.hyperledger.besu.datatypes.PendingTransaction;
import org.hyperledger.besu.plugin.data.TransactionProcessingResult;
import org.hyperledger.besu.plugin.data.TransactionSelectionResult;
import org.hyperledger.besu.plugin.services.txselection.PluginTransactionSelector;

@Slf4j
public class MaxBlockSizeTransactionSelector implements PluginTransactionSelector {
  private int totalBlockSize = EMPTY_L1_BLOCK_SIZE;
  private final int maxBlockSize;

  public MaxBlockSizeTransactionSelector(LineaConfiguration lineaConfiguration) {
    this.maxBlockSize = lineaConfiguration.maxBlockSize();
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
    if (isTransactionExceedingL1BlockSizeLimit(pendingTransaction, processingResult)) {
      return TransactionSelectionResult.BLOCK_FULL;
    }
    return TransactionSelectionResult.SELECTED;
  }

  /**
   * Checks if the total size of all transactions in a block would exceed the maximum allowed size
   * if the given transaction were added.
   *
   * @param transaction the transaction.
   * @param processingResult the transaction processing result.
   * @return true if the total call data size would be too big, false otherwise.
   */
  private boolean isTransactionExceedingL1BlockSizeLimit(
      final PendingTransaction transaction, final TransactionProcessingResult processingResult) {
    try {
      int L1Size = calculateL1TransactionSize(transaction, processingResult.getLogs());
      return Math.addExact(totalBlockSize, L1Size) > maxBlockSize;
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
    final int transactionL1Size = calculateL1TransactionSize(pendingTransaction, result.getLogs());
    totalBlockSize = Math.addExact(totalBlockSize, transactionL1Size);
  }
}
