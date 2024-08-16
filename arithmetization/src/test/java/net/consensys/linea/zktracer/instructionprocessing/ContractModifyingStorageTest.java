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
    // NOTE: 3050 in the beginning has been added manually
    Transaction tx =
        ToyTransaction.builder()
            .sender(userAccount)
            .keyPair(keyPair)
            .transactionType(TransactionType.FRONTIER)
            .gasLimit(0xffffffL) // 0xffffffffL
            .payload(
                Bytes.fromHexString(
                    "0x3050608060405234801561000f575f80fd5b505f8081905550600180819055506002808190555060038081905550600a5f546100399190610109565b5f81905550600b60015461004d9190610109565b600181905550600c6002546100629190610109565b600281905550600d6003546100779190610109565b6003819055505f805461008a919061013c565b5f81905550601460015461009e919061013c565b60018190555060156002546100b3919061013c565b60028190555060156003546100c8919061013c565b60038190555061017d565b5f819050919050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52601160045260245ffd5b5f610113826100d3565b915061011e836100d3565b9250828201905080821115610136576101356100dc565b5b92915050565b5f610146826100d3565b9150610151836100d3565b925082820261015f816100d3565b91508282048414831517610176576101756100dc565b5b5092915050565b61012a8061018a5f395ff3fe6080604052348015600e575f80fd5b50600436106044575f3560e01c8063501e82121460485780636cc014de146062578063a314150f14607c578063a5d666a9146096575b5f80fd5b604e60b0565b6040516059919060dd565b60405180910390f35b606860b5565b6040516073919060dd565b60405180910390f35b608260bb565b604051608d919060dd565b60405180910390f35b609c60c1565b60405160a7919060dd565b60405180910390f35b5f5481565b60015481565b60025481565b60035481565b5f819050919050565b60d78160c7565b82525050565b5f60208201905060ee5f83018460d0565b9291505056fea26469706673582212203b939ad06f3d7d92a207fb36abc74231e00c81272ebf0645d4d40e917fc5e43b64736f6c634300081a0033"))
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
                    "0x6080604052348015600e575f80fd5b506102ba8061001c5f395ff3fe608060405234801561000f575f80fd5b5060043610610055575f3560e01c8063501e8212146100595780636cc014de14610077578063a314150f14610095578063a5d666a9146100b3578063e8af2fa5146100d1575b5f80fd5b6100616100db565b60405161006e91906101ca565b60405180910390f35b61007f6100e0565b60405161008c91906101ca565b60405180910390f35b61009d6100e6565b6040516100aa91906101ca565b60405180910390f35b6100bb6100ec565b6040516100c891906101ca565b60405180910390f35b6100d96100f2565b005b5f5481565b60015481565b60025481565b60035481565b5f8081905550600180819055506002808190555060038081905550600a5f5461011b9190610210565b5f81905550600b60015461012f9190610210565b600181905550600c6002546101449190610210565b600281905550600d6003546101599190610210565b6003819055505f805461016c9190610243565b5f8190555060146001546101809190610243565b60018190555060156002546101959190610243565b60028190555060156003546101aa9190610243565b600381905550565b5f819050919050565b6101c4816101b2565b82525050565b5f6020820190506101dd5f8301846101bb565b92915050565b7f4e487b71000000000000000000000000000000000000000000000000000000005f52601160045260245ffd5b5f61021a826101b2565b9150610225836101b2565b925082820190508082111561023d5761023c6101e3565b5b92915050565b5f61024d826101b2565b9150610258836101b2565b9250828202610266816101b2565b9150828204841483151761027d5761027c6101e3565b5b509291505056fea2646970667358221220b40040e95e9f0674ff5c258759b2ac4564a8cf968f9d59477114688f3eff824364736f6c634300081a0033"))
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
