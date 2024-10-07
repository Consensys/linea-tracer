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

import static net.consensys.linea.zktracer.module.HexStringUtils.and;
import static net.consensys.linea.zktracer.module.HexStringUtils.rightShift;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import lombok.experimental.Accessors;
import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.zktracer.module.HexStringUtils;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Accessors(fluent = true)
public class ExpExtensiveTest {
  // Test vectors
  static final String P_1 = "f076b857fa9947c1f9ec558262c72704099ca8cd325566f73fb99238102ed171";
  static final String P_2 = "c809c9170ca6faec82d43ee6754dbad01d198ecae0823bac23ca30c7f0c9657d";

  static final String[] EVEN_BASE =
      IntStream.rangeClosed(1, 256).mapToObj(HexStringUtils::even).toArray(String[]::new);

  static final String[] SIMPLE_ODD_BASE =
      IntStream.rangeClosed(0, 32)
          .map(i -> i * 8) // Generates 0, 8, 16, ..., 256
          .mapToObj(HexStringUtils::odd)
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
  @MethodSource("expWithEvenBaseAndSpecialExponentTestSource")
  public void expWithEvenBaseAndSpecialExponentTest(String base, String exponent) {
    expProgramOf(base, exponent).run();
  }

  static Stream<Arguments> expWithEvenBaseAndSpecialExponentTestSource() {
    return generateSource(EVEN_BASE, SPECIAL_EXPONENTS);
  }

  @ParameterizedTest
  @MethodSource("expWithSimpleOddBaseAndSpecialExponentTestSource")
  public void expWithSimpleOddBaseAndSpecialExponentTest(String base, String exponent) {
    expProgramOf(base, exponent).run();
  }

  static Stream<Arguments> expWithSimpleOddBaseAndSpecialExponentTestSource() {
    return generateSource(SIMPLE_ODD_BASE, SPECIAL_EXPONENTS);
  }

  @ParameterizedTest
  @MethodSource("expWithSimpleOddBaseAndComplexExponentTestSource")
  public void expWithSimpleOddBaseAndComplexExponentTest(String base, String exponent) {
    expProgramOf(base, exponent).run();
  }

  static Stream<Arguments> expWithSimpleOddBaseAndComplexExponentTestSource() {
    return generateSource(SIMPLE_ODD_BASE, COMPLEX_EXPONENTS);
  }

  @ParameterizedTest
  @MethodSource("expWithOtherOddBaseAndComplexExponentTestSource")
  public void expWithOtherOddBaseAndComplexExponentTest(String base, String exponent) {
    expProgramOf(base, exponent).run();
  }

  static Stream<Arguments> expWithOtherOddBaseAndComplexExponentTestSource() {
    return generateSource(OTHER_ODD_BASE, COMPLEX_EXPONENTS);
  }

  // Support methods
  private BytecodeRunner expProgramOf(String base, String exponent) {
    return BytecodeRunner.of(
        BytecodeCompiler.newProgram().push(exponent).push(base).op(OpCode.EXP).compile());
  }

  static Stream<Arguments> generateSource(String[] bases, String[] exponents) {
    List<Arguments> arguments = new ArrayList<>();
    for (String base : bases) {
      for (String exponent : exponents) {
        arguments.add(Arguments.of(base, exponent));
      }
    }
    return arguments.stream();
  }
}
