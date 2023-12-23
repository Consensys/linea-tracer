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
import net.consensys.linea.zktracer.module.mmio.MmioData;
import net.consensys.linea.zktracer.module.mmu.MicroData;
import net.consensys.linea.zktracer.runtime.callstack.CallStack;
import net.consensys.linea.zktracer.types.UnsignedByte;

@RequiredArgsConstructor
public class PaddedExoFromTwoDispatcher implements MmioDispatcher {
  private final MicroData microData;

  private final CallStack callStack;

  @Override
  public MmioData dispatch() {
    MmioData mmioData = new MmioData();

    int sourceContext = microData.sourceContext();
    mmioData.cnA(sourceContext);
    mmioData.cnB(sourceContext);
    mmioData.cnC(0);

    int sourceLimbOffset = microData.sourceLimbOffset().toInt();
    int targetLimbOffset = microData.targetLimbOffset().toInt();
    mmioData.indexA(sourceLimbOffset);
    mmioData.indexB(sourceContext + 1);
    mmioData.indexC(0);
    mmioData.indexX(targetLimbOffset);

    mmioData.valA(callStack.valueFromMemory(mmioData.cnA(), mmioData.indexA()));
    mmioData.valB(callStack.valueFromMemory(mmioData.cnB(), mmioData.indexB()));
    mmioData.valC(UnsignedByte.EMPTY_BYTES16);
    mmioData.valX(UnsignedByte.EMPTY_BYTES16);

    int sourceByteOffset = microData.sourceByteOffset().toInteger();
    int size = microData.size();
    boolean wrongOffsets =
        sourceByteOffset < 0 || sourceByteOffset >= 16 || sourceByteOffset + size <= 16;

    Preconditions.checkArgument(
        wrongOffsets,
        """
Wrong size/sourceByteOffset combo in PaddedExoFromTwoDispatcher:
  sourceByteOffset = %s
  size = %d
  sourceByteOffset + size = %d > 16
        """
            .formatted(sourceByteOffset, size, sourceByteOffset + size));

    for (int i = 0; i < size; i++) {
      if (sourceByteOffset + i < 16) {
        mmioData.valX()[i] = mmioData.valA()[sourceByteOffset + i];
      } else {
        mmioData.valX()[i] = mmioData.valB()[i - (16 - sourceByteOffset)];
      }
    }

    mmioData.valANew(mmioData.valA());
    mmioData.valBNew(mmioData.valB());
    mmioData.valCNew(UnsignedByte.EMPTY_BYTES16);

    mmioData.updateLimbsInMemory(callStack);

    return mmioData;
  }

  @Override
  public void update(MmioData mmioData, int counter) {
    mmioData.twoToOnePadded(
        mmioData.valA(),
        mmioData.valB(),
        mmioData.byteA(counter),
        mmioData.byteB(counter),
        mmioData.acc1(),
        mmioData.acc2(),
        microData.sourceByteOffset(),
        microData.size(),
        counter);
  }
}
