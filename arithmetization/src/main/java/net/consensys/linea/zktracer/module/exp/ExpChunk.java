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
import static net.consensys.linea.zktracer.types.Conversions.booleanToInt;

import lombok.Getter;
import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.evm.frame.MessageFrame;

@Getter
public class ExpChunk extends ModuleOperation {
  private final boolean isExpLog;
  private Bytes pComputationPltJmp = Bytes.of(0);
  private Bytes pComputationRawAcc = Bytes.of(0); // paired with RawByte
  private Bytes pComputationTrimAcc = Bytes.of(0); // paired with TrimByte
  private Bytes pComputationTanzbAcc = Bytes.of(0); // Paired with Tanzb
  private UnsignedByte pComputationMsnzb = UnsignedByte.of(0);
  private Bytes pComputationManzbAcc = Bytes.of(0); // Paired with Manzb
  // Macro contains only one row, thus no need for an array
  private final Bytes pMacroInstructionExpInst;
  private final Bytes pMacroInstructionData1;
  private final Bytes pMacroInstructionData2;
  private final Bytes pMacroInstructionData3;
  private final Bytes pMacroInstructionData4;
  private final Bytes pMacroInstructionData5;
  private boolean[] pPreprocessingWcpFlag;
  private Bytes[] pPreprocessingWcpArg1Hi;
  private Bytes[] pPreprocessingWcpArg1Lo;
  private Bytes[] pPreprocessingWcpArg2Hi;
  private Bytes[] pPreprocessingWcpArg2Lo;
  private UnsignedByte[] pPreprocessingWcpInst;
  private boolean[] pPreprocessingWcpRes;

  MessageFrame frame;
  ExpLogExpParameters expLogExpParameters;
  ModexpLogExpParameters modexpLogExpParameters;

  Wcp wcp;

  public ExpChunk(
      final MessageFrame frame, Wcp wcp, final ExpLogExpParameters expLogExpParameters) {
    // EXP_LOG case
    this.frame = frame;
    this.wcp = wcp;
    this.expLogExpParameters = expLogExpParameters;
    // Fill isExpLog
    isExpLog = true;
    pMacroInstructionExpInst = Bytes.of(EXP_EXPLOG);
    pMacroInstructionData1 = bigIntegerToBytes(expLogExpParameters.exponentHi());
    pMacroInstructionData2 = bigIntegerToBytes(expLogExpParameters.exponentLo());
    pMacroInstructionData3 = Bytes.of(0);
    pMacroInstructionData4 = Bytes.of(0);
    pMacroInstructionData5 = bigIntegerToBytes(expLogExpParameters.dynCost());
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
    wcp.callISZERO(expLogExpParameters.exponent());

    // Linking constraints and fill rawAcc
    pComputationPltJmp = Bytes.of(16);
    if (expnHiIsZero) {
      pComputationRawAcc = bigIntegerToBytes(expLogExpParameters.exponentHi());
    } else {
      pComputationRawAcc = bigIntegerToBytes(expLogExpParameters.exponentLo());
    }

    // Fill trimAcc
    int maxCt = MAX_CT_CMPTN_EXP_LOG;
    for (int i = 0; i < maxCt + 1; i++) {
      boolean pltBit = i > pComputationPltJmp.toInt();
      byte rawByte = pComputationRawAcc.get(maxCt - i);
      byte trimByte = pltBit ? 0 : rawByte;
      pComputationTrimAcc = Bytes.concatenate(Bytes.of(trimByte), pComputationTrimAcc);
    }

    // Fill msnzb
    pComputationMsnzb = UnsignedByte.of(0);

    // Fill tanzbAcc, manzbAcc
    for (int i = 0; i < maxCt + 1; i++) {
      boolean tanzb = pComputationTrimAcc.slice(maxCt - i).toBigInteger().signum() != 0;
      pComputationTanzbAcc = Bytes.of(pComputationTanzbAcc.toInt() + (tanzb ? 1 : 0));
    }
    pComputationManzbAcc = Bytes.of(0);
  }

  public ExpChunk(
      final MessageFrame frame, Wcp wcp, final ModexpLogExpParameters modexpLogExpParameters) {
    // MODEXP_LOG case
    this.frame = frame;
    this.wcp = wcp;
    this.modexpLogExpParameters = modexpLogExpParameters;
    // Fill isExpLog
    isExpLog = false;
    pMacroInstructionExpInst = Bytes.of(EXP_MODEXPLOG);
    pMacroInstructionData1 = bigIntegerToBytes(modexpLogExpParameters.rawLeadHi());
    pMacroInstructionData2 = bigIntegerToBytes(modexpLogExpParameters.rawLeadLo());
    pMacroInstructionData3 = Bytes.of(modexpLogExpParameters.cdsCutoff());
    pMacroInstructionData4 = Bytes.of(modexpLogExpParameters.cdsCutoff());
    pMacroInstructionData5 = bigIntegerToBytes(modexpLogExpParameters.leadLog());
    initArrays(MAX_CT_PRPRC_MODEXP_LOG + 1);

    // Preprocessing
    int usedBits = modexpLogExpParameters.leadLog().intValue() % 8;
    int usedBytes = modexpLogExpParameters.leadLog().intValue() / 8;

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
    int paddedBase2Log = 8 * usedBytes + usedBits;

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
      pComputationRawAcc = bigIntegerToBytes(modexpLogExpParameters.rawLeadHi());
    } else {
      if (!rawHiPartIsZero) {
        pComputationRawAcc = bigIntegerToBytes(modexpLogExpParameters.rawLeadHi());
      } else {
        pComputationRawAcc = bigIntegerToBytes(modexpLogExpParameters.rawLeadLo());
      }
    }

    // Fill pltJmp
    pComputationPltJmp = Bytes.of(minCutoff - (1 - booleanToInt(minCutoffLeq16)) * 16);

    // Fill trimAcc
    int maxCt = MAX_CT_CMPTN_MODEXP_LOG;
    for (int i = 0; i < maxCt + 1; i++) {
      boolean pltBit = i > pComputationPltJmp.toInt();
      byte rawByte = pComputationRawAcc.get(maxCt - i);
      byte trimByte = pltBit ? 0 : rawByte;
      pComputationTrimAcc = Bytes.concatenate(Bytes.of(trimByte), pComputationTrimAcc);
    }

    // Fill msnzb
    pComputationMsnzb = UnsignedByte.of(bigIntegerToBytes(modexpLogExpParameters.lead()).get(0));

    // Fill tanzbAcc, manzbAcc
    for (int i = 0; i < maxCt + 1; i++) {
      boolean tanzb = pComputationTrimAcc.slice(maxCt - i).toBigInteger().signum() != 0;
      pComputationTanzbAcc = Bytes.of(pComputationTanzbAcc.toInt() + (tanzb ? 1 : 0));

      boolean manzb = i > maxCt - 8 && (pComputationMsnzb.toInteger() & ((1 << (i + 1)) - 1)) != 0;
      pComputationManzbAcc = Bytes.of(pComputationManzbAcc.toInt() + (manzb ? 1 : 0));
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
    int maxCt = isExpLog ? MAX_CT_CMPTN_EXP_LOG : MAX_CT_CMPTN_MODEXP_LOG;
    for (int i = 0; i < maxCt + 1; i++) {
      /*
      All the values are derived from
      isExpLog
      pComputationPltJmp
      pComputationRawAcc
      pComputationTrimAcc
      pComputationTanzbAcc
      pComputationMsnzb
      pComputationManzbAcc
      */
      trace
          .cmptn(true)
          .macro(false)
          .prprc(false)
          .stamp(Bytes.ofUnsignedLong(stamp))
          .ct(Bytes.of(i))
          .ctMax(Bytes.of(maxCt))
          .isExpLog(isExpLog)
          .isModexpLog(!isExpLog)
          .pComputationPltBit(i > pComputationPltJmp.toInt())
          .pComputationPltJmp(pComputationPltJmp)
          .pComputationRawByte(UnsignedByte.of(pComputationRawAcc.get(maxCt - i)))
          .pComputationRawAcc(pComputationRawAcc.slice(maxCt - i))
          .pComputationTrimByte(UnsignedByte.of(pComputationTrimAcc.get(maxCt - i)))
          .pComputationTrimAcc(pComputationTrimAcc.slice(maxCt - i))
          .pComputationTanzb(pComputationTrimAcc.slice(maxCt - i).toBigInteger().signum() != 0)
          .pComputationTanzbAcc(pComputationTanzbAcc.slice(maxCt - i))
          .pComputationMsnzb(pComputationMsnzb)
          .pComputationBitMsnzb(i > maxCt - 8 && ((pComputationMsnzb.toInteger() >> i) & 1) == 1)
          .pComputationAccMsnzb(
              UnsignedByte.of(
                  i > maxCt - 8 ? pComputationMsnzb.toInteger() & ((1 << (i + 1)) - 1) : 0))
          .pComputationManzb(
              i > maxCt - 8 && (pComputationMsnzb.toInteger() & ((1 << (i + 1)) - 1)) != 0)
          .pComputationManzbAcc(
              i > maxCt - 8
                  ? pComputationManzbAcc.slice(pComputationManzbAcc.size() - i)
                  : Bytes.of(0));
    }
  }

  final void traceMacro(int stamp, Trace trace) {
    // We assume MAX_CT_MACRO_EXP_LOG = MAX_CT_MACRO_MODEXP_LOG = 0;
    trace
        .cmptn(false)
        .macro(true)
        .prprc(false)
        .stamp(Bytes.ofUnsignedLong(stamp))
        .ct(Bytes.of(0))
        .ctMax(Bytes.of(0))
        .isExpLog(isExpLog)
        .isModexpLog(!isExpLog)
        .pMacroInstructionExpInst(pMacroInstructionExpInst)
        .pMacroInstructionData1(pMacroInstructionData1)
        .pMacroInstructionData2(pMacroInstructionData2)
        .pMacroInstructionData3(pMacroInstructionData3)
        .pMacroInstructionData4(pMacroInstructionData4)
        .pMacroInstructionData5(pMacroInstructionData5);
  }

  final void tracePreprocessing(int stamp, Trace trace) {
    int maxCt = isExpLog ? MAX_CT_PRPRC_EXP_LOG : MAX_CT_PRPRC_MODEXP_LOG;
    for (int i = 0; i < maxCt + 1; i++) { //
      trace
          .cmptn(false)
          .macro(false)
          .prprc(true)
          .stamp(Bytes.ofUnsignedLong(stamp))
          .ct(Bytes.of(i))
          .ctMax(Bytes.of(maxCt))
          .isExpLog(isExpLog)
          .isModexpLog(!isExpLog)
          .pPreprocessingWcpFlag(pPreprocessingWcpFlag[i])
          .pPreprocessingWcpArg1Hi(pPreprocessingWcpArg1Hi[i])
          .pPreprocessingWcpArg1Lo(pPreprocessingWcpArg1Lo[i])
          .pPreprocessingWcpArg2Hi(pPreprocessingWcpArg2Hi[i])
          .pPreprocessingWcpArg2Lo(pPreprocessingWcpArg2Lo[i])
          .pPreprocessingWcpInst(pPreprocessingWcpInst[i])
          .pPreprocessingWcpRes(pPreprocessingWcpRes[i])
          .validateRow();
    }
  }
}
