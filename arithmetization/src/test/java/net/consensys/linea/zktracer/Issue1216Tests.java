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

import static net.consensys.linea.testing.ReplayExecutionEnvironment.LINEA_SEPOLIA;
import static net.consensys.linea.zktracer.ReplayTests.replay;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class Issue1216Tests {

  @Tag("nightly")
  @Tag("replay")
  @Test
  void issue_1216_sepolia_block_2392659() {
    replay(LINEA_SEPOLIA, "2392659.json.gz", false);
  }
}
