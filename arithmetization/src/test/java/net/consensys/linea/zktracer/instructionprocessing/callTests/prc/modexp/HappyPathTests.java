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
import static net.consensys.linea.zktracer.opcode.OpCode.CALL;
import static net.consensys.linea.zktracer.opcode.OpCode.GAS;

import java.util.stream.Stream;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.zktracer.instructionprocessing.callTests.prc.ModexpCallParameters;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Wei;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class HappyPathTests {

  private final boolean variant1 = true;
  private final boolean variant2 = false;

  /**
   * MESSAGE_CALL transaction case
   *
   * @param params
   */
  @ParameterizedTest
  @MethodSource("happyPathParameterGeneration")
  public void messageCallTransactionTest(ModexpCallParameters params) {
    populateCodeOfMemoryHolderAccounts(params);
    BytecodeCompiler rootCode = happyPathWipeReturnDataHappyPathProgram(params);
    BytecodeRunner.of(rootCode).run(Wei.fromEth(1), 61_000_000L);
  }

  private BytecodeCompiler happyPathWipeReturnDataHappyPathProgram(ModexpCallParameters params) {

    BytecodeCompiler program = BytecodeCompiler.newProgram();

    // populate memory with the data for first MODEXP call
    copyForeignCodeToRam(program, modexpMemoryHolderAddress1);

    // happy path 1
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

    // happy path 2
    appendHappyPathPrecompileCall(program, params, variant2);
    copyHalfOfReturnDataOmittingTheFirstThirdOfIt(program, 0x026c);
    loadFirstReturnDataWordOntoStack(program, 0x02ff);

    return program;
  }

  private void populateCodeOfMemoryHolderAccounts(ModexpCallParameters params) {

    String code1 = params.callData.codeWhichWillBecomeMemoryOfModexpCall(variant1);
    String code2 = params.callData.codeWhichWillBecomeMemoryOfModexpCall(variant2);

    modexpMemoryHolder1.code(Bytes.fromHexString(code1));
    modexpMemoryHolder2.code(Bytes.fromHexString(code2));
  }

  public static Stream<Arguments> happyPathParameterGeneration() {
    return ParameterGeneration.happyPathParameterGeneration();
  }

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
  }
}
