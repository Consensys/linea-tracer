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
import static net.consensys.linea.zktracer.module.hub.signals.TracedException.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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

@ExtendWith(UnitTestWatcher.class)
public class MultiExceptionTest {
  // For Shanghai, will need to add initcodesize tests for CREATE and CREATE2

  @Test
  void rdcAndOogExceptionsReturnDataCopy() {

    final ToyAccount codeProviderAccount =
        getAccountWithCode(
            Bytes.fromHexString(
                "7fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff60005260206000f3"));

    // We calculate gas cost without triggering RDCX (programAddOne), else no gas cost is calculated
    BytecodeCompiler programWithoutRdcx =
        getProgramRDC(false, Bytes.ofUnsignedLong(65).trimLeadingZeros());
    BytecodeRunner bytecodeRunnerWithoutRdcx = BytecodeRunner.of(programWithoutRdcx.compile());
    long gasCostWithoutRdcx =
        bytecodeRunnerWithoutRdcx.runOnlyForGasCost(List.of(codeProviderAccount));

    // We compute the final gas cost with RDCX and OOGX trigger
    int gasCostAddOne = 3 + 3; // Push + ADD
    long gasCostWithRdcx = gasCostWithoutRdcx + gasCostAddOne - 1; // trigger OOGX

    // We run the program with RDCX trigger and gasCost for OOGX
    BytecodeCompiler program = getProgramRDC(true, Bytes.ofUnsignedLong(65).trimLeadingZeros());
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    bytecodeRunner.run(gasCostWithRdcx, List.of(codeProviderAccount));

    // RDCX check happens before OOGX in tracer
    assertEquals(
        RETURN_DATA_COPY_FAULT,
        bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
  }

  @Test
  void rdcAndMxpExceptionsReturnDataCopy() {
    final ToyAccount codeProviderAccount =
        getAccountWithCode(
            Bytes.fromHexString(
                "7fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff60005260206000f3"));

    // We prepare a program with RDCX and MXPX using the following offset
    BytecodeCompiler program = getProgramRDC(true, Bytes.fromHexStringLenient("0xFFFFFFFF"));

    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    bytecodeRunner.run(List.of(codeProviderAccount));

    // RDCX check happens before OOGX in tracer
    assertEquals(
        RETURN_DATA_COPY_FAULT,
        bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
  }

  /**
   * Trigger a jump exception and an out of gas exception Jump exception can be triggered by a jump
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
   * Trigger a jumpi exception and an out of gas exception. Jumpi exception can be triggered by a
   * jump to an invalid destination (here 6) or outside of codesize (here 9)
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
  void staticAndOogExceptions() {
    List<BytecodeCompiler> pgList = new ArrayList<>();
    Bytes address1 =
        Bytes.fromHexString("0x1FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
    Bytes address2 =
        Bytes.fromHexString("0x2FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
    Bytes address3 =
        Bytes.fromHexString("0x3FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
    Bytes address4 =
        Bytes.fromHexString("0x4FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");

    Collections.addAll(
        pgList,
        BytecodeCompiler.newProgram()
            .push(32) // size
            .push(1) //  offset
            .op(OpCode.LOG0),
        BytecodeCompiler.newProgram()
            .push(address1) // Topic 1
            .push(32) // size
            .push(1) // offset to trigger mem expansion
            .op(OpCode.LOG1),
        BytecodeCompiler.newProgram()
            .push(address2) // Topic 2
            .push(address1) // Topic 1
            .push(32) // size
            .push(1) // offset to trigger mem expansion
            .op(OpCode.LOG2),
        BytecodeCompiler.newProgram()
            .push(address3) // Topic 3
            .push(address2) // Topic 2
            .push(address1) // Topic 1
            .push(32) // size
            .push(1) // offset to trigger mem expansion
            .op(OpCode.LOG3),
        BytecodeCompiler.newProgram()
            .push(address4) // Topic 4
            .push(address3) // Topic 3
            .push(address2) // Topic 2
            .push(address1) // Topic 1
            .push(32) // size
            .push(1) // offset to trigger mem expansion
            .op(OpCode.LOG4),
        BytecodeCompiler.newProgram()
            .push(2) // value
            .push(1) // key
            .op(OpCode.SSTORE),
        BytecodeCompiler.newProgram().push(0).op(OpCode.SELFDESTRUCT));

    for (BytecodeCompiler pg : pgList) {

      Bytes pgCompile = pg.compile();

      // We calculate gas cost to trigger OOGX
      BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);
      long gasCostTx = bytecodeRunner.runOnlyForGasCost();
      int gasCostMinusOne = (int) gasCostTx - GAS_CONST_G_TRANSACTION - 1;

      // We prepare a program with a static call to code account
      ToyAccount codeProviderAccount = getAccountWithCode(pgCompile);
      BytecodeCompiler pgStaticCallToCode = getPgStaticCallToCodeAccount(gasCostMinusOne);

      // Run with linea block gas limit so gas cost is passed to child without 63/64
      BytecodeRunner bytecodeRunnerStaticCall = BytecodeRunner.of(pgStaticCallToCode.compile());
      bytecodeRunnerStaticCall.run(List.of(codeProviderAccount));

      // Static check happens before OOGX in tracer
      assertEquals(
          STATIC_FAULT,
          bytecodeRunnerStaticCall.getHub().previousTraceSection(2).commonValues.tracedException());
    }
  }

  @ParameterizedTest
  @MethodSource("opCodesForStaticAndMxpExceptionList")
  public void staticAndMxpExceptions(OpCode opCode) {
    // TODO : to check
    boolean triggerRoob = false;

    BytecodeCompiler pg = BytecodeCompiler.newProgram();
    new MxpTestUtils().triggerNonTrivialButMxpxOrRoobForOpCode(pg, triggerRoob, opCode);

    ToyAccount codeProviderAccount = getAccountWithCode(pg.compile());
    BytecodeCompiler pgStaticCallToCode = getPgStaticCallToCodeAccount();

    // Run with linea block gas limit
    BytecodeRunner bytecodeRunnerStaticCall = BytecodeRunner.of(pgStaticCallToCode.compile());
    bytecodeRunnerStaticCall.run(List.of(codeProviderAccount));

    // Static check happens before mxp exception
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
            OpCode.LOG4,
            OpCode.CREATE,
            OpCode.CREATE2,
            OpCode.CALL);
    return opCodesListArgument.stream();
  }

  @Test
  public void staticAndoutOfSStoreExceptions() {
    BytecodeCompiler pg = BytecodeCompiler.newProgram();

    pg.push(0).push(0).op(OpCode.SSTORE);
    Bytes pgCompile = pg.compile();
    int gasCostToTriggerOutOfSStore = 3 + 3 + GAS_CONST_G_CALL_STIPEND - 1;
    // 21000L is the intrinsic gas cost of a transaction and 3L is the gas cost of PUSH1

    ToyAccount SStoreProviderAccount = getAccountWithCode(pgCompile);

    BytecodeCompiler pgStaticCallToCode = getPgStaticCallToCodeAccount(gasCostToTriggerOutOfSStore);

    BytecodeRunner bytecodeRunnerStaticCall = BytecodeRunner.of(pgStaticCallToCode.compile());
    bytecodeRunnerStaticCall.run(List.of(SStoreProviderAccount));
    // Static check happens before outOfStore exception
    assertEquals(
        STATIC_FAULT,
        bytecodeRunnerStaticCall.getHub().previousTraceSection(2).commonValues.tracedException());
  }

  @ParameterizedTest
  @ValueSource(ints = {-6419, 100})
  /*
  Deployment code: "0x7F7EFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF60005260206000F3"
  1. OOGX for CREATE before deployment: remove 6400 (depositFee) + deployment code exec cost (18)
  2. OOGX for CREATE after deployment: enough gas for child creation, but not enough to complete deployment code or deposit
   */
  void staticAndOogExceptionCreateAndCreate2(int cornerCase) {
    List<BytecodeCompiler> pgList = new ArrayList<>();
    Bytes initCodePart1 =
        Bytes.fromHexString("0x7F7EFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
    Bytes initCodePart2 =
        Bytes.fromHexString("0xFF60005260206000F30000000000000000000000000000000000000000000000");
    Bytes salt =
        Bytes.fromHexString("0x1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef");

    BytecodeCompiler pushInitCodeToMemory =
        BytecodeCompiler.newProgram()
            .push(initCodePart1) // value
            .push(0) // offset
            .op(OpCode.MSTORE)
            .push(initCodePart2) // value
            .push(32) // offset
            .op(OpCode.MSTORE);

    Collections.addAll(
        pgList,
        pushInitCodeToMemory
            .copy()
            // Create the contract
            .push(41)
            .push(0)
            .push(0)
            .op(OpCode.CREATE), // No constructor so code executed and runtime code set to return
        // value
        pushInitCodeToMemory
            .copy()
            // Create the contract
            .push(salt) // salt
            .push(41)
            .push(0)
            .push(0)
            .op(OpCode.CREATE2)); // No constructor so code executed and runtime code set to return
    // value

    for (BytecodeCompiler pg : pgList) {
      Bytes pgCompile = pg.compile();
      BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);

      long gasCost = bytecodeRunner.runOnlyForGasCost();
      int gasCostPlusCornerCase = (int) gasCost + cornerCase - GAS_CONST_G_TRANSACTION;

      ToyAccount CreateProviderAccount = getAccountWithCode(pgCompile);

      BytecodeCompiler pgStaticCallToCode = getPgStaticCallToCodeAccount(gasCostPlusCornerCase);

      BytecodeRunner bytecodeRunnerStaticCall = BytecodeRunner.of(pgStaticCallToCode.compile());
      bytecodeRunnerStaticCall.run(List.of(CreateProviderAccount));

      // Static check happens before Oog exception
      assertEquals(
          STATIC_FAULT,
          bytecodeRunnerStaticCall.getHub().previousTraceSection(2).commonValues.tracedException());
    }
  }

  @ParameterizedTest
  @MethodSource("outOfGasExceptionCallSource")
  /*
  When value is transferred
  -> Add additional call stipend (2300) to avoid OOGX in order to complete the call execution, even if no code is executed
   */
  void outOfGasExceptionCallTest(boolean targetAddressExists, boolean isWarm) {
    // value has to be > 0 for static exception to be triggered on CALL
    int value = 1;
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

    ToyAccount CallProviderAccount = getAccountWithCode(pgCompile);

    if (targetAddressExists) {
      final ToyAccount calleeAccount =
          ToyAccount.builder()
              .balance(Wei.fromEth(1))
              .nonce(10)
              .address(Address.fromHexString("ca11ee"))
              .build();
      gasCost = bytecodeRunner.runOnlyForGasCost(List.of(calleeAccount));
      int gasCostPlusCornerCase = (int) gasCost + cornerCase;
      BytecodeCompiler pgStaticCallToCode = getPgStaticCallToCodeAccount(gasCostPlusCornerCase);
      bytecodeRunnerStaticCall = BytecodeRunner.of(pgStaticCallToCode.compile());
      bytecodeRunnerStaticCall.run(List.of(calleeAccount, CallProviderAccount));
    } else {
      gasCost = bytecodeRunner.runOnlyForGasCost();
      int gasCostPlusCornerCase = (int) gasCost + cornerCase - GAS_CONST_G_TRANSACTION;
      BytecodeCompiler pgStaticCallToCode = getPgStaticCallToCodeAccount(gasCostPlusCornerCase);
      bytecodeRunnerStaticCall = BytecodeRunner.of(pgStaticCallToCode.compile());
      bytecodeRunnerStaticCall.run(gasCost + cornerCase, List.of(CallProviderAccount));
    }

    assertEquals(
        STATIC_FAULT,
        bytecodeRunnerStaticCall.getHub().previousTraceSection(2).commonValues.tracedException());
  }

  static Stream<Arguments> outOfGasExceptionCallSource() {
    List<Arguments> arguments = new ArrayList<>();
    arguments.add(Arguments.of(true, true));
    arguments.add(Arguments.of(true, false));
    arguments.add(Arguments.of(false, false));
    return arguments.stream();
  }

  /* ***************** */
  /*  Helpers section  */
  /* ***************** */

  static ToyAccount getAccountWithCode(Bytes code) {
    return ToyAccount.builder()
        .balance(Wei.fromEth(1))
        .nonce(10)
        .address(Address.fromHexString("c0de"))
        .code(code)
        .build();
  }

  static BytecodeCompiler getPgStaticCallToCodeAccount(int gas) {
    return BytecodeCompiler.newProgram()
        .push(0) // byte size of return data
        .push(0) // retOffset
        .push(0) // byte size calldata
        .push(0) // argsOffset
        .push("c0de") // Address of account
        .push(gas) // gas
        .op(OpCode.STATICCALL);
  }

  static BytecodeCompiler getPgStaticCallToCodeAccount() {
    return BytecodeCompiler.newProgram()
        .push(0) // byte size of return data
        .push(0) // retOffset
        .push(0) // byte size calldata
        .push(0) // argsOffset
        .push("c0de") // Address of account
        .op(OpCode.GAS) // gas
        .op(OpCode.STATICCALL);
  }

  static BytecodeCompiler getProgramRDC(boolean withRDCX, Bytes offsetRDC) {
    // 1. Execute static call
    BytecodeCompiler programStartWithStaticCall = getPgStaticCallToCodeAccount();
    // 2. Clean the stack
    programStartWithStaticCall.op(OpCode.POP).op(OpCode.RETURNDATASIZE);
    // if withRDCX is true, we add the code to trigger the exception
    // 3. Trigger exceptional return data copy
    if (withRDCX) {
      programStartWithStaticCall
          .push(1)
          .op(OpCode.ADD); // size = RDS + 1, which will trigger the `returnDataCopyException`
    }
    programStartWithStaticCall
        .push(0) // offset
        .push(offsetRDC) // destoffset, trigger mem expansion
        .op(OpCode.RETURNDATACOPY);
    // Bytes.fromHexStringLenient("0xFFFFFFFF")
    return programStartWithStaticCall;
  }
}
