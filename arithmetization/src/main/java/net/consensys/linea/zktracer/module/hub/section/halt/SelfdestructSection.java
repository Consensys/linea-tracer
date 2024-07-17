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
package net.consensys.linea.zktracer.module.hub.section.halt;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.defer.PostTransactionDefer;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import net.consensys.linea.zktracer.module.hub.fragment.scenario.SelfdestructScenarioFragment;
import net.consensys.linea.zktracer.module.hub.section.TraceSection;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class SelfdestructSection extends TraceSection implements PostTransactionDefer {

  public SelfdestructSection(Hub hub) {
    // up to 8 = 1 + 7 rows
    super(hub, (short) 8);
    hub.addTraceSection(this);

    short exceptions = hub.pch().exceptions();

    // STATICX case
    // SCN fragment
    SelfdestructScenarioFragment selfdestructScenarioFragment = new SelfdestructScenarioFragment();
    this.addFragment(selfdestructScenarioFragment);

    // CON fragment (1)
    ContextFragment contextFragment = ContextFragment.readCurrentContextData(hub);
    this.addFragment(contextFragment);

    if (Exceptions.staticFault(exceptions)) {
      return;
    }

    // OOGX case
    // ACC fragment (1)
    AccountFragment accountFragment1 = null;
    this.addFragment(accountFragment1);

    // ACC fragment (2)
    AccountFragment accountFragment2 = null;
    this.addFragment(accountFragment2);

    if (Exceptions.outOfGasException(exceptions)) {
        return;
    }

    // From now on everything is deferred
    boolean willRevert = hub.callStack().frames().getLast().willRevert(); // ?

    boolean wontRevertAlreadyMarked;

    boolean wontRevertNotYetMarked;

    // TODO: look at EXTCODECOPY, RETURN, ACCOUNT
  }

  @Override
  public void resolvePostTransaction(
      Hub hub, WorldView state, Transaction tx, boolean isSuccessful) {}
}
