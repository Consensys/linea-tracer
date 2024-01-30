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

package net.consensys.linea.zktracer.module.hub.subsection;

import java.util.List;

import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.PrecompileScenarioFragment;
import net.consensys.linea.zktracer.module.hub.fragment.SubSection;
import net.consensys.linea.zktracer.module.hub.fragment.TraceFragment;
import net.consensys.linea.zktracer.module.hub.fragment.misc.MiscFragment;
import net.consensys.linea.zktracer.module.hub.memory.MemorySpan;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import net.consensys.linea.zktracer.runtime.callstack.CallStack;

@RequiredArgsConstructor
public class PrecompileScenarioTraceSubsection implements SubSection {
  private final CallStack callStack;
  private final CallFrame callFrame;
  private final PrecompileScenario scenario;
  private final long callGas;
  private final MemorySpan callData;
  private final MemorySpan returnDataTarget;

  @Override
  public List<TraceFragment> generate() {
    switch (this.scenario.precompile()) {
      case EC_RECOVER -> {
        assert !scenario.failureKnownToRam();

        if (scenario.failureKnownToHub()) {
          return List.of(
              new PrecompileScenarioFragment(this.scenario),
              new MiscFragment(scenario),
              new ContextFragment(this.callStack, this.callFrame, true));
        } else {
          return List.of(
              new PrecompileScenarioFragment(this.scenario),
              new MiscFragment(scenario),
              new MiscFragment(scenario),
              new MiscFragment(scenario),
              new ContextFragment(this.callStack, this.callFrame, true));
        }
      }
      case SHA2_256 -> {}
      case RIPEMD_160 -> {}
      case IDENTITY -> {}
      case MODEXP -> {}
      case EC_ADD -> {}
      case EC_MUL -> {}
      case EC_PAIRING -> {}
      case BLAKE2F -> {}
    }

    return List.of(); // TODO:
  }
}
