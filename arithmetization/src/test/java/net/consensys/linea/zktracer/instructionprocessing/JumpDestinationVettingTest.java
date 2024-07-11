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

import java.util.Random;

import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.testing.BytecodeCompiler;
import net.consensys.linea.zktracer.testing.BytecodeRunner;
import net.consensys.linea.zktracer.testing.EvmExtension;
import org.apache.tuweni.bytes.Bytes;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@ExtendWith(EvmExtension.class)
public class JumpDestinationVettingTest {
  final Random random = new Random(1);
  final int N_JUMPS = 10;

  @ParameterizedTest
  @ValueSource(ints = {1, 5, 15, 16, 17, 20, 31, 32})
  void jumpDestinationVettingTest(int positionOfDeceptiveJumpDest) {
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
        String deceptivePush = generateDeceptivePush(positionOfDeceptiveJumpDest);
        program.push(deceptivePush);
      }
    }
    Bytes bytecode = program.compile();
    System.out.println(bytecode.toHexString());
    BytecodeRunner.of(bytecode).run();
  }

  public String generateDeceptivePush(int positionOfDeceptiveJumpDest) {
    if (positionOfDeceptiveJumpDest < 1 || positionOfDeceptiveJumpDest > 32) {
      throw new IllegalArgumentException("positionOfDeceptiveJumpDest must be between 1 and 32");
    }
    StringBuilder deceptivePush = new StringBuilder();
    for (int i = 1; i <= 31; i++) {
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
}
