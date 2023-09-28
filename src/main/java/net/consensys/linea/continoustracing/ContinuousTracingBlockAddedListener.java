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
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.plugin.data.AddedBlockContext;
import org.hyperledger.besu.plugin.services.BesuEvents;

@Slf4j
public class ContinuousTracingBlockAddedListener implements BesuEvents.BlockAddedListener {
  final ContinuousTracingConfiguration config;
  final ContinuousTracer continuousTracer;

  public ContinuousTracingBlockAddedListener(
      final ContinuousTracingConfiguration config, final ContinuousTracer continuousTracer) {
    this.config = config;
    this.continuousTracer = continuousTracer;
  }

  @Override
  public void onBlockAdded(final AddedBlockContext addedBlockContext) {
    if (!config.continuousTracing()) {
      return;
    }

    final Hash blockHash = addedBlockContext.getBlockHeader().getBlockHash();
    log.info("Tracing block " + blockHash.toHexString());

    try {
      continuousTracer.verifyTraceOfBlock(blockHash, config.zkEvmBin());
    } catch (TraceVerificationException e) {
      log.error(e.getMessage());
    }
  }
}
