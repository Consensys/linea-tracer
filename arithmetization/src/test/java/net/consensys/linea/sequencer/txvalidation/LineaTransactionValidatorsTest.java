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

package net.consensys.linea.sequencer.txvalidation;

import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.config.LineaProfitabilityCliOptions;
import net.consensys.linea.config.LineaTransactionValidatorConfiguration;
import net.consensys.linea.sequencer.txvalidation.validators.LineaTransactionValidators;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.plugin.data.BlockContext;
import org.hyperledger.besu.plugin.data.BlockHeader;
import org.hyperledger.besu.plugin.services.BesuConfiguration;
import org.hyperledger.besu.plugin.services.BlockchainService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
@RequiredArgsConstructor
public class LineaTransactionValidatorsTest {

  public static final Address DENIED =
      Address.fromHexString("0x0000000000000000000000000000000000001000");
  public static final Address NOT_DENIED =
      Address.fromHexString("0x0000000000000000000000000000000000001001");
  public static final Address PRECOMPILED = Address.precompiled(0xa);
  public static final int MAX_TX_GAS_LIMIT = 9_000_000;
  public static final int MAX_TX_CALLDATA_SIZE = 10_000;
  public static final double TX_POOL_MIN_MARGIN = 1.0;
  private LineaTransactionValidators lineaTransactionValidators;

  @BeforeEach
  public void initialize() {
    Set<Address> denied = Set.of(DENIED);
    lineaTransactionValidators =
        new LineaTransactionValidators(
            new TestBesuConfiguration(),
            new TestBlockchainService(),
            new LineaTransactionValidatorConfiguration("", MAX_TX_GAS_LIMIT, MAX_TX_CALLDATA_SIZE),
            LineaProfitabilityCliOptions.create().toDomainObject().toBuilder()
                .txPoolMinMargin(TX_POOL_MIN_MARGIN)
                .build(),
            denied);
  }

  @Test
  public void validatedIfNoneOnList() {
    final org.hyperledger.besu.ethereum.core.Transaction.Builder builder =
        org.hyperledger.besu.ethereum.core.Transaction.builder();
    final org.hyperledger.besu.ethereum.core.Transaction transaction =
        builder.sender(NOT_DENIED).to(NOT_DENIED).gasPrice(Wei.ZERO).payload(Bytes.EMPTY).build();
    Assertions.assertEquals(
        lineaTransactionValidators.validateTransaction(transaction), Optional.empty());
  }

  @Test
  public void deniedIfFromAddressIsOnList() {
    final org.hyperledger.besu.ethereum.core.Transaction.Builder builder =
        org.hyperledger.besu.ethereum.core.Transaction.builder();
    final org.hyperledger.besu.ethereum.core.Transaction transaction =
        builder.sender(DENIED).to(NOT_DENIED).gasPrice(Wei.ZERO).payload(Bytes.EMPTY).build();
    Assertions.assertEquals(
        lineaTransactionValidators.validateTransaction(transaction).orElseThrow(),
        "sender 0x0000000000000000000000000000000000001000 is blocked as appearing on the SDN or other legally prohibited list");
  }

  @Test
  public void deniedIfToAddressIsOnList() {
    final org.hyperledger.besu.ethereum.core.Transaction.Builder builder =
        org.hyperledger.besu.ethereum.core.Transaction.builder();
    final org.hyperledger.besu.ethereum.core.Transaction transaction =
        builder.sender(NOT_DENIED).to(DENIED).gasPrice(Wei.ZERO).payload(Bytes.EMPTY).build();
    Assertions.assertEquals(
        lineaTransactionValidators.validateTransaction(transaction).orElseThrow(),
        "recipient 0x0000000000000000000000000000000000001000 is blocked as appearing on the SDN or other legally prohibited list");
  }

  @Test
  public void deniedIfToAddressIsPrecompiled() {
    final org.hyperledger.besu.ethereum.core.Transaction.Builder builder =
        org.hyperledger.besu.ethereum.core.Transaction.builder();
    final org.hyperledger.besu.ethereum.core.Transaction transaction =
        builder.sender(NOT_DENIED).to(PRECOMPILED).gasPrice(Wei.ZERO).payload(Bytes.EMPTY).build();
    Assertions.assertEquals(
        lineaTransactionValidators.validateTransaction(transaction).orElseThrow(),
        "destination address is a precompile address and cannot receive transactions");
  }

  @Test
  public void validatedWithValidGasLimit() {
    final org.hyperledger.besu.ethereum.core.Transaction.Builder builder =
        org.hyperledger.besu.ethereum.core.Transaction.builder();
    final org.hyperledger.besu.ethereum.core.Transaction transaction =
        builder
            .sender(NOT_DENIED)
            .to(NOT_DENIED)
            .gasLimit(MAX_TX_GAS_LIMIT)
            .gasPrice(Wei.ZERO)
            .payload(Bytes.EMPTY)
            .build();
    Assertions.assertEquals(
        lineaTransactionValidators.validateTransaction(transaction), Optional.empty());
  }

  @Test
  public void rejectedWithMaxGasLimitPlusOne() {
    final org.hyperledger.besu.ethereum.core.Transaction.Builder builder =
        org.hyperledger.besu.ethereum.core.Transaction.builder();
    final org.hyperledger.besu.ethereum.core.Transaction transaction =
        builder
            .sender(NOT_DENIED)
            .to(NOT_DENIED)
            .gasLimit(MAX_TX_GAS_LIMIT + 1)
            .gasPrice(Wei.ZERO)
            .payload(Bytes.EMPTY)
            .build();
    Assertions.assertEquals(
        lineaTransactionValidators.validateTransaction(transaction).orElseThrow(),
        "Gas limit of transaction is greater than the allowed max of " + MAX_TX_GAS_LIMIT);
  }

  @Test
  public void validatedWithValidCalldata() {
    final org.hyperledger.besu.ethereum.core.Transaction.Builder builder =
        org.hyperledger.besu.ethereum.core.Transaction.builder();
    final org.hyperledger.besu.ethereum.core.Transaction transaction =
        builder
            .sender(NOT_DENIED)
            .to(NOT_DENIED)
            .gasLimit(MAX_TX_GAS_LIMIT)
            .gasPrice(Wei.ZERO)
            .payload(Bytes.random(MAX_TX_CALLDATA_SIZE))
            .build();
    Assertions.assertEquals(
        lineaTransactionValidators.validateTransaction(transaction), Optional.empty());
  }

  @Test
  public void rejectedWithTooBigCalldata() {
    final org.hyperledger.besu.ethereum.core.Transaction.Builder builder =
        org.hyperledger.besu.ethereum.core.Transaction.builder();
    final org.hyperledger.besu.ethereum.core.Transaction transaction =
        builder
            .sender(NOT_DENIED)
            .to(NOT_DENIED)
            .gasLimit(MAX_TX_GAS_LIMIT)
            .gasPrice(Wei.ZERO)
            .payload(Bytes.random(MAX_TX_CALLDATA_SIZE + 1))
            .build();
    Assertions.assertEquals(
        lineaTransactionValidators.validateTransaction(transaction).orElseThrow(),
        "Calldata of transaction is greater than the allowed max of " + MAX_TX_CALLDATA_SIZE);
  }

  private static class TestBesuConfiguration implements BesuConfiguration {
    @Override
    public Path getStoragePath() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Path getDataPath() {
      throw new UnsupportedOperationException();
    }

    @Override
    public int getDatabaseVersion() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Wei getMinGasPrice() {
      return Wei.of(1_000_000_000);
    }
  }

  private static class TestBlockchainService implements BlockchainService {

    @Override
    public Optional<BlockContext> getBlockByNumber(final long l) {
      throw new UnsupportedOperationException();
    }

    @Override
    public Hash getChainHeadHash() {
      throw new UnsupportedOperationException();
    }

    @Override
    public BlockHeader getChainHeadHeader() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Wei> getNextBlockBaseFee() {
      return Optional.of(Wei.of(7));
    }
  }
}
