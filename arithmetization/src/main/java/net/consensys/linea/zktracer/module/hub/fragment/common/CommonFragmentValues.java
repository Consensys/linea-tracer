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

package net.consensys.linea.zktracer.module.hub.fragment.common;

import static net.consensys.linea.zktracer.module.hub.HubProcessingPhase.TX_EXEC;
import static net.consensys.linea.zktracer.module.hub.fragment.common.CommonFragment.computePcNew;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.HubProcessingPhase;
import net.consensys.linea.zktracer.module.hub.State;
import net.consensys.linea.zktracer.opcode.InstructionFamily;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import net.consensys.linea.zktracer.runtime.callstack.CallStack;
import net.consensys.linea.zktracer.types.TransactionProcessingMetadata;

@Accessors(fluent = true)
@RequiredArgsConstructor
public class CommonFragmentValues {
  public final TransactionProcessingMetadata txMetadata;
  public final HubProcessingPhase hubProcessingPhase;
  public final int hubStamp;
  public final CallStack callStack;
  public final State.TxState.Stamps stamps; // for MMU and MXP stamps
  @Setter public int logStamp; // TODO
  @Getter final CallFrame callFrame;
  public final boolean exceptionAhoy;
  public final int contextNumberNew;
  public final int cnRevertStamp;
  public final int pc;
  public final int pcNew;
  final short height;
  final short heightNew;
  public final long gasExpected;
  public final long gasActual;
  public final boolean contextMayChange;
  @Setter long gasCost; // Set at Post Execution
  @Setter long gasNext; // Set at Post Execution
  @Getter public final long refundDelta;
  @Setter public long gasRefund; // TODO
  @Setter public long gasRefundNew; /* TODO gasRefund + (willRevert ? 0 : refundDelta) */
  @Setter public int numberOfNonStackRows;
  @Setter public boolean TLI;
  @Setter public int codeFragmentIndex = -1;

  public CommonFragmentValues(Hub hub) {
    final boolean noStackException = hub.pch().exceptions().noStackException();
    final InstructionFamily instructionFamily = hub.opCode().getData().instructionFamily();

    this.txMetadata = hub.txStack().current();
    this.hubProcessingPhase = hub.state().getProcessingPhase();
    this.hubStamp = hub.state().stamps().hub();
    this.callStack = hub.callStack();
    this.stamps = hub.state().stamps();
    this.callFrame = hub.currentFrame();
    this.exceptionAhoy = hub.pch().exceptions().any();
    this.contextNumberNew =
        hub.contextNumberNew(
            callFrame); // TODO this should be in seal method, looking at CN of following section
    this.cnRevertStamp = 0; // TODO
    this.pc = hubProcessingPhase == TX_EXEC ? hub.currentFrame().pc() : 0;
    this.pcNew =
        computePcNew(
            hub,
            pc,
            noStackException,
            hub.state.getProcessingPhase()
                == TX_EXEC); // TODO this should be in seal method, looking at PC of following
    this.height = (short) callFrame.stack().getHeight();
    this.heightNew =
        (short)
            callFrame
                .stack()
                .getHeightNew(); // TODO this should be in seal method, looking at height of
    // following
    this.refundDelta =
        noStackException ? Hub.GAS_PROJECTOR.of(callFrame.frame(), hub.opCode()).refund() : 0;

    // TODO: partial solution, will not work in general
    this.gasExpected = hub.expectedGas();
    this.gasActual = hub.remainingGas();

    this.contextMayChange =
        hubProcessingPhase == HubProcessingPhase.TX_EXEC
            && ((instructionFamily == InstructionFamily.CALL
                    || instructionFamily == InstructionFamily.CREATE
                    || instructionFamily == InstructionFamily.HALT
                    || instructionFamily == InstructionFamily.INVALID)
                || exceptionAhoy);
  }
}
