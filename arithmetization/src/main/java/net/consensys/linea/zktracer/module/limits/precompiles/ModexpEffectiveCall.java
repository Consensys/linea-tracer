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

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.zktracer.container.module.CountingOnlyModule;
import net.consensys.linea.zktracer.container.stacked.CountOnlyOperation;

@Getter
@Slf4j
@RequiredArgsConstructor
@Accessors(fluent = true)
public class ModexpEffectiveCall implements CountingOnlyModule {
  private final CountOnlyOperation counts = new CountOnlyOperation();

  @Override
  public String moduleKey() {
    return "PRECOMPILE_MODEXP_EFFECTIVE_CALLS";
  }

  @Override
  public void addPrecompileLimit(final int count) {
    Preconditions.checkArgument(
        count == 1, "can't add more than one effective precompile call at a time");
    counts.add(count);
  }
}
