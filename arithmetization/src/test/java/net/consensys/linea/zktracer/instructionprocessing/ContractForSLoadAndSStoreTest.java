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

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.consensys.linea.UnitTestWatcher;
import net.consensys.linea.testing.ToyAccount;
import net.consensys.linea.testing.ToyExecutionEnvironmentV2;
import net.consensys.linea.testing.ToyTransaction;
import net.consensys.linea.zktracer.module.rlpcommon.RlpRandEdgeCase;
import net.consensys.linea.zktracer.types.AddressUtils;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.crypto.KeyPair;
import org.hyperledger.besu.crypto.SECP256K1;
import org.hyperledger.besu.crypto.SECPPublicKey;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.datatypes.TransactionType;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(UnitTestWatcher.class)
public class ContractForSLoadAndSStoreTest {

  @Test
  void contractModifyingStorageInFunctionTest() {
    // Deploy
    // arithmetization/src/test/resources/contracts/sloadAndSstore/ContractForSLoadAndSStoreTest.sol

    // User address
    final KeyPair keyPair = new SECP256K1().generateKeyPair();
    final Address userAddress =
        Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));
    final ToyAccount userAccount =
        ToyAccount.builder().balance(Wei.fromEth(1)).nonce(1).address(userAddress).build();

    // Deployment transaction
    final Transaction tx =
        ToyTransaction.builder()
            .sender(userAccount)
            .payload(
                Bytes.fromHexString(
                    ""))
            .transactionType(TransactionType.FRONTIER)
            .gasLimit(0x38444C0L)
            .value(Wei.ZERO)
            .keyPair(keyPair)
            .build();

    // TODO: add transaction to invoke incrementAndCall(0)

    ToyExecutionEnvironmentV2 toyExecutionEnvironment =
        ToyExecutionEnvironmentV2.builder().accounts(List.of(userAccount)).transaction(tx).build();

    toyExecutionEnvironment.run();
  }
}
