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

import static net.consensys.linea.zktracer.bytes.conversions.bigIntegerToBytes;
import static org.hyperledger.besu.crypto.Hash.keccak256;
import static org.hyperledger.besu.evm.internal.Words.clampedToLong;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class RomLex implements Module {
  final net.consensys.linea.zktracer.module.romLex.Trace.TraceBuilder builder = Trace.builder();
  final int llarge = 16;
  private static final Bytes CREATE2_SHIFT = bigIntegerToBytes(BigInteger.valueOf(0xff));

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
    // Contract creation with InitCode
    if (tx.getInit().isPresent()) {
      // TODO: get the address from the evm ?
      final Address deployementAddress = Address.contractAddress(tx.getSender(), tx.getNonce() - 1);
      int depNumber = 1; // TODO: put the right value

      final RomChunk chunk =
          new RomChunk(deployementAddress, depNumber, true, true, false, true, tx.getInit().get());
      // TODO: deploymentStatus might change, check at the end of the tx
      chunkList.add(chunk);
    }

    // Call to an account with bytecode
    if (tx.getTo().isPresent()) {
      if (worldView.get(tx.getTo().get()).hasCode()) {
        // TODO: get the DepNumber
        int depNumber = 2;
        final RomChunk chunk =
            new RomChunk(
                tx.getTo().get(),
                depNumber,
                true,
                false,
                true,
                false,
                worldView.get(tx.getTo().get()).getCode());
        chunkList.add(chunk);
      }
    }
  }

  @Override
  public void trace(MessageFrame frame) {
    OpCode opcode = OpCode.of(frame.getCurrentOperation().getOpcode());

    switch (opcode) {

      case CREATE -> {
        // TODO: get the address from the evm ?
        final Address deployementAddress =
            Address.contractAddress(
                frame.getSenderAddress(),
                frame.getWorldUpdater().getSenderAccount(frame).getNonce());
        int depNumber = 1; // TODO: put the right value
        final long offset = clampedToLong(frame.getStackItem(1));
        final long length = clampedToLong(frame.getStackItem(2));
        final Bytes initCode = frame.readMutableMemory(offset, length);
        if (!initCode.isEmpty()) {

          // TODO: get the right status
          final RomChunk chunk =
              new RomChunk(deployementAddress, depNumber, true, true, false, true, initCode);
          chunkList.add(chunk);
        }
      }

      case CREATE2 -> {
        // TODO: take the depAddress from the evm ?
        final long offset = clampedToLong(frame.getStackItem(1));
        final long length = clampedToLong(frame.getStackItem(2));
        final Bytes initCode = frame.readMutableMemory(offset, length);

        if (!initCode.isEmpty()) {
          final Bytes32 salt = Bytes32.leftPad(frame.getStackItem(3));
          final Bytes32 hash = keccak256(initCode);
          final Address deployementAddress =
              Address.extract(
                  keccak256(
                      Bytes.concatenate(CREATE2_SHIFT, frame.getSenderAddress(), salt, hash)));

          // TODO: put the ridht DepNb
          int depNumber = 1;

          // TODO: put the right DepStatus

          final RomChunk chunk =
              new RomChunk(deployementAddress, depNumber, true, true, false, true, initCode);
          chunkList.add(chunk);
        }
      }

      case EXTCODECOPY -> {
        // TODO: check we get the right code
        final int destOffset = frame.getStackItem(2).toUnsignedBigInteger().intValueExact();
        final int length = frame.getStackItem(4).toUnsignedBigInteger().intValueExact();
        final Bytes code = frame.readMutableMemory(destOffset, length);

        if (!code.isEmpty()) {
          // TODO: check the addr is ok
          final Address addr = Address.wrap(frame.getStackItem(1));
          // TODO: take the right DepNb
          int depNumber = 1;

          final RomChunk chunk = new RomChunk(addr, depNumber, true, false, true, false, code);
          chunkList.add(chunk);
        }
      }

      case RETURN -> {
        // TODO: check we get the right code
        final int destOffset = frame.getStackItem(1).toUnsignedBigInteger().intValueExact();
        final int length = frame.getStackItem(2).toUnsignedBigInteger().intValueExact();
        final Bytes code = frame.readMutableMemory(destOffset, length);

        if (!code.isEmpty()) {
          // TODO: depNumber
          int depNumber = 1;

          final RomChunk chunk =
              new RomChunk(frame.getContractAddress(), depNumber, true, false, true, false, code);
          chunkList.add(chunk);
        }
      }

      case CALL -> {
        final Address addrCall = Address.wrap(frame.getStackItem(2));
        //TODO: finish

      }
    }
  }

  private void traceChunk(RomChunk chunk, int cfi, int cfiInfty) {
    this.builder
        .codeFragmentIndex(BigInteger.valueOf(cfi))
        .codeFragmentIndexInfty(BigInteger.valueOf(cfiInfty))
        .addrHi(chunk.address().slice(0, 4).toUnsignedBigInteger())
        .addrLo(chunk.address().slice(4, llarge).toUnsignedBigInteger())
        .commitToState(chunk.commitToTheState())
        .depNumber(BigInteger.valueOf(chunk.deploymentNumber()))
        .depStatus(chunk.deploymentStatus())
        .isInit(chunk.isInitCode())
        .readFromState(chunk.readFromTheState());
    this.builder.validateRow();
  }

  @Override
  public int lineCount() {
    return chunkList.size();
  }

  @Override
  public Object commit() {
    int cfi = 0;
    final int cfiInfty = chunkList.size();
    for (RomChunk chunk : chunkList) {
      cfi += 1;
      traceChunk(chunk, cfi, cfiInfty);
    }
    return new RomLexTrace(builder.build());
  }
}
