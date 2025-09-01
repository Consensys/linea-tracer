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

import static net.consensys.linea.zktracer.module.txndata.rows.computationRows.EucRow.callToEuc;
import static net.consensys.linea.zktracer.module.txndata.rows.computationRows.WcpRow.smallCallToIszero;
import static net.consensys.linea.zktracer.module.txndata.rows.computationRows.WcpRow.smallCallToLeq;

import net.consensys.linea.zktracer.module.txndata.module.TxnDataRedesign;
import net.consensys.linea.zktracer.module.txndata.moduleOperation.TxnDataRedesignOperation;
import net.consensys.linea.zktracer.module.txndata.rows.computationRows.EucRow;
import net.consensys.linea.zktracer.module.txndata.rows.computationRows.WcpRow;
import net.consensys.linea.zktracer.module.txndata.rows.hubRows.HubRowForSystemTransactions;
import net.consensys.linea.zktracer.module.txndata.rows.hubRows.Type;
import net.consensys.linea.zktracer.types.EWord;
import org.apache.tuweni.bytes.Bytes32;

public class SysiEip4788Transaction extends TxnDataRedesignOperation {

  private final long nonsenseCancunTimestamp =
      0x1337L; // Placeholder for the actual Prague fork timestamp
  private final org.hyperledger.besu.plugin.data.ProcessableBlockHeader blockHeader;

  public SysiEip4788Transaction(
      final TxnDataRedesign txnData,
      final org.hyperledger.besu.plugin.data.ProcessableBlockHeader processableBlockHeader) {
    super(txnData);

    if (processableBlockHeader.getParentBeaconBlockRoot().isEmpty()) {
      throw new RuntimeException("Parent beacon block root not present in the block header");
    }

    this.blockHeader = processableBlockHeader;

    process();
  }

  private void process() {
    hubRow();
    computeTimestampModulo8191ComputationRow();
    detectTheGenesisBlockComputationRow();
    compareTimestampToLineaCancunForkTimestampComputationRow();

    /**
     * <b>% 8191</b> and <b>== 0</b> operations are lightweight enough that we don't care to
     * remember the results from teh computation rows.
     */
  }

  protected void hubRow() {
    HubRowForSystemTransactions hubRow = new HubRowForSystemTransactions(Type.EIP4788);

    long timestamp = blockHeader.getTimestamp();
    Bytes32 parentBeaconBlockRoot = blockHeader.getParentBeaconBlockRoot().orElseThrow();

    hubRow.systemTransactionData1 = EWord.of(timestamp);
    hubRow.systemTransactionData2 = EWord.of(timestamp % 8191);
    hubRow.systemTransactionData3 = EWord.of(EWord.of(parentBeaconBlockRoot).hi());
    hubRow.systemTransactionData4 = EWord.of(EWord.of(parentBeaconBlockRoot).lo());
    hubRow.systemTransactionData5 = EWord.of(blockHeader.getNumber() == 0 ? 1 : 0);

    rows.add(hubRow);
  }

  private void computeTimestampModulo8191ComputationRow() {
    // TODO: use the prime constant
    EucRow row = callToEuc(euc, blockHeader.getTimestamp(), 8191);
    rows.add(row);
  }

  private void detectTheGenesisBlockComputationRow() {
    WcpRow row = smallCallToIszero(wcp, blockHeader.getNumber());
    rows.add(row);
  }

  private void compareTimestampToLineaCancunForkTimestampComputationRow() {
    WcpRow row = smallCallToLeq(wcp, blockHeader.getTimestamp(), nonsenseCancunTimestamp);
    rows.add(row);
  }

  @Override
  protected int ctMax() {
    return 3;
  }
}
