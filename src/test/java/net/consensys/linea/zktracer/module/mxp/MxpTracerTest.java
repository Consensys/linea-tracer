/*
 * Copyright ConsenSys AG.
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

package net.consensys.linea.zktracer.module.mxp;

import static net.consensys.linea.zktracer.opcode.OpCode.MLOAD;
import static net.consensys.linea.zktracer.opcode.OpCode.MSTORE;
import static net.consensys.linea.zktracer.opcode.OpCode.MSTORE8;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import net.consensys.linea.zktracer.AbstractModuleCorsetTest;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.opcode.OpCodeData;
import org.apache.tuweni.bytes.Bytes32;
import org.apache.tuweni.units.bigints.UInt256;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class MxpTracerTest extends AbstractModuleCorsetTest {
  private static final int TEST_REPETITIONS = 2;

  @ParameterizedTest()
  @MethodSource("simpleMloadArgs")
  void simpleMloadTest(OpCodeData opCode, final List<Bytes32> args) {
    runTest(opCode, args);
  }

  @ParameterizedTest()
  @MethodSource("simpleType1Args")
  void type1InstructionsTest(OpCodeData opCode, final List<Bytes32> args) {
    runTest(opCode, args);
  }

  @Override
  protected Stream<Arguments> provideNonRandomArguments() {
    final List<Arguments> arguments = new ArrayList<>();

    for (OpCode opCode : getModuleTracer().supportedOpCodes()) {
      for (int i = 0; i <= TEST_REPETITIONS; i++) {
        List<Bytes32> args = new ArrayList<>();
        for (int j = 0; j < opCode.getData().numberOfArguments(); j++) {
          args.add(UInt256.valueOf(j));
        }
        arguments.add(Arguments.of(opCode.getData(), args));
      }
    }

    return arguments.stream();
  }

  protected Stream<Arguments> simpleMloadArgs() {
    Bytes32 arg1 =
        Bytes32.fromHexString("0xdcd5cf52e4daec5389587d0d0e996e6ce2d0546b63d3ea0a0dc48ad984d180a9");
    return Stream.of(Arguments.of(MLOAD.getData(), List.of(arg1)));
  }

  protected Stream<Arguments> simpleType1Args() {
    // one of each type1 instruction MLOAD, MSTORE, MSTORE8
    Bytes32 arg1 =
        Bytes32.fromHexString("0xdcd5cf52e4daec5389587d0d0e996e6ce2d0546b63d3ea0a0dc48ad984d180a9");
    Bytes32 arg2 =
        Bytes32.fromHexString("0x0479484af4a59464a48818b3980174687661bafb13d06f49537995fa6c02159e");
    return Stream.of(
        Arguments.of(MLOAD.getData(), List.of(arg2)),
        Arguments.of(MSTORE.getData(), List.of(arg1, arg2)),
        Arguments.of(MSTORE8.getData(), List.of(arg1, arg2)));
  }

  @Override
  protected Module getModuleTracer() {
    return new Mxp();
  }
}
