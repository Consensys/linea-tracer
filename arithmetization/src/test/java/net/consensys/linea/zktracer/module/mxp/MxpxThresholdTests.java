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

package net.consensys.linea.zktracer.module.mxp;

import static net.consensys.linea.zktracer.Fork.isPostCancun;
import static net.consensys.linea.zktracer.Trace.LLARGE;
import static net.consensys.linea.zktracer.Trace.LLARGEMO;
import static net.consensys.linea.zktracer.Trace.LLARGEPO;
import static net.consensys.linea.zktracer.Trace.WORD_SIZE;
import static net.consensys.linea.zktracer.Trace.WORD_SIZE_MO;
import static net.consensys.linea.zktracer.TraceCancun.Mxp.CANCUN_MXPX_THRESHOLD;
import static net.consensys.linea.zktracer.opcode.OpCode.MLOAD;
import static net.consensys.linea.zktracer.opcode.OpCode.MSTORE;
import static net.consensys.linea.zktracer.opcode.OpCode.POP;
import static net.consensys.linea.zktracer.opcode.OpCode.PUSH32;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import net.consensys.linea.reporting.TracerTestBase;
import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.zktracer.TraceCancun;
import net.consensys.linea.zktracer.TraceLondon;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public class MxpxThresholdTests extends TracerTestBase {

  static final BigInteger MAX_UINT256 =
      BigInteger.TWO.pow(256).subtract(BigInteger.ONE); // 2^256 - 1
  static final BigInteger LONDON_MXPX_THRESHOLD =
      BigInteger.valueOf(TraceLondon.Mxp.LONDON_MXPX_THRESHOLD);
  static final BigInteger CANCUN_MXPX_THRESHOLD =
      BigInteger.valueOf(TraceCancun.Mxp.CANCUN_MXPX_THRESHOLD);
  static final BigInteger SMALL = BigInteger.valueOf(32);

  static final List<OpCode> oneOffsetOpCodes = List.of(OpCode.MSTORE, OpCode.MSTORE8);
  static final List<OpCode> oneOffsetSizePairOpCodes = List.of(OpCode.CODECOPY);
  static final List<OpCode> twoOffsetSizePairsOpCodes = List.of(OpCode.CALL);

  /**
   * The following tests were written to test a bug fix in the tracer <a
   * href="https://github.com/Consensys/linea-tracer/pull/2251">PR</a>. The `HUB` and the `MXP`
   * modules used to have different, incompatible criteria by which they recognized of memory
   * expansion exception.
   *
   * <p>One issue was that the London `HUB` compared a sum `offset + size` to some threshold, while
   * the `MXP` module did the same but with a different comparison operator (≤ vs <). The issue
   * arose whenever opcodes had _precisely_ `offset + size = 256**4`.
   *
   * <p>Another issue was that the Cancun `HUB` still used sums `offset + size` as its gauge rather
   * than the individual offsets and sizes.
   *
   * <p>As such we are testing with parameters in that neighbourhood. We distinguish 4 classes of
   * MXP instructions:
   * <li>constant size opcodes (e.g. `MSTORE`, `MSTORE8` with respective 'sizes' 32 and 1)
   *
   *     <p>and variable size opcodes:
   * <li>single offset, single size (e.g., `CODECOPY`)
   * <li>double offset, single size (only `MCOPY`), which is covered in a separated test
   * <li>double offset, double size (the `CALL`'s)
   */
  @Tag("nightly")
  @ParameterizedTest
  @MethodSource({"testMxpxThresholdSource"})
  void testMxpxThreshold(
      OpCode opCode,
      BigInteger offset1,
      BigInteger offset2,
      BigInteger size1,
      BigInteger size2,
      TestInfo testInfo) {
    BytecodeCompiler program = BytecodeCompiler.newProgram(chainConfig);

    switch (opCode) {
        // 1 offset
      case MSTORE, MSTORE8 -> program
          .push(0) // value
          .push(offset1)
          .op(opCode);
        // 1 offset, 1 size
      case CODECOPY -> program.push(size1).push(0).push(offset1).op(opCode);
        // Note that CODECOPY has 2 offsets, but only one is relevant from the MXP perspective
        // 2 offsets, 2 sizes
      case CALL -> program
          .push(size2)
          .push(offset2)
          .push(size1)
          .push(offset1)
          .push(0) // value
          .push(0) // address
          .push(Bytes.fromHexStringLenient("0xFFFFFFFF")) // gas
          .op(opCode);
      default -> throw new IllegalArgumentException("Unsupported opCode: " + opCode);
    }

    BytecodeRunner.of(program.compile()).run(chainConfig, testInfo);
  }

  static Stream<Arguments> testMxpxThresholdSource() {
    final BigInteger MXPX_THRESHOLD = mxpxThreshold();
    List<Arguments> arguments = new ArrayList<>();
    List<BigInteger> values =
        List.of(
            BigInteger.ZERO,
            BigInteger.ONE,
            MXPX_THRESHOLD,
            MXPX_THRESHOLD.add(BigInteger.ONE),
            MAX_UINT256.subtract(BigInteger.valueOf(123)), // random huge number
            MAX_UINT256);

    for (OpCode opCode : oneOffsetOpCodes) {
      for (BigInteger offset1 : values) {
        arguments.add(Arguments.of(opCode, offset1, null, null, null));
      }
    }

    for (OpCode opCode : oneOffsetSizePairOpCodes) {
      for (BigInteger offset1 : values) {
        for (BigInteger size1 : values) {
          arguments.add(Arguments.of(opCode, offset1, null, size1, null));
        }
      }
    }

    for (OpCode opCode : twoOffsetSizePairsOpCodes) {
      for (BigInteger offset1 : values) {
        for (BigInteger offset2 : values) {
          for (BigInteger size1 : values) {
            for (BigInteger size2 : values) {
              arguments.add(Arguments.of(opCode, offset1, offset2, size1, size2));
            }
          }
        }
      }
    }

    final BigInteger MXP_THRESHOLD_DIVIDED_BY_TWO = MXPX_THRESHOLD.divide(BigInteger.TWO);
    final BigInteger MXP_THRESHOLD_MINUS_MXP_THRESHOLD_DIVIDED_BY_TWO =
        MXPX_THRESHOLD.subtract(MXP_THRESHOLD_DIVIDED_BY_TWO);

    for (OpCode opCode : oneOffsetSizePairOpCodes) {
      // offset1 + size1 == MXPX_THRESHOLD
      arguments.add(Arguments.of(opCode, MXPX_THRESHOLD.subtract(SMALL), null, SMALL, null));
      arguments.add(Arguments.of(opCode, SMALL, null, MXPX_THRESHOLD.subtract(SMALL), null));
      arguments.add(
          Arguments.of(
              opCode,
              MXP_THRESHOLD_DIVIDED_BY_TWO,
              SMALL,
              MXP_THRESHOLD_MINUS_MXP_THRESHOLD_DIVIDED_BY_TWO,
              null));
    }

    for (OpCode opCode : twoOffsetSizePairsOpCodes) {
      // offset1 + size1 == MXPX_THRESHOLD
      arguments.add(Arguments.of(opCode, MXPX_THRESHOLD.subtract(SMALL), SMALL, SMALL, SMALL));
      arguments.add(Arguments.of(opCode, SMALL, SMALL, MXPX_THRESHOLD.subtract(SMALL), SMALL));
      arguments.add(
          Arguments.of(
              opCode,
              MXP_THRESHOLD_DIVIDED_BY_TWO,
              SMALL,
              MXP_THRESHOLD_MINUS_MXP_THRESHOLD_DIVIDED_BY_TWO,
              SMALL));
      // offset2 + size2 == MXPX_THRESHOLD
      arguments.add(Arguments.of(opCode, SMALL, MXPX_THRESHOLD.subtract(SMALL), SMALL, SMALL));
      arguments.add(Arguments.of(opCode, SMALL, SMALL, SMALL, MXPX_THRESHOLD.subtract(SMALL)));
      arguments.add(
          Arguments.of(
              opCode,
              SMALL,
              MXP_THRESHOLD_DIVIDED_BY_TWO,
              SMALL,
              MXP_THRESHOLD_MINUS_MXP_THRESHOLD_DIVIDED_BY_TWO));
    }

    return arguments.stream();
  }

  static BigInteger mxpxThreshold() {
    if (isPostCancun(chainConfig.fork)) {
      return CANCUN_MXPX_THRESHOLD;
    } else {
      return LONDON_MXPX_THRESHOLD;
    }
  }

  @ParameterizedTest
  @ValueSource(ints = {16, 17, 18, 19, 20, 21})
  void testCodeCopyForDifferentSizes(int size, TestInfo testInfo) {
    BytecodeCompiler program = BytecodeCompiler.newProgram(chainConfig);
    program
        .push(size)
        .push(0) // offset (arbitrary value)
        .push(
            BigInteger.valueOf(13)
                .add(LONDON_MXPX_THRESHOLD)
                .subtract(BigInteger.valueOf(32))) // destOffset
        .op(OpCode.CODECOPY);
    BytecodeRunner.of(program.compile()).run(chainConfig, testInfo);
  }

  @ParameterizedTest
  @MethodSource({
    "testCodeCopyOverflowWithOneTinyParameterSource",
    "testCodeCopyOverflowWithTwoSimilarValuesSource",
    "testCodeCopyOverflowWithTwoLargeValuesSource"
  })
  void testCodeCopyOverflow(BigInteger size, BigInteger destOffset, TestInfo testInfo) {
    BytecodeCompiler program = BytecodeCompiler.newProgram(chainConfig);
    program
        .push(size)
        .push(0) // offset (arbitrary value)
        .push(destOffset) // destOffset
        .op(OpCode.CODECOPY);
    BytecodeRunner.of(program.compile()).run(chainConfig, testInfo);
  }

  static Stream<Arguments> testCodeCopyOverflowWithOneTinyParameterSource() {
    List<Arguments> arguments = new ArrayList<>();
    List<BigInteger> aValues =
        List.of(BigInteger.valueOf(16), BigInteger.valueOf(17), BigInteger.valueOf(18));
    BigInteger b =
        LONDON_MXPX_THRESHOLD.subtract(BigInteger.valueOf(32)).add(BigInteger.valueOf(15));
    for (BigInteger a : aValues) {
      arguments.add(Arguments.of(a, b));
      arguments.add(Arguments.of(b, a));
    }
    return arguments.stream();
  }

  static Stream<Arguments> testCodeCopyOverflowWithTwoSimilarValuesSource() {
    List<Arguments> arguments = new ArrayList<>();
    List<BigInteger> aValues =
        List.of(
            BigInteger.TWO.pow(31),
            BigInteger.TWO.pow(31).subtract(BigInteger.valueOf(1)),
            BigInteger.TWO.pow(31).add(BigInteger.valueOf(1)));
    BigInteger b = BigInteger.TWO.pow(31);
    for (BigInteger a : aValues) {
      arguments.add(Arguments.of(a, b));
      arguments.add(Arguments.of(b, a));
    }
    return arguments.stream();
  }

  static Stream<Arguments> testCodeCopyOverflowWithTwoLargeValuesSource() {
    List<Arguments> arguments = new ArrayList<>();
    List<BigInteger> values =
        List.of(
            LONDON_MXPX_THRESHOLD,
            LONDON_MXPX_THRESHOLD.subtract(BigInteger.valueOf(1)),
            LONDON_MXPX_THRESHOLD.add(BigInteger.valueOf(1)));
    for (BigInteger a : values) {
      for (BigInteger b : values) {
        arguments.add(Arguments.of(a, b));
      }
    }
    return arguments.stream();
  }

  // Specialized tests for MCOPY
  @ParameterizedTest
  @MethodSource("inputParamsUnit")
  void McopyLight(Bytes targetOffset, Bytes sourceOffset, Bytes size, TestInfo testInfo) {
    singleMcopy(targetOffset, sourceOffset, size, testInfo);
  }

  @Tag("nightly")
  @ParameterizedTest
  @MethodSource("inputParamsNightly")
  void McopyExtensive(Bytes targetOffset, Bytes sourceOffset, Bytes size, TestInfo testInfo) {
    singleMcopy(targetOffset, sourceOffset, size, testInfo);
  }

  private static Stream<Arguments> inputs(List<Bytes32> inputsValues) {
    final List<Arguments> arguments = new ArrayList<>();
    for (Bytes32 targetOffset : inputsValues) {
      for (Bytes32 sourceOffset : inputsValues) {
        for (Bytes32 size : inputsValues) {
          arguments.add(Arguments.of(targetOffset, sourceOffset, size));
        }
      }
    }
    return arguments.stream();
  }

  private static Stream<Arguments> inputParamsNightly() {
    return inputs(inputsValuesNightly);
  }

  private static Stream<Arguments> inputParamsUnit() {
    return inputs(inputsValuesUnit);
  }

  private static final List<Bytes32> inputsValuesUnit =
      List.of(
          Bytes32.ZERO,
          Bytes32.leftPad(Bytes.ofUnsignedInt(1)),
          Bytes32.leftPad(Bytes.ofUnsignedLong(TraceCancun.Mxp.CANCUN_MXPX_THRESHOLD - 1)),
          Bytes32.leftPad(Bytes.ofUnsignedLong(TraceCancun.Mxp.CANCUN_MXPX_THRESHOLD)),
          Bytes32.repeat((byte) 0xff));

  private static final List<Bytes32> inputsValuesNightly =
      Stream.concat(
              inputsValuesUnit.stream(),
              Stream.of(
                  Bytes32.leftPad(Bytes.ofUnsignedInt(LLARGEMO)),
                  Bytes32.leftPad(Bytes.ofUnsignedInt(LLARGE)),
                  Bytes32.leftPad(Bytes.ofUnsignedInt(LLARGEPO)),
                  Bytes32.leftPad(Bytes.ofUnsignedInt(WORD_SIZE_MO)),
                  Bytes32.leftPad(Bytes.ofUnsignedInt(WORD_SIZE)),
                  Bytes32.leftPad(Bytes.ofUnsignedInt(33)),
                  Bytes32.leftPad(Bytes.ofUnsignedLong(Long.MAX_VALUE)),
                  Bytes32.leftPad(Bytes.ofUnsignedLong(TraceCancun.Mxp.CANCUN_MXPX_THRESHOLD + 1))))
          .toList();

  // Main test
  private void singleMcopy(Bytes targetOffset, Bytes sourceOffset, Bytes size, TestInfo testInfo) {

    final Bytes FILL_MEMORY =
        BytecodeCompiler.newProgram(chainConfig)
            .push(
                Bytes32.fromHexString(
                    "0x11223344556677889900aabbccddeeff11223344556677889900aabbccddeeff")) // value
            .push(0) // offset
            .op(MSTORE)
            .compile();

    final Bytes MLOADS =
        BytecodeCompiler.newProgram(chainConfig)
            .push(0)
            .op(MLOAD)
            .op(POP)
            .push(WORD_SIZE)
            .op(MLOAD)
            .op(POP)
            .push(2 * WORD_SIZE)
            .op(MLOAD)
            .op(POP)
            .compile();

    BytecodeRunner.of(
            Bytes.concatenate(
                FILL_MEMORY, // We fill the first 32 bytes of memory with non-trivial value
                pushAndMcopy(targetOffset, sourceOffset, size), // We perform the MCOPY
                MLOADS // We load the first 3 words of memory to check the result
                ))
        .run(chainConfig, testInfo);
  }

  private Bytes pushAndMcopy(Bytes targetOffset, Bytes sourceOffset, Bytes size) {
    return Bytes.concatenate(
        Bytes.of(PUSH32.byteValue()),
        Bytes32.leftPad(size),
        Bytes.of(PUSH32.byteValue()),
        Bytes32.leftPad(sourceOffset),
        Bytes.of(PUSH32.byteValue()),
        Bytes32.leftPad(targetOffset),
        Bytes.fromHexString("0x5E") // MCOPY opcode
        );
  }
}
