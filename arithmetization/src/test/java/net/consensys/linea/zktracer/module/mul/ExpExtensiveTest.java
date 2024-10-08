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

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffffc0000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[161]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffff80000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[162]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffff00000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[163]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffe00000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[164]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffc00000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[165]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffff800000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[166]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffff000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[167]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffe000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[168]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffffc000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[169]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffff8000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[170]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffff0000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[171]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffe0000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[172]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffc0000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[173]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffff80000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[174]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffff00000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[175]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffe00000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[176]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffffc00000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[177]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffff800000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[178]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffff000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[179]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffe000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[180]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffc000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[181]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffff8000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[182]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffff0000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[183]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffe0000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[184]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffffc0000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[185]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffff80000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[186]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffff00000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[187]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffe00000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[188]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffc00000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[189]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffff800000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[190]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffff000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[191]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffe000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[192]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffffc000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[193]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffff8000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[194]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffff0000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[195]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffe0000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[196]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffc0000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[197]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffff80000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[198]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffff00000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[199]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffe00000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[200]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffffc00000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[201]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffff800000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[202]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffff000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[203]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffe000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[204]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffc000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[205]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffff8000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[206]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffff0000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[207]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffe0000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[208]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffffc0000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[209]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffff80000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[210]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffff00000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[211]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffe00000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[212]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffc00000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[213]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffff800000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[214]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffff000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[215]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffe000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[216]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffffc000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[217]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffff8000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[218]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffff0000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[219]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffe0000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[220]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffc0000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[221]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffff80000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[222]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffff00000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[223]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffe00000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[224]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffffc00000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[225]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffff800000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[226]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffff000000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[227]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffe000000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[228]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffc000000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[229]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffff8000000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[230]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffff0000000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[231]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffe0000000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[232]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffffc0000000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[233]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffff80000000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[234]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffff00000000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[235]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffe00000000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[236]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffc00000000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[237]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffff800000000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[238]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffff000000000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[239]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffe000000000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[240]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fffc000000000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[241]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fff8000000000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[242]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fff0000000000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[243]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffe0000000000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[244]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffc0000000000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[245]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ff80000000000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[246]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ff00000000000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[247]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fe00000000000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[248]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fc00000000000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[249]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_f800000000000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[250]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_f000000000000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[251]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_e000000000000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[252]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_c000000000000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[253]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_8000000000000000000000000000000000000000000000000000000000000000() {
    return expDynamicTestGeneratorForBase(BASES[254]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_0() {
    return expDynamicTestGeneratorForBase(BASES[255]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff() {
    return expDynamicTestGeneratorForBase(BASES[256]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff() {
    return expDynamicTestGeneratorForBase(BASES[257]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff() {
    return expDynamicTestGeneratorForBase(BASES[258]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffffff() {
    return expDynamicTestGeneratorForBase(BASES[259]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffffff() {
    return expDynamicTestGeneratorForBase(BASES[260]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffffff() {
    return expDynamicTestGeneratorForBase(BASES[261]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffffff() {
    return expDynamicTestGeneratorForBase(BASES[262]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffffff() {
    return expDynamicTestGeneratorForBase(BASES[263]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffffff() {
    return expDynamicTestGeneratorForBase(BASES[264]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffffff() {
    return expDynamicTestGeneratorForBase(BASES[265]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffffff() {
    return expDynamicTestGeneratorForBase(BASES[266]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffffff() {
    return expDynamicTestGeneratorForBase(BASES[267]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffffff() {
    return expDynamicTestGeneratorForBase(BASES[268]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_ffffffffffffffffffffffffffffffffffffff() {
    return expDynamicTestGeneratorForBase(BASES[269]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_ffffffffffffffffffffffffffffffffffff() {
    return expDynamicTestGeneratorForBase(BASES[270]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_ffffffffffffffffffffffffffffffffff() {
    return expDynamicTestGeneratorForBase(BASES[271]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_ffffffffffffffffffffffffffffffff() {
    return expDynamicTestGeneratorForBase(BASES[272]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_ffffffffffffffffffffffffffffff() {
    return expDynamicTestGeneratorForBase(BASES[273]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_ffffffffffffffffffffffffffff() {
    return expDynamicTestGeneratorForBase(BASES[274]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_ffffffffffffffffffffffffff() {
    return expDynamicTestGeneratorForBase(BASES[275]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_ffffffffffffffffffffffff() {
    return expDynamicTestGeneratorForBase(BASES[276]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_ffffffffffffffffffffff() {
    return expDynamicTestGeneratorForBase(BASES[277]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_ffffffffffffffffffff() {
    return expDynamicTestGeneratorForBase(BASES[278]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_ffffffffffffffffff() {
    return expDynamicTestGeneratorForBase(BASES[279]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_ffffffffffffffff() {
    return expDynamicTestGeneratorForBase(BASES[280]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_ffffffffffffff() {
    return expDynamicTestGeneratorForBase(BASES[281]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_ffffffffffff() {
    return expDynamicTestGeneratorForBase(BASES[282]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_ffffffffff() {
    return expDynamicTestGeneratorForBase(BASES[283]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_ffffffff() {
    return expDynamicTestGeneratorForBase(BASES[284]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_ffffff() {
    return expDynamicTestGeneratorForBase(BASES[285]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_ffff() {
    return expDynamicTestGeneratorForBase(BASES[286]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_ff() {
    return expDynamicTestGeneratorForBase(BASES[287]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_f076b857fa9947c1f9ec558262c72704099ca8cd325566f73fb99238102ed171() {
    return expDynamicTestGeneratorForBase(BASES[288]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_76b857fa9947c1f9ec558262c72704099ca8cd325566f73fb99238102ed171() {
    return expDynamicTestGeneratorForBase(BASES[289]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_b857fa9947c1f9ec558262c72704099ca8cd325566f73fb99238102ed171() {
    return expDynamicTestGeneratorForBase(BASES[290]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_57fa9947c1f9ec558262c72704099ca8cd325566f73fb99238102ed171() {
    return expDynamicTestGeneratorForBase(BASES[291]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_fa9947c1f9ec558262c72704099ca8cd325566f73fb99238102ed171() {
    return expDynamicTestGeneratorForBase(BASES[292]);
  }

  @TestFactory
  Stream<DynamicTest>
      expDynamicTestForBase_9947c1f9ec558262c72704099ca8cd325566f73fb99238102ed171() {
    return expDynamicTestGeneratorForBase(BASES[293]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_47c1f9ec558262c72704099ca8cd325566f73fb99238102ed171() {
    return expDynamicTestGeneratorForBase(BASES[294]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_c1f9ec558262c72704099ca8cd325566f73fb99238102ed171() {
    return expDynamicTestGeneratorForBase(BASES[295]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_f9ec558262c72704099ca8cd325566f73fb99238102ed171() {
    return expDynamicTestGeneratorForBase(BASES[296]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_ec558262c72704099ca8cd325566f73fb99238102ed171() {
    return expDynamicTestGeneratorForBase(BASES[297]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_558262c72704099ca8cd325566f73fb99238102ed171() {
    return expDynamicTestGeneratorForBase(BASES[298]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_8262c72704099ca8cd325566f73fb99238102ed171() {
    return expDynamicTestGeneratorForBase(BASES[299]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_62c72704099ca8cd325566f73fb99238102ed171() {
    return expDynamicTestGeneratorForBase(BASES[300]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_c72704099ca8cd325566f73fb99238102ed171() {
    return expDynamicTestGeneratorForBase(BASES[301]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_2704099ca8cd325566f73fb99238102ed171() {
    return expDynamicTestGeneratorForBase(BASES[302]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_4099ca8cd325566f73fb99238102ed171() {
    return expDynamicTestGeneratorForBase(BASES[303]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_99ca8cd325566f73fb99238102ed171() {
    return expDynamicTestGeneratorForBase(BASES[304]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_9ca8cd325566f73fb99238102ed171() {
    return expDynamicTestGeneratorForBase(BASES[305]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_a8cd325566f73fb99238102ed171() {
    return expDynamicTestGeneratorForBase(BASES[306]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_cd325566f73fb99238102ed171() {
    return expDynamicTestGeneratorForBase(BASES[307]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_325566f73fb99238102ed171() {
    return expDynamicTestGeneratorForBase(BASES[308]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_5566f73fb99238102ed171() {
    return expDynamicTestGeneratorForBase(BASES[309]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_66f73fb99238102ed171() {
    return expDynamicTestGeneratorForBase(BASES[310]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_f73fb99238102ed171() {
    return expDynamicTestGeneratorForBase(BASES[311]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_3fb99238102ed171() {
    return expDynamicTestGeneratorForBase(BASES[312]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_b99238102ed171() {
    return expDynamicTestGeneratorForBase(BASES[313]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_9238102ed171() {
    return expDynamicTestGeneratorForBase(BASES[314]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_38102ed171() {
    return expDynamicTestGeneratorForBase(BASES[315]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_102ed171() {
    return expDynamicTestGeneratorForBase(BASES[316]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_2ed171() {
    return expDynamicTestGeneratorForBase(BASES[317]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_d171() {
    return expDynamicTestGeneratorForBase(BASES[318]);
  }

  @TestFactory
  Stream<DynamicTest> expDynamicTestForBase_71() {
    return expDynamicTestGeneratorForBase(BASES[319]);
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
