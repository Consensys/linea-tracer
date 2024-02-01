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
import org.apache.tuweni.bytes.Bytes32;

public class MmuInstMLoadPreComputation implements MmuPreComputation {
  private final Euc euc;
  private final Wcp wcp;
  private List<MmuEucCallRecord> eucCallRecords;
  private List<MmuWcpCallRecord> wcpCallRecords;

  private boolean aligned;
  private int initialSourceLimbOffset;
  private int initialSourceByteOffset;

  public MmuInstMLoadPreComputation(Euc euc, Wcp wcp) {
    this.euc = euc;
    this.wcp = wcp;
    this.eucCallRecords = new ArrayList<>(Trace.MMU_INST_NB_PP_ROWS_MLOAD);
    this.wcpCallRecords = new ArrayList<>(Trace.MMU_INST_NB_PP_ROWS_MLOAD);
  }

  public MmuData preProcess(MmuData mmuData) {
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

    Bytes isZeroArg = Bytes.ofUnsignedInt(mmuData.sourceByteOffset().toInteger());
    boolean result = wcp.callISZERO(Bytes32.wrap(isZeroArg));
    aligned = result;

    wcpCallRecords.add(
        MmuWcpCallRecord.builder()
            .instruction(UnsignedByte.of(OpCode.ISZERO.byteValue()))
            .arg1Hi(Bytes.EMPTY)
            .arg1Lo(Bytes.EMPTY)
            .arg2Lo(isZeroArg)
            .result(result)
            .build());

    mmuData.eucCallRecords(eucCallRecords);
    mmuData.wcpCallRecords(wcpCallRecords);
    mmuData.outAndBinValues(MmuOutAndBinValues.builder().build());

    mmuData.totalLeftZeroesInitials(0);
    mmuData.totalNonTrivialInitials(2);
    mmuData.totalRightZeroesInitials(0);

    return mmuData;
  }

  public MmuData setMicroInstructions(MmuData mmuData) {
    HubToMmuValues hubToMmuValues = mmuData.hubToMmuValues();

    mmuData.mmuToMmioConstantValues(
        MmuToMmioConstantValues.builder().sourceContextNumber(hubToMmuValues.sourceId()).build());

    // First micro-instruction.
    mmuData.mmuToMmioInstruction(
        MmuToMmioInstruction.builder()
            .mmioInstruction(
                aligned
                    ? Trace.MMIO_INST_RAM_TO_LIMB_TRANSPLANT
                    : Trace.MMIO_INST_PADDED_LIMB_FROM_RAM_TWO_SOURCE)
            .sourceLimbOffset(initialSourceLimbOffset)
            .sourceByteOffset(initialSourceByteOffset)
            .limb(hubToMmuValues.limb1())
            .build());

    // Second micro-instruction.
    mmuData.mmuToMmioInstruction(
        MmuToMmioInstruction.builder()
            .mmioInstruction(
                aligned
                    ? Trace.MMIO_INST_RAM_TO_LIMB_TRANSPLANT
                    : Trace.MMIO_INST_PADDED_LIMB_FROM_RAM_TWO_SOURCE)
            .sourceLimbOffset(initialSourceLimbOffset + 1)
            .sourceByteOffset(initialSourceByteOffset)
            .limb(hubToMmuValues.limb2())
            .build());

    return mmuData;
  }
}
