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
package net.consensys.linea.zktracer.exceptions;

import static net.consensys.linea.zktracer.module.hub.signals.TracedException.INVALID_CODE_PREFIX;
import static org.junit.jupiter.api.Assertions.assertEquals;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.junit.jupiter.api.Test;

public class InvalidCodePrefixExceptionTest {

  @Test
  void invalidCodePrefixExceptionViaContractCreationTest() {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
      .push("ef" + "00".repeat(31))
      .push(0)
      .op(OpCode.MSTORE)
      .push(32)
      .push(0)
      .push(0)
      .op(OpCode.CREATE);

    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    bytecodeRunner.run();

    assertEquals(
      INVALID_CODE_PREFIX,
      bytecodeRunner.getHub().previousTraceSection(2).commonValues.tracedException());
  }

  @Test
  void invalidCodePrefixExceptionViaReturnTest() {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        .push("ef" + "00".repeat(31))
        .push(0)
        .op(OpCode.MSTORE)
        .push(1)
        .push(0)
        .op(OpCode.RETURN);

    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    bytecodeRunner.run();

    assertEquals(
        INVALID_CODE_PREFIX,
        bytecodeRunner.getHub().previousTraceSection(2).commonValues.tracedException());
  }
}
