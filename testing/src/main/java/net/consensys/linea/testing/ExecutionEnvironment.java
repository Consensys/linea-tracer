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

import static net.consensys.linea.zktracer.module.constants.GlobalConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.OptionalLong;

import net.consensys.linea.corset.CorsetValidator;
import net.consensys.linea.zktracer.ZkTracer;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.config.GenesisConfigFile;
import org.hyperledger.besu.config.GenesisConfigOptions;
import org.hyperledger.besu.consensus.clique.CliqueBlockHeaderFunctions;
import org.hyperledger.besu.consensus.clique.CliqueForksSchedulesFactory;
import org.hyperledger.besu.consensus.clique.CliqueProtocolSchedule;
import org.hyperledger.besu.crypto.KeyPair;
import org.hyperledger.besu.crypto.SignatureAlgorithm;
import org.hyperledger.besu.crypto.SignatureAlgorithmFactory;
import org.hyperledger.besu.cryptoservices.KeyPairSecurityModule;
import org.hyperledger.besu.cryptoservices.NodeKey;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.chain.BadBlockManager;
import org.hyperledger.besu.ethereum.core.BlockHeader;
import org.hyperledger.besu.ethereum.core.BlockHeaderBuilder;
import org.hyperledger.besu.ethereum.core.Difficulty;
import org.hyperledger.besu.ethereum.core.MiningParameters;
import org.hyperledger.besu.ethereum.core.PrivacyParameters;
import org.hyperledger.besu.ethereum.mainnet.MainnetProtocolSpecFactory;
import org.hyperledger.besu.ethereum.mainnet.ProtocolSchedule;
import org.hyperledger.besu.ethereum.mainnet.ProtocolSpec;
import org.hyperledger.besu.ethereum.mainnet.ProtocolSpecBuilder;
import org.hyperledger.besu.evm.internal.EvmConfiguration;
import org.hyperledger.besu.metrics.noop.NoOpMetricsSystem;
import org.slf4j.Logger;

public class ExecutionEnvironment {
  public static final String CORSET_VALIDATION_RESULT = "Corset validation result: ";
  static GenesisConfigFile GENESIS_CONFIG =
      GenesisConfigFile.fromSource(GenesisConfigFile.class.getResource("/linea.json"));

  public static void checkTracer(
      ZkTracer zkTracer, CorsetValidator corsetValidator, Optional<Logger> logger) {
    Path traceFilePath = null;
    try {
      traceFilePath = Files.createTempFile(null, ".lt");
      zkTracer.writeToFile(traceFilePath);
      final Path finalTraceFilePath = traceFilePath;
      logger.ifPresent(log -> log.debug("trace written to {}", finalTraceFilePath));
      CorsetValidator.Result corsetValidationResult = corsetValidator.validate(traceFilePath);
      assertThat(corsetValidationResult.isValid())
          .withFailMessage(CORSET_VALIDATION_RESULT + "%s", corsetValidationResult.corsetOutput())
          .isTrue();
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (IllegalArgumentException e) {
      if (e.getMessage().contains("has invalid value") || e.getMessage().contains("has invalid width")) {
       throw new ColumnWidthMismatchException(e.getMessage(), e);
      }
      throw e;
    }
      finally {
      if (traceFilePath != null) {
        if (System.getenv("PRESERVE_TRACE_FILES") == null) {
          boolean traceFileDeleted = traceFilePath.toFile().delete();
          final Path finalTraceFilePath = traceFilePath;
          logger.ifPresent(
              log -> log.debug("trace file {} deleted {}", finalTraceFilePath, traceFileDeleted));
        }
      }
    }
  }

  public static BlockHeaderBuilder getLineaBlockHeaderBuilder(
      Optional<BlockHeader> parentBlockHeader) {
    BlockHeaderBuilder blockHeaderBuilder =
        parentBlockHeader.isPresent()
            ? BlockHeaderBuilder.fromHeader(parentBlockHeader.get())
                .number(parentBlockHeader.get().getNumber() + 1)
                .timestamp(parentBlockHeader.get().getTimestamp() + 100)
                .parentHash(parentBlockHeader.get().getHash())
                .blockHeaderFunctions(new CliqueBlockHeaderFunctions())
            : BlockHeaderBuilder.createDefault();

    return blockHeaderBuilder
        .baseFee(Wei.of(LINEA_BASE_FEE))
        .gasLimit(LINEA_BLOCK_GAS_LIMIT)
        .difficulty(Difficulty.of(LINEA_DIFFICULTY));
  }

  public static ProtocolSpec getProtocolSpec(BigInteger chainId) {
    BadBlockManager badBlockManager = new BadBlockManager();
    final GenesisConfigOptions genesisConfigOptions = GENESIS_CONFIG.getConfigOptions();

    ProtocolSchedule schedule =
        CliqueProtocolSchedule.create(
            genesisConfigOptions,
            CliqueForksSchedulesFactory.create(genesisConfigOptions),
            createNodeKey(),
            false,
            EvmConfiguration.DEFAULT,
            MiningParameters.MINING_DISABLED,
            badBlockManager,
            false,
            new NoOpMetricsSystem());

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
        .badBlocksManager(badBlockManager)
        .build(schedule);
  }

  private static NodeKey createNodeKey() {
    final Bytes32 keyPairPrvKey =
        Bytes32.fromHexString("0xf7a58d5e755d51fa2f6206e91dd574597c73248aaf946ec1964b8c6268d6207b");
    final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithmFactory.getInstance();
    final KeyPair keyPair =
        signatureAlgorithm.createKeyPair(signatureAlgorithm.createPrivateKey(keyPairPrvKey));
    final KeyPairSecurityModule keyPairSecurityModule = new KeyPairSecurityModule(keyPair);

    return new NodeKey(keyPairSecurityModule);
  }
}

