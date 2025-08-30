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
package net.consensys.linea.zktracer.module.txndata.module;

import static com.google.common.base.Preconditions.checkState;
import static net.consensys.linea.zktracer.Fork.isPostPrague;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.Fork;
import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.container.module.OperationListModule;
import net.consensys.linea.zktracer.container.stacked.ModuleOperationStackedList;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.txndata.BlockSnapshot;
import net.consensys.linea.zktracer.module.txndata.moduleOperation.TxnDataRedesignOperation;
import net.consensys.linea.zktracer.module.txndata.moduleOperation.transactions.SysfNoopTransaction;
import net.consensys.linea.zktracer.module.txndata.moduleOperation.transactions.SysiEip4788Transaction;
import net.consensys.linea.zktracer.module.txndata.moduleOperation.transactions.UserTransaction;
import net.consensys.linea.zktracer.types.TransactionProcessingMetadata;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.worldstate.WorldView;
import org.hyperledger.besu.plugin.data.BlockBody;
import org.hyperledger.besu.plugin.data.BlockHeader;
import org.hyperledger.besu.plugin.data.ProcessableBlockHeader;

@RequiredArgsConstructor
public class TxnDataRedesign implements OperationListModule<TxnDataRedesignOperation> {

  @Getter private final Hub hub;
  @Getter private final Fork fork;
  @Getter private final List<BlockSnapshot> blocks = new ArrayList<>();
  @Getter private long number;

  @Getter
  private final ModuleOperationStackedList<TxnDataRedesignOperation> operations =
      new ModuleOperationStackedList<>();

  @Override
  public void traceStartBlock(
      WorldView world,
      final ProcessableBlockHeader processableBlockHeader,
      final Address miningBeneficiary) {

    blocks.add(new BlockSnapshot(processableBlockHeader));
    number = processableBlockHeader.getNumber();
    operations().add(new SysiEip4788Transaction(this, processableBlockHeader));

    if (isPostPrague(fork)) {
      // operations().add(new SysiEip2935Transaction(this, processableBlockHeader));
    }
  }

  @Override
  public void traceEndTx(TransactionProcessingMetadata tx) {
    operations().add(new UserTransaction(this, tx));
  }

  public void traceEndBlock(final BlockHeader blockHeader, final BlockBody blockBody) {

    checkState(
        blockHeader.getNumber() == number,
        "Block header number %s does not match current block number %s",
        blockHeader.getNumber(),
        number);

    operations().add(new SysfNoopTransaction(this));
  }

  @Override
  public String moduleKey() {
    return "TXN_DATA";
  }

  @Override
  public int spillage(Trace trace) {
    return trace.txndata().spillage();
  }

  @Override
  public List<Trace.ColumnHeader> columnHeaders(Trace trace) {
    return trace.txndata().headers(this.lineCount());
  }

  @Override
  public ModuleOperationStackedList<TxnDataRedesignOperation> operations() {
    return null;
  }
}
