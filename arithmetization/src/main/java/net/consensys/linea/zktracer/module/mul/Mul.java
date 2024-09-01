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

package net.consensys.linea.zktracer.module.mul;

import java.nio.MappedByteBuffer;
import java.util.List;

import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.container.module.StatelessModule;
import net.consensys.linea.zktracer.container.stacked.StackedSet;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.evm.frame.MessageFrame;

@RequiredArgsConstructor
public class Mul implements StatelessModule<MulOperation> {
  private final Hub hub;

  private final StackedSet<MulOperation> operations = new StackedSet<>();

  @Override
  public String moduleKey() {
    return "MUL";
  }

  @Override
  public void tracePreOpcode(MessageFrame frame) {
    final OpCode opCode = this.hub.opCode();
    final Bytes32 arg1 = Bytes32.leftPad(frame.getStackItem(0));
    final Bytes32 arg2 = Bytes32.leftPad(frame.getStackItem(1));

    operations.add(new MulOperation(opCode, arg1, arg2));
  }

  @Override
  public StackedSet<MulOperation> operations() {
    return operations;
  }

  @Override
  public int lineCount() {
    return 1 + operations.lineCount();
  }

  @Override
  public List<ColumnHeader> columnsHeaders() {
    return Trace.headers(this.lineCount());
  }

  @Override
  public void commit(List<MappedByteBuffer> buffers) {
    final Trace trace = new Trace(buffers);

    int stamp = 0;
    for (MulOperation op : operations.getAll()) {
      op.trace(trace, ++stamp);
    }
    (new MulOperation(OpCode.EXP, Bytes32.ZERO, Bytes32.ZERO)).trace(trace, stamp + 1);
  }
}
