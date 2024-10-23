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
import net.consensys.linea.testing.generated.TestingBase;
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
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.*;

public class StateManagerSolidityTest {
  TestContext testContext;
  enum RevertFlag {
    DISABLED,
    ENABLED
  }
  String boolToHexString(boolean x) {
    if (x) {
      return "0x0000000000000000000000000000000000000000000000000000000000000001";
    }
    else {
      return "0x0000000000000000000000000000000000000000000000000000000000000000";
    }
  }



  // destination must be our .yul smart contract
  Transaction writeToStorage(ToyAccount sender, KeyPair senderKeyPair, ToyAccount destination, Long key, Long value, boolean revertFlag) {
    Function yulFunction = new Function("writeToStorage",
            Arrays.asList(new Uint256(BigInteger.valueOf(key)), new Uint256(BigInteger.valueOf(value)), new org.web3j.abi.datatypes.Bool(revertFlag)),
            Collections.emptyList());

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


  // destination must be our .yul smart contract
  Transaction readFromStorage(ToyAccount sender, KeyPair senderKeyPair, ToyAccount destination, Long key, boolean revertFlag) {
    Function yulFunction = new Function("readFromStorage",
            Arrays.asList(new Uint256(BigInteger.valueOf(key)), new org.web3j.abi.datatypes.Bool(revertFlag)),
            Collections.emptyList());


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

  // destination must be our .yul smart contract
  Transaction selfDestruct(ToyAccount sender, KeyPair senderKeyPair, ToyAccount destination, ToyAccount recipient, boolean revertFlag) {
    String recipientAddressString = recipient.getAddress().toHexString();
    Function yulFunction = new Function("selfDestruct",
            Arrays.asList(new org.web3j.abi.datatypes.Address(recipientAddressString), new org.web3j.abi.datatypes.Bool(revertFlag)),
            Collections.emptyList());


    var encoding = FunctionEncoder.encode(yulFunction);
    FrameworkEntrypoint.ContractCall snippetContractCall =
            new FrameworkEntrypoint.ContractCall(
                    /*Address*/ destination.getAddress().toHexString(),
                    /*calldata*/ Bytes.fromHexStringLenient(encoding).toArray(),
                    /*gasLimit*/ BigInteger.ZERO,
                    /*value*/ BigInteger.ZERO,
                    /*callType*/ BigInteger.ONE); // Normal call, not a delegate call as would be the default

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

  // destination must be our .yul smart contract
  Transaction deployWithCreate2(ToyAccount sender, KeyPair senderKeyPair, ToyAccount destination, String saltString) {
    Bytes salt = Bytes.fromHexStringLenient(saltString);
    // the following are the bytecode of the .yul contract
    Bytes yulContractBytes = Bytes.fromHexStringLenient("61037d61001060003961037d6000f3fe6100076102e1565b63a770741d8114610064576397deb47b81146100715763acf07154811461007d57632d97bf1081146100b45763eba7ff7f81146100e757632b261e94811461012157633ecfd51e811461015b5763ffffffff811461017057600080fd5b61006c610177565b610171565b60005460005260206000f35b6004356024356044356100918183856102c7565b61009b828461019d565b600181036100ac576100ab61034d565b5b505050610171565b6004356024356100c481836102cf565b6100ce81846101da565b600182036100df576100de61034d565b5b505050610171565b6004356024356100f682610253565b600081036101095761010881836102db565b5b6001810361011a5761011961034d565b5b5050610171565b6004356024356044353061013682848661030a565b610141838583610217565b600182036101525761015161034d565b5b50505050610171565b61016361028b565b61016b61037a565b610171565b5b5061037c565b7f0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef600055565b7f577269746528616464726573732c75696e743235362c75696e74323536290000604051818152601e8120308585828460606020a4505050505050565b7f5265616428616464726573732c75696e743235362c75696e7432353629202020604051818152601d8120308585828460606020a4505050505050565b7f50617945544828616464726573732c616464726573732c75696e743235362900604051818152601f81208585858360606020a4505050505050565b7f436f6e747261637444657374726f796564286164647265737329000000000000604051818152601a8120838160606020a250505050565b7f52656345544828616464726573732c75696e743235362900000000000000000060405181815260178120303480828460606020a35050505050565b818155505050565b60008154905092915050565b80ff5050565b60007c010000000000000000000000000000000000000000000000000000000060003504905090565b6040517f3ecfd51e0000000000000000000000000000000000000000000000000000000080825260008060208487875af18061034557600080fd5b505050505050565b7f526576657274696e67000000000000000000000000000000000000000000000060206040518281528181fd5b565b");
    // prepare the Create2 function
    Function create2Function =
            new Function(
                    FrameworkEntrypoint.FUNC_DEPLOYWITHCREATE2,
                    Arrays.asList(new org.web3j.abi.datatypes.generated.Bytes32(salt.toArray()),
                            new org.web3j.abi.datatypes.DynamicBytes(yulContractBytes.toArray())),
                    Collections.emptyList());

    String encoding = FunctionEncoder.encode(create2Function);

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
    static final Long gasLimit = 5000000L;
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
                      .code(SmartContractUtils.getYulContractByteCode("StateManagerSnippets.yul"))
                      .build();
    }
  }



  @Test
  void testYulModifiedWrite() {
    // initialize the test context
    this.testContext = new TestContext();
    this.testContext.initializeTestContext();



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
    MultiBlockExecutionEnvironment.builder()
            .accounts(List.of(this.testContext.initialAccounts[0], this.testContext.initialAccounts[1], this.testContext.frameworkEntryPointAccount))
            .addBlock(List.of(writeToStorage(testContext.initialAccounts[1], testContext.initialKeyPairs[1], testContext.initialAccounts[0], 123L, 1L, false)))
            .addBlock(List.of(readFromStorage(testContext.initialAccounts[1], testContext.initialKeyPairs[1], testContext.initialAccounts[0], 123L, false)))
            .addBlock(List.of(selfDestruct(testContext.initialAccounts[1], testContext.initialKeyPairs[1], testContext.initialAccounts[0], testContext.frameworkEntryPointAccount, false)))
            .addBlock(List.of(deployWithCreate2(testContext.initialAccounts[1], testContext.initialKeyPairs[1], testContext.frameworkEntryPointAccount, "0x0000000000000000000000000000000000000000000000000000000000000002")))
            .transactionProcessingResultValidator(resultValidator)
            .build()
            .run();
    System.out.println("Done");
  }
}