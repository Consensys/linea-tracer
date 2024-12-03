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
package net.consensys.linea.zktracer.instructionprocessing.createTests.failure;

import static net.consensys.linea.zktracer.instructionprocessing.createTests.trivial.RootLevel.salt01;
import static net.consensys.linea.zktracer.instructionprocessing.utilities.MonoOpCodeSmcs.keyPair;
import static net.consensys.linea.zktracer.instructionprocessing.utilities.MonoOpCodeSmcs.userAccount;
import static net.consensys.linea.zktracer.opcode.OpCode.*;
import static net.consensys.linea.zktracer.types.Utils.leftPadTo;

import java.util.List;

import net.consensys.linea.testing.*;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.junit.jupiter.api.Test;

public class Create {

  final Bytes tinyAddress1 = Bytes.fromHexString("badd1ec0de");
  final Bytes tinyAddress2 = Bytes.fromHexString("900d1ec0de");
  final Bytes leftPaddedAddress1 = leftPadTo(tinyAddress1, 32);
  final Bytes leftPaddedAddress2 = leftPadTo(tinyAddress2, 32);
  final Address address1 = Address.wrap(leftPadTo(tinyAddress1, 20));
  final Address address2 = Address.wrap(leftPadTo(tinyAddress2, 20));
  final Address targetAddress = Address.fromHexString("797add7e55");

  final BytecodeCompiler simpleSelfDestruct =
      BytecodeCompiler.newProgram().op(ORIGIN).op(SELFDESTRUCT);

  final ToyAccount simpleSelfDestructor =
      ToyAccount.builder()
          .code(simpleSelfDestruct.compile())
          .nonce(91)
          .balance(Wei.of(1234L))
          .address(address1)
          .build();

  final BytecodeCompiler simpleCreate =
      BytecodeCompiler.newProgram()
          .push(0) // empty init code
          .push(0)
          .push(1) // value
          .op(CREATE);

  final ToyAccount simpleCreator =
      ToyAccount.builder()
          .code(simpleCreate.compile())
          .nonce(512)
          .balance(Wei.of(73L))
          .address(address2)
          .build();

  final BytecodeCompiler codeToDeploy =
      BytecodeCompiler.newProgram()
          .push(0) // rac
          .push(0) // rao
          .push(0) // cds
          .push(0) // cdo
          .push(0)
          .op(CALLDATALOAD) // should be an address
          .op(GAS)
          .op(DELEGATECALL);

  final BytecodeCompiler initCodeThatDeploysTheDesiredCode =
      BytecodeCompiler.newProgram()
          .push(codeToDeploy.compile())
          .push(8 * (32 - codeToDeploy.compile().size()))
          .op(SHL)
          .push(0)
          .op(MSTORE)
          .push(codeToDeploy.compile().size()) // code size
          .push(0)
          .op(RETURN);

  int addressKey = 0xad;

  final BytecodeCompiler targetByteCode =
      BytecodeCompiler.newProgram()
          .op(CALLDATASIZE)
          .op(ISZERO)
          .push(1)
          .op(SUB) // ¬ [CALLDATASIZE == 0]
          .push(
              1
                  + 1
                  + (1 + 1)
                  + 1
                  + (1 + 1)
                  + 1 //
                  + (1 + initCodeThatDeploysTheDesiredCode.compile().size())
                  + (1 + 1)
                  + 1
                  + (1 + 1)
                  + 1
                  + (1 + salt01.length() / 2)
                  + 1
                  + (1 + 1)
                  + (1 + 1)
                  + (1 + 1)
                  + 1
                  + (1 + 1)
                  + 1
                  + 1 //
              )
          .op(JUMPI)
          //
          .push(initCodeThatDeploysTheDesiredCode.compile())
          .push(8 * (32 - initCodeThatDeploysTheDesiredCode.compile().size()))
          .op(SHL)
          .push(0)
          .op(MSTORE)
          .push(salt01)
          .push(initCodeThatDeploysTheDesiredCode.compile().size()) // init code size
          .push(0) // offset
          .push(0xff) // value
          .op(CREATE2)
          .push(addressKey)
          .op(SSTORE)
          .op(STOP)
          //
          .op(JUMPDEST)
          .push(0)
          .op(CALLDATALOAD) // extracts (address) from call data
          .push(0)
          .op(MSTORE)
          .push(0) // rac
          .push(0) // rao
          .push(0x20) // cds
          .push(0) // cdo
          .push(addressKey)
          .op(SLOAD) // extract stored address
          .push(0xef) // value
          .op(GAS) // gas
          .op(CALL);

  final ToyAccount targetAccount =
      ToyAccount.builder()
          .code(targetByteCode.compile())
          .nonce(1337)
          .balance(Wei.of(73L))
          .address(targetAddress)
          .build();

  final ToyTransactionNonceSetter deploymentTransactionNumber1 =
      (long nonce) ->
          ToyTransaction.builder()
              .nonce(nonce)
              .to(targetAccount)
              .keyPair(keyPair)
              .value(Wei.of(0xffff))
              .gasLimit(1_000_000L)
              .gasPrice(Wei.of(8))
              .build();
  final ToyTransactionNonceSetter createTransactionNumber1 =
      (long nonce) ->
          ToyTransaction.builder()
              .nonce(nonce)
              .to(targetAccount)
              .keyPair(keyPair)
              .value(Wei.of(0xeeee))
              .gasLimit(1_000_000L)
              .gasPrice(Wei.of(8))
              .payload(leftPaddedAddress1)
              .build();
  final ToyTransactionNonceSetter selfDestructTransaction =
      (long nonce) ->
          ToyTransaction.builder()
              .nonce(nonce)
              .to(targetAccount)
              .keyPair(keyPair)
              .value(Wei.of(0xdddd))
              .gasLimit(1_000_000L)
              .gasPrice(Wei.of(8))
              .payload(leftPaddedAddress2)
              .build();
  final ToyTransactionNonceSetter deploymentTransactionNumber2 =
      (long nonce) ->
          ToyTransaction.builder()
              .nonce(nonce)
              .to(targetAccount)
              .keyPair(keyPair)
              .value(Wei.of(0xcccc))
              .gasLimit(1_000_000L)
              .gasPrice(Wei.of(8))
              .build();
  final ToyTransactionNonceSetter createTransactionNumber2 =
      (long nonce) ->
          ToyTransaction.builder()
              .nonce(nonce)
              .to(targetAccount)
              .keyPair(keyPair)
              .value(Wei.of(0xbbbb))
              .gasLimit(1_000_000L)
              .gasPrice(Wei.of(8))
              .payload(leftPaddedAddress1)
              .build();

  ToyTransactionNonceSetter[] transactionsNonceSetter = {
    deploymentTransactionNumber1,
    createTransactionNumber1,
    selfDestructTransaction,
    deploymentTransactionNumber2,
    createTransactionNumber2
  };
  final List<Transaction> transactions =
      ToyMultiTransaction.builder().build(transactionsNonceSetter, userAccount);

  final List<ToyAccount> accounts =
      List.of(userAccount, targetAccount, simpleSelfDestructor, simpleCreator);

  @Test
  void complexFailureConditionTest() {

    ToyExecutionEnvironmentV2.builder()
        .accounts(accounts)
        .transactions(transactions)
        .zkTracerValidator(zkTracer -> {})
        .build()
        .run();
  }
}
