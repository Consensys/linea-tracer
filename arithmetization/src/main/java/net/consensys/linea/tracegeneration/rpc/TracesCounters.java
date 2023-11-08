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

import com.fasterxml.jackson.annotation.JsonProperty;

/** Traces Counters represents an execution trace. */
public record TracesCounters(String tracesEngineVersion, Map<String, Long> tracesCountersByModule) {
  @Override
  @JsonProperty("tracesEngineVersion")
  public String tracesEngineVersion() {
    return tracesEngineVersion;
  }

  @JsonProperty("tracesCountersByModule")
  public Map<String, Long> tracesCountersByModule() {
    return tracesCountersByModule;
  }
}
