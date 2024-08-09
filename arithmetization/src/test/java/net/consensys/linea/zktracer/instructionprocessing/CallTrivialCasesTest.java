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
package net.consensys.linea.zktracer.instructionprocessing;

import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.testing.BytecodeCompiler;
import net.consensys.linea.zktracer.testing.BytecodeRunner;
import org.apache.tuweni.bytes.Bytes;
import org.junit.jupiter.api.Test;

public class CallTrivialCasesTest {
  @Test
  void eoaCallScenarioTest() {
    final Bytes bytecode =
        BytecodeCompiler.newProgram()
            .push(0)
            .push(0)
            .push(0)
            .push(0)
            .push(0)
            .push(0x0add7e55)
            .push(0xff)
            .op(OpCode.CALL)
            .op(OpCode.POP)
            .compile();
    BytecodeRunner.of(bytecode).run();
  }

  @Test
  void eoaCallWithCallDataAndReturnDataCapacityTest() {
    final Bytes bytecode =
            BytecodeCompiler.newProgram()
                    .push(0x31)
                    .push(0x11)
                    .push(0x22)
                    .push(0x90)
                    .push(0)
                    .push(0x0add7e55)
                    .push(0xff)
                    .op(OpCode.CALL)
                    .op(OpCode.POP)
                    .compile();
    BytecodeRunner.of(bytecode).run();
  }

  @Test
  void eoaCallScenarioTestZeroCalleeGas() {
    final Bytes bytecode =
        BytecodeCompiler.newProgram()
            .push(0)
            .push(0)
            .push(0)
            .push(0)
            .push(0)
            .push(0x0add7e55)
            .push(0)
            .op(OpCode.CALL)
            .op(OpCode.POP)
            .compile();
    BytecodeRunner.of(bytecode).run();
  }
}
