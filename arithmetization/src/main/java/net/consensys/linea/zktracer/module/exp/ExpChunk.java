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
import static net.consensys.linea.zktracer.module.exp.Trace.MAX_CT_CMPTN_EXP_LOG;
import static net.consensys.linea.zktracer.module.exp.Trace.MAX_CT_CMPTN_MODEXP_LOG;
import static net.consensys.linea.zktracer.module.exp.Trace.MAX_CT_MACRO_EXP_LOG;
import static net.consensys.linea.zktracer.module.exp.Trace.MAX_CT_MACRO_MODEXP_LOG;
import static net.consensys.linea.zktracer.module.exp.Trace.MAX_CT_PRPRC_EXP_LOG;
import static net.consensys.linea.zktracer.module.exp.Trace.MAX_CT_PRPRC_MODEXP_LOG;
import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;

import java.math.BigInteger;

import lombok.Getter;
import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.evm.frame.MessageFrame;

@Getter
public class ExpChunk extends ModuleOperation {
  private boolean isExpLog;
  private boolean[] pComputationPltBit;
  private Bytes[] pComputationPltJmp;
  private UnsignedByte[] pComputationRawByte;
  private Bytes[] pComputationRawAcc;
  private UnsignedByte[] pComputationTrimByte;
  private Bytes[] pComputationTrimAcc;
  private boolean[] pComputationTanzb;
  private Bytes[] pComputationTanzbAcc;
  private UnsignedByte[] pComputationMsnzb;
  private boolean[] pComputationBitMsnzb;
  private UnsignedByte[] pComputationAccMsnzb;
  private boolean[] pComputationManzb;
  private Bytes[] pComputationManzbAcc;
  // macro contains only one row, thus no need for an array
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

  MessageFrame frame;
  ExpLogExpParameters expLogExpParameters;
  ModexpLogExpParameters modexpLogExpParameters;

  public ExpChunk(final MessageFrame frame, final ExpLogExpParameters expLogExpParameters) {
    // EXP_LOG case
    this.frame = frame;
    this.expLogExpParameters = expLogExpParameters;
    isExpLog = true;
    pMacroInstructionExpInst = Bytes.of(EXP_EXPLOG);
    pMacroInstructionData1 = bigIntegerToBytes(expLogExpParameters.exponentHi());
    pMacroInstructionData2 = bigIntegerToBytes(expLogExpParameters.exponentLo());
    pMacroInstructionData3 = Bytes.of(0);
    pMacroInstructionData4 = Bytes.of(0);
    pMacroInstructionData5 = bigIntegerToBytes(expLogExpParameters.dynCost());
    initColumns(MAX_CT_CMPTN_EXP_LOG + 1, MAX_CT_PRPRC_EXP_LOG + 1);
    // Do preprocessing here
    // Then fill bytes / bits etc and ideally call the generic methods
  }

  public ExpChunk(final MessageFrame frame, final ModexpLogExpParameters modexpLogExpParameters) {
    // MODEXP_LOG case
    this.frame = frame;
    this.modexpLogExpParameters = modexpLogExpParameters;
    isExpLog = false;
    pMacroInstructionExpInst = Bytes.of(EXP_MODEXPLOG);
    pMacroInstructionData1 = bigIntegerToBytes(modexpLogExpParameters.rawLeadHi());
    pMacroInstructionData2 = bigIntegerToBytes(modexpLogExpParameters.rawLeadLo());
    pMacroInstructionData3 = Bytes.of(modexpLogExpParameters.cdsCutoff());
    pMacroInstructionData4 = Bytes.of(modexpLogExpParameters.cdsCutoff());
    pMacroInstructionData5 = bigIntegerToBytes(modexpLogExpParameters.leadLog());
    initColumns(MAX_CT_CMPTN_MODEXP_LOG + 1, MAX_CT_PRPRC_MODEXP_LOG + 1);
    // Do preprocessing here
    // Then fill bytes / bits etc and ideally call the generic methods
  }

  private void initColumns(int pComputationLen, int pPreprocessingLen) {
    pComputationPltBit = new boolean[pComputationLen];
    pComputationPltJmp = new Bytes[pComputationLen];
    pComputationRawByte = new UnsignedByte[pComputationLen];
    pComputationRawAcc = new Bytes[pComputationLen];
    pComputationTrimByte = new UnsignedByte[pComputationLen];
    pComputationTrimAcc = new Bytes[pComputationLen];
    pComputationTanzb = new boolean[pComputationLen];
    pComputationTanzbAcc = new Bytes[pComputationLen];
    pComputationMsnzb = new UnsignedByte[pComputationLen];
    pComputationBitMsnzb = new boolean[pComputationLen];
    pComputationAccMsnzb = new UnsignedByte[pComputationLen];
    pComputationManzb = new boolean[pComputationLen];
    pComputationManzbAcc = new Bytes[pComputationLen];
    pPreprocessingWcpFlag = new boolean[pPreprocessingLen];
    pPreprocessingWcpArg1Hi = new Bytes[pPreprocessingLen];
    pPreprocessingWcpArg1Lo = new Bytes[pPreprocessingLen];
    pPreprocessingWcpArg2Hi = new Bytes[pPreprocessingLen];
    pPreprocessingWcpArg2Lo = new Bytes[pPreprocessingLen];
    pPreprocessingWcpInst = new UnsignedByte[pPreprocessingLen];
  }

  private void setByteDecomposition(UnsignedByte[] dByte, Bytes[] dAcc) {
    dAcc[0] = Bytes.of(dByte[0].toInteger());
    for (int ct = 1; ct < dByte.length; ct++) {
      dAcc[ct] =
          bigIntegerToBytes(
              BigInteger.valueOf(256)
                  .multiply(dAcc[ct - 1].toUnsignedBigInteger())
                  .add(dByte[ct].toBigInteger()));
    }
  }

  private void setBitDecomposition(boolean[] dBit, UnsignedByte[] dAcc) {
    dAcc[0] = UnsignedByte.of(dBit[0] ? 1 : 0);
    for (int ct = 1; ct < dBit.length; ct++) {
      dAcc[ct] =
          UnsignedByte.of(
              BigInteger.valueOf(2)
                  .multiply(dAcc[ct - 1].toBigInteger())
                  .add(dBit[ct] ? BigInteger.ONE : BigInteger.ZERO)
                  .intValue());
    }
  }

  @Override
  protected int computeLineCount() {
    return 0; // maxCt
  }

  int maxCt(boolean cmptn, boolean macro, boolean prprc) {
    if (cmptn) {
      if (isExpLog) {
        return MAX_CT_CMPTN_EXP_LOG;
      } else {
        return MAX_CT_CMPTN_MODEXP_LOG;
      }
    } else if (macro) {
      if (isExpLog) {
        return MAX_CT_MACRO_EXP_LOG;
      } else {
        return MAX_CT_MACRO_MODEXP_LOG;
      }
    } else if (prprc) {
      if (isExpLog) {
        return MAX_CT_PRPRC_EXP_LOG;
      } else {
        return MAX_CT_PRPRC_MODEXP_LOG;
      }
    }
    return 0;
  }

  final void traceComputation(int stamp, Trace trace) {
    int maxCt = this.maxCt(true, false, false);
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
          .pComputationPltBit(pComputationPltBit[i])
          .pComputationPltJmp(pComputationPltJmp[i])
          .pComputationRawByte(pComputationRawByte[i])
          .pComputationRawAcc(pComputationRawAcc[i])
          .pComputationTrimByte(pComputationTrimByte[i])
          .pComputationTrimAcc(pComputationTrimAcc[i])
          .pComputationTanzb(pComputationTanzb[i])
          .pComputationTanzbAcc(pComputationTanzbAcc[i])
          .pComputationMsnzb(pComputationMsnzb[i])
          .pComputationBitMsnzb(pComputationBitMsnzb[i])
          .pComputationAccMsnzb(pComputationAccMsnzb[i])
          .pComputationManzb(pComputationManzb[i])
          .pComputationManzbAcc(pComputationManzbAcc[i]);
    }
  }

  final void traceMacro(int stamp, Trace trace) {
    int maxCt = this.maxCt(false, true, false);
    for (int i = 0; i < maxCt + 1; i++) {
      trace
          .cmptn(false)
          .macro(true)
          .prprc(false)
          .stamp(Bytes.ofUnsignedLong(stamp))
          .ct(Bytes.of(i))
          .ctMax(Bytes.of(maxCt))
          .isExpLog(isExpLog)
          .isModexpLog(!isExpLog)
          .pMacroInstructionExpInst(pMacroInstructionExpInst)
          .pMacroInstructionData1(pMacroInstructionData1)
          .pMacroInstructionData2(pMacroInstructionData2)
          .pMacroInstructionData3(pMacroInstructionData3)
          .pMacroInstructionData4(pMacroInstructionData4)
          .pMacroInstructionData5(pMacroInstructionData5);
    }
  }

  final void tracePreprocessing(int stamp, Trace trace) {
    int maxCt = this.maxCt(false, false, true);
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
          .validateRow();
    }
  }
}
