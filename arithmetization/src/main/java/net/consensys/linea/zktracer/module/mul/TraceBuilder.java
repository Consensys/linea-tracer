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

package net.consensys.linea.zktracer.module.mul;

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
  public TraceBuilder accA0(BigInteger b) {

    if (filled.get(0)) {
      throw new IllegalStateException("ACC_A_0 already set");
    } else {
      filled.set(0);
    }
    processBigInteger(b, 0);
    return this;
  }
  public TraceBuilder accA1(BigInteger b) {

    if (filled.get(1)) {
      throw new IllegalStateException("ACC_A_1 already set");
    } else {
      filled.set(1);
    }
    processBigInteger(b, 1);
    return this;
  }
  public TraceBuilder accA2(BigInteger b) {

    if (filled.get(2)) {
      throw new IllegalStateException("ACC_A_2 already set");
    } else {
      filled.set(2);
    }
    processBigInteger(b, 2);
    return this;
  }
  public TraceBuilder accA3(BigInteger b) {

    if (filled.get(3)) {
      throw new IllegalStateException("ACC_A_3 already set");
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
  public TraceBuilder accC0(BigInteger b) {

    if (filled.get(8)) {
      throw new IllegalStateException("ACC_C_0 already set");
    } else {
      filled.set(8);
    }
    processBigInteger(b, 8);
    return this;
  }
  public TraceBuilder accC1(BigInteger b) {

    if (filled.get(9)) {
      throw new IllegalStateException("ACC_C_1 already set");
    } else {
      filled.set(9);
    }
    processBigInteger(b, 9);
    return this;
  }
  public TraceBuilder accC2(BigInteger b) {

    if (filled.get(10)) {
      throw new IllegalStateException("ACC_C_2 already set");
    } else {
      filled.set(10);
    }
    processBigInteger(b, 10);
    return this;
  }
  public TraceBuilder accC3(BigInteger b) {

    if (filled.get(11)) {
      throw new IllegalStateException("ACC_C_3 already set");
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
  public TraceBuilder accH3(BigInteger b) {

    if (filled.get(15)) {
      throw new IllegalStateException("ACC_H_3 already set");
    } else {
      filled.set(15);
    }
    processBigInteger(b, 15);
    return this;
  }
  public TraceBuilder arg1Hi(BigInteger b) {

    if (filled.get(16)) {
      throw new IllegalStateException("ARG_1_HI already set");
    } else {
      filled.set(16);
    }
    processBigInteger(b, 16);
    return this;
  }
  public TraceBuilder arg1Lo(BigInteger b) {

    if (filled.get(17)) {
      throw new IllegalStateException("ARG_1_LO already set");
    } else {
      filled.set(17);
    }
    processBigInteger(b, 17);
    return this;
  }
  public TraceBuilder arg2Hi(BigInteger b) {

    if (filled.get(18)) {
      throw new IllegalStateException("ARG_2_HI already set");
    } else {
      filled.set(18);
    }
    processBigInteger(b, 18);
    return this;
  }
  public TraceBuilder arg2Lo(BigInteger b) {

    if (filled.get(19)) {
      throw new IllegalStateException("ARG_2_LO already set");
    } else {
      filled.set(19);
    }
    processBigInteger(b, 19);
    return this;
  }
  public TraceBuilder bitNum(BigInteger b) {

    if (filled.get(21)) {
      throw new IllegalStateException("BIT_NUM already set");
    } else {
      filled.set(21);
    }
    processBigInteger(b, 21);
    return this;
  }
  public TraceBuilder bits(Boolean b) {

    if (filled.get(20)) {
      throw new IllegalStateException("BITS already set");
    } else {
      filled.set(20);
    }
    processBoolean(b, 20);
    return this;
  }
  public TraceBuilder byteA0(UnsignedByte b) {

    if (filled.get(22)) {
      throw new IllegalStateException("BYTE_A_0 already set");
    } else {
      filled.set(22);
    }
    processUnsignedByte(b, 22);
    return this;
  }
  public TraceBuilder byteA1(UnsignedByte b) {

    if (filled.get(23)) {
      throw new IllegalStateException("BYTE_A_1 already set");
    } else {
      filled.set(23);
    }
    processUnsignedByte(b, 23);
    return this;
  }
  public TraceBuilder byteA2(UnsignedByte b) {

    if (filled.get(24)) {
      throw new IllegalStateException("BYTE_A_2 already set");
    } else {
      filled.set(24);
    }
    processUnsignedByte(b, 24);
    return this;
  }
  public TraceBuilder byteA3(UnsignedByte b) {

    if (filled.get(25)) {
      throw new IllegalStateException("BYTE_A_3 already set");
    } else {
      filled.set(25);
    }
    processUnsignedByte(b, 25);
    return this;
  }
  public TraceBuilder byteB0(UnsignedByte b) {

    if (filled.get(26)) {
      throw new IllegalStateException("BYTE_B_0 already set");
    } else {
      filled.set(26);
    }
    processUnsignedByte(b, 26);
    return this;
  }
  public TraceBuilder byteB1(UnsignedByte b) {

    if (filled.get(27)) {
      throw new IllegalStateException("BYTE_B_1 already set");
    } else {
      filled.set(27);
    }
    processUnsignedByte(b, 27);
    return this;
  }
  public TraceBuilder byteB2(UnsignedByte b) {

    if (filled.get(28)) {
      throw new IllegalStateException("BYTE_B_2 already set");
    } else {
      filled.set(28);
    }
    processUnsignedByte(b, 28);
    return this;
  }
  public TraceBuilder byteB3(UnsignedByte b) {

    if (filled.get(29)) {
      throw new IllegalStateException("BYTE_B_3 already set");
    } else {
      filled.set(29);
    }
    processUnsignedByte(b, 29);
    return this;
  }
  public TraceBuilder byteC0(UnsignedByte b) {

    if (filled.get(30)) {
      throw new IllegalStateException("BYTE_C_0 already set");
    } else {
      filled.set(30);
    }
    processUnsignedByte(b, 30);
    return this;
  }
  public TraceBuilder byteC1(UnsignedByte b) {

    if (filled.get(31)) {
      throw new IllegalStateException("BYTE_C_1 already set");
    } else {
      filled.set(31);
    }
    processUnsignedByte(b, 31);
    return this;
  }
  public TraceBuilder byteC2(UnsignedByte b) {

    if (filled.get(32)) {
      throw new IllegalStateException("BYTE_C_2 already set");
    } else {
      filled.set(32);
    }
    processUnsignedByte(b, 32);
    return this;
  }
  public TraceBuilder byteC3(UnsignedByte b) {

    if (filled.get(33)) {
      throw new IllegalStateException("BYTE_C_3 already set");
    } else {
      filled.set(33);
    }
    processUnsignedByte(b, 33);
    return this;
  }
  public TraceBuilder byteH0(UnsignedByte b) {

    if (filled.get(34)) {
      throw new IllegalStateException("BYTE_H_0 already set");
    } else {
      filled.set(34);
    }
    processUnsignedByte(b, 34);
    return this;
  }
  public TraceBuilder byteH1(UnsignedByte b) {

    if (filled.get(35)) {
      throw new IllegalStateException("BYTE_H_1 already set");
    } else {
      filled.set(35);
    }
    processUnsignedByte(b, 35);
    return this;
  }
  public TraceBuilder byteH2(UnsignedByte b) {

    if (filled.get(36)) {
      throw new IllegalStateException("BYTE_H_2 already set");
    } else {
      filled.set(36);
    }
    processUnsignedByte(b, 36);
    return this;
  }
  public TraceBuilder byteH3(UnsignedByte b) {

    if (filled.get(37)) {
      throw new IllegalStateException("BYTE_H_3 already set");
    } else {
      filled.set(37);
    }
    processUnsignedByte(b, 37);
    return this;
  }
  public TraceBuilder counter(BigInteger b) {

    if (filled.get(38)) {
      throw new IllegalStateException("COUNTER already set");
    } else {
      filled.set(38);
    }
    processBigInteger(b, 38);
    return this;
  }
  public TraceBuilder exponentBit(Boolean b) {

    if (filled.get(39)) {
      throw new IllegalStateException("EXPONENT_BIT already set");
    } else {
      filled.set(39);
    }
    processBoolean(b, 39);
    return this;
  }
  public TraceBuilder exponentBitAccumulator(BigInteger b) {

    if (filled.get(40)) {
      throw new IllegalStateException("EXPONENT_BIT_ACCUMULATOR already set");
    } else {
      filled.set(40);
    }
    processBigInteger(b, 40);
    return this;
  }
  public TraceBuilder exponentBitSource(Boolean b) {

    if (filled.get(41)) {
      throw new IllegalStateException("EXPONENT_BIT_SOURCE already set");
    } else {
      filled.set(41);
    }
    processBoolean(b, 41);
    return this;
  }
  public TraceBuilder instruction(BigInteger b) {

    if (filled.get(42)) {
      throw new IllegalStateException("INSTRUCTION already set");
    } else {
      filled.set(42);
    }
    processBigInteger(b, 42);
    return this;
  }
  public TraceBuilder mulStamp(BigInteger b) {

    if (filled.get(43)) {
      throw new IllegalStateException("MUL_STAMP already set");
    } else {
      filled.set(43);
    }
    processBigInteger(b, 43);
    return this;
  }
  public TraceBuilder oli(Boolean b) {

    if (filled.get(44)) {
      throw new IllegalStateException("OLI already set");
    } else {
      filled.set(44);
    }
    processBoolean(b, 44);
    return this;
  }
  public TraceBuilder resHi(BigInteger b) {

    if (filled.get(46)) {
      throw new IllegalStateException("RES_HI already set");
    } else {
      filled.set(46);
    }
    processBigInteger(b, 46);
    return this;
  }
  public TraceBuilder resLo(BigInteger b) {

    if (filled.get(47)) {
      throw new IllegalStateException("RES_LO already set");
    } else {
      filled.set(47);
    }
    processBigInteger(b, 47);
    return this;
  }
  public TraceBuilder resultVanishes(Boolean b) {

    if (filled.get(45)) {
      throw new IllegalStateException("RESULT_VANISHES already set");
    } else {
      filled.set(45);
    }
    processBoolean(b, 45);
    return this;
  }
  public TraceBuilder squareAndMultiply(Boolean b) {

    if (filled.get(48)) {
      throw new IllegalStateException("SQUARE_AND_MULTIPLY already set");
    } else {
      filled.set(48);
    }
    processBoolean(b, 48);
    return this;
  }
  public TraceBuilder tinyBase(Boolean b) {

    if (filled.get(49)) {
      throw new IllegalStateException("TINY_BASE already set");
    } else {
      filled.set(49);
    }
    processBoolean(b, 49);
    return this;
  }
  public TraceBuilder tinyExponent(Boolean b) {

    if (filled.get(50)) {
      throw new IllegalStateException("TINY_EXPONENT already set");
    } else {
      filled.set(50);
    }
    processBoolean(b, 50);
    return this;
  }

  public void validateRowAndFlush()  throws IOException {
    if (!filled.get(0)) {
      throw new IllegalStateException("ACC_A_0 has not been filled");
    }

    if (!filled.get(1)) {
      throw new IllegalStateException("ACC_A_1 has not been filled");
    }

    if (!filled.get(2)) {
      throw new IllegalStateException("ACC_A_2 has not been filled");
    }

    if (!filled.get(3)) {
      throw new IllegalStateException("ACC_A_3 has not been filled");
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
      throw new IllegalStateException("ACC_C_0 has not been filled");
    }

    if (!filled.get(9)) {
      throw new IllegalStateException("ACC_C_1 has not been filled");
    }

    if (!filled.get(10)) {
      throw new IllegalStateException("ACC_C_2 has not been filled");
    }

    if (!filled.get(11)) {
      throw new IllegalStateException("ACC_C_3 has not been filled");
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
      throw new IllegalStateException("ACC_H_3 has not been filled");
    }

    if (!filled.get(16)) {
      throw new IllegalStateException("ARG_1_HI has not been filled");
    }

    if (!filled.get(17)) {
      throw new IllegalStateException("ARG_1_LO has not been filled");
    }

    if (!filled.get(18)) {
      throw new IllegalStateException("ARG_2_HI has not been filled");
    }

    if (!filled.get(19)) {
      throw new IllegalStateException("ARG_2_LO has not been filled");
    }

    if (!filled.get(20)) {
      throw new IllegalStateException("BITS has not been filled");
    }

    if (!filled.get(21)) {
      throw new IllegalStateException("BIT_NUM has not been filled");
    }

    if (!filled.get(22)) {
      throw new IllegalStateException("BYTE_A_0 has not been filled");
    }

    if (!filled.get(23)) {
      throw new IllegalStateException("BYTE_A_1 has not been filled");
    }

    if (!filled.get(24)) {
      throw new IllegalStateException("BYTE_A_2 has not been filled");
    }

    if (!filled.get(25)) {
      throw new IllegalStateException("BYTE_A_3 has not been filled");
    }

    if (!filled.get(26)) {
      throw new IllegalStateException("BYTE_B_0 has not been filled");
    }

    if (!filled.get(27)) {
      throw new IllegalStateException("BYTE_B_1 has not been filled");
    }

    if (!filled.get(28)) {
      throw new IllegalStateException("BYTE_B_2 has not been filled");
    }

    if (!filled.get(29)) {
      throw new IllegalStateException("BYTE_B_3 has not been filled");
    }

    if (!filled.get(30)) {
      throw new IllegalStateException("BYTE_C_0 has not been filled");
    }

    if (!filled.get(31)) {
      throw new IllegalStateException("BYTE_C_1 has not been filled");
    }

    if (!filled.get(32)) {
      throw new IllegalStateException("BYTE_C_2 has not been filled");
    }

    if (!filled.get(33)) {
      throw new IllegalStateException("BYTE_C_3 has not been filled");
    }

    if (!filled.get(34)) {
      throw new IllegalStateException("BYTE_H_0 has not been filled");
    }

    if (!filled.get(35)) {
      throw new IllegalStateException("BYTE_H_1 has not been filled");
    }

    if (!filled.get(36)) {
      throw new IllegalStateException("BYTE_H_2 has not been filled");
    }

    if (!filled.get(37)) {
      throw new IllegalStateException("BYTE_H_3 has not been filled");
    }

    if (!filled.get(38)) {
      throw new IllegalStateException("COUNTER has not been filled");
    }

    if (!filled.get(39)) {
      throw new IllegalStateException("EXPONENT_BIT has not been filled");
    }

    if (!filled.get(40)) {
      throw new IllegalStateException("EXPONENT_BIT_ACCUMULATOR has not been filled");
    }

    if (!filled.get(41)) {
      throw new IllegalStateException("EXPONENT_BIT_SOURCE has not been filled");
    }

    if (!filled.get(42)) {
      throw new IllegalStateException("INSTRUCTION has not been filled");
    }

    if (!filled.get(43)) {
      throw new IllegalStateException("MUL_STAMP has not been filled");
    }

    if (!filled.get(44)) {
      throw new IllegalStateException("OLI has not been filled");
    }

    if (!filled.get(45)) {
      throw new IllegalStateException("RESULT_VANISHES has not been filled");
    }

    if (!filled.get(46)) {
      throw new IllegalStateException("RES_HI has not been filled");
    }

    if (!filled.get(47)) {
      throw new IllegalStateException("RES_LO has not been filled");
    }

    if (!filled.get(48)) {
      throw new IllegalStateException("SQUARE_AND_MULTIPLY has not been filled");
    }

    if (!filled.get(49)) {
      throw new IllegalStateException("TINY_BASE has not been filled");
    }

    if (!filled.get(50)) {
      throw new IllegalStateException("TINY_EXPONENT has not been filled");
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
