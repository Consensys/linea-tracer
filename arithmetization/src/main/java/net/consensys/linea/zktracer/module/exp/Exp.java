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

package net.consensys.linea.zktracer.module.exp;

import java.nio.MappedByteBuffer;
import java.util.List;

import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.container.stacked.list.StackedList;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.internal.Words;

public class Exp implements Module {
  /** A list of the operations to trace */
  private final StackedList<ExpChunk> chunks = new StackedList<>();

  private final Hub hub;

  public Exp(Hub hub) {
    this.hub = hub;
  }

  @Override
  public String moduleKey() {
    return "EXP";
  }

  @Override
  public void enterTransaction() {
    this.chunks.enter();
  }

  @Override
  public void popTransaction() {
    this.chunks.pop();
  }

  @Override
  public int lineCount() {
    return this.chunks.lineCount();
  }

  @Override
  public List<ColumnHeader> columnsHeaders() {
    return Trace.headers(this.lineCount());
  }

  @Override
  public void tracePreOpcode(MessageFrame frame) {
    if (hub.pch().exceptions().none() && hub.pch().aborts().none()) {
      OpCode opCode = OpCode.of(frame.getCurrentOperation().getOpcode());
      if (opCode.equals(OpCode.EXP)) {
        this.chunks.add(new ExpChunk(frame, true, false, false, true, false));
        this.chunks.add(new ExpChunk(frame, false, true, false, true, false));
        this.chunks.add(new ExpChunk(frame, false, false, true, true, false));
      } else if (opCode.isCall()) {
        Address target = Words.toAddress(frame.getStackItem(1));
        if (target.equals(Address.MODEXP)) {
          this.chunks.add(new ExpChunk(frame, true, false, false, false, true));
          this.chunks.add(new ExpChunk(frame, false, true, false, false, true));
          this.chunks.add(new ExpChunk(frame, false, false, true, false, true));
        }
      }
    }
  }

  @Override
  public void commit(List<MappedByteBuffer> buffers) {
    final Trace trace = new Trace(buffers);
    for (int i = 0; i < this.chunks.size(); i++) {
      this.chunks.get(i).trace(i + 1, trace);
    }
  }
}
