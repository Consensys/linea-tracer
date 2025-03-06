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

import java.math.BigInteger;
import java.util.List;

import net.consensys.linea.testing.MultiBlockExecutionEnvironment;
import net.consensys.linea.testing.TransactionProcessingResultValidator;
import net.consensys.linea.testing.generated.FrameworkEntrypoint;
import net.consensys.linea.zktracer.types.TransactionProcessingMetadata;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Wei;
import org.junit.jupiter.api.Test;

public class TransactionAccountTest {
  TestContext tc;

  @Test
  void testTransactionMapAccount() {
    // initialize the test context
    this.tc = new TestContext();
    this.tc.initializeTestContext();
    // prepare the transaction validator
    TransactionProcessingResultValidator resultValidator =
        new StateManagerTestValidator(
            tc.frameworkEntryPointAccount,
            // Creates, writes, reads and self-destructs generate 2 logs,
            // transfers generate 3 logs
            // Reverted operations only have 1 log
            List.of(10, 10));
    // fetch the Hub metadata for the state manager maps
    /*
            StateManagerMetadata stateManagerMetadata = Hub.stateManagerMetadata();
    */

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
                tc.newTxFromCalls(
                    tc.externallyOwnedAccounts[0],
                    tc.keyPairs[0],
                    new FrameworkEntrypoint.ContractCall[] {
                      tc.transferToCall(
                          tc.addresses[0], tc.addresses[2], 8L, false, BigInteger.ONE),
                      tc.transferToCall(
                          tc.addresses[2], tc.addresses[0], 9L, false, BigInteger.ONE),
                      tc.transferToCall(
                          tc.addresses[0], tc.addresses[2], 15L, false, BigInteger.ONE),
                      tc.transferToCall(
                          tc.addresses[2],
                          tc.addresses[0],
                          1234L,
                          true,
                          BigInteger.ONE), // revert this one
                    }),
                tc.newTxFromCalls(
                    tc.externallyOwnedAccounts[0],
                    tc.keyPairs[0],
                    new FrameworkEntrypoint.ContractCall[] {
                      tc.transferToCall(
                          tc.addresses[0], tc.addresses[2], 200L, false, BigInteger.ONE),
                      tc.transferToCall(
                          tc.addresses[2], tc.addresses[0], 500L, false, BigInteger.ONE),
                      tc.transferToCall(
                          tc.addresses[2],
                          tc.addresses[0],
                          1234L,
                          true,
                          BigInteger.ONE), // revert this one
                      tc.transferToCall(
                          tc.addresses[0], tc.addresses[2], 900L, false, BigInteger.ONE),
                    })))
        .transactionProcessingResultValidator(resultValidator)
        .build()
        .run();

    // List<TransactionProcessingMetadata> txn =
    // Hub.stateManagerMetadata().getHub().txStack().getTransactions();;
    int txCount = MultiBlockExecutionEnvironment.getHub().state().txCount();
    TransactionProcessingMetadata txPM =
        MultiBlockExecutionEnvironment.getHub().txStack().getByAbsoluteTransactionNumber(1);
    // prepare data for asserts
    // expected first values for the keys we are testing
    Wei[][] expectedFirst = {
      {
        TestContext.defaultBalance, TestContext.defaultBalance,
      },
      {
        TestContext.defaultBalance.subtract(8L).add(9L).subtract(15L),
        TestContext.defaultBalance.add(8L).subtract(9L).add(15L),
      }
    };

    // expected last values for the keys we are testing
    Wei[][] expectedLast = {
      {
        TestContext.defaultBalance.subtract(8L).add(9L).subtract(15L),
        TestContext.defaultBalance.add(8L).subtract(9L).add(15L),
      },
      {
        TestContext.defaultBalance
            .subtract(8L)
            .add(9L)
            .subtract(15L)
            .subtract(200L)
            .add(500L)
            .subtract(900L),
        TestContext.defaultBalance.add(8L).subtract(9L).add(15L).add(200L).subtract(500L).add(900L),
      }
    };

    // prepare the key pairs
    Address[] keys = {
      tc.initialAccounts[0].getAddress(), tc.initialAccounts[2].getAddress(),
    };

    // blocks are numbered starting from 1
    /*        for (int txCounter = 1; txCounter <= txn.size(); txCounter++) {
        Map<Address, TransactionProcessingMetadata. FragmentFirstAndLast<AccountFragment>>
                accountMap = txn.get(txCounter-1).getAccountFirstAndLastMap();
        for (int i = 0; i < keys.length; i++) {
            TransactionProcessingMetadata. FragmentFirstAndLast<AccountFragment>
                    accountData = accountMap.get(keys[i]);
            // asserts for the first and last storage values in conflation
            // -1 due to block numbering
            assertEquals(expectedFirst[txCounter-1][i], accountData.getFirst().oldState().balance());
            assertEquals(expectedLast[txCounter-1][i], accountData.getLast().newState().balance());
        }
    }*/

    System.out.println("Done");
  }
}
