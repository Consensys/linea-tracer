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

import java.math.BigInteger;
import java.util.List;
import java.util.function.Function;

import linea.plugin.acc.test.tests.web3j.generated.SimpleLog;
import org.hyperledger.besu.tests.acceptance.dsl.account.Accounts;
import org.hyperledger.besu.tests.acceptance.dsl.condition.Condition;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

public class BlockBytesLimitTest extends LineaPluginTestBase {

  private static final BigInteger GAS_PRICE = DefaultGasProvider.GAS_PRICE;
  private static final BigInteger VALUE = BigInteger.ZERO;
  private static final int AVERAGE_TX_RLP_SIZE = 400;
  private static final int FROM_ADDRESS_SIZE = 20;
  private static final int EMPTY_L1_BLOCK_SIZE = 64 + 63 + 64 + 64;
  private static final int LOG_SIZE = 32;

  @Override
  public List<String> getTestCliOptions() {
    int transactionCount = 1;
    int logsCount = 2;
    int maxBytesPerBlock = calculateMaxBytesPerBlock(transactionCount, logsCount);
    return new TestCommandLineOptionsBuilder()
        .set("--plugin-linea-max-block-bytes=", String.valueOf(maxBytesPerBlock))
        .build();
  }

  /**
   * Tests that a transaction is mined when the block bytes size limit is not exceeded. The log
   * count is set to one, which is within the maximum block bytes size set in the getTestCliOptions
   * method. The maximum block bytes size is calculated to accommodate one transaction and two logs.
   * Therefore, a transaction with one log does not exceed this limit and can be successfully mined
   * into a block.
   */
  @Test
  public void shouldMineTransactionWhenBlockBytesNotExceeded() throws Exception {
    verifyTransactionReceipt(BigInteger.ONE, eth::expectSuccessfulTransactionReceipt);
  }

  /**
   * Tests that a transaction is not mined when the block bytes size limit is exceeded. The log
   * count is set to ten, which exceeds the maximum block bytes size set in the getTestCliOptions
   * method. The maximum block bytes size is calculated to accommodate one transaction and two logs.
   * Therefore, a transaction with ten logs exceeds this limit and cannot be mined into a block.
   */
  @Test
  public void shouldNotMineTransactionWhenBlockBytesExceeded() throws Exception {
    verifyTransactionReceipt(BigInteger.TEN, eth::expectNoTransactionReceipt);
  }

  private void verifyTransactionReceipt(BigInteger logCount, Function<String, Condition> condition)
      throws Exception {
    SimpleLog simpleStorage = deploySimpleLogContract();
    Web3j web3j = minerNode.nodeRequests().eth();
    String contractAddress = simpleStorage.getContractAddress();
    Credentials credentials = Credentials.create(Accounts.GENESIS_ACCOUNT_ONE_PRIVATE_KEY);
    TransactionManager txManager = new RawTransactionManager(web3j, credentials, CHAIN_ID);
    String txData = simpleStorage.run(logCount).encodeFunctionCall();
    String hash =
        txManager
            .sendTransaction(
                GAS_PRICE, BigInteger.valueOf(MAX_TX_GAS_LIMIT), contractAddress, txData, VALUE)
            .getTransactionHash();
    minerNode.verify(condition.apply(hash));
  }

  private SimpleLog deploySimpleLogContract() throws Exception {
    Web3j web3j = minerNode.nodeRequests().eth();
    Credentials credentials = Credentials.create(Accounts.GENESIS_ACCOUNT_ONE_PRIVATE_KEY);
    TransactionManager txManager = new RawTransactionManager(web3j, credentials, CHAIN_ID);
    RemoteCall<SimpleLog> deploy = SimpleLog.deploy(web3j, txManager, new DefaultGasProvider());
    return deploy.send();
  }

  private int calculateMaxBytesPerBlock(int transactionCount, int logsCount) {
    return EMPTY_L1_BLOCK_SIZE
        + (transactionCount * (FROM_ADDRESS_SIZE + AVERAGE_TX_RLP_SIZE))
        + (logsCount * LOG_SIZE);
  }
}
