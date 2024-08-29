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

import lombok.Getter;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.State;
import net.consensys.linea.zktracer.module.hub.defer.PostRollbackDefer;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.DomSubStampsSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.storage.StorageFragment;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import net.consensys.linea.zktracer.types.EWord;
import org.apache.tuweni.bytes.Bytes32;
import org.apache.tuweni.units.bigints.UInt256;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.worldstate.WorldView;

import static net.consensys.linea.zktracer.module.hub.fragment.storage.StorageFragmentPurpose.SLOAD_DOING;
import static net.consensys.linea.zktracer.module.hub.fragment.storage.StorageFragmentPurpose.SLOAD_UNDOING;

@Getter
public class SloadSection extends TraceSection implements PostRollbackDefer {

  final WorldView world;
  final Address address;
  final int deploymentNumber;
  final Bytes32 storageKey;
  final boolean incomingWarmth;
  final EWord valueOriginal;
  final EWord valueCurrent;
  final short exceptions;

  public SloadSection(Hub hub, WorldView worldView) {
    // exceptional case:   1 (stack row) + 5 (non stack rows)
    // unexceptional case: 1 (stack row) + 4 (non stack rows)
    super(
        hub,
        (short)
            (hub.opCode().numberOfStackRows() + (Exceptions.any(hub.pch().exceptions()) ? 5 : 4)));

    world = worldView;
    address = hub.messageFrame().getRecipientAddress();
    deploymentNumber = hub.deploymentNumberOf(address);
    storageKey = Bytes32.leftPad(hub.messageFrame().getStackItem(0));
    incomingWarmth = hub.messageFrame().getWarmedUpStorage().contains(address, storageKey);
    valueOriginal =
        EWord.of(worldView.get(address).getOriginalStorageValue(UInt256.fromBytes(storageKey)));
    valueCurrent = EWord.of(worldView.get(address).getStorageValue(UInt256.fromBytes(storageKey)));
    exceptions = hub.pch().exceptions();

    hub.defers().scheduleForPostRollback(this, hub.currentFrame());

    // NOTE: SLOAD can only trigger
    // - stackUnderflowException
    // - outOfGasException
    // in the present case we don't have a stack exception
    final ContextFragment readCurrentContext = ContextFragment.readCurrentContextData(hub);
    final ImcFragment miscFragmentForSload = ImcFragment.empty(hub);
    final StorageFragment doingSload = doingSload(hub);

    // Update the First Last time seen map of storage keys
    final State.StorageSlotIdentifier storageSlotIdentifier =
        new State.StorageSlotIdentifier(
            address,
            hub.transients().conflation().deploymentInfo().deploymentNumber(address),
            EWord.of(storageKey));
    hub.state.updateOrInsertStorageSlotOccurrence(storageSlotIdentifier, doingSload);

    this.addStackAndFragments(hub, readCurrentContext, miscFragmentForSload, doingSload);
  }

  private StorageFragment doingSload(Hub hub) {

    return new StorageFragment(
        hub.state,
        new State.StorageSlotIdentifier(
            address, deploymentNumber, EWord.of(storageKey)),
        valueOriginal,
        valueCurrent,
        valueCurrent,
        incomingWarmth,
        true,
        DomSubStampsSubFragment.standardDomSubStamps(this.hubStamp(), 0),
        hub.state.firstAndLastStorageSlotOccurrences.size(),
            SLOAD_DOING);
  }

  @Override
  public void resolvePostRollback(Hub hub, MessageFrame messageFrame, CallFrame callFrame) {

    if (!this.undoingRequired()) {
      return;
    }

    // TODO: make sure that the "current" execution context is the one that is being rolled back
    //  so that we can use its revert stamp ()
    final DomSubStampsSubFragment undoingDomSubStamps =
        DomSubStampsSubFragment.revertWithCurrentDomSubStamps(
            this.hubStamp(), hub.callStack().current().revertStamp(), 0);

    final StorageFragment undoingSloadStorageFragment =
        new StorageFragment(
            hub.state,
            new State.StorageSlotIdentifier(
                address, deploymentNumber, EWord.of(storageKey)),
            valueOriginal,
            valueCurrent,
            valueCurrent,
            true,
            incomingWarmth,
            undoingDomSubStamps,
            hub.state.firstAndLastStorageSlotOccurrences.size(),
                SLOAD_UNDOING);

    this.addFragment(undoingSloadStorageFragment);
  }

  private boolean undoingRequired() {
    return Exceptions.outOfGasException(exceptions) || Exceptions.none(exceptions);
  }
}
