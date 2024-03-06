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

package net.consensys.linea.zktracer.module.mmio;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.module.ec_data.EcData;
import net.consensys.linea.zktracer.module.rlp.txn.RlpTxn;
import net.consensys.linea.zktracer.module.rlp.txrcpt.RlpTxrcpt;
import net.consensys.linea.zktracer.module.romLex.RomLex;
import net.consensys.linea.zktracer.runtime.callstack.CallStack;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class ExoSumDecoder {
  private Map<ExoSource, Boolean> exoSourcesMap = new HashMap<>();
  private final CallStack callStack;
  private final RomLex romLex;
  private final RlpTxn rlpTxn;
  private final RlpTxrcpt rlpTxrcpt;
  private final EcData ecData;
  // private final Blake2fModexpData blake2fModexpData;

  @Getter private boolean exoIsRom;
  @Getter private boolean exoIsBlake2fModexp;
  @Getter private boolean exoIsEcData;
  @Getter private boolean exoIsRipSha;
  @Getter private boolean exoIsKeccak;
  @Getter private boolean exoIsLog;
  @Getter private boolean exoIsTxcd;

  public void decode(int exoSum) {
    exoIsRom = decodeInternal(Trace.EXO_SUM_ROM, exoSum);
    exoIsBlake2fModexp = decodeInternal(Trace.EXO_SUM_BLAKEMODEXP, exoSum);
    exoIsEcData = decodeInternal(Trace.EXO_SUM_ECDATA, exoSum);
    exoIsRipSha = decodeInternal(Trace.EXO_SUM_RIPSHA, exoSum);
    exoIsKeccak = decodeInternal(Trace.EXO_SUM_KEC, exoSum);
    exoIsLog = decodeInternal(Trace.EXO_SUM_LOG, exoSum);
    exoIsTxcd = decodeInternal(Trace.EXO_SUM_TXCD, exoSum);

    exoSourcesMap =
        Map.of(
            ExoSource.ROM, exoIsRom,
            ExoSource.BLAKE2f_MODEXP, exoIsBlake2fModexp,
            ExoSource.EC_DATA, exoIsEcData,
            ExoSource.LOG, exoIsLog,
            ExoSource.RIPSHA, exoIsRipSha,
            ExoSource.KECCAK, exoIsKeccak,
            ExoSource.TX_CALLDATA, exoIsTxcd);
  }

  // this should be called only once, by the MMU preprocessing
  public Bytes extractBytesFromExo(final int id) {
    return exoSourcesMap.entrySet().stream()
        .filter(Map.Entry::getValue)
        .map(
            e -> {
              switch (e.getKey()) {
                case ROM -> this.romLex.sortedChunks().get(id - 1).byteCode().copy();
                case TX_CALLDATA -> this.rlpTxn.chunkList.get(id - 1).tx().getPayload();
                case KECCAK -> {}
                case RIPSHA -> {}
                case BLAKE2f_MODEXP -> {}
                case LOG -> this.rlpTxrcpt.getLogDataByAbsLogNumber(id);
                case EC_DATA -> {}
              }
              return Bytes.EMPTY;
            })
        .toList()
        .get(0);
  }

  private static boolean decodeInternal(int exoSumModuleConstant, int exoSum) {
    return UnsignedByte.of(exoSum)
        .shiftRight(exoSumModuleConstant)
        .mod(2)
        .equals(UnsignedByte.of(1));
  }

  // public static Bytes readLimbBytesFromBytecode(final Bytes data, final int limbIndex) {
  //  int bytePtrStart = LLARGE * limbIndex;
  //  if (bytePtrStart >= data.size()) {
  //    return Bytes.EMPTY;
  //  }
  //
  //  int bytePtrEnd = bytePtrStart + LLARGE;
  //  int byteCodeEnd = Math.min(bytePtrEnd, data.size());
  //
  //  return data.slice(bytePtrStart, byteCodeEnd);
  // }

  //  private UnsignedByte[] readLimbUnsignedBytesFromBytecode(int limbIndex, Bytes data) {
  //    UnsignedByte[] result = UnsignedByte.EMPTY_BYTES16;
  //
  //    int bytePtrStart = 16 * limbIndex;
  //    if (bytePtrStart >= data.size()) {
  //      return result;
  //    }
  //
  //    int bytePtrEnd = bytePtrStart + 16;
  //    int byteCodeEnd = Math.min(bytePtrEnd, data.size());
  //    int copied = byteCodeEnd - bytePtrStart;
  //    UnsignedByte[] src = bytesToUnsignedBytes(data.slice(bytePtrStart, byteCodeEnd).toArray());
  //    System.arraycopy(src, 0, result, 0, copied);
  //
  //    return result;
  //  }
}
