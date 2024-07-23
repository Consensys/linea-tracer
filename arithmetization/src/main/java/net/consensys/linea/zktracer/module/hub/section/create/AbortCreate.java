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

package net.consensys.linea.zktracer.module.hub.section.create;

import static net.consensys.linea.zktracer.module.hub.fragment.scenario.ScenarioEnum.CREATE_ABORT;

import net.consensys.linea.zktracer.module.hub.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.DomSubStampsSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.scenario.CreateScenarioFragment;
import net.consensys.linea.zktracer.module.hub.section.TraceSection;

class AbortCreate extends TraceSection {
  public AbortCreate(
      final Hub hub,
      final ContextFragment commonContext,
      final ImcFragment imcFragment,
      final AccountSnapshot oldCreatorSnapshot) {
    super(hub, (short) 7);
    hub.addTraceSection(this);

    final CreateScenarioFragment scenarioFragment = new CreateScenarioFragment(CREATE_ABORT);

    final AccountFragment.AccountFragmentFactory accountFragmentFactory =
        hub.factories().accountFragment();
    final AccountFragment creatorAccountFragment =
        accountFragmentFactory.make(
            oldCreatorSnapshot,
            oldCreatorSnapshot,
            DomSubStampsSubFragment.standardDomSubStamps(this.hubStamp(), 0));

    final ContextFragment contextFragment = ContextFragment.nonExecutionEmptyReturnData(hub);

    this.addFragmentsAndStack(
        hub, scenarioFragment, commonContext, imcFragment, creatorAccountFragment, contextFragment);
  }
}
