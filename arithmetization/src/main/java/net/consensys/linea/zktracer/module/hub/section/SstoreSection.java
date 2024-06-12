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
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.DomSubStampsSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.storage.StorageFragment;
import net.consensys.linea.zktracer.types.EWord;
import org.apache.tuweni.bytes.Bytes32;
import org.apache.tuweni.units.bigints.UInt256;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.worldstate.WorldView;

@Getter
public class SstoreSection extends TraceSection {

  final Hub hub;
  final WorldView world;

  private SstoreSection(Hub hub, WorldView world) {
    this.hub = hub;
    this.world = world;
  }

  public static void appendSection(Hub hub, WorldView world) {

    final SstoreSection sstoreSection = new SstoreSection(hub, world);
    hub.addTraceSection(sstoreSection);

    final Address address = hub.messageFrame().getRecipientAddress();
    final int deploymentNumber = hub.currentFrame().codeDeploymentNumber();
    final Bytes32 storageKey = (Bytes32) hub.messageFrame().getStackItem(0);

    final EWord valueOriginal =
        EWord.of(world.get(address).getOriginalStorageValue(UInt256.fromBytes(storageKey)));
    final EWord valueCurrent =
        EWord.of(world.get(address).getStorageValue(UInt256.fromBytes(storageKey)));
    final EWord valueNext = EWord.of(hub.messageFrame().getStackItem(1));

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
        doingSstore(
            hub, address, deploymentNumber, storageKey, valueOriginal, valueCurrent, valueNext);

    sstoreSection.addFragment(hub, hub.currentFrame(), doingSstore);

    // TODO: make sure we trace a context when there is an exception (oogx case)
    if (outOfGasException || contextWillRevert) {
      StorageFragment undoingSstore =
          undoingSstore(
              hub, address, deploymentNumber, storageKey, valueOriginal, valueCurrent, valueNext);
      sstoreSection.addFragment(hub, hub.currentFrame(), undoingSstore);
    }
  }

  private static StorageFragment doingSstore(
      Hub hub,
      Address address,
      int deploymentNumber,
      Bytes32 storageKey,
      EWord valueOriginal,
      EWord valueCurrent,
      EWord valueNext) {

    return new StorageFragment(
        hub.state,
        new State.StorageSlotIdentifier(address, deploymentNumber, EWord.of(storageKey)),
        valueOriginal,
        valueCurrent,
        valueNext,
        hub.messageFrame().isStorageWarm(address, storageKey),
        true,
        DomSubStampsSubFragment.standardDomSubStamps(hub, 0),
        hub.state.firstAndLastStorageSlotOccurrences.size());
  }

  private static StorageFragment undoingSstore(
      Hub hub,
      Address address,
      int deploymentNumber,
      Bytes32 storageKey,
      EWord valueOriginal,
      EWord valueCurrent,
      EWord valueNext) {

    return new StorageFragment(
        hub.state,
        new State.StorageSlotIdentifier(address, deploymentNumber, EWord.of(storageKey)),
        valueOriginal,
        valueNext,
        valueCurrent,
        true,
        hub.messageFrame().isStorageWarm(address, storageKey),
        DomSubStampsSubFragment.revertWithCurrentDomSubStamps(hub, 1),
        hub.state.firstAndLastStorageSlotOccurrences.size());
  }
}
