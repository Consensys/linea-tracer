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

package net.consensys.linea.zktracer.module.hub.state;

import static com.google.common.base.Preconditions.checkState;

import java.util.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.module.hub.fragment.storage.StorageFragmentPurpose;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Wei;

/** Stores block-specific information. */
@Accessors(fluent = true)
@RequiredArgsConstructor
@Getter
public class Block {
  private final Address coinbaseAddress;
  private final Wei baseFee;

  private final Set<Address> addressesSeenByTheHub = new HashSet<>();
  private final Map<Address, Set<KeyStatusPair>> storageSlotsSeenByTheHub = new HashMap<>();

  @RequiredArgsConstructor
  public class KeyStatusPair {
    private final Bytes32 storageKey;
    private final boolean addressIsUnderDeployment;
    private final StorageFragmentPurpose purpose;
    private final boolean willRevert;
  }

  public void addAddressSeenByHub(final Address address) {
    addressesSeenByTheHub.add(address);
  }

  public void addStorageSeenByHub(
      final Address address,
      final Bytes32 storageKey,
      final boolean addressIsUnderDeployment,
      boolean willRevert,
      final StorageFragmentPurpose purpose) {
    checkState(
        addressesSeenByTheHub.contains(address),
        "attempt to access storage slot of account not yet touched by the HUB");
    final Set<KeyStatusPair> storageSet =
        storageSlotsSeenByTheHub.computeIfAbsent(address, k -> new HashSet<>());
    storageSet.add(new KeyStatusPair(storageKey, addressIsUnderDeployment, purpose, willRevert));
  }
}
