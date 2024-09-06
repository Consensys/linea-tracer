/*
 * Copyright ConsenSys Inc.
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

package net.consensys.linea.zktracer.containers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import net.consensys.linea.zktracer.ZkTracer;
import net.consensys.linea.zktracer.container.module.CountingOnlyModule;
import net.consensys.linea.zktracer.module.limits.precompiles.EcAddEffectiveCall;
import net.consensys.linea.zktracer.module.limits.precompiles.ModexpEffectiveCall;
import org.junit.jupiter.api.Test;

public class CountOnlyModuleTest {
  @Test
  void test(){
    ZkTracer state = new ZkTracer();
    final ModexpEffectiveCall countingOnlyModule = state.getHub().modexpEffectiveCall();

    countingOnlyModule.enterTransaction();
    countingOnlyModule.addPrecompileLimit(1);
    assertThat(countingOnlyModule.lineCount()).isEqualTo(1);

    countingOnlyModule.enterTransaction();
    countingOnlyModule.addPrecompileLimit(1);
    assertThat(countingOnlyModule.lineCount()).isEqualTo(2);

    countingOnlyModule.popTransaction();
    assertThat(countingOnlyModule.lineCount()).isEqualTo(1);

    state = new ZkTracer();
    assertThat(state.getHub().modexpEffectiveCall().lineCount()).isEqualTo(0);
  }
}
