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

import static net.consensys.linea.zktracer.module.hub.fragment.scenario.CallScenarioFragment.CallScenario.CALL_EXCEPTION;

import net.consensys.linea.zktracer.module.hub.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.Factories;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.DomSubStampsSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.scenario.CallScenarioFragment;
import net.consensys.linea.zktracer.module.hub.section.TraceSection;
import org.apache.tuweni.bytes.Bytes;

public class OogXCall extends TraceSection {
  public OogXCall(
      Hub hub,
      final ContextFragment currentContextFragment,
      final ImcFragment firstImcFragment,
      final AccountSnapshot preOpcodeCallerAccount,
      final AccountSnapshot preOpcodeCalleeAccount,
      final Bytes rawCalleeAddress) {
    super(hub, (short) 6);
    hub.addTraceSection(this);

    final CallScenarioFragment scenarioFragment = new CallScenarioFragment(CALL_EXCEPTION);

    final Factories factories = hub.factories();

    final AccountFragment callerAccountFragment =
        factories
            .accountFragment()
            .make(
                preOpcodeCallerAccount,
                preOpcodeCallerAccount,
                DomSubStampsSubFragment.standardDomSubStamps(this.hubStamp(), 0));

    final AccountFragment calleeAccountFragment =
        factories
            .accountFragment()
            .makeWithTrm(
                preOpcodeCalleeAccount,
                preOpcodeCalleeAccount,
                rawCalleeAddress,
                DomSubStampsSubFragment.standardDomSubStamps(this.hubStamp(), 1));

    this.addFragmentsAndStack(
        hub,
        scenarioFragment,
        currentContextFragment,
        firstImcFragment,
        callerAccountFragment,
        calleeAccountFragment);
  }
}
