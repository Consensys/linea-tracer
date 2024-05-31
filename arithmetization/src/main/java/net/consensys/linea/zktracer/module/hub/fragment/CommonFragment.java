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

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.State;
import net.consensys.linea.zktracer.module.hub.Trace;
import net.consensys.linea.zktracer.module.hub.TransactionStack;
import net.consensys.linea.zktracer.module.hub.signals.AbortingConditions;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import net.consensys.linea.zktracer.module.hub.signals.FailureConditions;
import net.consensys.linea.zktracer.opcode.InstructionFamily;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import net.consensys.linea.zktracer.types.TxState;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.worldstate.WorldView;

import java.math.BigInteger;

@Accessors(fluent = true, chain = false)
@Builder
public final class CommonFragment implements TraceFragment {
  private final Hub hub;
  private final int txId;
  private final int batchNumber;
  private final TxState txState;
  private final State.TxState.Stamps stamps;
  private final InstructionFamily instructionFamily;
  private final Exceptions exceptions;
  private final AbortingConditions abortingConditions;
  private final FailureConditions failureConditions;
  private final int callFrameId;
  private final int callerContextNumber;
  @Getter private final int contextNumber;
  @Setter private int contextNumberNew;
  private final int revertStamp;
  @Getter final short height;
  @Getter final short heightNew;
  @Getter private final int pc;
  @Setter private int pcNew;
  private int codeDeploymentNumber;
  private final boolean codeDeploymentStatus;
  private final long gasExpected;
  private final long gasActual;
  private final long gasCost;
  private final long gasNext;
  @Getter private final long refundDelta;
  @Setter private long gasRefund;
  @Getter @Setter private boolean twoLineInstruction;
  @Getter @Setter private boolean twoLineInstructionCounter;
  @Getter @Setter private int numberOfNonStackRows;
  @Getter @Setter private int nonStackRowsCounter;

  public static CommonFragment fromHub(WorldView world, final Hub hub, final CallFrame frame, boolean tliCounter, int nonStackRowsCounter) {
    long refund = 0;
    boolean noStackException = hub.pch().exceptions().noStackException();

    if (noStackException) {
      refund = Hub.GAS_PROJECTOR.of(frame.frame(), hub.opCode()).refund();
    }

    int height = hub.currentFrame().stack().getHeight();
    int heightNew = (noStackException
            ? height - hub.opCode().getData().stackSettings().delta() + hub.opCode().getData().stackSettings().alpha()
            : 0);

    final int pc = frame.pc();
    final int pcNew = !noStackException
            ? computePcNew(hub, pc)
            : 0;

    return CommonFragment.builder()
            .hub(hub)
            .txId(hub.transients().tx().id())
            .batchNumber(hub.transients().conflation().number())
            .txState(hub.transients().tx().state())
            .stamps(hub.state.stamps())
            .instructionFamily(hub.opCodeData().instructionFamily())
            .exceptions(hub.pch().exceptions().snapshot())
            .abortingConditions(hub.pch().abortingConditions().snapshot())
            .failureConditions(hub.pch().failureConditions().snapshot())
            .callFrameId(frame.id())
            .contextNumber(frame.contextNumber())
            .contextNumberNew(hub.contextNumberNew(world))
            .pc(pc)
            .pcNew(pcNew)
            .height((short) height)
            .heightNew((short) heightNew)
            .codeDeploymentNumber(frame.codeDeploymentNumber())
            .codeDeploymentStatus(frame.underDeployment())
            .callerContextNumber(hub.callStack().getParentOf(frame.id()).contextNumber())
            .refundDelta(refund)
            .twoLineInstruction(hub.opCodeData().stackSettings().twoLinesInstruction())
            .twoLineInstructionCounter(tliCounter)
            .nonStackRowsCounter(nonStackRowsCounter)
            .build();
  }

  private static int computePcNew(final Hub hub, final int pc) {
    OpCode opCode = hub.opCode();

    if (opCode.getData().isPush()) {
      return pc + opCode.byteValue() - OpCode.PUSH1.byteValue() + 2;
    }

    if (opCode.getData().instructionFamily().equals(InstructionFamily.JUMP)) {
      BigInteger prospectivePcNew = hub.currentFrame().frame().getStackItem(0).toBigInteger();
      BigInteger codeSize = BigInteger.valueOf(hub.currentFrame().code().getSize());

      final int attemptedPcNew = codeSize.compareTo(prospectivePcNew) > 0
              ? prospectivePcNew.intValueExact()
              : 0;

      if (opCode.equals(OpCode.JUMP)) {
        return attemptedPcNew;
      }

      if (opCode.equals(OpCode.JUMPI)) {
        BigInteger condition = hub.currentFrame().frame().getStackItem(1).toBigInteger();
        if (!condition.equals(BigInteger.ZERO)) {
          return attemptedPcNew;
        }
      }
    }

    return pc + 1;
  }

  private int computeContextNumberNew(WorldView world, final Hub hub) {
    OpCode opCode = hub.opCode();
    if (exceptions.any()
            || opCode.getData().instructionFamily().equals(InstructionFamily.HALT)
            || opCode.getData().instructionFamily().equals(InstructionFamily.INVALID)){
      return callerContextNumber;
    }

    if (opCode.isCall()) {
      // If abortingConditions Then contextNumberNew <-- contextNumber
      Address calleeAddress = Address.extract((Bytes32) hub.currentFrame().frame().getStackItem(1));
      if (world.get(calleeAddress).hasCode()) {
        return 1 + hub.stamp();
      }
    }

    if (opCode.isCreate()) {
      // If (abortingConditions ∨ failureConditions) Then contextNumberNew <-- contextNumber
      final int initCodeSize = hub.currentFrame().frame().getStackItem(2).toInt();
      if (initCodeSize != 0) {
        return 1 + hub.stamp();
      }
    }

    return contextNumber;

  }

  public boolean txReverts() {
    return hub.txStack().getById(this.txId).status();
  }

  @Override
  public Trace trace(Trace trace) {
    throw new UnsupportedOperationException("should never be called");
  }

  public Trace trace(Trace trace, int stackHeight, int stackHeightNew) {
    final CallFrame frame = this.hub.callStack().getById(this.callFrameId);
    final TransactionStack.MetaTransaction tx = hub.txStack().getById(this.txId);
    // TODO: after ROMLex merge
    final int codeFragmentIndex = 0;
    //        frame.type() == CallFrameType.MANTLE
    //            ? 0
    //            : this.hub
    //                .romLex()
    //                .getCfiByMetadata(
    //                    ContractMetadata.make(
    //                        frame.codeAddress(),
    //                        frame.codeDeploymentNumber(),
    //                        frame.underDeployment()));
    final boolean selfReverts = frame.selfReverts();
    final boolean getsReverted = frame.getsReverted();


    return trace
            .absoluteTransactionNumber(tx.absNumber())
            .batchNumber(this.batchNumber)
            .txSkip(this.txState == TxState.TX_SKIP)
            .txWarm(this.txState == TxState.TX_WARM)
            .txInit(this.txState == TxState.TX_INIT)
            .txExec(this.txState == TxState.TX_EXEC)
            .txFinl(this.txState == TxState.TX_FINAL)
            .hubStamp(this.stamps.hub())
            .hubStampTransactionEnd(tx.endStamp())
            .contextMayChange(
                    this.txState == TxState.TX_EXEC
                            && ((instructionFamily == InstructionFamily.CALL
                            || instructionFamily == InstructionFamily.CREATE
                            || instructionFamily == InstructionFamily.HALT
                            || instructionFamily == InstructionFamily.INVALID)
                            || exceptions.any()))
            .exceptionAhoy(exceptions.any())
            .logInfoStamp(this.stamps.log())
            .mmuStamp(this.stamps.mmu())
            .mxpStamp(this.stamps.mxp())
            .contextNumber(contextNumber)
            .contextNumberNew(contextNumberNew) // TODO
            .callerContextNumber(callerContextNumber)
            .contextWillRevert(getsReverted || selfReverts)
            .contextGetsReverted(getsReverted)
            .contextSelfReverts(selfReverts)
            .contextRevertStamp(revertStamp)
            .codeFragmentIndex(codeFragmentIndex)
            .programCounter(pc)
            .programCounterNew(pcNew)
            .height((short) stackHeight)
            .heightNew((short) stackHeightNew)
            .gasExpected(gasExpected)
            .gasActual(gasActual)
            .gasCost(Bytes.ofUnsignedLong(gasCost))
            .gasNext(gasNext)
            .refundCounter(gasRefund)
            .refundCounterNew(gasRefund + refundDelta)
            .twoLineInstruction(twoLineInstruction)
            .counterTli(twoLineInstructionCounter)
            .nonStackRows((short) numberOfNonStackRows)
            .counterNsr((short) nonStackRowsCounter);
  }
}
