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

package net.consensys.linea.zktracer.module.rlp_txn;

import java.util.Optional;

import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Transaction;

public record RlpTxnChunk(
    Transaction tx,
    boolean requireEvmExecution,
    Optional<Address> addr,
    Optional<Integer> depNumber,
    Optional<Boolean> depStatus,
    Optional<Boolean> initCode,
    Optional<Boolean> readFromState,
    Optional<Boolean> commitToState,
    Optional<Bytes> byteCode) {
  public RlpTxnChunk(Transaction tx, boolean requireEvmExecution) {
    this(
        tx,
        requireEvmExecution,
        Optional.empty(),
        Optional.empty(),
        Optional.empty(),
        Optional.empty(),
        Optional.empty(),
        Optional.empty(),
        Optional.empty());
  }

  public RlpTxnChunk(
      Transaction tx,
      boolean requireEvmExecution,
      Address address,
      Integer depNumber,
      Boolean depStatus,
      Boolean initCode,
      Boolean readFromState,
      Boolean commitToState,
      Bytes byteCode) {
    this(
        tx,
        requireEvmExecution,
        Optional.of(address),
        Optional.of(depNumber),
        Optional.of(depStatus),
        Optional.of(initCode),
        Optional.of(readFromState),
        Optional.of(commitToState),
        Optional.of(byteCode));
  }
}
