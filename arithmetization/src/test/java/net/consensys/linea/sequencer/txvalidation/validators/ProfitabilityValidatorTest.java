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

package net.consensys.linea.sequencer.txvalidation.validators;

import java.math.BigInteger;
import java.nio.file.Path;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.config.LineaProfitabilityCliOptions;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.hyperledger.besu.crypto.SECPSignature;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.plugin.data.BlockContext;
import org.hyperledger.besu.plugin.data.BlockHeader;
import org.hyperledger.besu.plugin.services.BesuConfiguration;
import org.hyperledger.besu.plugin.services.BlockchainService;
import org.junit.jupiter.api.BeforeEach;

@Slf4j
@RequiredArgsConstructor
public class ProfitabilityValidatorTest {
  private static final SECPSignature FAKE_SIGNATURE;

  static {
    final X9ECParameters params = SECNamedCurves.getByName("secp256k1");
    final ECDomainParameters curve =
        new ECDomainParameters(params.getCurve(), params.getG(), params.getN(), params.getH());
    FAKE_SIGNATURE =
        SECPSignature.create(
            new BigInteger(
                "66397251408932042429874251838229702988618145381408295790259650671563847073199"),
            new BigInteger(
                "24729624138373455972486746091821238755870276413282629437244319694880507882088"),
            (byte) 0,
            curve.getN());
  }

  public static final double TX_POOL_MIN_MARGIN = 1.0;
  private ProfitabilityValidator profitabilityValidator;

  @BeforeEach
  public void initialize() {
    profitabilityValidator =
        new ProfitabilityValidator(
            new TestBesuConfiguration(),
            new TestBlockchainService(),
            LineaProfitabilityCliOptions.create().toDomainObject().toBuilder()
                .txPoolMinMargin(TX_POOL_MIN_MARGIN)
                .build());
  }

  private static class TestBesuConfiguration implements BesuConfiguration {
    @Override
    public Path getStoragePath() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Path getDataPath() {
      throw new UnsupportedOperationException();
    }

    @Override
    public int getDatabaseVersion() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Wei getMinGasPrice() {
      return Wei.of(1_000_000_000);
    }
  }

  private static class TestBlockchainService implements BlockchainService {

    @Override
    public Optional<BlockContext> getBlockByNumber(final long l) {
      throw new UnsupportedOperationException();
    }

    @Override
    public Hash getChainHeadHash() {
      throw new UnsupportedOperationException();
    }

    @Override
    public BlockHeader getChainHeadHeader() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Wei> getNextBlockBaseFee() {
      return Optional.of(Wei.of(7));
    }
  }
}
