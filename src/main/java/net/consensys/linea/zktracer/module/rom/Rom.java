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

import java.math.BigInteger;

import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.romLex.RomChunk;
import net.consensys.linea.zktracer.module.romLex.RomLex;

public class Rom implements Module {
  final net.consensys.linea.zktracer.module.rom.Trace.TraceBuilder builder = Trace.builder();

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
    int nbSlice = (chunk.byteCode().size() + 15) / 16;
    return 16 * nbSlice + 32;
  }

  private void traceChunk(RomChunk chunk, int cfi) {
    for (int i = 0; i < chunkRowSize(chunk); i++) {
      this.builder
          .codeFragmentIndex(BigInteger.valueOf(cfi))
          .programmeCounter(BigInteger.valueOf(i));
      this.builder.validateRow();
    }
  }

  @Override
  public Object commit() {
    int expectedTraceSize = 0;
    int cfi = 0;
    for (RomChunk chunk : RomLex.chunkList) {
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
