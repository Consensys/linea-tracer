package net.consensys.linea.zktracer.instructionprocessing.callTests.sixtyThreeSixtyFourths;

import static net.consensys.linea.zktracer.module.hub.signals.TracedException.OUT_OF_GAS_EXCEPTION;
import static org.hyperledger.besu.datatypes.Address.ALTBN128_ADD;
import static org.hyperledger.besu.datatypes.Address.ALTBN128_MUL;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.zktracer.module.constants.GlobalConstants;
import net.consensys.linea.zktracer.module.oob.OobOperation;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;
import org.junit.jupiter.api.Test;

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
public class SixtyThreeSixtyFourthsTests {

  /*
  A transaction needs to target an SMC that will:
    - expand memory by executing an MLOAD at offset 4096 - 32 = 4064
    - do a CALL type instruction to a precompile contract
    - the gas we provide to the precompile contract should cover the cases below.

      Cases to cover:
      - value = 0
      - value = 1 | targetAddressExists = false, true

      Optionally:
      - memoryExpansionBeforeCallToPrc = false, true | value = 0
      - memoryExpansionBeforeCallToPrc = false, true | value = 1 | targetAddressExists = false, true

   * Generic case:
      executionCostOfProgramBeforeFinalCallToPRC =
        value = 0 || !targetAddressExists : 21000 + MLOAD + PUSHEes
        value = 1 && targetAddressExists  : 21000 + MLOAD + PUSHEes + CALL (to send value to the precompile so as targetAddressExists = true)
      remainingGasBeforeCall = gasLimit - executionCostOfProgramBeforeFinalCallToPRC
      callOpcodeCost = 100 + (value > 0 && !targetAddressExists ? 25000 : 0) + (value > 0 ? 9000 : 0)

      For cornerCase = -1, 0 find gasLimit such that:
        providedGas = 63/64 * (remainingGasBeforeCall - callOpcodeCost) + (value > 0 ? 2300 : 0) = precompileGasCost + cornerCase

      Note that the value > 0 case is meaningful only when
      precompileGasCost + cornerCase >= 2300 as otherwise being able to pay for the call,
      that is remainingGasBeforeCall - callOpcodeCost >= 0, implies we can pay for the precompile, too (and we are
      interested in the case in which we can't pay for the precompile).

   Let x = remainingGasBeforeCall
   Let y = gasLimit

   Find y = x - executionCostOfProgramBeforeFinalCallToPRC such that:

   * value = 0
     63/64 * (x - 100) = precompileGasCost - 1, precompileGasCost

   * value = 1, targetAddressExists = false
     63/64 * (x - 100 - 25000 - 9000) + 2300 = precompileGasCost - 1, precompileGasCost

   * value = 1, targetAddressExists = true
     63/64 * (x - 100 - 9000) + 2300 = precompileGasCost - 1, precompileGasCost

   BLAKE2F is the only case that requires a input that is not 0 to have a cost greater than 2300.
   Otherwise, call data size is the only aspect we care.
   */

  final Bytes gas = Bytes.fromHexString("ff".repeat(32));

  // TODO: check the documentation here is consistent with the one in the issue and change the tests
  //  below accordingly

  @Test
  void sixtyThreeSixtyFourthsEcAddTest() {
    final BytecodeCompiler program = BytecodeCompiler.newProgram();

    program.push(4096 - 32).op(OpCode.MLOAD);
    program
        .push(0) // returnAtCapacity
        .push(0) // returnAtOffset
        .push(0) // callDataSize
        .push(0) // callDataOffset
        .push(0) // value
        .push(ALTBN128_ADD) // address
        .push(gas) // gas
        .op(OpCode.CALL);
    final BytecodeRunner bytecodeRunner = BytecodeRunner.of(program);

    final long gasCost = bytecodeRunner.runOnlyForGasCost();
    // 21693
    bytecodeRunner.run(gasCost);

    // providedGas = 63/64 * (250 - 100) + 2300 * 0 = 148 > 150
    // Indeed, without the 63/64 factor, the providedGas would be enough

    assertNotEquals(
        OUT_OF_GAS_EXCEPTION,
        bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());

    final boolean insufficientGasForPrecompile =
        bytecodeRunner.getHub().oob().operations().stream()
            .anyMatch(OobOperation::isInsufficientGasForPrecompile);
    assertTrue(insufficientGasForPrecompile);
  }

  @Test
  void sixtyThreeSixtyFourthsEcAddTestWithValue() {
    final BytecodeCompiler program = BytecodeCompiler.newProgram();

    program.push(4096 - 32).op(OpCode.MLOAD);
    program
        .push(0) // returnAtCapacity
        .push(0) // returnAtOffset
        .push(0) // callDataSize
        .push(0) // callDataOffset
        .push(1) // value
        .push(ALTBN128_ADD) // address
        .push(gas) // gas
        .op(OpCode.CALL);
    final BytecodeRunner bytecodeRunner = BytecodeRunner.of(program);

    final long gasCost = bytecodeRunner.runOnlyForGasCost();
    // 53393 = 21693 + 9000 + 25000 - 2300
    bytecodeRunner.run(gasCost + (2300 - 150));

    // providedGas =
    // 63/64 * (31950 + (2300 - 150) - 9000 - 25000 - 100) + 2300 * 1 = 2300  > 150

    // As long as we can pay for the call, we can pay for the precompile

    assertNotEquals(
        OUT_OF_GAS_EXCEPTION,
        bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());

    /*
    final boolean insufficientGasForPrecompile =
        bytecodeRunner.getHub().oob().operations().stream()
            .anyMatch(OobOperation::isInsufficientGasForPrecompile);
    assertTrue(insufficientGasForPrecompile);
    */
  }

  @Test
  void sixtyThreeSixtyFourthsEcMulTest() {
    final BytecodeCompiler program = BytecodeCompiler.newProgram();

    program.push(4096 - 32).op(OpCode.MLOAD);
    program
        .push(0) // returnAtCapacity
        .push(0) // returnAtOffset
        .push(0) // callDataSize
        .push(0) // callDataOffset
        .push(0) // value
        .push(ALTBN128_MUL) // address
        .push(gas) // gas
        .op(OpCode.CALL);
    final BytecodeRunner bytecodeRunner = BytecodeRunner.of(program);

    final long gasCost = bytecodeRunner.runOnlyForGasCost();
    // 27543
    bytecodeRunner.run(gasCost);

    // providedGas = 63/64 * (6100 - 100) + 2300 * 0 = 5907  > 6000
    // Indeed, without the 63/64 factor, the providedGas would be enough

    assertNotEquals(
        OUT_OF_GAS_EXCEPTION,
        bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());

    final boolean insufficientGasForPrecompile =
        bytecodeRunner.getHub().oob().operations().stream()
            .anyMatch(OobOperation::isInsufficientGasForPrecompile);
    assertTrue(insufficientGasForPrecompile);
  }

  private long callGasCostExcludingMemoryExpansion(
      boolean transfersValue, boolean targetAddressExists, boolean isWarm) {
    // GAS_CONST_G_CALL_VALUE = 9000
    // GAS_CONST_G_NEW_ACCOUNT = 25000
    // GAS_CONST_G_WARM_ACCESS = 100
    // GAS_CONST_G_COLD_ACCOUNT_ACCESS = 2600
    return (transfersValue ? GlobalConstants.GAS_CONST_G_CALL_VALUE : 0)
        + (targetAddressExists ? 0 : (transfersValue ? GlobalConstants.GAS_CONST_G_NEW_ACCOUNT : 0))
        + (isWarm
            ? GlobalConstants.GAS_CONST_G_WARM_ACCESS
            : GlobalConstants.GAS_CONST_G_COLD_ACCOUNT_ACCESS);
  }
}
