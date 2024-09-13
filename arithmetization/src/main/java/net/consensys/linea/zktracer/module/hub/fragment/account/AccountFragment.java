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

package net.consensys.linea.zktracer.module.hub.fragment.account;

import static com.google.common.base.Preconditions.*;
import static net.consensys.linea.zktracer.types.AddressUtils.highPart;
import static net.consensys.linea.zktracer.types.AddressUtils.isPrecompile;
import static net.consensys.linea.zktracer.types.AddressUtils.lowPart;

import java.util.Map;
import java.util.Optional;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.module.hub.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.Trace;
import net.consensys.linea.zktracer.module.hub.defer.DeferRegistry;
import net.consensys.linea.zktracer.module.hub.defer.PostConflationDefer;
import net.consensys.linea.zktracer.module.hub.defer.PostTransactionDefer;
import net.consensys.linea.zktracer.module.hub.fragment.DomSubStampsSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.TraceFragment;
import net.consensys.linea.zktracer.module.romlex.ContractMetadata;
import net.consensys.linea.zktracer.types.EWord;
import net.consensys.linea.zktracer.types.TransactionProcessingMetadata;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.worldstate.WorldView;

@Accessors(fluent = true)
public final class AccountFragment
    implements TraceFragment, PostTransactionDefer, PostConflationDefer {

  @Getter private final AccountSnapshot oldState;
  @Getter private final AccountSnapshot newState;
  @Setter private int deploymentNumberInfinity = 0; // retconned on conflation end
  private final int deploymentNumber;
  private final boolean isDeployment;
  @Setter private boolean existsInfinity = false; // retconned on conflation end
  @Setter private boolean requiresRomlex;
  private int codeFragmentIndex;
  private final Optional<Bytes> addressToTrim;
  private final DomSubStampsSubFragment domSubStampsSubFragment;
  @Setter private RlpAddrSubFragment rlpAddrSubFragment;
  private boolean markedForSelfDestruct;
  private boolean markedForSelfDestructNew;
  final int hubStamp;
  final TransactionProcessingMetadata transactionProcessingMetadata;

  /**
   * {@link AccountFragment} creation requires access to a {@link DeferRegistry} for post-conflation
   * data gathering, which is provided by this factory.
   */
  @RequiredArgsConstructor
  public static class AccountFragmentFactory {
    private final Hub hub;

    public AccountFragment make(
        AccountSnapshot oldState,
        AccountSnapshot newState,
        DomSubStampsSubFragment domSubStampsSubFragment) {
      return new AccountFragment(
          hub, oldState, newState, Optional.empty(), domSubStampsSubFragment);
    }

    public AccountFragment makeWithTrm(
        AccountSnapshot oldState,
        AccountSnapshot newState,
        Bytes toTrim,
        DomSubStampsSubFragment domSubStampsSubFragment) {
      hub.trm().callTrimming(toTrim);
      return new AccountFragment(
          hub, oldState, newState, Optional.of(toTrim), domSubStampsSubFragment);
    }
  }

  public AccountFragment(
      Hub hub,
      AccountSnapshot oldState,
      AccountSnapshot newState,
      Optional<Bytes> addressToTrim,
      DomSubStampsSubFragment domSubStampsSubFragment) {
    checkArgument(oldState.address().equals(newState.address()));

    transactionProcessingMetadata = hub.txStack().current();
    hubStamp = hub.stamp();

    this.oldState = oldState;
    this.newState = newState;
    deploymentNumber = newState.deploymentNumber();
    isDeployment = newState.deploymentStatus();
    this.addressToTrim = addressToTrim;
    this.domSubStampsSubFragment = domSubStampsSubFragment;
    // Updating the map
    updateAccountFirstAndLast();

    // This allows us to properly fill EXISTS_INFTY, DEPLOYMENT_NUMBER_INFTY and CODE_FRAGMENT_INDEX
    hub.defers().scheduleForPostConflation(this);

    // This allows us to properly fill MARKED_FOR_SELFDESTRUCT and MARKED_FOR_SELFDESTRUCT_NEW,
    // among other things
    hub.defers().scheduleForPostTransaction(this);
  }

  @Override
  public Trace trace(Trace trace) {
    final EWord eCodeHash = EWord.of(oldState.code().getCodeHash());
    final EWord eCodeHashNew = EWord.of(newState.code().getCodeHash());

    // tracing
    domSubStampsSubFragment.trace(trace);
    if (rlpAddrSubFragment != null) {
      rlpAddrSubFragment.trace(trace);
    }

    return trace
        .peekAtAccount(true)
        .pAccountAddressHi(highPart(oldState.address()))
        .pAccountAddressLo(lowPart(oldState.address()))
        .pAccountNonce(Bytes.ofUnsignedLong(oldState.nonce()))
        .pAccountNonceNew(Bytes.ofUnsignedLong(newState.nonce()))
        .pAccountBalance(oldState.balance())
        .pAccountBalanceNew(newState.balance())
        .pAccountCodeSize(oldState.code().getSize())
        .pAccountCodeSizeNew(newState.code().getSize())
        .pAccountCodeHashHi(eCodeHash.hi())
        .pAccountCodeHashHiNew(eCodeHashNew.hi())
        .pAccountCodeHashLo(eCodeHash.lo())
        .pAccountCodeHashLoNew(eCodeHashNew.lo())
        .pAccountHasCode(oldState.code().getCodeHash() != Hash.EMPTY)
        .pAccountHasCodeNew(newState.code().getCodeHash() != Hash.EMPTY)
        .pAccountCodeFragmentIndex(codeFragmentIndex)
        .pAccountRomlexFlag(requiresRomlex)
        .pAccountExists(
            oldState.nonce() > 0
                || oldState.code().getCodeHash() != Hash.EMPTY
                || !oldState.balance().isZero())
        .pAccountExistsNew(
            newState.nonce() > 0
                || newState.code().getCodeHash() != Hash.EMPTY
                || !newState.balance().isZero())
        .pAccountWarmth(oldState.isWarm())
        .pAccountWarmthNew(newState.isWarm())
        .pAccountMarkedForSelfdestruct(markedForSelfDestruct)
        .pAccountMarkedForSelfdestructNew(markedForSelfDestructNew)
        .pAccountDeploymentNumber(oldState.deploymentNumber())
        .pAccountDeploymentNumberNew(newState.deploymentNumber())
        .pAccountDeploymentStatus(oldState.deploymentStatus())
        .pAccountDeploymentStatusNew(newState.deploymentStatus())
        .pAccountDeploymentNumberInfty(deploymentNumberInfinity)
        .pAccountDeploymentStatusInfty(existsInfinity)
        .pAccountTrmFlag(addressToTrim.isPresent())
        .pAccountTrmRawAddressHi(addressToTrim.map(a -> EWord.of(a).hi()).orElse(Bytes.EMPTY))
        .pAccountIsPrecompile(isPrecompile(oldState.address()));
  }

  @Override
  public void resolvePostTransaction(
          Hub hub, WorldView state, Transaction tx, boolean isSuccessful) {
    final Map<TransactionProcessingMetadata.EphemeralAccount, Integer> effectiveSelfDestructMap =
        transactionProcessingMetadata.getEffectiveSelfDestructMap();
    final TransactionProcessingMetadata.EphemeralAccount ephemeralAccount =
        new TransactionProcessingMetadata.EphemeralAccount(
            oldState.address(), oldState.deploymentNumber());
    if (effectiveSelfDestructMap.containsKey(ephemeralAccount)) {
      final int selfDestructTime = effectiveSelfDestructMap.get(ephemeralAccount);
      markedForSelfDestruct = hubStamp > selfDestructTime;
      markedForSelfDestructNew = hubStamp >= selfDestructTime;
    } else {
      markedForSelfDestruct = false;
      markedForSelfDestructNew = false;
    }
  }

  @Override
  public void resolvePostConflation(Hub hub, WorldView world) {
    deploymentNumberInfinity = hub.deploymentNumberOf(oldState.address());
    existsInfinity = world.get(oldState.address()) != null;
    codeFragmentIndex =
        requiresRomlex
            ? hub.romLex()
                .getCodeFragmentIndexByMetadata(
                    ContractMetadata.make(oldState.address(), deploymentNumber, isDeployment))
            : 0;
  }
  public void updateAccountFirstAndLast() {
    // Setting the post transaction first and last value
    // Initialise the Account First and Last map
    final Map<Address, TransactionProcessingMetadata.TransactAccountFirstAndLast> txnAccountFirstAndLastMap =
            this. transactionProcessingMetadata.getTransactAccountFirstAndLastMap();
    if (!txnAccountFirstAndLastMap.containsKey(oldState.address())) {
      int dom = this.domSubStampsSubFragment.domStamp();
      int sub = this.domSubStampsSubFragment.subStamp();
      TransactionProcessingMetadata.TransactAccountFirstAndLast txnFirstAndLast =
              new TransactionProcessingMetadata.TransactAccountFirstAndLast(this, this, dom, sub, dom, sub);
      txnAccountFirstAndLastMap.put(oldState.address(), txnFirstAndLast);
    } else {
      TransactionProcessingMetadata.TransactAccountFirstAndLast txnFirstAndLast = txnAccountFirstAndLastMap.get(oldState.address());
      // Replace condition
      if (TransactionProcessingMetadata.TransactAccountFirstAndLast.strictlySmallerStamps(
              txnFirstAndLast.getLastDom(), txnFirstAndLast.getLastSub(),
              this.domSubStampsSubFragment.domStamp(), this.domSubStampsSubFragment.subStamp())) {
        txnFirstAndLast.setLast(this);
        txnFirstAndLast.setLastDom(this.domSubStampsSubFragment.domStamp());
        txnFirstAndLast.setLastSub(this.domSubStampsSubFragment.subStamp());
        txnAccountFirstAndLastMap.put(oldState.address(), txnFirstAndLast);
      }
    }

  }
}


