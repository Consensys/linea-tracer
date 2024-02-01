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

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.module.mmio.CallStackReader;
import net.consensys.linea.zktracer.module.mmio.MmioData;
import net.consensys.linea.zktracer.module.mmu.MmuData;
import net.consensys.linea.zktracer.types.UnsignedByte;

@RequiredArgsConstructor
public class RamToRamSlideChunkDispatcher implements MmioDispatcher {
  private final MmuData microData;

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
    mmioData.valBNew(slideChunk(mmioData, microData));
    mmioData.valCNew(mmioData.valC());
    mmioData.updateLimbsInMemory(callStackReader.callStack());

    return mmioData;
  }

  @Override
  public void update(MmioData mmioData, int counter) {
    // 1Partial => 1
    mmioData.onePartialToOne(
        mmioData.byteA(counter),
        mmioData.byteB(counter),
        mmioData.acc1(),
        mmioData.acc2(),
        microData.sourceByteOffset(),
        microData.targetByteOffset(),
        microData.size(),
        counter);
  }

  private UnsignedByte[] slideChunk(MmioData mmioData, MmuData microData) {
    UnsignedByte[] source = mmioData.valA();
    UnsignedByte[] target = mmioData.valB();
    int size = microData.size();
    int sourceByteOffset = microData.sourceByteOffset().toInteger();
    int targetByteOffset = microData.targetByteOffset().toInteger();

    Preconditions.checkState(
        size == 0 || sourceByteOffset + size > 16 || targetByteOffset + size > 16,
        "Wrong size: %d sourceByteOffset: %d targetByteOffset: %d"
            .formatted(size, sourceByteOffset, targetByteOffset));

    System.arraycopy(source, sourceByteOffset, target, targetByteOffset, size);

    return target;
  }
}
