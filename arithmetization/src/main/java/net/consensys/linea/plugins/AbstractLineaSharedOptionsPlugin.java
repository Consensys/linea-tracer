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

package net.consensys.linea.plugins;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.plugins.config.LineaL1L2BridgeSharedCliOptions;
import net.consensys.linea.plugins.config.LineaL1L2BridgeSharedConfiguration;
import net.consensys.linea.plugins.config.LineaTracerSharedCliOptions;
import net.consensys.linea.plugins.config.LineaTracerSharedConfiguration;

/** In this class we put CLI options that are shared with other plugins not defined here */
@Slf4j
public abstract class AbstractLineaSharedOptionsPlugin extends AbstractLineaOptionsPlugin {

  @Override
  public Map<String, LineaOptionsPluginConfiguration> getLineaPluginConfigMap() {
    final LineaTracerSharedCliOptions tracerCliOptions = LineaTracerSharedCliOptions.create();
    final LineaL1L2BridgeSharedCliOptions l1L2BridgeCliOptions =
        LineaL1L2BridgeSharedCliOptions.create();

    return Map.of(
        LineaTracerSharedCliOptions.CONFIG_KEY, tracerCliOptions.asPluginConfig(),
        LineaL1L2BridgeSharedCliOptions.CONFIG_KEY, l1L2BridgeCliOptions.asPluginConfig());
  }

  public LineaTracerSharedConfiguration tracerSharedConfiguration() {
    return (LineaTracerSharedConfiguration)
        lineaPluginConfigMap.get(LineaTracerSharedCliOptions.CONFIG_KEY).optionsConfig();
  }

  public LineaL1L2BridgeSharedConfiguration l1L2BridgeSharedConfiguration() {
    return (LineaL1L2BridgeSharedConfiguration)
        lineaPluginConfigMap.get(LineaL1L2BridgeSharedCliOptions.CONFIG_KEY).optionsConfig();
  }

  @Override
  public void start() {
    super.start();
  }
}
