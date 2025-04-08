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
import org.junit.jupiter.params.provider.ValueSource;

@ExtendWith(UnitTestWatcher.class)
public class MultiExceptionTest {
  // For Shanghai, will need to add initcodesize tests for CREATE and CREATE2

  @Test
  void rdcAndOogExceptionsReturnDataCopy() {
    BytecodeCompiler programWithoutRdcx = BytecodeCompiler.newProgram();
    BytecodeCompiler program = BytecodeCompiler.newProgram();
    BytecodeCompiler programRdcx = BytecodeCompiler.newProgram();
    BytecodeCompiler postRdcxrogram = BytecodeCompiler.newProgram();

    final ToyAccount returnDataProviderAccount =
        ToyAccount.builder()
            .balance(Wei.fromEth(1))
            .nonce(10)
            .address(Address.fromHexString("c0de"))
            // Constructor that returns 32 FF
            .code(
                Bytes.fromHexString(
                    "7fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff60005260206000f3"))
            .build();

    programWithoutRdcx
        // 1. Execute static call
        .push(0) // byte size of return data
        .push(0) // retOffset
        .push(0) // byte size calldata
        .push(0) // argsOffset
        .push("c0de") // Address of 'return data provider' account
        .op(OpCode.GAS) // gas
        .op(OpCode.STATICCALL)
        // 2. Clean the stack
        .op(OpCode.POP)
        .op(OpCode.RETURNDATASIZE);

    program.concatenate(programWithoutRdcx);

    postRdcxrogram
        .push(0) // offset
        .push(65) // destoffset, trigger mem expansion
        .op(OpCode.RETURNDATACOPY);

    programRdcx
        // 4. Doubly exceptional return data copy
        .push(1)
        .op(OpCode.ADD); // size = RDS + 1, which will trigger the `returnDataCopyException`

    programWithoutRdcx.concatenate(postRdcxrogram);
    BytecodeRunner bytecodeRunnerWithoutRdcx = BytecodeRunner.of(programWithoutRdcx.compile());

    // We calculate gas cost without rdcx, else no gas cost is calculated
    long gasCostWithoutRdcx =
        bytecodeRunnerWithoutRdcx.runOnlyForGasCost(List.of(returnDataProviderAccount));

    int cornerCase = -1;
    long gasCostWithRdcx =
        gasCostWithoutRdcx
            + 3 // Push
            + 3 // ADD
            + cornerCase; // trigger oogx

    program.concatenate(programRdcx);
    program.concatenate(postRdcxrogram);
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    bytecodeRunner.run(gasCostWithRdcx, List.of(returnDataProviderAccount));

    // Rdcx check happens before Oogx in tracer
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

    long gasCost;
    // JUMPI needs JUMPDEST to jump to
    // Calculate the gas cost to trigger OOGX on JUMPI and not on the last but one opcode
    gasCost = GAS_CONST_G_TRANSACTION + 2 * GAS_CONST_G_VERY_LOW + GAS_CONST_G_HIGH;

    bytecodeRunner.run(gasCost);

    // Jumpx check happens before Oogx in tracer
    assertEquals(
        JUMP_FAULT, bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
  }

  @Test
  void staticAndOogExceptions() {
    List<BytecodeCompiler> pgLogList = new ArrayList<>();
    Bytes address1 =
        Bytes.fromHexString("0x1FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
    Bytes address2 =
        Bytes.fromHexString("0x2FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
    Bytes address3 =
        Bytes.fromHexString("0x3FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
    Bytes address4 =
        Bytes.fromHexString("0x4FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");

    Collections.addAll(
        pgLogList,
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

    for (BytecodeCompiler pgLog : pgLogList) {

      Bytes pgLogCompile = pgLog.compile();

      ToyAccount LogProviderAccount =
          ToyAccount.builder()
              .balance(Wei.fromEth(1))
              .nonce(10)
              .address(Address.fromHexString("c0de"))
              // Constructor that returns 32 FF
              .code(pgLogCompile)
              .build();

      BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgLogCompile);
      long gasCostTx = bytecodeRunner.runOnlyForGasCost();
      int gasCostMinusOne = (int) gasCostTx - GAS_CONST_G_TRANSACTION - 1;

      BytecodeCompiler pgStaticCallToCode =
          BytecodeCompiler.newProgram()
              .push(0) // byte size of return data
              .push(0) // retOffset
              .push(0) // byte size calldata
              .push(0) // argsOffset
              .push("c0de") // Address of account
              .push(gasCostMinusOne) // gas
              .op(OpCode.STATICCALL);

      /*    long gasCost =
      3 * 4 + 3 + 2 + 2600 + 3 + 3 + 375 + 21000
              + 6; // 1/64 of 386 gas cost left when we enter child frame*/

      // Run with linea block gas limit
      Bytes pgStaticCallCompile = pgStaticCallToCode.compile();
      BytecodeRunner bytecodeRunnerStaticCall = BytecodeRunner.of(pgStaticCallCompile);
      bytecodeRunnerStaticCall.run(List.of(LogProviderAccount));

      // Static check happens before Oogx in tracer
      assertEquals(
          STATIC_FAULT,
          bytecodeRunnerStaticCall.getHub().previousTraceSection(2).commonValues.tracedException());
    }
  }

  @Test
  public void staticAndMxpExceptions() {
    // TODO : to check
    boolean triggerRoob = false;
    List<OpCode> opCodesList =
        Arrays.asList(
            OpCode.LOG0,
            OpCode.LOG1,
            OpCode.LOG2,
            OpCode.LOG4,
            OpCode.CREATE,
            OpCode.CREATE2,
            OpCode.CALL);

    for (OpCode opCode : opCodesList) {
      BytecodeCompiler pg = BytecodeCompiler.newProgram();
      new MxpTestUtils().triggerNonTrivialButMxpxOrRoobForOpCode(pg, triggerRoob, opCode);

      ToyAccount LogProviderAccount =
          ToyAccount.builder()
              .balance(Wei.fromEth(1))
              .nonce(10)
              .address(Address.fromHexString("c0de"))
              .code(pg.compile())
              .build();

      BytecodeCompiler pgStaticCallToCode =
          BytecodeCompiler.newProgram()
              .push(0) // byte size of return data
              .push(0) // retOffset
              .push(0) // byte size calldata
              .push(0) // argsOffset
              .push("c0de") // Address of account
              .op(OpCode.GAS) // gas
              .op(OpCode.STATICCALL);

      // Run with linea block gas limit
      Bytes pgStaticCallCompile = pgStaticCallToCode.compile();
      BytecodeRunner bytecodeRunnerStaticCall = BytecodeRunner.of(pgStaticCallCompile);
      bytecodeRunnerStaticCall.run(List.of(LogProviderAccount));

      // Static check happens before mxp exception
      assertEquals(
          STATIC_FAULT,
          bytecodeRunnerStaticCall.getHub().previousTraceSection(2).commonValues.tracedException());
    }
  }

  @Test
  public void staticAndoutOfSStoreExceptions() {
    BytecodeCompiler pg = BytecodeCompiler.newProgram();

    pg.push(0).push(0).op(OpCode.SSTORE);
    Bytes pgCompile = pg.compile();
    int gasCostToTriggerOutOfSStore = 3 + 3 + GAS_CONST_G_CALL_STIPEND - 1;
    // 21000L is the intrinsic gas cost of a transaction and 3L is the gas cost of PUSH1

    ToyAccount SStoreProviderAccount =
        ToyAccount.builder()
            .balance(Wei.fromEth(1))
            .nonce(10)
            .address(Address.fromHexString("c0de"))
            // Constructor that returns 32 FF
            .code(pgCompile)
            .build();

    BytecodeCompiler pgStaticCallToCode =
        BytecodeCompiler.newProgram()
            .push(0) // byte size of return data
            .push(0) // retOffset
            .push(0) // byte size calldata
            .push(0) // argsOffset
            .push("c0de") // Address of account
            .push(gasCostToTriggerOutOfSStore) // gas
            .op(OpCode.STATICCALL);

    BytecodeRunner bytecodeRunnerStaticCall = BytecodeRunner.of(pgStaticCallToCode.compile());
    bytecodeRunnerStaticCall.run(List.of(SStoreProviderAccount));
    // Static check happens before outOfStore exception
    assertEquals(
        STATIC_FAULT,
        bytecodeRunnerStaticCall.getHub().previousTraceSection(2).commonValues.tracedException());
  }
}
