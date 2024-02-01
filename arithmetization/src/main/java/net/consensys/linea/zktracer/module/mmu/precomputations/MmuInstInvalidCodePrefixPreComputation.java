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
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;

public class MmuInstInvalidCodePrefixPreComputation {
  private final Euc euc;
  private final Wcp wcp;
  private List<MmuEucCallRecord> eucCallRecords;
  private List<MmuWcpCallRecord> wcpCallRecords;

  private int initialSourceLimbOffset;
  private int initialSourceByteOffset;
  private Bytes microLimb;

  public MmuInstInvalidCodePrefixPreComputation(Euc euc, Wcp wcp) {
    this.euc = euc;
    this.wcp = wcp;
    this.eucCallRecords = new ArrayList<>(Trace.MMU_INST_NB_PP_ROWS_INVALID_CODE_PREFIX);
    this.wcpCallRecords = new ArrayList<>(Trace.MMU_INST_NB_PP_ROWS_INVALID_CODE_PREFIX);
  }

  public MmuData preProcess(MmuData mmuData) {

    // row n°1
    final long dividend1 = mmuData.hubToMmuValues().sourceOffsetLo().longValueExact();
    EucOperation eucOp = euc.callEUC(Bytes.ofUnsignedLong(dividend1), Bytes.of(16));
    int rem = eucOp.remainder().toInt();
    int quot = eucOp.quotient().toInt();
    initialSourceLimbOffset = quot;
    initialSourceByteOffset = rem;

    eucCallRecords.add(
        MmuEucCallRecord.builder()
            .dividend(dividend1)
            .divisor(Trace.LLARGE)
            .quotient(quot)
            .remainder(rem)
            .build());

    // TODO set microLimb to the right value
    Bytes arg1 = microLimb;
    Bytes arg2 = Bytes.of(0xEF);
    boolean result = wcp.callEQ(arg1, arg2);

    wcpCallRecords.add(
        MmuWcpCallRecord.builder()
            .instruction(UnsignedByte.of(OpCode.ISZERO.byteValue()))
            .arg1Hi(Bytes.EMPTY)
            .arg1Lo(arg1)
            .arg2Lo(arg2)
            .result(result)
            .build());

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
        MmuToMmioConstantValues.builder().sourceContextNumber(hubToMmuValues.sourceId()).build());

    // First and only micro-instruction.
    mmuData.mmuToMmioInstruction(
        MmuToMmioInstruction.builder()
            .mmioInstruction(Trace.MMIO_INST_PADDED_LIMB_FROM_RAM_ONE_SOURCE)
            .size(1)
            .sourceLimbOffset(initialSourceLimbOffset)
            .sourceByteOffset(initialSourceByteOffset)
            .targetByteOffset(15)
            .limb(microLimb)
            .build());

    return mmuData;
  }
}
