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
package net.consensys.linea.zktracer.module.hub.fragment.scenario;

import static net.consensys.linea.zktracer.module.hub.fragment.scenario.ReturnScenarioFragment.ReturnScenario.*;

import com.google.common.base.Preconditions;
import lombok.Setter;
import net.consensys.linea.zktracer.module.hub.Trace;
import net.consensys.linea.zktracer.module.hub.fragment.TraceFragment;

@Setter
public class ReturnScenarioFragment implements TraceFragment {

  ReturnScenario scenario;

  public ReturnScenarioFragment() {
    this.scenario = UNDEFINED;
  }

  public enum ReturnScenario {
    UNDEFINED,
    RETURN_EXCEPTION,
    RETURN_FROM_MESSAGE_CALL_WILL_TOUCH_RAM,
    RETURN_FROM_MESSAGE_CALL_WONT_TOUCH_RAM,
    RETURN_FROM_DEPLOYMENT_EMPTY_CODE_WILL_REVERT,
    RETURN_FROM_DEPLOYMENT_EMPTY_CODE_WONT_REVERT,
    RETURN_FROM_DEPLOYMENT_NONEMPTY_CODE_WILL_REVERT,
    RETURN_FROM_DEPLOYMENT_NONEMPTY_CODE_WONT_REVERT
  }

  @Override
  public Trace trace(Trace trace) {
    Preconditions.checkArgument(!this.scenario.equals(UNDEFINED));
    return trace
        .peekAtScenario(true)
        // // CALL scenarios
        ////////////////////
        // .pScenarioCallException(false)
        // .pScenarioCallAbort(false)
        // .pScenarioCallPrcFailure(false)
        // .pScenarioCallPrcSuccessCallerWillRevert(false)
        // .pScenarioCallPrcSuccessCallerWontRevert(false)
        // .pScenarioCallSmcFailureCallerWillRevert(false)
        // .pScenarioCallSmcFailureCallerWontRevert(false)
        // .pScenarioCallSmcSuccessCallerWontRevert(false)
        // .pScenarioCallSmcSuccessCallerWillRevert(false)
        // .pScenarioCallEoaSuccessCallerWontRevert(false)
        // .pScenarioCallEoaSuccessCallerWillRevert(false)
        // // CREATE scenarios
        //////////////////////
        // .pScenarioCreateException(false) .pScenarioCreateAbort(false)
        // .pScenarioCreateFailureConditionWillRevert(false)
        // .pScenarioCreateFailureConditionWontRevert(false)
        // .pScenarioCreateEmptyInitCodeWillRevert(false)
        // .pScenarioCreateEmptyInitCodeWontRevert(false)
        // .pScenarioCreateNonemptyInitCodeFailureWillRevert(false)
        // .pScenarioCreateNonemptyInitCodeFailureWontRevert(false)
        // .pScenarioCreateNonemptyInitCodeSuccessWillRevert(false)
        // .pScenarioCreateNonemptyInitCodeSuccessWontRevert(false)
        // // PRECOMPILE CALL scenarios
        ///////////////////////////////
        // .pScenarioPrcEcrecover(false)
        // .pScenarioPrcSha2256(false)
        // .pScenarioPrcRipemd160(false)
        // .pScenarioPrcIdentity(false)
        // .pScenarioPrcModexp(false)
        // .pScenarioPrcEcadd(false)
        // .pScenarioPrcEcmul(false)
        // .pScenarioPrcEcpairing(false)
        // .pScenarioPrcBlake2F(false)
        // .pScenarioPrcSuccessCallerWillRevert(false)
        // .pScenarioPrcSuccessCallerWontRevert(false)
        // .pScenarioPrcFailureKnownToHub(false)
        // .pScenarioPrcFailureKnownToRam(false)
        // .pScenarioPrcCallerGas(0)
        // .pScenarioPrcCalleeGas(0)
        // .pScenarioPrcReturnGas(0)
        // .pScenarioPrcCdo(0)
        // .pScenarioPrcCds(0)
        // .pScenarioPrcRao(0)
        // .pScenarioPrcRac(0)
        // RETURN scenarios
        ///////////////////
        .pScenarioReturnException(this.scenario.equals(RETURN_EXCEPTION))
        .pScenarioReturnFromMessageCallWillTouchRam(
            this.scenario.equals(RETURN_FROM_MESSAGE_CALL_WILL_TOUCH_RAM))
        .pScenarioReturnFromMessageCallWontTouchRam(
            this.scenario.equals(RETURN_FROM_MESSAGE_CALL_WONT_TOUCH_RAM))
        .pScenarioReturnFromDeploymentEmptyCodeWillRevert(
            this.scenario.equals(RETURN_FROM_DEPLOYMENT_EMPTY_CODE_WILL_REVERT))
        .pScenarioReturnFromDeploymentEmptyCodeWontRevert(
            this.scenario.equals(RETURN_FROM_DEPLOYMENT_EMPTY_CODE_WONT_REVERT))
        .pScenarioReturnFromDeploymentNonemptyCodeWillRevert(
            this.scenario.equals(RETURN_FROM_DEPLOYMENT_NONEMPTY_CODE_WILL_REVERT))
        .pScenarioReturnFromDeploymentNonemptyCodeWontRevert(
            this.scenario.equals(RETURN_FROM_DEPLOYMENT_NONEMPTY_CODE_WONT_REVERT))
    // // SELFDESTRUCT scenarios
    ////////////////////////////
    // .pScenarioSelfdestructException(false)
    // .pScenarioSelfdestructWillRevert(false)
    // .pScenarioSelfdestructWontRevertAlreadyMarked(false)
    // .pScenarioSelfdestructWontRevertNotYetMarked(false)
    ;
  }
}
