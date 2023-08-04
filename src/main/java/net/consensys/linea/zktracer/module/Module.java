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

package net.consensys.linea.zktracer.module;

import java.util.List;

import net.consensys.linea.zktracer.OpCode;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.plugin.data.BlockBody;
import org.hyperledger.besu.plugin.data.BlockHeader;

public interface Module {
  String jsonKey();

  List<OpCode> supportedOpCodes();

  default void traceStartConflation(final long blockCount) {}

  default void traceEndConflation() {}

  default void traceStartBlock(final BlockHeader blockHeader, final BlockBody blockBody) {}

  default void traceEndBlock(final BlockHeader blockHeader, final BlockBody blockBody) {}

  default void traceStartTx(Transaction tx) {}

  default void traceEndTx() {}

  default void traceContextStart(Transaction tx) {}

  default void traceContextEnd(Transaction tx) {}

  default void trace(MessageFrame frame) {}

  Object commit();
}
