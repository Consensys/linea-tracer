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

import static net.consensys.linea.zktracer.module.hub.fragment.scenario.ReturnScenarioFragment.ReturnScenario.RETURN_EXCEPTION;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.defer.PostRollbackDefer;
import net.consensys.linea.zktracer.module.hub.defer.PostTransactionDefer;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.MxpCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.mmu.MmuCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.oob.OobCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.oob.opcodes.XCallOobCall;
import net.consensys.linea.zktracer.module.hub.fragment.scenario.ReturnScenarioFragment;
import net.consensys.linea.zktracer.module.hub.section.TraceSection;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class ReturnSection
    extends TraceSection implements PostTransactionDefer, PostRollbackDefer {

  final short exceptions;
  final ImcFragment imcFragment;
  MmuCall mmuCall;
  boolean triggerMmu = false;
  boolean triggerOob = false;
  boolean triggerHashInfo = false;
  boolean messageCallTouchesRam = false;
  boolean returnFromDeployment;

  public ReturnSection(Hub hub) {
    // 5 = 1 + 4
    // 8 = 1 + 7
    super(hub, Exceptions.any(hub.pch().exceptions()) ? (short) 5 : (short) 8);
    hub.addTraceSection(this);

    exceptions = hub.pch().exceptions();

    final ReturnScenarioFragment returnScenarioFragment = new ReturnScenarioFragment();
    final ContextFragment currentContextFragment = ContextFragment.readCurrentContextData(hub);
    imcFragment = ImcFragment.empty(hub);
    final MxpCall mxpCall = new MxpCall(hub);
    imcFragment.callMxp(mxpCall);

    this.addStack(hub);
    this.addFragment(returnScenarioFragment);
    this.addFragment(currentContextFragment);
    this.addFragment(imcFragment);

    // Exceptional RETURN's
    ///////////////////////

    if (Exceptions.any(exceptions)) {
      returnScenarioFragment.setScenario(RETURN_EXCEPTION);
    }

    if (Exceptions.memoryExpansionException(exceptions)
        || Exceptions.outOfGasException(exceptions)) {
      return;
    }

    boolean triggerOobForMaxCodeSizeException = Exceptions.codeSizeOverflow(exceptions);
    if (triggerOobForMaxCodeSizeException) {
      OobCall oobCall = new XCallOobCall();
      imcFragment.callOob(oobCall);
      return;
    }

    final boolean triggerMmuForInvalidCodePrefix = Exceptions.invalidCodePrefix(exceptions);
    if (triggerMmuForInvalidCodePrefix) {
      // TODO: @françois: invalidCodePrefix currently unavailable
      // mmuCall = MmuCall.invalidCodePrefix();
    }

    // Unexceptional RETURN's
    /////////////////////////

    final boolean triggerOobForNonemptyDeployments =
        Exceptions.none(exceptions)
            && hub.currentFrame().isDeployment()
            && mxpCall.isMayTriggerNonTrivialMmuOperation();

    // returnFromDeployment =
  }

  @Override
  public void resolvePostRollback(Hub hub, MessageFrame messageFrame, CallFrame callFrame) {

  }

  @Override
  public void resolvePostTransaction(Hub hub, WorldView state, Transaction tx, boolean isSuccessful) {

  }
}
