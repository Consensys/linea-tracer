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
public class RamLimbExcisionDispatcher implements MmioDispatcher {
  private final MicroData microData;

  private final CallStack callStack;

  @Override
  public MmioData dispatch() {
    MmioData mmioData = new MmioData();

    mmioData.cnA(0);
    mmioData.cnB(microData.targetContext());
    mmioData.cnC(0);

    mmioData.indexA(0);
    mmioData.indexB(microData.targetLimbOffset().toInt());
    mmioData.indexC(0);

    mmioData.valA(UnsignedByte.EMPTY_BYTES16);
    mmioData.valB(callStack.valueFromMemory(mmioData.cnB(), mmioData.indexB()));
    mmioData.valC(UnsignedByte.EMPTY_BYTES16);

    mmioData.valANew(mmioData.valA());
    mmioData.valBNew(
        excise(mmioData.valB(), microData.targetByteOffset().toInteger(), microData.size()));
    mmioData.valCNew(mmioData.valC());

    mmioData.updateLimbsInMemory(callStack);

    return mmioData;
  }

  private UnsignedByte[] excise(UnsignedByte[] source, int start, int size) {
    int end = start + size;

    Preconditions.checkState(
        start < 0 || size <= 0,
        "offsets out of bounds: start = %d, size = %d\none should have start >= 0 & size > 0"
            .formatted(start, size));

    // TODO: explore this case: it happens in practice : )
    //    Preconditions.checkState(
    //      end > 16,
    //      "offsets out of bounds:, start + size = %d is bigger than 16".formatted(end));

    for (int i = start; i < Math.min(end, 16); i++) {
      source[i] = UnsignedByte.ZERO;
    }

    return source;
  }

  @Override
  public void update(MmioData mmioData, int counter) {
    mmioData.excision(
        mmioData.byteB(counter),
        mmioData.acc1(),
        microData.targetByteOffset(),
        microData.size(),
        counter);
  }
}
