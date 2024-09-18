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

package net.consensys.linea.zktracer;

import static net.consensys.linea.testing.ReplayExecutionEnvironment.LINEA_MAINNET;
import static net.consensys.linea.zktracer.ReplayTests.replay;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("replay")
// @Tag("nightly")
public class Issue1211TestslookuplogDataIntoRlprcpt {

  @Test
  void test_1() {
    replay(LINEA_MAINNET, "2435888-2435890.json.gz");
  }

  @Test
  void test_2() {
    replay(LINEA_MAINNET, "2435890-2435895.json.gz");
  }

  @Test
  void test_3() {
    replay(LINEA_MAINNET, "2435895-2435900.json.gz");
  }

  @Test
  void test_4() {
    replay(LINEA_MAINNET, "2435900-2435905.json.gz");
  }

  @Test
  void test_5() {
    replay(LINEA_MAINNET, "2435905-2435910.json.gz");
  }

  @Test
  void test_6() {
    replay(LINEA_MAINNET, "2435910-2435915.json.gz");
  }

  @Test
  void test_7() {
    replay(LINEA_MAINNET, "2435915-2435920.json.gz");
  }

  @Test
  void test_8() {
    replay(LINEA_MAINNET, "2435920-2435925.json.gz");
  }
}
