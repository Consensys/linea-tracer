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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class LeosNewRanges {
  @Test
  void leos_new_ranges_2258472_2258607() {
    replay(LINEA_MAINNET, "2258472-2258607.json.gz", false);
  }

  @Test
  void leos_new_ranges_2291967_2292180() {
    replay(LINEA_MAINNET, "2291967-2292180.json.gz", false);
  }

  @Test
  void leos_new_ranges_2321460_2321556() {
    replay(LINEA_MAINNET, "2321460-2321556.json.gz", false);
  }

  @Test
  void leos_new_ranges_2359782_2359913() {
    replay(LINEA_MAINNET, "2359782-2359913.json.gz", false);
  }

  @Test
  void leos_new_ranges_2362189_2362291() {
    replay(LINEA_MAINNET, "2362189-2362291.json.gz", false);
  }

  @Test
  void leos_new_ranges_5002125_5002158() {
    replay(LINEA_MAINNET, "5002125-5002158.json.gz", false);
  }

  @Test
  void leos_new_ranges_5004016_5004055() {
    replay(LINEA_MAINNET, "5004016-5004055.json.gz", false);
  }

  @Test
  void leos_new_ranges_5004767_5004806() {
    replay(LINEA_MAINNET, "5004767-5004806.json.gz", false);
  }

  @Test
  void leos_new_ranges_5006057_5006092() {
    replay(LINEA_MAINNET, "5006057-5006092.json.gz", false);
  }

  @Test
  void leos_new_ranges_5006988_5007039() {
    replay(LINEA_MAINNET, "5006988-5007039.json.gz", false);
  }

  @Test
  void leos_new_ranges_5012236_5012275() {
    replay(LINEA_MAINNET, "5012236-5012275.json.gz", false);
  }

  @Test
  void leos_new_ranges_5025817_5025859() {
    replay(LINEA_MAINNET, "5025817-5025859.json.gz", false);
  }

  @Test
  void leos_new_ranges_5037583_5037608() {
    replay(LINEA_MAINNET, "5037583-5037608.json.gz", false);
  }

  @Test
  void leos_new_ranges_5042942_5042990() {
    replay(LINEA_MAINNET, "5042942-5042990.json.gz", false);
  }

  @Test
  void leos_new_ranges_5043442_5043497() {
    replay(LINEA_MAINNET, "5043442-5043497.json.gz", false);
  }

  @Test
  void leos_new_ranges_5043997_5044049() {
    replay(LINEA_MAINNET, "5043997-5044049.json.gz", false);
  }

  @Test
  void leos_new_ranges_5044557_5044619() {
    replay(LINEA_MAINNET, "5044557-5044619.json.gz", false);
  }

  @Test
  void leos_new_ranges_5045161_5045232() {
    replay(LINEA_MAINNET, "5045161-5045232.json.gz", false);
  }

  @Test
  void leos_new_ranges_5046373_5046435() {
    replay(LINEA_MAINNET, "5046373-5046435.json.gz", false);
  }

  @Test
  void leos_new_ranges_5046997_5047058() {
    replay(LINEA_MAINNET, "5046997-5047058.json.gz", false);
  }

  @Test
  void leos_new_ranges_5050036_5050130() {
    replay(LINEA_MAINNET, "5050036-5050130.json.gz", false);
  }

  @Test
  void leos_new_ranges_5057558_5057616() {
    replay(LINEA_MAINNET, "5057558-5057616.json.gz", false);
  }
}
