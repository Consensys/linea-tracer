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
import net.consensys.linea.zktracer.module.mmio.PowType;
import net.consensys.linea.zktracer.module.mmu.MicroData;
import net.consensys.linea.zktracer.runtime.callstack.CallStack;
import net.consensys.linea.zktracer.types.UnsignedByte;

@RequiredArgsConstructor
public class NaRamToStack2To2PaddedDispatcher implements MmioDispatcher {
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
    mmioData.indexA(sourceLimbOffset);
    mmioData.indexB(sourceLimbOffset + 1);
    mmioData.indexC(0);

    Preconditions.checkState(
        microData.isRootContext() && microData.isType5(),
        "Should be: EXCEPTIONAL_RAM_TO_STACK_3_TO_2_FULL");

    mmioData.valA(callStack.valueFromMemory(mmioData.cnA(), mmioData.indexA()));
    mmioData.valB(callStack.valueFromMemory(mmioData.cnB(), mmioData.indexB()));
    mmioData.valC(UnsignedByte.EMPTY_BYTES16);

    mmioData.valANew(mmioData.valA());
    mmioData.valBNew(mmioData.valB());
    mmioData.valCNew(mmioData.valC());

    mmioData.valLo(UnsignedByte.EMPTY_BYTES16);

    int sourceByteOffset = microData.sourceByteOffset().toInteger();
    int diff = 16 - sourceByteOffset;

    // twoOneFull
    for (int i = 0; i < 16; i++) {
      if (sourceByteOffset + i < 16) {
        mmioData.valHi()[i] = mmioData.valA()[i + sourceByteOffset];
      } else {
        mmioData.valHi()[i] = mmioData.valB()[i - diff];
      }
    }

    // oneOnePadded
    for (int i = 0; i < microData.size(); i++) {
      mmioData.valLo()[i] = mmioData.valB()[sourceByteOffset + i];
    }

    return mmioData;
  }

  @Override
  public void update(MmioData mmioData, int counter) {
    mmioData.twoToOneFull(
        mmioData.byteA(counter),
        mmioData.byteB(counter),
        mmioData.acc1(),
        mmioData.acc2(),
        microData.sourceByteOffset(),
        counter);

    // [2 => 1Padded]
    mmioData.oneToOnePadded(
        mmioData.valB(),
        mmioData.byteB(counter),
        mmioData.acc3(),
        PowType.POW_256_2,
        microData.sourceByteOffset(),
        microData.size(),
        counter);
  }
}