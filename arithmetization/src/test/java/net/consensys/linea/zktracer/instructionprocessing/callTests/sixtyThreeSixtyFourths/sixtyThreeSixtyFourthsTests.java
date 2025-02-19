package net.consensys.linea.zktracer.instructionprocessing.callTests.sixtyThreeSixtyFourths;

import static net.consensys.linea.zktracer.module.constants.GlobalConstants.GAS_CONST_G_CALL_VALUE;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.GAS_CONST_G_COLD_ACCOUNT_ACCESS;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.GAS_CONST_G_NEW_ACCOUNT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.GAS_CONST_G_TRANSACTION;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.GAS_CONST_G_VERY_LOW;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.GAS_CONST_G_WARM_ACCESS;
import static net.consensys.linea.zktracer.module.hub.signals.TracedException.OUT_OF_GAS_EXCEPTION;
import static org.hyperledger.besu.datatypes.Address.ALTBN128_ADD;
import static org.junit.jupiter.api.Assertions.assertEquals;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.zktracer.opcode.OpCode;
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
public class sixtyThreeSixtyFourthsTests {

  @Test
  void sixtyThreeSixtyFourthsTest() {
    /*
    A transaction needs to target an SMC that will:
      - expand memory by executing an MLOAD at offset 4096 - 32 = 4064
      - do a CALL type instruction to a precompile contract
      - the gas we provide to the precompile contract should be:
          providedGas = 63/64 * (remainingGas - upfrontGasCost) + callStipend * (valueIsNonZero ? 1 : 0).
        this gas should be insufficient to pay for the execution of the precompile contract (cornerCase = -1, 0, 1).
        This means that I need to find the GAS_LIMIT for the transaction such that providedGas = precompileGasCost + cornerCase.
        Specifically, the GAS_LIMIT influences the remainingGas (note that we pay the MLOAD, PUSHEes etc and the 21000,
        GAS_CONST_G_TRANSACTION).
        NOTE: in case the precompile contract does not exist in the world state, we may pay an additional 25000 gas cost when
        transferring value in the call from SMC to PRC. We need to check if they exist in the world state.
        An option may be sending some value to the contract first to do not pay this 25000 during the test.
     */

    final BytecodeCompiler program = BytecodeCompiler.newProgram();

    program.push(4096 - 32).op(OpCode.MLOAD);

    long cost =
        GAS_CONST_G_TRANSACTION
            + GAS_CONST_G_VERY_LOW // PUSH
            + GAS_CONST_G_VERY_LOW
            + 416 // MLOAD
            + GAS_CONST_G_VERY_LOW * 7 // 7 PUSHes
        ; // + callGasCostExcludingMemoryExpansion(false, true, false); ?

    program
        .push(0) // returnAtCapacity
        .push(0) // returnAtOffset
        .push(0) // callDataSize
        .push(0) // callDataOffset
        .push(0) // value
        .push(ALTBN128_ADD) // address
        .push(150) // gas
        .op(OpCode.CALL);

    final BytecodeRunner bytecodeRunner = BytecodeRunner.of(program);

    bytecodeRunner.run(cost - 1);

    assertEquals(
        OUT_OF_GAS_EXCEPTION,
        bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
  }

  private long callGasCostExcludingMemoryExpansion(
      boolean transfersValue, boolean targetAddressExists, boolean isWarm) {
    return (transfersValue ? GAS_CONST_G_CALL_VALUE : 0)
        + (targetAddressExists ? 0 : (transfersValue ? GAS_CONST_G_NEW_ACCOUNT : 0))
        + (isWarm ? GAS_CONST_G_WARM_ACCESS : GAS_CONST_G_COLD_ACCOUNT_ACCESS);
  }
}
