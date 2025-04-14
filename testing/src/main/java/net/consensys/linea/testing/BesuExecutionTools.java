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
package net.consensys.linea.testing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hyperledger.besu.tests.acceptance.dsl.WaitUtils.waitFor;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import net.consensys.linea.plugins.config.LineaL1L2BridgeSharedConfiguration;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.hyperledger.besu.tests.acceptance.dsl.condition.net.NetConditions;
import org.hyperledger.besu.tests.acceptance.dsl.node.BesuNode;
import org.hyperledger.besu.tests.acceptance.dsl.node.ThreadBesuNodeRunner;
import org.hyperledger.besu.tests.acceptance.dsl.node.cluster.Cluster;
import org.hyperledger.besu.tests.acceptance.dsl.node.cluster.ClusterConfigurationBuilder;
import org.hyperledger.besu.tests.acceptance.dsl.node.configuration.BesuNodeConfigurationBuilder;
import org.hyperledger.besu.tests.acceptance.dsl.node.configuration.BesuNodeFactory;
import org.hyperledger.besu.tests.acceptance.dsl.node.configuration.NodeConfigurationFactory;
import org.hyperledger.besu.tests.acceptance.dsl.transaction.eth.EthTransactions;
import org.hyperledger.besu.tests.acceptance.dsl.transaction.net.NetTransactions;

public class BesuExecutionTools {
  private static BesuNode create(String genesisConfig) {
    NodeConfigurationFactory node = new NodeConfigurationFactory();
    BesuNodeConfigurationBuilder besuNodeConfigurationBuilder =
        new BesuNodeConfigurationBuilder()
            .name("example-test-node")
            .genesisConfigProvider(nodes -> genesisConfig.describeConstable())
            .miningEnabled()
            .jsonRpcEnabled()
            .jsonRpcConfiguration(node.createJsonRpcWithRpcApiEnabledConfig("LINEA"))
            .webSocketConfiguration(node.createWebSocketEnabledConfig())
            .requestedPlugins(
                List.of(
                    "TracerReadinessPlugin",
                    "TracesEndpointServicePlugin",
                    "LineCountsEndpointServicePlugin",
                    "CaptureEndpointServicePlugin"))
            .extraCLIOptions(
                List.of(
                    "--plugin-linea-conflated-trace-generation-traces-output-path=/Users/gaurav/Consensys/linea-tracer/traces",
                    "--plugin-linea-rpc-concurrent-requests-limit=1",
                    String.format(
                        "--plugin-linea-l1l2-bridge-contract=%s",
                        LineaL1L2BridgeSharedConfiguration.TEST_DEFAULT.contract().toHexString()),
                    String.format(
                        "--plugin-linea-l1l2-bridge-topic=%s",
                        LineaL1L2BridgeSharedConfiguration.TEST_DEFAULT.topic().toHexString()),
                    "--plugin-linea-tracer-readiness-server-host=127.0.0.1",
                    "--plugin-linea-tracer-readiness-server-port=8548",
                    "--plugin-linea-tracer-readiness-max-blocks-behind=1"));
    try {
      return new BesuNodeFactory().create(besuNodeConfigurationBuilder.build());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void executeTest(String genesisConfig, List<Transaction> transactions) {
    System.setProperty(
        "besu.plugins.dir", "/Users/gaurav/Consensys/linea-tracer/plugins/build/libs");

    Cluster cluster =
        new Cluster(
            new ClusterConfigurationBuilder().build(),
            new NetConditions(new NetTransactions()),
            new ThreadBesuNodeRunner());
    BesuNode besuNode = create(genesisConfig);
    cluster.start(besuNode);

    EthTransactions ethTransactions = new EthTransactions();
    List<String> txHashes =
        transactions.stream()
            .map(
                tx ->
                    besuNode.execute(
                        ethTransactions.sendRawTransaction(tx.encoded().toHexString())))
            .toList();
    AtomicReference<BigInteger> maxBlockNumber = new AtomicReference<BigInteger>(BigInteger.ZERO);
    waitFor(
        10,
        () -> {
          txHashes.forEach(
              (txHash) -> {
                var maybeTxReceipt =
                    besuNode.execute(ethTransactions.getTransactionReceipt(txHash));
                assertThat(maybeTxReceipt).isPresent();
                var txReceipt = maybeTxReceipt.get();
                maxBlockNumber.set(maxBlockNumber.get().max(txReceipt.getBlockNumber()));
                System.out.println(
                    String.format(
                        "Example test txHash=%s, blockNumber=%s",
                        txReceipt.getTransactionHash(), txReceipt.getBlockNumber()));
              });
        });
    System.out.println("maxBlockNumber=" + maxBlockNumber.get());
  }
}
