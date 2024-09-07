/*
 * Copyright Consensys Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License", false); you may not use this file except in compliance with
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

import static net.consensys.linea.zktracer.ReplayTests.replay;

import org.junit.jupiter.api.Test;

public class Issue1126Tests {
  @Test
  void test_3108622_3108633() {
    replay("3108622-3108633.json.gz", false);
  }

  @Test
  void test_3175608_3175636() {
    replay("3175608-3175636.json.gz", false);
  }

  @Test
  void test_3290673_3290679() {
    replay("3290673-3290679.json.gz", false);
  }

  @Test
  void test_3290746_3290752() {
    replay("3290746-3290752.json.gz", false);
  }

  @Test
  void test_3315684_3315690() {
    replay("3315684-3315690.json.gz", false);
  }

  @Test
  void test_3374278_3374284() {
    replay("3374278-3374284.json.gz", false);
  }

  @Test
  void test_3385404_3385411() {
    replay("3385404-3385411.json.gz", false);
  }

  @Test
  void test_3410170_3410240() {
    replay("3410170-3410240.json.gz", false);
  }

  @Test
  void test_3423488_3423521() {
    replay("3423488-3423521.json.gz", false);
  }

  @Test
  void test_3424829_3424864() {
    replay("3424829-3424864.json.gz", false);
  }

  @Test
  void test_3429701_3429735() {
    replay("3429701-3429735.json.gz", false);
  }

  @Test
  void test_3431193_3431232() {
    replay("3431193-3431232.json.gz", false);
  }

  @Test
  void test_3431567_3431601() {
    replay("3431567-3431601.json.gz", false);
  }

  @Test
  void test_3432730_3432768() {
    replay("3432730-3432768.json.gz", false);
  }

  @Test
  void test_4323962_4324012() {
    replay("4323962-4324012.json.gz", false);
  }

  @Test
  void test_4343434_4343473() {
    replay("4343434-4343473.json.gz", false);
  }

  @Test
  void test_4392225_4392280() {
    replay("4392225-4392280.json.gz", false);
  }

  @Test
  void test_4477086_4477226() {
    replay("4477086-4477226.json.gz", false);
  }

  @Test
  void test_4519246_4519309() {
    replay("4519246-4519309.json.gz", false);
  }

  @Test
  void test_4556007_4556115() {
    replay("4556007-4556115.json.gz", false);
  }

  @Test
  void test_4583379_4583463() {
    replay("4583379-4583463.json.gz", false);
  }
}
