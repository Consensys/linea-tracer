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

import java.nio.MappedByteBuffer;
import java.util.List;

import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.container.stacked.list.StackedList;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.State;
import net.consensys.linea.zktracer.module.mmu.MmuData;
import net.consensys.linea.zktracer.module.romLex.RomLex;
import net.consensys.linea.zktracer.runtime.callstack.CallFrameType;
import net.consensys.linea.zktracer.runtime.callstack.CallStack;

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
      int maxCounter = maxCounter(o.mmuData());

      MmioData mmioData = processor.dispatchMmioData();
      for (int i = 0; i < maxCounter; i++) {
        processor.updateMmioData(mmioData, maxCounter);
        //        o.trace(trace, mmioData, hub.tx().number(), i);
      }
    }
  }

  public void handleRam(
      final MmuData mmuData, final State.TxState.Stamps moduleStamps, final int microStamp) {
    if (mmuData.microOp() == 0) {
      return;
    }

    MmioDataProcessor mmioDataProcessor =
        new MmioDataProcessor(romLex, mmuData, new CallStackReader(callStack));
    boolean isInitCode = callStack.current().type() == CallFrameType.INIT_CODE;

    this.state.add(
        new MmioOperation(mmuData, mmioDataProcessor, moduleStamps, microStamp, isInitCode));
  }
}
