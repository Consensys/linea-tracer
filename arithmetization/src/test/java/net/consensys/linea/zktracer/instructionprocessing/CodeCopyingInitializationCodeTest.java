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
package net.consensys.linea.zktracer.instructionprocessing;

import java.util.List;

import net.consensys.linea.testing.*;
import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.ToyAccount;
import net.consensys.linea.testing.ToyExecutionEnvironmentV2;
import net.consensys.linea.testing.ToyTransaction;
import net.consensys.linea.testing.TransactionProcessingResultValidator;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.crypto.KeyPair;
import org.hyperledger.besu.crypto.SECP256K1;
import org.hyperledger.besu.datatypes.*;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.datatypes.TransactionType;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.junit.jupiter.api.Test;

public class CodeCopyingInitializationCodeTest {


  /**
   * The purpose of {@link CodeCopyingInitializationCodeTest#testSimpleCodeCopyOfInitializationCode} is to test the
   * coherence between the CODE_SIZE as expected by the zkEVM of an account under deployment, that is the
   * initialization code's size, and the codeSize contained in the account's snapshot. This test answers the
   * point raised in <a href="https://github.com/Consensys/linea-tracer/issues/1482">this issue</a>.
   */
  @Test
  void testSimpleCodeCopyOfInitializationCode() {
    Bytes initCode =
        BytecodeCompiler.newProgram()
            .op(OpCode.CODESIZE)
            .push(0)
            .push(0)
            .op(OpCode.CODECOPY)
            .compile();

    Transaction deploymentTransaction = deploymentTansactionFromInitCode(initCode);
    runTransaction(deploymentTransaction);
  }

  @Test
  void testSimpleCodeCopyOfInitializationCodeWithFollowupMload() {
    Bytes initCode =
        BytecodeCompiler.newProgram()
            .op(OpCode.CODESIZE)
            .push(0)
            .push(0)
            .op(OpCode.CODECOPY)
            .push(0)
            .op(OpCode.MLOAD)
            .compile();

    Transaction deploymentTransaction = deploymentTansactionFromInitCode(initCode);
    runTransaction(deploymentTransaction);
  }

  @Test
  void testInitializationCodeDeploysItselfThroughCodeCopy() {
    Bytes initCode =
        BytecodeCompiler.newProgram()
            .op(OpCode.CODESIZE)
            .push(0)
            .push(0)
            .op(OpCode.CODECOPY)
            .op(OpCode.CODESIZE)
            .push(0)
            .op(OpCode.RETURN)
            .compile();

    Transaction deploymentTransaction = deploymentTansactionFromInitCode(initCode);
    runTransaction(deploymentTransaction);
  }

  KeyPair keyPair = new SECP256K1().generateKeyPair();
  Address userAddress = Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));
  ToyAccount userAccount =
          ToyAccount.builder().balance(Wei.fromEth(100)).nonce(1).address(userAddress).build();

  List<ToyAccount> accounts = List.of(userAccount);

  Transaction deploymentTansactionFromInitCode(Bytes initCode) {
    return ToyTransaction.builder()
        .sender(userAccount)
        .payload(initCode)
        .transactionType(TransactionType.FRONTIER)
        .value(Wei.ZERO)
        .keyPair(keyPair)
        .gasLimit(100_000L)
        .gasPrice(Wei.of(8))
        .build();
  }

  private void runTransaction(Transaction transaction) {
    ToyExecutionEnvironmentV2.builder()
            .accounts(accounts)
            .transaction(transaction)
            .transactionProcessingResultValidator(TransactionProcessingResultValidator.EMPTY_VALIDATOR)
            .build()
            .run();
  }
}
