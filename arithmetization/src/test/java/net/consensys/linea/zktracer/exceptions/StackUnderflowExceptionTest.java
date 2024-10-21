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

import static net.consensys.linea.zktracer.module.hub.signals.TracedException.STACK_UNDERFLOW;
import static net.consensys.linea.zktracer.opcode.OpCodes.opCodeToOpCodeDataMap;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.opcode.OpCodeData;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class StackUnderflowExceptionTest {

  @ParameterizedTest
  @MethodSource("stackUnderflowExceptionSource")
  void stackUnderflowExceptionTest(
      OpCode opCode, int nPushes, boolean triggersStackUnderflowExceptions) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();
    for (int i = 0; i < nPushes; i++) {
      program.push(0);
    }
    program.op(opCode);
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    bytecodeRunner.run();

    if (triggersStackUnderflowExceptions) {
      assertEquals(
          STACK_UNDERFLOW,
          bytecodeRunner.getHub().currentTraceSection().commonValues.tracedException());
    }
  }

  static Stream<Arguments> stackUnderflowExceptionSource() {
    List<Arguments> arguments = new ArrayList<>();
    for (OpCodeData opCodeData : opCodeToOpCodeDataMap.values()) {
      OpCode opCode = opCodeData.mnemonic();
      int delta = opCodeData.stackSettings().delta();
      for (int nPushes = 0; nPushes <= delta; nPushes++) {
        arguments.add(Arguments.of(opCode, nPushes, nPushes < delta));
      }
    }
    return arguments.stream();
  }
}
