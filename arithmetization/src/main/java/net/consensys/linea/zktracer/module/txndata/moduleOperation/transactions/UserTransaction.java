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
import static net.consensys.linea.zktracer.Fork.isPostPrague;
import static net.consensys.linea.zktracer.Trace.*;
import static net.consensys.linea.zktracer.module.txndata.moduleOperation.ShanghaiTxndataOperation.MAX_INIT_CODE_SIZE_BYTES;
import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;
import static org.hyperledger.besu.datatypes.TransactionType.ACCESS_LIST;
import static org.hyperledger.besu.datatypes.TransactionType.FRONTIER;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import net.consensys.linea.zktracer.Fork;
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
  public final TransactionProcessingMetadata txn;
  public final List<TxnDataRow> rows = new ArrayList<>();
  public final Wcp wcp;
  public final Euc euc;
  public final Fork fork;

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

    this.txn = txnMetadata;
    this.wcp = txnData.getHub().wcp();
    this.euc = txnData.getHub().euc();
    this.fork = txnData.getFork();

    this.process();
  }

  private void process() {
    hubRow();
    rlpRow();
    maxNonceCheckCmptnRow();
    initialBalanceCheckCmptnRow();
    maxInitCodeSizeCheckCmptnRow();
    initCodePricingCmptnRow();
    gasLimitMustCoverTheUpfrontGasCostCmptnRow();
    gasLimitMustCoverTheTransactionFloorCostCmptnRow();
    final long upperLimitForGasRefunds = upperLimitForGasRefundsCmptnRow();
    final long consumedGasAfterRefunds = effectiveRefundsCmptnRow(upperLimitForGasRefunds);
    comparingEffectiveRefundToFloorCostCmptnRow(consumedGasAfterRefunds);
  }

  private void hubRow() {
    rows.add(new HubRow(txn));
  }

  private void rlpRow() {
    rows.add(new RlpRow(txn));
  }

  /** Performs the EIP-2681 check that the nonce is less than 2^64 - 1. */
  private void maxNonceCheckCmptnRow() {
    WcpRow maxNonceCheckEip2681 =
        WcpRow.smallCallToLt(
            wcp, Bytes.ofUnsignedLong(txn.getBesuTransaction().getNonce()), EIP_2681_MAX_NONCE);
    checkArgument(maxNonceCheckEip2681.result(), "Transaction nonce is too high");
    rows.add(maxNonceCheckEip2681);
  }

  /**
   * Ensures that the initial balance of the sender account is sufficient to cover the maximum
   * possible cost of the transaction (value + gas_limit * max(gas_price, max_fee)).
   */
  private void initialBalanceCheckCmptnRow() {
    final Bytes initialBalance = bigIntegerToBytes(txn.getInitialBalance());
    final BigInteger value = txn.getBesuTransaction().getValue().getAsBigInteger();
    final BigInteger maxGasPrice =
        transactionTypeHasEip1559GasSemantics()
            ? txn.getBesuTransaction().getMaxFeePerGas().get().getAsBigInteger()
            : txn.getBesuTransaction().getGasPrice().get().getAsBigInteger();
    final Bytes maxCostInWei =
        bigIntegerToBytes(
            value.add(
                maxGasPrice.multiply(BigInteger.valueOf(txn.getBesuTransaction().getGasLimit()))));
    final WcpRow initialBalanceMustCoverValueAndGas =
        WcpRow.smallCallToLeq(wcp, maxCostInWei, initialBalance);
    checkArgument(
        initialBalanceMustCoverValueAndGas.result(),
        "Initial balance %s does not cover the max value and gas cost %s",
        initialBalance,
        maxCostInWei);
  }

  /** Performs the EIP-3860 check that the init code size is at most 49152 bytes. */
  private void maxInitCodeSizeCheckCmptnRow() {
    final int initCodeSize = initCodeSizeOrZero();
    final WcpRow eip3860requiredInitCodeSizeCheck =
        WcpRow.smallCallToLeq(wcp, Bytes.ofUnsignedInt(initCodeSize), MAX_INIT_CODE_SIZE_BYTES);
    checkArgument(
        eip3860requiredInitCodeSizeCheck.result(),
        "Init code size %s exceeds the EIP-3860 limit of %s bytes",
        initCodeSize,
        MAX_INIT_CODE_SIZE_BYTES);
    rows.add(eip3860requiredInitCodeSizeCheck);
  }

  /** Adds the EIP-3860-induced init code pricing computation row. */
  private void initCodePricingCmptnRow() {
    // Deployment transaction
    final long dividend = initCodeSizeOrZero() + ((long) WORD_SIZE_MO);
    rows.add(EucRow.callToEuc(euc, dividend, WORD_SIZE));
  }

  private void gasLimitMustCoverTheUpfrontGasCostCmptnRow() {
    final long upfrontGasCost = upfrontGasCost();
    final WcpRow gasLimitMustCoverUpfrontGasCost =
        WcpRow.smallCallToLeq(
            wcp,
            Bytes.ofUnsignedLong(upfrontGasCost),
            Bytes.ofUnsignedLong(txn.getBesuTransaction().getGasLimit()));

    checkArgument(
        gasLimitMustCoverUpfrontGasCost.result(),
        "Gas limit %s does not cover the upfront gas cost %s",
        txn.getBesuTransaction().getGasLimit(),
        upfrontGasCost);

    rows.add(gasLimitMustCoverUpfrontGasCost);
  }

  private void gasLimitMustCoverTheTransactionFloorCostCmptnRow() {
    final long floorGasCost = callDataFloorCost();
    final WcpRow gasLimitMustCoverFloorGasCost =
        WcpRow.smallCallToLeq(
            wcp,
            Bytes.ofUnsignedLong(floorGasCost),
            Bytes.ofUnsignedLong(txn.getBesuTransaction().getGasLimit()));

    if (isPostPrague(fork)) {
      checkArgument(
          gasLimitMustCoverFloorGasCost.result(),
          "Gas limit %s does not cover the transaction floor gas cost %s",
          txn.getBesuTransaction().getGasLimit(),
          floorGasCost);
    }

    rows.add(gasLimitMustCoverFloorGasCost);
  }

  private long upperLimitForGasRefundsCmptnRow() {
    final long executionGasCost = txn.getBesuTransaction().getGasLimit() - txn.getLeftoverGas();
    final EucRow upperLimitForGasRefunds =
        EucRow.callToEuc(euc, executionGasCost, MAX_REFUND_QUOTIENT);

    return upperLimitForGasRefunds.quotient();
  }

  private long effectiveRefundsCmptnRow(long upperLimitForGasRefunds) {
    final WcpRow effectiveRefunds =
        WcpRow.smallCallToLt(
            wcp,
            Bytes.ofUnsignedLong(txn.getRefundCounterMax()),
            Bytes.ofUnsignedLong(upperLimitForGasRefunds));

    final boolean accruedRefundsAreLtUpperLimit = effectiveRefunds.result();
    final long consumedGasAfterRefunds =
        accruedRefundsAreLtUpperLimit
            ? txn.getBesuTransaction().getGasLimit()
                - txn.getLeftoverGas()
                - txn.getRefundCounterMax()
            : txn.getBesuTransaction().getGasLimit()
                - txn.getLeftoverGas()
                - upperLimitForGasRefunds;

    return consumedGasAfterRefunds;
  }

  private void comparingEffectiveRefundToFloorCostCmptnRow(long consumedGasAfterRefunds) {
    final WcpRow comparingEffectiveRefundsVsFloorCost =
        WcpRow.smallCallToLt(
            wcp,
            Bytes.ofUnsignedLong(consumedGasAfterRefunds),
            Bytes.ofUnsignedLong(callDataFloorCost()));

    final long refundEffective =
        txn.getBesuTransaction().getGasLimit()
            - (comparingEffectiveRefundsVsFloorCost.result()
                ? callDataFloorCost()
                : consumedGasAfterRefunds);

    checkArgument(refundEffective == txn.getRefundEffective());
  }

  private long initCodeCost() {
    final long numberOfInitCodeWords = (initCodeSizeOrZero() + WORD_SIZE_MO) / WORD_SIZE;
    return GAS_CONST_INIT_CODE_WORD * numberOfInitCodeWords;
  }

  private long weightedByteCount() {
    return ((long) txn.numberOfZeroBytesInPayload())
        + 4 * ((long) txn.numberOfNonZeroBytesInPayload());
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
    TransactionType type = txn.getBesuTransaction().getType();
    return switch (type) {
      case FRONTIER, ACCESS_LIST -> 14;
      case EIP1559 -> 16;
      default -> throw new RuntimeException("Transaction type " + type + " not supported");
    };
  }

  private boolean transactionTypeHasEip1559GasSemantics() {
    return !(txn.getBesuTransaction().getType() == FRONTIER
        || txn.getBesuTransaction().getType() == ACCESS_LIST);
  }

  // TODO: this will change with Prague's EIP-7702 and type 4 transactions
  private boolean transactionTypeSupportsAccessLists() {
    return txn.getBesuTransaction().getType() == ACCESS_LIST;
  }

  private int initCodeSizeOrZero() {
    return txn.isDeployment() ? txn.getBesuTransaction().getPayload().size() : 0;
  }

  private long upfrontGasCost() {
    return dataCost()
        + initCodeCost()
        + GAS_CONST_G_TRANSACTION
        + (txn.isDeployment() ? GAS_CONST_G_TX_CREATE : 0)
        + (transactionTypeSupportsAccessLists() ? txn.numberOfWarmedAddresses() : 0)
            * (long) GAS_CONST_G_ACCESS_LIST_ADRESS
        + (txn.isDeployment() ? GAS_CONST_G_TX_CREATE : 0)
        + (transactionTypeSupportsAccessLists() ? txn.numberOfWarmedStorageKeys() : 0)
            * (long) GAS_CONST_G_ACCESS_LIST_STORAGE;
  }
}
