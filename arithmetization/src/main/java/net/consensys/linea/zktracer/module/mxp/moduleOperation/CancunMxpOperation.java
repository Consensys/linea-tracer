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

package net.consensys.linea.zktracer.module.mxp.moduleOperation;

import static net.consensys.linea.zktracer.module.mxp.MxpUtils.*;
import static net.consensys.linea.zktracer.types.Conversions.booleanToLong;

import lombok.Getter;
import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.hub.fragment.imc.MxpCall;
import net.consensys.linea.zktracer.module.mxp.moduleCall.CancunMxpCall;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;

@Getter
public class CancunMxpOperation extends LondonMxpOperation {

  private final int contextNumber;
  private final CancunMxpCall superMxpCall;

  /**
   * The operation can follow 5 scenarii depending on the opcode. Each scenario executes
   * computations and inherits from the previous scenario as computations are cumulative
   *
   * <p>MSize scenario - no computation
   *
   * <p>Trivial scenario - computes size1IsZero and size2IsZero
   *
   * <p>Mxpx scenario - computes size1IsZero and size2IsZero and mxpxExpression
   *
   * <p>State update with word pricing scenario - computes size1IsZero and size2IsZero and
   * mxpxExpression and state update (wordsNew,cMemNew) and extraGasCost for word pricing opcodes
   *
   * <p>State update with byte pricing scenario - computes size1IsZero and size2IsZero and
   * mxpxExpression and state update (wordsNew,cMemNew) and extraGasCost for byte pricing opcodes
   */
  public CancunMxpOperation(final MxpCall mxpCall, Wcp wcp, Euc euc) {
    super(mxpCall);

    // Setting of global variables
    this.contextNumber = this.mxpCall.hub.currentFrame().contextNumber();

    // We instantiate an extended MxpCall (CancunMxpCall) depending on the scenario
    // This super MxpCall does the computation and stores the values
    this.superMxpCall = this.mxpCall.getMxpScenario(wcp, euc);
  }

  private int nRowsComputation() {
    return superMxpCall.ctMax() + 1;
  }

  @Override
  protected int computeLineCount() {
    return nRowsComputation() + 3; // 3 for decoder, macro and scenario
  }

  @Override
  public final void trace(int stamp, Trace tr) {
    Trace.Mxpcan trace = tr.mxpcan;
    traceDecoder(++stamp, trace);
    traceMacro(stamp, trace);
    traceScenario(stamp, trace);
    traceComputation(stamp, trace);
  }

  final void traceDecoder(int stamp, Trace.Mxpcan trace) {
    OpCode opCode = superMxpCall.getOpCodeData().mnemonic();

    trace
        .mxpStamp(stamp)
        .cn(this.getContextNumber())
        .decoder(true)
        .pDecoderInst(UnsignedByte.of(opCode.byteValue()))
        .pDecoderIsMsize(opCode == OpCode.MSIZE)
        .pDecoderIsReturn(opCode == OpCode.RETURN)
        .pDecoderIsMcopy(opCode == OpCode.MCOPY)
        .pDecoderIsFixedSize32(opCode == OpCode.MLOAD || opCode == OpCode.MSTORE)
        .pDecoderIsFixedSize1(opCode == OpCode.MSTORE8)
        .pDecoderIsSingleMaxOffset(isSingleOffsetOpcode(opCode))
        .pDecoderIsDoubleMaxOffset(isDoubleOffsetOpcode(opCode))
        .pDecoderIsWordPricing(isWordPricingOpcode(opCode))
        .pDecoderIsBytePricing(isBytePricingOpcode(opCode))
        .pDecoderGword((UnsignedByte) superMxpCall.gWord)
        .pDecoderGbyte((UnsignedByte) superMxpCall.gByte)
        .fillAndValidateRow();
  }

  final void traceMacro(int stamp, Trace.Mxpcan trace) {
    OpCode opCode = superMxpCall.getOpCodeData().mnemonic();

    trace
        .mxpStamp(stamp)
        .cn(this.getContextNumber())
        .macro(true)
        .pMacroInst(UnsignedByte.of(opCode.byteValue()))
        .pMacroDeploying(superMxpCall.isDeploys())
        .pMacroOffset1Hi(superMxpCall.getOffset1().hi())
        .pMacroOffset1Lo(superMxpCall.getOffset1().lo())
        .pMacroSize1Hi(superMxpCall.getSize1().hi())
        .pMacroSize1Lo(superMxpCall.getSize1().lo())
        .pMacroOffset2Hi(superMxpCall.getOffset2().hi())
        .pMacroOffset2Lo(superMxpCall.getOffset2().lo())
        .pMacroSize2Hi(superMxpCall.getSize2().hi())
        .pMacroSize2Lo(superMxpCall.getSize2().lo())
        .pMacroRes(
            superMxpCall.isMSizeScenario() ? superMxpCall.getMemorySizeInWords() : 0L) // to do
        .pMacroMxpx(superMxpCall.isMxpx())
        .pMacroGasMxp(Bytes.ofUnsignedLong(superMxpCall.getGasMxp()))
        .pMacroMayTriggerMmu(superMxpCall.isMayTriggerNontrivialMmuOperation())
        .pMacroS1Nznomxpx(!superMxpCall.getSize1().isZero() && !superMxpCall.isMxpx())
        .pMacroS2Nznomxpx(!superMxpCall.getSize2().isZero() && !superMxpCall.isMxpx())
        .fillAndValidateRow();
  }

  final void traceScenario(int stamp, Trace.Mxpcan trace) {
    trace
        .mxpStamp(stamp)
        .cn(this.getContextNumber())
        .scenario(true)
        .pScenarioMsize(superMxpCall.isMSizeScenario())
        .pScenarioTrivial(superMxpCall.isTrivialScenario())
        .pScenarioMxpx(superMxpCall.isMxpxScenario())
        .pScenarioStateUpdateWordPricing(superMxpCall.isStateUpdtWPricingScenario())
        .pScenarioStateUpdateBytePricing(superMxpCall.isStateUpdtBPricingScenario())
        .pScenarioWords(superMxpCall.words)
        .pScenarioWordsNew(superMxpCall.wordsNew)
        .pScenarioCmem(Bytes.ofUnsignedLong(superMxpCall.cMem))
        .pScenarioCmemNew(Bytes.ofUnsignedLong(superMxpCall.cMemNew))
        .fillAndValidateRow();
  }

  final void traceComputation(int stamp, Trace.Mxpcan trace) {

    for (int i = 0; i < nRowsComputation(); i++) {
      trace
          .mxpStamp(stamp)
          .cn(this.getContextNumber())
          .computation(true)
          .ct(i)
          .ctMax(superMxpCall.ctMax())
          .pComputationWcpFlag(superMxpCall.exoCalls.get(i).wcpFlag())
          .pComputationEucFlag(superMxpCall.exoCalls.get(i).eucFlag())
          .pComputationExoInst(superMxpCall.exoCalls.get(i).instruction())
          .pComputationArg1Hi(superMxpCall.exoCalls.get(i).arg1Hi())
          .pComputationArg1Lo(superMxpCall.exoCalls.get(i).arg1Lo())
          .pComputationArg2Hi(superMxpCall.exoCalls.get(i).arg2Hi())
          .pComputationArg2Lo(superMxpCall.exoCalls.get(i).arg2Lo())
          .pComputationResA(booleanToLong(superMxpCall.exoCalls.get(i).resultA()))
          .pComputationResB(superMxpCall.exoCalls.get(i).resultB().toLong())
          .fillAndValidateRow();
    }
  }
}
