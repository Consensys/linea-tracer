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
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

public class StateManagerSolidityTest {
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

    Transaction tx =
            ToyTransaction.builder()
                    .sender(sender)
                    .to(this.testContext.frameworkEntryPointAccount)
                    .payload(txPayload)
                    .keyPair(senderKeyPair)
                    .gasLimit(TestContext.gasLimit)
                    .build();
    return tx;
  }

  // destination must be our .yul smart contract
  Transaction readFromStorage(ToyAccount sender, KeyPair senderKeyPair, ToyAccount destination, Long key) {
    List<org.web3j.abi.datatypes.Uint> inputParams2 = new ArrayList<>();
    List<TypeReference<?>> outputParams2 = new ArrayList<>();
    inputParams2.addLast(new Uint256(BigInteger.valueOf(1)));
    Function yulFunction2 = new Function("readFromStorage", Collections.unmodifiableList(inputParams2), outputParams2);


    var encoding2 = FunctionEncoder.encode(yulFunction2);
    FrameworkEntrypoint.ContractCall snippetContractCall =
            new FrameworkEntrypoint.ContractCall(
                    /*Address*/ destination.getAddress().toHexString(),
                    /*calldata*/ Bytes.fromHexStringLenient(encoding2).toArray(),
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

    Transaction tx =
            ToyTransaction.builder()
                    .sender(sender)
                    .to(this.testContext.frameworkEntryPointAccount)
                    .payload(txPayload)
                    .keyPair(senderKeyPair)
                    .gasLimit(TestContext.gasLimit)
                    .build();
    return tx;
  }

  // destination must be our .yul smart contract
  Transaction selfDestruct(ToyAccount sender, KeyPair senderKeyPair, ToyAccount destination, ToyAccount recipient) {
    List<org.web3j.abi.datatypes.Address> inputParams = new ArrayList<>();
    List<TypeReference<?>> outputParams = new ArrayList<>();
    String recipientAddressString = recipient.getAddress().toHexString();
    inputParams.addLast(new org.web3j.abi.datatypes.Address(recipientAddressString));
    Function yulFunction = new Function("selfDestruct", Collections.unmodifiableList(inputParams), outputParams);


    var encoding = FunctionEncoder.encode(yulFunction);
    FrameworkEntrypoint.ContractCall snippetContractCall =
            new FrameworkEntrypoint.ContractCall(
                    /*Address*/ destination.getAddress().toHexString(),
                    /*calldata*/ Bytes.fromHexStringLenient(encoding).toArray(),
                    /*gasLimit*/ BigInteger.ZERO,
                    /*value*/ BigInteger.ZERO,
                    /*callType*/ BigInteger.ONE); // Normal call, not a delegate call as it is the default

    List<FrameworkEntrypoint.ContractCall> contractCalls = List.of(snippetContractCall);
    Function frameworkEntryPointFunction =
            new Function(
                    FrameworkEntrypoint.FUNC_EXECUTECALLS,
                    List.of(new DynamicArray<>(FrameworkEntrypoint.ContractCall.class, contractCalls)),
                    Collections.emptyList());
    Bytes txPayload =
            Bytes.fromHexStringLenient(FunctionEncoder.encode(frameworkEntryPointFunction));

    Transaction tx =
            ToyTransaction.builder()
                    .sender(sender)
                    .to(this.testContext.frameworkEntryPointAccount)
                    .payload(txPayload)
                    .keyPair(senderKeyPair)
                    .gasLimit(TestContext.gasLimit)
                    .build();
    return tx;
  }


  @NoArgsConstructor
  class TestContext {
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
                      .code(SmartContractUtils.getYulContractByteCode("YulContract.yul"))
                      .build();
      // generate extra accounts
      KeyPair keyPair = new SECP256K1().generateKeyPair();
      Address senderAddress = Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));
      ToyAccount senderAccount =
              ToyAccount.builder().balance(Wei.fromEth(1)).nonce(5).address(senderAddress).build();
      // add to arrays
      initialAccounts[1] = senderAccount;
      initialKeyPairs[1] = keyPair;
    }
  }



  @Test
  void testYulModifiedWrite() {
    // initialize the test context
    this.testContext = new TestContext();
    this.testContext.initializeTestContext();




    Consumer<TransactionProcessingResult> resultValidator =
        (TransactionProcessingResult result) -> {
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

    ToyExecutionEnvironmentV2.builder()
        .accounts(List.of(this.testContext.initialAccounts[0], this.testContext.initialAccounts[1], this.testContext.frameworkEntryPointAccount))
        //.transaction(writeToStorage(testContext.initialAccounts[1], testContext.initialKeyPairs[1], testContext.initialAccounts[0], 123L, 1L))
        //    .transaction(readFromStorage(testContext.initialAccounts[1], testContext.initialKeyPairs[1], testContext.initialAccounts[0], 123L))
            .transaction(selfDestruct(testContext.initialAccounts[1], testContext.initialKeyPairs[1], testContext.initialAccounts[0], testContext.frameworkEntryPointAccount))
            .testValidator(resultValidator)
        .build()
        .run();
    System.out.println("Done");
  }
}

