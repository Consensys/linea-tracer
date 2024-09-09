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

import static com.google.common.base.Preconditions.*;
import static net.consensys.linea.zktracer.module.hub.signals.Exceptions.OUT_OF_GAS_EXCEPTION;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.MxpCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.mmu.MmuCall;
import net.consensys.linea.zktracer.module.hub.section.TraceSection;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import net.consensys.linea.zktracer.module.hub.signals.PureException;

public class CallDataCopySection extends TraceSection {

  public CallDataCopySection(Hub hub) {
    // 3 = 1 + 2
    super(hub, maxNumberOfRows(hub));

    final ImcFragment imcFragment = ImcFragment.empty(hub);
    this.addStack(hub);
    this.addFragment(imcFragment);

    // triggerOob = false
    // triggerMxp = true
    final MxpCall mxpCall = new MxpCall(hub);
    imcFragment.callMxp(mxpCall);

    // the only allowable exceptions are
    // - memoryExpansionException (MXPX)
    // - outOfGasException OOGX
    ////////////////////////////////////

    final short exceptions = hub.pch().exceptions();
    checkArgument(mxpCall.mxpx == Exceptions.memoryExpansionException(exceptions));

    // The MXPX case
    if (mxpCall.mxpx) {
      commonValues.setTracedException(PureException.MEMORY_EXPANSION_EXCEPTION);
      return;
    }

    // The OOGX case
    if (Exceptions.any(exceptions)) {
      checkArgument(exceptions == OUT_OF_GAS_EXCEPTION);
      commonValues.setTracedException(PureException.OUT_OF_GAS_EXCEPTION);
      return;
    }

    // The unexceptional case
    final ContextFragment currentContext = ContextFragment.readCurrentContextData(hub);
    this.addFragment(currentContext);

    final boolean triggerMmu = mxpCall.mayTriggerNontrivialMmuOperation;

    if (triggerMmu) {
      final MmuCall mmuCall = MmuCall.callDataCopy(hub);
      imcFragment.callMmu(mmuCall);
    }
  }

  private static short maxNumberOfRows(Hub hub) {
    return (short) (hub.opCode().numberOfStackRows() + 2);
  }
}
