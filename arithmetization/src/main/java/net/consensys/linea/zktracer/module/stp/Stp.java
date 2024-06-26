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

package net.consensys.linea.zktracer.module.stp;

import static java.lang.Long.max;
import static net.consensys.linea.zktracer.types.AddressUtils.getDeploymentAddress;
import static net.consensys.linea.zktracer.types.Conversions.longToBytes32;

import java.nio.MappedByteBuffer;
import java.util.List;

import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.container.stacked.set.StackedSet;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.StpCall;
import net.consensys.linea.zktracer.module.mod.Mod;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.opcode.gas.GasConstants;
import net.consensys.linea.zktracer.types.EWord;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.internal.Words;

@RequiredArgsConstructor
public class Stp implements Module {
  private final Hub hub;
  private final Wcp wcp;
  private final Mod mod;

  public void call(StpCall stpCall, MessageFrame messageFrame) {
    this.operations.add(new StpOperation(stpCall));
  }

  @Override
  public String moduleKey() {
    return "STP";
  }

  private final StackedSet<StpOperation> operations = new StackedSet<>();

  @Override
  public void enterTransaction() {
    this.operations.enter();
  }

  @Override
  public void popTransaction() {
    this.operations.pop();
  }

  @Override
  public int lineCount() {
    return this.operations.lineCount();
  }

  @Override
  public List<ColumnHeader> columnsHeaders() {
    return Trace.headers(this.lineCount());
  }

  @Override
  public void tracePreOpcode(final MessageFrame frame) {
    OpCode opCode = hub.opCode();

    switch (opCode) {
      case CREATE, CREATE2 -> {
        final StpOperation stpOperation = stpOperationForCreate(frame);
        StpCall stpCall = stpOperation.stpCall();
        this.operations.add(stpOperation);
        this.wcp.callLT(longToBytes32(stpCall.gasActual()), Bytes32.ZERO);
        this.wcp.callLT(longToBytes32(stpCall.gasActual()), longToBytes32(stpCall.upfrontGasCost()));
        if (!stpCall.outOfGasException()) {
          this.mod.callDIV(longToBytes32(stpOperation.getGDiff()), longToBytes32(64L));
        }
      }
      case CALL, CALLCODE, DELEGATECALL, STATICCALL -> {
        final StpOperation stpOperation = stpOperationForCall(frame);
        StpCall stpCall = stpOperation.stpCall();
        this.operations.add(stpOperation);
        this.wcp.callLT(longToBytes32(stpCall.gasActual()), Bytes32.ZERO);
        if (callCanTransferValue(stpCall.opCode())) {
          this.wcp.callISZERO(Bytes32.leftPad(stpCall.value()));
        }
        this.wcp.callLT(longToBytes32(stpCall.gasActual()), longToBytes32(stpCall.upfrontGasCost()));
        if (!stpCall.outOfGasException()) {
          this.mod.callDIV(longToBytes32(stpOperation.getGDiff()), longToBytes32(64L));
          this.wcp.callLT(stpCall.gas(), longToBytes32(stpOperation.get63of64GDiff()));
        }
      }
    }
  }

  private StpOperation stpOperationForCreate(StpCall stpCall) {
    Hub hub = stpCall.hub();
    MessageFrame frame = hub.messageFrame();
    stpCall.gasActual(frame.getRemainingGas());
    stpCall.gas(EWord.ZERO);
    stpCall.value(EWord.of(frame.getWorldUpdater().get(frame.getContractAddress()).getBalance()));
    stpCall.exists(false); // irrelevant
    stpCall.warm(false); // irrelevant
    stpCall.upfrontGasCost(GasConstants.G_CREATE.cost() + stpCall.memoryExpansionGas());
    stpCall.outOfGasException(stpCall.gasActual() < stpCall.upfrontGasCost());
    final Address deploymentAddress = getDeploymentAddress(frame);
    final long gasActual = frame.getRemainingGas();
    return new StpOperation( stpCall);
  }

  private StpOperation stpOperationForCall(StpCall stpCall) {
    MessageFrame frame = stpCall.hub().messageFrame();
    final OpCode opcode = this.hub.opCode();
    final long gasActual = frame.getRemainingGas();
    final Bytes32 value =
        callCanTransferValue(opcode) ? Bytes32.leftPad(frame.getStackItem(2)) : Bytes32.ZERO;
    final Address to = Words.toAddress(frame.getStackItem(1));
    final long gasMxp = getGasMxpCall(frame);
    final boolean toWarm = frame.isAddressWarm(to);
    final boolean toExists =
        opcode == OpCode.CALLCODE
            || (frame.getWorldUpdater().get(to) != null
                && !frame.getWorldUpdater().get(to).isEmpty());

    long gasPrelim = gasMxp;
    if (!value.isZero() && callCanTransferValue(opcode)) {
      gasPrelim += GasConstants.G_CALL_VALUE.cost();
    }
    if (toWarm) {
      gasPrelim += GasConstants.G_WARM_ACCESS.cost();
    } else {
      gasPrelim += GasConstants.G_COLD_ACCOUNT_ACCESS.cost();
    }
    if (!toExists) {
      gasPrelim += GasConstants.G_NEW_ACCOUNT.cost();
    }
    final boolean oogx = gasActual < gasPrelim;
    return new StpOperation(stpCall);
  }

  static boolean callCanTransferValue(OpCode opCode) {
    return (opCode == OpCode.CALL) || (opCode == OpCode.CALLCODE);
  }

  // TODO get from Hub.GasProjector
  private long getGasMxpCreate(final MessageFrame frame) {
    long gasMxp = 0;
    final long offset = Words.clampedToLong(frame.getStackItem(1));
    final long length = Words.clampedToLong(frame.getStackItem(2));
    final long currentMemorySizeInWords = frame.memoryWordSize();
    final long updatedMemorySizeInWords = frame.calculateMemoryExpansion(offset, length);
    if (currentMemorySizeInWords < updatedMemorySizeInWords) {
      // computing the "linear" portion of CREATE2 memory expansion cost
      final long G_mem = GasConstants.G_MEMORY.cost();
      final long squareCurrent = (currentMemorySizeInWords * currentMemorySizeInWords) >> 9;
      final long squareUpdated = (updatedMemorySizeInWords * updatedMemorySizeInWords) >> 9;
      gasMxp +=
          G_mem * (updatedMemorySizeInWords - currentMemorySizeInWords)
              + (squareUpdated - squareCurrent);
    }
    if (OpCode.of(frame.getCurrentOperation().getOpcode()) == OpCode.CREATE2) {
      final long lengthInWords = (length + 31) >> 5; // ⌈ length / 32 ⌉
      gasMxp += lengthInWords * GasConstants.G_KECCAK_256_WORD.cost();
    }
    return gasMxp;
  }

  // TODO get from Hub.GasProjector
  private long getGasMxpCall(final MessageFrame frame) {
    long gasMxp = 0;

    final int offset =
        callCanTransferValue(OpCode.of(frame.getCurrentOperation().getOpcode())) ? 1 : 0;
    final long cdo = Words.clampedToLong(frame.getStackItem(2 + offset)); // call data offset
    final long cds = Words.clampedToLong(frame.getStackItem(3 + offset)); // call data size
    final long rdo = Words.clampedToLong(frame.getStackItem(4 + offset)); // return data offset
    final long rdl = Words.clampedToLong(frame.getStackItem(5 + offset)); // return data size

    final long memSize = frame.memoryWordSize();
    final long memSizeCallData = frame.calculateMemoryExpansion(cdo, cds);
    final long memSizeReturnData = frame.calculateMemoryExpansion(rdo, rdl);
    final long maybeNewMemSize = max(memSizeReturnData, memSizeCallData);

    if (memSize < maybeNewMemSize) {
      // computing the "linear" portion of CREATE2 memory expansion cost
      final long G_mem = GasConstants.G_MEMORY.cost();
      final long squareCurrent = (memSize * memSize) >> 9;
      final long squareUpdated = (maybeNewMemSize * maybeNewMemSize) >> 9;
      gasMxp += G_mem * (maybeNewMemSize - memSize) + (squareUpdated - squareCurrent);
    }
    return gasMxp;
  }

  @Override
  public void commit(List<MappedByteBuffer> buffers) {
    final Trace trace = new Trace(buffers);

    int stamp = 0;
    for (StpOperation chunk : operations) {
      stamp++;
      chunk.trace(trace, stamp);
    }
  }
}
