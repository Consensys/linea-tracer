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

package net.consensys.linea.zktracer.module.mmio.dispatchers;

import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.module.mmio.CallStackReader;
import net.consensys.linea.zktracer.module.mmio.MmioData;
import net.consensys.linea.zktracer.runtime.microdata.MicroData;
import net.consensys.linea.zktracer.types.UnsignedByte;

@RequiredArgsConstructor
public class RamToRamDispatcher implements MmioDispatcher {

  private final MicroData microData;

  private final CallStackReader callStackReader;

  @Override
  public MmioData dispatch() {
    MmioData mmioData = new MmioData();
    mmioData.cnA(microData.sourceContextId());
    mmioData.cnB(microData.targetContextId());
    mmioData.cnC(0);

    mmioData.indexA(microData.sourceLimbOffset().toInt());
    mmioData.indexB(microData.targetLimbOffset().toInt());
    mmioData.indexC(0);

    mmioData.valA(callStackReader.valueFromMemory(mmioData.cnA(), mmioData.indexA()));
    mmioData.valB(callStackReader.valueFromMemory(mmioData.cnB(), mmioData.indexB()));
    mmioData.valC(UnsignedByte.EMPTY_BYTES16);

    mmioData.valANew(mmioData.valA());
    mmioData.valBNew(mmioData.valA());
    mmioData.valCNew(UnsignedByte.EMPTY_BYTES16);

    mmioData.updateLimbsInMemory(callStackReader.callStack());

    return mmioData;
  }

  @Override
  public void update(MmioData mmioData, int counter) {}
}
