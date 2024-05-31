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

package net.consensys.linea.zktracer.module.hub.fragment;

import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;

import lombok.Setter;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.Trace;
import net.consensys.linea.zktracer.module.hub.defer.PostTransactionDefer;
import net.consensys.linea.zktracer.module.hub.section.TraceSection;
import net.consensys.linea.zktracer.types.EWord;
import net.consensys.linea.zktracer.types.LineaTransaction;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.datatypes.TransactionType;
import org.hyperledger.besu.evm.worldstate.WorldView;

public final class TransactionFragment implements TraceFragment, PostTransactionDefer {
  private final LineaTransaction lineaTransaction;
  @Setter private TraceSection parentSection;

  private TransactionFragment(LineaTransaction lineaTransaction) {
    this.lineaTransaction = lineaTransaction;
  }

  public static TransactionFragment prepare(LineaTransaction lineaTransaction) {
    return new TransactionFragment(lineaTransaction);
  }

  @Override
  public Trace trace(Trace trace) {
    final Transaction tx = lineaTransaction.besuTransaction();
    final EWord to = EWord.of(lineaTransaction.effectiveTo());
    final EWord from = EWord.of(lineaTransaction.getSender());
    final EWord miner = EWord.of(lineaTransaction.coinbase());

    return trace
        .peekAtTransaction(true)
        .pTransactionBatchNum(lineaTransaction.relativeBlockNumber())
        .pTransactionFromAddressHi(from.hi().toLong())
        .pTransactionFromAddressLo(from.lo())
        .pTransactionNonce(Bytes.ofUnsignedLong(tx.getNonce()))
        .pTransactionInitialBalance(bigIntegerToBytes(lineaTransaction.initialBalance()))
        .pTransactionValue(bigIntegerToBytes(tx.getValue().getAsBigInteger()))
        .pTransactionToAddressHi(to.hi().toLong())
        .pTransactionToAddressLo(to.lo())
        .pTransactionRequiresEvmExecution(lineaTransaction.requiresEvmExecution())
        .pTransactionCopyTxcd(lineaTransaction.copyTransactionCallData())
        .pTransactionIsDeployment(tx.getTo().isEmpty())
        .pTransactionIsType2(tx.getType() == TransactionType.EIP1559)
        .pTransactionGasLimit(Bytes.minimalBytes(tx.getGasLimit()))
        .pTransactionGasInitiallyAvailable(
            Bytes.minimalBytes(lineaTransaction.initiallyAvailableGas()))
        .pTransactionGasPrice(Bytes.minimalBytes(lineaTransaction.effectiveGasPrice()))
        .pTransactionBasefee(Bytes.minimalBytes(lineaTransaction.baseFee()))
        .pTransactionCallDataSize(tx.getData().map(Bytes::size).orElse(0))
        .pTransactionInitCodeSize(tx.getInit().map(Bytes::size).orElse(0))
        .pTransactionStatusCode(lineaTransaction.statusCode())
        .pTransactionGasLeftover(Bytes.minimalBytes(lineaTransaction.leftoverGas()))
        .pTransactionRefundCounterInfinity(Bytes.minimalBytes(lineaTransaction.refundCounterMax()))
        .pTransactionRefundEffective(Bytes.minimalBytes(lineaTransaction.refundEffective()))
        .pTransactionCoinbaseAddressHi(miner.hi().toLong())
        .pTransactionCoinbaseAddressLo(miner.lo());
  }

  @Override
  public void runPostTx(Hub hub, WorldView state, Transaction tx, boolean isSuccessful) {}
}
