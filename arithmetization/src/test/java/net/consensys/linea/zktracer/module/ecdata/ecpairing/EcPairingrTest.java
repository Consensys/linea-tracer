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

package net.consensys.linea.zktracer.module.ecdata.ecpairing;

import static net.consensys.linea.zktracer.module.ecdata.ecpairing.EcPairingTestSupport.argumentsListToPairingsAsString;
import static net.consensys.linea.zktracer.module.ecdata.ecpairing.EcPairingTestSupport.csvToArgumentsList;
import static net.consensys.linea.zktracer.module.ecdata.ecpairing.EcPairingTestSupport.generatePairings;
import static net.consensys.linea.zktracer.module.ecdata.ecpairing.EcPairingTestSupport.generatePairingsLargePointsMixed;
import static net.consensys.linea.zktracer.module.ecdata.ecpairing.EcPairingTestSupport.generatePairingsMixed;
import static net.consensys.linea.zktracer.module.ecdata.ecpairing.EcPairingTestSupport.generatePairingsSmallPointsMixed;
import static net.consensys.linea.zktracer.module.ecdata.ecpairing.EcPairingTestSupport.info;
import static net.consensys.linea.zktracer.module.ecdata.ecpairing.EcPairingTestSupport.pair;
import static net.consensys.linea.zktracer.module.ecdata.ecpairing.EcPairingTestSupport.pairingsAsStringToArgumentsList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;

// A TestWatcher is used to log the results of testEcPairingSingleForScenario
// into a csv file (one for successful and one for failing cases)
// that can be used to run the same test cases with @CsvFileSource
@ExtendWith(EcPairingTestWatcher.class)
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
  public static void initConcatenatedLists() {
    try {
      String basePath = "../arithmetization/src/test/resources/ecpairing/";
      smallPointsOutOfRange =
          csvToArgumentsList(Paths.get(basePath, "small_points_out_of_range.csv").toString());
      smallPointsNotOnC1 =
          csvToArgumentsList(Paths.get(basePath, "small_points_not_on_c1.csv").toString());
      smallPointsOnC1 =
          csvToArgumentsList(Paths.get(basePath, "small_points_on_c1.csv").toString());

      largePointsOutOfRange =
          csvToArgumentsList(Paths.get(basePath, "large_points_out_of_range.csv").toString());
      largePointsNotOnC2 =
          csvToArgumentsList(Paths.get(basePath, "large_points_not_on_c2.csv").toString());
      largePointsNotOnG2 =
          csvToArgumentsList(Paths.get(basePath, "large_points_not_on_g2.csv").toString());
      largePointsOnG2 =
          csvToArgumentsList(Paths.get(basePath, "large_points_on_g2.csv").toString());
    } catch (IOException e) {
      throw new RuntimeException("Failed to read CSV files", e);
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

  // Tests
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
  void testEcPairingSingleForScenarioUsingMethodSource(
      String Ax, String Ay, String BxIm, String BxRe, String ByIm, String ByRe) {
    testEcPairingSingleForScenario(Ax, Ay, BxIm, BxRe, ByIm, ByRe);
  }

  @Disabled // Useful only to test specific test cases in a CSV file
  @ParameterizedTest
  @CsvFileSource(
      resources =
          "/ecpairing/test_ec_pairing_single_for_scenario_using_method_source_failed_placeholder.csv")
  void testEcPairingSingleForScenarioUsingCsv(
      String Ax, String Ay, String BxIm, String BxRe, String ByIm, String ByRe) {
    testEcPairingSingleForScenario(Ax, Ay, BxIm, BxRe, ByIm, ByRe);
  }

  // Body of:
  // testEcPairingSingleForScenarioUsingMethodSource
  // testEcPairingSingleForScenarioUsingCsv
  private static void testEcPairingSingleForScenario(
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

    // Set arguments for TestWatcher
    EcPairingArgumentsSingleton.getInstance()
        .setArguments(Ax + "," + Ay + "," + BxIm + "," + BxRe + "," + ByIm + "," + ByRe);
  }

  // Method source of testEcPairingSingleForScenarioUsingMethodSource
  private static Stream<Arguments> ecPairingSingleSource() {
    List<Arguments> arguments = new ArrayList<>();
    for (Arguments smallPoint : smallPoints) {
      for (Arguments largePoint : largePoints) {
        arguments.add(pair(smallPoint, largePoint));
      }
    }
    return arguments.stream();
  }

  @ParameterizedTest
  @MethodSource({"ecPairingGenericSource", "ecPairingSuccessfulNonTrivialSource"})
  void testEcPairingGenericForScenarioUsingMethodSource(
      String description, String pairingsAsString) {
    testEcPairingGenericForScenario(description, pairingsAsString);
  }

  @Disabled // Useful only to test specific test cases in a CSV file
  @ParameterizedTest
  @CsvFileSource(
      resources =
          "/ecpairing/test_ec_pairing_generic_for_scenario_using_method_source_failed_placeholder.csv",
      maxCharsPerColumn = 100000)
  void testEcPairingGenericForScenarioUsingCsv(String description, String pairingsAsString) {
    testEcPairingGenericForScenario(description, pairingsAsString);
  }

  // Body of:
  // testEcPairingGenericForScenarioUsingMethodSource
  // testEcPairingGenericForScenarioUsingCsv
  private void testEcPairingGenericForScenario(String description, String pairingsAsString) {
    assertFalse(description.contains(" "), "Description cannot contain spaces");

    List<Arguments> pairings = pairingsAsStringToArgumentsList(pairingsAsString);

    BytecodeCompiler program = BytecodeCompiler.newProgram();
    for (int i = 0; i < pairings.size(); i++) {
      Arguments pair = pairings.get(i);

      // Extract the values of the pair
      String Ax = (String) pair.get()[0];
      String Ay = (String) pair.get()[1];
      String BxIm = (String) pair.get()[2];
      String BxRe = (String) pair.get()[3];
      String ByIm = (String) pair.get()[4];
      String ByRe = (String) pair.get()[5];

      // small point: (Ax,Ay)
      // large point: (BxRe + i*BxIm, ByRe + i*ByIm)

      // Add the pair to the program
      final int bytesOffset = 192 * i;

      // point supposed to be in C1
      program
          .push(Ax) // Ax
          .push(bytesOffset)
          .op(OpCode.MSTORE)
          .push(Ay) // Ay
          .push(0x20 + bytesOffset)
          .op(OpCode.MSTORE)
          // point supposed to be in G2
          .push(BxIm) // BxIm
          .push(0x40 + bytesOffset)
          .op(OpCode.MSTORE)
          .push(BxRe) // BxRe
          .push(0x60 + bytesOffset)
          .op(OpCode.MSTORE)
          .push(ByIm) // ByIm
          .push(0x80 + bytesOffset)
          .op(OpCode.MSTORE)
          .push(ByRe) // ByRe
          .push(0xA0 + bytesOffset)
          .op(OpCode.MSTORE);
    }

    // Once all the pairs are added, do the call
    program
        .push(0x20) // retSize
        .push(0) // retOffset
        .push(192 * pairings.size()) // argSize
        .push(0) // argOffset
        .push(8) // address
        .push(Bytes.fromHexStringLenient("0xFFFFFFFF")) // gas
        .op(OpCode.STATICCALL);

    // Run the program
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    bytecodeRunner.run();

    // Set arguments for TestWatcher
    EcPairingArgumentsSingleton.getInstance().setArguments(description + "," + pairingsAsString);
  }

  // Method source of testEcPairingGenericForScenarioUsingMethodSource
  private static Stream<Arguments> ecPairingGenericSource() {
    List<Arguments> allPairings = new ArrayList<>();

    final int REPETITIONS = 3;
    for (int r = 1; r <= REPETITIONS; r++) {
      for (Integer totalPairings : List.of(3, 4, 5)) {
        // PRECOMPILE_ECPAIRING_MILLER_LOOPS = 64
        // This test has totalPairings fixed to 64
        allPairings.add(
            generatePairings("maxGenericPairings" + info(64, r), 64, smallPoints, largePoints));

        // Focus on small points scenarios
        allPairings.add(
            generatePairings(
                "smallPointsOutOfRange" + info(totalPairings, r),
                totalPairings,
                smallPointsOutOfRange,
                largePoints));

        allPairings.add(
            generatePairingsSmallPointsMixed(
                "smallPointsOutOfRangeMixed" + info(totalPairings, r),
                totalPairings,
                smallPointsOutOfRange,
                smallPoints,
                largePoints));

        allPairings.add(
            generatePairings(
                "smallPointsNotOnC1" + info(totalPairings, r),
                totalPairings,
                smallPointsNotOnC1,
                largePoints));

        allPairings.add(
            generatePairingsSmallPointsMixed(
                "smallPointsNotOnC1Mixed" + info(totalPairings, r),
                totalPairings,
                smallPointsNotOnC1,
                smallPoints,
                largePoints));

        allPairings.add(
            generatePairings(
                "smallPointsOnC1" + info(totalPairings, r),
                totalPairings,
                smallPointsOnC1,
                largePoints));

        allPairings.add(
            generatePairingsSmallPointsMixed(
                "smallPointsOnC1Mixed" + info(totalPairings, r),
                totalPairings,
                smallPointsOnC1,
                smallPoints,
                largePoints));

        allPairings.add(
            generatePairings(
                "smallPointInfinity" + info(totalPairings, r),
                totalPairings,
                List.of(smallPointInfinity),
                largePoints));

        allPairings.add(
            generatePairingsSmallPointsMixed(
                "smallPointInfinityMixed" + info(totalPairings, r),
                totalPairings,
                List.of(smallPointInfinity),
                smallPoints,
                largePoints));

        // Focus on large points scenarios
        allPairings.add(
            generatePairings(
                "largePointsOutOfRange" + info(totalPairings, r),
                totalPairings,
                smallPoints,
                largePointsOutOfRange));

        allPairings.add(
            generatePairingsLargePointsMixed(
                "largePointsOutOfRangeMixed" + info(totalPairings, r),
                totalPairings,
                smallPoints,
                largePointsOutOfRange,
                largePoints));

        allPairings.add(
            generatePairings(
                "largePointsNotOnC2" + info(totalPairings, r),
                totalPairings,
                smallPoints,
                largePointsNotOnC2));

        allPairings.add(
            generatePairingsLargePointsMixed(
                "largePointsNotOnC2Mixed" + info(totalPairings, r),
                totalPairings,
                smallPoints,
                largePointsNotOnC2,
                largePoints));

        allPairings.add(
            generatePairings(
                "largePointsNotOnG2" + info(totalPairings, r),
                totalPairings,
                smallPoints,
                largePointsNotOnG2));

        allPairings.add(
            generatePairingsLargePointsMixed(
                "largePointsNotOnG2Mixed" + info(totalPairings, r),
                totalPairings,
                smallPoints,
                largePointsNotOnG2,
                largePoints));

        allPairings.add(
            generatePairings(
                "largePointsOnG2" + info(totalPairings, r),
                totalPairings,
                smallPoints,
                largePointsOnG2));

        allPairings.add(
            generatePairingsLargePointsMixed(
                "largePointsOnG2Mixed" + info(totalPairings, r),
                totalPairings,
                smallPoints,
                largePointsOnG2,
                largePoints));

        allPairings.add(
            generatePairings(
                "largePointInfinity" + info(totalPairings, r),
                totalPairings,
                smallPoints,
                List.of(largePointInfinity)));

        allPairings.add(
            generatePairingsLargePointsMixed(
                "largePointInfinityMixed" + info(totalPairings, r),
                totalPairings,
                smallPoints,
                List.of(largePointInfinity),
                largePoints));

        // Focus on combinations of small and large points scenarios
        allPairings.add(
            generatePairingsLargePointsMixed(
                "largePointNotOnG2Exists" + info(totalPairings, r),
                totalPairings,
                smallPointsOnC1,
                largePointsNotOnG2,
                largePoints));

        allPairings.add(
            generatePairings(
                "trivialPairingAllSmallPointsOnC1" + info(totalPairings, r),
                totalPairings,
                smallPointsOnC1,
                List.of(largePointInfinity)));

        allPairings.add(
            generatePairingsSmallPointsMixed(
                "trivialPairingSmallPointsOnC1AndInfinity" + info(totalPairings, r),
                totalPairings,
                smallPointsOnC1,
                List.of(smallPointInfinity),
                List.of(largePointInfinity)));

        allPairings.add(
            generatePairings(
                "nonTrivialPairingExcludingInfinity" + info(totalPairings, r),
                totalPairings,
                smallPointsOnC1,
                largePointsOnG2));

        allPairings.add(
            generatePairingsMixed(
                "nonTrivialPairingIncludingInfinity" + info(totalPairings, r),
                totalPairings,
                smallPointsOnC1,
                List.of(smallPointInfinity),
                largePointsOnG2,
                List.of(largePointInfinity)));
      }
    }
    return allPairings.stream();
  }

  // Method source of testEcPairingGenericForScenarioUsingMethodSource
  private static Stream<Arguments> ecPairingSuccessfulNonTrivialSource() {
    List<Arguments> allPairings = new ArrayList<>();
    // TODO: fill actual data
    List<Arguments> successfulNonTrivialPairing = new ArrayList<>();
    successfulNonTrivialPairing.add(Arguments.of("0", "0", "0", "0", "0", "0"));
    successfulNonTrivialPairing.add(Arguments.of("0", "0", "0", "0", "0", "0"));
    successfulNonTrivialPairing.add(Arguments.of("0", "0", "0", "0", "0", "0"));
    allPairings.add(
        Arguments.of(
            "successfulNonTrivialPairing",
            argumentsListToPairingsAsString(successfulNonTrivialPairing)));
    return allPairings.stream();
  }

  // Tests for support methods
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

  @Test
  public void testArgumentsListToPairingsAsString() {
    List<Arguments> pairings = new ArrayList<>();
    pairings.add(Arguments.of("Ax1", "Ay1", "BxIm1", "BxRe1", "ByIm1", "ByRe1"));
    pairings.add(Arguments.of("Ax2", "Ay2", "BxIm2", "BxRe2", "ByIm2", "ByRe2"));
    pairings.add(Arguments.of("Ax3", "Ay3", "BxIm3", "BxRe3", "ByIm3", "ByRe3"));
    assertEquals(
        "Ax1_Ay1_BxIm1_BxRe1_ByIm1_ByRe1_Ax2_Ay2_BxIm2_BxRe2_ByIm2_ByRe2_Ax3_Ay3_BxIm3_BxRe3_ByIm3_ByRe3",
        argumentsListToPairingsAsString(pairings));
  }
}
