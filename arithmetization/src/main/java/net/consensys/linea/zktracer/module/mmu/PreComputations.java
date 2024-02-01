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

package net.consensys.linea.zktracer.module.mmu;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.mmu.precomputations.MmuInstInvalidCodePrefixPreComputation;
import net.consensys.linea.zktracer.module.mmu.precomputations.MmuInstMLoadPreComputation;
import net.consensys.linea.zktracer.module.mmu.precomputations.MmuInstMStore8PreComputation;
import net.consensys.linea.zktracer.module.mmu.precomputations.MmuInstMStorePreComputation;
import net.consensys.linea.zktracer.module.wcp.Wcp;

@Accessors(fluent = true)
class PreComputations {
  @Getter private final MmuInstMLoadPreComputation mLoadPreComputation;
  @Getter private final MmuInstMStorePreComputation mStorePreComputation;
  private final MmuInstMStore8PreComputation mStore8PreComputation;
  private final MmuInstInvalidCodePrefixPreComputation invalidCodePrefixPreComputation;

  PreComputations(Euc euc, Wcp wcp) {
    // TODO need to instantiate only mmuInstPreComputation
    this.mLoadPreComputation = new MmuInstMLoadPreComputation(euc, wcp);
    this.mStorePreComputation = new MmuInstMStorePreComputation(euc, wcp);
    this.mStore8PreComputation = new MmuInstMStore8PreComputation(euc);
    this.invalidCodePrefixPreComputation = new MmuInstInvalidCodePrefixPreComputation(euc, wcp);
  }

  public MmuData compute(MmuData mmuData) {
    int mmuInstruction = mmuData.hubToMmuValues().mmuInstruction();

    return switch (mmuInstruction) {
      case Trace.MMU_INST_MLOAD -> {
        mmuData = mLoadPreComputation.preProcess(mmuData);
        yield mLoadPreComputation.setMicroInstructions(mmuData);
      }
      case Trace.MMU_INST_MSTORE -> {
        mmuData = mStorePreComputation.preProcess(mmuData);
        yield mStorePreComputation.setMicroInstructions(mmuData);
      }
      case Trace.MMU_INST_MSTORE8 -> {
        mmuData = mStore8PreComputation.preProcess(mmuData);
        yield mStore8PreComputation.setMicroInstructions(mmuData);
      }
      case Trace.MMU_INST_INVALID_CODE_PREFIX -> {
        mmuData = invalidCodePrefixPreComputation.preProcess(mmuData);
        yield invalidCodePrefixPreComputation.setMicroInstructions(mmuData);
      }
      default -> throw new IllegalArgumentException(
          "Unexpected MMU instruction: %d".formatted(mmuInstruction));
    };
  }
}
