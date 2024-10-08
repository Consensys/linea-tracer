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
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import lombok.experimental.Accessors;
import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.zktracer.module.HexStringUtils;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Accessors(fluent = true)
public class ExpExtensiveTest {
  // Test vectors
  static final String P_1 = "f076b857fa9947c1f9ec558262c72704099ca8cd325566f73fb99238102ed171";
  static final String P_2 = "c809c9170ca6faec82d43ee6754dbad01d198ecae0823bac23ca30c7f0c9657d";

  static final String[] EVEN_BASES =
      IntStream.rangeClosed(1, 256).mapToObj(HexStringUtils::even).toArray(String[]::new);

  static final String[] SIMPLE_ODD_BASES =
      IntStream.rangeClosed(0, 31)
          .map(i -> i * 8) // Generates 0, 8, 16, ..., 248
          .mapToObj(HexStringUtils::odd)
          .toArray(String[]::new);

  static final String[] OTHER_ODD_BASES =
      Stream.of(SIMPLE_ODD_BASES).map(mask -> and(P_1, mask)).toArray(String[]::new);

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
      Stream.of(TINY_EXPONENTS, THRESHOLD_EXPONENTS).flatMap(Stream::of).toArray(String[]::new);

  static final String[] BASES =
      Stream.of(EVEN_BASES, SIMPLE_ODD_BASES, OTHER_ODD_BASES)
          .flatMap(Stream::of)
          .toArray(String[]::new);

  static final String[] EXPONENTS =
      Stream.of(COMPLEX_EXPONENTS, SPECIAL_EXPONENTS).flatMap(Stream::of).toArray(String[]::new);

  static final List<Arguments> EXPONENTS_LIST = Stream.of(EXPONENTS).map(Arguments::of).toList();

  // Note that flatMap(Stream::of) converts stream of String[] to stream of String

  // This is not an actual test, but just a utility to generate test cases
  // @Disabled
  @Test
  public void generateTestCases() {
    for (int i = 0; i < BASES.length; i++) {
      System.out.println(
          "@ParameterizedTest\n"
              + "@MethodSource(\"expTestForBaseSource\")\n"
              + "void expTestForBase_"
              + BASES[i]
              + "(String exponent) {\n"
              + "    expProgramOf(BASES["
              + i
              + "], exponent).run();\n"
              + "}\n");
    }
  }

  private static Stream<Arguments> expTestForBaseSource() {
    return EXPONENTS_LIST.stream();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe(
      String exponent) {
    expProgramOf(BASES[0], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffc(
      String exponent) {
    expProgramOf(BASES[1], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8(
      String exponent) {
    expProgramOf(BASES[2], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff0(
      String exponent) {
    expProgramOf(BASES[3], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe0(
      String exponent) {
    expProgramOf(BASES[4], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffc0(
      String exponent) {
    expProgramOf(BASES[5], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff80(
      String exponent) {
    expProgramOf(BASES[6], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff00(
      String exponent) {
    expProgramOf(BASES[7], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe00(
      String exponent) {
    expProgramOf(BASES[8], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffc00(
      String exponent) {
    expProgramOf(BASES[9], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff800(
      String exponent) {
    expProgramOf(BASES[10], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff000(
      String exponent) {
    expProgramOf(BASES[11], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe000(
      String exponent) {
    expProgramOf(BASES[12], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffc000(
      String exponent) {
    expProgramOf(BASES[13], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8000(
      String exponent) {
    expProgramOf(BASES[14], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff0000(
      String exponent) {
    expProgramOf(BASES[15], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe0000(
      String exponent) {
    expProgramOf(BASES[16], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffc0000(
      String exponent) {
    expProgramOf(BASES[17], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffffff80000(
      String exponent) {
    expProgramOf(BASES[18], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffffff00000(
      String exponent) {
    expProgramOf(BASES[19], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe00000(
      String exponent) {
    expProgramOf(BASES[20], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffc00000(
      String exponent) {
    expProgramOf(BASES[21], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffff800000(
      String exponent) {
    expProgramOf(BASES[22], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffff000000(
      String exponent) {
    expProgramOf(BASES[23], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffffe000000(
      String exponent) {
    expProgramOf(BASES[24], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffffc000000(
      String exponent) {
    expProgramOf(BASES[25], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffff8000000(
      String exponent) {
    expProgramOf(BASES[26], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffff0000000(
      String exponent) {
    expProgramOf(BASES[27], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffe0000000(
      String exponent) {
    expProgramOf(BASES[28], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffc0000000(
      String exponent) {
    expProgramOf(BASES[29], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffff80000000(
      String exponent) {
    expProgramOf(BASES[30], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffff00000000(
      String exponent) {
    expProgramOf(BASES[31], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffe00000000(
      String exponent) {
    expProgramOf(BASES[32], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffc00000000(
      String exponent) {
    expProgramOf(BASES[33], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffff800000000(
      String exponent) {
    expProgramOf(BASES[34], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffff000000000(
      String exponent) {
    expProgramOf(BASES[35], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffe000000000(
      String exponent) {
    expProgramOf(BASES[36], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffc000000000(
      String exponent) {
    expProgramOf(BASES[37], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffff8000000000(
      String exponent) {
    expProgramOf(BASES[38], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffff0000000000(
      String exponent) {
    expProgramOf(BASES[39], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffe0000000000(
      String exponent) {
    expProgramOf(BASES[40], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffc0000000000(
      String exponent) {
    expProgramOf(BASES[41], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffff80000000000(
      String exponent) {
    expProgramOf(BASES[42], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffff00000000000(
      String exponent) {
    expProgramOf(BASES[43], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffe00000000000(
      String exponent) {
    expProgramOf(BASES[44], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffc00000000000(
      String exponent) {
    expProgramOf(BASES[45], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffff800000000000(
      String exponent) {
    expProgramOf(BASES[46], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffff000000000000(
      String exponent) {
    expProgramOf(BASES[47], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffe000000000000(
      String exponent) {
    expProgramOf(BASES[48], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffc000000000000(
      String exponent) {
    expProgramOf(BASES[49], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffff8000000000000(
      String exponent) {
    expProgramOf(BASES[50], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffff0000000000000(
      String exponent) {
    expProgramOf(BASES[51], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffe0000000000000(
      String exponent) {
    expProgramOf(BASES[52], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffc0000000000000(
      String exponent) {
    expProgramOf(BASES[53], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffff80000000000000(
      String exponent) {
    expProgramOf(BASES[54], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffff00000000000000(
      String exponent) {
    expProgramOf(BASES[55], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffe00000000000000(
      String exponent) {
    expProgramOf(BASES[56], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffc00000000000000(
      String exponent) {
    expProgramOf(BASES[57], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffff800000000000000(
      String exponent) {
    expProgramOf(BASES[58], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffff000000000000000(
      String exponent) {
    expProgramOf(BASES[59], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffe000000000000000(
      String exponent) {
    expProgramOf(BASES[60], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffc000000000000000(
      String exponent) {
    expProgramOf(BASES[61], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffff8000000000000000(
      String exponent) {
    expProgramOf(BASES[62], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffff0000000000000000(
      String exponent) {
    expProgramOf(BASES[63], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffe0000000000000000(
      String exponent) {
    expProgramOf(BASES[64], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffc0000000000000000(
      String exponent) {
    expProgramOf(BASES[65], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffff80000000000000000(
      String exponent) {
    expProgramOf(BASES[66], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffff00000000000000000(
      String exponent) {
    expProgramOf(BASES[67], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffe00000000000000000(
      String exponent) {
    expProgramOf(BASES[68], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffc00000000000000000(
      String exponent) {
    expProgramOf(BASES[69], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffff800000000000000000(
      String exponent) {
    expProgramOf(BASES[70], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffff000000000000000000(
      String exponent) {
    expProgramOf(BASES[71], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffe000000000000000000(
      String exponent) {
    expProgramOf(BASES[72], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffffc000000000000000000(
      String exponent) {
    expProgramOf(BASES[73], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffff8000000000000000000(
      String exponent) {
    expProgramOf(BASES[74], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffff0000000000000000000(
      String exponent) {
    expProgramOf(BASES[75], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffe0000000000000000000(
      String exponent) {
    expProgramOf(BASES[76], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffc0000000000000000000(
      String exponent) {
    expProgramOf(BASES[77], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffff80000000000000000000(
      String exponent) {
    expProgramOf(BASES[78], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffff00000000000000000000(
      String exponent) {
    expProgramOf(BASES[79], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffe00000000000000000000(
      String exponent) {
    expProgramOf(BASES[80], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffffc00000000000000000000(
      String exponent) {
    expProgramOf(BASES[81], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffff800000000000000000000(
      String exponent) {
    expProgramOf(BASES[82], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffff000000000000000000000(
      String exponent) {
    expProgramOf(BASES[83], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffe000000000000000000000(
      String exponent) {
    expProgramOf(BASES[84], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffc000000000000000000000(
      String exponent) {
    expProgramOf(BASES[85], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffff8000000000000000000000(
      String exponent) {
    expProgramOf(BASES[86], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffff0000000000000000000000(
      String exponent) {
    expProgramOf(BASES[87], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffe0000000000000000000000(
      String exponent) {
    expProgramOf(BASES[88], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffffc0000000000000000000000(
      String exponent) {
    expProgramOf(BASES[89], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffff80000000000000000000000(
      String exponent) {
    expProgramOf(BASES[90], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffff00000000000000000000000(
      String exponent) {
    expProgramOf(BASES[91], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffe00000000000000000000000(
      String exponent) {
    expProgramOf(BASES[92], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffc00000000000000000000000(
      String exponent) {
    expProgramOf(BASES[93], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffff800000000000000000000000(
      String exponent) {
    expProgramOf(BASES[94], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffff000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[95], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffe000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[96], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffffc000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[97], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffff8000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[98], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffff0000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[99], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffe0000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[100], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffc0000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[101], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffff80000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[102], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffff00000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[103], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffe00000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[104], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffffc00000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[105], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffff800000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[106], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffff000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[107], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffe000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[108], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffc000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[109], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffff8000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[110], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffff0000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[111], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffe0000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[112], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffffc0000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[113], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffff80000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[114], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffff00000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[115], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffe00000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[116], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffc00000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[117], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffff800000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[118], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffff000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[119], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffe000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[120], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffffc000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[121], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffff8000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[122], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffff0000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[123], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffe0000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[124], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffc0000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[125], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffff80000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[126], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffff00000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[127], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffe00000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[128], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffffc00000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[129], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffff800000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[130], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffff000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[131], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffe000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[132], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffc000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[133], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffff8000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[134], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffff0000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[135], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffe0000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[136], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffffc0000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[137], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffff80000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[138], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffff00000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[139], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffe00000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[140], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffc00000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[141], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffff800000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[142], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffff000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[143], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffe000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[144], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffffc000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[145], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffff8000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[146], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffff0000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[147], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffe0000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[148], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffc0000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[149], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffff80000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[150], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffff00000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[151], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffe00000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[152], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffffc00000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[153], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffff800000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[154], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffff000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[155], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffe000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[156], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffc000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[157], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffff8000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[158], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffff0000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[159], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffe0000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[160], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffffc0000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[161], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffff80000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[162], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffff00000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[163], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffe00000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[164], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffc00000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[165], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffff800000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[166], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffff000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[167], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffe000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[168], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffffc000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[169], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffff8000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[170], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffff0000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[171], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffe0000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[172], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffc0000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[173], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffff80000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[174], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffff00000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[175], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffe00000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[176], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffffc00000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[177], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffff800000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[178], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffff000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[179], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffe000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[180], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffc000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[181], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffff8000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[182], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffff0000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[183], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffe0000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[184], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffffc0000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[185], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffff80000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[186], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffff00000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[187], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffe00000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[188], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffc00000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[189], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffff800000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[190], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffff000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[191], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffe000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[192], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffffc000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[193], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffff8000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[194], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffff0000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[195], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffe0000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[196], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffc0000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[197], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffff80000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[198], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffff00000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[199], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffe00000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[200], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffffc00000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[201], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffff800000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[202], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffff000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[203], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffe000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[204], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffc000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[205], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffff8000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[206], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffff0000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[207], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffe0000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[208], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffffc0000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[209], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffff80000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[210], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffff00000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[211], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffe00000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[212], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffc00000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[213], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffff800000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[214], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffff000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[215], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffe000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[216], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffffc000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[217], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffff8000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[218], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffff0000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[219], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffe0000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[220], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffc0000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[221], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffff80000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[222], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffff00000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[223], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffe00000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[224], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffffc00000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[225], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffff800000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[226], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffff000000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[227], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffe000000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[228], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffc000000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[229], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffff8000000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[230], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffff0000000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[231], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffe0000000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[232], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffffc0000000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[233], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffff80000000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[234], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffff00000000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[235], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffe00000000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[236], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffc00000000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[237], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffff800000000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[238], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffff000000000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[239], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffe000000000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[240], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fffc000000000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[241], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fff8000000000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[242], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fff0000000000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[243], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffe0000000000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[244], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffc0000000000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[245], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ff80000000000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[246], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ff00000000000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[247], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fe00000000000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[248], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fc00000000000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[249], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_f800000000000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[250], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_f000000000000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[251], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_e000000000000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[252], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_c000000000000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[253], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_8000000000000000000000000000000000000000000000000000000000000000(
      String exponent) {
    expProgramOf(BASES[254], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_0(String exponent) {
    expProgramOf(BASES[255], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff(
      String exponent) {
    expProgramOf(BASES[256], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff(
      String exponent) {
    expProgramOf(BASES[257], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff(
      String exponent) {
    expProgramOf(BASES[258], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffff(String exponent) {
    expProgramOf(BASES[259], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffff(String exponent) {
    expProgramOf(BASES[260], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffff(String exponent) {
    expProgramOf(BASES[261], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffff(String exponent) {
    expProgramOf(BASES[262], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffff(String exponent) {
    expProgramOf(BASES[263], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffff(String exponent) {
    expProgramOf(BASES[264], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffffff(String exponent) {
    expProgramOf(BASES[265], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffffff(String exponent) {
    expProgramOf(BASES[266], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffffff(String exponent) {
    expProgramOf(BASES[267], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffffff(String exponent) {
    expProgramOf(BASES[268], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffffff(String exponent) {
    expProgramOf(BASES[269], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffffff(String exponent) {
    expProgramOf(BASES[270], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffffff(String exponent) {
    expProgramOf(BASES[271], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffffff(String exponent) {
    expProgramOf(BASES[272], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffffff(String exponent) {
    expProgramOf(BASES[273], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffffff(String exponent) {
    expProgramOf(BASES[274], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffffff(String exponent) {
    expProgramOf(BASES[275], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffffff(String exponent) {
    expProgramOf(BASES[276], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffffff(String exponent) {
    expProgramOf(BASES[277], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffffff(String exponent) {
    expProgramOf(BASES[278], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffffff(String exponent) {
    expProgramOf(BASES[279], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffffff(String exponent) {
    expProgramOf(BASES[280], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffffff(String exponent) {
    expProgramOf(BASES[281], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffffff(String exponent) {
    expProgramOf(BASES[282], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffffff(String exponent) {
    expProgramOf(BASES[283], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffffff(String exponent) {
    expProgramOf(BASES[284], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffffff(String exponent) {
    expProgramOf(BASES[285], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ffff(String exponent) {
    expProgramOf(BASES[286], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ff(String exponent) {
    expProgramOf(BASES[287], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_f076b857fa9947c1f9ec558262c72704099ca8cd325566f73fb99238102ed171(
      String exponent) {
    expProgramOf(BASES[288], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_76b857fa9947c1f9ec558262c72704099ca8cd325566f73fb99238102ed171(
      String exponent) {
    expProgramOf(BASES[289], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_b857fa9947c1f9ec558262c72704099ca8cd325566f73fb99238102ed171(
      String exponent) {
    expProgramOf(BASES[290], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_57fa9947c1f9ec558262c72704099ca8cd325566f73fb99238102ed171(String exponent) {
    expProgramOf(BASES[291], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_fa9947c1f9ec558262c72704099ca8cd325566f73fb99238102ed171(String exponent) {
    expProgramOf(BASES[292], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_9947c1f9ec558262c72704099ca8cd325566f73fb99238102ed171(String exponent) {
    expProgramOf(BASES[293], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_47c1f9ec558262c72704099ca8cd325566f73fb99238102ed171(String exponent) {
    expProgramOf(BASES[294], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_c1f9ec558262c72704099ca8cd325566f73fb99238102ed171(String exponent) {
    expProgramOf(BASES[295], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_f9ec558262c72704099ca8cd325566f73fb99238102ed171(String exponent) {
    expProgramOf(BASES[296], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_ec558262c72704099ca8cd325566f73fb99238102ed171(String exponent) {
    expProgramOf(BASES[297], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_558262c72704099ca8cd325566f73fb99238102ed171(String exponent) {
    expProgramOf(BASES[298], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_8262c72704099ca8cd325566f73fb99238102ed171(String exponent) {
    expProgramOf(BASES[299], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_62c72704099ca8cd325566f73fb99238102ed171(String exponent) {
    expProgramOf(BASES[300], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_c72704099ca8cd325566f73fb99238102ed171(String exponent) {
    expProgramOf(BASES[301], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_2704099ca8cd325566f73fb99238102ed171(String exponent) {
    expProgramOf(BASES[302], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_4099ca8cd325566f73fb99238102ed171(String exponent) {
    expProgramOf(BASES[303], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_99ca8cd325566f73fb99238102ed171(String exponent) {
    expProgramOf(BASES[304], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_9ca8cd325566f73fb99238102ed171(String exponent) {
    expProgramOf(BASES[305], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_a8cd325566f73fb99238102ed171(String exponent) {
    expProgramOf(BASES[306], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_cd325566f73fb99238102ed171(String exponent) {
    expProgramOf(BASES[307], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_325566f73fb99238102ed171(String exponent) {
    expProgramOf(BASES[308], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_5566f73fb99238102ed171(String exponent) {
    expProgramOf(BASES[309], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_66f73fb99238102ed171(String exponent) {
    expProgramOf(BASES[310], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_f73fb99238102ed171(String exponent) {
    expProgramOf(BASES[311], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_3fb99238102ed171(String exponent) {
    expProgramOf(BASES[312], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_b99238102ed171(String exponent) {
    expProgramOf(BASES[313], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_9238102ed171(String exponent) {
    expProgramOf(BASES[314], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_38102ed171(String exponent) {
    expProgramOf(BASES[315], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_102ed171(String exponent) {
    expProgramOf(BASES[316], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_2ed171(String exponent) {
    expProgramOf(BASES[317], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_d171(String exponent) {
    expProgramOf(BASES[318], exponent).run();
  }

  @ParameterizedTest
  @MethodSource("expTestForBaseSource")
  void expTestForBase_71(String exponent) {
    expProgramOf(BASES[319], exponent).run();
  }

  // Disabled tests due to length of time to run
  @Disabled
  @ParameterizedTest
  @MethodSource("expWithEvenBaseAndComplexExponentTestSource")
  public void expWithEvenBaseAndComplexExponentTest(String base, String exponent) {
    expProgramOf(base, exponent).run();
  }

  static Stream<Arguments> expWithEvenBaseAndComplexExponentTestSource() {
    return generateSource(EVEN_BASES, COMPLEX_EXPONENTS);
  }

  @Disabled
  @ParameterizedTest
  @MethodSource("expWithEvenBaseAndSpecialExponentTestSource")
  public void expWithEvenBaseAndSpecialExponentTest(String base, String exponent) {
    expProgramOf(base, exponent).run();
  }

  static Stream<Arguments> expWithEvenBaseAndSpecialExponentTestSource() {
    return generateSource(EVEN_BASES, SPECIAL_EXPONENTS);
  }

  @Disabled
  @ParameterizedTest
  @MethodSource("expWithSimpleOddBaseAndComplexExponentTestSource")
  public void expWithSimpleOddBaseAndComplexExponentTest(String base, String exponent) {
    expProgramOf(base, exponent).run();
  }

  static Stream<Arguments> expWithSimpleOddBaseAndComplexExponentTestSource() {
    return generateSource(SIMPLE_ODD_BASES, COMPLEX_EXPONENTS);
  }

  @Disabled
  @ParameterizedTest
  @MethodSource("expWithSimpleOddBaseAndSpecialExponentTestSource")
  public void expWithSimpleOddBaseAndSpecialExponentTest(String base, String exponent) {
    expProgramOf(base, exponent).run();
  }

  static Stream<Arguments> expWithSimpleOddBaseAndSpecialExponentTestSource() {
    return generateSource(SIMPLE_ODD_BASES, SPECIAL_EXPONENTS);
  }

  @Disabled
  @ParameterizedTest
  @MethodSource("expWithOtherOddBaseAndComplexExponentTestSource")
  public void expWithOtherOddBaseAndComplexExponentTest(String base, String exponent) {
    expProgramOf(base, exponent).run();
  }

  static Stream<Arguments> expWithOtherOddBaseAndComplexExponentTestSource() {
    return generateSource(OTHER_ODD_BASES, COMPLEX_EXPONENTS);
  }

  @Disabled
  @ParameterizedTest
  @MethodSource("expWithOtherOddBaseAndSpecialExponentTestSource")
  public void expWithOtherOddBaseAndSpecialExponentTest(String base, String exponent) {
    expProgramOf(base, exponent).run();
  }

  static Stream<Arguments> expWithOtherOddBaseAndSpecialExponentTestSource() {
    return generateSource(OTHER_ODD_BASES, SPECIAL_EXPONENTS);
  }

  // Support methods
  private BytecodeRunner expProgramOf(String base, String exponent) {
    return BytecodeRunner.of(
        BytecodeCompiler.newProgram().push(exponent).push(base).op(OpCode.EXP).compile());
  }

  private BytecodeRunner expProgramOf(String base) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();
    for (String exponent : EXPONENTS) {
      program.push(exponent).push(base).op(OpCode.EXP);
    }
    return BytecodeRunner.of(program.compile());
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
