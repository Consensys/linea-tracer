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
import static net.consensys.linea.zktracer.module.oob.Trace.G_QUADDIVISOR;
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
      Address precompileAddress,
      ArgumentCase argumentCase,
      GasCase gasCase,
      boolean modexpCostGT200) {
    final BytecodeCompiler program = BytecodeCompiler.newProgram();

    // In order to actually trigger the insufficient we need to:
    // - Set a specific args size for BLAKE2F and EC_PAIRING
    // - Set the r value of BLAKE2F to have precompileCost > gasBonus
    // - Populate the memory with a large enough number of words for SHA256, RIPEMD160, and ID
    //   to have precompileCost > gasBonus.
    final int value = argumentCase.isZeroCase() ? 0 : 1;
    final int argsSize; // depends on the called precompile
    final int argsOffset = 0;

    // retSize is defined below
    final int retOffset = 13;

    // TODO: consider creating test for different families of precompile contracts:
    //  - BLAKE2F
    //  - ECPAIRING
    //  - SHA256, RIPEMD160, ID
    //  - MODEXP
    //  - ECADD, ECMUL, ECRECOVER

    // BLAKE2F specific parameters
    final int rLeadingByte = argumentCase.isZeroCase() ? 0 : 0x12;
    final int r = rLeadingByte << 8;

    // MODEXP specific parameters
    final int bbs = modexpCostGT200 ? 0x01 : 0x02;
    final int ebs = modexpCostGT200 ? 0x06 : 0x03;
    final int mbs = modexpCostGT200 ? 0x19 : 0x04;
    BigInteger bigQuotient = BigInteger.ZERO;

    // Prepare the arguments for the different precompile calls
    if (precompileAddress == BLAKE2B_F_COMPRESSION) {
      argsSize = 213;
      prepareBlake2F(program, rLeadingByte, argsOffset);
    } else if (precompileAddress == ALTBN128_PAIRING) {
      argsSize = 192;
    } else if ((precompileAddress == SHA256
            || precompileAddress == RIPEMD160
            || precompileAddress == ID)
        && argumentCase.isNonZeroCase()) {
      final int nWords = 1024;
      argsSize = nWords * WORD_SIZE; // This guarantees that precompileCost > gasBonus
      prepareSha256Ripemd160Id(program, nWords, argsOffset);
    } else if (precompileAddress == MODEXP) {
      argsSize = 96 + bbs + ebs + mbs;
      bigQuotient = prepareModexp(bbs, mbs, ebs, argsSize, program);
    } else {
      // ECADD, ECMUL, ECRECOVER cases
      argsSize = argumentCase.isZeroCase() ? 0 : 1;
    }

    // Compute the return size
    final int retSize = getRetSize(precompileAddress, argsSize, mbs);

    // Compute the precompile cost
    final int precompileCost =
        getPrecompileCost(precompileAddress, argsSize, r, bigQuotient.intValueExact());

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
        .push(retSize) // retSize
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
    for (GasCase gasCase : GasCase.values()) {
      for (ArgumentCase argumentCase : ArgumentCase.values()) {
        arguments.add(Arguments.of(ECREC, argumentCase, gasCase, false));
        arguments.add(Arguments.of(SHA256, argumentCase, gasCase, false));
        arguments.add(Arguments.of(RIPEMD160, argumentCase, gasCase, false));
        arguments.add(Arguments.of(ID, argumentCase, gasCase, false));
        arguments.add(Arguments.of(ALTBN128_ADD, argumentCase, gasCase, false));
        arguments.add(Arguments.of(ALTBN128_MUL, argumentCase, gasCase, false));
        arguments.add(Arguments.of(Address.ALTBN128_PAIRING, argumentCase, gasCase, false));
        arguments.add(Arguments.of(BLAKE2B_F_COMPRESSION, argumentCase, gasCase, false));
      }
      // The NON_ZERO for MODEXP case will be treated in a separate test
      arguments.add(Arguments.of(MODEXP, ArgumentCase.ZERO, gasCase, false));
      arguments.add(Arguments.of(MODEXP, ArgumentCase.ZERO, gasCase, true));
    }
    return arguments.stream();
  }

  // Support methods
  private static void prepareBlake2F(BytecodeCompiler program, int rLeadingByte, int argsOffset) {
    program
        .push(rLeadingByte) // For simplicity, we only set the first byte of r
        .push(argsOffset + 2) // offset
        // Writing rLeadingByte at this offset
        // allows to have r = 0x00000000 or r = 0x00001200
        .op(OpCode.MSTORE8);
  }

  private void prepareSha256Ripemd160Id(BytecodeCompiler program, int nWords, int argsOffset) {
    populateMemory(program, nWords, argsOffset);
  }

  private static BigInteger prepareModexp(
      int bbs, int mbs, int ebs, int argsSize, BytecodeCompiler program) {
    final int words = (Math.max(bbs, mbs) + 7) / 8;
    final int fOfMax = words * words;
    final Bytes32 bbsPadded = Bytes32.leftPad(Bytes.of(bbs));
    final Bytes32 ebsPadded = Bytes32.leftPad(Bytes.of(ebs));
    final Bytes32 mbsPadded = Bytes32.leftPad(Bytes.of(mbs));
    final Bytes32 bemPadded =
        Bytes32.rightPad(
            Bytes.fromHexString("0x" + "aa".repeat(bbs) + "ff".repeat(ebs) + "bb".repeat(mbs)));
    // Note that is an arbitrary value respecting bbs, ebs, mbs

    program
        .push(bbsPadded)
        .push(0) // offset
        .op(OpCode.MSTORE)
        .push(ebsPadded)
        .push(32) // offset
        .op(OpCode.MSTORE)
        .push(mbsPadded)
        .push(64) // offset
        .op(OpCode.MSTORE)
        .push(bemPadded)
        .push(96) // offset
        .op(OpCode.MSTORE);

    final Bytes paddedCallData = Bytes.concatenate(bbsPadded, ebsPadded, mbsPadded, bemPadded);
    final BigInteger bigNumerator =
        BigInteger.valueOf(fOfMax)
            .multiply(
                OobOperation.computeExponentLog(
                        paddedCallData,
                        BigInteger.valueOf(argsSize),
                        BigInteger.valueOf(bbs),
                        BigInteger.valueOf(ebs),
                        BigInteger.valueOf(mbs))
                    .max(BigInteger.ONE));

    return bigNumerator.divide(BigInteger.valueOf(G_QUADDIVISOR)); // bigQuotient;
  }

  /**
   * Computes the retSize based on the precompile address, and argsSize in the case of ID.
   *
   * @param precompileAddress the address of the precompile contract.
   * @param argsSize the call data size. Beyond the case of ID, this value is ignored.
   * @param mbs the modulo byte size. Beyond the case of MODEXP, this value is ignored.
   * @return the computed return rac.
   */
  private static int getRetSize(Address precompileAddress, int argsSize, int mbs) {
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
      rac = argsSize;
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
   * @param argsSize the call data size.
   * @param r the r value for BLAKE2F. For other precompile contracts, this value is ignored.
   * @param bigQuotient the big quotient for MODEXP. For other precompile contracts, this value is
   *     ignored.
   * @return the computed precompile cost.
   */
  private static int getPrecompileCost(
      Address precompileAddress, int argsSize, int r, int bigQuotient) {
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
      precompileCost = Math.max(200, bigQuotient);
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
