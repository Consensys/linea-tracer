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

package net.consensys.linea.linecounting.rpc;

import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.zktracer.ZkTracer;
import org.hyperledger.besu.plugin.BesuContext;
import org.hyperledger.besu.plugin.services.TraceService;
import org.hyperledger.besu.plugin.services.exception.PluginRpcEndpointException;
import org.hyperledger.besu.plugin.services.rpc.PluginRpcRequest;

/** Responsible for conflated file traces generation. */
@Slf4j
public class RollupGenerateLineCountV0 {

  private final BesuContext besuContext;

  private TraceService traceService;

  public RollupGenerateLineCountV0(final BesuContext besuContext) {
    this.besuContext = besuContext;
  }

  public String getNamespace() {
    return "rollup";
  }

  public String getName() {
    return "getTracesCountersByBlockNumberV0";
  }

  /**
   * Handles execution traces generation logic.
   *
   * @param request holds parameters of the RPC request.
   * @return an execution file trace.
   */
  public LineCount execute(final PluginRpcRequest request) {
    if (traceService == null) {
      traceService = initTraceService();
    }

    try {
      LineCountRequestParams params = LineCountRequestParams.createTraceParams(request.getParams());

      final long blockNumber = params.blockNumber();
      final ZkTracer tracer = new ZkTracer();
      traceService.trace(
          blockNumber,
          blockNumber,
          worldStateBeforeTracing -> {
            // before tracing
            tracer.traceStartConflation(1);
          },
          worldStateAfterTracing -> {
            // after tracing
            tracer.traceEndConflation();
          },
          tracer);
      return new LineCount(params.runtimeVersion(), tracer.getModulesLineCount());
    } catch (Exception ex) {
      throw new PluginRpcEndpointException(ex.getMessage());
    }
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
