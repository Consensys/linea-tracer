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

import java.nio.MappedByteBuffer;
import java.util.List;

import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.bytestheta.BaseBytes;
import net.consensys.linea.zktracer.container.stacked.set.StackedSet;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.Util;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.Bytes16;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.evm.frame.MessageFrame;

/** Implementation of a {@link Module} for addition/subtraction. */
public class Bin implements Module {
  private static final byte NOT = (byte) 0x19;

  private final Hub hub;
  private int stamp = 0;

  /** A set of the operations to trace */
  private final StackedSet<BinOperation> chunks = new StackedSet<>();

  public Bin(final Hub hub) {
    this.hub = hub;
  }

  @Override
  public String moduleKey() {
    return "BIN";
  }

  @Override
  public void enterTransaction() {
    this.chunks.enter();
  }

  @Override
  public void popTransaction() {
    this.chunks.pop();
  }

  @Override
  public void tracePreOpcode(MessageFrame frame) {
    final OpCode opCode = this.hub.opCode();
    final Bytes32 arg1 = Bytes32.leftPad(frame.getStackItem(0));
    final Bytes32 arg2 =
        isOpCodeNot(opCode) ? Bytes32.ZERO : Bytes32.leftPad(frame.getStackItem(1));

    this.chunks.add(
        new BinOperation(opCode, BaseBytes.fromBytes32(arg1), BaseBytes.fromBytes32(arg2)));
  }

  @Override
  public void commit(List<MappedByteBuffer> buffers) {
    final Trace trace = new Trace(buffers);
    for (BinOperation op : this.chunks) {
      this.traceBinOperation(op, trace);
    }
  }

  private void traceBinOperation(final BinOperation op, final Trace trace) {
    this.stamp++;

    final Bytes16 arg1Hi = op.arg1Hi();
    final Bytes16 arg1Lo = op.arg1Lo();
    final Bytes16 arg2Hi = op.arg2Hi();
    final Bytes16 arg2Lo = op.arg2Lo();

    final BaseBytes result = op.getRes();
    final Bytes16 resultHi = result.getHigh();
    final Bytes16 resultLo = result.getLow();

    final UnsignedByte pivot = op.pivot();
    final Bytes pivotBytes = Bytes.of(pivot.toByte());
    final Boolean[] pivotBooleanBytes = Util.byteBits(pivot);

    final UnsignedByte lsb = UnsignedByte.of(arg1Lo.get(15));
    final Boolean[] lsbBooleanBytes = Util.byteBits(lsb);

    final Boolean[] bits = new Boolean[16];
    for (int i = 0; i < 16; i++) {
      if (i < 8) {
        bits[i] = pivotBooleanBytes[i];
      } else {
        bits[i] = lsbBooleanBytes[i - 8];
      }
    }

    final BaseBytes andRes = op.arg1().and(op.arg2());
    final BaseBytes orRes = op.arg1().or(op.arg2());
    final BaseBytes xorRes = op.arg1().xor(op.arg2());
    final BaseBytes notArg1Res = op.arg1().not();

    for (int i = 0; i < op.maxCt(); i++) {
      trace
          .binaryStamp(Bytes.ofUnsignedInt(stamp))
          .oneLineInstruction(op.isOneLineInstruction())
          .counter(Bytes.ofUnsignedInt(i))
          .argument1Hi(arg1Hi)
          .argument1Lo(arg1Lo)
          .argument2Hi(arg2Hi)
          .argument2Lo(arg2Lo)
          .resultHi(result.getHigh())
          .resultLo(result.getLow())
          .byte1(UnsignedByte.of(arg1Hi.get(i)))
          .byte2(UnsignedByte.of(arg1Lo.get(i)))
          .byte3(UnsignedByte.of(arg2Hi.get(i)))
          .byte4(UnsignedByte.of(arg2Lo.get(i)))
          .byte5(UnsignedByte.of(resultHi.get(i)))
          .byte6(UnsignedByte.of(resultLo.get(i)))
          .acc1(arg1Hi.slice(0, 1 + i))
          .acc2(arg1Lo.slice(0, 1 + i))
          .acc3(arg2Hi.slice(0, 1 + i))
          .acc4(arg2Lo.slice(0, 1 + i))
          .acc5(result.getHigh().slice(0, 1 + i))
          .acc6(result.getLow().slice(0, 1 + i))
          .bits(bits[i])
          .bit1(i >= op.low4().toInteger())
          .bitB4(op.bitB4())
          .inst(Bytes.of(op.opCode().byteValue()))
          .low4(Bytes.of(op.low4().toByte()))
          .neg(op.neg())
          .andByteHi(Bytes.of(andRes.getHigh().get(i)))
          .andByteLo(Bytes.of(andRes.getLow().get(i)))
          .orByteHi(Bytes.of(orRes.getHigh().get(i)))
          .orByteLo(Bytes.of(orRes.getLow().get(i)))
          .xorByteHi(Bytes.of(xorRes.getHigh().get(i)))
          .xorByteLo(Bytes.of(xorRes.getLow().get(i)))
          .notByteHi(Bytes.of(notArg1Res.getHigh().get(i)))
          .notByteLo(Bytes.of(notArg1Res.getLow().get(i)))
          .pivot(pivotBytes)
          .small(op.isSmall())
          .isData(stamp != 0)
          .validateRow();
    }
  }

  @Override
  public int lineCount() {
    return this.chunks.stream().mapToInt(BinOperation::maxCt).sum();
  }

  @Override
  public List<ColumnHeader> columnsHeaders() {
    return Trace.headers(this.lineCount());
  }

  private boolean isOpCodeNot(final OpCode opCode) {
    return opCode.byteValue() == NOT;
  }
}
