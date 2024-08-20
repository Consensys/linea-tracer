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

import static net.consensys.linea.zktracer.module.rlpcommon.RlpRandEdgeCase.randData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.base.Preconditions;
import net.consensys.linea.testing.ToyAccount;
import net.consensys.linea.testing.ToyExecutionEnvironment;
import net.consensys.linea.testing.ToyTransaction;
import net.consensys.linea.testing.ToyWorld;
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

public class ContractModifyingStorageTest {

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

  // NOTE: 0.8.0+commit.c7dfd78e compiler version is used and Remix VM (London) as environment to
  // compile and deploy the contracts below
  @Test
  void contractModifyingStorageInConstructorTest() {
    // Deploy
    // arithmetization/src/test/resources/contracts/contractModifyingStorage/ContractModifyingStorageInConstructor.sol

    // User address
    KeyPair keyPair = new SECP256K1().generateKeyPair();
    Address userAddress = Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));
    ToyAccount userAccount =
        ToyAccount.builder().balance(Wei.fromEth(1000)).nonce(1).address(userAddress).build();

    System.out.println("User address: " + userAddress);

    // Deployment transaction
    Transaction tx =
        ToyTransaction.builder()
            .sender(userAccount)
            .keyPair(keyPair)
            .transactionType(TransactionType.FRONTIER)
            .gasLimit(0xffffffL) // 0xffffffffL
            .payload(
                Bytes.fromHexString(
                    "0x608060405234801561001057600080fd5b5060008081905550600180819055506002808190555060038081905550600a60005461003c91906100d9565b600081905550600b60015461005191906100d9565b600181905550600c60025461006691906100d9565b600281905550600d60035461007b91906100d9565b6003819055506000805461008f919061012f565b60008190555060146001546100a4919061012f565b60018190555060156002546100b9919061012f565b60028190555060156003546100ce919061012f565b6003819055506101c2565b60006100e482610189565b91506100ef83610189565b9250827fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff0382111561012457610123610193565b5b828201905092915050565b600061013a82610189565b915061014583610189565b9250817fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff048311821515161561017e5761017d610193565b5b828202905092915050565b6000819050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b610131806101d16000396000f3fe6080604052348015600f57600080fd5b506004361060465760003560e01c8063501e821214604b5780636cc014de146065578063a314150f14607f578063a5d666a9146099575b600080fd5b605160b3565b604051605c919060d8565b60405180910390f35b606b60b9565b6040516076919060d8565b60405180910390f35b608560bf565b6040516090919060d8565b60405180910390f35b609f60c5565b60405160aa919060d8565b60405180910390f35b60005481565b60015481565b60025481565b60035481565b60d28160f1565b82525050565b600060208201905060eb600083018460cb565b92915050565b600081905091905056fea2646970667358221220cebaf0c22dd1be33c20b916dce0c167bffc36711ff2f805c6d8667fe803601f464736f6c63430008000033"))
            .build();

    Address deployedAddress = AddressUtils.effectiveToAddress(tx);
    System.out.println("Deployed address: " + deployedAddress);

    Preconditions.checkArgument(tx.isContractCreation());

    ToyWorld toyWorld = ToyWorld.builder().accounts(List.of(userAccount)).build();

    ToyExecutionEnvironment toyExecutionEnvironment =
        ToyExecutionEnvironment.builder()
            .toyWorld(toyWorld)
            .transaction(tx)
            .testValidator(x -> {})
            .build();

    toyExecutionEnvironment.run();
  }

  @Test
  void contractModifyingStorageInFunctionTest() {
    // Deploy
    // arithmetization/src/test/resources/contracts/contractModifyingStorage/ContractModifyingStorageInFunction.sol

    // User address
    KeyPair keyPair = new SECP256K1().generateKeyPair();
    Address userAddress = Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));
    ToyAccount userAccount =
        ToyAccount.builder().balance(Wei.fromEth(1)).nonce(1).address(userAddress).build();

    // Deployment transaction
    Transaction tx =
        ToyTransaction.builder()
            .sender(userAccount)
            .payload(
                Bytes.fromHexString(
                    "0x608060405234801561001057600080fd5b50610304806100206000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c8063501e82121461005c5780636cc014de1461007a578063a314150f14610098578063a5d666a9146100b6578063e8af2fa5146100d4575b600080fd5b6100646100de565b60405161007191906101ca565b60405180910390f35b6100826100e4565b60405161008f91906101ca565b60405180910390f35b6100a06100ea565b6040516100ad91906101ca565b60405180910390f35b6100be6100f0565b6040516100cb91906101ca565b60405180910390f35b6100dc6100f6565b005b60005481565b60015481565b60025481565b60035481565b60008081905550600180819055506002808190555060038081905550600a60005461012191906101e5565b600081905550600b60015461013691906101e5565b600181905550600c60025461014b91906101e5565b600281905550600d60035461016091906101e5565b60038190555060008054610174919061023b565b6000819055506014600154610189919061023b565b600181905550601560025461019e919061023b565b60028190555060156003546101b3919061023b565b600381905550565b6101c481610295565b82525050565b60006020820190506101df60008301846101bb565b92915050565b60006101f082610295565b91506101fb83610295565b9250827fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff038211156102305761022f61029f565b5b828201905092915050565b600061024682610295565b915061025183610295565b9250817fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff048311821515161561028a5761028961029f565b5b828202905092915050565b6000819050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fdfea26469706673582212200d443753b26ea215c94e8fd1b03e4d389ebc740d96823003d127316393455da664736f6c63430008000033"))
            .transactionType(TransactionType.FRONTIER)
            .gasLimit(0xffffffffL)
            .value(Wei.ZERO)
            .keyPair(keyPair)
            .build();

    ToyWorld toyWorld = ToyWorld.builder().accounts(List.of(userAccount)).build();

    ToyExecutionEnvironment toyExecutionEnvironment =
        ToyExecutionEnvironment.builder()
            .toyWorld(toyWorld)
            .transaction(tx)
            .testValidator(x -> {})
            .build();

    // TODO: add transaction to invoke function

    toyExecutionEnvironment.run();
  }

  private final Random rnd = new Random(666);

  @Test
  void temporaryTest() {
    ToyWorld.ToyWorldBuilder world = ToyWorld.builder();
    List<Transaction> txList = new ArrayList<>();

    KeyPair keyPair1 = new SECP256K1().generateKeyPair();
    SECPPublicKey pk1 = keyPair1.getPublicKey();
    Address address1 = Address.extract(Hash.hash(pk1.getEncodedBytes()));
    ToyAccount account1 = buildAnAccount(address1, 7);

    KeyPair keyPair2 = new SECP256K1().generateKeyPair();
    SECPPublicKey pk2 = keyPair2.getPublicKey();
    ToyAccount account2 = buildAnAccount(Address.extract(Hash.hash(pk2.getEncodedBytes())), 7);

    KeyPair keyPair3 = new SECP256K1().generateKeyPair();
    SECPPublicKey pk3 = keyPair3.getPublicKey();
    ToyAccount account3 = buildAnAccount(Address.extract(Hash.hash(pk3.getEncodedBytes())), 7);

    KeyPair keyPair4 = new SECP256K1().generateKeyPair();
    SECPPublicKey pk4 = keyPair4.getPublicKey();
    ToyAccount account4 =
        ToyAccount.builder()
            .balance(Wei.fromEth(10))
            .nonce(7)
            .address(Address.extract(Hash.hash(pk4.getEncodedBytes())))
            .code(Bytes.fromHexString("0x3050"))
            .build();

    KeyPair keyPair5 = new SECP256K1().generateKeyPair();
    SECPPublicKey pk5 = keyPair5.getPublicKey();
    ToyAccount account5 = buildAnAccount(Address.extract(Hash.hash(pk5.getEncodedBytes())), 7);

    Bytes initCode = randData(true);
    world.accounts(List.of(account1, account2, account3, account4, account5));

    // TODO: deployment transaction fails in our ToyWorld
    /*
    txList.add(
        ToyTransaction.builder()
            .sender(account1)
            .keyPair(keyPair1)
            .transactionType(TransactionType.FRONTIER)
            .gasLimit(rnd.nextLong(21000, 0xffffL))
            .payload(initCode)
            .value(Wei.fromEth(1))
            .build());
    */

    txList.add(
        ToyTransaction.builder()
            .sender(account2)
            .to(account4)
            .keyPair(keyPair2)
            .transactionType(TransactionType.FRONTIER)
            .gasLimit(rnd.nextLong(21000, 0xffffL))
            .value(Wei.fromEth(2))
            .build());

    txList.add(
        ToyTransaction.builder()
            .sender(account3)
            .to(account4)
            .keyPair(keyPair3)
            .transactionType(TransactionType.FRONTIER)
            .gasLimit(rnd.nextLong(21000, 0xffffL))
            .value(Wei.fromEth(3))
            .build());

    ToyExecutionEnvironment.builder()
        .toyWorld(world.build())
        .transactions(txList)
        .testValidator(x -> {})
        .build()
        .run();
  }

  // Temporary support function
  final ToyAccount buildAnAccount(Address address, long nonce) {
    return ToyAccount.builder().balance(Wei.fromEth(10)).nonce(nonce).address(address).build();
  }
}
