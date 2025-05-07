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

package net.consensys.linea.zktracer.module.mxp.moduleScenario;

import static net.consensys.linea.zktracer.module.mxp.MxpUtils.isWordPricingOpcode;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.hub.fragment.imc.MxpCall;
import net.consensys.linea.zktracer.module.mxp.MxpExoCall;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.opcode.OpCode;

public abstract class MxpScenario {
  /** Store all wcp and euc computations with params and results */
  public final List<MxpExoCall> exoCalls;

  /** Computed by TrivialMxpScenario */
  @Getter public boolean size1IsZero = false;

  @Getter public boolean size2IsZero = false;

  /** Computed by MxpxMxpScenario */
  @Getter public int mxpxExpression = 0;

  /**
   * Computed by State update scenarii (StateUpdtWPricingMxpScenario and
   * StateUpdtBPricingMxpScenario)
   */
  @Getter public final boolean isStateUpdate = false;

  @Getter public long wordsNew = 0L;
  @Getter public long cMemNew = 0L;
  @Getter public long extraGasCost = 0L;

  protected MxpScenario() {
    exoCalls = new ArrayList<>(ctMax());
  }

  public abstract void compute(MxpCall mxpCall, Wcp wcp, Euc euc);

  public abstract int ctMax();

  public boolean isMSizeScenario() {
    return false;
  }

  public boolean isTrivialScenario() {
    return false;
  }

  public boolean isMxpxScenario() {
    return false;
  }

  public boolean isStateUpdtWPricingScenario() {
    return false;
  }

  public boolean isStateUpdtBPricingScenario() {
    return false;
  }

  /**
   * Get the MxpScenario for the given MxpCall.
   *
   * @param mxpCall from which we retrieve the scenario
   * @return MxpScenario instance corresponding to the MxpCall
   */
  public static MxpScenario getMxpScenario(MxpCall mxpCall) {
    OpCode opCode = mxpCall.getOpCodeData().mnemonic();
    if (opCode == OpCode.MSIZE) {
      return new MSizeMxpScenario();
    }
    if (mxpCall.getSize1().isZero() && mxpCall.getSize2().isZero()) {
      return new TrivialMxpScenario();
    }
    if (mxpCall.isMxpx()) {
      return new MxpxMxpScenario();
    }
    if (isWordPricingOpcode(opCode)) {
      return new StateUpdtWPricingMxpScenario();
    }
    return new StateUpdtBPricingMxpScenario();
  }
}
