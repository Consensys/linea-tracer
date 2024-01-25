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

package net.consensys.linea.zktracer.runtime.microdata;

import static net.consensys.linea.zktracer.types.Conversions.unsignedBytesToEWord;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.runtime.callstack.CallFrameType;
import net.consensys.linea.zktracer.runtime.callstack.CallStack;
import net.consensys.linea.zktracer.runtime.callstack.ExoSource;
import net.consensys.linea.zktracer.types.EWord;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.internal.Words;

@AllArgsConstructor
@Accessors(fluent = true)
public class MicroData extends ModuleOperation {
  private static final UnsignedByte[] DEFAULT_NIBBLES = new UnsignedByte[9];
  private static final UnsignedByte[][] DEFAULT_ACCS = new UnsignedByte[8][32];
  private static final boolean[] DEFAULT_BITS = new boolean[8];
  private static final Set<Integer> FAST_MICRO_INSTRUCTIONS =
      Set.of(
          Trace.RAM_TO_RAM,
          Trace.EXO_TO_RAM,
          Trace.RAM_IS_EXO,
          Trace.KILLING_ONE,
          Trace.PUSH_TWO_RAM_TO_STACK,
          Trace.PUSH_ONE_RAM_TO_STACK,
          Trace.EXCEPTIONAL_RAM_TO_STACK_3_TO_2_FULL_FAST,
          Trace.PUSH_TWO_STACK_TO_RAM,
          Trace.STORE_X_IN_A_THREE_REQUIRED,
          Trace.STORE_X_IN_B,
          Trace.STORE_X_IN_C);

  @Getter @Setter private int callStackDepth;
  @Getter @Setter private int callStackSize;
  @Getter @Setter private int callDataOffset;
  @Getter @Setter private int callDataSize;
  @Getter @Setter private OpCode opCode;
  @Getter @Setter private boolean skip;
  @Getter @Setter private int processingRow;
  // Same as {@link ReadPad#totalNumberPaddingMicroInstructions()} in size.
  @Getter @Setter private boolean toRam;
  // < 16
  @Getter @Setter private int counter;
  @Getter @Setter private int microOp;
  @Getter @Setter private boolean aligned;
  @Getter @Setter private ReadPad readPad;
  // < 1.000.000
  @Getter @Setter private int sizeImported;
  // < 1.000.000
  @Getter @Setter private int size;
  // stack element <=> uint256
  @Getter @Setter private Bytes value;
  // stack element
  @Getter @Setter private Pointers pointers;
  // stack element
  @Getter @Setter private Offsets offsets;
  // precomputation type
  @Getter @Setter private int precomputation;
  // < 1.000.000
  @Getter @Setter private int min;
  @Getter @Setter private int ternary;
  @Getter @Setter private InstructionContext instructionContext;
  @Getter @Setter private Contexts contexts;
  // every acc is actually on 16 bytes
  @Getter @Setter private UnsignedByte[] nibbles;
  @Getter @Setter private UnsignedByte[][] accs;
  @Getter @Setter private boolean[] bits;
  @Getter @Setter private boolean exoIsHash;
  @Getter @Setter private boolean exoIsLog;
  @Getter @Setter private boolean exoIsRom;
  @Getter @Setter private boolean exoIsTxcd;
  @Getter @Setter private boolean info;
  @Getter @Setter private UnsignedByte[] valACache;
  @Getter @Setter private UnsignedByte[] valBCache;
  @Getter @Setter private UnsignedByte[] valCCache;
  // < 1.000.000
  @Getter @Setter private int referenceOffset;
  // < 1.000.000
  @Getter @Setter private int referenceSize;

  public MicroData() {
    this(
        0,
        0,
        0,
        0,
        null,
        false,
        0,
        false,
        0,
        0,
        false,
        new ReadPad(0, 0),
        0,
        0,
        null,
        Pointers.builder().build(),
        Offsets.builder().build(),
        0,
        0,
        0,
        null,
        Contexts.builder().build(),
        DEFAULT_NIBBLES,
        DEFAULT_ACCS,
        DEFAULT_BITS,
        false,
        false,
        false,
        false,
        false,
        UnsignedByte.EMPTY_BYTES16,
        UnsignedByte.EMPTY_BYTES16,
        UnsignedByte.EMPTY_BYTES16,
        0,
        0);
  }

  public boolean isErf() {
    return microOp == Trace.STORE_X_IN_A_THREE_REQUIRED;
  }

  public boolean isFast() {
    return FAST_MICRO_INSTRUCTIONS.contains(microOp);
  }

  public EWord eWordValue() {
    return EWord.of(value);
  }

  public Address addressValue() {
    return Words.toAddress(value);
  }

  public ExoSource exoSource() {
    if (exoIsRom) {
      return ExoSource.ROM;
    }

    if (exoIsTxcd) {
      return ExoSource.TX_CALLDATA;
    }

    throw new IllegalArgumentException(
        "ExoSource is neither ROM, nor TX_CALLDATA. This should not happen!");
  }

  public boolean isType5() {
    return microOp == OpCode.CALLDATALOAD.getData().value();
  }

  public int remainingMicroInstructions() {
    return readPad.remainingMicroInstructions(processingRow);
  }

  public boolean isRead() {
    return readPad.isRead(processingRow);
  }

  public int remainingReads() {
    return readPad.remainingReads(processingRow);
  }

  public int remainingPads() {
    return readPad.remainingPads(processingRow);
  }

  public boolean isFirstRead() {
    return readPad.isFirstRead(processingRow);
  }

  public boolean isFirstPad() {
    return readPad.isFirstPad(processingRow);
  }

  public boolean isFirstMicroInstruction() {
    return readPad.isFirstMicroInstruction(processingRow);
  }

  public boolean isLastRead() {
    return readPad.isLastRead(processingRow);
  }

  public boolean isLastPad() {
    return readPad.isLastPad(processingRow);
  }

  public boolean isRootContext() {
    return callStackDepth == 1;
  }

  public int sourceContextId() {
    return contexts.source();
  }

  public void sourceContextId(final int value) {
    contexts.source(value);
  }

  public int targetContextId() {
    return contexts.target();
  }

  public void targetContextId(final int value) {
    contexts.target(value);
  }

  public EWord sourceLimbOffset() {
    return offsets.source().limb();
  }

  public void sourceLimbOffset(final EWord value) {
    offsets.source().limb(value);
  }

  public UnsignedByte sourceByteOffset() {
    return offsets.source().uByte();
  }

  public void sourceByteOffset(final UnsignedByte value) {
    offsets.source().uByte(value);
  }

  public EWord targetLimbOffset() {
    return offsets.target().limb();
  }

  public void targetLimbOffset(final EWord value) {
    offsets.target().limb(value);
  }

  public UnsignedByte targetByteOffset() {
    return offsets.target().uByte();
  }

  public void targetByteOffset(final UnsignedByte value) {
    offsets.target().uByte(value);
  }

  public EWord getAccsAtIndex(final int index) {
    return unsignedBytesToEWord(accs[index]);
  }

  public void setAccsAtIndex(final int index, final EWord value) {
    //    accs[index] = bigIntegerToUnsignedBytes32(value.hiBigInt());
  }

  public void setAccsAtIndex(final int index, final BigInteger value) {
    //    accs[index] = bigIntegerToUnsignedBytes32(value);
  }

  public void setAccsAndNibblesAtIndex(final int index, final EWord value) {
    EWord div = value.divide(16);
    setAccsAtIndex(index, div);

    UnsignedByte modulus = UnsignedByte.of(value.mod(16).toLong());
    nibbles[index] = modulus;
  }

  public void setInfo(final CallStack callStack) {
    if (Arrays.asList(OpCode.CODECOPY, OpCode.RETURN).contains(opCode)) {
      info = callStack.current().type() == CallFrameType.INIT_CODE;
      // TODO: settle EXTCODEDOPY info for CODECOPY
    } else if (opCode == OpCode.CALLDATACOPY) {
      info = callStack.depth() == 1;
    }
  }

  public void incrementCounter(final int value) {
    counter += value;
  }

  public void incrementProcessingRow(final int value) {
    processingRow += value;
  }

  @Override
  protected int computeLineCount() {
    return 0;
  }
}
