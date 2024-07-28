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

package net.consensys.linea.zktracer.module.hub.fragment.scenario;

import java.util.Map;

import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.consensys.linea.zktracer.module.constants.GlobalConstants;
import net.consensys.linea.zktracer.module.hub.Trace;
import net.consensys.linea.zktracer.module.hub.fragment.TraceFragment;
import net.consensys.linea.zktracer.module.hub.section.call.precompileSubsection.PrecompileSubsection;

@Getter
@AllArgsConstructor
public class PrecompileScenarioFragment implements TraceFragment {

  public enum PrecompileScenario {
    PRC_FAILURE_KNOWN_TO_HUB,
    PRC_FAILURE_KNOWN_TO_RAM,
    PRC_SUCCESS_WILL_REVERT,
    PRC_SUCCESS_WONT_REVERT
  }

  public enum PrecompileFlag {
    PRC_UNDEFINED,
    PRC_ECRECOVER,
    PRC_SHA2_256,
    PRC_RIPEMD_160,
    PRC_IDENTITY,
    PRC_MODEXP,
    PRC_ECADD,
    PRC_ECMUL,
    PRC_ECPAIRING,
    PRC_BLAKE2F;

    private static final Map<PrecompileFlag, Integer> DATA_PHASE_MAP =
        Map.of(
            PRC_ECRECOVER, GlobalConstants.PHASE_ECRECOVER_DATA,
            PRC_SHA2_256, GlobalConstants.PHASE_SHA2_DATA,
            PRC_RIPEMD_160, GlobalConstants.PHASE_RIPEMD_DATA,
            // IDENTITY not supported
            // MODEXP not supported
            PRC_ECADD, GlobalConstants.PHASE_ECADD_DATA,
            PRC_ECMUL, GlobalConstants.PHASE_ECMUL_DATA,
            PRC_ECPAIRING, GlobalConstants.PHASE_ECPAIRING_DATA
            // BLAKE2f not supported
            );

    private static final Map<PrecompileFlag, Integer> RESULT_PHASE_MAP =
        Map.of(
            PRC_ECRECOVER, GlobalConstants.PHASE_ECRECOVER_RESULT,
            PRC_SHA2_256, GlobalConstants.PHASE_SHA2_RESULT,
            PRC_RIPEMD_160, GlobalConstants.PHASE_RIPEMD_RESULT,
            // IDENTITY not supported
            PRC_MODEXP, GlobalConstants.PHASE_MODEXP_RESULT,
            PRC_ECADD, GlobalConstants.PHASE_ECADD_RESULT,
            PRC_ECMUL, GlobalConstants.PHASE_ECMUL_RESULT,
            PRC_ECPAIRING, GlobalConstants.PHASE_ECPAIRING_RESULT,
            PRC_BLAKE2F, GlobalConstants.PHASE_BLAKE_RESULT);

    public int dataPhase() {
      if (!DATA_PHASE_MAP.containsKey(this)) {
        throw new IllegalArgumentException("Precompile not supported by the DATA_PHASE_MAP");
      }
      return DATA_PHASE_MAP.get(this);
    }

    public int resultPhase() {
      if (!RESULT_PHASE_MAP.containsKey(this)) {
        throw new IllegalArgumentException("Precompile not supported by the RESULT_PHASE_MAP");
      }
      return RESULT_PHASE_MAP.get(this);
    }

    public boolean isAnyOf(PrecompileFlag... flags) {
      for (PrecompileFlag flag : flags) {
        if (this == flag) {
          return true;
        }
      }
      return false;
    }
  }

  final PrecompileSubsection precompileSubSection;
  @Setter PrecompileScenario scenario;
  @Setter PrecompileFlag flag;

  public PrecompileScenarioFragment(
      final PrecompileSubsection precompileSubsection,
      final PrecompileFlag flag,
      final PrecompileScenario scenario) {
    this.precompileSubSection = precompileSubsection;
    this.flag = flag;
    this.scenario = scenario;
  }

  public boolean isPrcFailure() {
    return scenario == PrecompileScenario.PRC_FAILURE_KNOWN_TO_HUB
        || scenario == PrecompileScenario.PRC_FAILURE_KNOWN_TO_RAM;
  }

  @Override
  public Trace trace(Trace trace) {
    Preconditions.checkArgument(this.flag != PrecompileFlag.PRC_UNDEFINED);
    return trace
        .peekAtScenario(true)
        // // Precompile scenarios
        ////////////////////
        .pScenarioPrcEcrecover(flag == PrecompileFlag.PRC_ECRECOVER)
        .pScenarioPrcSha2256(flag == PrecompileFlag.PRC_SHA2_256)
        .pScenarioPrcRipemd160(flag == PrecompileFlag.PRC_RIPEMD_160)
        .pScenarioPrcIdentity(flag == PrecompileFlag.PRC_IDENTITY)
        .pScenarioPrcModexp(flag == PrecompileFlag.PRC_MODEXP)
        .pScenarioPrcEcadd(flag == PrecompileFlag.PRC_ECADD)
        .pScenarioPrcEcmul(flag == PrecompileFlag.PRC_ECMUL)
        .pScenarioPrcEcpairing(flag == PrecompileFlag.PRC_ECPAIRING)
        .pScenarioPrcBlake2F(flag == PrecompileFlag.PRC_BLAKE2F)
        .pScenarioPrcSuccessCallerWillRevert(scenario == PrecompileScenario.PRC_SUCCESS_WILL_REVERT)
        .pScenarioPrcSuccessCallerWontRevert(scenario == PrecompileScenario.PRC_SUCCESS_WONT_REVERT)
        .pScenarioPrcFailureKnownToHub(scenario == PrecompileScenario.PRC_FAILURE_KNOWN_TO_HUB)
        .pScenarioPrcFailureKnownToRam(scenario == PrecompileScenario.PRC_FAILURE_KNOWN_TO_RAM)
        .pScenarioPrcCallerGas(precompileSubSection.callerGas())
        .pScenarioPrcCalleeGas(precompileSubSection.calleeGas())
        .pScenarioPrcReturnGas(precompileSubSection.returnGas())
        .pScenarioPrcCdo(precompileSubSection.callDataMemorySpan().offset())
        .pScenarioPrcCds(precompileSubSection.callDataMemorySpan().length())
        .pScenarioPrcRao(precompileSubSection.parentReturnDataTarget().offset())
        .pScenarioPrcRac(precompileSubSection.parentReturnDataTarget().length());
  }
}
