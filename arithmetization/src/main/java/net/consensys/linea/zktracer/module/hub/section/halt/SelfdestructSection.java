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

import com.google.common.base.Preconditions;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.defer.PostRollbackDefer;
import net.consensys.linea.zktracer.module.hub.defer.PostTransactionDefer;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import net.consensys.linea.zktracer.module.hub.fragment.scenario.SelfdestructScenarioFragment;
import net.consensys.linea.zktracer.module.hub.section.TraceSection;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class SelfdestructSection extends TraceSection
    implements PostRollbackDefer, PostTransactionDefer {

  AccountFragment selfDestroyerFirstAccountFragment;
  AccountFragment recipientFirstAccountFragment;

  public SelfdestructSection(Hub hub) {
    // up to 8 = 1 + 7 rows
    super(hub, (short) 8);
    hub.addTraceSection(this);

    short exceptions = hub.pch().exceptions();

    SelfdestructScenarioFragment selfdestructScenarioFragment = new SelfdestructScenarioFragment();
    // SCN fragment
    this.addFragment(selfdestructScenarioFragment);
    if (Exceptions.any(exceptions)) {
      selfdestructScenarioFragment.setScenario(
          SelfdestructScenarioFragment.SelfdestructScenario.SELFDESTRUCT_EXCEPTION);
    }

    // CON fragment (1)
    ContextFragment contextFragment = ContextFragment.readCurrentContextData(hub);
    this.addFragment(contextFragment);

    // STATICX case
    if (Exceptions.staticFault(exceptions)) {
      return;
    }

    // OOGX case
    if (Exceptions.any(exceptions)) {
      Preconditions.checkArgument(exceptions == Exceptions.OUT_OF_GAS_EXCEPTION);

      // ACC fragment (1)
      selfDestroyerFirstAccountFragment = null;
      /*
      Use current account and see if it has balance or not:
      hub.factories()
                .accountFragment()
                .makeWithTrm(
                    this.accountBefore, this.accountBefore, this.rawAddress, TODOdoingDomSubStamps);
       */
      this.addFragment(selfDestroyerFirstAccountFragment); // i+2

      // ACC fragment (2)
      recipientFirstAccountFragment = null;
      /*
      Account recipient of the SELFDESTRUCT
      See EXTCODECOPY for an example of how to create an AccountFragment:
       hub.factories()
                .accountFragment()
                .makeWithTrm(
                    this.accountBefore, this.accountBefore, this.rawAddress, TODOdoingDomSubStamps);
      We do not modify accounts in exception cases, otherwise we do
       */
      this.addFragment(recipientFirstAccountFragment); // i+3

      return;
    }

    // Unexceptional case (add CON row in the end manually in any case)
    hub.defers().scheduleForPostRollback(this, hub.currentFrame());
    hub.defers().schedulePostTransaction(this);

    // Modify the current account and the recipient account
    // - The current account has its balance reduced to 0 (i+2)
    //   * selfDestroyerFirstAccountFragment
    // - The recipient account, if it is not the current account, receive that balance (+= balance),
    // otherwise remains 0 (i+3)
    // - The recipient address will become warm (i+3)
    //   * recipientFirstAccountFragment

    // TODO: look at EXTCODECOPY, RETURN, ACCOUNT
  }

  @Override
  public void resolvePostRollback(Hub hub, MessageFrame messageFrame, CallFrame callFrame) {
    /* willRevert case
    TODO: undo the modifications we applied to selfDestroyerFirstAccountFragment and recipientFirstAccountFragment
    this will add account rows
     */
  }

  @Override
  public void resolvePostTransaction(
      Hub hub, WorldView state, Transaction tx, boolean isSuccessful) {
    // will not revert (sub cases: already marked, not yet marked)
    // not yet marked corresponds to when SELFDESTRUCT produces no exceptions, will not be reverted
    // and it
    // is the first time this account will be succeseful in self destructing
    // already marked corresponds to when SELFDESTRUCT produces no exceptions, will not be reverted
    // and it
    // is not the first time this account will be successful in self destructing
    // mark for self destruct is associated to an address and a deployment number
    // use a maps that keeps track the (hub stamp, call frame) of all the unexceptional
    // SELFDESTRUCT for a given (address, deployment number)
    // at the end of the transaction we have that map
    // we analyse that map:
    // for every (address, deployment number) we walk through the list of [(hub stamp, call frame),
    // ...]
    // for every call frame we know if it was reverted or not
    // the first time (selfDestructTime) we find a call frame that has not been reverted, we
    // remember the hub stamp
    // this produces a new map (address, deployment number) -> selfDestructTime (of the first
    // succeseful and unreverted
    // SELFDESTRUCT)
    // we know have a map of all (addresse, deployment number) that have been succeseful in self
    // destructing
    // and the hub stamp in which it happened
    // for every account row in the entire trace, we can now decide what to write in the
    // MARKED_FOR_SELFDESTRUCT and MARKED_FOR_SELFDESTRUCT_NEW columns
    // MARKED_FOR_SELFDESTRUCT = [hub stamp < selfDestructTime]
    // MARKED_FOR_SELFDESTRUCT_NEW = [hub stamp >= selfDestructTime]

    // This will only be triggered at the hub stamp = selfDestructTime
    // i+4 for not yet marked case
    // we wipe the entire account
    // in the already marked case we know that this action has already been scheduled for the future

    // every transaction should start with an empty map
  }
}
