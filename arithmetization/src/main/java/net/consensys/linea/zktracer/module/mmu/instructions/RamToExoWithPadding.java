/*
 * Copyright ConsenSys Inc.
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

package net.consensys.linea.zktracer.module.mmu.instructions;

import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;
import static net.consensys.linea.zktracer.types.Conversions.longToBytes;

import java.util.ArrayList;
import java.util.List;

import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.euc.EucOperation;
import net.consensys.linea.zktracer.module.mmu.MmuData;
import net.consensys.linea.zktracer.module.mmu.Trace;
import net.consensys.linea.zktracer.module.mmu.values.HubToMmuValues;
import net.consensys.linea.zktracer.module.mmu.values.MmuEucCallRecord;
import net.consensys.linea.zktracer.module.mmu.values.MmuOutAndBinValues;
import net.consensys.linea.zktracer.module.mmu.values.MmuToMmioConstantValues;
import net.consensys.linea.zktracer.module.mmu.values.MmuToMmioInstruction;
import net.consensys.linea.zktracer.module.mmu.values.MmuWcpCallRecord;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;

public class RamToExoWithPadding implements MmuInstruction {
  private final Euc euc;
  private final Wcp wcp;
  private List<MmuEucCallRecord> eucCallRecords;
  private List<MmuWcpCallRecord> wcpCallRecords;

  private boolean aligned;
  private int lastLimbByteSize;
  private boolean lastLimbSingleSource;
  private boolean lastLimbIsFull;

  private Bytes initialSourceLimbOffset;
  private Bytes initialSourceByteOffset;
  private boolean hasRightPadding;
  private int paddingSize;
  private int extractionSize;

  public RamToExoWithPadding(Euc euc, Wcp wcp) {
    this.euc = euc;
    this.wcp = wcp;
    this.eucCallRecords = new ArrayList<>(Trace.NB_PP_ROWS_RAM_TO_EXO_WITH_PADDING);
    this.wcpCallRecords = new ArrayList<>(Trace.NB_PP_ROWS_RAM_TO_EXO_WITH_PADDING);
  }

  @Override
  public MmuData preProcess(MmuData mmuData) {
    MmuOutAndBinValues mmuOutAndBinValues = mmuData.outAndBinValues();
    aligned = mmuOutAndBinValues.bin1();
    lastLimbByteSize = mmuOutAndBinValues.out1();
    lastLimbSingleSource = mmuOutAndBinValues.bin2();
    lastLimbIsFull = mmuOutAndBinValues.bin3();

    final HubToMmuValues hubToMmuValues = mmuData.hubToMmuValues();
    row1(hubToMmuValues);
    row2(mmuData);
    row3(mmuData);
    row4(hubToMmuValues);

    mmuData.eucCallRecords(eucCallRecords);
    mmuData.wcpCallRecords(wcpCallRecords);

    mmuData.outAndBinValues(
        MmuOutAndBinValues.builder()
            .bin1(aligned)
            .bin2(lastLimbSingleSource)
            .bin3(lastLimbIsFull)
            .out1(lastLimbByteSize)
            .build());

    mmuData.totalLeftZeroesInitials(0);

    return mmuData;
  }

  private void row1(final HubToMmuValues hubToMmuValues) {
    // row n°1
    final Bytes dividend = Bytes.of(16);
    final Bytes divisor = bigIntegerToBytes(hubToMmuValues.sourceOffsetLo());
    final EucOperation eucOp = euc.callEUC(dividend, divisor);
    eucCallRecords.add(
        MmuEucCallRecord.builder()
            .dividend(dividend.toLong())
            .divisor(divisor.toLong())
            .quotient(eucOp.quotient().toLong())
            .remainder(eucOp.remainder().toLong())
            .build());

    initialSourceLimbOffset = eucOp.quotient();
    initialSourceByteOffset = eucOp.remainder();

    final boolean isZeroResult = wcp.callISZERO(initialSourceByteOffset);
    aligned = isZeroResult;

    wcpCallRecords.add(
        MmuWcpCallRecord.builder().arg1Lo(initialSourceByteOffset).result(isZeroResult).build());
  }

  private void row2(final MmuData mmuData) {
    // row n°2
    final long size = mmuData.hubToMmuValues().size();
    final Bytes wcpArg1 = longToBytes(size);
    final long refSize = mmuData.hubToMmuValues().referenceSize();
    final Bytes wcpArg2 = longToBytes(refSize);
    final boolean wcpResult = wcp.callLT(wcpArg1, wcpArg2);

    wcpCallRecords.add(
        MmuWcpCallRecord.builder().arg1Lo(wcpArg1).arg2Lo(wcpArg2).result(wcpResult).build());

    hasRightPadding = wcpResult;
    paddingSize = hasRightPadding ? (int) (refSize - size) : 0;
    extractionSize = (int) (hasRightPadding ? size : refSize);

    final Bytes dividend = Bytes.of(16);
    final Bytes divisor = Bytes.ofUnsignedShort(paddingSize);
    final EucOperation eucOp = euc.callEUC(dividend, divisor);

    eucCallRecords.add(
        MmuEucCallRecord.builder()
            .dividend(dividend.toLong())
            .divisor(divisor.toLong())
            .quotient(eucOp.quotient().toLong())
            .remainder(eucOp.remainder().toLong())
            .build());

    mmuData.totalRightZeroesInitials(eucOp.quotient().toInt());
  }

  private void row3(final MmuData mmuData) {
    // row n°3
    final Bytes dividend = Bytes.of(16);
    final Bytes divisor = Bytes.ofUnsignedShort(extractionSize);
    final EucOperation eucOp = euc.callEUC(dividend, divisor);

    Bytes quotient = eucOp.quotient();
    eucCallRecords.add(
        MmuEucCallRecord.builder()
            .dividend(dividend.toLong())
            .divisor(divisor.toLong())
            .quotient(quotient.toLong())
            .remainder(eucOp.remainder().toLong())
            .build());

    final boolean isZeroResult = wcp.callISZERO(eucOp.remainder());

    wcpCallRecords.add(
        MmuWcpCallRecord.builder().arg1Lo(eucOp.remainder()).result(isZeroResult).build());

    final int eucCeil = eucOp.remainder().toInt() == 0 ? quotient.toInt() + 1 : quotient.toInt();

    mmuData.totalNonTrivialInitials(eucCeil);

    lastLimbIsFull = isZeroResult;
    lastLimbByteSize = lastLimbIsFull ? 16 : eucOp.remainder().toInt();
  }

  private void row4(final HubToMmuValues hubToMmuValues) {
    // row n°4
    eucCallRecords.add(MmuEucCallRecord.EMPTY_CALL);

    final Bytes wcpArg1 =
        Bytes.ofUnsignedShort(initialSourceByteOffset.toInt() + (lastLimbByteSize - 1));
    final Bytes wcpArg2 = Bytes.of(16);
    boolean wcpResult = wcp.callLT(wcpArg1, wcpArg2);

    wcpCallRecords.add(
        MmuWcpCallRecord.builder()
            .instruction(UnsignedByte.of(Trace.LT))
            .arg1Lo(wcpArg1)
            .arg2Lo(wcpArg2)
            .result(wcpResult)
            .build());

    lastLimbSingleSource = wcpResult;
  }

  @Override
  public MmuData setMicroInstructions(MmuData mmuData) {
    HubToMmuValues hubToMmuValues = mmuData.hubToMmuValues();

    mmuData.mmuToMmioConstantValues(
        MmuToMmioConstantValues.builder()
            .sourceContextNumber(hubToMmuValues.sourceId())
            .successBit(hubToMmuValues.successBit())
            .exoSum(hubToMmuValues.exoSum())
            .phase(hubToMmuValues.phase())
            .exoId(hubToMmuValues.targetId())
            .kecId(hubToMmuValues.auxId())
            .totalSize((int) hubToMmuValues.referenceSize())
            .build());

    if (mmuData.totalNonTrivialInitials() == 1) {
      // TODO: Determine relative value of limb
      final Bytes onlyMicroInstLimb = Bytes.EMPTY;
      onlyMicroInstruction(mmuData, onlyMicroInstLimb);
    } else {
      // TODO: Determine relative value of limb
      final Bytes firstMicroInstLimb = Bytes.EMPTY;
      firstMicroInstruction(mmuData, firstMicroInstLimb);
      final int middleMicroInst =
          aligned ? Trace.MMIO_INST_RAM_TO_LIMB_TRANSPLANT : Trace.MMIO_INST_RAM_TO_LIMB_TWO_SOURCE;
      for (int i = 1; i < mmuData.totalNonTrivialInitials() - 1; i++) {
        // TODO: Determine relative value of limb
        final Bytes middleMicroInstlimb = Bytes.EMPTY;
        middleMicroInstruction(mmuData, middleMicroInstlimb, i, middleMicroInst);
      }
      // TODO: Determine relative value of limb
      final Bytes lastMicroInstLimb = Bytes.EMPTY;
      lastMicroInstruction(mmuData, lastMicroInstLimb);
    }

    for (int i = 0; i < mmuData.totalRightZeroesInitials(); i++) {
      vanishingMicroInstruction(mmuData, i);
    }

    return mmuData;
  }

  private void vanishingMicroInstruction(MmuData mmuData, final int i) {
    mmuData.mmuToMmioInstruction(
        MmuToMmioInstruction.builder()
            .mmioInstruction(Trace.MMIO_INST_LIMB_VANISHES)
            .targetLimbOffset(mmuData.totalNonTrivialInitials() + i)
            .build());
  }

  private void lastMicroInstruction(MmuData mmuData, final Bytes limb) {
    final int lastMicroInst = calculateLastOrOnlyMicroInstruction();

    mmuData.mmuToMmioInstruction(
        MmuToMmioInstruction.builder()
            .mmioInstruction(lastMicroInst)
            .size(lastLimbByteSize)
            .sourceLimbOffset(mmuData.sourceLimbOffset().add(1).toInt())
            .sourceByteOffset(mmuData.sourceByteOffset().toInteger())
            .targetLimbOffset(mmuData.totalNonTrivialInitials() - 1)
            .limb(limb)
            .build());
  }

  private int calculateLastOrOnlyMicroInstruction() {
    if (lastLimbSingleSource) {
      return lastLimbIsFull
          ? Trace.MMIO_INST_RAM_TO_LIMB_TRANSPLANT
          : Trace.MMIO_INST_RAM_TO_LIMB_ONE_SOURCE;
    }

    return Trace.MMIO_INST_RAM_TO_LIMB_TWO_SOURCE;
  }

  private void onlyMicroInstruction(MmuData mmuData, final Bytes limb) {
    final int onlyMicroInst = calculateLastOrOnlyMicroInstruction();

    mmuData.mmuToMmioInstruction(
        MmuToMmioInstruction.builder()
            .mmioInstruction(onlyMicroInst)
            .size(lastLimbByteSize)
            .sourceLimbOffset(initialSourceLimbOffset.toInt())
            .sourceByteOffset(initialSourceByteOffset.toInt())
            .limb(limb)
            .build());
  }

  private void firstMicroInstruction(MmuData mmuData, final Bytes limb) {
    final int firstMicroInst =
        aligned ? Trace.MMIO_INST_RAM_TO_LIMB_TRANSPLANT : Trace.MMIO_INST_RAM_TO_LIMB_TWO_SOURCE;
    mmuData.mmuToMmioInstruction(
        MmuToMmioInstruction.builder()
            .mmioInstruction(firstMicroInst)
            .size(16)
            .sourceLimbOffset(initialSourceLimbOffset.toInt())
            .sourceByteOffset(initialSourceByteOffset.toInt())
            .limb(limb)
            .build());
  }

  private void middleMicroInstruction(
      MmuData mmuData, final Bytes limb, final int targetByteOffset, final int middleMicroInst) {
    mmuData.mmuToMmioInstruction(
        MmuToMmioInstruction.builder()
            .mmioInstruction(middleMicroInst)
            .size(16)
            .sourceLimbOffset(mmuData.sourceLimbOffset().add(1).toInt())
            .sourceByteOffset(mmuData.sourceByteOffset().toInteger())
            .targetLimbOffset(targetByteOffset)
            .limb(limb)
            .build());
  }
}
