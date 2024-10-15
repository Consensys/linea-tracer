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

import java.util.List;

import net.consensys.linea.testing.*;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.crypto.KeyPair;
import org.hyperledger.besu.crypto.SECP256K1;
import org.hyperledger.besu.datatypes.AccessListEntry;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.datatypes.TransactionType;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.junit.jupiter.api.Test;
import net.consensys.linea.testing.generated.FrameworkEntrypoint;
import net.consensys.linea.testing.generated.TestSnippet_Events;
import net.consensys.linea.testing.generated.TestStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;

import org.hyperledger.besu.ethereum.core.Transaction;
import org.hyperledger.besu.ethereum.processing.TransactionProcessingResult;
import org.hyperledger.besu.evm.log.Log;

class ExampleMultiBlockTest {

  @Test
  void test() {
    final ToyAccount receiverAccount =
        ToyAccount.builder()
            .balance(Wei.fromEth(1))
            .nonce(116)
            .address(Address.fromHexString("0xdead000000000000000000000000000beef"))
            .build();

    final KeyPair senderKeyPair1 = new SECP256K1().generateKeyPair();
    final Address senderAddress1 =
        Address.extract(Hash.hash(senderKeyPair1.getPublicKey().getEncodedBytes()));
    final ToyAccount senderAccount1 =
        ToyAccount.builder().balance(Wei.fromEth(123)).nonce(5).address(senderAddress1).build();

    final KeyPair senderKeyPair2 = new SECP256K1().generateKeyPair();
    final Address senderAddress2 =
        Address.extract(Hash.hash(senderKeyPair2.getPublicKey().getEncodedBytes()));
    final ToyAccount senderAccount2 =
        ToyAccount.builder().balance(Wei.fromEth(1231)).nonce(15).address(senderAddress2).build();

    final KeyPair senderKeyPair3 = new SECP256K1().generateKeyPair();
    final Address senderAddress3 =
        Address.extract(Hash.hash(senderKeyPair3.getPublicKey().getEncodedBytes()));
    final ToyAccount senderAccount3 =
        ToyAccount.builder().balance(Wei.fromEth(1231)).nonce(15).address(senderAddress3).build();

    final KeyPair senderKeyPair4 = new SECP256K1().generateKeyPair();
    final Address senderAddress4 =
        Address.extract(Hash.hash(senderKeyPair4.getPublicKey().getEncodedBytes()));
    final ToyAccount senderAccount4 =
        ToyAccount.builder().balance(Wei.fromEth(11)).nonce(115).address(senderAddress4).build();

    final KeyPair senderKeyPair5 = new SECP256K1().generateKeyPair();
    final Address senderAddress5 =
        Address.extract(Hash.hash(senderKeyPair5.getPublicKey().getEncodedBytes()));
    final ToyAccount senderAccount5 =
        ToyAccount.builder().balance(Wei.fromEth(12)).nonce(0).address(senderAddress5).build();

    final KeyPair senderKeyPair6 = new SECP256K1().generateKeyPair();
    final Address senderAddress6 =
        Address.extract(Hash.hash(senderKeyPair6.getPublicKey().getEncodedBytes()));
    final ToyAccount senderAccount6 =
        ToyAccount.builder().balance(Wei.fromEth(12)).nonce(6).address(senderAddress6).build();

    final KeyPair senderKeyPair7 = new SECP256K1().generateKeyPair();
    final Address senderAddress7 =
        Address.extract(Hash.hash(senderKeyPair7.getPublicKey().getEncodedBytes()));
    final ToyAccount senderAccount7 =
        ToyAccount.builder().balance(Wei.fromEth(231)).nonce(21).address(senderAddress7).build();

    final Transaction pureTransfer =
        ToyTransaction.builder()
            .sender(senderAccount1)
            .to(receiverAccount)
            .keyPair(senderKeyPair1)
            .value(Wei.of(123))
            .build();

    final Transaction pureTransferWoValue =
        ToyTransaction.builder()
            .sender(senderAccount2)
            .to(receiverAccount)
            .keyPair(senderKeyPair2)
            .value(Wei.of(0))
            .build();

    final List<String> listOfKeys =
        List.of("0x0123", "0xdeadbeefdeadbeefdeadbeefdeadbeefdeadbeefdeadbeefdeadbeefdeadbeef");
    final List<AccessListEntry> accessList =
        List.of(
            AccessListEntry.createAccessListEntry(
                Address.fromHexString("0x1234567890"), listOfKeys));

    final Transaction pureTransferWithUselessAccessList =
        ToyTransaction.builder()
            .sender(senderAccount3)
            .to(receiverAccount)
            .keyPair(senderKeyPair3)
            .gasLimit(100000L)
            .transactionType(TransactionType.ACCESS_LIST)
            .accessList(accessList)
            .value(Wei.of(546))
            .build();

    final Transaction pureTransferWithUselessCalldata =
        ToyTransaction.builder()
            .sender(senderAccount4)
            .to(receiverAccount)
            .keyPair(senderKeyPair4)
            .gasLimit(1000001L)
            .value(Wei.of(546))
            .payload(Bytes.minimalBytes(0xdeadbeefL))
            .build();

    final Transaction pureTransferWithUselessCalldataAndAccessList =
        ToyTransaction.builder()
            .sender(senderAccount5)
            .to(receiverAccount)
            .gasLimit(1000020L)
            .transactionType(TransactionType.EIP1559)
            .keyPair(senderKeyPair5)
            .value(Wei.of(546))
            .accessList(accessList)
            .payload(Bytes.minimalBytes(0xdeadbeefL))
            .build();

    MultiBlockExecutionEnvironment.MultiBlockExecutionEnvironmentBuilder builder =
        MultiBlockExecutionEnvironment.builder();
    builder
        .accounts(
            List.of(
                senderAccount1,
                senderAccount2,
                senderAccount3,
                senderAccount4,
                senderAccount5,
                senderAccount6,
                senderAccount7,
                receiverAccount))
        .addBlock(List.of(pureTransfer))
        .addBlock(List.of(pureTransferWoValue, pureTransferWithUselessAccessList))
        .addBlock(
            List.of(pureTransferWithUselessCalldata, pureTransferWithUselessCalldataAndAccessList))
        .build()
        .run();
  }

  @Test
  void test2() {
    KeyPair keyPair = new SECP256K1().generateKeyPair();
    Address senderAddress = Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));

    ToyAccount senderAccount =
        ToyAccount.builder().balance(Wei.fromEth(1)).nonce(5).address(senderAddress).build();

    ToyAccount receiverAccount =
        ToyAccount.builder()
            .balance(Wei.ONE)
            .nonce(6)
            .address(Address.fromHexString("0x111111"))
            .code(
                BytecodeCompiler.newProgram()
                    .push(32, 0xbeef)
                    .push(32, 0xdead)
                    .op(OpCode.ADD)
                    .compile())
            .build();

    Transaction tx =
        ToyTransaction.builder().sender(senderAccount).to(receiverAccount).keyPair(keyPair).build();

    MultiBlockExecutionEnvironment.builder()
        .accounts(List.of(senderAccount, receiverAccount))
        .addBlock(List.of(tx))
        .build()
        .run();
  }

  @Test
  void testWithFrameworkEntrypoint() {
    KeyPair keyPair = new SECP256K1().generateKeyPair();
    Address senderAddress = Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));

    ToyAccount senderAccount =
            ToyAccount.builder().balance(Wei.fromEth(1000)).nonce(5).address(senderAddress).build();

    ToyAccount frameworkEntrypointAccount =
            ToyAccount.builder()
                    .address(Address.fromHexString("0x22222"))
                    .balance(Wei.of(1000))
                    .nonce(6)
                    .code(SmartContractUtils.getSolidityContractByteCode(FrameworkEntrypoint.class))
                    .build();

    ToyAccount snippetAccount =
            ToyAccount.builder()
                    .address(Address.fromHexString("0x11111"))
                    .balance(Wei.of(1000))
                    .nonce(7)
                    .code(SmartContractUtils.getSolidityContractByteCode(TestSnippet_Events.class))
                    .build();

    Function snippetFunction =
            new Function(
                    TestSnippet_Events.FUNC_EMITDATANOINDEXES,
                    List.of(new Uint256(BigInteger.valueOf(123456))),
                    Collections.emptyList());

    Function snippetFunction2 =
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

    FrameworkEntrypoint.ContractCall snippetContractCall2 =
            new FrameworkEntrypoint.ContractCall(
                    /*Address*/ snippetAccount.getAddress().toHexString(),
                    /*calldata*/ Bytes.fromHexStringLenient(FunctionEncoder.encode(snippetFunction2))
                    .toArray(),
                    /*gasLimit*/ BigInteger.ZERO,
                    /*value*/ BigInteger.ZERO,
                    /*callType*/ BigInteger.ZERO);

    List<FrameworkEntrypoint.ContractCall> contractCalls = List.of(snippetContractCall);
    List<FrameworkEntrypoint.ContractCall> contractCalls2 = List.of(snippetContractCall2);

    Function frameworkEntryPointFunction =
            new Function(
                    FrameworkEntrypoint.FUNC_EXECUTECALLS,
                    List.of(new DynamicArray<>(FrameworkEntrypoint.ContractCall.class, contractCalls)),
                    Collections.emptyList());
    Function frameworkEntryPointFunction2 =
            new Function(
                    FrameworkEntrypoint.FUNC_EXECUTECALLS,
                    List.of(new DynamicArray<>(FrameworkEntrypoint.ContractCall.class, contractCalls2)),
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

    Bytes txPayload2 =
            Bytes.fromHexStringLenient(FunctionEncoder.encode(frameworkEntryPointFunction2));

    Transaction tx2 =
            ToyTransaction.builder()
                    .sender(senderAccount)
                    .to(frameworkEntrypointAccount)
                    .payload(txPayload2)
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
                          TestSnippet_Events.getDataNoIndexesEventFromLog(Web3jUtils.fromBesuLog(log));
                  assertEquals(response.singleInt, BigInteger.valueOf(123456));
                } else if (EventEncoder.encode(FrameworkEntrypoint.CALLEXECUTED_EVENT)
                        .equals(logTopic)) {
                  FrameworkEntrypoint.CallExecutedEventResponse response =
                          FrameworkEntrypoint.getCallExecutedEventFromLog(Web3jUtils.fromBesuLog(log));
                  assertTrue(response.isSuccess);
                  assertEquals(response.destination, snippetAccount.getAddress().toHexString());
                } else {
                  fail();
                }
              }
            };

    MultiBlockExecutionEnvironment.builder()
            .accounts(List.of(senderAccount, frameworkEntrypointAccount, snippetAccount))
            .addBlock(List.of(tx))
            .addBlock(List.of(tx2))
            .build()
            .run();
  }
}
