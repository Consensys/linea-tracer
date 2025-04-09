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
Note : As MXPX is a subcase of OOGX, we don't test MXPX & OOGX
Note2 : For Shanghai, will need to add combinations with initcodesize exception for CREATE and CREATE2
 */

@ExtendWith(UnitTestWatcher.class)
public class MultiExceptionTest {

  @Test
  void rdcAndOogExceptionsReturnDataCopy() {
    boolean MXPX = true;
    boolean RDCX = true;
    final ToyAccount codeProviderAccount = getAccountForCodeAddress(return32BytesFFBytecode);

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
    final ToyAccount codeProviderAccount = getAccountForCodeAddress(return32BytesFFBytecode);

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
    long gasCost = GAS_CONST_G_TRANSACTION + GAS_CONST_G_VERY_LOW + GAS_CONST_G_MID;

    bytecodeRunner.run(gasCost);

    // JUMPX check happens before OOGX in tracer
    assertEquals(
        JUMP_FAULT, bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
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
    long gasCost = GAS_CONST_G_TRANSACTION + 2 * GAS_CONST_G_VERY_LOW + GAS_CONST_G_HIGH;

    bytecodeRunner.run(gasCost);

    // JUMPX check happens before OOGX in tracer
    assertEquals(
        JUMP_FAULT, bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
  }

  @Test
  public void staticAndOutOfSStoreExceptions() {
    BytecodeCompiler pg = BytecodeCompiler.newProgram();

    pg.push(0).push(0).op(OpCode.SSTORE);

    int gasCostToTriggerOutOfSStore = 3 + 3 + GAS_CONST_G_CALL_STIPEND - 1;
    // 21000L is the intrinsic gas cost of a transaction and 3L is the gas cost of PUSH1

    ToyAccount codeProviderAccount = getAccountForCodeAddress(pg.compile());
    // Static call with gasCostToTriggerOutOfSStore gas
    BytecodeCompiler pgStaticCallToCode = getPgStaticCallToCodeAddress(gasCostToTriggerOutOfSStore);

    BytecodeRunner bytecodeRunnerStaticCall = BytecodeRunner.of(pgStaticCallToCode.compile());
    bytecodeRunnerStaticCall.run(List.of(codeProviderAccount));

    // Static check happens before outOfStore exception
    assertEquals(
        STATIC_FAULT,
        bytecodeRunnerStaticCall.getHub().previousTraceSection(2).commonValues.tracedException());
  }

  /*
  For CREATE and CREATE2 deployment code: "0x7F7EFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF60005260206000F3"
  1. OOGX for CREATE/CREATE2 before deployment: remove 6400 (depositFee) + deployment code exec cost (18)
  2. OOGX for CREATE/CREATE2 after deployment: enough gas for child creation, but not enough to complete deployment code or deposit
   */
  @ParameterizedTest
  @MethodSource("opCodesForStaticAndOogExceptionList")
  void staticAndOogExceptions(OpCode opCode) {

    BytecodeCompiler program = simpleProgramEmptyStorage(opCode);
    Bytes pgCompile = program.compile();
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);
    long gasCostTx = bytecodeRunner.runOnlyForGasCost();

    int[] cornerCaseList =
        (opCode == OpCode.CREATE || opCode == OpCode.CREATE2)
            ? new int[] {-6419, 100}
            : new int[] {-1};

    for (int cornerCase : cornerCaseList) {
      // We calculate gas cost to trigger OOGX
      int gasCostMinusCornerCase = (int) gasCostTx - GAS_CONST_G_TRANSACTION + cornerCase;

      // We prepare a program with a static call to code account
      ToyAccount codeProviderAccount = getAccountForCodeAddress(pgCompile);
      BytecodeCompiler pgStaticCallToCode = getPgStaticCallToCodeAddress(gasCostMinusCornerCase);

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
    // TODO : to check
    boolean triggerRoob = false;

    // We prepare a program with an MXPX for the opcode
    BytecodeCompiler pg = BytecodeCompiler.newProgram();
    new MxpTestUtils().triggerNonTrivialButMxpxOrRoobForOpCode(pg, triggerRoob, opCode);

    // We prepare a program to static call the code account
    ToyAccount codeProviderAccount = getAccountForCodeAddress(pg.compile());
    BytecodeCompiler pgStaticCallToCode = getPgStaticCallToCodeAccount();

    // We run the program to static call the account with MXPX code
    BytecodeRunner bytecodeRunnerStaticCall = BytecodeRunner.of(pgStaticCallToCode.compile());
    bytecodeRunnerStaticCall.run(List.of(codeProviderAccount));

    // Static check happens before MXPX
    assertEquals(
        STATIC_FAULT,
        bytecodeRunnerStaticCall.getHub().previousTraceSection(2).commonValues.tracedException());
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

    ToyAccount CallProviderAccount = getAccountForCodeAddress(pgCompile);

    if (targetAddressExists) {
      final ToyAccount calleeAccount =
          ToyAccount.builder()
              .balance(Wei.fromEth(1))
              .nonce(10)
              .address(Address.fromHexString("ca11ee"))
              .build();
      gasCost = bytecodeRunner.runOnlyForGasCost(List.of(calleeAccount));
      int gasCostPlusCornerCase = (int) gasCost + cornerCase;
      BytecodeCompiler pgStaticCallToCode = getPgStaticCallToCodeAddress(gasCostPlusCornerCase);
      bytecodeRunnerStaticCall = BytecodeRunner.of(pgStaticCallToCode.compile());
      bytecodeRunnerStaticCall.run(List.of(calleeAccount, CallProviderAccount));
    } else {
      gasCost = bytecodeRunner.runOnlyForGasCost();
      int gasCostPlusCornerCase = (int) gasCost + cornerCase - GAS_CONST_G_TRANSACTION;
      BytecodeCompiler pgStaticCallToCode = getPgStaticCallToCodeAddress(gasCostPlusCornerCase);
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
}
