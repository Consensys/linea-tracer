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

package net.consensys.linea.zktracer.module.ext;

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
  public TraceBuilder accDelta0(BigInteger b) {

    if (filled.get(12)) {
      throw new IllegalStateException("ACC_DELTA_0 already set");
    } else {
      filled.set(12);
    }
    processBigInteger(b, 12);
    return this;
  }
  public TraceBuilder accDelta1(BigInteger b) {

    if (filled.get(13)) {
      throw new IllegalStateException("ACC_DELTA_1 already set");
    } else {
      filled.set(13);
    }
    processBigInteger(b, 13);
    return this;
  }
  public TraceBuilder accDelta2(BigInteger b) {

    if (filled.get(14)) {
      throw new IllegalStateException("ACC_DELTA_2 already set");
    } else {
      filled.set(14);
    }
    processBigInteger(b, 14);
    return this;
  }
  public TraceBuilder accDelta3(BigInteger b) {

    if (filled.get(15)) {
      throw new IllegalStateException("ACC_DELTA_3 already set");
    } else {
      filled.set(15);
    }
    processBigInteger(b, 15);
    return this;
  }
  public TraceBuilder accH0(BigInteger b) {

    if (filled.get(16)) {
      throw new IllegalStateException("ACC_H_0 already set");
    } else {
      filled.set(16);
    }
    processBigInteger(b, 16);
    return this;
  }
  public TraceBuilder accH1(BigInteger b) {

    if (filled.get(17)) {
      throw new IllegalStateException("ACC_H_1 already set");
    } else {
      filled.set(17);
    }
    processBigInteger(b, 17);
    return this;
  }
  public TraceBuilder accH2(BigInteger b) {

    if (filled.get(18)) {
      throw new IllegalStateException("ACC_H_2 already set");
    } else {
      filled.set(18);
    }
    processBigInteger(b, 18);
    return this;
  }
  public TraceBuilder accH3(BigInteger b) {

    if (filled.get(19)) {
      throw new IllegalStateException("ACC_H_3 already set");
    } else {
      filled.set(19);
    }
    processBigInteger(b, 19);
    return this;
  }
  public TraceBuilder accH4(BigInteger b) {

    if (filled.get(20)) {
      throw new IllegalStateException("ACC_H_4 already set");
    } else {
      filled.set(20);
    }
    processBigInteger(b, 20);
    return this;
  }
  public TraceBuilder accH5(BigInteger b) {

    if (filled.get(21)) {
      throw new IllegalStateException("ACC_H_5 already set");
    } else {
      filled.set(21);
    }
    processBigInteger(b, 21);
    return this;
  }
  public TraceBuilder accI0(BigInteger b) {

    if (filled.get(22)) {
      throw new IllegalStateException("ACC_I_0 already set");
    } else {
      filled.set(22);
    }
    processBigInteger(b, 22);
    return this;
  }
  public TraceBuilder accI1(BigInteger b) {

    if (filled.get(23)) {
      throw new IllegalStateException("ACC_I_1 already set");
    } else {
      filled.set(23);
    }
    processBigInteger(b, 23);
    return this;
  }
  public TraceBuilder accI2(BigInteger b) {

    if (filled.get(24)) {
      throw new IllegalStateException("ACC_I_2 already set");
    } else {
      filled.set(24);
    }
    processBigInteger(b, 24);
    return this;
  }
  public TraceBuilder accI3(BigInteger b) {

    if (filled.get(25)) {
      throw new IllegalStateException("ACC_I_3 already set");
    } else {
      filled.set(25);
    }
    processBigInteger(b, 25);
    return this;
  }
  public TraceBuilder accI4(BigInteger b) {

    if (filled.get(26)) {
      throw new IllegalStateException("ACC_I_4 already set");
    } else {
      filled.set(26);
    }
    processBigInteger(b, 26);
    return this;
  }
  public TraceBuilder accI5(BigInteger b) {

    if (filled.get(27)) {
      throw new IllegalStateException("ACC_I_5 already set");
    } else {
      filled.set(27);
    }
    processBigInteger(b, 27);
    return this;
  }
  public TraceBuilder accI6(BigInteger b) {

    if (filled.get(28)) {
      throw new IllegalStateException("ACC_I_6 already set");
    } else {
      filled.set(28);
    }
    processBigInteger(b, 28);
    return this;
  }
  public TraceBuilder accJ0(BigInteger b) {

    if (filled.get(29)) {
      throw new IllegalStateException("ACC_J_0 already set");
    } else {
      filled.set(29);
    }
    processBigInteger(b, 29);
    return this;
  }
  public TraceBuilder accJ1(BigInteger b) {

    if (filled.get(30)) {
      throw new IllegalStateException("ACC_J_1 already set");
    } else {
      filled.set(30);
    }
    processBigInteger(b, 30);
    return this;
  }
  public TraceBuilder accJ2(BigInteger b) {

    if (filled.get(31)) {
      throw new IllegalStateException("ACC_J_2 already set");
    } else {
      filled.set(31);
    }
    processBigInteger(b, 31);
    return this;
  }
  public TraceBuilder accJ3(BigInteger b) {

    if (filled.get(32)) {
      throw new IllegalStateException("ACC_J_3 already set");
    } else {
      filled.set(32);
    }
    processBigInteger(b, 32);
    return this;
  }
  public TraceBuilder accJ4(BigInteger b) {

    if (filled.get(33)) {
      throw new IllegalStateException("ACC_J_4 already set");
    } else {
      filled.set(33);
    }
    processBigInteger(b, 33);
    return this;
  }
  public TraceBuilder accJ5(BigInteger b) {

    if (filled.get(34)) {
      throw new IllegalStateException("ACC_J_5 already set");
    } else {
      filled.set(34);
    }
    processBigInteger(b, 34);
    return this;
  }
  public TraceBuilder accJ6(BigInteger b) {

    if (filled.get(35)) {
      throw new IllegalStateException("ACC_J_6 already set");
    } else {
      filled.set(35);
    }
    processBigInteger(b, 35);
    return this;
  }
  public TraceBuilder accJ7(BigInteger b) {

    if (filled.get(36)) {
      throw new IllegalStateException("ACC_J_7 already set");
    } else {
      filled.set(36);
    }
    processBigInteger(b, 36);
    return this;
  }
  public TraceBuilder accQ0(BigInteger b) {

    if (filled.get(37)) {
      throw new IllegalStateException("ACC_Q_0 already set");
    } else {
      filled.set(37);
    }
    processBigInteger(b, 37);
    return this;
  }
  public TraceBuilder accQ1(BigInteger b) {

    if (filled.get(38)) {
      throw new IllegalStateException("ACC_Q_1 already set");
    } else {
      filled.set(38);
    }
    processBigInteger(b, 38);
    return this;
  }
  public TraceBuilder accQ2(BigInteger b) {

    if (filled.get(39)) {
      throw new IllegalStateException("ACC_Q_2 already set");
    } else {
      filled.set(39);
    }
    processBigInteger(b, 39);
    return this;
  }
  public TraceBuilder accQ3(BigInteger b) {

    if (filled.get(40)) {
      throw new IllegalStateException("ACC_Q_3 already set");
    } else {
      filled.set(40);
    }
    processBigInteger(b, 40);
    return this;
  }
  public TraceBuilder accQ4(BigInteger b) {

    if (filled.get(41)) {
      throw new IllegalStateException("ACC_Q_4 already set");
    } else {
      filled.set(41);
    }
    processBigInteger(b, 41);
    return this;
  }
  public TraceBuilder accQ5(BigInteger b) {

    if (filled.get(42)) {
      throw new IllegalStateException("ACC_Q_5 already set");
    } else {
      filled.set(42);
    }
    processBigInteger(b, 42);
    return this;
  }
  public TraceBuilder accQ6(BigInteger b) {

    if (filled.get(43)) {
      throw new IllegalStateException("ACC_Q_6 already set");
    } else {
      filled.set(43);
    }
    processBigInteger(b, 43);
    return this;
  }
  public TraceBuilder accQ7(BigInteger b) {

    if (filled.get(44)) {
      throw new IllegalStateException("ACC_Q_7 already set");
    } else {
      filled.set(44);
    }
    processBigInteger(b, 44);
    return this;
  }
  public TraceBuilder accR0(BigInteger b) {

    if (filled.get(45)) {
      throw new IllegalStateException("ACC_R_0 already set");
    } else {
      filled.set(45);
    }
    processBigInteger(b, 45);
    return this;
  }
  public TraceBuilder accR1(BigInteger b) {

    if (filled.get(46)) {
      throw new IllegalStateException("ACC_R_1 already set");
    } else {
      filled.set(46);
    }
    processBigInteger(b, 46);
    return this;
  }
  public TraceBuilder accR2(BigInteger b) {

    if (filled.get(47)) {
      throw new IllegalStateException("ACC_R_2 already set");
    } else {
      filled.set(47);
    }
    processBigInteger(b, 47);
    return this;
  }
  public TraceBuilder accR3(BigInteger b) {

    if (filled.get(48)) {
      throw new IllegalStateException("ACC_R_3 already set");
    } else {
      filled.set(48);
    }
    processBigInteger(b, 48);
    return this;
  }
  public TraceBuilder arg1Hi(BigInteger b) {

    if (filled.get(49)) {
      throw new IllegalStateException("ARG_1_HI already set");
    } else {
      filled.set(49);
    }
    processBigInteger(b, 49);
    return this;
  }
  public TraceBuilder arg1Lo(BigInteger b) {

    if (filled.get(50)) {
      throw new IllegalStateException("ARG_1_LO already set");
    } else {
      filled.set(50);
    }
    processBigInteger(b, 50);
    return this;
  }
  public TraceBuilder arg2Hi(BigInteger b) {

    if (filled.get(51)) {
      throw new IllegalStateException("ARG_2_HI already set");
    } else {
      filled.set(51);
    }
    processBigInteger(b, 51);
    return this;
  }
  public TraceBuilder arg2Lo(BigInteger b) {

    if (filled.get(52)) {
      throw new IllegalStateException("ARG_2_LO already set");
    } else {
      filled.set(52);
    }
    processBigInteger(b, 52);
    return this;
  }
  public TraceBuilder arg3Hi(BigInteger b) {

    if (filled.get(53)) {
      throw new IllegalStateException("ARG_3_HI already set");
    } else {
      filled.set(53);
    }
    processBigInteger(b, 53);
    return this;
  }
  public TraceBuilder arg3Lo(BigInteger b) {

    if (filled.get(54)) {
      throw new IllegalStateException("ARG_3_LO already set");
    } else {
      filled.set(54);
    }
    processBigInteger(b, 54);
    return this;
  }
  public TraceBuilder bit1(Boolean b) {

    if (filled.get(55)) {
      throw new IllegalStateException("BIT_1 already set");
    } else {
      filled.set(55);
    }
    processBoolean(b, 55);
    return this;
  }
  public TraceBuilder bit2(Boolean b) {

    if (filled.get(56)) {
      throw new IllegalStateException("BIT_2 already set");
    } else {
      filled.set(56);
    }
    processBoolean(b, 56);
    return this;
  }
  public TraceBuilder bit3(Boolean b) {

    if (filled.get(57)) {
      throw new IllegalStateException("BIT_3 already set");
    } else {
      filled.set(57);
    }
    processBoolean(b, 57);
    return this;
  }
  public TraceBuilder byteA0(UnsignedByte b) {

    if (filled.get(58)) {
      throw new IllegalStateException("BYTE_A_0 already set");
    } else {
      filled.set(58);
    }
    processUnsignedByte(b, 58);
    return this;
  }
  public TraceBuilder byteA1(UnsignedByte b) {

    if (filled.get(59)) {
      throw new IllegalStateException("BYTE_A_1 already set");
    } else {
      filled.set(59);
    }
    processUnsignedByte(b, 59);
    return this;
  }
  public TraceBuilder byteA2(UnsignedByte b) {

    if (filled.get(60)) {
      throw new IllegalStateException("BYTE_A_2 already set");
    } else {
      filled.set(60);
    }
    processUnsignedByte(b, 60);
    return this;
  }
  public TraceBuilder byteA3(UnsignedByte b) {

    if (filled.get(61)) {
      throw new IllegalStateException("BYTE_A_3 already set");
    } else {
      filled.set(61);
    }
    processUnsignedByte(b, 61);
    return this;
  }
  public TraceBuilder byteB0(UnsignedByte b) {

    if (filled.get(62)) {
      throw new IllegalStateException("BYTE_B_0 already set");
    } else {
      filled.set(62);
    }
    processUnsignedByte(b, 62);
    return this;
  }
  public TraceBuilder byteB1(UnsignedByte b) {

    if (filled.get(63)) {
      throw new IllegalStateException("BYTE_B_1 already set");
    } else {
      filled.set(63);
    }
    processUnsignedByte(b, 63);
    return this;
  }
  public TraceBuilder byteB2(UnsignedByte b) {

    if (filled.get(64)) {
      throw new IllegalStateException("BYTE_B_2 already set");
    } else {
      filled.set(64);
    }
    processUnsignedByte(b, 64);
    return this;
  }
  public TraceBuilder byteB3(UnsignedByte b) {

    if (filled.get(65)) {
      throw new IllegalStateException("BYTE_B_3 already set");
    } else {
      filled.set(65);
    }
    processUnsignedByte(b, 65);
    return this;
  }
  public TraceBuilder byteC0(UnsignedByte b) {

    if (filled.get(66)) {
      throw new IllegalStateException("BYTE_C_0 already set");
    } else {
      filled.set(66);
    }
    processUnsignedByte(b, 66);
    return this;
  }
  public TraceBuilder byteC1(UnsignedByte b) {

    if (filled.get(67)) {
      throw new IllegalStateException("BYTE_C_1 already set");
    } else {
      filled.set(67);
    }
    processUnsignedByte(b, 67);
    return this;
  }
  public TraceBuilder byteC2(UnsignedByte b) {

    if (filled.get(68)) {
      throw new IllegalStateException("BYTE_C_2 already set");
    } else {
      filled.set(68);
    }
    processUnsignedByte(b, 68);
    return this;
  }
  public TraceBuilder byteC3(UnsignedByte b) {

    if (filled.get(69)) {
      throw new IllegalStateException("BYTE_C_3 already set");
    } else {
      filled.set(69);
    }
    processUnsignedByte(b, 69);
    return this;
  }
  public TraceBuilder byteDelta0(UnsignedByte b) {

    if (filled.get(70)) {
      throw new IllegalStateException("BYTE_DELTA_0 already set");
    } else {
      filled.set(70);
    }
    processUnsignedByte(b, 70);
    return this;
  }
  public TraceBuilder byteDelta1(UnsignedByte b) {

    if (filled.get(71)) {
      throw new IllegalStateException("BYTE_DELTA_1 already set");
    } else {
      filled.set(71);
    }
    processUnsignedByte(b, 71);
    return this;
  }
  public TraceBuilder byteDelta2(UnsignedByte b) {

    if (filled.get(72)) {
      throw new IllegalStateException("BYTE_DELTA_2 already set");
    } else {
      filled.set(72);
    }
    processUnsignedByte(b, 72);
    return this;
  }
  public TraceBuilder byteDelta3(UnsignedByte b) {

    if (filled.get(73)) {
      throw new IllegalStateException("BYTE_DELTA_3 already set");
    } else {
      filled.set(73);
    }
    processUnsignedByte(b, 73);
    return this;
  }
  public TraceBuilder byteH0(UnsignedByte b) {

    if (filled.get(74)) {
      throw new IllegalStateException("BYTE_H_0 already set");
    } else {
      filled.set(74);
    }
    processUnsignedByte(b, 74);
    return this;
  }
  public TraceBuilder byteH1(UnsignedByte b) {

    if (filled.get(75)) {
      throw new IllegalStateException("BYTE_H_1 already set");
    } else {
      filled.set(75);
    }
    processUnsignedByte(b, 75);
    return this;
  }
  public TraceBuilder byteH2(UnsignedByte b) {

    if (filled.get(76)) {
      throw new IllegalStateException("BYTE_H_2 already set");
    } else {
      filled.set(76);
    }
    processUnsignedByte(b, 76);
    return this;
  }
  public TraceBuilder byteH3(UnsignedByte b) {

    if (filled.get(77)) {
      throw new IllegalStateException("BYTE_H_3 already set");
    } else {
      filled.set(77);
    }
    processUnsignedByte(b, 77);
    return this;
  }
  public TraceBuilder byteH4(UnsignedByte b) {

    if (filled.get(78)) {
      throw new IllegalStateException("BYTE_H_4 already set");
    } else {
      filled.set(78);
    }
    processUnsignedByte(b, 78);
    return this;
  }
  public TraceBuilder byteH5(UnsignedByte b) {

    if (filled.get(79)) {
      throw new IllegalStateException("BYTE_H_5 already set");
    } else {
      filled.set(79);
    }
    processUnsignedByte(b, 79);
    return this;
  }
  public TraceBuilder byteI0(UnsignedByte b) {

    if (filled.get(80)) {
      throw new IllegalStateException("BYTE_I_0 already set");
    } else {
      filled.set(80);
    }
    processUnsignedByte(b, 80);
    return this;
  }
  public TraceBuilder byteI1(UnsignedByte b) {

    if (filled.get(81)) {
      throw new IllegalStateException("BYTE_I_1 already set");
    } else {
      filled.set(81);
    }
    processUnsignedByte(b, 81);
    return this;
  }
  public TraceBuilder byteI2(UnsignedByte b) {

    if (filled.get(82)) {
      throw new IllegalStateException("BYTE_I_2 already set");
    } else {
      filled.set(82);
    }
    processUnsignedByte(b, 82);
    return this;
  }
  public TraceBuilder byteI3(UnsignedByte b) {

    if (filled.get(83)) {
      throw new IllegalStateException("BYTE_I_3 already set");
    } else {
      filled.set(83);
    }
    processUnsignedByte(b, 83);
    return this;
  }
  public TraceBuilder byteI4(UnsignedByte b) {

    if (filled.get(84)) {
      throw new IllegalStateException("BYTE_I_4 already set");
    } else {
      filled.set(84);
    }
    processUnsignedByte(b, 84);
    return this;
  }
  public TraceBuilder byteI5(UnsignedByte b) {

    if (filled.get(85)) {
      throw new IllegalStateException("BYTE_I_5 already set");
    } else {
      filled.set(85);
    }
    processUnsignedByte(b, 85);
    return this;
  }
  public TraceBuilder byteI6(UnsignedByte b) {

    if (filled.get(86)) {
      throw new IllegalStateException("BYTE_I_6 already set");
    } else {
      filled.set(86);
    }
    processUnsignedByte(b, 86);
    return this;
  }
  public TraceBuilder byteJ0(UnsignedByte b) {

    if (filled.get(87)) {
      throw new IllegalStateException("BYTE_J_0 already set");
    } else {
      filled.set(87);
    }
    processUnsignedByte(b, 87);
    return this;
  }
  public TraceBuilder byteJ1(UnsignedByte b) {

    if (filled.get(88)) {
      throw new IllegalStateException("BYTE_J_1 already set");
    } else {
      filled.set(88);
    }
    processUnsignedByte(b, 88);
    return this;
  }
  public TraceBuilder byteJ2(UnsignedByte b) {

    if (filled.get(89)) {
      throw new IllegalStateException("BYTE_J_2 already set");
    } else {
      filled.set(89);
    }
    processUnsignedByte(b, 89);
    return this;
  }
  public TraceBuilder byteJ3(UnsignedByte b) {

    if (filled.get(90)) {
      throw new IllegalStateException("BYTE_J_3 already set");
    } else {
      filled.set(90);
    }
    processUnsignedByte(b, 90);
    return this;
  }
  public TraceBuilder byteJ4(UnsignedByte b) {

    if (filled.get(91)) {
      throw new IllegalStateException("BYTE_J_4 already set");
    } else {
      filled.set(91);
    }
    processUnsignedByte(b, 91);
    return this;
  }
  public TraceBuilder byteJ5(UnsignedByte b) {

    if (filled.get(92)) {
      throw new IllegalStateException("BYTE_J_5 already set");
    } else {
      filled.set(92);
    }
    processUnsignedByte(b, 92);
    return this;
  }
  public TraceBuilder byteJ6(UnsignedByte b) {

    if (filled.get(93)) {
      throw new IllegalStateException("BYTE_J_6 already set");
    } else {
      filled.set(93);
    }
    processUnsignedByte(b, 93);
    return this;
  }
  public TraceBuilder byteJ7(UnsignedByte b) {

    if (filled.get(94)) {
      throw new IllegalStateException("BYTE_J_7 already set");
    } else {
      filled.set(94);
    }
    processUnsignedByte(b, 94);
    return this;
  }
  public TraceBuilder byteQ0(UnsignedByte b) {

    if (filled.get(95)) {
      throw new IllegalStateException("BYTE_Q_0 already set");
    } else {
      filled.set(95);
    }
    processUnsignedByte(b, 95);
    return this;
  }
  public TraceBuilder byteQ1(UnsignedByte b) {

    if (filled.get(96)) {
      throw new IllegalStateException("BYTE_Q_1 already set");
    } else {
      filled.set(96);
    }
    processUnsignedByte(b, 96);
    return this;
  }
  public TraceBuilder byteQ2(UnsignedByte b) {

    if (filled.get(97)) {
      throw new IllegalStateException("BYTE_Q_2 already set");
    } else {
      filled.set(97);
    }
    processUnsignedByte(b, 97);
    return this;
  }
  public TraceBuilder byteQ3(UnsignedByte b) {

    if (filled.get(98)) {
      throw new IllegalStateException("BYTE_Q_3 already set");
    } else {
      filled.set(98);
    }
    processUnsignedByte(b, 98);
    return this;
  }
  public TraceBuilder byteQ4(UnsignedByte b) {

    if (filled.get(99)) {
      throw new IllegalStateException("BYTE_Q_4 already set");
    } else {
      filled.set(99);
    }
    processUnsignedByte(b, 99);
    return this;
  }
  public TraceBuilder byteQ5(UnsignedByte b) {

    if (filled.get(100)) {
      throw new IllegalStateException("BYTE_Q_5 already set");
    } else {
      filled.set(100);
    }
    processUnsignedByte(b, 100);
    return this;
  }
  public TraceBuilder byteQ6(UnsignedByte b) {

    if (filled.get(101)) {
      throw new IllegalStateException("BYTE_Q_6 already set");
    } else {
      filled.set(101);
    }
    processUnsignedByte(b, 101);
    return this;
  }
  public TraceBuilder byteQ7(UnsignedByte b) {

    if (filled.get(102)) {
      throw new IllegalStateException("BYTE_Q_7 already set");
    } else {
      filled.set(102);
    }
    processUnsignedByte(b, 102);
    return this;
  }
  public TraceBuilder byteR0(UnsignedByte b) {

    if (filled.get(103)) {
      throw new IllegalStateException("BYTE_R_0 already set");
    } else {
      filled.set(103);
    }
    processUnsignedByte(b, 103);
    return this;
  }
  public TraceBuilder byteR1(UnsignedByte b) {

    if (filled.get(104)) {
      throw new IllegalStateException("BYTE_R_1 already set");
    } else {
      filled.set(104);
    }
    processUnsignedByte(b, 104);
    return this;
  }
  public TraceBuilder byteR2(UnsignedByte b) {

    if (filled.get(105)) {
      throw new IllegalStateException("BYTE_R_2 already set");
    } else {
      filled.set(105);
    }
    processUnsignedByte(b, 105);
    return this;
  }
  public TraceBuilder byteR3(UnsignedByte b) {

    if (filled.get(106)) {
      throw new IllegalStateException("BYTE_R_3 already set");
    } else {
      filled.set(106);
    }
    processUnsignedByte(b, 106);
    return this;
  }
  public TraceBuilder cmp(Boolean b) {

    if (filled.get(107)) {
      throw new IllegalStateException("CMP already set");
    } else {
      filled.set(107);
    }
    processBoolean(b, 107);
    return this;
  }
  public TraceBuilder ct(BigInteger b) {

    if (filled.get(108)) {
      throw new IllegalStateException("CT already set");
    } else {
      filled.set(108);
    }
    processBigInteger(b, 108);
    return this;
  }
  public TraceBuilder inst(BigInteger b) {

    if (filled.get(109)) {
      throw new IllegalStateException("INST already set");
    } else {
      filled.set(109);
    }
    processBigInteger(b, 109);
    return this;
  }
  public TraceBuilder ofH(Boolean b) {

    if (filled.get(110)) {
      throw new IllegalStateException("OF_H already set");
    } else {
      filled.set(110);
    }
    processBoolean(b, 110);
    return this;
  }
  public TraceBuilder ofI(Boolean b) {

    if (filled.get(111)) {
      throw new IllegalStateException("OF_I already set");
    } else {
      filled.set(111);
    }
    processBoolean(b, 111);
    return this;
  }
  public TraceBuilder ofJ(Boolean b) {

    if (filled.get(112)) {
      throw new IllegalStateException("OF_J already set");
    } else {
      filled.set(112);
    }
    processBoolean(b, 112);
    return this;
  }
  public TraceBuilder ofRes(Boolean b) {

    if (filled.get(113)) {
      throw new IllegalStateException("OF_RES already set");
    } else {
      filled.set(113);
    }
    processBoolean(b, 113);
    return this;
  }
  public TraceBuilder oli(Boolean b) {

    if (filled.get(114)) {
      throw new IllegalStateException("OLI already set");
    } else {
      filled.set(114);
    }
    processBoolean(b, 114);
    return this;
  }
  public TraceBuilder resHi(BigInteger b) {

    if (filled.get(115)) {
      throw new IllegalStateException("RES_HI already set");
    } else {
      filled.set(115);
    }
    processBigInteger(b, 115);
    return this;
  }
  public TraceBuilder resLo(BigInteger b) {

    if (filled.get(116)) {
      throw new IllegalStateException("RES_LO already set");
    } else {
      filled.set(116);
    }
    processBigInteger(b, 116);
    return this;
  }
  public TraceBuilder stamp(BigInteger b) {

    if (filled.get(117)) {
      throw new IllegalStateException("STAMP already set");
    } else {
      filled.set(117);
    }
    processBigInteger(b, 117);
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
      throw new IllegalStateException("ACC_DELTA_0 has not been filled");
    }

    if (!filled.get(13)) {
      throw new IllegalStateException("ACC_DELTA_1 has not been filled");
    }

    if (!filled.get(14)) {
      throw new IllegalStateException("ACC_DELTA_2 has not been filled");
    }

    if (!filled.get(15)) {
      throw new IllegalStateException("ACC_DELTA_3 has not been filled");
    }

    if (!filled.get(16)) {
      throw new IllegalStateException("ACC_H_0 has not been filled");
    }

    if (!filled.get(17)) {
      throw new IllegalStateException("ACC_H_1 has not been filled");
    }

    if (!filled.get(18)) {
      throw new IllegalStateException("ACC_H_2 has not been filled");
    }

    if (!filled.get(19)) {
      throw new IllegalStateException("ACC_H_3 has not been filled");
    }

    if (!filled.get(20)) {
      throw new IllegalStateException("ACC_H_4 has not been filled");
    }

    if (!filled.get(21)) {
      throw new IllegalStateException("ACC_H_5 has not been filled");
    }

    if (!filled.get(22)) {
      throw new IllegalStateException("ACC_I_0 has not been filled");
    }

    if (!filled.get(23)) {
      throw new IllegalStateException("ACC_I_1 has not been filled");
    }

    if (!filled.get(24)) {
      throw new IllegalStateException("ACC_I_2 has not been filled");
    }

    if (!filled.get(25)) {
      throw new IllegalStateException("ACC_I_3 has not been filled");
    }

    if (!filled.get(26)) {
      throw new IllegalStateException("ACC_I_4 has not been filled");
    }

    if (!filled.get(27)) {
      throw new IllegalStateException("ACC_I_5 has not been filled");
    }

    if (!filled.get(28)) {
      throw new IllegalStateException("ACC_I_6 has not been filled");
    }

    if (!filled.get(29)) {
      throw new IllegalStateException("ACC_J_0 has not been filled");
    }

    if (!filled.get(30)) {
      throw new IllegalStateException("ACC_J_1 has not been filled");
    }

    if (!filled.get(31)) {
      throw new IllegalStateException("ACC_J_2 has not been filled");
    }

    if (!filled.get(32)) {
      throw new IllegalStateException("ACC_J_3 has not been filled");
    }

    if (!filled.get(33)) {
      throw new IllegalStateException("ACC_J_4 has not been filled");
    }

    if (!filled.get(34)) {
      throw new IllegalStateException("ACC_J_5 has not been filled");
    }

    if (!filled.get(35)) {
      throw new IllegalStateException("ACC_J_6 has not been filled");
    }

    if (!filled.get(36)) {
      throw new IllegalStateException("ACC_J_7 has not been filled");
    }

    if (!filled.get(37)) {
      throw new IllegalStateException("ACC_Q_0 has not been filled");
    }

    if (!filled.get(38)) {
      throw new IllegalStateException("ACC_Q_1 has not been filled");
    }

    if (!filled.get(39)) {
      throw new IllegalStateException("ACC_Q_2 has not been filled");
    }

    if (!filled.get(40)) {
      throw new IllegalStateException("ACC_Q_3 has not been filled");
    }

    if (!filled.get(41)) {
      throw new IllegalStateException("ACC_Q_4 has not been filled");
    }

    if (!filled.get(42)) {
      throw new IllegalStateException("ACC_Q_5 has not been filled");
    }

    if (!filled.get(43)) {
      throw new IllegalStateException("ACC_Q_6 has not been filled");
    }

    if (!filled.get(44)) {
      throw new IllegalStateException("ACC_Q_7 has not been filled");
    }

    if (!filled.get(45)) {
      throw new IllegalStateException("ACC_R_0 has not been filled");
    }

    if (!filled.get(46)) {
      throw new IllegalStateException("ACC_R_1 has not been filled");
    }

    if (!filled.get(47)) {
      throw new IllegalStateException("ACC_R_2 has not been filled");
    }

    if (!filled.get(48)) {
      throw new IllegalStateException("ACC_R_3 has not been filled");
    }

    if (!filled.get(49)) {
      throw new IllegalStateException("ARG_1_HI has not been filled");
    }

    if (!filled.get(50)) {
      throw new IllegalStateException("ARG_1_LO has not been filled");
    }

    if (!filled.get(51)) {
      throw new IllegalStateException("ARG_2_HI has not been filled");
    }

    if (!filled.get(52)) {
      throw new IllegalStateException("ARG_2_LO has not been filled");
    }

    if (!filled.get(53)) {
      throw new IllegalStateException("ARG_3_HI has not been filled");
    }

    if (!filled.get(54)) {
      throw new IllegalStateException("ARG_3_LO has not been filled");
    }

    if (!filled.get(55)) {
      throw new IllegalStateException("BIT_1 has not been filled");
    }

    if (!filled.get(56)) {
      throw new IllegalStateException("BIT_2 has not been filled");
    }

    if (!filled.get(57)) {
      throw new IllegalStateException("BIT_3 has not been filled");
    }

    if (!filled.get(58)) {
      throw new IllegalStateException("BYTE_A_0 has not been filled");
    }

    if (!filled.get(59)) {
      throw new IllegalStateException("BYTE_A_1 has not been filled");
    }

    if (!filled.get(60)) {
      throw new IllegalStateException("BYTE_A_2 has not been filled");
    }

    if (!filled.get(61)) {
      throw new IllegalStateException("BYTE_A_3 has not been filled");
    }

    if (!filled.get(62)) {
      throw new IllegalStateException("BYTE_B_0 has not been filled");
    }

    if (!filled.get(63)) {
      throw new IllegalStateException("BYTE_B_1 has not been filled");
    }

    if (!filled.get(64)) {
      throw new IllegalStateException("BYTE_B_2 has not been filled");
    }

    if (!filled.get(65)) {
      throw new IllegalStateException("BYTE_B_3 has not been filled");
    }

    if (!filled.get(66)) {
      throw new IllegalStateException("BYTE_C_0 has not been filled");
    }

    if (!filled.get(67)) {
      throw new IllegalStateException("BYTE_C_1 has not been filled");
    }

    if (!filled.get(68)) {
      throw new IllegalStateException("BYTE_C_2 has not been filled");
    }

    if (!filled.get(69)) {
      throw new IllegalStateException("BYTE_C_3 has not been filled");
    }

    if (!filled.get(70)) {
      throw new IllegalStateException("BYTE_DELTA_0 has not been filled");
    }

    if (!filled.get(71)) {
      throw new IllegalStateException("BYTE_DELTA_1 has not been filled");
    }

    if (!filled.get(72)) {
      throw new IllegalStateException("BYTE_DELTA_2 has not been filled");
    }

    if (!filled.get(73)) {
      throw new IllegalStateException("BYTE_DELTA_3 has not been filled");
    }

    if (!filled.get(74)) {
      throw new IllegalStateException("BYTE_H_0 has not been filled");
    }

    if (!filled.get(75)) {
      throw new IllegalStateException("BYTE_H_1 has not been filled");
    }

    if (!filled.get(76)) {
      throw new IllegalStateException("BYTE_H_2 has not been filled");
    }

    if (!filled.get(77)) {
      throw new IllegalStateException("BYTE_H_3 has not been filled");
    }

    if (!filled.get(78)) {
      throw new IllegalStateException("BYTE_H_4 has not been filled");
    }

    if (!filled.get(79)) {
      throw new IllegalStateException("BYTE_H_5 has not been filled");
    }

    if (!filled.get(80)) {
      throw new IllegalStateException("BYTE_I_0 has not been filled");
    }

    if (!filled.get(81)) {
      throw new IllegalStateException("BYTE_I_1 has not been filled");
    }

    if (!filled.get(82)) {
      throw new IllegalStateException("BYTE_I_2 has not been filled");
    }

    if (!filled.get(83)) {
      throw new IllegalStateException("BYTE_I_3 has not been filled");
    }

    if (!filled.get(84)) {
      throw new IllegalStateException("BYTE_I_4 has not been filled");
    }

    if (!filled.get(85)) {
      throw new IllegalStateException("BYTE_I_5 has not been filled");
    }

    if (!filled.get(86)) {
      throw new IllegalStateException("BYTE_I_6 has not been filled");
    }

    if (!filled.get(87)) {
      throw new IllegalStateException("BYTE_J_0 has not been filled");
    }

    if (!filled.get(88)) {
      throw new IllegalStateException("BYTE_J_1 has not been filled");
    }

    if (!filled.get(89)) {
      throw new IllegalStateException("BYTE_J_2 has not been filled");
    }

    if (!filled.get(90)) {
      throw new IllegalStateException("BYTE_J_3 has not been filled");
    }

    if (!filled.get(91)) {
      throw new IllegalStateException("BYTE_J_4 has not been filled");
    }

    if (!filled.get(92)) {
      throw new IllegalStateException("BYTE_J_5 has not been filled");
    }

    if (!filled.get(93)) {
      throw new IllegalStateException("BYTE_J_6 has not been filled");
    }

    if (!filled.get(94)) {
      throw new IllegalStateException("BYTE_J_7 has not been filled");
    }

    if (!filled.get(95)) {
      throw new IllegalStateException("BYTE_Q_0 has not been filled");
    }

    if (!filled.get(96)) {
      throw new IllegalStateException("BYTE_Q_1 has not been filled");
    }

    if (!filled.get(97)) {
      throw new IllegalStateException("BYTE_Q_2 has not been filled");
    }

    if (!filled.get(98)) {
      throw new IllegalStateException("BYTE_Q_3 has not been filled");
    }

    if (!filled.get(99)) {
      throw new IllegalStateException("BYTE_Q_4 has not been filled");
    }

    if (!filled.get(100)) {
      throw new IllegalStateException("BYTE_Q_5 has not been filled");
    }

    if (!filled.get(101)) {
      throw new IllegalStateException("BYTE_Q_6 has not been filled");
    }

    if (!filled.get(102)) {
      throw new IllegalStateException("BYTE_Q_7 has not been filled");
    }

    if (!filled.get(103)) {
      throw new IllegalStateException("BYTE_R_0 has not been filled");
    }

    if (!filled.get(104)) {
      throw new IllegalStateException("BYTE_R_1 has not been filled");
    }

    if (!filled.get(105)) {
      throw new IllegalStateException("BYTE_R_2 has not been filled");
    }

    if (!filled.get(106)) {
      throw new IllegalStateException("BYTE_R_3 has not been filled");
    }

    if (!filled.get(107)) {
      throw new IllegalStateException("CMP has not been filled");
    }

    if (!filled.get(108)) {
      throw new IllegalStateException("CT has not been filled");
    }

    if (!filled.get(109)) {
      throw new IllegalStateException("INST has not been filled");
    }

    if (!filled.get(110)) {
      throw new IllegalStateException("OF_H has not been filled");
    }

    if (!filled.get(111)) {
      throw new IllegalStateException("OF_I has not been filled");
    }

    if (!filled.get(112)) {
      throw new IllegalStateException("OF_J has not been filled");
    }

    if (!filled.get(113)) {
      throw new IllegalStateException("OF_RES has not been filled");
    }

    if (!filled.get(114)) {
      throw new IllegalStateException("OLI has not been filled");
    }

    if (!filled.get(115)) {
      throw new IllegalStateException("RES_HI has not been filled");
    }

    if (!filled.get(116)) {
      throw new IllegalStateException("RES_LO has not been filled");
    }

    if (!filled.get(117)) {
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
