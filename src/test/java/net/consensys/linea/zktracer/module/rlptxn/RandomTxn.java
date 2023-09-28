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

package net.consensys.linea.zktracer.module.rlptxn;

import java.math.BigInteger;
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
import org.hyperledger.besu.datatypes.TransactionType;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.junit.jupiter.api.Test;

class RandomTxn {

  @Test
  void test() {
    OpCodes.load();

    // long randLongSmall = new Random().nextLong(1, 128);
    // long randLongMedium = new Random().nextLong(128, 256);
    // long randLongLong = new Random().nextLong(128, 0xfffffffffffffffL);
    // BigInteger randBigIntSmall = new BigInteger(7, new Random());
    // BigInteger randBigIntSixteenBytes = new BigInteger(16 * 8, new Random());
    // BigInteger randBigIntThirtyTwoBytes = new BigInteger(32 * 8, new Random());
    // int randIntLEFiveFive = new Random().nextInt(2, 55);
    // int randIntGEFiveSix = new Random().nextInt(56, 1234);s

    for (int i = 0; i < new Random().nextInt(100, 200); i++) {

      KeyPair keyPair = new SECP256K1().generateKeyPair();
      Address senderAddress = Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));
      ToyAccount senderAccount = randToyAccount(senderAddress);
      ToyAccount receiverAccount = receiverAccount();
      Transaction tx = randTx(senderAccount, keyPair, receiverAccount);

      ToyWorld toyWorld =
          ToyWorld.builder().accounts(List.of(senderAccount, receiverAccount)).build();

      ToyExecutionEnvironment.builder().toyWorld(toyWorld).transaction(tx).build().run();
    }
  }

  final Transaction randTx(ToyAccount senderAccount, KeyPair keyPair, ToyAccount receiverAccount) {

    int txType = new Random().nextInt(0, 1);
    boolean txCreate = new Random().nextBoolean();

    return switch (txType) {
      case 0 -> ToyTransaction.builder()
          .sender(senderAccount)
          .keyPair(keyPair)
          .transactionType(TransactionType.FRONTIER)
          .gasLimit(randLong())
          .value(Wei.of(randBigInt(true)))
          .payload(randData())
          .build();

      case 1 -> ToyTransaction.builder()
          .sender(senderAccount)
          .keyPair(keyPair)
          .transactionType(TransactionType.ACCESS_LIST)
          .gasLimit(randLong())
          .value(Wei.of(randLong()))
          .payload(randData())
          .build();

      case 2 -> ToyTransaction.builder()
          .sender(senderAccount)
          .keyPair(keyPair)
          .transactionType(TransactionType.EIP1559)
          .gasLimit(randLong())
          .value(Wei.of(randLong()))
          .payload(randData())
          .build();

      default -> throw new IllegalStateException("Unexpected value: " + txType);
    };
  }

  final BigInteger randBigInt(boolean onlySixteenByte) {
    int selectorBound = 4;
    if (!onlySixteenByte) {
      selectorBound += 1;
    }
    int selector = new Random().nextInt(0, selectorBound);

    return switch (selector) {
      case 0 -> BigInteger.ZERO;
      case 1 -> BigInteger.valueOf(new Random().nextInt(1, 128));
      case 2 -> BigInteger.valueOf(new Random().nextInt(128, 256));
      case 3 -> new BigInteger(16 * 8, new Random());
      case 4 -> new BigInteger(32 * 8, new Random());
      default -> throw new IllegalStateException("Unexpected value: " + selector);
    };
  }

  final Bytes randData() {
    int selector = new Random().nextInt(0, 6);
    return switch (selector) {
      case 0 -> Bytes.EMPTY;
      case 1 -> Bytes.of(0x0);
      case 2 -> Bytes.minimalBytes(new Random().nextLong(1, 128));
      case 3 -> Bytes.minimalBytes(new Random().nextLong(128, 256));
      case 4 -> Bytes.random(new Random().nextInt(1, 56));
      case 5 -> Bytes.random(new Random().nextInt(56, 666));
      default -> throw new IllegalStateException("Unexpected value: " + selector);
    };
  }

  final Long randLong() {
    int selector = new Random().nextInt(0, 4);
    return switch (selector) {
      case 0 -> 0L;
      case 1 -> new Random().nextLong(1, 128);
      case 2 -> new Random().nextLong(128, 256);
      case 3 -> new Random().nextLong(256, 0xfffffffffffffffL);
      default -> throw new IllegalStateException("Unexpected value: " + selector);
    };
  }

  final ToyAccount receiverAccount() {

    return ToyAccount.builder()
        .balance(Wei.ONE)
        .nonce(6)
        .address(Address.wrap(Bytes.random(20)))
        .code(
            BytecodeCompiler.newProgram()
                .push(32, 0xbeef)
                .push(32, 0xdead)
                .op(OpCode.ADD)
                .compile())
        .build();
  }

  final ToyAccount randToyAccount(Address senderAddress) {

    return ToyAccount.builder()
        .balance(Wei.fromEth(5))
        .nonce(randLong())
        .address(senderAddress)
        .build();
  }
}
