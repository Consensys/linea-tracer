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
import net.consensys.linea.zktracer.types.UnsignedByte;

@RequiredArgsConstructor
public class FullExoFromTwoDispatcher implements MmioDispatcher {
  private final MmuData microData;

  private final CallStackReader callStackReader;

  @Override
  public MmioData dispatch() {
    MmioData mmioData = new MmioData();

    int sourceContext = microData.sourceContextId();
    mmioData.cnA(sourceContext);
    mmioData.cnB(sourceContext);
    mmioData.cnC(0);

    int sourceLimbOffset = microData.sourceLimbOffset().toInt();
    int targetLimbOffset = microData.targetLimbOffset().toInt();
    mmioData.indexA(sourceLimbOffset);
    mmioData.indexB(sourceContext + 1);
    mmioData.indexC(0);
    mmioData.indexX(targetLimbOffset);

    mmioData.valA(callStackReader.valueFromMemory(mmioData.cnA(), mmioData.indexA()));
    mmioData.valB(callStackReader.valueFromMemory(mmioData.cnB(), mmioData.indexB()));

    UnsignedByte[] valA = mmioData.valA();
    UnsignedByte[] valB = mmioData.valB();
    mmioData.valANew(valA);
    mmioData.valBNew(valB);

    int sourceByteOffset = microData.sourceByteOffset().toInteger();
    UnsignedByte[] valX = mmioData.valX();

    System.arraycopy(valA, sourceByteOffset, valX, 0, 16 - sourceByteOffset);
    System.arraycopy(valB, 0, valX, 16 - sourceByteOffset, valX.length - 16 - sourceByteOffset);

    mmioData.updateLimbsInMemory(callStackReader.callStack());

    return mmioData;
  }

  @Override
  public void update(MmioData mmioData, int counter) {
    // [2 => 1 Full]
    mmioData.twoToOneFull(
        mmioData.byteA(counter),
        mmioData.byteB(counter),
        mmioData.acc1(),
        mmioData.acc2(),
        microData.sourceByteOffset(),
        counter);
  }
}
