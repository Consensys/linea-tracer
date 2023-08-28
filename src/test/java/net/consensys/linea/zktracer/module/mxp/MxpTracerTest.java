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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import net.consensys.linea.zktracer.AbstractModuleCorsetTest;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.units.bigints.UInt256;
import org.junit.jupiter.params.provider.Arguments;

public class MxpTracerTest extends AbstractModuleCorsetTest {
  private static final int TEST_REPETITIONS = 4;

  @Override
  protected Module getModuleTracer() {
    return new Mxp();
  }

  @Override
  protected Stream<Arguments> provideNonRandomArguments() {
    List<Arguments> arguments = new ArrayList<>();
    for (OpCode opCode : getModuleTracer().supportedOpCodes()) {
      for (int k = 1; k <= TEST_REPETITIONS; k++) {
        for (int i = 1; i <= TEST_REPETITIONS; i++) {
          arguments.add(
              Arguments.of(opCode.getData(), List.of(UInt256.valueOf(i), UInt256.valueOf(k))));
        }
      }
    }
    return arguments.stream();
  }
}
