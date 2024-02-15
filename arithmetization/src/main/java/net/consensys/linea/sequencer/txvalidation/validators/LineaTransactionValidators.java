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

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.config.LineaProfitabilityConfiguration;
import net.consensys.linea.config.LineaTransactionValidatorConfiguration;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.plugin.services.BesuConfiguration;
import org.hyperledger.besu.plugin.services.BlockchainService;
import org.hyperledger.besu.plugin.services.txvalidator.PluginTransactionValidator;

/**
 * Represents an implementation of a plugin transaction validator, which validates a transaction
 * before it can be added to the transaction pool.
 */
@Slf4j
public class LineaTransactionValidators implements PluginTransactionValidator {
  private final PluginTransactionValidator[] validators;

  public LineaTransactionValidators(
      final BesuConfiguration besuConfiguration,
      final BlockchainService blockchainService,
      final LineaTransactionValidatorConfiguration txValidatorConf,
      final LineaProfitabilityConfiguration profitabilityConfiguration,
      final Set<Address> denied) {
    this.validators =
        new PluginTransactionValidator[] {
          new AllowedAddressValidator(denied),
          new GasLimitValidator(txValidatorConf),
          new CalldataValidator(txValidatorConf),
          new ProfitabilityValidator(
              besuConfiguration, blockchainService, profitabilityConfiguration)
        };
  }

  @Override
  public Optional<String> validateTransaction(final Transaction transaction) {
    final var maybeInvalid =
        Arrays.stream(validators)
            .map(v -> v.validateTransaction(transaction))
            .filter(Optional::isPresent)
            .findFirst();
    return maybeInvalid.map(Optional::get);
  }
}
