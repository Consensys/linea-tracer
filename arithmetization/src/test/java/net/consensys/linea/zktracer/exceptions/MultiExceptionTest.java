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

import java.util.List;

import net.consensys.linea.UnitTestWatcher;
import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.testing.ToyAccount;
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

  @Test
  void rdcxAndOogxReturnDataCopy() {
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

    // Rdcx check happens before Oogx in Besu
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
  void jumpxAndOogxJump(int jumpCounter) {
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
  void jumpixAndOogxJumpi(int jumpCounter) {
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

    // Jumpx check happens before Oogx in Besu
    assertEquals(
        JUMP_FAULT, bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
  }

  @Test
  void staticAndOogxExceptionLog0() {
    BytecodeCompiler programLog0 = BytecodeCompiler.newProgram();
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    programLog0
        .push(0) // size
        .push(0) //  offset
        .op(OpCode.LOG0);

    final ToyAccount Log0ProviderAccount =
        ToyAccount.builder()
            .balance(Wei.fromEth(1))
            .nonce(10)
            .address(Address.fromHexString("c0de"))
            // Constructor that returns 32 FF
            .code(programLog0.compile())
            .build();

    BytecodeRunner bytecodeRunnerLog = BytecodeRunner.of(programLog0.compile());
    long gasCostTx = bytecodeRunnerLog.runOnlyForGasCost();
    int gasCostLogPgMinusOne = (int) gasCostTx - GAS_CONST_G_TRANSACTION - 1;

    program
        // 1. Execute static call
        .push(0) // byte size of return data
        .push(0) // retOffset
        .push(0) // byte size calldata
        .push(0) // argsOffset
        .push("c0de") // Address of 'return data provider' account
        .push(gasCostLogPgMinusOne) // gas
        .op(OpCode.STATICCALL);

    /*    long gasCost =
    3 * 4 + 3 + 2 + 2600 + 3 + 3 + 375 + 21000
            + 6; // 1/64 of 386 gas cost left when we enter child frame*/

    Bytes pgCompile = program.compile();
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);
    // Run with linea block gas limit
    bytecodeRunner.run(List.of(Log0ProviderAccount));

    // Static check happens before Oogx in Besu
    assertEquals(
        STATIC_FAULT,
        bytecodeRunner.getHub().previousTraceSection(2).commonValues.tracedException());
  }
}
