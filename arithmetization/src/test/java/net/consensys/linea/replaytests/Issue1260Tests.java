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

package net.consensys.linea.replaytests;

import static net.consensys.linea.replaytests.ReplayTestTools.replay;
import static net.consensys.linea.testing.ReplayExecutionEnvironment.LINEA_MAINNET;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("nightly")
@Tag("replay")
public class Issue1260Tests {
  @Test
  void one() {
    replay(LINEA_MAINNET, "5081887.json.gz");
  }

  @Test
  void two() {
    replay(LINEA_MAINNET, "5081888.json.gz");
  }

  @Test
  void three() {
    replay(LINEA_MAINNET, "5081889.json.gz");
  }

  @Test
  void four() {
    replay(LINEA_MAINNET, "5081890.json.gz");
  }

  @Test
  void five() {
    replay(LINEA_MAINNET, "5081891.json.gz");
  }

  @Test
  void six() {
    replay(LINEA_MAINNET, "5081892.json.gz");
  }

  @Test
  void seven() {
    replay(LINEA_MAINNET, "5081893.json.gz");
  }

  @Test
  void eight() {
    replay(LINEA_MAINNET, "5081894.json.gz");
  }

  @Test
  void all() {
    replay(LINEA_MAINNET, "5081887-5081894.json.gz");
  }
}
