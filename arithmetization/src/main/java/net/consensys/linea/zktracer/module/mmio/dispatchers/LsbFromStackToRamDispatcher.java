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
public class LsbFromStackToRamDispatcher implements MmioDispatcher {

  private final MicroData microData;

  private final CallStackReader callStackReader;

  @Override
  public MmioData dispatch() {
    MmioData mmioData = new MmioData();

    int targetContext = microData.targetContextId();
    mmioData.cnA(targetContext);
    mmioData.cnB(0);
    mmioData.cnC(0);

    int targetLimbOffset = microData.targetLimbOffset().toInt();
    mmioData.indexA(targetLimbOffset);
    mmioData.indexB(0);
    mmioData.indexC(0);

    mmioData.valA(callStackReader.valueFromMemory(mmioData.cnA(), mmioData.indexA()));
    mmioData.valB(UnsignedByte.EMPTY_BYTES16);
    mmioData.valC(UnsignedByte.EMPTY_BYTES16);

    mmioData.valBNew(UnsignedByte.EMPTY_BYTES16);
    mmioData.valCNew(UnsignedByte.EMPTY_BYTES16);

    mmioData.setVal(microData.eWordValue());

    int targetByteOffset = microData.targetByteOffset().toInteger();
    for (int i = 0; i < 16; i++) {
      if (i == targetByteOffset) {
        mmioData.valANew()[targetByteOffset] = mmioData.valLo()[15];
      } else {
        mmioData.valANew()[i] = mmioData.valA()[i];
      }
    }

    mmioData.updateLimbsInMemory(callStackReader.callStack());

    return mmioData;
  }

  @Override
  public void update(MmioData mmioData, int counter) {
    mmioData.byteSwap(
        mmioData.byteA(counter), mmioData.acc1(), microData.targetByteOffset(), counter);
  }
}
