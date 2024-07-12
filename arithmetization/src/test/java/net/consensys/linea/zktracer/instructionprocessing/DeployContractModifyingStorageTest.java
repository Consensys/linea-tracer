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
public class DeployContractModifyingStorageTest {

  @Test
  void deployContractModifyingStorageInConstructorTest() {
    /* NOTE: The contract in this method is compiled by using
    solc *.sol --bin-runtime --evm-version london -o compiledContracts
    i.e., we do not include the init code of the contracts in the bytecode
    */

    // User address
    KeyPair keyPair = new SECP256K1().generateKeyPair();
    Address userAddress = Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));
    ToyAccount userAccount =
        ToyAccount.builder().balance(Wei.fromEth(1)).nonce(1).address(userAddress).build();

    // Contract modifying storage
    ToyAccount contractModifyingStorageAccount =
        ToyAccount.builder()
            .balance(Wei.fromEth(1))
            .nonce(2)
            .address(Address.fromHexString("0x")) // NOTE: here the address is intentionally set to 0x
            .code(Bytes.fromHexString("addherebytecodeofthecontractwithout0x"))
            .build();

    // Is this transaction necessary only if we want to invoke a specific function of the contract?
    // If so, we need it only in the other test case
    Transaction tx =
        ToyTransaction.builder()
            .sender(userAccount)
            .to(contractModifyingStorageAccount)
            .payload(Bytes.fromHexString("addherepayloadofthetransactionwith0x"))
            .transactionType(TransactionType.FRONTIER)
            .gasLimit(0xffffffffL)
            .value(Wei.ZERO)
            .keyPair(keyPair)
            .build();

    ToyWorld toyWorld =
        ToyWorld.builder().accounts(List.of(userAccount, contractModifyingStorageAccount)).build();

    ToyExecutionEnvironment toyExecutionEnvironment =
        ToyExecutionEnvironment.builder()
            .toyWorld(toyWorld)
            .transaction(tx)
            .testValidator(x -> {})
            .build();

    toyExecutionEnvironment.run();
  }

  @Test
  void deployContractModifyingStorageInFunctionTest() {}
}
