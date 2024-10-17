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

package net.consensys.linea.zktracer.module.hub.transients;

import java.util.HashMap;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import net.consensys.linea.zktracer.module.hub.fragment.storage.StorageFragment;
import net.consensys.linea.zktracer.types.TransactionProcessingMetadata;
import org.hyperledger.besu.datatypes.Address;

public class StateManagerMetadata {
  @EqualsAndHashCode
  public static class AddrBlockPair {
    @Getter private Address address;
    @Getter private int blockNumber;

    public AddrBlockPair(Address addr, int blockNumber) {
      this.address = addr;
      this.blockNumber = blockNumber;
    }
  }

  @EqualsAndHashCode
  public static class AddrStorageKeyBlockNumTuple {
    @Getter private TransactionProcessingMetadata.AddrStorageKeyPair addrStorageKeyPair;
    @Getter private int blockNumber;

    public AddrStorageKeyBlockNumTuple(
        TransactionProcessingMetadata.AddrStorageKeyPair addrStorageKeyPair, int blockNumber) {
      this.addrStorageKeyPair = addrStorageKeyPair;
      this.blockNumber = blockNumber;
    }
  }

  @Getter
  Map<AddrBlockPair, TransactionProcessingMetadata.FragmentFirstAndLast<AccountFragment>>
      accountFirstLastBlockMap = new HashMap<>();

  @Getter
  Map<
          AddrStorageKeyBlockNumTuple,
          TransactionProcessingMetadata.FragmentFirstAndLast<StorageFragment>>
      storageFirstLastBlockMap = new HashMap<>();

  @Getter
  Map<Address, TransactionProcessingMetadata.FragmentFirstAndLast<AccountFragment>>
      accountFirstLastConflationMap = new HashMap<>();

  @Getter
  Map<
          TransactionProcessingMetadata.AddrStorageKeyPair,
          TransactionProcessingMetadata.FragmentFirstAndLast<StorageFragment>>
      storageFirstLastConflationMap = new HashMap<>();

  @Getter Map<AddrBlockPair, Integer> minDeplNoBlock = new HashMap<>();
  @Getter Map<AddrBlockPair, Integer> maxDeplNoBlock = new HashMap<>();

  public void updateDeplNoBlockMaps(Address address, int blockNumber, int currentDeplNo) {
    AddrBlockPair addrBlockPair = new AddrBlockPair(address, blockNumber);
    if (minDeplNoBlock.containsKey(addrBlockPair)) {
      // the maps already contain deployment info for this address, and this is not the first one in
      // the block
      // since it is not the first, we do not update the minDeplNoBlock
      // but we update the maxDeplNoBlock
      maxDeplNoBlock.put(addrBlockPair, currentDeplNo);
    } else {
      // this is the first time we have a deployment at this address in the block
      minDeplNoBlock.put(addrBlockPair, currentDeplNo);
      maxDeplNoBlock.put(addrBlockPair, currentDeplNo);
    }
  }
}
