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
package net.consensys.linea.pragueReplayTests;

import static net.consensys.linea.ReplayTestTools.replay;
import static net.consensys.linea.zktracer.ChainConfig.MAINNET_TESTCONFIG;
import static net.consensys.linea.zktracer.Fork.PRAGUE;

import net.consensys.linea.UnitTestWatcher;
import net.consensys.linea.reporting.TracerTestBase;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

@Tag("nightly")
@Tag("replay")
@ExtendWith(UnitTestWatcher.class)
public class NightlyReplayTests extends TracerTestBase {

  // ============================================================================
  // Blocks 25080040 -- 25080104
  // ============================================================================

  @Test
  void block_25080040_25080044(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25080040-25080044.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25080045_25080049(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25080045-25080049.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25080050_25080054(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25080050-25080054.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25080055_25080059(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25080055-25080059.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25080060_25080064(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25080060-25080064.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25080065_25080069(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25080065-25080069.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25080070_25080074(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25080070-25080074.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25080075_25080079(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25080075-25080079.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25080080_25080084(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25080080-25080084.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25080085_25080089(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25080085-25080089.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25080090_25080094(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25080090-25080094.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25080095_25080099(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25080095-25080099.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25080100_25080104(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25080100-25080104.mainnet.prague.json.gz", testInfo);
  }

  // ==========================================================================
  // Blocks 25091000 -- 25091099
  // ==========================================================================

  @Test
  void block_25091000_25091004(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25091000-25091004.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25091005_25091009(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25091005-25091009.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25091010_25091014(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25091010-25091014.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25091015_25091019(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25091015-25091019.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25091020_25091024(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25091020-25091024.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25091025_25091029(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25091025-25091029.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25091030_25091034(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25091030-25091034.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25091035_25091039(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25091035-25091039.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25091040_25091044(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25091040-25091044.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25091045_25091049(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25091045-25091049.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25091050_25091054(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25091050-25091054.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25091055_25091059(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25091055-25091059.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25091060_25091064(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25091060-25091064.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25091065_25091069(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25091065-25091069.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25091070_25091074(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25091070-25091074.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25091075_25091079(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25091075-25091079.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25091080_25091084(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25091080-25091084.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25091085_25091089(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25091085-25091089.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25091090_25091094(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25091090-25091094.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25091095_25091099(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25091095-25091099.mainnet.prague.json.gz", testInfo);
  }

  // ==========================================================================
  // Blocks 25098000 -- 25098099
  // ==========================================================================

  @Test
  void block_25098000_25098004(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25098000-25098004.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25098005_25098009(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25098005-25098009.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25098010_25098014(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25098010-25098014.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25098015_25098019(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25098015-25098019.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25098020_25098024(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25098020-25098024.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25098025_25098029(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25098025-25098029.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25098030_25098034(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25098030-25098034.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25098035_25098039(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25098035-25098039.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25098040_25098044(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25098040-25098044.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25098045_25098049(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25098045-25098049.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25098050_25098054(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25098050-25098054.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25098055_25098059(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25098055-25098059.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25098060_25098064(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25098060-25098064.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25098065_25098069(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25098065-25098069.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25098070_25098074(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25098070-25098074.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25098075_25098079(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25098075-25098079.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25098080_25098084(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25098080-25098084.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25098085_25098089(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25098085-25098089.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25098090_25098094(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25098090-25098094.mainnet.prague.json.gz", testInfo);
  }

  @Test
  void block_25098095_25098099(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(PRAGUE), "prague/25098095-25098099.mainnet.prague.json.gz", testInfo);
  }
}
