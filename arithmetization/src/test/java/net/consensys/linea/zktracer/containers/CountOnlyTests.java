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

import net.consensys.linea.zktracer.container.stacked.CountOnlyOperation;
import org.junit.jupiter.api.Test;

public class CountOnlyTests {
  @Test
  void testAddedToFront() {
    final CountOnlyOperation state = new CountOnlyOperation();

    state.enter();
    state.add(1);
    assertThat(state.lineCount()).isEqualTo(1);

    state.enter();
    state.add(3);
    assertThat(state.lineCount()).isEqualTo(4);

    state.pop();
    assertThat(state.lineCount()).isEqualTo(1);

    state.enter();
    assertThat(state.lineCount()).isEqualTo(1);

    state.add(2);
    state.add(2);
    assertThat(state.lineCount()).isEqualTo(5);

    state.pop();
    assertThat(state.lineCount()).isEqualTo(1);

    state.enter();
    state.add(0);
    assertThat(state.lineCount()).isEqualTo(1);
  }
}
