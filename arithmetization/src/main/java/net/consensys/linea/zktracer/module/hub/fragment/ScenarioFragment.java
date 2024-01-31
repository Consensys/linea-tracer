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

package net.consensys.linea.zktracer.module.hub.fragment;

import java.util.Optional;

import net.consensys.linea.zktracer.module.hub.FailureConditions;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.Precompile;
import net.consensys.linea.zktracer.module.hub.Trace;
import net.consensys.linea.zktracer.module.hub.defer.PostTransactionDefer;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.worldstate.WorldView;

/** This machine generates lines */
public class ScenarioFragment implements TraceFragment, PostTransactionDefer {
  private final Optional<Precompile> targetPrecompile;
  private final boolean targetHasCode;
  private final boolean abort;
  private final int callerId;
  private final int calleeId;
  private final FailureConditions failures;
  private 
  private boolean callerReverts = false;
  private boolean calleeSelfReverts = false;

  public static ScenarioFragment forCreate(boolean targetHasCode,
                                           boolean abort,) {
        return new ScenarioFragment(
          Optional.empty(),

        );
  }

  private boolean targetIsPrecompile() {
    return this.targetPrecompile.isPresent();
  }

  @Override
  public void runPostTx(Hub hub, WorldView state, Transaction tx) {
    this.callerReverts = hub.callStack().get(callerId).hasReverted();
    this.calleeSelfReverts = hub.callStack().get(calleeId).hasReverted();
  }

  @Override
  public Trace trace(Trace trace) {
    return trace
        .peekAtScenario(true)
        .pScenarioCallException(false)
        .pScenarioCallAbort(abort)
        .pScenarioCallPrcFailure(
            !abort && targetIsPrecompile() && !callerReverts && calleeSelfReverts)
        .pScenarioCallPrcSuccessCallerWillRevert(
            !abort && targetIsPrecompile() && callerReverts && !calleeSelfReverts)
        .pScenarioCallPrcSuccessCallerWontRevert(
            !abort && targetIsPrecompile() && !callerReverts && !calleeSelfReverts)
        .pScenarioCallSmcFailureCallerWillRevert(
            !abort && targetHasCode && callerReverts && calleeSelfReverts)
        .pScenarioCallSmcFailureCallerWontRevert(
            !abort && targetHasCode && !callerReverts && calleeSelfReverts)
        .pScenarioCallSmcSuccessCallerWontRevert(
            !abort && targetHasCode && !callerReverts && !calleeSelfReverts)
        .pScenarioCallSmcSuccessCallerWillRevert(
            !abort && targetHasCode && callerReverts && !calleeSelfReverts)
        .pScenarioCallEoaSuccessCallerWontRevert(
            !abort && !targetIsPrecompile() && !targetHasCode && !callerReverts)
        .pScenarioCallEoaSuccessCallerWillRevert(
            !abort && !targetIsPrecompile() && !targetHasCode && callerReverts)
        .pScenarioCallEoaSuccessCallerWontRevert(false)
        .pScenarioCreateException(false)
        .pScenarioCreateAbort(false)
        .pScenarioCreateFailureConditionWillRevert(false)
        .pScenarioCreateFailureConditionWontRevert(false)
        .pScenarioCreateEmptyInitCodeWillRevert(false)
        .pScenarioCreateEmptyInitCodeWontRevert(false)
        .pScenarioCreateNonemptyInitCodeFailureWillRevert(false)
        .pScenarioCreateNonemptyInitCodeFailureWontRevert(false)
        .pScenarioCreateNonemptyInitCodeSuccessWillRevert(false)
        .pScenarioCreateNonemptyInitCodeSuccessWontRevert(false)
        .pScenarioEcrecover(false)
        .pScenarioSha2256(false)
        .pScenarioRipemd160(false)
        .pScenarioIdentity(false)
        .pScenarioModexp(false)
        .pScenarioEcadd(false)
        .pScenarioEcmul(false)
        .pScenarioEcpairing(false)
        .pScenarioBlake2F(false)
        .pScenarioPrcSuccessWillRevert(false)
        .pScenarioPrcSuccessWontRevert(false)
        .pScenarioPrcFailureKnownToHub(false)
        .pScenarioPrcFailureKnownToRam(false)
        .pScenarioPrcCallerGas(false)
        .pScenarioPrcCalleeGas(false)
        .pScenarioPrcReturnGas(false)
        .pScenarioPrcCdo(false)
        .pScenarioPrcCds(false)
        .pScenarioPrcRao(false)
        .pScenarioPrcRac(false)
        .pScenarioCodedeposit(false)
        .pScenarioCodedepositInvalidCodePrefix(false)
        .pScenarioCodedepositValidCodePrefix(false)
        .pScenarioSelfdestruct(false);
  }
}
