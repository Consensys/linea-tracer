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

import static net.consensys.linea.zktracer.module.hub.fragment.scenario.CreateScenarioFragment.CreateScenario.CREATE_EMPTY_INIT_CODE_WILL_REVERT;
import static net.consensys.linea.zktracer.module.hub.fragment.scenario.CreateScenarioFragment.CreateScenario.CREATE_FAILURE_CONDITION_WILL_REVERT;
import static net.consensys.linea.zktracer.module.hub.fragment.scenario.CreateScenarioFragment.CreateScenario.CREATE_FAILURE_CONDITION_WONT_REVERT;

import net.consensys.linea.zktracer.module.hub.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.DomSubStampsSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.RlpAddrSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.scenario.CreateScenarioFragment;
import net.consensys.linea.zktracer.module.hub.section.TraceSection;

public class FailureOrEmptyInitCreate extends TraceSection implements FillCreateSection {
  final CreateScenarioFragment scenarioFragment;

  public FailureOrEmptyInitCreate(
      final Hub hub,
      final CreateScenarioFragment.CreateScenario scenario,
      final ContextFragment currentContextFragment,
      final ImcFragment imcFragment) {
    super(hub, (short) 10);
    hub.addTraceSection(this);

    this.scenarioFragment = new CreateScenarioFragment(scenario);

    this.addFragmentsAndStack(hub, scenarioFragment, currentContextFragment, imcFragment);
  }

  @Override
  public void fillAccountFragment(
      Hub hub,
      boolean createSuccess,
      int childRevertStamp,
      RlpAddrSubFragment rlpAddrSubFragment,
      AccountSnapshot oldCreatorSnapshot,
      AccountSnapshot midCreatorSnapshot,
      AccountSnapshot newCreatorSnapshot,
      AccountSnapshot oldCreatedSnapshot,
      AccountSnapshot midCreatedSnapshot,
      AccountSnapshot newCreatedSnapshot) {
    final AccountFragment.AccountFragmentFactory accountFragmentFactory =
        hub.factories().accountFragment();

    final AccountFragment creatorAccountFragment =
        accountFragmentFactory.make(
            oldCreatorSnapshot,
            newCreatorSnapshot,
            DomSubStampsSubFragment.standardDomSubStamps(this.hubStamp(), 0));
    creatorAccountFragment.rlpAddrSubFragment(rlpAddrSubFragment);

    final AccountFragment createdAccountFragment =
        accountFragmentFactory.make(
            oldCreatedSnapshot,
            newCreatedSnapshot,
            DomSubStampsSubFragment.standardDomSubStamps(this.hubStamp(), 1));

    this.addFragmentsWithoutStack(creatorAccountFragment, createdAccountFragment);
  }

  @Override
  public void fillReverting(
      Hub hub,
      boolean createSuccess,
      int currentRevertStamp,
      AccountSnapshot oldCreatorSnapshot,
      AccountSnapshot midCreatorSnapshot,
      AccountSnapshot newCreatorSnapshot,
      AccountSnapshot oldCreatedSnapshot,
      AccountSnapshot midCreatedSnapshot,
      AccountSnapshot newCreatedSnapshot) {
    final CreateScenarioFragment.CreateScenario newScenario =
        scenarioFragment.getScenario() == CREATE_FAILURE_CONDITION_WONT_REVERT
            ? CREATE_FAILURE_CONDITION_WILL_REVERT
            : CREATE_EMPTY_INIT_CODE_WILL_REVERT;
    this.scenarioFragment.setScenario(newScenario);

    final AccountFragment.AccountFragmentFactory accountFragmentFactory =
        hub.factories().accountFragment();

    final AccountFragment undoCreatorAccountFragment =
        accountFragmentFactory.make(
            newCreatorSnapshot,
            oldCreatorSnapshot,
            DomSubStampsSubFragment.revertWithCurrentDomSubStamps(
                this.hubStamp(), currentRevertStamp, 2));
    final AccountFragment undoCreatedAccountFragment =
        accountFragmentFactory.make(
            newCreatedSnapshot,
            oldCreatedSnapshot,
            DomSubStampsSubFragment.revertWithCurrentDomSubStamps(
                this.hubStamp(), currentRevertStamp, 3));

    this.addFragmentsWithoutStack(undoCreatorAccountFragment, undoCreatedAccountFragment);
  }

  @Override
  public void fillContextFragment(ContextFragment contextFragment) {
    this.addFragment(contextFragment);
  }
}
