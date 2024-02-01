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
import net.consensys.linea.zktracer.module.mmu.MmuData;

@RequiredArgsConstructor
public class FullStackToRamDispatcher implements MmioDispatcher {

  private final MmuData microData;

  private final CallStackReader callStackReader;

  @Override
  public MmioData dispatch() {
    MmioData mmioData = new MmioData();

    int targetContext = microData.targetContextId();
    mmioData.cnA(targetContext);
    mmioData.cnB(targetContext);
    mmioData.cnC(targetContext);

    int targetLimbOffset = microData.targetLimbOffset().toInt();
    mmioData.indexA(targetLimbOffset);
    mmioData.indexB(targetLimbOffset + 1);
    mmioData.indexC(targetLimbOffset + 2);

    mmioData.setVal(microData.eWordValue());

    mmioData.valA(callStackReader.valueFromMemory(mmioData.cnA(), mmioData.indexA()));
    mmioData.valB(callStackReader.valueFromMemory(mmioData.cnB(), mmioData.indexB()));
    mmioData.valC(callStackReader.valueFromMemory(mmioData.cnC(), mmioData.indexC()));

    int targetByteOffset = microData.targetByteOffset().toInteger();
    for (int i = 0; i < 16; i++) {
      if (i < targetByteOffset) {
        mmioData.valANew()[i] = mmioData.valA()[i];
        mmioData.valBNew()[i] = mmioData.valHi()[i + 16 - targetByteOffset];
        mmioData.valCNew()[i] = mmioData.valLo()[i + 16 - targetByteOffset];
      } else {
        mmioData.valANew()[i] = mmioData.valHi()[i - targetByteOffset];
        mmioData.valBNew()[i] = mmioData.valLo()[i - targetByteOffset];
        mmioData.valCNew()[i] = mmioData.valC()[i];
      }
    }

    mmioData.updateLimbsInMemory(callStackReader.callStack());

    return mmioData;
  }

  @Override
  public void update(MmioData mmioData, int counter) {
    // 2Full=>3
    mmioData.twoFullToThree(
        mmioData.byteA(counter),
        mmioData.byteC(counter),
        mmioData.byteHi(counter),
        mmioData.byteLo(counter),
        mmioData.acc1(),
        mmioData.acc2(),
        mmioData.acc3(),
        mmioData.acc4(),
        mmioData.acc5(),
        mmioData.acc6(),
        microData.targetByteOffset(),
        counter);
  }
}
