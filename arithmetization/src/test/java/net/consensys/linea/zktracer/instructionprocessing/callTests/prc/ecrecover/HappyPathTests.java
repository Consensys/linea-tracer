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
package net.consensys.linea.zktracer.instructionprocessing.callTests.prc.ecrecover;

import static net.consensys.linea.zktracer.instructionprocessing.callTests.Utilities.*;
import static net.consensys.linea.zktracer.instructionprocessing.callTests.prc.CodeExecutionMethods.*;
import static net.consensys.linea.zktracer.instructionprocessing.callTests.prc.CodeExecutionMethods.memoryContentsHolder2;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.WORD_SIZE;
import static net.consensys.linea.zktracer.opcode.OpCode.*;

import java.util.stream.Stream;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.zktracer.instructionprocessing.callTests.prc.CodeExecutionMethods;
import org.hyperledger.besu.datatypes.Address;
import org.junit.jupiter.api.Tag;
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
    if (params.willRevert) revertWith(rootCode, 0, 4 * WORD_SIZE);

    runMessageCallTransactionWithProvidedCodeAsRootCode(rootCode);
  }

  /** <b>CONTRACT_DEPLOYMENT_TRANSACTION</b> case, see {@link CodeExecutionMethods}. */
  @ParameterizedTest
  @MethodSource("happyPathParameterGeneration")
  public void deploymentTransactionTest(CallParameters params) {

    BytecodeCompiler txInitCode = happyPathWipeReturnDataHappyPathProgram(params);
    if (params.willRevert) revertWith(txInitCode, 0, 4 * WORD_SIZE);

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

  /**
   * {@link #happyPathWipeReturnDataHappyPathProgram} constructs the byte code for the <b>happy
   * path</b> testing of <b>ECRECOVER</b>. This code does the following:
   *
   * @param params
   * @return
   */
  private BytecodeCompiler happyPathWipeReturnDataHappyPathProgram(CallParameters params) {

    setCodeOfHolderAccounts(params);

    BytecodeCompiler program = BytecodeCompiler.newProgram();

    // populate memory with the data for first ECRECOVER call
    copyForeignCodeToRam(program, memoryContentsHolderAddress1);

    // happy path: first ECRECOVER call
    appendHappyPathPrecompileCall(program, params);
    copyHalfOfReturnDataOmittingTheFirstThirdOfIt(program, 0x2a);
    loadFirstReturnDataWordOntoStack(program, 0x02ff);

    // return data wiping
    appendInsufficientBalanceCall(
        program, CALL, 34_000, Address.fromHexString("b077c0ffee1337"), 13, 15, 17, 19);
    copyHalfOfReturnDataOmittingTheFirstThirdOfIt(program, 21);
    loadFirstReturnDataWordOntoStack(program, 48);

    // populate memory with the data for second ECRECOVER call
    copyForeignCodeToRam(program, memoryContentsHolderAddress2);

    // happy path: second ECRECOVER call
    appendHappyPathPrecompileCall(program, params);
    copyHalfOfReturnDataOmittingTheFirstThirdOfIt(program, 0x026c);
    loadFirstReturnDataWordOntoStack(program, 0x00);

    return program;
  }

  private void setCodeOfHolderAccounts(CallParameters params) {

    // implicitly switches variants thus producing different contents
    BytecodeCompiler contents1 = params.memoryContent.memoryContents();
    BytecodeCompiler contents2 = params.memoryContent.memoryContents();

    memoryContentsHolder1.code(contents1.compile());
    memoryContentsHolder2.code(contents2.compile());
  }

  public static Stream<Arguments> happyPathParameterGeneration() {
    return ParameterGeneration.happyPathParameterGeneration();
  }

  /** Constructs a call to the <b>ECRECOVER</b> precompile in terms of {@link CallParameters}. */
  public void appendHappyPathPrecompileCall(BytecodeCompiler program, CallParameters params) {

    // push r@c onto the stack
    switch (params.returnAt) {
      case EMPTY -> program.push(0);
      case PARTIAL -> program.push(
          12 + 6); // the first 12 bytes are zeros for successful ECRECOVER calls
      case FULL -> program.push(WORD_SIZE);
    }

    // push the r@o onto the stack
    program.push(4 * WORD_SIZE);

    // push the cds onto the stack
    switch (params.cds) {
      case EMPTY -> program.push(0);
      case MISSING_FINAL_BYTE_OF_R -> program.push(3 * WORD_SIZE - 1);
      case MISSING_FINAL_BYTE_OF_S -> program.push(4 * WORD_SIZE - 1);
      case FULL -> program.op(MSIZE);
    }

    // push the cdo onto the stack;
    program.push(0);

    // if appropriate, push the value onto the stack
    if (params.call.callHasValueArgument()) {
      program.push(0x0600);
    }

    program.push(Address.ECREC);

    // push gas onto the stack
    int callStipend = params.call.callHasValueArgument() ? 2_300 : 0;
    switch (params.gas) {
      case ZERO -> program.push(0); // interesting in the nonzero value case
      case COST_MO -> program.push(3000 - callStipend - 1);
      case COST -> program.push(3000 - callStipend);
      case FULL -> program.op(GAS);
      default -> throw new RuntimeException("Unsupported gas parameter");
    }
    program.op(params.call);
  }
}
