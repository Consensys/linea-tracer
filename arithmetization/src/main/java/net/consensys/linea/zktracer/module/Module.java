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

package net.consensys.linea.zktracer.module;

import java.nio.MappedByteBuffer;
import java.util.List;

import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.types.TransactionProcessingMetadata;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.worldstate.WorldView;
import org.hyperledger.besu.plugin.data.BlockBody;
import org.hyperledger.besu.plugin.data.BlockHeader;
import org.hyperledger.besu.plugin.data.ProcessableBlockHeader;

public interface Module {
  String moduleKey();

  default void traceStartConflation(final long blockCount) {}

  default void traceEndConflation(final WorldView state) {}

  default void traceStartBlock(final ProcessableBlockHeader processableBlockHeader) {}

  default void traceEndBlock(final BlockHeader blockHeader, final BlockBody blockBody) {}

  default void traceStartTx(
      WorldView worldView, TransactionProcessingMetadata transactionProcessingMetadata) {}

  default void traceEndTx(TransactionProcessingMetadata tx) {}

  default void traceContextEnter(MessageFrame frame) {}

  default void traceContextExit(MessageFrame frame) {}

  default void tracePreOpcode(MessageFrame frame) {}

  default void tracePostOpcode(MessageFrame frame) {}

  /**
   * Called at the eve of a new transaction; intended to create a new modification context for the
   * stacked state of the module.
   */
  void enterTransaction();

  /** Called when a transaction execution is cancelled; should revert the state of the module. */
  void popTransaction();

  int lineCount();

  List<ColumnHeader> columnsHeaders();

  default void commit(List<MappedByteBuffer> buffers) {
    throw new UnsupportedOperationException();
  }
}
