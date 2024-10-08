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
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
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

  // Note that flatMap(Stream::of) converts stream of String[] to stream of String

  // This is not an actual test, but just a utility to generate test cases
  @Disabled
  @Test
  public void generateTestCases() {
    for (int i = 0; i < BASES.length; i++) {
      System.out.println(
          "@TestFactory\n"
              + "Stream<DynamicTest> expDynamicTestForBase_"
              + BASES[i]
              + "() {\n"
              + "    return expDynamicTestGeneratorForBase(BASES["
              + i
              + "]);\n"
              + "}\n");
    }
    System.out.println("test cases generated");
  }

  // For a given base, create a dynamic test for each exponent
  Stream<DynamicTest> expDynamicTestGeneratorForBase(String base) {
    return Stream.of(EXPONENTS)
        .map(
            exponent ->
                DynamicTest.dynamicTest(
                    base + ", " + exponent,
                    () -> {
                      expProgramOf(base, exponent).run();
                    }));
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe() {
    return expDynamicTestGeneratorForBase(BASES[0]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffc() {
    return expDynamicTestGeneratorForBase(BASES[1]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8() {
    return expDynamicTestGeneratorForBase(BASES[2]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff0() {
    return expDynamicTestGeneratorForBase(BASES[3]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe0() {
    return expDynamicTestGeneratorForBase(BASES[4]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffc0() {
    return expDynamicTestGeneratorForBase(BASES[5]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff80() {
    return expDynamicTestGeneratorForBase(BASES[6]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff00() {
    return expDynamicTestGeneratorForBase(BASES[7]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe00() {
    return expDynamicTestGeneratorForBase(BASES[8]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffc00() {
    return expDynamicTestGeneratorForBase(BASES[9]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff800() {
    return expDynamicTestGeneratorForBase(BASES[10]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff000() {
    return expDynamicTestGeneratorForBase(BASES[11]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe000() {
    return expDynamicTestGeneratorForBase(BASES[12]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffc000() {
    return expDynamicTestGeneratorForBase(BASES[13]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff8000() {
    return expDynamicTestGeneratorForBase(BASES[14]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff0000() {
    return expDynamicTestGeneratorForBase(BASES[15]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe0000() {
    return expDynamicTestGeneratorForBase(BASES[16]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffc0000() {
    return expDynamicTestGeneratorForBase(BASES[17]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffffff80000() {
    return expDynamicTestGeneratorForBase(BASES[18]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffffff00000() {
    return expDynamicTestGeneratorForBase(BASES[19]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe00000() {
    return expDynamicTestGeneratorForBase(BASES[20]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffc00000() {
    return expDynamicTestGeneratorForBase(BASES[21]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffff800000() {
    return expDynamicTestGeneratorForBase(BASES[22]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffff000000() {
    return expDynamicTestGeneratorForBase(BASES[23]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffffe000000() {
    return expDynamicTestGeneratorForBase(BASES[24]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffffc000000() {
    return expDynamicTestGeneratorForBase(BASES[25]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffff8000000() {
    return expDynamicTestGeneratorForBase(BASES[26]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffff0000000() {
    return expDynamicTestGeneratorForBase(BASES[27]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffe0000000() {
    return expDynamicTestGeneratorForBase(BASES[28]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffc0000000() {
    return expDynamicTestGeneratorForBase(BASES[29]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffff80000000() {
    return expDynamicTestGeneratorForBase(BASES[30]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffff00000000() {
    return expDynamicTestGeneratorForBase(BASES[31]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffe00000000() {
    return expDynamicTestGeneratorForBase(BASES[32]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffffc00000000() {
    return expDynamicTestGeneratorForBase(BASES[33]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffff800000000() {
    return expDynamicTestGeneratorForBase(BASES[34]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffff000000000() {
    return expDynamicTestGeneratorForBase(BASES[35]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffe000000000() {
    return expDynamicTestGeneratorForBase(BASES[36]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffc000000000() {
    return expDynamicTestGeneratorForBase(BASES[37]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffff8000000000() {
    return expDynamicTestGeneratorForBase(BASES[38]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffff0000000000() {
    return expDynamicTestGeneratorForBase(BASES[39]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffe0000000000() {
    return expDynamicTestGeneratorForBase(BASES[40]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffffc0000000000() {
    return expDynamicTestGeneratorForBase(BASES[41]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffff80000000000() {
    return expDynamicTestGeneratorForBase(BASES[42]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffff00000000000() {
    return expDynamicTestGeneratorForBase(BASES[43]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffe00000000000() {
    return expDynamicTestGeneratorForBase(BASES[44]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffc00000000000() {
    return expDynamicTestGeneratorForBase(BASES[45]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffff800000000000() {
    return expDynamicTestGeneratorForBase(BASES[46]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffff000000000000() {
    return expDynamicTestGeneratorForBase(BASES[47]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffe000000000000() {
    return expDynamicTestGeneratorForBase(BASES[48]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffffc000000000000() {
    return expDynamicTestGeneratorForBase(BASES[49]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffff8000000000000() {
    return expDynamicTestGeneratorForBase(BASES[50]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffff0000000000000() {
    return expDynamicTestGeneratorForBase(BASES[51]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffe0000000000000() {
    return expDynamicTestGeneratorForBase(BASES[52]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffc0000000000000() {
    return expDynamicTestGeneratorForBase(BASES[53]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffff80000000000000() {
    return expDynamicTestGeneratorForBase(BASES[54]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffff00000000000000() {
    return expDynamicTestGeneratorForBase(BASES[55]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffe00000000000000() {
    return expDynamicTestGeneratorForBase(BASES[56]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffffc00000000000000() {
    return expDynamicTestGeneratorForBase(BASES[57]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffff800000000000000() {
    return expDynamicTestGeneratorForBase(BASES[58]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffff000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[59]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffe000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[60]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffc000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[61]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffff8000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[62]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffff0000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[63]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffe0000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[64]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffffc0000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[65]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffff80000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[66]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffff00000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[67]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffe00000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[68]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffc00000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[69]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffff800000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[70]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffff000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[71]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffe000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[72]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffffc000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[73]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffff8000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[74]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffff0000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[75]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffe0000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[76]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffc0000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[77]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffff80000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[78]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffff00000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[79]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffe00000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[80]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffffc00000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[81]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffff800000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[82]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffff000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[83]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffe000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[84]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffc000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[85]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffff8000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[86]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffff0000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[87]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffe0000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[88]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffffc0000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[89]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffff80000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[90]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffff00000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[91]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffe00000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[92]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffc00000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[93]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffff800000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[94]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffff000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[95]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffe000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[96]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffffc000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[97]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffff8000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[98]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffff0000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[99]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffe0000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[100]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffc0000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[101]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffff80000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[102]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffff00000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[103]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffe00000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[104]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffffc00000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[105]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffff800000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[106]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffff000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[107]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffe000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[108]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffc000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[109]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffff8000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[110]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffff0000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[111]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffe0000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[112]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffffc0000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[113]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffff80000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[114]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffff00000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[115]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffe00000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[116]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffc00000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[117]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffff800000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[118]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffff000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[119]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffe000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[120]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffffc000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[121]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffff8000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[122]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffff0000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[123]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffe0000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[124]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffc0000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[125]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffff80000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[126]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffff00000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[127]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffe00000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[128]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffffc00000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[129]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffff800000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[130]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffff000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[131]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffe000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[132]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffc000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[133]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffff8000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[134]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffff0000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[135]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffe0000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[136]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffffc0000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[137]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffff80000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[138]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffff00000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[139]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffe00000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[140]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffc00000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[141]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffff800000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[142]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffff000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[143]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffe000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[144]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffffc000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[145]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffff8000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[146]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffff0000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[147]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffe0000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[148]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffc0000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[149]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffff80000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[150]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffff00000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[151]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffe00000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[152]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffffc00000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[153]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffff800000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[154]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffff000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[155]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffe000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[156]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffc000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[157]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffff8000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[158]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffff0000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[159]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffe0000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[160]);
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
