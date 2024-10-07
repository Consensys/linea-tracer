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

package net.consensys.linea.zktracer.module.hub.fragment;

import static com.google.common.base.Preconditions.*;
import static net.consensys.linea.zktracer.module.hub.signals.TracedException.*;
import static net.consensys.linea.zktracer.opcode.InstructionFamily.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.State;
import net.consensys.linea.zktracer.module.hub.Trace;
import net.consensys.linea.zktracer.module.hub.fragment.common.CommonFragmentValues;
import net.consensys.linea.zktracer.module.hub.signals.AbortingConditions;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import net.consensys.linea.zktracer.module.hub.signals.TracedException;
import net.consensys.linea.zktracer.opcode.InstructionFamily;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.opcode.gas.MxpType;
import net.consensys.linea.zktracer.opcode.gas.projector.GasProjection;
import net.consensys.linea.zktracer.runtime.stack.Stack;
import net.consensys.linea.zktracer.runtime.stack.StackItem;
import net.consensys.linea.zktracer.types.EWord;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.evm.internal.Words;

@Accessors(fluent = true)
public final class StackFragment implements TraceFragment {
  private final Stack stack;
  @Getter private final List<StackItem> stackOps;
  private final short exceptions;
  private final long staticGas;
  @Setter public boolean hashInfoFlag;
  private EWord hashInfoKeccak = EWord.ZERO;
  @Setter public Bytes hash;
  @Getter private final OpCode opCode;
  @Setter private boolean jumpDestinationVettingRequired;
  @Setter private boolean validJumpDestination;
  private final boolean willRevert;
  private final State.TxState.Stamps stamps;
  private final CommonFragmentValues commonFragmentValues;

  private StackFragment(
      final Hub hub,
      Stack stack,
      List<StackItem> stackOps,
      short exceptions,
      AbortingConditions aborts,
      GasProjection gp,
      boolean isDeploying,
      boolean willRevert,
      CommonFragmentValues commonFragmentValues) {
    this.stack = stack;
    this.stackOps = stackOps;
    this.exceptions = exceptions;
    this.opCode = stack.getCurrentOpcodeData().mnemonic();
    this.hashInfoFlag =
        switch (this.opCode) {
          case SHA3 -> Exceptions.none(exceptions) && gp.messageSize() > 0;
          case RETURN -> Exceptions.none(exceptions) && gp.messageSize() > 0 && isDeploying;
          case CREATE2 -> Exceptions.none(exceptions) && aborts.none() && gp.messageSize() > 0;
          default -> false;
        };
    if (this.hashInfoFlag) {
      Bytes memorySegmentToHash;
      switch (this.opCode) {
        case SHA3, RETURN -> {
          final long offset = Words.clampedToLong(hub.currentFrame().frame().getStackItem(0));
          final long size = Words.clampedToLong(hub.currentFrame().frame().getStackItem(1));
          memorySegmentToHash = hub.messageFrame().shadowReadMemory(offset, size);
        }
        case CREATE2 -> {
          final long offset = Words.clampedToLong(hub.currentFrame().frame().getStackItem(1));
          final long size = Words.clampedToLong(hub.currentFrame().frame().getStackItem(2));
          memorySegmentToHash = hub.messageFrame().shadowReadMemory(offset, size);
        }
        default -> throw new UnsupportedOperationException(
            "Hash was attempted by the following opcode: " + this.opCode().toString());
      }
      this.hashInfoKeccak = EWord.of(Hash.hash(memorySegmentToHash));
    }

    this.staticGas = gp.staticGas();

    if (opCode.isJump() && !Exceptions.stackException(exceptions)) {
      final BigInteger prospectivePcNew =
          hub.currentFrame().frame().getStackItem(0).toUnsignedBigInteger();
      final BigInteger codeSize = BigInteger.valueOf(hub.currentFrame().code().getSize());

      boolean prospectivePcNewIsInBounds = codeSize.compareTo(prospectivePcNew) > 0;

      if (opCode.equals(OpCode.JUMPI)) {
        boolean nonzeroJumpCondition =
            !hub.currentFrame()
                .frame()
                .getStackItem(1)
                .toUnsignedBigInteger()
                .equals(BigInteger.ZERO);
        prospectivePcNewIsInBounds = prospectivePcNewIsInBounds && nonzeroJumpCondition;
      }

      jumpDestinationVettingRequired = prospectivePcNewIsInBounds;
    } else {
      jumpDestinationVettingRequired = false;
    }

    this.willRevert = willRevert;
    this.stamps = hub.state().stamps();
    this.commonFragmentValues = commonFragmentValues;
  }

  public static StackFragment prepare(
      final Hub hub,
      final Stack stack,
      final List<StackItem> stackItems,
      final short exceptions,
      final AbortingConditions aborts,
      final GasProjection gp,
      boolean isDeploying,
      boolean willRevert,
      CommonFragmentValues commonFragmentValues) {
    return new StackFragment(
        hub,
        stack,
        stackItems,
        exceptions,
        aborts,
        gp,
        isDeploying,
        willRevert,
        commonFragmentValues);
  }

  private boolean traceLog() {
    return opCode.isLog()
        && Exceptions.none(
            exceptions) // TODO: should be redundant (exceptions trigger reverts) --- this
        // could be asserted
        && !willRevert;
  }

  @Override
  public Trace trace(Trace trace) {

    final List<Function<Bytes, Trace>> valHiTracers =
        List.of(
            trace::pStackStackItemValueHi1,
            trace::pStackStackItemValueHi2,
            trace::pStackStackItemValueHi3,
            trace::pStackStackItemValueHi4);

    final List<Function<Bytes, Trace>> valLoTracers =
        List.of(
            trace::pStackStackItemValueLo1,
            trace::pStackStackItemValueLo2,
            trace::pStackStackItemValueLo3,
            trace::pStackStackItemValueLo4);

    final List<Function<Boolean, Trace>> popTracers =
        List.of(
            trace::pStackStackItemPop1,
            trace::pStackStackItemPop2,
            trace::pStackStackItemPop3,
            trace::pStackStackItemPop4);

    final List<Function<Bytes, Trace>> heightTracers =
        List.of(
            trace::pStackStackItemHeight1,
            trace::pStackStackItemHeight2,
            trace::pStackStackItemHeight3,
            trace::pStackStackItemHeight4);

    final List<Function<Bytes, Trace>> stampTracers =
        List.of(
            trace::pStackStackItemStamp1,
            trace::pStackStackItemStamp2,
            trace::pStackStackItemStamp3,
            trace::pStackStackItemStamp4);

    EWord pushValue = EWord.ZERO;
    var it = stackOps.listIterator();
    while (it.hasNext()) {
      var i = it.nextIndex();
      var op = it.next();
      final EWord eValue = EWord.of(op.value());
      if (stack.getCurrentOpcodeData().isPush()) {
        pushValue = eValue;
      }

      heightTracers.get(i).apply(Bytes.ofUnsignedShort(op.height()));
      valLoTracers.get(i).apply(eValue.lo());
      valHiTracers.get(i).apply(eValue.hi());
      popTracers.get(i).apply(op.action() == Stack.POP);
      stampTracers.get(i).apply(Bytes.ofUnsignedLong(op.stackStamp()));
    }

    final InstructionFamily currentInstFamily = stack.getCurrentOpcodeData().instructionFamily();

    TracedException tracedException = commonFragmentValues.tracedException();

    // TODO: here only for debugging, remove it
    if (tracedException == UNDEFINED) {
      System.out.println(currentInstFamily);
      System.out.println(this.opCode);
    }

    this.tracedExceptionSanityChecks(tracedException);

    return trace
        .peekAtStack(true)
        // Instruction details
        .pStackAlpha(UnsignedByte.of(stack.getCurrentOpcodeData().stackSettings().alpha()))
        .pStackDelta(UnsignedByte.of(stack.getCurrentOpcodeData().stackSettings().delta()))
        .pStackNbAdded(UnsignedByte.of(stack.getCurrentOpcodeData().stackSettings().nbAdded()))
        .pStackNbRemoved(UnsignedByte.of(stack.getCurrentOpcodeData().stackSettings().nbRemoved()))
        .pStackInstruction(Bytes.of(stack.getCurrentOpcodeData().value()))
        .pStackStaticGas(staticGas)
        // Opcode families
        .pStackAccFlag(currentInstFamily == ACCOUNT)
        .pStackAddFlag(currentInstFamily == ADD)
        .pStackBinFlag(currentInstFamily == BIN)
        .pStackBtcFlag(currentInstFamily == BATCH)
        .pStackCallFlag(currentInstFamily == CALL)
        .pStackConFlag(currentInstFamily == CONTEXT)
        .pStackCopyFlag(currentInstFamily == COPY)
        .pStackCreateFlag(currentInstFamily == CREATE)
        .pStackDupFlag(currentInstFamily == DUP)
        .pStackExtFlag(currentInstFamily == EXT)
        .pStackHaltFlag(currentInstFamily == HALT)
        .pStackInvalidFlag(currentInstFamily == INVALID)
        .pStackJumpFlag(currentInstFamily == JUMP)
        .pStackKecFlag(currentInstFamily == KEC)
        .pStackLogFlag(currentInstFamily == LOG)
        .pStackMachineStateFlag(currentInstFamily == MACHINE_STATE)
        .pStackModFlag(currentInstFamily == MOD)
        .pStackMulFlag(currentInstFamily == MUL)
        .pStackPushpopFlag(currentInstFamily == PUSH_POP)
        .pStackShfFlag(currentInstFamily == SHF)
        .pStackStackramFlag(currentInstFamily == STACK_RAM)
        .pStackStoFlag(currentInstFamily == STORAGE)
        .pStackSwapFlag(currentInstFamily == SWAP)
        .pStackTxnFlag(currentInstFamily == TRANSACTION)
        .pStackWcpFlag(currentInstFamily == WCP)
        .pStackDecFlag1(stack.getCurrentOpcodeData().stackSettings().flag1())
        .pStackDecFlag2(stack.getCurrentOpcodeData().stackSettings().flag2())
        .pStackDecFlag3(stack.getCurrentOpcodeData().stackSettings().flag3())
        .pStackDecFlag4(stack.getCurrentOpcodeData().stackSettings().flag4())
        .pStackMxpFlag(
            Optional.ofNullable(stack.getCurrentOpcodeData().billing())
                .map(b -> b.type() != MxpType.NONE)
                .orElse(false))
        .pStackStaticFlag(stack.getCurrentOpcodeData().stackSettings().forbiddenInStatic())
        .pStackPushValueHi(pushValue.hi())
        .pStackPushValueLo(pushValue.lo())
        .pStackJumpDestinationVettingRequired(jumpDestinationVettingRequired) // TODO: confirm this
        // Exception flag
        .pStackOpcx(tracedException == INVALID_OPCODE)
        .pStackSux(tracedException == STACK_UNDERFLOW)
        .pStackSox(tracedException == STACK_OVERFLOW)
        .pStackMxpx(tracedException == MEMORY_EXPANSION_EXCEPTION)
        .pStackOogx(tracedException == OUT_OF_GAS_EXCEPTION)
        .pStackRdcx(tracedException == RETURN_DATA_COPY_FAULT)
        .pStackJumpx(tracedException == JUMP_FAULT)
        .pStackStaticx(tracedException == STATIC_FAULT)
        .pStackSstorex(tracedException == OUT_OF_SSTORE)
        .pStackIcpx(tracedException == INVALID_CODE_PREFIX)
        .pStackMaxcsx(tracedException == MAX_CODE_SIZE_EXCEPTION)
        // Hash data
        .pStackHashInfoFlag(hashInfoFlag)
        .pStackHashInfoKeccakHi(hashInfoKeccak.hi())
        .pStackHashInfoKeccakLo(hashInfoKeccak.lo())
        .pStackLogInfoFlag(this.traceLog()) // TODO: confirm this
    ;
  }

  private void tracedExceptionSanityChecks(TracedException tracedException) {
    switch (tracedException) {
      case NONE -> checkArgument(Exceptions.none(exceptions));
      case INVALID_OPCODE -> checkArgument(Exceptions.invalidOpcode(exceptions));
      case STACK_UNDERFLOW -> checkArgument(Exceptions.stackUnderflow(exceptions));
      case STACK_OVERFLOW -> checkArgument(Exceptions.stackOverflow(exceptions));
      case MEMORY_EXPANSION_EXCEPTION -> checkArgument(
          Exceptions.memoryExpansionException(exceptions));
      case OUT_OF_GAS_EXCEPTION -> checkArgument(Exceptions.outOfGasException(exceptions));
      case RETURN_DATA_COPY_FAULT -> checkArgument(Exceptions.returnDataCopyFault(exceptions));
      case JUMP_FAULT -> checkArgument(Exceptions.jumpFault(exceptions));
      case STATIC_FAULT -> checkArgument(Exceptions.staticFault(exceptions));
      case OUT_OF_SSTORE -> checkArgument(Exceptions.outOfSStore(exceptions));
      case INVALID_CODE_PREFIX -> checkArgument(Exceptions.invalidCodePrefix(exceptions));
      case MAX_CODE_SIZE_EXCEPTION -> checkArgument(Exceptions.maxCodeSizeException(exceptions));
      case UNDEFINED -> throw new RuntimeException(
          "tracedException remained UNDEFINED but "
              + Exceptions.prettyStringOf(this.opCode, exceptions));
    }
  }
}
