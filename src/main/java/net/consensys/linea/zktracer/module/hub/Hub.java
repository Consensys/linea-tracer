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

package net.consensys.linea.zktracer.module.hub;

import java.math.BigInteger;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import net.consensys.linea.zktracer.EWord;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.opcode.InstructionFamily;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.opcode.OpCodeData;
import net.consensys.linea.zktracer.opcode.gas.MxpType;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Quantity;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.operation.Operation;
import org.hyperledger.besu.plugin.data.BlockBody;
import org.hyperledger.besu.plugin.data.BlockHeader;

enum TxState {
  // A state marking the first trace of the current tx, required to set up some things
  TxPreInit,
  TxExec,
  TxFinal,
  TxInit,
  TxSkip,
  TxWarm,
}

record Exceptions(
    boolean InvalidOpcode,
    boolean StackUnderflow,
    boolean StackOverflow,
    boolean OutOfMemoryExpansion,
    boolean OutOfGas,
    boolean ReturnDataCopyFault,
    boolean JumpFault,
    boolean StaticViolation,
    boolean OutOfSStore,
    boolean InvalidCodePrefix,
    boolean CodeSizeOverflow) {}

public class Hub implements Module {
  private static final Address ADDRESS_ZERO = Address.fromHexString("0x0");
  private static final int TAU = 8;

  final Trace.TraceBuilder trace = Trace.builder();
  private int pc;
  private OpCode opCode;
  private Exceptions exceptions;
  
  private OpCodeData opCodeData() {
    return this.opCode.getData();
  }

  TxState txState;
  Transaction currentTx;
  CallStack callStack = new CallStack();
  int txNumber = 0;
  int batchNumber = 0;
  int blockNumber = 0;
  int stamp = 0;

  public Hub() {}

  @Override
  public String jsonKey() {
    return "hub";
  }

  @Override
  public final List<OpCode> supportedOpCodes() {
    // The Hub wants to catch all opcodes
    return List.of(OpCode.values());
  }

  private void checkExceptions(MessageFrame frame, Operation.OperationResult result) {
    this.exceptions =
        new Exceptions(
            this.opCode == OpCode.INVALID,
            frame.stackSize() < this.opCodeData().stackSettings().nbRemoved(),
            frame.stackSize()
                    + this.opCodeData().stackSettings().nbAdded()
                    - this.opCodeData().stackSettings().nbRemoved()
                > 1024,
            false, // TODO mxp
            frame.getRemainingGas() < 0,
            false, // TODO
            false, // TODO
            frame.isStatic() && !this.opCodeData().stackSettings().staticInstruction(),
            false, // TODO
            false, // TODO
            false // TODO
            );
  }

  public boolean isError() {
    return false;
  }

  private static boolean isPrecompile(Address to) {
    return List.of(
            Address.ECREC,
            Address.SHA256,
            Address.RIPEMD160,
            Address.ID,
            Address.MODEXP,
            Address.ALTBN128_ADD,
            Address.ALTBN128_MUL,
            Address.ALTBN128_PAIRING,
            Address.BLAKE2B_F_COMPRESSION)
        .contains(to);
  }

  /**
   * Traces a skipped transaction, i.e. a “pure” transaction without EVM execution.
   *
   * @param frame the frame of the transaction
   */
  void traceSkippedTx(MessageFrame frame) {
    this.stamp++;
    Quantity value = this.currentTx.getValue();
    boolean isDeployment = this.currentTx.getTo().isEmpty();

    // 3 lines -- account changes
    // From account information
    EWord from = EWord.of(this.currentTx.getSender());
    Wei currentFromBalance = frame.getWorldUpdater().get(this.currentTx.getSender()).getBalance();
    Wei newFromBalance = currentFromBalance.subtract((Wei) value);
    this.trace
        .absoluteTransactionNumber(BigInteger.valueOf(this.txNumber))
        .batchNumber(BigInteger.valueOf(this.batchNumber))
        .txSkip(true)
        .hubStamp(BigInteger.valueOf(this.stamp))
        .peekAtAccount(true)
        .pAccountAddressHi(from.hiBigInt())
        .pAccountAddressLo(from.loBigInt())
        .pAccountBalance(currentFromBalance.toUnsignedBigInteger())
        .pAccountBalanceNew(newFromBalance.toUnsignedBigInteger())
        .pAccountExists(true)
        .pAccountExistsNew(true)
        .pAccountSufficientBalance(true)
        .fillAndValidateRow();

    // To account information
    boolean exists = frame.getWorldUpdater().getAccount(this.currentTx.getTo().get()) != null;
    EWord to =
        EWord.of(
            this.currentTx.getTo().map(EWord::of).orElse(EWord.ZERO)); // TODO: fix empty ToAddress
    Wei currentToBalance =
        this.currentTx
            .getTo()
            .map(t -> frame.getWorldUpdater().getAccount(t).getBalance())
            .orElse(Wei.ZERO);
    Wei newToBalance = currentToBalance.add((Wei) value);
    this.traceHeader()
        .peekAtAccount(true)
        .pAccountAddressHi(to.hiBigInt())
        .pAccountAddressLo(to.loBigInt())
        .pAccountBalance(currentToBalance.toUnsignedBigInteger())
        .pAccountBalanceNew(newToBalance.toUnsignedBigInteger())
        .pAccountExists(exists)
        .pAccountExistsNew(true)
        .pAccountSufficientBalance(true)
        .fillAndValidateRow();

    // Basecoin/miner information
    EWord miner = EWord.of(frame.getMiningBeneficiary());
    Wei currentMinerBalance =
        frame.getWorldUpdater().get(frame.getMiningBeneficiary()).getBalance();
    Wei newMinerBalance = Wei.ZERO;
    this.traceHeader()
        .peekAtAccount(true)
        .pAccountAddressHi(miner.hiBigInt())
        .pAccountAddressLo(miner.loBigInt())
        .pAccountBalance(currentMinerBalance.toUnsignedBigInteger())
        .pAccountBalanceNew(newMinerBalance.toUnsignedBigInteger())
        .pAccountExists(true)
        .pAccountExistsNew(true)
        .pAccountSufficientBalance(true)
        .fillAndValidateRow();

    // 1 line -- tx data
    this.traceHeader()
        .peekAtTransaction(true)
        .pTransactionNonce(BigInteger.valueOf(this.currentTx.getNonce()))
        .pTransactionFromAddressHi(from.hiBigInt())
        .pTransactionFromAddressLo(from.loBigInt())
        .pTransactionValue(value.getAsBigInteger())
        .pTransactionToAddressHi(to.hiBigInt())
        .pTransactionToAddressLo(to.loBigInt())
        .pTransactionBatchNumber(BigInteger.valueOf(this.batchNumber))
        .pTransactionAbsoluteTransactionNumber(BigInteger.valueOf(this.txNumber))
        .pTransactionIsDeployment(isDeployment)
        .fillAndValidateRow();
  }

  /**
   * Fill the columns shared by all operations.
   *
   * @return the partially filled trace row
   */
  private Trace.TraceBuilder traceHeader() {
    return this.trace
        .absoluteTransactionNumber(BigInteger.valueOf(this.txNumber))
        .batchNumber(BigInteger.valueOf(this.batchNumber))
        .txSkip(this.txState == TxState.TxSkip)
        .txWarm(this.txState == TxState.TxWarm)
        .txInit(this.txState == TxState.TxInit)
        .txExec(this.txState == TxState.TxExec)
        .txFinl(this.txState == TxState.TxFinal)
        .hubStamp(BigInteger.valueOf(this.stamp))
        .transactionEndStamp(BigInteger.ZERO) // TODO
        .transactionReverts(BigInteger.ZERO) // TODO
        .contextMayChangeFlag(false) // TODO
        .exceptionAhoyFlag(false) // TODO
        .abortFlag(false) // TODO
        .failureConditionFlag(false) // TODO

        // Context data
        .contextNumber(BigInteger.valueOf(this.currentFrame().contextNumber))
        .contextNumberNew(BigInteger.ZERO) // TODO
        .contextRevertStamp(BigInteger.ZERO) // TODO
        .contextWillRevertFlag(false) // TODO
        .contextGetsRevrtdFlag(false) // TODO
        .contextSelfRevrtsFlag(false) // TODO
        .programCounter(BigInteger.valueOf(this.pc))
        .programCounterNew(BigInteger.ZERO) // TODO

        // Bytecode metadata
        .codeAddressHi(this.currentFrame().addressAsEWord().hiBigInt())
        .codeAddressLo(this.currentFrame().addressAsEWord().loBigInt())
        .codeDeploymentNumber(BigInteger.valueOf(this.currentFrame().deploymentNumber))
        .codeDeploymentStatus(false) // TODO
        .callerContextNumber(BigInteger.valueOf(this.callStack.caller().deploymentNumber));
  }

  /**
   * Trace the stack-related perspective columns for a given {@link StackLine}
   *
   * @param line the stack line to trace
   * @return the partially filled trace row
   */
  private Trace.TraceBuilder traceStackLine(StackLine line) {
    List<Function<BigInteger, Trace.TraceBuilder>> heightSetters =
        List.of(
            this.trace::pStackStackItemHeight1,
            this.trace::pStackStackItemHeight2,
            this.trace::pStackStackItemHeight3,
            this.trace::pStackStackItemHeight4);
    List<Function<BigInteger, Trace.TraceBuilder>> valHiSetters =
        List.of(
            this.trace::pStackStackItemValueHi1,
            this.trace::pStackStackItemValueHi2,
            this.trace::pStackStackItemValueHi3,
            this.trace::pStackStackItemValueHi4);
    List<Function<BigInteger, Trace.TraceBuilder>> valLoSetters =
        List.of(
            this.trace::pStackStackItemValueLo1,
            this.trace::pStackStackItemValueLo2,
            this.trace::pStackStackItemValueLo3,
            this.trace::pStackStackItemValueLo4);

    List<Function<Boolean, Trace.TraceBuilder>> popSetters =
        List.of(
            this.trace::pStackStackItemPop1,
            this.trace::pStackStackItemPop2,
            this.trace::pStackStackItemPop3,
            this.trace::pStackStackItemPop4);
    List<Function<BigInteger, Trace.TraceBuilder>> stampSetters =
        List.of(
            this.trace::pStackStackItemStamp1,
            this.trace::pStackStackItemStamp2,
            this.trace::pStackStackItemStamp3,
            this.trace::pStackStackItemStamp4);

    final var stack = currentFrame().stack;

    final var alpha = this.opCodeData().stackSettings().alpha();
    final var delta = this.opCodeData().stackSettings().delta();
    var heightUnder = stack.height - delta;
    var heightOver = 0;

    var overflow = stack.isOverflow() ? 1 : 0;

    if (!stack.isUnderflow()) {
      if (alpha == 1 && delta == 0 && stack.height == Stack.MAX_STACK_SIZE) {
        heightOver = stack.height + alpha - delta - Stack.MAX_STACK_SIZE - 1;
      } else {
        heightOver = (2 * overflow - 1) * (heightUnder + alpha - Stack.MAX_STACK_SIZE) - overflow;
      }
    } else {
      heightUnder = -heightUnder - 1;
    }

    final var stackOps = line.asStackOperations();
    var it = stackOps.listIterator();
    while (it.hasNext()) {
      var i = it.nextIndex();
      var op = it.next();

      heightSetters.get(i).apply(BigInteger.valueOf(op.height()));
      valLoSetters.get(i).apply(op.value().loBigInt());
      valHiSetters.get(i).apply(op.value().hiBigInt());
      popSetters.get(i).apply(op.action() == Action.Pop);
      stampSetters.get(i).apply(BigInteger.valueOf(op.stackStamp()));
    }

    return this.trace
        // Stack height
        .pStackHeight(BigInteger.valueOf(stack.height))
        .pStackHeightNew(BigInteger.valueOf(stack.heightNew))
        .pStackHeightUnder(BigInteger.valueOf(heightUnder))
        .pStackHeightOver(BigInteger.valueOf(heightOver))
        // Instruction details
        .pStackInstruction(BigInteger.valueOf(this.opCodeData().value()))
        .pStackStaticGas(BigInteger.ZERO) // TODO
        .pStackDecodedFlag1(this.opCodeData().stackSettings().flag1())
        .pStackDecodedFlag2(this.opCodeData().stackSettings().flag2())
        .pStackDecodedFlag3(this.opCodeData().stackSettings().flag3())
        .pStackDecodedFlag4(this.opCodeData().stackSettings().flag4())
        // Exception flag
        .pStackOpcx(false) // TODO
        .pStackSux(stack.isUnderflow())
        .pStackSox(stack.isOverflow())
        .pStackOogx(false) // TODO
        .pStackMxpx(false) // TODO
        .pStackRdcx(false) // TODO
        .pStackJumpx(false) // TODO
        .pStackStaticx(false) // TODO
        .pStackSstorex(false) // TODO
        .pStackInvprex(false) // TODO
        .pStackMaxcsx(false) // TODO
        // Opcode families
        .pStackAddFlag(this.opCodeData().instructionFamily() == InstructionFamily.ADD)
        .pStackModFlag(this.opCodeData().instructionFamily() == InstructionFamily.MOD)
        .pStackMulFlag(this.opCodeData().instructionFamily() == InstructionFamily.MUL)
        .pStackExtFlag(this.opCodeData().instructionFamily() == InstructionFamily.EXT)
        .pStackWcpFlag(this.opCodeData().instructionFamily() == InstructionFamily.WCP)
        .pStackBinFlag(this.opCodeData().instructionFamily() == InstructionFamily.BIN)
        .pStackShfFlag(this.opCodeData().instructionFamily() == InstructionFamily.SHF)
        .pStackKecFlag(this.opCodeData().instructionFamily() == InstructionFamily.KEC)
        .pStackConFlag(this.opCodeData().instructionFamily() == InstructionFamily.CONTEXT)
        .pStackAccFlag(this.opCodeData().instructionFamily() == InstructionFamily.ACCOUNT)
        .pStackCopyFlag(this.opCodeData().instructionFamily() == InstructionFamily.COPY)
        .pStackTxnFlag(this.opCodeData().instructionFamily() == InstructionFamily.TRANSACTION)
        .pStackBtcFlag(this.opCodeData().instructionFamily() == InstructionFamily.BATCH)
        .pStackStackramFlag(this.opCodeData().instructionFamily() == InstructionFamily.STACK_RAM)
        .pStackStoFlag(this.opCodeData().instructionFamily() == InstructionFamily.STORAGE)
        .pStackJumpFlag(this.opCodeData().instructionFamily() == InstructionFamily.JUMP)
        .pStackPushpopFlag(this.opCodeData().instructionFamily() == InstructionFamily.PUSH_POP)
        .pStackDupFlag(this.opCodeData().instructionFamily() == InstructionFamily.DUP)
        .pStackSwapFlag(this.opCodeData().instructionFamily() == InstructionFamily.SWAP)
        .pStackLogFlag(this.opCodeData().instructionFamily() == InstructionFamily.LOG)
        .pStackCreateFlag(this.opCodeData().instructionFamily() == InstructionFamily.CREATE)
        .pStackCallFlag(this.opCodeData().instructionFamily() == InstructionFamily.CALL)
        .pStackHaltFlag(this.opCodeData().instructionFamily() == InstructionFamily.HALT)
        .pStackInvalidFlag(this.opCodeData().instructionFamily() == InstructionFamily.INVALID)
        .pStackMxpFlag(this.opCodeData().billing().type() != MxpType.NONE)
        .pStackTrmFlag(this.opCodeData().stackSettings().addressTrimmingInstruction())
        .pStackStaticFlag(this.opCodeData().stackSettings().staticInstruction())
        .pStackOobFlag(this.opCodeData().stackSettings().oobFlag());
  }

  void processStateWarm() {
    this.stamp++;
    // x lines - warm addresses
    // y lines - warm storage keys
  }

  void processStateInit() {
    this.stamp++;
    // 2 lines -- trace from & to accounts
    // 1 line  -- trace tx data
  }

  private int currentLine() {
    return this.trace.size();
  }

  private boolean handleStack(MessageFrame frame) {
    boolean stackOk =
        this.currentFrame().stack.processInstruction(frame, this.currentFrame(), TAU * this.stamp);
    this.currentFrame().pending.startInTrace = this.currentLine();
    return stackOk;
  }

  void processStateExec(MessageFrame frame) {
    this.opCode = OpCode.of(frame.getCurrentOperation().getOpcode());
    this.pc = frame.getPC();
    this.stamp++;
    boolean noXFlow = this.handleStack(frame);

    if (this.currentFrame().stack.isOk()) {
      /*
      this.handleCallReturnData
      this.handleMemory
      this.handleRam
       */
    }

    // this.updateTrace();

    if (false /* frame.isError() */) {
      // ...
    }
  }

  void processStateFinal() {
    this.stamp++;
    // if no revert: 2 account rows (sender, coinbase) + 1 tx row
    // otherwise 4 account rows (sender, coinbase, sender, recipient) + 1 tx row

    this.txState = TxState.TxPreInit;
  }

  @Override
  public void traceStartTx(Transaction tx) {
    if (tx.getTo().isPresent() && isPrecompile(tx.getTo().get())) {
      throw new RuntimeException("Call to precompile forbidden");
    } else {
      this.txNumber += 1;
    }
    this.currentTx = tx;

    // impossible to do the pre-init here -- missing information from the MessageFrame
    this.txState = TxState.TxPreInit;
  }

  @Override
  public void traceEndTx() {
    this.txState = TxState.TxFinal;
    this.processStateFinal();
  }

  private void txInit(final MessageFrame frame) {
    // TODO: check that this actually does what it should
    if ((!frame.getRecipientAddress().equals(ADDRESS_ZERO)
            && frame.getCode().getSize() == 0) // pure transaction
        || (frame.getRecipientAddress().equals(ADDRESS_ZERO)
            && frame.getInputData().isEmpty())) { // contract creation without initcode
      this.txState = TxState.TxSkip;
      this.traceSkippedTx(frame);
      return;
    } else {
      // TODO: only if warmed stuff present
      this.txState = TxState.TxWarm;
    }

    this.processStateWarm();
    this.txState = TxState.TxInit;

    this.processStateInit();
    this.txState = TxState.TxExec;
  }

  private CallFrame currentFrame() {
    return this.callStack.top();
  }

  private void unlatchStack(MessageFrame stack, boolean failureState, boolean mxpx) {
    if (this.currentFrame().pending == null) {
      return;
    }

    List<BiFunction<BigInteger, Integer, Trace.TraceBuilder>> valHiSetters =
        List.of(
            this.trace::setPStackStackItemValueHi1At,
            this.trace::setPStackStackItemValueHi2At,
            this.trace::setPStackStackItemValueHi3At,
            this.trace::setPStackStackItemValueHi4At);
    List<BiFunction<BigInteger, Integer, Trace.TraceBuilder>> valLoSetters =
        List.of(
            this.trace::setPStackStackItemValueLo1At,
            this.trace::setPStackStackItemValueLo2At,
            this.trace::setPStackStackItemValueLo3At,
            this.trace::setPStackStackItemValueLo4At);

    boolean isFirstLine = true;
    StackContext pending = this.currentFrame().pending;

    for (StackLine line : this.currentFrame().pending.lines) {
      if (line.needsResult()) {
        EWord result = EWord.ZERO;
        if (!failureState) {
          result = EWord.of(stack.getStackItem(0));
        }

        int startLine = pending.startInTrace;
        // The fuck... first line is never expecting a value.
        //        if (isFirstLine) {
        //          line.setResult(result);
        //          // self.Mmu.handleMmu(...)
        //        }

        valHiSetters
            .get(line.resultColumn())
            .apply(result.hi().toUnsignedBigInteger(), startLine + line.ct());
        valLoSetters
            .get(line.resultColumn())
            .apply(result.lo().toUnsignedBigInteger(), startLine + line.ct());
      }
      isFirstLine = false;
    }
  }

  @Override
  public void traceContextStart(MessageFrame frame) {
    var type = CallFrameType.Root;
    this.callStack.enter(
        frame.getContractAddress(),
        frame.getCode(),
        type,
        frame.getValue(),
        frame.getRemainingGas(),
        this.trace.size(),
        frame.getInputData(),
        0,
        0);
  }

  @Override
  public void traceContextEnd(MessageFrame frame) {
    this.callStack.exit(this.trace.size() - 1, frame.getReturnData());
  }

  @Override
  public void trace(final MessageFrame frame) {
    if (this.txState == TxState.TxPreInit) {
      this.txInit(frame);
    }

    if (this.txState == TxState.TxSkip) {
      return;
    }

    this.processStateExec(frame);
  }

  public void tracePostExecution(MessageFrame frame, Operation.OperationResult operationResult) {
    boolean mxpx = false;
    this.unlatchStack(frame, false, mxpx);
    this.checkExceptions(frame, operationResult);
  }

  @Override
  public void traceStartBlock(final BlockHeader blockHeader, final BlockBody blockBody) {
    this.blockNumber++;
  }

  @Override
  public void traceStartConflation(long blockCount) {
    this.batchNumber++;
    this.callStack = new CallStack();
  }

  @Override
  public Object commit() {
    return new HubTrace(trace.build());
  }
}
