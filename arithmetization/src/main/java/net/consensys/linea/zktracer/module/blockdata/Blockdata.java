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

import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EVM_INST_BASEFEE;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EVM_INST_CHAINID;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EVM_INST_COINBASE;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EVM_INST_DIFFICULTY;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EVM_INST_GASLIMIT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EVM_INST_NUMBER;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EVM_INST_TIMESTAMP;

import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.util.*;

import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.container.module.Module;
import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.txndata.TxnData;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import org.hyperledger.besu.evm.worldstate.WorldView;
import org.hyperledger.besu.plugin.data.BlockBody;
import org.hyperledger.besu.plugin.data.BlockHeader;

@RequiredArgsConstructor
public class Blockdata implements Module {
  private final Wcp wcp;
  private final Euc euc;
  private final TxnData txnData;
  private final BigInteger chainId;

  private final Deque<BlockdataOperation> operations = new ArrayDeque<>();
  private boolean conflationFinished = false;
  private static final int TIMESTAMP_BYTESIZE = 4;
  private int previousTimestamp = 0;
  private Map<Integer, BlockdataOperation> prevOperationByInst = new HashMap<>();
  private int traceCounter = 0;
  private long firstBlockNumber;

  final int[] instructions = {
    EVM_INST_COINBASE,
    EVM_INST_TIMESTAMP,
    EVM_INST_NUMBER,
    EVM_INST_DIFFICULTY,
    EVM_INST_GASLIMIT,
    EVM_INST_CHAINID,
    EVM_INST_BASEFEE
  };

  @Override
  public String moduleKey() {
    return "BLOCK_DATA";
  }

  @Override
  public void traceStartConflation(final long blockCount) {
    wcp.additionalRows.add(TIMESTAMP_BYTESIZE);
  }

  @Override
  public void traceEndBlock(final BlockHeader blockHeader, final BlockBody blockBody) {
    final int currentTimestamp = (int) blockHeader.getTimestamp();
    final long blockNumber = blockHeader.getNumber();
    firstBlockNumber = (traceCounter < instructions.length) ? blockNumber : firstBlockNumber;
    for (int inst : instructions) {
      BlockdataOperation operation =
          new BlockdataOperation(
              blockHeader.getCoinbase(),
              currentTimestamp,
              blockNumber,
              blockHeader.getDifficulty().getAsBigInteger(),
              blockHeader.getGasLimit(),
              blockHeader.getBaseFee(),
              txnData.currentBlock().getNbOfTxsInBlock(),
              wcp,
              euc,
              txnData,
              chainId,
              inst,
              prevOperationByInst.get(inst),
              firstBlockNumber);
      operations.addLast(operation);
      prevOperationByInst.put(inst, operation);
      wcp.callGT(currentTimestamp, previousTimestamp); // ?
      previousTimestamp = currentTimestamp; // ?
      // Increase counter to track where we are in the conflation
      traceCounter++;
    }
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
    return numberOfBlock * (1); // TODO: update
  }

  @Override
  public List<ColumnHeader> columnsHeaders() {
    return Trace.headers(this.lineCount());
  }

  @Override
  public void commit(List<MappedByteBuffer> buffers) {
    final Trace trace = new Trace(buffers);

    final long firstBlockNumber = operations.getFirst().absoluteBlockNumber();
    int relblock = 0;
    for (BlockdataOperation blockData : operations) {
      blockData.trace(trace, ++relblock, firstBlockNumber, chainId);
    }
  }
}
