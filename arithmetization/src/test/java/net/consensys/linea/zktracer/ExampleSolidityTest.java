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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import net.consensys.linea.testing.SolidityUtils;
import net.consensys.linea.testing.ToyAccount;
import net.consensys.linea.testing.ToyExecutionEnvironmentV2;
import net.consensys.linea.testing.ToyTransaction;
import net.consensys.linea.testing.generated.FrameworkEntrypoint;
import net.consensys.linea.testing.generated.TestSnippet_Events;
import net.consensys.linea.testing.generated.TestStorage;
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

public class ExampleSolidityTest {

  @Test
  void testWithFrameworkEntrypoint() {
    KeyPair keyPair = new SECP256K1().generateKeyPair();
    Address senderAddress = Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));

    ToyAccount senderAccount =
        ToyAccount.builder().balance(Wei.fromEth(1)).nonce(5).address(senderAddress).build();

    ToyAccount frameworkEntrypointAccount =
        ToyAccount.builder()
            .address(Address.fromHexString("0x22222"))
            .balance(Wei.ONE)
            .nonce(5)
            .code(SolidityUtils.getContractByteCode(FrameworkEntrypoint.class))
            .build();

    ToyAccount snippetAccount =
        ToyAccount.builder()
            .address(Address.fromHexString("0x11111"))
            .balance(Wei.ONE)
            .nonce(6)
            .code(SolidityUtils.getContractByteCode(TestSnippet_Events.class))
            .build();

    Function snippetFunction =
        new Function(
            TestSnippet_Events.FUNC_EMITDATANOINDEXES,
            List.of(new Uint256(BigInteger.valueOf(123456))),
            Collections.emptyList());

    FrameworkEntrypoint.ContractCall snippetContractCall =
        new FrameworkEntrypoint.ContractCall(
            /*Address*/ snippetAccount.getAddress().toHexString(),
            /*calldata*/ Bytes.fromHexStringLenient(FunctionEncoder.encode(snippetFunction))
                .toArray(),
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
            .sender(senderAccount)
            .to(frameworkEntrypointAccount)
            .payload(txPayload)
            .keyPair(keyPair)
            .build();

    Consumer<TransactionProcessingResult> resultValidator =
        (TransactionProcessingResult result) -> {
          // One event from the snippet
          // One event from the framework entrypoint about contract call
          assertEquals(result.getLogs().size(), 2);
          for (Log log : result.getLogs()) {
            String logTopic = log.getTopics().getFirst().toHexString();
            if (EventEncoder.encode(TestSnippet_Events.DATANOINDEXES_EVENT).equals(logTopic)) {
              TestSnippet_Events.DataNoIndexesEventResponse response =
                  TestSnippet_Events.getDataNoIndexesEventFromLog(SolidityUtils.fromBesuLog(log));
              assertEquals(response.singleInt, BigInteger.valueOf(123456));
            } else if (EventEncoder.encode(FrameworkEntrypoint.CALLEXECUTED_EVENT)
                .equals(logTopic)) {
              FrameworkEntrypoint.CallExecutedEventResponse response =
                  FrameworkEntrypoint.getCallExecutedEventFromLog(SolidityUtils.fromBesuLog(log));
              assertTrue(response.isSuccess);
              assertEquals(response.destination, snippetAccount.getAddress().toHexString());
            } else {
              fail();
            }
          }
        };

    ToyExecutionEnvironmentV2.builder()
        .accounts(List.of(senderAccount, frameworkEntrypointAccount, snippetAccount))
        .transaction(tx)
        .testValidator(resultValidator)
        .build()
        .run();
  }

  @Test
  void testSnippetIndependently() {
    KeyPair keyPair = new SECP256K1().generateKeyPair();
    Address senderAddress = Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));

    ToyAccount senderAccount =
        ToyAccount.builder().balance(Wei.fromEth(1)).nonce(5).address(senderAddress).build();

    ToyAccount contractAccount =
        ToyAccount.builder()
            .address(Address.fromHexString("0x11111"))
            .balance(Wei.ONE)
            .nonce(6)
            .code(SolidityUtils.getContractByteCode(TestSnippet_Events.class))
            .build();

    Function function =
        new Function(
            TestSnippet_Events.FUNC_EMITDATANOINDEXES,
            List.of(new Uint256(BigInteger.valueOf(123456))),
            Collections.emptyList());
    String encodedFunction = FunctionEncoder.encode(function);
    Bytes txPayload = Bytes.fromHexStringLenient(encodedFunction);

    Transaction tx =
        ToyTransaction.builder()
            .sender(senderAccount)
            .to(contractAccount)
            .payload(txPayload)
            .keyPair(keyPair)
            .build();

    Consumer<TransactionProcessingResult> resultValidator =
        (TransactionProcessingResult result) -> {
          assertEquals(result.getLogs().size(), 1);
          TestSnippet_Events.DataNoIndexesEventResponse response =
              TestSnippet_Events.getDataNoIndexesEventFromLog(
                  SolidityUtils.fromBesuLog(result.getLogs().getFirst()));
          assertEquals(response.singleInt, BigInteger.valueOf(123456));
        };

    ToyExecutionEnvironmentV2.builder()
        .accounts(List.of(senderAccount, contractAccount))
        .transaction(tx)
        .testValidator(resultValidator)
        .build()
        .run();
  }

  @Test
  void testContractNotRelatedToTestingFramework() {
    KeyPair senderkeyPair = new SECP256K1().generateKeyPair();
    Address senderAddress =
        Address.extract(Hash.hash(senderkeyPair.getPublicKey().getEncodedBytes()));

    ToyAccount senderAccount =
        ToyAccount.builder().balance(Wei.fromEth(1)).nonce(5).address(senderAddress).build();

    ToyAccount contractAccount =
        ToyAccount.builder()
            .address(Address.fromHexString("0x11111"))
            .balance(Wei.ONE)
            .nonce(6)
            .code(SolidityUtils.getContractByteCode(TestStorage.class))
            .build();

    Function function =
        new Function(
            TestStorage.FUNC_STORE,
            List.of(new Uint256(BigInteger.valueOf(3))),
            Collections.emptyList());
    String encodedFunction = FunctionEncoder.encode(function);
    Bytes txPayload = Bytes.fromHexStringLenient(encodedFunction);
    Transaction tx =
        ToyTransaction.builder()
            .sender(senderAccount)
            .to(contractAccount)
            .payload(txPayload)
            .keyPair(senderkeyPair)
            .build();

    ToyExecutionEnvironmentV2.builder()
        .accounts(List.of(senderAccount, contractAccount))
        .transaction(tx)
        .build()
        .run();
  }

  @Test
  void testYul() {
    KeyPair senderkeyPair = new SECP256K1().generateKeyPair();
    Address senderAddress =
        Address.extract(Hash.hash(senderkeyPair.getPublicKey().getEncodedBytes()));

    ToyAccount senderAccount =
        ToyAccount.builder().balance(Wei.fromEth(1)).nonce(5).address(senderAddress).build();

    ToyAccount frameworkEntrypointAccount =
        ToyAccount.builder()
            .address(Address.fromHexString("0x22222"))
            .balance(Wei.ONE)
            .nonce(5)
            .code(SolidityUtils.getContractByteCode(FrameworkEntrypoint.class))
            .build();

    String dynamicBytecodeYul =
        "6005605f565b63a770741d8114601e576397deb47b8114602857600080fd5b60246039565b6034565b60005460005260206000f35b506088565b7f0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef600055565b60007c010000000000000000000000000000000000000000000000000000000060003504905090565b";

    ToyAccount yulAccount =
        ToyAccount.builder()
            .address(Address.fromHexString("0x11111"))
            .balance(Wei.ONE)
            .nonce(6)
            .code(Bytes.fromHexStringLenient(dynamicBytecodeYul))
            .build();

    Function yulFunction = new Function("Write", Collections.emptyList(), Collections.emptyList());

    String encodedContractCall = FunctionEncoder.encode(yulFunction);

    FrameworkEntrypoint.ContractCall yulContractCall =
        new FrameworkEntrypoint.ContractCall(
            /*Address*/ yulAccount.getAddress().toHexString(),
            /*calldata*/ Bytes.fromHexStringLenient(encodedContractCall).toArray(),
            /*gasLimit*/ BigInteger.ZERO,
            /*value*/ BigInteger.ZERO,
            /*callType*/ BigInteger.ZERO);

    List<FrameworkEntrypoint.ContractCall> contractCalls = List.of(yulContractCall);

    Function frameworkEntryPointFunction =
        new Function(
            FrameworkEntrypoint.FUNC_EXECUTECALLS,
            List.of(new DynamicArray<>(FrameworkEntrypoint.ContractCall.class, contractCalls)),
            Collections.emptyList());
    Bytes txPayload =
        Bytes.fromHexStringLenient(FunctionEncoder.encode(frameworkEntryPointFunction));

    Consumer<TransactionProcessingResult> resultValidator =
        (TransactionProcessingResult result) -> {
          assertFalse(result.getLogs().isEmpty());
        };

    Transaction tx =
        ToyTransaction.builder()
            .sender(senderAccount)
            .to(frameworkEntrypointAccount)
            .payload(txPayload)
            .keyPair(senderkeyPair)
            .gasLimit(500000L)
            .build();

    ToyExecutionEnvironmentV2.builder()
        .accounts(List.of(senderAccount, yulAccount))
        .transaction(tx)
        .testValidator(resultValidator)
        .build()
        .run();
  }

  @Test
  void testYulModifiedWrite() {
    KeyPair keyPair = new SECP256K1().generateKeyPair();
    Address senderAddress = Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));

    ToyAccount senderAccount =
        ToyAccount.builder().balance(Wei.fromEth(1)).nonce(5).address(senderAddress).build();

    ToyAccount frameworkEntrypointAccount =
        ToyAccount.builder()
            .address(Address.fromHexString("0x22222"))
            .balance(Wei.ONE)
            .nonce(5)
            .code(SolidityUtils.getContractByteCode(FrameworkEntrypoint.class))
            .build();

    String dynamicBytecodeYul =
        "60056057565b8063a770741d146029576397deb47b14601d57600080fd5b60005460005260206000f35b602f6031565b005b7f0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef600055565b600160e01b600035049056";

    ToyAccount yulAccount =
        ToyAccount.builder()
            .address(Address.fromHexString("0x11111"))
            .balance(Wei.ONE)
            .nonce(6)
            .code(Bytes.fromHexStringLenient(dynamicBytecodeYul))
            .build();

    Function yulFunction = new Function("Write", Collections.emptyList(), Collections.emptyList());

    FrameworkEntrypoint.ContractCall snippetContractCall =
        new FrameworkEntrypoint.ContractCall(
            /*Address*/ yulAccount.getAddress().toHexString(),
            /*calldata*/ Bytes.fromHexStringLenient(FunctionEncoder.encode(yulFunction)).toArray(),
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
            .sender(senderAccount)
            .to(frameworkEntrypointAccount)
            .payload(txPayload)
            .keyPair(keyPair)
            .gasLimit(500000L)
            .build();

    Consumer<TransactionProcessingResult> resultValidator =
        (TransactionProcessingResult result) -> {
          // One event from the snippet
          // One event from the framework entrypoint about contract call
          assertEquals(result.getLogs().size(), 1);
          for (Log log : result.getLogs()) {
            String logTopic = log.getTopics().getFirst().toHexString();
            if (EventEncoder.encode(FrameworkEntrypoint.CALLEXECUTED_EVENT).equals(logTopic)) {
              FrameworkEntrypoint.CallExecutedEventResponse response =
                  FrameworkEntrypoint.getCallExecutedEventFromLog(SolidityUtils.fromBesuLog(log));
              assertTrue(response.isSuccess);
              assertEquals(response.destination, yulAccount.getAddress().toHexString());
            } else {
              fail();
            }
          }
        };

    ToyExecutionEnvironmentV2.builder()
        .accounts(List.of(senderAccount, frameworkEntrypointAccount, yulAccount))
        .transaction(tx)
        .testValidator(resultValidator)
        .build()
        .run();
  }
}
