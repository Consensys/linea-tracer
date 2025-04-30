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

package net.consensys.linea.zktracer.module.mxpv3;

import static net.consensys.linea.zktracer.Trace.Mxp.*;
import static net.consensys.linea.zktracer.module.mxpv3.MxpScenario.*;
import static net.consensys.linea.zktracer.module.mxpv3.MxpUtils.*;
import static net.consensys.linea.zktracer.types.Conversions.*;

import lombok.Getter;
import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.hub.fragment.imc.MxpCall;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.opcode.gas.BillingRate;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;

@Getter
public class MxpOperationV3 extends ModuleOperation {

  // Todo list
  // - check hub justification
  // - bring mxp back and rename this mxp to mxp3
  // - take care of inst
  // - how to deal with Mxp imports in tests ?
  // - check if ok to removed filled fields from MxpCall
  // - fix constraints in existing Mxp
  // - review utils methods if needed in types
  // - check tests commenting of roob

  @Getter final MxpCall mxpCall;
  private final MxpComputation mxpComputation;

  private final Wcp wcp;
  private final Euc euc;

  private final int contextNumber;
  private final long words;
  private long wordsNew;
  private final long cMem;
  private long cMemNew;
  private final Bytes gWord;
  private final Bytes gByte;

  public MxpOperationV3(final MxpCall mxpCall, Wcp wcp, Euc euc) {
    this.wcp = wcp;
    this.euc = euc;
    this.mxpCall = mxpCall;
    this.mxpComputation = new MxpComputation(wcp, euc, nRows());

    this.contextNumber = this.mxpCall.hub.currentFrame().contextNumber();
    // State variables
    this.words = this.mxpCall.getMemorySizeInWords();
    this.wordsNew = this.mxpCall.getMemorySizeInWords(); // will (may) be updated later
    this.cMem = memoryCost(this.mxpCall.getMemorySizeInWords());
    this.cMemNew = memoryCost(this.mxpCall.getMemorySizeInWords()); // will (may) be updated later

    this.gWord = this.mxpCall.getCostBy(BillingRate.BY_WORD);
    this.gByte = this.mxpCall.getCostBy(BillingRate.BY_BYTE);

    computationsAndUpdates();
  }

  public int ctMax() {
    return switch (getMxpScenario(this.mxpCall)) {
      case MSIZE -> CT_MAX_MSIZE;
      case TRIVIAL -> CT_MAX_TRIV;
      case MXPX -> CT_MAX_MXPX;
      case UPDT_W -> CT_MAX_UPDT_W;
      case UPDT_B -> CT_MAX_UPDT_B;
    };
  }

  public int nRows() {
    return ctMax() + 1;
  }

  @Override
  protected int computeLineCount() {
    return this.nRows();
  }

  private void computationsAndUpdates() {
    var scenario = getMxpScenario(this.mxpCall);
    if (scenario == MSIZE) {
      mxpComputation.computeForMSize();
      // And we set the following to understand MSize scenario vs keeping implicit default values
      mxpCall.setMxpx(false);
      mxpCall.setMayTriggerNontrivialMmuOperation(false);
      // No state update
      mxpCall.setGasMxp(0L);
    } else {
      mxpComputation.computeForNotMSize(this.mxpCall);
      if (scenario == TRIVIAL) {
        // No state update
        mxpCall.setGasMxp(0L);
      } else {
        int mxpxExpression = mxpComputation.computeForNotMSizeNorTrivial(this.mxpCall);
        mxpCall.setMxpx(mxpxExpression != 0);
        mxpCall.setMayTriggerNontrivialMmuOperation(
            !this.mxpCall.getSize1().isZero() && !this.mxpCall.isMxpx());
        if (scenario == MXPX) {
          // No state update
          mxpCall.setGasMxp(0L);
        } else {
          // State update
          var stateUpdate = mxpComputation.computeForUpdt(this.mxpCall, this.words, this.cMem);
          var wordsNewUpdate = stateUpdate[0];
          var cMemNewUpdate = stateUpdate[1];
          this.wordsNew = wordsNewUpdate;
          this.cMemNew = cMemNewUpdate;

          if (scenario == UPDT_W) {
            long extraWordCost = mxpComputation.computeForUpdtW(this.mxpCall, this.gWord);
            mxpCall.setGasMxp(this.cMemNew - this.cMem + extraWordCost);
          } else {
            long extraByteCost = mxpComputation.computeForUpdtB(this.mxpCall, this.gByte);
            mxpCall.setGasMxp(this.cMemNew - this.cMem + extraByteCost);
          }
        }
      }
    }
  }

  final void traceDecoder(int stamp, Trace.Mxp trace) {
    OpCode opCode = this.mxpCall.getOpCodeData().mnemonic();

    // Remove the nRows, there is one row ?
    trace
        .mxpStamp(stamp)
        .cn(this.getContextNumber())
        .decoder(true)
        .macro(false)
        .scenario(false)
        .computation(false)
        .ct(0) // to change ?
        .ctMax(0)
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

  final void traceMacro(int stamp, Trace.Mxp trace) {
    OpCode opCode = this.mxpCall.getOpCodeData().mnemonic();

    trace
        .mxpStamp(stamp)
        .cn(this.getContextNumber())
        .decoder(false)
        .macro(true)
        .scenario(false)
        .computation(false)
        .ct(0) // to change ?
        .ctMax(0)
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
        .pMacroRes((opCode == OpCode.MSIZE) ? mxpCall.getMemorySizeInWords() : 0L) // to do
        .pMacroMxpx(this.mxpCall.isMxpx())
        .pMacroGasMxp(Bytes.ofUnsignedLong(this.mxpCall.getGasMxp()))
        .pMacroMayTriggerMmu(this.mxpCall.isMayTriggerNontrivialMmuOperation())
        .pMacroS1Nznomxpx(!this.mxpCall.getSize1().isZero() && !this.mxpCall.isMxpx())
        .pMacroS2Nznomxpx(!this.mxpCall.getSize2().isZero() && !this.mxpCall.isMxpx())
        .fillAndValidateRow();
  }

  final void traceScenario(int stamp, Trace.Mxp trace) {
    trace
        .mxpStamp(stamp)
        .cn(this.getContextNumber())
        .decoder(false)
        .macro(false)
        .scenario(true)
        .computation(false)
        .ct(0)
        .ctMax(0)
        .pScenarioMsize(getMxpScenario(this.mxpCall) == MSIZE)
        .pScenarioTrivial(getMxpScenario(this.mxpCall) == TRIVIAL)
        .pScenarioMxpx(getMxpScenario(this.mxpCall) == MxpScenario.MXPX)
        .pScenarioStateUpdateBytePricing(getMxpScenario(this.mxpCall) == MxpScenario.UPDT_B)
        .pScenarioStateUpdateBytePricing(getMxpScenario(this.mxpCall) == MxpScenario.UPDT_W)
        .pScenarioWords(this.words)
        .pScenarioWordsNew(this.wordsNew)
        .pScenarioCmem(Bytes.ofUnsignedLong(this.cMem))
        .pScenarioCmemNew(Bytes.ofUnsignedLong(this.cMemNew))
        .fillAndValidateRow();
  }

  final void traceComputation(int stamp, Trace.Mxp trace) {
    final int nRows = this.nRows();

    for (int i = 0; i < nRows; i++) {
      trace
          .mxpStamp(stamp)
          .cn(this.getContextNumber())
          .decoder(false)
          .macro(false)
          .scenario(false)
          .computation(true)
          .ct(i)
          .ctMax(ctMax())
          .pComputationWcpFlag(this.mxpComputation.wcpFlags[i])
          .pComputationEucFlag(this.mxpComputation.eucFlags[i])
          .pComputationExoInst(
              this.mxpComputation.wcpFlags[i]
                  ? this.mxpComputation.wcpCalls.get(i).instruction()
                  : this.mxpComputation.eucCalls.get(i).instruction())
          .pComputationArg1Hi(
              this.mxpComputation.wcpFlags[i]
                  ? this.mxpComputation.wcpCalls.get(i).arg1Hi()
                  : this.mxpComputation.eucCalls.get(i).arg1Hi())
          .pComputationArg1Lo(
              this.mxpComputation.wcpFlags[i]
                  ? this.mxpComputation.wcpCalls.get(i).arg1Lo()
                  : this.mxpComputation.eucCalls.get(i).arg1Lo())
          .pComputationArg2Hi(
              this.mxpComputation.wcpFlags[i]
                  ? this.mxpComputation.wcpCalls.get(i).arg2Hi()
                  : this.mxpComputation.eucCalls.get(i).arg2Hi())
          .pComputationArg2Lo(
              this.mxpComputation.wcpFlags[i]
                  ? this.mxpComputation.wcpCalls.get(i).arg2Lo()
                  : this.mxpComputation.eucCalls.get(i).arg2Lo())
          .pComputationResA(booleanToLong(this.mxpComputation.wcpCalls.get(i).result()))
          .pComputationResB(this.mxpComputation.eucCalls.get(i).result().toLong())
          .fillAndValidateRow();
    }
  }
}
