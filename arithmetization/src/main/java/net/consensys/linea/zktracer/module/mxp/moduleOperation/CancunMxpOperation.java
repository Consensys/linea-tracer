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

import lombok.Getter;
import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.hub.fragment.imc.MxpCall;
import net.consensys.linea.zktracer.module.mxp.moduleScenario.*;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.opcode.gas.BillingRate;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;

@Getter
public class CancunMxpOperation extends LondonMxpOperation {

  private final int contextNumber;
  private final long words;
  private long wordsNew;
  private final long cMem;
  private long cMemNew;
  private final Bytes gWord;
  private final Bytes gByte;
  private final MxpScenario scenario;

  /**
   * The operation can follow 5 scenarii depending on the opcode. Each scenario executes
   * computations and inherits from the previous one as computations are cumulative
   *
   * <p>MSize scenario - no computation
   *
   * <p>Trivial scenario - computes size1IsZero and size2IsZero
   *
   * <p>Mxpx scenario - computes size1IsZero and size2IsZero and mxpxExpression
   *
   * <p>State update with word pricing scenario - computes size1IsZero and size2IsZero and
   * mxpxExpression and extraGasCost for word pricing opcodes
   *
   * <p>State update with byte pricing scenario - computes size1IsZero and size2IsZero and
   * mxpxExpression and extraGasCost for byte pricing opcodes
   */
  public CancunMxpOperation(final MxpCall mxpCall, Wcp wcp, Euc euc) {
    super(mxpCall);

    // Setting of global variables
    this.contextNumber = this.mxpCall.hub.currentFrame().contextNumber();
    this.gWord = this.mxpCall.getCostBy(BillingRate.BY_WORD);
    this.gByte = this.mxpCall.getCostBy(BillingRate.BY_BYTE);

    // Initialization of state variables
    this.words = this.mxpCall.getMemorySizeInWords();
    this.wordsNew = this.mxpCall.getMemorySizeInWords(); // will (may) be updated later
    this.cMem = memoryCost(this.mxpCall.getMemorySizeInWords());
    this.cMemNew = memoryCost(this.mxpCall.getMemorySizeInWords()); // will (may) be updated later

    // Snapshot properties from the hub in mxpCall properties
    this.mxpCall.fillNoComputationMxpProperties();

    // We do the computation depending on the scenario
    this.scenario = MxpScenario.getMxpScenario(this.mxpCall);
    scenario.compute(mxpCall, wcp, euc);

    // After computation
    // We update the mxpCall properties and state variables accordingly
    this.mxpCall.setGasMxp(0L);
    this.mxpCall.setMxpx(scenario.getMxpxExpression() != 0);
    this.mxpCall.setMayTriggerNontrivialMmuOperation(
        !this.mxpCall.getSize1().isZero() && !this.mxpCall.isMxpx());
    if (scenario.isStateUpdate()) {
      this.wordsNew = scenario.getWordsNew();
      this.cMemNew = scenario.getCMemNew();
      // if state has changed, an extra gas cost is incurred
      mxpCall.setGasMxp(this.cMemNew - this.cMem + scenario.getExtraGasCost());
    }
  }

  public int nRows() {
    return scenario.ctMax() + 1;
  }

  @Override
  protected int computeLineCount() {
    return this.nRows();
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
    OpCode opCode = this.mxpCall.getOpCodeData().mnemonic();

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
        .pDecoderGword((UnsignedByte) this.gWord)
        .pDecoderGbyte((UnsignedByte) this.gByte)
        .fillAndValidateRow();
  }

  final void traceMacro(int stamp, Trace.Mxpcan trace) {
    OpCode opCode = this.mxpCall.getOpCodeData().mnemonic();

    trace
        .mxpStamp(stamp)
        .cn(this.getContextNumber())
        .macro(true)
        .pMacroInst(UnsignedByte.of(opCode.byteValue()))
        .pMacroDeploying(this.mxpCall.isDeploys())
        .pMacroOffset1Hi(this.mxpCall.getOffset1().hi())
        .pMacroOffset1Lo(this.mxpCall.getOffset1().lo())
        .pMacroSize1Hi(this.mxpCall.getSize1().hi())
        .pMacroSize1Lo(this.mxpCall.getSize1().lo())
        .pMacroOffset2Hi(this.mxpCall.getOffset2().hi())
        .pMacroOffset2Lo(this.mxpCall.getOffset2().lo())
        .pMacroSize2Hi(this.mxpCall.getSize2().hi())
        .pMacroSize2Lo(this.mxpCall.getSize2().lo())
        .pMacroRes((opCode == OpCode.MSIZE) ? this.mxpCall.getMemorySizeInWords() : 0L) // to do
        .pMacroMxpx(this.mxpCall.isMxpx())
        .pMacroGasMxp(Bytes.ofUnsignedLong(this.mxpCall.getGasMxp()))
        .pMacroMayTriggerMmu(this.mxpCall.isMayTriggerNontrivialMmuOperation())
        .pMacroS1Nznomxpx(!this.mxpCall.getSize1().isZero() && !this.mxpCall.isMxpx())
        .pMacroS2Nznomxpx(!this.mxpCall.getSize2().isZero() && !this.mxpCall.isMxpx())
        .fillAndValidateRow();
  }

  final void traceScenario(int stamp, Trace.Mxpcan trace) {
    trace
        .mxpStamp(stamp)
        .cn(this.getContextNumber())
        .scenario(true)
        .pScenarioMsize(scenario.isMSizeScenario())
        .pScenarioTrivial(scenario.isTrivialScenario())
        .pScenarioMxpx(scenario.isMxpxScenario())
        .pScenarioStateUpdateWordPricing(scenario.isStateUpdtWPricingScenario())
        .pScenarioStateUpdateBytePricing(scenario.isStateUpdtBPricingScenario())
        .pScenarioWords(this.words)
        .pScenarioWordsNew(this.wordsNew)
        .pScenarioCmem(Bytes.ofUnsignedLong(this.cMem))
        .pScenarioCmemNew(Bytes.ofUnsignedLong(this.cMemNew))
        .fillAndValidateRow();
  }

  final void traceComputation(int stamp, Trace.Mxpcan trace) {
    final int nRows = this.nRows();

    for (int i = 0; i < nRows; i++) {
      trace
          .mxpStamp(stamp)
          .cn(this.getContextNumber())
          .computation(true)
          .ct(i)
          .ctMax(scenario.ctMax())
          .pComputationWcpFlag(scenario.exoCalls.get(i).wcpFlag())
          .pComputationEucFlag(scenario.exoCalls.get(i).eucFlag())
          .pComputationExoInst(scenario.exoCalls.get(i).instruction())
          .pComputationArg1Hi(scenario.exoCalls.get(i).arg1Hi())
          .pComputationArg1Lo(scenario.exoCalls.get(i).arg1Lo())
          .pComputationArg2Hi(scenario.exoCalls.get(i).arg2Hi())
          .pComputationArg2Lo(scenario.exoCalls.get(i).arg2Lo())
          .pComputationResA(scenario.exoCalls.get(i).resultA().toLong())
          .pComputationResB(scenario.exoCalls.get(i).resultB().toLong())
          .fillAndValidateRow();
    }
  }
}
