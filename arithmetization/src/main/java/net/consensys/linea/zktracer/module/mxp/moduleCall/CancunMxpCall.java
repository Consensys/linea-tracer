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

package net.consensys.linea.zktracer.module.mxp.moduleCall;

import static net.consensys.linea.zktracer.module.mxp.MxpUtils.isWordPricingOpcode;
import static net.consensys.linea.zktracer.module.mxp.MxpUtils.memoryCost;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.imc.MxpCall;
import net.consensys.linea.zktracer.module.mxp.MxpExoCall;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.opcode.gas.BillingRate;
import org.apache.tuweni.bytes.Bytes;

/** The parent class of this MXP Call is located in the Hub. */
public class CancunMxpCall extends MxpCall {

  public final long words;
  public final long cMem;
  public final Bytes gWord;
  public final Bytes gByte;

  public CancunMxpCall(Hub hub) {
    super(hub);
    this.words = this.memorySizeInWords;
    this.cMem = memoryCost(this.memorySizeInWords);
    this.gWord = getCostBy(BillingRate.BY_WORD);
    this.gByte = getCostBy(BillingRate.BY_BYTE);
    // Initialization of the computed values of MxpCall
    this.gasMxp = 0L;
    setMxpxFromMxpxExpression();
  }

  /** Store all wcp and euc computations with params and results */
  public final MxpExoCall[] exoCalls = new MxpExoCall[ctMax()];

  /** Computed by CancunTrivialMxpCall */
  public boolean size1IsZero = false;

  public boolean size2IsZero = false;

  /** Computed by CancunNotMSizeNorTrivialMxpCall for CancunMxpxMxpCall */
  public int mxpxExpression = 0;

  /**
   * Computed in CancunStateUpdateMxpCall for State update scenarii CancunStateUpdtWPricingMxpCall
   * and CancunStateUpdtBPricingMxpCall
   */
  public boolean isStateUpdate = false;

  public long wordsNew = 0L;
  public long cMemNew = 0L;
  public long extraGasCost = 0L;

  public int ctMax() {
    return 0;
  }
  ;

  public boolean isMSizeScenario() {
    return false;
  }

  public boolean isTrivialScenario() {
    return false;
  }

  public boolean isMxpxScenario() {
    return false;
  }

  public boolean isStateUpdateWordPricingScenario() {
    return false;
  }

  public boolean isStateUpdateBytePricingScenario() {
    return false;
  }

  public void setMxpxFromMxpxExpression() {
    this.mxpx = this.mxpxExpression != 0;
  }

  public void setGasMpxFromExtraGasCost() {
    this.gasMxp = this.cMemNew - this.cMem + this.extraGasCost;
  }

  /**
   * User from Cancun fork - Get the Mxp scenario for the given MxpCall.
   *
   * @param mxpCall given mxpCall instance to convert to CancunMxpCall
   * @return CancunMxpCall instance corresponding to the Mxp scenario
   */
  public static CancunMxpCall getCancunMxpCall(MxpCall mxpCall) {
    OpCode opCode = OpCode.of(mxpCall.hub.messageFrame().getCurrentOperation().getOpcode());
    if (opCode == OpCode.MSIZE) {
      return (CancunMSizeMxpCall) mxpCall;
    }
    if (mxpCall.size1.isZero() && mxpCall.size2.isZero()) {
      return (CancunTrivialMxpCall) mxpCall;
    }
    CancunNotMSizeNorTrivialMxpCall cancunNotMSizeNorTrivialMxpCall =
        new CancunNotMSizeNorTrivialMxpCall(mxpCall.hub);
    if (cancunNotMSizeNorTrivialMxpCall.mxpx) {
      return (CancunMxpxMxpCall) mxpCall;
    } else {
      if (isWordPricingOpcode(opCode)) {
        return (CancunStateUpdateWordPricingMxpCall) mxpCall;
      }
      return (CancunStateUpdateBytePricingMxpCall) mxpCall;
    }
  }
}
