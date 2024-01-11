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

package net.consensys.linea.zktracer.module.mmio;

import java.nio.MappedByteBuffer;
import java.util.BitSet;
import java.util.List;

import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;

/**
 * WARNING: This code is generated automatically.
 *
 * <p>Any modifications to this code may be overwritten and could lead to unexpected behavior.
 * Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
public class Trace {
  static final int CALLDATACOPY = 55;
  static final int CALLDATALOAD = 53;
  static final int CODECOPY = 57;
  static final int EXTCODECOPY = 60;
  static final int ExceptionalRamToStack3To2FullFast = 607;
  static final int Exceptional_RamToStack_3To2Full = 627;
  static final int ExoToRam = 602;
  static final int ExoToRamSlideChunk = 616;
  static final int ExoToRamSlideOverlappingChunk = 618;
  static final int FirstFastSecondPadded = 625;
  static final int FirstPaddedSecondZero = 626;
  static final int FullExoFromTwo = 621;
  static final int FullStackToRam = 623;
  static final int KillingOne = 604;
  static final int LIMB_SIZE = 16;
  static final int LIMB_SIZE_MINUS_ONE = 15;
  static final int LsbFromStackToRAM = 624;
  static final int NA_RamToStack_1To1PaddedAndZero = 633;
  static final int NA_RamToStack_2To1FullAndZero = 631;
  static final int NA_RamToStack_2To1PaddedAndZero = 632;
  static final int NA_RamToStack_2To2Padded = 630;
  static final int NA_RamToStack_3To2Full = 628;
  static final int NA_RamToStack_3To2Padded = 629;
  static final int PaddedExoFromOne = 619;
  static final int PaddedExoFromTwo = 620;
  static final int PushOneRamToStack = 606;
  static final int PushTwoRamToStack = 605;
  static final int PushTwoStackToRam = 608;
  static final int RETURNDATACOPY = 62;
  static final int RamIsExo = 603;
  static final int RamLimbExcision = 613;
  static final int RamToRam = 601;
  static final int RamToRamSlideChunk = 614;
  static final int RamToRamSlideOverlappingChunk = 615;
  static final int SMALL_LIMB_SIZE = 4;
  static final int SMALL_LIMB_SIZE_MINUS_ONE = 3;
  static final int StoreXInAThreeRequired = 609;
  static final int StoreXInB = 610;
  static final int StoreXInC = 611;
  static final int tern0 = 0;
  static final int tern1 = 1;
  static final int tern2 = 2;
  static final int type1 = 100;
  static final int type2 = 200;
  static final int type3 = 300;
  static final int type4CC = 401;
  static final int type4CD = 402;
  static final int type4RD = 403;
  static final int type5 = 500;

  private final BitSet filled = new BitSet();
  private int currentLine = 0;

  private final MappedByteBuffer acc1;
  private final MappedByteBuffer acc2;
  private final MappedByteBuffer acc3;
  private final MappedByteBuffer acc4;
  private final MappedByteBuffer acc5;
  private final MappedByteBuffer acc6;
  private final MappedByteBuffer accA;
  private final MappedByteBuffer accB;
  private final MappedByteBuffer accC;
  private final MappedByteBuffer accValHi;
  private final MappedByteBuffer accValLo;
  private final MappedByteBuffer accX;
  private final MappedByteBuffer bin1;
  private final MappedByteBuffer bin2;
  private final MappedByteBuffer bin3;
  private final MappedByteBuffer bin4;
  private final MappedByteBuffer bin5;
  private final MappedByteBuffer byteA;
  private final MappedByteBuffer byteB;
  private final MappedByteBuffer byteC;
  private final MappedByteBuffer byteX;
  private final MappedByteBuffer cnA;
  private final MappedByteBuffer cnB;
  private final MappedByteBuffer cnC;
  private final MappedByteBuffer contextSource;
  private final MappedByteBuffer contextTarget;
  private final MappedByteBuffer counter;
  private final MappedByteBuffer erf;
  private final MappedByteBuffer exoIsHash;
  private final MappedByteBuffer exoIsLog;
  private final MappedByteBuffer exoIsRom;
  private final MappedByteBuffer exoIsTxcd;
  private final MappedByteBuffer fast;
  private final MappedByteBuffer indexA;
  private final MappedByteBuffer indexB;
  private final MappedByteBuffer indexC;
  private final MappedByteBuffer indexX;
  private final MappedByteBuffer isInit;
  private final MappedByteBuffer logNum;
  private final MappedByteBuffer microInstruction;
  private final MappedByteBuffer microInstructionStamp;
  private final MappedByteBuffer pow2561;
  private final MappedByteBuffer pow2562;
  private final MappedByteBuffer size;
  private final MappedByteBuffer sourceByteOffset;
  private final MappedByteBuffer sourceLimbOffset;
  private final MappedByteBuffer stackValueHiByte;
  private final MappedByteBuffer stackValueHigh;
  private final MappedByteBuffer stackValueLoByte;
  private final MappedByteBuffer stackValueLow;
  private final MappedByteBuffer targetByteOffset;
  private final MappedByteBuffer targetLimbOffset;
  private final MappedByteBuffer txNum;
  private final MappedByteBuffer valA;
  private final MappedByteBuffer valANew;
  private final MappedByteBuffer valB;
  private final MappedByteBuffer valBNew;
  private final MappedByteBuffer valC;
  private final MappedByteBuffer valCNew;
  private final MappedByteBuffer valX;

  static List<ColumnHeader> headers(int length) {
    return List.of(
        new ColumnHeader("mmio.ACC_1", 32, length),
        new ColumnHeader("mmio.ACC_2", 32, length),
        new ColumnHeader("mmio.ACC_3", 32, length),
        new ColumnHeader("mmio.ACC_4", 32, length),
        new ColumnHeader("mmio.ACC_5", 32, length),
        new ColumnHeader("mmio.ACC_6", 32, length),
        new ColumnHeader("mmio.ACC_A", 32, length),
        new ColumnHeader("mmio.ACC_B", 32, length),
        new ColumnHeader("mmio.ACC_C", 32, length),
        new ColumnHeader("mmio.ACC_VAL_HI", 32, length),
        new ColumnHeader("mmio.ACC_VAL_LO", 32, length),
        new ColumnHeader("mmio.ACC_X", 32, length),
        new ColumnHeader("mmio.BIN_1", 1, length),
        new ColumnHeader("mmio.BIN_2", 1, length),
        new ColumnHeader("mmio.BIN_3", 1, length),
        new ColumnHeader("mmio.BIN_4", 1, length),
        new ColumnHeader("mmio.BIN_5", 1, length),
        new ColumnHeader("mmio.BYTE_A", 1, length),
        new ColumnHeader("mmio.BYTE_B", 1, length),
        new ColumnHeader("mmio.BYTE_C", 1, length),
        new ColumnHeader("mmio.BYTE_X", 1, length),
        new ColumnHeader("mmio.CN_A", 32, length),
        new ColumnHeader("mmio.CN_B", 32, length),
        new ColumnHeader("mmio.CN_C", 32, length),
        new ColumnHeader("mmio.CONTEXT_SOURCE", 32, length),
        new ColumnHeader("mmio.CONTEXT_TARGET", 32, length),
        new ColumnHeader("mmio.COUNTER", 32, length),
        new ColumnHeader("mmio.ERF", 1, length),
        new ColumnHeader("mmio.EXO_IS_HASH", 1, length),
        new ColumnHeader("mmio.EXO_IS_LOG", 1, length),
        new ColumnHeader("mmio.EXO_IS_ROM", 1, length),
        new ColumnHeader("mmio.EXO_IS_TXCD", 1, length),
        new ColumnHeader("mmio.FAST", 1, length),
        new ColumnHeader("mmio.INDEX_A", 32, length),
        new ColumnHeader("mmio.INDEX_B", 32, length),
        new ColumnHeader("mmio.INDEX_C", 32, length),
        new ColumnHeader("mmio.INDEX_X", 32, length),
        new ColumnHeader("mmio.IS_INIT", 1, length),
        new ColumnHeader("mmio.LOG_NUM", 32, length),
        new ColumnHeader("mmio.MICRO_INSTRUCTION", 32, length),
        new ColumnHeader("mmio.MICRO_INSTRUCTION_STAMP", 32, length),
        new ColumnHeader("mmio.POW_256_1", 32, length),
        new ColumnHeader("mmio.POW_256_2", 32, length),
        new ColumnHeader("mmio.SIZE", 32, length),
        new ColumnHeader("mmio.SOURCE_BYTE_OFFSET", 32, length),
        new ColumnHeader("mmio.SOURCE_LIMB_OFFSET", 32, length),
        new ColumnHeader("mmio.STACK_VALUE_HI_BYTE", 1, length),
        new ColumnHeader("mmio.STACK_VALUE_HIGH", 32, length),
        new ColumnHeader("mmio.STACK_VALUE_LO_BYTE", 1, length),
        new ColumnHeader("mmio.STACK_VALUE_LOW", 32, length),
        new ColumnHeader("mmio.TARGET_BYTE_OFFSET", 32, length),
        new ColumnHeader("mmio.TARGET_LIMB_OFFSET", 32, length),
        new ColumnHeader("mmio.TX_NUM", 32, length),
        new ColumnHeader("mmio.VAL_A", 32, length),
        new ColumnHeader("mmio.VAL_A_NEW", 32, length),
        new ColumnHeader("mmio.VAL_B", 32, length),
        new ColumnHeader("mmio.VAL_B_NEW", 32, length),
        new ColumnHeader("mmio.VAL_C", 32, length),
        new ColumnHeader("mmio.VAL_C_NEW", 32, length),
        new ColumnHeader("mmio.VAL_X", 32, length));
  }

  public Trace(List<MappedByteBuffer> buffers) {
    this.acc1 = buffers.get(0);
    this.acc2 = buffers.get(1);
    this.acc3 = buffers.get(2);
    this.acc4 = buffers.get(3);
    this.acc5 = buffers.get(4);
    this.acc6 = buffers.get(5);
    this.accA = buffers.get(6);
    this.accB = buffers.get(7);
    this.accC = buffers.get(8);
    this.accValHi = buffers.get(9);
    this.accValLo = buffers.get(10);
    this.accX = buffers.get(11);
    this.bin1 = buffers.get(12);
    this.bin2 = buffers.get(13);
    this.bin3 = buffers.get(14);
    this.bin4 = buffers.get(15);
    this.bin5 = buffers.get(16);
    this.byteA = buffers.get(17);
    this.byteB = buffers.get(18);
    this.byteC = buffers.get(19);
    this.byteX = buffers.get(20);
    this.cnA = buffers.get(21);
    this.cnB = buffers.get(22);
    this.cnC = buffers.get(23);
    this.contextSource = buffers.get(24);
    this.contextTarget = buffers.get(25);
    this.counter = buffers.get(26);
    this.erf = buffers.get(27);
    this.exoIsHash = buffers.get(28);
    this.exoIsLog = buffers.get(29);
    this.exoIsRom = buffers.get(30);
    this.exoIsTxcd = buffers.get(31);
    this.fast = buffers.get(32);
    this.indexA = buffers.get(33);
    this.indexB = buffers.get(34);
    this.indexC = buffers.get(35);
    this.indexX = buffers.get(36);
    this.isInit = buffers.get(37);
    this.logNum = buffers.get(38);
    this.microInstruction = buffers.get(39);
    this.microInstructionStamp = buffers.get(40);
    this.pow2561 = buffers.get(41);
    this.pow2562 = buffers.get(42);
    this.size = buffers.get(43);
    this.sourceByteOffset = buffers.get(44);
    this.sourceLimbOffset = buffers.get(45);
    this.stackValueHiByte = buffers.get(46);
    this.stackValueHigh = buffers.get(47);
    this.stackValueLoByte = buffers.get(48);
    this.stackValueLow = buffers.get(49);
    this.targetByteOffset = buffers.get(50);
    this.targetLimbOffset = buffers.get(51);
    this.txNum = buffers.get(52);
    this.valA = buffers.get(53);
    this.valANew = buffers.get(54);
    this.valB = buffers.get(55);
    this.valBNew = buffers.get(56);
    this.valC = buffers.get(57);
    this.valCNew = buffers.get(58);
    this.valX = buffers.get(59);
  }

  public int size() {
    if (!filled.isEmpty()) {
      throw new RuntimeException("Cannot measure a trace with a non-validated row.");
    }

    return this.currentLine;
  }

  public Trace acc1(final Bytes b) {
    if (filled.get(0)) {
      throw new IllegalStateException("mmio.ACC_1 already set");
    } else {
      filled.set(0);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      acc1.put((byte) 0);
    }
    acc1.put(b.toArrayUnsafe());

    return this;
  }

  public Trace acc2(final Bytes b) {
    if (filled.get(1)) {
      throw new IllegalStateException("mmio.ACC_2 already set");
    } else {
      filled.set(1);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      acc2.put((byte) 0);
    }
    acc2.put(b.toArrayUnsafe());

    return this;
  }

  public Trace acc3(final Bytes b) {
    if (filled.get(2)) {
      throw new IllegalStateException("mmio.ACC_3 already set");
    } else {
      filled.set(2);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      acc3.put((byte) 0);
    }
    acc3.put(b.toArrayUnsafe());

    return this;
  }

  public Trace acc4(final Bytes b) {
    if (filled.get(3)) {
      throw new IllegalStateException("mmio.ACC_4 already set");
    } else {
      filled.set(3);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      acc4.put((byte) 0);
    }
    acc4.put(b.toArrayUnsafe());

    return this;
  }

  public Trace acc5(final Bytes b) {
    if (filled.get(4)) {
      throw new IllegalStateException("mmio.ACC_5 already set");
    } else {
      filled.set(4);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      acc5.put((byte) 0);
    }
    acc5.put(b.toArrayUnsafe());

    return this;
  }

  public Trace acc6(final Bytes b) {
    if (filled.get(5)) {
      throw new IllegalStateException("mmio.ACC_6 already set");
    } else {
      filled.set(5);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      acc6.put((byte) 0);
    }
    acc6.put(b.toArrayUnsafe());

    return this;
  }

  public Trace accA(final Bytes b) {
    if (filled.get(6)) {
      throw new IllegalStateException("mmio.ACC_A already set");
    } else {
      filled.set(6);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      accA.put((byte) 0);
    }
    accA.put(b.toArrayUnsafe());

    return this;
  }

  public Trace accB(final Bytes b) {
    if (filled.get(7)) {
      throw new IllegalStateException("mmio.ACC_B already set");
    } else {
      filled.set(7);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      accB.put((byte) 0);
    }
    accB.put(b.toArrayUnsafe());

    return this;
  }

  public Trace accC(final Bytes b) {
    if (filled.get(8)) {
      throw new IllegalStateException("mmio.ACC_C already set");
    } else {
      filled.set(8);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      accC.put((byte) 0);
    }
    accC.put(b.toArrayUnsafe());

    return this;
  }

  public Trace accValHi(final Bytes b) {
    if (filled.get(9)) {
      throw new IllegalStateException("mmio.ACC_VAL_HI already set");
    } else {
      filled.set(9);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      accValHi.put((byte) 0);
    }
    accValHi.put(b.toArrayUnsafe());

    return this;
  }

  public Trace accValLo(final Bytes b) {
    if (filled.get(10)) {
      throw new IllegalStateException("mmio.ACC_VAL_LO already set");
    } else {
      filled.set(10);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      accValLo.put((byte) 0);
    }
    accValLo.put(b.toArrayUnsafe());

    return this;
  }

  public Trace accX(final Bytes b) {
    if (filled.get(11)) {
      throw new IllegalStateException("mmio.ACC_X already set");
    } else {
      filled.set(11);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      accX.put((byte) 0);
    }
    accX.put(b.toArrayUnsafe());

    return this;
  }

  public Trace bin1(final Boolean b) {
    if (filled.get(12)) {
      throw new IllegalStateException("mmio.BIN_1 already set");
    } else {
      filled.set(12);
    }

    bin1.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace bin2(final Boolean b) {
    if (filled.get(13)) {
      throw new IllegalStateException("mmio.BIN_2 already set");
    } else {
      filled.set(13);
    }

    bin2.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace bin3(final Boolean b) {
    if (filled.get(14)) {
      throw new IllegalStateException("mmio.BIN_3 already set");
    } else {
      filled.set(14);
    }

    bin3.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace bin4(final Boolean b) {
    if (filled.get(15)) {
      throw new IllegalStateException("mmio.BIN_4 already set");
    } else {
      filled.set(15);
    }

    bin4.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace bin5(final Boolean b) {
    if (filled.get(16)) {
      throw new IllegalStateException("mmio.BIN_5 already set");
    } else {
      filled.set(16);
    }

    bin5.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace byteA(final UnsignedByte b) {
    if (filled.get(17)) {
      throw new IllegalStateException("mmio.BYTE_A already set");
    } else {
      filled.set(17);
    }

    byteA.put(b.toByte());

    return this;
  }

  public Trace byteB(final UnsignedByte b) {
    if (filled.get(18)) {
      throw new IllegalStateException("mmio.BYTE_B already set");
    } else {
      filled.set(18);
    }

    byteB.put(b.toByte());

    return this;
  }

  public Trace byteC(final UnsignedByte b) {
    if (filled.get(19)) {
      throw new IllegalStateException("mmio.BYTE_C already set");
    } else {
      filled.set(19);
    }

    byteC.put(b.toByte());

    return this;
  }

  public Trace byteX(final UnsignedByte b) {
    if (filled.get(20)) {
      throw new IllegalStateException("mmio.BYTE_X already set");
    } else {
      filled.set(20);
    }

    byteX.put(b.toByte());

    return this;
  }

  public Trace cnA(final Bytes b) {
    if (filled.get(21)) {
      throw new IllegalStateException("mmio.CN_A already set");
    } else {
      filled.set(21);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      cnA.put((byte) 0);
    }
    cnA.put(b.toArrayUnsafe());

    return this;
  }

  public Trace cnB(final Bytes b) {
    if (filled.get(22)) {
      throw new IllegalStateException("mmio.CN_B already set");
    } else {
      filled.set(22);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      cnB.put((byte) 0);
    }
    cnB.put(b.toArrayUnsafe());

    return this;
  }

  public Trace cnC(final Bytes b) {
    if (filled.get(23)) {
      throw new IllegalStateException("mmio.CN_C already set");
    } else {
      filled.set(23);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      cnC.put((byte) 0);
    }
    cnC.put(b.toArrayUnsafe());

    return this;
  }

  public Trace contextSource(final Bytes b) {
    if (filled.get(24)) {
      throw new IllegalStateException("mmio.CONTEXT_SOURCE already set");
    } else {
      filled.set(24);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      contextSource.put((byte) 0);
    }
    contextSource.put(b.toArrayUnsafe());

    return this;
  }

  public Trace contextTarget(final Bytes b) {
    if (filled.get(25)) {
      throw new IllegalStateException("mmio.CONTEXT_TARGET already set");
    } else {
      filled.set(25);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      contextTarget.put((byte) 0);
    }
    contextTarget.put(b.toArrayUnsafe());

    return this;
  }

  public Trace counter(final Bytes b) {
    if (filled.get(26)) {
      throw new IllegalStateException("mmio.COUNTER already set");
    } else {
      filled.set(26);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      counter.put((byte) 0);
    }
    counter.put(b.toArrayUnsafe());

    return this;
  }

  public Trace erf(final Boolean b) {
    if (filled.get(27)) {
      throw new IllegalStateException("mmio.ERF already set");
    } else {
      filled.set(27);
    }

    erf.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace exoIsHash(final Boolean b) {
    if (filled.get(28)) {
      throw new IllegalStateException("mmio.EXO_IS_HASH already set");
    } else {
      filled.set(28);
    }

    exoIsHash.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace exoIsLog(final Boolean b) {
    if (filled.get(29)) {
      throw new IllegalStateException("mmio.EXO_IS_LOG already set");
    } else {
      filled.set(29);
    }

    exoIsLog.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace exoIsRom(final Boolean b) {
    if (filled.get(30)) {
      throw new IllegalStateException("mmio.EXO_IS_ROM already set");
    } else {
      filled.set(30);
    }

    exoIsRom.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace exoIsTxcd(final Boolean b) {
    if (filled.get(31)) {
      throw new IllegalStateException("mmio.EXO_IS_TXCD already set");
    } else {
      filled.set(31);
    }

    exoIsTxcd.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace fast(final Boolean b) {
    if (filled.get(32)) {
      throw new IllegalStateException("mmio.FAST already set");
    } else {
      filled.set(32);
    }

    fast.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace indexA(final Bytes b) {
    if (filled.get(33)) {
      throw new IllegalStateException("mmio.INDEX_A already set");
    } else {
      filled.set(33);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      indexA.put((byte) 0);
    }
    indexA.put(b.toArrayUnsafe());

    return this;
  }

  public Trace indexB(final Bytes b) {
    if (filled.get(34)) {
      throw new IllegalStateException("mmio.INDEX_B already set");
    } else {
      filled.set(34);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      indexB.put((byte) 0);
    }
    indexB.put(b.toArrayUnsafe());

    return this;
  }

  public Trace indexC(final Bytes b) {
    if (filled.get(35)) {
      throw new IllegalStateException("mmio.INDEX_C already set");
    } else {
      filled.set(35);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      indexC.put((byte) 0);
    }
    indexC.put(b.toArrayUnsafe());

    return this;
  }

  public Trace indexX(final Bytes b) {
    if (filled.get(36)) {
      throw new IllegalStateException("mmio.INDEX_X already set");
    } else {
      filled.set(36);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      indexX.put((byte) 0);
    }
    indexX.put(b.toArrayUnsafe());

    return this;
  }

  public Trace isInit(final Boolean b) {
    if (filled.get(37)) {
      throw new IllegalStateException("mmio.IS_INIT already set");
    } else {
      filled.set(37);
    }

    isInit.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace logNum(final Bytes b) {
    if (filled.get(38)) {
      throw new IllegalStateException("mmio.LOG_NUM already set");
    } else {
      filled.set(38);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      logNum.put((byte) 0);
    }
    logNum.put(b.toArrayUnsafe());

    return this;
  }

  public Trace microInstruction(final Bytes b) {
    if (filled.get(39)) {
      throw new IllegalStateException("mmio.MICRO_INSTRUCTION already set");
    } else {
      filled.set(39);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      microInstruction.put((byte) 0);
    }
    microInstruction.put(b.toArrayUnsafe());

    return this;
  }

  public Trace microInstructionStamp(final Bytes b) {
    if (filled.get(40)) {
      throw new IllegalStateException("mmio.MICRO_INSTRUCTION_STAMP already set");
    } else {
      filled.set(40);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      microInstructionStamp.put((byte) 0);
    }
    microInstructionStamp.put(b.toArrayUnsafe());

    return this;
  }

  public Trace pow2561(final Bytes b) {
    if (filled.get(41)) {
      throw new IllegalStateException("mmio.POW_256_1 already set");
    } else {
      filled.set(41);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      pow2561.put((byte) 0);
    }
    pow2561.put(b.toArrayUnsafe());

    return this;
  }

  public Trace pow2562(final Bytes b) {
    if (filled.get(42)) {
      throw new IllegalStateException("mmio.POW_256_2 already set");
    } else {
      filled.set(42);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      pow2562.put((byte) 0);
    }
    pow2562.put(b.toArrayUnsafe());

    return this;
  }

  public Trace size(final Bytes b) {
    if (filled.get(43)) {
      throw new IllegalStateException("mmio.SIZE already set");
    } else {
      filled.set(43);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      size.put((byte) 0);
    }
    size.put(b.toArrayUnsafe());

    return this;
  }

  public Trace sourceByteOffset(final Bytes b) {
    if (filled.get(44)) {
      throw new IllegalStateException("mmio.SOURCE_BYTE_OFFSET already set");
    } else {
      filled.set(44);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      sourceByteOffset.put((byte) 0);
    }
    sourceByteOffset.put(b.toArrayUnsafe());

    return this;
  }

  public Trace sourceLimbOffset(final Bytes b) {
    if (filled.get(45)) {
      throw new IllegalStateException("mmio.SOURCE_LIMB_OFFSET already set");
    } else {
      filled.set(45);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      sourceLimbOffset.put((byte) 0);
    }
    sourceLimbOffset.put(b.toArrayUnsafe());

    return this;
  }

  public Trace stackValueHiByte(final UnsignedByte b) {
    if (filled.get(47)) {
      throw new IllegalStateException("mmio.STACK_VALUE_HI_BYTE already set");
    } else {
      filled.set(47);
    }

    stackValueHiByte.put(b.toByte());

    return this;
  }

  public Trace stackValueHigh(final Bytes b) {
    if (filled.get(46)) {
      throw new IllegalStateException("mmio.STACK_VALUE_HIGH already set");
    } else {
      filled.set(46);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      stackValueHigh.put((byte) 0);
    }
    stackValueHigh.put(b.toArrayUnsafe());

    return this;
  }

  public Trace stackValueLoByte(final UnsignedByte b) {
    if (filled.get(49)) {
      throw new IllegalStateException("mmio.STACK_VALUE_LO_BYTE already set");
    } else {
      filled.set(49);
    }

    stackValueLoByte.put(b.toByte());

    return this;
  }

  public Trace stackValueLow(final Bytes b) {
    if (filled.get(48)) {
      throw new IllegalStateException("mmio.STACK_VALUE_LOW already set");
    } else {
      filled.set(48);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      stackValueLow.put((byte) 0);
    }
    stackValueLow.put(b.toArrayUnsafe());

    return this;
  }

  public Trace targetByteOffset(final Bytes b) {
    if (filled.get(50)) {
      throw new IllegalStateException("mmio.TARGET_BYTE_OFFSET already set");
    } else {
      filled.set(50);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      targetByteOffset.put((byte) 0);
    }
    targetByteOffset.put(b.toArrayUnsafe());

    return this;
  }

  public Trace targetLimbOffset(final Bytes b) {
    if (filled.get(51)) {
      throw new IllegalStateException("mmio.TARGET_LIMB_OFFSET already set");
    } else {
      filled.set(51);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      targetLimbOffset.put((byte) 0);
    }
    targetLimbOffset.put(b.toArrayUnsafe());

    return this;
  }

  public Trace txNum(final Bytes b) {
    if (filled.get(52)) {
      throw new IllegalStateException("mmio.TX_NUM already set");
    } else {
      filled.set(52);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      txNum.put((byte) 0);
    }
    txNum.put(b.toArrayUnsafe());

    return this;
  }

  public Trace valA(final Bytes b) {
    if (filled.get(53)) {
      throw new IllegalStateException("mmio.VAL_A already set");
    } else {
      filled.set(53);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      valA.put((byte) 0);
    }
    valA.put(b.toArrayUnsafe());

    return this;
  }

  public Trace valANew(final Bytes b) {
    if (filled.get(54)) {
      throw new IllegalStateException("mmio.VAL_A_NEW already set");
    } else {
      filled.set(54);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      valANew.put((byte) 0);
    }
    valANew.put(b.toArrayUnsafe());

    return this;
  }

  public Trace valB(final Bytes b) {
    if (filled.get(55)) {
      throw new IllegalStateException("mmio.VAL_B already set");
    } else {
      filled.set(55);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      valB.put((byte) 0);
    }
    valB.put(b.toArrayUnsafe());

    return this;
  }

  public Trace valBNew(final Bytes b) {
    if (filled.get(56)) {
      throw new IllegalStateException("mmio.VAL_B_NEW already set");
    } else {
      filled.set(56);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      valBNew.put((byte) 0);
    }
    valBNew.put(b.toArrayUnsafe());

    return this;
  }

  public Trace valC(final Bytes b) {
    if (filled.get(57)) {
      throw new IllegalStateException("mmio.VAL_C already set");
    } else {
      filled.set(57);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      valC.put((byte) 0);
    }
    valC.put(b.toArrayUnsafe());

    return this;
  }

  public Trace valCNew(final Bytes b) {
    if (filled.get(58)) {
      throw new IllegalStateException("mmio.VAL_C_NEW already set");
    } else {
      filled.set(58);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      valCNew.put((byte) 0);
    }
    valCNew.put(b.toArrayUnsafe());

    return this;
  }

  public Trace valX(final Bytes b) {
    if (filled.get(59)) {
      throw new IllegalStateException("mmio.VAL_X already set");
    } else {
      filled.set(59);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      valX.put((byte) 0);
    }
    valX.put(b.toArrayUnsafe());

    return this;
  }

  public Trace validateRow() {
    if (!filled.get(0)) {
      throw new IllegalStateException("mmio.ACC_1 has not been filled");
    }

    if (!filled.get(1)) {
      throw new IllegalStateException("mmio.ACC_2 has not been filled");
    }

    if (!filled.get(2)) {
      throw new IllegalStateException("mmio.ACC_3 has not been filled");
    }

    if (!filled.get(3)) {
      throw new IllegalStateException("mmio.ACC_4 has not been filled");
    }

    if (!filled.get(4)) {
      throw new IllegalStateException("mmio.ACC_5 has not been filled");
    }

    if (!filled.get(5)) {
      throw new IllegalStateException("mmio.ACC_6 has not been filled");
    }

    if (!filled.get(6)) {
      throw new IllegalStateException("mmio.ACC_A has not been filled");
    }

    if (!filled.get(7)) {
      throw new IllegalStateException("mmio.ACC_B has not been filled");
    }

    if (!filled.get(8)) {
      throw new IllegalStateException("mmio.ACC_C has not been filled");
    }

    if (!filled.get(9)) {
      throw new IllegalStateException("mmio.ACC_VAL_HI has not been filled");
    }

    if (!filled.get(10)) {
      throw new IllegalStateException("mmio.ACC_VAL_LO has not been filled");
    }

    if (!filled.get(11)) {
      throw new IllegalStateException("mmio.ACC_X has not been filled");
    }

    if (!filled.get(12)) {
      throw new IllegalStateException("mmio.BIN_1 has not been filled");
    }

    if (!filled.get(13)) {
      throw new IllegalStateException("mmio.BIN_2 has not been filled");
    }

    if (!filled.get(14)) {
      throw new IllegalStateException("mmio.BIN_3 has not been filled");
    }

    if (!filled.get(15)) {
      throw new IllegalStateException("mmio.BIN_4 has not been filled");
    }

    if (!filled.get(16)) {
      throw new IllegalStateException("mmio.BIN_5 has not been filled");
    }

    if (!filled.get(17)) {
      throw new IllegalStateException("mmio.BYTE_A has not been filled");
    }

    if (!filled.get(18)) {
      throw new IllegalStateException("mmio.BYTE_B has not been filled");
    }

    if (!filled.get(19)) {
      throw new IllegalStateException("mmio.BYTE_C has not been filled");
    }

    if (!filled.get(20)) {
      throw new IllegalStateException("mmio.BYTE_X has not been filled");
    }

    if (!filled.get(21)) {
      throw new IllegalStateException("mmio.CN_A has not been filled");
    }

    if (!filled.get(22)) {
      throw new IllegalStateException("mmio.CN_B has not been filled");
    }

    if (!filled.get(23)) {
      throw new IllegalStateException("mmio.CN_C has not been filled");
    }

    if (!filled.get(24)) {
      throw new IllegalStateException("mmio.CONTEXT_SOURCE has not been filled");
    }

    if (!filled.get(25)) {
      throw new IllegalStateException("mmio.CONTEXT_TARGET has not been filled");
    }

    if (!filled.get(26)) {
      throw new IllegalStateException("mmio.COUNTER has not been filled");
    }

    if (!filled.get(27)) {
      throw new IllegalStateException("mmio.ERF has not been filled");
    }

    if (!filled.get(28)) {
      throw new IllegalStateException("mmio.EXO_IS_HASH has not been filled");
    }

    if (!filled.get(29)) {
      throw new IllegalStateException("mmio.EXO_IS_LOG has not been filled");
    }

    if (!filled.get(30)) {
      throw new IllegalStateException("mmio.EXO_IS_ROM has not been filled");
    }

    if (!filled.get(31)) {
      throw new IllegalStateException("mmio.EXO_IS_TXCD has not been filled");
    }

    if (!filled.get(32)) {
      throw new IllegalStateException("mmio.FAST has not been filled");
    }

    if (!filled.get(33)) {
      throw new IllegalStateException("mmio.INDEX_A has not been filled");
    }

    if (!filled.get(34)) {
      throw new IllegalStateException("mmio.INDEX_B has not been filled");
    }

    if (!filled.get(35)) {
      throw new IllegalStateException("mmio.INDEX_C has not been filled");
    }

    if (!filled.get(36)) {
      throw new IllegalStateException("mmio.INDEX_X has not been filled");
    }

    if (!filled.get(37)) {
      throw new IllegalStateException("mmio.IS_INIT has not been filled");
    }

    if (!filled.get(38)) {
      throw new IllegalStateException("mmio.LOG_NUM has not been filled");
    }

    if (!filled.get(39)) {
      throw new IllegalStateException("mmio.MICRO_INSTRUCTION has not been filled");
    }

    if (!filled.get(40)) {
      throw new IllegalStateException("mmio.MICRO_INSTRUCTION_STAMP has not been filled");
    }

    if (!filled.get(41)) {
      throw new IllegalStateException("mmio.POW_256_1 has not been filled");
    }

    if (!filled.get(42)) {
      throw new IllegalStateException("mmio.POW_256_2 has not been filled");
    }

    if (!filled.get(43)) {
      throw new IllegalStateException("mmio.SIZE has not been filled");
    }

    if (!filled.get(44)) {
      throw new IllegalStateException("mmio.SOURCE_BYTE_OFFSET has not been filled");
    }

    if (!filled.get(45)) {
      throw new IllegalStateException("mmio.SOURCE_LIMB_OFFSET has not been filled");
    }

    if (!filled.get(47)) {
      throw new IllegalStateException("mmio.STACK_VALUE_HI_BYTE has not been filled");
    }

    if (!filled.get(46)) {
      throw new IllegalStateException("mmio.STACK_VALUE_HIGH has not been filled");
    }

    if (!filled.get(49)) {
      throw new IllegalStateException("mmio.STACK_VALUE_LO_BYTE has not been filled");
    }

    if (!filled.get(48)) {
      throw new IllegalStateException("mmio.STACK_VALUE_LOW has not been filled");
    }

    if (!filled.get(50)) {
      throw new IllegalStateException("mmio.TARGET_BYTE_OFFSET has not been filled");
    }

    if (!filled.get(51)) {
      throw new IllegalStateException("mmio.TARGET_LIMB_OFFSET has not been filled");
    }

    if (!filled.get(52)) {
      throw new IllegalStateException("mmio.TX_NUM has not been filled");
    }

    if (!filled.get(53)) {
      throw new IllegalStateException("mmio.VAL_A has not been filled");
    }

    if (!filled.get(54)) {
      throw new IllegalStateException("mmio.VAL_A_NEW has not been filled");
    }

    if (!filled.get(55)) {
      throw new IllegalStateException("mmio.VAL_B has not been filled");
    }

    if (!filled.get(56)) {
      throw new IllegalStateException("mmio.VAL_B_NEW has not been filled");
    }

    if (!filled.get(57)) {
      throw new IllegalStateException("mmio.VAL_C has not been filled");
    }

    if (!filled.get(58)) {
      throw new IllegalStateException("mmio.VAL_C_NEW has not been filled");
    }

    if (!filled.get(59)) {
      throw new IllegalStateException("mmio.VAL_X has not been filled");
    }

    filled.clear();
    this.currentLine++;

    return this;
  }

  public Trace fillAndValidateRow() {
    if (!filled.get(0)) {
      acc1.position(acc1.position() + 32);
    }

    if (!filled.get(1)) {
      acc2.position(acc2.position() + 32);
    }

    if (!filled.get(2)) {
      acc3.position(acc3.position() + 32);
    }

    if (!filled.get(3)) {
      acc4.position(acc4.position() + 32);
    }

    if (!filled.get(4)) {
      acc5.position(acc5.position() + 32);
    }

    if (!filled.get(5)) {
      acc6.position(acc6.position() + 32);
    }

    if (!filled.get(6)) {
      accA.position(accA.position() + 32);
    }

    if (!filled.get(7)) {
      accB.position(accB.position() + 32);
    }

    if (!filled.get(8)) {
      accC.position(accC.position() + 32);
    }

    if (!filled.get(9)) {
      accValHi.position(accValHi.position() + 32);
    }

    if (!filled.get(10)) {
      accValLo.position(accValLo.position() + 32);
    }

    if (!filled.get(11)) {
      accX.position(accX.position() + 32);
    }

    if (!filled.get(12)) {
      bin1.position(bin1.position() + 1);
    }

    if (!filled.get(13)) {
      bin2.position(bin2.position() + 1);
    }

    if (!filled.get(14)) {
      bin3.position(bin3.position() + 1);
    }

    if (!filled.get(15)) {
      bin4.position(bin4.position() + 1);
    }

    if (!filled.get(16)) {
      bin5.position(bin5.position() + 1);
    }

    if (!filled.get(17)) {
      byteA.position(byteA.position() + 1);
    }

    if (!filled.get(18)) {
      byteB.position(byteB.position() + 1);
    }

    if (!filled.get(19)) {
      byteC.position(byteC.position() + 1);
    }

    if (!filled.get(20)) {
      byteX.position(byteX.position() + 1);
    }

    if (!filled.get(21)) {
      cnA.position(cnA.position() + 32);
    }

    if (!filled.get(22)) {
      cnB.position(cnB.position() + 32);
    }

    if (!filled.get(23)) {
      cnC.position(cnC.position() + 32);
    }

    if (!filled.get(24)) {
      contextSource.position(contextSource.position() + 32);
    }

    if (!filled.get(25)) {
      contextTarget.position(contextTarget.position() + 32);
    }

    if (!filled.get(26)) {
      counter.position(counter.position() + 32);
    }

    if (!filled.get(27)) {
      erf.position(erf.position() + 1);
    }

    if (!filled.get(28)) {
      exoIsHash.position(exoIsHash.position() + 1);
    }

    if (!filled.get(29)) {
      exoIsLog.position(exoIsLog.position() + 1);
    }

    if (!filled.get(30)) {
      exoIsRom.position(exoIsRom.position() + 1);
    }

    if (!filled.get(31)) {
      exoIsTxcd.position(exoIsTxcd.position() + 1);
    }

    if (!filled.get(32)) {
      fast.position(fast.position() + 1);
    }

    if (!filled.get(33)) {
      indexA.position(indexA.position() + 32);
    }

    if (!filled.get(34)) {
      indexB.position(indexB.position() + 32);
    }

    if (!filled.get(35)) {
      indexC.position(indexC.position() + 32);
    }

    if (!filled.get(36)) {
      indexX.position(indexX.position() + 32);
    }

    if (!filled.get(37)) {
      isInit.position(isInit.position() + 1);
    }

    if (!filled.get(38)) {
      logNum.position(logNum.position() + 32);
    }

    if (!filled.get(39)) {
      microInstruction.position(microInstruction.position() + 32);
    }

    if (!filled.get(40)) {
      microInstructionStamp.position(microInstructionStamp.position() + 32);
    }

    if (!filled.get(41)) {
      pow2561.position(pow2561.position() + 32);
    }

    if (!filled.get(42)) {
      pow2562.position(pow2562.position() + 32);
    }

    if (!filled.get(43)) {
      size.position(size.position() + 32);
    }

    if (!filled.get(44)) {
      sourceByteOffset.position(sourceByteOffset.position() + 32);
    }

    if (!filled.get(45)) {
      sourceLimbOffset.position(sourceLimbOffset.position() + 32);
    }

    if (!filled.get(47)) {
      stackValueHiByte.position(stackValueHiByte.position() + 1);
    }

    if (!filled.get(46)) {
      stackValueHigh.position(stackValueHigh.position() + 32);
    }

    if (!filled.get(49)) {
      stackValueLoByte.position(stackValueLoByte.position() + 1);
    }

    if (!filled.get(48)) {
      stackValueLow.position(stackValueLow.position() + 32);
    }

    if (!filled.get(50)) {
      targetByteOffset.position(targetByteOffset.position() + 32);
    }

    if (!filled.get(51)) {
      targetLimbOffset.position(targetLimbOffset.position() + 32);
    }

    if (!filled.get(52)) {
      txNum.position(txNum.position() + 32);
    }

    if (!filled.get(53)) {
      valA.position(valA.position() + 32);
    }

    if (!filled.get(54)) {
      valANew.position(valANew.position() + 32);
    }

    if (!filled.get(55)) {
      valB.position(valB.position() + 32);
    }

    if (!filled.get(56)) {
      valBNew.position(valBNew.position() + 32);
    }

    if (!filled.get(57)) {
      valC.position(valC.position() + 32);
    }

    if (!filled.get(58)) {
      valCNew.position(valCNew.position() + 32);
    }

    if (!filled.get(59)) {
      valX.position(valX.position() + 32);
    }

    filled.clear();
    this.currentLine++;

    return this;
  }

  public void build() {
    if (!filled.isEmpty()) {
      throw new IllegalStateException("Cannot build trace with a non-validated row.");
    }
  }
}
