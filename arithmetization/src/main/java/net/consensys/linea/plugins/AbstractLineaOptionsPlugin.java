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
import org.hyperledger.besu.plugin.BesuContext;
import org.hyperledger.besu.plugin.BesuPlugin;
import org.hyperledger.besu.plugin.services.PicoCLIOptions;

/**
 * In this class we put CLI options that are private to plugins in this repo.
 *
 * <p>For the moment is just a placeholder since there are no private options
 */
@Slf4j
public abstract class AbstractLineaOptionsPlugin implements BesuPlugin {
  private static final String CLI_OPTIONS_PREFIX = "linea";
  private static boolean cliOptionsRegistered = false;

  protected Map<String, LineaOptionsPluginConfiguration> lineaPluginConfigMap;

  public abstract Map<String, LineaOptionsPluginConfiguration> getLineaPluginConfigMap();

  @Override
  public synchronized void register(final BesuContext context) {
    if (!cliOptionsRegistered) {
      final PicoCLIOptions cmdlineOptions =
          context
              .getService(PicoCLIOptions.class)
              .orElseThrow(
                  () ->
                      new IllegalStateException(
                          "Failed to obtain PicoCLI options from the BesuContext"));

      lineaPluginConfigMap = getLineaPluginConfigMap();

      lineaPluginConfigMap.forEach(
          (optsKey, config) -> {
            cmdlineOptions.addPicoCLIOptions(CLI_OPTIONS_PREFIX, config.cliOptions());
          });

      cliOptionsRegistered = true;
    }
  }

  @Override
  public void beforeExternalServices() {
    lineaPluginConfigMap.forEach(
        (opts, config) -> {
          log.debug(
              "Configured plugin {} with configuration: {}", getName(), config.optionsConfig());
        });
  }

  @Override
  public void start() {}

  @Override
  public void stop() {
    cliOptionsRegistered = false;
  }
}
