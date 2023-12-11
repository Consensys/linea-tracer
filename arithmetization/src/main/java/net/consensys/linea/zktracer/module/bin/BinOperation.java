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

import static net.consensys.linea.zktracer.module.rlputils.Pattern.bitDecomposition;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.bytestheta.BaseBytes;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;

@Getter
@Accessors(fluent = true)
public class BinOperation {
  public BinOperation(OpCode opCode, BaseBytes arg1, BaseBytes arg2) {
    this.opCode = opCode;
    this.arg1 = arg1;
    this.arg2 = arg2;
  }

  private static final int LIMB_SIZE = 16;
  private final OpCode opCode;
  private final BaseBytes arg1;
  private final BaseBytes arg2;
  public final List<Boolean> lastEightBits = getLastEightBits();
  public final boolean bit4 = getBit4();
  public final int low4 = getLow4();
  public final boolean isSmall = isSmall();
  private final int pivotThreshold = getPivotThreshold();

  public final int pivot = getPivot();

  @Override
  public int hashCode() {
    return Objects.hashCode(this.opCode, this.arg1, this.arg2);
  }

  @Override
  public boolean equals(Object o) {
    // if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final BinOperation that = (BinOperation) o;
    return java.util.Objects.equals(opCode, that.opCode)
        && java.util.Objects.equals(arg1, that.arg1)
        && java.util.Objects.equals(arg2, that.arg2);
  }

  public boolean isOneLineInstruction() {
    return (opCode == OpCode.BYTE || opCode == OpCode.SIGNEXTEND) && !arg1.getHigh().isZero();
  }

  public int maxCt() {
    return isOneLineInstruction() ? 1 : LIMB_SIZE;
  }

  public boolean isSmall() {
    return arg1.getBytes32().compareTo(Bytes.of(32)) < 0;
  }

  private int getPivotThreshold() {
    return switch (opCode) {
      case AND, OR, XOR, NOT -> 16;
      case BYTE -> low4;
      case SIGNEXTEND -> 15 - low4;
      default -> throw new IllegalStateException("Bin doesn't support OpCode" + opCode);
    };
  }

  public BaseBytes getResult() {
    return switch (opCode) {
      case AND -> arg1.and(arg2);
      case OR -> arg1.or(arg2);
      case XOR -> arg1.xor(arg2);
      case NOT -> arg1.not();
      case BYTE -> byteResult();
      case SIGNEXTEND -> signExtensionResult();
      default -> throw new IllegalStateException("Bin doesn't support OpCode" + opCode);
    };
  }

  private BaseBytes signExtensionResult() {
    if (!isSmall) {
      return arg2;
    }
    final int indexLeadingByte = 30 - arg1.getLow().toUnsignedBigInteger().intValueExact();
    final byte toSet = (byte) (arg2().getByte(indexLeadingByte) < 0 ? 0xFF : 0x00);
    return BaseBytes.fromBytes32(
        Bytes32.leftPad(arg2.getBytes32().slice(indexLeadingByte, 32 - indexLeadingByte), toSet));
  }

  private BaseBytes byteResult() {
    final int result = isSmall ? pivot : 0;
    return BaseBytes.fromBytes32(Bytes32.leftPad(Bytes.ofUnsignedShort(result)));
  }

  public List<Boolean> getLastEightBits() {
    final int leastByteOfArg1 = arg1().getByte(31);
    return bitDecomposition(leastByteOfArg1, 8).bitDecList();
  }

  public boolean getBit4() {
    return getLastEightBits().get(3);
  }

  public int getLow4() {
    int r = 0;
    for (int k = 0; k < 4; k++) {
      if (lastEightBits.get(7 - k)) {
        r += (int) Math.pow(2, k);
      }
    }
    return r;
  }

  public List<Boolean> getBit1() {
    return plateau(pivotThreshold);
  }

  private List<Boolean> plateau(final int threshold) {
    ArrayList<Boolean> output = new ArrayList<>(16);
    for (int ct = 0; ct < 16; ct++) {
      output.add(ct, ct >= threshold);
    }
    return output;
  }

  private int getPivot() {
    switch (opCode) {
      case AND, OR, XOR, NOT -> {
        return 0;
      }
      case BYTE -> {
        if (low4 == 0) {
          return !bit4 ? arg2.getHigh().get(0) : arg2.getLow().get(0);
        } else {
          return !bit4 ? arg2.getHigh().get(pivotThreshold) : arg2.getLow().get(pivotThreshold);
        }
      }
      case SIGNEXTEND -> {
        if (low4 == 15) {
          return !bit4 ? arg2.getLow().get(0) : arg2.getHigh().get(0);
        } else {
          return !bit4 ? arg2.getLow().get(pivotThreshold) : arg2.getHigh().get(pivotThreshold);
        }
      }
      default -> throw new IllegalStateException("Bin doesn't support OpCode" + opCode);
    }
  }

  public List<Boolean> getFirstEightBits() {
    return bitDecomposition(pivot, 8).bitDecList();
  }
}
