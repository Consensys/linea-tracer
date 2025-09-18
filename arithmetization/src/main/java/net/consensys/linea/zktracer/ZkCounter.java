/*
 * Copyright ConsenSys Inc.
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

package net.consensys.linea.zktracer;

import static net.consensys.linea.zktracer.Fork.*;
import static net.consensys.linea.zktracer.Trace.Oob.CT_MAX_CDL;
import static net.consensys.linea.zktracer.Trace.Oob.CT_MAX_RDC;
import static net.consensys.linea.zktracer.TraceCancun.Mxp.*;
import static net.consensys.linea.zktracer.TraceCancun.Oob.CT_MAX_CALL;
import static net.consensys.linea.zktracer.TraceCancun.Oob.CT_MAX_CREATE;
import static net.consensys.linea.zktracer.TraceCancun.Rlpaddr.MAX_CT_CREATE;
import static net.consensys.linea.zktracer.TraceCancun.Rlpaddr.MAX_CT_CREATE2;
import static net.consensys.linea.zktracer.module.ModuleName.*;
import static net.consensys.linea.zktracer.module.hub.section.AccountSection.NROWS_HUB_ACCOUNT;
import static net.consensys.linea.zktracer.module.hub.section.CallDataLoadSection.NROWS_HUB_CALLDATALOAD;
import static net.consensys.linea.zktracer.module.hub.section.McopySection.NROWS_HUB_MCOPY;
import static net.consensys.linea.zktracer.module.hub.section.SstoreSection.NROWS_HUB_STORAGE;
import static net.consensys.linea.zktracer.module.hub.section.StackOnlySection.NROWS_HUB_SIMPLE_STACK_OP;
import static net.consensys.linea.zktracer.module.hub.section.StackRamSection.NROWS_HUB_STACKRAM;
import static net.consensys.linea.zktracer.module.hub.section.call.CallSection.NROWS_HUB_CALL;
import static net.consensys.linea.zktracer.module.hub.section.copy.CallDataCopySection.NBROWS_HUB_CALL_DATA_COPY;
import static net.consensys.linea.zktracer.module.hub.section.copy.CodeCopySection.NBROWS_HUB_CODE_COPY;
import static net.consensys.linea.zktracer.module.hub.section.copy.ExtCodeCopySection.NBROWS_HUB_EXT_CODE_COPY;
import static net.consensys.linea.zktracer.module.hub.section.copy.ReturnDataCopySection.NBROWS_HUB_RETURN_DATA_COPY;
import static net.consensys.linea.zktracer.module.hub.section.create.CreateSection.NROWS_HUB_CREATE;
import static net.consensys.linea.zktracer.module.hub.section.finalization.TxFinalizationSection.NROWS_HUB_FINL;
import static net.consensys.linea.zktracer.module.hub.section.halt.RevertSection.NBROWS_HUB_REVERT;
import static net.consensys.linea.zktracer.module.hub.section.halt.StopSection.NBROWS_HUB_STOP_DEPLOYMENT;
import static net.consensys.linea.zktracer.module.hub.section.halt.StopSection.NBROWS_HUB_STOP_MSG_CALL;
import static net.consensys.linea.zktracer.module.hub.section.systemTransaction.EIP2935HistoricalHash.NROWS_HUB_SYSI_EIP2935;
import static net.consensys.linea.zktracer.module.hub.section.systemTransaction.EIP4788BeaconBlockRootSection.NROWS_HUB_SYSI_EIP4788;
import static net.consensys.linea.zktracer.module.hub.section.systemTransaction.SysfNoopSection.NROWS_HUB_SYSF_NOOP;
import static net.consensys.linea.zktracer.module.hub.section.transients.TLoadSection.NROWS_HUB_TLOAD;
import static net.consensys.linea.zktracer.module.hub.section.transients.TStoreSection.NROWS_HUB_TSTORE;
import static net.consensys.linea.zktracer.module.hub.section.txInitializationSection.TxInitializationSection.NROWS_HUB_INIT;
import static net.consensys.linea.zktracer.module.mxp.moduleOperation.CancunMxpOperation.MXP_FROM_CTMAX_TO_LINECOUNT;
import static net.consensys.linea.zktracer.opcode.OpCode.MSIZE;
import static net.consensys.linea.zktracer.runtime.stack.Stack.MAX_STACK_SIZE;
import static net.consensys.linea.zktracer.types.AddressUtils.isBlsPrecompile;
import static org.hyperledger.besu.datatypes.Address.*;

import java.util.*;

import net.consensys.linea.plugins.config.LineaL1L2BridgeSharedConfiguration;
import net.consensys.linea.zktracer.container.module.CountingOnlyModule;
import net.consensys.linea.zktracer.container.module.IncrementAndDetectModule;
import net.consensys.linea.zktracer.container.module.IncrementingModule;
import net.consensys.linea.zktracer.container.module.Module;
import net.consensys.linea.zktracer.module.add.Add;
import net.consensys.linea.zktracer.module.bin.Bin;
import net.consensys.linea.zktracer.module.exp.Exp;
import net.consensys.linea.zktracer.module.ext.Ext;
import net.consensys.linea.zktracer.module.hub.fragment.imc.exp.ExplogExpCall;
import net.consensys.linea.zktracer.module.hub.precompiles.ModexpMetadata;
import net.consensys.linea.zktracer.module.limits.BlockTransactions;
import net.consensys.linea.zktracer.module.limits.Keccak;
import net.consensys.linea.zktracer.module.limits.L1BlockSize;
import net.consensys.linea.zktracer.module.limits.precompiles.BlakeRounds;
import net.consensys.linea.zktracer.module.limits.precompiles.Sha256Blocks;
import net.consensys.linea.zktracer.module.mod.Mod;
import net.consensys.linea.zktracer.module.mul.Mul;
import net.consensys.linea.zktracer.module.oob.Oob;
import net.consensys.linea.zktracer.module.rlpUtils.RlpUtils;
import net.consensys.linea.zktracer.module.rlptxn.RlpTxn;
import net.consensys.linea.zktracer.module.rlptxn.cancun.CancunRlpTxn;
import net.consensys.linea.zktracer.module.shf.Shf;
import net.consensys.linea.zktracer.module.trm.Trm;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.opcode.OpCodeData;
import net.consensys.linea.zktracer.opcode.OpCodes;
import net.consensys.linea.zktracer.types.MemoryRange;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.internal.Words;
import org.hyperledger.besu.evm.log.Log;
import org.hyperledger.besu.evm.worldstate.WorldView;
import org.hyperledger.besu.plugin.data.BlockBody;
import org.hyperledger.besu.plugin.data.BlockHeader;

public class ZkCounter implements LineCountingTracer {
  private final OpCodes opCodes = OpCodes.load(FORK_IN_PROD);
  private static final Trace trace = getTraceFromFork(FORK_IN_PROD);

  // traced modules
  final Add add = new Add();
  final Bin bin = new Bin();
  final CountingOnlyModule blakemodexp =
      new CountingOnlyModule(BLAKE_MODEXP_DATA); // useless to count imho
  // final Blockdata blockData; //TODO
  final CountingOnlyModule blockHash =
      new CountingOnlyModule(BLOCK_HASH, trace.blockhash().spillage());
  final CountingOnlyModule blsdata = new CountingOnlyModule(BLS_DATA); // useless to count imho
  final CountingOnlyModule ecdata = new CountingOnlyModule(EC_DATA); // useless to count imho
  // euc //TODO need MMU to be counted
  final Exp exp = new Exp();
  final Ext ext = new Ext();
  final CountingOnlyModule gas = new CountingOnlyModule(GAS, trace.gas().spillage());
  final CountingOnlyModule hub = new CountingOnlyModule(HUB, trace.hub().spillage());
  final CountingOnlyModule logData = new CountingOnlyModule(LOG_DATA, trace.logdata().spillage());
  final CountingOnlyModule logInfo = new CountingOnlyModule(LOG_INFO, trace.loginfo().spillage());
  // mmio
  final CountingOnlyModule mmu = new CountingOnlyModule(MMU, trace.mmu().spillage());
  final Mod mod = new Mod();
  final Mul mul = new Mul();
  final CountingOnlyModule mxp = new CountingOnlyModule(MXP, trace.mxp().spillage());
  final Oob oob;
  final CountingOnlyModule rlpAddr = new CountingOnlyModule(RLP_ADDR, trace.rlpaddr().spillage());
  final RlpTxn rlpTxn;
  final CountingOnlyModule rlpTxnRcpt =
      new CountingOnlyModule(RLP_TXN_RCPT, trace.rlptxrcpt().spillage());
  final RlpUtils rlpUtils;
  // rom // TODO
  // rolex // TODO
  final CountingOnlyModule shakiradata =
      new CountingOnlyModule(SHAKIRA_DATA); // useless to count imho
  final Shf shf = new Shf();
  final IncrementingModule stp = new IncrementingModule(STP);
  final Trm trm;
  // final TxnData txnData; // TODO
  final Wcp wcp = new Wcp(); // TODO need MMU to be counted

  // precompiles limits:
  // related to EcData
  private final IncrementingModule ecAddEffectiveCall =
      new IncrementingModule(PRECOMPILE_ECADD_EFFECTIVE_CALLS);
  private final IncrementingModule ecMulEffectiveCall =
      new IncrementingModule(PRECOMPILE_ECMUL_EFFECTIVE_CALLS);
  private final IncrementingModule ecRecoverEffectiveCall =
      new IncrementingModule(PRECOMPILE_ECRECOVER_EFFECTIVE_CALLS);
  private final CountingOnlyModule ecPairingG2MembershipCalls =
      new CountingOnlyModule(PRECOMPILE_ECPAIRING_G2_MEMBERSHIP_CALLS);
  private final CountingOnlyModule ecPairingMillerLoops =
      new CountingOnlyModule(PRECOMPILE_ECPAIRING_MILLER_LOOPS);
  private final IncrementingModule ecPairingFinalExponentiations =
      new IncrementingModule(PRECOMPILE_ECPAIRING_FINAL_EXPONENTIATIONS);

  //  related to Modexp
  private final IncrementAndDetectModule modexpEffectiveCall =
      new IncrementAndDetectModule(PRECOMPILE_MODEXP_EFFECTIVE_CALLS);

  // related to Blake
  private final IncrementAndDetectModule blakeEffectiveCall =
      new IncrementAndDetectModule(PRECOMPILE_BLAKE_EFFECTIVE_CALLS);
  private final BlakeRounds blakeRounds = new BlakeRounds();

  // related to Shakira:
  private final Keccak keccak;
  private final Sha256Blocks sha256Blocks = new Sha256Blocks();
  private final IncrementAndDetectModule ripemdBlocks =
      new IncrementAndDetectModule(PRECOMPILE_RIPEMD_BLOCKS);

  // Related to Bls
  // TODO: remove me when Linea supports Cancun & Prague precompiles
  private final IncrementAndDetectModule pointEval = new IncrementAndDetectModule(POINT_EVAL) {};
  private final IncrementAndDetectModule bls = new IncrementAndDetectModule(BLS) {};

  final IncrementingModule pointEvaluationEffectiveCall =
      new IncrementingModule(PRECOMPILE_BLS_POINT_EVALUATION_EFFECTIVE_CALLS);
  final IncrementingModule pointEvaluationFailureCall =
      new IncrementingModule(PRECOMPILE_POINT_EVALUATION_FAILURE_EFFECTIVE_CALLS);
  final IncrementingModule blsG1AddEffectiveCall =
      new IncrementingModule(PRECOMPILE_BLS_G1_ADD_EFFECTIVE_CALLS);
  final IncrementingModule blsG1MsmEffectiveCall =
      new IncrementingModule(PRECOMPILE_BLS_G1_MSM_EFFECTIVE_CALLS);
  final IncrementingModule blsG2AddEffectiveCall =
      new IncrementingModule(PRECOMPILE_BLS_G2_ADD_EFFECTIVE_CALLS);
  final IncrementingModule blsG2MsmEffectiveCall =
      new IncrementingModule(PRECOMPILE_BLS_G2_MSM_EFFECTIVE_CALLS);
  final CountingOnlyModule blsPairingCheckMillerLoops =
      new CountingOnlyModule(PRECOMPILE_BLS_PAIRING_CHECK_MILLER_LOOPS);
  final IncrementingModule blsPairingCheckFinalExponentiations =
      new IncrementingModule(PRECOMPILE_BLS_FINAL_EXPONENTIATIONS);
  final IncrementingModule blsG1MapFpToG1EffectiveCall =
      new IncrementingModule(PRECOMPILE_BLS_MAP_FP_TO_G1_EFFECTIVE_CALLS);
  final IncrementingModule blsG1MapFp2ToG2EffectiveCall =
      new IncrementingModule(PRECOMPILE_BLS_MAP_FP2_TO_G2_EFFECTIVE_CALLS);
  final IncrementingModule blsC1MembershipCalls =
      new IncrementingModule(PRECOMPILE_BLS_C1_MEMBERSHIP_CHECKS);
  final IncrementingModule blsC2MembershipCalls =
      new IncrementingModule(PRECOMPILE_BLS_C2_MEMBERSHIP_CALLS);
  final IncrementingModule blsG1MembershipCalls =
      new IncrementingModule(PRECOMPILE_BLS_G1_MEMBERSHIP_CALLS);
  final IncrementingModule blsG2MembershipCalls =
      new IncrementingModule(PRECOMPILE_BLS_G2_MEMBERSHIP_CALLS);

  // others:
  private final BlockTransactions blockTransactions = new BlockTransactions();
  final L1BlockSize l1BlockSize;
  final IncrementingModule l2l1Logs = new IncrementingModule(BLOCK_L2_L1_LOGS);

  // all modules
  final List<Module> moduleToCount;

  public ZkCounter(LineaL1L2BridgeSharedConfiguration bridgeConfiguration) {
    this.trm = new Trm(PRAGUE, wcp);
    this.rlpUtils = new RlpUtils(wcp);
    this.rlpTxn = new CancunRlpTxn(rlpUtils, trm);
    this.oob = new Oob(null, add, mod, wcp); // TODO fix me
    this.keccak = new Keccak(ecRecoverEffectiveCall, blockTransactions);

    l1BlockSize =
        new L1BlockSize(l2l1Logs, bridgeConfiguration.contract(), bridgeConfiguration.topic());
    moduleToCount =
        List.of(
            add,
            bin,
            // blockData,
            blockHash,
            exp,
            ext,
            gas,
            hub,
            // logData,
            // logInfo,
            // mmu,
            mod,
            mul,
            // mxp,
            // oob,
            // rlpAddr,
            // rlpTxn,
            // rlpTxnRcpt,
            // rlpUtils,
            shf,
            stp,
            // trm,
            // wcp,
            modexpEffectiveCall,
            ripemdBlocks,
            blakeEffectiveCall,
            bls,
            pointEval,
            l1BlockSize,
            l2l1Logs);
  }

  @Override
  public void traceStartConflation(long numBlocksInConflation) {}

  @Override
  public void traceEndConflation(WorldView state) {}

  @Override
  public void traceStartBlock(
      final WorldView world,
      final BlockHeader blockHeader,
      final BlockBody blockBody,
      final Address miningBeneficiary) {
    l1BlockSize.traceStartBlock(world, blockHeader, miningBeneficiary);
    hub.updateTally(NROWS_HUB_SYSI_EIP4788);
    hub.updateTally(NROWS_HUB_SYSI_EIP2935);
    hub.updateTally(NROWS_HUB_SYSF_NOOP);

    commitTransactionBundle();
  }

  @Override
  public void traceEndTransaction(
      WorldView worldView,
      Transaction tx,
      boolean status,
      Bytes output,
      List<Log> logs,
      long gasUsed,
      Set<Address> selfDestructs,
      long timeNs) {
    switch (tx.getType()) {
      case FRONTIER, ACCESS_LIST, EIP1559 -> l1BlockSize.traceEndTx(tx, logs);
      case BLOB, DELEGATE_CODE -> throw new IllegalStateException(
          "Unsupported tx type: " + tx.getType());
    }
    // HUB line count
    hub.updateTally(NROWS_HUB_INIT + NROWS_HUB_FINL);
    if (tx.getAccessList().isPresent()) {
      final int nRowsWarmPhase =
          tx.getAccessList().get().stream()
              .mapToInt(listEntry -> listEntry.storageKeys().size() + 1)
              .sum();
      hub.updateTally(nRowsWarmPhase);
    }

    // other modules:
    blockTransactions.traceStartTx(null, null);
  }

  @Override
  public void traceContextExit(final MessageFrame frame) {
    hub.updateTally(1); // One context row in case of exception
    gas.updateTally(1); // One gas row in case of exception
  }

  @Override
  public void tracePreExecution(final MessageFrame frame) {
    final OpCodeData opcode = opCodes.of(frame.getCurrentOperation().getOpcode());

    // Check for SUX / SOX, this is the only exception we check for
    final short stackSize = (short) frame.stackSize();
    final short deleted = (short) opcode.stackSettings().delta();
    // TODO: final boolean underflow = wcp.callLT(stackSize, deleted);
    final boolean underflow = stackSize < deleted;
    if (underflow) {
      hub.updateTally(opcode.numberOfStackRows());
      return;
    }
    final short heightNew = (short) (stackSize + opcode.stackSettings().alpha() - deleted);
    // TODO: final boolean overflow = wcp.callGT(heightNew, MAX_STACK_SIZE);
    final boolean overflow = heightNew > MAX_STACK_SIZE;
    if (overflow) {
      hub.updateTally(opcode.numberOfStackRows());
      return;
    }

    // No stack exception, we can move on
    switch (opcode.instructionFamily()) {
      case PUSH_POP, DUP, SWAP, BATCH, INVALID -> hub.updateTally(NROWS_HUB_SIMPLE_STACK_OP);
      case ADD -> {
        hub.updateTally(NROWS_HUB_SIMPLE_STACK_OP);
        add.tracePreOpcode(frame, opcode.mnemonic());
      }
      case MOD -> {
        hub.updateTally(NROWS_HUB_SIMPLE_STACK_OP);
        mod.tracePreOpcode(frame, opcode.mnemonic());
      }
      case SHF -> {
        hub.updateTally(NROWS_HUB_SIMPLE_STACK_OP);
        shf.tracePreOpcode(frame, opcode.mnemonic());
      }
      case BIN -> {
        hub.updateTally(NROWS_HUB_SIMPLE_STACK_OP);
        bin.tracePreOpcode(frame, opcode.mnemonic());
      }
      case WCP -> {
        hub.updateTally(NROWS_HUB_SIMPLE_STACK_OP);
        // TODO wcp.tracePreOpcode(frame, opcode.mnemonic());
      }
      case EXT -> {
        hub.updateTally(NROWS_HUB_SIMPLE_STACK_OP);
        ext.tracePreOpcode(frame, opcode.mnemonic());
      }
      case MACHINE_STATE -> {
        hub.updateTally(NROWS_HUB_SIMPLE_STACK_OP);
        if (opcode.mnemonic() == MSIZE) {
          mxp.updateTally(CT_MAX_MSIZE + MXP_FROM_CTMAX_TO_LINECOUNT);
        }
      }
      case MUL -> {
        switch (opcode.mnemonic()) {
          case OpCode.EXP -> {
            hub.updateTally(NROWS_HUB_SIMPLE_STACK_OP + 1);
            exp.call(new ExplogExpCall(frame));
          }
          case OpCode.MUL -> {
            hub.updateTally(NROWS_HUB_SIMPLE_STACK_OP);
            mul.tracePreOpcode(frame, opcode.mnemonic());
          }
        }
      }
      case HALT -> {
        switch (opcode.mnemonic()) {
          case RETURN -> {}
          case REVERT -> {
            hub.updateTally(NBROWS_HUB_REVERT);
            mxp.updateTally(CT_MAX_UPDT_W + MXP_FROM_CTMAX_TO_LINECOUNT);
            // TODO MMU
          }
          case STOP -> hub.updateTally(
              frame.getType() == MessageFrame.Type.MESSAGE_CALL
                  ? NBROWS_HUB_STOP_MSG_CALL
                  : NBROWS_HUB_STOP_DEPLOYMENT);
          case SELFDESTRUCT -> {}
        }
      }
      case KEC -> {
        hub.updateTally(NROWS_HUB_SIMPLE_STACK_OP + 1);
        mxp.updateTally(CT_MAX_UPDT_W + MXP_FROM_CTMAX_TO_LINECOUNT);
        final int sizeToHash = Words.clampedToInt(frame.getStackItem(1));
        if (sizeToHash != 0) {
          // TODO MMU
          keccak.updateTally(sizeToHash);
        }
      }
      case CONTEXT, TRANSACTION -> hub.updateTally(NROWS_HUB_SIMPLE_STACK_OP + 1);
      case LOG -> {
        hub.updateTally(opcode.numberOfStackRows() + 2); // CON + MISC
        mxp.updateTally(CT_MAX_UPDT_W + MXP_FROM_CTMAX_TO_LINECOUNT);
        // TODO: MMU
        // Note: nothing to do for LOG info / data / rlp, done at the end of the tx
      }
      case ACCOUNT -> hub.updateTally(NROWS_HUB_ACCOUNT);
      case COPY -> {
        switch (opcode.mnemonic()) {
          case CALLDATACOPY -> {
            hub.updateTally(NBROWS_HUB_CALL_DATA_COPY);
            mxp.updateTally(CT_MAX_UPDT_W + MXP_FROM_CTMAX_TO_LINECOUNT);
            // TODO MMU
          }
          case RETURNDATACOPY -> {
            hub.updateTally(NBROWS_HUB_RETURN_DATA_COPY);
            oob.updateTally(CT_MAX_RDC + 1);
            mxp.updateTally(CT_MAX_UPDT_W + MXP_FROM_CTMAX_TO_LINECOUNT);
            // TODO MMU
          }
          case CODECOPY -> {
            hub.updateTally(NBROWS_HUB_CODE_COPY);
            mxp.updateTally(CT_MAX_UPDT_W + MXP_FROM_CTMAX_TO_LINECOUNT);
            // TODO MMU
            // TODO ROM
          }
          case EXTCODECOPY -> {
            hub.updateTally(NBROWS_HUB_EXT_CODE_COPY);
            mxp.updateTally(CT_MAX_UPDT_W + MXP_FROM_CTMAX_TO_LINECOUNT);
            // TODO MMU
            // TODO ROM
          }
        }
      }
      case MCOPY -> {
        hub.updateTally(NROWS_HUB_MCOPY);
        mxp.updateTally(CT_MAX_UPDT_W + MXP_FROM_CTMAX_TO_LINECOUNT);
        // TODO MMU
      }
      case STACK_RAM -> {
        switch (opcode.mnemonic()) {
          case CALLDATALOAD -> {
            hub.updateTally(NROWS_HUB_CALLDATALOAD);
            oob.updateTally(CT_MAX_CDL + 1);
            // TODO MMU
          }
          case MSTORE, MLOAD -> {
            hub.updateTally(NROWS_HUB_STACKRAM);
            mxp.updateTally(CT_MAX_UPDT_W + MXP_FROM_CTMAX_TO_LINECOUNT);
            // TODO MMU
          }
          case MSTORE8 -> {
            hub.updateTally(NROWS_HUB_STACKRAM);
            mxp.updateTally(CT_MAX_UPDT_B + MXP_FROM_CTMAX_TO_LINECOUNT);
            // TODO MMU
          }
        }
      }
      case STORAGE -> hub.updateTally(NROWS_HUB_STORAGE);
      case TRANSIENT -> {
        switch (opcode.mnemonic()) {
          case TLOAD -> hub.updateTally(NROWS_HUB_TLOAD);
          case TSTORE -> hub.updateTally(NROWS_HUB_TSTORE);
        }
      }
      case JUMP -> {} // TODO
      case CREATE -> {
        hub.updateTally(NROWS_HUB_CREATE);
        // first IMC
        stp.updateTally(1);
        mxp.updateTally(CT_MAX_UPDT_W + MXP_FROM_CTMAX_TO_LINECOUNT);
        oob.updateTally(CT_MAX_CREATE + 1);
        // TODO: MMU
        final boolean isCreate2 = opcode.mnemonic() == OpCode.CREATE2;
        rlpAddr.updateTally(1 + (isCreate2 ? MAX_CT_CREATE2 : MAX_CT_CREATE));
        if (isCreate2) {
          final int size = Words.clampedToInt(frame.getStackItem(2));
          keccak.updateTally(size);
        }
      }
      case CALL -> {
        hub.updateTally(NROWS_HUB_CALL);
        gas.updateTally(1); // as CMC == 1
        oob.updateTally(CT_MAX_CALL + 1);
        mxp.updateTally(CT_MAX_UPDT_W + MXP_FROM_CTMAX_TO_LINECOUNT);
        stp.updateTally(1);
      }
      default -> throw new IllegalArgumentException("Unknown opcode: " + opcode.byteValue());
    }
  }

  @Override
  public void tracePrecompileCall(MessageFrame frame, long gasRequirement, Bytes output) {
    final Address precompileAddress = frame.getContractAddress();

    if (precompileAddress.equals(Address.MODEXP)) {
      final Bytes callData = frame.getInputData();
      final MemoryRange memoryRange = new MemoryRange(0, 0, callData.size(), callData);
      final ModexpMetadata modexpMetadata = new ModexpMetadata(memoryRange);
      if (modexpMetadata.unprovableModexp()) {
        modexpEffectiveCall.detectEvent();
      }
      return;
    }

    if (precompileAddress.equals(KZG_POINT_EVAL)) {
      pointEval.detectEvent();
      return;
    }

    if (isBlsPrecompile(precompileAddress)) {
      bls.detectEvent();
      return;
    }

    if (precompileAddress.equals(RIPEMD160)) {
      // We COULD accept empty input data, as it implies no gnark circuit, so nothing to detect. We
      // don't do it for simplicity.
      // if (frame.getInputData().isEmpty()) {
      //   return;
      // }
      ripemdBlocks.detectEvent();
      return;
    }

    if (precompileAddress.equals(BLAKE2B_F_COMPRESSION)) {
      blakeEffectiveCall.detectEvent();
      return;
    }
    // No other precompiles are tracked
  }

  /** When called, erase all tracing related to the bundle of all transactions since the last. */
  @Override
  public void popTransactionBundle() {
    for (Module m : moduleToCount) {
      m.popTransactionBundle();
    }
  }

  @Override
  public void commitTransactionBundle() {
    for (Module m : moduleToCount) {
      m.commitTransactionBundle();
    }
  }

  @Override
  public Map<String, Integer> getModulesLineCount() {
    final HashMap<String, Integer> modulesLineCount = HashMap.newHashMap(moduleToCount.size());

    for (Module m : moduleToCount) {
      modulesLineCount.put(m.moduleKey(), m.lineCount() + m.spillage(trace));
    }
    return modulesLineCount;
  }

  @Override
  public List<Module> getModulesToCount() {
    return moduleToCount;
  }
}
