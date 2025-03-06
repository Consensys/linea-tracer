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

package net.consensys.linea.zktracer.module.limits.precompiles;

import static java.lang.Integer.MAX_VALUE;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.container.module.CountingOnlyModule;
import net.consensys.linea.zktracer.container.stacked.CountOnlyOperation;

@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public final class RipemdBlocks implements CountingOnlyModule {
  private final CountOnlyOperation counts = new CountOnlyOperation();
  @Setter private boolean transactionBundleContainsIllegalOperation = false;
  public static final int RIPEMD160_BLOCKSIZE = 64 * 8;
  // If the length is > 2⁶4, we just use the lower 64 bits.
  private static final int RIPEMD160_LENGTH_APPEND = 64;
  private static final int RIPEMD160_ND_PADDED_ONE = 1;

  @Override
  public String moduleKey() {
    return "PRECOMPILE_RIPEMD_BLOCKS";
  }

  @Override
  public void updateTally(final int count) {
    final int blockCount = numberOfRipemd160locks(count);
    if (blockCount == MAX_VALUE) {
      transactionBundleContainsIllegalOperation(true);
      return;
    }
    counts.add(blockCount);
  }

  public static int numberOfRipemd160locks(final int dataByteLength) {
    final long tmp =
        dataByteLength * 8L
            + RIPEMD160_ND_PADDED_ONE
            + RIPEMD160_LENGTH_APPEND
            + (RIPEMD160_BLOCKSIZE - 1);
    return tmp < Integer.MAX_VALUE ? (int) (tmp / RIPEMD160_BLOCKSIZE) : Integer.MAX_VALUE;
  }

  @Override
  public int lineCount() {
    return transactionBundleContainsIllegalOperation
        ? MAX_VALUE
        : CountingOnlyModule.super.lineCount();
  }

  @Override
  public void popTransactionBundle() {
    CountingOnlyModule.super.popTransactionBundle();
    transactionBundleContainsIllegalOperation(false);
  }
}
