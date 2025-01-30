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

import net.consensys.linea.UnitTestWatcher;
import net.consensys.linea.testing.ToyAccount;
import net.consensys.linea.testing.ToyExecutionEnvironmentV2;
import net.consensys.linea.testing.ToyTransaction;
import net.consensys.linea.testing.TransactionProcessingResultValidator;
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

@ExtendWith(UnitTestWatcher.class)
public class ContractForSLoadAndSStoreTest {
  // See https://github.com/Consensys/linea-tracer/issues/1660 for documentation

  /* NOTE: The contracts in this test are compiled by using
  solc *.sol --bin-runtime --evm-version london -o compiledContracts
  */

  @Test
  void contractForSLoadAndSStoreTest() {
    // arithmetization/src/test/resources/contracts/sloadAndSstore/ContractForSLoadAndSStoreTest.sol
    String contractCodeAsString =
        "608060405234801561001057600080fd5b50600436106100575760003560e01c806303a3d1f31461005c57806309e3d31d1461007a57806360a394a51461009857806361bc221a146100b45780636f172129146100d2575b600080fd5b6100646100ee565b60405161007191906102f6565b60405180910390f35b6100826100f3565b60405161008f9190610352565b60405180910390f35b6100b260048036038101906100ad919061039e565b610119565b005b6100bc61015d565b6040516100c991906102f6565b60405180910390f35b6100ec60048036038101906100e791906103f7565b610163565b005b600581565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b80600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b60005481565b6001816101709190610453565b6000819055507f4785d80d2593e2cb7a3331d31eb5106408bdde2aab0db9e9b616b036a1b6039d6000546040516101a791906102f6565b60405180910390a16005600054106101f4576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016101eb9061050a565b60405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff16600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16146102da57600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16636f1721296000546040518263ffffffff1660e01b81526004016102a791906102f6565b600060405180830381600087803b1580156102c157600080fd5b505af11580156102d5573d6000803e3d6000fd5b505050505b50565b6000819050919050565b6102f0816102dd565b82525050565b600060208201905061030b60008301846102e7565b92915050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b600061033c82610311565b9050919050565b61034c81610331565b82525050565b60006020820190506103676000830184610343565b92915050565b600080fd5b61037b81610331565b811461038657600080fd5b50565b60008135905061039881610372565b92915050565b6000602082840312156103b4576103b361036d565b5b60006103c284828501610389565b91505092915050565b6103d4816102dd565b81146103df57600080fd5b50565b6000813590506103f1816103cb565b92915050565b60006020828403121561040d5761040c61036d565b5b600061041b848285016103e2565b91505092915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b600061045e826102dd565b9150610469836102dd565b925082820190508082111561048157610480610424565b5b92915050565b600082825260208201905092915050565b7f636f756e746572207265616368656420434f554e5445525f5448524553484f4c60008201527f445f464f525f5245564552540000000000000000000000000000000000000000602082015250565b60006104f4602c83610487565b91506104ff82610498565b604082019050919050565b60006020820190508181036000830152610523816104e7565b905091905056fea26469706673582212203aa582b50d2d094e033fbe4389b1ff0b7ffc053fb3226b2cbb985ab399f06d9464736f6c63430008190033";
    Address addressA = Address.fromHexString("0x0498B7c793D7432Cd9dB27fb02fc9cfdBAfA1Fd3");
    Address addressB = Address.fromHexString("0xEf9f1ACE83dfbB8f559Da621f4aEA72C6EB10eBf");
    Address addressC = Address.fromHexString("0x540d7E428D5207B30EE03F2551Cbb5751D3c7569");
    Address addressD = Address.fromHexString("0x4a9C121080f6D9250Fc0143f41B595fD172E31bf");
    Address addressE = Address.fromHexString("0x406AB5033423Dcb6391Ac9eEEad73294FA82Cfbc");

    // User address
    KeyPair keyPair = new SECP256K1().generateKeyPair();
    Address userAddress = Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));
    ToyAccount userAccount =
        ToyAccount.builder().balance(Wei.fromEth(100)).nonce(1).address(userAddress).build();

    // A
    ToyAccount contractAccountA =
        ToyAccount.builder()
            .balance(Wei.fromEth(1))
            .nonce(2)
            .address(addressA)
            .code(Bytes.fromHexString(contractCodeAsString))
            .build();

    // B
    ToyAccount contractAccountB =
        ToyAccount.builder()
            .balance(Wei.fromEth(1))
            .nonce(3)
            .address(addressB)
            .code(Bytes.fromHexString(contractCodeAsString))
            .build();

    // C
    ToyAccount contractAccountC =
        ToyAccount.builder()
            .balance(Wei.fromEth(1))
            .nonce(4)
            .address(addressC)
            .code(Bytes.fromHexString(contractCodeAsString))
            .build();

    // D
    ToyAccount contractAccountD =
        ToyAccount.builder()
            .balance(Wei.fromEth(1))
            .nonce(5)
            .address(addressD)
            .code(Bytes.fromHexString(contractCodeAsString))
            .build();

    // E
    ToyAccount contractAccountE =
        ToyAccount.builder()
            .balance(Wei.fromEth(1))
            .nonce(6)
            .address(addressE)
            .code(Bytes.fromHexString(contractCodeAsString))
            .build();

    // Setting nextInstanceAddress
    Transaction txToAToSetNextInstanceAddress =
        ToyTransaction.builder()
            .sender(userAccount)
            .to(contractAccountA)
            .payload(
                Bytes.fromHexString(
                    "0x60a394a5000000000000000000000000" + addressB.toString().substring(2)))
            .transactionType(TransactionType.FRONTIER)
            .value(Wei.ZERO)
            .keyPair(keyPair)
            .nonce(1L)
            .build();

    Transaction txToBToSetNextInstanceAddress =
        ToyTransaction.builder()
            .sender(userAccount)
            .to(contractAccountB)
            .payload(
                Bytes.fromHexString(
                    "0x60a394a5000000000000000000000000" + addressC.toString().substring(2)))
            .transactionType(TransactionType.FRONTIER)
            .value(Wei.ZERO)
            .keyPair(keyPair)
            .nonce(2L)
            .build();

    Transaction txToCToSetNextInstanceAddress =
        ToyTransaction.builder()
            .sender(userAccount)
            .to(contractAccountC)
            .payload(
                Bytes.fromHexString(
                    "0x60a394a5000000000000000000000000" + addressD.toString().substring(2)))
            .transactionType(TransactionType.FRONTIER)
            .value(Wei.ZERO)
            .keyPair(keyPair)
            .nonce(3L)
            .build();

    Transaction txToDToSetNextInstanceAddress =
        ToyTransaction.builder()
            .sender(userAccount)
            .to(contractAccountD)
            .payload(
                Bytes.fromHexString(
                    "0x60a394a5000000000000000000000000" + addressE.toString().substring(2)))
            .transactionType(TransactionType.FRONTIER)
            .value(Wei.ZERO)
            .keyPair(keyPair)
            .nonce(4L)
            .build();

    Transaction txToEToSetNextInstanceAddress =
        ToyTransaction.builder()
            .sender(userAccount)
            .to(contractAccountE)
            .payload(
                Bytes.fromHexString(
                    "0x60a394a50000000000000000000000000000000000000000000000000000000000000000"))
            .transactionType(TransactionType.FRONTIER)
            .value(Wei.ZERO)
            .keyPair(keyPair)
            .nonce(5L)
            .build();

    // Initiate recursive calls
    Transaction txToAToInitiateRecursiveCalls =
        ToyTransaction.builder()
            .sender(userAccount)
            .to(contractAccountA)
            .payload(
                Bytes.fromHexString(
                    "0x6f1721290000000000000000000000000000000000000000000000000000000000000000"))
            .transactionType(TransactionType.FRONTIER)
            .value(Wei.ZERO)
            .keyPair(keyPair)
            .nonce(6L)
            .gasLimit(0xffffffL)
            .build();

    /* Expected behaviour: the contract will call itself recursively until the depth reaches 5
     * EOA A.incrementAndCall(0)
     * A. 1 < 5 -> B.incrementAndCall(1)
     * B. 2 < 5 -> C.incrementAndCall(2)
     * C. 3 < 5 -> D.incrementAndCall(3)
     * D. 4 < 5 -> E.incrementAndCall(4)
     * E. 5 = 5 -> REVERT
     */

    List<ToyAccount> accounts =
        List.of(
            userAccount,
            contractAccountA,
            contractAccountB,
            contractAccountC,
            contractAccountD,
            contractAccountE);

    ToyExecutionEnvironmentV2 toyExecutionEnvironmentV2 =
        ToyExecutionEnvironmentV2.builder()
            .accounts(accounts)
            .transactions(
                List.of(
                    txToAToSetNextInstanceAddress,
                    txToBToSetNextInstanceAddress,
                    txToCToSetNextInstanceAddress,
                    txToDToSetNextInstanceAddress,
                    txToEToSetNextInstanceAddress,
                    txToAToInitiateRecursiveCalls))
            .transactionProcessingResultValidator(
                TransactionProcessingResultValidator.EMPTY_VALIDATOR)
            .build();

    toyExecutionEnvironmentV2.run();
  }
}
