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
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.Bytes16;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;

@Slf4j
public class WcpOperation {
  private static final int LLARGEMO = 15;
  private static final int LLARGE = 16;

  private final OpCode opCode;
  private final Bytes32 arg1;
  private final Bytes32 arg2;
  final int ctMax;

  private Bytes arg1Hi;
  private Bytes arg1Lo;
  private Bytes arg2Hi;
  private Bytes arg2Lo;

  private Bytes16 adjHi;
  private Bytes16 adjLo;
  private Boolean neg1;

  private Boolean neg2;
  private Boolean bit1 = true;
  private Boolean bit2 = true;
  private Boolean bit3;
  private Boolean bit4;
  private Boolean resLo;

  final List<Boolean> bits = new ArrayList<>(16);

  public WcpOperation(OpCode opCode, Bytes32 arg1, Bytes32 arg2) {
    this.opCode = opCode;
    this.arg1 = arg1;
    this.arg2 = arg2;

    this.ctMax = maxCt();
  }

  private void compute() {
    final int length = this.isOli() ? LLARGE : this.ctMax + 1;
    final int offset = LLARGE - length;
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
      for (int ct = 0; ct < this.ctMax; ct++) {
        bits.add(ct, false);
      }
    }

    // Set bit 1 and 2
    if (this.isMli()) {

      for (int i = 0; i < 16; i++) {
        if (arg1Hi.get(i) != arg2Hi.get(i)) {
          bit1 = false;
        }
        if (arg1Lo.get(i) != arg2Lo.get(i)) {
          bit2 = false;
        }
      }
    } else {
      bit1 = false;
      bit2 = false;
    }

    // Set bit 3 and AdjHi
    final BigInteger firstHi = arg1Hi.toUnsignedBigInteger();
    final BigInteger secondHi = arg2Hi.toUnsignedBigInteger();
    bit3 = firstHi.compareTo(secondHi) > 0;
    this.adjHi = calculateAdj(bit3, firstHi, secondHi);

    // Set bit 4 and AdjLo
    final BigInteger firstLo = arg1Lo.toUnsignedBigInteger();
    final BigInteger secondLo = arg2Lo.toUnsignedBigInteger();
    bit4 = firstLo.compareTo(secondLo) > 0;
    this.adjLo = calculateAdj(bit4, firstLo, secondLo);
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

  private boolean calculateResLow(OpCode opCode, Bytes32 arg1, Bytes32 arg2) {
    return switch (opCode) {
      case EQ -> arg1.compareTo(arg2) == 0;
      case ISZERO -> arg1.isZero();
      case SLT -> reallyToSignedBigInteger(arg1).compareTo(reallyToSignedBigInteger(arg2)) < 0;
      case SGT -> reallyToSignedBigInteger(arg1).compareTo(reallyToSignedBigInteger(arg2)) > 0;
      case LT -> arg1.compareTo(arg2) < 0;
      case GT -> arg1.compareTo(arg2) > 0;
      case LEQ -> arg1.compareTo(arg2) < 0 || arg1.compareTo(arg2) == 0;
      case GEQ -> arg1.compareTo(arg2) > 0 || arg1.compareTo(arg2) == 0;
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
    final UnsignedByte inst = UnsignedByte.of(this.opCode.byteValue());

    for (int ct = 0; ct < this.maxCt(); ct++) {
      trace
          .wordComparisonStamp(Bytes.ofUnsignedInt(stamp))
          .oneLineInstruction(oli)
          .multiLineInstruction(mli)
          .variableLengthInstruction(vli)
          .counter(UnsignedByte.of(ct))
          .inst(inst)
          .isEq(this.opCode == OpCode.EQ)
          .isIszero(this.opCode == OpCode.ISZERO)
          .isSlt(this.opCode == OpCode.SLT)
          .isSgt(this.opCode == OpCode.SGT)
          .isLt(this.opCode == OpCode.LT)
          .isGt(this.opCode == OpCode.GT)
          .isLeq(this.opCode == OpCode.LEQ)
          .isGeq(this.opCode == OpCode.GEQ)
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
      case ISZERO, EQ -> true;
      case SLT, SGT, LT, GT, LEQ, GEQ -> false;
      default -> throw new IllegalStateException("Unexpected value: " + this.opCode);
    };
  }

  private boolean isMli() {
    return switch (this.opCode) {
      case SLT, SGT -> true;
      case ISZERO, EQ, LT, GT, LEQ, GEQ -> false;
      default -> throw new IllegalStateException("Unexpected value: " + this.opCode);
    };
  }

  private boolean isVli() {
    return switch (this.opCode) {
      case LT, GT, LEQ, GEQ -> true;
      case ISZERO, EQ, SLT, SGT -> false;
      default -> throw new IllegalStateException("Unexpected value: " + this.opCode);
    };
  }

  private int maxCt() {
    return switch (this.opCode) {
      case ISZERO, EQ -> 0;
      case SLT, SGT -> LLARGEMO;
      case LT, GT, LEQ, GEQ -> Math.max(
          Math.max(this.arg1.slice(0, LLARGE).size(), this.arg2.slice(0, LLARGE).size()),
          Math.max(this.arg1.slice(LLARGE, LLARGE).size(), this.arg2.slice(LLARGE, LLARGE).size()));
      default -> throw new IllegalStateException("Unexpected value: " + this.opCode);
    };
  }
}
