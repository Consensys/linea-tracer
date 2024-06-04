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

package net.consensys.linea.zktracer.module.hub;

import java.util.ArrayList;
import java.util.List;

import net.consensys.linea.zktracer.container.StackedContainer;
import net.consensys.linea.zktracer.module.hub.transients.Block;
import net.consensys.linea.zktracer.types.TransactionProcessingMetadata;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class TransactionStack implements StackedContainer {
  private final List<TransactionProcessingMetadata> txs = new ArrayList<>(200);
  private int currentAbsNumber;
  private int relativeTransactionNumber;

  public TransactionProcessingMetadata current() {
    return this.txs.get(this.txs.size() - 1);
  }

  public TransactionProcessingMetadata getByAbsoluteTransactionNumber(final int id) {
    return this.txs.get(id - 1);
  }

  @Override
  public void enter() {
    this.currentAbsNumber += 1;
    this.relativeTransactionNumber += 1;
  }

  @Override
  public void pop() {
    this.txs.remove(this.current());
    this.currentAbsNumber -= 1;
    this.relativeTransactionNumber -= 1;
  }

  public void resetBlock() {
    this.relativeTransactionNumber = 0;
  }

  public void enterTransaction(final WorldView world, final Transaction tx, Block block) {
    this.enter();

    final TransactionProcessingMetadata newTx =
        new TransactionProcessingMetadata(
            world, tx, block, relativeTransactionNumber, currentAbsNumber);
    this.txs.add(newTx);
  }

  public void exitTransaction(final Hub hub, boolean isSuccessful) {
    if (hub.state.processingPhase != HubProcessingPhase.TX_SKIP) {
      hub.state.setProcessingPhase(HubProcessingPhase.TX_FINAL);
    }
    this.current().setHubStampTransactionEnd(hub.stamp() + 1);
  }

  public void setCodeFragmentIndex(Hub hub) {
    for (TransactionProcessingMetadata tx : this.txs) {
      final int cfi =
          tx.requiresCfiUpdate() ? hub.getCfiByMetaData(tx.getEffectiveTo(), 1, true) : 0;
      tx.setCodeFragmentIndex(cfi);
    }
  }
}
