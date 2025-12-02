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

package net.consensys.linea.osakaReplayTests;

import static net.consensys.linea.ReplayTestTools.replay;
import static net.consensys.linea.zktracer.ChainConfig.SEPOLIA_TESTCONFIG;
import static net.consensys.linea.zktracer.Fork.OSAKA;

import net.consensys.linea.UnitTestWatcher;
import net.consensys.linea.reporting.TracerTestBase;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

@Tag("replay")
@ExtendWith(UnitTestWatcher.class)
public class SepoliaReplayTests extends TracerTestBase {

  @Test
  void block_21511543(TestInfo testInfo) {
    replay(SEPOLIA_TESTCONFIG(OSAKA), "osaka/21511543.sepolia.json.gz", testInfo);
  }

  @Test
  void block_21511544(TestInfo testInfo) {
    replay(SEPOLIA_TESTCONFIG(OSAKA), "osaka/21511544.sepolia.json.gz", testInfo);
  }

  @Test
  void block_21511580(TestInfo testInfo) {
    replay(SEPOLIA_TESTCONFIG(OSAKA), "osaka/21511580.sepolia.json.gz", testInfo);
  }

  @Test
  void block_21511584(TestInfo testInfo) {
    replay(SEPOLIA_TESTCONFIG(OSAKA), "osaka/21511584.sepolia.json.gz", testInfo);
  }

  @Test
  void block_21511612(TestInfo testInfo) {
    replay(SEPOLIA_TESTCONFIG(OSAKA), "osaka/21511612.sepolia.json.gz", testInfo);
  }

  @Test
  void block_21511617(TestInfo testInfo) {
    replay(SEPOLIA_TESTCONFIG(OSAKA), "osaka/21511617.sepolia.json.gz", testInfo);
  }

  @Test
  void block_21511648(TestInfo testInfo) {
    replay(SEPOLIA_TESTCONFIG(OSAKA), "osaka/21511648.sepolia.json.gz", testInfo);
  }

  @Test
  void block_21511745(TestInfo testInfo) {
    replay(SEPOLIA_TESTCONFIG(OSAKA), "osaka/21511745.sepolia.json.gz", testInfo);
  }

  @Test
  void block_21511792(TestInfo testInfo) {
    replay(SEPOLIA_TESTCONFIG(OSAKA), "osaka/21511792.sepolia.json.gz", testInfo);
  }

  @Test
  void block_21511798(TestInfo testInfo) {
    replay(SEPOLIA_TESTCONFIG(OSAKA), "osaka/21511798.sepolia.json.gz", testInfo);
  }

  @Test
  void block_21511806(TestInfo testInfo) {
    replay(SEPOLIA_TESTCONFIG(OSAKA), "osaka/21511806.sepolia.json.gz", testInfo);
  }

  @Test
  void block_21511810(TestInfo testInfo) {
    replay(SEPOLIA_TESTCONFIG(OSAKA), "osaka/21511810.sepolia.json.gz", testInfo);
  }

  @Test
  void block_21511877(TestInfo testInfo) {
    replay(SEPOLIA_TESTCONFIG(OSAKA), "osaka/21511877.sepolia.json.gz", testInfo);
  }

  @Test
  void block_21511882(TestInfo testInfo) {
    replay(SEPOLIA_TESTCONFIG(OSAKA), "osaka/21511882.sepolia.json.gz", testInfo);
  }

  @Test
  void block_21511923(TestInfo testInfo) {
    replay(SEPOLIA_TESTCONFIG(OSAKA), "osaka/21511923.sepolia.json.gz", testInfo);
  }

  @Test
  void block_21511928(TestInfo testInfo) {
    replay(SEPOLIA_TESTCONFIG(OSAKA), "osaka/21511928.sepolia.json.gz", testInfo);
  }

  @Test
  void block_21511947(TestInfo testInfo) {
    replay(SEPOLIA_TESTCONFIG(OSAKA), "osaka/21511947.sepolia.json.gz", testInfo);
  }

  @Test
  void block_21511973(TestInfo testInfo) {
    replay(SEPOLIA_TESTCONFIG(OSAKA), "osaka/21511973.sepolia.json.gz", testInfo);
  }

  @Test
  void block_21511975(TestInfo testInfo) {
    replay(SEPOLIA_TESTCONFIG(OSAKA), "osaka/21511975.sepolia.json.gz", testInfo);
  }

  @Test
  void block_21511976(TestInfo testInfo) {
    replay(SEPOLIA_TESTCONFIG(OSAKA), "osaka/21511976.sepolia.json.gz", testInfo);
  }

  @Test
  void block_21511985(TestInfo testInfo) {
    replay(SEPOLIA_TESTCONFIG(OSAKA), "osaka/21511985.sepolia.json.gz", testInfo);
  }

  @Test
  void block_21512014(TestInfo testInfo) {
    replay(SEPOLIA_TESTCONFIG(OSAKA), "osaka/21512014.sepolia.json.gz", testInfo);
  }

  @Test
  void block_21512018(TestInfo testInfo) {
    replay(SEPOLIA_TESTCONFIG(OSAKA), "osaka/21512018.sepolia.json.gz", testInfo);
  }

  @Test
  void block_21512024(TestInfo testInfo) {
    replay(SEPOLIA_TESTCONFIG(OSAKA), "osaka/21512024.sepolia.json.gz", testInfo);
  }
}
