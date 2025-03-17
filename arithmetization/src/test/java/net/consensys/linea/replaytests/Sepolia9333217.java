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
import static net.consensys.linea.zktracer.ChainConfig.OLD_SEPOLIA_TESTCONFIG;

import net.consensys.linea.UnitTestWatcher;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@Tag("replay")
@Tag("weekly")
@ExtendWith(UnitTestWatcher.class)
public class Sepolia9333217 {

  /**
   * This Sepolia block is unprovable due to the modexp call. One transaction does two calls to
   * modexp with arg > 512 bytes. This is unprovable. It was sequenced as there was a line count
   * issue for modexp.
   */
  @Test
  void sepolia9333217() {
    try {
      replay(OLD_SEPOLIA_TESTCONFIG, "9333217.sepolia.json.gz");
    } catch (Exception e) {
      // This is expected as the modexp call is unprovable
      if (!e.getMessage().contains("Final CallScenario, CALL_PRC_UNDEFINED, is still undefined")) {
        throw e;
      }
    }
  }
}
