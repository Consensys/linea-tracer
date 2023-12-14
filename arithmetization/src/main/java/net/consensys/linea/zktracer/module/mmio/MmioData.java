/*
 * Copyright Consensys Software Inc.
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

import static net.consensys.linea.zktracer.module.mmio.MmioPatterns.isolateChunk;
import static net.consensys.linea.zktracer.module.mmio.MmioPatterns.isolatePrefix;
import static net.consensys.linea.zktracer.module.mmio.MmioPatterns.isolateSuffix;
import static net.consensys.linea.zktracer.module.mmio.MmioPatterns.plateau;
import static net.consensys.linea.zktracer.module.mmio.MmioPatterns.power;
import static net.consensys.linea.zktracer.types.Conversions.bytesToUnsignedBytes;
import static net.consensys.linea.zktracer.types.Conversions.unsignedBytesToEWord;

import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.runtime.callstack.CallStack;
import net.consensys.linea.zktracer.runtime.stack.StackContext;
import net.consensys.linea.zktracer.types.EWord;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.units.bigints.UInt256;

@Getter
@Setter
@Accessors(fluent = true)
@AllArgsConstructor
public class MmioData {
  private int cnA;
  private int cnB;
  private int cnC;

  private int indexA;
  private int indexB;
  private int indexC;
  // pointer into exo data
  private int indexX;

  private UnsignedByte[] valA;
  private UnsignedByte[] valB;
  private UnsignedByte[] valC;

  private UnsignedByte[] valANew;
  private UnsignedByte[] valBNew;
  private UnsignedByte[] valCNew;

  private EWord val;
  private UnsignedByte[] valHi;
  private UnsignedByte[] valLo;
  private UnsignedByte[] valX;

  private boolean bin1;
  private boolean bin2;
  private boolean bin3;
  private boolean bin4;
  private boolean bin5;

  private UInt256 pow2561;
  private UInt256 pow2562;

  private UInt256 acc1;
  private UInt256 acc2;
  private UInt256 acc3;
  private UInt256 acc4;
  private UInt256 acc5;
  private UInt256 acc6;

  public MmioData() {
    this(
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        UnsignedByte.EMPTY_BYTES16,
        UnsignedByte.EMPTY_BYTES16,
        UnsignedByte.EMPTY_BYTES16,
        UnsignedByte.EMPTY_BYTES16,
        UnsignedByte.EMPTY_BYTES16,
        UnsignedByte.EMPTY_BYTES16,
        null,
        UnsignedByte.EMPTY_BYTES16,
        UnsignedByte.EMPTY_BYTES16,
        UnsignedByte.EMPTY_BYTES16,
        false,
        false,
        false,
        false,
        false,
        UInt256.ZERO,
        UInt256.ZERO,
        UInt256.ZERO,
        UInt256.ZERO,
        UInt256.ZERO,
        UInt256.ZERO,
        UInt256.ZERO,
        UInt256.ZERO);
  }

  public UnsignedByte byteA(int counter) {
    return valA[counter];
  }

  public UnsignedByte byteB(int counter) {
    return valB[counter];
  }

  public UnsignedByte byteC(int counter) {
    return valC[counter];
  }

  public UnsignedByte byteX(int counter) {
    return valX[counter];
  }

  public UnsignedByte byteHi(int counter) {
    return valHi[counter];
  }

  public UnsignedByte byteLo(int counter) {
    return valLo[counter];
  }

  public EWord valAEword() {
    return unsignedBytesToEWord(valA);
  }

  public EWord valBEword() {
    return unsignedBytesToEWord(valB);
  }

  public void onePartialToOne(
      UnsignedByte sb,
      UnsignedByte tb,
      UInt256 acc1,
      UInt256 acc2,
      UnsignedByte sm,
      UnsignedByte tm,
      int size,
      int counter) {
    bin1 = plateau(tm.toInteger(), counter);
    bin2 = plateau(tm.toInteger() + size, counter);
    bin3 = plateau(sm.toInteger(), counter);
    bin4 = plateau(sm.toInteger() + size, counter);

    this.acc1 = isolateChunk(acc1, tb, bin1, bin2, counter);
    this.acc2 = isolateChunk(acc2, sb, bin3, bin4, counter);

    pow2561 = power(pow2561, bin2, counter);
  }

  public void onePartialToTwo(
      UnsignedByte sb,
      UnsignedByte t1b,
      UnsignedByte t2b,
      UInt256 acc1,
      UInt256 acc2,
      UInt256 acc3,
      UInt256 acc4,
      UnsignedByte sm,
      UnsignedByte t1m,
      int size,
      int counter) {
    bin1 = plateau(t1m.toInteger(), counter);
    bin2 = plateau(t1m.toInteger() + size - 16, counter);
    bin3 = plateau(sm.toInteger(), counter);
    bin4 = plateau(sm.toInteger() + 16 - t1m.toInteger(), counter);
    bin5 = plateau(sm.toInteger() + size, counter);

    this.acc1 = isolateSuffix(acc1, bin1, t1b);
    this.acc2 = isolateSuffix(acc2, bin2, t2b);

    pow2561 = power(pow2561, bin2, counter);

    this.acc3 = isolateChunk(acc3, sb, bin3, bin4, counter);
    this.acc4 = isolateChunk(acc4, sb, bin4, bin5, counter);
  }

  public void threeToTwoFull(
      UnsignedByte s1b,
      UnsignedByte s2b,
      UnsignedByte s3b,
      UInt256 acc1,
      UInt256 acc2,
      UInt256 acc3,
      UInt256 acc4,
      UnsignedByte sm,
      int counter) {
    bin1 = plateau(sm.toInteger(), counter);
    bin2 = plateau(16 - sm.toInteger(), counter);

    this.acc1 = isolateSuffix(acc1, bin1, s1b);
    this.acc3 = isolateSuffix(acc3, bin1, s2b);

    this.acc2 = isolatePrefix(acc2, bin1, s2b);
    this.acc4 = isolatePrefix(acc4, bin1, s3b);

    pow2561 = power(pow2561, bin2, counter);
  }

  public void oneToOnePadded(
      UnsignedByte[] s,
      UnsignedByte sb,
      UInt256 acc,
      PowType powType,
      UnsignedByte sm,
      int size,
      int counter) {
    Preconditions.checkArgument(
        sb != s[counter], "oneOnePadded: SB = %s != %s = S[%d]".formatted(sb, s[counter], counter));

    boolean b1 = plateau(sm.toInteger(), counter);
    boolean b2 = plateau(sm.toInteger() + size, counter);
    boolean b3 = plateau(size, counter);

    switch (powType) {
      case POW_256_1 -> {
        bin1 = b1;
        bin2 = b2;
        bin3 = b3;

        acc1 = isolateChunk(acc, sb, bin1, bin2, counter);
        pow2561 = power(pow2561, bin3, counter);
      }
      case POW_256_2 -> {
        bin1 = b1;
        bin3 = b2;
        bin4 = b3;

        acc3 = isolateChunk(acc, sb, bin1, bin3, counter);
        pow2562 = power(pow2562, bin4, counter);
      }
    }
  }

  public void setVal(EWord x) {
    System.arraycopy(bytesToUnsignedBytes(x.hi().toArray()), 0, valHi, 0, valHi.length);
    System.arraycopy(bytesToUnsignedBytes(x.lo().toArray()), 0, valLo, 0, valLo.length);
  }

  public void excision(UnsignedByte tb, UInt256 acc, UnsignedByte tm, int size, int counter) {
    bin1 = plateau(tm.toInteger(), counter);
    bin2 = plateau(tm.toInteger() + size, counter);

    this.acc1 = isolateChunk(acc, tb, bin1, bin2, counter);

    pow2561 = power(pow2561, bin2, counter);
  }

  public void updateLimbsInMemory(final CallStack callStack) {
    StackContext pending = callStack.get(cnA).pending();
    pending.memory().updateLimb(indexA, valANew);
    pending.memory().updateLimb(indexB, valBNew);
    pending.memory().updateLimb(indexC, valCNew);
  }

  public void twoToOnePadded(
      UnsignedByte[] s1,
      UnsignedByte[] s2,
      UnsignedByte s1b,
      UnsignedByte s2b,
      UInt256 acc1,
      UInt256 acc2,
      UnsignedByte s1m,
      int size,
      int counter) {
    Preconditions.checkArgument(
        s1b != s1[counter],
        "twoOnePadded: S1B = %s != %s = S1[%d]".formatted(s1b, s1[counter], counter));

    Preconditions.checkArgument(
        s2b != s2[counter],
        "twoOnePadded: S2B = %s != %s = S2[%d]".formatted(s2b, s2[counter], counter));

    bin1 = plateau(s1m.toInteger(), counter);
    bin2 = plateau(s1m.toInteger() + size - 16, counter);
    bin3 = plateau(16 - s1m.toInteger(), counter);
    bin4 = plateau(size, counter);

    this.acc1 = isolateSuffix(acc1, bin1, s1b);
    this.acc2 = isolateSuffix(acc2, bin2, s2b);

    pow2561 = power(pow2561, bin3, counter);
    pow2562 = power(pow2562, bin4, counter);
  }

  public void twoToOneFull(
      UnsignedByte s1b,
      UnsignedByte s2b,
      UInt256 acc1,
      UInt256 acc2,
      UnsignedByte sm,
      int counter) {

    bin1 = plateau(sm.toInteger(), counter);
    bin2 = plateau(16 - sm.toInteger(), counter);

    this.acc1 = isolateSuffix(acc1, bin1, s1b);
    this.acc2 = isolatePrefix(acc2, bin2, s2b);

    pow2561 = power(pow2561, bin2, counter);
  }

  public void twoFullToThree(
      UnsignedByte t1b,
      UnsignedByte t2b,
      UnsignedByte s1b,
      UnsignedByte s2b,
      UInt256 acc1,
      UInt256 acc2,
      UInt256 acc3,
      UInt256 acc4,
      UInt256 acc5,
      UInt256 acc6,
      UnsignedByte tm,
      int counter) {
    bin1 = plateau(tm.toInteger(), counter);
    bin2 = plateau(16 - tm.toInteger(), counter);

    this.acc1 = isolateSuffix(acc1, bin1, t1b);
    this.acc2 = isolatePrefix(acc2, bin1, t2b);

    this.acc3 = isolatePrefix(acc3, bin2, s1b);
    this.acc4 = isolateSuffix(acc4, bin2, s2b);

    this.acc5 = isolatePrefix(acc5, bin2, s2b);
    this.acc6 = isolateSuffix(acc6, bin2, s2b);

    pow2561 = power(pow2561, bin1, counter);
  }

  public void byteSwap(UnsignedByte tb, UInt256 acc, UnsignedByte tm, int counter) {
    bin1 = plateau(tm.toInteger(), counter);
    bin2 = plateau(tm.toInteger() + 1, counter);

    this.acc1 = isolateChunk(acc, tb, bin1, bin2, counter);

    pow2561 = power(pow2561, bin2, counter);
  }
}
