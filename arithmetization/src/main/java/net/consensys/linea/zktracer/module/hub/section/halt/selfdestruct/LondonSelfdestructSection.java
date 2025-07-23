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

import static com.google.common.base.Preconditions.checkArgument;
import static net.consensys.linea.zktracer.module.hub.fragment.scenario.SelfdestructScenarioFragment.SelfdestructScenario.*;

import java.util.Map;

import net.consensys.linea.zktracer.module.hub.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.DomSubStampsSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import net.consensys.linea.zktracer.module.hub.section.halt.EphemeralAccount;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class LondonSelfdestructSection extends SelfdestructSection {

  public LondonSelfdestructSection(Hub hub, MessageFrame frame) {
    super(hub, frame);
  }

  @Override
  public void resolveAtEndTransaction(
      Hub hub, WorldView state, Transaction tx, boolean isSuccessful) {

    if (selfDestructWasReverted) {
      this.addFragment(finalUnexceptionalContextFragment);
      return;
    }

    // beyond this point the selfdestruct was not reverted
    final Map<EphemeralAccount, Integer> effectiveSelfDestructMap =
        transactionProcessingMetadata.getEffectiveSelfDestructMap();
    final EphemeralAccount ephemeralAccount =
        new EphemeralAccount(selfdestructor.address(), selfdestructorNew.deploymentNumber());

    checkArgument(effectiveSelfDestructMap.containsKey(ephemeralAccount));

    // This grabs the accounts right after the coinbase and sender got their gas money back
    // in particular this will get the coinbase address post gas reward.
    final AccountSnapshot accountWiping =
        transactionProcessingMetadata.getDestructedAccountsSnapshot().stream()
            .filter(accountSnapshot -> accountSnapshot.address().equals(selfdestructor.address()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Account not found"));

    // We modify the account fragment to reflect the self-destruct time
    final int hubStampOfTheActionableSelfDestruct = effectiveSelfDestructMap.get(ephemeralAccount);
    checkArgument(hubStamp >= hubStampOfTheActionableSelfDestruct);

    if (hubStamp == hubStampOfTheActionableSelfDestruct) {
      selfdestructScenarioFragment.setScenario(SELFDESTRUCT_WONT_REVERT_NOT_YET_MARKED);

      accountWipingNew = accountWiping.deepCopy();
      // the hub's defers.resolvePostTransaction() gets called after the
      // hub's completeLineaTransaction which in turn calls
      // freshDeploymentNumberFinishingSelfdestruct()
      // which raises the deployment number and sets the deployment status to false
      final AccountFragment accountWipingFragment =
          hub.factories()
              .accountFragment()
              .make(
                  accountWiping,
                  accountWipingNew,
                  DomSubStampsSubFragment.selfdestructDomSubStamps(hub, hubStamp));

      this.addFragment(accountWipingFragment);
      this.addFragment(finalUnexceptionalContextFragment);

      hub.defers().scheduleForAfterTransactionFinalization(this);

    } else {
      selfdestructScenarioFragment.setScenario(SELFDESTRUCT_WONT_REVERT_ALREADY_MARKED);
      this.addFragment(finalUnexceptionalContextFragment);
    }
  }
}
