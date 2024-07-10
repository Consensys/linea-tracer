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

import static net.consensys.linea.zktracer.types.Utils.addOffsetToHexString;

import java.math.BigInteger;
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

  // NOTE: the bytecode we propose will at time use the following offsets (unless pcNew is large)
  // bytecode:
  // - PUSH1 b // 0, 1, ..., jumpiConditionByteOffset + 1
  // - PUSHX pcNew // offsets: jumpiConditionByteOffset + 2, jumpiConditionByteOffset + 3
  // - JUMPI // offset: jumpiConditionByteOffset + 4
  // - INVALID // offset: jumpiConditionByteOffset + 5
  // - JUMPDEST // offset: jumpiConditionByteOffset + 6
  // - PUSH1 0x5b // offsets: jumpiConditionByteOffset + 7, jumpiConditionByteOffset + 8 <- 0x5b is
  // byte value of JUMPDEST
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
            "deadbeefdeadcafedeadbeefdeadcafe",
            "ffffffffffffffffffffffffffffffff",
            "0100000000000000000000000000000001",
            "cafefeedcafebabecafefeedcafebabecafefeedcafebabecafefeedcafebabe",
            "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff");
    for (String jumpiCondition : jumpiConditions) {
      jumpiScenarios.addAll(provideJumpiScenarioForJumpiCondition(jumpiCondition));
    }
    return jumpiScenarios.stream();
  }

  private static List<Arguments> provideJumpiScenarioForJumpiCondition(String jumpiCondition) {
    int jumpiConditionByteSize =
        jumpiCondition.equals("0")
            ? 1
            : (int) Math.ceil((double) new BigInteger(jumpiCondition, 16).bitLength() / 8);
    int jumpiConditionByteOffset = jumpiConditionByteSize - 1;
    return List.of(
        Arguments.of(
            "jumpOntoJumpDestTest",
            jumpiCondition,
            addOffsetToHexString(jumpiConditionByteOffset, "6")),
        Arguments.of(
            "jumpOntoInvalidTest",
            jumpiCondition,
            addOffsetToHexString(jumpiConditionByteOffset, "5")),
        Arguments.of(
            "jumpOntoJumpDestByteOwnedBySomePush",
            jumpiCondition,
            addOffsetToHexString(jumpiConditionByteOffset, "8")),
        // NOTE: in the cases below we add
        Arguments.of(
            "jumpOutOfBoundsSmall",
            jumpiCondition,
            addOffsetToHexString(jumpiConditionByteOffset, "ff")),
        Arguments.of(
            "jumpOutOfBoundsMaxUint128",
            jumpiCondition,
            addOffsetToHexString(jumpiConditionByteOffset, "ffffffffffffffffffffffffffffffff")),
        Arguments.of(
            "jumpOutOfBoundsTwoToThe128",
            jumpiCondition,
            addOffsetToHexString(jumpiConditionByteOffset, "0100000000000000000000000000000000")),
        Arguments.of(
            "jumpOutOfBoundsTwoToThe128Plus4",
            jumpiCondition,
            addOffsetToHexString(jumpiConditionByteOffset, "0100000000000000000000000000000004")),
        Arguments.of(
            "jumpOutOfBoundsMaxUint256",
            jumpiCondition,
            addOffsetToHexString(
                jumpiConditionByteOffset,
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff")));
  }
}
