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
package net.consensys.linea.zktracer.instructionprocessing.callTests.prc.modexp;

import static net.consensys.linea.zktracer.instructionprocessing.callTests.Utilities.*;
import static net.consensys.linea.zktracer.instructionprocessing.callTests.prc.CodeExecutionMethods.*;
import static net.consensys.linea.zktracer.opcode.OpCode.*;

import java.util.stream.Stream;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.zktracer.instructionprocessing.callTests.prc.*;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Tag("weekly")
public class HappyPathTests {

  private final boolean variant1 = true;
  private final boolean variant2 = false;

  /**
   * <b>MESSAGE_CALL_TRANSACTION</b> case.
   * <p> See {@link CodeExecutionMethods} for
   * documentation and context.
   *
   * @param params
   */
  @ParameterizedTest
  @MethodSource("happyPathParameterGeneration")
  public void messageCallTransactionTest(ModexpCallParameters params) {
    BytecodeCompiler rootCode = happyPathWipeReturnDataHappyPathProgram(params);
    runMessageCallTransactionWithProvidedCodeAsRootCode(rootCode);
  }

  /**
   * <b>CONTRACT_DEPLOYMENT_TRANSACTION</b> case.
   * <p> See {@link CodeExecutionMethods} for
   * documentation and context.
   *
   * @param params
   */
  @ParameterizedTest
  @MethodSource("happyPathParameterGeneration")
  public void deploymentTransactionTest(ModexpCallParameters params) {

    BytecodeCompiler txInitCode = happyPathWipeReturnDataHappyPathProgram(params);
    if (params.willRevert) revertWith(txInitCode, 0, 0);

    runDeploymentTransactionWithProvidedCodeAsInitCode(txInitCode);
  }

  /**
   * <b>MESSAGE_CALL_FROM_ROOT</b> case.
   * <p> See {@link CodeExecutionMethods} for
   * documentation and context.
   *
   * @param params
   */
  @ParameterizedTest
  @MethodSource("happyPathParameterGeneration")
  public void messageCallFromRootTest(ModexpCallParameters params) {
    BytecodeCompiler chadPrcEnjoyerCode = happyPathWipeReturnDataHappyPathProgram(params);
    runMessageCallToAccountEndowedWithProvidedCode(chadPrcEnjoyerCode, params.willRevert);
  }

  /**
   * <b>DURING_DEPLOYMENT</b> case.
   * <p> See {@link CodeExecutionMethods} for
   * documentation and context.
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
  public void happyPathDuringCreate(ModexpCallParameters params) {
      BytecodeCompiler foreignCode = happyPathWipeReturnDataHappyPathProgram(params);
      runForeignByteCodeAsInitCode(foreignCode, params.willRevert);
  }

  /**
   * <b>AFTER_DEPLOYMENT</b> case.
   * <p> See {@link CodeExecutionMethods} for
   * documentation and context.
   */
  @ParameterizedTest
  @MethodSource("happyPathParameterGeneration")
  public void happyPathAfterCreate(ModexpCallParameters params) {
      BytecodeCompiler foreignCode = happyPathWipeReturnDataHappyPathProgram(params);
      runCreateDeployingForeignCodeAndCallIntoIt(foreignCode, params.willRevert);
  }

  /** Non-parametric test to make sure things are working as expected. */
  @Test
  public void singleMessageCallTransactionTest() {

    CallDataParametersForModexp callDataParameters =
        new CallDataParametersForModexp(
            ByteSizeParameter.MODERATE, // bbs
            ByteSizeParameter.MODERATE, // ebs
            ByteSizeParameter.MAX, // mbs
            ModexpCallDataSizeParameter.MODULUS_FULL // cds
            );
    ModexpCallParameters params =
        new ModexpCallParameters(
            STATICCALL,
            GasParameter.FULL,
            callDataParameters,
            ReturnAtParameter.FULL,
            RelativeRangePosition.OVERLAP,
            true);
    setCodeOfHolderAccounts(params);
    BytecodeCompiler rootCode = happyPathWipeReturnDataHappyPathProgram(params);
    runMessageCallTransactionWithProvidedCodeAsRootCode(rootCode);
  }

  /**
   * {@link #happyPathWipeReturnDataHappyPathProgram} constructs the byte code for the <b>happy path</b>
   * testing of <b>MODEXP</b>. This code does the following:
   *
   * <p>- populate memory with the data for first MODEXP call
   * <p>- perform first MODEXP call and play around with its return data
   * <p>- wipe return data
   * <p>- populate memory with the data for second MODEXP call
   * <p>- perform second MODEXP call and play around with its return data
   * @param params
   * @return
   */
  private BytecodeCompiler happyPathWipeReturnDataHappyPathProgram(ModexpCallParameters params) {

    setCodeOfHolderAccounts(params);

    BytecodeCompiler program = BytecodeCompiler.newProgram();

    // populate memory with the data for first MODEXP call
    copyForeignCodeToRam(program, modexpMemoryHolderAddress1);

    // happy path: first MODEXP call
    appendHappyPathPrecompileCall(program, params, variant1);
    copyHalfOfReturnDataOmittingTheFirstThirdOfIt(program, 0x0140);
    loadFirstReturnDataWordOntoStack(program, 0x02ff);

    // return data wiping
    appendInsufficientBalanceCall(
        program, CALL, 34_000, Address.fromHexString("c0ffee69"), 13, 15, 17, 19);
    copyHalfOfReturnDataOmittingTheFirstThirdOfIt(program, 21);
    loadFirstReturnDataWordOntoStack(program, 48);

    // populate memory with the data for second MODEXP call
    copyForeignCodeToRam(program, modexpMemoryHolderAddress2);

    // happy path: second MODEXP call
    appendHappyPathPrecompileCall(program, params, variant2);
    copyHalfOfReturnDataOmittingTheFirstThirdOfIt(program, 0x026c);
    loadFirstReturnDataWordOntoStack(program, 0x02ff);

    return program;
  }

  /**
   * Populate the byte code of {@link CodeExecutionMethods#modexpMemoryHolder1} and {@link
   * CodeExecutionMethods#modexpMemoryHolder2} with "byte code" that is well-formed data for a
   * MODEXP call.
   *
   * @param params
   */
  private void setCodeOfHolderAccounts(ModexpCallParameters params) {

    String code1 = params.callData.wellFormedCallDataForModexpCall(variant1);
    String code2 = params.callData.wellFormedCallDataForModexpCall(variant2);

    modexpMemoryHolder1.code(Bytes.fromHexString(code1));
    modexpMemoryHolder2.code(Bytes.fromHexString(code2));
  }

  public static Stream<Arguments> happyPathParameterGeneration() {
    return ParameterGeneration.happyPathParameterGeneration();
  }

  /**
   * Constructs a CALL to the MODEXP precompile in terms of {@link ModexpCallParameters}.
   * @param program
   * @param params
   * @param variant
   */
  public void appendHappyPathPrecompileCall(
          BytecodeCompiler program, ModexpCallParameters params, boolean variant) {

    int cds = params.callData.memorySize(variant);

    // pushing r@c onto the stack
    int rac =
        switch (params.returnAt) {
          case EMPTY -> 0;
          case PARTIAL -> cds / 2;
          case FULL -> cds;
          case LARGE -> cds + 256;
        };
    program.push(rac);

    // pushing r@o onto the stack
    int rao =
        switch (params.relPos) {
          case DISJOINT -> cds;
          case OVERLAP -> 0;
        };
    program.push(rao);

    // pushing cds onto the stack
    program.push(cds);

    // pushing cdo onto the stack
    program.push(0);

    // pushing zero value onto the stack
    if (params.call.callHasValueArgument()) {
      program.push(0);
    }

    // pushing MODEXP address onto the stack
    program.push(Address.MODEXP);

    // TODO: replace with actual gas price
    int cost = 10_000;

    // pushing gas onto the stack
    switch (params.gas) {
      case ZERO -> program.push(0);
      case EXACT_MO -> program.push(cost - 1);
      case EXACT -> program.push(cost);
      case EXACT_PO -> program.push(cost + 1);
      case FULL -> program.op(GAS);
    }

    program.op(params.call);
  }
}
