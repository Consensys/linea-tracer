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

package net.consensys.linea.zktracer.module.mmu.precomputations;

import static net.consensys.linea.zktracer.types.Conversions.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.mmu.MmuData;
import net.consensys.linea.zktracer.module.mmu.Trace;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import org.apache.tuweni.bytes.Bytes;

public class MmuInstRightPaddedWordExtractionPreComputation implements MmuPreComputation {
  private final Euc euc;
  private final Wcp wcp;
  private List<MmuEucCallRecord> eucCallRecords;
  private List<MmuWcpCallRecord> wcpCallRecords;

  private boolean secondLimbPadded;
  private long extractionSize;

  public MmuInstRightPaddedWordExtractionPreComputation(Euc euc, Wcp wcp) {
    this.euc = euc;
    this.wcp = wcp;
    this.eucCallRecords = new ArrayList<>(Trace.MMU_INST_NB_PP_ROWS_MSTORE);
    this.wcpCallRecords = new ArrayList<>(Trace.MMU_INST_NB_PP_ROWS_MSTORE);
  }

  @Override
  public MmuData preProcess(MmuData mmuData) {
    final HubToMmuValues hubToMmuValues = mmuData.hubToMmuValues();
    row1(hubToMmuValues);

    wcpCallRecords.add(MmuWcpCallRecord.builder().build()); // no call to WCP

    mmuData.eucCallRecords(eucCallRecords);
    mmuData.wcpCallRecords(wcpCallRecords);
    // setting Out and Bin values
    mmuData.outAndBinValues(MmuOutAndBinValues.builder().build()); // all 0

    mmuData.totalLeftZeroesInitials(0);
    mmuData.totalNonTrivialInitials(1);
    mmuData.totalRightZeroesInitials(0);

    return mmuData;
  }

  private void row1(final HubToMmuValues hubToMmuValues) {
    // row n°1
    final Bytes wcpArg1 =
        bigIntegerToBytes(hubToMmuValues.sourceOffsetLo().add(BigInteger.valueOf(32)));
    final long refSize = hubToMmuValues.referenceSize();
    final Bytes wcpArg2 = longToBytes(refSize);
    final boolean wcpResult = wcp.callLT(wcpArg1, wcpArg2);
    secondLimbPadded = wcpResult;
    extractionSize = secondLimbPadded ? refSize - hubToMmuValues.sourceOffsetLo().longValue() : 32;

    wcpCallRecords.add(
        MmuWcpCallRecord.builder().arg1Lo(wcpArg1).arg2Lo(wcpArg2).result(wcpResult).build());

    eucCallRecords.add(MmuEucCallRecord.builder().build());
  }

  private void row2(final HubToMmuValues hubToMmuValues) {
    // row n°1
    final Bytes wcpArg1 = longToBytes(extractionSize);
    final long refSize = hubToMmuValues.referenceSize();
    final Bytes wcpArg2 = longToBytes(refSize);
    final boolean wcpResult = wcp.callLT(wcpArg1, wcpArg2);
    secondLimbPadded = wcpResult;
    extractionSize = secondLimbPadded ? refSize - hubToMmuValues.sourceOffsetLo().longValue() : 32;

    wcpCallRecords.add(
        MmuWcpCallRecord.builder().arg1Lo(wcpArg1).arg2Lo(wcpArg2).result(wcpResult).build());

    eucCallRecords.add(MmuEucCallRecord.builder().build());
  }

  @Override
  public MmuData setMicroInstructions(MmuData mmuData) {
    //    HubToMmuValues hubToMmuValues = mmuData.hubToMmuValues();
    //
    //    mmuData.mmuToMmioConstantValues(
    //
    // MmuToMmioConstantValues.builder().targetContextNumber(hubToMmuValues.targetId()).build());
    //
    //    // First and only micro-instruction.
    //    mmuData.mmuToMmioInstruction(
    //        MmuToMmioInstruction.builder()
    //            .mmioInstruction(Trace.MMIO_INST_LIMB_TO_RAM_WRITE_LSB)
    //            .targetLimbOffset(initialTargetLimbOffset)
    //            .targetByteOffset(initialTargetByteOffset)
    //            .limb(hubToMmuValues.limb2())
    //            .build());

    return mmuData;
  }
}
