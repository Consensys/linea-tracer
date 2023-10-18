/*
 * Copyright ConsenSys AG.
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

package net.consensys.linea.tracegeneration;

import java.nio.file.Path;

import com.google.auto.service.AutoService;
import net.consensys.linea.tracegeneration.rpc.RollupGenerateConflatedTracesToFileV0;
import org.hyperledger.besu.plugin.BesuContext;
import org.hyperledger.besu.plugin.BesuPlugin;
import org.hyperledger.besu.plugin.services.BesuConfiguration;
import org.hyperledger.besu.plugin.services.RpcEndpointService;
import org.hyperledger.besu.plugin.services.TraceService;

/** Test plugin with RPC endpoint. */
@AutoService(BesuPlugin.class)
public class RollupRpcEndpointServicePlugin implements BesuPlugin {
  private BesuContext context;

  @Override
  public void register(final BesuContext context) {
    this.context = context;
  }

  @Override
  public void start() {
    final Path dataPath =
        context
            .getService(BesuConfiguration.class)
            .map(BesuConfiguration::getDataPath)
            .orElseThrow(
                () ->
                    new RuntimeException(
                        "Unable to find data path. Please ensure BesuConfiguration is registered."));

    final TraceService traceService =
        context
            .getService(TraceService.class)
            .orElseThrow(
                () ->
                    new RuntimeException(
                        "Unable to find trace service. Please ensure TraceService is registered."));

    RollupGenerateConflatedTracesToFileV0 method =
        new RollupGenerateConflatedTracesToFileV0(dataPath, traceService);

    System.out.println("Registering RPC plugin");
    context
        .getService(RpcEndpointService.class)
        .ifPresent(
            rpcEndpointService -> {
              System.out.println("Registering RPC plugin endpoints");
              rpcEndpointService.registerRPCEndpoint(
                  method.getNamespace(), method.getName(), method::execute);
            });
  }

  @Override
  public void stop() {}
}
