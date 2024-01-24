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
import net.consensys.linea.zktracer.module.romLex.RomLex;
import net.consensys.linea.zktracer.runtime.microdata.MicroData;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;

@RequiredArgsConstructor
public class ExoToRamSlideChunkDispatcher implements MmioDispatcher {
  private final MicroData microData;

  private final CallStackReader callStackReader;

  private final RomLex romLex;

  @Override
  public MmioData dispatch() {
    MmioData mmioData = new MmioData();

    Address exoAddress = microData.addressValue();
    Bytes contractByteCode = romLex.addressRomChunkMap().get(exoAddress).byteCode();

    int targetContext = microData.targetContext();
    mmioData.cnA(0);
    mmioData.cnB(targetContext);
    mmioData.cnC(0);

    int targetLimbOffset = microData.targetLimbOffset().toInt();
    int sourceLimbOffset = microData.sourceLimbOffset().toInt();
    mmioData.indexA(0);
    mmioData.indexB(targetLimbOffset);
    mmioData.indexC(0);
    mmioData.indexX(sourceLimbOffset);

    mmioData.valA(UnsignedByte.EMPTY_BYTES16);
    mmioData.valB(callStackReader.valueFromMemory(mmioData.cnB(), mmioData.indexB()));
    mmioData.valC(UnsignedByte.EMPTY_BYTES16);
    mmioData.valX(
        callStackReader.valueFromExo(contractByteCode, microData.exoSource(), mmioData.indexX()));

    mmioData.valANew(UnsignedByte.EMPTY_BYTES16);
    mmioData.valBNew(mmioData.valB());
    mmioData.valCNew(UnsignedByte.EMPTY_BYTES16);

    int targetByteOffset = microData.targetByteOffset().toInteger();
    int sourceByteOffset = microData.sourceByteOffset().toInteger();
    int size = microData.size();

    Preconditions.checkArgument(
        sourceByteOffset + size > 16,
        "op: %s, sourceByteOffset: %d, size %d"
            .formatted(microData.opCode(), sourceByteOffset, size));

    System.arraycopy(
        mmioData.valX(),
        sourceByteOffset,
        mmioData.valBNew(),
        targetByteOffset,
        mmioData.valX().length - sourceByteOffset + size);

    mmioData.updateLimbsInMemory(callStackReader.callStack());

    return mmioData;
  }

  @Override
  public void update(MmioData mmioData, int counter) {
    mmioData.onePartialToOne(
        mmioData.byteX(counter),
        mmioData.byteB(counter),
        mmioData.acc1(),
        mmioData.acc2(),
        microData.sourceByteOffset(),
        microData.targetByteOffset(),
        microData.size(),
        counter);
  }
}
