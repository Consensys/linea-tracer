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

package net.consensys.linea.zktracer.module.hub.transients;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import net.consensys.linea.zktracer.types.TransactionProcessingMetadata;
import org.apache.commons.lang3.tuple.Pair;
import org.hyperledger.besu.datatypes.Address;

import java.util.HashMap;
import java.util.Map;

/**
 * This class stores data and provide information accessible through the {@link Hub} of various
 * lifetimes.
 */
@Getter
@Accessors(fluent = true)
public class Transients {
  private final Hub hub;

  /** stores conflation-lived information */
  final Conflation conflation = new Conflation();

  /** stores block-lived information */
  final Block block = new Block();

  /** provides operation-related information */
  final OperationAncillaries op;



  @Getter
  Map<AddrBlockPair, TransactionProcessingMetadata.TransactAccountFirstAndLast> txnAccountFirstLastBlockMap = new HashMap<>();

  public static class AddrBlockPair {
    @Getter
    private Address address;
    @Getter
    private int blockNumber;

    public AddrBlockPair(Address addr, int blockNumber) {
      this.address = addr;
      this.blockNumber = blockNumber;
    }
  }


  public TransactionProcessingMetadata tx() {
    return this.hub.txStack().current();
  }

  public Transients(final Hub hub) {
    this.hub = hub;
    this.op = new OperationAncillaries(hub);
  }
}
