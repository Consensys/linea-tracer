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

import static net.consensys.linea.zktracer.module.constants.GlobalConstants.WORD_SIZE;
import static net.consensys.linea.zktracer.opcode.OpCode.MSTORE;
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

  enum ValueParameter {
    ZERO,
    NON_ZERO;

    boolean isZeroArgument() {
      return this == ZERO;
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
    // - Set the r value of BLAKE2F to something greater than the gas stipend
    final int value = valueParameter.isZeroArgument() ? 0 : 1;
    final int argsSize; // depends on the called precompile
    int argsOffset = valueParameter.isZeroArgument() ? 0 : 1;
    final int retSize = valueParameter.isZeroArgument() ? 0 : 1;
    final int retOffset = valueParameter.isZeroArgument() ? 0 : 1;

    // BLAKE2F specific parameters
    int rFirstByte = valueParameter.isZeroArgument() ? 0 : 0x12;
    int r = rFirstByte << 8;
    if (precompileAddress == BLAKE2B_F_COMPRESSION) {
      program
          .push(rFirstByte) // For simplicity, we only set the first byte of r
          .push(argsOffset + 2) // offset
          // Writing rFirstByte at this offset
          // allows to have r = 0x00000000 or r = 0x00001200
          .op(OpCode.MSTORE8);
      argsSize = 213;
    } else if (precompileAddress == ALTBN128_PAIRING) {
      argsSize = 192;
    } else if ((precompileAddress == SHA256
            || precompileAddress == RIPEMD160
            || precompileAddress == ID)
        && !valueParameter.isZeroArgument()) {
      int nWords = 1024;
      argsSize = nWords * WORD_SIZE; // This guarantees that precompileCost > gasBonus
      populateMemory(program, nWords, argsOffset);
    } else {
      argsSize = valueParameter.isZeroArgument() ? 0 : 1;
    }

    final int precompileCost;
    if (precompileAddress.equals(ECREC)) {
      precompileCost = 3000;
    } else if (precompileAddress.equals(SHA256)) {
      precompileCost = (5 + (argsSize + 31) / 32) * 12;
    } else if (precompileAddress.equals(RIPEMD160)) {
      precompileCost = (5 + (argsSize + 31) / 32) * 120;
    } else if (precompileAddress.equals(ID)) {
      precompileCost = (5 + (argsSize + 31) / 32) * 3;
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

    int gas =
        switch (gasParameter) {
          case ZERO -> 0;
          case ONE -> 1;
          case COST_MINUS_ONE -> precompileCost - 1;
          case COST -> precompileCost;
          case COST_PLUS_ONE -> precompileCost + 1;
        };

    // In case funds are sent to the precompile contract (valueParameter == NON_ZERO)
    // a gas bonus of 2300 is added to the transaction (gas stipend).
    // We now deduce that gas bonus from the gas stipend to trigger the
    // insufficient gas for the precompile call in the non-trivial cases (COST_MINUS_ONE, COST,
    // COST_PLUS_ONE).
    // Note that we exclude the case of MODEXP as it is treated in a separate test
    // and the case of ALTBN128_ADD as it has a fixed gas cost of 150.
    if (!valueParameter.isZeroArgument()
        && (gasParameter == GasParameter.COST_MINUS_ONE
            || gasParameter == GasParameter.COST
            || gasParameter == GasParameter.COST_PLUS_ONE)
        && !precompileAddress.equals(ALTBN128_ADD)
        && !precompileAddress.equals(MODEXP)) {
      final int gasBonus = 2300;
      gas -= gasBonus;
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

    // Here we check if OOB detects the insufficient gas for the precompile call.
    // As the number of OOB operation required is variable, we iterate over all the operations.
    boolean insufficientGasForPrecompile = false;
    BigInteger actualPrecompileCost = BigInteger.ZERO;
    for (int i = 0; i < hub.oob().operations().size(); i++) {
      final OobOperation operation = hub.oob().operations().get(i);
      insufficientGasForPrecompile =
          insufficientGasForPrecompile || operation.isInsufficientGasForPrecompile();
      if (operation.getPrecompileCost() != null) {
        actualPrecompileCost = operation.getPrecompileCost();
      }
    }

    // We assert that the precompileCost we compute here is the same as the one computed in OOB
    assertEquals(BigInteger.valueOf(precompileCost), actualPrecompileCost);

    // We assert that the insufficientGasForPrecompile flag is set correctly
    if (gasParameter == GasParameter.COST
        || gasParameter == GasParameter.COST_PLUS_ONE
        || (precompileAddress.equals(BLAKE2B_F_COMPRESSION) && r == 0) // precompileCost is 0
        || (precompileAddress.equals(ALTBN128_ADD)
            && value > 0) // precompileCost is 150 but stipend is at least 2300
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

  // TODO: do not replicate this method
  //  use the one in
  // arithmetization/src/test/java/net/consensys/linea/zktracer/instructionprocessing/callTests/Utilities.java
  //  after merge
  /**
   * {@link #populateMemory} populates memory with <b>nWords</b> chosen cyclically from the set of 6
   * EVM words obtained by repeating the strings <b>aa</b>, <b>bb</b>, ..., <b>ff</b> 32 times.
   *
   * @param program
   * @param nWords
   */
  public static void populateMemory(BytecodeCompiler program, int nWords, int offset) {
    List<String> abcdef = List.of("aa", "bb", "cc", "dd", "ee", "ff");
    for (int i = 0; i < nWords; i++) {
      program
          .push(abcdef.get(i % abcdef.size()).repeat(WORD_SIZE)) // value, a 32 byte word
          .push(offset + i * WORD_SIZE) // offset
          .op(MSTORE);
    }
  }
}
