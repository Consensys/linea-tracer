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

package net.consensys.linea.zktracer.module.mmu;

import static net.consensys.linea.zktracer.module.mmu.MicroDataProcessor.*;
import static net.consensys.linea.zktracer.types.Conversions.booleanToBytes;
import static net.consensys.linea.zktracer.types.Conversions.unsignedBytesToBytes;

import java.nio.MappedByteBuffer;
import java.util.List;
import java.util.Objects;

import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.container.stacked.list.StackedList;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.mmio.Mmio;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.runtime.callstack.CallStack;
import net.consensys.linea.zktracer.runtime.microdata.InstructionContext;
import net.consensys.linea.zktracer.runtime.microdata.MicroData;
import net.consensys.linea.zktracer.runtime.microdata.Pointers;
import net.consensys.linea.zktracer.runtime.stack.StackOperation;
import net.consensys.linea.zktracer.types.EWord;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.tuweni.bytes.Bytes;

public class Mmu implements Module {
  private final StackedList<MmuOperation> state = new StackedList<>();
  private int ramStamp;
  private final int microStamp = 0;
  private boolean isMicro;
  private final MicroDataProcessor microDataProcessor;
  private final Hub hub;
  private final Mmio mmio;
  private final CallStack callStack;

  public Mmu(final Hub hub, final Mmio mmio, final CallStack callStack) {
    this.hub = hub;
    this.callStack = callStack;
    this.mmio = mmio;
    this.microDataProcessor = new MicroDataProcessor(microStamp);
  }

  @Override
  public String moduleKey() {
    return "MMU";
  }

  @Override
  public void enterTransaction() {
    this.state.enter();
  }

  @Override
  public void popTransaction() {
    this.state.pop();
  }

  @Override
  public int lineCount() {
    return this.state.lineCount();
  }

  @Override
  public List<ColumnHeader> columnsHeaders() {
    return Trace.headers(this.lineCount());
  }

  @Override
  public void commit(List<MappedByteBuffer> buffers) {
    final Trace trace = new Trace(buffers);

    for (MmuOperation m : this.state) {
      traceMicroData(m.microData(), callStack, trace);
    }
  }

  /**
   * TODO: should be called from the hub.
   *
   * @param opCode
   * @param stackOps
   * @param callStack
   */
  public void handleRam(
      final OpCode opCode, final List<StackOperation> stackOps, final CallStack callStack) {
    MicroData microData = microDataProcessor.dispatchOpCode(opCode, stackOps, callStack);

    this.state.add(new MmuOperation(microData));
  }

  private void traceMicroData(MicroData microData, final CallStack callStack, Trace trace) {
    if (microData.skip()) {
      return;
    }

    this.ramStamp++;
    this.isMicro = false;
    int maxCounter = maxCounter(microData.pointers().oob());

    microData.processingRow(-1);

    while (microData.counter() < maxCounter) {
      microDataProcessor.initializePreProcessing(callStack);
      trace(microData, trace);
      microData.incrementCounter(1);
    }

    this.isMicro = true;
    microData.counter(0);
    microData.processingRow(0);

    while (microData.processingRow() < microData.readPad().totalNumber()) {
      microDataProcessor.initializeProcessing(callStack, microData);
      mmio.handleRam(microData, hub.state().stamps(), microStamp);
      trace(microData, trace);
      microData.incrementProcessingRow(1);
    }
  }

  private void trace(MicroData microData, Trace trace) {
    Pointers pointers = microData.pointers();

    Bytes value = microData.value();

    InstructionContext stackFrames = microData.instructionContext();

    UnsignedByte[] nibbles = microData.nibbles();

    boolean[] bits = microData.bits();

    EWord eStack1 = EWord.of(pointers.stack1());

    trace
        .ramStamp(Bytes.ofUnsignedInt(this.ramStamp))
        .microInstructionStamp(Bytes.EMPTY)
        .isMicroInstruction(this.isMicro)
        .off1Lo(eStack1.lo())
        .off2Lo(pointers.stack2())
        .off2Hi(pointers.stack2())
        .sizeImported(Bytes.ofUnsignedInt(microData.sizeImported()))
        .valHi(value)
        .valLo(value)
        .contextNumber(Bytes.ofUnsignedInt(stackFrames.self()))
        .caller(Bytes.ofUnsignedInt(stackFrames.caller()))
        .returner(Bytes.ofUnsignedInt(stackFrames.returner()))
        .contextSource(Bytes.ofUnsignedInt(microData.sourceContext()))
        .contextTarget(Bytes.ofUnsignedInt(microData.targetContext()))
        .counter(Bytes.ofUnsignedInt(microData.counter()))
        .offsetOutOfBounds(pointers.oob())
        .precomputation(Bytes.ofUnsignedInt(microData.precomputation()))
        .ternary(Bytes.ofUnsignedInt(microData.ternary()))
        .microInstruction(Bytes.ofUnsignedInt(microData.microOp()))
        .exoIsRom(microData.exoIsRom())
        .exoIsLog(microData.exoIsLog())
        .exoIsHash(microData.exoIsHash())
        .exoIsTxcd(microData.exoIsTxcd())
        .sourceLimbOffset(microData.sourceLimbOffset())
        .sourceByteOffset(Bytes.of(microData.sourceByteOffset().toByte()))
        .targetLimbOffset(microData.targetLimbOffset())
        .targetByteOffset(Bytes.of(microData.targetByteOffset().toByte()))
        .size(Bytes.ofUnsignedInt(microData.size()))
        .nib1(Objects.requireNonNullElse(nibbles[0], UnsignedByte.ZERO))
        .nib2(Objects.requireNonNullElse(nibbles[1], UnsignedByte.ZERO))
        .nib3(Objects.requireNonNullElse(nibbles[2], UnsignedByte.ZERO))
        .nib4(Objects.requireNonNullElse(nibbles[3], UnsignedByte.ZERO))
        .nib5(Objects.requireNonNullElse(nibbles[4], UnsignedByte.ZERO))
        .nib6(Objects.requireNonNullElse(nibbles[5], UnsignedByte.ZERO))
        .nib7(Objects.requireNonNullElse(nibbles[6], UnsignedByte.ZERO))
        .nib8(Objects.requireNonNullElse(nibbles[7], UnsignedByte.ZERO))
        .nib9(Objects.requireNonNullElse(nibbles[8], UnsignedByte.ZERO))
        .acc1(acc(0, microData))
        .byte1(accByte(0, microData))
        .acc2(acc(1, microData))
        .byte2(accByte(1, microData))
        .acc3(acc(2, microData))
        .byte3(accByte(2, microData))
        .acc4(acc(3, microData))
        .byte4(accByte(3, microData))
        .acc5(acc(4, microData))
        .byte5(accByte(4, microData))
        .acc6(acc(5, microData))
        .byte6(accByte(5, microData))
        .acc7(acc(6, microData))
        .byte7(accByte(6, microData))
        .acc8(acc(7, microData))
        .byte8(accByte(7, microData))
        .bit1(bits[0])
        .bit2(bits[1])
        .bit3(bits[2])
        .bit4(bits[3])
        .bit5(bits[4])
        .bit6(bits[5])
        .bit7(bits[6])
        .bit8(bits[7])
        .aligned(Bytes.of(BooleanUtils.toInteger(microData.aligned())))
        .fast(booleanToBytes(microData.isFast()))
        .min(Bytes.ofUnsignedInt(microData.min()))
        .callDataOffset(Bytes.ofUnsignedInt(microData.callDataOffset()))
        .callStackDepth(Bytes.ofUnsignedInt(microData.callStackDepth()))
        .callDataSize(Bytes.ofUnsignedInt(microData.callDataSize()))
        .instruction(Bytes.ofUnsignedInt(microData.opCode().getData().value()))
        .totalNumberOfMicroInstructions(
            Bytes.ofUnsignedInt(this.ramStamp == 0 ? 0 : microData.remainingMicroInstructions()))
        .totalNumberOfReads(Bytes.ofUnsignedInt(microData.remainingReads()))
        .totalNumberOfPaddings(Bytes.ofUnsignedInt(microData.remainingPads()))
        .toRam(microData.toRam())
        .erf(isMicro && microData.isErf())
        .returnOffset(
            stackFrames.returnOffset().isUInt64() || stackFrames.returnCapacity() == 0
                ? Bytes.EMPTY
                : stackFrames.returnOffset())
        .returnCapacity(Bytes.ofUnsignedInt(stackFrames.returnCapacity()))
        .refs(Bytes.ofUnsignedInt(microData.referenceSize()))
        .refo(Bytes.ofUnsignedInt(microData.referenceOffset()))
        .info(booleanToBytes(microData.info()))
        .isData(this.ramStamp != 0)
        .validateRow();
  }

  private Bytes acc(final int accIndex, final MicroData microData) {
    int maxCounter = maxCounter(microData.pointers().oob());

    return unsignedBytesToBytes(
        ArrayUtils.subarray(
            microData.accs()[accIndex],
            32 - maxCounter,
            32 - maxCounter + microData.counter() + 1));
  }

  private UnsignedByte accByte(final int accIndex, final MicroData microData) {
    int maxCounter = maxCounter(microData.pointers().oob());

    return Objects.requireNonNullElse(
        microData.accs()[accIndex][32 - maxCounter + microData.counter()], UnsignedByte.ZERO);
  }
}
