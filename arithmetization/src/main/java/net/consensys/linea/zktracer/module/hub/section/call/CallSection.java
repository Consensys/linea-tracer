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

package net.consensys.linea.zktracer.module.hub.section.call;

import com.google.common.base.Preconditions;
import net.consensys.linea.zktracer.module.hub.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.defer.PostRollbackDefer;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.MxpCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.oob.opcodes.CallOobCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.oob.opcodes.XCallOobCall;
import net.consensys.linea.zktracer.module.hub.section.TraceSection;
import net.consensys.linea.zktracer.module.hub.signals.AbortingConditions;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import net.consensys.linea.zktracer.types.EWord;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.frame.MessageFrame;

public class CallSection implements PostRollbackDefer {
  private TraceSection callSection;

  // row i+2
  private final ImcFragment firstImcFragment;

  // Just before call
  private AccountSnapshot preOpcodeCallerSnapshot;
  private AccountSnapshot preOpcodeCalleeSnapshot;

  public CallSection(Hub hub) {
    final short exceptions = hub.pch().exceptions();

    // row i+1
    final ContextFragment currentContextFragment = ContextFragment.readCurrentContextData(hub);

    // row i+2
    this.firstImcFragment = ImcFragment.empty(hub);

    if (Exceptions.any(exceptions)) {
      final XCallOobCall oobCall = new XCallOobCall();
      firstImcFragment.callOob(oobCall);
    }

    // STATICX cases
    if (Exceptions.staticFault(exceptions)) {
      new StaticMxpXCall(hub, currentContextFragment, firstImcFragment);
      return;
    }

    final MxpCall mxpCall = new MxpCall(hub);
    firstImcFragment.callMxp(mxpCall);

    Preconditions.checkArgument(mxpCall.mxpx == Exceptions.memoryExpansionException(exceptions));

    // MXPX case
    if (Exceptions.memoryExpansionException(exceptions)) {
      new StaticMxpXCall(hub, currentContextFragment, firstImcFragment);
      return;
    }

    final Address callerAddress = hub.currentFrame().callerAddress();
    this.preOpcodeCallerSnapshot = AccountSnapshot.canonical(hub, callerAddress);

    final Bytes rawCalleeAddress = hub.currentFrame().frame().getStackItem(1);
    final Address calleeAddress = Address.extract(EWord.of(rawCalleeAddress)); // TODO check this
    this.preOpcodeCalleeSnapshot = AccountSnapshot.canonical(hub, calleeAddress);

    // OOGX case
    if (Exceptions.outOfGasException(exceptions)) {
      new OogXCall(
          hub,
          currentContextFragment,
          firstImcFragment,
          preOpcodeCallerSnapshot,
          preOpcodeCalleeSnapshot,
          rawCalleeAddress);
      return;
    }

    // The CALL is now unexceptional
    Preconditions.checkArgument(Exceptions.none(exceptions));

    final CallOobCall oobCall = new CallOobCall();
    firstImcFragment.callOob(oobCall);

    final AbortingConditions aborts = hub.pch().abortingConditions().snapshot();
    Preconditions.checkArgument(oobCall.isAbortingCondition() == aborts.any());

    hub.defers().scheduleForPostRollback(this, hub.currentFrame());

    if (aborts.any()) {
      new AbortCall();
      return;
    }

    // The CALL is now unexceptional and unaborted
  }

  @Override
  public void resolvePostRollback(Hub hub, MessageFrame messageFrame, CallFrame callFrame) {}
}
