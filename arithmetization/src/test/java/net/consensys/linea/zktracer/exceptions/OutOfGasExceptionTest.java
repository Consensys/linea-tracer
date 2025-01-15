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

import static net.consensys.linea.testing.ToyExecutionEnvironmentV2.DEFAULT_BLOCK_NUMBER;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.GAS_CONST_G_CALL_VALUE;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.GAS_CONST_G_COLD_ACCOUNT_ACCESS;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.GAS_CONST_G_COLD_SLOAD;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.GAS_CONST_G_NEW_ACCOUNT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.GAS_CONST_G_SSET;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.GAS_CONST_G_TRANSACTION;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.GAS_CONST_G_VERY_LOW;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.GAS_CONST_G_WARM_ACCESS;
import static net.consensys.linea.zktracer.module.hub.signals.TracedException.OUT_OF_GAS_EXCEPTION;
import static net.consensys.linea.zktracer.opcode.OpCodes.opCodeToOpCodeDataMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.google.common.base.Preconditions;
import net.consensys.linea.UnitTestWatcher;
import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.testing.ToyAccount;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.opcode.OpCodeData;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Wei;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

@ExtendWith(UnitTestWatcher.class)
public class OutOfGasExceptionTest {

  // TODO: can we generalize this test to treat all opcodes, warm and cold and types of gas costs?
  @ParameterizedTest
  @MethodSource("outOfGasExceptionColdWithPositiveStaticCostAndNoMemoryExpansionCostSource")
  void outOfGasExceptionColdWithPositiveStaticCostAndNoMemoryExpansionCostTest(
      OpCode opCode, int opCodeStaticCost, int nPushes, int corneCase) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    for (int i = 0; i < nPushes; i++) {
      // In order to disambiguate between empty stack items and writing a result of 0 on the stack
      // we push small integers to the stack which all produce non-zero results

      int pushedValue =
          switch (opCode) {
            case OpCode.BLOCKHASH -> Math.toIntExact(DEFAULT_BLOCK_NUMBER) - 1;
            case OpCode.SELFDESTRUCT -> 100; // address of an empty account
            case OpCode.EXP -> i == 0 ? 5 : 2; // EXP 2 5 (2 ** 5)
            default -> 7 * i + 5;
          };
      program.push(pushedValue);
    }
    program.op(opCode);
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    int opCodeDynamicCost =
        switch (opCode) {
          case OpCode.SELFDESTRUCT -> GAS_CONST_G_NEW_ACCOUNT
              + GAS_CONST_G_COLD_ACCOUNT_ACCESS; // since the account is empty
          case OpCode.EXP -> 50; // since the exponent requires 1 byte
          default -> 0;
        };

    bytecodeRunner.run(
        (long) GAS_CONST_G_TRANSACTION
            + (long) nPushes * GAS_CONST_G_VERY_LOW
            + opCodeStaticCost
            + opCodeDynamicCost
            + corneCase);
    if (corneCase == -1) {
      assertEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    } else {
      assertNotEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    }
  }

  static Stream<Arguments>
      outOfGasExceptionColdWithPositiveStaticCostAndNoMemoryExpansionCostSource() {
    List<Arguments> arguments = new ArrayList<>();
    for (OpCodeData opCodeData : opCodeToOpCodeDataMap.values()) {
      OpCode opCode = opCodeData.mnemonic();
      int opCodeStaticCost = opCodeData.stackSettings().staticGas().cost();
      int nPushes = opCodeData.stackSettings().delta(); // number of items popped from the stack
      if (opCodeStaticCost > 0
          && opCode != OpCode.MLOAD // MLOAD needs the memory expansion cost
          && opCode != OpCode.MSTORE8 // MSTORE8 needs the memory expansion cost
          && opCode != OpCode.MSTORE // MSTORE needs the memory expansion cost
          && opCode != OpCode.RETURNDATACOPY // RETURNDATACOPY needs the memory expansion cost
          && opCode != OpCode.CALLDATACOPY // CALLDATACOPY needs the memory expansion cost
          && opCode != OpCode.CODECOPY // CODECOPY needs the memory expansion cost
          && opCode != OpCode.SHA3 // SHA3 needs the memory expansion cost
          && !opCodeData.isCreate() // CREATE needs the memory expansion cost
          && !opCodeData.isLog()) // LOG needs the memory expansion cost
      {
        arguments.add(Arguments.of(opCode, opCodeStaticCost, nPushes, -1));
        arguments.add(Arguments.of(opCode, opCodeStaticCost, nPushes, 0));
        arguments.add(Arguments.of(opCode, opCodeStaticCost, nPushes, 1));
      }
    }
    return arguments.stream();
  }

  @ParameterizedTest
  @MethodSource("outOfGasExceptionCallSource")
  void outOfGasExceptionCallTest(
      int value, boolean targetAddressExists, boolean isWarm, int cornerCase) {
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
        .push(1000) // gas
        .op(OpCode.CALL);

    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());

    long gasLimit =
        GAS_CONST_G_TRANSACTION
            + // base gas cost
            (isWarm ? GAS_CONST_G_VERY_LOW + GAS_CONST_G_COLD_ACCOUNT_ACCESS : 0) // PUSH + BALANCE
            + 7 * GAS_CONST_G_VERY_LOW // 7 PUSH
            + callGasCost(value != 0, targetAddressExists, isWarm); // CALL

    if (targetAddressExists) {
      final ToyAccount calleeAccount =
          ToyAccount.builder()
              .balance(Wei.fromEth(1))
              .nonce(10)
              .address(Address.fromHexString("ca11ee"))
              .build();
      bytecodeRunner.run(gasLimit + cornerCase, List.of(calleeAccount));
    } else {
      bytecodeRunner.run(gasLimit + cornerCase);
    }

    if (cornerCase == -1) {
      assertEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    } else {
      assertNotEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    }
  }

  static Stream<Arguments> outOfGasExceptionCallSource() {
    List<Arguments> arguments = new ArrayList<>();
    for (int value : new int[] {0, 1}) {
      for (int cornerCase : new int[] {-1, 0, 1}) {
        arguments.add(Arguments.of(value, true, true, cornerCase));
        arguments.add(Arguments.of(value, true, false, cornerCase));
        arguments.add(Arguments.of(value, false, false, cornerCase));
      }
    }
    return arguments.stream();
  }

  private long callGasCost(boolean transfersValue, boolean targetAddressExists, boolean isWarm) {
    Preconditions.checkArgument(
        !(isWarm && !targetAddressExists), "isWarm implies targetAddressExists");
    return (transfersValue ? GAS_CONST_G_CALL_VALUE : 0)
        + (targetAddressExists ? 0 : (transfersValue ? GAS_CONST_G_NEW_ACCOUNT : 0))
        + (isWarm ? GAS_CONST_G_WARM_ACCESS : GAS_CONST_G_COLD_ACCOUNT_ACCESS);
  }

  /**
   * We provide a non-zero value in storage so to disambiguate between writing the value in storage
   * to the stack and writing 0 to the stack.
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 0, 1})
  void outOfGasExceptionSStore(int cornerCase) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        .push(2) // value
        .push(1) // key
        .op(OpCode.SSTORE);

    program
        .push(1)
        . // key
        op(OpCode.SLOAD);

    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());

    bytecodeRunner.run(
        (long) GAS_CONST_G_TRANSACTION
            + (long) 2 * GAS_CONST_G_VERY_LOW // 2 PUSH
            + GAS_CONST_G_SSET
            // SSTORE cost since current_value == original_value
            // and original_value == 0 (20000)
            + GAS_CONST_G_COLD_SLOAD // SSTORE cost since slot is cold (2100)
            + (long) GAS_CONST_G_VERY_LOW // PUSH
            + GAS_CONST_G_WARM_ACCESS
            + cornerCase); // SLOAD (100)

    if (cornerCase == -1) {
      assertEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    } else {
      assertNotEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    }
  }
}
