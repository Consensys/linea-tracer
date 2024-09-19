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
package net.consensys.linea.replaytests;

import static net.consensys.linea.replaytests.ReplayTestTools.replay;
import static net.consensys.linea.testing.ReplayExecutionEnvironment.LINEA_MAINNET;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("replay")
public class Issue1090Tests {

  /**
   * This is an interesting block: at transaction 13 (which is a CONTRACT_CREATION transaction) the
   * address being deployed at the ROOT does a DELEGATECALL to 0xa092..., which then CALLs its
   * CALLER address twice. The CALLER, being under deployment, is seen as having empty bytecode by
   * the outside world, in particular by 0xa092... .
   */
  @Test
  void issue_1090_block_1507291() {
    replay(LINEA_MAINNET, "1507291-1507291.json.gz");
  }

  @Tag("nightly")
  @Test
  void issue_1090_block_1809818() {
    replay(LINEA_MAINNET, "1809818-1809818.json.gz");
  }

  @Tag("nightly")
  @Test
  void issue_1090_block_1812784() {
    replay(LINEA_MAINNET, "1812784-1812784.json.gz");
  }

  @Tag("nightly")
  @Test
  void issue_1090_block_1814860() {
    replay(LINEA_MAINNET, "1814860-1814860.json.gz");
  }

  @Tag("nightly")
  @Test
  void issue_1090_block_1714851() {
    replay(LINEA_MAINNET, "1714851-1714851.json.gz");
  }

  @Tag("nightly")
  @Test
  void issue_1090_block_1505729() {
    replay(LINEA_MAINNET, "1505729-1505729.json.gz");
  }

  @Tag("nightly")
  @Test
  void issue_1090_block_1511808() {
    replay(LINEA_MAINNET, "1511808-1511808.json.gz");
  }

  @Tag("nightly")
  @Test
  void issue_1090_block_1400040() {
    replay(LINEA_MAINNET, "1400040-1400040.json.gz");
  }

  @Tag("nightly")
  @Test
  void issue_1090_block_1409462() {
    replay(LINEA_MAINNET, "1409462-1409462.json.gz");
  }

  @Tag("nightly")
  @Test
  void issue_1090_block_1410650() {
    replay(LINEA_MAINNET, "1410650-1410650.json.gz");
  }

  @Tag("nightly")
  @Test
  void issue_1090_block_1204298() {
    replay(LINEA_MAINNET, "1204298-1204298.json.gz");
  }

  @Tag("nightly")
  @Test
  void issue_1090_block_1213822() {
    replay(LINEA_MAINNET, "1213822-1213822.json.gz");
  }

  @Tag("nightly")
  @Test
  void issue_1090_block_1214117() {
    replay(LINEA_MAINNET, "1214117-1214117.json.gz");
  }

  @Tag("nightly")
  @Test
  void issue_1090_block_1214259() {
    replay(LINEA_MAINNET, "1214259-1214259.json.gz");
  }

  @Tag("nightly")
  @Test
  void issue_1090_block_1214280() {
    replay(LINEA_MAINNET, "1214280-1214280.json.gz");
  }

  @Tag("nightly")
  @Test
  void issue_1090_block_1214528() {
    replay(LINEA_MAINNET, "1214528-1214528.json.gz");
  }

  @Tag("nightly")
  @Test
  void issue_1090_block_1100579() {
    replay(LINEA_MAINNET, "1100579-1100579.json.gz");
  }

  @Tag("nightly")
  @Test
  void issue_1090_block_1104982() {
    replay(LINEA_MAINNET, "1104982-1104982.json.gz");
  }

  @Tag("nightly")
  @Test
  void issue_1090_block_1105022() {
    replay(LINEA_MAINNET, "1105022-1105022.json.gz");
  }

  @Tag("nightly")
  @Test
  void issue_1090_block_1105029() {
    replay(LINEA_MAINNET, "1105029-1105029.json.gz");
  }

  @Tag("nightly")
  @Test
  void issue_1090_block_1105038() {
    replay(LINEA_MAINNET, "1105038-1105038.json.gz");
  }

  @Tag("nightly")
  @Test
  void issue_1090_block_1106506() {
    replay(LINEA_MAINNET, "1106506-1106506.json.gz");
  }

  @Tag("nightly")
  @Test
  void issue_1090_block_1106648() {
    replay(LINEA_MAINNET, "1106648-1106648.json.gz");
  }

  @Tag("nightly")
  @Test
  void issue_1090_block_1107867() {
    replay(LINEA_MAINNET, "1107867-1107867.json.gz");
  }

  @Tag("nightly")
  @Test
  void issue_1090_block_1002387() {
    replay(LINEA_MAINNET, "1002387-1002387.json.gz");
  }

  @Tag("nightly")
  @Test
  void issue_1090_block_1003970() {
    replay(LINEA_MAINNET, "1003970-1003970.json.gz");
  }

  @Tag("nightly")
  @Test
  void issue_1090_block_1010069() {
    replay(LINEA_MAINNET, "1010069-1010069.json.gz");
  }
}
