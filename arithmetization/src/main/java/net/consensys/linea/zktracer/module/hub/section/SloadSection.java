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

@Getter
public class SloadSection extends TraceSection implements PostRollbackDefer {
  final Address address;
  final int deploymentNumber;
  final Bytes32 storageKey;
  final boolean incomingWarmth;
  final EWord valueOriginal;
  final EWord valueCurrent;
  final WorldView world;
  final short exceptions;

  public SloadSection(Hub hub, WorldView world) {
    super(hub);
    this.world = world;
    this.address = hub.messageFrame().getRecipientAddress();
    this.deploymentNumber = hub.currentFrame().accountDeploymentNumber();
    this.storageKey = Bytes32.leftPad(hub.messageFrame().getStackItem(0));
    this.incomingWarmth = hub.messageFrame().getWarmedUpStorage().contains(address, storageKey);
    this.valueOriginal =
        EWord.of(world.get(address).getOriginalStorageValue(UInt256.fromBytes(storageKey)));
    this.valueCurrent = EWord.of(world.get(address).getStorageValue(UInt256.fromBytes(storageKey)));
    this.exceptions = hub.pch().exceptions();

    hub.addTraceSection(this);
    hub.defers().scheduleForPostRollback(this, hub.currentFrame());
  }

  public void populateSection(Hub hub, WorldView world) {

    // NOTE: SLOAD can only trigger
    // - stackUnderflowException
    // - outOfGasException
    // in the present case we don't have a stack exception
    ContextFragment readCurrentContext = ContextFragment.readCurrentContextData(hub);
    ImcFragment miscFragmentForSload = ImcFragment.empty(hub);
    StorageFragment doingSload = doingSload(hub);

    this.addFragmentsAndStack(hub, readCurrentContext, miscFragmentForSload, doingSload);
  }

  private StorageFragment doingSload(Hub hub) {

    return new StorageFragment(
        hub.state,
        new State.StorageSlotIdentifier(
            this.address, this.deploymentNumber, EWord.of(this.storageKey)),
        this.valueOriginal,
        this.valueCurrent,
        this.valueCurrent,
        this.incomingWarmth,
        true,
        DomSubStampsSubFragment.standardDomSubStamps(hub, 0),
        hub.state.firstAndLastStorageSlotOccurrences.size());
  }

  @Override
  public void resolvePostRollback(Hub hub, MessageFrame messageFrame, CallFrame callFrame) {

    if (!this.undoingRequired()) {
      return;
    }

    final DomSubStampsSubFragment undoingDomSubStamps =
        DomSubStampsSubFragment.revertWithCurrentDomSubStamps(hub, 0);

    final StorageFragment undoingSloadStorageFragment =
        new StorageFragment(
            hub.state,
            new State.StorageSlotIdentifier(
                this.address, this.deploymentNumber, EWord.of(this.storageKey)),
            this.valueOriginal,
            this.valueCurrent,
            this.valueCurrent,
            true,
            this.incomingWarmth,
            undoingDomSubStamps,
            hub.state.firstAndLastStorageSlotOccurrences.size());

    this.addFragment(undoingSloadStorageFragment);
  }

  private boolean undoingRequired() {
    return Exceptions.outOfGasException(this.exceptions) || Exceptions.none(this.exceptions);
  }
}
