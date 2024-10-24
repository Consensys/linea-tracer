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
package net.consensys.linea.zktracer.exceptions;

import static com.google.common.base.Preconditions.checkArgument;
import static net.consensys.linea.zktracer.module.hub.signals.TracedException.INVALID_CODE_PREFIX;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.testing.ToyAccount;
import net.consensys.linea.testing.ToyExecutionEnvironmentV2;
import net.consensys.linea.testing.ToyTransaction;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.AddressUtils;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.crypto.KeyPair;
import org.hyperledger.besu.crypto.SECP256K1;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.datatypes.TransactionType;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.junit.jupiter.api.Test;

public class InvalidCodePrefixExceptionTest {

  @Test
  void invalidCodePrefixExceptionViaDeploymentTest() {
    KeyPair keyPair = new SECP256K1().generateKeyPair();
    Address userAddress = Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));
    ToyAccount userAccount =
        ToyAccount.builder().balance(Wei.fromEth(1000)).nonce(1).address(userAddress).build();

    Transaction tx =
        ToyTransaction.builder()
            .sender(userAccount)
            .keyPair(keyPair)
            .transactionType(TransactionType.FRONTIER)
            .gasLimit(0xffffffL)
            .payload(
                Bytes.fromHexString(
                  "0xef6000600052")) // bytecode starting with 0xef, which is EIP_3541_MARKER
            .build();

    Address deployedAddress = AddressUtils.effectiveToAddress(tx);
    System.out.println("Deployed address: " + deployedAddress);

    checkArgument(tx.isContractCreation());

    ToyExecutionEnvironmentV2 toyExecutionEnvironment =
        ToyExecutionEnvironmentV2.builder().accounts(List.of(userAccount)).transaction(tx).build();

    toyExecutionEnvironment.run();

    /*
    assertEquals(
        INVALID_CODE_PREFIX,
        toyExecutionEnvironment.getHub().currentTraceSection().commonValues.tracedException());
    */
  }

  @Test
  void invalidCodePrefixExceptionViaReturnTest() {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        .push("ef" + "00".repeat(31))
        .push(0)
        .op(OpCode.MSTORE)
        .push(1)
        .push(0)
        .op(OpCode.RETURN);

    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    bytecodeRunner.run();

    assertEquals(
        INVALID_CODE_PREFIX,
        bytecodeRunner.getHub().previousTraceSection(2).commonValues.tracedException());
  }
}
