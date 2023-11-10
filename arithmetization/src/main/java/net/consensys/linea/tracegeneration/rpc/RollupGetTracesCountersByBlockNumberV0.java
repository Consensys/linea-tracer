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

package net.consensys.linea.tracegeneration.rpc;

import com.google.common.annotations.VisibleForTesting;
import net.consensys.linea.tracegeneration.LineCountsByBlockCache;
import org.hyperledger.besu.plugin.BesuContext;
import org.hyperledger.besu.plugin.services.TraceService;
import org.hyperledger.besu.plugin.services.exception.PluginRpcEndpointException;
import org.hyperledger.besu.plugin.services.rpc.PluginRpcRequest;

/** Responsible for getting trace counters. */
public class RollupGetTracesCountersByBlockNumberV0 implements RollupRpcMethod {

  private final BesuContext besuContext;

  private TraceService traceService;

  public RollupGetTracesCountersByBlockNumberV0(final BesuContext besuContext) {
    this.besuContext = besuContext;
  }

  public String getNamespace() {
    return "rollup";
  }

  public String getName() {
    return "getTracesCountersByBlockNumberV0";
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

      return getTracesCounters(params);
    } catch (Exception ex) {
      throw new PluginRpcEndpointException(ex.getMessage());
    }
  }

  @VisibleForTesting
  TracesCounters getTracesCounters(final TracesCountersRequestParams params) {
    if (traceService == null) {
      traceService = initTraceService();
    }
    return new TracesCounters(
        params.runtimeVersion(),
        LineCountsByBlockCache.getBlockTraces(traceService, params.fromBlock()));
  }

  private TraceService initTraceService() {
    return besuContext
        .getService(TraceService.class)
        .orElseThrow(
            () ->
                new RuntimeException(
                    "Unable to find trace service. Please ensure TraceService is registered."));
  }
}
