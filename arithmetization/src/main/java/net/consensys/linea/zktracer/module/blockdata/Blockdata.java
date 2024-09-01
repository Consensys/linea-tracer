/*
 * Copyright ConsenSys Inc.
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

package net.consensys.linea.zktracer.module.blockdata;

import static net.consensys.linea.zktracer.module.blockdata.Trace.MAX_CT;
import static net.consensys.linea.zktracer.types.TransactionUtils.getChainIdFromTransaction;

import java.nio.MappedByteBuffer;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.container.module.Module;
import net.consensys.linea.zktracer.module.rlptxn.RlpTxn;
import net.consensys.linea.zktracer.module.txndata.TxnData;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import org.hyperledger.besu.evm.worldstate.WorldView;
import org.hyperledger.besu.plugin.data.BlockBody;
import org.hyperledger.besu.plugin.data.BlockHeader;
import org.hyperledger.besu.plugin.data.ProcessableBlockHeader;

@RequiredArgsConstructor
public class Blockdata implements Module {
  private final Wcp wcp;
  private final TxnData txnData;
  private final RlpTxn rlpTxn;
  private final Deque<BlockdataOperation> operations = new ArrayDeque<>();
  private boolean conflationFinished = false;
  private final int TIMESTAMP_BYTESIZE = 4;
  private int previousTimestamp = 0;

  @Override
  public String moduleKey() {
    return "BLOCK_DATA";
  }

  @Override
  public void traceStartBlock(final ProcessableBlockHeader processableBlockHeader) {
    wcp.additionalRows.add(TIMESTAMP_BYTESIZE);
  }

  @Override
  public void traceEndBlock(final BlockHeader blockHeader, final BlockBody blockBody) {
    final int currentTimestamp = (int) blockHeader.getTimestamp();
    operations.addLast(
        new BlockdataOperation(
            blockHeader.getCoinbase(),
            currentTimestamp,
            blockHeader.getNumber(),
            blockHeader.getDifficulty().getAsBigInteger(),
            this.txnData.currentBlock().getNbOfTxsInBlock()));

    wcp.callGT(currentTimestamp, previousTimestamp);
    wcp.additionalRows.remove(TIMESTAMP_BYTESIZE); // Remove what have been done at traceStartBlock
    previousTimestamp = currentTimestamp;
  }

  @Override
  public void traceEndConflation(final WorldView state) {
    conflationFinished = true;
  }

  @Override
  public void enterTransaction() {}

  @Override
  public void popTransaction() {}

  @Override
  public int lineCount() {
    final int numberOfBlock = conflationFinished ? operations.size() : operations.size() + 1;
    return numberOfBlock * (MAX_CT + 1);
  }

  @Override
  public List<ColumnHeader> columnsHeaders() {
    return Trace.headers(this.lineCount());
  }

  @Override
  public void commit(List<MappedByteBuffer> buffers) {
    final long firstBlockNumber = operations.getFirst().absoluteBlockNumber();
    final long chainId = getChainIdFromTransaction(rlpTxn.operations().getLast().tx());
    final Trace trace = new Trace(buffers);
    int relblock = 0;
    for (BlockdataOperation blockData : this.operations) {
      relblock += 1;
      blockData.trace(trace, relblock, firstBlockNumber, chainId);
    }
  }
}
