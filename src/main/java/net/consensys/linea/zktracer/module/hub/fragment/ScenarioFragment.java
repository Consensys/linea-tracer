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

import net.consensys.linea.zktracer.module.hub.Trace;

/** This machine generates lines */
public abstract class ScenarioFragment implements TraceFragment {
  protected abstract boolean[] computeFlags();

  @Override
  public Trace.TraceBuilder trace(Trace.TraceBuilder trace) {
    final boolean[] flags = computeFlags();
    return trace
        .peekAtScenario(true)
        .pScenarioScn1(flags[0])
        .pScenarioScn2(flags[1])
        .pScenarioScn3(flags[2])
        .pScenarioScn4(flags[3])
        .pScenarioScn5(flags[4])
        .pScenarioScn6(flags[5])
        .pScenarioScn7(flags[6])
        .pScenarioScn8(flags[7])
        .pScenarioScn9(flags[8])
        .pScenarioScn10(flags[9])
        .pScenarioScn11(flags[10])
        .pScenarioScn12(flags[11]);
  }
}
