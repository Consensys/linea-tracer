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

import java.util.stream.Stream;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.testing.DynamicTests;
import org.apache.tuweni.bytes.Bytes32;
import org.apache.tuweni.units.bigints.UInt256;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class MxpTracerTest {
  //  private static final Random RAND = new Random();
  private static final int TEST_REPETITIONS = 2;
  private static final Module MODULE = new Mxp();
  private static final DynamicTests DYN_TESTS = DynamicTests.forModule(MODULE);

  @TestFactory
  Stream<DynamicTest> runDynamicTests() {
    return DYN_TESTS
        .testCase("non random arguments test", provideNonRandomArguments())
        .testCase("simple mload arguments test", simpleMloadArgs())
        .testCase("one of each type1 instruction MLOAD, MSTORE, MSTORE8", simpleType1Args())
        .run();
  }

  private Multimap<OpCode, Bytes32> provideNonRandomArguments() {
    return DYN_TESTS.newModuleArgumentsProvider(
        (arguments, opCode) -> {
          for (int i = 0; i < TEST_REPETITIONS; i++) {
            for (int j = 0; j < opCode.getData().numberOfArguments(); j++) {
              arguments.put(opCode, UInt256.valueOf(j));
            }
          }
        });
  }

  protected Multimap<OpCode, Bytes32> simpleMloadArgs() {
    Multimap<OpCode, Bytes32> arguments = ArrayListMultimap.create();
    Bytes32 arg1 =
        Bytes32.fromHexString("0xdcd5cf52e4daec5389587d0d0e996e6ce2d0546b63d3ea0a0dc48ad984d180a9");
    arguments.put(MLOAD, arg1);
    return arguments;
  }

  protected Multimap<OpCode, Bytes32> simpleType1Args() {
    // one of each type1 instruction MLOAD, MSTORE, MSTORE8
    Multimap<OpCode, Bytes32> arguments = ArrayListMultimap.create();
    Bytes32 arg1 =
        Bytes32.fromHexString("0xdcd5cf52e4daec5389587d0d0e996e6ce2d0546b63d3ea0a0dc48ad984d180a9");
    arguments.put(MLOAD, arg1);
    arguments.put(MSTORE, arg1);
    arguments.put(MSTORE8, arg1);
    return arguments;
  }
}
