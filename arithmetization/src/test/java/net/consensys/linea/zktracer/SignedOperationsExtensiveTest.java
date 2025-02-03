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

package net.consensys.linea.zktracer;

import static org.identityconnectors.common.ByteUtil.randomBytes;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class SignedOperationsExtensiveTest {
  // See https://github.com/Consensys/linea-tracer/issues/1182 for documentation

  static final String ZERO = "00".repeat(32);
  static final String ONE = "00".repeat(31) + "01";

  // 10^3 < SMALL_1 < SMALL_2 < 10^9
  static final String SMALL_1 = "66" + randomBytes(1, 1);
  static final String SMALL_2 = "66" + randomBytes(2, 2);

  static final String LARGE_1 = randomBytes(16, 3);
  static final String LARGE_2 = "01" + randomBytes(16, 4);

  static final String RND_POS = "7f" + randomBytes(31, 5); // < 0x80 ...
  static final String RND_NEG = "81" + randomBytes(31, 6); // > 0x80 ...

  static final String NEG_ONE = "ff".repeat(32);

  static String[] VALUES = {
    ZERO, ONE, SMALL_1, SMALL_2, LARGE_1, LARGE_2, RND_POS, RND_NEG, NEG_ONE
  };

  @ParameterizedTest
  @MethodSource("signedComparisonsModDivTestSource")
  void signedComparisonsModDivTest(OpCode opCode, String a, String b) {
    BytecodeCompiler program = BytecodeCompiler.newProgram().push(b).push(a).op(opCode);
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    bytecodeRunner.run();
  }

  static Stream<Arguments> signedComparisonsModDivTestSource() {
    List<Arguments> arguments = new ArrayList<>();
    for (OpCode opCode : List.of(OpCode.SLT, OpCode.SGT, OpCode.SMOD, OpCode.SDIV)) {
      for (String a : VALUES) {
        for (String b : VALUES) {
          arguments.add(Arguments.of(opCode, a, b));
        }
      }
    }
    return arguments.stream();
  }

  @ParameterizedTest
  @MethodSource("signExtendTestSource")
  void signExtendTest(String b, int x) {
    BytecodeCompiler program = BytecodeCompiler.newProgram().push(x).push(b).op(OpCode.SIGNEXTEND);
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    bytecodeRunner.run();
  }

  static Stream<Arguments> signExtendTestSource() {
    List<Arguments> arguments = new ArrayList<>();
    for (String firstByte : List.of("00", "ff", "7f", "81", "ff")) {
      for (int nTrailingBytes : List.of(0, 15, 16, 31)) {
        String b = firstByte + randomBytes(nTrailingBytes, 7);
        arguments.add(Arguments.of(b, b.length() / 2 - 1));
      }
    }
    return arguments.stream();
  }

  // Support method
  private static String randomBytes(int n, long seed) {
    Random random = new Random(seed);
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < n; i++) {
      sb.append(String.format("%02x", new BigInteger(8, random).byteValue()));
    }
    return sb.toString();
  }
}
