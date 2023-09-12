/*
 * Copyright contributors to Hyperledger Besu
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
 *
 */

package net.consensys.linea.zktracer.testing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.evm.account.Account;
import org.hyperledger.besu.evm.account.EvmAccount;
import org.hyperledger.besu.evm.worldstate.WorldUpdater;

public class ToyWorld implements WorldUpdater {
  @Getter private ToyWorld parent;
  @Getter private List<ToyAccount> accounts;
  private Map<Address, ToyAccount> addressAccountMap;

  private ToyWorld(final ToyWorld parent) {
    this(parent, new ArrayList<>());
  }

  @Builder
  private ToyWorld(final ToyWorld parent, @Singular final List<ToyAccount> accounts) {
    this.parent = parent;
    this.accounts = accounts;
    this.addressAccountMap = new HashMap<>();
  }

  public static ToyWorld empty() {
    return builder().build();
  }

  @Override
  public WorldUpdater updater() {
    return new ToyWorld(this);
  }

  @Override
  public Account get(final Address address) {
    if (addressAccountMap.containsKey(address)) {
      return addressAccountMap.get(address);
    } else if (parent != null) {
      return parent.get(address);
    }

    return null;
  }

  @Override
  public EvmAccount createAccount(final Address address, final long nonce, final Wei balance) {
    return createAccount(null, address, nonce, balance, Bytes.EMPTY);
  }

  private EvmAccount createAccount(
      final Account parentAccount,
      final Address address,
      final long nonce,
      final Wei balance,
      final Bytes code) {

    ToyAccount account =
        ToyAccount.builder()
            .parent(parentAccount)
            .code(code)
            .address(address)
            .nonce(nonce)
            .balance(balance)
            .build();

    addressAccountMap.put(address, account);

    return account;
  }

  @Override
  public EvmAccount getAccount(final Address address) {
    if (addressAccountMap.containsKey(address)) {
      return addressAccountMap.get(address);
    } else if (parent != null) {
      Account parentAccount = parent.getAccount(address);
      if (parentAccount != null) {
        return createAccount(
            parentAccount,
            parentAccount.getAddress(),
            parentAccount.getNonce(),
            parentAccount.getBalance(),
            parentAccount.getCode());
      }
    }

    return null;
  }

  @Override
  public void deleteAccount(final Address address) {
    addressAccountMap.put(address, null);
  }

  @Override
  public Collection<? extends Account> getTouchedAccounts() {
    return addressAccountMap.values();
  }

  @Override
  public Collection<Address> getDeletedAccountAddresses() {
    return addressAccountMap.entrySet().stream()
        .filter(e -> e.getValue() == null)
        .map(Map.Entry::getKey)
        .collect(Collectors.toList());
  }

  @Override
  public void revert() {
    addressAccountMap = new HashMap<>();
  }

  @Override
  public void commit() {
    if (parent != null) {
      parent.addressAccountMap.putAll(addressAccountMap);
    }
  }

  @Override
  public Optional<WorldUpdater> parentUpdater() {
    return Optional.empty();
  }
}
