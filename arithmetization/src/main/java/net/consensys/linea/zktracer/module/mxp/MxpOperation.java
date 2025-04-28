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

package net.consensys.linea.zktracer.module.mxp;

import static net.consensys.linea.zktracer.Trace.GAS_CONST_G_MEMORY;
import static net.consensys.linea.zktracer.Trace.Mxp.*;
import static net.consensys.linea.zktracer.module.wcp.WcpCall.*;
import static org.hyperledger.besu.evm.internal.Words.clampedAdd;
import static org.hyperledger.besu.evm.internal.Words.clampedMultiply;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.imc.MxpCall;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.module.wcp.WcpCall;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.EWord;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.evm.frame.MessageFrame;

@Getter
public class MxpOperation extends ModuleOperation {

  @Getter final MxpCall mxpCall;
  private final int contextNumber;

  private BigInteger maxOffset1 = BigInteger.ZERO;
  private BigInteger maxOffset2 = BigInteger.ZERO;
  private BigInteger maxOffset = BigInteger.ZERO;

  private final long words;
  private long wordsNew;
  private final long cMem;
  private long cMemNew;

  private final Wcp wcp;
  private final Euc euc;
  private final List<WcpCall> wcpCalls;
  private final boolean[] wcpFlags;
  private final boolean[] eucFlags;

  // Todo list
  // - check hub justification
  // - bring mxp back and rename this mxp to mxp3
  // - take care of inst

  public MxpOperation(final MxpCall mxpCall, Wcp wcp, Euc euc) {
    final Hub hub = mxpCall.hub;
    this.wcp = wcp;
    this.euc = euc;
    final MessageFrame frame = hub.messageFrame();

    this.mxpCall = mxpCall;
    this.mxpCall.setOpCodeData(hub.opCodeData());
    this.mxpCall.setDeploys(
        mxpCall.getOpCodeData().mnemonic() == OpCode.RETURN & hub.currentFrame().isDeployment());
    this.mxpCall.setMemorySizeInWords(frame.memoryWordSize());
    this.words = frame.memoryWordSize();
    this.wordsNew = frame.memoryWordSize(); // will (may) be updated later
    this.cMem = memoryCost(frame.memoryWordSize());
    this.cMemNew = memoryCost(frame.memoryWordSize()); // will (may) be updated later
    this.contextNumber = hub.currentFrame().contextNumber();

    this.wcpCalls = new ArrayList<>(nRows());
    this.wcpFlags = new boolean[nRows()];
    this.eucFlags = new boolean[nRows()];

    setOffsetsAndSizes();
    computations();
  }

  @Override
  protected int computeLineCount() {
    return this.nRows();
  }

  private void setOffsetsAndSizes() {
    final MessageFrame frame = this.mxpCall.hub.messageFrame();
    final OpCode opCode = OpCode.of(frame.getCurrentOperation().getOpcode());
    switch (opCode) {
      case MSIZE -> {}
      case MLOAD, MSTORE -> {
        mxpCall.setOffset1(EWord.of(frame.getStackItem(0)));
        mxpCall.setSize1(EWord.of(32));
      }
      case MSTORE8 -> {
        mxpCall.setOffset1(EWord.of(frame.getStackItem(0)));
        mxpCall.setSize1(EWord.of(1));
      }
      case REVERT, RETURN, LOG0, LOG1, LOG2, LOG3, LOG4, SHA3 -> {
        mxpCall.setOffset1(EWord.of(frame.getStackItem(0)));
        mxpCall.setSize1(EWord.of(frame.getStackItem(1)));
      }
      case CALLDATACOPY, RETURNDATACOPY, CODECOPY -> {
        mxpCall.setOffset1(EWord.of(frame.getStackItem(0)));
        mxpCall.setSize1(EWord.of(frame.getStackItem(2)));
      }
      case EXTCODECOPY -> {
        mxpCall.setOffset1(EWord.of(frame.getStackItem(1)));
        mxpCall.setSize1(EWord.of(frame.getStackItem(3)));
      }
      case CREATE, CREATE2 -> {
        mxpCall.setOffset1(EWord.of(frame.getStackItem(1)));
        mxpCall.setSize1(EWord.of(frame.getStackItem(2)));
      }
      case MCOPY -> {
        mxpCall.setOffset1(EWord.of(frame.getStackItem(0)));
        mxpCall.setSize1(EWord.of(frame.getStackItem(2)));
        mxpCall.setOffset2(EWord.of(frame.getStackItem(1)));
        mxpCall.setSize2(EWord.of(frame.getStackItem(2)));
      }
      case CALL, CALLCODE -> {
        mxpCall.setOffset1(EWord.of(frame.getStackItem(3)));
        mxpCall.setSize1(EWord.of(frame.getStackItem(4)));
        mxpCall.setOffset2(EWord.of(frame.getStackItem(5)));
        mxpCall.setSize2(EWord.of(frame.getStackItem(6)));
      }
      case DELEGATECALL, STATICCALL -> {
        mxpCall.setOffset1(EWord.of(frame.getStackItem(2)));
        mxpCall.setSize1(EWord.of(frame.getStackItem(3)));
        mxpCall.setOffset2(EWord.of(frame.getStackItem(4)));
        mxpCall.setSize2(EWord.of(frame.getStackItem(5)));
      }
      default -> throw new IllegalStateException("Unexpected value: " + opCode);
    }
  }

  private void computations() {
    switch (getMxpScenario()) {
      case MSIZE -> {
        // no computations takes place for MSIZE scenario
        wcpFlags[1] = false;
        eucFlags[1] = false;
      }
      case TRIVIAL -> {
        handleTrivial();
      }
      case MXPX -> {
        handleMxpx();
      }
      case UPDT_W -> {
        handleUpdt();
      }
      case UPDT_B -> {}
    }
  }

  private void handleTrivial() {
    wcpFlags[1] = true;
    wcpFlags[2] = true;
    // testing zeroness of size parameters
    wcpCalls.add(1, isZeroCall(wcp, mxpCall.getOffset1()));
    wcpCalls.add(2, isZeroCall(wcp, mxpCall.getOffset2()));
  }

  private void handleMxpx() {
    wcpFlags[3] = true;
    wcpFlags[4] = true;
    wcpFlags[5] = true;
    wcpFlags[6] = true;
    // testing for small-ness of size and offset parameters
    wcpCalls.add(3, leqCall(wcp, mxpCall.getSize1(), Bytes.ofUnsignedLong(MXPX_THRESHOLD)));
    wcpCalls.add(4, leqCall(wcp, mxpCall.getSize2(), Bytes.ofUnsignedLong(MXPX_THRESHOLD)));
    wcpCalls.add(5, leqCall(wcp, mxpCall.getOffset1(), Bytes.ofUnsignedLong(MXPX_THRESHOLD)));
    wcpCalls.add(6, leqCall(wcp, mxpCall.getOffset2(), Bytes.ofUnsignedLong(MXPX_THRESHOLD)));
  }

  private void handleUpdt() {
    OpCode opCode = this.mxpCall.getOpCodeData().mnemonic();
    if (isDoubleOffsetOpcode(opCode)) {
      wcpFlags[7] = true;
      Bytes max1 =
          Bytes.fromHexString(
              mxpCall.getOffset1().lo().toString() + mxpCall.getSize1().lo().toString());
      Bytes max2 =
          Bytes.fromHexString(
              mxpCall.getOffset2().lo().toString() + mxpCall.getSize2().lo().toString());
      wcpCalls.add(7, ltCall(wcp, max1, max2));
      boolean useParams2 = wcpCalls.get(7).result();
      boolean useParams1 = !useParams2;
      Bytes maxOffset1;
      Bytes maxOffset2;
      eucFlags[8] = true;
    }
  }

  // This is a copy and past from FrontierGasCalculator.java
  private static long memoryCost(final long length) {
    final long lengthSquare = clampedMultiply(length, length);
    final long base =
        (lengthSquare == Long.MAX_VALUE)
            ? clampedMultiply(length / 512, length)
            : lengthSquare / 512;
    return clampedAdd(clampedMultiply(GAS_CONST_G_MEMORY, length), base);
  }

  protected enum MxpScenario {
    MSIZE,
    TRIVIAL,
    MXPX,
    UPDT_W,
    UPDT_B
  }

  private MxpScenario getMxpScenario() {
    OpCode opCode = this.mxpCall.getOpCodeData().mnemonic();
    if (opCode == OpCode.MSIZE) {
      return MxpScenario.MSIZE;
    }
    if (this.mxpCall.getSize1().isZero() && this.mxpCall.getSize2().isZero()) {
      return MxpScenario.TRIVIAL;
    }
    if (this.mxpCall.isMxpx()) {
      return MxpScenario.MXPX;
    }
    if (isWordPricingOpcode(opCode)) {
      return MxpScenario.UPDT_W;
    }
    return MxpScenario.UPDT_B;
  }

  public int ctMax() {
    return switch (this.getMxpScenario()) {
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
        .pDecoderGword(0)
        .pDecoderGbyte(0)
        .fillAndValidateRow();
  }

  final void traceMacro(int stamp, Trace.Mxp trace) {
    OpCode opCode = this.mxpCall.getOpCodeData().mnemonic();
    final EWord eOffset1 = EWord.of(this.mxpCall.getOffset1());
    final EWord eOffset2 = EWord.of(this.mxpCall.getOffset2());
    final EWord eSize1 = EWord.of(this.mxpCall.getSize1());
    final EWord eSize2 = EWord.of(this.mxpCall.getSize2());

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
        .pMacroDeploying(this.mxpCall.deploys)
        .pMacroOffset1Hi(eOffset1.hi())
        .pMacroOffset1Lo(eOffset1.lo())
        .pMacroSize1Hi(eSize1.hi())
        .pMacroSize1Lo(eSize1.lo())
        .pMacroOffset2Hi(eOffset2.hi())
        .pMacroOffset2Lo(eOffset2.lo())
        .pMacroSize2Hi(eSize2.hi())
        .pMacroSize2Lo(eSize2.lo())
        .pMacroRes(0) // to do
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
        .pScenarioMsize(getMxpScenario() == MxpScenario.MSIZE)
        .pScenarioTrivial(getMxpScenario() == MxpScenario.TRIVIAL)
        .pScenarioMxpx(getMxpScenario() == MxpScenario.MXPX)
        .pScenarioStateUpdateBytePricing(getMxpScenario() == MxpScenario.UPDT_B)
        .pScenarioStateUpdateBytePricing(getMxpScenario() == MxpScenario.UPDT_W)
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
          .computation(false)
          .ct(0)
          .ctMax(0)
          .pComputationWcpFlag(false)
          .pComputationEucFlag(false)
          .pComputationExoInst(0)
          .pComputationArg1Hi(Bytes.EMPTY)
          .pComputationArg1Lo(Bytes.EMPTY)
          .pComputationArg2Hi(Bytes.EMPTY)
          .pComputationArg2Lo(Bytes.EMPTY)
          .pComputationResA(0)
          .pComputationResB(0)
          .fillAndValidateRow();
    }
  }

  private boolean isSingleOffsetOpcode(OpCode opCode) {
    return opCode == OpCode.MLOAD
        || opCode == OpCode.MSTORE
        || opCode == OpCode.MSTORE8
        || opCode == OpCode.REVERT
        || opCode == OpCode.RETURN
        || opCode.isLog()
        || opCode == OpCode.SHA3
        || opCode.isCopy()
        || opCode.isCreate();
  }

  ///  Helpers ///
  private boolean isDoubleOffsetOpcode(OpCode opCode) {
    return opCode == OpCode.MCOPY || opCode.isCall();
  }

  private boolean isWordPricingOpcode(OpCode opCode) {
    return opCode.isLog()
        || opCode == OpCode.SHA3
        || opCode.isCopy()
        || opCode.isCreate()
        || opCode == OpCode.MCOPY;
  }

  private boolean isBytePricingOpcode(OpCode opCode) {
    return opCode == OpCode.MLOAD
        || opCode == OpCode.MSTORE
        || opCode == OpCode.MSTORE8
        || opCode == OpCode.REVERT
        || opCode == OpCode.RETURN
        || opCode.isCall();
  }
}
