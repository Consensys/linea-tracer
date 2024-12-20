/*
 * Copyright Consensys Software Inc.
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

import static net.consensys.linea.zktracer.module.hub.HubProcessingPhase.TX_EXEC;

import com.google.common.base.Preconditions;
import lombok.Getter;
import net.consensys.linea.zktracer.module.hub.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.defer.PostTransactionDefer;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.DomSubStampsSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.TransactionFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.transients.DeploymentInfo;
import net.consensys.linea.zktracer.types.Bytecode;
import net.consensys.linea.zktracer.types.TransactionProcessingMetadata;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.evm.account.Account;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class TxInitializationSection extends TraceSection implements PostTransactionDefer {

  @Getter private final int hubStamp;
  final AccountFragment.AccountFragmentFactory accountFragmentFactory;

  @Getter private final AccountSnapshot senderGasPayment;
  @Getter private final AccountSnapshot senderGasPaymentNew;

  final Wei value;
  @Getter private final AccountSnapshot senderValueTransfer;
  @Getter private final AccountSnapshot senderValueTransferNew;

  @Getter private final AccountSnapshot recipientValueReception;
  @Getter private final AccountSnapshot recipientValueReceptionNew;

  @Getter private AccountSnapshot senderUndoingValueTransfer;
  @Getter private AccountSnapshot senderUndoingValueTransferNew;

  @Getter private AccountSnapshot recipientUndoingValueReception;
  @Getter private AccountSnapshot recipientUndoingValueReceptionNew;

  @Getter private final ContextFragment initializationContextFragment;

  public TxInitializationSection(Hub hub, WorldView world) {
    super(hub, (short) 5);
    hubStamp = hub.stamp();
    accountFragmentFactory = hub.factories().accountFragment();

    hub.defers().scheduleForPostTransaction(this);

    hub.txStack().setInitializationSection(this);

    final TransactionProcessingMetadata tx = hub.txStack().current();
    final Address senderAddress = tx.getSender();
    final Address recipientAddress = tx.getEffectiveRecipient();
    final Account senderAccount = world.get(senderAddress);
    final DeploymentInfo deploymentInfo = hub.transients().conflation().deploymentInfo();

    final boolean isDeployment = tx.isDeployment();
    final Wei transactionGasPrice = Wei.of(tx.getEffectiveGasPrice());
    final Wei gasCost = transactionGasPrice.multiply(tx.getBesuTransaction().getGasLimit());

    senderGasPayment =
        AccountSnapshot.fromAccount(
            senderAccount,
            tx.isSenderPreWarmed(),
            deploymentInfo.deploymentNumber(senderAddress),
            deploymentInfo.getDeploymentStatus(senderAddress));
    senderGasPaymentNew =
        senderGasPayment.deepCopy().decrementBalanceBy(gasCost).turnOnWarmth().raiseNonceByOne();

    value = (Wei) tx.getBesuTransaction().getValue();

    senderValueTransfer = senderGasPaymentNew.deepCopy();
    senderValueTransferNew = senderValueTransfer.deepCopy().decrementBalanceBy(value);

    final Account recipientAccount = world.get(recipientAddress);

    if (recipientAccount != null) {
      recipientValueReception =
          isSelfCredit(hub)
              ? senderValueTransferNew
              : AccountSnapshot.canonical(hub, world, recipientAddress, tx.isRecipientPreWarmed())
                  .setWarmthTo(tx.isRecipientPreWarmed());
    } else {
      recipientValueReception =
          AccountSnapshot.fromAddress(
              recipientAddress,
              tx.isRecipientPreWarmed(),
              deploymentInfo.deploymentNumber(recipientAddress),
              deploymentInfo.getDeploymentStatus(recipientAddress));
    }

    if (isDeployment) {
      deploymentInfo.newDeploymentWithExecutionAt(
          recipientAddress, tx.getBesuTransaction().getInit().orElse(Bytes.EMPTY));
    }

    final Bytecode initCode = new Bytecode(tx.getBesuTransaction().getInit().orElse(Bytes.EMPTY));

    recipientValueReceptionNew = recipientValueReception.deepCopy();

    if (isDeployment) {
      Preconditions.checkState(
          !recipientValueReception.deploymentStatus()
              && deploymentInfo.getDeploymentStatus(recipientAddress)
              && recipientValueReception.deploymentNumber() + 1
                  == deploymentInfo.deploymentNumber(recipientAddress),
          "Deployment status should be true and deployment number should be positive");

      recipientValueReceptionNew
          .raiseNonceByOne()
          .incrementBalanceBy(value)
          .code(initCode)
          .turnOnWarmth()
          .setDeploymentInfo(deploymentInfo);
    } else {
      recipientValueReceptionNew.incrementBalanceBy(value).turnOnWarmth();
    }
    recipientUndoingValueReception = recipientValueReceptionNew.deepCopy();

    this.addFragments(ImcFragment.forTxInit(hub)); // MISC i + 0
    this.addFragment(TransactionFragment.prepare(tx)); // TXN i + 1

    this.addFragment( // ACC i + 2 (sender: gas payment)
        accountFragmentFactory.makeWithTrm(
            senderGasPayment,
            senderGasPaymentNew,
            senderAddress,
            DomSubStampsSubFragment.standardDomSubStamps(hubStamp, 0)));

    this.addFragment( // ACC i + 3 (sender: value transfer)
        accountFragmentFactory.make(
            senderValueTransfer,
            senderValueTransferNew,
            DomSubStampsSubFragment.standardDomSubStamps(hubStamp, 1)));

    this.addFragment( // ACC i + 4 (recipient: value reception)
        accountFragmentFactory
            .makeWithTrm(
                recipientValueReception,
                recipientValueReceptionNew,
                recipientAddress,
                DomSubStampsSubFragment.standardDomSubStamps(hub.stamp(), 2))
            .requiresRomlex(true));

    initializationContextFragment = ContextFragment.initializeExecutionContext(hub);

    hub.state.setProcessingPhase(TX_EXEC);
  }

  @Override
  public void resolvePostTransaction(
      Hub hub, WorldView state, Transaction tx, boolean isSuccessful) {

    if (!isSuccessful) {

      senderUndoingValueTransfer = senderValueTransferNew.deepCopy().setDeploymentNumber(hub);
      senderUndoingValueTransferNew = senderValueTransfer.deepCopy().setDeploymentNumber(hub);

      recipientUndoingValueReception =
          recipientValueReceptionNew.deepCopy().setDeploymentNumber(hub);
      recipientUndoingValueReceptionNew =
          recipientUndoingValueReception.deepCopy().setDeploymentNumber(hub);

      final int revertStamp = hub.currentFrame().revertStamp();

      this.addFragment( // ACC i + 5 (sender)
          accountFragmentFactory.make(
              senderUndoingValueTransfer,
              senderUndoingValueTransferNew,
              DomSubStampsSubFragment.revertWithCurrentDomSubStamps(hubStamp, revertStamp, 3)));

      this.addFragment( // ACC i + 6 (recipient)
          accountFragmentFactory.make(
              recipientUndoingValueReception,
              recipientUndoingValueReceptionNew,
              DomSubStampsSubFragment.revertWithCurrentDomSubStamps(hubStamp, revertStamp, 4)));
    }

    this.addFragment(initializationContextFragment); // CON i + 5/7
  }

  private boolean isSelfCredit(Hub hub) {
    final TransactionProcessingMetadata tx = hub.txStack().current();
    final Address senderAddress = tx.getSender();
    final Address recipientAddress = tx.getEffectiveRecipient();

    return recipientAddress.equals(senderAddress);
  }
}
