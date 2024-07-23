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

import static net.consensys.linea.zktracer.module.hub.fragment.scenario.ScenarioEnum.CREATE_NON_EMPTY_INIT_CODE_FAILURE_WILL_REVERT;
import static net.consensys.linea.zktracer.module.hub.fragment.scenario.ScenarioEnum.CREATE_NON_EMPTY_INIT_CODE_FAILURE_WONT_REVERT;
import static net.consensys.linea.zktracer.module.hub.fragment.scenario.ScenarioEnum.CREATE_NON_EMPTY_INIT_CODE_SUCCESS_WILL_REVERT;
import static net.consensys.linea.zktracer.module.hub.fragment.scenario.ScenarioEnum.CREATE_NON_EMPTY_INIT_CODE_SUCCESS_WONT_REVERT;

import net.consensys.linea.zktracer.module.hub.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.DomSubStampsSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.RlpAddrSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.scenario.CreateScenarioFragment;
import net.consensys.linea.zktracer.module.hub.fragment.scenario.ScenarioEnum;
import net.consensys.linea.zktracer.module.hub.section.TraceSection;

public class NonEmptyInitCodeCreate extends TraceSection implements FillCreateSection {
  final int hubStamp;
  final CreateScenarioFragment scenarioFragment;

  public NonEmptyInitCodeCreate(
      final Hub hub, final ContextFragment commonContext, final ImcFragment imcFragment) {
    super(hub, (short) 11);
    hub.addTraceSection(this);

    this.hubStamp = hub.stamp();
    this.scenarioFragment = new CreateScenarioFragment();

    this.addFragmentsAndStack(hub, scenarioFragment, commonContext, imcFragment);
    hub.romLex().triggerRomLex(hub.messageFrame());
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
    final ScenarioEnum scenario =
        createSuccess
            ? CREATE_NON_EMPTY_INIT_CODE_SUCCESS_WONT_REVERT
            : CREATE_NON_EMPTY_INIT_CODE_FAILURE_WONT_REVERT;
    this.scenarioFragment.setScenario(scenario);

    final AccountFragment.AccountFragmentFactory accountFragmentFactory =
        hub.factories().accountFragment();

    final AccountFragment oldToMidCreatorAccountFragment =
        accountFragmentFactory.make(
            oldCreatorSnapshot,
            midCreatorSnapshot,
            DomSubStampsSubFragment.standardDomSubStamps(hubStamp, 0));
    oldToMidCreatorAccountFragment.rlpAddrSubFragment(rlpAddrSubFragment);

    final AccountFragment oldToMidCreatedAccountFragment =
        accountFragmentFactory.make(
            oldCreatedSnapshot,
            midCreatedSnapshot,
            DomSubStampsSubFragment.standardDomSubStamps(hubStamp, 1));
    oldToMidCreatedAccountFragment.requiresRomlex(true);

    this.addFragmentsWithoutStack(oldToMidCreatorAccountFragment, oldToMidCreatedAccountFragment);

    if (!createSuccess) {
      final AccountFragment midToNewCreatorAccountFragment =
          accountFragmentFactory.make(
              midCreatorSnapshot,
              newCreatorSnapshot,
              DomSubStampsSubFragment.revertsWithChildDomSubStamps(hubStamp, childRevertStamp, 2));

      final AccountFragment midToNewCreatedAccountFragment =
          accountFragmentFactory.make(
              midCreatedSnapshot,
              newCreatedSnapshot,
              DomSubStampsSubFragment.revertsWithChildDomSubStamps(hubStamp, childRevertStamp, 3));

      this.addFragmentsWithoutStack(midToNewCreatorAccountFragment, midToNewCreatedAccountFragment);
    }
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

    final ScenarioEnum newScenario =
        createSuccess
            ? CREATE_NON_EMPTY_INIT_CODE_SUCCESS_WILL_REVERT
            : CREATE_NON_EMPTY_INIT_CODE_FAILURE_WILL_REVERT;
    this.scenarioFragment.setScenario(newScenario);

    final AccountFragment.AccountFragmentFactory accountFragmentFactory =
        hub.factories().accountFragment();

    final AccountFragment undoCreatorAccountFragment =
        accountFragmentFactory.make(
            createSuccess ? midCreatorSnapshot : newCreatorSnapshot,
            oldCreatorSnapshot,
            DomSubStampsSubFragment.revertWithCurrentDomSubStamps(
                hubStamp, currentRevertStamp, createSuccess ? 2 : 4));
    final AccountFragment undoCreatedAccountFragment =
        accountFragmentFactory.make(
            createSuccess ? midCreatedSnapshot : newCreatedSnapshot,
            oldCreatedSnapshot,
            DomSubStampsSubFragment.revertWithCurrentDomSubStamps(
                hubStamp, currentRevertStamp, createSuccess ? 3 : 5));

    this.addFragmentsWithoutStack(undoCreatorAccountFragment, undoCreatedAccountFragment);
  }

  @Override
  public void fillContextFragment(ContextFragment contextFragment) {
    this.addFragment(contextFragment);
  }
}
