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
package net.consensys.linea.zktracer.module.hub.section.call.precompileSubsection;

import static com.google.common.base.Preconditions.checkArgument;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.WORD_SIZE;
import static net.consensys.linea.zktracer.module.hub.fragment.imc.oob.OobInstruction.*;
import static net.consensys.linea.zktracer.module.hub.fragment.scenario.PrecompileScenarioFragment.PrecompileScenario.PRC_FAILURE_KNOWN_TO_HUB;
import static net.consensys.linea.zktracer.module.hub.fragment.scenario.PrecompileScenarioFragment.PrecompileScenario.PRC_FAILURE_KNOWN_TO_RAM;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.imc.mmu.MmuCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.oob.precompiles.PrecompileCommonOobCall;
import net.consensys.linea.zktracer.module.hub.section.call.CallSection;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;

public class EllipticCurvePrecompileSubsection extends PrecompileSubsection {
  final PrecompileCommonOobCall oobCall;

  public EllipticCurvePrecompileSubsection(Hub hub, CallSection callSection) {
    super(hub, callSection);

    oobCall =
        switch (flag()) {
          case PRC_ECRECOVER -> new PrecompileCommonOobCall(OOB_INST_ECRECOVER);
          case PRC_ECADD -> new PrecompileCommonOobCall(OOB_INST_ECADD);
          case PRC_ECMUL -> new PrecompileCommonOobCall(OOB_INST_ECMUL);
          case PRC_ECPAIRING -> new PrecompileCommonOobCall(OOB_INST_ECPAIRING);
          default -> throw new IllegalArgumentException(
              String.format(
                  "Precompile address %s not supported by constructor", this.flag().toString()));
        };

    firstImcFragment.callOob(oobCall);

    // Recall that the default scenario is PRC_SUCCESS_WONT_REVERT
    if (!oobCall.isHubSuccess()) {
      this.setScenario(PRC_FAILURE_KNOWN_TO_HUB);
    }
  }

  @Override
  public void resolveAtContextReEntry(Hub hub, CallFrame callFrame) {
    super.resolveAtContextReEntry(hub, callFrame);

    // sanity checks
    switch (flag()) {
      case PRC_ECRECOVER -> {
        checkArgument(oobCall.isHubSuccess() == callSuccess);
        checkArgument(returnData.size() == (callSuccess ? WORD_SIZE : 0));
      }
      case PRC_ECPAIRING -> checkArgument(returnData.size() == (callSuccess ? WORD_SIZE : 0));
      case PRC_ECADD, PRC_ECMUL -> checkArgument(
          returnData.size() == (callSuccess ? 2 * WORD_SIZE : 0));
      default -> throw new IllegalArgumentException("Not an elliptic curve precompile");
    }

    if (!oobCall.isHubSuccess()) {
      return;
    }

    switch (flag()) {
      case PRC_ECADD, PRC_ECMUL, PRC_ECPAIRING -> {
        if (oobCall.isHubSuccess() && !callSuccess) {
          precompileScenarioFragment.scenario(PRC_FAILURE_KNOWN_TO_RAM);
        }
      }
        // ECRECOVER can only be FAILURE_KNOWN_TO_HUB or some form of SUCCESS_XXXX_REVERT
      default -> {}
    }

    MmuCall firstMmuCall;
    final boolean nonemptyCallData = !callData.isEmpty();

    if (nonemptyCallData) {
      switch (flag()) {
        case PRC_ECRECOVER -> {
          final boolean successfulRecovery = !returnData.isEmpty();
          firstMmuCall = MmuCall.callDataExtractionForEcrecover(hub, this, successfulRecovery);
        }
        case PRC_ECADD -> firstMmuCall = MmuCall.callDataExtractionForEcadd(hub, this, callSuccess);
        case PRC_ECMUL -> firstMmuCall = MmuCall.callDataExtractionForEcmul(hub, this, callSuccess);
        case PRC_ECPAIRING -> firstMmuCall = MmuCall.callDataExtractionForEcpairing(hub, this, callSuccess);
        default -> throw new IllegalArgumentException("Not an elliptic curve precompile");
      }
      firstImcFragment.callMmu(firstMmuCall);
    }
  }
}
