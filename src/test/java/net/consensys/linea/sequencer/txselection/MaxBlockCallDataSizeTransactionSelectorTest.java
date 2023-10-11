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
package net.consensys.linea.sequencer.txselection;

import static net.consensys.linea.sequencer.txselection.selectors.MaxBlockCallDataTransactionSelector.CALL_DATA_TOO_BIG_INVALID_REASON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import net.consensys.linea.sequencer.LineaConfiguration;
import net.consensys.linea.sequencer.txselection.selectors.MaxBlockCallDataTransactionSelector;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.PendingTransaction;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.plugin.data.TransactionSelectionResult;
import org.hyperledger.besu.plugin.services.txselection.TransactionSelector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MaxBlockCallDataSizeTransactionSelectorTest {
  private static final int BLOCK_CALL_DATA_MAX_SIZE = 100;
  private static final int BLOCK_CALL_DATA_HALF_SIZE = 50;
  private static final int MAX_TX_CALL_DATA_SIZE = 60;
  private TransactionSelector transactionSelector;

  @BeforeEach
  public void initialize() {
    LineaConfiguration configuration =
        new LineaConfiguration.Builder()
            .maxTxCallDataSize(MAX_TX_CALL_DATA_SIZE)
            .maxBlockCallDataSize(BLOCK_CALL_DATA_MAX_SIZE)
            .build();
    transactionSelector = new MaxBlockCallDataTransactionSelector(configuration);
  }

  @Test
  public void shouldSelectTransactionWhen_CallDataSize_IsLessThan_MaxTxCallDataSize() {
    var mockTransaction = mockTransactionOfCallDataSize(MAX_TX_CALL_DATA_SIZE - 1);
    verifyTransactionSelection(
        transactionSelector, mockTransaction, TransactionSelectionResult.SELECTED);
  }

  @Test
  public void shouldSelectTransactionWhen_CallDataSize_IsEqualTo_MaxTxCallDataSize() {
    var mockTransaction = mockTransactionOfCallDataSize(MAX_TX_CALL_DATA_SIZE);
    verifyTransactionSelection(
        transactionSelector, mockTransaction, TransactionSelectionResult.SELECTED);
  }

  @Test
  public void shouldNotSelectTransactionWhen_CallDataSize_IsGreaterThan_MaxTxCallDataSize() {
    var mockTransaction = mockTransactionOfCallDataSize(MAX_TX_CALL_DATA_SIZE + 1);
    verifyTransactionSelection(
        transactionSelector,
        mockTransaction,
        TransactionSelectionResult.invalid(CALL_DATA_TOO_BIG_INVALID_REASON));
  }

  @Test
  public void shouldSelectTransactionWhen_BlockCallDataSize_IsLessThan_MaxBlockCallDataSize() {
    var mockTransaction = mockTransactionOfCallDataSize(BLOCK_CALL_DATA_HALF_SIZE);
    var mockTransaction2 = mockTransactionOfCallDataSize(BLOCK_CALL_DATA_HALF_SIZE - 1);
    verifyTransactionSelection(
        transactionSelector, mockTransaction, TransactionSelectionResult.SELECTED);
    verifyTransactionSelection(
        transactionSelector, mockTransaction2, TransactionSelectionResult.SELECTED);
  }

  @Test
  public void shouldSelectTransactionWhen_BlockCallDataSize_IsEqualTo_MaxBlockCallDataSize() {
    var mockTransaction = mockTransactionOfCallDataSize(BLOCK_CALL_DATA_HALF_SIZE);
    var mockTransaction2 = mockTransactionOfCallDataSize(BLOCK_CALL_DATA_HALF_SIZE);
    verifyTransactionSelection(
        transactionSelector, mockTransaction, TransactionSelectionResult.SELECTED);
    verifyTransactionSelection(
        transactionSelector, mockTransaction2, TransactionSelectionResult.SELECTED);
  }

  @Test
  public void
      shouldNotSelectTransactionWhen_BlockCallDataSize_IsGreaterThan_MaxBlockCallDataSize() {
    var mockTransaction = mockTransactionOfCallDataSize(BLOCK_CALL_DATA_HALF_SIZE);
    var mockTransaction2 = mockTransactionOfCallDataSize(BLOCK_CALL_DATA_HALF_SIZE + 1);
    verifyTransactionSelection(
        transactionSelector, mockTransaction, TransactionSelectionResult.SELECTED);
    verifyTransactionSelection(
        transactionSelector, mockTransaction2, TransactionSelectionResult.BLOCK_FULL);
  }

  @Test
  public void shouldNotSelectAdditionalTransactionOnceBlockIsFull() {
    var firstTransaction = mockTransactionOfCallDataSize(MAX_TX_CALL_DATA_SIZE);
    var secondTransaction = mockTransactionOfCallDataSize(MAX_TX_CALL_DATA_SIZE);
    var thirdTransaction = mockTransactionOfCallDataSize(MAX_TX_CALL_DATA_SIZE);

    verifyTransactionSelection(
        transactionSelector, firstTransaction, TransactionSelectionResult.SELECTED);
    verifyTransactionSelection(
        transactionSelector, secondTransaction, TransactionSelectionResult.BLOCK_FULL);
    verifyTransactionSelection(
        transactionSelector, thirdTransaction, TransactionSelectionResult.BLOCK_FULL);
  }

  private void verifyTransactionSelection(
      final TransactionSelector selector,
      final PendingTransaction transaction,
      final TransactionSelectionResult expectedSelectionResult) {
    var selectionResult = selector.evaluateTransactionPreProcessing(transaction);
    assertThat(selectionResult).isEqualTo(expectedSelectionResult);
    notifySelector(selector, transaction, selectionResult);
  }

  private void notifySelector(
      final TransactionSelector selector,
      final PendingTransaction transaction,
      final TransactionSelectionResult selectionResult) {
    if (selectionResult.equals(TransactionSelectionResult.SELECTED)) {
      selector.onTransactionSelected(transaction);
    } else {
      selector.onTransactionNotSelected(transaction, selectionResult);
    }
  }

  private PendingTransaction mockTransactionOfCallDataSize(final int size) {
    PendingTransaction mockTransaction = mock(PendingTransaction.class);
    Transaction transaction = mock(Transaction.class);
    when(mockTransaction.getTransaction()).thenReturn(transaction);
    when(transaction.getPayload()).thenReturn(Bytes.repeat((byte) 1, size));
    return mockTransaction;
  }
}
