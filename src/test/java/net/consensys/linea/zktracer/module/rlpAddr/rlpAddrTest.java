/*
 * Copyright ConsenSys AG.
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

package net.consensys.linea.zktracer.module.rlpAddr;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.opcode.OpCodes;
import net.consensys.linea.zktracer.testing.BytecodeCompiler;
import net.consensys.linea.zktracer.testing.ToyAccount;
import net.consensys.linea.zktracer.testing.ToyExecutionEnvironment;
import net.consensys.linea.zktracer.testing.ToyTransaction;
import net.consensys.linea.zktracer.testing.ToyWorld;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.crypto.KeyPair;
import org.hyperledger.besu.crypto.SECP256K1;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.junit.jupiter.api.Test;

public class rlpAddrTest {
  private final Random rnd = new Random(666);

  @Test
  void test() {
    OpCodes.load();

    ToyWorld.ToyWorldBuilder world =
      ToyWorld.builder();
    List<Transaction> txList = new ArrayList<>();

    for (int i = 0; i < 100; i++) {

      KeyPair keyPair = new SECP256K1().generateKeyPair();
      Address senderAddress = Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));
      ToyAccount senderAccount = randSenderAccount(senderAddress);
      ToyAccount receiverAccount = randReceiverAccount();

      world.account(senderAccount).account(receiverAccount);
      txList.add(randTx(senderAccount, keyPair, receiverAccount));

    }
    ToyExecutionEnvironment.builder()
      .toyWorld(world.build())
      .transactions(txList)
      .testValidator(x -> {})
      .build()
      .run();
  }
  private Transaction randTx(ToyAccount senderAccount, KeyPair keyPair, ToyAccount receiverAccount) {
    Transaction tx = new Transaction();
    return tx;
  }

  final ToyAccount randReceiverAccount() {
    int create = rnd.nextInt(1, 3);
    return switch (create) {
      case 1 -> ToyAccount.builder()
          .balance(Wei.MAX_WEI)
          .nonce(randLong())
          .address(Address.wrap(Bytes.random(20)))
          .code(BytecodeCompiler.newProgram().op(OpCode.CREATE).compile())
          .build();
      case 2 -> ToyAccount.builder()
          .balance(Wei.MAX_WEI)
          .nonce(randLong())
          .address(Address.wrap(Bytes.random(20)))
          .code(BytecodeCompiler.newProgram().op(OpCode.CREATE2).compile())
          .build();
      default -> throw new IllegalStateException("Unexpected value: " + create);
    };
  }

  final ToyAccount randSenderAccount(Address senderAddress) {
    return ToyAccount.builder()
        .balance(Wei.MAX_WEI)
        .nonce(randLong())
        .address(senderAddress)
        .build();
  }

  final Long randLong() {
    int selector = rnd.nextInt(0, 4);
    return switch (selector) {
      case 0 -> 0L;
      case 1 -> rnd.nextLong(1, 128);
      case 2 -> rnd.nextLong(128, 256);
      case 3 -> rnd.nextLong(256, 0xfffffffffffffffL);
      default -> throw new IllegalStateException("Unexpected value: " + selector);
    };
  }
}
