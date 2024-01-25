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

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.module.mmio.CallStackReader;
import net.consensys.linea.zktracer.module.mmio.MmioData;
import net.consensys.linea.zktracer.module.romLex.RomChunk;
import net.consensys.linea.zktracer.module.romLex.RomLex;
import net.consensys.linea.zktracer.runtime.microdata.MicroData;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;

@RequiredArgsConstructor
public class ExoToRamSlideOverlappingChunkDispatcher implements MmioDispatcher {

  private final MicroData microData;

  private final CallStackReader callStackReader;

  private final RomLex romLex;

  @Override
  public MmioData dispatch() {
    MmioData mmioData = new MmioData();

    Address exoAddress = microData.addressValue();
    Optional<RomChunk> romChunk = Optional.ofNullable(romLex.addressRomChunkMap().get(exoAddress));
    Bytes contractByteCode = romChunk.isPresent() ? romChunk.get().byteCode() : Bytes.EMPTY;

    int targetContext = microData.targetContextId();
    mmioData.cnA(0);
    mmioData.cnB(targetContext);
    mmioData.cnC(targetContext);

    int targetLimbOffset = microData.targetLimbOffset().toInt();
    int sourceLimbOffset = microData.sourceLimbOffset().toInt();
    mmioData.indexA(0);
    mmioData.indexB(targetLimbOffset);
    mmioData.indexC(targetLimbOffset + 1);
    mmioData.indexX(sourceLimbOffset);

    mmioData.valA(UnsignedByte.EMPTY_BYTES16);
    mmioData.valB(callStackReader.valueFromMemory(mmioData.cnB(), mmioData.indexB()));
    mmioData.valC(callStackReader.valueFromMemory(mmioData.cnC(), mmioData.indexC()));
    mmioData.valX(
        callStackReader.valueFromExo(contractByteCode, microData.exoSource(), mmioData.indexX()));

    mmioData.valANew(UnsignedByte.EMPTY_BYTES16);

    int targetByteOffset = microData.targetByteOffset().toInteger();
    int sourceByteOffset = microData.sourceByteOffset().toInteger();

    for (int i = 0; i < 16; i++) {
      if (i < targetByteOffset) {
        mmioData.valBNew()[i] = mmioData.valB()[i];
      } else {
        mmioData.valBNew()[i] = mmioData.valX()[sourceByteOffset + (i - targetByteOffset)];
      }
    }

    for (int i = 0; i < 16; i++) {
      int cutoff = (targetByteOffset + microData.size()) - 16;
      int start = sourceByteOffset + (16 - targetByteOffset);

      if (i < cutoff) {
        mmioData.valCNew()[i] = mmioData.valX()[i + start];
      } else {
        mmioData.valCNew()[i] = mmioData.valC()[i];
      }
    }

    mmioData.updateLimbsInMemory(callStackReader.callStack());

    return mmioData;
  }

  @Override
  public void update(MmioData mmioData, int counter) {
    mmioData.onePartialToTwo(
        mmioData.byteX(counter),
        mmioData.byteB(counter),
        mmioData.byteC(counter),
        mmioData.acc1(),
        mmioData.acc2(),
        mmioData.acc3(),
        mmioData.acc4(),
        microData.sourceByteOffset(),
        microData.targetByteOffset(),
        microData.size(),
        counter);
  }
}
