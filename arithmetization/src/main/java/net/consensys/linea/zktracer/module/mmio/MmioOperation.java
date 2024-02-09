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

package net.consensys.linea.zktracer.module.mmio;

import static net.consensys.linea.zktracer.module.mmio.MmioDataProcessor.maxCounter;
import static net.consensys.linea.zktracer.types.Conversions.unsignedBytesSubArrayToBytes;
import static net.consensys.linea.zktracer.types.Conversions.unsignedBytesToBytes;

import java.util.Objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.module.hub.State;
import net.consensys.linea.zktracer.module.mmu.MmuData;
import net.consensys.linea.zktracer.types.UnsignedByte;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
class MmioOperation extends ModuleOperation {
  private final MmuData mmuData;
  private final MmioDataProcessor mmioDataProcessor;
  private final State.TxState.Stamps moduleStamps;
  private final int microStamp;
  private final boolean isInitCode;

  @Override
  protected int computeLineCount() {
    return 1 + maxCounter(mmuData);
  }

  void trace(Trace trace, MmioData mmioData, int txNum, int counter) {
    trace
        .cnA(mmioData.cnA())
        .cnB(mmioData.cnB())
        .cnC(mmioData.cnC())
        .indexA(UnsignedByte.of(mmioData.indexA()))
        .indexB(UnsignedByte.of(mmioData.indexB()))
        .indexC(UnsignedByte.of(mmioData.indexC()))
        .valA(unsignedBytesToBytes(mmioData.valA()))
        .valB(unsignedBytesToBytes(mmioData.valB()))
        .valC(unsignedBytesToBytes(mmioData.valC()))
        .valANew(unsignedBytesToBytes(mmioData.valANew()))
        .valBNew(unsignedBytesToBytes(mmioData.valBNew()))
        .valCNew(unsignedBytesToBytes(mmioData.valCNew()))
        .byteA(Objects.requireNonNullElse(mmioData.valA()[counter], UnsignedByte.ZERO))
        .byteB(Objects.requireNonNullElse(mmioData.valB()[counter], UnsignedByte.ZERO))
        .byteC(Objects.requireNonNullElse(mmioData.valC()[counter], UnsignedByte.ZERO))
        .accA(unsignedBytesSubArrayToBytes(mmioData.valA(), counter + 1))
        .accB(unsignedBytesSubArrayToBytes(mmioData.valB(), counter + 1))
        .accC(unsignedBytesSubArrayToBytes(mmioData.valC(), counter + 1))
        //        .microInstructionStamp(Bytes.of(microStamp))
        //        .microInstruction(Bytes.ofUnsignedInt(microData.microOp()))
        .contextSource(mmuData.sourceContextId())
        .contextTarget(mmuData.targetContextId())
        //        .isInit(isInitCode)
        .sourceLimbOffset(mmuData.sourceLimbOffset().toLong())
        .targetLimbOffset(mmuData.targetLimbOffset().toLong())
        .sourceByteOffset((short) mmuData.sourceByteOffset().toInteger())
        .targetByteOffset((short) mmuData.targetByteOffset().toInteger())
        .size(mmuData.hubToMmuValues().size())
        //        .erf(microData.isErf())
        //        .fast(microData.isFast())
        //        .stackValueHigh(unsignedBytesToBytes(mmioData.valHi()))
        //        .stackValueLow(unsignedBytesToBytes(mmioData.valLo()))
        //        .stackValueHiByte(Objects.requireNonNullElse(mmioData.valHi()[counter],
        // UnsignedByte.ZERO))
        //        .stackValueLoByte(Objects.requireNonNullElse(mmioData.valLo()[counter],
        // UnsignedByte.ZERO))
        //        .accValHi(unsignedBytesSubArrayToBytes(mmioData.valHi(), counter + 1))
        //        .accValLo(unsignedBytesSubArrayToBytes(mmioData.valLo(), counter + 1))
        .exoIsRom(mmuData.exoIsRom())
        .exoIsLog(mmuData.exoIsLog())
        //        .exoIsHash(mmuData.exoIsHash())
        .exoIsTxcd(mmuData.exoIsTxcd())
        .indexX(mmioData.indexX())
        //        .valX(unsignedBytesToBytes(mmioData.valX()))
        //        .byteX(Objects.requireNonNullElse(mmioData.valX()[counter], UnsignedByte.ZERO))
        //        .accX(unsignedBytesSubArrayToBytes(mmioData.valX(), counter + 1))
        //        .txNum(Bytes.ofUnsignedInt(txNum))
        //        .logNum(Bytes.ofUnsignedInt(moduleStamps.log()))
        .bin1(mmioData.bin1())
        .bin2(mmioData.bin2())
        .bin3(mmioData.bin3())
        .bin4(mmioData.bin4())
        .bin5(mmioData.bin5())
        .acc1(mmioData.acc1())
        .acc2(mmioData.acc2())
        .acc3(mmioData.acc3())
        .acc4(mmioData.acc4())
        //        .acc5(mmioData.acc5())
        //        .acc6(mmioData.acc6())
        .pow2561(mmioData.pow2561())
        .pow2562(mmioData.pow2562())
        .counter((short) counter)
        .validateRow();
  }
}
