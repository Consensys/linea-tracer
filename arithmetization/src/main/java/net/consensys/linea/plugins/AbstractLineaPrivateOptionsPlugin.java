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

import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.plugins.config.LineaTracerPrivateCliOptions;
import net.consensys.linea.plugins.config.LineaTracerPrivateConfiguration;
import org.hyperledger.besu.plugin.BesuContext;
import org.hyperledger.besu.plugin.services.PicoCLIOptions;

/**
 * In this class we put CLI options that are private to plugins in this repo.
 *
 * <p>For the moment is just a placeholder since there are no private options
 */
@Slf4j
public abstract class AbstractLineaPrivateOptionsPlugin extends AbstractLineaSharedOptionsPlugin {
  private static final String CLI_OPTIONS_PREFIX = "linea";
  private static boolean cliOptionsRegistered = false;
  private static boolean configured = false;
  private static LineaTracerPrivateCliOptions tracerPrivateCliOptions;
  protected static LineaTracerPrivateConfiguration tracerPrivateConfiguration;

  @Override
  public synchronized void register(final BesuContext context) {
    super.register(context);
    if (!cliOptionsRegistered) {
      final PicoCLIOptions cmdlineOptions =
          context
              .getService(PicoCLIOptions.class)
              .orElseThrow(
                  () ->
                      new IllegalStateException(
                          "Failed to obtain PicoCLI options from the BesuContext"));

      tracerPrivateCliOptions = LineaTracerPrivateCliOptions.create();

      cmdlineOptions.addPicoCLIOptions(CLI_OPTIONS_PREFIX, tracerPrivateCliOptions);

      cliOptionsRegistered = true;
    }
  }

  @Override
  public void beforeExternalServices() {
    super.beforeExternalServices();
    if (!configured) {
      tracerPrivateConfiguration = tracerPrivateCliOptions.toDomainObject();
      configured = true;
    }

    log.debug(
        "Configured plugin {} with tracer private configuration: {}",
        getName(),
        tracerPrivateConfiguration);
  }

  @Override
  public void start() {
    super.start();
  }

  @Override
  public void stop() {
    super.stop();
    cliOptionsRegistered = false;
    configured = false;
  }
}
