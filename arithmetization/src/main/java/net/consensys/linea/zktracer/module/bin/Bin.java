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
import java.util.stream.Stream;

import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.bytestheta.BaseBytes;
import net.consensys.linea.zktracer.container.stacked.set.StackedSet;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.Bytes16;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.evm.frame.MessageFrame;

/** Implementation of a {@link Module} for addition/subtraction. */
public class Bin implements Module {
  private final Hub hub;

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
        opCode == OpCode.NOT ? Bytes32.ZERO : Bytes32.leftPad(frame.getStackItem(1));

    this.chunks.add(
        new BinOperation(opCode, BaseBytes.fromBytes32(arg1), BaseBytes.fromBytes32(arg2)));
  }

  @Override
  public void commit(List<MappedByteBuffer> buffers) {
    final Trace trace = new Trace(buffers);

    int stamp = 0;
    for (BinOperation op : this.chunks) {
      stamp++;
      traceChunk(op, stamp, trace);
    }
  }

  private void traceChunk(BinOperation op, int stamp, Trace trace) {
    final int ctMax = op.maxCt();
    final Bytes16 resHi = op.getResult().getHigh();
    final Bytes16 resLo = op.arg1().getLow();
    final List<Boolean> bit1 = op.getBit1();
    final List<Boolean> bits =
        Stream.concat(op.getFirstEightBits().stream(), op.lastEightBits.stream()).toList();
    for (int ct = 0; ct < ctMax; ct++) {
      trace
          .stamp(Bytes.ofUnsignedInt(stamp))
          .oneLineInstruction(ctMax == 1)
          .mli(ctMax != 1)
          .counter(UnsignedByte.of(ct))
          .inst(Bytes.of(op.opCode().byteValue()))
          .argument1Hi(op.arg1().getHigh())
          .argument1Lo(op.arg1().getLow())
          .argument1Hi(op.arg2().getHigh())
          .argument2Lo(op.arg2().getLow())
          .resultHi(resHi)
          .resultLo(resLo)
          .isAnd(op.opCode() == OpCode.AND)
          .isOr(op.opCode() == OpCode.OR)
          .isXor(op.opCode() == OpCode.XOR)
          .isNot(op.opCode() == OpCode.NOT)
          .isByte(op.opCode() == OpCode.BYTE)
          .isSignextend(op.opCode() == OpCode.SIGNEXTEND)
          .small(op.isSmall)
          .bits(bits.get(ct))
          .bitB4(op.bit4)
          .low4(UnsignedByte.of(op.low4))
          .neg(bits.get(0))
          .bit1(bit1.get(ct))
          .pivot(UnsignedByte.of(op.pivot))
          .byte1(UnsignedByte.of(op.arg1().getHigh().get(ct)))
          .byte2(UnsignedByte.of(op.arg1().getLow().get(ct)))
          .byte3(UnsignedByte.of(op.arg2().getHigh().get(ct)))
          .byte4(UnsignedByte.of(op.arg2().getLow().get(ct)))
          .byte5(UnsignedByte.of(resHi.get(ct)))
          .byte6(UnsignedByte.of(resLo.get(ct)))
          .acc1(op.arg1().getHigh().slice(0, ct + 1))
          .acc2(op.arg1().getLow().slice(0, ct + 1))
          .acc3(op.arg2().getHigh().slice(0, ct + 1))
          .acc4(op.arg2().getLow().slice(0, ct + 1))
          .acc5(resHi.slice(0, ct + 1))
          .acc6(resLo.slice(0, ct + 1))
          .xxxByteHi(UnsignedByte.of(resHi.get(ct)))
          .xxxByteLo(UnsignedByte.of(resLo.get(ct)))
          .validateRow();
    }
  }

  @Override
  public List<ColumnHeader> columnsHeaders() {
    return Trace.headers(this.lineCount());
  }

  @Override
  public int lineCount() {
    int sum = 0;
    for (BinOperation op : this.chunks) {
      sum += op.maxCt();
    }
    return sum;
  }
}
