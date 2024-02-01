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

import java.util.ArrayList;
import java.util.List;

import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.euc.EucOperation;
import net.consensys.linea.zktracer.module.mmu.MmuData;
import net.consensys.linea.zktracer.module.mmu.Trace;
import org.apache.tuweni.bytes.Bytes;

public class MmuInstMStore8PreComputation implements MmuPreComputation {
  private final Euc euc;
  private List<MmuEucCallRecord> eucCallRecords;
  private List<MmuWcpCallRecord> wcpCallRecords;

  private int initialTargetLimbOffset;
  private int initialTargetByteOffset;

  public MmuInstMStore8PreComputation(Euc euc) {
    this.euc = euc;
    this.eucCallRecords = new ArrayList<>(Trace.MMU_INST_NB_PP_ROWS_MSTORE);
    this.wcpCallRecords = new ArrayList<>(Trace.MMU_INST_NB_PP_ROWS_MSTORE);
  }

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

  public MmuData setMicroInstructions(MmuData mmuData) {
    HubToMmuValues hubToMmuValues = mmuData.hubToMmuValues();

    mmuData.mmuToMmioConstantValues(
        MmuToMmioConstantValues.builder().targetContextNumber(hubToMmuValues.targetId()).build());

    // First and only micro-instruction.
    mmuData.mmuToMmioInstruction(
        MmuToMmioInstruction.builder()
            .mmioInstruction(Trace.MMIO_INST_LIMB_TO_RAM_WRITE_LSB)
            .targetLimbOffset(initialTargetLimbOffset)
            .targetByteOffset(initialTargetByteOffset)
            .limb(hubToMmuValues.limb2())
            .build());

    return mmuData;
  }
}
