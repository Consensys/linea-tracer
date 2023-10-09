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

import java.math.BigInteger;
import java.util.Arrays;

import lombok.AllArgsConstructor;
import net.consensys.linea.zktracer.module.runtime.callstack.CallFrameType;
import net.consensys.linea.zktracer.module.runtime.callstack.CallStack;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.EWord;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.commons.lang3.NotImplementedException;

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
  private BigInteger precomputation;
  // < 1.000.000
  private int min;
  private BigInteger ternary;
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
        BigInteger.ZERO,
        0,
        BigInteger.ZERO,
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

  void setInfo(final CallStack callStack) {
    if (Arrays.asList(OpCode.CODECOPY, OpCode.RETURN).contains(opCode)) {
      info = callStack.top().type() == CallFrameType.INIT_CODE;
      // TODO: settle EXTCODEDOPY info for CODECOPY
    } else if (opCode == OpCode.CALLDATACOPY) {
      info = callStack.depth() == 1;
    }
  }

  void setContext(final boolean isMicro, final CallStack callStack) {
    contexts.target(callStack.top().contextNumber());

    switch (opCode) {
      case RETURNDATACOPY -> {
        exoIsHash = false;
        exoIsLog = false;
        exoIsRom = false;
        exoIsTxcd = false;
      }
      case CALLDATACOPY -> {
        exoIsHash = false;
        exoIsLog = false;
        exoIsRom = false;

        if (isMicro && isRead()) {
          exoIsTxcd = info;
        } else {
          exoIsTxcd = false;
        }

        if (callStackDepth != 1) {
          contexts.source(callStack.caller().contextNumber());
        } else {
          contexts.source(0);
        }
      }
      case CODECOPY, EXTCODECOPY -> {
        exoIsHash = false;
        exoIsLog = false;

        exoIsRom = isMicro && isRead();
        exoIsTxcd = false;

        contexts.source(0);
      }
      default -> throw new UnsupportedOperationException(
          "OpCode.%s is not supported for MMU type 4 pre-processing and/or processing"
              .formatted(opCode));
    }
  }

  void processing(final CallStack callStack) {
    if (precomputation.equals(MmuTrace.type1)) {
      type1Processing();
      return;
    } else if (precomputation.equals(MmuTrace.type2)) {
      type2Processing();
      return;
    } else if (precomputation.equals(MmuTrace.type3)) {
      type3Processing();
      return;
    } else if (Arrays.asList(MmuTrace.type4CC, MmuTrace.type4CD, MmuTrace.type4RD)
        .contains(precomputation)) {
      type4Processing(callStack);
      return;
    } else if (precomputation.equals(MmuTrace.type5)) {
      type5Processing(callStack);
      return;
    }

    throw new NotImplementedException();
  }

  private void type5Processing(final CallStack callStack) {
    updateMicroOpType5(callStack);
    updateOffsetType5(callStack);
  }

  private void updateOffsetType5(final CallStack callStack) {}

  private void updateMicroOpType5(final CallStack callStack) {}

  private void type3Processing() {
    updateMicroOpType3();
    updateOffsetType3();
  }

  private void updateOffsetType3() {}

  private void updateMicroOpType3() {}

  private void type2Processing() {
    updateMicroOpType2();
    updateOffsetType2();
  }

  private void updateOffsetType2() {}

  private void updateMicroOpType2() {}

  private void type1Processing() {}

  void preProcessing(final CallStack callStack) {
    if (precomputation.equals(MmuTrace.type1)) {
      type1PreProcessing();
      return;
    } else if (precomputation.equals(MmuTrace.type2)) {
      type2PreProcessing();
      return;
    } else if (precomputation.equals(MmuTrace.type3)) {
      type3PreProcessing();
      return;
    } else if (Arrays.asList(MmuTrace.type4CC, MmuTrace.type4CD, MmuTrace.type4RD)
        .contains(precomputation)) {
      type4PreProcessing(callStack);
      return;
    } else if (precomputation.equals(MmuTrace.type5)) {
      type5PreProcessing();
      return;
    }

    throw new NotImplementedException();
  }

  private void type5PreProcessing() {}

  private void type4PreProcessing(final CallStack callStack) {
    setContext(false, callStack);
  }

  private void type4Processing(final CallStack callStack) {
    setContext(true, callStack);

    if (ternary.equals(MmuTrace.tern0)) {
      updateTern0();
    } else if (ternary.equals(MmuTrace.tern1)) {
      updateMicroOpType4Tern1();
      updateOffsetType4Tern1();
    } else if (ternary.equals(MmuTrace.tern2)) {
      updateMicroOpType4Tern2();
      updateOffsetType4Tern2();
    }
  }

  private void updateOffsetType4Tern2() {}

  private void updateMicroOpType4Tern2() {}

  private void updateOffsetType4Tern1() {}

  private void updateMicroOpType4Tern1() {}

  private void updateTern0() {
    if (bits[2]) {
      type4Tern0SingleMicroInstruction();
    } else {
      type4Tern0MultipleMicroInstruction();
    }
  }

  private void type4Tern0MultipleMicroInstruction() {}

  private void type4Tern0SingleMicroInstruction() {}

  private void type3PreProcessing() {
    switch (opCode) {
      case SHA3 -> exoIsHash = true;
      case LOG0, LOG1, LOG2, LOG3, LOG4 -> exoIsLog = true;
      case CREATE, RETURN -> exoIsRom = true;
      case CREATE2 -> {
        exoIsRom = true;
        exoIsHash = true;
      }
      default -> throw new UnsupportedOperationException(
          "OpCode.%s is not supported for MMU type 3 pre-processing.".formatted(opCode));
    }
  }

  private void type2PreProcessing() {}

  private void type1PreProcessing() {}
}
