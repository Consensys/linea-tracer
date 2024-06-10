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

import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.State;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.DomSubStampsSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.storage.StorageFragment;
import net.consensys.linea.zktracer.types.EWord;
import org.hyperledger.besu.datatypes.Address;

@RequiredArgsConstructor
public class SloadSection extends TraceSection {

  final Hub hub;
  final State.StorageSlotIdentifier storageSlotIdentifier;
  final EWord valueCurrent;

  private SloadSection(Hub hub) {
    this.hub = hub;
    Address address = hub.currentFrame().accountAddress();
    EWord key = EWord.of(hub.messageFrame().getStackItem(0));
    this.storageSlotIdentifier =
        new State.StorageSlotIdentifier(address, hub.currentFrame().accountDeploymentNumber(), key);
    this.valueCurrent = EWord.of(hub.messageFrame().getTransientStorageValue(address, key));
  }

  public static void appendSection(Hub hub) {

    final SloadSection sloadSection = new SloadSection(hub);
    hub.addTraceSection(sloadSection);

    final State.StorageSlotIdentifier storageSlotIdentifier = sloadSection.storageSlotIdentifier;
    final Address address = storageSlotIdentifier.getAddress();
    final EWord storageKey = storageSlotIdentifier.getStorageKey();
    final EWord valueOriginal =
        hub.txStack().current().getStorage().getOriginalValueOrUpdate(address, storageKey);
    final EWord valueCurrent = sloadSection.valueCurrent;

    ContextFragment readCurrentContext = ContextFragment.readCurrentContextData(hub);
    ImcFragment miscFragmentForSload = ImcFragment.empty(hub);
    StorageFragment doingSload =
        doingSload(hub, address, storageKey, valueOriginal, valueCurrent);

    sloadSection.addFragmentsAndStack(
        hub, hub.currentFrame(), readCurrentContext, miscFragmentForSload, doingSload);

    final boolean outOfGasException = hub.pch().exceptions().outOfGasException();
    final boolean contextWillRevert = hub.callStack().current().willRevert();

    if (outOfGasException || contextWillRevert) {

      final StorageFragment undoingSload =
          undoingSload(hub, address, storageKey, valueOriginal, valueCurrent);

      // TODO: make sure we trace a context when there is an exception
      sloadSection.nonStackRowCounter = outOfGasException ? 5 : 4;
      sloadSection.addFragment(hub, hub.currentFrame(), undoingSload);
      hub.state.updateOrInsertStorageSlotOccurrence(storageSlotIdentifier, undoingSload);
      return;
    }

    hub.state.updateOrInsertStorageSlotOccurrence(storageSlotIdentifier, doingSload);
    sloadSection.nonStackRows = 3;
  }

  private static StorageFragment doingSload(
      Hub hub, Address address, EWord storageKey, EWord valueOriginal, EWord valueCurrent) {

    return new StorageFragment(
        hub.state,
        new State.StorageSlotIdentifier(
            address, hub.currentFrame().accountDeploymentNumber(), storageKey),
        valueOriginal,
        valueCurrent,
        valueCurrent,
        hub.currentFrame().frame().isStorageWarm(address, storageKey),
        true,
        DomSubStampsSubFragment.standardDomSubStamps(hub, 0),
        hub.state.firstAndLastStorageSlotOccurrences.size());
  }

  private static StorageFragment undoingSload(
      Hub hub, Address address, EWord storageKey, EWord valueOriginal, EWord valueCurrent) {

    return new StorageFragment(
        hub.state,
        new State.StorageSlotIdentifier(
            address, hub.currentFrame().accountDeploymentNumber(), storageKey),
        valueOriginal,
        valueCurrent,
        valueCurrent,
        true,
        hub.currentFrame().frame().isStorageWarm(address, storageKey),
        DomSubStampsSubFragment.revertWithCurrentDomSubStamps(hub, 1),
        hub.state.firstAndLastStorageSlotOccurrences.size());
  }
}
