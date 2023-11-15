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

package net.consensys.linea.zktracer.module.logData;

import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.ModuleTrace;
import net.consensys.linea.zktracer.module.rlp_txrcpt.RlpTxrcpt;
import net.consensys.linea.zktracer.module.rlp_txrcpt.RlpTxrcptChunk;
import org.hyperledger.besu.evm.log.Log;

public class LogData implements Module {
  private final RlpTxrcpt rlpTxrcpt;
  public LogData(RlpTxrcpt rlpTxrcpt){this.rlpTxrcpt=rlpTxrcpt;}
  @Override
  public String jsonKey() {
    return "logData";
  }

  @Override
  public void enterTransaction() {}

  @Override
  public void popTransaction() {}

  @Override
  public int lineCount() {
    int rowSize = 0;
    for (RlpTxrcptChunk tx: this.rlpTxrcpt.chunkList) {
      rowSize += txRowSize(tx);
    }
    return  rowSize;
  }

  private int txRowSize(RlpTxrcptChunk tx){
    int txRowSize = 0;
    if (tx.logs().isEmpty()){return 0;} else {
      for (Log log: tx.logs()) {
        txRowSize += indexMax(log)+1;
      }
      return  txRowSize;
    }}

  private int indexMax(Log log){
    return log.getData().isEmpty()? 0 : log.getData().size()/16;
  }

  @Override
  public ModuleTrace commit() {
    final Trace.TraceBuilder trace = Trace.builder(this.lineCount());

    int absLogNumMax = 0;
    for (RlpTxrcptChunk tx: this.rlpTxrcpt.chunkList) {
      absLogNumMax+=tx.logs().size();
    }

    int absLogNum = 0;
    for (RlpTxrcptChunk tx: this.rlpTxrcpt.chunkList) {
      if (tx.logs().isEmpty()){
        traceTxWoLog(absLogNum, absLogNumMax);
      } else {
        for (Log log:tx.logs()) {
          absLogNum+=1;
          traceLog(log, absLogNum, absLogNumMax);
        }
      }
    }

    return new LogDataTrace(trace.build());
  }

  public void traceTxWoLog(int absLogNum, int absLogNumMax){

  }

  public void traceLog(Log log, int absLogNum, int absLogNumMax){
final int indexMax = indexMax(log);
    for (int index = 0; index < indexMax+1; index++) {

    }
  }

}
