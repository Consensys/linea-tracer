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
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Wei;
import org.junit.jupiter.api.Test;

public class BlockwiseAccountTest {
  TestContext tc;

  @Test
  void testBlockwiseMapAccount() {
    // initialize the test context
    this.tc = new TestContext();
    this.tc.initializeTestContext();
    // prepare the transaction validator
    TransactionProcessingResultValidator resultValidator =
        new StateManagerTestValidator(
            tc.frameworkEntryPointAccount,
            // Creates, writes, reads and self-destructs generate 2 logs,
            // Reverted operations only have 1 log
            List.of(3, 3, 3, 3, 3, 3, 3, 3, 3, 1));
    // fetch the Hub metadata for the state manager maps
    /*        StateManagerMetadata stateManagerMetadata = Hub.stateManagerMetadata();*/

    // prepare a multi-block execution of transactions
    final MultiBlockExecutionEnvironment multiBlockEnv =
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
                    tc.transferTo(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[0],
                        tc.addresses[2],
                        1L,
                        false,
                        BigInteger.ONE),
                    tc.transferTo(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[2],
                        tc.addresses[0],
                        2L,
                        false,
                        BigInteger.ONE),
                    tc.transferTo(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[0],
                        tc.addresses[2],
                        5L,
                        false,
                        BigInteger.ONE)))
            .addBlock(
                List.of(
                    tc.transferTo(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[0],
                        tc.addresses[2],
                        10L,
                        false,
                        BigInteger.ONE),
                    tc.transferTo(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[2],
                        tc.addresses[0],
                        20L,
                        false,
                        BigInteger.ONE),
                    tc.transferTo(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[0],
                        tc.addresses[2],
                        50L,
                        false,
                        BigInteger.ONE)))
            .addBlock(
                List.of(
                    tc.transferTo(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[0],
                        tc.addresses[2],
                        100L,
                        false,
                        BigInteger.ONE),
                    tc.transferTo(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[2],
                        tc.addresses[0],
                        200L,
                        false,
                        BigInteger.ONE),
                    tc.transferTo(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[0],
                        tc.addresses[2],
                        500L,
                        false,
                        BigInteger.ONE),
                    tc.transferTo(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[0],
                        tc.addresses[2],
                        1234L,
                        true,
                        BigInteger.ONE)))
            .transactionProcessingResultValidator(resultValidator)
            .build();

    multiBlockEnv.run();

    /*        Map<StateManagerMetadata. AddrBlockPair, TransactionProcessingMetadata. FragmentFirstAndLast<AccountFragment>>
    blockMap = stateManagerMetadata.getAccountFirstLastBlockMap();*/

    // Total number of transactions seen in the hub
    int txCount = multiBlockEnv.getHub().state().txCount();

    // Replay the transaction's trace from the hub to compute the first and last values for the
    // account storage
    List<Map<Address, FragmentFirstAndLast<AccountFragment>>> accountFirstAndLastMapList =
        StateManagerUtils.computeAccountFirstAndLastMapList(multiBlockEnv.getHub());

    Map<Address, Map<Integer, FragmentFirstAndLast<AccountFragment>>> blockMapAccount =
        new HashMap<>();
    int blockCount = multiBlockEnv.getHub().blockdata().getOperations().size() / 7;
    for (int i = 0; i < blockCount; i++) {
      int relBlokNoFromBlock =
          (int) multiBlockEnv.getHub().blockdata().getOperations().get(i * 7).relBlock();

      for (int txNb = 0; txNb < txCount; txNb++) {
        int relBlokNoFromTx =
            multiBlockEnv
                .getHub()
                .txStack()
                .getByAbsoluteTransactionNumber(txNb + 1)
                .getRelativeBlockNumber();

        if (relBlokNoFromTx == relBlokNoFromBlock) {
          Map<Address, FragmentFirstAndLast<AccountFragment>> accountFirstAndLastMap =
              accountFirstAndLastMapList.get(txNb);
          for (var entry : accountFirstAndLastMap.entrySet()) {
            Address addr = entry.getKey();
            FragmentFirstAndLast<AccountFragment> localValueAccount = entry.getValue();

            if (!blockMapAccount.containsKey(addr)) {
              // the pair is not present in the map
              blockMapAccount.put(addr, new HashMap<>());
              blockMapAccount.get(addr).put(relBlokNoFromBlock, localValueAccount);
            } else if (!blockMapAccount.get(addr).containsKey(relBlokNoFromBlock)) {
              // the pair is present in the map, but the block is not present
              blockMapAccount.get(addr).put(relBlokNoFromBlock, localValueAccount);
            } else {
              FragmentFirstAndLast<AccountFragment> fetchedValue =
                  blockMapAccount.get(addr).get(relBlokNoFromBlock);
              // we make a copy that will be modified to not change the values already present in
              // the
              // transaction maps
              FragmentFirstAndLast<AccountFragment> blockValue = fetchedValue.copy();
              // update the first part of the blockValue
              // Todo: Refactor and remove code duplication
              if (FragmentFirstAndLast.strictlySmallerStamps(
                  localValueAccount.getFirstDom(),
                  localValueAccount.getFirstSub(),
                  blockValue.getFirstDom(),
                  blockValue.getFirstSub())) {
                // chronologically checks that localValue.First is before blockValue.First
                // localValue comes chronologically before, and should be the first value of the
                // map.
                blockValue.setFirst(localValueAccount.getFirst());
                blockValue.setFirstDom(localValueAccount.getFirstDom());
                blockValue.setFirstSub(localValueAccount.getFirstSub());
              }

              // update the last part of the blockValue
              if (FragmentFirstAndLast.strictlySmallerStamps(
                  blockValue.getLastDom(),
                  blockValue.getLastSub(),
                  localValueAccount.getLastDom(),
                  localValueAccount.getLastSub())) {
                // chronologically checks that blockValue.Last is before localValue.Last
                // localValue comes chronologically after, and should be the final value of the map.
                blockValue.setLast(localValueAccount.getLast());
                blockValue.setLastDom(localValueAccount.getLastDom());
                blockValue.setLastSub(localValueAccount.getLastSub());
              }
              blockMapAccount.get(addr).put(relBlokNoFromBlock, blockValue);
            }
          }
        }
      }
    }

    // prepare data for asserts
    // expected first values for the keys we are testing
    int noBlocks = 3;
    Wei[][] expectedFirst = {
      {TestContext.defaultBalance, TestContext.defaultBalance},
      {
        TestContext.defaultBalance.subtract(1L).add(2L).subtract(5L),
        TestContext.defaultBalance.add(1L).subtract(2L).add(5L),
      },
      {
        TestContext.defaultBalance
            .subtract(1L)
            .add(2L)
            .subtract(5L)
            .subtract(10L)
            .add(20L)
            .subtract(50L),
        TestContext.defaultBalance.add(1L).subtract(2L).add(5L).add(10L).subtract(20L).add(50L),
      },
    };
    // expected last values for the keys we are testing
    Wei[][] expectedLast = {
      {
        TestContext.defaultBalance.subtract(1L).add(2L).subtract(5L),
        TestContext.defaultBalance.add(1L).subtract(2L).add(5L),
      },
      {
        TestContext.defaultBalance
            .subtract(1L)
            .add(2L)
            .subtract(5L)
            .subtract(10L)
            .add(20L)
            .subtract(50L),
        TestContext.defaultBalance.add(1L).subtract(2L).add(5L).add(10L).subtract(20L).add(50L),
      },
      {
        TestContext.defaultBalance
            .subtract(1L)
            .add(2L)
            .subtract(5L)
            .subtract(10L)
            .add(20L)
            .subtract(50L)
            .subtract(100L)
            .add(200L)
            .subtract(500L),
        TestContext.defaultBalance
            .add(1L)
            .subtract(2L)
            .add(5L)
            .add(10L)
            .subtract(20L)
            .add(50L)
            .add(100L)
            .subtract(200L)
            .add(500L)
      },
    };
    // prepare the key pairs
    Address[] keys = {
      tc.initialAccounts[0].getAddress(), tc.initialAccounts[2].getAddress(),
    };

    // blocks are numbered starting from 1
    for (int block = 1; block <= noBlocks; block++) {
      for (int i = 0; i < keys.length; i++) {
        FragmentFirstAndLast<AccountFragment> accountData = blockMapAccount.get(keys[i]).get(block);
        // asserts for the first and last storage values in conflation
        // -1 due to block numbering
        assertEquals(expectedFirst[block - 1][i], accountData.getFirst().oldState().balance());
        assertEquals(expectedLast[block - 1][i], accountData.getLast().newState().balance());
      }
    }

    System.out.println("Done");
  }
}
