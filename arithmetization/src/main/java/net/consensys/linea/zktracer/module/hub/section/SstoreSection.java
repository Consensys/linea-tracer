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

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.State;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.DomSubStampsSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.storage.StorageFragment;
import net.consensys.linea.zktracer.types.EWord;
import org.hyperledger.besu.datatypes.Address;

public class SstoreSection extends TraceSection {

  final Hub hub;
  final State.StorageSlotIdentifier storageSlotIdentifier;
  final EWord valueCurrent;
  final EWord valueNext;

  private SstoreSection(Hub hub) {
    this.hub = hub;
    Address address = hub.currentFrame().accountAddress();
    EWord key = EWord.of(hub.messageFrame().getStackItem(0));
    this.storageSlotIdentifier =
        new State.StorageSlotIdentifier(address, hub.currentFrame().accountDeploymentNumber(), key);
    this.valueCurrent = EWord.of(hub.messageFrame().getTransientStorageValue(address, key));
    this.valueNext = EWord.of(hub.messageFrame().getStackItem(1));
  }

  public static void appendSection(Hub hub) {

    final SstoreSection sstoreSection = new SstoreSection(hub);
    hub.addTraceSection(sstoreSection);

    final State.StorageSlotIdentifier storageSlotIdentifier = sstoreSection.storageSlotIdentifier;
    final Address address = storageSlotIdentifier.getAddress();
    final EWord storageKey = storageSlotIdentifier.getStorageKey();
    final EWord valueOriginal =
        hub.txStack().current().getStorage().getOriginalValueOrUpdate(address, storageKey);
    final EWord valueCurrent = sstoreSection.valueCurrent;
    final EWord valueNext = sstoreSection.valueNext;

    final boolean staticContextException = hub.pch().exceptions().staticException();
    final boolean sstoreException = hub.pch().exceptions().sstoreException();
    final boolean outOfGasException = hub.pch().exceptions().outOfGasException();
    final boolean contextWillRevert = hub.callStack().current().willRevert();

    ContextFragment readCurrentContext = ContextFragment.readCurrentContextData(hub);
    sstoreSection.addFragmentsAndStack(hub, hub.currentFrame(), readCurrentContext);

    // TODO: make sure we trace a context when there is an exception
    if (staticContextException) {
      return;
    }

    ImcFragment miscForSstore = ImcFragment.forOpcode(hub, hub.messageFrame());
    sstoreSection.addFragment(hub, hub.currentFrame(), miscForSstore);

    // TODO: make sure we trace a context when there is an exception
    if (sstoreException) {
      return;
    }

    StorageFragment doingSstore =
        doingSstore(hub, address, storageKey, valueOriginal, valueCurrent, valueNext);
    StorageFragment undoingSstore =
        undoingSstore(hub, address, storageKey, valueOriginal, valueCurrent, valueNext);

    sstoreSection.addFragment(hub, hub.currentFrame(), doingSstore);

    // TODO: make sure we trace a context when there is an exception (oogx case)
    if (outOfGasException || contextWillRevert) {
      sstoreSection.addFragment(hub, hub.currentFrame(), undoingSstore);
    }
  }

  private static StorageFragment doingSstore(
      Hub hub,
      Address address,
      EWord storageKey,
      EWord valueOriginal,
      EWord valueCurrent,
      EWord valueNext) {

    return new StorageFragment(
        hub.state,
        new State.StorageSlotIdentifier(
            address, hub.currentFrame().accountDeploymentNumber(), storageKey),
        valueOriginal,
        valueCurrent,
        valueNext,
        hub.currentFrame().frame().isStorageWarm(address, storageKey),
        true,
        DomSubStampsSubFragment.standardDomSubStamps(hub, 0),
        hub.state.firstAndLastStorageSlotOccurrences.size());
  }

  private static StorageFragment undoingSstore(
      Hub hub,
      Address address,
      EWord storageKey,
      EWord valueOriginal,
      EWord valueCurrent,
      EWord valueNext) {

    return new StorageFragment(
        hub.state,
        new State.StorageSlotIdentifier(
            address, hub.currentFrame().accountDeploymentNumber(), storageKey),
        valueOriginal,
        valueNext,
        valueCurrent,
        true,
        hub.currentFrame().frame().isStorageWarm(address, storageKey),
        DomSubStampsSubFragment.revertWithCurrentDomSubStamps(hub, 1),
        hub.state.firstAndLastStorageSlotOccurrences.size());
  }
}
