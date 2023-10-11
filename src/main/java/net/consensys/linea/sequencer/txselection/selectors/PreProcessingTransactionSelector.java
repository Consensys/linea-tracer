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

import org.hyperledger.besu.datatypes.PendingTransaction;
import org.hyperledger.besu.plugin.data.TransactionProcessingResult;
import org.hyperledger.besu.plugin.data.TransactionSelectionResult;
import org.hyperledger.besu.plugin.services.txselection.TransactionSelector;

/** Abstract class for transaction selection with a focus on pre-processing. */
public abstract class PreProcessingTransactionSelector implements TransactionSelector {

  /**
   * Implementations must define how to evaluate a transaction before processing.
   *
   * @param pendingTransaction The transaction to evaluate.
   * @return Evaluation result.
   */
  @Override
  public abstract TransactionSelectionResult evaluateTransactionPreProcessing(
      final PendingTransaction pendingTransaction);

  /**
   * After processing, the transaction is always selected. No need for further implementation.
   *
   * @param pendingTransaction The processed transaction.
   * @param processingResult The result of processing.
   * @return Always SELECTED.
   */
  @Override
  public TransactionSelectionResult evaluateTransactionPostProcessing(
      final PendingTransaction pendingTransaction,
      final TransactionProcessingResult processingResult) {
    return TransactionSelectionResult.SELECTED;
  }
}
