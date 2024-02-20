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
import static net.consensys.linea.zktracer.module.exp.Trace.EXP_EXPLOG;
import static net.consensys.linea.zktracer.module.exp.Trace.EXP_MODEXPLOG;
import static net.consensys.linea.zktracer.module.exp.Trace.ISZERO;
import static net.consensys.linea.zktracer.module.exp.Trace.LT;
import static net.consensys.linea.zktracer.module.exp.Trace.MAX_CT_CMPTN_EXP_LOG;
import static net.consensys.linea.zktracer.module.exp.Trace.MAX_CT_CMPTN_MODEXP_LOG;
import static net.consensys.linea.zktracer.module.exp.Trace.MAX_CT_PRPRC_EXP_LOG;
import static net.consensys.linea.zktracer.module.exp.Trace.MAX_CT_PRPRC_MODEXP_LOG;
import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;
import static net.consensys.linea.zktracer.types.Utils.leftPadTo;

import java.math.BigInteger;
import java.math.RoundingMode;

import lombok.Getter;
import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;

@Getter
public class ExpChunk extends ModuleOperation {
  private final boolean isExpLog;
  private short pComputationPltJmp = 0;
  private Bytes pComputationRawAcc; // (last row) paired with RawByte
  private Bytes pComputationTrimAcc = Bytes.EMPTY; // (last row) paired with TrimByte
  private UnsignedByte pComputationMsb = UnsignedByte.of(0);
  // Macro contains only one row, thus no need for an array
  private final int pMacroExpInst;
  private final Bytes pMacroData1;
  private final Bytes pMacroData2;
  private final Bytes pMacroData3;
  private final Bytes pMacroData4;
  private final Bytes pMacroData5;
  private boolean[] pPreprocessingWcpFlag;
  private Bytes[] pPreprocessingWcpArg1Hi;
  private Bytes[] pPreprocessingWcpArg1Lo;
  private Bytes[] pPreprocessingWcpArg2Hi;
  private Bytes[] pPreprocessingWcpArg2Lo;
  private UnsignedByte[] pPreprocessingWcpInst;
  private boolean[] pPreprocessingWcpRes;

  ExpLogExpParameters expLogExpParameters;
  ModexpLogExpParameters modexpLogExpParameters;

  Wcp wcp;

  public ExpChunk(Wcp wcp, final ExpLogExpParameters expLogExpParameters) {
    // EXP_LOG case
    this.wcp = wcp;
    this.expLogExpParameters = expLogExpParameters;

    // Fill isExpLog
    isExpLog = true;

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

  public ExpChunk(Wcp wcp, final ModexpLogExpParameters modexpLogExpParameters) {
    // MODEXP_LOG case
    this.wcp = wcp;
    this.modexpLogExpParameters = modexpLogExpParameters;

    // Fill isExpLog
    isExpLog = false;

    pMacroExpInst = EXP_MODEXPLOG;
    pMacroData1 = bigIntegerToBytes(modexpLogExpParameters.rawLeadHi());
    pMacroData2 = bigIntegerToBytes(modexpLogExpParameters.rawLeadLo());
    pMacroData3 = Bytes.of(modexpLogExpParameters.cdsCutoff());
    pMacroData4 = Bytes.of(modexpLogExpParameters.ebsCutoff());
    pMacroData5 = bigIntegerToBytes(modexpLogExpParameters.leadLog());
    initArrays(MAX_CT_PRPRC_MODEXP_LOG + 1);

    // Preprocessing
    BigInteger trimLimb =
        modexpLogExpParameters.trimHi().signum() == 0
            ? modexpLogExpParameters.trimLo()
            : modexpLogExpParameters.trimHi();
    int trimLog = trimLimb.signum() == 0 ? 0 : log2(trimLimb, RoundingMode.FLOOR);
    int nBitsOfLeadingByteExcludingLeadingBit = trimLog % 8;
    int nBytesExcludingLeadingByte = trimLog / 8;

    // First row
    pPreprocessingWcpFlag[0] = true;
    pPreprocessingWcpArg1Hi[0] = Bytes.of(0);
    pPreprocessingWcpArg1Lo[0] = Bytes.of(modexpLogExpParameters.cdsCutoff());
    pPreprocessingWcpArg2Hi[0] = Bytes.of(0);
    pPreprocessingWcpArg2Lo[0] = Bytes.of(modexpLogExpParameters.ebsCutoff());
    pPreprocessingWcpInst[0] = UnsignedByte.of(LT);
    pPreprocessingWcpRes[0] =
        modexpLogExpParameters.cdsCutoff() < modexpLogExpParameters.ebsCutoff();
    int minCutoff = min(modexpLogExpParameters.cdsCutoff(), modexpLogExpParameters.ebsCutoff());

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
    boolean minCutoffLeq16 = pPreprocessingWcpRes[1];

    // Lookup
    wcp.callLT(Bytes.of(minCutoff), Bytes.of(17));

    // Third row
    pPreprocessingWcpFlag[2] = true;
    pPreprocessingWcpArg1Hi[2] = Bytes.of(0);
    pPreprocessingWcpArg1Lo[2] = Bytes.of(getModexpLogExpParameters().ebsCutoff());
    pPreprocessingWcpArg2Hi[2] = Bytes.of(0);
    pPreprocessingWcpArg2Lo[2] = Bytes.of(17);
    pPreprocessingWcpInst[2] = UnsignedByte.of(LT);
    pPreprocessingWcpRes[2] = modexpLogExpParameters.ebsCutoff() < 17;
    boolean ebsCutoffLeq16 = pPreprocessingWcpRes[2];

    // Lookup
    wcp.callLT(Bytes.of(getModexpLogExpParameters().ebsCutoff()), Bytes.of(17));

    // Fourth row
    pPreprocessingWcpFlag[3] = true;
    pPreprocessingWcpArg1Hi[3] = Bytes.of(0);
    pPreprocessingWcpArg1Lo[3] = bigIntegerToBytes(modexpLogExpParameters.rawLeadHi());
    pPreprocessingWcpArg2Hi[3] = Bytes.of(0);
    pPreprocessingWcpArg2Lo[3] = Bytes.of(0);
    pPreprocessingWcpInst[3] = UnsignedByte.of(ISZERO);
    pPreprocessingWcpRes[3] = modexpLogExpParameters.rawLeadHi().signum() == 0;
    boolean rawHiPartIsZero = pPreprocessingWcpRes[3];

    // Lookup
    wcp.callISZERO(bigIntegerToBytes(modexpLogExpParameters.rawLeadHi()));

    // Fifth row
    int paddedBase2Log = 8 * nBytesExcludingLeadingByte + nBitsOfLeadingByteExcludingLeadingBit;

    pPreprocessingWcpFlag[4] = true;
    pPreprocessingWcpArg1Hi[4] = Bytes.of(0);
    pPreprocessingWcpArg1Lo[4] = Bytes.of(paddedBase2Log);
    pPreprocessingWcpArg2Hi[4] = Bytes.of(0);
    pPreprocessingWcpArg2Lo[4] = Bytes.of(0);
    pPreprocessingWcpInst[4] = UnsignedByte.of(ISZERO);
    pPreprocessingWcpRes[4] = paddedBase2Log == 0;
    boolean trivialTrim = pPreprocessingWcpRes[4];

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
    short maxCt = (short) MAX_CT_CMPTN_MODEXP_LOG;
    for (short i = 0; i < maxCt + 1; i++) {
      boolean pltBit = i >= pComputationPltJmp;
      byte rawByte = pComputationRawAcc.get(i);
      byte trimByte = pltBit ? 0 : rawByte;
      pComputationTrimAcc = Bytes.concatenate(pComputationTrimAcc, Bytes.of(trimByte));
      if (trimByte != 0 && pComputationMsb.toInteger() == 0) {
        // Fill msb
        pComputationMsb = UnsignedByte.of(trimByte);
      }
    }
  }

  private void initArrays(int pPreprocessingLen) {
    pPreprocessingWcpFlag = new boolean[pPreprocessingLen];
    pPreprocessingWcpArg1Hi = new Bytes[pPreprocessingLen];
    pPreprocessingWcpArg1Lo = new Bytes[pPreprocessingLen];
    pPreprocessingWcpArg2Hi = new Bytes[pPreprocessingLen];
    pPreprocessingWcpArg2Lo = new Bytes[pPreprocessingLen];
    pPreprocessingWcpInst = new UnsignedByte[pPreprocessingLen];
    pPreprocessingWcpRes = new boolean[pPreprocessingLen];
  }

  @Override
  protected int computeLineCount() {
    // We assume MAX_CT_MACRO_EXP_LOG = MAX_CT_MACRO_MODEXP_LOG = 0;
    if (isExpLog) {
      return MAX_CT_CMPTN_EXP_LOG + MAX_CT_PRPRC_EXP_LOG + 3;
    } else {
      return MAX_CT_CMPTN_MODEXP_LOG + MAX_CT_PRPRC_MODEXP_LOG + 3;
    }
  }

  final void traceComputation(int stamp, Trace trace) {
    boolean tanzb;
    short pComputationTanzbAcc = 0; // Paired with Tanzb
    boolean manzb;
    short pComputationManzbAcc = 0; // Paired with Manzb
    short maxCt = (short) (isExpLog ? MAX_CT_CMPTN_EXP_LOG : MAX_CT_CMPTN_MODEXP_LOG);
    for (short i = 0; i < maxCt + 1; i++) {
      /*
      All the values are derived from
      isExpLog
      pComputationPltJmp
      pComputationRawAcc
      pComputationTrimAcc
      pComputationMsb
      */
      // tanzb turns to 1 iff trimAcc is nonzero
      tanzb = pComputationTrimAcc.slice(0, i + 1).toBigInteger().signum() != 0;
      pComputationTanzbAcc += (short) (tanzb ? 1 : 0);
      // manzb turns to 1 iff msbAcc is nonzero
      manzb = i > maxCt - 8 && pComputationMsb.slice(0, i % 8 + 1) != 0;
      pComputationManzbAcc += (short) (manzb ? 1 : 0);
      trace
          .cmptn(true)
          .stamp(stamp)
          .ct(i)
          .ctMax(maxCt)
          .isExpLog(isExpLog)
          .isModexpLog(!isExpLog)
          .pComputationPltBit(i >= pComputationPltJmp)
          .pComputationPltJmp(pComputationPltJmp)
          .pComputationRawByte(UnsignedByte.of(pComputationRawAcc.get(i)))
          .pComputationRawAcc(pComputationRawAcc.slice(0, i + 1))
          .pComputationTrimByte(UnsignedByte.of(pComputationTrimAcc.get(i)))
          .pComputationTrimAcc(pComputationTrimAcc.slice(0, i + 1))
          .pComputationTanzb(tanzb)
          .pComputationTanzbAcc(pComputationTanzbAcc)
          .pComputationMsb(pComputationMsb)
          .pComputationMsbBit(i > maxCt - 8 && pComputationMsb.get(i % 8))
          .pComputationMsbAcc(
              UnsignedByte.of(i > maxCt - 8 ? pComputationMsb.slice(0, i % 8 + 1) : 0))
          .pComputationManzb(manzb)
          .pComputationManzbAcc(pComputationManzbAcc)
          .fillAndValidateRow();
    }
  }

  final void traceMacro(int stamp, Trace trace) {
    // We assume MAX_CT_MACRO_EXP_LOG = MAX_CT_MACRO_MODEXP_LOG = 0;
    trace
        .macro(true)
        .stamp(stamp)
        .ct((short) 0)
        .ctMax((short) 0)
        .isExpLog(isExpLog)
        .isModexpLog(!isExpLog)
        .pMacroExpInst(pMacroExpInst)
        .pMacroData1(pMacroData1)
        .pMacroData2(pMacroData2)
        .pMacroData3(pMacroData3)
        .pMacroData4(pMacroData4)
        .pMacroData5(pMacroData5)
        .fillAndValidateRow();
  }

  final void tracePreprocessing(int stamp, Trace trace) {
    short maxCt = (short) (isExpLog ? MAX_CT_PRPRC_EXP_LOG : MAX_CT_PRPRC_MODEXP_LOG);
    for (short i = 0; i < maxCt + 1; i++) {
      trace
          .prprc(true)
          .stamp(stamp)
          .ct(i)
          .ctMax(maxCt)
          .isExpLog(isExpLog)
          .isModexpLog(!isExpLog)
          .pPreprocessingWcpFlag(pPreprocessingWcpFlag[i])
          .pPreprocessingWcpArg1Hi(pPreprocessingWcpArg1Hi[i])
          .pPreprocessingWcpArg1Lo(pPreprocessingWcpArg1Lo[i])
          .pPreprocessingWcpArg2Hi(pPreprocessingWcpArg2Hi[i])
          .pPreprocessingWcpArg2Lo(pPreprocessingWcpArg2Lo[i])
          .pPreprocessingWcpInst(pPreprocessingWcpInst[i])
          .pPreprocessingWcpRes(pPreprocessingWcpRes[i])
          .fillAndValidateRow();
    }
  }
}
