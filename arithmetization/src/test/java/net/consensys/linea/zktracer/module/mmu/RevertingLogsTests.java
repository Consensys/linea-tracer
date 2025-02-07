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

package net.consensys.linea.zktracer.module.mmu;

import static net.consensys.linea.testing.BytecodeCompiler.newProgram;

import java.util.List;

import net.consensys.linea.testing.ToyAccount;
import net.consensys.linea.testing.ToyExecutionEnvironmentV2;
import net.consensys.linea.testing.ToyTransaction;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.crypto.KeyPair;
import org.hyperledger.besu.crypto.SECP256K1;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.datatypes.TransactionType;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.junit.jupiter.api.Test;

public class RevertingLogsTests {

  private static final Bytes CALLDATACOPY =
      newProgram()
          .op(OpCode.CALLDATASIZE) // size
          .push(0) // offset
          .push(0) // dest offset
          .op(OpCode.CALLDATACOPY)
          .compile();

  private static final Bytes LOG0 =
      newProgram()
          .push(18) // size
          .push(0x1) // offset
          .op(OpCode.LOG0)
          .compile();

  private static final Bytes LOG1 =
      newProgram()
          .push(
              Bytes.fromHexString(
                  "0x000007031c100000000007031c100000000007031c100000000007031c100000")) // topic 1
          .push(18) // size
          .push(0x1) // offset
          .op(OpCode.LOG1)
          .compile();

  private static final Bytes LOG2 =
      newProgram()
          .push(
              Bytes.fromHexString(
                  "0x000007031c200000000007031c200000000007031c200000000007031c200000")) // topic 2
          .push(
              Bytes.fromHexString(
                  "0x000007031c100000000007031c100000000007031c100000000007031c100000")) // topic 1
          .push(18) // size
          .push(0x1) // offset
          .op(OpCode.LOG2)
          .compile();

  private static final Bytes LOG3 =
      newProgram()
          .push(
              Bytes.fromHexString(
                  "0x000007031c300000000007031c300000000007031c300000000007031c300000")) // topic 3
          .push(
              Bytes.fromHexString(
                  "0x000007031c200000000007031c200000000007031c200000000007031c200000")) // topic 2
          .push(
              Bytes.fromHexString(
                  "0x000007031c100000000007031c100000000007031c100000000007031c100000")) // topic 1
          .push(18) // size
          .push(0x1) // offset
          .op(OpCode.LOG4)
          .compile();

  private static final Bytes LOG4 =
      newProgram()
          .push(
              Bytes.fromHexString(
                  "0x000007031c400000000007031c400000000007031c400000000007031c400000")) // topic 4
          .push(
              Bytes.fromHexString(
                  "0x000007031c300000000007031c300000000007031c300000000007031c300000")) // topic 3
          .push(
              Bytes.fromHexString(
                  "0x000007031c200000000007031c200000000007031c200000000007031c200000")) // topic 2
          .push(
              Bytes.fromHexString(
                  "0x000007031c100000000007031c100000000007031c100000000007031c100000")) // topic 1
          .push(18) // size
          .push(0x1) // offset
          .op(OpCode.LOG4)
          .compile();

  private static final Bytes REVERT = newProgram().push(0).push(0).op(OpCode.REVERT).compile();

  private static final Bytes SELFREVERT_LOG_BYTECODE =
      newProgram().immediate(CALLDATACOPY).immediate(LOG3).immediate(REVERT).compile();

  private static final Bytes NON_REVERTING_LOG_BYTECODE =
      newProgram().immediate(CALLDATACOPY).immediate(LOG4).compile();

  private static final ToyAccount nonRevertingLogSMC =
      ToyAccount.builder()
          .address(Address.fromHexString("0xaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"))
          .code(NON_REVERTING_LOG_BYTECODE)
          .balance(Wei.of(98989898))
          .build();

  private static final ToyAccount selfRevertingLogSMC =
      ToyAccount.builder()
          .address(Address.fromHexString("0xbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb"))
          .code(SELFREVERT_LOG_BYTECODE)
          .balance(Wei.of(1235678))
          .build();

  private static final ToyAccount callNonRevertLogAndSelfRevert =
      ToyAccount.builder()
          .address(Address.fromHexString("0xcccccccccccccccccccccccccccccccccccccccc"))
          .code(Bytes.concatenate(call(nonRevertingLogSMC.getAddress()), REVERT))
          .balance(Wei.of(10000))
          .build();

  private static Bytes call(Address address) {
    return newProgram()
        .immediate(CALLDATACOPY)
        .push(0) // retSize
        .push(0) // retOffset
        .op(OpCode.MSIZE) // arg size
        .push(0) // argOffset
        .push(1) // value
        .push(address) // address
        .push(100000) // gas
        .op(OpCode.CALL)
        .compile();
  }

  @Test
  void mixtureOfRevertedLogs() {

    final KeyPair keyPair = new SECP256K1().generateKeyPair();
    final Address senderAddress =
        Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));
    final ToyAccount senderAccount =
        ToyAccount.builder().balance(Wei.of(100000000)).address(senderAddress).build();

    final ToyAccount recipientAccount =
        ToyAccount.builder()
            .balance(Wei.of(10000))
            .address(Address.fromHexString("0xdeadbeefdeadbeefdeadbeefdeadbeefdeadbeef"))
            .code(
                Bytes.concatenate(
                    CALLDATACOPY,
                    LOG0,
                    call(nonRevertingLogSMC.getAddress()),
                    LOG1,
                    call(selfRevertingLogSMC.getAddress()),
                    LOG2,
                    call(callNonRevertLogAndSelfRevert.getAddress()),
                    LOG4))
            .build();

    final Transaction tx =
        ToyTransaction.builder()
            .sender(senderAccount)
            .keyPair(keyPair)
            .transactionType(TransactionType.FRONTIER)
            .value(Wei.ONE)
            .gasLimit(1000000L)
            .gasPrice(Wei.of(10L))
            .payload(
                Bytes.fromHexString(
                    "0x00112233445566778899aabbccddeeff00112233445566778899aabbccddeeff"))
            .to(recipientAccount)
            .build();

    ToyExecutionEnvironmentV2.builder()
        .accounts(
            List.of(
                senderAccount,
                selfRevertingLogSMC,
                callNonRevertLogAndSelfRevert,
                nonRevertingLogSMC,
                recipientAccount))
        .transaction(tx)
        .build()
        .run();
  }
}
