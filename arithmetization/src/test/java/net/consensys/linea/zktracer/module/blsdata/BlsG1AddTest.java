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

import static net.consensys.linea.zktracer.Fork.isPostPrague;
import static net.consensys.linea.zktracer.module.blsdata.BlsTestUtils.G1_POINT_NOT_IN_SUBGROUP;
import static net.consensys.linea.zktracer.module.blsdata.BlsTestUtils.SMALL_POINTS;
import static net.consensys.linea.zktracer.module.blsdata.BlsTestUtils.VALID_G1_POINT;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
public class BlsG1AddTest extends TracerTestBase {

  @ParameterizedTest
  @MethodSource("blsG1AddSource")
  void testBlsG1Add(String a, String b, TestInfo testInfo) {
    BytecodeCompiler program = BytecodeCompiler.newProgram(chainConfig);

    final Address codeOwnerAddress = Address.fromHexString("0xC0DE");
    final ToyAccount codeOwnerAccount =
        ToyAccount.builder()
            .balance(Wei.of(0))
            .nonce(1)
            .address(codeOwnerAddress)
            .code(Bytes.concatenate(Bytes.fromHexString(a), Bytes.fromHexString(b)))
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
        .push(Address.BLS12_G1ADD) // address
        .push(Bytes.fromHexStringLenient("0xFFFFFFFF")) // gas
        .op(OpCode.STATICCALL);
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    bytecodeRunner.run(List.of(codeOwnerAccount), chainConfig, testInfo);

    if (isPostPrague(fork)) {
      // TODO: fix this also in the other BLS tests
      final boolean failureIsExpected = !a.equals(VALID_G1_POINT) || !b.equals(VALID_G1_POINT);
      final BlsData blsdata = (BlsData) bytecodeRunner.getHub().blsData();
      assertEquals(failureIsExpected, blsdata.blsDataOperation().mext());
      assertEquals(failureIsExpected, !blsdata.blsDataOperation().successBit());
    }
  }

  private static Stream<Arguments> blsG1AddSource() {
    List<Arguments> arguments = new ArrayList<>();
    for (String a : SMALL_POINTS) {
      for (String b : SMALL_POINTS) {
        arguments.add(Arguments.of(a, b));
      }
    }
    arguments.clear();
    arguments.add(Arguments.of(G1_POINT_NOT_IN_SUBGROUP, G1_POINT_NOT_IN_SUBGROUP));
    return arguments.stream();
  }
}
