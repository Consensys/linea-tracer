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

package net.consensys.linea.zktracer.module.hub.defer;

import java.util.ArrayList;
import java.util.List;

import net.consensys.linea.zktracer.module.hub.Bytecode;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.chunks.AccountChunk;
import net.consensys.linea.zktracer.module.hub.chunks.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.chunks.TraceChunk;
import net.consensys.linea.zktracer.module.hub.chunks.TransactionChunk;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.code.CodeV0;
import org.hyperledger.besu.evm.worldstate.WorldView;

/**
 * SkippedTransaction latches data at the pre-execution of the transaction data that will be used
 * later, through a {@link TransactionDefer}, to generate the trace chunks required for the proving
 * of a pure transaction.
 *
 * @param oldFromAccount
 * @param oldToAccount
 * @param oldMinerAccount
 */
public record SkippedTransaction(
    AccountSnapshot oldFromAccount, AccountSnapshot oldToAccount, AccountSnapshot oldMinerAccount)
    implements TransactionDefer {
  @Override
  public void run(Hub hub, WorldView state, Transaction tx) {
    List<TraceChunk> currentChunk = new ArrayList<>();

    Address fromAddress = this.oldFromAccount.address();
    Address toAddress = this.oldToAccount.address();
    Address minerAddress = this.oldMinerAccount.address();

    AccountSnapshot newFromAccount =
        AccountSnapshot.fromAccount(
            state.get(fromAddress),
            new Bytecode(CodeV0.EMPTY_CODE), // From can't have code
            true,
            hub.deploymentNumber.getOrDefault(fromAddress, 0),
            false);

    AccountSnapshot newToAccount =
        AccountSnapshot.fromAccount(
            state.get(fromAddress),
            new Bytecode(state.get(fromAddress).getCode()),
            true,
            hub.deploymentNumber.getOrDefault(toAddress, 0),
            false);

    AccountSnapshot newMinerAccount =
        AccountSnapshot.fromAccount(
            state.get(minerAddress),
            new Bytecode(state.get(minerAddress).getCode()),
            true,
            hub.deploymentNumber.getOrDefault(minerAddress, 0),
            false);

    // 3 lines -- account changes
    // From
    currentChunk.add(
        new AccountChunk(fromAddress, oldFromAccount, newFromAccount, false, 0, false, 0, false));
    // To
    currentChunk.add(
        new AccountChunk(toAddress, oldToAccount, newToAccount, false, 0, false, 0, false));
    // Miner
    currentChunk.add(
        new AccountChunk(
            minerAddress, oldMinerAccount, newMinerAccount, false, 0, false, 0, false));
    // 1 line -- transaction data
    currentChunk.add(new TransactionChunk(hub.getBatchNumber(), minerAddress, tx, false));

    // Append the final chunk to the hub chunks
    hub.getTraceChunks().add(currentChunk);
  }
}
