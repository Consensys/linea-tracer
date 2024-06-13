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

import static net.consensys.linea.zktracer.opcode.OpCode.EXTCODECOPY;

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
        OpCode.MSIZE,
        false,
        EWord.ZERO,
        EWord.ZERO,
        EWord.ZERO,
        EWord.ZERO,
        false,
        hub.messageFrame().memoryWordSize(),
        0);
  }

  public MxpCall mxpCallType2(Hub hub) {

    EWord offset = EWord.of(hub.messageFrame().getStackItem(0));

    return new MxpCall(
        hub,
        getOpcode(hub),
        false,
        offset,
        EWord.ZERO,
        EWord.ZERO,
        EWord.ZERO,
        getMemoryExpansionException(hub),
        hub.messageFrame().memoryWordSize(),
        0x1337L); // TODO
  }

  public MxpCall mxpCallType3(Hub hub) {

    EWord offset = EWord.of(hub.messageFrame().getStackItem(0));

    return new MxpCall(
        hub,
        OpCode.MSTORE8,
        false,
        offset,
        EWord.ZERO,
        EWord.ZERO,
        EWord.ZERO,
        getMemoryExpansionException(hub),
        hub.messageFrame().memoryWordSize(),
        0x1337L); // TODO
  }

  public static MxpCall mxpCallType4(Hub hub) {

    final OpCode opCode = getOpcode(hub);
    int offsetIndex;
    int sizeIndex;

    switch (opCode) {
      case SHA3, LOG0, LOG1, LOG2, LOG3, LOG4, RETURN, REVERT -> {
        offsetIndex = 0;
        sizeIndex = 1;
      }
      case CALLDATACOPY, CODECOPY, RETURNDATACOPY -> {
        offsetIndex = 0;
        sizeIndex = 2;
      }
      case EXTCODECOPY -> {
        offsetIndex = 1;
        sizeIndex = 3;
      }
      case CREATE, CREATE2 -> {
        offsetIndex = 1;
        sizeIndex = 2;
      }
      default -> {
        throw new RuntimeException("MXP Type 4 incompatible opcode");
      }
    }

    EWord offset = EWord.of(hub.messageFrame().getStackItem(offsetIndex));
    EWord size = EWord.of(hub.messageFrame().getStackItem(sizeIndex));

    return new MxpCall(
        hub,
        opCode,
        getDeploys(hub),
        offset,
        size,
        EWord.ZERO,
        EWord.ZERO,
        getMemoryExpansionException(hub),
        hub.messageFrame().memoryWordSize(),
        0x1337L); // TODO
  }

  public MxpCall mxpCallType5(Hub hub) {

    final OpCode opCode = getOpcode(hub);
    int extra;

    switch (opCode) {
      case CALL, CALLCODE -> {
        extra = 1;
      }
      case DELEGATECALL, STATICCALL -> {
        extra = 0;
      }
      default -> {
        throw new RuntimeException("MXP Type 5 incompatible opcode");
      }
    }

    EWord callDataOffset = EWord.of(hub.messageFrame().getStackItem(2 + extra));
    EWord callDataSize = EWord.of(hub.messageFrame().getStackItem(3 + extra));
    EWord returnAtOffset = EWord.of(hub.messageFrame().getStackItem(4 + extra));
    EWord returnAtCapacity = EWord.of(hub.messageFrame().getStackItem(5 + extra));

    return new MxpCall(
        hub,
        getOpcode(hub),
        false,
        callDataOffset,
        callDataSize,
        returnAtOffset,
        returnAtCapacity,
        getMemoryExpansionException(hub),
        hub.messageFrame().memoryWordSize(),
        0x1337L); // TODO
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

  public final boolean type4InstructionMayTriggerNonTrivialOperation(Hub hub) {
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
