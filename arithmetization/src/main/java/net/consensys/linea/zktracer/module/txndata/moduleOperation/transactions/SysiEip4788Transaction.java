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

import static net.consensys.linea.zktracer.module.txndata.rows.EucRow.callToEuc;
import static net.consensys.linea.zktracer.module.txndata.rows.WcpRow.smallCallToIszero;
import static net.consensys.linea.zktracer.module.txndata.rows.WcpRow.smallCallToLeq;

import lombok.Getter;
import net.consensys.linea.zktracer.module.txndata.module.TxnDataRedesign;
import net.consensys.linea.zktracer.module.txndata.moduleOperation.TxnDataRedesignOperation;
import net.consensys.linea.zktracer.module.txndata.rows.EucRow;
import net.consensys.linea.zktracer.module.txndata.rows.HubRowForSystemTransactions;
import net.consensys.linea.zktracer.module.txndata.rows.WcpRow;
import net.consensys.linea.zktracer.types.EWord;
import org.apache.tuweni.bytes.Bytes32;

public class SysiEip4788Transaction extends TxnDataRedesignOperation {
  @Getter private final long timestamp;
  @Getter private final Bytes32 beaconRoot;

  public SysiEip4788Transaction(
      final TxnDataRedesign txnData,
      final org.hyperledger.besu.plugin.data.ProcessableBlockHeader processableBlockHeader) {
    super(txnData);

    if (processableBlockHeader.getParentBeaconBlockRoot().isEmpty()) {
      throw new RuntimeException("Parent beacon block root not present in the block header");
    }

    this.timestamp = processableBlockHeader.getTimestamp();
    this.beaconRoot = processableBlockHeader.getParentBeaconBlockRoot().get();

    process();
  }

  private void process() {
    final HubRowForSystemTransactions hubRow = hubRow();
    computeTimestampModulo8191ComputationRow();
    detectingTheGenesisBlockComputationRow();
    compareTimestampToLineaCancunForkTimestampComputationRow();

    /**
     * <b>% 8191</b> and <b>== 0</b> operations are lightweight enough that we don't care to
     * remember the results from teh computation rows.
     */
    hubRow.systemTransactionData.add(EWord.of(timestamp));
    hubRow.systemTransactionData.add(EWord.of(timestamp % 8191));
    hubRow.systemTransactionData.add(EWord.of(EWord.of(beaconRoot).hi()));
    hubRow.systemTransactionData.add(EWord.of(EWord.of(beaconRoot).lo()));
    hubRow.systemTransactionData.add(EWord.of(number == 0 ? 1 : 0));
  }

  protected HubRowForSystemTransactions hubRow() {
    HubRowForSystemTransactions hubRow = new HubRowForSystemTransactions();
    rows.add(hubRow);
    return hubRow;
  }

  private void computeTimestampModulo8191ComputationRow() {
    // TODO: use the prime constant
    EucRow computeTimestampModulo8191Row = callToEuc(euc, timestamp, 8191);
    rows.add(computeTimestampModulo8191Row);
  }

  private void detectingTheGenesisBlockComputationRow() {
    WcpRow detectingTheGenesisBlockRow = smallCallToIszero(wcp, number);
    rows.add(detectingTheGenesisBlockRow);
  }

  private void compareTimestampToLineaCancunForkTimestampComputationRow() {
      WcpRow compareTimestampToLineaCancunForkTimestampRow = smallCallToLeq(wcp, timestamp, 0x1337L);
      rows.add(compareTimestampToLineaCancunForkTimestampRow);
  }

  @Override
  protected int ctMax() {
    return 3;
  }
}
