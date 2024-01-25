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

package net.consensys.linea.zktracer.module.mmu;

import static net.consensys.linea.zktracer.module.mmu.MicroDataProcessor.*;

import java.nio.MappedByteBuffer;
import java.util.List;

import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.container.stacked.list.StackedList;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.mmio.Mmio;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.runtime.callstack.CallStack;
import net.consensys.linea.zktracer.runtime.microdata.MicroData;
import net.consensys.linea.zktracer.runtime.stack.StackOperation;

public class Mmu implements Module {
  private final StackedList<MmuOperation> state = new StackedList<>();
  private int ramStamp;
  private final int microStamp = 0;
  private boolean isMicro;
  private final MicroDataProcessor microDataProcessor;
  private final Hub hub;
  private final Mmio mmio;
  private final CallStack callStack;

  public Mmu(final Hub hub, final Mmio mmio, final CallStack callStack) {
    this.hub = hub;
    this.callStack = callStack;
    this.mmio = mmio;
    this.microDataProcessor = new MicroDataProcessor(microStamp);
  }

  @Override
  public String moduleKey() {
    return "MMU";
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
    final Trace trace = new Trace(buffers);

    for (MmuOperation o : this.state) {
      o.trace(o.microData(), callStack, trace);
    }
  }

  /**
   * TODO: should be called from the hub.
   *
   * @param opCode
   * @param stackOps
   * @param callStack
   */
  public void handleRam(
      final OpCode opCode, final List<StackOperation> stackOps, final CallStack callStack) {
    MicroData microData = microDataProcessor.dispatchOpCode(opCode, stackOps, callStack);

    mmio.handleRam(microData, hub.state().stamps(), microStamp);

    this.state.add(new MmuOperation(microData, callStack, microDataProcessor, ramStamp, isMicro));
  }
}
