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

package net.consensys.linea.zktracer.module.rlp_txn;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.consensys.linea.zktracer.types.UnsignedByte;
import com.fasterxml.jackson.annotation.JsonIgnore;
import net.consensys.linea.zktracer.module.ParquetTrace;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.function.Consumer;
import org.apache.hadoop.hive.ql.exec.vector.VectorizedRowBatch;
import org.apache.hadoop.hive.ql.exec.vector.BytesColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.LongColumnVector;
import org.apache.orc.Writer;
import java.io.IOException;

/**
 * WARNING: This code is generated automatically.
 * Any modifications to this code may be overwritten and could lead to unexpected behavior.
 * Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
public class TraceBuilder {

  private final int rowId;
  private final VectorizedRowBatch batch;
  private final Writer writer;

  public TraceBuilder(int row, VectorizedRowBatch batch, Writer writer) {
    this.writer = writer;
    this.batch = batch;
    this.rowId = row;
  }

  private final BitSet filled = new BitSet();
  public TraceBuilder absTxNum(BigInteger b) {

    if (filled.get(0)) {
      throw new IllegalStateException("ABS_TX_NUM already set");
    } else {
      filled.set(0);
    }
    processBigInteger(b, 0);
    return this;
  }
  public TraceBuilder absTxNumInfiny(BigInteger b) {

    if (filled.get(1)) {
      throw new IllegalStateException("ABS_TX_NUM_INFINY already set");
    } else {
      filled.set(1);
    }
    processBigInteger(b, 1);
    return this;
  }
  public TraceBuilder acc1(BigInteger b) {

    if (filled.get(3)) {
      throw new IllegalStateException("ACC_1 already set");
    } else {
      filled.set(3);
    }
    processBigInteger(b, 3);
    return this;
  }
  public TraceBuilder acc2(BigInteger b) {

    if (filled.get(4)) {
      throw new IllegalStateException("ACC_2 already set");
    } else {
      filled.set(4);
    }
    processBigInteger(b, 4);
    return this;
  }
  public TraceBuilder accBytesize(BigInteger b) {

    if (filled.get(5)) {
      throw new IllegalStateException("ACC_BYTESIZE already set");
    } else {
      filled.set(5);
    }
    processBigInteger(b, 5);
    return this;
  }
  public TraceBuilder accessTupleBytesize(BigInteger b) {

    if (filled.get(2)) {
      throw new IllegalStateException("ACCESS_TUPLE_BYTESIZE already set");
    } else {
      filled.set(2);
    }
    processBigInteger(b, 2);
    return this;
  }
  public TraceBuilder addrHi(BigInteger b) {

    if (filled.get(6)) {
      throw new IllegalStateException("ADDR_HI already set");
    } else {
      filled.set(6);
    }
    processBigInteger(b, 6);
    return this;
  }
  public TraceBuilder addrLo(BigInteger b) {

    if (filled.get(7)) {
      throw new IllegalStateException("ADDR_LO already set");
    } else {
      filled.set(7);
    }
    processBigInteger(b, 7);
    return this;
  }
  public TraceBuilder bit(Boolean b) {

    if (filled.get(8)) {
      throw new IllegalStateException("BIT already set");
    } else {
      filled.set(8);
    }
    processBoolean(b, 8);
    return this;
  }
  public TraceBuilder bitAcc(UnsignedByte b) {

    if (filled.get(9)) {
      throw new IllegalStateException("BIT_ACC already set");
    } else {
      filled.set(9);
    }
    processUnsignedByte(b, 9);
    return this;
  }
  public TraceBuilder byte1(UnsignedByte b) {

    if (filled.get(10)) {
      throw new IllegalStateException("BYTE_1 already set");
    } else {
      filled.set(10);
    }
    processUnsignedByte(b, 10);
    return this;
  }
  public TraceBuilder byte2(UnsignedByte b) {

    if (filled.get(11)) {
      throw new IllegalStateException("BYTE_2 already set");
    } else {
      filled.set(11);
    }
    processUnsignedByte(b, 11);
    return this;
  }
  public TraceBuilder codeFragmentIndex(BigInteger b) {

    if (filled.get(12)) {
      throw new IllegalStateException("CODE_FRAGMENT_INDEX already set");
    } else {
      filled.set(12);
    }
    processBigInteger(b, 12);
    return this;
  }
  public TraceBuilder counter(UnsignedByte b) {

    if (filled.get(13)) {
      throw new IllegalStateException("COUNTER already set");
    } else {
      filled.set(13);
    }
    processUnsignedByte(b, 13);
    return this;
  }
  public TraceBuilder dataHi(BigInteger b) {

    if (filled.get(15)) {
      throw new IllegalStateException("DATA_HI already set");
    } else {
      filled.set(15);
    }
    processBigInteger(b, 15);
    return this;
  }
  public TraceBuilder dataLo(BigInteger b) {

    if (filled.get(16)) {
      throw new IllegalStateException("DATA_LO already set");
    } else {
      filled.set(16);
    }
    processBigInteger(b, 16);
    return this;
  }
  public TraceBuilder datagascost(BigInteger b) {

    if (filled.get(14)) {
      throw new IllegalStateException("DATAGASCOST already set");
    } else {
      filled.set(14);
    }
    processBigInteger(b, 14);
    return this;
  }
  public TraceBuilder depth1(Boolean b) {

    if (filled.get(17)) {
      throw new IllegalStateException("DEPTH_1 already set");
    } else {
      filled.set(17);
    }
    processBoolean(b, 17);
    return this;
  }
  public TraceBuilder depth2(Boolean b) {

    if (filled.get(18)) {
      throw new IllegalStateException("DEPTH_2 already set");
    } else {
      filled.set(18);
    }
    processBoolean(b, 18);
    return this;
  }
  public TraceBuilder done(Boolean b) {

    if (filled.get(19)) {
      throw new IllegalStateException("DONE already set");
    } else {
      filled.set(19);
    }
    processBoolean(b, 19);
    return this;
  }
  public TraceBuilder indexData(BigInteger b) {

    if (filled.get(20)) {
      throw new IllegalStateException("INDEX_DATA already set");
    } else {
      filled.set(20);
    }
    processBigInteger(b, 20);
    return this;
  }
  public TraceBuilder indexLt(BigInteger b) {

    if (filled.get(21)) {
      throw new IllegalStateException("INDEX_LT already set");
    } else {
      filled.set(21);
    }
    processBigInteger(b, 21);
    return this;
  }
  public TraceBuilder indexLx(BigInteger b) {

    if (filled.get(22)) {
      throw new IllegalStateException("INDEX_LX already set");
    } else {
      filled.set(22);
    }
    processBigInteger(b, 22);
    return this;
  }
  public TraceBuilder input1(BigInteger b) {

    if (filled.get(23)) {
      throw new IllegalStateException("INPUT_1 already set");
    } else {
      filled.set(23);
    }
    processBigInteger(b, 23);
    return this;
  }
  public TraceBuilder input2(BigInteger b) {

    if (filled.get(24)) {
      throw new IllegalStateException("INPUT_2 already set");
    } else {
      filled.set(24);
    }
    processBigInteger(b, 24);
    return this;
  }
  public TraceBuilder isPrefix(Boolean b) {

    if (filled.get(25)) {
      throw new IllegalStateException("IS_PREFIX already set");
    } else {
      filled.set(25);
    }
    processBoolean(b, 25);
    return this;
  }
  public TraceBuilder lcCorrection(Boolean b) {

    if (filled.get(26)) {
      throw new IllegalStateException("LC_CORRECTION already set");
    } else {
      filled.set(26);
    }
    processBoolean(b, 26);
    return this;
  }
  public TraceBuilder limb(BigInteger b) {

    if (filled.get(27)) {
      throw new IllegalStateException("LIMB already set");
    } else {
      filled.set(27);
    }
    processBigInteger(b, 27);
    return this;
  }
  public TraceBuilder limbConstructed(Boolean b) {

    if (filled.get(28)) {
      throw new IllegalStateException("LIMB_CONSTRUCTED already set");
    } else {
      filled.set(28);
    }
    processBoolean(b, 28);
    return this;
  }
  public TraceBuilder lt(Boolean b) {

    if (filled.get(29)) {
      throw new IllegalStateException("LT already set");
    } else {
      filled.set(29);
    }
    processBoolean(b, 29);
    return this;
  }
  public TraceBuilder lx(Boolean b) {

    if (filled.get(30)) {
      throw new IllegalStateException("LX already set");
    } else {
      filled.set(30);
    }
    processBoolean(b, 30);
    return this;
  }
  public TraceBuilder nAddr(BigInteger b) {

    if (filled.get(53)) {
      throw new IllegalStateException("nADDR already set");
    } else {
      filled.set(53);
    }
    processBigInteger(b, 53);
    return this;
  }
  public TraceBuilder nBytes(UnsignedByte b) {

    if (filled.get(54)) {
      throw new IllegalStateException("nBYTES already set");
    } else {
      filled.set(54);
    }
    processUnsignedByte(b, 54);
    return this;
  }
  public TraceBuilder nKeys(BigInteger b) {

    if (filled.get(55)) {
      throw new IllegalStateException("nKEYS already set");
    } else {
      filled.set(55);
    }
    processBigInteger(b, 55);
    return this;
  }
  public TraceBuilder nKeysPerAddr(BigInteger b) {

    if (filled.get(56)) {
      throw new IllegalStateException("nKEYS_PER_ADDR already set");
    } else {
      filled.set(56);
    }
    processBigInteger(b, 56);
    return this;
  }
  public TraceBuilder nStep(UnsignedByte b) {

    if (filled.get(57)) {
      throw new IllegalStateException("nSTEP already set");
    } else {
      filled.set(57);
    }
    processUnsignedByte(b, 57);
    return this;
  }
  public TraceBuilder phase0(Boolean b) {

    if (filled.get(31)) {
      throw new IllegalStateException("PHASE_0 already set");
    } else {
      filled.set(31);
    }
    processBoolean(b, 31);
    return this;
  }
  public TraceBuilder phase1(Boolean b) {

    if (filled.get(32)) {
      throw new IllegalStateException("PHASE_1 already set");
    } else {
      filled.set(32);
    }
    processBoolean(b, 32);
    return this;
  }
  public TraceBuilder phase10(Boolean b) {

    if (filled.get(33)) {
      throw new IllegalStateException("PHASE_10 already set");
    } else {
      filled.set(33);
    }
    processBoolean(b, 33);
    return this;
  }
  public TraceBuilder phase11(Boolean b) {

    if (filled.get(34)) {
      throw new IllegalStateException("PHASE_11 already set");
    } else {
      filled.set(34);
    }
    processBoolean(b, 34);
    return this;
  }
  public TraceBuilder phase12(Boolean b) {

    if (filled.get(35)) {
      throw new IllegalStateException("PHASE_12 already set");
    } else {
      filled.set(35);
    }
    processBoolean(b, 35);
    return this;
  }
  public TraceBuilder phase13(Boolean b) {

    if (filled.get(36)) {
      throw new IllegalStateException("PHASE_13 already set");
    } else {
      filled.set(36);
    }
    processBoolean(b, 36);
    return this;
  }
  public TraceBuilder phase14(Boolean b) {

    if (filled.get(37)) {
      throw new IllegalStateException("PHASE_14 already set");
    } else {
      filled.set(37);
    }
    processBoolean(b, 37);
    return this;
  }
  public TraceBuilder phase2(Boolean b) {

    if (filled.get(38)) {
      throw new IllegalStateException("PHASE_2 already set");
    } else {
      filled.set(38);
    }
    processBoolean(b, 38);
    return this;
  }
  public TraceBuilder phase3(Boolean b) {

    if (filled.get(39)) {
      throw new IllegalStateException("PHASE_3 already set");
    } else {
      filled.set(39);
    }
    processBoolean(b, 39);
    return this;
  }
  public TraceBuilder phase4(Boolean b) {

    if (filled.get(40)) {
      throw new IllegalStateException("PHASE_4 already set");
    } else {
      filled.set(40);
    }
    processBoolean(b, 40);
    return this;
  }
  public TraceBuilder phase5(Boolean b) {

    if (filled.get(41)) {
      throw new IllegalStateException("PHASE_5 already set");
    } else {
      filled.set(41);
    }
    processBoolean(b, 41);
    return this;
  }
  public TraceBuilder phase6(Boolean b) {

    if (filled.get(42)) {
      throw new IllegalStateException("PHASE_6 already set");
    } else {
      filled.set(42);
    }
    processBoolean(b, 42);
    return this;
  }
  public TraceBuilder phase7(Boolean b) {

    if (filled.get(43)) {
      throw new IllegalStateException("PHASE_7 already set");
    } else {
      filled.set(43);
    }
    processBoolean(b, 43);
    return this;
  }
  public TraceBuilder phase8(Boolean b) {

    if (filled.get(44)) {
      throw new IllegalStateException("PHASE_8 already set");
    } else {
      filled.set(44);
    }
    processBoolean(b, 44);
    return this;
  }
  public TraceBuilder phase9(Boolean b) {

    if (filled.get(45)) {
      throw new IllegalStateException("PHASE_9 already set");
    } else {
      filled.set(45);
    }
    processBoolean(b, 45);
    return this;
  }
  public TraceBuilder phaseEnd(Boolean b) {

    if (filled.get(46)) {
      throw new IllegalStateException("PHASE_END already set");
    } else {
      filled.set(46);
    }
    processBoolean(b, 46);
    return this;
  }
  public TraceBuilder phaseSize(BigInteger b) {

    if (filled.get(47)) {
      throw new IllegalStateException("PHASE_SIZE already set");
    } else {
      filled.set(47);
    }
    processBigInteger(b, 47);
    return this;
  }
  public TraceBuilder power(BigInteger b) {

    if (filled.get(48)) {
      throw new IllegalStateException("POWER already set");
    } else {
      filled.set(48);
    }
    processBigInteger(b, 48);
    return this;
  }
  public TraceBuilder requiresEvmExecution(Boolean b) {

    if (filled.get(49)) {
      throw new IllegalStateException("REQUIRES_EVM_EXECUTION already set");
    } else {
      filled.set(49);
    }
    processBoolean(b, 49);
    return this;
  }
  public TraceBuilder rlpLtBytesize(BigInteger b) {

    if (filled.get(50)) {
      throw new IllegalStateException("RLP_LT_BYTESIZE already set");
    } else {
      filled.set(50);
    }
    processBigInteger(b, 50);
    return this;
  }
  public TraceBuilder rlpLxBytesize(BigInteger b) {

    if (filled.get(51)) {
      throw new IllegalStateException("RLP_LX_BYTESIZE already set");
    } else {
      filled.set(51);
    }
    processBigInteger(b, 51);
    return this;
  }
  public TraceBuilder type(UnsignedByte b) {

    if (filled.get(52)) {
      throw new IllegalStateException("TYPE already set");
    } else {
      filled.set(52);
    }
    processUnsignedByte(b, 52);
    return this;
  }

  public void validateRowAndFlush()  throws IOException {
    if (!filled.get(0)) {
      throw new IllegalStateException("ABS_TX_NUM has not been filled");
    }

    if (!filled.get(1)) {
      throw new IllegalStateException("ABS_TX_NUM_INFINY has not been filled");
    }

    if (!filled.get(2)) {
      throw new IllegalStateException("ACCESS_TUPLE_BYTESIZE has not been filled");
    }

    if (!filled.get(3)) {
      throw new IllegalStateException("ACC_1 has not been filled");
    }

    if (!filled.get(4)) {
      throw new IllegalStateException("ACC_2 has not been filled");
    }

    if (!filled.get(5)) {
      throw new IllegalStateException("ACC_BYTESIZE has not been filled");
    }

    if (!filled.get(6)) {
      throw new IllegalStateException("ADDR_HI has not been filled");
    }

    if (!filled.get(7)) {
      throw new IllegalStateException("ADDR_LO has not been filled");
    }

    if (!filled.get(8)) {
      throw new IllegalStateException("BIT has not been filled");
    }

    if (!filled.get(9)) {
      throw new IllegalStateException("BIT_ACC has not been filled");
    }

    if (!filled.get(10)) {
      throw new IllegalStateException("BYTE_1 has not been filled");
    }

    if (!filled.get(11)) {
      throw new IllegalStateException("BYTE_2 has not been filled");
    }

    if (!filled.get(12)) {
      throw new IllegalStateException("CODE_FRAGMENT_INDEX has not been filled");
    }

    if (!filled.get(13)) {
      throw new IllegalStateException("COUNTER has not been filled");
    }

    if (!filled.get(14)) {
      throw new IllegalStateException("DATAGASCOST has not been filled");
    }

    if (!filled.get(15)) {
      throw new IllegalStateException("DATA_HI has not been filled");
    }

    if (!filled.get(16)) {
      throw new IllegalStateException("DATA_LO has not been filled");
    }

    if (!filled.get(17)) {
      throw new IllegalStateException("DEPTH_1 has not been filled");
    }

    if (!filled.get(18)) {
      throw new IllegalStateException("DEPTH_2 has not been filled");
    }

    if (!filled.get(19)) {
      throw new IllegalStateException("DONE has not been filled");
    }

    if (!filled.get(20)) {
      throw new IllegalStateException("INDEX_DATA has not been filled");
    }

    if (!filled.get(21)) {
      throw new IllegalStateException("INDEX_LT has not been filled");
    }

    if (!filled.get(22)) {
      throw new IllegalStateException("INDEX_LX has not been filled");
    }

    if (!filled.get(23)) {
      throw new IllegalStateException("INPUT_1 has not been filled");
    }

    if (!filled.get(24)) {
      throw new IllegalStateException("INPUT_2 has not been filled");
    }

    if (!filled.get(25)) {
      throw new IllegalStateException("IS_PREFIX has not been filled");
    }

    if (!filled.get(26)) {
      throw new IllegalStateException("LC_CORRECTION has not been filled");
    }

    if (!filled.get(27)) {
      throw new IllegalStateException("LIMB has not been filled");
    }

    if (!filled.get(28)) {
      throw new IllegalStateException("LIMB_CONSTRUCTED has not been filled");
    }

    if (!filled.get(29)) {
      throw new IllegalStateException("LT has not been filled");
    }

    if (!filled.get(30)) {
      throw new IllegalStateException("LX has not been filled");
    }

    if (!filled.get(31)) {
      throw new IllegalStateException("PHASE_0 has not been filled");
    }

    if (!filled.get(32)) {
      throw new IllegalStateException("PHASE_1 has not been filled");
    }

    if (!filled.get(33)) {
      throw new IllegalStateException("PHASE_10 has not been filled");
    }

    if (!filled.get(34)) {
      throw new IllegalStateException("PHASE_11 has not been filled");
    }

    if (!filled.get(35)) {
      throw new IllegalStateException("PHASE_12 has not been filled");
    }

    if (!filled.get(36)) {
      throw new IllegalStateException("PHASE_13 has not been filled");
    }

    if (!filled.get(37)) {
      throw new IllegalStateException("PHASE_14 has not been filled");
    }

    if (!filled.get(38)) {
      throw new IllegalStateException("PHASE_2 has not been filled");
    }

    if (!filled.get(39)) {
      throw new IllegalStateException("PHASE_3 has not been filled");
    }

    if (!filled.get(40)) {
      throw new IllegalStateException("PHASE_4 has not been filled");
    }

    if (!filled.get(41)) {
      throw new IllegalStateException("PHASE_5 has not been filled");
    }

    if (!filled.get(42)) {
      throw new IllegalStateException("PHASE_6 has not been filled");
    }

    if (!filled.get(43)) {
      throw new IllegalStateException("PHASE_7 has not been filled");
    }

    if (!filled.get(44)) {
      throw new IllegalStateException("PHASE_8 has not been filled");
    }

    if (!filled.get(45)) {
      throw new IllegalStateException("PHASE_9 has not been filled");
    }

    if (!filled.get(46)) {
      throw new IllegalStateException("PHASE_END has not been filled");
    }

    if (!filled.get(47)) {
      throw new IllegalStateException("PHASE_SIZE has not been filled");
    }

    if (!filled.get(48)) {
      throw new IllegalStateException("POWER has not been filled");
    }

    if (!filled.get(49)) {
      throw new IllegalStateException("REQUIRES_EVM_EXECUTION has not been filled");
    }

    if (!filled.get(50)) {
      throw new IllegalStateException("RLP_LT_BYTESIZE has not been filled");
    }

    if (!filled.get(51)) {
      throw new IllegalStateException("RLP_LX_BYTESIZE has not been filled");
    }

    if (!filled.get(52)) {
      throw new IllegalStateException("TYPE has not been filled");
    }

    if (!filled.get(53)) {
      throw new IllegalStateException("nADDR has not been filled");
    }

    if (!filled.get(54)) {
      throw new IllegalStateException("nBYTES has not been filled");
    }

    if (!filled.get(55)) {
      throw new IllegalStateException("nKEYS has not been filled");
    }

    if (!filled.get(56)) {
      throw new IllegalStateException("nKEYS_PER_ADDR has not been filled");
    }

    if (!filled.get(57)) {
      throw new IllegalStateException("nSTEP has not been filled");
    }


    filled.clear();

    if (batch.size == batch.getMaxSize()) {
      writer.addRowBatch(batch);
      batch.reset();
    }
  }

  private void processBigInteger(BigInteger b, int columnId) {
    BytesColumnVector columnVector = (BytesColumnVector) batch.cols[columnId];
    columnVector.setVal(rowId, b.toByteArray());
  }

  private void processUnsignedByte(UnsignedByte b, int columnId) {
    BytesColumnVector columnVector = (BytesColumnVector) batch.cols[columnId];
    columnVector.setVal(rowId, new byte[]{b.toByte()});
  }

  private void processBoolean(Boolean b, int columnId) {
    LongColumnVector columnVector = (LongColumnVector) batch.cols[columnId];
    columnVector.vector[rowId] = b ? 1L : 0L;
  }
}
