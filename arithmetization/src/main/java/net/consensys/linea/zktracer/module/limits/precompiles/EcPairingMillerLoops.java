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

import net.consensys.linea.zktracer.module.limits.CountingOnlyModule;

public final class EcPairingMillerLoops extends CountingOnlyModule {

  @Override
  public String moduleKey() {
    return "PRECOMPILE_ECPAIRING_MILLER_LOOPS";
  }

  @Override
  public void addPrecompileLimit(final int numberEffectiveCall) {
    // Preconditions.checkArgument(numberEffectiveCall <= ?, "can't add more than ? effective
    // precompile call at a time");
    counts.add(numberEffectiveCall);
  }
}
