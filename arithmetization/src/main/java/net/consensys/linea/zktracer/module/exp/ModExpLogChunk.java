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

import static com.google.common.math.BigIntegerMath.log2;
import static java.lang.Math.min;
import static net.consensys.linea.zktracer.module.exp.Trace.EXP_MODEXPLOG;
import static net.consensys.linea.zktracer.module.exp.Trace.ISZERO;
import static net.consensys.linea.zktracer.module.exp.Trace.LT;
import static net.consensys.linea.zktracer.module.exp.Trace.MAX_CT_CMPTN_MODEXP_LOG;
import static net.consensys.linea.zktracer.module.exp.Trace.MAX_CT_PRPRC_MODEXP_LOG;
import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;
import static net.consensys.linea.zktracer.types.Utils.leftPadTo;

import java.math.BigInteger;
import java.math.RoundingMode;

import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;

public class ModExpLogChunk extends ExpChunk {
  private final ModexpLogExpParameters modexpLogExpParameters;

  @Override
  protected boolean isExpLog() {
    return false;
  }

  public ModExpLogChunk(final Wcp wcp, final ModexpLogExpParameters modexpLogExpParameters) {
    // MODEXP_LOG case
    this.wcp = wcp;
    this.modexpLogExpParameters = modexpLogExpParameters;
  }

  @Override
  public void preCompute() {
    pMacroExpInst = EXP_MODEXPLOG;
    pMacroData1 = bigIntegerToBytes(modexpLogExpParameters.rawLeadHi());
    pMacroData2 = bigIntegerToBytes(modexpLogExpParameters.rawLeadLo());
    pMacroData3 = Bytes.of(modexpLogExpParameters.cdsCutoff());
    pMacroData4 = Bytes.of(modexpLogExpParameters.ebsCutoff());
    pMacroData5 = bigIntegerToBytes(modexpLogExpParameters.leadLog());
    initArrays(MAX_CT_PRPRC_MODEXP_LOG + 1);

    // Preprocessing
    final BigInteger trimLimb =
        modexpLogExpParameters.trimHi().signum() == 0
            ? modexpLogExpParameters.trimLo()
            : modexpLogExpParameters.trimHi();
    final int trimLog = trimLimb.signum() == 0 ? 0 : log2(trimLimb, RoundingMode.FLOOR);
    final int nBitsOfLeadingByteExcludingLeadingBit = trimLog % 8;
    final int nBytesExcludingLeadingByte = trimLog / 8;

    // First row
    pPreprocessingWcpFlag[0] = true;
    pPreprocessingWcpArg1Hi[0] = Bytes.of(0);
    pPreprocessingWcpArg1Lo[0] = Bytes.of(modexpLogExpParameters.cdsCutoff());
    pPreprocessingWcpArg2Hi[0] = Bytes.of(0);
    pPreprocessingWcpArg2Lo[0] = Bytes.of(modexpLogExpParameters.ebsCutoff());
    pPreprocessingWcpInst[0] = UnsignedByte.of(LT);
    pPreprocessingWcpRes[0] =
        modexpLogExpParameters.cdsCutoff() < modexpLogExpParameters.ebsCutoff();
    final int minCutoff =
        min(modexpLogExpParameters.cdsCutoff(), modexpLogExpParameters.ebsCutoff());

    // Lookup
    wcp.callLT(
        Bytes.of(modexpLogExpParameters.cdsCutoff()), Bytes.of(modexpLogExpParameters.ebsCutoff()));

    // Second row
    pPreprocessingWcpFlag[1] = true;
    pPreprocessingWcpArg1Hi[1] = Bytes.of(0);
    pPreprocessingWcpArg1Lo[1] = Bytes.of(minCutoff);
    pPreprocessingWcpArg2Hi[1] = Bytes.of(0);
    pPreprocessingWcpArg2Lo[1] = Bytes.of(17);
    pPreprocessingWcpInst[1] = UnsignedByte.of(LT);
    pPreprocessingWcpRes[1] = minCutoff < 17;
    final boolean minCutoffLeq16 = pPreprocessingWcpRes[1];

    // Lookup
    wcp.callLT(Bytes.of(minCutoff), Bytes.of(17));

    // Third row
    pPreprocessingWcpFlag[2] = true;
    pPreprocessingWcpArg1Hi[2] = Bytes.of(0);
    pPreprocessingWcpArg1Lo[2] = Bytes.of(modexpLogExpParameters.ebsCutoff());
    pPreprocessingWcpArg2Hi[2] = Bytes.of(0);
    pPreprocessingWcpArg2Lo[2] = Bytes.of(17);
    pPreprocessingWcpInst[2] = UnsignedByte.of(LT);
    pPreprocessingWcpRes[2] = modexpLogExpParameters.ebsCutoff() < 17;

    // Lookup
    wcp.callLT(Bytes.of(modexpLogExpParameters.ebsCutoff()), Bytes.of(17));

    // Fourth row
    pPreprocessingWcpFlag[3] = true;
    pPreprocessingWcpArg1Hi[3] = Bytes.of(0);
    pPreprocessingWcpArg1Lo[3] = bigIntegerToBytes(modexpLogExpParameters.rawLeadHi());
    pPreprocessingWcpArg2Hi[3] = Bytes.of(0);
    pPreprocessingWcpArg2Lo[3] = Bytes.of(0);
    pPreprocessingWcpInst[3] = UnsignedByte.of(ISZERO);
    pPreprocessingWcpRes[3] = modexpLogExpParameters.rawLeadHi().signum() == 0;
    final boolean rawHiPartIsZero = pPreprocessingWcpRes[3];

    // Lookup
    wcp.callISZERO(bigIntegerToBytes(modexpLogExpParameters.rawLeadHi()));

    // Fifth row
    final int paddedBase2Log =
        8 * nBytesExcludingLeadingByte + nBitsOfLeadingByteExcludingLeadingBit;

    pPreprocessingWcpFlag[4] = true;
    pPreprocessingWcpArg1Hi[4] = Bytes.of(0);
    pPreprocessingWcpArg1Lo[4] = Bytes.of(paddedBase2Log);
    pPreprocessingWcpArg2Hi[4] = Bytes.of(0);
    pPreprocessingWcpArg2Lo[4] = Bytes.of(0);
    pPreprocessingWcpInst[4] = UnsignedByte.of(ISZERO);
    pPreprocessingWcpRes[4] = paddedBase2Log == 0;

    // Lookup
    wcp.callISZERO(Bytes.of(paddedBase2Log));

    // Linking constraints and fill rawAcc
    if (minCutoffLeq16) {
      pComputationRawAcc = leftPadTo(bigIntegerToBytes(modexpLogExpParameters.rawLeadHi()), 16);
    } else {
      if (!rawHiPartIsZero) {
        pComputationRawAcc = leftPadTo(bigIntegerToBytes(modexpLogExpParameters.rawLeadHi()), 16);
      } else {
        pComputationRawAcc = leftPadTo(bigIntegerToBytes(modexpLogExpParameters.rawLeadLo()), 16);
      }
    }

    // Fill pltJmp
    if (minCutoffLeq16) {
      pComputationPltJmp = (short) minCutoff;
    } else {
      if (!rawHiPartIsZero) {
        pComputationPltJmp = (short) 16;
      } else {
        pComputationPltJmp = (short) (minCutoff - 16);
      }
    }

    // Fill trimAcc
    final short maxCt = (short) MAX_CT_CMPTN_MODEXP_LOG;
    for (short i = 0; i < maxCt + 1; i++) {
      final boolean pltBit = i >= pComputationPltJmp;
      final byte rawByte = pComputationRawAcc.get(i);
      final byte trimByte = pltBit ? 0 : rawByte;
      pComputationTrimAcc = Bytes.concatenate(pComputationTrimAcc, Bytes.of(trimByte));
      if (trimByte != 0 && pComputationMsb.toInteger() == 0) {
        // Fill msb
        pComputationMsb = UnsignedByte.of(trimByte);
      }
    }
  }
}
