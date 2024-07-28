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

package net.consensys.linea.zktracer.module.hub.section;

import com.google.common.base.Preconditions;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.defer.PostRollbackDefer;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.MxpCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.mmu.MmuCall;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import net.consensys.linea.zktracer.runtime.LogData;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import org.hyperledger.besu.evm.frame.MessageFrame;

public class LogSection extends TraceSection implements PostRollbackDefer {

  MmuCall mmuCall;

  public LogSection(Hub hub) {
    super(hub, maxNumberOfRows(hub));

    final short exceptions = hub.pch().exceptions();

    final ContextFragment currentContextFragment = ContextFragment.readCurrentContextData(hub);

    this.addStack(hub);
    this.addFragment(currentContextFragment);

    // Static Case
    if (hub.currentFrame().frame().isStatic()) {
      Preconditions.checkArgument(Exceptions.staticFault(exceptions));
      return;
    }

    final ImcFragment imcFragment = ImcFragment.empty(hub);
    this.addFragment(imcFragment);

    final MxpCall mxpCall = new MxpCall(hub);
    imcFragment.callMxp(mxpCall);

    Preconditions.checkArgument(
        mxpCall.isMxpx() == Exceptions.memoryExpansionException(exceptions));

    // MXPX case
    if (mxpCall.isMxpx()) {
      return;
    }

    // OOGX case
    if (Exceptions.outOfGasException(exceptions)) {
      return;
    }

    // the unexceptional case
    Preconditions.checkArgument(Exceptions.none(exceptions));

    final LogData logData = new LogData(hub);
    Preconditions.checkArgument(
        logData.nontrivialLog() == mxpCall.mayTriggerNontrivialMmuOperation);
    mmuCall = (logData.nontrivialLog()) ? MmuCall.LogX(hub, logData) : null;

    if (mmuCall != null) {
      imcFragment.callMmu(mmuCall);
      hub.defers().scheduleForPostRollback(this, hub.currentFrame());
    }
  }

  private static short maxNumberOfRows(Hub hub) {
    return (short)
        (hub.opCode().numberOfStackRows()
            + (Exceptions.staticFault(hub.pch().exceptions()) ? 2 : 3));
  }

  @Override
  public void resolvePostRollback(Hub hub, MessageFrame messageFrame, CallFrame callFrame) {
    if (mmuCall != null) {
      mmuCall.dontTraceMe();
    }
  }
}
