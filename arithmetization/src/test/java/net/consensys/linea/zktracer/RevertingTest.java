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

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.consensys.linea.testing.*;
import net.consensys.linea.testing.generated.FrameworkEntrypoint;
import net.consensys.linea.testing.generated.StateManagerEvents;
import net.consensys.linea.testing.generated.TestSnippet_Events;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.crypto.KeyPair;
import org.hyperledger.besu.crypto.SECP256K1;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.hyperledger.besu.ethereum.processing.TransactionProcessingResult;
import org.hyperledger.besu.evm.log.Log;
import org.junit.jupiter.api.Test;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RevertingTest {
  TestContext testContext;
  // destination must be our .yul smart contract
  Transaction writeToStorage(ToyAccount sender, KeyPair senderKeyPair, ToyAccount destination, Long key, Long value) {
    List<org.web3j.abi.datatypes.Uint> inputParams = new ArrayList<>();
    inputParams.addLast(new Uint256(BigInteger.valueOf(key)));
    inputParams.addLast(new Uint256(BigInteger.valueOf(value)));
    Function yulFunction = new Function("writeToStorage", Collections.unmodifiableList(inputParams), Collections.emptyList());

    var encoding = FunctionEncoder.encode(yulFunction);
    FrameworkEntrypoint.ContractCall snippetContractCall =
            new FrameworkEntrypoint.ContractCall(
                    /*Address*/ destination.getAddress().toHexString(),
                    /*calldata*/ Bytes.fromHexStringLenient(encoding).toArray(),
                    /*gasLimit*/ BigInteger.ZERO,
                    /*value*/ BigInteger.ZERO,
                    /*callType*/ BigInteger.ZERO);

    List<FrameworkEntrypoint.ContractCall> contractCalls = List.of(snippetContractCall);
    Function frameworkEntryPointFunction =
            new Function(
                    FrameworkEntrypoint.FUNC_EXECUTECALLS,
                    List.of(new DynamicArray<>(FrameworkEntrypoint.ContractCall.class, contractCalls)),
                    Collections.emptyList());
    Bytes txPayload =
            Bytes.fromHexStringLenient(FunctionEncoder.encode(frameworkEntryPointFunction));


    ToyTransaction.ToyTransactionBuilder tempTx = ToyTransaction.builder()
            .sender(sender)
            .to(this.testContext.frameworkEntryPointAccount)
            .payload(txPayload)
            .keyPair(senderKeyPair)
            .gasLimit(TestContext.gasLimit);

    if (TestContext.txNonce != null) {
      tempTx = tempTx.nonce(++TestContext.txNonce);
    }
    Transaction tx = tempTx.build();
    if (TestContext.txNonce == null) {
      TestContext.txNonce = tx.getNonce();
    }
    return tx;
  }

  Transaction writeToStorageRevert(ToyAccount sender, KeyPair senderKeyPair, ToyAccount destination, Long key, Long value) {
    List<org.web3j.abi.datatypes.Uint> inputParams = new ArrayList<>();
    inputParams.addLast(new Uint256(BigInteger.valueOf(key)));
    inputParams.addLast(new Uint256(BigInteger.valueOf(value)));
    Function yulFunction = new Function("writeToStorage", Collections.unmodifiableList(inputParams), Collections.emptyList());

    var encoding = FunctionEncoder.encode(yulFunction);
    FrameworkEntrypoint.ContractCall snippetContractCall =
            new FrameworkEntrypoint.ContractCall(
                    /*Address*/ destination.getAddress().toHexString(),
                    /*calldata*/ Bytes.fromHexStringLenient(encoding).toArray(),
                    /*gasLimit*/ BigInteger.ZERO,
                    /*value*/ BigInteger.ZERO,
                    /*callType*/ BigInteger.ZERO);

    List<FrameworkEntrypoint.ContractCall> contractCalls = List.of(snippetContractCall);
    Function frameworkEntryPointFunction =
            new Function(
                    FrameworkEntrypoint.FUNC_EXECUTECALLS,
                    List.of(new DynamicArray<>(FrameworkEntrypoint.ContractCall.class, contractCalls)),
                    Collections.emptyList());
    Bytes txPayload =
            Bytes.fromHexStringLenient(FunctionEncoder.encode(frameworkEntryPointFunction));


    ToyTransaction.ToyTransactionBuilder tempTx = ToyTransaction.builder()
            .sender(sender)
            .to(this.testContext.frameworkEntryPointAccount)
            .payload(txPayload)
            .keyPair(senderKeyPair)
            .gasLimit(TestContext.gasLimit);

    if (TestContext.txNonce != null) {
      tempTx = tempTx.nonce(++TestContext.txNonce);
    }
    Transaction tx = tempTx.build();
    if (TestContext.txNonce == null) {
      TestContext.txNonce = tx.getNonce();
    }
    return tx;
  }






  @NoArgsConstructor
  class TestContext {
    static Long txNonce = null;
    static final Long gasLimit = 500000L;
    static final int numberOfAccounts = 3;
    @Getter
    ToyAccount frameworkEntryPointAccount;
    ToyAccount[] initialAccounts;
    KeyPair[] initialKeyPairs;
    public void initializeTestContext() {
      // initialize vectors
      initialAccounts = new ToyAccount[numberOfAccounts];
      initialKeyPairs = new KeyPair[numberOfAccounts];
      // initialize the testing framework entry point account
      frameworkEntryPointAccount =
              ToyAccount.builder()
                      .address(Address.fromHexString("0x22222"))
                      .balance(Wei.ONE)
                      .nonce(5)
                      .code(SmartContractUtils.getSolidityContractByteCode(FrameworkEntrypoint.class))
                      .build();
      // initialize the .yul snippets account
      // load the .yul bytecode
      initialAccounts[0] =
              ToyAccount.builder()
                      .address(Address.fromHexString("0x11111"))
                      .balance(Wei.ONE)
                      .nonce(6)
                      .code(SmartContractUtils.getYulContractByteCode("StateManagerSnippets.yul"))
                      .build();
      // generate extra accounts
      KeyPair keyPair = new SECP256K1().generateKeyPair();
      Address senderAddress = Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));
      ToyAccount senderAccount =
              ToyAccount.builder().balance(Wei.fromEth(1)).nonce(5).address(senderAddress).build();
      // add to arrays
      initialAccounts[1] = senderAccount;
      initialKeyPairs[1] = keyPair;
      // an account with revert
      initialAccounts[2] =
              ToyAccount.builder()
                      .address(Address.fromHexString("0x44444"))
                      .balance(Wei.ONE)
                      .nonce(8)
                      .code(SmartContractUtils.getYulContractByteCode("RevertOperations.yul"))
                      .build();
    }
  }



  @Test
  void testYulModifiedWrite() {
    // initialize the test context
    this.testContext = new TestContext();
    this.testContext.initializeTestContext();

    int txNumber = 1;

    TransactionProcessingResultValidator resultValidator =
            (Transaction transaction, TransactionProcessingResult result) -> {
              TransactionProcessingResultValidator.DEFAULT_VALIDATOR.accept(transaction, result);
              // One event from the snippet
              // One event from the framework entrypoint about contract call
              System.out.println("Number of logs: "+result.getLogs().size());
              //assertEquals(result.getLogs().size(), 2);
              for (Log log : result.getLogs()) {
                String callEventSignature = EventEncoder.encode(FrameworkEntrypoint.CALLEXECUTED_EVENT);
                String writeEventSignature = EventEncoder.encode(StateManagerEvents.WRITE_EVENT);
                String readEventSignature = EventEncoder.encode(StateManagerEvents.READ_EVENT);
                String destroyedEventSignature = EventEncoder.encode(FrameworkEntrypoint.CONTRACTDESTROYED_EVENT);
                String createdEventSignature = EventEncoder.encode(FrameworkEntrypoint.CONTRACTCREATED_EVENT);
                String logTopic = log.getTopics().getFirst().toHexString();
                if (EventEncoder.encode(TestSnippet_Events.DATANOINDEXES_EVENT).equals(logTopic)) {
                  TestSnippet_Events.DataNoIndexesEventResponse response =
                          TestSnippet_Events.getDataNoIndexesEventFromLog(Web3jUtils.fromBesuLog(log));
                  //assertEquals(response.singleInt, BigInteger.valueOf(123456));
                } else if (EventEncoder.encode(FrameworkEntrypoint.CALLEXECUTED_EVENT)
                        .equals(logTopic)) {
                  FrameworkEntrypoint.CallExecutedEventResponse response =
                          FrameworkEntrypoint.getCallExecutedEventFromLog(Web3jUtils.fromBesuLog(log));
                  assertTrue(response.isSuccess);
                  if (logTopic.equals(createdEventSignature)) {
                    assertEquals(response.destination, this.testContext.frameworkEntryPointAccount.getAddress().toHexString());
                  } else {
                    //assertEquals(response.destination, this.testContext.initialAccounts[0].getAddress().toHexString());
                  }
                } else {
                    if (!(logTopic.equals(callEventSignature) || logTopic.equals(writeEventSignature) || logTopic.equals(readEventSignature) || logTopic.equals(destroyedEventSignature) || logTopic.equals(createdEventSignature))) {
                      fail();
                    }
                }
              }
            };

    var tx1 = writeToStorage(testContext.initialAccounts[1], testContext.initialKeyPairs[1], testContext.initialAccounts[0], 123L, 1L);
    var tx2 = writeToStorageRevert(testContext.initialAccounts[1], testContext.initialKeyPairs[1], testContext.initialAccounts[2], 123L, 1L);
    var tx3 = writeToStorage(testContext.initialAccounts[1], testContext.initialKeyPairs[1], testContext.initialAccounts[0], 123456L, 1L);
    MultiBlockExecutionEnvironment.builder()
            .accounts(List.of(this.testContext.initialAccounts[0], this.testContext.initialAccounts[1], this.testContext.frameworkEntryPointAccount))
            .addBlock(List.of(tx1, tx2, tx3))
            .transactionProcessingResultValidator(resultValidator)
            .build()
            .run();
    System.out.println("Done");
  }
}
