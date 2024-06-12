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

package net.consensys.linea.zktracer.module.hub.fragment.imc.call;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.Trace;
import net.consensys.linea.zktracer.module.hub.fragment.TraceSubFragment;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.EWord;
import org.apache.tuweni.bytes.Bytes;

@RequiredArgsConstructor
public class MxpCall implements TraceSubFragment {

  final Hub hub;
  final OpCode opCode;
  final boolean deploys;
  final EWord offset1;
  final EWord size1;
  final EWord offset2;
  final EWord size2;
  final boolean memoryExpansionException;
  final long memorySizeInWords;
  final long gasMxp;

  public static MxpCall build(Hub hub) {

    // TODO: call the MXP here
    // TODO: get from Mxp all the following
    // TODO: check hub mxpx == mxp mxpx
    EWord offset1 = EWord.ZERO;
    EWord offset2 = EWord.ZERO;
    EWord size1 = EWord.ZERO;
    EWord size2 = EWord.ZERO;

    long gasMxp = 0;

    return new MxpCall(
        hub,
        hub.opCode(),
        getDeploys(hub),
        offset1,
        size1,
        offset2,
        size2,
        getMemoryExpansionException(hub),
        gasMxp,
        hub.messageFrame().memoryWordSize());
  }

  public MxpCall mxpCallType1(Hub hub) {
    Preconditions.checkArgument(hub.opCode().equals(OpCode.MSIZE));
    return new MxpCall(
        hub,
        getOpcode(hub),
        getDeploys(hub),
        EWord.ZERO,
        EWord.ZERO,
        EWord.ZERO,
        EWord.ZERO,
        getMemoryExpansionException(hub),
        hub.messageFrame().memoryWordSize(),
        0);
  }

  public MxpCall mxpCallType2(Hub hub, EWord offset1) {
    return new MxpCall(
        hub,
        getOpcode(hub),
        getDeploys(hub),
        EWord.ZERO,
        EWord.ZERO,
        EWord.ZERO,
        EWord.ZERO,
        getMemoryExpansionException(hub),
        hub.messageFrame().memoryWordSize(),
        0);
  }

  public MxpCall mxpCallType3(Hub hub) {
    return new MxpCall(
        hub,
        getOpcode(hub),
        getDeploys(hub),
        EWord.ZERO,
        EWord.ZERO,
        EWord.ZERO,
        EWord.ZERO,
        getMemoryExpansionException(hub),
        hub.messageFrame().memoryWordSize(),
        0);
  }

  public MxpCall mxpCallType4(Hub hub) {
    return new MxpCall(
        hub,
        getOpcode(hub),
        getDeploys(hub),
        EWord.ZERO,
        EWord.ZERO,
        EWord.ZERO,
        EWord.ZERO,
        getMemoryExpansionException(hub),
        hub.messageFrame().memoryWordSize(),
        0);
  }

  public MxpCall mxpCallType5(Hub hub) {
    return new MxpCall(
        hub,
        getOpcode(hub),
        getDeploys(hub),
        EWord.ZERO,
        EWord.ZERO,
        EWord.ZERO,
        EWord.ZERO,
        getMemoryExpansionException(hub),
        hub.messageFrame().memoryWordSize(),
        0);
  }

  static OpCode getOpcode(Hub hub) {
    return hub.opCode();
  }

  static boolean getDeploys(Hub hub) {
    return getOpcode(hub) == OpCode.RETURN && hub.currentFrame().isDeployment();
  }

  static boolean getMemoryExpansionException(Hub hub) {
    return hub.pch().exceptions().memoryExpansionException();
  }

  final boolean type4InstructionMayTriggerNonTrivialOperation(Hub hub) {
    return !getMemoryExpansionException(hub) && !this.size1.isZero();
  }

  @Override
  public Trace trace(Trace trace) {
    return trace
        .pMiscMxpFlag(true)
        .pMiscMxpInst(this.opCode.byteValue())
        .pMiscMxpDeploys(this.deploys)
        .pMiscMxpOffset1Hi(this.offset1.hi())
        .pMiscMxpOffset1Lo(this.offset1.lo())
        .pMiscMxpSize1Hi(this.size1.hi())
        .pMiscMxpSize1Lo(this.size1.lo())
        .pMiscMxpOffset2Hi(this.offset2.hi())
        .pMiscMxpOffset2Lo(this.offset2.lo())
        .pMiscMxpSize2Hi(this.size2.hi())
        .pMiscMxpSize2Lo(this.size2.lo())
        .pMiscMxpMtntop(this.type4InstructionMayTriggerNonTrivialOperation(hub))
        .pMiscMxpMxpx(this.memoryExpansionException)
        .pMiscMxpWords(Bytes.ofUnsignedLong(this.memorySizeInWords))
        .pMiscMxpGasMxp(Bytes.ofUnsignedLong(this.gasMxp));
  }
}
