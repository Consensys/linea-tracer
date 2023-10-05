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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
  private final List<Address> denied;

  @Override
  public boolean validateTransaction(final Transaction transaction) {
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
}
