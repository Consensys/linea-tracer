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

package net.consensys.linea.plugins.rpc.tracegeneration;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.common.base.Stopwatch;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.plugins.BesuServiceProvider;
import net.consensys.linea.plugins.config.LineaL1L2BridgeSharedConfiguration;
import net.consensys.linea.plugins.rpc.RequestLimiter;
import net.consensys.linea.plugins.rpc.Validator;
import net.consensys.linea.tracewriter.TraceWriter;
import net.consensys.linea.zktracer.Fork;
import net.consensys.linea.zktracer.ZkTracer;
import net.consensys.linea.zktracer.json.JsonConverter;
import org.hyperledger.besu.plugin.ServiceManager;
import org.hyperledger.besu.plugin.services.BlockchainService;
import org.hyperledger.besu.plugin.services.TraceService;
import org.hyperledger.besu.plugin.services.rpc.PluginRpcRequest;

/**
 * Sets up an RPC endpoint for generating conflated file trace. This class provides an RPC endpoint
 * named 'generateConflatedTracesToFileV0' under the 'rollup' namespace. When this endpoint is
 * called, it triggers the execution of the 'execute' method, which generates conflated file traces
 * based on the provided request parameters and writes them to a file.
 */
@Slf4j
public class GenerateConflatedTracesV2 {
  private static final JsonConverter CONVERTER = JsonConverter.builder().build();

  private final boolean traceFileCaching = true;
  private final RequestLimiter requestLimiter;
  private final TraceWriter traceWriter;
  private final ServiceManager besuContext;
  private TraceService traceService;
  private final LineaL1L2BridgeSharedConfiguration l1L2BridgeSharedConfiguration;
  private final Fork fork;

  public GenerateConflatedTracesV2(
      final ServiceManager besuContext,
      final RequestLimiter requestLimiter,
      final TracesEndpointConfiguration endpointConfiguration,
      final LineaL1L2BridgeSharedConfiguration lineaL1L2BridgeSharedConfiguration,
      final Fork fork) {
    this.besuContext = besuContext;
    this.requestLimiter = requestLimiter;
    this.traceWriter = new TraceWriter(Paths.get(endpointConfiguration.tracesOutputPath()));
    this.l1L2BridgeSharedConfiguration = lineaL1L2BridgeSharedConfiguration;
    this.fork = fork;
  }

  public String getNamespace() {
    return "linea";
  }

  public String getName() {
    return "generateConflatedTracesToFileV2";
  }

  /**
   * Handles execution traces generation logic.
   *
   * @param request holds parameters of the RPC request.
   * @return an execution file trace.
   */
  public TraceFile execute(final PluginRpcRequest request) {
    return requestLimiter.execute(request, this::generateTraceFile);
  }

  private TraceFile generateTraceFile(PluginRpcRequest request) {
    Stopwatch sw = Stopwatch.createStarted();

    this.traceService =
        Optional.ofNullable(traceService).orElse(BesuServiceProvider.getTraceService(besuContext));

    final Object[] rawParams = request.getParams();

    Validator.validatePluginRpcRequestParams(rawParams);

    TraceRequestParams params =
        CONVERTER.fromJson(CONVERTER.toJson(rawParams[0]), TraceRequestParams.class);

    params.validate();

    final long fromBlock = params.startBlockNumber();
    final long toBlock = params.endBlockNumber();
    // Determine expected path of the trace file.
    Path path = this.traceWriter.traceFilePath(fromBlock, toBlock, params.expectedTracesEngineVersion());
    // Check whether the trace file already exists (or not).
    if(cachedTraceFileAvailable(path)) {
      log.info("[TRACING] reusing existing trace for {}-{} serialized to {} in {}", toBlock, fromBlock, path, sw);
    } else {
      final ZkTracer tracer =
        new ZkTracer(
          fork,
          l1L2BridgeSharedConfiguration,
          BesuServiceProvider.getBesuService(besuContext, BlockchainService.class)
            .getChainId()
            .orElseThrow());

      traceService.trace(
        fromBlock,
        toBlock,
        worldStateBeforeTracing -> tracer.traceStartConflation(toBlock - fromBlock + 1),
        tracer::traceEndConflation,
        tracer);

      log.info("[TRACING] trace for {}-{} computed in {}", fromBlock, toBlock, sw);
      sw.reset().start();
      // Generate trace file
      path = traceWriter.writeTraceToFile(
          tracer,
          params.startBlockNumber(),
          params.endBlockNumber(),
          params.expectedTracesEngineVersion());
      log.info("[TRACING] trace for {}-{} serialized to {} in {}", path, toBlock, fromBlock, sw);
    }

    return new TraceFile(params.expectedTracesEngineVersion(), path.toString());
  }

  /**
   * Determine whether a suitable trace file already exists in the desired location, and that it has the correction
   * versioning, etc.
   *
   * @param path Expected path for tracefile
   * @return
   */
  @SneakyThrows(IOException.class)
  private boolean cachedTraceFileAvailable(final Path path) {
    // Initial sanity checks
    if(!traceFileCaching || !Files.exists(path)) {
     // Caching disabled or trace file doesn't exist.
      return false;
    }
    // Trace file exists.  Check that it has matching tracer versioning.
    String expectedVersion = ZkTracer.class.getPackage().getSpecificationVersion();
    String actualVersion = extractTraceFileReleaseVersion(path);
    //
    return expectedVersion.equals(actualVersion);
  }

  /**
   * Extract the embedded release version from the trace file.  This requires loading the file, and
   * parsing its metadata to look for the release version.
   *
   * @param path
   * @return
   */
  private String extractTraceFileReleaseVersion(final Path path) throws IOException {
    HashMap<String,Object> metadata = new HashMap<>();
    byte[] bytes = extractTraceFileMetaDataBytes(path);
    // Sanity check we correctly read something
    if(bytes != null) {
      // Attempt to parse metadata bytes
      JsonNode node = objectMapper.readTree(bytes).get("releaseVersion");
      // Look for the appropriate field
      if(node != null && node.isValueNode()) {
        return node.asText();
      }
    }
    // Return empty string
    return "";
  }

  private byte[] extractTraceFileMetaDataBytes(final Path path) throws IOException {
    byte[] zktracer = {'z','k','t','r','a','c','e','r'};

    try (FileChannel ch = FileChannel.open(path)) {
      ByteBuffer header = ByteBuffer.allocate(16);
      // Reader header bytes
      int nBytes = ch.read(header);
      //
      if(nBytes != 16) {
        return null;
      }
      // Sanity check watermark
      for(int i=0;i<zktracer.length;i++) {
        if(zktracer[i] != header.get(i)) {
          // corrupted trace file
          return null;
        }
      }
      // Read metadata bytes
      int metadataLength = header.getInt(8 + 2 + 2);
      //
      ByteBuffer metadata = ByteBuffer.allocate(metadataLength);
      byte []metadataBytes = new byte[metadataLength];
      nBytes = ch.read(metadata);
      // Sanity check
      if(nBytes != metadataLength) {
        return null;
      }
      // Looks ok.
      metadata.get(metadataBytes);
      //
      return metadataBytes;
    }
  }


  /**
   * Object writer is used for generating JSON byte strings.
   */
   private static final ObjectMapper objectMapper = new ObjectMapper();

}
