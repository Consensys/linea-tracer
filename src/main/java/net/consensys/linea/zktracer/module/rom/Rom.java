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

package net.consensys.linea.zktracer.module.rom;

import static net.consensys.linea.zktracer.module.rlppatterns.pattern.padToGivenSizeWithRightZero;

import java.math.BigInteger;

import net.consensys.linea.zktracer.bytes.UnsignedByte;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.romLex.RomChunk;
import net.consensys.linea.zktracer.module.romLex.RomLex;
import org.apache.tuweni.bytes.Bytes;

public class Rom implements Module {
  final net.consensys.linea.zktracer.module.rom.Trace.TraceBuilder builder = Trace.builder();
  final int llarge = 16;
  final int llargeMO = 15;
  final int evmWordMO = 31;
  final int push0 = 0x60;
  final int push32 = 0x7F;
  final UnsignedByte invalid = UnsignedByte.of(0xFE);

  @Override
  public String jsonKey() {
    return "rom";
  }

  @Override
  public int lineCount() {
    int traceRowSize = 0;
    for (RomChunk chunk : RomLex.chunkMap.keySet()) {
      traceRowSize += chunkRowSize(chunk);
    }
    return traceRowSize;
  }

  public int chunkRowSize(RomChunk chunk) {
    final int nPaddingRow = 32;
    final int codeSize = chunk.byteCode().size();
    final int nbSlice = (codeSize + (llarge - 1)) / llarge;

    return llarge * nbSlice + nPaddingRow;
  }

  private void traceChunk(RomChunk chunk, int cfi) {
    final int chunkRowSize = chunkRowSize(chunk);
    final int codeSize = chunk.byteCode().size();
    final int nbSlice = (codeSize + (llarge - 1)) / llarge;
    final Bytes dataPadded = padToGivenSizeWithRightZero(chunk.byteCode(), chunkRowSize);
    final int nRowData = nbSlice * llarge;
    final int nBytesLastRow = codeSize % llarge;
    int pushParameter = 0;
    int ctPush = 0;
    Bytes pushValueHigh = Bytes.minimalBytes(0);
    Bytes pushValueLow = Bytes.minimalBytes(0);

    for (int i = 0; i < chunkRowSize; i++) {
      boolean codeSizeReached = i >= codeSize;
      int sliceNumber = i / 16 + 1;
      if (sliceNumber == nbSlice + 2) {
        sliceNumber = nbSlice + 1;
      }

      // Fill Generic columns
      this.builder
          .codeFragmentIndex(BigInteger.valueOf(cfi))
          .programmeCounter(BigInteger.valueOf(i))
          .limb(dataPadded.slice(i / llarge, llarge).toUnsignedBigInteger())
          .codesize(BigInteger.valueOf(codeSize))
          .paddedBytecodeByte(UnsignedByte.of(dataPadded.get(i)))
          .acc(dataPadded.slice(sliceNumber * llarge, (i % llarge) + 1).toUnsignedBigInteger())
          .codesizeReached(codeSizeReached)
          .index(BigInteger.valueOf(sliceNumber));
      if (sliceNumber <= nbSlice) {
        this.builder.counterMax(BigInteger.valueOf(llargeMO));
      } else {
        this.builder.counterMax(BigInteger.valueOf(evmWordMO));
      }

      // Fill CT, nBYTES, nBYTES_ACC
      if (i < nRowData) {
        this.builder.counter(BigInteger.valueOf(i % llarge));
        if (sliceNumber < nbSlice) {
          this.builder
              .nBytes(BigInteger.valueOf(llarge))
              .nBytesAcc(BigInteger.valueOf((i % llarge) + 1));
        } else {
          this.builder
              .nBytes(BigInteger.valueOf(nBytesLastRow))
              .nBytesAcc(
                  BigInteger.valueOf(nBytesLastRow).min(BigInteger.valueOf((i % llarge) + 1)));
        }

      } else {
        this.builder
            .counter(BigInteger.valueOf(i - nRowData))
            .nBytes(BigInteger.ZERO)
            .nBytesAcc(BigInteger.ZERO);
      }

      // Deal when not in a PUSH instruction
      if (pushParameter == 0) {
        UnsignedByte opCode = UnsignedByte.of(dataPadded.get(i));
        Boolean isPush = false;

        // The OpCode is a PUSH instruction
        if (push0 <= opCode.toInteger() && opCode.toInteger() < push32) {
          isPush = true;
          pushParameter = opCode.toInteger() - push0 + 1;
          if (pushParameter > llarge) {
            pushValueHigh = dataPadded.slice(i + 1, pushParameter - llarge);
            pushValueLow = dataPadded.slice(i + 1 + pushParameter - llarge, llarge);
          } else {
            pushValueLow = dataPadded.slice(i + 1, pushParameter);
          }
        }

        this.builder
            .isPush(isPush)
            .isPushData(false)
            .opcode(opCode)
            .pushParameter(BigInteger.valueOf(pushParameter))
            .counterPush(BigInteger.ZERO)
            .pushValueAcc(BigInteger.ZERO)
            .pushValueHigh(pushValueHigh.toUnsignedBigInteger())
            .pushValueLow(pushValueLow.toUnsignedBigInteger())
            .pushFunnelBit(false)
            .validJumpDestination(opCode == invalid);
      }

      // Deal when in a PUSH instruction
      else {
        ctPush += 1;
        this.builder
            .isPush(false)
            .isPushData(true)
            .opcode(invalid)
            .pushParameter(BigInteger.valueOf(pushParameter))
            .pushValueHigh(pushValueHigh.toUnsignedBigInteger())
            .pushValueLow(pushValueLow.toUnsignedBigInteger())
            .counterPush(BigInteger.valueOf(ctPush))
            .pushFunnelBit(pushParameter > llarge && ctPush > pushParameter - llarge)
            .validJumpDestination(false);

        if (pushParameter <= llarge) {
          this.builder.pushValueAcc(pushValueLow.slice(0, ctPush).toUnsignedBigInteger());
        } else {
          if (ctPush <= pushParameter - llarge) {
            this.builder.pushValueAcc(pushValueHigh.slice(0, ctPush).toUnsignedBigInteger());
          } else {
            this.builder.pushValueAcc(
                pushValueLow.slice(0, ctPush + llarge - pushParameter).toUnsignedBigInteger());
          }
        }

        // reinitialise push constant data
        if (ctPush == pushParameter) {
          ctPush = 0;
          pushParameter = 0;
          pushValueHigh = Bytes.minimalBytes(0);
          pushValueLow = Bytes.minimalBytes(0);
        }
      }

      this.builder.validateRow();
    }
  }

  @Override
  public Object commit() {
    int expectedTraceSize = 0;
    int cfi = 0;
    for (RomChunk chunk : RomLex.chunkMap.keySet()) {
      cfi += 1;
      traceChunk(chunk, cfi);
      expectedTraceSize += chunkRowSize(chunk);
      if (this.builder.size() != expectedTraceSize) {
        throw new RuntimeException(
            "ChunkSize is not the right one, chunk n°: "
                + cfi
                + " calculated size ="
                + expectedTraceSize
                + " trace size ="
                + this.builder.size());
      }
    }
    return new RomTrace(builder.build());
  }
}
