package net.consensys.linea.zktracer.statemanager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.consensys.linea.testing.MultiBlockExecutionEnvironment;
import net.consensys.linea.testing.TransactionProcessingResultValidator;
import net.consensys.linea.zktracer.module.hub.fragment.TraceFragment;
import net.consensys.linea.zktracer.module.hub.fragment.storage.StorageFragment;
import net.consensys.linea.zktracer.module.hub.section.TraceSection;
import net.consensys.linea.zktracer.types.EWord;
import org.hyperledger.besu.datatypes.Address;
import org.junit.jupiter.api.Test;

public class BlockwiseStorageTest {
  TestContext tc;

  @Test
  void testBlockwiseMapStorage() {
    // initialize the test context
    this.tc = new TestContext();
    this.tc.initializeTestContext();
    // prepare the transaction validator
    TransactionProcessingResultValidator resultValidator =
        new StateManagerTestValidator(
            tc.frameworkEntryPointAccount,
            // Creates, writes, reads and self-destructs generate 2 logs,
            // Reverted operations only have 1 log
            List.of(2, 2, 2, 2, 2, 2, 2, 2, 2, 1));
    // fetch the Hub metadata for the state manager maps
    // StateManagerMetadata stateManagerMetadata = Hub.stateManagerMetadata();
    // compute the addresses for several accounts that will be deployed later
    tc.newAddresses[0] =
        tc.getCreate2AddressForSnippet(
            "0x0000000000000000000000000000000000000000000000000000000000000002");
    tc.newAddresses[1] =
        tc.getCreate2AddressForSnippet(
            "0x0000000000000000000000000000000000000000000000000000000000000003");
    tc.newAddresses[2] =
        tc.getCreate2AddressForSnippet(
            "0x0000000000000000000000000000000000000000000000000000000000000004");

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
                    tc.writeToStorage(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[0],
                        3L,
                        1L,
                        false,
                        BigInteger.ONE),
                    tc.writeToStorage(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[0],
                        3L,
                        2L,
                        false,
                        BigInteger.ONE),
                    tc.writeToStorage(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[0],
                        3L,
                        3L,
                        false,
                        BigInteger.ONE)))
            .addBlock(
                List.of(
                    tc.writeToStorage(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[0],
                        3L,
                        4L,
                        false,
                        BigInteger.ONE),
                    tc.writeToStorage(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[0],
                        3L,
                        5L,
                        false,
                        BigInteger.ONE),
                    tc.writeToStorage(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[0],
                        3L,
                        6L,
                        false,
                        BigInteger.ONE)))
            .addBlock(
                List.of(
                    tc.writeToStorage(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[0],
                        3L,
                        7L,
                        false,
                        BigInteger.ONE),
                    tc.writeToStorage(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[0],
                        3L,
                        8L,
                        false,
                        BigInteger.ONE),
                    tc.writeToStorage(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[0],
                        3L,
                        9L,
                        false,
                        BigInteger.ONE),
                    tc.writeToStorage(
                        tc.externallyOwnedAccounts[0],
                        tc.keyPairs[0],
                        tc.addresses[0],
                        3L,
                        1234L,
                        true,
                        BigInteger.ONE)))
            .transactionProcessingResultValidator(resultValidator)
            .build();

    multiBlockEnv.run();

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

    // prepare data for asserts
    // expected first values for the keys we are testing
    int noBlocks = 3;
    EWord[][] expectedFirst = {
      {
        EWord.of(0L),
      },
      {
        EWord.of(3L),
      },
      {
        EWord.of(6L),
      },
    };
    // expected last values for the keys we are testing
    EWord[][] expectedLast = {
      {
        EWord.of(3L),
      },
      {
        EWord.of(6L),
      },
      {
        EWord.of(9L),
      },
    };
    // prepare the key pairs
    List<Map<Address, EWord>> addrStorageKeyMapList =
        List.of(Map.of(tc.initialAccounts[0].getAddress(), EWord.of(3L)));

    // blocks are numbered starting from 1
    for (int block = 1; block <= noBlocks; block++) {
      for (int i = 0; i < addrStorageKeyMapList.size(); i++) {
        FragmentFirstAndLast<StorageFragment> storageData =
            blockMapStorage.get(addrStorageKeyMapList.get(i)).get(block);
        // asserts for the first and last storage values in conflation
        // -1 due to block numbering
        assertEquals(expectedFirst[block - 1][i], storageData.getFirst().getValueCurrent());
        assertEquals(expectedLast[block - 1][i], storageData.getLast().getValueNext());
      }
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
