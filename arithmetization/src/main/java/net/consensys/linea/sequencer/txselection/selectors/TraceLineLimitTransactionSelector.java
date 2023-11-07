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

import java.util.Map;
import java.util.function.Supplier;

import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.zktracer.ZkTracer;
import org.hyperledger.besu.datatypes.PendingTransaction;
import org.hyperledger.besu.plugin.data.TransactionProcessingResult;
import org.hyperledger.besu.plugin.data.TransactionSelectionResult;
import org.hyperledger.besu.plugin.services.tracer.BlockAwareOperationTracer;
import org.hyperledger.besu.plugin.services.txselection.PluginTransactionSelector;

/**
 * This class implements TransactionSelector and provides a specific implementation for evaluating
 * transactions based on the size of the call data. It checks if adding a transaction to the block
 * pushes the call data size of the block over the limit.
 */
@Slf4j
public class TraceLineLimitTransactionSelector implements PluginTransactionSelector {

  private final Supplier<Map<String, Integer>> moduleLimitsProvider;
  private ZkTracer zkTracer;

  public TraceLineLimitTransactionSelector(
      final Supplier<Map<String, Integer>> moduleLimitsProvider) {
    this.moduleLimitsProvider = moduleLimitsProvider;
    zkTracer = new ZkTracer();
    zkTracer.traceStartConflation(1L);
  }

  /**
   * Evaluates a transaction before processing. Checks if adding the transaction to the block pushes
   * the call data size of the block over the limit.
   *
   * @param pendingTransaction The transaction to evaluate.
   * @return BLOCK_FULL if the call data size of a transactions pushes the size for the block over
   *     the limit, otherwise SELECTED.
   */
  @Override
  public TransactionSelectionResult evaluateTransactionPreProcessing(
      final PendingTransaction pendingTransaction) {
    return TransactionSelectionResult.SELECTED;
  }

  @Override
  public void onTransactionNotSelected(
      final PendingTransaction pendingTransaction,
      final TransactionSelectionResult transactionSelectionResult) {
    zkTracer.popTransaction(pendingTransaction);
  }

  /**
   * No evaluation is performed post-processing.
   *
   * @param pendingTransaction The processed transaction.
   * @param processingResult The result of the transaction processing.
   * @return Always returns SELECTED.
   */
  @Override
  public TransactionSelectionResult evaluateTransactionPostProcessing(
      final PendingTransaction pendingTransaction,
      final TransactionProcessingResult processingResult) {
    final Map<String, Integer> moduleLimits = moduleLimitsProvider.get();
    // check that we are not exceed line number for any module
    final Map<String, Integer> lineCounts = zkTracer.getModulesLineCount();
    for (var e : lineCounts.entrySet()) {
      final String module = e.getKey();
      if (!moduleLimits.containsKey(module)) {
        throw new RuntimeException("Module " + module + " does not exist in the limits file");
      }
      if (lineCounts.get(module) > moduleLimits.get(module)) {
        return TransactionSelectionResult.BLOCK_FULL;
      }
    }
    return TransactionSelectionResult.SELECTED;
  }

  @Override
  public BlockAwareOperationTracer getOperationTracer() {
    return zkTracer;
  }
}
