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

package net.consensys.linea.sequencer.txvalidation;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.plugin.services.txvalidator.PluginTransactionValidator;

/**
 * Represents an implementation of a plugin transaction validator, which validates a transaction
 * before it can be added to the transaction pool.
 */
@Slf4j
@RequiredArgsConstructor
public class LineaTransactionValidator implements PluginTransactionValidator {
  private final LineaTransactionValidatorCliOptions options;
  private final AtomicBoolean needsInitialization = new AtomicBoolean(true);
  private final List<Address> denied = new ArrayList<>();

  @Override
  public boolean validateTransaction(final Transaction transaction) {
    // Currently the cli options are not set at the time when we create the factory.
    // That is why we do the initialization here at first use
    if (needsInitialization.compareAndSet(true, false)) {
      initialize();
    }
    if (denied.contains(transaction.getSender())) {
      log.debug("Sender {} is on the deny list", transaction.getSender());
      return false;
    }
    if (transaction.getTo().isPresent() && denied.contains(transaction.getTo().get())) {
      log.debug("To address {} is on the deny list", transaction.getTo().get());
      return false;
    }
    return true;
  }

  private void initialize() {
    final LineaConfiguration lineaConfiguration = options.toDomainObject();
    Path filePath = Paths.get(lineaConfiguration.denyListPath());

    try (Stream<String> lines = Files.lines(filePath)) {
      lines.forEach(
          l -> {
            final Address address = Address.fromHexString(l.trim());
            denied.add(address);
          });
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
