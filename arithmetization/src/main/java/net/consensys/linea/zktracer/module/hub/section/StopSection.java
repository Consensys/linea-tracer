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
package net.consensys.linea.zktracer.module.hub.section;

import static net.consensys.linea.zktracer.module.hub.fragment.ContextFragment.executionProvidesEmptyReturnData;
import static net.consensys.linea.zktracer.module.hub.fragment.ContextFragment.readContextData;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.TraceFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;

public class StopSection extends TraceSection {

  public StopSection() {}

  public StopSection(Hub hub, TraceFragment... fragments) {
    this.addFragmentsAndStack(hub, fragments);
  }

  public static StopSection messageCallStopSection(Hub hub) {
    return new StopSection(hub, readContextData(hub), executionProvidesEmptyReturnData(hub));
  }

  public static StopSection revertedDeploymentStopSection(Hub hub) {
    AccountFragment.AccountFragmentFactory accountFragmentFactory =
        hub.factories().accountFragment();
    return new StopSection(
        hub,
        readContextData(hub),
        // current (under deployment => deployed with empty byte code)
        // undoing of the above
        executionProvidesEmptyReturnData(hub));
  }

  public static StopSection unrevertedDeploymentStopSection(Hub hub) {
    return new StopSection(
        hub,
        readContextData(hub),
        // current (under deployment => deployed with empty byte code)
        executionProvidesEmptyReturnData(hub));
  }
}
