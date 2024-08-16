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
package net.consensys.linea.zktracer.module.mxp;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.junit.jupiter.api.Test;

public class SeveralKeccaks {
  @Test
  public void testMul() {
    BytecodeRunner.of(BytecodeCompiler.newProgram().push(32).push(7).op(OpCode.MUL).compile())
        .run();
  }

  @Test
  void testSeveralKeccaks() {
    BytecodeRunner.of(
            BytecodeCompiler.newProgram()
                .push(0)
                .push(0)
                .op(OpCode.SHA3)
                .op(OpCode.POP)
                .push(64)
                .push(13)
                .op(OpCode.SHA3)
                .op(OpCode.POP)
                .push(11)
                .push(75)
                .op(OpCode.SHA3)
                .op(OpCode.POP)
                .push(32)
                .push(32)
                .op(OpCode.SHA3)
                .op(OpCode.POP)
                .compile())
        .run();
  }
}
