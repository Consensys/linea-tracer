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

import static net.consensys.linea.testing.ToyExecutionEnvironment.DEFAULT_COINBASE_ADDRESS;

import java.util.List;

import net.consensys.linea.testing.ToyAccount;
import net.consensys.linea.testing.ToyExecutionEnvironmentV2;
import net.consensys.linea.testing.ToyTransaction;
import net.consensys.linea.testing.ToyWorld;
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

public class TxSkipTests {

  @Test
  void test() {
    final ToyAccount receiverAccount =
        ToyAccount.builder()
            .balance(Wei.fromEth(1))
            .nonce(116)
            .address(Address.fromHexString("0xdead000000000000000000000000000beef"))
            .build();

    final ToyAccount coinbaseAccount =
        ToyAccount.builder()
            .address(DEFAULT_COINBASE_ADDRESS)
            .balance(Wei.fromEth(2))
            .nonce(5)
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

    final Transaction deploymentWithEmptyInit =
        ToyTransaction.builder()
            .sender(senderAccount6)
            .keyPair(senderKeyPair6)
            .gasLimit(1234567L)
            .value(Wei.of(546))
            .build();

    final Transaction deploymentWithEmptyInitAndUselessAccessList =
        ToyTransaction.builder()
            .sender(senderAccount7)
            .gasLimit(100002120L)
            .keyPair(senderKeyPair7)
            .transactionType(TransactionType.ACCESS_LIST)
            .value(Wei.of(546))
            .accessList(accessList)
            .build();

    final List<Transaction> txs =
        List.of(
            pureTransfer,
            pureTransferWoValue,
            pureTransferWithUselessAccessList,
            pureTransferWithUselessCalldata,
            pureTransferWithUselessCalldataAndAccessList
            // TODO: deploymentWithEmptyInit,
            // TODO:  deploymentWithEmptyInitAndUselessAccessList
            );

    ToyWorld toyWorld =
        ToyWorld.builder()
            .accounts(
                List.of(
                    coinbaseAccount,
                    senderAccount1,
                    senderAccount2,
                    senderAccount3,
                    senderAccount4,
                    senderAccount5,
                    senderAccount6,
                    senderAccount7,
                    receiverAccount))
            .build();

    ToyExecutionEnvironmentV2.builder()
        .toyWorld(toyWorld)
        .transactions(txs)
        .zkTracerValidator(
            zkTracer -> {

              // Ensure we have the right nb of rows in the HUB
              // assertThat(zkTracer.getHub().lineCount()).isEqualTo(txs.size() * nbOfRowsTxSkip);
            })
        .build()
        .run();
  }
}
