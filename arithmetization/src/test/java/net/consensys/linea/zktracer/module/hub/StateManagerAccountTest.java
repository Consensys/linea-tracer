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

import net.consensys.linea.testing.MultiBlockExecutionEnvironment;
import net.consensys.linea.testing.ToyAccount;
import net.consensys.linea.testing.ToyTransaction;
import org.hyperledger.besu.crypto.KeyPair;
import org.hyperledger.besu.crypto.SECP256K1;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.junit.jupiter.api.Test;

public class StateManagerAccountTest {

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
    System.out.println("Sender address1: " + senderAddress1);
    final ToyAccount senderAccount1 =
        ToyAccount.builder().balance(Wei.fromEth(123)).nonce(5).address(senderAddress1).build();

    final KeyPair senderKeyPair2 = new SECP256K1().generateKeyPair();
    final Address senderAddress2 =
        Address.extract(Hash.hash(senderKeyPair2.getPublicKey().getEncodedBytes()));
    System.out.println("Sender address2: " + senderAddress2);
    final ToyAccount senderAccount2 =
        ToyAccount.builder().balance(Wei.fromEth(1231)).nonce(15).address(senderAddress2).build();

    final KeyPair senderKeyPair3 = new SECP256K1().generateKeyPair();
    final Address senderAddress3 =
        Address.extract(Hash.hash(senderKeyPair3.getPublicKey().getEncodedBytes()));
    System.out.println("Sender address3: " + senderAddress3);
    final ToyAccount senderAccount3 =
        ToyAccount.builder().balance(Wei.fromEth(1231)).nonce(15).address(senderAddress3).build();

    final KeyPair senderKeyPair4 = new SECP256K1().generateKeyPair();
    final Address senderAddress4 =
        Address.extract(Hash.hash(senderKeyPair4.getPublicKey().getEncodedBytes()));
    System.out.println("Sender address4: " + senderAddress4);
    final ToyAccount senderAccount4 =
        ToyAccount.builder().balance(Wei.fromEth(11)).nonce(115).address(senderAddress4).build();

    final KeyPair senderKeyPair5 = new SECP256K1().generateKeyPair();
    final Address senderAddress5 =
        Address.extract(Hash.hash(senderKeyPair5.getPublicKey().getEncodedBytes()));
    System.out.println("Sender address5: " + senderAddress5);
    final ToyAccount senderAccount5 =
        ToyAccount.builder().balance(Wei.fromEth(12)).nonce(0).address(senderAddress5).build();

    final KeyPair senderKeyPair6 = new SECP256K1().generateKeyPair();
    final Address senderAddress6 =
        Address.extract(Hash.hash(senderKeyPair6.getPublicKey().getEncodedBytes()));
    System.out.println("Sender address6: " + senderAddress6);
    final ToyAccount senderAccount6 =
        ToyAccount.builder().balance(Wei.fromEth(12)).nonce(6).address(senderAddress6).build();

    final KeyPair senderKeyPair7 = new SECP256K1().generateKeyPair();
    final Address senderAddress7 =
        Address.extract(Hash.hash(senderKeyPair7.getPublicKey().getEncodedBytes()));
    System.out.println("Sender address7: " + senderAddress7);
    final ToyAccount senderAccount7 =
        ToyAccount.builder().balance(Wei.fromEth(231)).nonce(21).address(senderAddress7).build();

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
}
