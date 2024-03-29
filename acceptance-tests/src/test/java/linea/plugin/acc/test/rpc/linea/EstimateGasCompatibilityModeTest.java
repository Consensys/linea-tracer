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
package linea.plugin.acc.test.rpc.linea;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.core.Transaction;

public class EstimateGasCompatibilityModeTest extends EstimateGasTest {

  @Override
  public List<String> getTestCliOptions() {
    return getTestCommandLineOptionsBuilder()
        .set("--plugin-linea-estimate-gas-compatibility-mode-enabled=", "true")
        .build();
  }

  @Override
  protected void assertIsProfitable(
      final Transaction tx,
      final Wei baseFee,
      final Wei estimatedPriorityFee,
      final Wei estimatedMaxGasPrice,
      final long estimatedGasLimit) {
    final var minGasPrice = minerNode.getMiningParameters().getMinTransactionGasPrice();

    // since we are in compatibility mode, we want to check that returned profitable priority fee is
    // the value returned by eth_gasPrice
    final var expectedMaxGasPrice = minGasPrice;
    assertThat(estimatedMaxGasPrice).isEqualTo(expectedMaxGasPrice);
  }

  @Override
  protected void assertMinGasPriceLowerBound(final Wei baseFee, final Wei estimatedMaxGasPrice) {
    // since we are in compatibility mode, we want to check that returned profitable priority fee is
    // the value returned by eth_gasPrice
    assertIsProfitable(null, baseFee, null, estimatedMaxGasPrice, 0);
  }
}
