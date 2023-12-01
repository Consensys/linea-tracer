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

import java.util.Map;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.consensys.linea.zktracer.ZkTracer;
import org.hyperledger.besu.plugin.services.TraceService;

public class LineCountsByBlockCache {
  private static final int NUMBER_OF_BLOCKS_TO_CACHE = 10_000;
  static final Cache<Long, Map<String, Integer>> lineCountsCache =
      CacheBuilder.newBuilder().recordStats().maximumSize(NUMBER_OF_BLOCKS_TO_CACHE).build();

  public static void putBlockTraces(final long blockNumber, final Map<String, Integer> lineCounts) {
    lineCountsCache.put(blockNumber, lineCounts);
  }

  public static Map<String, Integer> getBlockTraces(
      final TraceService traceService, final long blockNumber) {
    if (lineCountsCache.asMap().containsKey(blockNumber)) {
      return lineCountsCache.getIfPresent(blockNumber);

    } else {
      // trace the block, which will add the counters to the cache
      traceService.traceBlock(blockNumber, new ZkTracer());
      return lineCountsCache.getIfPresent(blockNumber);
    }
  }
}
