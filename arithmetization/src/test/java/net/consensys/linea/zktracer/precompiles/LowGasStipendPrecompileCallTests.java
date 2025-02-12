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
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.datatypes.Address;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class LowGasStipendPrecompileCallTests {

  // Enums for the different testing scenarios
  enum ArgumentCase {
    ZERO,
    NON_ZERO;

    boolean isZeroCase() {
      return this == ZERO;
    }

    boolean isNonZeroCase() {
      return this == NON_ZERO;
    }
  }

  enum GasCase {
    ZERO,
    ONE,
    COST_MINUS_ONE,
    COST,
    COST_PLUS_ONE;
  }

  @ParameterizedTest
  @MethodSource("lowGasStipendPrecompileCallTestSource")
  void lowGasStipendPrecompileCallTest(
      Address precompileAddress, ArgumentCase argumentCase, GasCase gasCase) {
    final BytecodeCompiler program = BytecodeCompiler.newProgram();

    // In order to actually trigger the insufficient we need to:
    // - Set a specific args size for BLAKE2F and EC_PAIRING
    // - Set the r value of BLAKE2F to have precompileCost > gasBonus
    // - Populate the memory with a large enough number of words for SHA256, RIPEMD160, and ID
    //   to have precompileCost > gasBonus.
    final int value = getArgument(argumentCase);
    final int cds; // depends on the called precompile
    final int rac = getArgument(argumentCase);
    final int rao = getArgument(argumentCase);

    // BLAKE2F specific parameters
    final int rLeadingByte = argumentCase.isZeroCase() ? 0 : 0x12;
    final int r = rLeadingByte << 8;

    // MODEXP specific parameters
    final int bbs = 0x02;
    final int ebs = 0x03;
    final int mbs = 0x04;

    if (precompileAddress == BLAKE2B_F_COMPRESSION) {
      program
          .push(rLeadingByte) // For simplicity, we only set the first byte of r
          .push(rac + 2) // offset
          // Writing rLeadingByte at this offset
          // allows to have r = 0x00000000 or r = 0x00001200
          .op(OpCode.MSTORE8);
      cds = 213;
    } else if (precompileAddress == ALTBN128_PAIRING) {
      // EC_PAIRING specific parameters
      cds = 192;
    } else if ((precompileAddress == SHA256
            || precompileAddress == RIPEMD160
            || precompileAddress == ID)
        && argumentCase.isNonZeroCase()) {
      // SHA256, RIPEMD160, and ID specific parameters
      int nWords = 1024;
      cds = nWords * WORD_SIZE; // This guarantees that precompileCost > gasBonus
      populateMemory(program, nWords, rac);
    } else if (precompileAddress == MODEXP) {
      cds = 96 + bbs + ebs + mbs;
      program
          .push(Bytes32.leftPad(Bytes.of(bbs)))
          .push(0)
          .op(OpCode.MSTORE)
          .push(Bytes32.leftPad(Bytes.of(ebs)))
          .push(32)
          .op(OpCode.MSTORE)
          .push(Bytes32.leftPad(Bytes.of(mbs)))
          .push(64)
          .op(OpCode.MSTORE)
          .push(Bytes32.rightPad(Bytes.fromHexString("0xba7e" + "000ec7" + "0000080d")))
          .push(96)
          .op(OpCode.MSTORE);
    } else {
      // Default case
      cds = getArgument(argumentCase);
    }

    // Compute the precompile cost
    final int precompileCost = getPrecompileCost(precompileAddress, cds, r);

    // Compute the gas stipend in the different testing scenarios
    int gas = getGas(gasCase, precompileCost);

    // In case funds are sent to the precompile contract (argumentCase == NON_ZERO)
    // a gas stipend of 2300 is added to the transaction.
    // We now deduce that gas stipend from the gas given to the transaction to trigger
    // insufficient gas for the precompile call in the non-trivial cases (COST_MINUS_ONE, COST,
    // COST_PLUS_ONE).
    // Note that we exclude the case of MODEXP as it is treated in a separate test
    // and the case of ALTBN128_ADD as it has a fixed gas cost of 150.
    if (argumentCase.isNonZeroCase()
        && (gasCase == GasCase.COST_MINUS_ONE
            || gasCase == GasCase.COST
            || gasCase == GasCase.COST_PLUS_ONE)
        && !precompileAddress.equals(ALTBN128_ADD)
        && !precompileAddress.equals(MODEXP)) {
      gas -= GAS_CONST_G_CALL_STIPEND;
    }

    // Common program for all precompile calls
    program
        .push(getReturnAtCapacity(precompileAddress, cds, mbs)) // rac
        .push(rao) // rao
        .push(cds) // cds
        .push(rac) // rac
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
    if (gasCase == GasCase.COST
        || gasCase == GasCase.COST_PLUS_ONE
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
    for (ArgumentCase argumentCase : ArgumentCase.values()) {
      for (GasCase gasCase : GasCase.values()) {
        arguments.add(Arguments.of(ECREC, argumentCase, gasCase));
        arguments.add(Arguments.of(SHA256, argumentCase, gasCase));
        arguments.add(Arguments.of(RIPEMD160, argumentCase, gasCase));
        arguments.add(Arguments.of(ID, argumentCase, gasCase));
        if (argumentCase == ArgumentCase.ZERO) {
          // The NON_ZERO for MODEXP case will be treated in a separate test
          arguments.add(Arguments.of(MODEXP, argumentCase, gasCase));
        }
        arguments.add(Arguments.of(ALTBN128_ADD, argumentCase, gasCase));
        arguments.add(Arguments.of(ALTBN128_MUL, argumentCase, gasCase));
        arguments.add(Arguments.of(Address.ALTBN128_PAIRING, argumentCase, gasCase));
        arguments.add(Arguments.of(BLAKE2B_F_COMPRESSION, argumentCase, gasCase));
      }
    }
    return arguments.stream();
  }

  // Support methods
  private static int getArgument(ArgumentCase argumentCase) {
    return argumentCase.isZeroCase() ? 0 : 1;
  }

  /**
   * Computes the rac based on the precompile address, and cds in the case of ID.
   *
   * @param precompileAddress the address of the precompile contract.
   * @param cds the call data size. Beyond the case of ID, this value is ignored.
   * @param mbs the modulo byte size. Beyond the case of MODEXP, this value is ignored.
   * @return the computed return rac.
   */
  private static int getReturnAtCapacity(Address precompileAddress, int cds, int mbs) {
    final int rac;
    if (precompileAddress == ECREC
        || precompileAddress == SHA256
        || precompileAddress == RIPEMD160
        || precompileAddress == ALTBN128_PAIRING) {
      rac = WORD_SIZE;
    } else if (precompileAddress == ALTBN128_ADD
        || precompileAddress == ALTBN128_MUL
        || precompileAddress == BLAKE2B_F_COMPRESSION) {
      rac = 2 * WORD_SIZE;
    } else if (precompileAddress == MODEXP) {
      rac = mbs;
    } else if (precompileAddress == ID) {
      rac = cds;
    } else {
      throw new IllegalArgumentException("Unknown precompile address");
    }
    return rac;
  }

  /**
   * Computes the precompile cost based on the precompile address, arguments size, and r value in
   * case of BLAKE2F.
   *
   * @param precompileAddress the address of the precompile contract.
   * @param cds the call data size.
   * @param r the r value for BLAKE2F. For other precompile contracts, this value is ignored.
   * @return the computed precompile cost.
   */
  private static int getPrecompileCost(Address precompileAddress, int cds, int r) {
    final int precompileCost;
    if (precompileAddress.equals(ECREC)) {
      precompileCost = 3000;
    } else if (precompileAddress.equals(SHA256)) {
      precompileCost = (5 + (cds + WORD_SIZE_MO) / WORD_SIZE) * 12;
    } else if (precompileAddress.equals(RIPEMD160)) {
      precompileCost = (5 + (cds + WORD_SIZE_MO) / WORD_SIZE) * 120;
    } else if (precompileAddress.equals(ID)) {
      precompileCost = (5 + (cds + WORD_SIZE_MO) / WORD_SIZE) * 3;
    } else if (precompileAddress.equals(MODEXP)) {
      precompileCost = 200;
    } else if (precompileAddress.equals(ALTBN128_ADD)) {
      precompileCost = 150;
    } else if (precompileAddress.equals(ALTBN128_MUL)) {
      precompileCost = 6000;
    } else if (precompileAddress.equals(ALTBN128_PAIRING)) {
      precompileCost = 45000 + 34000 * (cds / 192);
    } else if (precompileAddress.equals(BLAKE2B_F_COMPRESSION)) {
      precompileCost = r;
    } else {
      throw new IllegalArgumentException("Unknown precompile address");
    }
    return precompileCost;
  }

  /**
   * Computes the gas based on the gas parameter and precompile cost.
   *
   * @param gasCase the gas case.
   * @param precompileCost the precompile cost.
   * @return the computed gas.
   */
  private static int getGas(GasCase gasCase, int precompileCost) {
    return switch (gasCase) {
      case ZERO -> 0;
      case ONE -> 1;
      case COST_MINUS_ONE -> precompileCost - 1;
      case COST -> precompileCost;
      case COST_PLUS_ONE -> precompileCost + 1;
    };
  }
}
