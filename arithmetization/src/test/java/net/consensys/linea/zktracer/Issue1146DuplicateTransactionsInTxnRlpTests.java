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

import org.junit.jupiter.api.Test;

public class Issue1146DuplicateTransactionsInTxnRlpTests {
  @Test
  public void issue_1146_block_7566651_7566655() {
    replay(LINEA_MAINNET, "7566651-7566655.json.gz", false);
  }

  @Test
  public void issue_1146_block_7566656_7566663() {
    replay(LINEA_MAINNET, "7566656-7566663.json.gz", false);
  }

  @Test
  public void issue_1146_block_7566664_7566669() {
    replay(LINEA_MAINNET, "7566664-7566669.json.gz", false);
  }

  @Test
  public void issue_1146_block_7566670_7566675() {
    replay(LINEA_MAINNET, "7566670-7566675.json.gz", false);
  }

  @Test
  public void issue_1146_block_7566676_7566681() {
    replay(LINEA_MAINNET, "7566676-7566681.json.gz", false);
  }

  @Test
  public void issue_1146_block_7566682_7566688() {
    replay(LINEA_MAINNET, "7566682-7566688.json.gz", false);
  }

  @Test
  public void issue_1146_block_7566689_7566692() {
    replay(LINEA_MAINNET, "7566689-7566692.json.gz", false);
  }

  @Test
  public void issue_1146_block_7566693_7566700() {
    replay(LINEA_MAINNET, "7566693-7566700.json.gz", false);
  }

  @Test
  public void issue_1146_block_7566701_7566707() {
    replay(LINEA_MAINNET, "7566701-7566707.json.gz", false);
  }

  @Test
  public void issue_1146_block_7566708_7566714() {
    replay(LINEA_MAINNET, "7566708-7566714.json.gz", false);
  }

  @Test
  public void issue_1146_block_7566715_7566721() {
    replay(LINEA_MAINNET, "7566715-7566721.json.gz", false);
  }

  @Test
  public void issue_1146_block_7566722_7566727() {
    replay(LINEA_MAINNET, "7566722-7566727.json.gz", false);
  }

  @Test
  public void issue_1146_block_7566728_7566732() {
    replay(LINEA_MAINNET, "7566728-7566732.json.gz", false);
  }

  @Test
  public void issue_1146_block_7566733_7566735() {
    replay(LINEA_MAINNET, "7566733-7566735.json.gz", false);
  }
}
