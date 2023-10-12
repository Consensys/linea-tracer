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
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

import net.consensys.linea.zktracer.container.stacked.set.StackedSet;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class RomLex implements Module {
  final Trace.TraceBuilder builder = Trace.builder();
  private static final int LLARGE = 16;
  private static final Bytes CREATE2_SHIFT = bigIntegerToBytes(BigInteger.valueOf(0xff));
  private final Hub hub;
  public static int codeIdentifierBeforeLexOrder = 0;

  private static final RomChunkComparator romChunkComparator = new RomChunkComparator();

  private final StackedSet<RomChunkWIdentifier> chunks = new StackedSet<>();

  public static final SortedMap<RomChunk, Integer> chunkMap = new TreeMap<>(romChunkComparator);

  static class RomChunkComparator implements Comparator<RomChunk> {
    // Initialize the ChunkList
    public int compare(RomChunk chunk1, RomChunk chunk2) {
      // First sort by Address
      int addressComparison = chunk1.address().compareTo(chunk2.address());
      if (addressComparison != 0) {
        return addressComparison;
      } else {
        // Second, sort by Deployment Number
        int deploymentNumberComparison = chunk1.deploymentNumber() - chunk2.deploymentNumber();
        if (deploymentNumberComparison != 0) {
          return deploymentNumberComparison;
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

  @Override
  public String jsonKey() {
    return "romLex";
  }

  public RomLex(Hub hub) {
    this.hub = hub;
  }

  @Override
  public void enterTransaction() {
    this.chunks.enter();
  }

  @Override
  public void popTransaction() {
    this.chunks.pop();
  }

  public static <Integer> RomChunk getKeyByValue(
      final SortedMap<RomChunk, Integer> map, Integer value) {
    for (Map.Entry<RomChunk, Integer> entry : map.entrySet()) {
      if (Objects.equals(value, entry.getValue())) {
        return entry.getKey();
      }
    }
    return null;
  }

  @Override
  public void traceStartTx(WorldView worldView, Transaction tx) {
    // Contract creation with InitCode
    if (tx.getInit().isPresent() && !tx.getInit().get().isEmpty()) {
      codeIdentifierBeforeLexOrder += 1;
      final Address deployementAddress = Address.contractAddress(tx.getSender(), tx.getNonce() - 1);
      int depNumber = hub.conflation().deploymentInfo().number(deployementAddress);
      boolean depStatus = hub.conflation().deploymentInfo().isDeploying(deployementAddress);

      final RomChunk chunk =
          new RomChunk(deployementAddress, depNumber, depStatus, true, false, tx.getInit().get());
      this.chunks.add(new RomChunkWIdentifier(chunk, codeIdentifierBeforeLexOrder));
    }

    // Call to an account with bytecode
    if (tx.getTo().isPresent()) {
      if (worldView.get(tx.getTo().get()).hasCode()) {
        codeIdentifierBeforeLexOrder += 1;
        int depNumber = hub.conflation().deploymentInfo().number(tx.getTo().get());
        boolean depStatus;
        depStatus = hub.conflation().deploymentInfo().isDeploying(tx.getTo().get());
        final RomChunk chunk =
            new RomChunk(
                tx.getTo().get(),
                depNumber,
                depStatus,
                false,
                true,
                worldView.get(tx.getTo().get()).getCode());
        this.chunks.add(new RomChunkWIdentifier(chunk, codeIdentifierBeforeLexOrder));
      }
    }
  }

  @Override
  public void tracePreOpcode(MessageFrame frame) {
    OpCode opcode = OpCode.of(frame.getCurrentOperation().getOpcode());

    switch (opcode) {
      case CREATE -> {
        final Address deployementAddress =
            Address.contractAddress(
                frame.getSenderAddress(),
                frame.getWorldUpdater().getSenderAccount(frame).getNonce());

        final long offset = clampedToLong(frame.getStackItem(1));
        final long length = clampedToLong(frame.getStackItem(2));
        final Bytes initCode = frame.readMemory(offset, length);
        if (!initCode.isEmpty()) {
          codeIdentifierBeforeLexOrder += 1;
          final int depNumber = hub.conflation().deploymentInfo().number(deployementAddress);
          final boolean depStatus =
              hub.conflation().deploymentInfo().isDeploying(deployementAddress);
          final RomChunk chunk =
              new RomChunk(deployementAddress, depNumber, depStatus, true, false, initCode);
          this.chunks.add(new RomChunkWIdentifier(chunk, codeIdentifierBeforeLexOrder));
        }
      }

      case CREATE2 -> {
        final long offset = clampedToLong(frame.getStackItem(1));
        final long length = clampedToLong(frame.getStackItem(2));
        final Bytes initCode = frame.readMemory(offset, length);

        if (!initCode.isEmpty()) {
          codeIdentifierBeforeLexOrder += 1;
          // TODO: take the depAddress from the evm ?
          final Bytes32 salt = Bytes32.leftPad(frame.getStackItem(3));
          final Bytes32 hash = keccak256(initCode);
          final Address deployementAddress =
              Address.extract(
                  keccak256(
                      Bytes.concatenate(CREATE2_SHIFT, frame.getSenderAddress(), salt, hash)));
          final int depNumber = hub.conflation().deploymentInfo().number(deployementAddress);
          final boolean depStatus =
              hub.conflation().deploymentInfo().isDeploying(deployementAddress);

          final RomChunk chunk =
              new RomChunk(deployementAddress, depNumber, depStatus, true, false, initCode);
          this.chunks.add(new RomChunkWIdentifier(chunk, codeIdentifierBeforeLexOrder));
        }
      }

      case EXTCODECOPY -> {
        final int destOffset = frame.getStackItem(1).toUnsignedBigInteger().intValueExact();
        final int length = frame.getStackItem(3).toUnsignedBigInteger().intValueExact();
        final Bytes code = frame.readMemory(destOffset, length);

        if (!code.isEmpty()) {
          codeIdentifierBeforeLexOrder += 1;
          final Address sourceAddress = Address.wrap(frame.getStackItem(0));
          int depNumber = hub.conflation().deploymentInfo().number(sourceAddress);
          final boolean deploymentStatus =
              hub.conflation().deploymentInfo().isDeploying(sourceAddress);

          final RomChunk chunk =
              new RomChunk(sourceAddress, depNumber, deploymentStatus, false, true, code);
          this.chunks.add(new RomChunkWIdentifier(chunk, codeIdentifierBeforeLexOrder));
        }
      }

      case RETURN -> {
        // TODO: check we get the right code
        final int destOffset = frame.getStackItem(1).toUnsignedBigInteger().intValueExact();
        final int length = frame.getStackItem(2).toUnsignedBigInteger().intValueExact();
        final Bytes code = frame.readMemory(destOffset, length);
        final boolean depStatus =
            hub.conflation().deploymentInfo().isDeploying(frame.getContractAddress());
        if (!code.isEmpty() && depStatus) {
          codeIdentifierBeforeLexOrder += 1;
          int depNumber = hub.conflation().deploymentInfo().number(frame.getContractAddress());

          final RomChunk chunk =
              new RomChunk(frame.getContractAddress(), depNumber, depStatus, true, false, code);
          this.chunks.add(new RomChunkWIdentifier(chunk, codeIdentifierBeforeLexOrder));
        }
      }

      case CALL, CALLCODE -> {
        final Address calledAddress = Address.wrap(frame.getStackItem(1));
        final boolean depStatus =
            hub.conflation().deploymentInfo().isDeploying(frame.getContractAddress());
        final int depNumber = hub.conflation().deploymentInfo().number(frame.getContractAddress());
        final int argsOffset = frame.getStackItem(3).toUnsignedBigInteger().shortValueExact();
        final int argsLength = frame.getStackItem(4).toUnsignedBigInteger().shortValueExact();
        final Bytes byteCode =
            frame.getWorldUpdater().get(calledAddress).getCode().slice(argsOffset, argsLength);
        if (!byteCode.isEmpty()) {
          codeIdentifierBeforeLexOrder += 1;
          final RomChunk chunk =
              new RomChunk(calledAddress, depNumber, depStatus, true, false, byteCode);
          this.chunks.add(new RomChunkWIdentifier(chunk, codeIdentifierBeforeLexOrder));
        }
      }

      case DELEGATECALL, STATICCALL -> {
        final Address addrCall = Address.wrap(frame.getStackItem(1));
        final boolean depStatus =
            hub.conflation().deploymentInfo().isDeploying(frame.getContractAddress());
        final int depNumber = hub.conflation().deploymentInfo().number(frame.getContractAddress());
        final int argsOffset = frame.getStackItem(2).toUnsignedBigInteger().shortValueExact();
        final int argsLength = frame.getStackItem(3).toUnsignedBigInteger().shortValueExact();
        final Bytes byteCode =
            frame.getWorldUpdater().get(addrCall).getCode().slice(argsOffset, argsLength);
        if (!byteCode.isEmpty()) {
          codeIdentifierBeforeLexOrder += 1;
          final RomChunk chunk =
              new RomChunk(addrCall, depNumber, depStatus, true, false, byteCode);
          this.chunks.add(new RomChunkWIdentifier(chunk, codeIdentifierBeforeLexOrder));
        }
      }
    }
  }

  private void traceChunk(final RomChunk chunk, int cfi, int codeFragmentIndexInfinity) {
    this.builder
        .codeFragmentIndex(BigInteger.valueOf(cfi))
        .codeFragmentIndexInfty(BigInteger.valueOf(codeFragmentIndexInfinity))
        .addrHi(chunk.address().slice(0, 4).toUnsignedBigInteger())
        .addrLo(chunk.address().slice(4, LLARGE).toUnsignedBigInteger())
        .commitToState(chunk.commitToTheState())
        .depNumber(BigInteger.valueOf(chunk.deploymentNumber()))
        .depStatus(chunk.deploymentStatus())
        .readFromState(chunk.readFromTheState())
        .validateRow();
  }

  @Override
  public void traceEndConflation() {
    for (RomChunkWIdentifier chunkWIdentifier : this.chunks) {
      chunkMap.put(chunkWIdentifier.chunk(), chunkWIdentifier.chunkIdentifierBeforeLex());
    }
  }

  @Override
  public int lineCount() {
    return chunkMap.size();
  }

  @Override
  public Object commit() {
    int cfi = 0;
    final int codeFragmentIndexInfinity = chunkMap.size();
    for (RomChunk chunk : chunkMap.keySet()) {
      cfi += 1;
      traceChunk(chunk, cfi, codeFragmentIndexInfinity);
    }
    return new RomLexTrace(builder.build());
  }
}
