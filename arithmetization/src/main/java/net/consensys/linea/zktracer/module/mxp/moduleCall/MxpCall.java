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

package net.consensys.linea.zktracer.module.mxp.moduleCall;

import lombok.Getter;
import lombok.Setter;
import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.TraceSubFragment;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import net.consensys.linea.zktracer.module.hub.state.State;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.opcode.OpCodeData;
import net.consensys.linea.zktracer.opcode.gas.BillingRate;
import net.consensys.linea.zktracer.types.EWord;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.evm.frame.MessageFrame;

public class MxpCall implements TraceSubFragment {

  public final Hub hub;

  /** The following properties will be filled in by MXP module * */
  /** - don't necessitate computation * */
  @Getter public OpCodeData opCodeData;

  @Getter public boolean deploys;
  @Getter public long memorySizeInWords;
  @Getter public EWord offset1 = EWord.ZERO;
  @Getter public EWord size1 = EWord.ZERO;
  @Getter public EWord offset2 = EWord.ZERO;
  @Getter public EWord size2 = EWord.ZERO;

  /** - filled after computation by the module */
  @Getter @Setter public boolean mayTriggerNontrivialMmuOperation;

  @Getter @Setter public boolean mxpx;

  /** mxpx is short of Memory eXPansion eXception */
  @Getter @Setter public long gasMxp;

  public MxpCall(Hub hub) {
    this.hub = hub;
    final MessageFrame frame = this.hub.messageFrame();
    // set opCodeData
    this.opCodeData = this.hub.opCodeData();
    // set deploys
    this.deploys =
        this.opCodeData.mnemonic() == OpCode.RETURN & this.hub.currentFrame().isDeployment();
    // set memorySizeInWords
    this.memorySizeInWords = this.hub.messageFrame().memoryWordSize();
    // set sizes and offsets
    final OpCode opCode = OpCode.of(frame.getCurrentOperation().getOpcode());
    switch (opCode) {
      case MSIZE -> {}
      case MLOAD -> {
        this.offset1 = EWord.of(frame.getStackItem(0));
      }
      case MSTORE -> {
        this.offset1 = EWord.of(frame.getStackItem(0));
        this.size1 = EWord.of(32);
      }
      case MSTORE8 -> {
        this.offset1 = EWord.of(frame.getStackItem(0));
        this.size1 = EWord.of(1);
      }
      case REVERT, RETURN, LOG0, LOG1, LOG2, LOG3, LOG4, SHA3 -> {
        this.offset1 = EWord.of(frame.getStackItem(0));
        this.size1 = EWord.of(frame.getStackItem(1));
      }
      case CALLDATACOPY, RETURNDATACOPY, CODECOPY -> {
        this.offset1 = EWord.of(frame.getStackItem(0));
        this.size1 = EWord.of(frame.getStackItem(2));
      }
      case EXTCODECOPY -> {
        this.offset1 = EWord.of(frame.getStackItem(1));
        this.size1 = EWord.of(frame.getStackItem(3));
      }
      case CREATE, CREATE2 -> {
        this.offset1 = EWord.of(frame.getStackItem(1));
        this.size1 = EWord.of(frame.getStackItem(2));
      }
      case MCOPY -> {
        this.offset1 = EWord.of(frame.getStackItem(0));
        this.offset2 = EWord.of(frame.getStackItem(1));
        this.size2 = EWord.of(frame.getStackItem(2));
      }
      case CALL, CALLCODE -> {
        this.offset1 = EWord.of(frame.getStackItem(3));
        this.size1 = EWord.of(frame.getStackItem(4));
        this.offset2 = EWord.of(frame.getStackItem(5));
        this.size2 = EWord.of(frame.getStackItem(6));
      }
      case DELEGATECALL, STATICCALL -> {
        this.offset1 = EWord.of(frame.getStackItem(2));
        this.size1 = EWord.of(frame.getStackItem(3));
        this.offset2 = EWord.of(frame.getStackItem(4));
        this.size2 = EWord.of(frame.getStackItem(5));
      }
      default -> throw new IllegalStateException("Unexpected value: " + opCode);
    }
  }

  static boolean getMemoryExpansionException(Hub hub) {
    return Exceptions.memoryExpansionException(hub.pch().exceptions());
  }

  public boolean getSize1NonZeroNoMxpx() {
    return !this.mxpx && !this.size1.isZero();
  }

  public boolean getSize2NonZeroNoMxpx() {
    return !this.mxpx && !this.size2.isZero();
  }

  public Bytes getCostBy(BillingRate billingRate) {
    return Bytes.of(
        getOpCodeData().billing().billingRate() == billingRate
            ? getOpCodeData().billing().perUnit().cost()
            : 0);
  }

  public Trace.Hub trace(Trace.Hub trace, State hubState) {
    hubState.incrementMxpStamp();
    return trace
        .pMiscMxpFlag(true)
        .pMiscMxpInst(this.opCodeData.value())
        .pMiscMxpDeploys(this.deploys)
        .pMiscMxpOffset1Hi(this.offset1.hi())
        .pMiscMxpOffset1Lo(this.offset1.lo())
        .pMiscMxpSize1Hi(this.size1.hi())
        .pMiscMxpSize1Lo(this.size1.lo())
        .pMiscMxpOffset2Hi(this.offset2.hi())
        .pMiscMxpOffset2Lo(this.offset2.lo())
        .pMiscMxpSize2Hi(this.size2.hi())
        .pMiscMxpSize2Lo(this.size2.lo())
        .pMiscMxpMtntop(this.mayTriggerNontrivialMmuOperation)
        .pMiscMxpSize1NonzeroNoMxpx(this.getSize1NonZeroNoMxpx())
        .pMiscMxpSize2NonzeroNoMxpx(this.getSize2NonZeroNoMxpx())
        .pMiscMxpMxpx(this.mxpx)
        .pMiscMxpWords(Bytes.ofUnsignedLong(this.memorySizeInWords))
        .pMiscMxpGasMxp(Bytes.ofUnsignedLong(this.gasMxp));
  }
}
