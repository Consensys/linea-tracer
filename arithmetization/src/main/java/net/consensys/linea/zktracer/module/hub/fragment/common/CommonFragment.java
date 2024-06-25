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

package net.consensys.linea.zktracer.module.hub.fragment.common;

import static net.consensys.linea.zktracer.module.hub.HubProcessingPhase.TX_EXEC;

import java.math.BigInteger;
import java.util.function.Supplier;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.ZkTracer;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.HubProcessingPhase;
import net.consensys.linea.zktracer.module.hub.Trace;
import net.consensys.linea.zktracer.module.hub.fragment.TraceFragment;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import net.consensys.linea.zktracer.types.EWord;
import net.consensys.linea.zktracer.types.TransactionProcessingMetadata;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.apache.tuweni.units.bigints.UInt256;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.evm.EVM;
import org.hyperledger.besu.evm.EvmSpecVersion;
import org.hyperledger.besu.evm.internal.EvmConfiguration;
import org.hyperledger.besu.evm.operation.Operation;
import org.hyperledger.besu.evm.operation.OperationRegistry;
import org.hyperledger.besu.evm.operation.SelfDestructOperation;
import org.hyperledger.besu.evm.worldstate.WorldView;

@Accessors(fluent = true, chain = false)
@RequiredArgsConstructor
public final class CommonFragment implements TraceFragment {

  private final CommonFragmentValues commonFragmentValues;
  private final int nonStackRowsCounter;
  private final boolean twoLineInstructionCounter;

  public CommonFragment(
      CommonFragmentValues commonValues, int stackLineCounter, int nonStackLineCounter) {
    this.commonFragmentValues = commonValues;
    this.twoLineInstructionCounter = stackLineCounter == 1;
    this.nonStackRowsCounter = nonStackLineCounter;
  }

  /* public static CommonFragment fromHub(
      final Hub hub,
      final CallFrame callFrame,
      boolean counterTli,
      int counterNsr,
      int numberOfNonStackRows) {

    final boolean noStackException = hub.pch().exceptions().noStackException();
    final long refundDelta =
        noStackException ? Hub.GfAS_PROJECTOR.of(callFrame.frame(), hub.opCode()).refund() : 0;

    // TODO: partial solution, will not work in general
    final long gasExpected = hub.expectedGas();
    final long gasActual = hub.remainingGas();

    //    final boolean gasCostComputationIsRequired =
    //            (hub.state.getProcessingPhase() == HubProcessingPhase.TX_EXEC)
    //                    & noStackException
    //                    & (hub.pch().exceptions().outOfGasException() ||
    // hub.pch().exceptions().none());
    //    final long gasCost =
    //            gasCostComputationIsRequired
    //                    ? CommonFragment.computeGasCost(hub, callFrame.frame().getWorldUpdater())
    //                    : 0;
    //
    //    final long gasNext =
    //        hub.pch().exceptions().any()
    //            ? 0
    //            : Math.max(
    //                gasActual - gasCost, 0); // TODO: ugly, to fix just to not trace negative
    // value

    final int height = hub.currentFrame().stack().getHeight();
    final int heightNew =
        (noStackException
            ? height
                - hub.opCode().getData().stackSettings().delta()
                + hub.opCode().getData().stackSettings().alpha()
            : 0);
    final boolean hubInExecPhase = hub.state.getProcessingPhase() == HubProcessingPhase.TX_EXEC;
    final int pc = hubInExecPhase ? callFrame.pc() : 0;
    final int pcNew = computePcNew(hub, pc, noStackException, hubInExecPhase);

    return CommonFragment.builder()
        .hub(hub)
        .absoluteTransactionNumber(hub.txStack().current().getAbsoluteTransactionNumber())
        .relativeBlockNumber(hub.txStack().current().getRelativeBlockNumber())
        .hubProcessingPhase(hub.state.getProcessingPhase())
        .stamps(hub.state.stamps().snapshot())
        .instructionFamily(hub.opCodeData().instructionFamily())
        .exceptions(hub.pch().exceptions().snapshot())
        .abortingConditions(hub.pch().abortingConditions().snapshot())
        .failureConditions(hub.pch().failureConditions().snapshot())
        .callFrameId(callFrame.id())
        .contextNumber(hubInExecPhase ? callFrame.contextNumber() : 0)
        .contextNumberNew(hub.contextNumberNew(callFrame))
        .pc(pc)
        .pcNew(pcNew)
        .height((short) height)
        .heightNew((short) heightNew)
        .codeDeploymentNumber(callFrame.codeDeploymentNumber())
        .codeDeploymentStatus(callFrame.isDeployment())
        .gasExpected(gasExpected)
        .gasActual(gasActual)
        //        .gasCost(gasCost)
        //        .gasNext(gasNext)
        .callerContextNumber(hub.callStack().getParentOf(callFrame.id()).contextNumber())
        .refundDelta(refundDelta)
        .twoLineInstruction(hub.opCodeData().stackSettings().twoLinesInstruction())
        .twoLineInstructionCounter(counterTli)
        .nonStackRowsCounter(counterNsr)
        .numberOfNonStackRows(numberOfNonStackRows)
        .build();
  }
   */

  static int computePcNew(
      final Hub hub, final int pc, boolean noStackException, boolean hubInExecPhase) {
    OpCode opCode = hub.opCode();
    if (!(noStackException && hubInExecPhase)) {
      return 0;
    }

    if (opCode.getData().isPush()) {
      return pc + opCode.byteValue() - OpCode.PUSH1.byteValue() + 2;
    }

    if (opCode.isJump()) {
      final BigInteger prospectivePcNew =
          hub.currentFrame().frame().getStackItem(0).toUnsignedBigInteger();
      final BigInteger codeSize = BigInteger.valueOf(hub.currentFrame().code().getSize());

      final int attemptedPcNew =
          codeSize.compareTo(prospectivePcNew) > 0 ? prospectivePcNew.intValueExact() : 0;

      if (opCode.equals(OpCode.JUMP)) {
        return attemptedPcNew;
      }

      if (opCode.equals(OpCode.JUMPI)) {
        BigInteger condition = hub.currentFrame().frame().getStackItem(1).toUnsignedBigInteger();
        if (!condition.equals(BigInteger.ZERO)) {
          return attemptedPcNew;
        }
      }
    }
    ;

    return pc + 1;
  }

  public boolean txReverts() {
    return commonFragmentValues.txMetadata.statusCode();
  }

  public Trace trace(Trace trace) {
    final CallFrame frame = commonFragmentValues.callFrame;
    final TransactionProcessingMetadata tx = commonFragmentValues.txMetadata;

    return trace
        .absoluteTransactionNumber(tx.getAbsoluteTransactionNumber())
        .batchNumber(tx.getRelativeBlockNumber())
        .txSkip(commonFragmentValues.hubProcessingPhase == HubProcessingPhase.TX_SKIP)
        .txWarm(commonFragmentValues.hubProcessingPhase == HubProcessingPhase.TX_WARM)
        .txInit(commonFragmentValues.hubProcessingPhase == HubProcessingPhase.TX_INIT)
        .txExec(commonFragmentValues.hubProcessingPhase == HubProcessingPhase.TX_EXEC)
        .txFinl(commonFragmentValues.hubProcessingPhase == HubProcessingPhase.TX_FINAL)
        .hubStamp(commonFragmentValues.hubStamp)
        .hubStampTransactionEnd(tx.getHubStampTransactionEnd())
        .contextMayChange(commonFragmentValues.contextMayChange)
        .exceptionAhoy(commonFragmentValues.exceptionAhoy)
        .logInfoStamp(commonFragmentValues.logStamp)
        .mmuStamp(commonFragmentValues.stamps.mmu())
        .mxpStamp(commonFragmentValues.stamps.mxp())
        // nontrivial dom / sub are traced in storage or account fragments only
        .contextNumber(
            commonFragmentValues.hubProcessingPhase == TX_EXEC ? frame.contextNumber() : 0)
        .contextNumberNew(commonFragmentValues.contextNumberNew)
        .callerContextNumber(
            commonFragmentValues.callStack.getById(frame.parentFrame()).contextNumber())
        .contextWillRevert(frame.willRevert())
        .contextGetsReverted(frame.getsReverted())
        .contextSelfReverts(frame.selfReverts())
        .contextRevertStamp(commonFragmentValues.cnRevertStamp)
        .codeFragmentIndex(commonFragmentValues.codeFragmentIndex)
        .programCounter(commonFragmentValues.pc)
        .programCounterNew(commonFragmentValues.pcNew)
        .height(commonFragmentValues.height)
        .heightNew(commonFragmentValues.heightNew)
        // peeking flags are traced in the respective fragments
        .gasExpected(commonFragmentValues.gasExpected)
        .gasActual(commonFragmentValues.gasActual)
        .gasCost(Bytes.ofUnsignedLong(commonFragmentValues.gasCost))
        .gasNext(commonFragmentValues.gasNext)
        .refundCounter(commonFragmentValues.gasRefund)
        .refundCounterNew(commonFragmentValues.gasRefundNew)
        .twoLineInstruction(commonFragmentValues.TLI)
        .counterTli(twoLineInstructionCounter)
        .nonStackRows((short) commonFragmentValues.numberOfNonStackRows)
        .counterNsr((short) nonStackRowsCounter);
  }

  static long computeGasCost(Hub hub, WorldView world) {

    switch (hub.opCodeData().instructionFamily()) {
      case ADD, MOD, SHF, BIN, WCP, EXT, BATCH, MACHINE_STATE, PUSH_POP, DUP, SWAP, INVALID -> {
        if (hub.pch().exceptions().outOfGasException() || hub.pch().exceptions().none()) {
          return hub.opCode().getData().stackSettings().staticGas().cost();
        }
        return 0;
      }
      case STORAGE -> {
        switch (hub.opCode()) {
          case SSTORE -> {
            return gasCostSstore(hub, world);
          }
          case SLOAD -> {
            return gasCostSload(hub, world);
          }
          default -> throw new RuntimeException(
              "Gas cost not covered for " + hub.opCode().toString());
        }
      }
      case HALT -> {
        switch (hub.opCode()) {
          case STOP -> {
            return 0;
          }
          case RETURN, REVERT -> {
            Bytes offset = hub.messageFrame().getStackItem(0);
            Bytes size = hub.messageFrame().getStackItem(0);
            return hub.pch().exceptions().memoryExpansionException()
                ? 0
                : ZkTracer.gasCalculator.memoryExpansionGasCost(
                    hub.messageFrame(), offset.toLong(), size.toLong());
          }
          case SELFDESTRUCT -> {
            SelfDestructOperation op = new SelfDestructOperation(ZkTracer.gasCalculator);
            Operation.OperationResult operationResult =
                op.execute(
                    hub.messageFrame(),
                    new EVM(
                        new OperationRegistry(),
                        ZkTracer.gasCalculator,
                        EvmConfiguration.DEFAULT,
                        EvmSpecVersion.LONDON));
            long gasCost = operationResult.getGasCost();
            Address recipient = Address.extract((Bytes32) hub.messageFrame().getStackItem(0));
            Wei inheritance = world.get(hub.messageFrame().getRecipientAddress()).getBalance();
            return ZkTracer.gasCalculator.selfDestructOperationGasCost(
                world.get(recipient), inheritance);
          }
        }
        return 0;
      }
      default -> {
        throw new RuntimeException("Gas cost not covered for " + hub.opCode().toString());
      }
    }
  }

  static long gasCostSstore(Hub hub, WorldView world) {

    final Address address = hub.currentFrame().accountAddress();
    final EWord storageKey = EWord.of(hub.messageFrame().getStackItem(0));

    final UInt256 storageKeyUint256 = UInt256.fromBytes(hub.messageFrame().getStackItem(0));
    final UInt256 valueNextUint256 = UInt256.fromBytes(hub.messageFrame().getStackItem(1));

    final Supplier<UInt256> valueCurrentSupplier =
        () -> world.get(address).getStorageValue(storageKeyUint256);
    final Supplier<UInt256> valueOriginalSupplier =
        () -> world.get(address).getOriginalStorageValue(storageKeyUint256);

    final long storageCost =
        ZkTracer.gasCalculator.calculateStorageCost(
            valueNextUint256, valueCurrentSupplier, valueOriginalSupplier);
    final boolean storageSlotWarmth = hub.currentFrame().frame().isStorageWarm(address, storageKey);

    return storageCost + (storageSlotWarmth ? 0L : ZkTracer.gasCalculator.getColdSloadCost());
  }

  static long gasCostSload(Hub hub, WorldView world) {
    final Address address = hub.currentFrame().accountAddress();
    final EWord storageKey = EWord.of(hub.messageFrame().getStackItem(0));
    final boolean storageSlotWarmth = hub.currentFrame().frame().isStorageWarm(address, storageKey);

    return ZkTracer.gasCalculator.getSloadOperationGasCost()
        + (storageSlotWarmth
            ? ZkTracer.gasCalculator.getWarmStorageReadCost()
            : ZkTracer.gasCalculator.getColdSloadCost());
  }
}
