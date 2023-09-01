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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.zktracer.EWord;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.add.Add;
import net.consensys.linea.zktracer.module.ext.Ext;
import net.consensys.linea.zktracer.module.hub.callstack.CallFrame;
import net.consensys.linea.zktracer.module.hub.callstack.CallFrameType;
import net.consensys.linea.zktracer.module.hub.callstack.CallStack;
import net.consensys.linea.zktracer.module.hub.chunks.AccountChunk;
import net.consensys.linea.zktracer.module.hub.chunks.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.chunks.CommonChunk;
import net.consensys.linea.zktracer.module.hub.chunks.ContextChunk;
import net.consensys.linea.zktracer.module.hub.chunks.StackChunk;
import net.consensys.linea.zktracer.module.hub.chunks.StorageChunk;
import net.consensys.linea.zktracer.module.hub.chunks.TraceChunk;
import net.consensys.linea.zktracer.module.hub.defer.SkippedTransaction;
import net.consensys.linea.zktracer.module.hub.defer.TransactionDefer;
import net.consensys.linea.zktracer.module.hub.stack.StackContext;
import net.consensys.linea.zktracer.module.hub.stack.StackLine;
import net.consensys.linea.zktracer.module.mod.Mod;
import net.consensys.linea.zktracer.module.mul.Mul;
import net.consensys.linea.zktracer.module.shf.Shf;
import net.consensys.linea.zktracer.module.trm.Trm;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.opcode.OpCodeData;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.evm.account.Account;
import org.hyperledger.besu.evm.code.CodeV0;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.gascalculator.GasCalculator;
import org.hyperledger.besu.evm.gascalculator.LondonGasCalculator;
import org.hyperledger.besu.evm.internal.Words;
import org.hyperledger.besu.evm.operation.Operation;
import org.hyperledger.besu.plugin.data.BlockBody;
import org.hyperledger.besu.plugin.data.BlockHeader;

@Slf4j
public class Hub implements Module {
  private static final Address ADDRESS_ZERO = Address.fromHexString("0x0");
  private static final int TAU = 8;
  private static final GasCalculator gc = new LondonGasCalculator();

  public final Trace.TraceBuilder trace = Trace.builder();

  private int pc;
  private OpCode opCode;
  private int maxContextNumber;

  private OpCodeData opCodeData() {
    return this.opCode.getData();
  }

  public final Map<Address, Integer> deploymentNumber = new HashMap<>();

  public final int deploymentNumber(Address address) {
    return this.deploymentNumber.getOrDefault(address, 0);
  }

  private void increaseDeploymentNumber(Address address) {
    this.deploymentNumber.put(address, this.deploymentNumber(address) + 1);
  }

  final Map<Address, Boolean> isDeploying = new HashMap<>();

  public final boolean isDeploying(Address address) {
    return this.isDeploying.getOrDefault(address, false);
  }

  public final void markDeploying(Address address) {
    this.increaseDeploymentNumber(address);
    this.isDeploying.put(address, true);
  }

  public final void unmarkDeploying(Address address) {
    this.isDeploying.put(address, false);
  }
  // Tx -> Opcode -> [Chunks]
  private final List<List<List<TraceChunk>>> traceChunks = new ArrayList<>();

  int txChunksCount() {
    return this.traceChunks.size();
  }

  int opcodeChunksCount() {
    return this.traceChunks.get(this.txChunksCount() - 1).size();
  }

  List<List<TraceChunk>> currentTxChunks() {
    return this.traceChunks.get(this.txChunksCount() - 1);
  }

  List<TraceChunk> currentOpCodeChunks() {
    return this.traceChunks.get(this.txChunksCount() - 1).get(this.opcodeChunksCount() - 1);
  }

  void chunkNewTransaction() {
    this.traceChunks.add(new ArrayList<>());
  }

  void chunkNewOpcode() {
    this.currentTxChunks().add(new ArrayList<>());
  }

  public void addChunk(TraceChunk... chunks) {
    for (TraceChunk chunk : chunks) {
      this.currentOpCodeChunks().add(traceCommon());
      this.currentOpCodeChunks().add(chunk);
    }
  }

  private Exceptions exceptions;

  TxState txState;
  @Getter Transaction currentTx;
  CallStack callStack;
  int txNumber = 0;
  @Getter int batchNumber = 0;
  @Getter int blockNumber = 0;
  int stamp = 0;

  /** A list of latches deferred until the end of the current transaction */
  private final List<TransactionDefer> transactionLatches = new ArrayList<>();
  /** Defers a latch to be executed at the end of the current transaction. */
  private void deferPostTx(TransactionDefer latch) {
    this.transactionLatches.add(latch);
  }

  private final Module add;
  private final Module ext;
  private final Module mod;
  private final Module mul;
  private final Module shf;
  private final Module trm;
  private final Module wcp;

  public Hub(Add add, Ext ext, Mod mod, Mul mul, Shf shf, Trm trm, Wcp wcp) {
    this.add = add;
    this.ext = ext;
    this.mod = mod;
    this.mul = mul;
    this.shf = shf;
    this.trm = trm;
    this.wcp = wcp;
  }

  @Override
  public String jsonKey() {
    return "hub_v2_off";
  }

  @Override
  public final List<OpCode> supportedOpCodes() {
    // The Hub wants to catch all opcodes
    return List.of(OpCode.values());
  }

  public static boolean isPrecompile(Address to) {
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

  public static boolean isValidPrecompileCall(MessageFrame frame) {
    return switch (OpCode.of(frame.getCurrentOperation().getOpcode())) {
      case CALL, CALLCODE, STATICCALL, DELEGATECALL -> {
        if (frame.stackSize() < 2) {
          yield false; // invalid stack for a *CALL
        }

        Address targetAddress = Words.toAddress(frame.getStackItem(1));
        yield isPrecompile(targetAddress);
      }
      default -> false;
    };
  }

  /**
   * Traces a skipped transaction, i.e. a “pure” transaction without EVM execution.
   *
   * @param frame the frame of the transaction
   */
  void processStateSkip(MessageFrame frame) {
    this.stamp++;
    var world = frame.getWorldUpdater();
    boolean isDeployment = this.currentTx.getTo().isEmpty();

    //
    // 3 lines -- account changes
    //
    // From account information
    Address fromAddress = this.currentTx.getSender();
    AccountSnapshot oldFromAccount =
        AccountSnapshot.fromAccount(
            world.get(fromAddress), false, this.deploymentNumber(fromAddress), false);

    // To account information
    Address toAddress = effectiveToAddress(this.currentTx, fromAddress, 0);
    boolean toIsWarm =
        (fromAddress == toAddress)
            || isPrecompile(toAddress); // should never happen – no TX to PC allowed
    AccountSnapshot oldToAccount =
        AccountSnapshot.fromAccount(
            world.get(toAddress), toIsWarm, this.deploymentNumber(toAddress), false);

    // Miner account information
    Address minerAddress = frame.getMiningBeneficiary();
    boolean minerIsWarm =
        (minerAddress == fromAddress) || (minerAddress == toAddress) || isPrecompile(minerAddress);
    AccountSnapshot oldMinerAccount =
        AccountSnapshot.fromAccount(
            world.get(minerAddress), minerIsWarm, this.deploymentNumber(minerAddress), false);

    // Putatively update deployment number
    if (isDeployment) {
      this.increaseDeploymentNumber(toAddress);
    }

    this.deferPostTx(new SkippedTransaction(oldFromAccount, oldToAccount, oldMinerAccount));
  }

  public static BigInteger computeInitGas(Transaction tx) {
    boolean isDeployment = tx.getTo().isEmpty();
    return BigInteger.valueOf(
        tx.getGasLimit()
            - gc.transactionIntrinsicGasCost(tx.getPayload(), isDeployment)
            - tx.getAccessList().map(gc::accessListGasCost).orElse(0L));
  }

  /**
   * Fill the columns shared by all operations.
   *
   * @return a chunk representing the share columns
   */
  public TraceChunk traceCommon() {
    return new CommonChunk(
        this.txNumber,
        this.batchNumber,
        this.txState,
        this.stamp,
        0, // TODO
        false, // TODO
        this.opCodeData().instructionFamily(),
        this.exceptions.snapshot(),
        false, // TODO
        false, // TODO
        this.currentFrame().getContextNumber(),
        0, // TODO
        0, // TODO
        false, // TODO
        false, // TODO
        false, // TODO
        this.pc,
        0, // TODO
        this.currentFrame().addressAsEWord(),
        this.currentFrame().getCodeDeploymentNumber(),
        this.currentFrame().isCodeDeploymentStatus(),
        this.currentFrame().getAccountDeploymentNumber(),
        0,
        0,
        0,
        0,
        0, // TODO
        this.opCodeData().stackSettings().twoLinesInstruction(),
        false, // TODO
        0, // TODO
        0 // TODO
        );
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

  private CallFrame currentFrame() {
    return this.callStack.top();
  }

  private void handleStack(MessageFrame frame) {
    this.currentFrame().getStack().processInstruction(frame, this.currentFrame(), TAU * this.stamp);
    this.currentFrame().getPending().setStartInTrace(this.currentLine());
  }

  void triggerModules(MessageFrame frame) {
    switch (this.opCodeData().instructionFamily()) {
      case ADD -> {
        if (this.exceptions.noStackException()) {
          this.add.trace(frame);
        }
      }
      case MOD -> {
        if (this.exceptions.noStackException()) {
          this.mod.trace(frame);
        }
      }
      case MUL -> {
        if (this.exceptions.noStackException()) {
          this.mul.trace(frame);
        }
      }
      case EXT -> {
        if (this.exceptions.noStackException()) {
          this.ext.trace(frame);
        }
      }
      case WCP -> {
        if (this.exceptions.noStackException()) {
          this.wcp.trace(frame);
        }
      }
      case BIN -> {}
      case SHF -> {
        if (this.exceptions.noStackException()) {
          this.shf.trace(frame);
        }
      }
      case KEC -> {}
      case CONTEXT -> {}
      case ACCOUNT -> {}
      case COPY -> {}
      case TRANSACTION -> {}
      case BATCH -> {}
      case STACK_RAM -> {}
      case STORAGE -> {}
      case JUMP -> {}
      case MACHINE_STATE -> {}
      case PUSH_POP -> {}
      case DUP -> {}
      case SWAP -> {}
      case LOG -> {}
      case CREATE -> {}
      case CALL -> {}
      case HALT -> {}
      case INVALID -> {}
    }
  }

  void processStateExec(MessageFrame frame) {
    this.opCode = OpCode.of(frame.getCurrentOperation().getOpcode());
    this.pc = frame.getPC();
    this.stamp++;
    this.exceptions = Exceptions.fromFrame(frame);
    this.handleStack(frame);
    this.triggerModules(frame);

    if (this.currentFrame().getStack().isOk()) {
      /*
      this.handleCallReturnData
      this.handleMemory
      this.handleRam
       */
    }

    this.traceOperation(frame);
  }

  void processStateFinal() {
    this.stamp++;
    // if no revert: 2 account rows (sender, coinbase) + 1 tx row
    // otherwise 4 account rows (sender, coinbase, sender, recipient) + 1 tx row

    this.txState = TxState.TX_PRE_INIT;
  }

  /**
   * Compute the effective address of a transaction target, i.e. the specified target if explicitly
   * set, or the to-be-deployed address otherwise.
   *
   * @return the effective target address of tx
   */
  private static Address effectiveToAddress(Transaction tx, Address fromAddress, long fromNonce) {
    return tx.getTo().map(x -> (Address) x).orElse(Address.contractAddress(fromAddress, fromNonce));
  }

  @Override
  public void traceStartTx(Transaction tx) {
    this.chunkNewTransaction();
    if (tx.getTo().isPresent() && isPrecompile(tx.getTo().get())) {
      throw new RuntimeException("Call to precompile forbidden");
    } else {
      this.txNumber += 1;
    }
    this.currentTx = tx;

    // impossible to do the pre-init here -- missing information from the MessageFrame
    this.txState = TxState.TX_PRE_INIT;
  }

  @Override
  public void traceEndTx(final Bytes output, final long gasUsed) {
    this.txState = TxState.TX_FINAL;
    this.processStateFinal();

    for (TransactionDefer defer : this.transactionLatches) {
      defer.run(this, null, null); // TODO
    }
    this.transactionLatches.clear();
  }

  private void txInit(final MessageFrame frame) {
    // TODO: check that this actually does what it should
    if ((!frame.getRecipientAddress().equals(ADDRESS_ZERO)
            && frame.getCode().getSize() == 0) // pure transaction
        || (frame.getRecipientAddress().equals(ADDRESS_ZERO)
            && frame.getInputData().isEmpty())) { // contract creation without init code
      this.txState = TxState.TX_SKIP;
      this.processStateSkip(frame);
      return;
    }

    var fromAddress = this.currentTx.getSender();
    boolean isDeployment = this.currentTx.getTo().isEmpty();
    Address toAddress =
        effectiveToAddress(
            this.currentTx, fromAddress, frame.getWorldUpdater().get(fromAddress).getNonce());
    this.callStack =
        new CallStack(
            toAddress,
            isDeployment ? CallFrameType.INIT_CODE : CallFrameType.STANDARD,
            CodeV0.EMPTY_CODE, // TODO
            Wei.of(this.currentTx.getValue().getAsBigInteger()),
            this.currentTx.getGasLimit(),
            this.currentTx.getData().orElse(Bytes.EMPTY),
            this.maxContextNumber,
            0, // TODO
            toAddress.isEmpty() ? 0 : this.deploymentNumber.getOrDefault(toAddress, 0),
            false); // TODO

    if (false /* doWarm */) { // TODO
      this.processStateWarm();
    }
    this.processStateInit();
    this.txState = TxState.TX_STATE;
  }

  private void unlatchStack(MessageFrame frame, boolean mxpx) {
    if (this.currentFrame().getPending() == null) {
      return;
    }

    StackContext pending = this.currentFrame().getPending();
    for (StackLine line : pending.getLines()) {
      if (line.needsResult()) {
        EWord result = EWord.ZERO;
        if (!exceptions.any()) {
          result = EWord.of(frame.getStackItem(0));
        }

        // This works because we are certain that the stack chunks are the first.
        ((StackChunk) this.currentOpCodeChunks().get(2 * line.ct() + 1))
            .stackOps()
            .get(line.resultColumn() - 1)
            .setValue(result);
      }
    }
  }

  @Override
  public void traceContextEnter(MessageFrame frame) {
    this.maxContextNumber += 1;

    final boolean isDeployment = frame.getType() == MessageFrame.Type.CONTRACT_CREATION;
    final Address codeAddress = frame.getContractAddress();
    final CallFrameType frameType =
        frame.isStatic() ? CallFrameType.STATIC : CallFrameType.STANDARD;
    if (isDeployment) {
      this.markDeploying(codeAddress);
    }
    final int codeDeploymentNumber = this.deploymentNumber.getOrDefault(codeAddress, 0);
    this.callStack.enter(
        frame.getContractAddress(),
        frame.getCode(),
        frameType,
        frame.getValue(),
        frame.getRemainingGas(),
        this.trace.size(),
        frame.getInputData(),
        this.stamp + 1,
        this.deploymentNumber.getOrDefault(codeAddress, 0),
        codeDeploymentNumber,
        isDeployment);
  }

  @Override
  public void traceContextExit(MessageFrame frame) {
    unmarkDeploying(this.currentFrame().getCodeAddress());
    this.callStack.exit(this.trace.size() - 1, frame.getReturnData());
  }

  @Override
  public void trace(final MessageFrame frame) {
    if (this.txState == TxState.TX_PRE_INIT) {
      this.txInit(frame);
    }

    if (this.txState == TxState.TX_SKIP) {
      return;
    }

    this.processStateExec(frame);
  }

  public void tracePostExecution(MessageFrame frame, Operation.OperationResult operationResult) {
    boolean mxpx = false;
    this.unlatchStack(frame, mxpx);

    if (this.opCode.isCreate() && operationResult.getHaltReason() == null) {
      this.handleCreate(Address.wrap(frame.getStackItem(0)));
    }
  }

  private void handleCreate(Address target) {
    this.deploymentNumber.put(target, this.deploymentNumber.getOrDefault(target, 0) + 1);
  }

  @Override
  public void traceStartBlock(final BlockHeader blockHeader, final BlockBody blockBody) {
    this.blockNumber++;
  }

  @Override
  public void traceStartConflation(long blockCount) {
    this.batchNumber++;
  }

  @Override
  public void traceEndConflation() {
    this.retconChunks();
  }

  @Override
  public Object commit() {
    for (var txChunks : traceChunks) {
      for (List<TraceChunk> opChunks : txChunks) {
        for (TraceChunk chunk : opChunks) {
          chunk.trace(this.trace);
          this.trace.fillAndValidateRow();
        }
      }
    }
    return new HubTrace(trace.build());
  }

  void traceOperation(MessageFrame frame) {
    this.chunkNewOpcode();
    this.makeStackChunks();
    boolean updateReturnData =
        this.opCodeData().isHalt()
            || this.opCodeData().isInvalid()
            || exceptions.any()
            || isValidPrecompileCall(frame);

    switch (this.opCodeData().instructionFamily()) {
      case ADD,
          MOD,
          MUL,
          SHF,
          BIN,
          WCP,
          EXT,
          KEC,
          BATCH,
          MACHINE_STATE,
          PUSH_POP,
          DUP,
          SWAP,
          HALT,
          INVALID -> {}
      case CONTEXT, LOG -> {
        this.addChunk(new ContextChunk(this.callStack, this.currentFrame(), updateReturnData));
      }
      case ACCOUNT -> {
        if (this.opCodeData().stackSettings().flag1()) {
          this.addChunk(new ContextChunk(this.callStack, this.currentFrame(), updateReturnData));
          //          opChunks.add(new AccountChunk());
        } else {
          //          opChunks.add(new AccountChunk());
        }
      }
      case COPY -> {
        if (this.opCodeData().stackSettings().flag1()) {
          Address targetAddress =
              switch (this.opCode) {
                case CODECOPY -> this.currentFrame().getCodeAddress();
                case EXTCODECOPY -> Words.toAddress(frame.getStackItem(0));
                default -> {
                  throw new IllegalStateException("unexpected opcode");
                }
              };
          Account targetAccount = frame.getWorldUpdater().getAccount(targetAddress);
          AccountSnapshot accountSnapshot =
              AccountSnapshot.fromAccount(
                  targetAccount,
                  frame.isAddressWarm(targetAddress),
                  this.deploymentNumber(targetAddress),
                  this.isDeploying(targetAddress));

          this.addChunk(
              new AccountChunk(targetAddress, accountSnapshot, accountSnapshot, false, 0, false));
          //          this.addChunk(new AccountChunk());
        } else {
          this.addChunk(new ContextChunk(this.callStack, this.currentFrame(), updateReturnData));
        }
      }
      case TRANSACTION -> {
        //        opChunks.add(new TransactionChunk());
      }
      case STACK_RAM -> {
        if (this.opCodeData().stackSettings().flag2()) {
          this.addChunk(new ContextChunk(this.callStack, this.currentFrame(), updateReturnData));
        }
      }
      case STORAGE -> {
        this.addChunk(
            new ContextChunk(this.callStack, this.currentFrame(), updateReturnData),
            new StorageChunk());
      }
      case CREATE, CALL -> {
        this.addChunk(new ContextChunk(this.callStack, this.currentFrame(), updateReturnData));
        //        opChunks.add(new AccountChunk());
        //        opChunks.add(new AccountChunk());
        //        opChunks.add(new AccountChunk());
      }
      case JUMP -> {
        AccountSnapshot codeAccountSnapshot =
            AccountSnapshot.fromAccount(
                frame.getWorldUpdater().getAccount(this.currentFrame().getCodeAddress()),
                true,
                this.deploymentNumber(this.currentFrame().getCodeAddress()),
                this.currentFrame().isCodeDeploymentStatus());

        this.addChunk(
            new ContextChunk(this.callStack, this.currentFrame(), updateReturnData),
            new AccountChunk(
                this.currentFrame().getCodeAddress(),
                codeAccountSnapshot,
                codeAccountSnapshot,
                false,
                0,
                false));
      }
    }
  }

  void makeStackChunks() {
    if (this.currentFrame().getPending().getLines().isEmpty()) {
      for (int i = 0; i < (this.opCodeData().stackSettings().twoLinesInstruction() ? 2 : 1); i++) {
        this.addChunk(
            new StackChunk(
                this.currentFrame().getStack().snapshot(), new StackLine(i).asStackOperations()));
      }
    } else {
      for (StackLine line : this.currentFrame().getPending().getLines()) {
        this.addChunk(
            new StackChunk(this.currentFrame().getStack().snapshot(), line.asStackOperations()));
      }
    }
  }

  private void retconAccountChunk(AccountChunk chunk) {
    Address address = chunk.who();
    chunk.deploymentNumberInfnty(this.deploymentNumber(address)).existsInfinity(false);
    // TODO should be account != null; see with Besu team if we can get a view on
    // the state in traceEndConflation
  }

  private void retconChunks() {
    for (List<List<TraceChunk>> txChunk : this.traceChunks) {
      for (List<TraceChunk> opChunks : txChunk) {
        for (TraceChunk chunk : opChunks) {
          if (chunk instanceof AccountChunk) {
            this.retconAccountChunk((AccountChunk) chunk);
          }
        }
      }
    }
  }
}
