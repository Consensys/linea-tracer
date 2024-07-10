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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.testing.BytecodeCompiler;
import net.consensys.linea.zktracer.testing.BytecodeRunner;
import net.consensys.linea.zktracer.testing.EvmExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@ExtendWith(EvmExtension.class)
public class JumpiTests {

  // NOTE: the bytecode we propose will at time use the following offsets (unless one of the two
  // initial push
  // is large!) bytecode:
  // - PUSH1 b // 0, 1
  // - PUSHX pcNew // offsets: 2, 3
  // - JUMP // offset: 4
  // - INVALID // offset: 5
  // - JUMPDEST // offset: 6
  // - PUSH1 0x5b // offsets: 7, 8 <- 0x5b is the byte value of JUMPDEST
  @ParameterizedTest
  @MethodSource("provideJumpiScenario")
  void testCodeForJumpiScenario(String description, String jumpiCondition, String pcNew) {
    BytecodeRunner.of(
            BytecodeCompiler.newProgram()
                .push(jumpiCondition)
                .push(pcNew)
                .op(OpCode.JUMP)
                .op(OpCode.INVALID)
                .op(OpCode.JUMPDEST)
                .push(OpCode.JUMPDEST.byteValue()) // false JUMPDEST
                .compile())
        .run();
  }

  private static Stream<Arguments> provideJumpiScenario() {
    List<Arguments> jumpiScenarios = new ArrayList<>();
    List<String> jumpiConditions =
        List.of(
            "0",
            "1",
            "0xdeadbeefdeadcafedeadbeefdeadcafe",
            "0xffffffffffffffffffffffffffffffff",
            "0x0100000000000000000000000000000001",
            "0xcafefeedcafebabecafefeedcafebabecafefeedcafebabecafefeedcafebabe",
            "0xffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
    for (String jumpiCondition : jumpiConditions) {
      jumpiScenarios.addAll(provideJumpiScenarioForJumpiCondition(jumpiCondition));
    }
    return jumpiScenarios.stream();
  }

  private static List<Arguments> provideJumpiScenarioForJumpiCondition(String jumpiCondition) {
    return List.of(
        Arguments.of("jumpOntoJumpDestTest", jumpiCondition, "6"),
        Arguments.of("jumpOntoInvalidTest", jumpiCondition, "5"),
        Arguments.of("jumpOntoJumpDestByteOwnedBySomePush", jumpiCondition, "8"),
        Arguments.of("jumpOutOfBoundsSmall", jumpiCondition, "0xff"),
        Arguments.of(
            "jumpOutOfBoundsMaxUint128", jumpiCondition, "0xffffffffffffffffffffffffffffffff"),
        Arguments.of(
            "jumpOutOfBoundsTwoToThe128", jumpiCondition, "0x0100000000000000000000000000000000"),
        Arguments.of(
            "jumpOutOfBoundsTwoToThe128Plus4",
            jumpiCondition,
            "0x0100000000000000000000000000000004"),
        Arguments.of(
            "jumpOutOfBoundsMaxUint256",
            jumpiCondition,
            "0xffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"));
  }
}
