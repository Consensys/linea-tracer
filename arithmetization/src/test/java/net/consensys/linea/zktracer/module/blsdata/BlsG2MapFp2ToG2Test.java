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

package net.consensys.linea.zktracer.module.blsdata;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import net.consensys.linea.UnitTestWatcher;
import net.consensys.linea.reporting.TracerTestBase;
import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.testing.ToyAccount;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Wei;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@ExtendWith(UnitTestWatcher.class)
public class BlsG2MapFp2ToG2Test extends TracerTestBase {

  @ParameterizedTest
  @MethodSource("blsG2MapFp2ToG2Source")
  void testBlsG2MapFpToG2(String input, TestInfo testInfo) {
    BytecodeCompiler program = BytecodeCompiler.newProgram(chainConfig);

    final Address codeOwnerAddress = Address.fromHexString("0xC0DE");
    final ToyAccount codeOwnerAccount =
        ToyAccount.builder()
            .balance(Wei.of(0))
            .nonce(1)
            .address(codeOwnerAddress)
            .code(Bytes.fromHexString(input))
            .build();

    // First place the parameters in memory
    // Copy to targetOffset the code of codeOwnerAccount
    program
        .push(codeOwnerAddress)
        .op(OpCode.EXTCODESIZE) // size
        .push(0) // offset
        .push(0) // targetOffset
        .push(codeOwnerAddress) // address
        .op(OpCode.EXTCODECOPY);

    // Do the call
    program
        .push(0x80) // retSize
        .push(0x100) // retOffset
        .push(0x100) // argSize
        .push(0) // argOffset
        .push(Address.BLS12_MAP_FP2_TO_G2) // address
        .push(Bytes.fromHexStringLenient("0xFFFFFFFF")) // gas
        .op(OpCode.STATICCALL);
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    bytecodeRunner.run(List.of(codeOwnerAccount), chainConfig, testInfo);
  }

  private static Stream<Arguments> blsG2MapFp2ToG2Source() {
    List<Arguments> arguments = new ArrayList<>();
    arguments.add(Arguments.of("00000000000000000000000000000000167ab0f743a50c14cfe36fe095886cefd958c60233367db3f904f6f2b40d5df62f75958a02b52daca5316718966a8fb7000000000000000000000000000000001904f0ab97b4fad64e7833426ba8a6311c0694716cf407c8a015cb266153aae30e45b5796e0143da9f608fd01aaf77d2"));
    return arguments.stream();
  }
}
