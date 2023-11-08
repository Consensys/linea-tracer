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

package net.consensys.linea.zktracer.module.mod;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.ByteString;
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
  public TraceBuilder acc12(BigInteger b) {

    if (filled.get(0)) {
      throw new IllegalStateException("ACC_1_2 already set");
    } else {
      filled.set(0);
    }
    processBigInteger(b, 0);
    return this;
  }
  public TraceBuilder acc13(BigInteger b) {

    if (filled.get(1)) {
      throw new IllegalStateException("ACC_1_3 already set");
    } else {
      filled.set(1);
    }
    processBigInteger(b, 1);
    return this;
  }
  public TraceBuilder acc22(BigInteger b) {

    if (filled.get(2)) {
      throw new IllegalStateException("ACC_2_2 already set");
    } else {
      filled.set(2);
    }
    processBigInteger(b, 2);
    return this;
  }
  public TraceBuilder acc23(BigInteger b) {

    if (filled.get(3)) {
      throw new IllegalStateException("ACC_2_3 already set");
    } else {
      filled.set(3);
    }
    processBigInteger(b, 3);
    return this;
  }
  public TraceBuilder accB0(BigInteger b) {

    if (filled.get(4)) {
      throw new IllegalStateException("ACC_B_0 already set");
    } else {
      filled.set(4);
    }
    processBigInteger(b, 4);
    return this;
  }
  public TraceBuilder accB1(BigInteger b) {

    if (filled.get(5)) {
      throw new IllegalStateException("ACC_B_1 already set");
    } else {
      filled.set(5);
    }
    processBigInteger(b, 5);
    return this;
  }
  public TraceBuilder accB2(BigInteger b) {

    if (filled.get(6)) {
      throw new IllegalStateException("ACC_B_2 already set");
    } else {
      filled.set(6);
    }
    processBigInteger(b, 6);
    return this;
  }
  public TraceBuilder accB3(BigInteger b) {

    if (filled.get(7)) {
      throw new IllegalStateException("ACC_B_3 already set");
    } else {
      filled.set(7);
    }
    processBigInteger(b, 7);
    return this;
  }
  public TraceBuilder accDelta0(BigInteger b) {

    if (filled.get(8)) {
      throw new IllegalStateException("ACC_DELTA_0 already set");
    } else {
      filled.set(8);
    }
    processBigInteger(b, 8);
    return this;
  }
  public TraceBuilder accDelta1(BigInteger b) {

    if (filled.get(9)) {
      throw new IllegalStateException("ACC_DELTA_1 already set");
    } else {
      filled.set(9);
    }
    processBigInteger(b, 9);
    return this;
  }
  public TraceBuilder accDelta2(BigInteger b) {

    if (filled.get(10)) {
      throw new IllegalStateException("ACC_DELTA_2 already set");
    } else {
      filled.set(10);
    }
    processBigInteger(b, 10);
    return this;
  }
  public TraceBuilder accDelta3(BigInteger b) {

    if (filled.get(11)) {
      throw new IllegalStateException("ACC_DELTA_3 already set");
    } else {
      filled.set(11);
    }
    processBigInteger(b, 11);
    return this;
  }
  public TraceBuilder accH0(BigInteger b) {

    if (filled.get(12)) {
      throw new IllegalStateException("ACC_H_0 already set");
    } else {
      filled.set(12);
    }
    processBigInteger(b, 12);
    return this;
  }
  public TraceBuilder accH1(BigInteger b) {

    if (filled.get(13)) {
      throw new IllegalStateException("ACC_H_1 already set");
    } else {
      filled.set(13);
    }
    processBigInteger(b, 13);
    return this;
  }
  public TraceBuilder accH2(BigInteger b) {

    if (filled.get(14)) {
      throw new IllegalStateException("ACC_H_2 already set");
    } else {
      filled.set(14);
    }
    processBigInteger(b, 14);
    return this;
  }
  public TraceBuilder accQ0(BigInteger b) {

    if (filled.get(15)) {
      throw new IllegalStateException("ACC_Q_0 already set");
    } else {
      filled.set(15);
    }
    processBigInteger(b, 15);
    return this;
  }
  public TraceBuilder accQ1(BigInteger b) {

    if (filled.get(16)) {
      throw new IllegalStateException("ACC_Q_1 already set");
    } else {
      filled.set(16);
    }
    processBigInteger(b, 16);
    return this;
  }
  public TraceBuilder accQ2(BigInteger b) {

    if (filled.get(17)) {
      throw new IllegalStateException("ACC_Q_2 already set");
    } else {
      filled.set(17);
    }
    processBigInteger(b, 17);
    return this;
  }
  public TraceBuilder accQ3(BigInteger b) {

    if (filled.get(18)) {
      throw new IllegalStateException("ACC_Q_3 already set");
    } else {
      filled.set(18);
    }
    processBigInteger(b, 18);
    return this;
  }
  public TraceBuilder accR0(BigInteger b) {

    if (filled.get(19)) {
      throw new IllegalStateException("ACC_R_0 already set");
    } else {
      filled.set(19);
    }
    processBigInteger(b, 19);
    return this;
  }
  public TraceBuilder accR1(BigInteger b) {

    if (filled.get(20)) {
      throw new IllegalStateException("ACC_R_1 already set");
    } else {
      filled.set(20);
    }
    processBigInteger(b, 20);
    return this;
  }
  public TraceBuilder accR2(BigInteger b) {

    if (filled.get(21)) {
      throw new IllegalStateException("ACC_R_2 already set");
    } else {
      filled.set(21);
    }
    processBigInteger(b, 21);
    return this;
  }
  public TraceBuilder accR3(BigInteger b) {

    if (filled.get(22)) {
      throw new IllegalStateException("ACC_R_3 already set");
    } else {
      filled.set(22);
    }
    processBigInteger(b, 22);
    return this;
  }
  public TraceBuilder arg1Hi(BigInteger b) {

    if (filled.get(23)) {
      throw new IllegalStateException("ARG_1_HI already set");
    } else {
      filled.set(23);
    }
    processBigInteger(b, 23);
    return this;
  }
  public TraceBuilder arg1Lo(BigInteger b) {

    if (filled.get(24)) {
      throw new IllegalStateException("ARG_1_LO already set");
    } else {
      filled.set(24);
    }
    processBigInteger(b, 24);
    return this;
  }
  public TraceBuilder arg2Hi(BigInteger b) {

    if (filled.get(25)) {
      throw new IllegalStateException("ARG_2_HI already set");
    } else {
      filled.set(25);
    }
    processBigInteger(b, 25);
    return this;
  }
  public TraceBuilder arg2Lo(BigInteger b) {

    if (filled.get(26)) {
      throw new IllegalStateException("ARG_2_LO already set");
    } else {
      filled.set(26);
    }
    processBigInteger(b, 26);
    return this;
  }
  public TraceBuilder byte12(UnsignedByte b) {

    if (filled.get(27)) {
      throw new IllegalStateException("BYTE_1_2 already set");
    } else {
      filled.set(27);
    }
    processUnsignedByte(b, 27);
    return this;
  }
  public TraceBuilder byte13(UnsignedByte b) {

    if (filled.get(28)) {
      throw new IllegalStateException("BYTE_1_3 already set");
    } else {
      filled.set(28);
    }
    processUnsignedByte(b, 28);
    return this;
  }
  public TraceBuilder byte22(UnsignedByte b) {

    if (filled.get(29)) {
      throw new IllegalStateException("BYTE_2_2 already set");
    } else {
      filled.set(29);
    }
    processUnsignedByte(b, 29);
    return this;
  }
  public TraceBuilder byte23(UnsignedByte b) {

    if (filled.get(30)) {
      throw new IllegalStateException("BYTE_2_3 already set");
    } else {
      filled.set(30);
    }
    processUnsignedByte(b, 30);
    return this;
  }
  public TraceBuilder byteB0(UnsignedByte b) {

    if (filled.get(31)) {
      throw new IllegalStateException("BYTE_B_0 already set");
    } else {
      filled.set(31);
    }
    processUnsignedByte(b, 31);
    return this;
  }
  public TraceBuilder byteB1(UnsignedByte b) {

    if (filled.get(32)) {
      throw new IllegalStateException("BYTE_B_1 already set");
    } else {
      filled.set(32);
    }
    processUnsignedByte(b, 32);
    return this;
  }
  public TraceBuilder byteB2(UnsignedByte b) {

    if (filled.get(33)) {
      throw new IllegalStateException("BYTE_B_2 already set");
    } else {
      filled.set(33);
    }
    processUnsignedByte(b, 33);
    return this;
  }
  public TraceBuilder byteB3(UnsignedByte b) {

    if (filled.get(34)) {
      throw new IllegalStateException("BYTE_B_3 already set");
    } else {
      filled.set(34);
    }
    processUnsignedByte(b, 34);
    return this;
  }
  public TraceBuilder byteDelta0(UnsignedByte b) {

    if (filled.get(35)) {
      throw new IllegalStateException("BYTE_DELTA_0 already set");
    } else {
      filled.set(35);
    }
    processUnsignedByte(b, 35);
    return this;
  }
  public TraceBuilder byteDelta1(UnsignedByte b) {

    if (filled.get(36)) {
      throw new IllegalStateException("BYTE_DELTA_1 already set");
    } else {
      filled.set(36);
    }
    processUnsignedByte(b, 36);
    return this;
  }
  public TraceBuilder byteDelta2(UnsignedByte b) {

    if (filled.get(37)) {
      throw new IllegalStateException("BYTE_DELTA_2 already set");
    } else {
      filled.set(37);
    }
    processUnsignedByte(b, 37);
    return this;
  }
  public TraceBuilder byteDelta3(UnsignedByte b) {

    if (filled.get(38)) {
      throw new IllegalStateException("BYTE_DELTA_3 already set");
    } else {
      filled.set(38);
    }
    processUnsignedByte(b, 38);
    return this;
  }
  public TraceBuilder byteH0(UnsignedByte b) {

    if (filled.get(39)) {
      throw new IllegalStateException("BYTE_H_0 already set");
    } else {
      filled.set(39);
    }
    processUnsignedByte(b, 39);
    return this;
  }
  public TraceBuilder byteH1(UnsignedByte b) {

    if (filled.get(40)) {
      throw new IllegalStateException("BYTE_H_1 already set");
    } else {
      filled.set(40);
    }
    processUnsignedByte(b, 40);
    return this;
  }
  public TraceBuilder byteH2(UnsignedByte b) {

    if (filled.get(41)) {
      throw new IllegalStateException("BYTE_H_2 already set");
    } else {
      filled.set(41);
    }
    processUnsignedByte(b, 41);
    return this;
  }
  public TraceBuilder byteQ0(UnsignedByte b) {

    if (filled.get(42)) {
      throw new IllegalStateException("BYTE_Q_0 already set");
    } else {
      filled.set(42);
    }
    processUnsignedByte(b, 42);
    return this;
  }
  public TraceBuilder byteQ1(UnsignedByte b) {

    if (filled.get(43)) {
      throw new IllegalStateException("BYTE_Q_1 already set");
    } else {
      filled.set(43);
    }
    processUnsignedByte(b, 43);
    return this;
  }
  public TraceBuilder byteQ2(UnsignedByte b) {

    if (filled.get(44)) {
      throw new IllegalStateException("BYTE_Q_2 already set");
    } else {
      filled.set(44);
    }
    processUnsignedByte(b, 44);
    return this;
  }
  public TraceBuilder byteQ3(UnsignedByte b) {

    if (filled.get(45)) {
      throw new IllegalStateException("BYTE_Q_3 already set");
    } else {
      filled.set(45);
    }
    processUnsignedByte(b, 45);
    return this;
  }
  public TraceBuilder byteR0(UnsignedByte b) {

    if (filled.get(46)) {
      throw new IllegalStateException("BYTE_R_0 already set");
    } else {
      filled.set(46);
    }
    processUnsignedByte(b, 46);
    return this;
  }
  public TraceBuilder byteR1(UnsignedByte b) {

    if (filled.get(47)) {
      throw new IllegalStateException("BYTE_R_1 already set");
    } else {
      filled.set(47);
    }
    processUnsignedByte(b, 47);
    return this;
  }
  public TraceBuilder byteR2(UnsignedByte b) {

    if (filled.get(48)) {
      throw new IllegalStateException("BYTE_R_2 already set");
    } else {
      filled.set(48);
    }
    processUnsignedByte(b, 48);
    return this;
  }
  public TraceBuilder byteR3(UnsignedByte b) {

    if (filled.get(49)) {
      throw new IllegalStateException("BYTE_R_3 already set");
    } else {
      filled.set(49);
    }
    processUnsignedByte(b, 49);
    return this;
  }
  public TraceBuilder cmp1(Boolean b) {

    if (filled.get(50)) {
      throw new IllegalStateException("CMP_1 already set");
    } else {
      filled.set(50);
    }
    processBoolean(b, 50);
    return this;
  }
  public TraceBuilder cmp2(Boolean b) {

    if (filled.get(51)) {
      throw new IllegalStateException("CMP_2 already set");
    } else {
      filled.set(51);
    }
    processBoolean(b, 51);
    return this;
  }
  public TraceBuilder ct(BigInteger b) {

    if (filled.get(52)) {
      throw new IllegalStateException("CT already set");
    } else {
      filled.set(52);
    }
    processBigInteger(b, 52);
    return this;
  }
  public TraceBuilder decOutput(Boolean b) {

    if (filled.get(53)) {
      throw new IllegalStateException("DEC_OUTPUT already set");
    } else {
      filled.set(53);
    }
    processBoolean(b, 53);
    return this;
  }
  public TraceBuilder decSigned(Boolean b) {

    if (filled.get(54)) {
      throw new IllegalStateException("DEC_SIGNED already set");
    } else {
      filled.set(54);
    }
    processBoolean(b, 54);
    return this;
  }
  public TraceBuilder inst(BigInteger b) {

    if (filled.get(55)) {
      throw new IllegalStateException("INST already set");
    } else {
      filled.set(55);
    }
    processBigInteger(b, 55);
    return this;
  }
  public TraceBuilder msb1(Boolean b) {

    if (filled.get(56)) {
      throw new IllegalStateException("MSB_1 already set");
    } else {
      filled.set(56);
    }
    processBoolean(b, 56);
    return this;
  }
  public TraceBuilder msb2(Boolean b) {

    if (filled.get(57)) {
      throw new IllegalStateException("MSB_2 already set");
    } else {
      filled.set(57);
    }
    processBoolean(b, 57);
    return this;
  }
  public TraceBuilder oli(Boolean b) {

    if (filled.get(58)) {
      throw new IllegalStateException("OLI already set");
    } else {
      filled.set(58);
    }
    processBoolean(b, 58);
    return this;
  }
  public TraceBuilder resHi(BigInteger b) {

    if (filled.get(59)) {
      throw new IllegalStateException("RES_HI already set");
    } else {
      filled.set(59);
    }
    processBigInteger(b, 59);
    return this;
  }
  public TraceBuilder resLo(BigInteger b) {

    if (filled.get(60)) {
      throw new IllegalStateException("RES_LO already set");
    } else {
      filled.set(60);
    }
    processBigInteger(b, 60);
    return this;
  }
  public TraceBuilder stamp(BigInteger b) {

    if (filled.get(61)) {
      throw new IllegalStateException("STAMP already set");
    } else {
      filled.set(61);
    }
    processBigInteger(b, 61);
    return this;
  }

  public void validateRowAndFlush()  throws IOException {
    if (!filled.get(0)) {
      throw new IllegalStateException("ACC_1_2 has not been filled");
    }

    if (!filled.get(1)) {
      throw new IllegalStateException("ACC_1_3 has not been filled");
    }

    if (!filled.get(2)) {
      throw new IllegalStateException("ACC_2_2 has not been filled");
    }

    if (!filled.get(3)) {
      throw new IllegalStateException("ACC_2_3 has not been filled");
    }

    if (!filled.get(4)) {
      throw new IllegalStateException("ACC_B_0 has not been filled");
    }

    if (!filled.get(5)) {
      throw new IllegalStateException("ACC_B_1 has not been filled");
    }

    if (!filled.get(6)) {
      throw new IllegalStateException("ACC_B_2 has not been filled");
    }

    if (!filled.get(7)) {
      throw new IllegalStateException("ACC_B_3 has not been filled");
    }

    if (!filled.get(8)) {
      throw new IllegalStateException("ACC_DELTA_0 has not been filled");
    }

    if (!filled.get(9)) {
      throw new IllegalStateException("ACC_DELTA_1 has not been filled");
    }

    if (!filled.get(10)) {
      throw new IllegalStateException("ACC_DELTA_2 has not been filled");
    }

    if (!filled.get(11)) {
      throw new IllegalStateException("ACC_DELTA_3 has not been filled");
    }

    if (!filled.get(12)) {
      throw new IllegalStateException("ACC_H_0 has not been filled");
    }

    if (!filled.get(13)) {
      throw new IllegalStateException("ACC_H_1 has not been filled");
    }

    if (!filled.get(14)) {
      throw new IllegalStateException("ACC_H_2 has not been filled");
    }

    if (!filled.get(15)) {
      throw new IllegalStateException("ACC_Q_0 has not been filled");
    }

    if (!filled.get(16)) {
      throw new IllegalStateException("ACC_Q_1 has not been filled");
    }

    if (!filled.get(17)) {
      throw new IllegalStateException("ACC_Q_2 has not been filled");
    }

    if (!filled.get(18)) {
      throw new IllegalStateException("ACC_Q_3 has not been filled");
    }

    if (!filled.get(19)) {
      throw new IllegalStateException("ACC_R_0 has not been filled");
    }

    if (!filled.get(20)) {
      throw new IllegalStateException("ACC_R_1 has not been filled");
    }

    if (!filled.get(21)) {
      throw new IllegalStateException("ACC_R_2 has not been filled");
    }

    if (!filled.get(22)) {
      throw new IllegalStateException("ACC_R_3 has not been filled");
    }

    if (!filled.get(23)) {
      throw new IllegalStateException("ARG_1_HI has not been filled");
    }

    if (!filled.get(24)) {
      throw new IllegalStateException("ARG_1_LO has not been filled");
    }

    if (!filled.get(25)) {
      throw new IllegalStateException("ARG_2_HI has not been filled");
    }

    if (!filled.get(26)) {
      throw new IllegalStateException("ARG_2_LO has not been filled");
    }

    if (!filled.get(27)) {
      throw new IllegalStateException("BYTE_1_2 has not been filled");
    }

    if (!filled.get(28)) {
      throw new IllegalStateException("BYTE_1_3 has not been filled");
    }

    if (!filled.get(29)) {
      throw new IllegalStateException("BYTE_2_2 has not been filled");
    }

    if (!filled.get(30)) {
      throw new IllegalStateException("BYTE_2_3 has not been filled");
    }

    if (!filled.get(31)) {
      throw new IllegalStateException("BYTE_B_0 has not been filled");
    }

    if (!filled.get(32)) {
      throw new IllegalStateException("BYTE_B_1 has not been filled");
    }

    if (!filled.get(33)) {
      throw new IllegalStateException("BYTE_B_2 has not been filled");
    }

    if (!filled.get(34)) {
      throw new IllegalStateException("BYTE_B_3 has not been filled");
    }

    if (!filled.get(35)) {
      throw new IllegalStateException("BYTE_DELTA_0 has not been filled");
    }

    if (!filled.get(36)) {
      throw new IllegalStateException("BYTE_DELTA_1 has not been filled");
    }

    if (!filled.get(37)) {
      throw new IllegalStateException("BYTE_DELTA_2 has not been filled");
    }

    if (!filled.get(38)) {
      throw new IllegalStateException("BYTE_DELTA_3 has not been filled");
    }

    if (!filled.get(39)) {
      throw new IllegalStateException("BYTE_H_0 has not been filled");
    }

    if (!filled.get(40)) {
      throw new IllegalStateException("BYTE_H_1 has not been filled");
    }

    if (!filled.get(41)) {
      throw new IllegalStateException("BYTE_H_2 has not been filled");
    }

    if (!filled.get(42)) {
      throw new IllegalStateException("BYTE_Q_0 has not been filled");
    }

    if (!filled.get(43)) {
      throw new IllegalStateException("BYTE_Q_1 has not been filled");
    }

    if (!filled.get(44)) {
      throw new IllegalStateException("BYTE_Q_2 has not been filled");
    }

    if (!filled.get(45)) {
      throw new IllegalStateException("BYTE_Q_3 has not been filled");
    }

    if (!filled.get(46)) {
      throw new IllegalStateException("BYTE_R_0 has not been filled");
    }

    if (!filled.get(47)) {
      throw new IllegalStateException("BYTE_R_1 has not been filled");
    }

    if (!filled.get(48)) {
      throw new IllegalStateException("BYTE_R_2 has not been filled");
    }

    if (!filled.get(49)) {
      throw new IllegalStateException("BYTE_R_3 has not been filled");
    }

    if (!filled.get(50)) {
      throw new IllegalStateException("CMP_1 has not been filled");
    }

    if (!filled.get(51)) {
      throw new IllegalStateException("CMP_2 has not been filled");
    }

    if (!filled.get(52)) {
      throw new IllegalStateException("CT has not been filled");
    }

    if (!filled.get(53)) {
      throw new IllegalStateException("DEC_OUTPUT has not been filled");
    }

    if (!filled.get(54)) {
      throw new IllegalStateException("DEC_SIGNED has not been filled");
    }

    if (!filled.get(55)) {
      throw new IllegalStateException("INST has not been filled");
    }

    if (!filled.get(56)) {
      throw new IllegalStateException("MSB_1 has not been filled");
    }

    if (!filled.get(57)) {
      throw new IllegalStateException("MSB_2 has not been filled");
    }

    if (!filled.get(58)) {
      throw new IllegalStateException("OLI has not been filled");
    }

    if (!filled.get(59)) {
      throw new IllegalStateException("RES_HI has not been filled");
    }

    if (!filled.get(60)) {
      throw new IllegalStateException("RES_LO has not been filled");
    }

    if (!filled.get(61)) {
      throw new IllegalStateException("STAMP has not been filled");
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
