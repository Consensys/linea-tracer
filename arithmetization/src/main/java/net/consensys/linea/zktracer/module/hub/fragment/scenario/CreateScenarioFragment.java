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
package net.consensys.linea.zktracer.module.hub.fragment.scenario;

import static net.consensys.linea.zktracer.module.hub.fragment.scenario.ReturnScenarioFragment.ReturnScenario.*;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import net.consensys.linea.zktracer.module.hub.Trace;
import net.consensys.linea.zktracer.module.hub.fragment.TraceFragment;

public class CreateScenarioFragment implements TraceFragment {

  @Setter @Getter private ScenarioEnum scenario;

  public CreateScenarioFragment() {
    this.scenario = ScenarioEnum.UNDEFINED;
  }

  public CreateScenarioFragment(ScenarioEnum scenario) {
    this.scenario = scenario;
  }

  @Override
  public Trace trace(Trace trace) {
    Preconditions.checkArgument(this.scenario.isCreate());
    return trace
        .peekAtScenario(true)
        // CREATE scenarios
        .pScenarioCreateException(scenario == ScenarioEnum.CREATE_EXCEPTION)
        .pScenarioCreateAbort(scenario == ScenarioEnum.CREATE_ABORT)
        .pScenarioCreateFailureConditionWillRevert(
            scenario == ScenarioEnum.CREATE_NON_EMPTY_INIT_CODE_FAILURE_WILL_REVERT)
        .pScenarioCreateFailureConditionWontRevert(
            scenario == ScenarioEnum.CREATE_FAILURE_CONDITION_WONT_REVERT)
        .pScenarioCreateEmptyInitCodeWillRevert(
            scenario == ScenarioEnum.CREATE_EMPTY_INIT_CODE_WILL_REVERT)
        .pScenarioCreateEmptyInitCodeWontRevert(
            scenario == ScenarioEnum.CREATE_EMPTY_INIT_CODE_WONT_REVERT)
        .pScenarioCreateNonemptyInitCodeFailureWillRevert(
            scenario == ScenarioEnum.CREATE_NON_EMPTY_INIT_CODE_FAILURE_WILL_REVERT)
        .pScenarioCreateNonemptyInitCodeFailureWontRevert(
            scenario == ScenarioEnum.CREATE_NON_EMPTY_INIT_CODE_FAILURE_WONT_REVERT)
        .pScenarioCreateNonemptyInitCodeSuccessWillRevert(
            scenario == ScenarioEnum.CREATE_NON_EMPTY_INIT_CODE_SUCCESS_WILL_REVERT)
        .pScenarioCreateNonemptyInitCodeSuccessWontRevert(
            scenario == ScenarioEnum.CREATE_NON_EMPTY_INIT_CODE_SUCCESS_WONT_REVERT);
  }
}