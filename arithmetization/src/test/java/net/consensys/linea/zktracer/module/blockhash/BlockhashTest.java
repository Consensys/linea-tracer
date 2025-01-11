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

import static net.consensys.linea.zktracer.module.constants.GlobalConstants.BLOCKHASH_MAX_HISTORY;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Preconditions;
import net.consensys.linea.UnitTestWatcher;
import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.testing.MultiBlockExecutionEnvironment;
import net.consensys.linea.testing.ToyAccount;
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
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(UnitTestWatcher.class)
public class BlockhashTest {

  @Test
  void severalBlockhash() {
    BytecodeRunner.of(
            BytecodeCompiler.newProgram()

                // arg is NUMBER - 1
                .push(1)
                .op(OpCode.NUMBER)
                .op(OpCode.SUB)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)

                // arg is NUMBER
                .op(OpCode.NUMBER)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)

                // arg is NUMBER + 1
                .op(OpCode.NUMBER)
                .push(1)
                .op(OpCode.ADD)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)

                // arg is ridiculously big
                .push(256)
                .op(OpCode.NUMBER)
                .op(OpCode.MUL)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)

                // arg is NUMBER / 256 << NUMBER
                .push(256)
                .op(OpCode.NUMBER)
                .op(OpCode.DIV)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)

                // arg is 0 << NUMBER
                .push(0)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)

                // arg is 1 << NUMBER
                .push(1)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)

                // arg is ridiculously big
                .push(
                    Bytes.fromHexString(
                        "0x123456789012345678901234567890123456789012345678901234567890ffff"))
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)

                // arg of BlockHash is NUMBER - (256 + 2)
                .push(BLOCKHASH_MAX_HISTORY + 2)
                .op(OpCode.NUMBER)
                .op(OpCode.SUB)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)

                // arg of BlockHash is NUMBER - (256 + 1)
                .push(BLOCKHASH_MAX_HISTORY + 1)
                .op(OpCode.NUMBER)
                .op(OpCode.SUB)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)

                // arg of BlockHash is NUMBER - 256
                .push(BLOCKHASH_MAX_HISTORY)
                .op(OpCode.NUMBER)
                .op(OpCode.SUB)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)

                // arg of BlockHash is NUMBER - (256 - 1)
                .push(BLOCKHASH_MAX_HISTORY - 1)
                .op(OpCode.NUMBER)
                .op(OpCode.SUB)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)

                // arg of BlockHash is NUMBER - (256 - 2)
                .push(BLOCKHASH_MAX_HISTORY - 2)
                .op(OpCode.NUMBER)
                .op(OpCode.SUB)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)
                .compile())
        .run();
  }

  @Test
  void singleBlockhash() {
    BytecodeRunner.of(
            BytecodeCompiler.newProgram()

                // arg of BlockHash is Blocknumber +1
                .op(OpCode.NUMBER)
                .push(1)
                .op(OpCode.ADD)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)
                .compile())
        .run();
  }

  @Test
  void blockhashArgumentUpperRangeCheckMultiBlockTest() {
    // Block 1
    Bytes program1 = BytecodeCompiler.newProgram().op(OpCode.NUMBER).op(OpCode.BLOCKHASH).compile();

    // Block 2
    Bytes program2 =
        BytecodeCompiler.newProgram()
            .push(1)
            .op(OpCode.NUMBER)
            .op(OpCode.SUB)
            .op(OpCode.BLOCKHASH)
            .compile();

    multiBlocksTest(List.of(program1, program2));
  }

  @Test
  void blockhashArgumentLowerRangeCheckMultiBlockTest() {
    Bytes fillerProgram = BytecodeCompiler.newProgram().op(OpCode.COINBASE).compile();

    Bytes program0 =
        BytecodeCompiler.newProgram()
            .push(1)
            .op(OpCode.NUMBER)
            .op(OpCode.SUB)
            .op(OpCode.BLOCKHASH)
            .compile();

    // Block no longer available
    // Block 1
    Bytes program1 =
        BytecodeCompiler.newProgram()
            .push(256)
            .op(OpCode.NUMBER)
            .op(OpCode.SUB)
            .op(OpCode.BLOCKHASH)
            .compile();

    // Block 2
    Bytes program2 =
        BytecodeCompiler.newProgram()
            .push(257)
            .op(OpCode.NUMBER)
            .op(OpCode.SUB)
            .op(OpCode.BLOCKHASH)
            .compile();

    Bytes program3 =
        BytecodeCompiler.newProgram()
            .push(16)
            .op(OpCode.NUMBER)
            .op(OpCode.SUB)
            .op(OpCode.BLOCKHASH)
            .compile();

    multiBlocksTest(
        Stream.concat(
                Collections.nCopies(256, fillerProgram).stream(),
                List.of(program0, program1, program2, program3).stream())
            .collect(Collectors.toList()));
  }

  // TODO: move this to blockData tests

  // Support methods
  void multiBlocksTest(List<Bytes> programs) {
    multiBlocksTest(programs, List.of());
  }

  public static void multiBlocksTest(List<Bytes> programs, List<Long> gasLimits) {
    Preconditions.checkArgument(gasLimits.isEmpty() || programs.size() == gasLimits.size());

    List<KeyPair> keyPairs = new ArrayList<>();
    List<Address> senderAddresses = new ArrayList<>();
    List<ToyAccount> senderAccounts = new ArrayList<>();
    List<ToyAccount> receiverAccounts = new ArrayList<>();
    List<Transaction> transactions = new ArrayList<>();

    for (int i = 0; i < programs.size(); i++) {
      Bytes program = programs.get(i);
      keyPairs.add(new SECP256K1().generateKeyPair());
      senderAddresses.add(
          Address.extract(
              Hash.hash(keyPairs.get(keyPairs.size() - 1).getPublicKey().getEncodedBytes())));
      senderAccounts.add(
          ToyAccount.builder()
              .balance(Wei.fromEth(1 + i))
              .nonce(3 + i)
              .address(senderAddresses.get(senderAddresses.size() - 1))
              .build());
      receiverAccounts.add(
          ToyAccount.builder()
              .balance(Wei.ONE)
              .nonce(5 + i)
              .address(Address.fromHexString("0x" + (20 + i)))
              .code(program)
              .build());
      transactions.add(
          ToyTransaction.builder()
              .sender(senderAccounts.get(senderAccounts.size() - 1))
              .to(receiverAccounts.get(receiverAccounts.size() - 1))
              .keyPair(keyPairs.get(keyPairs.size() - 1))
              .build());
    }

    MultiBlockExecutionEnvironment.MultiBlockExecutionEnvironmentBuilder builder =
        MultiBlockExecutionEnvironment.builder()
            .accounts(
                Stream.concat(senderAccounts.stream(), receiverAccounts.stream())
                    .collect(Collectors.toList()));

    for (int i = 0; i < transactions.size(); i++) {
      if (gasLimits.isEmpty()) {
        builder.addBlock(List.of(transactions.get(i)));
      } else {
        builder.addBlock(List.of(transactions.get(i)), gasLimits.get(i));
      }
    }

    builder.build().run();
  }
}
