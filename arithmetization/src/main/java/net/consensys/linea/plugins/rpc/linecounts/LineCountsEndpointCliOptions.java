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

import com.google.common.base.MoreObjects;
import net.consensys.linea.plugins.LineaCliOptions;
import picocli.CommandLine;

class LineCountsEndpointCliOptions implements LineaCliOptions {

  static final String CONFIG_KEY = "line-counts-endpoint-config";

  static final String MODULES_TO_COUNT_CONFIG_FILE_PATH =
      "--plugin-linea-line-counts-modules-to-count-config-file-path";

  @CommandLine.Option(
      names = {MODULES_TO_COUNT_CONFIG_FILE_PATH},
      hidden = true,
      paramLabel = "<PATH>",
      description = "TOML config file path with a list of modules to count")
  private String modulesToCountConfigFilePath = null;

  private LineCountsEndpointCliOptions() {}

  /**
   * Create Linea cli options.
   *
   * @return the Linea cli options
   */
  static LineCountsEndpointCliOptions create() {
    return new LineCountsEndpointCliOptions();
  }

  /**
   * Linea cli options from config.
   *
   * @param config the config
   * @return the Linea cli options
   */
  static LineCountsEndpointCliOptions fromConfig(final LineCountsEndpointConfiguration config) {
    final LineCountsEndpointCliOptions options = create();
    options.modulesToCountConfigFilePath = config.modulesToCountConfigFilePath();
    return options;
  }

  /**
   * To domain object Linea factory configuration.
   *
   * @return the Linea factory configuration
   */
  @Override
  public LineCountsEndpointConfiguration toDomainObject() {
    return LineCountsEndpointConfiguration.builder()
        .modulesToCountConfigFilePath(modulesToCountConfigFilePath)
        .build();
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add(MODULES_TO_COUNT_CONFIG_FILE_PATH, modulesToCountConfigFilePath)
        .toString();
  }
}
