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
package net.consensys.linea.zktracer.module.trm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import net.consensys.linea.zktracer.AbstractModuleTracerCorsetTest;
import net.consensys.linea.zktracer.OpCode;
import net.consensys.linea.zktracer.module.ModuleTracer;
import org.apache.tuweni.bytes.Bytes32;
import org.apache.tuweni.units.bigints.UInt256;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrmTracerTest extends AbstractModuleTracerCorsetTest {
  private final Random rand = new Random();

  @Override
  protected Stream<Arguments> provideNonRandomArguments() {
    List<Arguments> arguments = new ArrayList<>();
    for (OpCode opCode : getModuleTracer().supportedOpCodes()) {
      for (int i = 1; i <= 4; i++) {
        arguments.add(Arguments.of(opCode, List.of(UInt256.valueOf(i))));
      }
    }
    return arguments.stream();
  }

  @Override
  public Stream<Arguments> provideRandomArguments() {
    final List<Arguments> arguments = new ArrayList<>();
    for (OpCode opCode : getModuleTracer().supportedOpCodes()) {
      for (int i = 0; i <= 16; i++) {
        arguments.add(Arguments.of(opCode, List.of(Bytes32.random(rand))));
      }
    }
    return arguments.stream();
  }

  @Override
  protected ModuleTracer getModuleTracer() {
    return new TrmTracer();
  }
}
