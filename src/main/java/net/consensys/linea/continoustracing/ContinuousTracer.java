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
package net.consensys.linea.continoustracing;

import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Supplier;

import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.corset.CorsetValidator;
import net.consensys.linea.zktracer.ZkTracer;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.plugin.services.TraceService;

@Slf4j
public class ContinuousTracer {
  private final TraceService traceService;
  private final CorsetValidator corsetValidator;
  private final Supplier<ZkTracer> zkTracerSupplier;

  public ContinuousTracer(
      final TraceService traceService,
      final CorsetValidator corsetValidator,
      final Supplier<ZkTracer> zkTracerSupplier) {
    this.traceService = traceService;
    this.corsetValidator = corsetValidator;
    this.zkTracerSupplier = zkTracerSupplier;
  }

  public void verifyTraceOfBlock(final Hash blockHash, final String zkEvmBin)
      throws TraceVerificationException {
    final ZkTracer zkTracer = zkTracerSupplier.get();
    traceService.traceBlock(blockHash, zkTracer);

    final CorsetValidator.Result result;
    try {
      result = corsetValidator.isValid(zkTracer.getJsonTrace(), zkEvmBin);
      if (!result.isValid()) {
        log.error("Trace of block {} is not valid", blockHash.toHexString());
        throw new TraceVerificationException(blockHash);
      }
    } catch (RuntimeException e) {
      log.error(
          "Error while validating trace of block {}: {}", blockHash.toHexString(), e.getMessage());
      throw new TraceVerificationException(blockHash, e.getMessage());
    }

    try {
      Files.delete(result.traceFile());
    } catch (IOException e) {
      log.warn("Error while deleting trace file {}: {}", result.traceFile(), e.getMessage());
    }

    log.info("Trace of block {} is valid", blockHash.toHexString());
  }
}
