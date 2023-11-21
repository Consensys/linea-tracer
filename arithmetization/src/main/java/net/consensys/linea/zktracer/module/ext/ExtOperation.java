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

package net.consensys.linea.zktracer.module.ext;

import static net.consensys.linea.zktracer.module.Util.boolToInt;

import java.util.Objects;

import lombok.Getter;
import net.consensys.linea.zktracer.bytestheta.BaseBytes;
import net.consensys.linea.zktracer.bytestheta.BaseTheta;
import net.consensys.linea.zktracer.bytestheta.BytesArray;
import net.consensys.linea.zktracer.module.ext.calculator.AbstractExtCalculator;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes32;
import org.apache.tuweni.units.bigints.UInt256;

public class ExtOperation {
  private static final int MMEDIUM = 8;
  @Getter private final OpCode opCode;
  private final Bytes32 rawArg1;
  private final Bytes32 rawArg2;
  private final Bytes32 rawArg3;
  @Getter private final boolean oli;

  @Getter private BaseBytes arg1;
  @Getter private BaseBytes arg2;
  @Getter private BaseBytes arg3;
  @Getter private BaseTheta result;
  @Getter private BaseTheta aBytes;
  @Getter private BaseTheta bBytes;
  @Getter private BaseTheta cBytes;
  @Getter private BaseTheta deltaBytes;
  @Getter private BytesArray hBytes;
  @Getter private BaseTheta rBytes;
  @Getter private BytesArray iBytes;
  @Getter private BytesArray jBytes;
  @Getter private BytesArray qBytes;
  @Getter private boolean[] cmp = new boolean[8];
  @Getter boolean[] overflowH = new boolean[8];
  @Getter boolean[] overflowJ = new boolean[8];
  @Getter boolean[] overflowRes = new boolean[8];
  @Getter boolean[] overflowI = new boolean[8];

  /**
   * This custom hash function ensures that all identical operations are only traced once per
   * conflation block.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.opCode, this.rawArg1, this.rawArg2, this.rawArg3);
  }

  public ExtOperation(OpCode opCode, Bytes32 arg1, Bytes32 arg2, Bytes32 arg3) {
    this.opCode = opCode;
    this.rawArg1 = arg1.copy();
    this.rawArg2 = arg2.copy();
    this.rawArg3 = arg3.copy();
    this.oli = isOneLineInstruction();
  }

  public void setup() {
    this.arg1 = BaseBytes.fromBytes32(this.rawArg1);
    this.arg2 = BaseBytes.fromBytes32(this.rawArg2);
    this.arg3 = BaseBytes.fromBytes32(rawArg3);
    this.aBytes = BaseTheta.fromBytes32(this.rawArg1);
    this.bBytes = BaseTheta.fromBytes32(this.rawArg2);
    this.cBytes = BaseTheta.fromBytes32(this.rawArg3);
    this.iBytes = new BytesArray(7);
    this.jBytes = new BytesArray(8);
    this.qBytes = new BytesArray(8);
    this.deltaBytes = BaseTheta.fromBytes32(Bytes32.ZERO);
    this.hBytes = new BytesArray(6);

    AbstractExtCalculator computer = AbstractExtCalculator.create(opCode);
    UInt256 result = computer.computeResult(this.rawArg1, this.rawArg2, this.rawArg3);

    this.result = BaseTheta.fromBytes32(result);
    this.rBytes = BaseTheta.fromBytes32(result);

    if (!this.oli) {
      cmp = computer.computeComparisonFlags(cBytes, rBytes);
      deltaBytes = computer.computeDeltas(cBytes, rBytes);
      jBytes = computer.computeJs(this.rawArg1, this.rawArg2);
      qBytes = computer.computeQs(this.rawArg1, this.rawArg2, this.rawArg3);
      overflowH = computer.computeHs(aBytes, bBytes, hBytes);
      overflowI = computer.computeIs(qBytes, cBytes, iBytes);
      overflowJ = computer.computeOverflowJ(qBytes, cBytes, rBytes, iBytes, getSigma(), getTau());
      overflowRes =
          computer.computeOverflowRes(
              this.arg1, this.arg2, aBytes, bBytes, hBytes, getAlpha(), getBeta());
    }
  }

  public boolean getBit1() {
    return this.opCode == OpCode.MULMOD && this.rawArg1.isZero();
  }

  public boolean getBit2() {
    return this.opCode == OpCode.MULMOD && this.rawArg2.isZero();
  }

  public boolean getBit3() {
    return UInt256.ONE.compareTo(UInt256.fromBytes(this.rawArg3)) >= 0;
  }

  /** Returns true if any of the bit1, bit2, or bit3 flags are set. */
  private boolean isOneLineInstruction() {
    return getBit1() || getBit2() || getBit3();
  }

  public int maxCounter() {
    if (this.isOli()) {
      return 1;
    }

    return MMEDIUM;
  }

  private UInt256 getSigma() {
    return UInt256.valueOf(boolToInt(overflowI[0]));
  }

  private UInt256 getAlpha() {
    return UInt256.valueOf(boolToInt(overflowH[0]));
  }

  private UInt256 getBeta() {
    return UInt256.valueOf(boolToInt(overflowH[1]) + 2L * boolToInt(overflowH[2]));
  }

  private UInt256 getTau() {
    return UInt256.valueOf(boolToInt(overflowI[1]) + 2L * boolToInt(overflowI[2]));
  }
}
