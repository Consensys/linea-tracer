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

package net.consensys.linea.zktracer.module.bin;

import net.consensys.linea.zktracer.container.stacked.set.StackedSet;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.ModuleTrace;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.evm.frame.MessageFrame;

/** Implementation of a {@link Module} for addition/subtraction. */
public class Bin implements Module {
  private static final byte NOT = (byte) 0x19;

  private int stamp = 0;

  /** A set of the operations to trace */
  private final StackedSet<BinOperation> operations = new StackedSet<>();

  @Override
  public String jsonKey() {
    return "bin";
  }

  @Override
  public void enterTransaction() {
    this.operations.enter();
  }

  @Override
  public void popTransaction() {
    this.operations.pop();
  }

  @Override
  public void tracePreOpcode(MessageFrame frame) {
    final OpCode opCode = OpCode.of(frame.getCurrentOperation().getOpcode());
    final Bytes32 arg1 = Bytes32.leftPad(frame.getStackItem(0));
    final Bytes32 arg2 =
        isOpCodeNot(opCode) ? Bytes32.ZERO : Bytes32.leftPad(frame.getStackItem(1));

    this.operations.add(new BinOperation(opCode, arg1, arg2));
  }

  @Override
  public int lineCount() {
    return 0;
  }

  @Override
  public ModuleTrace commit() {
    return null;
  }

  private boolean isOpCodeNot(final OpCode opCode) {
    return opCode.byteValue() == NOT;
  }
}
