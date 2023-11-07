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

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.sequencer.LineaConfiguration;
import org.hyperledger.besu.datatypes.PendingTransaction;
import org.hyperledger.besu.plugin.data.TransactionProcessingResult;
import org.hyperledger.besu.plugin.data.TransactionSelectionResult;
import org.hyperledger.besu.plugin.services.tracer.BlockAwareOperationTracer;
import org.hyperledger.besu.plugin.services.txselection.PluginTransactionSelector;

/** Class for transaction selection using a list of selectors. */
@Slf4j
public class LineaTransactionSelector implements PluginTransactionSelector {

  private static TraceLineLimitTransactionSelector traceLineLimitTransactionSelector;
  private static Map<String, Integer> limitsMap;
  private static Supplier<Map<String, Integer>> limitsMapSupplier;
  final LineaConfiguration lineaConfiguration;
  List<PluginTransactionSelector> selectors;

  public LineaTransactionSelector(
      LineaConfiguration lineaConfiguration,
      final Supplier<Map<String, Integer>> limitsMapSupplier) {
    this.lineaConfiguration = lineaConfiguration;
    this.selectors = createTransactionSelectors(lineaConfiguration);
    LineaTransactionSelector.limitsMapSupplier = limitsMapSupplier;
  }

  /**
   * Creates a list of selectors based on Linea configuration.
   *
   * @param lineaConfiguration The configuration to use.
   * @return A list of selectors.
   */
  private static List<PluginTransactionSelector> createTransactionSelectors(
      final LineaConfiguration lineaConfiguration) {

    traceLineLimitTransactionSelector = new TraceLineLimitTransactionSelector(limitsMapSupplier);
    return List.of(
        new MaxTransactionCallDataTransactionSelector(lineaConfiguration.maxTxCallDataSize()),
        new MaxBlockCallDataTransactionSelector(lineaConfiguration.maxBlockCallDataSize()),
        traceLineLimitTransactionSelector);
  }

  /**
   * Evaluates a transaction before processing using all selectors. Stops if any selector doesn't
   * select the transaction.
   *
   * @param pendingTransaction The transaction to evaluate.
   * @return The first non-SELECTED result or SELECTED if all selectors select the transaction.
   */
  @Override
  public TransactionSelectionResult evaluateTransactionPreProcessing(
      PendingTransaction pendingTransaction) {
    return selectors.stream()
        .map(selector -> selector.evaluateTransactionPreProcessing(pendingTransaction))
        .filter(result -> !result.equals(TransactionSelectionResult.SELECTED))
        .findFirst()
        .orElse(TransactionSelectionResult.SELECTED);
  }

  /**
   * Evaluates a transaction considering its processing result. Stops if any selector doesn't select
   * the transaction.
   *
   * @param pendingTransaction The processed transaction.
   * @param processingResult The result of the transaction processing.
   * @return The first non-SELECTED result or SELECTED if all selectors select the transaction.
   */
  @Override
  public TransactionSelectionResult evaluateTransactionPostProcessing(
      PendingTransaction pendingTransaction, TransactionProcessingResult processingResult) {
    for (var selector : selectors) {
      TransactionSelectionResult result =
          selector.evaluateTransactionPostProcessing(pendingTransaction, processingResult);
      if (!result.equals(TransactionSelectionResult.SELECTED)) {
        return result;
      }
    }
    return TransactionSelectionResult.SELECTED;
  }

  /**
   * Notifies all selectors when a transaction is selected.
   *
   * @param pendingTransaction The selected transaction.
   * @param processingResult The transaction processing result.
   */
  @Override
  public void onTransactionSelected(
      final PendingTransaction pendingTransaction,
      final TransactionProcessingResult processingResult) {
    selectors.forEach(
        selector -> selector.onTransactionSelected(pendingTransaction, processingResult));
  }

  /**
   * Notifies all selectors when a transaction is not selected.
   *
   * @param pendingTransaction The non-selected transaction.
   * @param transactionSelectionResult The reason for not selecting the transaction.
   */
  @Override
  public void onTransactionNotSelected(
      final PendingTransaction pendingTransaction,
      final TransactionSelectionResult transactionSelectionResult) {
    selectors.forEach(
        selector ->
            selector.onTransactionNotSelected(pendingTransaction, transactionSelectionResult));
  }

  @Override
  public BlockAwareOperationTracer getOperationTracer() {
    return traceLineLimitTransactionSelector.getOperationTracer();
  }
}
