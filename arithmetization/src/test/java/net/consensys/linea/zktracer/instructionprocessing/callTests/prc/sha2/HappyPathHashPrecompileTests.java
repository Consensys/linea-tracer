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
package net.consensys.linea.zktracer.instructionprocessing.callTests.prc.sha2;

import static net.consensys.linea.zktracer.instructionprocessing.callTests.Utilities.*;
import static net.consensys.linea.zktracer.instructionprocessing.callTests.prc.GasParameter.*;
import static net.consensys.linea.zktracer.instructionprocessing.callTests.prc.RelativeRangePosition.*;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.WORD_SIZE;
import static net.consensys.linea.zktracer.opcode.OpCode.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.zktracer.instructionprocessing.callTests.prc.*;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.hyperledger.besu.datatypes.Wei;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * <b>Happy path</b> tests for <b>SHA2-256</b>, <b>RIPEMD-160</b> and <b>IDENTITY</b>. The present
 * tests pertain to precompile calls where
 *
 * <p>- the precompile is provided with sufficient gas (ensuring <b>scenario/PRC_SUCCESS</b>)
 *
 * <p>- nothing <b>REVERT</b>s (thus value only matters in terms of pricing)
 *
 * <p>To avoid trivialities we pre-populate memory with nonzero values. We force interactions
 * between the <b>call data</b> range, the <b>return at</b> range and <b>return data</b> ranges in
 * the <b>OVERLAP</b> case.
 *
 * <p>After the call we interact with return data via <b>RETURNDATA[SIZE/COPY]</b> and <b>MLOAD</b>.
 *
 * <p>The tests do more: we then wipe the return data and start all over again with the next
 * precompile.
 */
public class HappyPathHashPrecompileTests {

  private final int otherCds = 39;

  @ParameterizedTest
  @MethodSource("happyPathHashPrecompileParameters")
  public void happyPathWipeReturnDataHappyPathHashPrecompileTests(
      OpCode call,
      HashPrecompile prc,
      CallOffset cdo,
      CallSize cds,
      CallOffset rao,
      CallSize rac,
      RelativeRangePosition relPos) {

    BytecodeCompiler program = BytecodeCompiler.newProgram();

    populateMemory(program);

    // happy path 1
    appendHappyPathPrecompileCall(program, call, FULL, prc, cdo, cds, rao, rac, relPos);
    copyHalfOfReturnDataOmittingTheFirstThirdOfIt(program, relPos == OVERLAP ? 4 : 4 * WORD_SIZE);
    loadFirstReturnDataWordOntoStack(program, relPos == OVERLAP ? 15 : 5 * WORD_SIZE);

    // return data wiping
    appendInsufficientBalanceCall(program, call, 20_000, prc.getAddress(), 1, 2, 3, 4);
    copyHalfOfReturnDataOmittingTheFirstThirdOfIt(program, relPos == OVERLAP ? 4 : 4 * WORD_SIZE);
    loadFirstReturnDataWordOntoStack(program, relPos == OVERLAP ? 15 : 5 * WORD_SIZE);

    // happy path 2
    appendHappyPathPrecompileCall(program, call, FULL, prc.next(), cdo, cds, rao, rac, relPos);
    copyHalfOfReturnDataOmittingTheFirstThirdOfIt(program, relPos == OVERLAP ? 4 : 4 * WORD_SIZE);
    loadFirstReturnDataWordOntoStack(program, relPos == OVERLAP ? 15 : 5 * WORD_SIZE);

    BytecodeRunner.of(program.compile()).run(Wei.fromEth(1), 61_000_000L);
  }

  /**
   * Generates test parameters for the happy path tests.
   *
   * @return Stream of test parameters
   */
  public static Stream<Arguments> happyPathHashPrecompileParameters() {
    List<OpCode> CallOpCodes = List.of(CALL, CALLCODE, DELEGATECALL, STATICCALL);

    List<Arguments> argumentsList = new ArrayList<>();
    for (CallSize rac : CallSize.values()) {
      for (CallOffset rao : CallOffset.values()) {
        for (CallSize cds : CallSize.values()) {
          for (CallOffset cdo : CallOffset.values()) {
            for (RelativeRangePosition relPos : RelativeRangePosition.values()) {
              for (HashPrecompile precompile : HashPrecompile.values()) {
                for (OpCode callOpcode : CallOpCodes) {
                  argumentsList.add(
                      Arguments.of(callOpcode, precompile, cdo, cds, rao, rac, relPos));
                }
              }
            }
          }
        }
      }
    }
    return argumentsList.stream();
  }

  public void appendHappyPathPrecompileCall(
      BytecodeCompiler program,
      OpCode callOpcode,
      GasParameter gasParameter,
      HashPrecompile precompile,
      CallOffset cdo,
      CallSize cds,
      CallOffset rao,
      CallSize rac,
      RelativeRangePosition relPos) {

    // if DISJOINT the "return at range"; it lives among words 2 and 3 of RAM
    switch (rac) {
      case ZERO -> program.push(0);
      case WORD -> program.push(WORD_SIZE);
      case OTHER -> program.push(58);
    }

    switch (rao) {
      case ALIGNED -> program.push(
          (relPos == OVERLAP ? 0 : 2 * WORD_SIZE) + precompile.smallOffset1());
      case MISALIGNED -> program.push(
          (relPos == OVERLAP ? 0 : 2 * WORD_SIZE) + 4 + precompile.smallOffset1());
      case INFINITY -> program.push("ff".repeat(32));
    }

    // call data can occupy up to 52 bytes; it lives among words 0 and 1 of RAM
    int callDataSize =
        switch (cds) {
          case ZERO -> 0;
          case WORD -> WORD_SIZE;
          case OTHER -> otherCds;
        };
    program.push(callDataSize);

    switch (cdo) {
      case ALIGNED -> program.push(0 + precompile.smallOffset2());
      case MISALIGNED -> program.push(13 + precompile.smallOffset2());
      case INFINITY -> program.push("ff".repeat(32));
    }

    // push address
    program.push(precompile.getAddress());

    // push value if required
    if (callOpcode.callHasValueArgument()) {
      program.push(1);
    }

    // push gas parameter
    int cost = precompile.cost(callDataSize);
    switch (gasParameter) {
      case ZERO -> program.push(0);
      case EXACT_MO -> program.push(cost - 1);
      case EXACT -> program.push(cost);
      case EXACT_PO -> program.push(cost + 1);
      case FULL -> program.op(GAS);
    }

    program.op(callOpcode);
  }
}
