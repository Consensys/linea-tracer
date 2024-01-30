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

package net.consensys.linea.zktracer.module.hub.fragment;

import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.module.hub.Precompile;
import net.consensys.linea.zktracer.module.hub.Trace;
import net.consensys.linea.zktracer.module.hub.subsection.PrecompileScenario;

@RequiredArgsConstructor
public class PrecompileScenarioFragment implements TraceFragment {
  private final PrecompileScenario scenario;

  @Override
  public Trace trace(Trace trace) {
    return trace
        .peekAtScenario(true)
        .pScenarioPrcSuccess(this.scenario.success())
        .pScenarioPrcFailureKnownToHub(this.scenario.failureKnownToHub())
        .pScenarioPrcFailureKnownToRam(this.scenario.failureKnownToRam())
        .pScenarioBlake2F(scenario.precompile().equals(Precompile.BLAKE2F))
        .pScenarioEcadd(scenario.precompile().equals(Precompile.EC_ADD))
        .pScenarioEcmul(scenario.precompile().equals(Precompile.EC_MUL))
        .pScenarioEcpairing(scenario.precompile().equals(Precompile.EC_PAIRING))
        .pScenarioEcrecover(scenario.precompile().equals(Precompile.EC_RECOVER))
        .pScenarioIdentity(scenario.precompile().equals(Precompile.IDENTITY))
        .pScenarioModexp(scenario.precompile().equals(Precompile.MODEXP))
        .pScenarioRipemd160(scenario.precompile().equals(Precompile.RIPEMD_160))
        .pScenarioSha2256(scenario.precompile().equals(Precompile.SHA2_256));
  }
}
