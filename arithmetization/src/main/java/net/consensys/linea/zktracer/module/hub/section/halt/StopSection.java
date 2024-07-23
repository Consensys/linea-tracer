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

import static net.consensys.linea.zktracer.module.hub.fragment.ContextFragment.executionProvidesEmptyReturnData;
import static net.consensys.linea.zktracer.module.hub.fragment.ContextFragment.readCurrentContextData;

import com.google.common.base.Preconditions;
import net.consensys.linea.zktracer.module.hub.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.defer.PostRollbackDefer;
import net.consensys.linea.zktracer.module.hub.defer.PostTransactionDefer;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.DomSubStampsSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import net.consensys.linea.zktracer.module.hub.section.TraceSection;
import net.consensys.linea.zktracer.module.hub.transients.DeploymentInfo;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import net.consensys.linea.zktracer.types.Bytecode;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class StopSection extends TraceSection implements PostRollbackDefer, PostTransactionDefer {

  final int hubStamp;
  final Address address;
  final int deploymentNumber;
  final boolean deploymentStatus;
  final int contextNumber;
  final ContextFragment parentContextReturnDataReset;

  public StopSection(Hub hub) {
    // 3 = 1 + max_NON_STACK_ROWS in message call case
    // 5 = 1 + max_NON_STACK_ROWS in deployment case
    super(hub, hub.callStack().current().isMessageCall() ? (short) 3 : (short) 5);
    hub.addTraceSection(this);
    hub.defers().schedulePostTransaction(this); // always

    hubStamp = hub.stamp();
    address = hub.currentFrame().accountAddress();
    contextNumber = hub.currentFrame().contextNumber();
    {
      DeploymentInfo deploymentInfo = hub.transients().conflation().deploymentInfo();
      deploymentNumber = deploymentInfo.number(address);
      deploymentStatus = deploymentInfo.isDeploying(address);
    }
    parentContextReturnDataReset = executionProvidesEmptyReturnData(hub);

    Preconditions.checkArgument(
        hub.currentFrame().isDeployment() == deploymentStatus); // sanity check

    // Message call case
    ////////////////////
    if (!deploymentStatus) {
      this.addFragmentsAndStack(hub, readCurrentContextData(hub));
      return;
    }

    // Deployment case
    //////////////////
    this.deploymentStopSection(hub);
    hub.defers().scheduleForPostRollback(this, hub.currentFrame()); // for deployments only
  }

  public void deploymentStopSection(Hub hub) {

    AccountSnapshot priorEmptyDeployment = AccountSnapshot.canonical(hub, address);
    AccountSnapshot afterEmptyDeployment = priorEmptyDeployment.deployByteCode(Bytecode.EMPTY);
    AccountFragment doingAccountFragment =
        hub.factories()
            .accountFragment()
            .make(
                priorEmptyDeployment,
                afterEmptyDeployment,
                DomSubStampsSubFragment.standardDomSubStamps(hub, 0));

    this.addFragmentsAndStack(hub, readCurrentContextData(hub), doingAccountFragment);
  }

  /**
   * Adds the missing account "undoing operation" to the StopSection provided the relevant context
   * reverted and the STOP instruction happened in a deployment context.
   *
   * @param hub
   * @param messageFrame access point to world state & accrued state
   * @param callFrame reference to call frame whose actions are to be undone
   */
  @Override
  public void resolvePostRollback(Hub hub, MessageFrame messageFrame, CallFrame callFrame) {

    if (!this.deploymentStatus) {
      return;
    }

    Preconditions.checkArgument(this.fragments().getLast() instanceof AccountFragment);

    AccountFragment lastAccountFragment = (AccountFragment) this.fragments().getLast();
    DomSubStampsSubFragment undoingDomSubStamps = DomSubStampsSubFragment.revertWithCurrentDomSubStamps(hubStamp, hub.callStack().current().revertStamp(), 1);

    this.addFragmentsWithoutStack(
        hub.factories()
            .accountFragment()
            .make(
                lastAccountFragment.newState(),
                lastAccountFragment.oldState(),
                    undoingDomSubStamps
                ));
  }

  /**
   * Adds the missing context fragment. This context fragment squashes the caller (parent) context
   * return data. Applies in all cases.
   *
   * @param hub the {@link Hub} in which the {@link Transaction} took place
   * @param state a view onto the current blockchain state
   * @param tx the {@link Transaction} that just executed
   * @param isSuccessful
   */
  @Override
  public void resolvePostTransaction(
      Hub hub, WorldView state, Transaction tx, boolean isSuccessful) {
    this.addFragmentsWithoutStack(this.parentContextReturnDataReset);
  }
}
