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

package net.consensys.linea.sequencer.txselection;

import com.google.common.base.MoreObjects;
import picocli.CommandLine;

/** The Linea CLI options. */
public class LineaTransactionSelectorCliOptions {
  public static final int DEFAULT_MAX_TX_CALLDATA_SIZE = 60000;
  public static final int DEFAULT_MAX_BLOCK_CALLDATA_SIZE = 70000;

  private static final String MAX_TX_CALLDATA_SIZE = "--plugin-linea-max-tx-calldata-size";
  private static final String MAX_BLOCK_CALLDATA_SIZE = "--plugin-linea-max-block-calldata-size";

  @CommandLine.Option(
      names = {MAX_TX_CALLDATA_SIZE},
      hidden = true,
      paramLabel = "<INTEGER>",
      description =
          "Maximum size for the calldata of a Transaction (default: "
              + DEFAULT_MAX_TX_CALLDATA_SIZE
              + ")")
  private int maxTxCalldataSize = DEFAULT_MAX_TX_CALLDATA_SIZE;

  @CommandLine.Option(
      names = {MAX_BLOCK_CALLDATA_SIZE},
      hidden = true,
      paramLabel = "<INTEGER>",
      description =
          "Maximum size for the calldata of a Block (default: "
              + DEFAULT_MAX_BLOCK_CALLDATA_SIZE
              + ")")
  private int maxBlockCalldataSize = DEFAULT_MAX_BLOCK_CALLDATA_SIZE;

  private LineaTransactionSelectorCliOptions() {}

  /**
   * Create Linea cli options.
   *
   * @return the Linea cli options
   */
  public static LineaTransactionSelectorCliOptions create() {
    return new LineaTransactionSelectorCliOptions();
  }

  /**
   * Cli options from config.
   *
   * @param config the config
   * @return the cli options
   */
  public static LineaTransactionSelectorCliOptions fromConfig(
      final LineaTransactionSelectorConfiguration config) {
    final LineaTransactionSelectorCliOptions options = create();
    options.maxTxCalldataSize = config.maxTxCalldataSize();
    options.maxBlockCalldataSize = config.maxBlockCalldataSize();

    return options;
  }

  /**
   * To domain object Linea factory configuration.
   *
   * @return the Linea factory configuration
   */
  public LineaTransactionSelectorConfiguration toDomainObject() {
    return new LineaTransactionSelectorConfiguration(maxTxCalldataSize, maxBlockCalldataSize);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add(MAX_TX_CALLDATA_SIZE, maxTxCalldataSize)
        .add(MAX_BLOCK_CALLDATA_SIZE, maxBlockCalldataSize)
        .toString();
  }
}
