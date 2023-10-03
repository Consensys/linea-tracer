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

package net.consensys.linea.zktracer.module.rlp_addr;

import static net.consensys.linea.zktracer.bytes.conversions.bigIntegerToBytes;
import static net.consensys.linea.zktracer.module.rlppatterns.pattern.bitDecomposition;
import static net.consensys.linea.zktracer.module.rlppatterns.pattern.byteCounting;
import static net.consensys.linea.zktracer.module.rlppatterns.pattern.padToGivenSizeWithLeftZero;
import static net.consensys.linea.zktracer.module.rlppatterns.pattern.padToGivenSizeWithRightZero;
import static org.hyperledger.besu.crypto.Hash.keccak256;
import static org.hyperledger.besu.evm.internal.Words.clampedToLong;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import net.consensys.linea.zktracer.bytes.UnsignedByte;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.rlppatterns.RlpBitDecOutput;
import net.consensys.linea.zktracer.module.rlppatterns.RlpByteCountAndPowerOutput;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class RlpAddr implements Module {
  Bytes create2_shift = Bytes.ofUnsignedShort(0xff);
  Bytes int_short = bigIntegerToBytes(BigInteger.valueOf(0x80));
  int list_short = 0xc0;
  int llarge = 16;

  private final Trace.TraceBuilder builder = Trace.builder();

  private final List<RlpAddrChunk> chunkList = new ArrayList<>();

  @Override
  public String jsonKey() {
    return "rlpAddr";
  }

  @Override
  public void traceStartTx(WorldView world, Transaction tx) {
    if (tx.getTo().isEmpty()) {
      RlpAddrChunk chunk = new RlpAddrChunk(OpCode.CREATE, tx.getNonce(), tx.getSender());
      this.chunkList.add(chunk);
    }
  }

  @Override
  public void trace(MessageFrame frame) {
    OpCode opcode = OpCode.of(frame.getCurrentOperation().getOpcode());
    // CREATE
    if (opcode.equals(OpCode.CREATE)) {
      RlpAddrChunk chunk =
          new RlpAddrChunk(
              OpCode.CREATE,
              frame.getWorldUpdater().getSenderAccount(frame).getNonce() - 1,
              frame.getSenderAddress());
      this.chunkList.add(chunk);
    }
    // CREATE2
    if (opcode.equals(OpCode.CREATE2)) {

      final long offset = clampedToLong(frame.getStackItem(1));
      final long length = clampedToLong(frame.getStackItem(2));
      final Bytes initCode = frame.readMutableMemory(offset, length);
      final Bytes32 salt = Bytes32.leftPad(frame.getStackItem(3));
      final Bytes32 hash = keccak256(initCode);

      RlpAddrChunk chunk = new RlpAddrChunk(OpCode.CREATE2, frame.getSenderAddress(), salt, hash);
      this.chunkList.add(chunk);
    }
  }

  private void traceCreate2(int stamp, Address addr, Bytes32 salt, Bytes32 kec) {

    final Address deployementAddress =
        Address.extract(keccak256(Bytes.concatenate(create2_shift, addr, salt, kec)));

    for (int ct = 0; ct < 6; ct++) {
      this.builder
          .stamp(BigInteger.valueOf(stamp))
          .recipe(BigInteger.valueOf(2))
          .recipe1(false)
          .recipe2(true)
          .depAddrHi(deployementAddress.slice(0, 4).toUnsignedBigInteger())
          .depAddrLo(deployementAddress.slice(4, llarge).toUnsignedBigInteger())
          .addrHi(addr.slice(0, 4).toUnsignedBigInteger())
          .addrLo(addr.slice(4, llarge).toUnsignedBigInteger())
          .saltHi(salt.slice(0, llarge).toUnsignedBigInteger())
          .saltLo(salt.slice(llarge, llarge).toUnsignedBigInteger())
          .kecHi(kec.slice(0, llarge).toUnsignedBigInteger())
          .kecLo(kec.slice(llarge, llarge).toUnsignedBigInteger())
          .lc(true)
          .index(BigInteger.valueOf(ct))
          .ct(BigInteger.valueOf(ct));

      switch (ct) {
        case 0:
          this.builder.limb(
              padToGivenSizeWithRightZero(
                      Bytes.concatenate(create2_shift, addr.slice(0, 4)), llarge)
                  .toUnsignedBigInteger());
          this.builder.nBytes(BigInteger.valueOf(5));
          break;

        case 1:
          this.builder
              .limb(addr.slice(4, llarge).toUnsignedBigInteger())
              .nBytes(BigInteger.valueOf(llarge));
          break;

        case 2:
          this.builder
              .limb(salt.slice(0, llarge).toUnsignedBigInteger())
              .nBytes(BigInteger.valueOf(llarge));
          break;

        case 3:
          this.builder
              .limb(salt.slice(llarge, llarge).toUnsignedBigInteger())
              .nBytes(BigInteger.valueOf(llarge));
          break;

        case 4:
          this.builder
              .limb(kec.slice(0, llarge).toUnsignedBigInteger())
              .nBytes(BigInteger.valueOf(llarge));
          break;

        case 5:
          this.builder
              .limb(kec.slice(llarge, llarge).toUnsignedBigInteger())
              .nBytes(BigInteger.valueOf(llarge));
          break;
      }

      // Columns unused for Recipe2
      this.builder
          .nonce(BigInteger.ZERO)
          .byte1(UnsignedByte.of(0))
          .acc(BigInteger.ZERO)
          .accBytesize(BigInteger.ZERO)
          .power(BigInteger.ZERO)
          .bit1(false)
          .bitAcc(UnsignedByte.of(0))
          .tinyNonZeroNonce(false);

      this.builder.validateRow();
    }
  }

  private void traceCreate(int stamp, BigInteger nonce, Address addr) {
    int recipe1CtMax = 8;

    Bytes nonceShifted = padToGivenSizeWithLeftZero(bigIntegerToBytes(nonce), recipe1CtMax);
    Boolean tinyNonZeroNonce = true;
    if (nonce.compareTo(BigInteger.ZERO) == 0 || nonce.compareTo(BigInteger.valueOf(128)) >= 0) {
      tinyNonZeroNonce = false;
    }
    // Compute the bytesize and Power columns
    int nonceByteSize = nonce.bitLength() / 8;
    if (nonce.bitLength() % 8 != 0) {
      nonceByteSize += 1;
    }
    RlpByteCountAndPowerOutput byteCounting;
    if (nonce.compareTo(BigInteger.ZERO) == 0) {
      byteCounting = byteCounting(0, recipe1CtMax);
    } else {
      byteCounting = byteCounting(nonceByteSize, recipe1CtMax);
    }

    // Compute the bit decomposition of the last input's byte
    Byte lastByte = nonceShifted.get(recipe1CtMax - 1);
    RlpBitDecOutput bitDecomposition = bitDecomposition(lastByte, recipe1CtMax);

    int size_rlp_nonce = byteCounting.getAccByteSizeList().get(recipe1CtMax - 1);
    if (tinyNonZeroNonce) {
      size_rlp_nonce += 1;
    }

    // Bytes RLP(nonce)
    Bytes rlpNonce;
    if (nonce.compareTo(BigInteger.ZERO) == 0) {
      rlpNonce = int_short;
    } else {
      if (tinyNonZeroNonce) {
        rlpNonce = bigIntegerToBytes(nonce);
      } else {
        rlpNonce =
            Bytes.concatenate(
                bigIntegerToBytes(
                    BigInteger.valueOf(
                        128 + byteCounting.getAccByteSizeList().get(recipe1CtMax - 1))),
                bigIntegerToBytes(nonce));
      }
    }

    // Keccak of the Rlp to get the deployment address
    final Address deployementAddress = Address.contractAddress(addr, nonce.longValueExact());

    for (int ct = 0; ct < 8; ct++) {
      this.builder
          .stamp(BigInteger.valueOf(stamp))
          .recipe(BigInteger.ONE)
          .recipe1(true)
          .recipe2(false)
          .addrHi(addr.slice(0, 4).toUnsignedBigInteger())
          .addrLo(addr.slice(4, llarge).toUnsignedBigInteger())
          .depAddrHi(deployementAddress.slice(0, 4).toUnsignedBigInteger())
          .depAddrLo(deployementAddress.slice(4, llarge).toUnsignedBigInteger())
          .nonce(nonce)
          .ct(BigInteger.valueOf(ct))
          .byte1(UnsignedByte.of(nonceShifted.get(ct)))
          .acc(nonceShifted.slice(0, ct + 1).toUnsignedBigInteger())
          .accBytesize(BigInteger.valueOf(byteCounting.getAccByteSizeList().get(ct)))
          .power(byteCounting.getPowerList().get(ct))
          .bit1(bitDecomposition.getBitDecList().get(ct))
          .bitAcc(UnsignedByte.of(bitDecomposition.getBitAccList().get(ct)))
          .tinyNonZeroNonce(tinyNonZeroNonce);

      switch (ct) {
        case 0, 1, 2, 3:
          this.builder
              .lc(false)
              .limb(BigInteger.ZERO)
              .nBytes(BigInteger.ZERO)
              .index(BigInteger.ZERO);
          break;

        case 4:
          this.builder
              .lc(true)
              .limb(
                  padToGivenSizeWithRightZero(
                          bigIntegerToBytes(
                              BigInteger.valueOf(list_short)
                                  .add(BigInteger.valueOf(21))
                                  .add(BigInteger.valueOf(size_rlp_nonce))),
                          llarge)
                      .toUnsignedBigInteger())
              .nBytes(BigInteger.ONE)
              .index(BigInteger.ZERO);
          break;

        case 5:
          this.builder
              .lc(true)
              .limb(
                  padToGivenSizeWithRightZero(
                          Bytes.concatenate(
                              bigIntegerToBytes(BigInteger.valueOf(148)), addr.slice(0, 4)),
                          llarge)
                      .toUnsignedBigInteger())
              .nBytes(BigInteger.valueOf(5))
              .index(BigInteger.ONE);
          break;

        case 6:
          this.builder
              .lc(true)
              .limb(addr.slice(4, llarge).toUnsignedBigInteger())
              .nBytes(BigInteger.valueOf(16))
              .index(BigInteger.valueOf(2));
          break;

        case 7:
          this.builder
              .lc(true)
              .limb(padToGivenSizeWithRightZero(rlpNonce, llarge).toUnsignedBigInteger())
              .nBytes(BigInteger.valueOf(size_rlp_nonce))
              .index(BigInteger.valueOf(3));
          break;
      }

      // Column not used fo recipe 1:
      this.builder
          .saltHi(BigInteger.ZERO)
          .saltLo(BigInteger.ZERO)
          .kecHi(BigInteger.ZERO)
          .kecLo(BigInteger.ZERO);
      this.builder.validateRow();
    }
  }

  private void traceChunks(RlpAddrChunk chunk, int stamp) {
    if (chunk.opCode().equals(OpCode.CREATE)) {
      traceCreate(stamp, BigInteger.valueOf(chunk.nonce().get()), chunk.addr());
    } else {
      traceCreate2(stamp, chunk.addr(), chunk.salt().get(), chunk.kec().get());
    }
  }

  public int ChunkRowSize(RlpAddrChunk chunk) {
    int rowSize;
    if (chunk.opCode().equals(OpCode.CREATE)) {
      rowSize = 8;
    } else {
      rowSize = 6;
    }
    return rowSize;
  }

  public int TraceRowSize() {
    int traceRowSize = 0;
    for (RlpAddrChunk chunk : this.chunkList) {
      traceRowSize += ChunkRowSize(chunk);
    }
    return traceRowSize;
  }

  @Override
  public Object commit() {
    int estTraceSize = 0;
    for (int i = 0; i < this.chunkList.size(); i++) {
      traceChunks(chunkList.get(i), i + 1);
      estTraceSize += ChunkRowSize(chunkList.get(i));
      if (this.builder.size() != estTraceSize) {
        throw new RuntimeException(
            "ChunkSize is not the right one, chunk n°: "
                + i
                + " calculated size ="
                + estTraceSize
                + " trace size ="
                + this.builder.size());
      }
    }
    return new RlpAddrTrace(builder.build());
  }
}
