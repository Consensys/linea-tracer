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

package net.consensys.linea.zktracer.module.hub.fragment.imc.oob.precompiles.common.bls.fixedSizeFixedGasCost;

import static net.consensys.linea.zktracer.Trace.GAS_CONST_BLS_MAP_FP2_TO_G2;
import static net.consensys.linea.zktracer.Trace.OOB_INST_BLS_MAP_FP2_TO_G2;
import static net.consensys.linea.zktracer.Trace.PRC_BLS_MAP_FP2_TO_G2_SIZE;
import static net.consensys.linea.zktracer.TraceCancun.Oob.CT_MAX_BLS_MAP_FP2_TO_G2;

import java.math.BigInteger;

import net.consensys.linea.zktracer.Trace;

public class BlsPointEvaluationOobCall extends BlsFixedSizeFixedGasCostOobCall {
  public BlsPointEvaluationOobCall(BigInteger calleeGas) {
    super(calleeGas);
  }

  @Override
  long precompileExpectedCds() {
    return PRC_BLS_MAP_FP2_TO_G2_SIZE;
  }

  @Override
  long precompileLongCost() {
    return GAS_CONST_BLS_MAP_FP2_TO_G2;
  }

  @Override
  protected void traceOobInstructionInOob(Trace.Oob trace) {
    trace.isBlsMapFp2ToG2(true).oobInst(OOB_INST_BLS_MAP_FP2_TO_G2);
  }

  @Override
  protected void traceOobInstructionInHub(Trace.Hub trace) {
    trace.pMiscOobInst(OOB_INST_BLS_MAP_FP2_TO_G2);
  }

  @Override
  public int ctMax() {
    return CT_MAX_BLS_MAP_FP2_TO_G2;
  }
}
