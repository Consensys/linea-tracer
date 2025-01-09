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

import java.util.List;

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

                // arg of BlockHash is Blocknumber +1
                .op(OpCode.NUMBER)
                .push(1)
                .op(OpCode.ADD)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)

                // arg of BlockHash is Blocknumber
                .op(OpCode.NUMBER)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)

                // arg of BlockHash is ridiculously big
                .push(256)
                .op(OpCode.NUMBER)
                .op(OpCode.MUL)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)

                // arg of BlockHash is ridiculously small
                .push(256)
                .op(OpCode.NUMBER)
                .op(OpCode.DIV)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)

                // arg of BlockHash is 0
                .push(0)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)

                // arg of BlockHash is 1 (ie ridiculously small)
                .push(1)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)

                // another arg of BlockHash is ridiculously big
                .push(
                    Bytes.fromHexString(
                        "0x123456789012345678901234567890123456789012345678901234567890"))
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)

                // arg of BlockHash is Blocknumber -256 -2
                .push(BLOCKHASH_MAX_HISTORY + 2)
                .op(OpCode.NUMBER)
                .op(OpCode.SUB)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)

                // arg of BlockHash is Blocknumber -256 -1
                .push(BLOCKHASH_MAX_HISTORY + 1)
                .op(OpCode.NUMBER)
                .op(OpCode.SUB)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)

                // arg of BlockHash is Blocknumber -256
                .push(BLOCKHASH_MAX_HISTORY)
                .op(OpCode.NUMBER)
                .op(OpCode.SUB)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)

                // arg of BlockHash is Blocknumber -256 +1
                .push(BLOCKHASH_MAX_HISTORY - 1)
                .op(OpCode.NUMBER)
                .op(OpCode.SUB)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)

                // arg of BlockHash is Blocknumber -256 +2
                .push(BLOCKHASH_MAX_HISTORY - 2)
                .op(OpCode.NUMBER)
                .op(OpCode.SUB)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)

                // arg of BlockHash is Blocknumber  -1
                .push(1)
                .op(OpCode.NUMBER)
                .op(OpCode.ADD)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)

                // Duplicate of arg of BlockHash is Blocknumber  -1
                .push(1)
                .op(OpCode.NUMBER)
                .op(OpCode.ADD)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)

                // Truplicate of arg of BlockHash is Blocknumber  -1
                .push(1)
                .op(OpCode.NUMBER)
                .op(OpCode.ADD)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)

                // arg of BlockHash is Blocknumber  -2
                .push(2)
                .op(OpCode.NUMBER)
                .op(OpCode.ADD)
                .op(OpCode.BLOCKHASH)
                .op(OpCode.POP)

                // TODO: add test with different block in the conflated batch

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
  void multiBlockTest1() {
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

    twoBlocksTest(program1, program2);
  }

  @Test
  void multiBlockTest2() {
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

    twoBlocksTest(program1, program2);
  }

  void twoBlocksTest(Bytes program1, Bytes program2) {
    KeyPair keyPair = new SECP256K1().generateKeyPair();
    Address senderAddress = Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));

    ToyAccount senderAccount =
        ToyAccount.builder().balance(Wei.fromEth(1)).nonce(5).address(senderAddress).build();

    ToyAccount receiverAccount1 =
        ToyAccount.builder()
            .balance(Wei.ONE)
            .nonce(6)
            .address(Address.fromHexString("0x111111"))
            .code(program1)
            .build();

    ToyAccount receiverAccount2 =
        ToyAccount.builder()
            .balance(Wei.ONE)
            .nonce(6)
            .address(Address.fromHexString("0x222222"))
            .code(program2)
            .build();

    Transaction tx1 =
        ToyTransaction.builder()
            .sender(senderAccount)
            .to(receiverAccount1)
            .keyPair(keyPair)
            .build();

    Transaction tx2 =
        ToyTransaction.builder()
            .sender(senderAccount)
            .to(receiverAccount2)
            .keyPair(keyPair)
            .build();

    MultiBlockExecutionEnvironment.builder()
        .accounts(List.of(senderAccount, receiverAccount1, receiverAccount2))
        .addBlock(List.of(tx1))
        .addBlock(List.of(tx2))
        .build()
        .run();
  }
}
