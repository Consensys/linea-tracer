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
import static net.consensys.linea.zktracer.types.Conversions.booleanToInt;

import lombok.Getter;
import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.evm.frame.MessageFrame;

@Getter
public class ExpChunk extends ModuleOperation {
  private boolean cmptn;
  private boolean macro;
  private boolean prprc;
  private int ctMax;
  private boolean isExpLog;
  private boolean isModexpLog;
  private boolean pComputationPltBit;
  private Bytes pComputationPltJmp;
  private UnsignedByte pComputationRawByte;
  private Bytes pComputationRawAcc;
  private UnsignedByte pComputationTrimByte;
  private Bytes pComputationTrimAcc;
  private boolean pComputationTanzb;
  private Bytes pComputationTanzbAcc;
  private UnsignedByte pComputationMsnzb;
  private boolean pComputationBitMsnzb;
  private UnsignedByte pComputationAccMsnzb;
  private boolean pComputationManzb;
  private Bytes pComputationManzbAcc;
  private Bytes pMacroInstructionExpInst;
  private Bytes pMacroInstructionData1;
  private Bytes pMacroInstructionData2;
  private Bytes pMacroInstructionData3;
  private Bytes pMacroInstructionData4;
  private Bytes pMacroInstructionData5;
  private boolean pPreprocessingWcpFlag;
  private Bytes pPreprocessingWcpArg1Hi;
  private Bytes pPreprocessingWcpArg1Lo;
  private Bytes pPreprocessingWcpArg2Hi;
  private Bytes pPreprocessingWcpArg2Lo;
  private UnsignedByte pPreprocessingWcpInst;

  public ExpChunk(
      final MessageFrame frame,
      boolean cmptn,
      boolean macro,
      boolean prprc,
      boolean isExpLog,
      boolean isModexpLog) {
    this.cmptn = cmptn;
    this.macro = macro;
    this.prprc = prprc;
    this.isExpLog = isExpLog;
    this.isModexpLog = isModexpLog;
  }

  @Override
  protected int computeLineCount() {
    return 0; // maxCt
  }

  int flagSumPerspective() {
    return booleanToInt(cmptn) + booleanToInt(macro) + booleanToInt(prprc);
  }

  int flagSumMacro() {
    return booleanToInt(isExpLog) + booleanToInt(isModexpLog);
  }

  int wghtSumMacro() {
    if (isExpLog) {
      return EXP_EXPLOG;
    } else if (isModexpLog) {
      return EXP_MODEXPLOG;
    }
    return 0;
  }

  int maxCt() {
    if (cmptn) {
      if (isExpLog) {
        return MAX_CT_CMPTN_EXP_LOG;
      } else if (isModexpLog) {
        return MAX_CT_CMPTN_MODEXP_LOG;
      }
    } else if (macro) {
      if (isExpLog) {
        return MAX_CT_MACRO_EXP_LOG;
      } else if (isModexpLog) {
        return MAX_CT_MACRO_MODEXP_LOG;
      }
    } else if (prprc) {
      if (isExpLog) {
        return MAX_CT_PRPRC_EXP_LOG;
      } else if (isModexpLog) {
        return MAX_CT_PRPRC_MODEXP_LOG;
      }
    }
    return 0;
  }

  final void trace(int stamp, Trace trace) {
    for (int i = 0; i < this.maxCt(); i++) { // TODO
      trace
          .cmptn(cmptn)
          .macro(macro)
          .prprc(prprc)
          .stamp(Bytes.ofUnsignedLong(stamp))
          .ct(Bytes.of(i))
          .ctMax(Bytes.of(0))
          .isExpLog(isExpLog)
          .isModexpLog(isModexpLog)
          .pComputationPltBit(false)
          .pComputationPltJmp(Bytes.of(0))
          .pComputationRawByte(UnsignedByte.ZERO)
          .pComputationRawAcc(Bytes.of(0))
          .pComputationTrimByte(UnsignedByte.ZERO)
          .pComputationTrimAcc(Bytes.of(0))
          .pComputationTanzb(false)
          .pComputationTanzbAcc(Bytes.of(0))
          .pComputationMsnzb(UnsignedByte.ZERO)
          .pComputationBitMsnzb(false)
          .pComputationAccMsnzb(UnsignedByte.ZERO)
          .pComputationManzb(false)
          .pComputationManzbAcc(Bytes.of(0))
          .pMacroInstructionExpInst(Bytes.of(wghtSumMacro()))
          .pMacroInstructionData1(Bytes.of(0))
          .pMacroInstructionData2(Bytes.of(0))
          .pMacroInstructionData3(Bytes.of(0))
          .pMacroInstructionData4(Bytes.of(0))
          .pMacroInstructionData5(Bytes.of(0))
          .pPreprocessingWcpFlag(false)
          .pPreprocessingWcpArg1Hi(Bytes.of(0))
          .pPreprocessingWcpArg1Lo(Bytes.of(0))
          .pPreprocessingWcpArg2Hi(Bytes.of(0))
          .pPreprocessingWcpArg2Lo(Bytes.of(0))
          .pPreprocessingWcpInst(UnsignedByte.ZERO)
          .validateRow();
    }
  }
}
