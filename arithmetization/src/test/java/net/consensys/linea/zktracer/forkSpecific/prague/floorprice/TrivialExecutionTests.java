/*
 * Copyright ConsenSys Inc.
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

package net.consensys.linea.zktracer.forkSpecific.prague.floorprice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.google.common.base.Preconditions;
import net.consensys.linea.UnitTestWatcher;
import net.consensys.linea.reporting.TracerTestBase;
import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@ExtendWith(UnitTestWatcher.class)
public class TrivialExecutionTests extends TracerTestBase {

  @Test
  void txSkipTest(TestInfo testInfo) {}

  /**
   * The 'to' address has byte code equal to 0x00. The transaction does immediately stop.
   *
   * @param testInfo
   */
  @ParameterizedTest
  @MethodSource("trivialCalleeTestSource")
  void trivialCalleeTest(Bytes callData, TestInfo testInfo) {
    BytecodeCompiler program = BytecodeCompiler.newProgram(chainConfig);
    program.op(OpCode.STOP);
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    bytecodeRunner.run(callData, chainConfig, testInfo);
  }

  static Stream<Arguments> trivialCalleeTestSource() {
    List<Arguments> arguments = new ArrayList<>();
    for (CallDataSetting callDataSetting : CallDataSetting.values()) {
      for (boolean startsWithZero : new boolean[] {true, false}) {
        for (int length = 1; length <= 10; length++) {
          arguments.add(Arguments.of(buildCallData(callDataSetting, startsWithZero, 1)));
        }
      }
    }
    return arguments.stream();
  }

  // Support enums and methods
  enum CallDataSetting {
    ALL_ZEROS,
    ZEROS_AND_NON_ZEROS
  }

  static Bytes buildCallData(CallDataSetting callDataSetting, boolean startsWithZero, int length) {
    Preconditions.checkArgument(length > 0, "length must be positive");
    return switch (callDataSetting) {
      case ALL_ZEROS -> Bytes.fromHexString("00".repeat(length));
      case ZEROS_AND_NON_ZEROS -> Bytes.fromHexString(
          (startsWithZero ? "00" : "01")
              + "ff00".repeat((length - 1) / 2)
              + (length % 2 == 0 ? "ff" : ""));
    };
  }

  // Testing support method
  @Test
  void buildCallDataTest(TestInfo testInfo) {
    for (int size = 1; size <= 4; size++) {
      Preconditions.checkArgument(
          buildCallData(CallDataSetting.ZEROS_AND_NON_ZEROS, false, size).size() == size);
    }
  }
}
