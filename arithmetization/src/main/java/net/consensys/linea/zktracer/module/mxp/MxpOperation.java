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

import static com.google.common.base.Preconditions.*;
import static net.consensys.linea.zktracer.Trace.GAS_CONST_G_MEMORY;
import static net.consensys.linea.zktracer.Trace.Mxp.CT_MAX_MSIZE;
import static net.consensys.linea.zktracer.Trace.Mxp.CT_MAX_MXPX;
import static net.consensys.linea.zktracer.Trace.Mxp.CT_MAX_TRIV;
import static net.consensys.linea.zktracer.Trace.Mxp.CT_MAX_UPDT_B;
import static net.consensys.linea.zktracer.Trace.Mxp.CT_MAX_UPDT_W;
import static org.hyperledger.besu.evm.internal.Words.clampedAdd;
import static org.hyperledger.besu.evm.internal.Words.clampedMultiply;

import java.math.BigInteger;

import lombok.Getter;
import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.imc.MxpCall;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.EWord;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.evm.frame.MessageFrame;

@Getter
public class MxpOperation extends ModuleOperation {
  public static final BigInteger TWO_POW_128 = BigInteger.ONE.shiftLeft(128);
  public static final BigInteger TWO_POW_32 = BigInteger.ONE.shiftLeft(32);

  @Getter final MxpCall mxpCall;
  private final int contextNumber;

  private BigInteger maxOffset1 = BigInteger.ZERO;
  private BigInteger maxOffset2 = BigInteger.ZERO;
  private BigInteger maxOffset = BigInteger.ZERO;

  @Getter private boolean roob;
  @Getter private boolean noOperation;
  private long wordsNew;
  private final long cMem;
  private long cMemNew;
  private long quadCost = 0;
  private long linCost = 0;

  public MxpOperation(final MxpCall mxpCall) {
    final Hub hub = mxpCall.hub;
    final MessageFrame frame = hub.messageFrame();

    this.mxpCall = mxpCall;
    this.mxpCall.setOpCodeData(hub.opCodeData());
    this.mxpCall.setDeploys(
        mxpCall.getOpCodeData().mnemonic() == OpCode.RETURN & hub.currentFrame().isDeployment());
    this.mxpCall.setMemorySizeInWords(frame.memoryWordSize());
    this.wordsNew = frame.memoryWordSize(); // will (may) be updated later
    this.cMem = memoryCost(frame.memoryWordSize());
    this.cMemNew = memoryCost(frame.memoryWordSize()); // will (may) be updated later
    this.contextNumber = hub.currentFrame().contextNumber();

    setOffsetsAndSizes();
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

  // This is a copy and past from FrontierGasCalculator.java
  private static long memoryCost(final long length) {
    final long lengthSquare = clampedMultiply(length, length);
    final long base =
        (lengthSquare == Long.MAX_VALUE)
            ? clampedMultiply(length / 512, length)
            : lengthSquare / 512;
    return clampedAdd(clampedMultiply(GAS_CONST_G_MEMORY, length), base);
  }

  protected enum MxpExecutionPath {
    MSIZE,
    TRIVIAL,
    MXPX,
    UPDT_W,
    UPDT_B
  }

  private MxpExecutionPath getMxpExecutionPath() {
    // TODO
    return MxpExecutionPath.MSIZE;
  }

  public int ctMax() {
    return switch (this.getMxpExecutionPath()) {
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

  final void trace(int stamp, Trace.Mxp trace) {
    final EWord eOffset1 = EWord.of(this.mxpCall.getOffset1());
    final EWord eOffset2 = EWord.of(this.mxpCall.getOffset2());
    final EWord eSize1 = EWord.of(this.mxpCall.getSize1());
    final EWord eSize2 = EWord.of(this.mxpCall.getSize2());

    final int nRows = this.nRows();

    for (int i = 0; i < nRows; i++) {
      trace
          .mxpStamp(stamp)
          .cn(this.getContextNumber())
          // TODO: fill and split
          .decoder(false)
          .macro(false)
          .scenario(false)
          .computation(false)
          .ct(0)
          .ctMax(0)
          .pDecoderInst(0)
          .pDecoderIsMsize(false)
          .pDecoderIsReturn(false)
          .pDecoderIsMcopy(false)
          .pDecoderIsFixedSize32(false)
          .pDecoderIsFixedSize1(false)
          .pDecoderIsSingleMaxOffset(false)
          .pDecoderIsDoubleMaxOffset(false)
          .pDecoderIsWordPricing(false)
          .pDecoderIsBytePricing(false)
          .pDecoderGword(0)
          .pDecoderGbyte(0)
          .pMacroInst(0)
          .pMacroDeploying(false)
          .pMacroOffset1Hi(eOffset1.hi())
          .pMacroOffset1Lo(eOffset1.lo())
          .pMacroSize1Hi(eSize1.hi())
          .pMacroSize1Lo(eSize1.lo())
          .pMacroOffset2Hi(eOffset2.hi())
          .pMacroOffset2Lo(eOffset2.lo())
          .pMacroSize2Hi(eSize2.hi())
          .pMacroSize2Lo(eSize2.lo())
          .pMacroRes(0)
          .pMacroMxpx(false)
          .pMacroGasMxp(Bytes.EMPTY)
          .pMacroMayTriggerMmu(false)
          .pMacroS1Nznomxpx(false)
          .pMacroS2Nznomxpx(false)
          .pScenarioMsize(false)
          .pScenarioTrivial(false)
          .pScenarioMxpx(false)
          .pScenarioStateUpdateBytePricing(false)
          .pScenarioStateUpdateBytePricing(false)
          .pScenarioWords(0)
          .pScenarioWordsNew(0)
          .pScenarioCmem(Bytes.EMPTY)
          .pScenarioCmemNew(Bytes.EMPTY)
          .validateRow();
    }
  }
}
