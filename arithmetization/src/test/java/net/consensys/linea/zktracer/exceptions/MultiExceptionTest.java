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

import java.util.List;

import net.consensys.linea.UnitTestWatcher;
import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.testing.ToyAccount;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Wei;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@ExtendWith(UnitTestWatcher.class)
public class MultiExceptionTest {

  @ParameterizedTest
  @ValueSource(ints = {-1, 0, 1})
  void outOfGasExceptionReturnDataCopy(int cornerCase) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    final ToyAccount returnDataProviderAccount =
        ToyAccount.builder()
            .balance(Wei.fromEth(1))
            .nonce(10)
            .address(Address.fromHexString("c0de"))
            // Constructor that returns 32 FF
            .code(
                Bytes.fromHexString(
                    "7fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff60005260206000f3"))
            .build();

    program
        // 1. Execute static call
        .push(0) // byte size of return data
        .push(0) // retOffset
        .push(0) // byte size calldata
        .push(0) // argsOffset
        .push("c0de") // Address of 'return data provider' account
        .op(OpCode.GAS) // gas
        .op(OpCode.STATICCALL)
        // 2. Clean the stack
        .op(OpCode.POP)
        // 3. Return data copy
        .push(32) // size
        .push(0) // offset
        .push(65) // destoffset, trigger mem expansion
        .op(OpCode.RETURNDATACOPY);

    Bytes pgCompile = program.compile();
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);

    long gasCost = bytecodeRunner.runOnlyForGasCost(List.of(returnDataProviderAccount));

    bytecodeRunner.run(gasCost + cornerCase, List.of(returnDataProviderAccount));

    ExceptionUtils.assertEqualsOutOfGasIfCornerCaseMinusOneElseAssertNotEquals(
        cornerCase, bytecodeRunner);
  }
}
