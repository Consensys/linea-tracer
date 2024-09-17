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

package net.consensys.linea.testing;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.OptionalLong;

import net.consensys.linea.corset.CorsetValidator;
import net.consensys.linea.zktracer.ZkTracer;
import org.hyperledger.besu.config.GenesisConfigFile;
import org.hyperledger.besu.ethereum.chain.BadBlockManager;
import org.hyperledger.besu.ethereum.core.MiningParameters;
import org.hyperledger.besu.ethereum.core.PrivacyParameters;
import org.hyperledger.besu.ethereum.mainnet.MainnetProtocolSchedule;
import org.hyperledger.besu.ethereum.mainnet.MainnetProtocolSpecFactory;
import org.hyperledger.besu.ethereum.mainnet.ProtocolSchedule;
import org.hyperledger.besu.ethereum.mainnet.ProtocolSpec;
import org.hyperledger.besu.ethereum.mainnet.ProtocolSpecBuilder;
import org.hyperledger.besu.evm.internal.EvmConfiguration;
import org.hyperledger.besu.metrics.noop.NoOpMetricsSystem;
import org.slf4j.Logger;

public class ExecutionEnvironment {
  static GenesisConfigFile GENESIS_CONFIG =
      GenesisConfigFile.fromSource(GenesisConfigFile.class.getResource("/linea.json"));

  public static void checkTracer(
      ZkTracer zkTracer, CorsetValidator corsetValidator, Optional<Logger> logger) {
    Path traceFilePath = null;
    try {
      traceFilePath = Files.createTempFile(null, ".lt");
      zkTracer.writeToFile(traceFilePath);
      final Path finalTraceFilePath = traceFilePath;
      logger.ifPresent(log -> log.info("trace written to {}", finalTraceFilePath));
      CorsetValidator.Result corsetValidationResult = corsetValidator.validate(traceFilePath);
      logger.ifPresent(log -> log.info("Corset validation result {}", corsetValidationResult));
      assertThat(corsetValidationResult.isValid()).isTrue();
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      if (traceFilePath != null) {
        if (System.getenv("PRESERVE_TRACE_FILES") == null) {
          boolean traceFileDeleted = traceFilePath.toFile().delete();
          final Path finalTraceFilePath = traceFilePath;
          logger.ifPresent(
              log -> log.info("trace file {} deleted {}", finalTraceFilePath, traceFileDeleted));
        }
      }
    }
  }

  public static ProtocolSchedule getProtocolSchedule() {
    return MainnetProtocolSchedule.fromConfig(
        GENESIS_CONFIG.getConfigOptions(),
        MiningParameters.MINING_DISABLED,
        new BadBlockManager(),
        false,
        new NoOpMetricsSystem());
  }

  public static ProtocolSpec getProtocolSpec(BigInteger chainId) {
    ProtocolSpecBuilder builder =
        new MainnetProtocolSpecFactory(
                Optional.of(chainId),
                true,
                OptionalLong.empty(),
                EvmConfiguration.DEFAULT,
                MiningParameters.MINING_DISABLED,
                false,
                new NoOpMetricsSystem())
            .londonDefinition(GENESIS_CONFIG.getConfigOptions());
    // .lineaOpCodesDefinition(GENESIS_CONFIG.getConfigOptions());

    return builder
        .privacyParameters(PrivacyParameters.DEFAULT)
        .badBlocksManager(new BadBlockManager())
        .build(getProtocolSchedule());
  }
}
