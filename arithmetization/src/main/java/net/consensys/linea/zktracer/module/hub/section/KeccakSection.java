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

package net.consensys.linea.zktracer.module.hub.section;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.defer.PostExecDefer;
import net.consensys.linea.zktracer.module.hub.defer.PostTransactionDefer;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.MxpCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.mmu.MmuCall;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.operation.Operation;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class KeccakSection extends TraceSection implements PostExecDefer, PostTransactionDefer {

  final ImcFragment miscFragment;
  final boolean triggerMmu;
  MmuCall mmuCall;

  public KeccakSection(Hub hub) {
    super(hub, (short) 3);
    hub.addTraceSection(this);

    hub.addTraceSection(this);

    miscFragment = ImcFragment.empty(hub);
    this.addFragmentsAndStack(hub, miscFragment);

    final MxpCall mxpCall = new MxpCall(hub);
    miscFragment.callMxp(mxpCall);

    final boolean mayTriggerNonTrivialOperation = mxpCall.isMayTriggerNonTrivialMmuOperation();
    triggerMmu = mayTriggerNonTrivialOperation & Exceptions.none(hub.pch().exceptions());

    if (triggerMmu) {
      hub.defers().schedulePostExecution(this);
      hub.defers().schedulePostTransaction(this);
      mmuCall = MmuCall.sha3(hub);
    }
  }

  @Override
  public void resolvePostTransaction(
      Hub hub, WorldView state, Transaction tx, boolean isSuccessful) {
    miscFragment.callMmu(mmuCall);
  }

  @Override
  public void resolvePostExecution(
      Hub hub, MessageFrame frame, Operation.OperationResult operationResult) {
    // retroactively set HASH_INFO_FLAG and HASH_INFO_KECCAK_HI, HASH_INFO_KECCAK_LO
    this.triggerHashInfo(frame.getStackItem(0));
  }
}
