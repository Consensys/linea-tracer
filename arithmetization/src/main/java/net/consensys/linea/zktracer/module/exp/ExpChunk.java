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
import static net.consensys.linea.zktracer.module.exp.Trace.EXP_MODEXPLOG;
import static net.consensys.linea.zktracer.module.exp.Trace.ISZERO;
import static net.consensys.linea.zktracer.module.exp.Trace.MAX_CT_CMPTN_EXP_LOG;
import static net.consensys.linea.zktracer.module.exp.Trace.MAX_CT_CMPTN_MODEXP_LOG;
import static net.consensys.linea.zktracer.module.exp.Trace.MAX_CT_PRPRC_EXP_LOG;
import static net.consensys.linea.zktracer.module.exp.Trace.MAX_CT_PRPRC_MODEXP_LOG;
import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;

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

    // Linking constraints
    pComputationPltJmp = Bytes.of(16);
    if (expnHiIsZero) {
      pComputationRawAcc = bigIntegerToBytes(expLogExpParameters.exponentHi());
    } else {
      pComputationRawAcc = bigIntegerToBytes(expLogExpParameters.exponentLo());
    }
  }

  public ExpChunk(
      final MessageFrame frame, Wcp wcp, final ModexpLogExpParameters modexpLogExpParameters) {
    // MODEXP_LOG case
    this.frame = frame;
    this.wcp = wcp;
    this.modexpLogExpParameters = modexpLogExpParameters;
    isExpLog = false;
    pMacroInstructionExpInst = Bytes.of(EXP_MODEXPLOG);
    pMacroInstructionData1 = bigIntegerToBytes(modexpLogExpParameters.rawLeadHi());
    pMacroInstructionData2 = bigIntegerToBytes(modexpLogExpParameters.rawLeadLo());
    pMacroInstructionData3 = Bytes.of(modexpLogExpParameters.cdsCutoff());
    pMacroInstructionData4 = Bytes.of(modexpLogExpParameters.cdsCutoff());
    pMacroInstructionData5 = bigIntegerToBytes(modexpLogExpParameters.leadLog());
    initArrays(MAX_CT_PRPRC_MODEXP_LOG + 1);
    // TODO preprocessing and linking constraints
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
          .pComputationRawAcc(pComputationRawAcc.slice(maxCt - i)) //
          .pComputationTrimByte(UnsignedByte.of(pComputationTrimAcc.get(maxCt - i)))
          .pComputationTrimAcc(pComputationTrimAcc.slice(maxCt - i)) //
          .pComputationTanzb(pComputationTrimAcc.get(maxCt - i) != 0)
          .pComputationTanzbAcc(pComputationTanzbAcc.slice(maxCt - i)) //
          .pComputationMsnzb(pComputationMsnzb)
          .pComputationBitMsnzb(i > maxCt - 8 && ((pComputationMsnzb.toInteger() >> i) & 1) == 1)
          .pComputationAccMsnzb(
              UnsignedByte.of(
                  i > maxCt - 8 ? pComputationMsnzb.toInteger() & ((1 << (i + 1)) - 1) : 0)) //
          .pComputationManzb(
              i > maxCt - 8 && (pComputationMsnzb.toInteger() & ((1 << (i + 1)) - 1)) != 0)
          .pComputationManzbAcc(
              i > maxCt - 8
                  ? pComputationManzbAcc.slice(pComputationManzbAcc.size() - i)
                  : Bytes.of(0)); //
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
