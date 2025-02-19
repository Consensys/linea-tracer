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
package net.consensys.linea.zktracer.instructionprocessing.callTests.prc.ecmul;

import static net.consensys.linea.zktracer.instructionprocessing.callTests.Utilities.*;
import static net.consensys.linea.zktracer.instructionprocessing.callTests.prc.CodeExecutionMethods.*;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.WORD_SIZE;
import static net.consensys.linea.zktracer.opcode.OpCode.*;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.zktracer.instructionprocessing.callTests.prc.GasParameter;
import net.consensys.linea.zktracer.instructionprocessing.callTests.prc.ReturnAtParameter;
import org.hyperledger.besu.datatypes.Address;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("weekly")
public class HappyPathTests {

  /** Non-parametric test to make sure things are working as expected. */
  @Test
  public void singleMessageCallTransactionTest() {
    CallParameters params =
        new CallParameters(
            CALL,
            GasParameter.COST,
            MemoryContentsParameter.WELL_FORMED_POINT_AND_NONTRIVIAL_MULTIPLIER,
            CallDataSizeParameter.FULL,
            ReturnAtParameter.FULL,
            true);

    params.setCodeOfHolderAccounts();
    BytecodeCompiler rootCode = happyPathWipeReturnDataHappyPathProgram(params);
    if (params.willRevert) revertWith(rootCode, 3 * WORD_SIZE, 2 * WORD_SIZE);

    runMessageCallTransactionWithProvidedCodeAsRootCode(rootCode);
  }

  private BytecodeCompiler happyPathWipeReturnDataHappyPathProgram(CallParameters params) {

    params.setCodeOfHolderAccounts();

    BytecodeCompiler program = BytecodeCompiler.newProgram();

    // populate memory with the data for first ECMUL call
    copyForeignCodeToRam(program, memoryContentsHolderAddress1);

    // happy path: first ECMUL call
    appendHappyPathPrecompileCall(program, params);
    copyHalfOfReturnDataOmittingTheFirstThirdOfIt(program, 0x0140);
    loadFirstReturnDataWordOntoStack(program, 0x02ff);

    // return data wiping
    appendInsufficientBalanceCall(
        program, CALL, 34_000, Address.fromHexString("deadc0ffee"), 13, 15, 17, 19);
    copyHalfOfReturnDataOmittingTheFirstThirdOfIt(program, 21);
    loadFirstReturnDataWordOntoStack(program, 48);

    // populate memory with the data for second MODEXP call
    copyForeignCodeToRam(program, memoryContentsHolderAddress2);

    // happy path: second ECMUL call
    appendHappyPathPrecompileCall(program, params);
    copyHalfOfReturnDataOmittingTheFirstThirdOfIt(program, 0x11);
    loadFirstReturnDataWordOntoStack(program, 0x02ff);

    return program;
  }

  /**
   * Constructs a call to the <b>ECADD</b> precompile in terms of {@link
   * net.consensys.linea.zktracer.instructionprocessing.callTests.prc.ecadd.CallParameters}.
   */
  public void appendHappyPathPrecompileCall(BytecodeCompiler program, CallParameters params) {

    // push r@c onto the stack
    switch (params.returnAt) {
      case EMPTY -> program.push(0);
      case PARTIAL -> program.push(23);
      case FULL -> program.push(2 * WORD_SIZE);
      default -> throw new RuntimeException("Unsupported returnAt parameter");
    }

    // push the r@o onto the stack
    program.push(3 * WORD_SIZE);

    // push the cds onto the stack
    switch (params.cds) {
      case EMPTY -> program.push(0);
        // partial words
      case NONEMPTY_1f -> program.push(0x1f);
      case NONEMPTY_3f -> program.push(0x3f);
        // full words
      case NONEMPTY_20 -> program.push(0x20);
      case NONEMPTY_40 -> program.push(0x40);
      case NONEMPTY_60 -> program.push(0x60);
      case FULL -> program.op(MSIZE);
      case LARGE -> program.push("ff".repeat(WORD_SIZE));
    }

    // push the cdo onto the stack;
    program.push(0);

    // if appropriate, push the value onto the stack
    if (params.call.callHasValueArgument()) {
      program.push(0x0400);
    }

    program.push(Address.ALTBN128_MUL);

    // push gas onto the stack
    int callStipend = params.call.callHasValueArgument() ? 2_300 : 0;
    switch (params.gas) {
      case ZERO -> program.push(0); // interesting in the nonzero value case
      case COST_MO -> program.push(5_999 - callStipend);
      case COST -> program.push(6_000 - callStipend);
      case FULL -> program.op(GAS);
      default -> throw new RuntimeException("Unsupported gas parameter");
    }

    program.op(params.call);
  }
}
