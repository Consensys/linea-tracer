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

package net.consensys.linea.zktracer.module.rlpaddr;

import static net.consensys.linea.zktracer.module.constants.GlobalConstants.LLARGE;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.RLP_ADDR_RECIPE_1;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.RLP_ADDR_RECIPE_2;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.RLP_PREFIX_INT_SHORT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.RLP_PREFIX_LIST_SHORT;
import static net.consensys.linea.zktracer.module.rlpaddr.Trace.MAX_CT_CREATE;
import static net.consensys.linea.zktracer.module.rlpaddr.Trace.MAX_CT_CREATE2;
import static net.consensys.linea.zktracer.module.rlputils.Pattern.byteCounting;
import static net.consensys.linea.zktracer.types.AddressUtils.getCreate2RawAddress;
import static net.consensys.linea.zktracer.types.AddressUtils.getCreateRawAddress;
import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;
import static net.consensys.linea.zktracer.types.Conversions.longToUnsignedBigInteger;
import static net.consensys.linea.zktracer.types.Utils.bitDecomposition;
import static net.consensys.linea.zktracer.types.Utils.leftPadTo;
import static net.consensys.linea.zktracer.types.Utils.rightPadTo;
import static org.hyperledger.besu.crypto.Hash.keccak256;
import static org.hyperledger.besu.evm.internal.Words.clampedToLong;

import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.util.List;

import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.container.stacked.list.StackedList;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.constants.GlobalConstants;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.rlputils.ByteCountAndPowerOutput;
import net.consensys.linea.zktracer.module.trm.Trm;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.BitDecOutput;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.worldstate.WorldView;

@RequiredArgsConstructor
public class RlpAddr implements Module {
  private static final Bytes CREATE2_SHIFT = Bytes.minimalBytes(GlobalConstants.CREATE2_SHIFT);
  private static final Bytes INT_SHORT = Bytes.minimalBytes(RLP_PREFIX_INT_SHORT);
  private static final UnsignedByte BYTES_LLARGE = UnsignedByte.of(LLARGE);
  final int recipe1NbRows = MAX_CT_CREATE + 1;

  private final Hub hub;
  private final Trm trm;
  private final StackedList<RlpAddrOperation> chunkList = new StackedList<>();

  @Override
  public String moduleKey() {
    return "RLP_ADDR";
  }

  @Override
  public void enterTransaction() {
    this.chunkList.enter();
  }

  @Override
  public void popTransaction() {
    this.chunkList.pop();
  }

  @Override
  public void traceStartTx(WorldView world, Transaction tx) {
    if (tx.getTo().isEmpty()) {
      final Address senderAddress = tx.getSender();
      final long nonce = tx.getNonce();
      final Bytes32 rawTo = getCreateRawAddress(senderAddress, nonce);
      RlpAddrOperation chunk =
          new RlpAddrOperation(
              rawTo, OpCode.CREATE, longToUnsignedBigInteger(nonce), senderAddress);
      this.chunkList.add(chunk);
      this.trm.callTrimming(rawTo);
    }
  }

  @Override
  public void tracePreOpcode(MessageFrame frame) {
    final OpCode opcode = this.hub.opCode();
    switch (opcode) {
      case CREATE -> {
        final Address currentAddress = frame.getRecipientAddress();
        final Bytes32 rawCreateAddress = getCreateRawAddress(frame);
        RlpAddrOperation chunk =
            new RlpAddrOperation(
                rawCreateAddress,
                OpCode.CREATE,
                longToUnsignedBigInteger(frame.getWorldUpdater().get(currentAddress).getNonce()),
                currentAddress);
        this.chunkList.add(chunk);
        this.trm.callTrimming(rawCreateAddress);
      }
      case CREATE2 -> {
        final Address sender = frame.getRecipientAddress();

        final Bytes32 salt = Bytes32.leftPad(frame.getStackItem(3));

        final long offset = clampedToLong(frame.getStackItem(1));
        final long length = clampedToLong(frame.getStackItem(2));
        final Bytes initCode = frame.shadowReadMemory(offset, length);
        final Bytes32 hash = keccak256(initCode);

        final Bytes32 rawCreate2Address = getCreate2RawAddress(sender, salt, hash);

        RlpAddrOperation chunk =
            new RlpAddrOperation(rawCreate2Address, OpCode.CREATE2, sender, salt, hash);
        this.chunkList.add(chunk);
        this.trm.callTrimming(rawCreate2Address);
      }
    }
  }

  private void traceCreate2(int stamp, RlpAddrOperation chunk, Trace trace) {
    final Bytes rawAddressHi = chunk.rawHash().slice(0, LLARGE);
    final long depAddressHi = rawAddressHi.slice(12, 4).toLong();
    final Bytes depAddressLo = chunk.rawHash().slice(LLARGE, LLARGE);

    for (int ct = 0; ct <= MAX_CT_CREATE2; ct++) {
      trace
          .stamp(stamp)
          .recipe(UnsignedByte.of(RLP_ADDR_RECIPE_2))
          .recipe1(false)
          .recipe2(true)
          .rawAddrHi(rawAddressHi)
          .depAddrHi(depAddressHi)
          .depAddrLo(depAddressLo)
          .addrHi(chunk.address().slice(0, 4).toLong())
          .addrLo(chunk.address().slice(4, LLARGE))
          .saltHi(chunk.salt().orElseThrow().slice(0, LLARGE))
          .saltLo(chunk.salt().orElseThrow().slice(LLARGE, LLARGE))
          .kecHi(chunk.keccak().orElseThrow().slice(0, LLARGE))
          .kecLo(chunk.keccak().orElseThrow().slice(LLARGE, LLARGE))
          .lc(true)
          .index(UnsignedByte.of(ct))
          .counter(UnsignedByte.of(ct));

      switch (ct) {
        case 0 -> {
          trace.limb(
              rightPadTo(Bytes.concatenate(CREATE2_SHIFT, chunk.address().slice(0, 4)), LLARGE));
          trace.nBytes(UnsignedByte.of(5)).selectorKeccakRes(true);
        }
        case 1 -> trace
            .limb(chunk.address().slice(4, LLARGE))
            .nBytes(BYTES_LLARGE)
            .selectorKeccakRes(false);
        case 2 -> trace
            .limb(chunk.salt().orElseThrow().slice(0, LLARGE))
            .nBytes(BYTES_LLARGE)
            .selectorKeccakRes(false);
        case 3 -> trace
            .limb(chunk.salt().orElseThrow().slice(LLARGE, LLARGE))
            .nBytes(BYTES_LLARGE)
            .selectorKeccakRes(false);
        case 4 -> trace
            .limb(chunk.keccak().orElseThrow().slice(0, LLARGE))
            .nBytes(BYTES_LLARGE)
            .selectorKeccakRes(false);
        case 5 -> trace
            .limb(chunk.keccak().orElseThrow().slice(LLARGE, LLARGE))
            .nBytes(BYTES_LLARGE)
            .selectorKeccakRes(false);
      }

      // Columns unused for Recipe2
      trace.fillAndValidateRow();
    }
  }

  private void traceCreate(int stamp, RlpAddrOperation chunk, Trace trace) {
    final BigInteger nonce = chunk.nonce().orElseThrow();

    Bytes nonceShifted = leftPadTo(bigIntegerToBytes(nonce), recipe1NbRows);
    Boolean tinyNonZeroNonce = true;
    if (nonce.compareTo(BigInteger.ZERO) == 0 || nonce.compareTo(BigInteger.valueOf(128)) >= 0) {
      tinyNonZeroNonce = false;
    }
    // Compute the BYTESIZE and POWER columns
    int nonceByteSize = bigIntegerToBytes(nonce).size();
    if (nonce.equals(BigInteger.ZERO)) {
      nonceByteSize = 0;
    }
    ByteCountAndPowerOutput byteCounting = byteCounting(nonceByteSize, recipe1NbRows);

    // Compute the bit decomposition of the last input's byte
    final byte lastByte = nonceShifted.get(recipe1NbRows - 1);
    BitDecOutput bitDecomposition = bitDecomposition(0xff & lastByte, recipe1NbRows);

    int size_rlp_nonce = nonceByteSize;
    if (!tinyNonZeroNonce) {
      size_rlp_nonce += 1;
    }

    // Bytes RLP(nonce)
    Bytes rlpNonce;
    if (nonce.compareTo(BigInteger.ZERO) == 0) {
      rlpNonce = INT_SHORT;
    } else {
      if (tinyNonZeroNonce) {
        rlpNonce = bigIntegerToBytes(nonce);
      } else {
        rlpNonce =
            Bytes.concatenate(
                bigIntegerToBytes(
                    BigInteger.valueOf(
                        128 + byteCounting.accByteSizeList().get(recipe1NbRows - 1))),
                bigIntegerToBytes(nonce));
      }
    }

    final Bytes rawAddressHi = chunk.rawHash().slice(0, LLARGE);
    final long depAddressHi = rawAddressHi.slice(12, 4).toLong();
    final Bytes depAddressLo = chunk.rawHash().slice(LLARGE, LLARGE);
    final Bytes nonceBytes = bigIntegerToBytes(nonce);

    for (int ct = 0; ct < recipe1NbRows; ct++) {
      trace
          .stamp(stamp)
          .recipe(UnsignedByte.of(RLP_ADDR_RECIPE_1))
          .recipe1(true)
          .recipe2(false)
          .addrHi(chunk.address().slice(0, 4).toLong())
          .addrLo(chunk.address().slice(4, LLARGE))
          .rawAddrHi(rawAddressHi)
          .depAddrHi(depAddressHi)
          .depAddrLo(depAddressLo)
          .nonce(nonceBytes)
          .counter(UnsignedByte.of(ct))
          .byte1(UnsignedByte.of(nonceShifted.get(ct)))
          .acc(nonceShifted.slice(0, ct + 1))
          .accBytesize(UnsignedByte.of(byteCounting.accByteSizeList().get(ct)))
          .power(
              bigIntegerToBytes(byteCounting.powerList().get(ct).divide(BigInteger.valueOf(256))))
          .bit1(bitDecomposition.bitDecList().get(ct))
          .bitAcc(UnsignedByte.of(bitDecomposition.bitAccList().get(ct)))
          .tinyNonZeroNonce(tinyNonZeroNonce);

      switch (ct) {
        case 0, 1, 2, 3 -> trace
            .lc(false)
            .limb(Bytes.EMPTY)
            .nBytes(UnsignedByte.ZERO)
            .index(UnsignedByte.ZERO)
            .selectorKeccakRes(ct == 0);
        case 4 -> trace
            .lc(true)
            .limb(
                rightPadTo(
                    bigIntegerToBytes(
                        BigInteger.valueOf(RLP_PREFIX_LIST_SHORT)
                            .add(BigInteger.valueOf(21))
                            .add(BigInteger.valueOf(size_rlp_nonce))),
                    LLARGE))
            .nBytes(UnsignedByte.of(1))
            .index(UnsignedByte.ZERO)
            .selectorKeccakRes(false);
        case 5 -> trace
            .lc(true)
            .limb(
                rightPadTo(
                    Bytes.concatenate(
                        bigIntegerToBytes(BigInteger.valueOf(148)), chunk.address().slice(0, 4)),
                    LLARGE))
            .nBytes(UnsignedByte.of(5))
            .index(UnsignedByte.of(1))
            .selectorKeccakRes(false);
        case 6 -> trace
            .lc(true)
            .limb(chunk.address().slice(4, LLARGE))
            .nBytes(UnsignedByte.of(LLARGE))
            .index(UnsignedByte.of(2))
            .selectorKeccakRes(false);
        case 7 -> trace
            .lc(true)
            .limb(rightPadTo(rlpNonce, LLARGE))
            .nBytes(UnsignedByte.of(size_rlp_nonce))
            .index(UnsignedByte.of(3))
            .selectorKeccakRes(false);
      }

      // Column not used fo recipe 1:
      trace.fillAndValidateRow();
    }
  }

  private void traceChunks(RlpAddrOperation chunk, int stamp, Trace trace) {
    if (chunk.opCode().equals(OpCode.CREATE)) {
      traceCreate(stamp, chunk, trace);
    } else {
      traceCreate2(stamp, chunk, trace);
    }
  }

  @Override
  public int lineCount() {
    return this.chunkList.lineCount();
  }

  @Override
  public List<ColumnHeader> columnsHeaders() {
    return Trace.headers(this.lineCount());
  }

  @Override
  public void commit(List<MappedByteBuffer> buffers) {
    final Trace trace = new Trace(buffers);

    for (int i = 0; i < this.chunkList.size(); i++) {
      traceChunks(chunkList.get(i), i + 1, trace);
    }
  }
}
