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

package net.consensys.linea.zktracer.module.blockhash;

import static net.consensys.linea.zktracer.Trace.BLOCKHASH_MAX_HISTORY;

import java.util.List;

import net.consensys.linea.UnitTestWatcher;
import net.consensys.linea.reporting.TracerTestBase;
import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.ToyAccount;
import net.consensys.linea.testing.ToyExecutionEnvironmentV2;
import net.consensys.linea.testing.ToyTransaction;
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
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(UnitTestWatcher.class)
public class BlockhashTest extends TracerTestBase {

  @Test
  void severalBlockhash(TestInfo testInfo) {

    final KeyPair keyPair = new SECP256K1().generateKeyPair();
    final Address senderAddress =
        Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));
    final ToyAccount senderAccount =
        ToyAccount.builder().balance(Wei.fromEth(1)).nonce(5).address(senderAddress).build();

    // arg is NUMBER - 1 (happy path)
    final ToyAccount receiverAccount1 =
        getReceiverAccount(
            1,
            BytecodeCompiler.newProgram(chainConfig)
                .push(1)
                .op(OpCode.NUMBER)
                .op(OpCode.SUB)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)
                .compile());
    final Transaction tx1 =
        ToyTransaction.builder()
            .sender(senderAccount)
            .to(receiverAccount1)
            .keyPair(keyPair)
            .build();

    // arg is NUMBER
    final ToyAccount receiverAccount2 =
        getReceiverAccount(
            2,
            BytecodeCompiler.newProgram(chainConfig)
                .op(OpCode.NUMBER)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)
                .compile());
    final Transaction tx2 =
        ToyTransaction.builder()
            .sender(senderAccount)
            .to(receiverAccount2)
            .keyPair(keyPair)
            .build();

    // arg is NUMBER + 1
    final ToyAccount receiverAccount3 =
        getReceiverAccount(
            3,
            BytecodeCompiler.newProgram(chainConfig)
                .op(OpCode.NUMBER)
                .push(1)
                .op(OpCode.ADD)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)
                .compile());
    final Transaction tx3 =
        ToyTransaction.builder()
            .sender(senderAccount)
            .to(receiverAccount3)
            .keyPair(keyPair)
            .build();

    // arg is 0 << NUMBER
    final ToyAccount receiverAccount4 =
        getReceiverAccount(
            4,
            BytecodeCompiler.newProgram(chainConfig)
                .push(0)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)
                .compile());
    final Transaction tx4 =
        ToyTransaction.builder()
            .sender(senderAccount)
            .to(receiverAccount4)
            .keyPair(keyPair)
            .build();

    // arg is 1 << NUMBER
    final ToyAccount receiverAccount5 =
        getReceiverAccount(
            5,
            BytecodeCompiler.newProgram(chainConfig)
                .push(1)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)
                .compile());
    final Transaction tx5 =
        ToyTransaction.builder()
            .sender(senderAccount)
            .to(receiverAccount5)
            .keyPair(keyPair)
            .build();

    // arg is ridiculously big
    final ToyAccount receiverAccount6 =
        getReceiverAccount(
            6,
            BytecodeCompiler.newProgram(chainConfig)
                .push(
                    Bytes.fromHexString(
                        "0x123456789012345678901234567890123456789012345678901234567890ffff"))
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)
                .compile());
    final Transaction tx6 =
        ToyTransaction.builder()
            .sender(senderAccount)
            .to(receiverAccount6)
            .keyPair(keyPair)
            .build();

    // arg of BlockHash is NUMBER - (256 + 2)
    final ToyAccount receiverAccount7 =
        getReceiverAccount(
            7,
            BytecodeCompiler.newProgram(chainConfig)
                .push(BLOCKHASH_MAX_HISTORY + 2)
                .op(OpCode.NUMBER)
                .op(OpCode.SUB)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)
                .compile());
    final Transaction tx7 =
        ToyTransaction.builder()
            .sender(senderAccount)
            .to(receiverAccount7)
            .keyPair(keyPair)
            .build();

    // arg of BlockHash is NUMBER - (256 + 1)
    final ToyAccount receiverAccount8 =
        getReceiverAccount(
            8,
            BytecodeCompiler.newProgram(chainConfig)
                .push(BLOCKHASH_MAX_HISTORY + 1)
                .op(OpCode.NUMBER)
                .op(OpCode.SUB)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)
                .compile());
    final Transaction tx8 =
        ToyTransaction.builder()
            .sender(senderAccount)
            .to(receiverAccount8)
            .keyPair(keyPair)
            .build();

    // arg of BlockHash is NUMBER - 256
    final ToyAccount receiverAccount9 =
        getReceiverAccount(
            9,
            BytecodeCompiler.newProgram(chainConfig)
                .push(BLOCKHASH_MAX_HISTORY)
                .op(OpCode.NUMBER)
                .op(OpCode.SUB)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)
                .compile());
    final Transaction tx9 =
        ToyTransaction.builder()
            .sender(senderAccount)
            .to(receiverAccount9)
            .keyPair(keyPair)
            .build();

    // arg of BlockHash is NUMBER - (256 - 1)
    final ToyAccount receiverAccount10 =
        getReceiverAccount(
            10,
            BytecodeCompiler.newProgram(chainConfig)
                .push(BLOCKHASH_MAX_HISTORY - 1)
                .op(OpCode.NUMBER)
                .op(OpCode.SUB)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)
                .compile());
    final Transaction tx10 =
        ToyTransaction.builder()
            .sender(senderAccount)
            .to(receiverAccount10)
            .keyPair(keyPair)
            .build();

    // arg of BlockHash is NUMBER - (256 - 2)
    final ToyAccount receiverAccount11 =
        getReceiverAccount(
            11,
            BytecodeCompiler.newProgram(chainConfig)
                .push(BLOCKHASH_MAX_HISTORY - 2)
                .op(OpCode.NUMBER)
                .op(OpCode.SUB)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)
                .compile());
    final Transaction tx11 =
        ToyTransaction.builder()
            .sender(senderAccount)
            .to(receiverAccount11)
            .keyPair(keyPair)
            .build();

    ToyExecutionEnvironmentV2.builder(chainConfig, testInfo)
        .accounts(
            List.of(
                senderAccount,
                receiverAccount1,
                receiverAccount2,
                receiverAccount3,
                receiverAccount4,
                receiverAccount5,
                receiverAccount6,
                receiverAccount7,
                receiverAccount8,
                receiverAccount9,
                receiverAccount10,
                receiverAccount11))
        .transactions(List.of(tx1, tx2, tx3, tx4, tx5, tx6, tx7, tx8, tx9, tx10, tx11))
        .firstBlockNumber(BLOCKHASH_MAX_HISTORY - 4) // to have some blockhashes available
        .runWithBesuNode(true)
        .oneTxPerBlockOnBesuNode(true)
        .build()
        .run();
  }

  private static ToyAccount getReceiverAccount(int nb, Bytes code) {
    return ToyAccount.builder()
        .balance(Wei.ONE)
        .nonce(6)
        .address(
            Address.wrap(
                Bytes.concatenate(
                    Bytes.fromHexString("0xFF000000000000000000000000000000000000"), Bytes.of(nb))))
        .code(code)
        .build();
  }
}
