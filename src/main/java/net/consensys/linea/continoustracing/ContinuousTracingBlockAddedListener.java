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

import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.continoustracing.exception.InvalidTraceHandlerException;
import net.consensys.linea.continoustracing.exception.TraceVerificationException;
import net.consensys.linea.corset.CorsetValidator;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.plugin.data.AddedBlockContext;
import org.hyperledger.besu.plugin.data.BlockHeader;
import org.hyperledger.besu.plugin.services.BesuEvents;

@Slf4j
public class ContinuousTracingBlockAddedListener implements BesuEvents.BlockAddedListener {
  final ContinuousTracer continuousTracer;
  final InvalidTraceHandler invalidTraceHandler;
  final String zkEvmBin;

  public ContinuousTracingBlockAddedListener(
      final ContinuousTracer continuousTracer,
      final InvalidTraceHandler invalidTraceHandler,
      final String zkEvmBin) {
    this.continuousTracer = continuousTracer;
    this.invalidTraceHandler = invalidTraceHandler;
    this.zkEvmBin = zkEvmBin;
  }

  @Override
  public void onBlockAdded(final AddedBlockContext addedBlockContext) {
    final BlockHeader blockHeader = addedBlockContext.getBlockHeader();
    final Hash blockHash = blockHeader.getBlockHash();
    log.info("Tracing block {} ({})", blockHeader.getNumber(), blockHash.toHexString());

    try {
      final CorsetValidator.Result traceResult =
          continuousTracer.verifyTraceOfBlock(blockHash, zkEvmBin);

      if (!traceResult.isValid()) {
        invalidTraceHandler.handle(blockHeader, traceResult);
      }
    } catch (TraceVerificationException e) {
      log.error(e.getMessage());
    } catch (InvalidTraceHandlerException e) {
      log.error("Error while handling invalid trace: {}", e.getMessage());
    }
  }
}
