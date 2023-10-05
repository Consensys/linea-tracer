/*
 * Copyright ConsenSys AG.
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
import net.consensys.linea.zktracer.module.hub.defer.NextContextDefer;
import net.consensys.linea.zktracer.module.hub.defer.PostExecDefer;
import net.consensys.linea.zktracer.module.hub.defer.PostTransactionDefer;
import net.consensys.linea.zktracer.module.hub.fragment.AccountSnapshot;
import net.consensys.linea.zktracer.module.runtime.callstack.CallFrame;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.account.Account;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.operation.Operation;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class WithCodeCallSection extends TraceSection
    implements PostTransactionDefer, PostExecDefer, NextContextDefer {
  private final CallFrame callerCallFrame;
  private final AccountSnapshot preCallCallerAccountSnapshot;
  private final AccountSnapshot preCallCalledAccountSnapshot;

  private AccountSnapshot postCallCallerAccountSnapshot;
  private AccountSnapshot postCallCalledAccountSnapshot;
  private boolean successBit;

  public WithCodeCallSection(
      Hub hub,
      AccountSnapshot preCallCallerAccountSnapshot,
      AccountSnapshot preCallCalledAccountSnapshot) {
    this.callerCallFrame = hub.currentFrame();
    this.preCallCallerAccountSnapshot = preCallCallerAccountSnapshot;
    this.preCallCalledAccountSnapshot = preCallCalledAccountSnapshot;
    for (var stackChunk : hub.makeStackChunks(hub.currentFrame())) {
      this.addChunk(hub, hub.currentFrame(), stackChunk);
    }
  }

  @Override
  public void runPostExec(Hub hub, MessageFrame frame, Operation.OperationResult operationResult) {
    final Address callerAddress = preCallCallerAccountSnapshot.address();
    final Account callerAccount = frame.getWorldUpdater().getAccount(callerAddress);
    final Address calledAddress = preCallCalledAccountSnapshot.address();
    final Account calledAccount = frame.getWorldUpdater().getAccount(calledAddress);

    this.postCallCallerAccountSnapshot =
      AccountSnapshot.fromAccount(
        callerAccount,
        frame.isAddressWarm(callerAddress),
        hub.conflation().deploymentInfo().number(callerAddress),
        hub.conflation().deploymentInfo().isDeploying(callerAddress));
    this.postCallCalledAccountSnapshot =
      AccountSnapshot.fromAccount(
        calledAccount,
        frame.isAddressWarm(calledAddress),
        hub.conflation().deploymentInfo().number(calledAddress),
        hub.conflation().deploymentInfo().isDeploying(calledAddress));

    this.successBit = !frame.getStackItem(0).isZero();
  }

  @Override
  public void runNextContext(Hub hub, MessageFrame frame) {

  }

  @Override
  public void runPostTx(Hub hub, WorldView state, Transaction tx) {}
}
