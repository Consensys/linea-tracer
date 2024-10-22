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

package net.consensys.linea.plugins.rpc.linecounts;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

import com.google.common.base.Stopwatch;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.plugins.BesuServiceProvider;
import net.consensys.linea.plugins.rpc.RequestLimiter;
import net.consensys.linea.plugins.rpc.Validator;
import net.consensys.linea.zktracer.ZkTracer;
import net.consensys.linea.zktracer.json.JsonConverter;
import org.hyperledger.besu.plugin.BesuContext;
import org.hyperledger.besu.plugin.services.TraceService;
import org.hyperledger.besu.plugin.services.rpc.PluginRpcRequest;

/** This class is used to generate trace counters. */
@Slf4j
public class GenerateLineCountsV2 {
  private static final JsonConverter CONVERTER = JsonConverter.builder().build();
  private static final int CACHE_SIZE = 10_000;
  private static final Cache<Long, Map<String, Integer>> CACHE =
      CacheBuilder.newBuilder().maximumSize(CACHE_SIZE).build();

  private final RequestLimiter requestLimiter;

  private final BesuContext besuContext;
  private Path modulesToCountConfigFilePath;
  private TraceService traceService;

  public GenerateLineCountsV2(
      final BesuContext context,
      final RequestLimiter requestLimiter,
      final LineCountsEndpointConfiguration endpointConfiguration) {
    this.besuContext = context;
    this.requestLimiter = requestLimiter;

    if (endpointConfiguration.modulesToCountConfigFilePath() != null) {
      this.modulesToCountConfigFilePath =
          Paths.get(endpointConfiguration.modulesToCountConfigFilePath());
    }
  }

  public String getNamespace() {
    return "linea";
  }

  public String getName() {
    return "getBlockTracesCountersV2";
  }

  /**
   * Executes an RPC request to generate trace counters.
   *
   * @param request The PluginRpcRequest object encapsulating the parameters of the RPC request.
   * @return A Counters object encapsulating the results of the counters generation (Modules Line
   *     Count). The method uses a caching mechanism to store and retrieve previously computed trace
   *     counters for specific block numbers
   *     <p>If an exception occurs during the execution of the request, it is caught and wrapped in
   *     a PluginRpcEndpointException and rethrown.
   */
  public LineCounts execute(final PluginRpcRequest request) {
    return requestLimiter.execute(request, this::getLineCounts);
  }

  private LineCounts getLineCounts(PluginRpcRequest request) {
    final Stopwatch sw = Stopwatch.createStarted();

    this.traceService =
        Optional.ofNullable(traceService).orElse(BesuServiceProvider.getTraceService(besuContext));

    final Object[] rawParams = request.getParams();

    Validator.validatePluginRpcRequestParams(rawParams);

    final LineCountsRequestParams params =
        CONVERTER.fromJson(CONVERTER.toJson(rawParams[0]), LineCountsRequestParams.class);

    params.validate();

    final long requestedBlockNumber = params.blockNumber();

    final LineCounts r =
        new LineCounts(
            params.expectedTracesEngineVersion(),
            requestedBlockNumber,
            CACHE
                .asMap()
                .computeIfAbsent(
                    requestedBlockNumber,
                    blockNumber -> {
                      final ZkTracer tracer = new ZkTracer(modulesToCountConfigFilePath);
                      traceService.trace(
                          blockNumber,
                          blockNumber,
                          worldStateBeforeTracing -> tracer.traceStartConflation(1),
                          tracer::traceEndConflation,
                          tracer);

                      return tracer.getModulesLineCount();
                    }));

    log.info("Line count for {} returned in {}", requestedBlockNumber, sw);

    return r;
  }
}
