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
import static net.consensys.linea.zktracer.instructionprocessing.utilities.MonoOpCodeSmcs.keyPair;
import static net.consensys.linea.zktracer.instructionprocessing.utilities.MonoOpCodeSmcs.userAccount;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.WORD_SIZE;
import static net.consensys.linea.zktracer.opcode.OpCode.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.testing.ToyTransaction;
import net.consensys.linea.zktracer.instructionprocessing.callTests.prc.*;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.hyperledger.besu.datatypes.TransactionType;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.core.Transaction;
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
 * <p>1. happy path precompile CALL
 *
 * <p>2. play with (precompile) return data
 *
 * <p>3. wipe return data
 *
 * <p>4. happy path
 *
 * <p>5. play with (precompile) return data
 *
 * <p>In various setups: in the root context of a <b>MESSAGE_CALL_TRANSACTION</b> or a
 * <b>CONTRACT_DEPLOYMENT_TRANSACTION</b>, at depth 1 in a <b>MESSAGE_CALL_FROM_ROOT</b>,
 * <b>DURING_DEPLOYMENT</b> or <b>AFTER_DEPLOYMENT</b>.
 */
public class HappyPathHashPrecompileTests {

  private final int otherCds = 39;

  /**
   * In the case where everything happens in the root context.
   *
   * @param params
   */
  @ParameterizedTest
  @MethodSource("happyPathHashPrecompileParameters")
  public void happyPathPrecompileMessageCallTransactionTest(PrecompileCallParameters params) {

    BytecodeCompiler program = happyPathWipeReturnDataHappyPathProgram(params);

    BytecodeRunner.of(program.compile()).run(Wei.fromEth(1), 61_000_000L);

    Transaction deploymentTransaction =
        ToyTransaction.builder()
            .sender(userAccount)
            .keyPair(keyPair)
            .transactionType(TransactionType.FRONTIER)
            .gasLimit(0xffffffL)
            .payload(program.compile()) // init code
            .value(Wei.of(1_000_000_000L))
            .build();
  }

  /**
   * Generates test parameters for the happy path tests.
   *
   * @return Stream of test parameters
   */
  public static Stream<Arguments> happyPathHashPrecompileParameters() {
    List<OpCode> CallOpCodes = List.of(CALL, CALLCODE, DELEGATECALL, STATICCALL);

    List<Arguments> argumentsList = new ArrayList<>();
    for (OpCode callOpcode : CallOpCodes) {
      for (GasParameter gas : GasParameter.values()) {
        for (HashPrecompile precompile : HashPrecompile.values()) {
          for (CallOffset cdo : CallOffset.values()) {
            for (CallSize cds : CallSize.values()) {
              for (CallOffset rao : CallOffset.values()) {
                for (CallSize rac : CallSize.values()) {
                  for (RelativeRangePosition relPos : RelativeRangePosition.values()) {
                    argumentsList.add(
                            Arguments.of(
                                    new PrecompileCallParameters(
                                            callOpcode, gas, precompile, 1, cdo, cds, rao, rac, relPos)));
                  }
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
      BytecodeCompiler program, PrecompileCallParameters params) {

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
          case OTHER -> otherCds;
        };
    program.push(callDataSize);

    switch (params.cdo) {
      case ALIGNED -> program.push(0 + params.prc.smallOffset2());
      case MISALIGNED -> program.push(13 + params.prc.smallOffset2());
      case INFINITY -> program.push("ff".repeat(32));
    }

    // push address
    program.push(params.prc.getAddress());

    if (params.call.callHasValueArgument()) {
      program.push(params.value);
    }

    // push gas parameter
    int cost = params.prc.cost(callDataSize);
    switch (params.gas) {
      case ZERO -> program.push(0);
      case EXACT_MO -> program.push(cost - 1);
      case EXACT -> program.push(cost);
      case EXACT_PO -> program.push(cost + 1);
      case FULL -> program.op(GAS);
    }

    program.op(params.call);
  }

  private BytecodeCompiler happyPathWipeReturnDataHappyPathProgram(
      PrecompileCallParameters params) {

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
    appendInsufficientBalanceCall(
        program, params.call, 20_000, params.prc.getAddress(), 1, 2, 3, 4);
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
