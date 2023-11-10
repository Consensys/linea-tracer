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
package linea.plugin.acc.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.tests.acceptance.dsl.account.Accounts;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

/** This class tests the block gas limit functionality of the plugin. */
public class BlockGasLimitTest extends LineaPluginTestBase {
  public static final int MAX_CALLDATA_SIZE = 1092; // contract has a call data size of 979
  public static final int MAX_TX_GAS_LIMIT = DefaultGasProvider.GAS_LIMIT.intValue();

  @Override
  public List<String> getTestCliOptions() {
    return new TestCommandLineOptionsBuilder()
        .set("--plugin-linea-max-block-gas=", "44000")
        .build();
  }

  @Override
  public void setup() throws Exception {
    minerNode = besu.createMinerNodeWithExtraCliOptions("miner1", getTestCliOptions());
    cluster.start(minerNode);
    minerNode.execute(minerTransactions.minerStop());
  }

  @Test
  public void shouldNotIncludeTransactionsWhenBlockGasAboveLimit() throws Exception {
    final Web3j web3j = minerNode.nodeRequests().eth();

    final String transactionHash1 =
        sendTransactionWithGivenLengthPayload(Accounts.GENESIS_ACCOUNT_ONE_PRIVATE_KEY, web3j, 0);

    final String largeGasTransactionHash =
        sendTransactionWithGivenLengthPayload(
            Accounts.GENESIS_ACCOUNT_TWO_PRIVATE_KEY, web3j, MAX_CALLDATA_SIZE);

    final String transactionHash2 =
        sendTransactionWithGivenLengthPayload(Accounts.GENESIS_ACCOUNT_ONE_PRIVATE_KEY, web3j, 0);

    // Assert that all three transactions are in the pool
    assertThat(minerNode.execute(txPoolTransactions.getTxPoolContents()).size()).isEqualTo(3);

    // start mining
    startMining();

    assertTransactionsMinedInSameBlock(web3j, List.of(transactionHash1, transactionHash2));

    assertTransactionsMinedInSeparateBlocks(
        web3j, List.of(largeGasTransactionHash, transactionHash2));
  }

  private void startMining() {
    minerNode.execute(minerTransactions.minerStart());
  }

  private String sendTransactionWithGivenLengthPayload(
      final String account, final Web3j web3j, final int num) throws IOException {
    String to = Address.fromHexString("fe3b557e8fb62b89f4916b721be55ceb828dbd73").toString();
    TransactionManager txManager = new RawTransactionManager(web3j, Credentials.create(account));

    return txManager
        .sendTransaction(
            DefaultGasProvider.GAS_PRICE,
            BigInteger.valueOf(MAX_TX_GAS_LIMIT),
            to,
            RandomStringUtils.randomAlphabetic(num),
            BigInteger.ZERO)
        .getTransactionHash();
  }
}
