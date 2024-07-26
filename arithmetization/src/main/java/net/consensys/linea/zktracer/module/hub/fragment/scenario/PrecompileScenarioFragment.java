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

package net.consensys.linea.zktracer.module.hub.fragment.scenario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.consensys.linea.zktracer.module.hub.Trace;
import net.consensys.linea.zktracer.module.hub.fragment.TraceFragment;
import net.consensys.linea.zktracer.module.hub.section.call.precompileSubsection.PrecompileFlag;
import net.consensys.linea.zktracer.module.hub.section.call.precompileSubsection.PrecompileScenario;

@Getter
@AllArgsConstructor
public class PrecompileScenarioFragment implements TraceFragment {

  @Setter PrecompileScenario scenario;
  final PrecompileFlag flag;

  public PrecompileScenarioFragment(final PrecompileFlag flag, final PrecompileScenario scenario) {
    this.flag = flag;
    this.scenario = scenario;
  }

  @Override
  public Trace trace(Trace trace) {
    return null;
  }
}
