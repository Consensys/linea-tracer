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

package net.consensys.linea.zktracer.module.hub.section.copy;

import com.google.common.base.Preconditions;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.defer.PostTransactionDefer;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.MxpCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.mmu.MmuCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.oob.opcodes.ReturnDataCopyOobCall;
import net.consensys.linea.zktracer.module.hub.section.TraceSection;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class ReturnDataCopySection extends TraceSection implements PostTransactionDefer {

  final short exceptions;

  final ImcFragment miscFragment;
  boolean triggerMmu = false;
  MmuCall mmuCall;

  public ReturnDataCopySection(Hub hub) {
    // 4 = 1 + 3
    super(hub, (short) 4);
    this.exceptions = hub.pch().exceptions();

    hub.addTraceSection(this);

    final ContextFragment currentContext = ContextFragment.readCurrentContextData(hub);
    this.addFragmentsAndStack(hub, currentContext);

    miscFragment = ImcFragment.empty(hub);
    this.addFragment(miscFragment);

    final ReturnDataCopyOobCall oobCall = new ReturnDataCopyOobCall();
    miscFragment.callOob(oobCall);

    final boolean returnDataCopyException = oobCall.isRdcx();
    Preconditions.checkArgument(
        returnDataCopyException == Exceptions.returnDataCopyFault(this.exceptions));

    if (returnDataCopyException) {
      return;
    }

    final MxpCall mxpCall = new MxpCall(hub);
    miscFragment.callMxp(mxpCall);

    final boolean memoryExpansionException = mxpCall.mxpx;
    Preconditions.checkArgument(
        memoryExpansionException == Exceptions.memoryExpansionException(this.exceptions));

    // if MXPX ∨ OOGX
    if (Exceptions.any(this.exceptions)) {
      return;
    }

    // beyond this point unexceptional
    triggerMmu = mxpCall.mayTriggerNonTrivialMmuOperation;
    if (triggerMmu) {
      mmuCall = MmuCall.returnDataCopy(hub);
      hub.defers().scheduleForPostTransaction(this);
    }
  }

  @Override
  public void resolvePostTransaction(
      Hub hub, WorldView state, Transaction tx, boolean isSuccessful) {
    if (triggerMmu) {
      miscFragment.callMmu(mmuCall);
    }
  }
}
