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
package net.consensys.linea.osakaReplayTests;

import static net.consensys.linea.ReplayTestTools.replay;
import static net.consensys.linea.zktracer.ChainConfig.MAINNET_TESTCONFIG;
import static net.consensys.linea.zktracer.ChainConfig.SEPOLIA_TESTCONFIG;
import static net.consensys.linea.zktracer.Fork.OSAKA;

import net.consensys.linea.UnitTestWatcher;
import net.consensys.linea.reporting.TracerTestBase;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

@Tag("replay")
@ExtendWith(UnitTestWatcher.class)
public class FastReplayTests extends TracerTestBase {
  @Test
  @Disabled
  void fatMxp(TestInfo testInfo) {
    // outcome does not match expected outcome (expected true, was false)?
    replay(MAINNET_TESTCONFIG(OSAKA, false), "legacy/2492975-2492977.mainnet.json.gz", testInfo);
  }

  @Test
  void failingMmuModexp(TestInfo testInfo) {
    // row 7 of column txndata.CT_MAX is out-of-bounds (16) (AIR)
    replay(MAINNET_TESTCONFIG(OSAKA, false), "osaka/5995162.mainnet.json.gz", testInfo);
  }

  @Test
  void failRlpAddress(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(OSAKA, false), "osaka/5995097.mainnet.json.gz", testInfo);
  }

  @Test
  void rlprcptManyTopicsWoLogData(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(OSAKA, false), "osaka/6569423.mainnet.json.gz", testInfo);
  }

  @Test
  @Disabled
  void multipleFailingCallToEcrecover(TestInfo testInfo) {
    // Contains tx that exceeds tx gas limit
    replay(MAINNET_TESTCONFIG(OSAKA, false), "osaka/5000544.mainnet.json.gz", testInfo, false);
  }

  @Test
  void incident777zkGethMainnet(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(OSAKA, false), "osaka/7461019-7461030.mainnet.json.gz", testInfo);
  }

  @Test
  void issue1006(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(OSAKA, false), "osaka/6032696-6032699.mainnet.json.gz", testInfo);
  }

  @Test
  void issue1004(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(OSAKA, false), "osaka/6020023-6020029.mainnet.json.gz", testInfo);
  }

  @Test
  void block_6110045(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(OSAKA, false), "osaka/6110045.mainnet.json.gz", testInfo);
  }

  @Test
  void failingCreate2(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(OSAKA, false), "legacy/2250197.mainnet.json.gz", testInfo, false);
  }

  // @Disabled("Fails to create the ConflationSnapshot from the gson file")
  @Test
  void blockHash1(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(OSAKA, false), "legacy/8718090.mainnet.json.gz", testInfo, false);
  }

  // @Disabled("Fails to create the ConflationSnapshot from the gson file")
  @Test
  void blockHash2(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(OSAKA, false), "legacy/8718330.mainnet.json.gz", testInfo, false);
  }

  @Test
  void negativeNumberOfMmioInstruction(TestInfo testInfo) {
    replay(
        MAINNET_TESTCONFIG(OSAKA, false),
        "legacy/6029454-6029459.mainnet.json.gz",
        testInfo,
        false);
  }

  @Test
  void simpleSelfDestruct(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(OSAKA, false), "legacy/50020-50029.mainnet.json.gz", testInfo, false);
  }

  @Test
  void failedCreate2(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(OSAKA, false), "legacy/41640-41649.mainnet.json.gz", testInfo, false);
  }

  @Test
  void largeInitCode(TestInfo testInfo) {
    replay(SEPOLIA_TESTCONFIG(OSAKA, false), "legacy/3318494.sepolia.json.gz", testInfo, false);
  }

  @Test
  @Disabled
  void hotOrColdPrecompile(TestInfo testInfo) {
    // constraint failure RELATIVE_USER_TXN_NUMBER_MAX
    replay(
        MAINNET_TESTCONFIG(OSAKA, false),
        "legacy/2019510-2019519.mainnet.json.gz",
        testInfo,
        false);
  }

  @Test
  void callDataCopyCnNotFound(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(OSAKA, false), "legacy/67050-67059.mainnet.json.gz", testInfo, false);
  }

  @Test
  @Disabled
  void returnOogxForCodeDepositCost(TestInfo testInfo) {
    // constraint failure RELATIVE_USER_TXN_NUMBER_MAX
    replay(MAINNET_TESTCONFIG(OSAKA, false), "legacy/1002387.mainnet.json.gz", testInfo, false);
  }

  @Test
  void modexpTriggeringNonAlignedFirstLimbSingleSourceMmuModexp(TestInfo testInfo) {
    replay(
        MAINNET_TESTCONFIG(OSAKA, false),
        "legacy/3108622-3108633.mainnet.json.gz",
        testInfo,
        false);
  }

  @Test
  void mainnet1339346ContextRevertTwice(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(OSAKA, false), "legacy/1339346.mainnet.json.gz", testInfo, false);
  }

  @Test
  void legacyTxWithoutChainID(TestInfo testInfo) {
    replay(SEPOLIA_TESTCONFIG(OSAKA, false), "legacy/254251.sepolia.json.gz", testInfo, false);
  }

  @Test
  void incorrectCreationCapture(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(OSAKA, false), "legacy/4323985.mainnet.json.gz", testInfo, false);
  }

  @Test
  @Disabled
  void duplicateSubZero(TestInfo testInfo) {
    // constraint failure RELATIVE_USER_TXN_NUMBER_MAX
    replay(
        MAINNET_TESTCONFIG(OSAKA, false),
        "legacy/20197061-20197173.mainnet.json.gz",
        testInfo,
        false);
  }

  @Test
  void stateManagerIntegrationTest(TestInfo testInfo) {
    replay(MAINNET_TESTCONFIG(OSAKA, false), "legacy/SSTOREX_on_mainnet.json.gz", testInfo, false);
  }
}
