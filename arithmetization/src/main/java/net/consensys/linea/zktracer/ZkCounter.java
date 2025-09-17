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
import static net.consensys.linea.zktracer.TraceCancun.Hub.*;
import static net.consensys.linea.zktracer.TraceCancun.Mxp.CT_MAX_MSIZE;
import static net.consensys.linea.zktracer.TraceCancun.Mxp.CT_MAX_MXPX;
import static net.consensys.linea.zktracer.TraceCancun.Oob.CT_MAX_CALL;
import static net.consensys.linea.zktracer.TracePrague.Hub.NROWS_HUB_SYSI_EIP2935;
import static net.consensys.linea.zktracer.TracePrague.Hub.NROWS_HUB_SYSI_EIP4788;
import static net.consensys.linea.zktracer.module.ModuleName.*;
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
import net.consensys.linea.zktracer.module.ext.Ext;
import net.consensys.linea.zktracer.module.hub.precompiles.ModexpMetadata;
import net.consensys.linea.zktracer.module.hub.section.*;
import net.consensys.linea.zktracer.module.limits.L1BlockSize;
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
  // blakemodexp
  // final Blockdata blockData; //TODO
  final CountingOnlyModule blockHash =
      new CountingOnlyModule(BLOCK_HASH, trace.blockhash().spillage());
  // blsdata
  // ecdata
  // euc
  // final Exp exp = new Exp(); // TODO
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
  // shakiradata
  final Shf shf = new Shf();
  // stp // TODO
  final Trm trm;
  // final TxnData txnData; // TODO
  final Wcp wcp = new Wcp();

  // counting only modules
  // TODO ...
  final IncrementAndDetectModule modexp =
      new IncrementAndDetectModule(PRECOMPILE_MODEXP_EFFECTIVE_CALLS) {};
  final IncrementAndDetectModule rip = new IncrementAndDetectModule(PRECOMPILE_RIPEMD_BLOCKS) {};
  final IncrementAndDetectModule blake =
      new IncrementAndDetectModule(PRECOMPILE_BLAKE_EFFECTIVE_CALLS) {};
  final IncrementAndDetectModule pointEval = new IncrementAndDetectModule(POINT_EVAL);
  final IncrementAndDetectModule bls = new IncrementAndDetectModule(BLS);
  final L1BlockSize l1BlockSize;
  final IncrementingModule l2l1Logs = new IncrementingModule(BLOCK_L2_L1_LOGS);
  final List<Module> moduleToCount;

  public ZkCounter(LineaL1L2BridgeSharedConfiguration bridgeConfiguration) {
    this.trm = new Trm(PRAGUE, wcp);
    this.rlpUtils = new RlpUtils(wcp);
    this.rlpTxn = new CancunRlpTxn(rlpUtils, trm);
    this.oob = new Oob(null, add, mod, wcp); // TODO fix me

    l1BlockSize =
        new L1BlockSize(l2l1Logs, bridgeConfiguration.contract(), bridgeConfiguration.topic());
    moduleToCount =
        List.of(
            add,
            bin,
            // blockData,
            blockHash,
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
            // trm,
            // wcp,
            modexp,
            rip,
            blake,
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
    hub.updateTally(NROWS_HUB_INIT + NROWS_HUB_FINL);
    if (tx.getAccessList().isPresent()) {
      final int nRowsWarmPhase =
          tx.getAccessList().get().stream()
              .mapToInt(listEntry -> listEntry.storageKeys().size() + 1)
              .sum();
      hub.updateTally(nRowsWarmPhase);
    }
  }

  @Override
  public void traceContextExit(final MessageFrame frame) {
    hub.updateTally(1); // One context row in case of exception
    gas.updateTally(1); // One gas row in case of exception
  }

  @Override
  public void tracePreExecution(final MessageFrame frame) {
    final OpCodeData opcode = opCodes.of(frame.getCurrentOperation().getOpcode());
    final short stackSize = (short) frame.stackSize();
    final short deleted = (short) opcode.stackSettings().delta();

    final boolean underflow = wcp.callLT(stackSize, deleted);
    if (underflow) {
      hub.updateTally(NROWS_HUB_SUXSOX_INVALID);
      return;
    }
    final short heightNew = (short) (stackSize + opcode.stackSettings().alpha() - deleted);
    final boolean overflow = wcp.callGT(heightNew, MAX_STACK_SIZE);
    if (overflow) {
      hub.updateTally(NROWS_HUB_SUXSOX_INVALID);
      return;
    }

    switch (opcode.instructionFamily()) {
      case PUSH_POP, DUP, SWAP, BATCH -> hub.updateTally(NROWS_HUB_SIMPLE_STACK_OP);
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
        wcp.tracePreOpcode(frame, opcode.mnemonic());
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
            // TODO EXP once exp asm is merged
          }
          case OpCode.MUL -> {
            hub.updateTally(NROWS_HUB_SIMPLE_STACK_OP);
            mul.tracePreOpcode(frame, opcode.mnemonic());
          }
        }
      }
      case HALT -> {} // TODO
      case KEC -> {} // TODO
      case CONTEXT, TRANSACTION -> hub.updateTally(NROWS_HUB_SIMPLE_STACK_OP + 1);
      case LOG -> {} // TODO
      case ACCOUNT -> {} // TODO
      case COPY -> {} // TODO
      case MCOPY -> {
        hub.updateTally(NROWS_HUB_MCOPY);
        // TODO MMU
      }
      case STACK_RAM -> {} // TODO
      case STORAGE -> hub.updateTally(NROWS_HUB_STORAGE);
      case TRANSIENT -> {
        switch (opcode.mnemonic()) {
          case TLOAD -> hub.updateTally(NROWS_HUB_TLOAD);
          case TSTORE -> hub.updateTally(NROWS_HUB_TSTORE);
        }
      }
      case JUMP -> {} // TODO
      case CREATE -> {} // TODO
      case CALL -> {
        hub.updateTally(11); // 2 stack + up to 9 for SMC failure will revert
        // Note: in case of precompile call, we'll add more rows, done in tracePrecompileCall
        gas.updateTally(1); // as CMC == 1
        oob.updateTally(CT_MAX_CALL + 1);
        mxp.updateTally(CT_MAX_MXPX + MXP_FROM_CTMAX_TO_LINECOUNT);
        // TODO STP
      }
      case INVALID -> hub.updateTally(NROWS_HUB_SUXSOX_INVALID);
      default -> throw new UnsupportedOperationException("not yet implemented");
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
        modexp.detectEvent();
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
      rip.detectEvent();
      return;
    }

    if (precompileAddress.equals(BLAKE2B_F_COMPRESSION)) {
      blake.detectEvent();
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
