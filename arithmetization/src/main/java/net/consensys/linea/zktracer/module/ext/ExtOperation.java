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
import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.module.ext.calculator.AbstractExtCalculator;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.apache.tuweni.units.bigints.UInt256;

public class ExtOperation extends ModuleOperation {
  private static final int MMEDIUM = 8;

  @Getter private final OpCode opCode;
  @Getter private final boolean oli;
  @Getter private final BaseBytes arg1;
  @Getter private final BaseBytes arg2;
  @Getter private final BaseBytes arg3;

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
    return Objects.hash(this.opCode, this.arg1, this.arg2, this.arg3);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final ExtOperation that = (ExtOperation) o;
    return Objects.equals(this.opCode, that.opCode)
        && Objects.equals(this.arg1, that.arg1)
        && Objects.equals(this.arg2, that.arg2)
        && Objects.equals(this.arg3, that.arg3);
  }

  public ExtOperation(OpCode opCode, Bytes32 arg1, Bytes32 arg2, Bytes32 arg3) {
    this.opCode = opCode;
    this.arg1 = BaseBytes.fromBytes32(arg1.copy());
    this.arg2 = BaseBytes.fromBytes32(arg2.copy());
    this.arg3 = BaseBytes.fromBytes32(arg3.copy());
    this.oli = isOneLineInstruction();
  }

  public UInt256 compute() {
    AbstractExtCalculator computer = AbstractExtCalculator.create(this.opCode);
    return computer.computeResult(
        this.arg1.getBytes32(), this.arg2.getBytes32(), this.arg3.getBytes32());
  }

  public void setup() {
    this.aBytes = BaseTheta.fromBytes32(this.arg1.getBytes32());
    this.bBytes = BaseTheta.fromBytes32(this.arg2.getBytes32());
    this.cBytes = BaseTheta.fromBytes32(this.arg3.getBytes32());
    this.iBytes = new BytesArray(7);
    this.jBytes = new BytesArray(8);
    this.qBytes = new BytesArray(8);
    this.deltaBytes = BaseTheta.fromBytes32(Bytes32.ZERO);
    this.hBytes = new BytesArray(6);

    AbstractExtCalculator computer = AbstractExtCalculator.create(this.opCode);

    final UInt256 result = this.compute();
    this.result = BaseTheta.fromBytes32(result);
    this.rBytes = BaseTheta.fromBytes32(result);

    if (!this.oli) {
      cmp = computer.computeComparisonFlags(cBytes, rBytes);
      deltaBytes = computer.computeDeltas(cBytes, rBytes);
      jBytes = computer.computeJs(this.arg1.getBytes32(), this.arg2.getBytes32());
      qBytes =
          computer.computeQs(
              this.arg1.getBytes32(), this.arg2.getBytes32(), this.arg3.getBytes32());
      overflowH = computer.computeHs(aBytes, bBytes, hBytes);
      overflowI = computer.computeIs(qBytes, cBytes, iBytes);
      overflowJ = computer.computeOverflowJ(qBytes, cBytes, rBytes, iBytes, getSigma(), getTau());
      overflowRes =
          computer.computeOverflowRes(
              this.arg1, this.arg2, aBytes, bBytes, hBytes, getAlpha(), getBeta());
    }
  }

  public boolean getBit1() {
    return this.opCode == OpCode.MULMOD && this.arg1.isZero();
  }

  public boolean getBit2() {
    return this.opCode == OpCode.MULMOD && this.arg2.isZero();
  }

  public boolean getBit3() {
    return UInt256.ONE.compareTo(UInt256.fromBytes(this.arg3.getBytes32())) >= 0;
  }

  /** Returns true if any of the bit1, bit2, or bit3 flags are set. */
  private boolean isOneLineInstruction() {
    return getBit1() || getBit2() || getBit3();
  }

  private int maxCt() {
    if (this.isOli()) {
      return 1;
    }

    return MMEDIUM;
  }

  @Override
  protected int computeLineCount() {
    return this.maxCt();
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

  void trace(Trace trace, int stamp) {
    this.setup();

    for (int i = 0; i < this.maxCt(); i++) {
      final int accLength = i + 1;
      trace
          // Byte A and Acc A
          .byteA0(UnsignedByte.of(this.getABytes().get(0).get(i)))
          .byteA1(UnsignedByte.of(this.getABytes().get(1).get(i)))
          .byteA2(UnsignedByte.of(this.getABytes().get(2).get(i)))
          .byteA3(UnsignedByte.of(this.getABytes().get(3).get(i)))
          .accA0(this.getABytes().get(0).slice(0, accLength))
          .accA1(this.getABytes().get(1).slice(0, accLength))
          .accA2(this.getABytes().get(2).slice(0, accLength))
          .accA3(this.getABytes().get(3).slice(0, accLength))
          // Byte B and Acc B
          .byteB0(UnsignedByte.of(this.getBBytes().get(0).get(i)))
          .byteB1(UnsignedByte.of(this.getBBytes().get(1).get(i)))
          .byteB2(UnsignedByte.of(this.getBBytes().get(2).get(i)))
          .byteB3(UnsignedByte.of(this.getBBytes().get(3).get(i)))
          .accB0(this.getBBytes().get(0).slice(0, accLength))
          .accB1(this.getBBytes().get(1).slice(0, accLength))
          .accB2(this.getBBytes().get(2).slice(0, accLength))
          .accB3(this.getBBytes().get(3).slice(0, accLength))
          // Byte C and Acc C
          .byteC0(UnsignedByte.of(this.getCBytes().get(0).get(i)))
          .byteC1(UnsignedByte.of(this.getCBytes().get(1).get(i)))
          .byteC2(UnsignedByte.of(this.getCBytes().get(2).get(i)))
          .byteC3(UnsignedByte.of(this.getCBytes().get(3).get(i)))
          .accC0(this.getCBytes().get(0).slice(0, accLength))
          .accC1(this.getCBytes().get(1).slice(0, accLength))
          .accC2(this.getCBytes().get(2).slice(0, accLength))
          .accC3(this.getCBytes().get(3).slice(0, accLength))
          // Byte Delta and Acc Delta
          .byteDelta0(UnsignedByte.of(this.getDeltaBytes().get(0).get(i)))
          .byteDelta1(UnsignedByte.of(this.getDeltaBytes().get(1).get(i)))
          .byteDelta2(UnsignedByte.of(this.getDeltaBytes().get(2).get(i)))
          .byteDelta3(UnsignedByte.of(this.getDeltaBytes().get(3).get(i)))
          .accDelta0(this.getDeltaBytes().get(0).slice(0, accLength))
          .accDelta1(this.getDeltaBytes().get(1).slice(0, accLength))
          .accDelta2(this.getDeltaBytes().get(2).slice(0, accLength))
          .accDelta3(this.getDeltaBytes().get(3).slice(0, accLength))
          // Byte H and Acc H
          .byteH0(UnsignedByte.of(this.getHBytes().get(0).get(i)))
          .byteH1(UnsignedByte.of(this.getHBytes().get(1).get(i)))
          .byteH2(UnsignedByte.of(this.getHBytes().get(2).get(i)))
          .byteH3(UnsignedByte.of(this.getHBytes().get(3).get(i)))
          .byteH4(UnsignedByte.of(this.getHBytes().get(4).get(i)))
          .byteH5(UnsignedByte.of(this.getHBytes().get(5).get(i)))
          .accH0(this.getHBytes().get(0).slice(0, accLength))
          .accH1(this.getHBytes().get(1).slice(0, accLength))
          .accH2(this.getHBytes().get(2).slice(0, accLength))
          .accH3(this.getHBytes().get(3).slice(0, accLength))
          .accH4(this.getHBytes().get(4).slice(0, accLength))
          .accH5(this.getHBytes().get(5).slice(0, accLength))
          // Byte I and Acc I
          .byteI0(UnsignedByte.of(this.getIBytes().get(0).get(i)))
          .byteI1(UnsignedByte.of(this.getIBytes().get(1).get(i)))
          .byteI2(UnsignedByte.of(this.getIBytes().get(2).get(i)))
          .byteI3(UnsignedByte.of(this.getIBytes().get(3).get(i)))
          .byteI4(UnsignedByte.of(this.getIBytes().get(4).get(i)))
          .byteI5(UnsignedByte.of(this.getIBytes().get(5).get(i)))
          .byteI6(UnsignedByte.of(this.getIBytes().get(6).get(i)))
          .accI0(this.getIBytes().get(0).slice(0, accLength))
          .accI1(this.getIBytes().get(1).slice(0, accLength))
          .accI2(this.getIBytes().get(2).slice(0, accLength))
          .accI3(this.getIBytes().get(3).slice(0, accLength))
          .accI4(this.getIBytes().get(4).slice(0, accLength))
          .accI5(this.getIBytes().get(5).slice(0, accLength))
          .accI6(this.getIBytes().get(6).slice(0, accLength))
          // Byte J and Acc J
          .byteJ0(UnsignedByte.of(this.getJBytes().get(0).get(i)))
          .byteJ1(UnsignedByte.of(this.getJBytes().get(1).get(i)))
          .byteJ2(UnsignedByte.of(this.getJBytes().get(2).get(i)))
          .byteJ3(UnsignedByte.of(this.getJBytes().get(3).get(i)))
          .byteJ4(UnsignedByte.of(this.getJBytes().get(4).get(i)))
          .byteJ5(UnsignedByte.of(this.getJBytes().get(5).get(i)))
          .byteJ6(UnsignedByte.of(this.getJBytes().get(6).get(i)))
          .byteJ7(UnsignedByte.of(this.getJBytes().get(7).get(i)))
          .accJ0(this.getJBytes().get(0).slice(0, accLength))
          .accJ1(this.getJBytes().get(1).slice(0, accLength))
          .accJ2(this.getJBytes().get(2).slice(0, accLength))
          .accJ3(this.getJBytes().get(3).slice(0, accLength))
          .accJ4(this.getJBytes().get(4).slice(0, accLength))
          .accJ5(this.getJBytes().get(5).slice(0, accLength))
          .accJ6(this.getJBytes().get(6).slice(0, accLength))
          .accJ7(this.getJBytes().get(7).slice(0, accLength))
          // Byte Q and Acc Q
          .byteQ0(UnsignedByte.of(this.getQBytes().get(0).get(i)))
          .byteQ1(UnsignedByte.of(this.getQBytes().get(1).get(i)))
          .byteQ2(UnsignedByte.of(this.getQBytes().get(2).get(i)))
          .byteQ3(UnsignedByte.of(this.getQBytes().get(3).get(i)))
          .byteQ4(UnsignedByte.of(this.getQBytes().get(4).get(i)))
          .byteQ5(UnsignedByte.of(this.getQBytes().get(5).get(i)))
          .byteQ6(UnsignedByte.of(this.getQBytes().get(6).get(i)))
          .byteQ7(UnsignedByte.of(this.getQBytes().get(7).get(i)))
          .accQ0(this.getQBytes().get(0).slice(0, accLength))
          .accQ1(this.getQBytes().get(1).slice(0, accLength))
          .accQ2(this.getQBytes().get(2).slice(0, accLength))
          .accQ3(this.getQBytes().get(3).slice(0, accLength))
          .accQ4(this.getQBytes().get(4).slice(0, accLength))
          .accQ5(this.getQBytes().get(5).slice(0, accLength))
          .accQ6(this.getQBytes().get(6).slice(0, accLength))
          .accQ7(this.getQBytes().get(7).slice(0, accLength))
          // Byte R and Acc R
          .byteR0(UnsignedByte.of(this.getRBytes().get(0).get(i)))
          .byteR1(UnsignedByte.of(this.getRBytes().get(1).get(i)))
          .byteR2(UnsignedByte.of(this.getRBytes().get(2).get(i)))
          .byteR3(UnsignedByte.of(this.getRBytes().get(3).get(i)))
          .accR0(this.getRBytes().get(0).slice(0, accLength))
          .accR1(this.getRBytes().get(1).slice(0, accLength))
          .accR2(this.getRBytes().get(2).slice(0, accLength))
          .accR3(this.getRBytes().get(3).slice(0, accLength))
          // other
          .arg1Hi(this.getArg1().getHigh())
          .arg1Lo(this.getArg1().getLow())
          .arg2Hi(this.getArg2().getHigh())
          .arg2Lo(this.getArg2().getLow())
          .arg3Hi(this.getArg3().getHigh())
          .arg3Lo(this.getArg3().getLow())
          .resHi(this.getResult().getHigh())
          .resLo(this.getResult().getLow())
          .cmp(this.getCmp()[i])
          .ofH(this.getOverflowH()[i])
          .ofJ(this.getOverflowJ()[i])
          .ofI(this.getOverflowI()[i])
          .ofRes(this.getOverflowRes()[i])
          .ct(Bytes.of(i))
          .inst(Bytes.of(this.getOpCode().byteValue()))
          .oli(this.isOli())
          .bit1(this.getBit1())
          .bit2(this.getBit2())
          .bit3(this.getBit3())
          .stamp(Bytes.ofUnsignedLong(stamp))
          .validateRow();
    }
  }
}
