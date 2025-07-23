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
package net.consensys.linea.zktracer.module.hub.section.halt.selfdestruct;

import static net.consensys.linea.zktracer.module.hub.fragment.scenario.SelfdestructScenarioFragment.SelfdestructScenario.*;

import net.consensys.linea.zktracer.module.hub.Hub;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class CancunSelfdestructSection extends LondonSelfdestructSection {

  public CancunSelfdestructSection(Hub hub, MessageFrame frame) {
    super(hub, frame);
  }

  @Override
  public void resolveAtEndTransaction(
      Hub hub, WorldView state, Transaction tx, boolean isSuccessful) {

    if (selfDestructWasReverted) {
      this.addFragment(finalUnexceptionalContextFragment);
      return;
    }

    // Default values that can be overridden by handleAccountWiping
    selfdestructScenarioFragment.setScenario(SELFDESTRUCT_WONT_REVERT_ALREADY_MARKED);
    this.addFragment(finalUnexceptionalContextFragment);

    // From Cancun, we only handle the account wiping if self-destruct is in the same transaction
    if (!transactionProcessingMetadata
        .hadCodeInitiallyMap()
        .get(selfdestructor.address())
        .hadCode()) {
      handleAccountWiping(hub);
    }
  }
}
