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

package net.consensys.linea.zktracer.module.logInfo;

import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.ModuleTrace;
import net.consensys.linea.zktracer.module.rlp_txrcpt.RlpTxrcpt;
import net.consensys.linea.zktracer.module.rlp_txrcpt.RlpTxrcptChunk;
import org.hyperledger.besu.evm.log.Log;

public class LogInfo implements Module {
  private final RlpTxrcpt rlpTxrcpt;

  public LogInfo(RlpTxrcpt rlpTxrcpt) {
    this.rlpTxrcpt = rlpTxrcpt;
  }

  @Override
  public String jsonKey() {
    return "logInfo";
  }

  @Override
  public void enterTransaction() {}

  @Override
  public void popTransaction() {}

  @Override
  public int lineCount() {
    int rowSize =0;
    for (RlpTxrcptChunk chunk: this.rlpTxrcpt.chunkList) {
rowSize+= txRowSize(chunk)+1;
    }
    return  rowSize;
  }

  @Override
  public ModuleTrace commit() {
    final Trace.TraceBuilder trace = Trace.builder(this.lineCount());

    int absLogNumMax =0;
    for (RlpTxrcptChunk tx: this.rlpTxrcpt.chunkList) {
      absLogNumMax += tx.logs().size();
    }

    int absTxNum = 0;
    int absLogNum = 0;
    for (RlpTxrcptChunk tx: this.rlpTxrcpt.chunkList) {
      absTxNum+=1;
      if (tx.logs().isEmpty()){
        traceTxWoLog(absTxNum, absLogNum, absLogNumMax);
      } else {
      for (Log log: tx.logs()) {
        absLogNum +=1;
        traceLog(log, absTxNum, absLogNum, absLogNumMax);
      }}
    }
    return new LogInfoTrace(trace.build());
  }

  private int txRowSize(RlpTxrcptChunk tx) {
    int txRowSize = 0;
    if (tx.logs().isEmpty()) {
      return 0;
    } else {
      for (Log log : tx.logs()) {
        txRowSize += ctMax(log)+1;
      }
      return txRowSize;
    }
  }

  public void traceTxWoLog(int absTxNum, int absLogNum, int absLogNumMax){

  }

  public void traceLog(Log log, int absTxNum, int absLogNum, int absLogNumMax){
final int ctMax = ctMax(log);
    for (int ct = 0; ct < ctMax+1; ct++) {
switch (ct){
  case 0 -> {}
  case 1 -> {}
  case 2 -> {}
  case 3 -> {}
  case 4 -> {}
  case 5 -> {}
  case 6 -> {}
  default -> throw new IllegalArgumentException("ct = " + ct + " greater than ctMax =" + ctMax);
}
    }
  }

  private int ctMax(Log log){
    return log.getTopics().size()+1;
  }
}