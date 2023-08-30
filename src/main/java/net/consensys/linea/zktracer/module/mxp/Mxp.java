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

import java.math.BigInteger;
import java.util.List;

import net.consensys.linea.zktracer.bytes.Bytes16;
import net.consensys.linea.zktracer.bytes.UnsignedByte;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.opcode.OpCodeData;
import net.consensys.linea.zktracer.opcode.OpCodes;
import net.consensys.linea.zktracer.opcode.gas.MxpType;
import org.apache.tuweni.bytes.Bytes32;
import org.apache.tuweni.units.bigints.UInt256;
import org.hyperledger.besu.evm.frame.MessageFrame;

/** Implementation of a {@link Module} for memory expansion. */
public class Mxp implements Module {
  public static final UInt256 TWO_POW_128 = UInt256.ONE.shiftLeft(128);
  public static final UInt256 TWO_POW_32 = UInt256.ONE.shiftLeft(32);

  public static final String MXP_JSON_KEY = "mxp";
  final Trace.TraceBuilder builder = Trace.builder();
  private int stamp = 0;

  private MxpType typeMxp;
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

  @Override
  public String jsonKey() {
    return MXP_JSON_KEY;
  }

  @Override
  public final List<OpCode> supportedOpCodes() {
    return List.of(OpCode.MLOAD, OpCode.MSTORE, OpCode.MSTORE8);
  }

  @Override
  public void trace(MessageFrame frame) {
    final OpCodeData opCodeData = OpCodes.of(frame.getCurrentOperation().getOpcode());
    final OpCode opCode = opCodeData.mnemonic();

    final Bytes32 arg1 = Bytes32.leftPad(frame.getStackItem(0));
    final Bytes16 arg1Hi = Bytes16.wrap(arg1.slice(0, 16));
    final Bytes32 arg1Lo = Bytes32.leftPad(arg1.slice(16));

    Bytes32 arg2 = Bytes32.ZERO;
    if (opCode.getData().numberOfArguments() > 1) {
      arg2 = Bytes32.leftPad(frame.getStackItem(1));
    }
    final Bytes16 arg2Hi = Bytes16.wrap(arg2.slice(0, 16));
    final Bytes16 arg2Lo = Bytes16.wrap(arg2.slice(16));

    this.typeMxp = opCode.getData().billing().type();
    setSizesAndOffsets();
    setMaxOffset1and2();
    this.roob = getRoob(typeMxp, offset1, size1, offset2, size2);
    this.noop = getNoop(roob, typeMxp, size1, size2);
    setMaxOffsetAndMxpx();

    stamp++;

    for (int i = 0; i < 16; i++) {

      builder
          .counter(BigInteger.valueOf(i))
          .mxpInst(BigInteger.valueOf(opCodeData.value()))
          .offset1Hi(offset1.toUnsignedBigInteger())
          .offset1Lo(offset1.toUnsignedBigInteger())
          .offset2Hi(offset2.toUnsignedBigInteger())
          .offset2Lo(offset2.toUnsignedBigInteger())
          //              .size1Hi()
          //              .size1Lo()
          //              .size2Hi()
          //              .size2Lo()
          //          .acc1(acc.slice(0, 1 + i).toUnsignedBigInteger())
          .acc2(arg1Lo.slice(0, 1 + i).toUnsignedBigInteger())
          .acc3(arg2Hi.slice(0, 1 + i).toUnsignedBigInteger())
          .acc4(arg2Lo.slice(0, 1 + i).toUnsignedBigInteger())
          //              .accA()
          .byte1(UnsignedByte.of(arg1Hi.get(i)))
          .byte2(UnsignedByte.of(arg1Lo.get(i)))
          .byte3(UnsignedByte.of(arg2Hi.get(i)))
          .byte4(UnsignedByte.of(arg2Lo.get(i)))
          .stamp(BigInteger.valueOf(stamp))
          .validateRow();
    }
  }

  @Override
  public Object commit() {
    return new MxpTrace(builder.build());
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

  /** get ridiculously out of bounds. */
  public static boolean getRoob(
      final MxpType typeMxp,
      final UInt256 offset1,
      final UInt256 size1,
      final UInt256 offset2,
      final UInt256 size2) {
    return switch (typeMxp) {
      case TYPE_2 , TYPE_3 -> offset1.greaterOrEqualThan(TWO_POW_128);
      case TYPE_4 -> size1.greaterOrEqualThan(TWO_POW_128)
          || (offset1.greaterOrEqualThan(TWO_POW_128) && !size1.isZero());
      case TYPE_5 -> size1.greaterOrEqualThan(TWO_POW_128)
          || (offset1.greaterOrEqualThan(TWO_POW_128) && !size1.isZero())
          || (size2.greaterOrEqualThan(TWO_POW_128)
              || (offset2.greaterOrEqualThan(TWO_POW_128) && !size2.isZero()));
      default -> false;
    };
  }

  /** get no op. */
  public static boolean getNoop(
      final boolean roob, final MxpType typeMxp, final UInt256 size1, final UInt256 size2) {
    if (roob) {
      return false;
    }
    return switch (typeMxp) {
      case TYPE_1 -> true;
      case TYPE_4 -> size1.isZero();
      case TYPE_5 -> size1.isZero() && size2.isZero();
      default -> false;
    };
  }

  /** set max offsets 1 and 2. */
  public void setMaxOffset1and2() {
    switch (typeMxp) {
      case TYPE_2 -> maxOffset1 = offset1.add(UInt256.valueOf(31));
      case TYPE_3 -> maxOffset1 = offset1;
      case TYPE_4 -> maxOffset1 = offset1.add(size1).subtract(UInt256.ONE);
      case TYPE_5 -> {
        if (!size1.isZero()) {
          maxOffset1 = offset1.add(size1).subtract(UInt256.ONE);
        }
        if (!size2.isZero()) {
          maxOffset2 = offset2.add(size2).subtract(UInt256.ONE);
        }
      }
      default -> {
        maxOffset1 = UInt256.ZERO;
        maxOffset2 = UInt256.ZERO;
      }
    }
  }

  /** compare max offsets 1 and 2. */
  public static boolean getComp(final UInt256 maxOffset1, final UInt256 maxOffset2) {
    return !(maxOffset1.lessThan(maxOffset2));
  }

  /** set max offset and mxpx. */
  public void setMaxOffsetAndMxpx() {
    if (roob || noop) {
      mxpx = roob;
    } else {
      // choose the max value
      maxOffset = maxOffset1.greaterThan(maxOffset2) ? maxOffset1 : maxOffset2;
      mxpx = maxOffset.greaterOrEqualThan(TWO_POW_32);
    }
  }
}
