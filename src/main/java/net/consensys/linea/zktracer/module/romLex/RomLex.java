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
  final net.consensys.linea.zktracer.module.romLex.Trace.TraceBuilder builder = Trace.builder();
  final int llarge = 16;
  private static final Bytes CREATE2_SHIFT = bigIntegerToBytes(BigInteger.valueOf(0xff));
  private final Hub hub;

  @Override
  public String jsonKey() {
    return "romLex";
  }

  public RomLex(Hub hub) {
    this.hub = hub;
  }

  public static int codeIdentifierBeforeLexOrder = 0;

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

  private static final RomChunkComparator romChunkComparator = new RomChunkComparator();

  public static final SortedMap<RomChunk, Integer> chunkMap = new TreeMap<>(romChunkComparator);

  public static <Integer> RomChunk getKeyByValue(SortedMap<RomChunk, Integer> map, Integer value) {
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
      chunkMap.put(chunk, codeIdentifierBeforeLexOrder);
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
        chunkMap.put(chunk, codeIdentifierBeforeLexOrder);
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

        // TODO: get the byteCode from the hub
        final long offset = clampedToLong(frame.getStackItem(1));
        final long length = clampedToLong(frame.getStackItem(2));
        final Bytes initCode = frame.readMemory(offset, length);
        if (!initCode.isEmpty()) {
          codeIdentifierBeforeLexOrder += 1;
          int depNumber = hub.conflation().deploymentInfo().number(deployementAddress);
          // TODO: deploymentStatus == isDeploying ??
          boolean depStatus;
          depStatus = hub.conflation().deploymentInfo().isDeploying(deployementAddress);
          final RomChunk chunk =
              new RomChunk(deployementAddress, depNumber, depStatus, true, false, initCode);
          chunkMap.put(chunk, codeIdentifierBeforeLexOrder);
        }
      }

      case CREATE2 -> {
        // TODO: get the initCode from the HUB
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

          int depNumber = hub.conflation().deploymentInfo().number(deployementAddress);

          boolean depStatus;
          depStatus = hub.conflation().deploymentInfo().isDeploying(deployementAddress);

          final RomChunk chunk =
              new RomChunk(deployementAddress, depNumber, depStatus, true, false, initCode);
          chunkMap.put(chunk, codeIdentifierBeforeLexOrder);
        }
      }

      case EXTCODECOPY -> {
        // TODO: get the initCode from the HUB
        final int destOffset = frame.getStackItem(2).toUnsignedBigInteger().intValueExact();
        final int length = frame.getStackItem(4).toUnsignedBigInteger().intValueExact();
        final Bytes code = frame.readMemory(destOffset, length);

        if (!code.isEmpty()) {
          codeIdentifierBeforeLexOrder += 1;
          // TODO: check the addr is ok
          final Address addr = Address.wrap(frame.getStackItem(1));
          int depNumber = hub.conflation().deploymentInfo().number(addr);
          // TODO: deploymentStatus == isDeploying ??
          boolean depStatus;
          depStatus = hub.conflation().deploymentInfo().isDeploying(addr);

          final RomChunk chunk = new RomChunk(addr, depNumber, depStatus, false, true, code);
          chunkMap.put(chunk, codeIdentifierBeforeLexOrder);
        }
      }

      case RETURN -> {
        // TODO: check we get the right code
        final int destOffset = frame.getStackItem(1).toUnsignedBigInteger().intValueExact();
        final int length = frame.getStackItem(2).toUnsignedBigInteger().intValueExact();
        final Bytes code = frame.readMemory(destOffset, length);
        boolean depStatus;
        depStatus = hub.conflation().deploymentInfo().isDeploying(frame.getContractAddress());
        if (!code.isEmpty() && depStatus) {
          codeIdentifierBeforeLexOrder += 1;
          int depNumber = hub.conflation().deploymentInfo().number(frame.getContractAddress());

          final RomChunk chunk =
              new RomChunk(frame.getContractAddress(), depNumber, depStatus, true, false, code);
          chunkMap.put(chunk, codeIdentifierBeforeLexOrder);
        }
      }

      case CALL -> {
        final Address addrCall = Address.wrap(frame.getStackItem(2));
        final boolean depStatus =
            hub.conflation().deploymentInfo().isDeploying(frame.getContractAddress());
        final int depNumber = hub.conflation().deploymentInfo().number(frame.getContractAddress());
        final int argsOffset = frame.getStackItem(4).toUnsignedBigInteger().shortValueExact();
        final int argsLength = frame.getStackItem(5).toUnsignedBigInteger().shortValueExact();
        final Bytes byteCode =
            frame.getWorldUpdater().get(addrCall).getCode().slice(argsOffset, argsLength);
        if (!byteCode.isEmpty()) {
          codeIdentifierBeforeLexOrder += 1;
          final RomChunk chunk =
              new RomChunk(addrCall, depNumber, depStatus, true, false, byteCode);
          chunkMap.put(chunk, codeIdentifierBeforeLexOrder);
        }
      }

      case CALLCODE -> {
        final Address addrCall = Address.wrap(frame.getStackItem(2));
        final boolean depStatus =
            hub.conflation().deploymentInfo().isDeploying(frame.getContractAddress());
        final int depNumber = hub.conflation().deploymentInfo().number(frame.getContractAddress());
        final int argsOffset = frame.getStackItem(4).toUnsignedBigInteger().shortValueExact();
        final int argsLength = frame.getStackItem(5).toUnsignedBigInteger().shortValueExact();
        final Bytes byteCode =
            frame.getWorldUpdater().get(addrCall).getCode().slice(argsOffset, argsLength);
        if (!byteCode.isEmpty()) {
          codeIdentifierBeforeLexOrder += 1;
          final RomChunk chunk =
              new RomChunk(addrCall, depNumber, depStatus, true, false, byteCode);
          chunkMap.put(chunk, codeIdentifierBeforeLexOrder);
        }
      }

      case DELEGATECALL -> {
        final Address addrCall = Address.wrap(frame.getStackItem(2));
        final boolean depStatus =
            hub.conflation().deploymentInfo().isDeploying(frame.getContractAddress());
        final int depNumber = hub.conflation().deploymentInfo().number(frame.getContractAddress());
        final int argsOffset = frame.getStackItem(3).toUnsignedBigInteger().shortValueExact();
        final int argsLength = frame.getStackItem(4).toUnsignedBigInteger().shortValueExact();
        final Bytes byteCode =
            frame.getWorldUpdater().get(addrCall).getCode().slice(argsOffset, argsLength);
        if (!byteCode.isEmpty()) {
          codeIdentifierBeforeLexOrder += 1;
          final RomChunk chunk =
              new RomChunk(addrCall, depNumber, depStatus, true, false, byteCode);
          chunkMap.put(chunk, codeIdentifierBeforeLexOrder);
        }
      }

      case STATICCALL -> {
        final Address addrCall = Address.wrap(frame.getStackItem(2));
        final boolean depStatus =
            hub.conflation().deploymentInfo().isDeploying(frame.getContractAddress());
        final int depNumber = hub.conflation().deploymentInfo().number(frame.getContractAddress());
        final int argsOffset = frame.getStackItem(3).toUnsignedBigInteger().shortValueExact();
        final int argsLength = frame.getStackItem(4).toUnsignedBigInteger().shortValueExact();
        final Bytes byteCode =
            frame.getWorldUpdater().get(addrCall).getCode().slice(argsOffset, argsLength);
        if (!byteCode.isEmpty()) {
          codeIdentifierBeforeLexOrder += 1;
          final RomChunk chunk =
              new RomChunk(addrCall, depNumber, depStatus, true, false, byteCode);
          chunkMap.put(chunk, codeIdentifierBeforeLexOrder);
        }
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
        .readFromState(chunk.readFromTheState());
    this.builder.validateRow();
  }

  @Override
  public int lineCount() {
    return chunkMap.size();
  }

  @Override
  public Object commit() {
    int cfi = 0;
    final int cfiInfty = chunkMap.size();
    for (RomChunk chunk : chunkMap.keySet()) {
      cfi += 1;
      traceChunk(chunk, cfi, cfiInfty);
    }
    return new RomLexTrace(builder.build());
  }
}
