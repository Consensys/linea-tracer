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

import java.nio.MappedByteBuffer;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.txndata.TxnData;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import org.hyperledger.besu.plugin.data.BlockBody;
import org.hyperledger.besu.plugin.data.BlockHeader;
import org.hyperledger.besu.plugin.data.ProcessableBlockHeader;

public class Blockdata implements Module {
  private final Wcp wcp;
  private final TxnData txnData;
  private final Deque<BlockdataOperation> operation = new ArrayDeque<>();
  private boolean batchUnderConstruction;

  public Blockdata(Wcp wcp, TxnData txnData) {
    this.wcp = wcp;
    this.txnData = txnData;
    this.batchUnderConstruction = true;
  }

  @Override
  public String moduleKey() {
    return "BLOCKDATA";
  }

  @Override
  public void traceStartBlock(final ProcessableBlockHeader processableBlockHeader) {
    this.batchUnderConstruction = true;
  }

  @Override
  public void traceEndBlock(final BlockHeader blockHeader, final BlockBody blockBody) {
    this.batchUnderConstruction = false;
    this.operation.add(
        new BlockdataOperation(
            blockHeader.getCoinbase(),
            blockHeader.getTimestamp(),
            blockHeader.getNumber(),
            blockHeader.getDifficulty().getAsBigInteger()));
  }

  @Override
  public void enterTransaction() {}

  @Override
  public void popTransaction() {}

  @Override
  public int lineCount() {
    final int numberOfBlock =
        this.batchUnderConstruction ? this.operation.size() + 1 : this.operation.size();
    return numberOfBlock * (MAX_CT + 1);
  }

  @Override
  public List<ColumnHeader> columnsHeaders() {
    return List.of();
  }

  @Override
  public void commit(List<MappedByteBuffer> buffers) {
    Module.super.commit(buffers);
  }
}
