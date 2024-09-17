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
import static net.consensys.linea.testing.ReplayExecutionEnvironment.LINEA_SEPOLIA;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("replay")
public class ReplayTests {
  @Test
  void traceTxStartNotTheSameAsTxPrepare() {
    replay(LINEA_MAINNET, "start-vs-prepare-tx.json.gz");
  }

  @Test
  void fatMxp() {
    replay(LINEA_MAINNET, "2492975-2492977.json.gz");
  }

  /**
   * bulk-replay of multiple replay files specified by a directory. The conflated traces will be
   * moved to "conflated" directory once replayed. The replay files will be moved to "replayed"
   * directory once completed. Note: CORSET_VALIDATOR.validate() is disabled by default for
   * bulkReplay. Usage: bulkReplay("/path/to/your/directory");
   */
  @Test
  void bulkReplay() {
    // bulkReplay("./src/test/resources/replays");
    // bulkReplay(LINEA_MAINNET, "");
  }

  @Test
  void leoFailingRange() {
    replay(LINEA_MAINNET, "5389571-5389577.json.gz");
  }

  // @Disabled
  @Test
  void failingMmuModexp() {
    replay(LINEA_MAINNET, "5995162.json.gz");
  }

  // @Disabled
  @Test
  void failRlpAddress() {
    replay(LINEA_MAINNET, "5995097.json.gz");
  }

  // @Disabled
  @Test
  void rlprcptManyTopicsWoLogData() {
    replay(LINEA_MAINNET, "6569423.json.gz", false);
  }

  // @Disabled
  @Test
  void multipleFailingCallToEcrecover() {
    replay(LINEA_MAINNET, "5000544.json.gz");
  }

  // @Disabled
  @Test
  void incident777zkGethMainnet() {
    replay(LINEA_MAINNET, "7461019-7461030.json.gz");
  }

  // @Disabled
  @Test
  void issue1006() {
    replay(LINEA_MAINNET, "6032696-6032699.json.gz");
  }

  // @Disabled
  @Test
  void issue1004() {
    replay(LINEA_MAINNET, "6020023-6020029.json.gz", false);
  }

  // @Disabled
  @Test
  void block_6110045() {
    // The purpose of this test is to check the mechanism for spotting divergence between the replay
    // tests and mainnet.  Specifically, this replay has transaction result information embedded
    // within it.
    replay(LINEA_MAINNET, "6110045.json.gz");
  }

  // @Disabled
  @Test
  void failingCreate2() {
    replay(LINEA_MAINNET, "2250197-2250197.json.gz");
  }

  // @Disabled
  @Test
  void blockHash1() {
    replay(LINEA_MAINNET, "8718090.json.gz");
  }

  // @Disabled
  @Test
  void blockHash2() {
    replay(LINEA_MAINNET, "8718330.json.gz");
  }

  // TODO: should be replaced by a unit test triggering AnyToRamWithPadding (mixed case) MMU
  // instruction

  // @Disabled
  @Test
  void negativeNumberOfMmioInstruction() {
    replay(LINEA_MAINNET, "6029454-6029459.json.gz");
  }

  @Test
  void simpleSelfDestruct() {
    replay(LINEA_MAINNET, "50020-50029.json.gz", false);
  }

  // TODO: should be replaced by a unit test triggering a failed CREATE2
  @Test
  void failedCreate2() {
    replay(LINEA_MAINNET, "41640-41649.json.gz");
  }

  @Disabled() // Unknown Problem
  @Test
  void largeInitCode() {
    replay(LINEA_SEPOLIA, "3318494.sepolia.json.gz");
  }

  /**
   * TODO: should be replace by a unit test triggering a STATICCALL to a precompile, without enough
   * remaining gas if the precompile was considered COLD
   */
  @Test
  void hotOrColdPrecompile() {
    replay(LINEA_MAINNET, "2019510-2019519.json.gz");
  }

  // TODO: should be replace by a unit test triggering a CALLDATACOPY in a ROOT context of a
  // deployment transaction
  @Test
  void callDataCopyCnNotFound() {
    replay(LINEA_MAINNET, "67050-67059.json.gz");
  }

  /**
   * TODO: should be replace by a unit test triggering a RETURN during a deployment transaction,
   * where we run OOG when need to pay the gas cost of the code deposit
   */
  @Test
  void returnOogxForCodeDepositCost() {
    replay(LINEA_MAINNET, "1002387.json.gz");
  }

  @Test
  void modexpTriggeringNonAlignedFirstLimbSingleSourceMmuModexp() {
    replay(LINEA_MAINNET, "3108622-3108633.json.gz");
  }

  /**
   * Not sure if we need to keep this replayTest. We were using a source offset instead of the dest
   * Offset to compute the memory expansion cost, thus creating a fake OOGX
   */
  @Test
  void mainnet1339346ContextRevertTwice() {
    replay(LINEA_MAINNET, "1339346.json.gz");
  }

  @Disabled("#1173")
  @Test
  void legacyTxWithoutChainID() {
    replay(LINEA_SEPOLIA, "254251.sepolia.json.gz");
  }
}
