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

import static net.consensys.linea.zktracer.types.Conversions.unsignedBytesSubArrayToUnsignedBigInteger;
import static net.consensys.linea.zktracer.types.Conversions.unsignedBytesToUnsignedBigInteger;

import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.util.List;

import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.hub.State;
import net.consensys.linea.zktracer.module.romLex.RomLex;
import net.consensys.linea.zktracer.runtime.callstack.CallFrameType;
import net.consensys.linea.zktracer.runtime.callstack.CallStack;
import net.consensys.linea.zktracer.runtime.microdata.MicroData;

/** MMIO contains the MEMORY MAPPED INPUT OUTPUT module's state. */
@RequiredArgsConstructor
public class Mmio implements Module {
  private Trace trace;

  private Trace currentTrace;

  private final RomLex romLex;
  private final CallStack callStack;

  @Override
  public String moduleKey() {
    return "MMIO";
  }

  @Override
  public void enterTransaction() {}

  @Override
  public void popTransaction() {}

  @Override
  public int lineCount() {
    return 0;
  }

  @Override
  public List<ColumnHeader> columnsHeaders() {
    return null;
  }

  @Override
  public void commit(List<MappedByteBuffer> buffers) {
    // TODO: concatenate currentTrace to trace
    Trace trace = new Trace(buffers);
  }

  public void handleRam(
      MicroData microData, final State.TxState.Stamps moduleStamps, final int microStamp) {
    MmioDataProcessor mmioDataProcessor = new MmioDataProcessor(romLex, microData, callStack);

    if (microData.microOp() == 0) {
      return;
    }

    int maxCounter = mmioDataProcessor.maxCounter();
    MmioData mmioData = mmioDataProcessor.dispatchMmioData();

    for (int i = 0; i < maxCounter; i++) {
      mmioDataProcessor.updateMmioData(mmioData, maxCounter);
      boolean isInitCode = callStack.current().type() == CallFrameType.INIT_CODE;
      int txNum = 0 /*TODO: callStack.txNum*/;
      trace(microData, mmioData, microStamp, isInitCode, txNum, moduleStamps, i);
    }
  }

  private void trace(
      MicroData microData,
      MmioData mmioData,
      int microStamp,
      boolean isInit,
      int txNum,
      State.TxState.Stamps moduleStamps,
      int counter) {
    currentTrace
        .cnA(BigInteger.valueOf(mmioData.cnA()))
        .cnB(BigInteger.valueOf(mmioData.cnB()))
        .cnC(BigInteger.valueOf(mmioData.cnC()))
        .indexA(BigInteger.valueOf(mmioData.indexA()))
        .indexB(BigInteger.valueOf(mmioData.indexB()))
        .indexC(BigInteger.valueOf(mmioData.indexC()))
        .valA(unsignedBytesToUnsignedBigInteger(mmioData.valA()))
        .valB(unsignedBytesToUnsignedBigInteger(mmioData.valB()))
        .valC(unsignedBytesToUnsignedBigInteger(mmioData.valC()))
        .valANew(unsignedBytesToUnsignedBigInteger(mmioData.valANew()))
        .valBNew(unsignedBytesToUnsignedBigInteger(mmioData.valBNew()))
        .valCNew(unsignedBytesToUnsignedBigInteger(mmioData.valCNew()))
        .byteA(mmioData.valA()[counter])
        .byteB(mmioData.valB()[counter])
        .byteC(mmioData.valC()[counter])
        .accA(unsignedBytesSubArrayToUnsignedBigInteger(mmioData.valA(), counter + 1))
        .accB(unsignedBytesSubArrayToUnsignedBigInteger(mmioData.valB(), counter + 1))
        .accC(unsignedBytesSubArrayToUnsignedBigInteger(mmioData.valC(), counter + 1))
        .microInstructionStamp(BigInteger.valueOf(microStamp))
        .microInstruction(BigInteger.valueOf(microData.microOp()))
        .contextSource(BigInteger.valueOf(microData.sourceContext()))
        .contextTarget(BigInteger.valueOf(microData.targetContext()))
        .isInit(isInit)
        .sourceLimbOffset(microData.sourceLimbOffset().toUnsignedBigInteger())
        .targetLimbOffset(microData.targetLimbOffset().toUnsignedBigInteger())
        .sourceByteOffset(microData.sourceByteOffset().toBigInteger())
        .targetByteOffset(microData.targetByteOffset().toBigInteger())
        .size(BigInteger.valueOf(microData.size()))
        .erf(microData.isErf())
        .stackValueHigh(unsignedBytesToUnsignedBigInteger(mmioData.valHi()))
        .stackValueLow(unsignedBytesToUnsignedBigInteger(mmioData.valLo()))
        .stackValueHiByte(mmioData.valHi()[counter])
        .stackValueLoByte(mmioData.valLo()[counter])
        .accValHi(unsignedBytesSubArrayToUnsignedBigInteger(mmioData.valHi(), counter + 1))
        .accValLo(unsignedBytesSubArrayToUnsignedBigInteger(mmioData.valLo(), counter + 1))
        .exoIsRom(microData.exoIsRom())
        .exoIsLog(microData.exoIsLog())
        .exoIsHash(microData.exoIsHash())
        .exoIsTxcd(microData.exoIsTxcd())
        .indexX(BigInteger.valueOf(mmioData.indexX()))
        .valX(unsignedBytesToUnsignedBigInteger(mmioData.valX()))
        .byteX(mmioData.valX()[counter])
        .accX(unsignedBytesSubArrayToUnsignedBigInteger(mmioData.valX(), counter + 1))
        .txNum(BigInteger.valueOf(txNum))
        .logNum(BigInteger.valueOf(moduleStamps.log()))
        .bin1(mmioData.bin1())
        .bin2(mmioData.bin2())
        .bin3(mmioData.bin3())
        .bin4(mmioData.bin4())
        .bin5(mmioData.bin5())
        .acc1(mmioData.acc1().toUnsignedBigInteger())
        .acc2(mmioData.acc2().toUnsignedBigInteger())
        .acc3(mmioData.acc3().toUnsignedBigInteger())
        .acc4(mmioData.acc4().toUnsignedBigInteger())
        .acc5(mmioData.acc5().toUnsignedBigInteger())
        .acc6(mmioData.acc6().toUnsignedBigInteger())
        .pow2561(mmioData.pow2561().toUnsignedBigInteger())
        .pow2562(mmioData.pow2562().toUnsignedBigInteger())
        .counter(BigInteger.valueOf(counter))
        .validateRow()
        .build();
  }
}
