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
import java.util.*;

import net.consensys.linea.testing.MultiBlockExecutionEnvironment;
import net.consensys.linea.testing.TransactionProcessingResultValidator;
import net.consensys.linea.zktracer.module.hub.fragment.TraceFragment;
import net.consensys.linea.zktracer.module.hub.fragment.account.AccountFragment;
import net.consensys.linea.zktracer.module.hub.section.TraceSection;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Wei;
import org.junit.jupiter.api.Test;

public class ConflationAccountTest {
  TestContext tc;

  @Test
  void testConflationMapAccount() {
    // initialize the test context
    this.tc = new TestContext();
    this.tc.initializeTestContext();
    // prepare the transaction validator
    TransactionProcessingResultValidator resultValidator =
        new StateManagerTestValidator(
            tc.frameworkEntryPointAccount,
            // Creates and self-destructs generate 2 logs,
            // Transfers generate 3 logs, the 1s are for reverted operations
            List.of(3, 3, 1, 3, 2, 3, 3, 2, 3, 2, 2, 3, 2, 1)); /*
        // fetch the Hub metadata for the state manager maps
        StateManagerMetadata stateManagerMetadata = Hub.stateManagerMetadata();*/

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
            // test account operations for an account prexisting in the state
            .addBlock(
                List.of(
                    tc.transferTo(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[0],
                        tc.addresses[2],
                        8L,
                        false,
                        BigInteger.ONE)))
            .addBlock(
                List.of(
                    tc.transferTo(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[2],
                        tc.addresses[0],
                        20L,
                        false,
                        BigInteger.ONE)))
            .addBlock(
                List.of(
                    tc.transferTo(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[0],
                        tc.addresses[2],
                        50L,
                        true,
                        BigInteger.ONE))) // this action is reverted
            .addBlock(
                List.of(
                    tc.transferTo(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[0],
                        tc.addresses[2],
                        10L,
                        false,
                        BigInteger.ONE)))
            // deploy another account ctxt.addresses[3] and perform account operations on it
            .addBlock(
                List.of(
                    tc.deployWithCreate2(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.frameworkEntryPointAddress,
                        tc.salts[0],
                        TestContext.snippetsCodeForCreate2,
                        false)))
            .addBlock(
                List.of(
                    tc.transferTo(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[0],
                        tc.newAddresses[0],
                        49L,
                        false,
                        BigInteger.ONE)))
            .addBlock(
                List.of(
                    tc.transferTo(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.newAddresses[0],
                        tc.addresses[0],
                        27L,
                        false,
                        BigInteger.ONE)))
            // deploy another account and self destruct it at the end, redeploy it and change its
            // balance  again
            .addBlock(
                List.of(
                    tc.deployWithCreate2(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.frameworkEntryPointAddress,
                        tc.salts[1],
                        TestContext.snippetsCodeForCreate2,
                        false)))
            .addBlock(
                List.of(
                    tc.transferTo(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[0],
                        tc.newAddresses[1],
                        98L,
                        false,
                        BigInteger.ONE)))
            .addBlock(
                List.of(
                    tc.selfDestruct(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.newAddresses[1],
                        tc.addresses[2],
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
                        false)))
            .addBlock(
                List.of(
                    tc.transferTo(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[0],
                        tc.newAddresses[1],
                        123L,
                        false,
                        BigInteger.ONE)))
            // deploy a new account and check revert operations on it
            .addBlock(
                List.of(
                    tc.deployWithCreate2(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.frameworkEntryPointAddress,
                        tc.salts[2],
                        TestContext.snippetsCodeForCreate2,
                        false)))
            .addBlock(
                List.of(
                    tc.transferTo(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[2],
                        tc.newAddresses[2],
                        1L,
                        true,
                        BigInteger.ONE)))
            .transactionProcessingResultValidator(resultValidator)
            .build();

    multiBlockEnv.run();
    /*
            Map<Address, TransactionProcessingMetadata. FragmentFirstAndLast<AccountFragment>>
                    conflationMap = stateManagerMetadata.getAccountFirstLastConflationMap();
    */
    List<Map<Address, FragmentFirstAndLast<AccountFragment>>> accountFirstAndLastMapList =
        new ArrayList<>();

    // We count the number of transactions in the hub
    int txCount = multiBlockEnv.getHub().state().txCount();
    // We iterate over the transactions
    for (int txNb = 0; txNb < txCount; txNb++) {
      // We create an accountFirstAndLastMap for each transaction
      accountFirstAndLastMapList.add(new HashMap<>());
      // We retrieve the trace section list
      List<TraceSection> traceSectionList =
          multiBlockEnv
              .getHub()
              .state()
              .getState()
              .operationsInTransactionBundle()
              .get(txNb)
              .traceSections()
              .trace();
      // For each trace section
      for (TraceSection traceSection : traceSectionList) {
        // We iterate over the fragments
        for (TraceFragment traceFragment : traceSection.fragments()) {
          // We cast them to AccountFragment
          // If an exception occurs, it means the Fragment is not an AccountFragment so we
          // disregard it and continue
          try {
            AccountFragment accountFragment = (AccountFragment) traceFragment;
            // We update the AccountFirstAndLastMap
            updateAccountFirstAndLast(accountFragment, accountFirstAndLastMapList.get(txNb));
          } catch (Exception e) {
            // ignore
          }
        }
      }
    }

    List<Map<Address, Map<Integer, FragmentFirstAndLast<AccountFragment>>>> blockMapAccountList =
        new ArrayList<>();

    int blockCount = multiBlockEnv.getHub().blockdata().getOperations().size() / 7;
    for (int i = 0; i < blockCount; i++) {
      Map<Address, Map<Integer, FragmentFirstAndLast<AccountFragment>>> blockMapAccount =
          new HashMap<>();
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

            if (!blockMapAccount.containsKey(addr)
                || !blockMapAccount.get(addr).containsKey(relBlokNoFromBlock)) {
              // the pair is not present in the map
              blockMapAccount.put(addr, new HashMap<>());
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
      blockMapAccountList.add(blockMapAccount);
    }

    Map<Address, FragmentFirstAndLast<AccountFragment>> conflationMapAccount = new HashMap<>();

    HashSet<Address> allAccounts = new HashSet<Address>();

    Map<Address, Map<Integer, FragmentFirstAndLast<AccountFragment>>> blockMapAccount =
        blockMapAccountList.get(blockCount - 1);

    // We iterate over the transactions
    for (int txNb = 0; txNb < txCount; txNb++) {

      Map<Address, FragmentFirstAndLast<AccountFragment>> txnMapAccount =
          accountFirstAndLastMapList.get(txNb);

      allAccounts.addAll(txnMapAccount.keySet());
    }

    for (Address addr : allAccounts) {
      FragmentFirstAndLast<AccountFragment> firstValue = null;
      // Update the first value of the conflation map for Account
      // We update the value of the conflation map with the earliest value of the block map
      // TODO: change transients.block().blockNumber()
      for (int i = 1; i <= blockCount; i++) {
        if (blockMapAccount.containsKey(addr) && blockMapAccount.get(addr).containsKey(i)) {
          firstValue = blockMapAccount.get(addr).get(i);
          conflationMapAccount.put(addr, firstValue);
          break;
        }
      }

      // Update the last value of the conflation map
      // We update the last value for the conflation map with the latest blockMap's last values,
      // if some address is not present in the last block, we ignore the corresponding account
      for (int i = blockCount; i >= 1; i--) {
        if (blockMapAccount.containsKey(addr) && blockMapAccount.get(addr).containsKey(i)) {
          FragmentFirstAndLast<AccountFragment> blockValue = blockMapAccount.get(addr).get(i);

          FragmentFirstAndLast<AccountFragment> updatedValue =
              new FragmentFirstAndLast<AccountFragment>(
                  firstValue.getFirst(),
                  blockValue.getLast(),
                  firstValue.getFirstDom(),
                  firstValue.getFirstSub(),
                  blockValue.getLastDom(),
                  blockValue.getLastSub());
          conflationMapAccount.put(addr, updatedValue);
          break;
        }
      }
    }

    // prepare data for asserts
    // expected first values for the keys we are testing
    Wei[] expectedFirst = {
      TestContext.defaultBalance, TestContext.defaultBalance, Wei.of(0L), Wei.of(0L), Wei.of(0L)
    };
    // expected last values for the keys we are testing
    Wei[] expectedLast = {
      TestContext.defaultBalance
          .subtract(8L)
          .add(20L)
          .subtract(10L)
          .subtract(49L)
          .add(27L)
          .subtract(98L)
          .subtract(123L),
      TestContext.defaultBalance
          .add(8L)
          .subtract(20L)
          .add(10L)
          .add(98L), // 98L obtained from the self destruct of the account at ctxt.addresses[4]
      Wei.of(0L).add(49L).subtract(27L),
      Wei.of(123L),
      Wei.of(0L)
    };

    // prepare the key pairs
    Address[] keys = {
      tc.initialAccounts[0].getAddress(),
      tc.initialAccounts[2].getAddress(),
      tc.newAddresses[0],
      tc.newAddresses[1],
      tc.newAddresses[2]
    };

    for (int i = 0; i < keys.length; i++) {
      System.out.println("Index is " + i);
      FragmentFirstAndLast<AccountFragment> accountData = conflationMapAccount.get(keys[i]);
      // asserts for the first and last storage values in conflation
      assertEquals(expectedFirst[i], accountData.getFirst().oldState().balance());
      assertEquals(expectedLast[i], accountData.getLast().newState().balance());
    }

    System.out.println("Done");
  }

  /*    // Update the conflation level map for the state manager
  public void updateConflationMapAccount() {
      Map<Address, TransactionProcessingMetadata.FragmentFirstAndLast<AccountFragment>>
              conflationMapAccount = Hub.stateManagerMetadata().getAccountFirstLastConflationMap();

      List<TransactionProcessingMetadata> txn = txStack.getTransactions();
      HashSet<Address> allAccounts = new HashSet<Address>();

      Map<
              StateManagerMetadata.AddrBlockPair,
              TransactionProcessingMetadata.FragmentFirstAndLast<AccountFragment>>
              blockMapAccount = Hub.stateManagerMetadata().getAccountFirstLastBlockMap();

      for (TransactionProcessingMetadata metadata : txn) {

          Map<Address, TransactionProcessingMetadata.FragmentFirstAndLast<AccountFragment>>
                  txnMapAccount = metadata.getAccountFirstAndLastMap();

          allAccounts.addAll(txnMapAccount.keySet());
      }

      for (Address addr : allAccounts) {
          TransactionProcessingMetadata.FragmentFirstAndLast<AccountFragment> firstValue = null;
          // Update the first value of the conflation map for Account
          // We update the value of the conflation map with the earliest value of the block map
          for (int i = 1; i <= transients.block().blockNumber(); i++) {
              StateManagerMetadata.AddrBlockPair pairAddrBlock =
                      new StateManagerMetadata.AddrBlockPair(addr, i);
              if (blockMapAccount.containsKey(pairAddrBlock)) {
                  firstValue = blockMapAccount.get(pairAddrBlock);
                  conflationMapAccount.put(addr, firstValue);
                  break;
              }
          }
          // Update the last value of the conflation map
          // We update the last value for the conflation map with the latest blockMap's last values,
          // if some address is not present in the last block, we ignore the corresponding account
          for (int i = transients.block().blockNumber(); i >= 1; i--) {
              StateManagerMetadata.AddrBlockPair pairAddrBlock =
                      new StateManagerMetadata.AddrBlockPair(addr, i);
              if (blockMapAccount.containsKey(pairAddrBlock)) {
                  TransactionProcessingMetadata.FragmentFirstAndLast<AccountFragment> blockValue =
                          blockMapAccount.get(pairAddrBlock);

                  TransactionProcessingMetadata.FragmentFirstAndLast<AccountFragment> updatedValue =
                          new TransactionProcessingMetadata.FragmentFirstAndLast<AccountFragment>(
                                  firstValue.getFirst(),
                                  blockValue.getLast(),
                                  firstValue.getFirstDom(),
                                  firstValue.getFirstSub(),
                                  blockValue.getLastDom(),
                                  blockValue.getLastSub());
                  conflationMapAccount.put(addr, updatedValue);
                  break;
              }
          }
      }
  }*/

  public void updateAccountFirstAndLast(
      AccountFragment fragment,
      Map<Address, FragmentFirstAndLast<AccountFragment>> accountFirstAndLastMap) {
    // Setting the post transaction first and last value
    int dom = fragment.domSubStampsSubFragment().domStamp();
    int sub = fragment.domSubStampsSubFragment().subStamp();

    Address key = fragment.oldState().address();

    if (!accountFirstAndLastMap.containsKey(key)) {
      FragmentFirstAndLast<AccountFragment> txnFirstAndLast =
          new FragmentFirstAndLast<AccountFragment>(fragment, fragment, dom, sub, dom, sub);
      accountFirstAndLastMap.put(key, txnFirstAndLast);
    } else {
      FragmentFirstAndLast<AccountFragment> txnFirstAndLast = accountFirstAndLastMap.get(key);
      // Replace condition
      if (FragmentFirstAndLast.strictlySmallerStamps(
          txnFirstAndLast.getLastDom(), txnFirstAndLast.getLastSub(), dom, sub)) {
        txnFirstAndLast.setLast(fragment);
        txnFirstAndLast.setLastDom(dom);
        txnFirstAndLast.setLastSub(sub);
        accountFirstAndLastMap.put(key, txnFirstAndLast);
      }
    }
  }
}
