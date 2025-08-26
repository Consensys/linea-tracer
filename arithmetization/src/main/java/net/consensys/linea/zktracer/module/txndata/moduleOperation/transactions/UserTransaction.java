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
package net.consensys.linea.zktracer.module.txndata.moduleOperation.transactions;

import static com.google.common.base.Preconditions.*;
import static net.consensys.linea.zktracer.Trace.*;
import static net.consensys.linea.zktracer.module.txndata.moduleOperation.ShanghaiTxndataOperation.MAX_INIT_CODE_SIZE_BYTES;
import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;
import static org.hyperledger.besu.datatypes.TransactionType.ACCESS_LIST;
import static org.hyperledger.besu.datatypes.TransactionType.FRONTIER;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import net.consensys.linea.zktracer.module.euc.Euc;
import net.consensys.linea.zktracer.module.txndata.module.*;
import net.consensys.linea.zktracer.module.txndata.moduleOperation.TxnDataRedesignOperation;
import net.consensys.linea.zktracer.module.txndata.rows.*;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.types.TransactionProcessingMetadata;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.TransactionType;

public class UserTransaction extends TxnDataRedesignOperation {

  private static final Bytes EIP_2681_MAX_NONCE = bigIntegerToBytes(EIP2681_MAX_NONCE);
  public final TransactionProcessingMetadata txnMetadata;
  public final List<TxnDataRow> rows = new ArrayList<>();
  public final Wcp wcp;
  public final Euc euc;

  public UserTransaction(
      final TxnDataRedesign txnData, final TransactionProcessingMetadata txnMetadata) {
    super(
        txnData.getSysiTransactionNumber(),
        txnData.getUpdatedUserTransactionNumber(),
        txnData.getSysfTransactionNumber());

    checkArgument(
        txnData.getUserTransactionNumber() == txnMetadata.getUserTransactionNumber(),
        "User transaction number mismatch: expected %s, got %s",
        txnMetadata.getUserTransactionNumber(),
        txnData.getUserTransactionNumber());

    this.txnMetadata = txnMetadata;
    this.wcp = txnData.getHub().wcp();
    this.euc = txnData.getHub().euc();

    this.process();
  }

  private void process() {
    addHubRow();
    addRlpRow();
    addMaxNonceCheckCmptnRow();
    addInitialBalanceCheckCmptnRow();
    addMaxInitCodeSizeCheckCmptnRow();
    addInitCodePricingCmptnRow();
    addGasLimitMustCoverTheUpfrontGasCostCmptnRow();
  }

  private void addHubRow() {
    rows.add(new HubRow(txnMetadata));
  }

  private void addRlpRow() {
    rows.add(new RlpRow(txnMetadata));
  }

  /** Performs the EIP-2681 check that the nonce is less than 2^64 - 1. */
  private void addMaxNonceCheckCmptnRow() {
    WcpRow maxNonceCheckEip2681 =
        WcpRow.smallCallToLt(
            wcp,
            Bytes.ofUnsignedLong(txnMetadata.getBesuTransaction().getNonce()),
            EIP_2681_MAX_NONCE);
    checkArgument(maxNonceCheckEip2681.result(), "Transaction nonce is too high");
    rows.add(maxNonceCheckEip2681);
  }

  /**
   * Ensures that the initial balance of the sender account is sufficient to cover the maximum
   * possible cost of the transaction (value + gas_limit * max(gas_price, max_fee)).
   */
  private void addInitialBalanceCheckCmptnRow() {
    final Bytes initialBalance = bigIntegerToBytes(txnMetadata.getInitialBalance());
    final BigInteger value = txnMetadata.getBesuTransaction().getValue().getAsBigInteger();
    final BigInteger maxGasPrice =
        transactionHasEip1559GasSemantics()
            ? txnMetadata.getBesuTransaction().getMaxFeePerGas().get().getAsBigInteger()
            : txnMetadata.getBesuTransaction().getGasPrice().get().getAsBigInteger();
    final Bytes maxCostInWei =
        bigIntegerToBytes(
            value.add(
                maxGasPrice.multiply(
                    BigInteger.valueOf(txnMetadata.getBesuTransaction().getGasLimit()))));
    final WcpRow initialBalanceMustCoverValueAndGas =
        WcpRow.smallCallToLeq(wcp, maxCostInWei, initialBalance);
    checkArgument(
        initialBalanceMustCoverValueAndGas.result(),
        "Initial balance %s does not cover the max value and gas cost %s",
        initialBalance,
        maxCostInWei);
  }

  private void addMaxInitCodeSizeCheckCmptnRow() {
    final int initCodeSize = initCodeSize();
    final WcpRow eip3860requiredInitCodeSizeCheck =
        WcpRow.smallCallToLeq(wcp, Bytes.ofUnsignedInt(initCodeSize), MAX_INIT_CODE_SIZE_BYTES);
    checkArgument(
        eip3860requiredInitCodeSizeCheck.result(),
        "Init code size %s exceeds the EIP-3860 limit of 49152 bytes",
        initCodeSize);
    rows.add(eip3860requiredInitCodeSizeCheck);
  }

  private void addInitCodePricingCmptnRow() {

    // Deployment transaction
    final long dividend = initCodeSize() + ((long) WORD_SIZE_MO);
    rows.add(EucRow.callToEuc(euc, dividend, WORD_SIZE));
  }

  private void addGasLimitMustCoverTheUpfrontGasCostCmptnRow() {
    final long upfrontGasCost =
        dataCost()
            + initCodeCost()
            + GAS_CONST_G_TRANSACTION
            + (txnMetadata.isDeployment() ? GAS_CONST_G_TX_CREATE : 0)
            + (transactionSupportsAccessLists() ? txnMetadata.numberOfWarmedAddresses() : 0)
                * (long) GAS_CONST_G_ACCESS_LIST_ADRESS
            + (txnMetadata.isDeployment() ? GAS_CONST_G_TX_CREATE : 0)
            + (transactionSupportsAccessLists() ? txnMetadata.numberOfWarmedStorageKeys() : 0)
                * (long) GAS_CONST_G_ACCESS_LIST_STORAGE;
    final WcpRow gasLimitMustCoverUpfrontGasCost =
        WcpRow.smallCallToLeq(
            wcp,
            Bytes.ofUnsignedLong(upfrontGasCost),
            Bytes.ofUnsignedLong(txnMetadata.getBesuTransaction().getGasLimit()));
    checkArgument(
        gasLimitMustCoverUpfrontGasCost.result(),
        "Gas limit %s does not cover the upfront gas cost %s",
        txnMetadata.getBesuTransaction().getGasLimit(),
        upfrontGasCost);
    rows.add(gasLimitMustCoverUpfrontGasCost);
  }

  private long initCodeCost() {
    final long numberOfInitCodeWords = (initCodeSize() + WORD_SIZE_MO) / WORD_SIZE;
    return GAS_CONST_INIT_CODE_WORD * numberOfInitCodeWords;
  }

  private long weightedByteCount() {
    long nonzeroBytes = 0;
    for (int i = 0; i < txnMetadata.getBesuTransaction().getPayload().size(); i++) {
      if (txnMetadata.getBesuTransaction().getPayload().get(i) == 0) {
        nonzeroBytes++;
      }
    }
    return nonzeroBytes * 3 + (long) txnMetadata.getBesuTransaction().getPayload().size();
  }

  private long dataCost() {
    // TODO: replace 4 with STANDARD_TOKEN_COST
    return 4 * weightedByteCount();
  }

  private long callDataFloorCost() {
    // TODO: replace 10 with FLOOR_TOKEN_COST
    return 10 * weightedByteCount();
  }

  @Override
  protected int computeLineCount() {
    TransactionType type = txnMetadata.getBesuTransaction().getType();
    return switch (type) {
      case FRONTIER, ACCESS_LIST -> 14;
      case EIP1559 -> 16;
      default -> throw new RuntimeException("Transaction type " + type + " not supported");
    };
  }

  private boolean transactionHasEip1559GasSemantics() {
    return !(txnMetadata.getBesuTransaction().getType() == FRONTIER
        || txnMetadata.getBesuTransaction().getType() == ACCESS_LIST);
  }

  // TODO: this will change with Prague's EIP-7702 and type 4 transactions
  private boolean transactionSupportsAccessLists() {
    return txnMetadata.getBesuTransaction().getType() == ACCESS_LIST;
  }

  private int initCodeSize() {
    return txnMetadata.isDeployment() ? txnMetadata.getBesuTransaction().getPayload().size() : 0;
  }
}
