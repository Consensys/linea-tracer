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

package net.consensys.linea.zktracer.module.hub.section;

import static net.consensys.linea.zktracer.module.constants.GlobalConstants.MMU_INST_MLOAD;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.MMU_INST_MSTORE;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.MMU_INST_MSTORE8;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.WORD_SIZE;

import java.util.Optional;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.defer.PostTransactionDefer;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.MxpCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.mmu.MmuCall;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.EWord;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.internal.Words;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class StackRamSection extends TraceSection implements PostTransactionDefer {
  final OpCode instruction;
  final Bytes currentRam;
  final int currentContextNumber;
  final short exception;
  final ImcFragment imcFragment;
  final EWord offset;
  final EWord value;

  public StackRamSection(Hub hub) {
    super(hub, (short) 3);
    this.addStack(hub);

    this.instruction = hub.opCode();
    this.exception = hub.pch().exceptions();
    this.currentRam =
        hub.currentFrame().frame().shadowReadMemory(0, hub.currentFrame().frame().memoryByteSize());
    this.currentContextNumber = hub.currentFrame().contextNumber();

    this.imcFragment = ImcFragment.empty(hub);
    this.offset = EWord.of(hub.currentFrame().frame().getStackItem(0));
    this.value =
        instruction.equals(OpCode.MLOAD)
            ? EWord.of(hub.messageFrame().shadowReadMemory(Words.clampedToLong(offset), WORD_SIZE))
            : EWord.of(hub.currentFrame().frame().getStackItem(1));

    final MxpCall mxpCall = new MxpCall(hub);
    imcFragment.callMxp(mxpCall);

    this.addFragment(imcFragment);

    hub.defers().postTx(this);
  }

  @Override
  public void runPostTx(Hub hub, WorldView state, Transaction tx, boolean isSuccessful) {
    if (Exceptions.none(exception)) {
      switch (instruction) {
        case MSTORE -> {
          final MmuCall call =
              new MmuCall(MMU_INST_MSTORE)
                  .targetId(currentContextNumber)
                  .targetOffset(offset)
                  .limb1(value.hi())
                  .limb2(value.lo())
                  .targetRamBytes(Optional.of(currentRam));

          imcFragment.callMmu(call);
        }
        case MSTORE8 -> {
          final MmuCall call =
              new MmuCall(MMU_INST_MSTORE8)
                  .targetId(currentContextNumber)
                  .targetOffset(offset)
                  .limb1(value.hi())
                  .limb2(value.lo())
                  .targetRamBytes(Optional.of(currentRam));
          imcFragment.callMmu(call);
        }
        case MLOAD -> {
          final MmuCall call =
              new MmuCall(MMU_INST_MLOAD)
                  .sourceId(currentContextNumber)
                  .sourceOffset(offset)
                  .limb1(value.hi())
                  .limb2(value.lo())
                  .sourceRamBytes(Optional.of(currentRam));
          imcFragment.callMmu(call);
        }
      }
    }
  }
}
