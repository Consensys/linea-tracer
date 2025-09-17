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

import static com.google.common.base.Preconditions.checkArgument;
import static net.consensys.linea.zktracer.module.blsdata.BlsTestUtils.LARGE_POINTS;
import static net.consensys.linea.zktracer.module.blsdata.BlsTestUtils.SMALL_POINTS;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
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
public class BlsPairingCheckTest extends TracerTestBase {

  @ParameterizedTest
  @MethodSource("blsPairingCheckSource")
  void testBlsPairingCheckTest(
      List<String> smallPoints, List<String> largePoints, TestInfo testInfo) {
    checkArgument(smallPoints.size() == largePoints.size());
    Bytes input =
        IntStream.range(0, smallPoints.size())
            .mapToObj(
                i ->
                    Bytes.concatenate(
                        Bytes.fromHexString(smallPoints.get(i)),
                        Bytes.fromHexString(largePoints.get(i))))
            .reduce(Bytes.EMPTY, Bytes::concatenate);

    BytecodeCompiler program = BytecodeCompiler.newProgram(chainConfig);

    // TODO: extract method for that
    final Address codeOwnerAddress = Address.fromHexString("0xC0DE");
    final ToyAccount codeOwnerAccount =
        ToyAccount.builder()
            .balance(Wei.of(0))
            .nonce(1)
            .address(codeOwnerAddress)
            .code(input)
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
        .push(Address.BLS12_PAIRING) // address
        .push(Bytes.fromHexStringLenient("0xFFFFFFFF")) // gas
        .op(OpCode.STATICCALL);
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    bytecodeRunner.run(List.of(codeOwnerAccount), chainConfig, testInfo);
  }

  private static Stream<Arguments> blsPairingCheckSource() {
    List<Arguments> arguments = new ArrayList<>();
    for (String s1 : SMALL_POINTS) {
      for (String l1 : LARGE_POINTS) {
        arguments.add(Arguments.of(List.of(s1), List.of(l1)));
        for (String s2 : SMALL_POINTS) {
          for (String l2 : LARGE_POINTS) {
            arguments.add(Arguments.of(List.of(s1, s2), List.of(l1, l2)));
            for (String s3 : SMALL_POINTS) {
              for (String l3 : LARGE_POINTS) {
                arguments.add(Arguments.of(List.of(s1, s2, s3), List.of(l1, l2, l3)));
              }
            }
          }
        }
      }
    }
    return arguments.stream();
  }
}
