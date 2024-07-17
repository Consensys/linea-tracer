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

import net.consensys.linea.zktracer.module.hub.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.DomSubStampsSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.MxpCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.StpCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.oob.OobCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.oob.opcodes.CreateOobCall;
import net.consensys.linea.zktracer.module.hub.fragment.scenario.CreateScenarioFragment;
import net.consensys.linea.zktracer.module.hub.fragment.scenario.ScenarioEnum;
import net.consensys.linea.zktracer.module.hub.section.TraceSection;
import net.consensys.linea.zktracer.module.hub.signals.AbortingConditions;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import net.consensys.linea.zktracer.module.hub.signals.FailureConditions;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.account.Account;
import org.hyperledger.besu.evm.frame.MessageFrame;

public class CreateSection {

  private int creatorContextId;
  private boolean emptyInitCode;

  // Just before create
  private AccountSnapshot oldCreatorSnapshot;
  private AccountSnapshot oldCreatedSnapshot;

  // Just after create but before entering child frame
  private AccountSnapshot midCreatorSnapshot;
  private AccountSnapshot midCreatedSnapshot;

  // After return from child-context
  private AccountSnapshot newCreatorSnapshot;
  private AccountSnapshot newCreatedSnapshot;

  private boolean createSuccessful;

  /* true if the putatively created account already has code **/

  private boolean targetHasCode() {
    return !oldCreatedSnapshot.code().isEmpty();
  }

  // row i+ 0
  private final CreateScenarioFragment scenarioFragment = new CreateScenarioFragment();
  // row i+2
  private final ImcFragment imcFragment;

  public CreateSection(Hub hub) {
    final short exception = hub.pch().exceptions();

    final ContextFragment commonContext = ContextFragment.readCurrentContextData(hub);
    imcFragment = ImcFragment.empty(hub);

    if (Exceptions.staticFault(exception)) {
      new ExceptionalCreate(hub, commonContext);
      return;
    }

    final MxpCall mxpCall = new MxpCall(hub);
    imcFragment.callMxp(mxpCall);

    if (Exceptions.memoryExpansionException(exception)) {
      new ExceptionalCreate(hub, commonContext);
      return;
    }

    final StpCall stpCall = new StpCall(hub, mxpCall.getGasMxp());
    stpCall.stpCallForCreates(hub);
    imcFragment.callStp(stpCall);

    if (Exceptions.outOfGasException(exception)) {
      new ExceptionalCreate(hub, commonContext);
      return;
    }

    final OobCall oobCall = new CreateOobCall();
    imcFragment.callOob(oobCall);

    // We are now with unexceptional create
    final AbortingConditions aborts = hub.pch().abortingConditions().snapshot();
    this.creatorContextId = hub.currentFrame().id();
    this.emptyInitCode = hub.transients().op().callDataSegment().isEmpty();

    final CallFrame callFrame = hub.currentFrame();
    final MessageFrame frame = callFrame.frame();

    final Address creatorAddress = callFrame.accountAddress();
    final Account creatorAccount = frame.getWorldUpdater().get(creatorAddress);
    oldCreatorSnapshot =
        AccountSnapshot.fromAccount(
            creatorAccount,
            frame.isAddressWarm(creatorAddress),
            hub.transients().conflation().deploymentInfo().number(creatorAddress),
            hub.transients().conflation().deploymentInfo().isDeploying(creatorAddress));

    if (aborts.any()) {
      new AbortCreate(hub, commonContext);
      return;
    }

    // We are now with unexceptional, non-aborting create
    final FailureConditions failures = hub.pch().failureConditions().snapshot();

    // final Address createdAddress = getCreateAddress(frame);
    // final Account createdAccount = frame.getWorldUpdater().get(createdAddress);
    // oldCreatedSnapshot =
    //    AccountSnapshot.fromAccount(
    //        createdAccount,
    //        frame.isAddressWarm(createdAddress),
    //        hub.transients().conflation().deploymentInfo().number(createdAddress),
    //        hub.transients().conflation().deploymentInfo().isDeploying(createdAddress));

  }

  private class ExceptionalCreate extends TraceSection {
    public ExceptionalCreate(final Hub hub, final ContextFragment commonContext) {
      super(hub, (short) 6);
      hub.addTraceSection(this);

      scenarioFragment.setScenario(ScenarioEnum.CREATE_EXCEPTION);

      this.addFragmentsAndStack(hub, scenarioFragment, commonContext, imcFragment);
    }
  }

  private class AbortCreate extends TraceSection {
    public AbortCreate(final Hub hub, final ContextFragment commonContext) {
      super(hub, (short) 7);
      hub.addTraceSection(this);

      scenarioFragment.setScenario(ScenarioEnum.CREATE_ABORT);

      final AccountFragment.AccountFragmentFactory accountFragmentFactory =
          hub.factories().accountFragment();
      final AccountFragment creatorAccountFragment =
          accountFragmentFactory.make(
              oldCreatorSnapshot,
              oldCreatorSnapshot,
              DomSubStampsSubFragment.standardDomSubStamps(hub, 0));

      this.addFragmentsAndStack(
          hub, scenarioFragment, commonContext, imcFragment, creatorAccountFragment);
    }
  }
}
