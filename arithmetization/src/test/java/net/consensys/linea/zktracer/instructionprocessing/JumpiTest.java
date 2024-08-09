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
import org.apache.tuweni.bytes.Bytes;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class JumpiTest {

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
  void jumpiScenarioTest(String description, String jumpiCondition, String pcNew) {
    final Bytes bytecode =
        BytecodeCompiler.newProgram()
            .push(jumpiCondition)
            .push(pcNew)
            .op(OpCode.JUMP)
            .op(OpCode.INVALID)
            .op(OpCode.JUMPDEST)
            .push(OpCode.JUMPDEST.byteValue()) // false JUMPDEST
            .compile();
    System.out.println(bytecode.toHexString());
    BytecodeRunner.of(bytecode).run();
  }

  private static Stream<Arguments> provideJumpiScenario() {
    List<Arguments> jumpiScenarios = new ArrayList<>();
    List<String> jumpiConditions =
        List.of(
            "0",
            "1",
            "1a30", // very small random integer
            "deadbeefdeadcafedeadbeefdeadcafe", // some random Bytes16
            "ffffffffffffffffffffffffffffffff", // max Bytes16 (hi = 0x00)
            "0100000000000000000000000000000000", // 256 ^16     (hi = 0x01, lo = 0x00)
            "0100000000000000000000000000000001", // 256 ^16 + 1 (hi = 0x01, lo = 0x01)
            "cafefeedcafebabecafefeedcafebabecafefeedcafebabecafefeedcafebabe", // some random
            // Bytes32
            "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" // max Bytes32
            );
    for (String jumpiCondition : jumpiConditions) {
      jumpiScenarios.addAll(provideJumpiScenarioForJumpiCondition(jumpiCondition));
    }
    return jumpiScenarios.stream();
  }

  private static List<Arguments> provideJumpiScenarioForJumpiCondition(String jumpiCondition) {
    final int jumpiConditionByteSize =
        jumpiCondition.equals("0")
            ? 1
            : (int) Math.ceil((double) new BigInteger(jumpiCondition, 16).bitLength() / 8);
    final int jumpiConditionByteOffset = jumpiConditionByteSize - 1;
    return List.of(
        Arguments.of(
            "jumpiOntoValidJumpDestination",
            jumpiCondition,
            addOffsetToHexString(jumpiConditionByteOffset, "6")),
        Arguments.of(
            "jumpiOntoINVALID",
            jumpiCondition,
            addOffsetToHexString(jumpiConditionByteOffset, "5")),
        Arguments.of(
            "jumpiOntoJumpDestByteOwnedBySomePush",
            jumpiCondition,
            addOffsetToHexString(jumpiConditionByteOffset, "8")),
        Arguments.of(
            "jumpiOutOfBoundsSmall",
            jumpiCondition,
            addOffsetToHexString(jumpiConditionByteOffset, "ff")),
        Arguments.of(
            "jumpiOutOfBoundsMaxUint128",
            jumpiCondition,
            addOffsetToHexString(jumpiConditionByteOffset, "ffffffffffffffffffffffffffffffff")),
        Arguments.of(
            "jumpiOutOfBoundsTwoToThe128",
            jumpiCondition,
            addOffsetToHexString(jumpiConditionByteOffset, "0100000000000000000000000000000000")),
        Arguments.of(
            "jumpiOutOfBoundsTwoToThe128Plus4",
            jumpiCondition,
            addOffsetToHexString(jumpiConditionByteOffset, "0100000000000000000000000000000004")),
        Arguments.of(
            "jumpiOutOfBoundsMaxUint256",
            jumpiCondition,
            addOffsetToHexString(
                jumpiConditionByteOffset,
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff")));
                // TODO: @lorenzo
                //  this returns "1000000000000000000000000000000000000000000000000000000000000001e", a 65 character long hex string
                //  this in turn blows up the push instruction
  }
}