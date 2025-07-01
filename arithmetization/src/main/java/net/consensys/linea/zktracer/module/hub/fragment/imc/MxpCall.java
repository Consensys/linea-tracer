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

package net.consensys.linea.zktracer.module.hub.fragment.imc;

import static net.consensys.linea.zktracer.module.mxp.MxpUtils.*;

import lombok.Getter;
import lombok.Setter;
import net.consensys.linea.zktracer.Fork;
import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.TraceSubFragment;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import net.consensys.linea.zktracer.module.hub.state.State;
import net.consensys.linea.zktracer.module.mxp.moduleCall.CancunMxpCall;
import net.consensys.linea.zktracer.module.mxp.moduleCall.LondonMxpCall;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.opcode.OpCodeData;
import net.consensys.linea.zktracer.opcode.gas.BillingRate;
import net.consensys.linea.zktracer.types.EWord;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.evm.frame.MessageFrame;

/**
 * This is the parent class for all MXP Calls. The fork dependent classes extending this are located
 * in Mxp module (LondonMxpCall, CancunMxpCall, ...).
 */
public abstract class MxpCall implements TraceSubFragment {

  public final Hub hub;

  /** The following properties will be filled in by MXP module * */
  /** - don't necessitate computation * */
  @Getter public OpCodeData opCodeData;

  @Getter public boolean deploys;
  @Getter public long memorySizeInWords;
  @Getter public EWord offset1;
  @Getter public EWord size1;
  @Getter public EWord offset2;
  @Getter public EWord size2;

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
    EWord[] sizesAndOffsets = getSizesAndOffsets(frame);
    this.size1 = sizesAndOffsets[0] != null ? sizesAndOffsets[0] : EWord.ZERO;
    this.offset1 = sizesAndOffsets[1] != null ? sizesAndOffsets[1] : EWord.ZERO;
    this.size2 = sizesAndOffsets[2] != null ? sizesAndOffsets[2] : EWord.ZERO;
    this.offset2 = sizesAndOffsets[3] != null ? sizesAndOffsets[3] : EWord.ZERO;
  }

  public static MxpCall getMxpCallByFork(Fork fork, Hub hub) {
    switch (fork) {
      case LONDON, PARIS, SHANGHAI -> {
        return new LondonMxpCall(hub);
      }
      case CANCUN, PRAGUE -> {
        return new CancunMxpCall(hub);
      }
      default -> {
        throw new IllegalArgumentException("Unsupported fork: " + fork);
      }
    }
  }

  static boolean getMemoryExpansionException(Hub hub) {
    return Exceptions.memoryExpansionException(hub.pch().exceptions());
  }

  public void setMayTriggerNontrivialMmuOperationFromMxpx() {
    this.mayTriggerNontrivialMmuOperation = !this.size1.isZero() && !this.mxpx;
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

  // Method only filled for LondonMxpCall
  protected void traceMayTriggerNonTrivialMmuOperationFromMxpx(Trace.Hub trace) {}
  ;

  public Trace.Hub trace(Trace.Hub trace, State hubState) {
    hubState.incrementMxpStamp();
    traceMayTriggerNonTrivialMmuOperationFromMxpx(trace);
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
        .pMiscMxpSize1NonzeroNoMxpx(this.getSize1NonZeroNoMxpx())
        .pMiscMxpSize2NonzeroNoMxpx(this.getSize2NonZeroNoMxpx())
        .pMiscMxpMxpx(this.mxpx)
        .pMiscMxpWords(Bytes.ofUnsignedLong(this.memorySizeInWords))
        .pMiscMxpGasMxp(Bytes.ofUnsignedLong(this.gasMxp));
  }
}
