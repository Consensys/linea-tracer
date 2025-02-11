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
import static net.consensys.linea.zktracer.instructionprocessing.callTests.prc.RelativeRangePosition.*;
import static net.consensys.linea.zktracer.instructionprocessing.utilities.MonoOpCodeSmcs.keyPair;
import static net.consensys.linea.zktracer.instructionprocessing.utilities.MonoOpCodeSmcs.userAccount;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.WORD_SIZE;
import static net.consensys.linea.zktracer.opcode.OpCode.*;
import static org.hyperledger.besu.datatypes.TransactionType.FRONTIER;

import java.util.List;
import java.util.stream.Stream;

import net.consensys.linea.testing.*;
import net.consensys.linea.zktracer.instructionprocessing.callTests.prc.*;
import org.hyperledger.besu.datatypes.Address;
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
 *
 * <p>In various setups:
 *
 * <p>- at depth 0 in the root context of a <b>MESSAGE_CALL_TRANSACTION</b>
 *
 * <p>- at depth 0 in the root context of a <b>CONTRACT_DEPLOYMENT_TRANSACTION</b>
 *
 * <p>- at depth 1 in a <b>MESSAGE_CALL_FROM_ROOT</b>
 *
 * <p>- at depth 1 in a <b>DURING_DEPLOYMENT</b>
 *
 * <p>- at depth 1 in a <b>AFTER_DEPLOYMENT</b>
 */
public class HappyPathHashPrecompileTests {

  final Address rootAddress = Address.fromHexString("7007");
  final ToyAccount.ToyAccountBuilder root =
      ToyAccount.builder().address(rootAddress).balance(Wei.of(65536L)).nonce(1865);

  final Address chadPrcEnjoyerAddress = Address.fromHexString("cbad");
  final ToyAccount.ToyAccountBuilder chadPrcEnjoyer =
      ToyAccount.builder().address(chadPrcEnjoyerAddress).balance(Wei.of(1024L)).nonce(64);

  final Address initCodeOwnerAddress = Address.fromHexString("1717");
  final ToyAccount.ToyAccountBuilder initCodeOwner =
      ToyAccount.builder().address(initCodeOwnerAddress).balance(Wei.of(0x1337L)).nonce(127);

  final Address foreignCodeOwnerAddress = Address.fromHexString("f00d");
  final ToyAccount.ToyAccountBuilder foreignCodeOwner =
      ToyAccount.builder().address(foreignCodeOwnerAddress).balance(Wei.of(0x1789L)).nonce(255);

  final ToyTransaction.ToyTransactionBuilder transaction =
      ToyTransaction.builder()
          .sender(userAccount)
          .keyPair(keyPair)
          .transactionType(FRONTIER)
          .gasLimit(0xffffffL)
          .value(Wei.of(1_000_000_000L));

  public static Stream<Arguments> happyPathParameterGeneration() {
    return ParameterGeneration.happyPathParameterGeneration();
  }

  /**
   * MESSAGE_CALL transaction case
   *
   * @param params
   */
  @ParameterizedTest
  @MethodSource("happyPathParameterGeneration")
  public void messageCallTransactionTest(PrecompileCallParameters params) {
    if (!params.willRevert) {
      BytecodeCompiler rootCode = happyPathWipeReturnDataHappyPathProgram(params);
      BytecodeRunner.of(rootCode.compile()).run(Wei.fromEth(1), 61_000_000L);
    }
  }

  /**
   * CONTRACT_DEPLOYMENT transaction case
   *
   * @param params
   */
  @ParameterizedTest
  @MethodSource("happyPathParameterGeneration")
  public void deploymentTransactionTest(PrecompileCallParameters params) {

    BytecodeCompiler txInitCode = happyPathWipeReturnDataHappyPathProgram(params);
    if (params.willRevert) revertWith(txInitCode, 0, 0);

    runDeploymentTransactionWithProvidedCodeAsInitCode(txInitCode);
  }

  /**
   * MESSAGE_CALL_FROM_ROOT case:
   *
   * <p>- the transaction is a MESSAGE_CALL targeting {@code callDataAddressCaller}
   *
   * <p>- the ROOT contract is therefore {@code callDataAddressCaller}
   *
   * <p>- the ROOT calls the {@code chadPrcEnjoyer} contract which executes the <b>happy path</b>
   *
   * <p>- the ROOT optionally reverts
   *
   * @param params
   */
  @ParameterizedTest
  @MethodSource("happyPathParameterGeneration")
  public void messageCallFromRootTest(PrecompileCallParameters params) {
    BytecodeCompiler chadPrcEnjoyerCode = happyPathWipeReturnDataHappyPathProgram(params);
    runMessageCallToAccountEndowedWithProvidedCode(chadPrcEnjoyerCode, params.willRevert);
  }

  /**
   * The {@link #root} contract fully copies the code of the account whose address is in the {@link
   * #transaction} call data. This account is the {@link #chadPrcEnjoyer}. That code is then used as
   * the initialization code of a <b>CREATE</b>. The whole operation optionally <b>REVERT</b>'s.
   *
   * @param params
   */
  @ParameterizedTest
  @MethodSource("happyPathParameterGeneration")
  public void happyPathDuringCreate(PrecompileCallParameters params) {

    if (!params.willMxpx()) {
      BytecodeCompiler foreignCode = happyPathWipeReturnDataHappyPathProgram(params);
      runForeignByteCodeAsInitCode(foreignCode, params.willRevert);
    }
  }

  @ParameterizedTest
  @MethodSource("happyPathParameterGeneration")
  public void happyPathAfterCreate(PrecompileCallParameters params) {

    if (!params.willMxpx()) {
      BytecodeCompiler chadPrcEnjoyerCode = happyPathWipeReturnDataHappyPathProgram(params);
      runCreateDeployingForeignCodeAndCallIntoIt(chadPrcEnjoyerCode, params.willRevert);
    }
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
          case OTHER -> 43;
        };
    program.push(callDataSize);

    switch (params.cdo) {
      case ALIGNED -> program.push(0 + params.prc.smallOffset2());
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

  public void runDeploymentTransactionWithProvidedCodeAsInitCode(
      BytecodeCompiler transactionInitCode) {

    transaction.payload(transactionInitCode.compile()); // init code

    ToyExecutionEnvironmentV2.builder()
        .transaction(transaction.build())
        .accounts(List.of(userAccount))
        .zkTracerValidator(zkTracer -> {})
        .build()
        .run();
  }

  /**
   * We provide {@link #foreignCodeOwner} with {@code foreignCode} as its byte code. We provide
   * {@link #root} with byte code that copies the code of {@link #foreignCodeOwnerAddress} and runs
   * it as initialization code. {@link #root} optionally reverts.
   *
   * @param foreignCode
   * @param embedRevertIntoInitCode
   */
  public void runForeignByteCodeAsInitCode(
      BytecodeCompiler foreignCode, boolean embedRevertIntoInitCode) {

    foreignCodeOwner.code(foreignCode.compile());

    BytecodeCompiler rootCode = BytecodeCompiler.newProgram();
    copyForeignCodeAndRunItAsInitCode(rootCode, foreignCodeOwnerAddress);
    if (embedRevertIntoInitCode) revertWith(rootCode, 0, 0);
    root.code(rootCode.compile());

    transaction.to(root.build());

    ToyExecutionEnvironmentV2.builder()
        .accounts(List.of(userAccount, root.build(), foreignCodeOwner.build()))
        .transaction(transaction.build())
        .build()
        .run();
  }

  /**
   * {@link #chadPrcEnjoyer} is given {@code providedCode} as its byte code. The {@code root} of the
   * transaction calls {@link #chadPrcEnjoyer}. It then optionally reverts.
   *
   * @param providedCode
   * @param revertRoot
   */
  public void runMessageCallToAccountEndowedWithProvidedCode(
      BytecodeCompiler providedCode, boolean revertRoot) {

    chadPrcEnjoyer.code(providedCode.compile());

    BytecodeCompiler rootCode = BytecodeCompiler.newProgram();
    appendCallTo(rootCode, CALL, chadPrcEnjoyerAddress);
    if (revertRoot) revertWith(rootCode, 0, 0); // we let the ROOT revert
    root.code(rootCode.compile());

    transaction.to(root.build());

    ToyExecutionEnvironmentV2.builder()
        .accounts(List.of(userAccount, root.build(), chadPrcEnjoyer.build()))
        .transaction(transaction.build())
        .build()
        .run();
  }

  /**
   * - We provide {@link #root} with byte code that (<i>a</i>) copies {@link #initCodeOwner}'s code
   * and runs it as the init code of a <b>CREATE</b> (<i>b</i>) <b>CALL</b>'s into the newly
   * deployed contract (<i>c</i>) and optionally reverts.
   *
   * <p>- We provide {@link #initCodeOwner} with byte code that copies the code of {@link
   * #foreignCodeOwnerAddress} and <b>RETURN</b>'s it.
   *
   * <p>- We provide {@link #foreignCodeOwner} with {@code foreignCode} as its byte code.
   *
   * @param foreignCode
   * @param rootReverts
   */
  public void runCreateDeployingForeignCodeAndCallIntoIt(
      BytecodeCompiler foreignCode, boolean rootReverts) {

    // ROOT code
    int key = 65537; // 0x 01 00 01
    BytecodeCompiler rootCode = BytecodeCompiler.newProgram();
    copyForeignCodeAndRunItAsInitCode(rootCode, initCodeOwnerAddress);
    sstoreTopOfStackTo(rootCode, key); // store deployment address
    pushSeveral(rootCode, 0, 0, 0, 0, 0); // zero value
    sloadFrom(rootCode, key);
    rootCode.op(GAS).op(CALL); // call the deployed contract
    if (rootReverts) revertWith(rootCode, 0, 0);
    root.code(rootCode.compile());

    // init code owner code
    BytecodeCompiler initCode = BytecodeCompiler.newProgram();
    copyForeignCodeAndReturnIt(initCode, foreignCodeOwnerAddress);
    initCodeOwner.code(initCode.compile());

    // foreign code owner code
    foreignCodeOwner.code(foreignCode.compile());

    transaction.to(root.build());

    ToyExecutionEnvironmentV2.builder()
        .accounts(
            List.of(userAccount, root.build(), foreignCodeOwner.build(), initCodeOwner.build()))
        .transaction(transaction.build())
        .build()
        .run();
  }
}
