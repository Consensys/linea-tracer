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

package net.consensys.linea.zktracer.exceptions;

import static net.consensys.linea.zktracer.Trace.*;
import static net.consensys.linea.zktracer.exceptions.ExceptionUtils.*;
import static net.consensys.linea.zktracer.module.hub.signals.TracedException.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;
import java.util.stream.Stream;

import net.consensys.linea.UnitTestWatcher;
import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.testing.ToyAccount;
import net.consensys.linea.zktracer.module.mxp.MxpTestUtils;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Wei;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/*
In this test, we trigger all subsets possible of exceptions (except stack exceptions) at the same time for each opcode.
List of the combinations tested below
RDCX & OOGX : RETURNDATACOPY
RDCX & MXPX : RETURNDATACOPY
JUMP & OOGX : JUMP, JUMPI
STATIC & OOSX : SSTORE
STATIC & OOGX : LOG0, LOG1, LOG2, LOG3, LOG4, SSTORE, SELFDESTRUCT, CREATE, CREATE2, CALL
STATIC & MXPX : LOG0, LOG1, LOG2, LOG3, LOG4, CREATE, CREATE2, CALL
STATIC & ROOB : LOG0, LOG1, LOG2, LOG3, LOG4, CREATE, CREATE2, CALL
InvalidCodePrefix & OOGX : RETURN
InvalidCodePrefix & MaxCodeSize : RETURN
MaxCodeSize & OOGX : RETURN
InvalidCodePrefix & MaxCodeSize & OOGX : RETURN
Note : As MXPX is a subcase of OOGX, we don't test MXPX & OOGX
Note2 : For Shanghai, will need to add combinations with initcodesize exception for CREATE and CREATE2
 */

@ExtendWith(UnitTestWatcher.class)
public class MultiExceptionTest {

  @Test
  void rdcAndOogExceptionsReturnDataCopy() {
    boolean MXPX = true;
    boolean RDCX = true;
    final ToyAccount codeProviderAccount =
        getAccountForAddressWithBytecode(codeAddress, return32BytesFFBytecode);

    // We calculate gas cost without triggering RDCX, else no gas cost is calculated
    BytecodeCompiler programWithoutRdcx = getProgramRDCFromStaticCallToCodeAccount(!RDCX, !MXPX);
    BytecodeRunner bytecodeRunnerWithoutRdcx = BytecodeRunner.of(programWithoutRdcx.compile());
    long gasCostWithoutRdcx =
        bytecodeRunnerWithoutRdcx.runOnlyForGasCost(List.of(codeProviderAccount));

    // We compute the final gas cost with RDCX and OOGX trigger
    // We trigger RDCX by adding 1 to RDS
    int gasCostAddOne = 3 + 3; // Push + ADD
    long gasCostWithRdcxAndOogx = gasCostWithoutRdcx + gasCostAddOne - 1; // trigger OOGX

    // We run the program with RDCX trigger and gasCost for OOGX
    BytecodeCompiler program = getProgramRDCFromStaticCallToCodeAccount(RDCX, !MXPX);
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    bytecodeRunner.run(gasCostWithRdcxAndOogx, List.of(codeProviderAccount));

    // RDCX check happens before OOGX in tracer
    assertEquals(
        RETURN_DATA_COPY_FAULT,
        bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
  }

  @Test
  void rdcAndMxpExceptionsReturnDataCopy() {
    boolean MXPX = true;
    boolean RDCX = true;
    final ToyAccount codeProviderAccount =
        getAccountForAddressWithBytecode(codeAddress, return32BytesFFBytecode);

    // We prepare a program with RDCX and MXPX
    BytecodeCompiler program = getProgramRDCFromStaticCallToCodeAccount(RDCX, MXPX);

    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    bytecodeRunner.run(List.of(codeProviderAccount));

    // RDCX check happens before MXPX in tracer
    assertEquals(
        RETURN_DATA_COPY_FAULT,
        bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
  }

  /**
   * Trigger a jump exception and an out of gas exception. Jump exception can be triggered by a jump
   * to an invalid destination (here 5) or outside of codesize (here 6)
   */
  @ParameterizedTest
  @ValueSource(ints = {5, 6})
  void jumpAndOogExceptionsJump(int jumpCounter) {
    final Bytes bytecode =
        BytecodeCompiler.newProgram()
            .push(jumpCounter) // pc: 0 - 5 i/o 4, Trigger Jump Exception
            .op(OpCode.JUMP) // pc: 2
            .op(OpCode.INVALID) // pc: 3
            .op(OpCode.JUMPDEST) // pc: 4
            .push(OpCode.JUMPDEST.byteValue()) // pc: 5
            .compile();

    BytecodeRunner bytecodeRunner = BytecodeRunner.of(bytecode);
    // JUMP needs JUMPDEST to jump to
    // Calculate the gas cost to trigger OOGX on JUMP and not on the last but one opcode
    long gasCost = GAS_CONST_G_TRANSACTION + GAS_CONST_G_VERY_LOW;

    bytecodeRunner.run(gasCost);

    // OOGX check happens before JUMPX in tracer
    assertEquals(
        OUT_OF_GAS_EXCEPTION,
        bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
  }

  /**
   * Trigger a jump exception and an out of gas exception. Jump exception can be triggered by a
   * jumpi to an invalid destination (here 6) or outside of codesize (here 9)
   */
  @ParameterizedTest
  @ValueSource(ints = {6, 9})
  void jumpAndOogExceptionsJumpi(int jumpCounter) {
    final Bytes bytecode =
        BytecodeCompiler.newProgram()
            .push(1) // pc = 0, 1
            .push(jumpCounter) // pc = 2, 3, i/o 7, Trigger Jump Exception
            .op(OpCode.JUMPI) // pc = 4
            .op(OpCode.JUMPDEST) // pc = 5
            .op(OpCode.INVALID) // pc = 6
            .op(OpCode.JUMPDEST) // pc = 7
            .push(1) // pc = 8
            .compile();

    BytecodeRunner bytecodeRunner = BytecodeRunner.of(bytecode);

    // JUMPI needs JUMPDEST to jump to
    // Calculate the gas cost to trigger OOGX on JUMPI and not on the last but one opcode
    long gasCost = GAS_CONST_G_TRANSACTION + 2 * GAS_CONST_G_VERY_LOW;

    bytecodeRunner.run(gasCost);

    // JUMPX check happens before OOGX in tracer
    assertEquals(
        OUT_OF_GAS_EXCEPTION,
        bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
  }

  @Test
  public void staticAndOutOfSStoreExceptions() {
    BytecodeCompiler pg = BytecodeCompiler.newProgram();

    pg.push(0).push(0).op(OpCode.SSTORE);

    ToyAccount codeProviderAccount = getAccountForAddressWithBytecode(codeAddress, pg.compile());
    // Static call with gasCostToTriggerOutOfSStore gas
    // 3L PUSH + 3L PUSH + 2300 (limit for OutOfStore trigger) and we retrieve 1
    int gasCostToTriggerOutOfSStore = 3 + 3 + GAS_CONST_G_CALL_STIPEND - 1;
    BytecodeCompiler pgStaticCallToCode =
        getProgramStaticCallToCodeAddress(gasCostToTriggerOutOfSStore);

    BytecodeRunner bytecodeRunnerStaticCall = BytecodeRunner.of(pgStaticCallToCode.compile());
    bytecodeRunnerStaticCall.run(List.of(codeProviderAccount));

    // Static check happens before outOfStore exception
    assertEquals(
        STATIC_FAULT,
        bytecodeRunnerStaticCall.getHub().previousTraceSection(2).commonValues.tracedException());
  }

  @ParameterizedTest
  @MethodSource("opCodesForStaticAndOogExceptionList")
  void staticAndOogExceptions(OpCode opCode) {

    BytecodeCompiler program = simpleProgramEmptyStorage(opCode);
    Bytes pgCompile = program.compile();
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);
    long gasCostTx = bytecodeRunner.runOnlyForGasCost();

    /*
    for CREATE/CREATE2, Static Exception happens before deployment, so we test OOGX before deployment
    We remove 6400 (depositFee) + deployment code exec cost (18) from gas cost calculated
     */
    int[] cornerCaseList =
        (opCode == OpCode.CREATE || opCode == OpCode.CREATE2) ? new int[] {-6419} : new int[] {-1};

    for (int cornerCase : cornerCaseList) {
      // We calculate gas cost to trigger OOGX
      int gasCostMinusCornerCase = (int) gasCostTx - GAS_CONST_G_TRANSACTION + cornerCase;

      // We prepare a program with a static call to code account
      ToyAccount codeProviderAccount = getAccountForAddressWithBytecode(codeAddress, pgCompile);
      BytecodeCompiler pgStaticCallToCode =
          getProgramStaticCallToCodeAddress(gasCostMinusCornerCase);

      // Run with linea block gas limit so gas cost is passed to child without 63/64
      BytecodeRunner bytecodeRunnerStaticCall = BytecodeRunner.of(pgStaticCallToCode.compile());
      bytecodeRunnerStaticCall.run(List.of(codeProviderAccount));

      // Static check happens before OOGX in tracer
      assertEquals(
          STATIC_FAULT,
          bytecodeRunnerStaticCall.getHub().previousTraceSection(2).commonValues.tracedException());
    }
  }

  static Stream<OpCode> opCodesForStaticAndOogExceptionList() {
    List<OpCode> opCodesListArgument =
        Arrays.asList(
            OpCode.LOG0,
            OpCode.LOG1,
            OpCode.LOG2,
            OpCode.LOG3,
            OpCode.LOG4,
            OpCode.SSTORE,
            OpCode.SELFDESTRUCT,
            OpCode.CREATE,
            OpCode.CREATE2);
    return opCodesListArgument.stream();
  }

  @ParameterizedTest
  @MethodSource("opCodesForStaticAndMxpExceptionList")
  public void staticAndMxpExceptions(OpCode opCode) {
    // We test with or without Roob
    boolean[] triggerRoob = new boolean[] {false, true};

    for (boolean roob : triggerRoob) {
      // We prepare a program with an MXPX for the opcode
      BytecodeCompiler pg = BytecodeCompiler.newProgram();
      new MxpTestUtils().triggerNonTrivialButMxpxOrRoobForOpCode(pg, roob, opCode);

      // We prepare a program to static call the code account
      ToyAccount codeProviderAccount = getAccountForAddressWithBytecode(codeAddress, pg.compile());
      BytecodeCompiler pgStaticCallToCode = getProgramStaticCallToCodeAccount();

      // We run the program to static call the account with MXPX code
      BytecodeRunner bytecodeRunnerStaticCall = BytecodeRunner.of(pgStaticCallToCode.compile());
      bytecodeRunnerStaticCall.run(List.of(codeProviderAccount));

      // Static check happens before MXPX
      assertEquals(
          STATIC_FAULT,
          bytecodeRunnerStaticCall.getHub().previousTraceSection(2).commonValues.tracedException());
    }
  }

  static Stream<OpCode> opCodesForStaticAndMxpExceptionList() {
    List<OpCode> opCodesListArgument =
        Arrays.asList(
            OpCode.LOG0,
            OpCode.LOG1,
            OpCode.LOG2,
            OpCode.LOG3,
            OpCode.LOG4,
            OpCode.CREATE,
            OpCode.CREATE2,
            OpCode.CALL);
    return opCodesListArgument.stream();
  }

  @ParameterizedTest
  @MethodSource("addExistsAndIsWarmCallSource")
  /*
  When value is transferred
  -> Add additional call stipend (2300) to avoid OOGX in order to complete the call execution, even if no code is executed
   */
  void staticAndOogExceptionsCall(boolean targetAddressExists, boolean isWarm) {
    // value has to be > 0 for static exception to be triggered on CALL
    int value = 1;
    // call stipend - 1
    int cornerCase = 2299;
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    if (targetAddressExists && isWarm) {
      // Note: this is a possible way to warm the address
      program.push("ca11ee").op(OpCode.BALANCE);
    }

    program
        .push(0) // return at capacity
        .push(0) // return at offset
        .push(0) // call data size
        .push(0) // call data offset
        .push(value) // value
        .push("ca11ee") // address
        .push(0) // gas for subcontext (floored at 2300)
        .op(OpCode.CALL);

    Bytes pgCompile = program.compile();
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);
    long gasCost;
    BytecodeRunner bytecodeRunnerStaticCall;

    ToyAccount CallProviderAccount = getAccountForAddressWithBytecode(codeAddress, pgCompile);

    if (targetAddressExists) {
      final ToyAccount calleeAccount =
          ToyAccount.builder()
              .balance(Wei.fromEth(1))
              .nonce(10)
              .address(Address.fromHexString("ca11ee"))
              .build();
      gasCost = bytecodeRunner.runOnlyForGasCost(List.of(calleeAccount));
      // We calculate gas cost to trigger OOGX
      // We retrieve the gas cost of the transaction as it's the gas used for the static call, so
      // intrinsic gas cost already accounted
      int gasCostPlusCornerCase = (int) gasCost + cornerCase - GAS_CONST_G_TRANSACTION;
      BytecodeCompiler pgStaticCallToCode =
          getProgramStaticCallToCodeAddress(gasCostPlusCornerCase);
      bytecodeRunnerStaticCall = BytecodeRunner.of(pgStaticCallToCode.compile());
      bytecodeRunnerStaticCall.run(List.of(calleeAccount, CallProviderAccount));
    } else {
      gasCost = bytecodeRunner.runOnlyForGasCost();
      // We calculate gas cost to trigger OOGX
      // We retrieve the gas cost of the transaction as it's the gas used for the static call, so
      // intrinsic gas cost already accounted
      int gasCostPlusCornerCase = (int) gasCost + cornerCase - GAS_CONST_G_TRANSACTION;
      BytecodeCompiler pgStaticCallToCode =
          getProgramStaticCallToCodeAddress(gasCostPlusCornerCase);
      bytecodeRunnerStaticCall = BytecodeRunner.of(pgStaticCallToCode.compile());
      bytecodeRunnerStaticCall.run(gasCost + cornerCase, List.of(CallProviderAccount));
    }

    assertEquals(
        STATIC_FAULT,
        bytecodeRunnerStaticCall.getHub().previousTraceSection(2).commonValues.tracedException());
  }

  static Stream<Arguments> addExistsAndIsWarmCallSource() {
    List<Arguments> arguments = new ArrayList<>();
    arguments.add(Arguments.of(true, true));
    arguments.add(Arguments.of(true, false));
    arguments.add(Arguments.of(false, false));
    return arguments.stream();
  }

  @Test
  void invalidCodePrefixAndOogExceptionForCreate() {
    // We run gas cost calculation on program without Invalid Code Prefix exception
    int startByte = 0xee;
    BytecodeCompiler programWithoutICP =
        getPgCreateInitCodeWithReturnStartByteAndSize(startByte, 1);
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(programWithoutICP.compile());
    long gascost = bytecodeRunner.runOnlyForGasCost();

    // We prepare program with Invalid Code Prefix exception
    int startByteWithICPX = EIP_3541_MARKER;
    BytecodeCompiler programWithICP =
        getPgCreateInitCodeWithReturnStartByteAndSize(startByteWithICPX, 1);

    // We run program with Invalid Code Prefix and OOG exception
    long gasCostMinusOne = gascost - 2;
    BytecodeRunner bytecodeRunnerWithICP = BytecodeRunner.of(programWithICP.compile());
    bytecodeRunnerWithICP.run(gasCostMinusOne);

    // OOGX check is done prior to Invalid Code Prefix exception in tracer
    assertEquals(
        OUT_OF_GAS_EXCEPTION,
        bytecodeRunnerWithICP.getHub().previousTraceSection(2).commonValues.tracedException());
  }

  @Test
  void initCodePrefixAndMaxCodeSizeExceptionForCreate() {
    // We prepare program with Invalid Code Prefix and Max Code Size exceptions
    int startByteWithICPX = EIP_3541_MARKER;
    int returnSize = MAX_CODE_SIZE + 1;
    BytecodeCompiler programWithICPXAndMCSX =
        getPgCreateInitCodeWithReturnStartByteAndSize(startByteWithICPX, returnSize);

    BytecodeRunner bytecodeRunnerWithICPXAndMCSX =
        BytecodeRunner.of(programWithICPXAndMCSX.compile());
    bytecodeRunnerWithICPXAndMCSX.run();

    // Max Code Size Exception check is done prior to Invalid Code Prefix exception in tracer
    assertEquals(
        MAX_CODE_SIZE_EXCEPTION,
        bytecodeRunnerWithICPXAndMCSX
            .getHub()
            .previousTraceSection(2)
            .commonValues
            .tracedException());
  }

  @Test
  void maxCodeSizeAndOogExceptionForCreate() {
    BytecodeCompiler initProgram = BytecodeCompiler.newProgram();
    initProgram.push(MAX_CODE_SIZE + 1).push(0).op(OpCode.RETURN);
    final String initProgramAsString = initProgram.compile().toString().substring(2);
    final int initProgramByteSize = initProgram.compile().size();

    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        .push(initProgramAsString + "00".repeat(32 - initProgramByteSize))
        .push(0)
        .op(OpCode.MSTORE)
        .push(initProgramByteSize)
        .push(0)
        .push(0)
        .op(OpCode.CREATE);

    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    // We run the program with a gas cost that triggers OOGX
    // We calculate all opcodes gas before the RETURN opcode
    // 32027L = 3L PUSH + 3L PUSH + 6L MSTORE + 3L PUSH + 3L PUSH + 3L PUSH + 32000L CREATE +
    // ((32027-3-3-6-3-3-3-32000/64))(less than 0.5 so not adding gas) + 3L PUSH + 3L PUSH
    // 21000L for the intrinsic transaction cost
    bytecodeRunner.run(32027L + 21000L);

    // Max Code Size Exception check before OOGX in tracer
    assertEquals(
        MAX_CODE_SIZE_EXCEPTION,
        bytecodeRunner.getHub().previousTraceSection(2).commonValues.tracedException());
  }

  @Test
  void initCodePrefixAndMaxCodeSizeAndOogExceptionForCreate() {
    // We prepare program with Invalid Code Prefix and Max Code Size exceptions
    int startByteWithICPX = EIP_3541_MARKER;
    int returnSize = MAX_CODE_SIZE + 1;
    BytecodeCompiler programWithICPXAndMCSX =
        getPgCreateInitCodeWithReturnStartByteAndSize(startByteWithICPX, returnSize);

    BytecodeRunner bytecodeRunnerWithICPXAndMCSX =
        BytecodeRunner.of(programWithICPXAndMCSX.compile());
    // We run the program with a gas cost that triggers OOGX
    // We calculate all opcodes gas before the RETURN opcode
    // 32036L = 3L PUSH + 3L PUSH + 6L MSTORE + 3L PUSH + 3L PUSH + 3L PUSH + 32000L CREATE +
    // ((32036-3-3-6-3-3-3-32000)/64)(less than 0.5 so not adding gas) + 3L PUSH + 3L PUSH + 6L
    // MSTORE8 + 3L PUSH + 3L PUSH
    // 21000L for the intrinsic transaction cost
    bytecodeRunnerWithICPXAndMCSX.run(32039L + 21000L);

    // Max Code Size Exception check is done prior to OOGX and Invalid Code Prefix exception in
    // tracer
    assertEquals(
        MAX_CODE_SIZE_EXCEPTION,
        bytecodeRunnerWithICPXAndMCSX
            .getHub()
            .previousTraceSection(2)
            .commonValues
            .tracedException());
  }
}
