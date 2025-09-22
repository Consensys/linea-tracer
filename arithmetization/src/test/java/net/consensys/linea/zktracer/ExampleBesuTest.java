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

import net.consensys.linea.reporting.TracerTestBase;
import net.consensys.linea.testing.*;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.crypto.KeyPair;
import org.hyperledger.besu.crypto.SECP256K1;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

public class ExampleBesuTest extends TracerTestBase {

  @Test
  void testPerFork(TestInfo testInfo) {
    KeyPair keyPair = new SECP256K1().generateKeyPair();
    Address senderAddress = Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));

    ToyAccount senderAccount =
        ToyAccount.builder().balance(Wei.fromEth(1)).nonce(5).address(senderAddress).build();

    BytecodeCompiler compiler =
        BytecodeCompiler.newProgram(chainConfig).push(32, 0xbeef).push(32, 0xdead).op(OpCode.ADD);

    switch (fork) {
      case LONDON -> {}
      case PARIS -> compiler.op(OpCode.PREVRANDAO);
      case SHANGHAI -> compiler.op(OpCode.PUSH0);
      case CANCUN, PRAGUE -> compiler.op(OpCode.MCOPY);
      default -> throw new IllegalArgumentException("Unsupported fork: " + fork);
    }

    ToyAccount receiverAccount =
        ToyAccount.builder()
            .balance(Wei.ONE)
            .nonce(6)
            .address(Address.fromHexString("0x111111"))
            .code(compiler.compile())
            .build();

    Transaction tx =
        ToyTransaction.builder().sender(senderAccount).to(receiverAccount).keyPair(keyPair).build();

    ToyExecutionEnvironmentV2.builder(chainConfig, testInfo)
        .accounts(List.of(senderAccount, receiverAccount))
        .transaction(tx)
        .runWithBesuNode(true)
        .build()
        .run();
  }

  @Test
  void testForkSwitchParisToCancun(TestInfo testInfo) {
    KeyPair keyPair = new SECP256K1().generateKeyPair();
    Address senderAddress = Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));

    ToyAccount senderAccount =
        ToyAccount.builder().balance(Wei.fromEth(1)).nonce(5).address(senderAddress).build();

    BytecodeCompiler compilerMain =
        BytecodeCompiler.newProgram(chainConfig).push(32, 0xbeef).push(32, 0xdead).op(OpCode.ADD);

    // PREVRANDAO opcode
    Bytes codeParis = Bytes.concatenate(compilerMain.compile(), Bytes.fromHexString("0x44"));

    // PUSH0
    Bytes codeShanghai = Bytes.concatenate(compilerMain.compile(), Bytes.fromHexString("0x5F"));

    // MCOPY
    Bytes codeCancun = Bytes.concatenate(compilerMain.compile(), Bytes.fromHexString("0x5E"));

    ToyAccount receiverAccountParis = getReceiverAccount("0x111120", codeParis);

    ToyAccount receiverAccountShanghai = getReceiverAccount("0x111111", codeShanghai);

    ToyAccount receiverAccountCancun = getReceiverAccount("0x111112", codeCancun);

    ToyTransaction.ToyTransactionBuilder txBuilderParis =
        ToyTransaction.builder().to(receiverAccountParis).keyPair(keyPair);

    ToyTransaction.ToyTransactionBuilder txBuilderShanghai =
        ToyTransaction.builder().to(receiverAccountShanghai).keyPair(keyPair);

    ToyTransaction.ToyTransactionBuilder txBuilderCancun =
        ToyTransaction.builder().to(receiverAccountCancun).keyPair(keyPair);

    // create transactions with the same sender, manages nonce
    final List<Transaction> transactions =
        ToyMultiTransaction.builder()
            .build(List.of(txBuilderParis, txBuilderShanghai, txBuilderCancun), senderAccount);

    ToyExecutionEnvironmentV2.builder(chainConfig, testInfo)
        .accounts(
            List.of(
                senderAccount,
                receiverAccountParis,
                receiverAccountShanghai,
                receiverAccountCancun))
        .transactions(transactions)
        .runWithBesuNode(true)
        .oneTxPerBlockOnBesuNode(true)
        .customBesuNodeGenesis("BesuExecutionToolsGenesis_ParisToCancun.json")
        .build()
        .run();
  }

  @Test
  void testForkSwitchLondonToParis(TestInfo testInfo) {
    KeyPair keyPair = new SECP256K1().generateKeyPair();
    Address senderAddress = Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));

    ToyAccount senderAccount =
        ToyAccount.builder().balance(Wei.fromEth(1)).nonce(5).address(senderAddress).build();

    BytecodeCompiler compilerMain =
        BytecodeCompiler.newProgram(chainConfig).push(32, 0xbeef).push(32, 0xdead).op(OpCode.ADD);

    // PREVRANDAO opcode
    Bytes codeParis = Bytes.concatenate(compilerMain.compile(), Bytes.fromHexString("0x44"));

    ToyAccount receiverAccountLondon = getReceiverAccount("0x111100", compilerMain.compile());

    ToyAccount receiverAccountParis = getReceiverAccount("0x111120", codeParis);

    ToyTransaction.ToyTransactionBuilder txBuilderLondon =
        ToyTransaction.builder().to(receiverAccountLondon).keyPair(keyPair);

    ToyTransaction.ToyTransactionBuilder txBuilderParis =
        ToyTransaction.builder().to(receiverAccountParis).keyPair(keyPair);

    // create transactions with the same sender, manages nonce
    final List<Transaction> transactions =
        ToyMultiTransaction.builder()
            .build(List.of(txBuilderLondon, txBuilderParis), senderAccount);

    ToyExecutionEnvironmentV2.builder(chainConfig, testInfo)
        .accounts(List.of(senderAccount, receiverAccountLondon, receiverAccountParis))
        .transactions(transactions)
        .runWithBesuNode(true)
        .oneTxPerBlockOnBesuNode(true)
        .customBesuNodeGenesis("BesuExecutionToolsGenesis_LondonToParis.json")
        .build()
        .run();
  }

  private ToyAccount getReceiverAccount(String address, Bytes code) {
    return ToyAccount.builder()
        .balance(Wei.ONE)
        .nonce(6)
        .address(Address.fromHexString(address))
        .code(code)
        .build();
  }
}
