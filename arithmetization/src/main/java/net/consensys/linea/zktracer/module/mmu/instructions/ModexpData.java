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
import org.apache.tuweni.bytes.Bytes;

public class ModexpData implements MmuInstruction {
  private final Euc euc;
  private final Wcp wcp;
  private List<MmuEucCallRecord> eucCallRecords;
  private List<MmuWcpCallRecord> wcpCallRecords;
  private int initialTotalLeftZeroes;
  private int initialTotalNonTrivial;
  private int initialTotalRightZeroes;
  private int initialTargetByteOffset;
  private int initialSourceLimbOffset;
  private int initialSourceByteOffset;
  private int firstLimbByteSize;
  private int lastLimbByteSize;
  private boolean firstLimbSingleSource;
  private boolean aligned;
  private boolean lastLimbSingleSource;
  private int parameterByteSize;
  private int parameterOffset;
  private int leftoverDataSize;
  private boolean dataRunsOut;
  private int rightPaddingRemainder;
  private int middleSourceByteOffset;
  private int middleFirstSourceLimbOffset;
  private int middleMicroInst;

  public ModexpData(Euc euc, Wcp wcp) {
    this.euc = euc;
    this.wcp = wcp;
    this.eucCallRecords = new ArrayList<>(Trace.NB_PP_ROWS_MODEXP_DATA);
    this.wcpCallRecords = new ArrayList<>(Trace.NB_PP_ROWS_MODEXP_DATA);
  }

  @Override
  public MmuData preProcess(MmuData mmuData) {
    final HubToMmuValues hubToMmuValues = mmuData.hubToMmuValues();
    row1(hubToMmuValues);
    row2();
    row3();
    row4();
    row5();
    row6();

    mmuData.eucCallRecords(eucCallRecords);
    mmuData.wcpCallRecords(wcpCallRecords);

    // setting Out and Bin values
    mmuData.outAndBinValues(
        MmuOutAndBinValues.builder()
            .out1(initialTargetByteOffset)
            .out2(initialSourceLimbOffset)
            .out3(initialSourceByteOffset)
            .out4(firstLimbByteSize)
            .out5(lastLimbByteSize)
            .bin1(firstLimbSingleSource)
            .bin2(aligned)
            .bin3(lastLimbSingleSource)
            .build());

    mmuData.totalLeftZeroesInitials(initialTotalLeftZeroes);
    mmuData.totalNonTrivialInitials(initialTotalNonTrivial);
    mmuData.totalRightZeroesInitials(initialTotalRightZeroes);

    return mmuData;
  }

  private void row1(final HubToMmuValues hubToMmuValues) {
    // row n°1
    parameterByteSize = (int) hubToMmuValues.size();
    parameterOffset =
        (int) (hubToMmuValues.referenceOffset() + hubToMmuValues.sourceOffsetLo().longValueExact());
    leftoverDataSize =
        (int) (hubToMmuValues.referenceSize() - hubToMmuValues.sourceOffsetLo().longValueExact());
    final int numberLeftPaddingBytes = 512 - parameterByteSize;

    EucOperation eucOp = euc.callEUC(longToBytes(numberLeftPaddingBytes), Bytes.of(Trace.LLARGE));

    initialTargetByteOffset = eucOp.remainder().toInt();
    initialTotalLeftZeroes = eucOp.quotient().toInt();

    eucCallRecords.add(
        MmuEucCallRecord.builder()
            .dividend(numberLeftPaddingBytes)
            .divisor(Trace.LLARGE)
            .quotient(eucOp.quotient().toLong())
            .remainder(eucOp.remainder().toLong())
            .build());

    wcpCallRecords.add(MmuWcpCallRecord.EMPTY_CALL);
  }

  private void row2() {
    // row n°2
    final Bytes wcpArg1 = longToBytes(leftoverDataSize);
    final Bytes wcpArg2 = longToBytes(parameterByteSize);
    final boolean wcpResult = wcp.callLT(wcpArg1, wcpArg2);
    dataRunsOut = wcpResult;
    int numberRightPaddingBytes = dataRunsOut ? parameterByteSize - leftoverDataSize : 0;
    wcpCallRecords.add(
        MmuWcpCallRecord.instLtBuilder().arg1Lo(wcpArg1).arg2Lo(wcpArg2).result(wcpResult).build());

    EucOperation eucOp = euc.callEUC(longToBytes(numberRightPaddingBytes), Bytes.of(Trace.LLARGE));
    initialTotalRightZeroes = eucOp.quotient().toInt();
    initialTotalNonTrivial =
        Trace.NB_MICRO_ROWS_TOT_MODEXP_DATA - initialTotalLeftZeroes - initialTotalRightZeroes;
    rightPaddingRemainder = eucOp.remainder().toInt();
    eucCallRecords.add(
        MmuEucCallRecord.builder()
            .dividend(numberRightPaddingBytes)
            .divisor(Trace.LLARGE)
            .quotient(eucOp.quotient().toLong())
            .remainder(eucOp.remainder().toLong())
            .build());
  }

  private void row3() {
    // row n°3
    final Bytes wcpArg1 = longToBytes(initialTotalNonTrivial);
    final Bytes wcpArg2 = Bytes.of(1);
    final boolean wcpResult = wcp.callEQ(wcpArg1, wcpArg2);
    wcpCallRecords.add(
        MmuWcpCallRecord.instEqBuilder().arg1Lo(wcpArg1).arg2Lo(wcpArg2).result(wcpResult).build());

    final long dividend = parameterOffset;
    EucOperation eucOp = euc.callEUC(longToBytes(dividend), Bytes.of(Trace.LLARGE));
    initialSourceLimbOffset = eucOp.quotient().toInt();
    initialSourceByteOffset = eucOp.remainder().toInt();
    eucCallRecords.add(
        MmuEucCallRecord.builder()
            .dividend(dividend)
            .divisor(Trace.LLARGE)
            .quotient(eucOp.quotient().toLong())
            .remainder(eucOp.remainder().toLong())
            .build());
    if (wcpResult) {
      firstLimbByteSize = dataRunsOut ? leftoverDataSize : parameterByteSize;
    } else {
      firstLimbByteSize = Trace.LLARGE - initialTargetByteOffset;
    }
    lastLimbByteSize = dataRunsOut ? Trace.LLARGE - rightPaddingRemainder : Trace.LLARGE;
  }

  private void row4() {
    // row n°4
    final Bytes wcpArg1 = longToBytes(initialSourceByteOffset + firstLimbByteSize - 1);
    final Bytes wcpArg2 = Bytes.of(Trace.LLARGE);
    final boolean wcpResult = wcp.callLT(wcpArg1, wcpArg2);
    firstLimbSingleSource = wcpResult;
    wcpCallRecords.add(
        MmuWcpCallRecord.instLtBuilder().arg1Lo(wcpArg1).arg2Lo(wcpArg2).result(wcpResult).build());

    eucCallRecords.add(MmuEucCallRecord.EMPTY_CALL);
  }

  private void row5() {
    // row n°5
    final Bytes wcpArg1 = longToBytes(initialSourceByteOffset);
    final Bytes wcpArg2 = Bytes.of(initialTargetByteOffset);
    final boolean wcpResult = wcp.callEQ(wcpArg1, wcpArg2);
    aligned = wcpResult;
    wcpCallRecords.add(
        MmuWcpCallRecord.instEqBuilder().arg1Lo(wcpArg1).arg2Lo(wcpArg2).result(wcpResult).build());

    eucCallRecords.add(MmuEucCallRecord.EMPTY_CALL);
  }

  private void row6() {
    // row n°6
    final long dividend = initialSourceByteOffset + firstLimbByteSize;
    EucOperation eucOp = euc.callEUC(longToBytes(dividend), Bytes.of(Trace.LLARGE));
    middleSourceByteOffset = eucOp.remainder().toInt();
    eucCallRecords.add(
        MmuEucCallRecord.builder()
            .dividend(dividend)
            .divisor(Trace.LLARGE)
            .quotient(eucOp.quotient().toLong())
            .remainder(eucOp.remainder().toLong())
            .build());

    final Bytes wcpArg1 = longToBytes(middleSourceByteOffset + lastLimbByteSize - 1);
    final Bytes wcpArg2 = Bytes.of(Trace.LLARGE);
    final boolean wcpResult = wcp.callEQ(wcpArg1, wcpArg2);
    lastLimbSingleSource = aligned ? true : wcpResult;
    wcpCallRecords.add(
        MmuWcpCallRecord.instEqBuilder().arg1Lo(wcpArg1).arg2Lo(wcpArg2).result(wcpResult).build());
  }

  @Override
  public MmuData setMicroInstructions(MmuData mmuData) {
    HubToMmuValues hubToMmuValues = mmuData.hubToMmuValues();

    mmuData.mmuToMmioConstantValues(
        MmuToMmioConstantValues.builder()
            .sourceContextNumber(hubToMmuValues.sourceId())
            .exoSum(hubToMmuValues.exoSum())
            .phase(hubToMmuValues.phase())
            .exoId(hubToMmuValues.targetId())
            .build());

    // Left Zeroes
    for (int i = 0; i < initialTotalLeftZeroes; i++) {
      vanishingMicroInstruction(mmuData, i);
    }

    // Non Trivial Rows
    // TODO: Determine relative value of limb
    final Bytes firstOrOnlyMicroInstLimb = Bytes.EMPTY;
    firstOrOnlyMicroInstruction(mmuData, firstOrOnlyMicroInstLimb);

    middleFirstSourceLimbOffset = aligned ? initialSourceLimbOffset + 1 : initialSourceLimbOffset;
    middleMicroInst =
        aligned ? Trace.MMIO_INST_RAM_TO_LIMB_TRANSPLANT : Trace.MMIO_INST_RAM_TO_LIMB_TWO_SOURCE;
    for (int i = 1; i < mmuData.totalNonTrivialInitials() - 1; i++) {
      // TODO: Determine relative value of limb
      final Bytes middleMicroInstlimb = Bytes.EMPTY;
      final int sourceLimbOffset = middleFirstSourceLimbOffset + i - 1;
      final int targetLimbOffset = initialTotalLeftZeroes + i;
      middleMicroInstruction(mmuData, sourceLimbOffset, targetLimbOffset, middleMicroInstlimb);
    }
    // TODO: Determine relative value of limb
    final Bytes lastMicroInstLimb = Bytes.EMPTY;
    lastMicroInstruction(mmuData, lastMicroInstLimb);

    // Right Zeroes
    for (int i = 0; i < initialTotalRightZeroes; i++) {
      vanishingMicroInstruction(mmuData, initialTotalLeftZeroes + initialTotalNonTrivial + i);
    }

    return mmuData;
  }

  private void vanishingMicroInstruction(MmuData mmuData, final int i) {
    mmuData.mmuToMmioInstruction(
        MmuToMmioInstruction.builder()
            .mmioInstruction(Trace.MMIO_INST_LIMB_VANISHES)
            .targetLimbOffset(i)
            .build());
  }

  private void firstOrOnlyMicroInstruction(MmuData mmuData, final Bytes limb) {
    final int firstMicroInst =
        firstLimbSingleSource
            ? Trace.MMIO_INST_RAM_TO_LIMB_ONE_SOURCE
            : Trace.MMIO_INST_RAM_TO_LIMB_TWO_SOURCE;
    mmuData.mmuToMmioInstruction(
        MmuToMmioInstruction.builder()
            .mmioInstruction(firstMicroInst)
            .size(firstLimbByteSize)
            .sourceLimbOffset(initialSourceLimbOffset)
            .sourceByteOffset(initialSourceByteOffset)
            .targetLimbOffset(initialTotalLeftZeroes)
            .targetByteOffset(initialTargetByteOffset)
            .limb(limb)
            .build());
  }

  private void middleMicroInstruction(
      MmuData mmuData, final int sourceLimbOffset, final int targetLimbOffset, final Bytes limb) {
    mmuData.mmuToMmioInstruction(
        MmuToMmioInstruction.builder()
            .mmioInstruction(middleMicroInst)
            .size(Trace.LLARGE)
            .sourceLimbOffset(sourceLimbOffset)
            .sourceByteOffset(middleSourceByteOffset)
            .targetLimbOffset(targetLimbOffset)
            .targetByteOffset(0)
            .limb(limb)
            .build());
  }

  private void lastMicroInstruction(MmuData mmuData, final Bytes limb) {
    final int lastMicroInstruction =
        lastLimbSingleSource
            ? Trace.MMIO_INST_RAM_TO_LIMB_ONE_SOURCE
            : Trace.MMIO_INST_RAM_TO_LIMB_TWO_SOURCE;
    mmuData.mmuToMmioInstruction(
        MmuToMmioInstruction.builder()
            .mmioInstruction(lastMicroInstruction)
            .size(lastLimbByteSize)
            .sourceLimbOffset(middleFirstSourceLimbOffset + initialTotalNonTrivial)
            .sourceByteOffset(middleSourceByteOffset)
            .targetLimbOffset(initialTotalLeftZeroes + initialTotalNonTrivial)
            .targetByteOffset(0)
            .limb(limb)
            .build());
  }
}
