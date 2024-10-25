/*
 * Copyright ConsenSys Inc.
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
package net.consensys.linea.zktracer.module.hub;

import java.util.List;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.MultiBlockExecutionEnvironment;
import net.consensys.linea.testing.ToyAccount;
import net.consensys.linea.testing.ToyTransaction;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.crypto.KeyPair;
import org.hyperledger.besu.crypto.SECP256K1;
import org.hyperledger.besu.datatypes.*;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.junit.jupiter.api.Test;

public class StateManagerStorageTest {

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
  private final String keyString =
      "0x00010203040060708090A0B0C0DE0F10101112131415161718191A1B1C1D1E1F";
  private final Bytes32 key = Bytes32.fromHexString(keyString);
  private final Bytes32 value1 = Bytes32.repeat((byte) 1);
  private final Bytes32 value2 = Bytes32.repeat((byte) 2);

  private final Address receiverAddress =
      Address.fromHexString("0x00000bad0000000000000000000000000000b077");
  final ToyAccount receiverAccount =
      ToyAccount.builder()
          .balance(Wei.fromEth(1))
          .address(receiverAddress)
          .code(
              BytecodeCompiler.newProgram()
                  // SLOAD initial value
                  .push(key)
                  .op(OpCode.SLOAD)
                  .op(OpCode.POP)
                  // SSTORE value 1
                  .push(value1)
                  .push(key)
                  .op(OpCode.SSTORE)
                  // SSTORE value 2
                  .push(value2)
                  .push(key)
                  .op(OpCode.SSTORE)
                  .compile())
          .nonce(116)
          .build();
  final List<String> simpleKey = List.of(keyString);
  final List<String> duplicateKey = List.of(keyString, keyString);
  final List<String> simpleKeyAndTrash =
      List.of(keyString, "0xdeadbeefdeadbeefdeadbeefdeadbeefdeadbeefdeadbeefdeadbeefdeadbeef");

  final List<AccessListEntry> warmOnlyReceiver =
      List.of(AccessListEntry.createAccessListEntry(receiverAddress, simpleKey));

  final List<AccessListEntry> stupidWarmer =
      List.of(
          AccessListEntry.createAccessListEntry(receiverAddress, duplicateKey),
          AccessListEntry.createAccessListEntry(senderAddress4, simpleKeyAndTrash),
          AccessListEntry.createAccessListEntry(senderAddress1, simpleKeyAndTrash),
          AccessListEntry.createAccessListEntry(senderAddress3, simpleKey),
          AccessListEntry.createAccessListEntry(senderAddress7, duplicateKey));

  @Test
  void test_simple() {

    System.out.println("Sender address1: " + senderAddress1);
    System.out.println("Sender address2: " + senderAddress2);
    System.out.println("Sender address3: " + senderAddress3);
    System.out.println("Sender address4: " + senderAddress4);
    System.out.println("Sender address5: " + senderAddress5);
    System.out.println("Sender address6: " + senderAddress6);
    System.out.println("Sender address7: " + senderAddress7);
    final Transaction pureTransferSender1 =
        ToyTransaction.builder()
            .sender(senderAccount1)
            .to(receiverAccount)
            .keyPair(senderKeyPair1)
            .value(Wei.of(123))
            .build();
    final Transaction pureTransferSender2 =
        ToyTransaction.builder()
            .sender(senderAccount2)
            .to(receiverAccount)
            .keyPair(senderKeyPair2)
            .value(Wei.of(120))
            .build();
    final Transaction pureTransferSender3 =
        ToyTransaction.builder()
            .sender(senderAccount3)
            .to(receiverAccount)
            .keyPair(senderKeyPair3)
            .value(Wei.of(110))
            .build();
    final Transaction pureTransferSender4 =
        ToyTransaction.builder()
            .sender(senderAccount4)
            .to(receiverAccount)
            .keyPair(senderKeyPair4)
            .value(Wei.of(10))
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
        .addBlock(List.of(pureTransferSender1, pureTransferSender2))
        .addBlock(List.of(pureTransferSender3, pureTransferSender4))
        .build()
        .run();
  }

  @Test
  void test_warming() {
    System.out.println("Sender address1: " + senderAddress1);
    System.out.println("Sender address2: " + senderAddress2);
    System.out.println("Sender address3: " + senderAddress3);
    System.out.println("Sender address4: " + senderAddress4);
    System.out.println("Sender address5: " + senderAddress5);
    System.out.println("Sender address6: " + senderAddress6);
    System.out.println("Sender address7: " + senderAddress7);
    final Transaction simpleWarm =
        ToyTransaction.builder()
            .sender(senderAccount1)
            .to(receiverAccount)
            .keyPair(senderKeyPair1)
            .gasLimit(1000000L)
            .transactionType(TransactionType.ACCESS_LIST)
            .accessList(warmOnlyReceiver)
            .value(Wei.of(50000))
            .build();

    final Transaction stupidWarm =
        ToyTransaction.builder()
            .sender(senderAccount2)
            .to(receiverAccount)
            .keyPair(senderKeyPair2)
            .gasLimit(1000000L)
            .transactionType(TransactionType.ACCESS_LIST)
            .accessList(stupidWarmer)
            .value(Wei.of(50000))
            .build();

    final Transaction noWarm =
        ToyTransaction.builder()
            .sender(senderAccount3)
            .to(receiverAccount)
            .keyPair(senderKeyPair3)
            .gasLimit(1000000L)
            .transactionType(TransactionType.ACCESS_LIST)
            .accessList(List.of())
            .value(Wei.of(50000))
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
        .addBlock(List.of(simpleWarm, noWarm))
        .addBlock(List.of(stupidWarm))
        .build()
        .run();
  }
}
