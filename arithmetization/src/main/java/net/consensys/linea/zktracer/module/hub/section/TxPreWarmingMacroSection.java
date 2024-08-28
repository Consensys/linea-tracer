/*
 * Copyright ConsenSys Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use hub file except in compliance with
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

import static net.consensys.linea.zktracer.types.AddressUtils.effectiveToAddress;
import static net.consensys.linea.zktracer.types.AddressUtils.precompileAddress;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.common.base.Preconditions;
import net.consensys.linea.zktracer.module.hub.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.State;
import net.consensys.linea.zktracer.module.hub.fragment.DomSubStampsSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.TraceFragment;
import net.consensys.linea.zktracer.module.hub.fragment.storage.StorageFragment;
import net.consensys.linea.zktracer.module.hub.transients.DeploymentInfo;
import net.consensys.linea.zktracer.types.EWord;
import net.consensys.linea.zktracer.types.TransactionProcessingMetadata;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.apache.tuweni.units.bigints.UInt256;
import org.hyperledger.besu.datatypes.AccessListEntry;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class TxPreWarmingMacroSection {
  public TxPreWarmingMacroSection(WorldView world, Hub hub) {

    final TransactionProcessingMetadata currentTxMetadata = hub.txStack().current();

    currentTxMetadata
        .getBesuTransaction()
        .getAccessList()
        .ifPresent(
            accessList -> {
              if (!accessList.isEmpty()) {
                final Set<Address> seenAddresses = new HashSet<>(precompileAddress);
                final HashMap<Address, Set<Bytes>> seenKeys = new HashMap<>();

                for (AccessListEntry entry : accessList) {
                  final Address address = entry.address();

                  final DeploymentInfo deploymentInfo =
                      hub.transients().conflation().deploymentInfo();

                  final int deploymentNumber = deploymentInfo.deploymentNumber(address);
                  Preconditions.checkArgument(
                      !deploymentInfo.getDeploymentStatus(address),
                      "Deployment status during TX_INIT phase of any address should always be false");

                  final boolean isAccountWarm = seenAddresses.contains(address);

                  final AccountSnapshot preWarmingAccountSnapshot =
                      AccountSnapshot.fromAccount(
                          world.get(address), isAccountWarm, deploymentNumber, false);

                  final AccountSnapshot postWarmingAccountSnapshot =
                      AccountSnapshot.fromAccount(
                          world.get(address), true, deploymentNumber, false);

                  final DomSubStampsSubFragment domSubStampsSubFragment =
                      new DomSubStampsSubFragment(
                          DomSubStampsSubFragment.DomSubType.STANDARD, hub.stamp(), 0, 0, 0, 0, 0);

                  new TxPrewarmingSection(
                      hub,
                      hub.factories()
                          .accountFragment()
                          .makeWithTrm(
                              preWarmingAccountSnapshot,
                              postWarmingAccountSnapshot,
                              address,
                              domSubStampsSubFragment));

                  seenAddresses.add(address);

                  final List<Bytes32> keys = entry.storageKeys();
                  for (Bytes32 k : keys) {

                    final UInt256 key = UInt256.fromBytes(k);
                    final EWord value =
                        Optional.ofNullable(world.get(address))
                            .map(account -> EWord.of(account.getStorageValue(key)))
                            .orElse(EWord.ZERO);

                    final State.StorageSlotIdentifier storageSlotIdentifier =
                        new State.StorageSlotIdentifier(
                            address, deploymentInfo.deploymentNumber(address), EWord.of(k));

                    final StorageFragment storageFragment =
                        new StorageFragment(
                            hub.state,
                            new State.StorageSlotIdentifier(
                                address, deploymentInfo.deploymentNumber(address), EWord.of(key)),
                            value,
                            value,
                            value,
                            seenKeys.computeIfAbsent(address, x -> new HashSet<>()).contains(key),
                            true,
                            DomSubStampsSubFragment.standardDomSubStamps(hub.stamp(), 0),
                            hub.state.firstAndLastStorageSlotOccurrences.size());

                    new TxPrewarmingSection(hub, storageFragment);
                    hub.state.updateOrInsertStorageSlotOccurrence(
                        storageSlotIdentifier, storageFragment);

                    seenKeys.get(address).add(key);
                  }
                }

                final Transaction besuTx = currentTxMetadata.getBesuTransaction();
                final Address senderAddress = besuTx.getSender();
                final Address recipientAddress = effectiveToAddress(besuTx);
                currentTxMetadata.isSenderPreWarmed(seenAddresses.contains(senderAddress));
                currentTxMetadata.isRecipientPreWarmed(seenAddresses.contains(recipientAddress));
              }
            });
  }

  public static class TxPrewarmingSection extends TraceSection {
    public TxPrewarmingSection(Hub hub, TraceFragment fragment) {
      super(hub, (short) 1);
      this.addFragments(fragment);
    }
  }
}
