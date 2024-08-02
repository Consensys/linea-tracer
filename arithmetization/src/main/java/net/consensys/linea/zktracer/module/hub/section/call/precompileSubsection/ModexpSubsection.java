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

package net.consensys.linea.zktracer.module.hub.section.call.precompileSubsection;

import static net.consensys.linea.zktracer.module.hub.fragment.imc.mmu.MmuCall.forModexpExtractBase;
import static net.consensys.linea.zktracer.module.hub.fragment.imc.mmu.MmuCall.forModexpExtractBbs;
import static net.consensys.linea.zktracer.module.hub.fragment.imc.mmu.MmuCall.forModexpExtractEbs;
import static net.consensys.linea.zktracer.module.hub.fragment.imc.mmu.MmuCall.forModexpExtractExponent;
import static net.consensys.linea.zktracer.module.hub.fragment.imc.mmu.MmuCall.forModexpExtractMbs;
import static net.consensys.linea.zktracer.module.hub.fragment.imc.mmu.MmuCall.forModexpExtractModulus;
import static net.consensys.linea.zktracer.module.hub.fragment.imc.mmu.MmuCall.forModexpFullResultCopy;
import static net.consensys.linea.zktracer.module.hub.fragment.imc.mmu.MmuCall.forModexpLoadLead;
import static net.consensys.linea.zktracer.module.hub.fragment.imc.mmu.MmuCall.forModexpPartialResultCopy;
import static net.consensys.linea.zktracer.module.hub.fragment.imc.oob.OobInstruction.OOB_INST_MODEXP_CDS;
import static net.consensys.linea.zktracer.module.hub.fragment.imc.oob.OobInstruction.OOB_INST_MODEXP_EXTRACT;
import static net.consensys.linea.zktracer.module.hub.fragment.imc.oob.OobInstruction.OOB_INST_MODEXP_LEAD;
import static net.consensys.linea.zktracer.module.hub.fragment.imc.oob.OobInstruction.OOB_INST_MODEXP_PRICING;
import static net.consensys.linea.zktracer.module.hub.fragment.imc.oob.OobInstruction.OOB_INST_MODEXP_XBS;
import static net.consensys.linea.zktracer.module.hub.fragment.scenario.PrecompileScenarioFragment.PrecompileScenario.PRC_FAILURE_KNOWN_TO_HUB;
import static net.consensys.linea.zktracer.module.limits.precompiles.ModexpEffectiveCall.PROVER_MAX_INPUT_BYTE_SIZE;

import com.google.common.base.Preconditions;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.mmu.MmuCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.oob.precompiles.PrecompileCommonOobCall;
import net.consensys.linea.zktracer.module.hub.precompiles.ModExpMetadata;
import net.consensys.linea.zktracer.module.hub.section.call.CallSection;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;

public class ModexpSubsection extends PrecompileSubsection {

  private final ModExpMetadata modExpMetadata;
  private PrecompileCommonOobCall sixthOobCall;

  public ModexpSubsection(final Hub hub, final CallSection callSection) {
    super(hub, callSection);

    modExpMetadata = new ModExpMetadata(callData, callDataMemorySpan);
    if (modExpMetadata.bbsInt() > PROVER_MAX_INPUT_BYTE_SIZE
        || modExpMetadata.mbsInt() > PROVER_MAX_INPUT_BYTE_SIZE
        || modExpMetadata.ebsInt() > PROVER_MAX_INPUT_BYTE_SIZE) {
      hub.modexpEffectiveCall().addPrecompileLimit(Integer.MAX_VALUE);
      return;
    }

    final PrecompileCommonOobCall firstOobCAll = new PrecompileCommonOobCall(OOB_INST_MODEXP_CDS);
    firstImcFragment.callOob(firstOobCAll);

    // TODO: sounds weird we construct all OOB Call same way ...
    final ImcFragment secondImcFragment = ImcFragment.empty(hub);
    fragments().add(secondImcFragment);
    if (modExpMetadata.extractBbs()) {
      final MmuCall mmuCall = forModexpExtractBbs(hub, this, modExpMetadata);
      secondImcFragment.callMmu(mmuCall);
    }
    final PrecompileCommonOobCall secondOobCall = new PrecompileCommonOobCall(OOB_INST_MODEXP_XBS);
    secondImcFragment.callOob(secondOobCall);

    final ImcFragment thirdImcFragment = ImcFragment.empty(hub);
    fragments().add(thirdImcFragment);
    if (modExpMetadata.extractEbs()) {
      final MmuCall mmuCall = forModexpExtractEbs(hub, this, modExpMetadata);
      thirdImcFragment.callMmu(mmuCall);
    }
    final PrecompileCommonOobCall thirdOobCall = new PrecompileCommonOobCall(OOB_INST_MODEXP_XBS);
    thirdImcFragment.callOob(thirdOobCall);

    final ImcFragment fourthImcFragment = ImcFragment.empty(hub);
    fragments().add(fourthImcFragment);
    if (modExpMetadata.extractMbs()) {
      final MmuCall mmuCall = forModexpExtractMbs(hub, this, modExpMetadata);
      fourthImcFragment.callMmu(mmuCall);
    }
    final PrecompileCommonOobCall fourthOobCall = new PrecompileCommonOobCall(OOB_INST_MODEXP_XBS);
    fourthImcFragment.callOob(fourthOobCall);

    final ImcFragment fifthImcFragment = ImcFragment.empty(hub);
    fragments().add(fifthImcFragment);
    final PrecompileCommonOobCall fifthOobCall = new PrecompileCommonOobCall(OOB_INST_MODEXP_LEAD);
    fifthImcFragment.callOob(fifthOobCall);
    if (modExpMetadata.loadRawLeadingWord()) {
      final MmuCall mmuCall = forModexpLoadLead(hub, this, modExpMetadata);
      fifthImcFragment.callMmu(mmuCall);
      // final ExpCall expCall = new ModexpLogExpCall(); // TODO: to do
      // fifthImcFragment.callExp(expCall);
    }

    final ImcFragment sixthImcFragment = ImcFragment.empty(hub);
    fragments().add(sixthImcFragment);
    sixthOobCall = new PrecompileCommonOobCall(OOB_INST_MODEXP_PRICING);
    sixthImcFragment.callOob(sixthOobCall);
  }

  @Override
  public void resolveAtContextReEntry(Hub hub, CallFrame callFrame) {
    super.resolveAtContextReEntry(hub, callFrame);

    // sanity check
    Preconditions.checkArgument(callSuccess == sixthOobCall.isHubSuccess());

    if (!callSuccess) {
      precompileScenarioFragment.scenario(PRC_FAILURE_KNOWN_TO_HUB);
      return;
    }

    modExpMetadata.rawResult(returnData);
    hub.blakeModexpData().callModexp(modExpMetadata, this.exoModuleOperationId());

    final ImcFragment seventhImcFragment = ImcFragment.empty(hub);
    fragments().add(seventhImcFragment);
    final PrecompileCommonOobCall seventhOobCall =
        new PrecompileCommonOobCall(OOB_INST_MODEXP_EXTRACT);
    seventhImcFragment.callOob(seventhOobCall);
    if (modExpMetadata.extractModulus()) {
      final MmuCall mmuCall = forModexpExtractBase(hub, this, modExpMetadata);
      seventhImcFragment.callMmu(mmuCall);
    }

    final ImcFragment eighthImcFragment = ImcFragment.empty(hub);
    fragments().add(eighthImcFragment);
    if (modExpMetadata.extractModulus()) {
      final MmuCall mmuCall = forModexpExtractExponent(hub, this, modExpMetadata);
      eighthImcFragment.callMmu(mmuCall);
    }

    final ImcFragment ninthImcFragment = ImcFragment.empty(hub);
    fragments().add(ninthImcFragment);
    if (modExpMetadata.extractModulus()) {
      final MmuCall mmuCall = forModexpExtractModulus(hub, this, modExpMetadata);
      ninthImcFragment.callMmu(mmuCall);
    }

    final ImcFragment tenthImcFragment = ImcFragment.empty(hub);
    fragments().add(tenthImcFragment);
    if (modExpMetadata.extractModulus()) {
      final MmuCall mmuCall = forModexpFullResultCopy(hub, this, modExpMetadata);
      tenthImcFragment.callMmu(mmuCall);
    }

    final ImcFragment eleventhImcFragment = ImcFragment.empty(hub);
    fragments().add(eleventhImcFragment);
    if (modExpMetadata.mbsNonZero() && !parentReturnDataTarget.lengthNull()) {
      final MmuCall mmuCall = forModexpPartialResultCopy(hub, this, modExpMetadata);
      eleventhImcFragment.callMmu(mmuCall);
    }
  }

  // 13 = 1 + 12 (scenario row + up to 12 miscellaneous fragments)
  @Override
  protected short maxNumberOfLines() {
    return 13;
    // Note: we don't have the successBit available at the moment
    // and can't provide the "real" value (8 in case of failure.)
  }
}
