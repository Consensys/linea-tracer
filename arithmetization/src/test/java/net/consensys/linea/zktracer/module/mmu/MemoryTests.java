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

package net.consensys.linea.zktracer.module.mmu;

import java.util.Random;

import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.testing.BytecodeCompiler;
import net.consensys.linea.zktracer.testing.BytecodeRunner;
import net.consensys.linea.zktracer.testing.EvmExtension;
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
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(EvmExtension.class)
class MemoryTests {
  private final Random rnd = new Random(666);

  @Test
  void fastMstore() {
    BytecodeRunner.of(BytecodeCompiler.newProgram().push(25).push(32).op(OpCode.MSTORE).compile())
        .run();
  }

  @Test
  void slowMstore() {
    BytecodeRunner.of(BytecodeCompiler.newProgram().push(13).push(27).op(OpCode.MSTORE).compile())
        .run();
  }

  @Test
  void fastMload() {
    BytecodeRunner.of(BytecodeCompiler.newProgram().push(34).push(0).op(OpCode.MLOAD).compile())
        .run();
  }

  @Test
  void slowMload() {
    BytecodeRunner.of(BytecodeCompiler.newProgram().push(34).push(76).op(OpCode.MLOAD).compile())
        .run();
  }

  @Test
  void alignedMstore8() {
    BytecodeRunner.of(BytecodeCompiler.newProgram().push(12).push(0).op(OpCode.MSTORE8).compile())
        .run();
  }

  @Test
  void nonAlignedMstore8() {
    BytecodeRunner.of(
            BytecodeCompiler.newProgram().push(66872).push(35).op(OpCode.MSTORE8).compile())
        .run();
  }

  @Test
  void extcodecopyThenCreate(){
    ToyWorld.ToyWorldBuilder world = ToyWorld.builder();

    // create called account
    final ToyAccount calledAccount =
      ToyAccount.builder()
        .balance(Wei.of(rnd.nextInt(10000, 10000000)))
        .nonce(rnd.nextInt(2, 500))
        .address(Address.wrap(Bytes.random(20)))
        .code(Bytes.random(1000))
        .build();
    world.account(calledAccount);

    // create sender account
    final KeyPair keyPair = new SECP256K1().generateKeyPair();
    final Address senderAddress =
      Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));
    final ToyAccount senderAccount =
      ToyAccount.builder()
        .balance(Wei.of(rnd.nextLong(30000, 100000000)))
        .nonce(rnd.nextInt(0, 500))
        .address(senderAddress)
        .build();
    world.account(senderAccount);

    final int lengthExtcodecopy = 133;
    final int offsetExtcodecopy = 0;
    final int destoffsetExtcodecopy = 16;

    final int lengthCreate = 65;
    final int offsetCreate = 3;
    final int valueCreate = 0;

    // create transaction
    final Transaction tx =
      ToyTransaction.builder()
        .sender(senderAccount)
        .keyPair(keyPair)
        .transactionType(TransactionType.FRONTIER)
        .gasLimit(rnd.nextLong(210000, 0xfffffffffffffL))
        .value(Wei.of(rnd.nextInt(0, 1000)))
        .payload( BytecodeCompiler.newProgram()
          .push(lengthExtcodecopy)
          .push(offsetExtcodecopy)
          .push(destoffsetExtcodecopy)
          .push(calledAccount.getAddress())
          .op(OpCode.EXTCODECOPY)
          .push(lengthCreate)
          .push(offsetCreate)
          .push(valueCreate)
          .op(OpCode.CREATE)
          .compile())
        .build();

    // build and run
    ToyExecutionEnvironment.builder()
      .toyWorld(world.build())
      .transaction(tx)
      .testValidator(x -> {})
      .build()
      .run();

  }

  @Test
  void txInitialisationTriggersMmu() {
    ToyWorld.ToyWorldBuilder world = ToyWorld.builder();

    // create receiver account
    final ToyAccount receiverAccount =
        ToyAccount.builder()
            .balance(Wei.of(rnd.nextInt(10000, 10000000)))
            .nonce(rnd.nextInt(2, 500))
            .address(Address.wrap(Bytes.random(20)))
            .code(
                BytecodeCompiler.newProgram()
                    .push(32, 0xbeef)
                    .push(32, 0xdead)
                    .op(OpCode.ADD)
                    .compile())
            .build();
    world.account(receiverAccount);

    // create sender account
    final KeyPair keyPair = new SECP256K1().generateKeyPair();
    final Address senderAddress =
        Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));
    final ToyAccount senderAccount =
        ToyAccount.builder()
            .balance(Wei.of(rnd.nextLong(30000, 100000000)))
            .nonce(rnd.nextInt(0, 500))
            .address(senderAddress)
            .build();
    world.account(senderAccount);

    // create transaction
    final Transaction tx =
        ToyTransaction.builder()
            .sender(senderAccount)
            .to(receiverAccount)
            .keyPair(keyPair)
            .transactionType(TransactionType.FRONTIER)
            .gasLimit(rnd.nextLong(21000, 0xfffffffffffffL))
            .value(Wei.of(rnd.nextInt(0, 1000)))
            .payload(Bytes.random(rnd.nextInt(1, 1000)))
            .build();

    // build and run
    ToyExecutionEnvironment.builder()
        .toyWorld(world.build())
        .transaction(tx)
        .testValidator(x -> {})
        .build()
        .run();
  }
}
