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
package net.consensys.linea.zktracer.module.mxp;

import net.consensys.linea.zktracer.OpCode;
import net.consensys.linea.zktracer.module.instruction_decoder.Columns;
import net.consensys.linea.zktracer.module.instruction_decoder.InstructionDecoder;
import org.apache.tuweni.bytes.Bytes32;
import org.apache.tuweni.units.bigints.UInt256;

class MxpData {
  public static final UInt256 TWO_POW_128 = UInt256.valueOf(2).pow(128);
  public static final UInt256 TWO_POW_32 = UInt256.valueOf(2).pow(32);

  final OpCode opCode;
  //  private final Bytes16 arg1Hi;
  //  private final Bytes16 arg1Lo;
  //  private final Bytes16 arg2Hi;
  //  private final Bytes16 arg2Lo;
  private int typeMxp;
  private UInt256 offset1;
  private UInt256 size1;
  private UInt256 offset2;
  private UInt256 size2;
  private UInt256 maxOffset1;
  private UInt256 maxOffset2;
  private UInt256 maxOffset;
  private boolean roob;
  private boolean noop;
  private boolean mxpx;

  public MxpData(final OpCode opCode, final Bytes32 arg1, final Bytes32 arg2) {
    this.opCode = opCode;
    this.typeMxp = getTypeFromOpCode(opCode);
    setSizesAndOffsets();
    setMaxOffset1and2();
    this.roob = getRoob(typeMxp, offset1, size1, offset2, size2);
    this.noop = getNoop(roob, typeMxp, size1, size2);
    setMaxOffsetAndMXPX();

    //    this.arg1Hi = Bytes16.wrap(arg1.slice(0, 16));
    //    this.arg1Lo = Bytes16.wrap(arg1.slice(16));
    //    this.arg2Hi = Bytes16.wrap(arg2.slice(0, 16));
    //    this.arg2Lo = Bytes16.wrap(arg2.slice(16));

  }

  private void setSizesAndOffsets() {
    offset1 = UInt256.ZERO;
    offset2 = UInt256.ZERO;
    size1 = UInt256.ZERO;
    size2 = UInt256.ZERO;

    maxOffset1 = UInt256.ZERO;
    maxOffset2 = UInt256.ZERO;
    maxOffset = UInt256.ZERO;
  }

  private int getTypeFromOpCode(final OpCode opCode) {
    int t = InstructionDecoder.get(opCode, Columns.MXP_TYPE_1);
    t += 2 * InstructionDecoder.get(opCode, Columns.MXP_TYPE_2);
    t += 3 * InstructionDecoder.get(opCode, Columns.MXP_TYPE_3);
    t += 4 * InstructionDecoder.get(opCode, Columns.MXP_TYPE_4);
    t += 5 * InstructionDecoder.get(opCode, Columns.MXP_TYPE_5);
    return t;
  }

  public static boolean getRoob(
      final int typeMxp,
      final UInt256 offset1,
      final UInt256 size1,
      final UInt256 offset2,
      final UInt256 size2) {
    return switch (typeMxp) {
      case 1 -> false;
      case 2, 3 -> offset1.greaterOrEqualThan(TWO_POW_128);
      case 4 -> size1.greaterOrEqualThan(TWO_POW_128)
          || (offset1.greaterOrEqualThan(TWO_POW_128) && !size1.isZero());
      case 5 -> size1.greaterOrEqualThan(TWO_POW_128)
          || (offset1.greaterOrEqualThan(TWO_POW_128) && !size1.isZero())
          || (size2.greaterOrEqualThan(TWO_POW_128)
              || (offset2.greaterOrEqualThan(TWO_POW_128) && !size2.isZero()));
      default -> false;
    };
  }

  public static boolean getNoop(
      final boolean roob, final int typeMxp, final UInt256 size1, final UInt256 size2) {
    if (roob) {
      return false;
    }
    return switch (typeMxp) {
      case 1 -> true;
      case 4 -> size1.isZero();
      case 5 -> size1.isZero() && size2.isZero();
      default -> false;
    };
  }

  public void setMaxOffset1and2() {
    switch (getTypeMxp()) {
      case 2 -> maxOffset1 = getOffset1().add(UInt256.valueOf(31));
      case 3 -> maxOffset1 = getOffset1();
      case 4 -> maxOffset1 = getOffset1().add(getSize1()).subtract(UInt256.ONE);
      case 5 -> {
        if (!getSize1().isZero()) {
          maxOffset1 = getOffset1().add(getSize1()).subtract(UInt256.ONE);
        }
        if (!getSize2().isZero()) {
          maxOffset2 = getOffset2().add(getSize2()).subtract(UInt256.ONE);
        }
      }
    }
  }

  public static boolean getComp(final UInt256 maxOffset1, final UInt256 maxOffset2) {
    return !(maxOffset1.lessThan(maxOffset2));
  }

  public void setMaxOffsetAndMXPX() {
    if (isRoob() || isNoop()) {
      mxpx = isRoob();
    } else {
      // choose the max value
      maxOffset = getMaxOffset1().greaterThan(getMaxOffset2()) ? getMaxOffset1() : getMaxOffset2();
      mxpx = getMaxOffset().greaterOrEqualThan(TWO_POW_32);
    }
  }

  public int getTypeMxp() {
    return typeMxp;
  }

  public UInt256 getOffset1() {
    return offset1;
  }

  public UInt256 getSize1() {
    return size1;
  }

  public UInt256 getOffset2() {
    return offset2;
  }

  public UInt256 getSize2() {
    return size2;
  }

  public UInt256 getMaxOffset1() {
    return maxOffset1;
  }

  public UInt256 getMaxOffset2() {
    return maxOffset2;
  }

  public UInt256 getMaxOffset() {
    return maxOffset;
  }

  public boolean isRoob() {
    return roob;
  }

  public boolean isNoop() {
    return noop;
  }

  public boolean isMxpx() {
    return mxpx;
  }
}
