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

package net.consensys.linea.zktracer.module.wcp;

import static net.consensys.linea.zktracer.module.Util.byteBits;
import static net.consensys.linea.zktracer.types.Conversions.reallyToSignedBigInteger;

import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.zktracer.types.Bytes16;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;

@Slf4j
public class WcpOperation {
  private static final int LLARGEMO = 15;
  private static final int LLARGE = 16;
  public static final byte LEQbv = 0x0E;
  public static final byte GEQbv = 0x0F;
  private static final byte LTbv = 0x10;
  private static final byte GTbv = 0x11;
  private static final byte SLTbv = 0x12;
  private static final byte SGTbv = 0x13;
  private static final byte EQbv = 0x14;

  private static final byte ISZERObv = 0x15;

  private final byte opCode;
  private final Bytes32 arg1;
  private final Bytes32 arg2;
  final int ctMax;
  private int length;
  private int offset;
  private Bytes arg1Hi;
  private Bytes arg1Lo;
  private Bytes arg2Hi;
  private Bytes arg2Lo;

  private Bytes adjHi;
  private Bytes adjLo;
  private Boolean neg1;

  private Boolean neg2;
  private boolean bit1;
  private Boolean bit2;
  private Boolean bit3;
  private Boolean bit4;
  private Boolean resLo;

  final List<Boolean> bits = new ArrayList<>(16);

  public WcpOperation(byte opCode, Bytes32 arg1, Bytes32 arg2) {
    this.opCode = opCode;
    this.arg1 = arg1;
    this.arg2 = arg2;

    this.ctMax = maxCt();
  }

  private void compute() {
    this.length = (this.isOli() || this.isMli()) ? LLARGE : this.ctMax + 1;
    this.offset = LLARGE - length;
    this.arg1Hi = arg1.slice(offset, length);
    this.arg1Lo = arg1.slice(LLARGE + offset, length);
    this.arg2Hi = arg2.slice(offset, length);
    this.arg2Lo = arg2.slice(LLARGE + offset, length);

    // Calculate Result Low
    resLo = calculateResLow(opCode, arg1, arg2);

    // Initiate negatives
    UnsignedByte msb1 = UnsignedByte.of(this.arg1Hi.get(0));
    UnsignedByte msb2 = UnsignedByte.of(this.arg2Hi.get(0));
    Boolean[] msb1Bits = byteBits(msb1);
    Boolean[] msb2Bits = byteBits(msb2);
    this.neg1 = msb1Bits[0];
    this.neg2 = msb2Bits[0];

    // Initiate bits
    if (this.isMli()) {
      Collections.addAll(bits, msb1Bits);
      Collections.addAll(bits, msb2Bits);
    } else {
      for (int ct = 0; ct <= this.ctMax; ct++) {
        bits.add(ct, false);
      }
    }

    // Set bit 1 and 2
    this.bit1 = this.arg1Hi.compareTo(this.arg2Hi) == 0;
    this.bit2 = this.arg1Lo.compareTo(this.arg2Lo) == 0;

    // Set bit 3 and AdjHi
    final BigInteger firstHi = arg1Hi.toUnsignedBigInteger();
    final BigInteger secondHi = arg2Hi.toUnsignedBigInteger();
    bit3 = firstHi.compareTo(secondHi) > 0;
    this.adjHi = calculateAdj(bit3, firstHi, secondHi).slice(offset, length);

    // Set bit 4 and AdjLo
    final BigInteger firstLo = arg1Lo.toUnsignedBigInteger();
    final BigInteger secondLo = arg2Lo.toUnsignedBigInteger();
    bit4 = firstLo.compareTo(secondLo) > 0;
    this.adjLo = calculateAdj(bit4, firstLo, secondLo).slice(offset, length);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.opCode, this.arg1, this.arg2);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final WcpOperation that = (WcpOperation) o;
    return Objects.equals(opCode, that.opCode)
        && Objects.equals(arg1, that.arg1)
        && Objects.equals(arg2, that.arg2);
  }

  private boolean calculateResLow(byte opCode, Bytes32 arg1, Bytes32 arg2) {
    return switch (opCode) {
      case EQbv -> arg1.compareTo(arg2) == 0;
      case ISZERObv -> arg1.isZero();
      case SLTbv -> reallyToSignedBigInteger(arg1).compareTo(reallyToSignedBigInteger(arg2)) < 0;
      case SGTbv -> reallyToSignedBigInteger(arg1).compareTo(reallyToSignedBigInteger(arg2)) > 0;
      case LTbv -> arg1.compareTo(arg2) < 0;
      case GTbv -> arg1.compareTo(arg2) > 0;
      case LEQbv -> arg1.compareTo(arg2) <= 0;
      case GEQbv -> arg1.compareTo(arg2) >= 0;
      default -> throw new InvalidParameterException("Invalid opcode");
    };
  }

  private Bytes16 calculateAdj(boolean cmp, BigInteger arg1, BigInteger arg2) {
    BigInteger adjHi;
    if (cmp) {
      adjHi = arg1.subtract(arg2).subtract(BigInteger.ONE);
    } else {
      adjHi = arg2.subtract(arg1);
    }
    var bytes32 = Bytes32.leftPad(Bytes.of(adjHi.toByteArray()));

    return Bytes16.wrap(bytes32.slice(16));
  }

  void trace(Trace trace, int stamp) {
    this.compute();

    final boolean resLo = this.resLo;
    final boolean oli = isOli();
    final boolean mli = isMli();
    final boolean vli = isVli();
    final UnsignedByte inst = UnsignedByte.of(this.opCode);

    for (int ct = 0; ct <= this.maxCt(); ct++) {
      trace
          .wordComparisonStamp(Bytes.ofUnsignedInt(stamp))
          .oneLineInstruction(oli)
          .multiLineInstruction(mli)
          .variableLengthInstruction(vli)
          .counter(UnsignedByte.of(ct))
          .ctMax(UnsignedByte.of(this.ctMax))
          .inst(inst)
          .isEq(this.opCode == EQbv)
          .isIszero(this.opCode == ISZERObv)
          .isSlt(this.opCode == SLTbv)
          .isSgt(this.opCode == SGTbv)
          .isLt(this.opCode == LTbv)
          .isGt(this.opCode == GTbv)
          .isLeq(this.opCode == LEQbv)
          .isGeq(this.opCode == GEQbv)
          .argument1Hi(this.arg1Hi)
          .argument1Lo(this.arg1Lo)
          .argument2Hi(this.arg2Hi)
          .argument2Lo(this.arg2Lo)
          .result(resLo)
          .bits(bits.get(ct))
          .neg1(neg1)
          .neg2(neg2)
          .byte1(UnsignedByte.of(this.arg1Hi.get(ct)))
          .byte2(UnsignedByte.of(this.arg1Lo.get(ct)))
          .byte3(UnsignedByte.of(this.arg2Hi.get(ct)))
          .byte4(UnsignedByte.of(this.arg2Lo.get(ct)))
          .byte5(UnsignedByte.of(adjHi.get(ct)))
          .byte6(UnsignedByte.of(adjLo.get(ct)))
          .acc1(this.arg1Hi.slice(0, 1 + ct))
          .acc2(this.arg1Lo.slice(0, 1 + ct))
          .acc3(this.arg2Hi.slice(0, 1 + ct))
          .acc4(this.arg2Lo.slice(0, 1 + ct))
          .acc5(adjHi.slice(0, 1 + ct))
          .acc6(adjLo.slice(0, 1 + ct))
          .bit1(bit1)
          .bit2(bit2)
          .bit3(bit3)
          .bit4(bit4)
          .validateRow();
    }
  }

  private boolean isOli() {
    return switch (this.opCode) {
      case ISZERObv, EQbv -> true;
      case SLTbv, SGTbv, LTbv, GTbv, LEQbv, GEQbv -> false;
      default -> throw new IllegalStateException("Unexpected value: " + this.opCode);
    };
  }

  private boolean isMli() {
    return switch (this.opCode) {
      case SLTbv, SGTbv -> true;
      case ISZERObv, EQbv, LTbv, GTbv, LEQbv, GEQbv -> false;
      default -> throw new IllegalStateException("Unexpected value: " + this.opCode);
    };
  }

  private boolean isVli() {
    return switch (this.opCode) {
      case LTbv, GTbv, LEQbv, GEQbv -> true;
      case ISZERObv, EQbv, SLTbv, SGTbv -> false;
      default -> throw new IllegalStateException("Unexpected value: " + this.opCode);
    };
  }

  private int maxCt() {
    return switch (this.opCode) {
      case ISZERObv, EQbv -> 0;
      case SLTbv, SGTbv -> LLARGEMO;
      case LTbv, GTbv, LEQbv, GEQbv -> Math.max(
              Math.max(
                  this.arg1.slice(0, LLARGE).trimLeadingZeros().size(),
                  this.arg2.slice(0, LLARGE).trimLeadingZeros().size()),
              Math.max(
                  this.arg1.slice(LLARGE, LLARGE).trimLeadingZeros().size(),
                  this.arg2.slice(LLARGE, LLARGE).trimLeadingZeros().size()))
          - 1;
      default -> throw new IllegalStateException("Unexpected value: " + this.opCode);
    };
  }
}
