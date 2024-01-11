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

import java.nio.MappedByteBuffer;
import java.util.List;
import java.util.Objects;

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
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;

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
      int maxCounter = maxCounter(o.microData());

      MmioData mmioData = processor.dispatchMmioData();
      for (int i = 0; i < maxCounter; i++) {
        processor.updateMmioData(mmioData, maxCounter);
        trace(trace, o, mmioData, hub.tx().number(), i);
      }
    }
  }

  public void handleRam(
    final MicroData microData, final State.TxState.Stamps moduleStamps, final int microStamp) {
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
      .cnA(Bytes.ofUnsignedInt(mmioData.cnA()))
      .cnB(Bytes.ofUnsignedInt(mmioData.cnB()))
      .cnC(Bytes.ofUnsignedInt(mmioData.cnC()))
      .indexA(Bytes.ofUnsignedInt(mmioData.indexA()))
      .indexB(Bytes.ofUnsignedInt(mmioData.indexB()))
      .indexC(Bytes.ofUnsignedInt(mmioData.indexC()))
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
      .microInstructionStamp(Bytes.of(mmioOperation.microStamp()))
      .microInstruction(Bytes.ofUnsignedInt(microData.microOp()))
      .contextSource(Bytes.ofUnsignedInt(microData.sourceContext()))
      .contextTarget(Bytes.ofUnsignedInt(microData.targetContext()))
      .isInit(mmioOperation.isInitCode())
      .sourceLimbOffset(microData.sourceLimbOffset())
      .targetLimbOffset(microData.targetLimbOffset())
      .sourceByteOffset(Bytes.of(microData.sourceByteOffset().toByte()))
      .targetByteOffset(Bytes.of(microData.targetByteOffset().toByte()))
      .size(Bytes.ofUnsignedInt(microData.size()))
      .erf(microData.isErf())
      .fast(microData.isFast())
      .stackValueHigh(unsignedBytesToBytes(mmioData.valHi()))
      .stackValueLow(unsignedBytesToBytes(mmioData.valLo()))
      .stackValueHiByte(Objects.requireNonNullElse(mmioData.valHi()[counter], UnsignedByte.ZERO))
      .stackValueLoByte(Objects.requireNonNullElse(mmioData.valLo()[counter], UnsignedByte.ZERO))
      .accValHi(unsignedBytesSubArrayToBytes(mmioData.valHi(), counter + 1))
      .accValLo(unsignedBytesSubArrayToBytes(mmioData.valLo(), counter + 1))
      .exoIsRom(microData.exoIsRom())
      .exoIsLog(microData.exoIsLog())
      .exoIsHash(microData.exoIsHash())
      .exoIsTxcd(microData.exoIsTxcd())
      .indexX(Bytes.ofUnsignedInt(mmioData.indexX()))
      .valX(unsignedBytesToBytes(mmioData.valX()))
      .byteX(Objects.requireNonNullElse(mmioData.valX()[counter], UnsignedByte.ZERO))
      .accX(unsignedBytesSubArrayToBytes(mmioData.valX(), counter + 1))
      .txNum(Bytes.ofUnsignedInt(txNum))
      .logNum(Bytes.ofUnsignedInt(mmioOperation.moduleStamps().log()))
      .bin1(mmioData.bin1())
      .bin2(mmioData.bin2())
      .bin3(mmioData.bin3())
      .bin4(mmioData.bin4())
      .bin5(mmioData.bin5())
      .acc1(mmioData.acc1())
      .acc2(mmioData.acc2())
      .acc3(mmioData.acc3())
      .acc4(mmioData.acc4())
      .acc5(mmioData.acc5())
      .acc6(mmioData.acc6())
      .pow2561(mmioData.pow2561())
      .pow2562(mmioData.pow2562())
      .counter(Bytes.ofUnsignedInt(counter))
      .validateRow();
  }
}