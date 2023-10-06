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

  @Override
  public String jsonKey() {
    return "rom";
  }

  @Override
  public int lineCount() {
    int traceRowSize = 0;
    for (RomChunk chunk : RomLex.chunkList) {
      traceRowSize += chunkRowSize(chunk);
    }
    return traceRowSize;
  }

  public int chunkRowSize(RomChunk chunk) {
    final int nPaddingRow = 32;
    final int codeSize = chunk.getByteCode().size();
    final int nbSlice = (codeSize + (llarge - 1)) / llarge;

    return llarge * nbSlice + nPaddingRow;
  }

  private void traceChunk(RomChunk chunk, int cfi) {
    final int chunkRowSize = chunkRowSize(chunk);
    final int codeSize = chunk.getByteCode().size();
    final int nbSlice = (codeSize + (llarge - 1)) / llarge;
    Bytes dataPadded = padToGivenSizeWithRightZero(chunk.getByteCode(), chunkRowSize);
    for (int i = 0; i < chunkRowSize; i++) {
      this.builder
          .codeFragmentIndex(BigInteger.valueOf(cfi))
          .addressHi(chunk.getAddress().slice(0, 4).toUnsignedBigInteger())
          .addressLo(chunk.getAddress().slice(4, llarge).toUnsignedBigInteger())
          .programmeCounter(BigInteger.valueOf(i))
          .limb(dataPadded.slice(i / llarge, llarge).toUnsignedBigInteger())
          .codesize(BigInteger.valueOf(codeSize));
      if (i < codeSize) {
        this.builder
            .codesizeReached(false)
            .paddedBytecodeByte(UnsignedByte.of(chunk.byteCode().get(i)));
      } else {
        this.builder.codesizeReached(true).paddedBytecodeByte(UnsignedByte.of(0));
      }
      final int nRowData = nbSlice * llarge;
      if (i < nRowData) {
        this.builder.counter(BigInteger.valueOf(i % llarge));
      } else {
        this.builder.counter(BigInteger.valueOf(i - nRowData));
      }
      this.builder.validateRow();
    }
  }

  @Override
  public Object commit() {
    int expectedTraceSize = 0;
    int cfi = 0;
    for (RomChunk chunk : RomLex.chunkList) {
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
