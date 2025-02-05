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

package net.consensys.linea.zktracer.module.hub.state;

import lombok.Getter;
import lombok.Setter;
import net.consensys.linea.zktracer.container.stacked.StackedList;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.section.TxInitializationSection;
import net.consensys.linea.zktracer.module.hub.transients.Block;
import net.consensys.linea.zktracer.types.TransactionProcessingMetadata;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.worldstate.WorldView;

@Getter
public class TransactionStack {
  private final StackedList<TransactionProcessingMetadata> transactions =
      new StackedList<>(); // TODO: write the allocated memory from .toml file
  private int currentAbsNumber;
  private int relativeTransactionNumber;
  @Setter @Getter public TxInitializationSection initializationSection;

  public TransactionProcessingMetadata current() {
    return transactions.getLast();
  }

  /* WARN: can't be called if currentAbsNumber == 1*/
  public TransactionProcessingMetadata previous() {
    return transactions.get(transactions.size() - 2);
  }

  public TransactionProcessingMetadata getByAbsoluteTransactionNumber(final int id) {
    return transactions.get(id - 1);
  }

  public void commitTransactions() {
    transactions.commitTransactions();
  }

  public void popTransactions() {
    final int numberOfTransactionToPop = transactions.operationsInTransactionBundle().size();
    transactions.popTransactions();
    this.currentAbsNumber -= numberOfTransactionToPop;
    this.relativeTransactionNumber -= numberOfTransactionToPop;
  }

  public void resetBlock() {
    this.relativeTransactionNumber = 0;
  }

  public void enterTransaction(final WorldView world, final Transaction tx, Block block) {
    currentAbsNumber += 1;
    relativeTransactionNumber += 1;

    final TransactionProcessingMetadata newTx =
        new TransactionProcessingMetadata(
            world, tx, block, relativeTransactionNumber, currentAbsNumber);

    transactions.add(newTx);
  }

  public void setCodeFragmentIndex(Hub hub) {
    for (TransactionProcessingMetadata tx : transactions.getAll()) {
      final int cfi =
          tx.requiresCfiUpdate()
              ? hub.getCfiByMetaData(
                  tx.getEffectiveRecipient(),
                  tx.getUpdatedRecipientAddressDeploymentNumberAtTransactionStart(),
                  tx.isUpdatedRecipientAddressDeploymentStatusAtTransactionStart())
              : 0;
      tx.setCodeFragmentIndex(cfi);
    }
  }

  public long getAccumulativeGasUsedInBlockBeforeTxStart() {
    return this.relativeTransactionNumber == 1 ? 0 : this.previous().getAccumulatedGasUsedInBlock();
  }
}
