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

package net.consensys.linea.zktracer.module.limits;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.container.module.Module;
import net.consensys.linea.zktracer.container.stacked.CountOnlyOperation;
import net.consensys.linea.zktracer.types.TransactionProcessingMetadata;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.evm.log.Log;
import org.hyperledger.besu.evm.log.LogTopic;

@Accessors(fluent = true)
@Getter
@RequiredArgsConstructor
public class L2Block implements Module {
  private final L2L1Logs l2l1Logs;
  private final Address l2l1Address;
  private final LogTopic l2l1Topic;

  private static final int L1_MSG_INDICES_BYTES = 8;
  private static final int L1_TIMESTAMPS_BYTES = 8;
  private static final int ABI_OFFSET_BYTES = 32;
  private static final int ABI_LEN_BYTES = 32;

  /** The number of transaction */
  private final CountOnlyOperation numberOfTransactions = new CountOnlyOperation();

  /** The byte size of the RLP-encoded transaction of the conflation */
  private final CountOnlyOperation sizesRlpEncodedTxs = new CountOnlyOperation();

  /** The byte size of the L2->L1 logs messages of the conflation */
  private final CountOnlyOperation l2l1LogSizes = new CountOnlyOperation();

  @Override
  public String moduleKey() {
    return "BLOCK_L1_SIZE";
  }

  @Override
  public void commitTransactionBundle() {
    numberOfTransactions.commitTransactionBundle();
    sizesRlpEncodedTxs.commitTransactionBundle();
    l2l1LogSizes.commitTransactionBundle();
  }

  @Override
  public void popTransactionBundle() {
    numberOfTransactions.popTransactionBundle();
    sizesRlpEncodedTxs.popTransactionBundle();
    l2l1LogSizes.popTransactionBundle();
  }

  @Override
  public int lineCount() {

    return sizesRlpEncodedTxs.lineCount()
        + l2l1LogSizes.lineCount()

        // Calculates the data size related to the abi encoding of the list of the
        // from addresses. The field is a simple array of bytes20. We need to take
        // into account the offset and the length in the ABI encoding.
        + numberOfTransactions.lineCount() * Address.SIZE
        + ABI_OFFSET_BYTES
        + ABI_LEN_BYTES

        // Accumulates the data occupied for the hashes of the L2 to L1 messages
        // hashes each of them occupies 32 bytes. Also accounts for the overheads
        // of L2 and L1 messages encoding.
        + Hash.SIZE * l2l1LogSizes.lineCount()
        + ABI_OFFSET_BYTES
        + ABI_LEN_BYTES

        // Account for the overheads of sending the resulting root hash, the
        // timestamp and the L1 msg reception. For a sequence of conflated L2 blocks
        // , we will need to also need to send the initial timestamp and the parent
        // state root hash. Since we cannot forsee, at this point, the number of
        // blocks that will be conflated together with this block we make the worst
        // assumption that the block will be conflated alone. This corresponds to
        // counting twice the root hash and the timestamps. For the L1 messages, we
        // unfortunately do not have the data in the tracer yet. For that reason,
        // we also make a worst-case assumption that that every transaction is a
        // batch reception on layer 2. Finally, since what is sent on L1 is an array
        // of L2BlockData, we also make a worst-case assumption that the block will
        // be alone in the structure and account for the ABI encoding.
        + 2 * L1_TIMESTAMPS_BYTES
        + // the timestamp
        2 * Hash.SIZE
        + // the root hash
        L1_MSG_INDICES_BYTES * numberOfTransactions.lineCount()
        + ABI_LEN_BYTES
        + ABI_OFFSET_BYTES
        + // the L1 messages
        ABI_LEN_BYTES
        + ABI_OFFSET_BYTES; // abi overheads for the blockdata struct.
  }

  @Override
  public List<Trace.ColumnHeader> columnHeaders() {
    throw new IllegalStateException("non-tracing module");
  }

  @Override
  public void traceEndTx(TransactionProcessingMetadata tx) {
    for (Log log : tx.getLogs()) {
      if (isL2L1Log(log)) {
        l2l1LogSizes.add(log.getData().size());
        // The L2L1Logs module counts only the number of L2->L1 logs
        l2l1Logs.addLimit(1);
      }
    }

    // This calculates the data size related to the transaction field of the
    // data sent on L1. This field is a double array of byte. Each subarray
    // corresponds to an RLP encoded transaction. The abi encoding incurs an
    // overhead for each transaction (32 bytes for an offset, and 32 bytes for
    // to encode the length of each sub bytes array). This overhead is also
    // incurred by the top-level array, hence the +1.
    sizesRlpEncodedTxs.add(tx.getBesuTransaction().encoded().size());
  }

  private boolean isL2L1Log(Log log) {
    return log.getLogger().equals(l2l1Address)
        && !log.getTopics().isEmpty()
        && log.getTopics().getFirst().equals(l2l1Topic);
  }
}
