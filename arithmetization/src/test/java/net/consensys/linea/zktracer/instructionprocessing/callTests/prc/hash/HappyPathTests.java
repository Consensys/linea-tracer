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
package net.consensys.linea.zktracer.instructionprocessing.callTests.prc.hash;

import static net.consensys.linea.zktracer.instructionprocessing.callTests.Utilities.*;
import static net.consensys.linea.zktracer.instructionprocessing.callTests.prc.CodeExecutionMethods.*;
import static net.consensys.linea.zktracer.instructionprocessing.callTests.prc.RelativeRangePosition.*;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.WORD_SIZE;
import static net.consensys.linea.zktracer.opcode.OpCode.*;

import java.util.stream.Stream;

import net.consensys.linea.testing.*;
import net.consensys.linea.zktracer.instructionprocessing.callTests.prc.*;
import org.junit.jupiter.api.Tag;
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
 *
 * <p>To give full details, we will test the following scenario which we call <b>happy path
 * precompile</b>:
 *
 * <p>- happy path precompile CALL
 *
 * <p>- play with (precompile) return data
 *
 * <p>- wipe return data
 *
 * <p>- (different) happy path precompile CALL
 *
 * <p>- play with (precompile) return data
 */
@Tag("weekly")
public class HappyPathTests {

  public static Stream<Arguments> happyPathParameterGeneration() {
    return ParameterGeneration.happyPathParameterGeneration();
  }

  /**
   * <b>MESSAGE_CALL_TRANSACTION</b> case.
   *
   * <p>See {@link CodeExecutionMethods} for documentation and context.
   *
   * @param params
   */
  @ParameterizedTest
  @MethodSource("happyPathParameterGeneration")
  public void messageCallTransactionTest(HashPrecompileCallParameters params) {
    if (!params.willRevert) {
      BytecodeCompiler rootCode = happyPathWipeReturnDataHappyPathProgram(params);
      runMessageCallTransactionWithProvidedCodeAsRootCode(rootCode);
    }
  }

  /**
   * <b>CONTRACT_DEPLOYMENT_TRANSACTION</b> case.
   *
   * <p>See {@link CodeExecutionMethods} for documentation and context.
   *
   * @param params
   */
  @ParameterizedTest
  @MethodSource("happyPathParameterGeneration")
  public void deploymentTransactionTest(HashPrecompileCallParameters params) {

    BytecodeCompiler txInitCode = happyPathWipeReturnDataHappyPathProgram(params);
    if (params.willRevert) revertWith(txInitCode, 0, 0);

    runDeploymentTransactionWithProvidedCodeAsInitCode(txInitCode);
  }

  /**
   * <b>MESSAGE_CALL_FROM_ROOT</b> case.
   *
   * <p>See {@link CodeExecutionMethods} for documentation and context.
   */
  @ParameterizedTest
  @MethodSource("happyPathParameterGeneration")
  public void messageCallFromRootTest(HashPrecompileCallParameters params) {
    BytecodeCompiler chadPrcEnjoyerCode = happyPathWipeReturnDataHappyPathProgram(params);
    runMessageCallToAccountEndowedWithProvidedCode(chadPrcEnjoyerCode, params.willRevert);
  }

  /**
   * <b>DURING_DEPLOYMENT</b> case.
   *
   * <p>See {@link CodeExecutionMethods} for documentation and context.
   *
   * <p>The {@link CodeExecutionMethods#root} contract fully copies the code of the account whose
   * address is in the {@link CodeExecutionMethods#transaction} call data. This account is the
   * {@link CodeExecutionMethods#chadPrcEnjoyer}. That code is then used as the initialization code
   * of a <b>CREATE</b>. The whole operation optionally <b>REVERT</b>'s.
   *
   * @param params
   */
  @ParameterizedTest
  @MethodSource("happyPathParameterGeneration")
  public void happyPathDuringCreate(HashPrecompileCallParameters params) {
    if (!params.willMxpx()) {
      BytecodeCompiler foreignCode = happyPathWipeReturnDataHappyPathProgram(params);
      runForeignByteCodeAsInitCode(foreignCode, params.willRevert);
    }
  }

  /**
   * <b>AFTER_DEPLOYMENT</b> case.
   *
   * <p>See {@link CodeExecutionMethods} for documentation and context.
   */
  @ParameterizedTest
  @MethodSource("happyPathParameterGeneration")
  public void happyPathAfterCreate(HashPrecompileCallParameters params) {
    if (!params.willMxpx()) {
      BytecodeCompiler chadPrcEnjoyerCode = happyPathWipeReturnDataHappyPathProgram(params);
      runCreateDeployingForeignCodeAndCallIntoIt(chadPrcEnjoyerCode, params.willRevert);
    }
  }

  public void appendHappyPathPrecompileCall(
      BytecodeCompiler program, HashPrecompileCallParameters params) {

    // if DISJOINT the "return at range"; it lives among words 2 and 3 of RAM
    switch (params.rac) {
      case ZERO -> program.push(0);
      case WORD -> program.push(WORD_SIZE);
      case OTHER -> program.push(58);
    }

    switch (params.rao) {
      case ALIGNED -> program.push(
          (params.relPos == OVERLAP ? 0 : 2 * WORD_SIZE) + params.prc.smallOffset1());
      case MISALIGNED -> program.push(
          (params.relPos == OVERLAP ? 0 : 2 * WORD_SIZE) + 4 + params.prc.smallOffset1());
      case INFINITY -> program.push("ff".repeat(32));
    }

    // call data can occupy up to 52 bytes; it lives among words 0 and 1 of RAM
    int callDataSize =
        switch (params.cds) {
          case ZERO -> 0;
          case WORD -> WORD_SIZE;
          case OTHER -> 43;
        };
    program.push(callDataSize);

    switch (params.cdo) {
      case ALIGNED -> program.push(params.prc.smallOffset2());
      case MISALIGNED -> program.push(13 + params.prc.smallOffset2());
      case INFINITY -> program.push("ff".repeat(32));
    }

    if (params.call.callHasValueArgument()) {
      switch (params.value) {
        case ZERO -> program.push(0);
        case ONE -> program.push(1);
        case VALUE -> program.push("69");
      }
    }

    // push address
    program.push(params.prc.getAddress());

    // push gas parameter
    int cost = params.prc.cost(callDataSize);
    switch (params.gas) {
      case ZERO -> program.push(0);
      case COST_MO -> program.push(cost - 1);
      case COST -> program.push(cost);
      case FULL -> program.op(GAS);
      case MAX -> program.push("ff".repeat(32));
    }

    program.op(params.call);
  }

  private BytecodeCompiler happyPathWipeReturnDataHappyPathProgram(
      HashPrecompileCallParameters params) {

    BytecodeCompiler program = BytecodeCompiler.newProgram();

    populateMemory(program);

    // Note: we provide zero value, otherwise the precompile would receive a G_stipend = 2300 gas
    // bonus, offsetting our gas cost computation
    // happy path 1
    appendHappyPathPrecompileCall(program, params);
    copyHalfOfReturnDataOmittingTheFirstThirdOfIt(
        program, params.relPos == OVERLAP ? 4 : 4 * WORD_SIZE);
    loadFirstReturnDataWordOntoStack(program, params.relPos == OVERLAP ? 15 : 5 * WORD_SIZE);

    // return data wiping
    appendInsufficientBalanceCall(program, CALL, 20_000, params.prc.getAddress(), 1, 2, 3, 4);
    copyHalfOfReturnDataOmittingTheFirstThirdOfIt(
        program, params.relPos == OVERLAP ? 4 : 4 * WORD_SIZE);
    loadFirstReturnDataWordOntoStack(program, params.relPos == OVERLAP ? 15 : 5 * WORD_SIZE);

    // happy path 2
    appendHappyPathPrecompileCall(program, params.next());
    copyHalfOfReturnDataOmittingTheFirstThirdOfIt(
        program, params.relPos == OVERLAP ? 4 : 4 * WORD_SIZE);
    loadFirstReturnDataWordOntoStack(program, params.relPos == OVERLAP ? 15 : 5 * WORD_SIZE);

    return program;
  }
}
