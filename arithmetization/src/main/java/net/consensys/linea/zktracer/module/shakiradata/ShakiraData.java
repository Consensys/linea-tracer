/*
 * Copyright ConsenSys Inc.
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

package net.consensys.linea.zktracer.module.shakiradata;

import static net.consensys.linea.zktracer.module.constants.GlobalConstants.LLARGE;

import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.container.stacked.set.StackedSet;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.limits.precompiles.RipemdBlocks;
import net.consensys.linea.zktracer.module.limits.precompiles.Sha256Blocks;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.types.TransactionProcessingMetadata;
import org.hyperledger.besu.evm.worldstate.WorldView;

@RequiredArgsConstructor
public class ShakiraData implements Module {
  private final Wcp wcp;
  private final StackedSet<ShakiraDataOperation> operations = new StackedSet<>();
  private final List<ShakiraDataOperation> sortedOperations = new ArrayList<>();
  private int numberOfOperationsAtStartTx = 0;
  private final ShakiraDataComparator comparator = new ShakiraDataComparator();

  private final Sha256Blocks sha256Blocks;
  private final RipemdBlocks ripemdBlocks;

  @Override
  public String moduleKey() {
    return "SHAKIRA_DATA";
  }

  @Override
  public void enterTransaction() {
    this.operations.enter();
  }

  @Override
  public void popTransaction() {
    this.sortedOperations.removeAll(this.operations.sets.getLast());
    this.operations.pop();
  }

  @Override
  public int lineCount() {
    return this.operations.lineCount()
        + 1; /*because the lookup HUB -> SHAKIRA requires at least two padding rows. TODO: shouldn't it be done by Corset via the spilling ? */
  }

  @Override
  public List<ColumnHeader> columnsHeaders() {
    return Trace.headers(this.lineCount());
  }

  public void call(final ShakiraDataOperation operation) {
    this.operations.add(operation);
    this.wcp.callGT(operation.lastNBytes(), 0);
    this.wcp.callLEQ(operation.lastNBytes(), LLARGE);

    switch (operation.precompileType()) {
      case SHA256 -> sha256Blocks.addPrecompileLimit(operation.inputSize());
      case KECCAK -> { // TODO
      }
      case RIPEMD -> ripemdBlocks.addPrecompileLimit(operation.inputSize());
      default -> throw new IllegalArgumentException("Precompile type not supported by SHAKIRA");
    }
  }

  @Override
  public void traceStartTx(WorldView worldView, TransactionProcessingMetadata tx) {
    this.numberOfOperationsAtStartTx = operations.size();
  }

  @Override
  public void traceEndTx(TransactionProcessingMetadata tx) {
    final List<ShakiraDataOperation> newOperations =
        new ArrayList<>(this.operations.sets.getLast());
    newOperations.sort(comparator);
    this.sortedOperations.addAll(newOperations);
    final int numberOfOperationsAtEndTx = sortedOperations.size();
    for (int i = numberOfOperationsAtStartTx; i < numberOfOperationsAtEndTx; i++) {
      final long previousID = i == 0 ? 0 : sortedOperations.get(i - 1).ID();
      this.wcp.callLT(previousID, sortedOperations.get(i).ID());
    }
  }

  @Override
  public void commit(List<MappedByteBuffer> buffers) {
    final Trace trace = new Trace(buffers);

    /* WARN: do not remove, the lookup HUB -> SHAKIRA requires at least two padding rows. TODO: should be done by Corset*/
    trace.fillAndValidateRow();

    int stamp = 0;
    for (ShakiraDataOperation operation : sortedOperations) {
      stamp++;
      operation.trace(trace, stamp);
    }
  }
}
