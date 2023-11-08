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

package net.consensys.linea.tracegeneration.rpc;

import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import net.consensys.linea.zktracer.ZkTracer;
import org.hyperledger.besu.plugin.BesuContext;
import org.hyperledger.besu.plugin.services.exception.PluginRpcEndpointException;
import org.hyperledger.besu.plugin.services.rpc.PluginRpcRequest;

/** Responsible for getting trace counters by block number. */
public class RollupGetTracesCountersByBlockNumberV0 {

  private final BesuContext context;
  private final JsonFactory jsonFactory = new JsonFactory();

  public RollupGetTracesCountersByBlockNumberV0(final BesuContext context) {
    this.context = context;
  }

  public String getNamespace() {
    return "rollup";
  }

  public String getName() {
    return "generateTracesCountersByBlockNumberV0";
  }

  /**
   * Get traces counters for a specified block number. Intended to be used for recovery purposes.
   *
   * @param request holds parameters of the RPC request.
   * @return traces counts by module for the given block.
   */
  public TracesCounters execute(final PluginRpcRequest request) {
    try {
      final TracesCountersRequestParams params =
          TracesCountersRequestParams.createParams(request.getParams());

      final ZkTracer tracer = new ZkTracer();

      return new TracesCounters(
          params.runtimeVersion(), tracer.getTracesCounters(params.fromBlock()));
    } catch (Exception ex) {
      throw new PluginRpcEndpointException(ex.getMessage());
    }
  }
}
