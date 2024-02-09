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
import org.apache.tuweni.bytes.Bytes;

public class MStore8 implements MmuInstruction {
  private final Euc euc;
  private List<MmuEucCallRecord> eucCallRecords;
  private List<MmuWcpCallRecord> wcpCallRecords;
  private int initialTargetLimbOffset;
  private int initialTargetByteOffset;

  public MStore8(Euc euc) {
    this.euc = euc;
    this.eucCallRecords = new ArrayList<>(Trace.NB_PP_ROWS_MSTORE8);
    this.wcpCallRecords = new ArrayList<>(Trace.NB_PP_ROWS_MSTORE8);
  }

  @Override
  public MmuData preProcess(MmuData mmuData) {

    // row n°1
    final long dividend1 = mmuData.hubToMmuValues().targetOffset();
    EucOperation eucOp = euc.callEUC(Bytes.ofUnsignedLong(dividend1), Bytes.of(16));
    int rem = eucOp.remainder().toInt();
    int quot = eucOp.quotient().toInt();
    initialTargetLimbOffset = quot;
    initialTargetByteOffset = rem;

    eucCallRecords.add(
        MmuEucCallRecord.builder()
            .dividend(dividend1)
            .divisor(Trace.LLARGE)
            .quotient(quot)
            .remainder(rem)
            .build());

    wcpCallRecords.add(MmuWcpCallRecord.EMPTY_CALL); // no call to WCP

    mmuData.eucCallRecords(eucCallRecords);
    mmuData.wcpCallRecords(wcpCallRecords);
    // setting Out and Bin values
    mmuData.outAndBinValues(MmuOutAndBinValues.DEFAULT); // all 0

    mmuData.totalLeftZeroesInitials(0);
    mmuData.totalNonTrivialInitials(Trace.NB_MICRO_ROWS_TOT_MSTORE_EIGHT);
    mmuData.totalRightZeroesInitials(0);

    return mmuData;
  }

  @Override
  public MmuData setMicroInstructions(MmuData mmuData) {
    HubToMmuValues hubToMmuValues = mmuData.hubToMmuValues();

    mmuData.mmuToMmioConstantValues(
        MmuToMmioConstantValues.builder().targetContextNumber(hubToMmuValues.targetId()).build());

    // First and only micro-instruction.
    mmuData.mmuToMmioInstruction(
        MmuToMmioInstruction.builder()
            .mmioInstruction(Trace.MMIO_INST_LIMB_TO_RAM_ONE_TARGET)
            .size(1)
            .sourceByteOffset(Trace.LLARGEMO)
            .targetLimbOffset(initialTargetLimbOffset)
            .targetByteOffset(initialTargetByteOffset)
            .limb(hubToMmuValues.limb2())
            .build());

    return mmuData;
  }
}
