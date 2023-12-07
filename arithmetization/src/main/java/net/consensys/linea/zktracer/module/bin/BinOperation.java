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

package net.consensys.linea.zktracer.module.bin;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.bytestheta.BaseBytes;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.Bytes16;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.apache.tuweni.bytes.MutableBytes32;

@Getter
@Accessors(fluent = true)
public class BinOperation {
  private static final int LIMB_SIZE = 16;

  private final OpCode opCode;
  private final BaseBytes arg1;
  private final BaseBytes arg2;

  final Bytes16 arg1Hi;
  final Bytes16 arg1Lo;
  final Bytes16 arg2Hi;
  final Bytes16 arg2Lo;
  private final boolean isSmall;
  private final UnsignedByte lsb;
  private final UnsignedByte low4;
  private final boolean bitB4;
  private final boolean neg;

  public BinOperation(OpCode opCode, BaseBytes arg1, BaseBytes arg2) {
    this.opCode = opCode;
    this.arg1 = arg1;
    this.arg2 = arg2;

    arg1Hi = arg1.getHigh();
    arg1Lo = arg1.getLow();
    arg2Hi = arg2.getHigh();
    arg2Lo = arg2.getLow();

    this.isSmall = arg1.getBytes32().compareTo(Bytes.ofUnsignedInt(32)) < 0;
    this.lsb = UnsignedByte.of(arg1.getLow().get(15));
    this.low4 = lsb.mod(16);
    this.bitB4 =
        (lsb.toInteger() << 3) >> 7 == 1; // shiftLeft(3).shiftRight(7).equals(UnsignedByte.of(1));
    this.neg = Bytes.ofUnsignedInt(pivot().toInteger()).get(0) == 1;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.opCode, this.arg1, this.arg2);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final BinOperation that = (BinOperation) o;
    return java.util.Objects.equals(opCode, that.opCode)
        && java.util.Objects.equals(arg1, that.arg1)
        && java.util.Objects.equals(arg2, that.arg2);
  }

  public boolean isOneLineInstruction() {
    return (opCode == OpCode.BYTE || opCode == OpCode.SIGNEXTEND) && !arg1Hi.isZero();
  }

  public int maxCt() {
    return isOneLineInstruction() ? 1 : LIMB_SIZE;
  }

  public BaseBytes getRes() {
    return BaseBytes.fromBytes32(
        switch (opCode) {
          case AND -> arg1.getBytes32().and(arg2.getBytes32());
          case OR -> arg1.getBytes32().or(arg2.getBytes32());
          case XOR -> arg1.getBytes32().xor(arg2.getBytes32());
          case NOT -> arg1.getBytes32().not();
          case SIGNEXTEND -> signNExtendRes();
          case BYTE -> byteRes();
          default -> throw new RuntimeException("Modular arithmetic was given wrong opcode");
        });
  }

  public UnsignedByte pivot() {
    if (opCode.equals(OpCode.BYTE)) {
      if (bitB4) {
        return UnsignedByte.of(arg2Lo().get(low4.toInteger()));
      } else {
        return UnsignedByte.of(arg2Hi().get(low4.toInteger()));
      }
    }

    if (opCode.equals(OpCode.SIGNEXTEND)) {
      if (bitB4) {
        return UnsignedByte.of(arg2Hi().get(LIMB_SIZE - 1 - low4.toInteger()));
      } else {
        return UnsignedByte.of(arg2Lo().get(LIMB_SIZE - 1 - low4.toInteger()));
      }
    }

    return UnsignedByte.ZERO;
  }

  private Bytes32 signNExtendRes() {
    final Bytes value0 = arg1.getBytes32().trimLeadingZeros();
    final Bytes value1 = Bytes32.leftPad(arg2.getBytes32());

    final MutableBytes32 result = MutableBytes32.create();

    // Any value >= 31 imply an index <= 0, so no work to do (note that 0 itself is a valid index,
    // but copying the 0th byte to itself is only so useful).
    int value0size = value0.size();
    if (value0size > 1) {
      return Bytes32.leftPad(value1);
    }

    int value0Value = value0.toInt();
    if (value0Value >= 31) {
      return Bytes32.leftPad(value1);
    }

    final int byteIndex = 31 - value0.toInt();
    final byte toSet = value1.get(byteIndex) < 0 ? (byte) 0xFF : 0x00;
    result.mutableSlice(0, byteIndex).fill(toSet);
    value1.slice(byteIndex).copyTo(result, byteIndex);
    return Bytes32.leftPad(result);
  }

  private Bytes32 byteRes() {
    final Bytes offset = arg1.getBytes32();
    final Bytes seq = arg2.getBytes32();

    Bytes trimmedOffset = offset.trimLeadingZeros();
    if (trimmedOffset.size() > 1) {
      return Bytes32.ZERO;
    }
    final int index = trimmedOffset.toInt();

    int size = seq.size();
    int pos = index - 32 + size;
    if (pos >= size || pos < 0) {
      return Bytes32.ZERO;
    } else {
      final byte b = seq.get(pos);
      return Bytes32.leftPad(Bytes.of(b));
    }
  }
}
