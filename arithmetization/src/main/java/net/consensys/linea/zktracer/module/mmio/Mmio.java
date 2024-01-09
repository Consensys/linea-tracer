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
import net.consensys.linea.zktracer.container.stacked.list.StackedList;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.State;
import net.consensys.linea.zktracer.module.romLex.RomLex;
import net.consensys.linea.zktracer.runtime.callstack.CallFrameType;
import net.consensys.linea.zktracer.runtime.callstack.CallStack;
import net.consensys.linea.zktracer.runtime.microdata.MicroData;

/** MMIO contains the MEMORY MAPPED INPUT OUTPUT module's state. */
@RequiredArgsConstructor
public class Mmio implements Module {
  private final StackedList<MmioOperation> state = new StackedList<>();
  private final Hub hub;
  private final RomLex romLex;
  private final CallStack callStack;

  @Override
  public String moduleKey() {
    return "MMIO";
  }

  @Override
  public void enterTransaction() {
    this.state.enter();
  }

  @Override
  public void popTransaction() {
    this.state.pop();
  }

  @Override
  public int lineCount() {
    return this.state.lineCount();
  }

  @Override
  public List<ColumnHeader> columnsHeaders() {
    return Trace.headers(this.lineCount());
  }

  @Override
  public void commit(List<MappedByteBuffer> buffers) {
    Trace trace = new Trace(buffers);

    for (MmioOperation o : this.state) {
      MmioDataProcessor processor = o.mmioDataProcessor();
      int maxCounter = processor.maxCounter();

      MmioData mmioData = processor.dispatchMmioData();
      for (int i = 0; i < maxCounter; i++) {
        processor.updateMmioData(mmioData, maxCounter);
        trace(trace, o, mmioData, hub.tx().number(), i);
      }
    }
  }

  public void handleRam(
      MicroData microData, final State.TxState.Stamps moduleStamps, final int microStamp) {
    if (microData.microOp() == 0) {
      return;
    }

    MmioDataProcessor mmioDataProcessor = new MmioDataProcessor(romLex, microData, callStack);
    boolean isInitCode = callStack.current().type() == CallFrameType.INIT_CODE;

    this.state.add(
        new MmioOperation(microData, mmioDataProcessor, moduleStamps, microStamp, isInitCode));
  }

  private void trace(
      Trace trace, MmioOperation mmioOperation, MmioData mmioData, int txNum, int counter) {
    MicroData microData = mmioOperation.microData();

    trace
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
        .microInstructionStamp(BigInteger.valueOf(mmioOperation.microStamp()))
        .microInstruction(BigInteger.valueOf(microData.microOp()))
        .contextSource(BigInteger.valueOf(microData.sourceContext()))
        .contextTarget(BigInteger.valueOf(microData.targetContext()))
        .isInit(mmioOperation.isInitCode())
        .sourceLimbOffset(microData.sourceLimbOffset().toUnsignedBigInteger())
        .targetLimbOffset(microData.targetLimbOffset().toUnsignedBigInteger())
        .sourceByteOffset(microData.sourceByteOffset().toBigInteger())
        .targetByteOffset(microData.targetByteOffset().toBigInteger())
        .size(BigInteger.valueOf(microData.size()))
        .erf(microData.isErf())
        .fast(microData.isFast())
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
        .logNum(BigInteger.valueOf(mmioOperation.moduleStamps().log()))
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
        .validateRow();
  }
}
