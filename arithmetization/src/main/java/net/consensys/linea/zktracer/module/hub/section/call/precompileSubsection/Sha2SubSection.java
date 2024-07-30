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

import static net.consensys.linea.zktracer.module.hub.fragment.imc.oob.OobInstruction.OOB_INST_SHA2;
import static net.consensys.linea.zktracer.module.hub.fragment.scenario.PrecompileScenarioFragment.PrecompileFlag.PRC_SHA2_256;
import static net.consensys.linea.zktracer.module.hub.fragment.scenario.PrecompileScenarioFragment.PrecompileScenario.PRC_FAILURE_KNOWN_TO_HUB;
import static net.consensys.linea.zktracer.module.shakiradata.HashType.SHA256;

import com.google.common.base.Preconditions;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.mmu.MmuCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.oob.precompiles.PrecompileCommonOobCall;
import net.consensys.linea.zktracer.module.hub.section.call.CallSection;
import net.consensys.linea.zktracer.module.shakiradata.ShakiraDataOperation;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;

public class Sha2SubSection extends PrecompileSubsection {
  final ImcFragment firstImcFragment;
  final PrecompileCommonOobCall oobCall;

  public Sha2SubSection(Hub hub, CallSection callSection) {
    super(hub, callSection);

    precompileScenarioFragment.setFlag(PRC_SHA2_256);
    oobCall = new PrecompileCommonOobCall(OOB_INST_SHA2);

    firstImcFragment = ImcFragment.empty(hub);
    firstImcFragment.callOob(oobCall);

    if (!oobCall.isHubSuccess()) {
      precompileScenarioFragment.setScenario(PRC_FAILURE_KNOWN_TO_HUB);
    }
  }

  @Override
  public void resolveAtContextReEntry(Hub hub, CallFrame callFrame) {
    super.resolveAtContextReEntry(hub, callFrame);

    // sanity check
    Preconditions.checkArgument(successBit == oobCall.isHubSuccess());

    if (!successBit) {
      return;
    }

    // NOTE: we trigger the SHAKIRA module for nonempty call data only
    if (!callData.isEmpty()) {
      final ShakiraDataOperation shakiraCall =
          new ShakiraDataOperation(
              this.callSection.hubStamp(), SHA256, callData(), callFrame.frame().getReturnData());
      hub.shakiraData().call(shakiraCall);

      final MmuCall mmuCall = MmuCall.forShaTwoOrRipemdCallDataExtraction(hub, this);
      firstImcFragment.callMmu(mmuCall);
    }

    // the full result transfer happens in all cases
    final ImcFragment secondImcFragment = ImcFragment.empty(hub);
    this.fragments().add(secondImcFragment);

    final MmuCall fullOutputDataTransfer = MmuCall.forShaTwoOrRipemdFullResultTransfer(hub, this);
    secondImcFragment.callMmu(fullOutputDataTransfer);

    final ImcFragment thirdImcFragment = ImcFragment.empty(hub);
    this.fragments().add(thirdImcFragment);

    // the partial copy of return data happens only if the caller context
    // provided a nonempty return data target
    if (!parentReturnDataTarget.isEmpty()) {
      final MmuCall partialReturnDataCopy = MmuCall.forShaTwoOrRipemdPartialResultCopy(hub, this);
      thirdImcFragment.callMmu(partialReturnDataCopy);
    }
  }

  // 4 = 1 + 3 (scenario row + up to 3 miscellaneous fragments)
  @Override
  protected short maxNumberOfLines() {
    return 4;
    // Note: we don't have the successBit available at the moment
    // and can't provide the "real" value (2 in case of FKTH.)
  }
}
