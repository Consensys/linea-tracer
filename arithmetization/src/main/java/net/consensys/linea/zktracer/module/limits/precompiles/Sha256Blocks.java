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

import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.container.module.CountingOnlyModule;
import net.consensys.linea.zktracer.container.stacked.CountOnlyOperation;

@Getter
@Accessors(fluent = true)
public final class Sha256Blocks implements CountingOnlyModule {
  private final CountOnlyOperation counts = new CountOnlyOperation();
  private static final int PRECOMPILE_BASE_GAS_FEE = 60;
  private static final int PRECOMPILE_GAS_FEE_PER_EWORD = 12;
  private static final int SHA256_BLOCKSIZE = 64 * 8;
  // The length of the data to be hashed is 2**64 maximum.
  private static final int SHA256_PADDING_LENGTH = 64;
  private static final int SHA256_NB_PADDED_ONE = 1;

  @Override
  public String moduleKey() {
    return "PRECOMPILE_SHA2_BLOCKS";
  }

  @Override
  public void addPrecompileLimit(final int count) {
    final int blockCount = numberOfSha256Blocks(count);
    counts.add(blockCount);
  }

  private static int numberOfSha256Blocks(final int dataByteLength) {
    return (dataByteLength * 8
            + SHA256_NB_PADDED_ONE
            + SHA256_PADDING_LENGTH
            + (SHA256_BLOCKSIZE - 1))
        / SHA256_BLOCKSIZE;
  }
}
