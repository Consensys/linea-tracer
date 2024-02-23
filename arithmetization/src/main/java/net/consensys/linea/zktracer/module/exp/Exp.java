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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.container.stacked.list.StackedList;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.ExpLogCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.oob.ModExpLogCall;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.internal.Words;

@Slf4j
@RequiredArgsConstructor
public class Exp implements Module {
  /** A list of the operations to trace */
  private final StackedList<ExpChunk> chunks = new StackedList<>();

  private final Hub hub;
  private final Wcp wcp;

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
    final OpCode opCode = OpCode.of(frame.getCurrentOperation().getOpcode());
    final Exceptions exceptions = hub.pch().exceptions();

    if (opCode == OpCode.EXP) {
      if (!exceptions.stackUnderflow()) {
        this.chunks.add(ExpLogChunk.fromMessageFrame(this.wcp, frame));
      }
    } else if (exceptions.none() && hub.pch().aborts().none()) {
      if (opCode.isCall()) {
        Address target = Words.toAddress(frame.getStackItem(1));
        if (target.equals(Address.MODEXP)) {
          final ExpChunk modexpOperation = ModExpLogChunk.fromFrame(this.wcp, frame);
          if (modexpOperation != null) {
            this.chunks.add(modexpOperation);
          }
        }
      }
    }
  }

  public void callExpLogCall(final ExpLogCall c) {
    this.chunks.add(ExpLogChunk.fromExpLogCall(this.wcp, c));
  }

  public void callModExpLogCall(final ModExpLogCall c) {
    this.chunks.add(ModExpLogChunk.fromExpLogCall(this.wcp, c));
  }

  @Override
  public void commit(List<MappedByteBuffer> buffers) {
    final Trace trace = new Trace(buffers);

    for (int i = 0; i < this.chunks.size(); i++) {
      ExpChunk expChunk = this.chunks.get(i);
      expChunk.traceComputation(i + 1, trace);
      expChunk.traceMacro(i + 1, trace);
      expChunk.tracePreprocessing(i + 1, trace);
    }
  }
}
