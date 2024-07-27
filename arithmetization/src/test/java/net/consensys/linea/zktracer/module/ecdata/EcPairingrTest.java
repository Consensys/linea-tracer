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

package net.consensys.linea.zktracer.module.ecdata;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.testing.BytecodeCompiler;
import net.consensys.linea.zktracer.testing.BytecodeRunner;
import org.apache.tuweni.bytes.Bytes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.util.Preconditions;

public class EcPairingrTest {
  // https://github.com/Consensys/linea-arithmetization/issues/822

  // SMALL POINTS
  static List<Arguments> smallPointsOutOfRange;
  static List<Arguments> smallPointsNotOnC1;
  static List<Arguments> smallPointsOnC1;
  static final Arguments smallPointInfinity = Arguments.of("0", "0");

  // LARGE POINTS
  static List<Arguments> largePointsOutOfRange;
  static List<Arguments> largePointsNotOnC2;
  static List<Arguments> largePointsNotOnG2;
  static List<Arguments> largePointsOnG2;
  static final Arguments largePointInfinity = Arguments.of("0", "0", "0", "0");

  static List<Arguments> smallPoints;
  static List<Arguments> largePoints;

  @BeforeAll
  public static void initConcatenetedLists() throws IOException {
    try {
      String basePath = "../arithmetization/src/test/resources/ecpairing/";
      smallPointsOutOfRange =
          readCsvToArgumentsList(Paths.get(basePath, "small_points_out_of_range.csv").toString());
      smallPointsNotOnC1 =
          readCsvToArgumentsList(Paths.get(basePath, "small_points_not_on_c1.csv").toString());
      smallPointsOnC1 =
          readCsvToArgumentsList(Paths.get(basePath, "small_points_on_c1.csv").toString());

      largePointsOutOfRange =
          readCsvToArgumentsList(Paths.get(basePath, "large_points_out_of_range.csv").toString());
      largePointsNotOnC2 =
          readCsvToArgumentsList(Paths.get(basePath, "large_points_not_on_c2.csv").toString());
      largePointsNotOnG2 =
          readCsvToArgumentsList(Paths.get(basePath, "large_points_not_on_g2.csv").toString());
      largePointsOnG2 =
          readCsvToArgumentsList(Paths.get(basePath, "large_points_on_g2.csv").toString());
    } catch (IOException e) {
      throw new IOException(e);
    }

    smallPoints =
        Stream.of(
                smallPointsOutOfRange,
                smallPointsNotOnC1,
                smallPointsOnC1,
                List.of(smallPointInfinity))
            .flatMap(List::stream)
            .collect(Collectors.toList());
    largePoints =
        Stream.of(
                largePointsOutOfRange,
                largePointsNotOnC2,
                largePointsNotOnG2,
                largePointsOnG2,
                List.of(largePointInfinity))
            .flatMap(List::stream)
            .collect(Collectors.toList());
  }

  @Test
  void testEcPairingWithSingleTrivialPairing() {
    BytecodeCompiler program =
        BytecodeCompiler.newProgram()
            .push(0x20) // retSize
            .push(0) // retOffset
            .push(192) // argSize
            .push(0) // argOffset
            .push(8) // address
            .push(Bytes.fromHexStringLenient("0xFFFFFFFF")) // gas
            .op(OpCode.STATICCALL);

    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    bytecodeRunner.run();
  }

  @Test
  void testEcPairingWithSingleNonTrivialValidPairing() {
    // small point: (Ax,Ay)
    // large point: (BxRe + i*BxIm, ByRe + i*ByIm)
    BytecodeCompiler program =
        BytecodeCompiler.newProgram()
            // random point in C1
            .push("26d7d8759964ac70b4d5cdf698ad5f70da246752481ea37da637551a60a2a57f") // Ax
            .push(0)
            .op(OpCode.MSTORE)
            .push("13991eda70bd3bd91c43e7c93f8f89c949bd271ba8f9f5a38bce7248f1f6056b") // Ay
            .push(0x20)
            .op(OpCode.MSTORE)
            // random point in G2
            .push("13eb8555f322c3885846df8a505693a0012493a30c34196a529f964a684c0cb2") // BxIm
            .push(0x40)
            .op(OpCode.MSTORE)
            .push("18335a998f3db61d3d0b63cd1371789a9a8a5ed4fb1a4adaa20ab9573251a9d0") // BxRe
            .push(0x60)
            .op(OpCode.MSTORE)
            .push("20494259608bfb4cd04716ba62e1350ce90d00d8af00a5170f46f59ae72d060c") // ByIm
            .push(0x80)
            .op(OpCode.MSTORE)
            .push("257a64fbc5a9cf9c3f7be349d09efa099305fe61f364c31c082ef6a81c815c1d") // ByRe
            .push(0xA0)
            .op(OpCode.MSTORE)
            // Do the call
            .push(0x20) // retSize
            .push(0) // retOffset
            .push(192) // argSize
            .push(0) // argOffset
            .push(8) // address
            .push(Bytes.fromHexStringLenient("0xFFFFFFFF")) // gas
            .op(OpCode.STATICCALL);

    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    bytecodeRunner.run();
  }

  @ParameterizedTest
  @MethodSource("ecPairingSingleSource")
  void testEcPairingSingleForScenario(
      String Ax, String Ay, String BxIm, String BxRe, String ByIm, String ByRe) {
    // small point: (Ax,Ay)
    // large point: (BxRe + i*BxIm, ByRe + i*ByIm)
    BytecodeCompiler program =
        BytecodeCompiler.newProgram()
            // point supposed to be in C1
            .push(Ax) // Ax
            .push(0)
            .op(OpCode.MSTORE)
            .push(Ay) // Ay
            .push(0x20)
            .op(OpCode.MSTORE)
            // point supposed to be in G2
            .push(BxIm) // BxIm
            .push(0x40)
            .op(OpCode.MSTORE)
            .push(BxRe) // BxRe
            .push(0x60)
            .op(OpCode.MSTORE)
            .push(ByIm) // ByIm
            .push(0x80)
            .op(OpCode.MSTORE)
            .push(ByRe) // ByRe
            .push(0xA0)
            .op(OpCode.MSTORE)
            .push(0x20) // retSize
            .push(0) // retOffset
            .push(192) // argSize
            .push(0) // argOffset
            .push(8) // address
            .push(Bytes.fromHexStringLenient("0xFFFFFFFF")) // gas
            .op(OpCode.STATICCALL);

    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    bytecodeRunner.run();
  }

  private static Stream<Arguments> ecPairingSingleSource() {
    List<Arguments> arguments = new ArrayList<>();
    for (Arguments smallPoint : smallPoints) {
      for (Arguments largePoint : largePoints) {
        arguments.add(pair(smallPoint, largePoint));
      }
    }
    return arguments.stream();
  }

  // Support methods
  @Test
  public void testPair() {
    Arguments smallPoint = Arguments.of("Ax", "Ay");
    Arguments largePoint = Arguments.of("BxIm", "BxRe", "ByIm", "ByRe");
    Arguments pair = pair(smallPoint, largePoint);
    assertEquals(6, pair.get().length);
    assertEquals("Ax", pair.get()[0]);
    assertEquals("Ay", pair.get()[1]);
    assertEquals("BxIm", pair.get()[2]);
    assertEquals("BxRe", pair.get()[3]);
    assertEquals("ByIm", pair.get()[4]);
    assertEquals("ByRe", pair.get()[5]);
  }

  public static Arguments pair(Arguments smallPointArgs, Arguments largePointArgs) {
    Preconditions.notNull(smallPointArgs, "first argument must not be null");
    Preconditions.notNull(largePointArgs, "second argument must not be null");

    Object[] smallPointArray = smallPointArgs.get();
    Object[] largePointArray = largePointArgs.get();
    Object[] pairArray = new Object[smallPointArray.length + largePointArray.length];

    System.arraycopy(smallPointArray, 0, pairArray, 0, smallPointArray.length);
    System.arraycopy(largePointArray, 0, pairArray, smallPointArray.length, largePointArray.length);

    return Arguments.of(pairArray);
  }

  public static List<Arguments> readCsvToArgumentsList(String filePath) throws IOException {
    List<Arguments> argumentsList = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String line;
      boolean isFirstLine = true;
      while ((line = br.readLine()) != null) {
        if (isFirstLine) {
          isFirstLine = false;
          continue; // Skip the first line (column names)
        }
        String[] values = line.split(",");
        argumentsList.add(Arguments.of((Object[]) values));
      }
    }
    return argumentsList;
  }
}