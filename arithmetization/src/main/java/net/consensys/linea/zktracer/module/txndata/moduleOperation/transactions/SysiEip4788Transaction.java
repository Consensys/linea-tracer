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
package net.consensys.linea.zktracer.module.txndata.moduleOperation.transactions;

import lombok.Getter;
import net.consensys.linea.zktracer.module.txndata.module.TxnDataRedesign;
import net.consensys.linea.zktracer.module.txndata.moduleOperation.TxnDataRedesignOperation;
import org.apache.tuweni.bytes.Bytes32;

public class SysiEip4788Transaction extends TxnDataRedesignOperation {
  @Getter private final long timestamp;
  @Getter private final Bytes32 beaconRoot;

  public SysiEip4788Transaction(
      final TxnDataRedesign txnData,
      final org.hyperledger.besu.plugin.data.ProcessableBlockHeader processableBlockHeader) {
    super(
        txnData.getUpdatedSysiTransactionNumber(),
        txnData.getUserTransactionNumber(),
        txnData.getSysfTransactionNumber());

    if (processableBlockHeader.getParentBeaconBlockRoot().isEmpty()) {
      throw new RuntimeException("Parent beacon block root not present in the block header");
    }

    this.timestamp = processableBlockHeader.getTimestamp();
    this.beaconRoot = processableBlockHeader.getParentBeaconBlockRoot().get();
  }

  @Override
  protected int computeLineCount() {
    return -1;
  }
}
