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
package net.consensys.linea.zktracer;

import static net.consensys.linea.testing.ReplayExecutionEnvironment.LINEA_MAINNET;
import static net.consensys.linea.zktracer.ReplayTests.replay;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/** Same underlying NPE issue as that solved in #1216. */
@Tag("nightly")
public class Issue1123Tests {

  @Test
  void issue_1123_mainnet_block_8043758() {
    replay(LINEA_MAINNET, "8043758-8043758.json.gz");
  }

  @Test
  void issue_1123_mainnet_block_8019521() {
    replay(LINEA_MAINNET, "8019521-8019521.json.gz");
  }

  @Test
  void issue_1123_mainnet_block_8005327() {
    replay(LINEA_MAINNET, "8005327-8005327.json.gz");
  }
}
