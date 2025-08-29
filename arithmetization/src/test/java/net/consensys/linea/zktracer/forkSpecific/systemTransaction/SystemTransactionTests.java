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

package net.consensys.linea.zktracer.forkSpecific.systemTransaction;

import static net.consensys.linea.testing.ToyExecutionEnvironmentV2.DEFAULT_BLOCK_NUMBER;
import static net.consensys.linea.testing.ToyExecutionEnvironmentV2.DEFAULT_TIME_STAMP;
import static net.consensys.linea.zktracer.forkSpecific.systemTransaction.SystemTransactionTestUtils.byteCodeCallingBeaconRootSystemAccount;
import static net.consensys.linea.zktracer.forkSpecific.systemTransaction.SystemTransactionTestUtils.byteCodeCallingBeaconRootSystemAccountFromCallData;
import static net.consensys.linea.zktracer.module.hub.section.systemTransaction.EIP2935HistoricalHash.HISTORY_STORAGE_ADDRESS;
import static net.consensys.linea.zktracer.module.hub.section.systemTransaction.EIP4788BeaconBlockRoot.BEACONROOT_ADDRESS;
import static net.consensys.linea.zktracer.types.Utils.leftPadTo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import net.consensys.linea.reporting.TracerTestBase;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.testing.MultiBlockExecutionEnvironment;
import net.consensys.linea.testing.ToyAccount;
import net.consensys.linea.testing.ToyTransaction;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.crypto.KeyPair;
import org.hyperledger.besu.crypto.SECP256K1;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class SystemTransactionTests extends TracerTestBase {

  // This test checks the consistency of system account by calling the system account eip-2935 in
  // happy path: not genesis block, system address exists
  @Test
  void systemTransaction2935ConsistencyTest() {
    BytecodeRunner.of(
            byteCodeCallingBeaconRootSystemAccount(
                testInfo, HISTORY_STORAGE_ADDRESS, DEFAULT_BLOCK_NUMBER - 1))
        .run(testInfo);
  }

  // This test checks the consistency of system account by calling the system account of eip-4788 in
  // happy path: not genesis block, system address exists
  @Test
  void systemTransaction4788ConsistencyTest() {
    BytecodeRunner.of(
            byteCodeCallingBeaconRootSystemAccount(
                testInfo, BEACONROOT_ADDRESS, DEFAULT_TIME_STAMP))
        .run(testInfo);
  }

  private static Stream<Arguments> scenariiForSystemContract() {
    final List<Address> supportedSystemContracts =
        List.of(BEACONROOT_ADDRESS, HISTORY_STORAGE_ADDRESS);
    final List<Arguments> scenarii = new ArrayList<>();
    for (Address systemContract : supportedSystemContracts) {
      for (int systemContractDeployedBeforeBlockNumber = 0;
          systemContractDeployedBeforeBlockNumber <= 2;
          systemContractDeployedBeforeBlockNumber++) {
        scenarii.add(Arguments.of(systemContract, systemContractDeployedBeforeBlockNumber));
      }
    }
    return scenarii.stream();
  }

  @ParameterizedTest
  @MethodSource("scenariiForSystemContract")
  void genesisBlockTest(Address systemContract, int systemContractDeployedBeforeBlockNumber) {

    // random sender account that will send a tx to system account
    final KeyPair senderKeyPair = new SECP256K1().generateKeyPair();
    final Address senderAddress =
        Address.extract(Hash.hash(senderKeyPair.getPublicKey().getEncodedBytes()));
    final ToyAccount senderAccount =
        ToyAccount.builder().balance(Wei.fromEth(123)).nonce(0).address(senderAddress).build();

    final ToyAccount deployerOf2935 = null;
    final Transaction deploy2935 =
        ToyTransaction.builder()
            .sender(deployerOf2935)
            // .keyPair()
            .value(Wei.of(0))
            .build();
    final ToyAccount deployerOf4788 = null;
    final Transaction deploy4788 =
        ToyTransaction.builder()
            .sender(deployerOf4788)
            // .keyPair()
            .value(Wei.of(0))
            .build();

    final ToyAccount callerOf2935 =
        ToyAccount.builder()
            .address(Address.wrap(leftPadTo(Bytes.fromHexString("0x2935"), Address.SIZE)))
            .code(
                byteCodeCallingBeaconRootSystemAccountFromCallData(
                    testInfo, HISTORY_STORAGE_ADDRESS))
            .build();
    final ToyAccount callerOf4788 =
        ToyAccount.builder()
            .address(Address.wrap(leftPadTo(Bytes.fromHexString("0x4788"), Address.SIZE)))
            .code(byteCodeCallingBeaconRootSystemAccountFromCallData(testInfo, BEACONROOT_ADDRESS))
            .build();

    final List<Transaction> genesisBlockTransactions = new ArrayList<>();
    final List<Transaction> blockNb1Transactions = new ArrayList<>();
    final List<Transaction> blockNb2Transactions = new ArrayList<>();

    final MultiBlockExecutionEnvironment.MultiBlockExecutionEnvironmentBuilder builder =
        MultiBlockExecutionEnvironment.builder(
            testInfo, systemContractDeployedBeforeBlockNumber == 0, 0);
    builder
        .accounts(
            List.of(senderAccount, deployerOf2935, deployerOf4788, callerOf2935, callerOf4788))
        .addBlock(genesisBlockTransactions)
        .addBlock(blockNb1Transactions)
        .addBlock(blockNb2Transactions)
        .build()
        .run();
  }
}
