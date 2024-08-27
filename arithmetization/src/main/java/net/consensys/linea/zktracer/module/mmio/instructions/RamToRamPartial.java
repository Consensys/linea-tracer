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

package net.consensys.linea.zktracer.module.mmio.instructions;

import static net.consensys.linea.zktracer.module.mmio.MmioPatterns.onePartialToOne;
import static net.consensys.linea.zktracer.module.mmio.MmioPatterns.updateTemporaryTargetRam;
import static net.consensys.linea.zktracer.types.Bytecodes.readLimb;

import net.consensys.linea.zktracer.module.mmio.MmioData;
import net.consensys.linea.zktracer.module.mmu.MmuData;
import net.consensys.linea.zktracer.types.Bytes16;

public class RamToRamPartial extends MmioInstruction {

  public RamToRamPartial(MmuData mmuData, int instructionNumber) {
    super(mmuData, instructionNumber);
  }

  @Override
  public MmioData execute() {
    final MmioData mmioData = super.execute();

    mmioData.cnA(mmioData.sourceContext());
    mmioData.cnB(mmioData.targetContext());
    mmioData.cnC(0);

    mmioData.indexA(mmioData.sourceLimbOffset());
    mmioData.indexB(mmioData.targetLimbOffset());
    mmioData.indexC(0);
    mmioData.indexX(0);

    mmioData.valA(readLimb(mmuData.sourceRamBytes(), mmioData.indexA()));
    mmioData.valB(readLimb(mmuData.targetRamBytes(), mmioData.indexB()));
    mmioData.valC(Bytes16.ZERO);
    mmioData.limb(Bytes16.ZERO);

    mmioData.valANew(mmioData.valA());
    mmioData.valBNew(
        onePartialToOne(
            mmioData.valA(),
            mmioData.valB(),
            mmioData.sourceByteOffset(),
            mmioData.targetByteOffset(),
            mmioData.size()));
    mmioData.valCNew(Bytes16.ZERO);

    mmioData.onePartialToOne(
        mmioData.valA(),
        mmioData.valB(),
        mmioData.sourceByteOffset(),
        mmioData.targetByteOffset(),
        mmioData.size());

    if (mmioData.targetLimbIsTouchedTwice()) {
      updateTemporaryTargetRam(mmuData, mmioData.indexB(), mmioData.valBNew());
    }

    return mmioData;
  }
}
