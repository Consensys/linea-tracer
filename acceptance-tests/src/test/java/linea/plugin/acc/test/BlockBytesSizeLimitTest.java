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

import linea.plugin.acc.test.tests.web3j.generated.SimpleLog;
import linea.plugin.acc.test.tests.web3j.generated.SimpleStorage;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.tests.acceptance.dsl.account.Account;
import org.hyperledger.besu.tests.acceptance.dsl.account.Accounts;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BlockBytesSizeLimitTest extends LineaPluginTestBase {

  public static final int MAX_TX_GAS_LIMIT = DefaultGasProvider.GAS_LIMIT.intValue();
  private static final BigInteger GAS_PRICE = DefaultGasProvider.GAS_PRICE;
  private static final BigInteger VALUE = BigInteger.ZERO;

  @Override
  public List<String> getTestCliOptions() {
    return new TestCommandLineOptionsBuilder()
        .set("--plugin-linea-max-tx-gas-limit=", String.valueOf(MAX_TX_GAS_LIMIT))
        .build();
  }

  @Test
  public void transactionIsMinedWhenGasLimitIsNotExceeded() throws Exception {
    final SimpleLog simpleStorage = deploySimpleLogContract();

    final Web3j web3j = minerNode.nodeRequests().eth();
    final String contractAddress = simpleStorage.getContractAddress();
    final Credentials credentials = Credentials.create(Accounts.GENESIS_ACCOUNT_ONE_PRIVATE_KEY);
    TransactionManager txManager = new RawTransactionManager(web3j, credentials, CHAIN_ID);

    final String txData = simpleStorage.run(BigInteger.ONE).encodeFunctionCall();

    final String hash =
        txManager
            .sendTransaction(
                GAS_PRICE, BigInteger.valueOf(MAX_TX_GAS_LIMIT), contractAddress, txData, VALUE)
            .getTransactionHash();

    final EthGetTransactionReceipt receipt = web3j.ethGetTransactionReceipt(hash).send();
    assertThat(receipt.getTransactionReceipt()).isEmpty();

    // make sure that a transaction that is not too big was mined
    minerNode.verify(eth.expectSuccessfulTransactionReceipt(hash));
  }


  protected SimpleLog deploySimpleLogContract() throws Exception {
    final Web3j web3j = minerNode.nodeRequests().eth();
    final Credentials credentials = Credentials.create(Accounts.GENESIS_ACCOUNT_ONE_PRIVATE_KEY);
    TransactionManager txManager = new RawTransactionManager(web3j, credentials, CHAIN_ID);

    final RemoteCall<SimpleLog> deploy =
            SimpleLog.deploy(web3j, txManager, new DefaultGasProvider());
    return deploy.send();
  }
}
