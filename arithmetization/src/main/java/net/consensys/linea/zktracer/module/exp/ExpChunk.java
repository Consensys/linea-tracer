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
import static java.lang.Math.max;
import static java.lang.Math.min;
import static net.consensys.linea.zktracer.module.exp.Trace.EXP_EXPLOG;
import static net.consensys.linea.zktracer.module.exp.Trace.EXP_MODEXPLOG;
import static net.consensys.linea.zktracer.module.exp.Trace.MAX_CT_CMPTN_EXP_LOG;
import static net.consensys.linea.zktracer.module.exp.Trace.MAX_CT_CMPTN_MODEXP_LOG;
import static net.consensys.linea.zktracer.module.exp.Trace.MAX_CT_MACRO_EXP_LOG;
import static net.consensys.linea.zktracer.module.exp.Trace.MAX_CT_MACRO_MODEXP_LOG;
import static net.consensys.linea.zktracer.module.exp.Trace.MAX_CT_PRPRC_EXP_LOG;
import static net.consensys.linea.zktracer.module.exp.Trace.MAX_CT_PRPRC_MODEXP_LOG;
import static net.consensys.linea.zktracer.types.Conversions.booleanToInt;

import java.math.BigInteger;
import java.math.RoundingMode;

import lombok.Getter;
import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.EWord;
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
    populateColumns(frame);
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

  private void populateColumns(final MessageFrame frame) {
    // TODO
    if (isExpLog) {
      BigInteger exponentHi = EWord.of(frame.getStackItem(1)).hiBigInt();
      BigInteger exponentLo = EWord.of(frame.getStackItem(1)).loBigInt();
      BigInteger dynamicCost;
    } else if (isModexpLog) {
      OpCode opCode = OpCode.of(frame.getCurrentOperation().getOpcode());
      // DELEGATECALL, STATICCALL cases
      int argsOffset = 2;
      int cdsIndex = 3;
      // CALL, CALLCODE cases
      if (opCode == OpCode.CALL || opCode == OpCode.CALLCODE) {
        argsOffset = 3;
        cdsIndex = 4;
      }
      BigInteger cds = EWord.of(frame.getStackItem(cdsIndex)).toUnsignedBigInteger();
      // Note that this check will disappear since it will be the MXP module taking care of it
      if (cds.compareTo(EWord.of(frame.getStackItem(cdsIndex)).loBigInt()) > 0) {
        throw new IllegalArgumentException("cds hi part is non-zero");
      }

      Bytes unpaddedCallData = frame.shadowReadMemory(argsOffset, cds.longValue());
      // pad unpaddedCallData to 96
      Bytes paddedCallData =
          cds.intValue() < 96
              ? Bytes.concatenate(unpaddedCallData, Bytes.repeat((byte) 0, 96 - cds.intValue()))
              : unpaddedCallData;

      BigInteger bbs = paddedCallData.slice(0, 32).toUnsignedBigInteger();
      BigInteger ebs = paddedCallData.slice(32, 64).toUnsignedBigInteger();

      // Check if bbs and ebs are <= 512
      if (bbs.compareTo(BigInteger.valueOf(512)) > 0
          || ebs.compareTo(BigInteger.valueOf(512)) > 0) {
        throw new IllegalArgumentException("byte sizes are too big");
      }

      // cdsCutoff
      int cdsCutoff = min(max(cds.intValue() - (96 + ebs.intValue()), 0), 32);
      // ebsCutoff?
      int ebsCutoff = min(ebs.intValue(), 32);

      // pad paddedCallData to 96 + bbs + 32
      Bytes rawLead =
          cds.intValue() < 96 + bbs.intValue() + 32
              ? Bytes.concatenate(
                  paddedCallData, Bytes.repeat((byte) 0, 96 + bbs.intValue() + 32 - cds.intValue()))
              : paddedCallData;
      // ...

      // pad paddedCallData to 96 + bbs + ebs
      Bytes doublePaddedCallData =
          cds.intValue() < 96 + bbs.intValue() + ebs.intValue()
              ? Bytes.concatenate(
                  paddedCallData,
                  Bytes.repeat((byte) 0, 96 + bbs.intValue() + ebs.intValue() - cds.intValue()))
              : paddedCallData;

      // lead
      BigInteger lead =
          doublePaddedCallData
              .slice(96 + bbs.intValue(), min(ebs.intValue(), 32))
              .toUnsignedBigInteger();

      // leadLog
      BigInteger leadLog;
      if (ebs.intValue() <= 32 && lead.signum() == 0) {
        leadLog = BigInteger.ZERO;
      } else if (ebs.intValue() <= 32 && lead.signum() != 0) {
        leadLog = BigInteger.valueOf(log2(lead, RoundingMode.FLOOR));
      } else if (ebs.intValue() > 32 && lead.signum() != 0) {
        leadLog =
            BigInteger.valueOf(8)
                .multiply(ebs.subtract(BigInteger.valueOf(32)))
                .add(BigInteger.valueOf(log2(lead, RoundingMode.FLOOR)));
      } else {
        leadLog = BigInteger.valueOf(8).multiply(ebs.subtract(BigInteger.valueOf(32)));
      }
    }
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
