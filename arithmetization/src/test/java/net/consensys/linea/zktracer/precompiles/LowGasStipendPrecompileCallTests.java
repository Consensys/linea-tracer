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

import static org.hyperledger.besu.datatypes.Address.ALTBN128_ADD;
import static org.hyperledger.besu.datatypes.Address.ALTBN128_MUL;
import static org.hyperledger.besu.datatypes.Address.ALTBN128_PAIRING;
import static org.hyperledger.besu.datatypes.Address.BLAKE2B_F_COMPRESSION;
import static org.hyperledger.besu.datatypes.Address.ECREC;
import static org.hyperledger.besu.datatypes.Address.ID;
import static org.hyperledger.besu.datatypes.Address.MODEXP;
import static org.hyperledger.besu.datatypes.Address.RIPEMD160;
import static org.hyperledger.besu.datatypes.Address.SHA256;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    // - Set a specific args size from BLAKE2F AND EC_PAIRING
    // - Set the r value of BLAKE2F to something greater than the gas stipend
    int argsSize;
    if (precompileAddress == BLAKE2B_F_COMPRESSION) {
      program
          .push(0xab) // r (as r is 4 bytes, it is padded to 0xab000000)
          .push(valueParameter.isZeroArgument() ? 0 : 1) // offset
          .op(OpCode.MSTORE8);
      argsSize = 213;
    } else if (precompileAddress == ALTBN128_PAIRING) {
      argsSize = 192;
    } else {
      argsSize = valueParameter.isZeroArgument() ? 0 : 1;
    }

    int precompileCost = 0;
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
      precompileCost = 0xab000000;
    } else {
      throw new IllegalArgumentException("Unknown precompile address");
    }

    // In case funds are sent to the precompile contract, the precompile cost is increased by 2300
    // int extraCost = valueParameter.isZeroArgument() ? 0 : 2300;
    // precompileCost += extraCost;

    int gas =
        switch (gasParameter) {
          case ZERO -> 0;
          case ONE -> 1;
          case COST_MINUS_ONE -> precompileCost - 1;
          case COST -> precompileCost;
          case COST_PLUS_ONE -> precompileCost + 1;
        };

    // Common program for all precompile calls
    program
        .push(valueParameter.isZeroArgument() ? 0 : 1) // retSize
        .push(valueParameter.isZeroArgument() ? 0 : 1) // retOffset
        .push(argsSize) // argsSize
        .push(valueParameter.isZeroArgument() ? 0 : 1) // argsOffset
        .push(valueParameter.isZeroArgument() ? 0 : 1) // value
        .push(precompileAddress) // address
        .push(gas) // gas, that is deliberately insufficient
        .op(OpCode.CALL)
        .compile();
    final BytecodeRunner bytecodeRunner = BytecodeRunner.of(program);
    bytecodeRunner.run(1_000_000L); // huge gas limit
    final Hub hub = bytecodeRunner.getHub();

    // Here we check if OOB detects the insufficient gas for the precompile call
    // As the number of OOB operation required is variable, we iterate over all the operations
    boolean insufficientGasForPrecompile = false;
    for (int i = 0; i < hub.oob().operations().size(); i++) {
      final OobOperation operation = hub.oob().operations().get(i);
      insufficientGasForPrecompile =
          insufficientGasForPrecompile || operation.isInsufficientGasForPrecompile();
    }

    if (gasParameter == GasParameter.ZERO
        || gasParameter == GasParameter.ONE
        || gasParameter == GasParameter.COST_MINUS_ONE) {
      assertTrue(insufficientGasForPrecompile);
    } else {
      assertFalse(insufficientGasForPrecompile);
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
          // The NON_ZERO case will be treated in a separate test
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
}
