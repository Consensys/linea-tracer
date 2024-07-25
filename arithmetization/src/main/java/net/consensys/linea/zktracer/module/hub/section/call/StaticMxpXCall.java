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

package net.consensys.linea.zktracer.module.hub.section.call;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.oob.OobCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.oob.opcodes.XCallOobCall;
import net.consensys.linea.zktracer.module.hub.fragment.scenario.ScenarioFragment;
import net.consensys.linea.zktracer.module.hub.section.TraceSection;

public class StaticMxpXCall extends TraceSection {
  public StaticMxpXCall(
      Hub hub, final ContextFragment currentContextFragment, final ImcFragment firstImcFragment) {
    super(hub, (short) 6);
    hub.addTraceSection(this);

    final ScenarioFragment scenarioFragment = new ScenarioFragment(); // TODO

    final OobCall oobCall = new XCallOobCall();
    firstImcFragment.callOob(oobCall);

    this.addFragmentsAndStack(hub, scenarioFragment, currentContextFragment, firstImcFragment);
  }
}
