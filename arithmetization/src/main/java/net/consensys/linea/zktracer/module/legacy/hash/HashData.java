/*
 * Copyright ConsenSys AG.
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

package net.consensys.linea.zktracer.module.legacy.hash;

import static net.consensys.linea.zktracer.module.Util.roundedUpDivision;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.internal.Words;

@RequiredArgsConstructor
public class HashData implements Module {
  private final Hub hub;
  private final Deque<Integer> state = new ArrayDeque<>();

  @Override
  public String moduleKey() {
    return "PUB_HASH";
  }

  @Override
  public void enterTransaction() {
    this.state.push(0);
  }

  @Override
  public void popTransaction() {
    this.state.pop();
  }

  @Override
  public int lineCount() {
    return this.state.stream().mapToInt(x -> x).sum();
  }

  @Override
  public List<ColumnHeader> columnsHeaders() {
    throw new IllegalStateException("should never be called");
  }

  private void add(int x) {
    this.state.push(this.state.pop() + x);
  }

  @Override
  public void tracePreOpcode(MessageFrame frame) {
    final OpCode opCode = hub.opCode();
    if (opCode == OpCode.CREATE2 || opCode == OpCode.SHA3) {
      if (this.hub.pch().exceptions().none()) {
        final long size = Words.clampedToLong(frame.getStackItem(1));
        final int limbCount = (int) roundedUpDivision(size, 16);
        this.add(limbCount);
      }
    }
  }
}
