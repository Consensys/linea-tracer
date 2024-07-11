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

import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;

import java.math.BigInteger;
import java.util.Random;
import java.util.stream.Stream;

import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.testing.BytecodeCompiler;
import net.consensys.linea.zktracer.testing.BytecodeRunner;
import net.consensys.linea.zktracer.testing.EvmExtension;
import org.apache.tuweni.bytes.Bytes;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@ExtendWith(EvmExtension.class)
public class JumpDestinationVettingTest {
  final Random random = new Random(1);
  final int N_JUMPS = 10;

  @ParameterizedTest
  @MethodSource("jumpDestinationVettingCases")
  void jumpDestinationVettingTest(
      int positionOfDeceptiveJumpDest, OpCode pushK, int pushKArgumentLength) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();
    int nTotalInvalid = 0;
    for (int i = 0; i < N_JUMPS; i++) {
      int nPartialInvalid = random.nextInt(10) + 1;
      nTotalInvalid += nPartialInvalid;
      // NOTE: the last attempted JUMP "points" to a deceptive JUMPDEST owned by a PUSH32
      program
          .push(3 + 4 * i + nTotalInvalid + (i < N_JUMPS - 1 ? 0 : positionOfDeceptiveJumpDest))
          .op(OpCode.JUMP)
          .immediate(Bytes.repeat(OpCode.INVALID.byteValue(), nPartialInvalid));
      if (i < N_JUMPS - 1) {
        program.op(OpCode.JUMPDEST);
      } else {
        String pushKBytecode = Bytes.of(pushK.byteValue()).toHexString().substring(2);
        String deceptivePush =
            generateDeceptivePush(positionOfDeceptiveJumpDest, pushKArgumentLength);
        program.immediate(bigIntegerToBytes(new BigInteger(pushKBytecode + deceptivePush, 16)));
      }
    }
    Bytes bytecode = program.compile();
    System.out.println(bytecode.toHexString());
    BytecodeRunner.of(bytecode).run();
  }

  public String generateDeceptivePush(int positionOfDeceptiveJumpDest, int pushKArgumentLength) {
    if (pushKArgumentLength == 0) {
      return "";
    }
    if (positionOfDeceptiveJumpDest < 1 || positionOfDeceptiveJumpDest > pushKArgumentLength) {
      throw new IllegalArgumentException(
          "positionOfDeceptiveJumpDest must be between 1 and pushKArgumentLength");
    }
    StringBuilder deceptivePush = new StringBuilder();
    for (int i = 1; i <= pushKArgumentLength; i++) {
      if (i == positionOfDeceptiveJumpDest) {
        deceptivePush.append("5b"); // deceptive JUMPDEST "pointed" by the last JUMP
      } else {
        deceptivePush.append(
            random.nextInt(2) == 0
                ? "60"
                : "5b"); // PUSH1, JUMPDEST (not "pointed" by the last JUMP)
      }
    }
    return deceptivePush.toString();
  }

  static Stream<Arguments> jumpDestinationVettingCases() {
    return Stream.of(
        Arguments.of(1, OpCode.PUSH1, 1),
        Arguments.of(1, OpCode.PUSH2, 2),
        Arguments.of(2, OpCode.PUSH2, 2),
        Arguments.of(1, OpCode.PUSH32, 32),
        Arguments.of(2, OpCode.PUSH32, 32),
        Arguments.of(15, OpCode.PUSH32, 32),
        Arguments.of(16, OpCode.PUSH32, 32),
        Arguments.of(17, OpCode.PUSH32, 32),
        Arguments.of(20, OpCode.PUSH32, 32),
        Arguments.of(31, OpCode.PUSH32, 32),
        Arguments.of(32, OpCode.PUSH32, 32),
        Arguments.of(1, OpCode.PUSH7, 5), // generic "incomplete" push
        Arguments.of(1, OpCode.PUSH7, 0), // minimal edge case
        Arguments.of(1, OpCode.PUSH7, 1), // the bytecode terminates in PUSH7 B1
        Arguments.of(1, OpCode.PUSH16, 0), // minimal edge case + missing 16 bytes
        Arguments.of(1, OpCode.PUSH16, 1), // minimal edge case + missing 16 bytes
        Arguments.of(1, OpCode.PUSH17, 0), // minimal edge case + missing 16 bytes
        Arguments.of(1, OpCode.PUSH17, 1), // minimal edge case + missing 16 bytes
        Arguments.of(1, OpCode.PUSH23, 7), // differs by 16
        Arguments.of(1, OpCode.PUSH32, 0), // minimal edge case, the final opcode is PUSH32
        Arguments.of(1, OpCode.PUSH32, 1), // the bytecode terminates in PUSH32 B1
        Arguments.of(1, OpCode.PUSH32, 31)); // missing a single byte
  }
}
