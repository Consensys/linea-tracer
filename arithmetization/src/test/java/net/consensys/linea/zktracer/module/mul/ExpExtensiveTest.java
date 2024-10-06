package net.consensys.linea.zktracer.module.mul;

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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import lombok.experimental.Accessors;
import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Accessors(fluent = true)
public class ExpExtensiveTest {
  // Test vectors
  static final String P_1 = "F076B857FA9947C1F9EC558262C72704099CA8CD325566F73FB99238102ED171";
  static final String P_2 = "C809C9170CA6FAEC82D43EE6754DBAD01D198ECAE0823BAC23CA30C7F0C9657D";

  static final String[] EVEN_BASE =
      IntStream.rangeClosed(1, 256).mapToObj(ExpExtensiveTest::evenBase).toArray(String[]::new);

  static final String[] SIMPLE_ODD_BASE =
      IntStream.rangeClosed(0, 32)
          .map(i -> i * 8) // Generates 0, 8, 16, ..., 256
          .mapToObj(ExpExtensiveTest::oddBase)
          .toArray(String[]::new);

  static final String[] OTHER_ODD_BASE =
      Stream.of(SIMPLE_ODD_BASE).map(mask -> and(P_1, mask)).toArray(String[]::new);

  static final String[] COMPLEX_EXPONENTS =
      IntStream.rangeClosed(0, 256).mapToObj(n -> rightShift(P_2, n)).toArray(String[]::new);

  static final String[] TINY_EXPONENTS =
      Stream.concat(
              IntStream.rangeClosed(0, 257) // Generates numbers 0 to 257
                  .mapToObj(BigInteger::valueOf),
              Stream.of(
                  BigInteger.valueOf(65535), BigInteger.valueOf(65536), BigInteger.valueOf(65537)))
          .map(bigInteger -> bigInteger.toString(16))
          .toArray(String[]::new);

  static final String[] THRESHOLD_EXPONENTS =
      Stream.of(
              BigInteger.ONE.shiftLeft(128).subtract(BigInteger.ONE), // (1 << 128) - 1
              BigInteger.ONE.shiftLeft(128), // (1 << 128)
              BigInteger.ONE.shiftLeft(128).add(BigInteger.ONE), // (1 << 128) + 1
              BigInteger.ONE.shiftLeft(256).subtract(BigInteger.ONE) // (1 << 256) - 1
              )
          .map(bigInteger -> bigInteger.toString(16))
          .toArray(String[]::new);

  static final String[] SPECIAL_EXPONENTS =
      Stream.concat(Arrays.stream(TINY_EXPONENTS), Arrays.stream(THRESHOLD_EXPONENTS))
          .toArray(String[]::new);

  // Tests
  @ParameterizedTest
  @MethodSource("expWithEvenBaseAndTinyExponentTestSource")
  public void expWithEvenBaseAndTinyExponentTest(String base, String exponent) {
    BytecodeRunner.of(
            BytecodeCompiler.newProgram().push(exponent).push(base).op(OpCode.EXP).compile())
        .run();
  }

  static Stream<Arguments> expWithEvenBaseAndTinyExponentTestSource() {
    List<Arguments> arguments = new ArrayList<>();
    // Example of inputs
    for (String base : EVEN_BASE) {
      arguments.add(Arguments.of(base, TINY_EXPONENTS[69]));
    }
    return arguments.stream();
  }

  // Support tests
  @ParameterizedTest
  @MethodSource("checkInputsSource")
  public void checkInputs(String value, String length, String description) {}

  static Stream<Arguments> checkInputsSource() {
    List<Arguments> arguments = new ArrayList<>();
    for (String value : EVEN_BASE) {
      arguments.add(Arguments.of(value, value.length() / 2, "Even base"));
    }
    for (String value : SIMPLE_ODD_BASE) {
      arguments.add(Arguments.of(value, value.length() / 2, "Simple odd base"));
    }
    for (String value : OTHER_ODD_BASE) {
      arguments.add(Arguments.of(value, value.length() / 2, "Other odd base"));
    }
    for (String value : COMPLEX_EXPONENTS) {
      arguments.add(Arguments.of(value, value.length() / 2, "Complex exponent"));
    }
    for (String value : TINY_EXPONENTS) {
      arguments.add(Arguments.of(value, value.length() / 2, "Tiny exponent"));
    }
    for (String value : THRESHOLD_EXPONENTS) {
      arguments.add(Arguments.of(value, value.length() / 2, "Threshold exponent"));
    }
    return arguments.stream();
  }

  // Support methods
  private static String evenBase(int n) {
    return new BigInteger("1".repeat(256 - n) + "0".repeat(n), 2).toString(16);
  }

  private static String oddBase(int n) {
    return new BigInteger("0".repeat(256 - n) + "1".repeat(n), 2).toString(16);
  }

  private static String rightShift(String value, int n) {
    // TODO: Likely no need to format it as 64 hex characters
    return String.format("%064X", new BigInteger(value, 16).shiftRight(n));
  }

  private static String and(String value, String mask) {
    // TODO: Likely no need to format it as 64 hex characters
    return String.format("%064X", new BigInteger(value, 16).and(new BigInteger(mask, 16)));
  }
}
