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

import java.nio.MappedByteBuffer;
import java.util.List;

import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.container.stacked.list.StackedList;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.mmio.Mmio;
import net.consensys.linea.zktracer.module.mmu.values.HubToMmuValues;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.runtime.callstack.CallStack;

public class Mmu implements Module {
  private final StackedList<MmuOperation> mmuOperationList = new StackedList<>();
  private boolean isMicro;
  private final Hub hub;
  private final Euc euc;
  private final Wcp wcp;
  private final CallStack callStack;

  public Mmu(
      final Hub hub, final Euc euc, final Wcp wcp, final Mmio mmio, final CallStack callStack) {
    this.hub = hub;
    this.euc = euc;
    this.wcp = wcp;
    this.callStack = callStack;
  }

  @Override
  public String moduleKey() {
    return "MMU";
  }

  @Override
  public void enterTransaction() {
    this.mmuOperationList.enter();
  }

  @Override
  public void popTransaction() {
    this.mmuOperationList.pop();
  }

  @Override
  public int lineCount() {
    return this.mmuOperationList.lineCount();
  }

  @Override
  public List<ColumnHeader> columnsHeaders() {
    return Trace.headers(this.lineCount());
  }

  @Override
  public void commit(List<MappedByteBuffer> buffers) {
    final Trace trace = new Trace(buffers);

    int mmuStamp = 0;
    int mmioStamp = 0;

    for (MmuOperation mmuOp : this.mmuOperationList) {
      mmuStamp += 1;
      mmuOp.trace(mmuStamp, mmioStamp, trace);
      mmioStamp += mmuOp.mmuData().numberMmioInstructions();
    }
  }

  /**
   * TODO: should be called from the hub.
   *
   * @param opCode
   * @param stackOps
   * @param callStack
   */
  public void call(final HubToMmuValues hubToMmuValues, final CallStack callStack) {
    MmuData mmuData = new MmuData();
    mmuData.hubToMmuValues(hubToMmuValues);

    MmuInstructions mmuInstructions = new MmuInstructions(euc, wcp);
    mmuData = mmuInstructions.compute(mmuData);

    // TODO: Deal with calling of the MMIO later.
    //    mmio.handleRam(mmuData, hub.state().stamps(), microStamp);

    this.mmuOperationList.add(new MmuOperation(mmuData, callStack));
  }
}
