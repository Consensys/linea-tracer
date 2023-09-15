/*
 * Copyright ConsenSys AG.
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

package net.consensys.linea.zktracer.module.mmu;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import net.consensys.linea.zktracer.module.hub.callstack.CallFrameType;
import net.consensys.linea.zktracer.module.hub.callstack.CallStack;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.EWord;
import net.consensys.linea.zktracer.types.UnsignedByte;

@AllArgsConstructor
class MicroData {
  private int callStackDepth;
  private int callStackSize;
  private int callDataOffset;
  private OpCode opCode;
  private boolean skip;
  private int processingRow;
  // Same as {@link ReadPad#totalNumberPaddingMicroInstructions()} in size.
  private boolean toRam;
  // < 16
  private int counter;
  private MicroOp microOp;
  private boolean aligned;
  private ReadPad readPad;
  // < 1.000.000
  private int sizeImported;
  // < 1.000.000
  private int size;
  // stack element <=> uint256
  private EWord value;
  // stack element
  private Pointers pointers;
  // stack element
  private Offsets offsets;
  // precomputation type
  private int precomputation;
  // < 1.000.000
  private int min;
  private UnsignedByte ternary;
  private StackFrames stackFrames;
  private Contexts contexts;
  // every acc is actually on 16 bytes
  private UnsignedByte[] nibbles;
  private UnsignedByte[][] accs;
  private boolean[] bits;
  private boolean exoIsHash;
  private boolean exoIsLog;
  private boolean exoIsRom;
  private boolean exoIsTxcd;
  private boolean info;
  private UnsignedByte[] valACache;
  private UnsignedByte[] valBCache;
  private UnsignedByte[] valCCache;
  // < 1.000.000
  private int referenceOffset;
  // < 1.000.000
  private int referenceSize;

  MicroData() {
    this(
        0,
        0,
        0,
        null,
        false,
        0,
        false,
        0,
        null,
        false,
        null,
        0,
        0,
        null,
        null,
        null,
        0,
        0,
        UnsignedByte.of(0),
        null,
        null,
        new UnsignedByte[9],
        new UnsignedByte[8][32],
        new boolean[8],
        false,
        false,
        false,
        false,
        false,
        new UnsignedByte[16],
        new UnsignedByte[16],
        null,
        0,
        0);
  }

  boolean isErf() {
    return microOp.value().equals(MmuTrace.StoreXInAThreeRequired);
  }

  boolean isFast() {
    return Arrays.asList(
            MmuTrace.RamToRam,
            MmuTrace.ExoToRam,
            MmuTrace.RamIsExo,
            MmuTrace.KillingOne,
            MmuTrace.PushTwoRamToStack,
            MmuTrace.PushOneRamToStack,
            MmuTrace.ExceptionalRamToStack3To2FullFast,
            MmuTrace.PushTwoStackToRam,
            MmuTrace.StoreXInAThreeRequired,
            MmuTrace.StoreXInB,
            MmuTrace.StoreXInC)
        .contains(microOp.value());
  }

  boolean isType5() {
    Long microOpVal = microOp.value().longValue();
    Long callDataLoadVal = OpCode.CALLDATALOAD.getData().value();

    return microOpVal.equals(callDataLoadVal);
  }

  int remainingMicroInstructions() {
    return readPad.remainingMicroInstructions(processingRow);
  }

  boolean isRead() {
    return readPad.isRead(processingRow);
  }

  int remainingReads() {
    return readPad.remainingReads(processingRow);
  }

  int remainingPads() {
    return readPad.remainingPads(processingRow);
  }

  boolean isFirstRead() {
    return readPad.isFirstRead(processingRow);
  }

  boolean isFirstPad() {
    return readPad.isFirstPad(processingRow);
  }

  boolean isFirstMicroInstruction() {
    return readPad.isFirstMicroInstruction(processingRow);
  }

  boolean isLastRead() {
    return readPad.isLastRead(processingRow);
  }

  boolean isLastPad() {
    return readPad.isLastPad(processingRow);
  }

  boolean isRootContext() {
    return callStackDepth == 1;
  }

  int sourceContext() {
    return contexts.source();
  }

  int targetContext() {
    return contexts.target();
  }

  EWord sourceLimbOffset() {
    return offsets.source().limb();
  }

  UnsignedByte sourceByteOffset() {
    return offsets.source().uByte();
  }

  EWord targetLimbOffset() {
    return offsets.target().limb();
  }

  UnsignedByte targetByteOffset() {
    return offsets.target().uByte();
  }

  void setInfo(CallStack callStack) {
    if (Arrays.asList(OpCode.CODECOPY, OpCode.RETURN).contains(opCode)) {
      info = callStack.top().getType() == CallFrameType.INIT_CODE;
      // TODO: settle EXTCODEDOPY info for CODECOPY
    } else if (opCode == OpCode.CALLDATACOPY) {
      info = callStack.depth() == 1;
    }
  }
}
