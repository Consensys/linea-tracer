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

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.bl.TransactionProfitabilityCalculator;
import net.consensys.linea.config.LineaProfitabilityConfiguration;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.plugin.services.BesuConfiguration;
import org.hyperledger.besu.plugin.services.BlockchainService;
import org.hyperledger.besu.plugin.services.txvalidator.PluginTransactionValidator;

@Slf4j
public class ProfitabilityValidator implements PluginTransactionValidator {
  final BesuConfiguration besuConfiguration;
  final BlockchainService blockchainService;
  final LineaProfitabilityConfiguration profitabilityConf;
  final TransactionProfitabilityCalculator profitabilityCalculator;

  public ProfitabilityValidator(
      final BesuConfiguration besuConfiguration,
      final BlockchainService blockchainService,
      final LineaProfitabilityConfiguration profitabilityConf) {
    this.besuConfiguration = besuConfiguration;
    this.blockchainService = blockchainService;
    this.profitabilityConf = profitabilityConf;
    this.profitabilityCalculator = new TransactionProfitabilityCalculator(profitabilityConf);
  }

  @Override
  public Optional<String> validateTransaction(final Transaction transaction) {

    return profitabilityCalculator.isProfitable(
            "Txpool",
            transaction,
            profitabilityConf.txPoolMinMargin(),
            besuConfiguration.getMinGasPrice().getAsBigInteger().doubleValue(),
            blockchainService.getNextBlockBaseFee().get().getAsBigInteger().doubleValue(),
            transaction.getGasLimit())
        ? Optional.empty()
        : Optional.of("Gas price too low");
  }
}
