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
package net.consensys.linea.zktracer.instructionprocessing.callTests.prc.ecadd;

import static net.consensys.linea.zktracer.instructionprocessing.callTests.Utilities.*;
import static net.consensys.linea.zktracer.instructionprocessing.callTests.prc.CodeExecutionMethods.*;
import static net.consensys.linea.zktracer.instructionprocessing.callTests.prc.CodeExecutionMethods.memoryContentsHolderAddress2;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.WORD_SIZE;
import static net.consensys.linea.zktracer.opcode.OpCode.*;

import java.util.stream.Stream;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.zktracer.instructionprocessing.callTests.prc.CodeExecutionMethods;
import net.consensys.linea.zktracer.instructionprocessing.callTests.prc.GasParameter;
import net.consensys.linea.zktracer.instructionprocessing.callTests.prc.ReturnAtParameter;
import org.hyperledger.besu.datatypes.Address;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Tag("weekly")
public class HappyPathTests {

  /** <b>MESSAGE_CALL_TRANSACTION</b> case, see {@link CodeExecutionMethods}. */
  @ParameterizedTest
  @MethodSource("happyPathParameterGeneration")
  public void messageCallTransactionTest(CallParameters params) {

    BytecodeCompiler rootCode = happyPathWipeReturnDataHappyPathProgram(params);
    if (params.willRevert) revertWith(rootCode, 0, 5 * WORD_SIZE);

    runMessageCallTransactionWithProvidedCodeAsRootCode(rootCode);
  }

  /** <b>CONTRACT_DEPLOYMENT_TRANSACTION</b> case, see {@link CodeExecutionMethods}. */
  @ParameterizedTest
  @MethodSource("happyPathParameterGeneration")
  public void deploymentTransactionTest(CallParameters params) {

    BytecodeCompiler txInitCode = happyPathWipeReturnDataHappyPathProgram(params);
    if (params.willRevert) revertWith(txInitCode, 0, 5 * WORD_SIZE);

    runDeploymentTransactionWithProvidedCodeAsInitCode(txInitCode);
  }

  /** <b>CONTRACT_DEPLOYMENT_TRANSACTION</b> case, see {@link CodeExecutionMethods}. */
  @ParameterizedTest
  @MethodSource("happyPathParameterGeneration")
  public void messageCallFromRootTest(CallParameters params) {

    BytecodeCompiler txInitCode = happyPathWipeReturnDataHappyPathProgram(params);
    if (params.willRevert) revertWith(txInitCode, 0, 0);

    runDeploymentTransactionWithProvidedCodeAsInitCode(txInitCode);
  }

  /**
   * <b>DURING_DEPLOYMENT</b> case, see {@link CodeExecutionMethods}.
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
  public void happyPathDuringCreate(CallParameters params) {
    BytecodeCompiler foreignCode = happyPathWipeReturnDataHappyPathProgram(params);
    runForeignByteCodeAsInitCode(foreignCode, params.willRevert);
  }

  /** <b>AFTER_DEPLOYMENT</b> case, see {@link CodeExecutionMethods}. */
  @ParameterizedTest
  @MethodSource("happyPathParameterGeneration")
  public void happyPathAfterCreate(CallParameters params) {
    BytecodeCompiler foreignCode = happyPathWipeReturnDataHappyPathProgram(params);
    runCreateDeployingForeignCodeAndCallIntoIt(foreignCode, params.willRevert);
  }

  /** Non-parametric test to make sure things are working as expected. */
  @Test
  public void singleMessageCallTransactionTest() {
    CallParameters params =
        new CallParameters(
            CALL,
            GasParameter.COST_MO,
            MemoryContentsParameter.WELL_FORMED_POINTS,
            CallDataSizeParameter.FULL,
            ReturnAtParameter.FULL,
            true);

    setCodeOfHolderAccounts(params);
    BytecodeCompiler rootCode = happyPathWipeReturnDataHappyPathProgram(params);
    runMessageCallTransactionWithProvidedCodeAsRootCode(rootCode);
  }

  /**
   * {@link #happyPathWipeReturnDataHappyPathProgram} constructs the byte code for the <b>happy
   * path</b> testing of <b>ECADD</b>. This code does the following:
   *
   * @param params
   * @return
   */
  private BytecodeCompiler happyPathWipeReturnDataHappyPathProgram(CallParameters params) {

    setCodeOfHolderAccounts(params);

    BytecodeCompiler program = BytecodeCompiler.newProgram();

    // populate memory with the data for first ECADD call
    copyForeignCodeToRam(program, memoryContentsHolderAddress1);

    // happy path: first ECADD call
    appendHappyPathPrecompileCall(program, params);
    copyHalfOfReturnDataOmittingTheFirstThirdOfIt(program, 0x0140);
    loadFirstReturnDataWordOntoStack(program, 0x02ff);

    // return data wiping
    appendInsufficientBalanceCall(
        program, CALL, 34_000, Address.fromHexString("c0ffeebabe"), 13, 15, 17, 19);
    copyHalfOfReturnDataOmittingTheFirstThirdOfIt(program, 21);
    loadFirstReturnDataWordOntoStack(program, 48);

    // populate memory with the data for second MODEXP call
    copyForeignCodeToRam(program, memoryContentsHolderAddress2);

    // happy path: second ECADD call
    appendHappyPathPrecompileCall(program, params);
    copyHalfOfReturnDataOmittingTheFirstThirdOfIt(program, 0x026c);
    loadFirstReturnDataWordOntoStack(program, 0x02ff);

    return program;
  }

  private void setCodeOfHolderAccounts(CallParameters params) {

    BytecodeCompiler code1 = params.memoryContent.memoryContents();
    BytecodeCompiler code2 = params.memoryContent.memoryContents();

    memoryContentsHolder1.code(code1.compile());
    memoryContentsHolder2.code(code2.compile());
  }

  public static Stream<Arguments> happyPathParameterGeneration() {
    return ParameterGeneration.happyPathParameterGeneration();
  }

  /** Constructs a call to the <b>ECADD</b> precompile in terms of {@link CallParameters}. */
  public void appendHappyPathPrecompileCall(BytecodeCompiler program, CallParameters params) {

    // push r@c onto the stack
    switch (params.returnAt) {
      case EMPTY -> program.push(0);
      case PARTIAL -> program.push(23);
      case FULL -> program.push(2 * WORD_SIZE);
      default -> throw new RuntimeException("Unsupported returnAt parameter");
    }

    // push the r@o onto the stack
    program.push(4 * WORD_SIZE);

    // push the cds onto the stack
    switch (params.cds) {
      case EMPTY -> program.push(0);
        // partial words
      case NONEMPTY_1f -> program.push(0x1f);
      case NONEMPTY_3f -> program.push(0x3f);
      case NONEMPTY_5f -> program.push(0x5f);
      case NONEMPTY_7f -> program.push(0x7f);
        // full words
      case NONEMPTY_20 -> program.push(0x20);
      case NONEMPTY_40 -> program.push(0x40);
      case NONEMPTY_60 -> program.push(0x60);
      case NONEMPTY_80 -> program.push(0x80);
      case FULL -> program.op(MSIZE);
      case LARGE -> program.push("ff".repeat(WORD_SIZE));
    }

    // push the cdo onto the stack;
    program.push(0);

    // if appropriate, push the value onto the stack
    if (params.call.callHasValueArgument()) {
      program.push(params.willRevert ? 0x0200 : 0);
    }

    program.push(Address.ALTBN128_ADD);

    // push gas onto the stack
    switch (params.gas) {
      case ZERO -> program.push(0); // interesting in the nonzero value case
      case COST_MO -> program.push(149);
      case COST -> program.push(150);
      case FULL -> program.op(GAS);
      default -> throw new RuntimeException("Unsupported gas parameter");
    }

    program.op(params.call);
  }
}
