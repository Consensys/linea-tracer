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

import static net.consensys.linea.zktracer.module.mmu.MicroDataProcessor.maxCounter;
import static net.consensys.linea.zktracer.types.Conversions.unsignedBytesToBytes;

import java.util.Objects;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.runtime.callstack.CallStack;
import net.consensys.linea.zktracer.runtime.microdata.InstructionContext;
import net.consensys.linea.zktracer.runtime.microdata.MicroData;
import net.consensys.linea.zktracer.runtime.microdata.Pointers;
import net.consensys.linea.zktracer.types.EWord;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.tuweni.bytes.Bytes;

@Accessors(fluent = true)
class MmuOperation extends ModuleOperation {
  @Getter private final MicroData microData;
  private final MicroDataProcessor microDataProcessor;

  private final CallStack callStack;
  private boolean isMicro;
  private final int nbPreprocessingRows = nbPreprocessingRows();
  int nbMmioRows;

  MmuOperation(
      MicroData microData,
      CallStack callStack,
      MicroDataProcessor microDataProcessor,
      boolean isMicro) {
    this.microData = microData;
    this.callStack = callStack;
    this.microDataProcessor = microDataProcessor;
    this.isMicro = isMicro;
  }

  @Override
  protected int computeLineCount() {
    return 1 + nbPreprocessingRows + nbMmioRows;
  }

  void trace(MicroData microData, final CallStack callStack, final int mmuStamp, final int mmioStamp, Trace trace) {
    if (microData.skip()) {
      return;
    }

    final Bytes mmuStampBytes = Bytes.ofUnsignedShort(mmuStamp);
    final Bytes mmioStampInit = Bytes.ofUnsignedShort(mmioStamp);

    // Trace Macro Instruction decoding Row
    traceMacroRow(mmuStampBytes, mmioStampInit, trace);

    // Trace Preprocessing rows
    tracePreprocessingRows(mmuStampBytes, mmioStampInit, trace);

    // Trace Micro Instructions Rows
    traceMicroRows(mmuStampBytes, mmioStamp, trace);
  }

  private void traceFillMmuInstructionFlag(Trace trace){
    trace.isMload()
      .isMstore()
      .isMstore8()
      .isInvalidCodePrefix()
      .isRightPaddedWordExtraction()
      .isRamToExoWithPadding()
      .isExoToRamTransplants()
      .isRamToRamSansPadding()
      .isAnyToRamWithPaddingSomeData()
      .isAnyToRamWithPaddingPurePadding()
      .isModexpZero()
      .isModexpData()
      .isBlakeParam();
  }

  private void traceOutAndBin(Trace trace){
    trace
      .out1()
      .out2()
      .out3()
      .out4()
      .out5()
      .bin1()
      .bin2()
      .bin3()
      .bin4()
      .bin5();
  }

  private int nbPreprocessingRows(){
    return switch ();
  }

  private void traceMacroRow(final Bytes mmuStamp, final Bytes mmioStamp, Trace trace){
    traceFillMmuInstructionFlag(trace);
    traceOutAndBin(trace);
    trace.stamp(mmuStamp)
      .mmioStamp(mmioStamp)
      .macro(true)
      .prprc(false)
      .micro(false)
      .tot(Bytes.ofUnsignedShort(nbMmioRows))
      .totlz()
      .totnt()
      .totrz()
      .pMacroInst()
      .pMacroSrcId()
      .pMacroTgtId()
      .pMacroAuxId()
      .pMacroSrcOffsetHi()
      .pMacroSrcOffsetLo()
      .pMacroSize()
      .pMacroRefOffset()
      .pMacroRefSize()
      .pMacroSuccessBit()
      .pMacroLimb1()
      .pMacroLimb2()
      .pMacroPhase()
      .pMacroExoSum()
      .fillAndValidateRow();
  }

  private void tracePreprocessingRows(final Bytes mmuStamp, final Bytes mmioStamp, Trace trace){
    for (int i = 0; i < this.nbPreprocessingRows; i++) {
      traceFillMmuInstructionFlag(trace);
      traceOutAndBin(trace);
      trace.stamp(mmuStamp)
        .mmioStamp(mmioStamp)
        .macro(false)
        .prprc(true)
        .micro(false)
        .tot(Bytes.ofUnsignedShort(nbMmioRows))
        .totlz()
        .totnt()
        .totrz()
        .pPrprcCt(UnsignedByte.of(this.nbPreprocessingRows -i))
        .pPrprcEucFlag()
        .pPrprcEucA()
        .pPrprcEucB()
        .pPrprcEucQuot()
        .pPrprcEucRem()
        .pPrprcEucCeil()
        .pPrprcWcpFlag()
        .pPrprcWcpArg1Hi()
        .pPrprcWcpArg1Lo()
        .pPrprcWcpArg2Hi()
        .pPrprcWcpArg2Lo()
        .pPrprcWcpRes()
        .pPrprcWcpInst()
        .fillAndValidateRow();
    }
  }

  private void traceMicroData(MicroData microData, Trace trace) {
    Pointers pointers = microData.pointers();

    Bytes value = microData.value();

    InstructionContext stackFrames = microData.instructionContext();

    UnsignedByte[] nibbles = microData.nibbles();

    boolean[] bits = microData.bits();

    EWord eStack1 = EWord.of(pointers.stack1());

    //    trace
    //        .ramStamp(Bytes.ofUnsignedInt(this.ramStamp))
    //        .microInstructionStamp(Bytes.EMPTY)
    //        .isMicroInstruction(this.isMicro)
    //        .off1Lo(eStack1.lo())
    //        .off2Lo(pointers.stack2())
    //        .off2Hi(pointers.stack2())
    //        .sizeImported(Bytes.ofUnsignedInt(microData.sizeImported()))
    //        .valHi(value)
    //        .valLo(value)
    //        .contextNumber(Bytes.ofUnsignedInt(stackFrames.self()))
    //        .caller(Bytes.ofUnsignedInt(stackFrames.caller()))
    //        .returner(Bytes.ofUnsignedInt(stackFrames.returner()))
    //        .contextSource(contextNumberOf(microData.sourceContextId()))
    //        .contextTarget(contextNumberOf(microData.targetContextId()))
    //        .counter(Bytes.ofUnsignedInt(microData.counter()))
    //        .offsetOutOfBounds(pointers.oob())
    //        .precomputation(Bytes.ofUnsignedInt(microData.precomputation()))
    //        .ternary(Bytes.ofUnsignedInt(microData.ternary()))
    //        .microInstruction(Bytes.ofUnsignedInt(microData.microOp()))
    //        .exoIsRom(microData.exoIsRom())
    //        .exoIsLog(microData.exoIsLog())
    //        .exoIsHash(microData.exoIsHash())
    //        .exoIsTxcd(microData.exoIsTxcd())
    //        .sourceLimbOffset(microData.sourceLimbOffset())
    //        .sourceByteOffset(Bytes.of(microData.sourceByteOffset().toByte()))
    //        .targetLimbOffset(microData.targetLimbOffset())
    //        .targetByteOffset(Bytes.of(microData.targetByteOffset().toByte()))
    //        .size(Bytes.ofUnsignedInt(microData.size()))
    //        .nib1(Objects.requireNonNullElse(nibbles[0], UnsignedByte.ZERO))
    //        .nib2(Objects.requireNonNullElse(nibbles[1], UnsignedByte.ZERO))
    //        .nib3(Objects.requireNonNullElse(nibbles[2], UnsignedByte.ZERO))
    //        .nib4(Objects.requireNonNullElse(nibbles[3], UnsignedByte.ZERO))
    //        .nib5(Objects.requireNonNullElse(nibbles[4], UnsignedByte.ZERO))
    //        .nib6(Objects.requireNonNullElse(nibbles[5], UnsignedByte.ZERO))
    //        .nib7(Objects.requireNonNullElse(nibbles[6], UnsignedByte.ZERO))
    //        .nib8(Objects.requireNonNullElse(nibbles[7], UnsignedByte.ZERO))
    //        .nib9(Objects.requireNonNullElse(nibbles[8], UnsignedByte.ZERO))
    //        .acc1(acc(0, microData))
    //        .byte1(accByte(0, microData))
    //        .acc2(acc(1, microData))
    //        .byte2(accByte(1, microData))
    //        .acc3(acc(2, microData))
    //        .byte3(accByte(2, microData))
    //        .acc4(acc(3, microData))
    //        .byte4(accByte(3, microData))
    //        .acc5(acc(4, microData))
    //        .byte5(accByte(4, microData))
    //        .acc6(acc(5, microData))
    //        .byte6(accByte(5, microData))
    //        .acc7(acc(6, microData))
    //        .byte7(accByte(6, microData))
    //        .acc8(acc(7, microData))
    //        .byte8(accByte(7, microData))
    //        .bit1(bits[0])
    //        .bit2(bits[1])
    //        .bit3(bits[2])
    //        .bit4(bits[3])
    //        .bit5(bits[4])
    //        .bit6(bits[5])
    //        .bit7(bits[6])
    //        .bit8(bits[7])
    //        .aligned(Bytes.of(BooleanUtils.toInteger(microData.aligned())))
    //        .fast(booleanToBytes(microData.isFast()))
    //        .min(Bytes.ofUnsignedInt(microData.min()))
    //        .callDataOffset(Bytes.ofUnsignedInt(microData.callDataOffset()))
    //        .callStackDepth(Bytes.ofUnsignedInt(microData.callStackDepth()))
    //        .callDataSize(Bytes.ofUnsignedInt(microData.callDataSize()))
    //        .instruction(Bytes.ofUnsignedInt(microData.opCode().getData().value()))
    //        .totalNumberOfMicroInstructions(
    //            Bytes.ofUnsignedInt(this.ramStamp == 0 ? 0 :
    // microData.remainingMicroInstructions()))
    //        .totalNumberOfReads(Bytes.ofUnsignedInt(microData.remainingReads()))
    //        .totalNumberOfPaddings(Bytes.ofUnsignedInt(microData.remainingPads()))
    //        .toRam(microData.toRam())
    //        .erf(isMicro && microData.isErf())
    //        .returnOffset(
    //            stackFrames.returnOffset().isUInt64() || stackFrames.returnCapacity() == 0
    //                ? Bytes.EMPTY
    //                : stackFrames.returnOffset())
    //        .returnCapacity(Bytes.ofUnsignedInt(stackFrames.returnCapacity()))
    //        .refs(Bytes.ofUnsignedInt(microData.referenceSize()))
    //        .refo(Bytes.ofUnsignedInt(microData.referenceOffset()))
    //        .info(booleanToBytes(microData.info()))
    //        .isData(this.ramStamp != 0)
    //        .validateRow();
=======
  private void traceMicroRows(final Bytes mmuStamp, int mmioStamp, Trace trace){
    for (int i = 0; i < this.nbMmioRows; i++) {
      mmioStamp +=1;
      traceFillMmuInstructionFlag(trace);
      traceOutAndBin(trace);
      trace.stamp(mmuStamp)
        .mmioStamp(Bytes.ofUnsignedShort(mmioStamp))
        .macro(false)
        .prprc(false)
        .micro(true)
        .tot()
        .totlz()
        .totnt()
        .totrz()
        .pMicroInst()
        .pMicroSize()
        .pMicroSlo()
        .pMicroSbo()
        .pMicroTlo()
        .pMicroTbo()
        .pMicroLimb()
        .pMicroCnS()
        .pMicroCnT()
        .pMicroSuccessBit()
        .pMicroExoSum()
        .pMicroPhase()
        .pMicroId1()
        .pMicroId2()
        .pMicroTotalSize()
        .fillAndValidateRow();
  }

  private Bytes contextNumberOf(int contextId) {
    return Bytes.ofUnsignedInt(callStack.get(contextId).contextNumber());
  }

  private Bytes acc(final int accIndex, final MicroData microData) {
    int maxCounter = maxCounter(microData);

    return unsignedBytesToBytes(
        ArrayUtils.subarray(
            microData.accs()[accIndex],
            32 - maxCounter,
            32 - maxCounter + microData.counter() + 1));
  }

  private UnsignedByte accByte(final int accIndex, final MicroData microData) {
    int maxCounter = maxCounter(microData);

    return Objects.requireNonNullElse(
        microData.accs()[accIndex][32 - maxCounter + microData.counter()], UnsignedByte.ZERO);
  }
}
