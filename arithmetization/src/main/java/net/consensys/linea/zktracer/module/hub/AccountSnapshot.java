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

package net.consensys.linea.zktracer.module.hub;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static net.consensys.linea.zktracer.types.AddressUtils.isAddressWarm;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.module.hub.transients.DeploymentInfo;
import net.consensys.linea.zktracer.types.Bytecode;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.evm.account.Account;
import org.hyperledger.besu.evm.worldstate.WorldView;

@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true, fluent = true)
public class AccountSnapshot {
  private Address address;
  private long nonce;
  private Wei balance;
  private boolean isWarm;
  private Bytecode code;
  private int deploymentNumber;
  private boolean deploymentStatus;

  // TODO: is there a "canonical" way to take a snapshot fo an account
  //  where getWorldUpdater().getAccount(address) return null ?

  /**
   * Canonical way of creating an account snapshot.
   *
   * @param hub
   * @param address
   * @return
   */
  public static AccountSnapshot canonical(Hub hub, Address address) {
    return fromArguments(
        hub.messageFrame().getWorldUpdater(),
        address,
        hub.transients.conflation().deploymentInfo(),
        isAddressWarm(hub.messageFrame(), address));
  }

  public static AccountSnapshot canonical(
      Hub hub, WorldView world, Address address, boolean warmth) {
    return fromArguments(world, address, hub.transients.conflation().deploymentInfo(), warmth);
  }

  private static AccountSnapshot fromArguments(
      final WorldView worldView,
      final Address address,
      final DeploymentInfo deploymentInfo,
      final boolean warmth) {

    final Account account = worldView.get(address);
    if (account != null) {
      return new AccountSnapshot(
          account.getAddress(),
          account.getNonce(),
          account.getBalance(),
          warmth,
          new Bytecode(account.getCode()),
          deploymentInfo.deploymentNumber(address),
          deploymentInfo.getDeploymentStatus(address));
    } else {
      return new AccountSnapshot(
          address,
          0,
          Wei.ZERO,
          warmth,
          new Bytecode(Bytes.EMPTY),
          deploymentInfo.deploymentNumber(address),
          deploymentInfo.getDeploymentStatus(address));
    }
  }

  public static AccountSnapshot fromAccount(
      Account account, boolean isWarm, int deploymentNumber, boolean deploymentStatus) {
    return fromAccount(Optional.ofNullable(account), isWarm, deploymentNumber, deploymentStatus);
  }

  public static AccountSnapshot empty(
      boolean isWarm, int deploymentNumber, boolean deploymentStatus) {
    return new AccountSnapshot(
        Address.ZERO, 0, Wei.ZERO, isWarm, Bytecode.EMPTY, deploymentNumber, deploymentStatus);
  }

  public static AccountSnapshot fromAddress(
      Address address, boolean isWarm, int deploymentNumber, boolean deploymentStatus) {
    return new AccountSnapshot(
        address, 0, Wei.ZERO, isWarm, Bytecode.EMPTY, deploymentNumber, deploymentStatus);
  }

  public static AccountSnapshot fromAccount(
      Optional<Account> account, boolean isWarm, int deploymentNumber, boolean deploymentStatus) {

    return account
        .map(
            a ->
                new AccountSnapshot(
                    a.getAddress(),
                    a.getNonce(),
                    a.getBalance().copy(),
                    isWarm,
                    new Bytecode(a.getCode().copy()),
                    deploymentNumber,
                    deploymentStatus))
        .orElseGet(() -> AccountSnapshot.empty(isWarm, deploymentNumber, deploymentStatus));
  }

  // TODO: confirm with @Tsvetan that this indeed creates a deep copy
  /**
   * Creates deep copy of {@code this} {@link AccountSnapshot}.
   *
   * @return deep copy of {@code this}
   */
  public AccountSnapshot deepCopy() {
    return new AccountSnapshot(
        address, nonce, balance, isWarm, code, deploymentNumber, deploymentStatus);
  }

  public AccountSnapshot wipe(DeploymentInfo deploymentInfo) {
    final boolean deploymentStatus = deploymentInfo.getDeploymentStatus(address);
    checkArgument(!deploymentStatus);
    return new AccountSnapshot(
        address,
        0,
        Wei.of(0),
        isWarm,
        Bytecode.EMPTY,
        deploymentInfo.deploymentNumber(address),
        deploymentStatus);
  }

  /**
   * Decrements the balance by {@code quantity}. <b>WARNING:</b> this modifies the underlying {@link
   * AccountSnapshot}. Be sure to work with a {@link AccountSnapshot#deepCopy} if necessary.
   *
   * @param quantity
   * @return {@code this} with decremented balance
   */
  public AccountSnapshot decrementBalanceBy(Wei quantity) {
    checkState(
        balance.greaterOrEqualThan(quantity),
        "Insufficient balance"
            + String.format("\n\t\tAddress: %s", address)
            + String.format("\n\t\tBalance: %s", balance)
            + String.format("\n\t\tValue:   %s", quantity));

    balance = balance.subtract(quantity);
    return this;
  }

  /**
   * Increments the balance by {@code quantity}. <b>WARNING:</b> this modifies the underlying {@link
   * AccountSnapshot}. Be sure to work with a {@link AccountSnapshot#deepCopy} if necessary.
   *
   * @param quantity
   * @return {@code this} with incremented balance
   */
  public AccountSnapshot incrementBalanceBy(Wei quantity) {
    balance = balance.add(quantity);
    return this;
  }

  /**
   * {@link AccountSnapshot#setBalanceToZero()} changes the balance of the AccountSnapshot to be
   * zero. <b>WARNING:</b> this modifies the underlying {@link AccountSnapshot}. Be sure to work
   * with a {@link AccountSnapshot#deepCopy} if necessary.
   *
   * @return
   */
  public AccountSnapshot setBalanceToZero() {
    balance = Wei.ZERO;
    return this;
  }

  /**
   * Set the warmth to true. <b>WARNING:</b> this modifies the underlying {@link AccountSnapshot}.
   * Be sure to work with a {@link AccountSnapshot#deepCopy} if necessary.
   *
   * @return {@code this} with warmth = true
   */
  public AccountSnapshot turnOnWarmth() {
    return this.setWarmthTo(true);
  }

  /**
   * Set the warmth to {@code newWarmth}. <b>WARNING:</b> this modifies the underlying {@link
   * AccountSnapshot}. Be sure to work with a {@link AccountSnapshot#deepCopy} if necessary.
   *
   * @param newWarmth
   * @return {@code this} with updated warmth
   */
  public AccountSnapshot setWarmthTo(boolean newWarmth) {
    isWarm(newWarmth);
    return this;
  }

  /**
   * Raises the nonce by 1. <b>WARNING:</b> this modifies the underlying {@link AccountSnapshot}. Be
   * sure to work with a {@link AccountSnapshot#deepCopy} if necessary.
   *
   * @return {@code this} with nonce++
   */
  public AccountSnapshot raiseNonceByOne() {
    nonce(nonce + 1);
    return this;
  }

  public AccountSnapshot setDeploymentInfo(DeploymentInfo deploymentInfo) {
    deploymentNumber(deploymentInfo.deploymentNumber(address));
    deploymentStatus(deploymentInfo.getDeploymentStatus(address));
    return this;
  }

  public AccountSnapshot deployByteCode(Bytecode code) {
    checkState(deploymentStatus, "Deployment status should be true before deploying byte code.");

    return new AccountSnapshot(address, nonce, balance, true, code, deploymentNumber, false);
  }

  public AccountSnapshot copyDeploymentInfoFrom(AccountSnapshot snapshot) {
    return this.deploymentNumber(snapshot.deploymentNumber)
        .deploymentStatus(snapshot.deploymentStatus);
  }
}
