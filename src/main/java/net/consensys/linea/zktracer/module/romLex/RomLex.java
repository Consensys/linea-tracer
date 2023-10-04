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

package net.consensys.linea.zktracer.module.romLex;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import net.consensys.linea.zktracer.module.Module;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class RomLex implements Module {
  final net.consensys.linea.zktracer.module.romLex.Trace.TraceBuilder builder = Trace.builder();

  @Override
  public String jsonKey() {
    return "romLex";
  }

  static class RomChunkComparator implements Comparator<RomChunk> {

    // Initialize the ChunkList
    public int compare(RomChunk chunk1, RomChunk chunk2) {
      // First sort by Address
      int cmpAddr = chunk1.address().compareTo(chunk2.address());
      if (cmpAddr != 0) {
        return cmpAddr;
      } else {

        // Second, sort by Deployment Number
        int cmpDeploymentNumber = chunk1.deploymentNumber() - chunk2.deploymentNumber();
        if (cmpDeploymentNumber != 0) {
          return cmpDeploymentNumber;
        } else {

          // Third sort by Deployment Status (true greater)
          if (chunk1.deploymentStatus() == chunk2.deploymentStatus()) {
            return 0;
          } else {
            if (chunk1.deploymentStatus()) {
              return 1;
            } else {
              return -1;
            }
          }
        }
      }
    }
  }

  public static final SortedSet<RomChunk> chunkList = new TreeSet<>(new RomChunkComparator());

  @Override
  public void traceStartTx(WorldView worldView, Transaction tx) {
    if (tx.getInit().isPresent()) {
      // TODO: get the address from the evm ?
      final Address deployementAddress = Address.contractAddress(tx.getSender(), tx.getNonce() - 1);
      int depNumber = 1; // TODO: verify it's always 1.

      RomChunk chunk =
          new RomChunk(deployementAddress, depNumber, true, true, false, true, tx.getInit().get());
      // TODO: deploymentStatus might change, check at the end of the tx
      chunkList.add(chunk);
    }
  }

  private void traceChunk(RomChunk chunk, int cfi) {
    this.builder.codeFragmentIndex(BigInteger.valueOf(cfi));
    this.builder.validateRow();
  }

  @Override
  public int lineCount() {
    return chunkList.size();
  }

  @Override
  public Object commit() {
    int cfi = 0;
    for (RomChunk chunk : chunkList) {
      cfi += 1;
      traceChunk(chunk, cfi);
    }
    return new RomLexTrace(builder.build());
  }
}
