package net.consensys.linea.zktracer.statemanager;

import java.math.BigInteger;
import java.util.List;

import net.consensys.linea.testing.MultiBlockExecutionEnvironment;
import net.consensys.linea.testing.TransactionProcessingResultValidator;
import net.consensys.linea.zktracer.types.EWord;
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

    /*
            Map<StateManagerMetadata. AddrStorageKeyBlockNumTuple, TransactionProcessingMetadata. FragmentFirstAndLast<StorageFragment>>
                    blockMap = stateManagerMetadata.getStorageFirstLastBlockMap();
    */

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
    /*           AddrStorageKeyPair[] rawKeys = {
            new TransactionProcessingMetadata.AddrStorageKeyPair(tc.initialAccounts[0].getAddress(), EWord.of(3L)),
    };
           // blocks are numbered starting from 1
    for (int block = 1; block <= noBlocks; block++) {
        for (int i = 0; i < rawKeys.length; i++) {
            StateManagerMetadata.AddrStorageKeyBlockNumTuple key =
                    new StateManagerMetadata.AddrStorageKeyBlockNumTuple(
                            rawKeys[i],
                            block);
            TransactionProcessingMetadata. FragmentFirstAndLast<StorageFragment>
                    storageData = blockMap.get(key);
            // asserts for the first and last storage values in conflation
            // -1 due to block numbering
            assertEquals(expectedFirst[block-1][i], storageData.getFirst().getValueCurrent());
            assertEquals(expectedLast[block-1][i], storageData.getLast().getValueNext());
        }
    }*/

    System.out.println("Done");
  }
}
