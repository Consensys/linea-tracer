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

package net.consensys.linea.zktracer.module.limits.precompiles;

import static net.consensys.linea.zktracer.module.constants.GlobalConstants.WORD_SIZE;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.WORD_SIZE_MO;

import java.nio.MappedByteBuffer;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.shakiradata.ShakiraData;
import net.consensys.linea.zktracer.module.shakiradata.ShakiraDataOperation;
import net.consensys.linea.zktracer.module.shakiradata.ShakiraPrecompileType;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.internal.Words;

@RequiredArgsConstructor
public final class Sha256Blocks implements Module {

  private static final int PRECOMPILE_BASE_GAS_FEE = 60;
  private static final int PRECOMPILE_GAS_FEE_PER_EWORD = 12;
  private static final int SHA256_BLOCKSIZE = 64 * 8;
  // The length of the data to be hashed is 2**64 maximum.
  private static final int SHA256_PADDING_LENGTH = 64;
  private static final int SHA256_NB_PADDED_ONE = 1;

  private final Hub hub;
  private final Deque<Integer> counts = new ArrayDeque<>();

  @Getter private final ShakiraData shakiraData;

  @Override
  public String moduleKey() {
    return "PRECOMPILE_SHA2_BLOCKS";
  }

  @Override
  public void traceStartConflation(final long blockCount) {
    counts.push(0);
  }

  @Override
  public void enterTransaction() {
    counts.push(counts.getFirst());
  }

  @Override
  public void popTransaction() {
    counts.pop();
  }

  public static boolean hasEnoughGas(final Hub hub) {
    return hub.transients().op().gasAllowanceForCall() >= gasCost(hub);
  }

  public static long gasCost(final Hub hub) {
    final OpCode opCode = hub.opCode();
    final MessageFrame frame = hub.messageFrame();

    if (opCode.isCall()) {
      final Address target = Words.toAddress(frame.getStackItem(1));
      if (target.equals(Address.SHA256)) {
        final long dataByteLength = hub.transients().op().callDataSegment().length();
        final long wordCount = (dataByteLength + WORD_SIZE_MO) / WORD_SIZE;
        return PRECOMPILE_BASE_GAS_FEE + PRECOMPILE_GAS_FEE_PER_EWORD * wordCount;
      }
    }

    return 0;
  }

  @Override
  public void tracePreOpcode(MessageFrame frame) {
    final OpCode opCode = hub.opCode();

    if (opCode.isCall()) {
      final Address target = Words.toAddress(frame.getStackItem(1));
      if (target.equals(Address.SHA256)) {
        final long dataByteLength = hub.transients().op().callDataSegment().length();
        if (dataByteLength == 0) {
          return;
        }
        final int blockCount =
            (int)
                    (dataByteLength * 8
                        + SHA256_NB_PADDED_ONE
                        + SHA256_PADDING_LENGTH
                        + (SHA256_BLOCKSIZE - 1))
                / SHA256_BLOCKSIZE;

        final Bytes inputData = hub.transients().op().callData();

        if (hasEnoughGas(this.hub)) {
          this.shakiraData.call(
              new ShakiraDataOperation(hub.stamp(), ShakiraPrecompileType.SHA256, inputData));

          this.counts.push(this.counts.pop() + blockCount);
        }
      }
    }
  }

  @Override
  public int lineCount() {
    return counts.getFirst();
  }

  @Override
  public List<ColumnHeader> columnsHeaders() {
    throw new IllegalStateException("should never be called");
  }

  @Override
  public void commit(List<MappedByteBuffer> buffers) {
    throw new IllegalStateException("should never be called");
  }
}
