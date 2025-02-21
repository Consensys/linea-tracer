package net.consensys.linea.zktracer.instructionprocessing.callTests.sixtyThreeSixtyFourths;

import static org.hyperledger.besu.datatypes.Address.ALTBN128_ADD;
import static org.hyperledger.besu.datatypes.Address.ALTBN128_MUL;
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
    - the gas we provide to the precompile contract should be:
        providedGas = 63/64 * (remainingGasBeforeCall - callOpcodeCost) + callStipend * (valueIsNonZero ? 1 : 0).
      this gas should be insufficient to pay for the execution of the precompile contract (cornerCase = -1, 0, 1).
      This means that I need to find the GAS_LIMIT for the transaction such that providedGas = precompileGasCost + cornerCase.
      Specifically, the GAS_LIMIT influences the remainingGas (note that we pay the MLOAD, PUSHEes etc and the 21000,
      GAS_CONST_G_TRANSACTION).
      NOTE: in case the precompile contract does not exist in the world state, we may pay an additional 25000 gas cost when
      transferring value in the call from SMC to PRC. We need to check if they exist in the world state.
      An option may be sending some value to the contract first to do not pay this 25000 during the test.

      Cases to cover:
      - value = 0
      - value = 1, targetAddressExists = false, true

      Optionally:
      - memoryExpansionBeforeCallToPrc = false, true, value = 0
      - memoryExpansionBeforeCallToPrc = false, true, value = 1, targetAddressExists = false, true
   */

  final Bytes gas = Bytes.fromHexString("ff".repeat(32));

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

    final long gasCost = bytecodeRunner.runOnlyForGasCost(); // 21693
    bytecodeRunner.run(gasCost);

    // providedGas = 63/64 * (250 - 100) + 2300 * 0 = 148 > 150
    // Indeed, without the 63/64 factor, the providedGas would be enough

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
    // 53393 = 21693 + GlobalConstants.GAS_CONST_G_CALL_VALUE + GlobalConstants.GAS_CONST_G_NEW_ACCOUNT
    bytecodeRunner.run(gasCost);

    // providedGas = ...
    // Indeed, without the 63/64 factor, the providedGas would be enough

    final boolean insufficientGasForPrecompile =
        bytecodeRunner.getHub().oob().operations().stream()
            .anyMatch(OobOperation::isInsufficientGasForPrecompile);
    assertTrue(insufficientGasForPrecompile);
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

    final long gasCost = bytecodeRunner.runOnlyForGasCost(); // 27543
    bytecodeRunner.run(gasCost);

    // providedGas = 63/64 * (6100 - 100) + 2300 * 0 = 5907  > 6000
    // Indeed, without the 63/64 factor, the providedGas would be enough

    final boolean insufficientGasForPrecompile =
        bytecodeRunner.getHub().oob().operations().stream()
            .anyMatch(OobOperation::isInsufficientGasForPrecompile);
    assertTrue(insufficientGasForPrecompile);
  }

  private long callGasCostExcludingMemoryExpansion(
      boolean transfersValue, boolean targetAddressExists, boolean isWarm) {
    return (transfersValue ? GlobalConstants.GAS_CONST_G_CALL_VALUE : 0)
        + (targetAddressExists ? 0 : (transfersValue ? GlobalConstants.GAS_CONST_G_NEW_ACCOUNT : 0))
        + (isWarm
            ? GlobalConstants.GAS_CONST_G_WARM_ACCESS
            : GlobalConstants.GAS_CONST_G_COLD_ACCOUNT_ACCESS);
  }
}
