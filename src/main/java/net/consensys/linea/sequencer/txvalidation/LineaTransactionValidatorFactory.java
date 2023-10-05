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
import java.util.stream.Stream;

import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.plugin.services.txvalidator.PluginTransactionValidator;
import org.hyperledger.besu.plugin.services.txvalidator.PluginTransactionValidatorFactory;

/** Represents a factory for creating transaction validators. */
public class LineaTransactionValidatorFactory implements PluginTransactionValidatorFactory {
  private final LineaTransactionValidatorCliOptions options;

  public LineaTransactionValidatorFactory(final LineaTransactionValidatorCliOptions options) {
    this.options = options;
  }

  @Override
  public PluginTransactionValidator create() {
    final ArrayList<Address> denied = new ArrayList<>();
    final LineaTransactionValidatorConfiguration config = options.toDomainObject();
    Path filePath = Paths.get(config.denyListPath());

    try (Stream<String> lines = Files.lines(filePath)) {
      lines.forEach(
          l -> {
            final Address address = Address.fromHexString(l.trim());
            denied.add(address);
          });
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return new LineaTransactionValidator(denied);
  }
}
