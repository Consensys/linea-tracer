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

package net.consensys.linea.zktracer.module.oob;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.util.List;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import net.consensys.linea.zktracer.testing.BytecodeRunner;
import net.consensys.linea.zktracer.testing.EvmExtension;
import net.consensys.linea.zktracer.testing.ToyAccount;
import net.consensys.linea.zktracer.testing.ToyExecutionEnvironment;
import net.consensys.linea.zktracer.testing.ToyTransaction;
import net.consensys.linea.zktracer.testing.ToyWorld;
import net.consensys.linea.zktracer.types.EWord;
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
public class OobCallTest {

  @Test
  void TestCallSendValueGreaterThanBalanceHiNonZero() {
    EWord balanceOfCaller = EWord.of(BigInteger.ONE);
    EWord amountToSend = EWord.of(BigInteger.ONE, BigInteger.ZERO);

    testCallSendValue(balanceOfCaller, amountToSend, 1);
  }

  @Test
  void TestCallSendValueGreaterThanBalanceLoNonZero() {
    EWord balanceOfCaller = EWord.of(BigInteger.ONE);
    EWord amountToSend = EWord.of(BigInteger.ZERO, BigInteger.TWO);

    testCallSendValue(balanceOfCaller, amountToSend, 1);
  }

  @Test
  void TestCallSendValueGreaterThanBalanceHiLoNonZero() {
    EWord balanceOfCaller = EWord.of(BigInteger.ONE);
    EWord amountToSend = EWord.of(BigInteger.TWO, BigInteger.TWO);

    testCallSendValue(balanceOfCaller, amountToSend, 1);
  }

  @Test
  void TestCallSendValueSmallerThanBalanceLoNonZero() {
    EWord balanceOfCaller = EWord.of(BigInteger.TWO);
    EWord amountToSend = EWord.of(BigInteger.ZERO, BigInteger.ONE);

    testCallSendValue(balanceOfCaller, amountToSend);
  }

  /*
  The two tests below may fail because of java.lang.OutOfMemoryError: Java heap space
  unless maxHeapSize is adjusted in tests.gradle
   */
  @Test
  void testRecursiveCalls1024() {
    EWord iterations = EWord.of(BigInteger.valueOf(1024));

    testRecursiveCalls(iterations);
  }

  @Test
  void testRecursiveCalls1025() {
    EWord iterations = EWord.of(BigInteger.valueOf(1025));

    testRecursiveCalls(iterations, 1);
  }

  @Test
  void TestRecursiveCallsWithBytecode() {
    BytecodeRunner bytecodeRunner =
        BytecodeRunner.of(Bytes.fromHexString("60006000600060006000305af1"));
    bytecodeRunner.run(Wei.fromEth(400), 0xfffffffffffL);

    Hub hub = bytecodeRunner.getHub();

    assertTrue(Exceptions.none(hub.pch().exceptions()));

    // assertNumberOfOnesInOobEvent1(bytecodeRunner.getHub().oob(), 1);
  }

  // Support methods
  private void testCallSendValue(EWord balanceOfCaller, EWord amountToSend) {
    testCallSendValue(balanceOfCaller, amountToSend, 0);
  }

  private void testCallSendValue(
      EWord balanceOfCaller, EWord amountToSend, int numberOfOnesInOobEvent1) {
    /* NOTE: The contracts in this method are compiled by using
    solc *.sol --bin-runtime --evm-version london -o compiledContracts
    i.e., we do not include the init code of the contracts in the bytecode
    */

    // User address
    KeyPair keyPair = new SECP256K1().generateKeyPair();
    Address userAddress = Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));
    ToyAccount userAccount =
        ToyAccount.builder().balance(Wei.fromEth(1)).nonce(1).address(userAddress).build();

    // Caller
    ToyAccount contractCallerAccount =
        ToyAccount.builder()
            .balance(Wei.of(balanceOfCaller.toBigInteger()))
            .nonce(2)
            .address(Address.fromHexString("0xd9145CCE52D386f254917e481eB44e9943F39138"))
            .code(
                Bytes.fromHexString(
                    "60806040526004361061002d5760003560e01c806363acac8e14610039578063ff277a621461006257610034565b3661003457005b600080fd5b34801561004557600080fd5b50610060600480360381019061005b91906101b5565b61008b565b005b34801561006e57600080fd5b5061008960048036038101906100849190610240565b61010f565b005b600081111561010c573073ffffffffffffffffffffffffffffffffffffffff166363acac8e6001836100bd91906102af565b6040518263ffffffff1660e01b81526004016100d991906102f2565b600060405180830381600087803b1580156100f357600080fd5b505af1158015610107573d6000803e3d6000fd5b505050505b50565b60008290508073ffffffffffffffffffffffffffffffffffffffff1663671dcbd7836040518263ffffffff1660e01b81526004016000604051808303818588803b15801561015c57600080fd5b505af1158015610170573d6000803e3d6000fd5b5050505050505050565b600080fd5b6000819050919050565b6101928161017f565b811461019d57600080fd5b50565b6000813590506101af81610189565b92915050565b6000602082840312156101cb576101ca61017a565b5b60006101d9848285016101a0565b91505092915050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b600061020d826101e2565b9050919050565b61021d81610202565b811461022857600080fd5b50565b60008135905061023a81610214565b92915050565b600080604083850312156102575761025661017a565b5b60006102658582860161022b565b9250506020610276858286016101a0565b9150509250929050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b60006102ba8261017f565b91506102c58361017f565b92508282039050818111156102dd576102dc610280565b5b92915050565b6102ec8161017f565b82525050565b600060208201905061030760008301846102e3565b9291505056fea2646970667358221220eeda6cd078a1e0b43b7e0e6267949ef02a8119de7d68431781e3b1ef33a616d464736f6c63430008150033"))
            .build();

    // Callee
    ToyAccount contractCalleeAccount =
        ToyAccount.builder()
            .balance(Wei.fromEth(1))
            .nonce(3)
            .address(Address.fromHexString("0xd8b934580fcE35a11B58C6D73aDeE468a2833fa8"))
            .code(
                Bytes.fromHexString(
                    "60806040526004361061002d5760003560e01c806363acac8e14610039578063ff277a621461006257610034565b3661003457005b600080fd5b34801561004557600080fd5b50610060600480360381019061005b91906101b5565b61008b565b005b34801561006e57600080fd5b5061008960048036038101906100849190610240565b61010f565b005b600081111561010c573073ffffffffffffffffffffffffffffffffffffffff166363acac8e6001836100bd91906102af565b6040518263ffffffff1660e01b81526004016100d991906102f2565b600060405180830381600087803b1580156100f357600080fd5b505af1158015610107573d6000803e3d6000fd5b505050505b50565b60008290508073ffffffffffffffffffffffffffffffffffffffff1663671dcbd7836040518263ffffffff1660e01b81526004016000604051808303818588803b15801561015c57600080fd5b505af1158015610170573d6000803e3d6000fd5b5050505050505050565b600080fd5b6000819050919050565b6101928161017f565b811461019d57600080fd5b50565b6000813590506101af81610189565b92915050565b6000602082840312156101cb576101ca61017a565b5b60006101d9848285016101a0565b91505092915050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b600061020d826101e2565b9050919050565b61021d81610202565b811461022857600080fd5b50565b60008135905061023a81610214565b92915050565b600080604083850312156102575761025661017a565b5b60006102658582860161022b565b9250506020610276858286016101a0565b9150509250929050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b60006102ba8261017f565b91506102c58361017f565b92508282039050818111156102dd576102dc610280565b5b92915050565b6102ec8161017f565b82525050565b600060208201905061030760008301846102e3565b9291505056fea2646970667358221220eeda6cd078a1e0b43b7e0e6267949ef02a8119de7d68431781e3b1ef33a616d464736f6c63430008150033"))
            .build();

    Transaction tx =
        ToyTransaction.builder()
            .sender(userAccount)
            .to(contractCallerAccount)
            .payload(
                Bytes.fromHexString(
                    "0xff277a62000000000000000000000000d8b934580fce35a11b58c6d73adee468a2833fa8"
                        + amountToSend.toString().substring(2)))
            .transactionType(TransactionType.FRONTIER)
            .gasLimit(0xffffffffL)
            .value(Wei.ZERO)
            .keyPair(keyPair)
            .build();

    ToyWorld toyWorld =
        ToyWorld.builder()
            .accounts(List.of(userAccount, contractCallerAccount, contractCalleeAccount))
            .build();

    ToyExecutionEnvironment toyExecutionEnvironment =
        ToyExecutionEnvironment.builder()
            .toyWorld(toyWorld)
            .transaction(tx)
            .testValidator(x -> {})
            .build();

    toyExecutionEnvironment.run();

    Hub hub = toyExecutionEnvironment.getHub();

    assertTrue(Exceptions.none(hub.pch().exceptions()));

    // assertNumberOfOnesInOobEvent1(toyExecutionEnvironment.getHub().oob(),
    // numberOfOnesInOobEvent1);
  }

  private void testRecursiveCalls(EWord iterations) {
    testRecursiveCalls(iterations, 0);
  }

  private void testRecursiveCalls(EWord iterations, int numberOfOnesInOobEvent1) {
    /* NOTE: The contracts in this method are compiled by using
    solc *.sol --bin-runtime --evm-version london -o compiledContracts
    i.e., we do not include the init code of the contracts in the bytecode
    */
    // User address
    KeyPair keyPair = new SECP256K1().generateKeyPair();
    Address userAddress = Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));
    ToyAccount userAccount =
        ToyAccount.builder().balance(Wei.fromEth(25)).nonce(1).address(userAddress).build();

    // Caller
    ToyAccount contractCallerAccount =
        ToyAccount.builder()
            .balance(Wei.fromEth(1))
            .nonce(2)
            .address(Address.fromHexString("0xd9145CCE52D386f254917e481eB44e9943F39138"))
            .code(
                Bytes.fromHexString(
                    "60806040526004361061002d5760003560e01c806363acac8e14610039578063ff277a621461006257610034565b3661003457005b600080fd5b34801561004557600080fd5b50610060600480360381019061005b91906101b5565b61008b565b005b34801561006e57600080fd5b5061008960048036038101906100849190610240565b61010f565b005b600081111561010c573073ffffffffffffffffffffffffffffffffffffffff166363acac8e6001836100bd91906102af565b6040518263ffffffff1660e01b81526004016100d991906102f2565b600060405180830381600087803b1580156100f357600080fd5b505af1158015610107573d6000803e3d6000fd5b505050505b50565b60008290508073ffffffffffffffffffffffffffffffffffffffff1663671dcbd7836040518263ffffffff1660e01b81526004016000604051808303818588803b15801561015c57600080fd5b505af1158015610170573d6000803e3d6000fd5b5050505050505050565b600080fd5b6000819050919050565b6101928161017f565b811461019d57600080fd5b50565b6000813590506101af81610189565b92915050565b6000602082840312156101cb576101ca61017a565b5b60006101d9848285016101a0565b91505092915050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b600061020d826101e2565b9050919050565b61021d81610202565b811461022857600080fd5b50565b60008135905061023a81610214565b92915050565b600080604083850312156102575761025661017a565b5b60006102658582860161022b565b9250506020610276858286016101a0565b9150509250929050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b60006102ba8261017f565b91506102c58361017f565b92508282039050818111156102dd576102dc610280565b5b92915050565b6102ec8161017f565b82525050565b600060208201905061030760008301846102e3565b9291505056fea2646970667358221220eeda6cd078a1e0b43b7e0e6267949ef02a8119de7d68431781e3b1ef33a616d464736f6c63430008150033"))
            .build();

    Transaction tx =
        ToyTransaction.builder()
            .sender(userAccount)
            .to(contractCallerAccount)
            .payload(Bytes.fromHexString("0x63acac8e" + iterations.toString().substring(2)))
            .transactionType(TransactionType.FRONTIER)
            .gasLimit(0xffffffffffL)
            .value(Wei.ZERO)
            .keyPair(keyPair)
            .build();

    ToyWorld toyWorld =
        ToyWorld.builder().accounts(List.of(userAccount, contractCallerAccount)).build();

    ToyExecutionEnvironment toyExecutionEnvironment =
        ToyExecutionEnvironment.builder()
            .toyWorld(toyWorld)
            .transaction(tx)
            .testValidator(x -> {})
            .build();

    toyExecutionEnvironment.run();

    Hub hub = toyExecutionEnvironment.getHub();

    assertTrue(Exceptions.none(hub.pch().exceptions()));

    // assertNumberOfOnesInOobEvent1(toyExecutionEnvironment.getHub().oob(),
    // numberOfOnesInOobEvent1);
  }
}
