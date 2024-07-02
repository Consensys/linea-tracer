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

import static net.consensys.linea.zktracer.module.constants.GlobalConstants.MMU_INST_RIGHT_PADDED_WORD_EXTRACTION;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.WORD_SIZE;
import static net.consensys.linea.zktracer.module.hub.fragment.ContextFragment.readCurrentContextData;

import java.util.Arrays;
import java.util.Optional;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.defer.PostTransactionDefer;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.mmu.MmuCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.oob.OobCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.oob.opcodes.CallDataLoadOobCall;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.EWord;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.internal.Words;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class CallDataLoadSection extends TraceSection implements PostTransactionDefer {
  final short exception;
  final Bytes callDataRam;
  final int currentContextNumber;
  final long callDataCN;
  final ImcFragment imcFragment;
  final EWord sourceOffset;

  long callDataOffset = -1;
  long callDataSize = -1;

  public CallDataLoadSection(Hub hub) {
    super(hub, (short) (hub.opCode().equals(OpCode.CALLDATALOAD) ? 4 : 3));
    this.addStack(hub);

    this.exception = hub.pch().exceptions();
    this.currentContextNumber = hub.currentFrame().contextNumber();
    this.callDataSize = hub.currentFrame().callDataInfo().memorySpan().length();
    this.callDataOffset = hub.currentFrame().callDataInfo().memorySpan().offset();
    this.sourceOffset = (EWord) hub.currentFrame().frame().getStackItem(0);
    this.callDataCN = hub.currentFrame().callDataInfo().callDataContextNumber();
    this.callDataRam = hub.currentFrame().callDataInfo().data();

    this.imcFragment = ImcFragment.empty(hub);

    final OobCall oobCall = new CallDataLoadOobCall();
    imcFragment.callOob(oobCall);

    this.addFragment(imcFragment);

    final ContextFragment context = readCurrentContextData(hub);
    this.addFragment(context);

    hub.defers().postTx(this);
  }

  @Override
  public void runPostTx(Hub hub, WorldView state, Transaction tx, boolean isSuccessful) {
    if (Exceptions.none(exception)) {

      if (Words.clampedToLong(sourceOffset) >= callDataSize) {
        return;
      }

      final EWord read =
          EWord.of(
              Bytes.wrap(
                  Arrays.copyOfRange(
                      hub.currentFrame().callDataInfo().data().toArray(),
                      Words.clampedToInt(sourceOffset),
                      Words.clampedToInt(sourceOffset) + WORD_SIZE)));

      final MmuCall call =
          new MmuCall(MMU_INST_RIGHT_PADDED_WORD_EXTRACTION)
              .sourceId((int) callDataCN)
              .sourceOffset(sourceOffset)
              .referenceOffset(callDataOffset)
              .referenceSize(callDataSize)
              .limb1(read.hi())
              .limb2(read.lo())
              .sourceRamBytes(Optional.of(callDataRam));

      imcFragment.callMmu(call);
    }
  }
}
