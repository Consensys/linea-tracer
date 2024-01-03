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
package net.consensys.linea.sequencer.txselection;

import static net.consensys.linea.sequencer.txselection.LineaTransactionSelectionResult.*;
import static net.consensys.linea.sequencer.txselection.selectors.MaxBlockSizeTransactionSelector.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hyperledger.besu.plugin.data.TransactionSelectionResult.SELECTED;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import net.consensys.linea.sequencer.txselection.selectors.MaxBlockSizeTransactionSelector;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.datatypes.PendingTransaction;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.log.Log;
import org.hyperledger.besu.plugin.data.TransactionProcessingResult;
import org.hyperledger.besu.plugin.data.TransactionSelectionResult;
import org.hyperledger.besu.plugin.services.txselection.PluginTransactionSelector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MaxBlockBytesSizeTransactionSelectorTest {
  private MaxBlockSizeTransactionSelector txSelector;

  @BeforeEach
  public void initialize() {
    int transactionCount = 1;
    int logsCount = 2;
    int maxBytesPerBlock =
        EMPTY_L1_BLOCK_SIZE
            + (transactionCount * (FROM_ADDRESS_SIZE + AVERAGE_TX_RLP_SIZE))
            + (logsCount * LOG_SIZE);
    txSelector = spy(new MaxBlockSizeTransactionSelector(maxBytesPerBlock));
  }

  @Test
  public void shouldSelectWhen_BlockBytesSize_IsLessThan_MaxBytesPerBlock() {
    var mockTransactionProcessingResult = mockTransactionProcessingResultWithLogs(1);
    verifyTransactionSelection(
        txSelector, mockTransaction(), mockTransactionProcessingResult, SELECTED);
    assertThat(txSelector.getCumulativeBlockBytesSize())
        .isLessThan(txSelector.getMaxBytesPerBlock());
  }

  @Test
  public void shouldSelectWhen_BlockBytesSize_IsEqual_MaxBytesPerBlock() {
    var mockTransactionProcessingResult = mockTransactionProcessingResultWithLogs(2);
    verifyTransactionSelection(
        txSelector, mockTransaction(), mockTransactionProcessingResult, SELECTED);
    assertThat(txSelector.getCumulativeBlockBytesSize())
        .isEqualTo(txSelector.getMaxBytesPerBlock());
  }

  @Test
  public void shouldNotSelectWhen_BlockBytesSize_IsGreaterThan_MaxBytesPerBlock() {
    var mockTransactionProcessingResult = mockTransactionProcessingResultWithLogs(3);
    verifyTransactionSelection(
        txSelector, mockTransaction(), mockTransactionProcessingResult, BLOCK_BYTES_SIZE_OVERFLOW);
    assertThat(txSelector.getCumulativeBlockBytesSize())
        .isLessThan(txSelector.getMaxBytesPerBlock());
  }

  private void verifyTransactionSelection(
      final PluginTransactionSelector selector,
      final PendingTransaction transaction,
      final TransactionProcessingResult processingResult,
      final TransactionSelectionResult expectedSelectionResult) {
    var selectionResult = selector.evaluateTransactionPostProcessing(transaction, processingResult);
    assertThat(selectionResult).isEqualTo(expectedSelectionResult);
    notifySelector(txSelector, transaction, processingResult, selectionResult);
    verify(txSelector, times(1)).calculateTransactionSize(transaction, processingResult.getLogs());
  }

  private PendingTransaction mockTransaction() {
    PendingTransaction mockTransaction = mock(PendingTransaction.class);
    Transaction transaction = mock(Transaction.class);
    when(transaction.getHash()).thenReturn(Hash.hash(Bytes.random(32)));
    when(mockTransaction.getTransaction()).thenReturn(transaction);
    return mockTransaction;
  }

  private TransactionProcessingResult mockTransactionProcessingResultWithLogs(int logsCount) {
    TransactionProcessingResult mockTransactionProcessingResult =
        mock(TransactionProcessingResult.class);
    @SuppressWarnings("unchecked") // we are just interested in the size()
    List<Log> logs = mock(List.class);
    when(logs.size()).thenReturn(logsCount);
    when(mockTransactionProcessingResult.getLogs()).thenReturn(logs);
    return mockTransactionProcessingResult;
  }

  private void notifySelector(
      final PluginTransactionSelector selector,
      final PendingTransaction transaction,
      final TransactionProcessingResult transactionProcessingResult,
      final TransactionSelectionResult selectionResult) {
    if (selectionResult.equals(SELECTED)) {
      selector.onTransactionSelected(transaction, transactionProcessingResult);
    } else {
      selector.onTransactionNotSelected(transaction, selectionResult);
    }
  }
}
