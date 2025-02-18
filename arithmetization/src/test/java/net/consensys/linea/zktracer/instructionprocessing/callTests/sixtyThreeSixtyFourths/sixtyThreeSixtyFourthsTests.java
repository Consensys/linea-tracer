package net.consensys.linea.zktracer.instructionprocessing.callTests.sixtyThreeSixtyFourths;

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

  }
}
