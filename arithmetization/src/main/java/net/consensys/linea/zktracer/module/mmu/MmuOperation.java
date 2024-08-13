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

import static net.consensys.linea.zktracer.module.constants.GlobalConstants.LLARGE;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.MMU_INST_ANY_TO_RAM_WITH_PADDING;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.MMU_INST_BLAKE;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.MMU_INST_EXO_TO_RAM_TRANSPLANTS;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.MMU_INST_INVALID_CODE_PREFIX;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.MMU_INST_MLOAD;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.MMU_INST_MODEXP_DATA;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.MMU_INST_MODEXP_ZERO;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.MMU_INST_MSTORE;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.MMU_INST_MSTORE8;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.MMU_INST_RAM_TO_EXO_WITH_PADDING;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.MMU_INST_RAM_TO_RAM_SANS_PADDING;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.MMU_INST_RIGHT_PADDED_WORD_EXTRACTION;
import static net.consensys.linea.zktracer.module.mmio.MmioData.numberOfRowOfMmioInstruction;
import static net.consensys.linea.zktracer.types.Bytecodes.readBytes;
import static net.consensys.linea.zktracer.types.Conversions.*;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.module.mmio.CallStackReader;
import net.consensys.linea.zktracer.module.mmu.values.HubToMmuValues;
import net.consensys.linea.zktracer.module.mmu.values.MmuEucCallRecord;
import net.consensys.linea.zktracer.module.mmu.values.MmuOutAndBinValues;
import net.consensys.linea.zktracer.module.mmu.values.MmuToMmioConstantValues;
import net.consensys.linea.zktracer.module.mmu.values.MmuToMmioInstruction;
import net.consensys.linea.zktracer.module.mmu.values.MmuWcpCallRecord;
import net.consensys.linea.zktracer.module.mmu.values.RowTypeRecord;
import net.consensys.linea.zktracer.runtime.callstack.CallStack;
import net.consensys.linea.zktracer.types.Bytes16;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;

@Accessors(fluent = true)
public class MmuOperation extends ModuleOperation {
  @Getter private final MmuData mmuData;

  private boolean isMload;
  private boolean isMstore;
  private boolean isMstore8;
  private boolean isInvalidCodePrefix;
  private boolean isRightPaddedWordExtraction;
  private boolean isRamToExoWithPadding;
  private boolean isExoToRamTransplants;
  private boolean isRamToRamSansPadding;
  private boolean isAnyToRamWithPaddingSomeData;
  private boolean isAnyToRamWithPaddingPurePadding;
  private boolean isModexpZero;
  private boolean isModexpData;
  private boolean isBlake;
  private final CallStackReader callStackReader;

  MmuOperation(MmuData mmuData, final CallStack callStack) {
    this.mmuData = mmuData;
    this.callStackReader = new CallStackReader(callStack);
  }

  @Override
  protected int computeLineCount() {
    return 1 + mmuData.numberMmuPreprocessingRows() + mmuData.numberMmioInstructions();
  }

  public int computeMmioLineCount() {
    int sum = 0;
    for (int i = 0; i < mmuData().numberMmioInstructions(); i++) {
      sum += numberOfRowOfMmioInstruction(mmuData.mmuToMmioInstructions().get(i).mmioInstruction());
    }
    return sum;
  }

  void trace(final int mmuStamp, final int mmioStamp, Trace trace) {

    setInstructionFlag();

    // Trace Macro Instruction decoding Row
    traceMacroRow(mmuStamp, mmioStamp, trace);

    // Trace Preprocessing rows
    tracePreprocessingRows(this.mmuData, mmuStamp, mmioStamp, trace);

    // Trace Micro Instructions Rows
    traceMicroRows(mmuStamp, mmioStamp, trace);
  }

  private void setInstructionFlag() {
    final int mmuInstruction = mmuData.hubToMmuValues().mmuInstruction();
    isMload = mmuInstruction == MMU_INST_MLOAD;
    isMstore = mmuInstruction == MMU_INST_MSTORE;
    isMstore8 = mmuInstruction == MMU_INST_MSTORE8;
    isInvalidCodePrefix = mmuInstruction == MMU_INST_INVALID_CODE_PREFIX;
    isRightPaddedWordExtraction = mmuInstruction == MMU_INST_RIGHT_PADDED_WORD_EXTRACTION;
    isRamToExoWithPadding = mmuInstruction == MMU_INST_RAM_TO_EXO_WITH_PADDING;
    isExoToRamTransplants = mmuInstruction == MMU_INST_EXO_TO_RAM_TRANSPLANTS;
    isRamToRamSansPadding = mmuInstruction == MMU_INST_RAM_TO_RAM_SANS_PADDING;
    isAnyToRamWithPaddingSomeData =
        mmuInstruction == MMU_INST_ANY_TO_RAM_WITH_PADDING
            && !mmuData.mmuInstAnyToRamWithPaddingIsPurePadding();
    isAnyToRamWithPaddingPurePadding =
        mmuInstruction == MMU_INST_ANY_TO_RAM_WITH_PADDING
            && mmuData.mmuInstAnyToRamWithPaddingIsPurePadding();
    isModexpZero = mmuInstruction == MMU_INST_MODEXP_ZERO;
    isModexpData = mmuInstruction == MMU_INST_MODEXP_DATA;
    isBlake = mmuInstruction == MMU_INST_BLAKE;
  }

  public void getCFI() {
    if (mmuData.hubToMmuValues().exoIsRom()) {
      if (mmuData.exoLimbIsSource()) {
        final int sourceId = mmuData.mmuCall().sourceId();
        mmuData.hubToMmuValues().sourceId(sourceId);
        mmuData.mmuToMmioConstantValues().exoId(sourceId);
      } else {
        final int targetId = mmuData.mmuCall().targetId();
        mmuData.hubToMmuValues().targetId(targetId);
        mmuData.mmuToMmioConstantValues().exoId(targetId);
      }
    }
  }

  public void setExoBytes(ExoSumDecoder exoSumDecoder) {
    if (mmuData.mmuCall().exoBytes().isPresent()) {
      mmuData.exoBytes(mmuData.mmuCall().exoBytes().get());
      return;
    }
    final int exoSum = mmuData.hubToMmuValues().exoSum();

    if (exoSum != 0) {
      mmuData.exoSumDecoder(exoSumDecoder);
      final int exoId =
          (int)
              (mmuData.exoLimbIsSource()
                  ? this.mmuData.hubToMmuValues().sourceId()
                  : this.mmuData.hubToMmuValues().targetId());
      mmuData.exoBytes(exoSumDecoder.getExoBytes(mmuData.hubToMmuValues(), exoId));
    }
  }

  public void fillLimb() {
    final int mmuInstruction = mmuData.hubToMmuValues().mmuInstruction();
    // Job already done in the preprocessing only for INVALID_CODE_PREFIX
    if (mmuInstruction == MMU_INST_INVALID_CODE_PREFIX) {
      return;
    }

    // the limb for BLAKE is given by the HUB
    if (mmuInstruction == MMU_INST_BLAKE) {
      return;
    }

    if (!mmuData.exoBytes().isEmpty()) {
      final boolean exoIsSource = mmuData.exoLimbIsSource();
      final boolean exoIsTarget = mmuData.exoLimbIsTarget();
      Preconditions.checkArgument(
          exoIsSource == !exoIsTarget, "ExoLimb is either the source or the target");

      for (MmuToMmioInstruction mmioInst : this.mmuData.mmuToMmioInstructions()) {
        final int offset =
            (int)
                (exoIsSource
                    ? mmioInst.sourceLimbOffset() * LLARGE + mmioInst.sourceByteOffset()
                    : mmioInst.targetLimbOffset() * LLARGE + mmioInst.targetByteOffset());
        final int sizeToExtract = mmioInst.size() == 0 ? LLARGE : mmioInst.size();
        final Bytes16 exoLimb = readBytes(mmuData.exoBytes(), offset, sizeToExtract);
        mmioInst.limb(exoLimb);
      }
    }
  }

  private boolean exoLimbIsSource() {
    return List.of(MMU_INST_ANY_TO_RAM_WITH_PADDING, MMU_INST_EXO_TO_RAM_TRANSPLANTS)
        .contains(this.mmuData.hubToMmuValues().mmuInstruction());
  }

  private void traceFillMmuInstructionFlag(Trace trace) {
    trace
        .isMload(isMload)
        .isMstore(isMstore)
        .isMstore8(isMstore8)
        .isInvalidCodePrefix(isInvalidCodePrefix)
        .isRightPaddedWordExtraction(isRightPaddedWordExtraction)
        .isRamToExoWithPadding(isRamToExoWithPadding)
        .isExoToRamTransplants(isExoToRamTransplants)
        .isRamToRamSansPadding(isRamToRamSansPadding)
        .isAnyToRamWithPaddingSomeData(isAnyToRamWithPaddingSomeData)
        .isAnyToRamWithPaddingPurePadding(isAnyToRamWithPaddingPurePadding)
        .isModexpZero(isModexpZero)
        .isModexpData(isModexpData)
        .isBlake(isBlake);
  }

  private void traceOutAndBin(Trace trace) {
    MmuOutAndBinValues mmuOutAndBinRecord = mmuData.outAndBinValues();

    trace
        .out1(Bytes.minimalBytes(mmuOutAndBinRecord.out1()))
        .out2(Bytes.minimalBytes(mmuOutAndBinRecord.out2()))
        .out3(Bytes.minimalBytes(mmuOutAndBinRecord.out3()))
        .out4(Bytes.minimalBytes(mmuOutAndBinRecord.out4()))
        .out5(Bytes.minimalBytes(mmuOutAndBinRecord.out5()))
        .bin1(mmuOutAndBinRecord.bin1())
        .bin2(mmuOutAndBinRecord.bin2())
        .bin3(mmuOutAndBinRecord.bin3())
        .bin4(mmuOutAndBinRecord.bin4())
        .bin5(mmuOutAndBinRecord.bin5());
  }

  private void traceMacroRow(final long mmuStamp, final long mmioStamp, Trace trace) {
    traceFillMmuInstructionFlag(trace);
    traceOutAndBin(trace);

    HubToMmuValues mmuHubInput = mmuData.hubToMmuValues();

    trace
        .stamp(mmuStamp)
        .mmioStamp(mmioStamp)
        .macro(true)
        .prprc(false)
        .micro(false)
        .tot(mmuData.numberMmioInstructions())
        .totlz(mmuData.totalLeftZeroesInitials())
        .totnt(mmuData.totalNonTrivialInitials())
        .totrz(mmuData.totalRightZeroesInitials())
        .pMacroInst(mmuHubInput.mmuInstruction())
        .pMacroSrcId(Bytes.minimalBytes(mmuHubInput.sourceId()))
        .pMacroTgtId(Bytes.minimalBytes(mmuHubInput.targetId()))
        .pMacroAuxId(Bytes.minimalBytes(mmuHubInput.auxId()))
        .pMacroSrcOffsetHi(bigIntegerToBytes(mmuHubInput.sourceOffsetHi()))
        .pMacroSrcOffsetLo(bigIntegerToBytes(mmuHubInput.sourceOffsetLo()))
        .pMacroTgtOffsetLo(Bytes.ofUnsignedLong(mmuHubInput.targetOffset()))
        .pMacroSize(Bytes.ofUnsignedLong(mmuHubInput.size()))
        .pMacroRefOffset(Bytes.ofUnsignedLong(mmuHubInput.referenceOffset()))
        .pMacroRefSize(Bytes.ofUnsignedLong(mmuHubInput.referenceSize()))
        .pMacroSuccessBit(mmuHubInput.successBit())
        .pMacroLimb1(mmuHubInput.limb1())
        .pMacroLimb2(mmuHubInput.limb2())
        .pMacroPhase(mmuHubInput.phase())
        .pMacroExoSum(mmuHubInput.exoSum())
        .fillAndValidateRow();
  }

  private void tracePreprocessingRows(
      final MmuData mmuData, final long mmuStamp, final long mmioStamp, Trace trace) {

    for (int i = 1; i <= mmuData().numberMmuPreprocessingRows(); i++) {
      traceFillMmuInstructionFlag(trace);
      traceOutAndBin(trace);
      MmuEucCallRecord currentMmuEucCallRecord = mmuData.eucCallRecords().get(i - 1);
      MmuWcpCallRecord currentMmuWcpCallRecord = mmuData.wcpCallRecords().get(i - 1);
      trace
          .stamp(mmuStamp)
          .mmioStamp(mmioStamp)
          .macro(false)
          .prprc(true)
          .micro(false)
          .tot(mmuData.numberMmioInstructions())
          .totlz(mmuData.totalLeftZeroesInitials())
          .totnt(mmuData.totalNonTrivialInitials())
          .totrz(mmuData.totalRightZeroesInitials())
          .pPrprcCt(mmuData.numberMmuPreprocessingRows() - i)
          .pPrprcEucFlag(currentMmuEucCallRecord.flag())
          .pPrprcEucA(Bytes.ofUnsignedLong(currentMmuEucCallRecord.dividend()))
          .pPrprcEucB(Bytes.ofUnsignedLong(currentMmuEucCallRecord.divisor()))
          .pPrprcEucQuot(Bytes.ofUnsignedLong(currentMmuEucCallRecord.quotient()))
          .pPrprcEucRem(Bytes.ofUnsignedLong(currentMmuEucCallRecord.remainder()))
          .pPrprcEucCeil(Bytes.ofUnsignedLong(currentMmuEucCallRecord.ceiling()))
          .pPrprcWcpFlag(currentMmuWcpCallRecord.flag())
          .pPrprcWcpArg1Hi(currentMmuWcpCallRecord.arg1Hi())
          .pPrprcWcpArg1Lo(currentMmuWcpCallRecord.arg1Lo())
          .pPrprcWcpArg2Lo(currentMmuWcpCallRecord.arg2Lo())
          .pPrprcWcpRes(currentMmuWcpCallRecord.result())
          .pPrprcWcpInst(currentMmuWcpCallRecord.instruction())
          .fillAndValidateRow();
    }
  }

  private List<RowTypeRecord> generateRowTypeList() {
    final int totInit = mmuData().numberMmioInstructions();
    List<RowTypeRecord> output = new ArrayList<>(totInit);

    final int totLeftZeroInit = mmuData.totalLeftZeroesInitials();
    final int totNonTrivialInit = mmuData.totalNonTrivialInitials();
    final int totRightZeroInit = mmuData.totalRightZeroesInitials();

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

  private void traceMicroRows(final long mmuStamp, int mmioStamp, Trace trace) {
    final List<RowTypeRecord> rowType = generateRowTypeList();
    final HubToMmuValues mmuHubInput = mmuData.hubToMmuValues();

    final boolean successBit = mmuHubInput.successBit();

    final MmuToMmioConstantValues mmioConstantValues = mmuData.mmuToMmioConstantValues();

    for (int i = 0; i < mmuData().numberMmioInstructions(); i++) {
      mmioStamp += 1;
      traceFillMmuInstructionFlag(trace);
      traceOutAndBin(trace);

      final RowTypeRecord currentRowTypeRecord = rowType.get(i);
      final MmuToMmioInstruction currentMmuToMmioInstruction =
          mmuData.mmuToMmioInstructions().get(i);

      trace
          .stamp(mmuStamp)
          .mmioStamp(mmioStamp)
          .macro(false)
          .prprc(false)
          .micro(true)
          .tot(currentRowTypeRecord.total())
          .totlz(currentRowTypeRecord.totalLeftZeroes())
          .totnt(currentRowTypeRecord.totalNonTrivial())
          .totrz(currentRowTypeRecord.totalRightZeroes())
          .lzro(currentRowTypeRecord.leftZeroRow())
          .ntOnly(currentRowTypeRecord.onlyNonTrivialRow())
          .ntFirst(currentRowTypeRecord.firstNonTrivialRow())
          .ntMddl(currentRowTypeRecord.middleNonTrivialRow())
          .ntLast(currentRowTypeRecord.lastNonTrivialRow())
          .rzOnly(currentRowTypeRecord.onlyRightZeroRow())
          .rzFirst(currentRowTypeRecord.firstRightZeroRow())
          .rzMddl(currentRowTypeRecord.middleRightZeroRow())
          .rzLast(currentRowTypeRecord.lastRightZeroRow())
          .pMicroInst(currentMmuToMmioInstruction.mmioInstruction())
          .pMicroSize(UnsignedByte.of(currentMmuToMmioInstruction.size()))
          .pMicroSlo(Bytes.minimalBytes(currentMmuToMmioInstruction.sourceLimbOffset()))
          .pMicroSbo(UnsignedByte.of(currentMmuToMmioInstruction.sourceByteOffset()))
          .pMicroTlo(Bytes.minimalBytes(currentMmuToMmioInstruction.targetLimbOffset()))
          .pMicroTbo(UnsignedByte.of(currentMmuToMmioInstruction.targetByteOffset()))
          .pMicroLimb(currentMmuToMmioInstruction.limb())
          .pMicroCnS(Bytes.minimalBytes(mmioConstantValues.sourceContextNumber()))
          .pMicroCnT(Bytes.minimalBytes(mmioConstantValues.targetContextNumber()))
          .pMicroSuccessBit(successBit)
          .pMicroExoSum(mmioConstantValues.exoSum())
          .pMicroPhase(mmioConstantValues.phase())
          .pMicroExoId(mmioConstantValues.exoId())
          .pMicroKecId(mmioConstantValues.kecId())
          .pMicroTotalSize(Bytes.minimalBytes(mmioConstantValues.totalSize()))
          .fillAndValidateRow();
    }
  }
}
