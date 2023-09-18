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

package net.consensys.linea.tracegeneration;

import com.google.auto.service.AutoService;
import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.tracegeneration.rpc.RollupGenerateConflatedTracesToFileV0;
import net.consensys.linea.tracegeneration.rpc.RollupGetTracesCountersByBlockNumberV0;
import net.consensys.linea.zktracer.opcode.OpCodes;
import org.hyperledger.besu.plugin.BesuContext;
import org.hyperledger.besu.plugin.BesuPlugin;
import org.hyperledger.besu.plugin.services.RpcEndpointService;

/** Rollup plugin with RPC endpoints. */
@AutoService(BesuPlugin.class)
@Slf4j
public class RollupRpcEndpointServicePlugin implements BesuPlugin {

  @Override
  public void register(final BesuContext context) {
    RollupGenerateConflatedTracesToFileV0 conflateMethod =
        new RollupGenerateConflatedTracesToFileV0(context);
    RollupGetTracesCountersByBlockNumberV0 countersMethod =
        new RollupGetTracesCountersByBlockNumberV0(context);

    System.out.println("Registering RPC plugin");

    context
        .getService(RpcEndpointService.class)
        .ifPresent(
            rpcEndpointService -> {
              log.info("Registering RPC plugin endpoints");
              rpcEndpointService.registerRPCEndpoint(
                  conflateMethod.getNamespace(), conflateMethod.getName(), conflateMethod::execute);
              rpcEndpointService.registerRPCEndpoint(
                  countersMethod.getNamespace(), countersMethod.getName(), countersMethod::execute);
            });
  }

  @Override
  public void start() {
    OpCodes.load();
  }

  @Override
  public void stop() {}
}
