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

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import net.consensys.linea.plugins.config.LineaL1L2BridgeSharedConfiguration;
import org.hyperledger.besu.ethereum.api.jsonrpc.JsonRpcConfiguration;
import org.hyperledger.besu.tests.acceptance.dsl.node.BesuNode;
import org.hyperledger.besu.tests.acceptance.dsl.node.configuration.BesuNodeConfigurationBuilder;
import org.hyperledger.besu.tests.acceptance.dsl.node.configuration.BesuNodeFactory;
import org.hyperledger.besu.tests.acceptance.dsl.node.configuration.NodeConfigurationFactory;

public class BesuNodeBuilder {

  public static BesuNode create(
      LineaL1L2BridgeSharedConfiguration bridgeConfiguration,
      String genesisConfig,
      Integer jsonRpcPort,
      Path tracesPath,
      Integer shomeiPort)
      throws IOException {
    assert (bridgeConfiguration != null);
    assert (genesisConfig != null);
    assert (tracesPath != null);
    assert (jsonRpcPort != null);
    assert (shomeiPort != null);

    NodeConfigurationFactory node = new NodeConfigurationFactory();
    JsonRpcConfiguration jsonRpcConfiguration =
        node.createJsonRpcWithRpcApiEnabledConfig("LINEA", "SHOMEI");
    jsonRpcConfiguration.setPort(jsonRpcPort);
    BesuNodeConfigurationBuilder besuNodeConfigurationBuilder =
        new BesuNodeConfigurationBuilder()
            .name("example-test-node")
            .genesisConfigProvider(nodes -> genesisConfig.describeConstable())
            .miningEnabled()
            .jsonRpcEnabled()
            .jsonRpcConfiguration(jsonRpcConfiguration)
            .requestedPlugins(
                List.of(
                    "BesuShomeiRpcPlugin",
                    "ZkTrieLogPlugin",
                    // "TracerReadinessPlugin",
                    "TracesEndpointServicePlugin",
                    "LineCountsEndpointServicePlugin",
                    "CaptureEndpointServicePlugin"))
            .extraCLIOptions(
                List.of(
                    "--plugin-shomei-http-host=127.0.0.1",
                    String.format("--plugin-shomei-http-port=%s", jsonRpcPort),
                    String.format(
                        "--plugin-linea-conflated-trace-generation-traces-output-path=%s",
                        tracesPath),
                    "--plugin-linea-rpc-concurrent-requests-limit=1",
                    String.format(
                        "--plugin-linea-l1l2-bridge-contract=%s",
                        bridgeConfiguration.contract().toHexString()),
                    String.format(
                        "--plugin-linea-l1l2-bridge-topic=%s",
                        bridgeConfiguration.topic().toHexString())
                    // "--plugin-linea-tracer-readiness-server-host=127.0.0.1",
                    // "--plugin-linea-tracer-readiness-server-port=8548",
                    //  "--plugin-linea-tracer-readiness-max-blocks-behind=1"
                    ));
    return new BesuNodeFactory().create(besuNodeConfigurationBuilder.build());
  }
}
