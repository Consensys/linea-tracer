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

package net.consensys.linea.zktracer.statemanager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.consensys.linea.testing.MultiBlockExecutionEnvironment;
import net.consensys.linea.testing.TransactionProcessingResultValidator;
import net.consensys.linea.zktracer.module.hub.fragment.TraceFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import net.consensys.linea.zktracer.module.hub.section.TraceSection;
import org.hyperledger.besu.datatypes.Address;
import org.junit.jupiter.api.Test;

public class BlockwiseDeplNoTest {
  TestContext tc;

  @Test
  void testBlockwiseDeplNo() {
    // initialize the test context
    this.tc = new TestContext();
    this.tc.initializeTestContext();
    // prepare the transaction validator
    TransactionProcessingResultValidator resultValidator =
        new StateManagerTestValidator(
            tc.frameworkEntryPointAccount,
            // Creates, writes, reads and self-destructs generate 2 logs,
            // Reverted operations only have 1 log
            List.of(2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 1));
    // fetch the Hub metadata for the state manager maps
    // StateManagerMetadata stateManagerMetadata = Hub.stateManagerMetadata();

    // prepare a multi-block execution of transactions
    MultiBlockExecutionEnvironment.builder()
        // initialize accounts
        .accounts(
            List.of(
                tc.initialAccounts[0],
                tc.externallyOwnedAccounts[0],
                tc.initialAccounts[2],
                tc.frameworkEntryPointAccount))
        // Block 1
        .addBlock(
            List.of(
                tc.deployWithCreate2(
                    tc.externallyOwnedAccounts[0],
                    tc.keyPairs[0],
                    tc.frameworkEntryPointAddress,
                    tc.salts[0],
                    TestContext.snippetsCodeForCreate2,
                    false),
                tc.selfDestruct(
                    tc.externallyOwnedAccounts[0],
                    tc.keyPairs[0],
                    tc.newAddresses[0],
                    tc.frameworkEntryPointAddress,
                    false,
                    BigInteger.ONE),
                tc.deployWithCreate2(
                    tc.externallyOwnedAccounts[0],
                    tc.keyPairs[0],
                    tc.frameworkEntryPointAddress,
                    tc.salts[0],
                    TestContext.snippetsCodeForCreate2,
                    false),
                tc.selfDestruct(
                    tc.externallyOwnedAccounts[0],
                    tc.keyPairs[0],
                    tc.newAddresses[0],
                    tc.frameworkEntryPointAddress,
                    false,
                    BigInteger.ONE)))
        .addBlock(
            List.of(
                tc.deployWithCreate2(
                    tc.externallyOwnedAccounts[0],
                    tc.keyPairs[0],
                    tc.frameworkEntryPointAddress,
                    tc.salts[1],
                    TestContext.snippetsCodeForCreate2,
                    false),
                tc.selfDestruct(
                    tc.externallyOwnedAccounts[0],
                    tc.keyPairs[0],
                    tc.newAddresses[1],
                    tc.frameworkEntryPointAddress,
                    false,
                    BigInteger.ONE)))
        .addBlock(
            List.of( // test some reverted calls
                tc.deployWithCreate2(
                    tc.externallyOwnedAccounts[0],
                    tc.keyPairs[0],
                    tc.frameworkEntryPointAddress,
                    tc.salts[2],
                    TestContext.snippetsCodeForCreate2,
                    false),
                tc.selfDestruct(
                    tc.externallyOwnedAccounts[0],
                    tc.keyPairs[0],
                    tc.newAddresses[2],
                    tc.frameworkEntryPointAddress,
                    false,
                    BigInteger.ONE),
                tc.deployWithCreate2(
                    tc.externallyOwnedAccounts[0],
                    tc.keyPairs[0],
                    tc.frameworkEntryPointAddress,
                    tc.salts[2],
                    TestContext.snippetsCodeForCreate2,
                    true),
                tc.deployWithCreate2(
                    tc.externallyOwnedAccounts[0],
                    tc.keyPairs[0],
                    tc.frameworkEntryPointAddress,
                    tc.salts[2],
                    TestContext.snippetsCodeForCreate2,
                    false),
                tc.selfDestruct(
                    tc.externallyOwnedAccounts[0],
                    tc.keyPairs[0],
                    tc.newAddresses[2],
                    tc.frameworkEntryPointAddress,
                    true,
                    BigInteger.ONE)
                // since the last self-destruct gets reverted, the last call will not increase the
                // deplNo
                ))
        .transactionProcessingResultValidator(resultValidator)
        .build()
        .run();

    MultiBlockExecutionEnvironment.getHub();

    Map<AddrBlockPair, Integer> minDeplNoBlock = new HashMap<>();
    Map<AddrBlockPair, Integer> maxDeplNoBlock = new HashMap<>();

    int txCount = MultiBlockExecutionEnvironment.getHub().state().txCount();

    for (int txNb = 0; txNb < txCount; txNb++) {
      List<TraceSection> traceSectionList =
          MultiBlockExecutionEnvironment.getHub()
              .state()
              .getState()
              .operationsInTransactionBundle()
              .get(txNb)
              .traceSections()
              .trace();
      // int lenTraceSectionList = traceSectionList.size();

      List<TraceFragment> traceFragmentList = traceSectionList.get(0).fragments();

      for (int i = 2; i <= 4; i++) {
        TraceFragment traceFragment = traceFragmentList.get(i);
        AccountFragment accountFragment = (AccountFragment) traceFragment;
        Address address = accountFragment.oldState().address();
        int relBlokNo = accountFragment.transactionProcessingMetadata().getRelativeBlockNumber();
        int deplNo = accountFragment.newState().deploymentNumber();
        updateDeplNoBlockMaps(address, relBlokNo, deplNo, minDeplNoBlock, maxDeplNoBlock);
      }
    }

    // prepare data for asserts
    // expected first values for the keys we are testing
    int noBlocks = 3;
    Integer[][] expectedMin = {
      {1, null, null},
      {null, 1, null},
      {null, null, 1},
    };
    // expected last values for the keys we are testing
    Integer[][] expectedMax = {
      {
        4, null, null,
      },
      {
        null, 2, null,
      },
      {null, null, 4},
    };
    // prepare the key pairs
    Address[] keys = {
      tc.newAddresses[0], tc.newAddresses[1], tc.newAddresses[2],
    };

    // blocks are numbered starting from 1
    for (int block = 1; block <= noBlocks; block++) {
      for (int i = 0; i < keys.length; i++) {
        AddrBlockPair key = new AddrBlockPair(keys[i], block);
        Integer minNo = minDeplNoBlock.get(key);
        Integer maxNo = maxDeplNoBlock.get(key);
        // asserts for the first and last storage values in conflation
        // -1 due to block numbering
        assertEquals(expectedMin[block - 1][i], minNo);
        assertEquals(expectedMax[block - 1][i], maxNo);
      }
    }

    System.out.println("Done");
  }

  public static class AddrBlockPair {
    private Address address;
    private int blockNumber;

    public AddrBlockPair(Address addr, int blockNumber) {
      this.address = addr;
      this.blockNumber = blockNumber;
    }
  }

  public void updateDeplNoBlockMaps(
      Address address,
      int blockNumber,
      int currentDeplNo,
      Map<AddrBlockPair, Integer> minDeplNoBlock,
      Map<AddrBlockPair, Integer> maxDeplNoBlock) {
    AddrBlockPair addrBlockPair = new AddrBlockPair(address, blockNumber);
    if (minDeplNoBlock.containsKey(addrBlockPair)) {
      // the maps already contain deployment info for this address, and this is not the first one in
      // the block
      // since it is not the first, we do not update the minDeplNoBlock
      // but we update the maxDeplNoBlock
      maxDeplNoBlock.put(addrBlockPair, currentDeplNo);
    } else {
      // this is the first time we have a deployment at this address in the block
      minDeplNoBlock.put(addrBlockPair, currentDeplNo);
      maxDeplNoBlock.put(addrBlockPair, currentDeplNo);
    }
  }
}
