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

package net.consensys.linea.zktracer.module.hub;

import static net.consensys.linea.testing.ToyExecutionEnvironmentV2.DEFAULT_COINBASE_ADDRESS;
import static net.consensys.linea.zktracer.module.hub.AddressCollisions.*;
import static net.consensys.linea.zktracer.types.AddressUtils.getCreate2RawAddress;
import static net.consensys.linea.zktracer.types.Utils.leftPadTo;
import static net.consensys.linea.zktracer.types.Utils.rightPadTo;
import static org.hyperledger.besu.crypto.Hash.keccak256;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.ToyAccount;
import net.consensys.linea.testing.ToyExecutionEnvironmentV2;
import net.consensys.linea.testing.ToyTransaction;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.crypto.KeyPair;
import org.hyperledger.besu.crypto.SECP256K1;
import org.hyperledger.besu.datatypes.*;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class AddressColisionWarmingAndDeploymentTests {

  // sender account
  private static final KeyPair senderKeyPair = new SECP256K1().generateKeyPair();
  private static final Address senderAddress =
      Address.extract(Hash.hash(senderKeyPair.getPublicKey().getEncodedBytes()));
  private static final ToyAccount senderAccount =
      ToyAccount.builder().balance(Wei.fromEth(123)).nonce(12).address(senderAddress).build();

  private static final Address RECIPIENT_STD_ADDRESS =
      Address.wrap(leftPadTo(Bytes.fromHexString("0xdeadbeef"), Address.SIZE));

  private static final Bytes32 STD_KEY = Bytes32.wrap(Bytes.fromHexString("0xdeadbeef"));
  private static final Bytes SSTORE_INITCODE =
      BytecodeCompiler.newProgram()
          .push("Ox7a12e") // value
          .push(STD_KEY) // key
          .op(OpCode.SSTORE)
          .compile();

  private final Bytes32 INITCODE_HASH = keccak256(SSTORE_INITCODE);

  private static final Bytes CREATE2_AND_SSTORE =
      BytecodeCompiler.newProgram()
          .push(0) // offset
          .push(SSTORE_INITCODE) // value
          .op(OpCode.MSTORE)
          .push(0) // salt
          .push(SSTORE_INITCODE.size()) // size
          .push(0) // offset
          .push(0) // value
          .op(OpCode.CREATE2)
          .compile();

  private static Stream<Arguments> inputs() {
    final List<Arguments> arguments = new ArrayList<>();

    for (int skip = 0; skip <= 1; skip++) {
      for (AddressCollisions collision : AddressCollisions.values()) {
        for (int isDeployment = 0; isDeployment <= 1; isDeployment++) {
          for (WarmingScenarii warming : WarmingScenarii.values()) {
            arguments.add(Arguments.of(skip == 1, collision, isDeployment == 1, warming));
          }
        }
      }
    }

    return arguments.stream();
  }

  @ParameterizedTest
  @MethodSource("inputs")
  void addressCollisionWarmingAndDeployment(
      boolean skip, AddressCollisions collision, boolean deployment, WarmingScenarii warming) {

    // not possible to have a sender and recipient collision
    if ((deployment || !skip) && senderRecipientCollision(collision)) {
      return;
    }

    // there is no point as we skip the tx
    if (skip && warming == WarmingScenarii.WARMING_TO_BE_DEPLOYED_STORAGE) {
      return;
    }

    final ToyAccount receiverAccount =
        ToyAccount.builder()
            .balance(Wei.fromEth(12))
            .nonce(128)
            .address(senderRecipientCollision(collision) ? senderAddress : RECIPIENT_STD_ADDRESS)
            .code(skip ? Bytes.EMPTY : CREATE2_AND_SSTORE)
            .build();

    final Address effectiveToAddress = receiverAccount.getAddress();

    Address coinBaseAddress = DEFAULT_COINBASE_ADDRESS;
    if (recipientCoinbaseCollision(collision)) {
      coinBaseAddress = effectiveToAddress;
    }
    if (senderCoinbaseCollision(collision)) {
      coinBaseAddress = senderAddress;
    }

    final List<AccessListEntry> accessList = new ArrayList<>();
    switch (warming) {
      case NO_WARMING -> {}
      case WARMING_SENDER -> accessList.add(new AccessListEntry(senderAddress, List.of()));
      case WARMING_RECIPIENT -> accessList.add(new AccessListEntry(effectiveToAddress, List.of()));
      case WARMING_COINBASE -> accessList.add(new AccessListEntry(coinBaseAddress, List.of()));
      case WARMING_PRECOMPILE -> {
        accessList.add(new AccessListEntry(Address.MODEXP, List.of()));
        accessList.add(
            new AccessListEntry(Address.ID, List.of(Bytes32.ZERO, Bytes32.repeat((byte) 1))));
      }
      case WARMING_TO_BE_DEPLOYED_STORAGE -> {
        final Address deployerAddress = deployment ? senderAddress : receiverAccount.getAddress();
        final Address deployedAddress =
            Address.extract(getCreate2RawAddress(deployerAddress, Bytes32.ZERO, INITCODE_HASH));
        accessList.add(new AccessListEntry(deployedAddress, List.of(STD_KEY, Bytes32.ZERO)));
      }
      case RANDOM_ADDRESS_DUPLICATE -> {
        accessList.add(
            new AccessListEntry(
                Address.wrap(leftPadTo(Bytes.fromHexString("0xbadb077"), Address.SIZE)),
                List.of(Bytes32.ZERO, Bytes32.repeat((byte) 1))));
        accessList.add(
            new AccessListEntry(
                Address.wrap(rightPadTo(Bytes.fromHexString("0xxbadb077"), Address.SIZE)),
                List.of()));
      }
    }

    final Transaction tx =
        ToyTransaction.builder()
            .sender(senderAccount)
            .to(deployment ? null : receiverAccount)
            .keyPair(senderKeyPair)
            .gasLimit(300000L)
            .transactionType(TransactionType.ACCESS_LIST)
            .accessList(accessList)
            .value(Wei.of(1000))
            .payload(deployment && !skip ? CREATE2_AND_SSTORE : Bytes.EMPTY)
            .build();

    ToyExecutionEnvironmentV2.builder()
        .accounts(List.of(senderAccount, receiverAccount))
        .transaction(tx)
        .coinbase(coinBaseAddress)
        .zkTracerValidator(zkTracer -> {})
        .build()
        .run();
  }
}
