/*
* Copyright Consensys Software Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
except in compliance with
* the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software distributed under the
License is distributed on
* an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
the License for the
* specific language governing permissions and limitations under the License.
*
* SPDX-License-Identifier: Apache-2.0
*/

package net.consensys.linea.zktracer.module.mmu;

import static net.consensys.linea.zktracer.types.Conversions.*;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.module.mmu.precomputations.HubToMmuValues;
import net.consensys.linea.zktracer.module.mmu.precomputations.MmuEucCallRecord;
import net.consensys.linea.zktracer.module.mmu.precomputations.MmuOutAndBinValues;
import net.consensys.linea.zktracer.module.mmu.precomputations.MmuToMmioConstantValues;
import net.consensys.linea.zktracer.module.mmu.precomputations.MmuToMmioInstruction;
import net.consensys.linea.zktracer.module.mmu.precomputations.MmuWcpCallRecord;
import net.consensys.linea.zktracer.runtime.callstack.CallStack;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;

@Accessors(fluent = true)
class MmuOperation extends ModuleOperation {
  @Getter private final MmuData mmuData;
  private final CallStack callStack;

  MmuOperation(MmuData microData, CallStack callStack) {
    this.mmuData = microData;
    this.callStack = callStack;
  }

  @Override
  protected int computeLineCount() {
    return 1 + mmuData.numberMmuPreprocessingRows() + mmuData.numberMmioInstructions();
  }

  void trace(final int mmuStamp, final int mmioStamp, Trace trace) {
    //    if (mmuData.skip()) {
    //      return;
    //    }

    final Bytes mmuStampBytes = Bytes.ofUnsignedShort(mmuStamp);
    final Bytes mmioStampInit = Bytes.ofUnsignedShort(mmioStamp);

    // Trace Macro Instruction decoding Row
    traceMacroRow(mmuStampBytes, mmioStampInit, trace);

    // Trace Preprocessing rows
    tracePreprocessingRows(this.mmuData, mmuStampBytes, mmioStampInit, trace);

    // Trace Micro Instructions Rows
    traceMicroRows(mmuStampBytes, mmioStamp, trace);
  }

  private void traceFillMmuInstructionFlag(Trace trace) {
    int mmuInstruction = mmuData.hubToMmuValues().mmuInstruction();
    trace
        .isMload(mmuInstruction == Trace.MMU_INST_MLOAD)
        .isMstore(mmuInstruction == Trace.MMU_INST_MSTORE)
        .isMstore8(mmuInstruction == Trace.MMU_INST_MSTORE8)
        .isInvalidCodePrefix(mmuInstruction == Trace.MMU_INST_INVALID_CODE_PREFIX)
        .isRightPaddedWordExtraction(mmuInstruction == Trace.MMU_INST_RIGHT_PADDED_WORD_EXTRACTION)
        .isRamToExoWithPadding(mmuInstruction == Trace.MMU_INST_RAM_TO_EXO_WITH_PADDING)
        .isExoToRamTransplants(mmuInstruction == Trace.MMU_INST_EXO_TO_RAM_TRANSPLANTS)
        .isRamToRamSansPadding(mmuInstruction == Trace.MMU_INST_RAM_TO_RAM_SANS_PADDING)
        .isAnyToRamWithPaddingSomeData(
            mmuInstruction == Trace.MMU_INST_ANY_TO_RAM_WITH_PADDING_SOME_DATA)
        .isAnyToRamWithPaddingPurePadding(
            mmuInstruction == Trace.MMU_INST_ANY_TO_RAM_WITH_PADDING_PURE_PADDING)
        .isModexpZero(mmuInstruction == Trace.MMU_INST_MODEXP_ZERO)
        .isModexpData(mmuInstruction == Trace.MMU_INST_MODEXP_DATA)
        .isBlake(mmuInstruction == Trace.MMU_INST_BLAKE);
  }

  private void traceOutAndBin(Trace trace) {
    MmuOutAndBinValues mmuOutAndBinRecord = mmuData.outAndBinValues();

    trace
        .out1(Bytes.ofUnsignedInt(mmuOutAndBinRecord.out1()))
        .out2(Bytes.ofUnsignedInt(mmuOutAndBinRecord.out2()))
        .out3(Bytes.ofUnsignedInt(mmuOutAndBinRecord.out3()))
        .out4(Bytes.ofUnsignedInt(mmuOutAndBinRecord.out4()))
        .out5(Bytes.ofUnsignedInt(mmuOutAndBinRecord.out5()))
        .bin1(mmuOutAndBinRecord.bin1())
        .bin2(mmuOutAndBinRecord.bin2())
        .bin3(mmuOutAndBinRecord.bin3())
        .bin4(mmuOutAndBinRecord.bin4())
        .bin5(mmuOutAndBinRecord.bin5());
  }

  private void traceMacroRow(final Bytes mmuStamp, final Bytes mmioStamp, Trace trace) {
    traceFillMmuInstructionFlag(trace);
    traceOutAndBin(trace);

    HubToMmuValues mmuHubInput = mmuData.hubToMmuValues();

    trace
        .stamp(mmuStamp)
        .mmioStamp(mmioStamp)
        .macro(true)
        .prprc(false)
        .micro(false)
        .tot(Bytes.ofUnsignedLong(mmuData.numberMmioInstructions()))
        .totlz(Bytes.ofUnsignedLong(mmuData.totalLeftZeroesInitials()))
        .totnt(Bytes.ofUnsignedLong(mmuData.totalNonTrivialInitials()))
        .totrz(Bytes.ofUnsignedLong(mmuData.totalRightZeroesInitials()))
        .pMacroInst(Bytes.ofUnsignedShort(mmuHubInput.mmuInstruction()))
        .pMacroSrcId(Bytes.ofUnsignedInt(mmuHubInput.sourceId()))
        .pMacroTgtId(Bytes.ofUnsignedInt(mmuHubInput.targetId()))
        .pMacroAuxId(Bytes.ofUnsignedInt(mmuHubInput.auxId()))
        .pMacroSrcOffsetHi(bigIntegerToBytes(mmuHubInput.sourceOffsetHi()))
        .pMacroSrcOffsetLo(bigIntegerToBytes(mmuHubInput.sourceOffsetLo()))
        .pMacroTgtOffsetLo(Bytes.ofUnsignedLong(mmuHubInput.targetOffset()))
        .pMacroSize(Bytes.ofUnsignedLong(mmuHubInput.size()))
        .pMacroRefOffset(Bytes.ofUnsignedLong(mmuHubInput.referenceOffset()))
        .pMacroRefSize(Bytes.ofUnsignedLong(mmuHubInput.referenceSize()))
        .pMacroSuccessBit(mmuHubInput.successBit())
        .pMacroLimb1(mmuHubInput.limb1())
        .pMacroLimb2(mmuHubInput.limb2())
        .pMacroPhase(Bytes.ofUnsignedInt(mmuHubInput.phase()))
        .pMacroExoSum(Bytes.ofUnsignedInt(mmuHubInput.exoSum()))
        .fillAndValidateRow();
  }

  private void tracePreprocessingRows(
      final MmuData mmuData, final Bytes mmuStamp, final Bytes mmioStamp, Trace trace) {
    final Bytes totalLeftZeroes = Bytes.ofUnsignedLong(mmuData.totalLeftZeroesInitials());
    final Bytes totalNonTrivial = Bytes.ofUnsignedLong(mmuData.totalNonTrivialInitials());
    final Bytes totalRightZeroes = Bytes.ofUnsignedLong(mmuData.totalRightZeroesInitials());
    final Bytes total = Bytes.ofUnsignedLong(mmuData.numberMmioInstructions());

    for (int i = 1; i <= mmuData().numberMmuPreprocessingRows(); i++) {
      traceFillMmuInstructionFlag(trace);
      traceOutAndBin(trace);
      MmuEucCallRecord currentMmuEucCallRecord = mmuData.eucCallRecords().get(i);
      MmuWcpCallRecord currentMmuWcpCallRecord = mmuData.wcpCallRecords().get(i);
      trace
          .stamp(mmuStamp)
          .mmioStamp(mmioStamp)
          .macro(false)
          .prprc(true)
          .micro(false)
          .tot(total)
          .totlz(totalLeftZeroes)
          .totnt(totalNonTrivial)
          .totrz(totalRightZeroes)
          .pPrprcCt(Bytes.of(mmuData.numberMmuPreprocessingRows() - i))
          .pPrprcEucFlag(currentMmuEucCallRecord.flag())
          .pPrprcEucA(Bytes.ofUnsignedLong(currentMmuEucCallRecord.dividend()))
          .pPrprcEucB(Bytes.ofUnsignedLong(currentMmuEucCallRecord.divisor()))
          .pPrprcEucQuot(Bytes.ofUnsignedLong(currentMmuEucCallRecord.quotient()))
          .pPrprcEucRem(Bytes.ofUnsignedLong(currentMmuEucCallRecord.remainder()))
          .pPrprcEucCeil(
              Bytes.ofUnsignedLong(
                  currentMmuEucCallRecord.remainder() == 0 && currentMmuWcpCallRecord.flag()
                      ? currentMmuEucCallRecord.quotient() + 1
                      : currentMmuEucCallRecord.quotient()))
          .pPrprcWcpFlag(currentMmuWcpCallRecord.flag())
          .pPrprcWcpArg1Hi(currentMmuWcpCallRecord.arg1Hi())
          .pPrprcWcpArg1Lo(currentMmuWcpCallRecord.arg1Lo())
          .pPrprcWcpArg2Lo(currentMmuWcpCallRecord.arg2Lo())
          .pPrprcWcpRes(currentMmuWcpCallRecord.result())
          .pPrprcWcpInst(currentMmuWcpCallRecord.instruction())
          .fillAndValidateRow();
    }
  }

  private List<RowTypeRecord> generateRowTypeList(
      final int totLeftZeroInit, final int totNonTrivialInit, final int totRightZeroInit) {
    final int totInit = mmuData().numberMmioInstructions();
    List<RowTypeRecord> output = new ArrayList<>(totInit);

    int totLeftZero = totLeftZeroInit;
    int totNonTrivial = totNonTrivialInit;
    int totRightZero = totRightZeroInit;

    for (int i = 0; i < totInit; i++) {
      if (totLeftZero != 0) {
        totLeftZero -= 1;
      } else if (totNonTrivial != 0) {
        totNonTrivial -= 1;
      } else {
        totRightZero -= 1;
      }
      final boolean isLeftZeroRow = i < totLeftZeroInit;
      final boolean isNonTrivialRow = !isLeftZeroRow && i < totLeftZeroInit + totNonTrivialInit;
      final boolean isRightZeroRow = !isLeftZeroRow && !isNonTrivialRow;

      final boolean isOnlyNonTrivial = isNonTrivialRow && totNonTrivialInit == 1;
      final boolean isFirstNonTrivial =
          isNonTrivialRow && !isOnlyNonTrivial && i == totLeftZeroInit;
      final boolean isLastNonTrivial = isNonTrivialRow && !isOnlyNonTrivial && totNonTrivial == 0;
      final boolean isMiddleNonTrivial =
          isNonTrivialRow && !isFirstNonTrivial && !isLastNonTrivial && !isOnlyNonTrivial;

      final boolean isOnlyRightZero = isRightZeroRow && totRightZeroInit == 1;
      final boolean isFirstRightZero =
          isRightZeroRow && !isOnlyRightZero && i == totLeftZeroInit + totNonTrivialInit;
      final boolean isLastRightZero = isRightZeroRow && !isOnlyRightZero && totRightZero == 0;
      final boolean isMiddleRightZero =
          isRightZeroRow && !isFirstRightZero && !isLastRightZero && !isOnlyRightZero;

      output.add(
          new RowTypeRecord(
              totInit - (i + 1),
              totLeftZero,
              totNonTrivial,
              totRightZero,
              isLeftZeroRow,
              isOnlyNonTrivial,
              isFirstNonTrivial,
              isMiddleNonTrivial,
              isLastNonTrivial,
              isOnlyRightZero,
              isFirstRightZero,
              isMiddleRightZero,
              isLastRightZero));
    }

    return output;
  }

  private void traceMicroRows(final Bytes mmuStamp, int mmioStamp, Trace trace) {
    final List<RowTypeRecord> rowType =
        generateRowTypeList(
            this.mmuData.totalLeftZeroesInitials(),
            this.mmuData.totalNonTrivialInitials(),
            this.mmuData.totalRightZeroesInitials());
    final HubToMmuValues mmuHubInput = mmuData.hubToMmuValues();

    final Bytes exoSum = Bytes.ofUnsignedInt(mmuHubInput.exoSum());
    final Bytes phase = Bytes.ofUnsignedInt(mmuHubInput.phase());
    final boolean successBit = mmuHubInput.successBit();

    final MmuToMmioConstantValues mmioConstantValues = mmuData.mmuToMmioConstantValues();
    final Bytes sourceContextNumber = Bytes.ofUnsignedInt(mmioConstantValues.sourceContextNumber());
    final Bytes targetContextNumber = Bytes.ofUnsignedInt(mmioConstantValues.targetContextNumber());
    final Bytes microId1 = Bytes.ofUnsignedInt(mmioConstantValues.microId1());
    final Bytes microId2 = Bytes.ofUnsignedInt(mmioConstantValues.microId2());
    final Bytes microTotalSize = Bytes.ofUnsignedInt(mmioConstantValues.totalSize());

    for (int i = 0; i < mmuData().numberMmioInstructions(); i++) {
      mmioStamp += 1;
      traceFillMmuInstructionFlag(trace);
      traceOutAndBin(trace);

      final RowTypeRecord currentRowTypeRecord = rowType.get(i);
      final MmuToMmioInstruction currentMmuToMmioInstruction =
          mmuData.mmuToMmioInstructions().get(i);

      trace
          .stamp(mmuStamp)
          .mmioStamp(Bytes.ofUnsignedShort(mmioStamp))
          .macro(false)
          .prprc(false)
          .micro(true)
          .tot(Bytes.ofUnsignedShort(currentRowTypeRecord.total()))
          .totlz(Bytes.ofUnsignedShort(currentRowTypeRecord.totalLeftZeroes()))
          .totnt(Bytes.ofUnsignedShort(currentRowTypeRecord.totalNonTrivial()))
          .totrz(Bytes.ofUnsignedShort(currentRowTypeRecord.totalRightZeroes()))
          .lzro(currentRowTypeRecord.leftZeroRow())
          .ntOnly(currentRowTypeRecord.onlyNonTrivialRow())
          .ntFirst(currentRowTypeRecord.firstNonTrivialRow())
          .ntMddl(currentRowTypeRecord.middleNonTrivialRow())
          .ntLast(currentRowTypeRecord.lastNonTrivialRow())
          .rzOnly(currentRowTypeRecord.onlyRightZeroRow())
          .rzFirst(currentRowTypeRecord.firstRightZeroRow())
          .rzMddl(currentRowTypeRecord.middleRightZeroRow())
          .rzLast(currentRowTypeRecord.lastRightZeroRow())
          .pMicroInst(Bytes.ofUnsignedInt(currentMmuToMmioInstruction.mmioInstruction()))
          .pMicroSize(UnsignedByte.of(currentMmuToMmioInstruction.size()))
          .pMicroSlo(Bytes.ofUnsignedInt(currentMmuToMmioInstruction.sourceLimbOffset()))
          .pMicroSbo(UnsignedByte.of(currentMmuToMmioInstruction.sourceByteOffset()))
          .pMicroTlo(Bytes.ofUnsignedInt(currentMmuToMmioInstruction.targetLimbOffset()))
          .pMicroTbo(UnsignedByte.of(currentMmuToMmioInstruction.targetByteOffset()))
          .pMicroLimb(currentMmuToMmioInstruction.limb())
          .pMicroCnS(sourceContextNumber)
          .pMicroCnT(targetContextNumber)
          .pMicroSuccessBit(successBit)
          .pMicroExoSum(exoSum)
          .pMicroPhase(phase)
          .pMicroId1(microId1)
          .pMicroId2(microId2)
          .pMicroTotalSize(microTotalSize)
          .fillAndValidateRow();
    }
  }
}
