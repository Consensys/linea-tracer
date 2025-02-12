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

package net.consensys.linea.zktracer.precompiles;

import static net.consensys.linea.zktracer.instructionprocessing.callTests.Utilities.populateMemory;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.GAS_CONST_G_CALL_STIPEND;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.WORD_SIZE;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.WORD_SIZE_MO;
import static org.hyperledger.besu.datatypes.Address.ALTBN128_ADD;
import static org.hyperledger.besu.datatypes.Address.ALTBN128_MUL;
import static org.hyperledger.besu.datatypes.Address.ALTBN128_PAIRING;
import static org.hyperledger.besu.datatypes.Address.BLAKE2B_F_COMPRESSION;
import static org.hyperledger.besu.datatypes.Address.ECREC;
import static org.hyperledger.besu.datatypes.Address.ID;
import static org.hyperledger.besu.datatypes.Address.MODEXP;
import static org.hyperledger.besu.datatypes.Address.RIPEMD160;
import static org.hyperledger.besu.datatypes.Address.SHA256;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.oob.OobOperation;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.hyperledger.besu.datatypes.Address;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class LowGasStipendPrecompileCallTests {

  // Enums for the different testing scenarios
  enum ValueParameter {
    ZERO,
    NON_ZERO;

    boolean isZeroArgument() {
      return this == ZERO;
    }

    boolean isNonZeroArgument() {
      return this == NON_ZERO;
    }
  }

  enum GasParameter {
    ZERO,
    ONE,
    COST_MINUS_ONE,
    COST,
    COST_PLUS_ONE;
  }

  @ParameterizedTest
  @MethodSource("lowGasStipendPrecompileCallTestSource")
  void lowGasStipendPrecompileCallTest(
      Address precompileAddress, ValueParameter valueParameter, GasParameter gasParameter) {
    final BytecodeCompiler program = BytecodeCompiler.newProgram();

    // In order to actually trigger the insufficient we need to:
    // - Set a specific args size for BLAKE2F and EC_PAIRING
    // - Set the r value of BLAKE2F to have precompileCost > gasBonus
    // - Populate the memory with a large enough number of words for SHA256, RIPEMD160, and ID
    //   to have precompileCost > gasBonus.
    final int value = valueParameter.isZeroArgument() ? 0 : 1;
    final int argsSize; // depends on the called precompile
    final int argsOffset = valueParameter.isZeroArgument() ? 0 : 1;
    final int retOffset = valueParameter.isZeroArgument() ? 0 : 1;

    // BLAKE2F specific parameters
    int rLeadingByte = valueParameter.isZeroArgument() ? 0 : 0x12;
    int r = rLeadingByte << 8;
    if (precompileAddress == BLAKE2B_F_COMPRESSION) {
      program
          .push(rLeadingByte) // For simplicity, we only set the first byte of r
          .push(argsOffset + 2) // offset
          // Writing rLeadingByte at this offset
          // allows to have r = 0x00000000 or r = 0x00001200
          .op(OpCode.MSTORE8);
      argsSize = 213;
    } else if (precompileAddress == ALTBN128_PAIRING) {
      // EC_PAIRING specific parameters
      argsSize = 192;
    } else if ((precompileAddress == SHA256
            || precompileAddress == RIPEMD160
            || precompileAddress == ID)
        && valueParameter.isNonZeroArgument()) {
      // SHA256, RIPEMD160, and ID specific parameters
      int nWords = 1024;
      argsSize = nWords * WORD_SIZE; // This guarantees that precompileCost > gasBonus
      populateMemory(program, nWords, argsOffset);
    } else {
      // Default case
      argsSize = valueParameter.isZeroArgument() ? 0 : 1;
    }

    // Compute the precompile cost
    final int precompileCost = getPrecompileCost(precompileAddress, argsSize, r);

    // Compute the gas stipend in the different testing scenarios
    int gas = getGas(gasParameter, precompileCost);

    // In case funds are sent to the precompile contract (valueParameter == NON_ZERO)
    // a gas bonus of 2300 is added to the transaction (gas stipend).
    // We now deduce that gas bonus from the gas stipend to trigger the
    // insufficient gas for the precompile call in the non-trivial cases (COST_MINUS_ONE, COST,
    // COST_PLUS_ONE).
    // Note that we exclude the case of MODEXP as it is treated in a separate test
    // and the case of ALTBN128_ADD as it has a fixed gas cost of 150.
    if (valueParameter.isNonZeroArgument()
        && (gasParameter == GasParameter.COST_MINUS_ONE
            || gasParameter == GasParameter.COST
            || gasParameter == GasParameter.COST_PLUS_ONE)
        && !precompileAddress.equals(ALTBN128_ADD)
        && !precompileAddress.equals(MODEXP)) {
      gas -= GAS_CONST_G_CALL_STIPEND;
    }

    // Common program for all precompile calls
    program
        .push(getRetSize(precompileAddress, argsSize)) // retSize
        .push(retOffset) // retOffset
        .push(argsSize) // argsSize
        .push(argsOffset) // argsOffset
        .push(value) // value
        .push(precompileAddress) // address
        .push(gas) // gas
        .op(OpCode.CALL)
        .compile();
    final BytecodeRunner bytecodeRunner = BytecodeRunner.of(program);
    bytecodeRunner.run(1_000_000L); // huge gas limit
    final Hub hub = bytecodeRunner.getHub();

    // Here we check if OOB detects the insufficient gas for the precompile call
    // and the precompile cost computed by OOB.
    // As the number of OOB operation required is variable, we look for it over all the operations.
    boolean insufficientGasForPrecompile =
        hub.oob().operations().stream().anyMatch(OobOperation::isInsufficientGasForPrecompile);

    BigInteger precompileCostComputedByOOB =
        hub.oob().operations().stream()
            .map(OobOperation::getPrecompileCost)
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(BigInteger.ZERO);

    // We assert that the precompileCost we compute here is the same as the one computed in OOB
    assertEquals(BigInteger.valueOf(precompileCost), precompileCostComputedByOOB);

    // We assert that the insufficientGasForPrecompile flag is set correctly in OOB
    if (gasParameter == GasParameter.COST
        || gasParameter == GasParameter.COST_PLUS_ONE
        || (precompileAddress.equals(BLAKE2B_F_COMPRESSION)
            && r == 0) // precompileCost is 0 so gas cannot be insufficient
        || (precompileAddress.equals(ALTBN128_ADD)
            && value > 0) // precompileCost is 150 but stipend is at least 2300 so gas cannot be
    // insufficient
    ) {
      assertFalse(insufficientGasForPrecompile);
    } else {
      assertTrue(insufficientGasForPrecompile);
    }
  }

  static Stream<Arguments> lowGasStipendPrecompileCallTestSource() {
    List<Arguments> arguments = new ArrayList<>();
    for (ValueParameter valueParameter : ValueParameter.values()) {
      for (GasParameter gasParameter : GasParameter.values()) {
        arguments.add(Arguments.of(ECREC, valueParameter, gasParameter));
        arguments.add(Arguments.of(SHA256, valueParameter, gasParameter));
        arguments.add(Arguments.of(RIPEMD160, valueParameter, gasParameter));
        arguments.add(Arguments.of(ID, valueParameter, gasParameter));
        if (valueParameter == ValueParameter.ZERO) {
          // The NON_ZERO for MODEXP case will be treated in a separate test
          arguments.add(Arguments.of(MODEXP, valueParameter, gasParameter));
        }
        arguments.add(Arguments.of(ALTBN128_ADD, valueParameter, gasParameter));
        arguments.add(Arguments.of(ALTBN128_MUL, valueParameter, gasParameter));
        arguments.add(Arguments.of(Address.ALTBN128_PAIRING, valueParameter, gasParameter));
        arguments.add(Arguments.of(BLAKE2B_F_COMPRESSION, valueParameter, gasParameter));
      }
    }
    return arguments.stream();
  }

  // Support methods

  /**
   * Computes the return size based on the precompile address, and arguments size in the case of ID.
   *
   * @param precompileAddress the address of the precompile contract.
   * @param argsSize the size of the arguments for ID. For other precompiles, this value is ignored.
   * @return the computed return size.
   */
  private static int getRetSize(Address precompileAddress, int argsSize) {
    final int retSize;
    if (precompileAddress == ECREC
        || precompileAddress == SHA256
        || precompileAddress == RIPEMD160
        || precompileAddress == ALTBN128_PAIRING
        || precompileAddress == MODEXP) {
      retSize = WORD_SIZE;
    } else if (precompileAddress == ALTBN128_ADD
        || precompileAddress == ALTBN128_MUL
        || precompileAddress == BLAKE2B_F_COMPRESSION) {
      retSize = 2 * WORD_SIZE;
    } else if (precompileAddress == ID) {
      retSize = argsSize;
    } else {
      throw new IllegalArgumentException("Unknown precompile address");
    }
    return retSize;
  }

  /**
   * Computes the precompile cost based on the precompile address, arguments size, and r value in
   * case of BLAKE2F.
   *
   * @param precompileAddress the address of the precompile contract.
   * @param argsSize the size of the arguments.
   * @param r the r value for BLAKE2F. For other precompiles, this value is ignored.
   * @return the computed precompile cost.
   */
  private static int getPrecompileCost(Address precompileAddress, int argsSize, int r) {
    final int precompileCost;
    if (precompileAddress.equals(ECREC)) {
      precompileCost = 3000;
    } else if (precompileAddress.equals(SHA256)) {
      precompileCost = (5 + (argsSize + WORD_SIZE_MO) / WORD_SIZE) * 12;
    } else if (precompileAddress.equals(RIPEMD160)) {
      precompileCost = (5 + (argsSize + WORD_SIZE_MO) / WORD_SIZE) * 120;
    } else if (precompileAddress.equals(ID)) {
      precompileCost = (5 + (argsSize + WORD_SIZE_MO) / WORD_SIZE) * 3;
    } else if (precompileAddress.equals(MODEXP)) {
      precompileCost = 200;
    } else if (precompileAddress.equals(ALTBN128_ADD)) {
      precompileCost = 150;
    } else if (precompileAddress.equals(ALTBN128_MUL)) {
      precompileCost = 6000;
    } else if (precompileAddress.equals(ALTBN128_PAIRING)) {
      precompileCost = 45000 + 34000 * (argsSize / 192);
    } else if (precompileAddress.equals(BLAKE2B_F_COMPRESSION)) {
      precompileCost = r;
    } else {
      throw new IllegalArgumentException("Unknown precompile address");
    }
    return precompileCost;
  }

  /**
   * Computes the gas stipend based on the gas parameter and precompile cost.
   *
   * @param gasParameter the gas parameter.
   * @param precompileCost the precompile cost.
   * @return the computed gas stipend.
   */
  private static int getGas(GasParameter gasParameter, int precompileCost) {
    return switch (gasParameter) {
      case ZERO -> 0;
      case ONE -> 1;
      case COST_MINUS_ONE -> precompileCost - 1;
      case COST -> precompileCost;
      case COST_PLUS_ONE -> precompileCost + 1;
    };
  }
}
