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

import static net.consensys.linea.zktracer.types.Bytecodes.readLimb;

import net.consensys.linea.zktracer.module.mmio.MmioData;
import net.consensys.linea.zktracer.module.mmu.MmuData;
import net.consensys.linea.zktracer.types.Bytes16;

public class RamVanishes extends MmioInstruction {

  public RamVanishes(MmuData mmuData, int instructionNumber) {
    super(mmuData, instructionNumber);
  }

  @Override
  public MmioData execute() {
    final MmioData mmioData = super.execute();

    mmioData.cnA(mmioData.targetContext());
    mmioData.cnB(0);
    mmioData.cnC(0);

    mmioData.indexA(mmioData.targetLimbOffset());
    mmioData.indexB(0);
    mmioData.indexC(0);
    mmioData.indexX(0);

    mmioData.valA(readLimb(mmuData.targetRamBytes(), mmioData.indexA()));
    mmioData.valB(Bytes16.ZERO);
    mmioData.valC(Bytes16.ZERO);
    mmioData.limb(Bytes16.ZERO);

    mmioData.valANew(Bytes16.ZERO);
    mmioData.valBNew(Bytes16.ZERO);
    mmioData.valCNew(Bytes16.ZERO);

    mmioData.excision(mmioData.valA(), mmioData.targetByteOffset(), mmioData.size());

    return mmioData;
  }
}
