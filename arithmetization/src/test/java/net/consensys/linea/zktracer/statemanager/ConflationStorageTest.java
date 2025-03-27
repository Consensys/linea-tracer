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

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import java.util.*;

import net.consensys.linea.testing.*;
import net.consensys.linea.zktracer.module.hub.fragment.TraceFragment;
import net.consensys.linea.zktracer.module.hub.fragment.storage.StorageFragment;
import net.consensys.linea.zktracer.module.hub.section.TraceSection;
import net.consensys.linea.zktracer.types.EWord;
import org.hyperledger.besu.datatypes.Address;
import org.junit.jupiter.api.Test;

public class ConflationStorageTest {
  TestContext tc;

  @Test
  void testConflationMapStorage() {
    // initialize the test context
    this.tc = new TestContext();
    this.tc.initializeTestContext();
    // prepare the transaction validator
    TransactionProcessingResultValidator resultValidator =
        new StateManagerTestValidator(
            tc.frameworkEntryPointAccount,
            // Creates, writes, reads and self-destructs generate 2 logs,
            // Reverted operations only have 1 log
            List.of(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1));
    /*        // fetch the Hub metadata for the state manager maps
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
            // test storage operations for an account prexisting in the state
            .addBlock(
                List.of(
                    tc.writeToStorage(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[0],
                        123L,
                        8L,
                        false,
                        BigInteger.ONE)))
            .addBlock(
                List.of(
                    tc.readFromStorage(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[0],
                        123L,
                        false,
                        BigInteger.ONE)))
            .addBlock(
                List.of(
                    tc.writeToStorage(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[0],
                        123L,
                        10L,
                        false,
                        BigInteger.ONE)))
            .addBlock(
                List.of(
                    tc.readFromStorage(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[0],
                        123L,
                        false,
                        BigInteger.ONE)))
            .addBlock(
                List.of(
                    tc.writeToStorage(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[0],
                        123L,
                        15L,
                        false,
                        BigInteger.ONE)))
            // deploy another account and perform storage operations on it
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
                    tc.writeToStorage(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.newAddresses[0],
                        345L,
                        20L,
                        false,
                        BigInteger.ONE)))
            .addBlock(
                List.of(
                    tc.readFromStorage(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.newAddresses[0],
                        345L,
                        false,
                        BigInteger.ONE)))
            .addBlock(
                List.of(
                    tc.writeToStorage(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.newAddresses[0],
                        345L,
                        40L,
                        false,
                        BigInteger.ONE)))
            // deploy another account and self destruct it at the end, redeploy it and change the
            // storage again
            // the salt will be the same twice in a row, which will be on purpose
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
                    tc.writeToStorage(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.newAddresses[1],
                        400L,
                        12L,
                        false,
                        BigInteger.ONE)))
            .addBlock(
                List.of(
                    tc.readFromStorage(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.newAddresses[1],
                        400L,
                        false,
                        BigInteger.ONE)))
            .addBlock(
                List.of(
                    tc.writeToStorage(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.newAddresses[1],
                        400L,
                        13L,
                        false,
                        BigInteger.ONE)))
            .addBlock(
                List.of(
                    tc.selfDestruct(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.newAddresses[1],
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
                        false)))
            .addBlock(
                List.of(
                    tc.writeToStorage(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.newAddresses[1],
                        400L,
                        99L,
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
                    tc.writeToStorage(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.newAddresses[2],
                        500L,
                        23L,
                        false,
                        BigInteger.ONE)))
            .addBlock(
                List.of(
                    tc.writeToStorage(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.newAddresses[2],
                        500L,
                        53L,
                        true,
                        BigInteger.ONE))) // revert flag on
            .addBlock(
                List.of(
                    tc.writeToStorage(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.newAddresses[2],
                        500L,
                        63L,
                        true,
                        BigInteger.ONE))) // revert flag on
            .transactionProcessingResultValidator(resultValidator)
            .build();

    multiBlockEnv.run();

    /*
            Map<Address, TransactionProcessingMetadata. FragmentFirstAndLast<AccountFragment>>
                    conflationMap = stateManagerMetadata.getAccountFirstLastConflationMap();
            Map<TransactionProcessingMetadata. AddrStorageKeyPair, TransactionProcessingMetadata. FragmentFirstAndLast<StorageFragment>>
                    conflationStorage = stateManagerMetadata.getStorageFirstLastConflationMap();
    */
    // Initialize the storageFirstAndLastMap list
    List<Map<Map<Address, EWord>, FragmentFirstAndLast<StorageFragment>>>
        storageFirstAndLastMapList = new ArrayList<>();

    // We count the number of transactions in the hub
    int txCount = multiBlockEnv.getHub().state().txCount();
    // We iterate over the transactions
    for (int txNb = 0; txNb < txCount; txNb++) {
      // We create an storageFirstAndLastMap for each transaction
      storageFirstAndLastMapList.add(new HashMap<>());
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
          // We cast them to StorageFragment
          // If an exception occurs, it means the Fragment is not a StorageFragment so we
          // disregard it and continue
          try {
            StorageFragment storageFragment = (StorageFragment) traceFragment;
            Address address = storageFragment.getStorageSlotIdentifier().getAddress();
            EWord key = storageFragment.getStorageSlotIdentifier().getStorageKey();
            // We update the storageFirstAndLastMapList
            updateStorageFirstAndLast(
                storageFragment, storageFirstAndLastMapList.get(txNb), Map.of(address, key));
          } catch (Exception e) {
            // ignore
          }
        }
      }
    }

    Map<Map<Address, EWord>, Map<Integer, FragmentFirstAndLast<StorageFragment>>> blockMapStorage =
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
          Map<Map<Address, EWord>, FragmentFirstAndLast<StorageFragment>> storageFirstAndLastMap =
              storageFirstAndLastMapList.get(txNb);

          // Update the block map for storage
          for (var entry : storageFirstAndLastMap.entrySet()) {
            Map<Address, EWord> addrStorageMapKey = entry.getKey();
            // localValue exists for sure because addr belongs to the keySet of the local map
            FragmentFirstAndLast<StorageFragment> localValueStorage = entry.getValue();

            if (!blockMapStorage.containsKey(addrStorageMapKey)) {
              // the pair is not present in the map
              blockMapStorage.put(addrStorageMapKey, new HashMap<>());
              blockMapStorage.get(addrStorageMapKey).put(relBlokNoFromBlock, localValueStorage);
            } else if (!blockMapStorage.get(addrStorageMapKey).containsKey(relBlokNoFromBlock)) {
              blockMapStorage.get(addrStorageMapKey).put(relBlokNoFromBlock, localValueStorage);
            } else {
              FragmentFirstAndLast<StorageFragment> fetchedValue =
                  blockMapStorage.get(addrStorageMapKey).get(relBlokNoFromBlock);
              // we make a copy that will be modified to not change the values already present in
              // the
              // transaction maps
              FragmentFirstAndLast<StorageFragment> blockValueStorage = fetchedValue.copy();
              // update the first part of the blockValue
              // Todo: Refactor and remove code duplication
              if (FragmentFirstAndLast.strictlySmallerStamps(
                  localValueStorage.getFirstDom(),
                  localValueStorage.getFirstSub(),
                  blockValueStorage.getFirstDom(),
                  blockValueStorage.getFirstSub())) {
                // chronologically checks that localValue.First is before blockValue.First
                // localValue comes chronologically before, and should be the first value of the
                // map.
                blockValueStorage.setFirst(localValueStorage.getFirst());
                blockValueStorage.setFirstDom(localValueStorage.getFirstDom());
                blockValueStorage.setFirstSub(localValueStorage.getFirstSub());
              }

              // update the last part of the blockValue
              if (FragmentFirstAndLast.strictlySmallerStamps(
                  blockValueStorage.getLastDom(),
                  blockValueStorage.getLastSub(),
                  localValueStorage.getLastDom(),
                  localValueStorage.getLastSub())) {
                // chronologically checks that blockValue.Last is before localValue.Last
                // localValue comes chronologically after, and should be the final value of the map.
                blockValueStorage.setLast(localValueStorage.getLast());
                blockValueStorage.setLastDom(localValueStorage.getLastDom());
                blockValueStorage.setLastSub(localValueStorage.getLastSub());
              }
              blockMapStorage.get(addrStorageMapKey).put(relBlokNoFromBlock, blockValueStorage);
            }
          }
        }
      }
    }

    Map<Map<Address, EWord>, FragmentFirstAndLast<StorageFragment>> conflationMapStorage =
        new HashMap<>();

    HashSet<Map<Address, EWord>> allStorage = new HashSet<Map<Address, EWord>>();

    // We iterate over the transactions
    for (int txNb = 0; txNb < txCount; txNb++) {

      Map<Map<Address, EWord>, FragmentFirstAndLast<StorageFragment>> txnMapAccount =
          storageFirstAndLastMapList.get(txNb);

      allStorage.addAll(txnMapAccount.keySet());
    }

    for (Map<Address, EWord> addrStorageKeyPair : allStorage) {
      FragmentFirstAndLast<StorageFragment> firstValue = null;
      // Update the first value of the conflation map for Storage
      // We update the value of the conflation map with the earliest value of the block map
      for (int i = 1; i <= blockCount; i++) {
        if (blockMapStorage.containsKey(addrStorageKeyPair)
            && blockMapStorage.get(addrStorageKeyPair).containsKey(i)) {
          firstValue = blockMapStorage.get(addrStorageKeyPair).get(i);
          conflationMapStorage.put(addrStorageKeyPair, firstValue);
          break;
        }
      }
      // Update the last value of the conflation map
      // We update the last value for the conflation map with the latest blockMap's last values,
      // if some address is not present in the last block, we ignore the corresponding account
      for (int i = blockCount; i >= 1; i--) {
        if (blockMapStorage.containsKey(addrStorageKeyPair)
            && blockMapStorage.get(addrStorageKeyPair).containsKey(i)) {
          FragmentFirstAndLast<StorageFragment> blockValue =
              blockMapStorage.get(addrStorageKeyPair).get(i);

          FragmentFirstAndLast<StorageFragment> updatedValue =
              new FragmentFirstAndLast<StorageFragment>(
                  firstValue.getFirst(),
                  blockValue.getLast(),
                  firstValue.getFirstDom(),
                  firstValue.getFirstSub(),
                  blockValue.getLastDom(),
                  blockValue.getLastSub());
          conflationMapStorage.put(addrStorageKeyPair, updatedValue);
          break;
        }
      }
    }

    // prepare data for asserts
    // expected first values for the keys we are testing
    EWord[] expectedFirst = {EWord.of(0L), EWord.of(0), EWord.of(0), EWord.of(0)};
    // expected last values for the keys we are testing
    EWord[] expectedLast = {EWord.of(15L), EWord.of(40L), EWord.of(99L), EWord.of(23L)};
    // prepare the key pairs
    List<Map<Address, EWord>> keys =
        List.of(
            Map.of(tc.initialAccounts[0].getAddress(), EWord.of(123L)),
            Map.of(tc.newAddresses[0], EWord.of(345L)),
            Map.of(tc.newAddresses[1], EWord.of(400L)),
            Map.of(tc.newAddresses[2], EWord.of(500L)));

    for (int i = 0; i < keys.size(); i++) {
      FragmentFirstAndLast<StorageFragment> storageData = conflationMapStorage.get(keys.get(i));
      // asserts for the first and last storage values in conflation
      assertEquals(expectedFirst[i], storageData.getFirst().getValueCurrent());
      assertEquals(expectedLast[i], storageData.getLast().getValueNext());
    }
    System.out.println("Done");
  }

  public void updateStorageFirstAndLast(
      StorageFragment fragment,
      Map<Map<Address, EWord>, FragmentFirstAndLast<StorageFragment>> storageFirstAndLastMap,
      Map<Address, EWord> key) {
    // Setting the post transaction first and last value
    int dom = fragment.getDomSubStampsSubFragment().domStamp();
    int sub = fragment.getDomSubStampsSubFragment().subStamp();

    if (!storageFirstAndLastMap.containsKey(key)) {
      FragmentFirstAndLast<StorageFragment> txnFirstAndLast =
          new FragmentFirstAndLast<StorageFragment>(fragment, fragment, dom, sub, dom, sub);
      storageFirstAndLastMap.put(key, txnFirstAndLast);
    } else {
      // the storage key has already been accessed for this account
      FragmentFirstAndLast<StorageFragment> txnFirstAndLast = storageFirstAndLastMap.get(key);
      // Replace condition
      if (FragmentFirstAndLast.strictlySmallerStamps(
          txnFirstAndLast.getLastDom(), txnFirstAndLast.getLastSub(), dom, sub)) {
        txnFirstAndLast.setLast(fragment);
        txnFirstAndLast.setLastDom(dom);
        txnFirstAndLast.setLastSub(sub);
        storageFirstAndLastMap.put(key, txnFirstAndLast);
      }
    }
  }
}

/*
TransactionProcessingResultValidator resultValidator =
        (Transaction transaction, TransactionProcessingResult result) -> {
          // One event from the snippet
          // One event from the framework entrypoint about contract call
          //assertEquals(result.getLogs().size(), 1);
          System.out.println("Number of logs: "+result.getLogs().size());
          var noTopics = result.getLogs().size();
          for (Log log : result.getLogs()) {
            String logTopic = log.getTopics().getFirst().toHexString();
            String callEventSignature = EventEncoder.encode(FrameworkEntrypoint.CALLEXECUTED_EVENT);
            String writeEventSignature = EventEncoder.encode(FrameworkEntrypoint.WRITE_EVENT);
            String readEventSignature = EventEncoder.encode(FrameworkEntrypoint.READ_EVENT);
            String destructEventSignature = EventEncoder.encode(FrameworkEntrypoint.CONTRACTDESTROYED_EVENT);
            if (callEventSignature.equals(logTopic)) {
              FrameworkEntrypoint.CallExecutedEventResponse response =
                      FrameworkEntrypoint.getCallExecutedEventFromLog(Web3jUtils.fromBesuLog(log));
              assertTrue(response.isSuccess);
              assertEquals(response.destination, this.testContext.initialAccounts[0].getAddress().toHexString());
              continue;
            }
            if (writeEventSignature.equals(logTopic)) {
              // write event
              continue;
            }
            if (readEventSignature.equals(logTopic)) {
              // read event
              continue;
            }
            if (destructEventSignature.equals(logTopic)) {
              // self destruct
              continue;
            }
            fail();
          }
        };
 */
