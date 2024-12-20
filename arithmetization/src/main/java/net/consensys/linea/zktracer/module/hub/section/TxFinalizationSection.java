/*
 * Copyright ConsenSys Inc.
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

import static com.google.common.base.Preconditions.*;

import net.consensys.linea.zktracer.module.hub.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.defer.PostTransactionDefer;
import net.consensys.linea.zktracer.module.hub.fragment.DomSubStampsSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.TransactionFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import net.consensys.linea.zktracer.module.hub.transients.DeploymentInfo;
import net.consensys.linea.zktracer.types.TransactionProcessingMetadata;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class TxFinalizationSection extends TraceSection implements PostTransactionDefer {
  private final TransactionProcessingMetadata txMetadata;

  private AccountSnapshot sender;
  private AccountSnapshot senderNew;

  private AccountSnapshot coinbase;
  private AccountSnapshot coinbaseNew;

  public TxFinalizationSection(Hub hub, WorldView world, boolean exceptionOrRevert) {
    super(hub, (short) 4);
    hub.defers().scheduleForEndTransaction(this);
    txMetadata = hub.txStack().current();
  }

  @Override
  public void resolveAtEndTransaction(
      Hub hub, WorldView world, Transaction tx, boolean isSuccessful) {

    checkArgument(isSuccessful == txMetadata.statusCode());

    DeploymentInfo deploymentInfo = hub.transients().conflation().deploymentInfo();
    checkArgument(
        !deploymentInfo.getDeploymentStatus(txMetadata.getCoinbase()),
        "The coinbase may not be under deployment");

    setSnapshots(hub, world);

    final AccountFragment senderAccountFragment =
        hub.factories()
            .accountFragment()
            .make(
                sender,
                senderNew,
                DomSubStampsSubFragment.standardDomSubStamps(hub.stamp(), 0)); //

    final AccountFragment coinbaseAccountFragment =
        hub.factories()
            .accountFragment()
            .makeWithTrm(
                coinbase,
                coinbaseNew,
                coinbase.address(),
                DomSubStampsSubFragment.standardDomSubStamps(hub.stamp(), 1));

    this.addFragment(senderAccountFragment);
    this.addFragment(coinbaseAccountFragment);
    this.addFragment(TransactionFragment.prepare(hub.txStack().current())); // TXN i+2
  }

  /**
   * 1. snapshot the coinbase, this yields coinbaseNew
   *
   * <p>2. undo the gas reward, this yields coinbase
   *
   * <p>3.1. if {@link #senderIsCoinbase(Hub)} set {@link #senderNew} = {@link #coinbase}.deepCopy()
   *
   * <p>3.2. else set {@link #senderNew} = snapshot the sender
   *
   * <p>4. get sender by undoing the left over gas refund which is already implicitly in coinbase
   *
   * <p><b>N.B.</b> The processing is independent of the success or failure of the transaction.
   */
  private void setSnapshots(Hub hub, WorldView world) {
    final Address senderAddress = txMetadata.getSender();
    final Address coinbaseAddress = txMetadata.getCoinbase();

    if (senderIsCoinbase(hub)) {
      checkState(coinbaseWarmth());
    }

    coinbaseNew =
        AccountSnapshot.canonical(hub, world, coinbaseAddress)
            .setWarmthTo(coinbaseWarmth())
            .setDeploymentInfo(hub);
    coinbase = coinbaseNew.deepCopy().decrementBalanceBy(txMetadata.getCoinbaseReward());

    senderNew =
        senderIsCoinbase(hub)
            ? coinbase.deepCopy().setWarmthTo(true)
            : AccountSnapshot.canonical(hub, world, senderAddress).setWarmthTo(true);
    sender = senderNew.deepCopy().decrementBalanceBy(txMetadata.getGasRefundInWei());
  }

  private boolean coinbaseWarmth() {
    return txMetadata.isCoinbaseWarmAtTransactionEnd();
  }

  public static boolean senderIsCoinbase(Hub hub) {
    final TransactionProcessingMetadata tx = hub.txStack().current();
    final Address senderAddress = tx.getSender();
    final Address coinbaseAddress = tx.getCoinbase();
    return coinbaseAddress.equals(senderAddress);
  }
}
