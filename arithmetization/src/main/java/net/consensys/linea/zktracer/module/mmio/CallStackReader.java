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

import static net.consensys.linea.zktracer.types.Conversions.bytesToUnsignedBytes;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.runtime.callstack.CallStack;
import net.consensys.linea.zktracer.runtime.callstack.ExoSource;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;

@RequiredArgsConstructor
@Accessors(fluent = true)
public class CallStackReader {
  @Getter private final CallStack callStack;

  public UnsignedByte[] valueFromMemory(final int counter, final int index) {
    return callStack.get(counter).pending().memorySegmentSnapshot().limbAtIndex(index);
  }

  public UnsignedByte[] valueFromExo(
      final Bytes contractByteCode, final ExoSource exoSource, final int limbIndex) {
    switch (exoSource) {
      case ROM -> {
        return readLimbFromBytecode(limbIndex, contractByteCode);
      }
      case TX_CALLDATA -> {
        Preconditions.checkArgument(
            callStack.depth() != 1, "Only the root contract can read TxCalldata");
        return readLimbFromBytecode(limbIndex, callStack.current().callData());
      }
      default -> throw new IllegalArgumentException(
          "Unknown ExoSource -> %s. Possible values: %s".formatted(exoSource, ExoSource.values()));
    }
  }

  private UnsignedByte[] readLimbFromBytecode(int limbIndex, Bytes data) {
    UnsignedByte[] result = UnsignedByte.EMPTY_BYTES16;

    int bytePtrStart = 16 * limbIndex;
    if (bytePtrStart >= data.size()) {
      return result;
    }

    int bytePtrEnd = bytePtrStart + 16;
    int byteCodeEnd = Math.min(bytePtrEnd, data.size());
    int copied = byteCodeEnd - bytePtrStart;
    UnsignedByte[] src = bytesToUnsignedBytes(data.slice(bytePtrStart, byteCodeEnd).toArray());
    System.arraycopy(src, 0, result, 0, copied);

    return result;
  }
}
