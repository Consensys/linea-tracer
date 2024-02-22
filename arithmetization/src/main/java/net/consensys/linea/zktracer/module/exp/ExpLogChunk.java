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

package net.consensys.linea.zktracer.module.exp;

import static net.consensys.linea.zktracer.module.exp.Trace.EXP_EXPLOG;
import static net.consensys.linea.zktracer.module.exp.Trace.ISZERO;
import static net.consensys.linea.zktracer.module.exp.Trace.MAX_CT_CMPTN_EXP_LOG;
import static net.consensys.linea.zktracer.module.exp.Trace.MAX_CT_PRPRC_EXP_LOG;
import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;
import static net.consensys.linea.zktracer.types.Utils.leftPadTo;

import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;

public class ExpLogChunk extends ExpChunk {
  private final ExpLogExpParameters expLogExpParameters;

  @Override
  protected boolean isExpLog() {
    return true;
  }

  public ExpLogChunk(final Wcp wcp, final ExpLogExpParameters expLogExpParameters) {
    // EXP_LOG case
    this.wcp = wcp;
    this.expLogExpParameters = expLogExpParameters;
  }

  @Override
  public void preCompute() {
    pMacroExpInst = EXP_EXPLOG;
    pMacroData1 = bigIntegerToBytes(expLogExpParameters.exponentHi());
    pMacroData2 = bigIntegerToBytes(expLogExpParameters.exponentLo());
    pMacroData3 = Bytes.of(0);
    pMacroData4 = Bytes.of(0);
    pMacroData5 = bigIntegerToBytes(expLogExpParameters.dynCost());
    initArrays(MAX_CT_PRPRC_EXP_LOG + 1);

    // Preprocessing
    // First row
    pPreprocessingWcpFlag[0] = true;
    pPreprocessingWcpArg1Hi[0] = Bytes.of(0);
    pPreprocessingWcpArg1Lo[0] = bigIntegerToBytes(expLogExpParameters.exponentHi());
    pPreprocessingWcpArg2Hi[0] = Bytes.of(0);
    pPreprocessingWcpArg2Lo[0] = Bytes.of(0);
    pPreprocessingWcpInst[0] = UnsignedByte.of(ISZERO);
    pPreprocessingWcpRes[0] = expLogExpParameters.exponentHi().signum() == 0;
    boolean expnHiIsZero = pPreprocessingWcpRes[0];

    // Lookup
    wcp.callISZERO(bigIntegerToBytes(expLogExpParameters.exponentHi()));

    // Linking constraints and fill rawAcc
    pComputationPltJmp = 16;
    if (expnHiIsZero) {
      pComputationRawAcc = leftPadTo(bigIntegerToBytes(expLogExpParameters.exponentLo()), 16);
    } else {
      pComputationRawAcc = leftPadTo(bigIntegerToBytes(expLogExpParameters.exponentHi()), 16);
    }

    // Fill trimAcc
    short maxCt = (short) MAX_CT_CMPTN_EXP_LOG;
    for (short i = 0; i < maxCt + 1; i++) {
      boolean pltBit = i >= pComputationPltJmp;
      byte rawByte = pComputationRawAcc.get(i);
      byte trimByte = pltBit ? 0 : rawByte;
      pComputationTrimAcc = Bytes.concatenate(pComputationTrimAcc, Bytes.of(trimByte));
    }

    // msb remains 0
  }
}
