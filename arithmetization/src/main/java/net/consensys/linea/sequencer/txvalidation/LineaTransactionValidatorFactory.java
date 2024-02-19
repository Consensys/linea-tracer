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

package net.consensys.linea.sequencer.txvalidation;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import net.consensys.linea.config.LineaProfitabilityConfiguration;
import net.consensys.linea.config.LineaTransactionValidatorConfiguration;
import net.consensys.linea.sequencer.txvalidation.validators.AllowedAddressValidator;
import net.consensys.linea.sequencer.txvalidation.validators.CalldataValidator;
import net.consensys.linea.sequencer.txvalidation.validators.GasLimitValidator;
import net.consensys.linea.sequencer.txvalidation.validators.ProfitabilityValidator;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.plugin.services.BesuConfiguration;
import org.hyperledger.besu.plugin.services.BlockchainService;
import org.hyperledger.besu.plugin.services.txvalidator.PluginTransactionValidator;
import org.hyperledger.besu.plugin.services.txvalidator.PluginTransactionValidatorFactory;

/** Represents a factory for creating transaction validators. */
public class LineaTransactionValidatorFactory implements PluginTransactionValidatorFactory {

  private final BesuConfiguration besuConfiguration;
  private final BlockchainService blockchainService;
  private final LineaTransactionValidatorConfiguration txValidatorConf;
  private final LineaProfitabilityConfiguration profitabilityConf;
  private final Set<Address> denied;

  public LineaTransactionValidatorFactory(
      final BesuConfiguration besuConfiguration,
      final BlockchainService blockchainService,
      final LineaTransactionValidatorConfiguration txValidatorConf,
      final LineaProfitabilityConfiguration profitabilityConf,
      final Set<Address> denied) {
    this.besuConfiguration = besuConfiguration;
    this.blockchainService = blockchainService;
    this.txValidatorConf = txValidatorConf;
    this.profitabilityConf = profitabilityConf;
    this.denied = denied;
  }

  @Override
  public PluginTransactionValidator create() {
    final var validators =
        new PluginTransactionValidator[] {
          new AllowedAddressValidator(denied),
          new GasLimitValidator(txValidatorConf),
          new CalldataValidator(txValidatorConf),
          new ProfitabilityValidator(besuConfiguration, blockchainService, profitabilityConf)
        };

    return (transaction, isLocal, hasPriority) ->
        Arrays.stream(validators)
            .map(v -> v.validateTransaction(transaction, isLocal, hasPriority))
            .filter(Optional::isPresent)
            .findFirst()
            .map(Optional::get);
  }
}
