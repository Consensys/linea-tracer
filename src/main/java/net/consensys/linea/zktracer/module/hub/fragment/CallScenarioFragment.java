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

package net.consensys.linea.zktracer.module.hub.fragment;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.Trace;
import net.consensys.linea.zktracer.module.hub.defer.PostTransactionDefer;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.worldstate.WorldView;

public final class CallScenarioFragment extends ScenarioFragment implements PostTransactionDefer {
  private final boolean targetIsPrecompile;
  private final boolean targetHasCode;
  private final boolean abort;
  private final int callerId;
  private final int calleeId;
  private boolean callerReverts = false;
  private boolean calleeSelfReverts = false;

  public CallScenarioFragment(
      boolean targetIsPrecompile,
      boolean targetHasCode,
      boolean abort,
      int callerId,
      int calleeId) {
    this.targetIsPrecompile = targetIsPrecompile;
    this.targetHasCode = targetHasCode;
    this.abort = abort;
    this.callerId = callerId;
    this.calleeId = calleeId;
  }

  @Override
  protected boolean[] computeFlags() {
    return new boolean[] {
      targetIsPrecompile,
      targetHasCode,
      abort,
      !abort && targetHasCode && callerReverts && calleeSelfReverts,
      !abort && targetHasCode && callerReverts && !calleeSelfReverts,
      !abort && targetHasCode && !callerReverts && calleeSelfReverts,
      !abort && targetHasCode && !callerReverts && !calleeSelfReverts,
      !abort && !targetHasCode && callerReverts,
      !abort && !targetHasCode && !callerReverts,
      false,
      false,
      false,
    };
  }

  @Override
  public void runPostTx(Hub hub, WorldView state, Transaction tx) {
    this.callerReverts = hub.callStack().get(callerId).hasReverted();
    this.calleeSelfReverts = hub.callStack().get(calleeId).hasReverted();
  }

  @Override
  public Trace.TraceBuilder trace(Trace.TraceBuilder trace) {
    return super.trace(trace)
        .pScenarioCall(true)
        .pScenarioBlake2F(false)
        .pScenarioCodedeposit(false)
        .pScenarioCreate(false)
        .pScenarioEcadd(false)
        .pScenarioEcmul(false)
        .pScenarioEcpairing(false)
        .pScenarioEcrecover(false)
        .pScenarioIdentity(false)
        .pScenarioModexp(false)
        .pScenarioRipemd160(false)
        .pScenarioSha2256(false)
        .pScenarioSelfdestruct(false);
  }
}
