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

    // Deployment transaction
    Transaction tx =
        ToyTransaction.builder()
            .sender(userAccount)
            //.to()
            .payload(Bytes.fromHexString("addhereinitializationcodewith0x"))
            .transactionType(TransactionType.FRONTIER)
            .gasLimit(0xffffffffL)
            .value(Wei.ZERO)
            .keyPair(keyPair)
            .build();
    /*
    - Initialization code is what you give in the payload of the transaction in case the "to" address is empty
      - It should contain some SLOAD and SSTORE
      - Have at least 2 storages keys to interact with (ideally 4). Both of them should be SLOAD and SSTORE several times. One should
      contain 0 and 2-3 containing non-zero.
      - Preferably interacting with the same storage key several times
      - Finish by deploying bytecode
      - At the end, it returns a slice of memory that will become the deployed bytecode
        - That bytecode should do some SLOAD and SSTORE
        - Preferably interacting with the same storage key several times
        - In particular, with the storage key that was used in the initialization but also new others

        Remix:
        - Deploy contract, initialization code is called "input". The construct should generate SLOAD nad SSTORE by modifying some variables in storage.
        - Create variables (in storage by default), SLOAD them (not explicitly), modify them, SSTORE them (not explicitly).
        - The same contract can have a function doing the same stuff.
      */



    ToyWorld toyWorld =
        ToyWorld.builder().accounts(List.of(userAccount)).build();

    ToyExecutionEnvironment toyExecutionEnvironment =
        ToyExecutionEnvironment.builder()
            .toyWorld(toyWorld)
            .transaction(tx)
            .testValidator(x -> {})
            .build();

    toyExecutionEnvironment.run();
  }

  @Test
  void deployContractModifyingStorageInFunctionTest() {

  }
}
