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

package net.consensys.linea.zktracer.precompiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import lombok.Getter;
import net.consensys.linea.testing.ToyAccount;
import net.consensys.linea.testing.ToyExecutionEnvironmentV2;
import net.consensys.linea.testing.ToyTransaction;
import net.consensys.linea.testing.TransactionProcessingResultValidator;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.crypto.KeyPair;
import org.hyperledger.besu.crypto.SECP256K1;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.datatypes.TransactionType;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ModexpTestForSequencer {
  Random RANDOM = new Random(123);

  @ParameterizedTest
  @MethodSource("modexpTestForSequencerSource")
  void modexpTestForSequencer(Bytes input, boolean willFailDueToConstraints) {
    // arithmetization/src/test/resources/contracts/modexp/ModexpContract.sol
    // solc-select use 0.8.20
    // solc *.sol --bin-runtime --evm-version london -o compiledContracts

    // User address
    KeyPair keyPair = new SECP256K1().generateKeyPair();
    Address userAddress = Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));
    ToyAccount userAccount =
        ToyAccount.builder().balance(Wei.fromEth(1)).nonce(1).address(userAddress).build();

    ToyAccount modexpContractAccount =
        ToyAccount.builder()
            .balance(Wei.fromEth(0))
            .nonce(1)
            .address(Address.fromHexString("0xCA77"))
            .code(
                Bytes.fromHexString(
                    "608060405234801561001057600080fd5b506004361061002b5760003560e01c8063bdabdbbd14610030575b600080fd5b61004a60048036038101906100459190610232565b610060565b60405161005791906102fa565b60405180910390f35b60606000825190506000602067ffffffffffffffff81111561008557610084610107565b5b6040519080825280601f01601f1916602001820160405280156100b75781602001600182028036833780820191505090505b5090506020840160208201600081858460055afa5050508092505050919050565b6000604051905090565b600080fd5b600080fd5b600080fd5b600080fd5b6000601f19601f8301169050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b61013f826100f6565b810181811067ffffffffffffffff8211171561015e5761015d610107565b5b80604052505050565b60006101716100d8565b905061017d8282610136565b919050565b600067ffffffffffffffff82111561019d5761019c610107565b5b6101a6826100f6565b9050602081019050919050565b82818337600083830152505050565b60006101d56101d084610182565b610167565b9050828152602081018484840111156101f1576101f06100f1565b5b6101fc8482856101b3565b509392505050565b600082601f830112610219576102186100ec565b5b81356102298482602086016101c2565b91505092915050565b600060208284031215610248576102476100e2565b5b600082013567ffffffffffffffff811115610266576102656100e7565b5b61027284828501610204565b91505092915050565b600081519050919050565b600082825260208201905092915050565b60005b838110156102b557808201518184015260208101905061029a565b60008484015250505050565b60006102cc8261027b565b6102d68185610286565b93506102e6818560208601610297565b6102ef816100f6565b840191505092915050565b6000602082019050818103600083015261031481846102c1565b90509291505056fea2646970667358221220180bbc5fd440cd7487f3c2d407b6c825ea7fbac21f31e09b956884b1fa2f2c0164736f6c63430008140033"))
            .build();

    Bytes32 size = Bytes32.leftPad(Bytes.ofUnsignedInt(input.size()));

    Transaction txToModexpContractAccount =
        ToyTransaction.builder()
            .sender(userAccount)
            .to(modexpContractAccount)
            .payload(
                Bytes.concatenate(
                    Bytes.fromHexString(
                        "0xbdabdbbd"
                            + "0000000000000000000000000000000000000000000000000000000000000020"),
                    size,
                    input)) // function selector, offset (always 0x20), size, input
            .transactionType(TransactionType.FRONTIER)
            .value(Wei.ZERO)
            .keyPair(keyPair)
            .nonce(1L)
            .gasLimit(61_000_000L)
            .build();

    ToyExecutionEnvironmentV2 toyExecutionEnvironmentV2 =
        ToyExecutionEnvironmentV2.builder()
            .accounts(List.of(userAccount, modexpContractAccount))
            .transaction(txToModexpContractAccount)
            .transactionProcessingResultValidator(
                TransactionProcessingResultValidator.EMPTY_VALIDATOR)
            .build();
    // TODO: add try catch to catch the exception assert that what is failing is what is expected to
    //  fail only
    toyExecutionEnvironmentV2.run();
  }

  private Stream<Arguments> modexpTestForSequencerSource() {
    List<Arguments> arguments = new ArrayList<>();
    for (ModexpCallDataWordVariants w1 : ModexpCallDataWordVariants.values()) {
      arguments.add(Arguments.of(w1.getW(), w1.isInvalid()));
      for (ModexpCallDataWordVariants w2 : ModexpCallDataWordVariants.values()) {
        arguments.add(
            Arguments.of(
                Bytes.concatenate(w1.getW(), w2.getW()), w1.isInvalid() || w2.isInvalid()));
        for (ModexpCallDataWordVariants w3 : ModexpCallDataWordVariants.values()) {
          arguments.add(
              Arguments.of(
                  Bytes.concatenate(w1.getW(), w2.getW(), w3.getW()),
                  w1.isInvalid() || w2.isInvalid() || w3.isInvalid()));
          arguments.add(
              Arguments.of(
                  Bytes.concatenate(w1.getW(), w2.getW(), w3.getW(), Bytes.random(1536, RANDOM)),
                  w1.isInvalid() || w2.isInvalid() || w3.isInvalid()));
        }
      }
    }
    return arguments.stream();
  }

  @Getter
  public enum ModexpCallDataWordVariants {
    EMPTY(Bytes.fromHexString("")),
    ZERO(Bytes.fromHexString("0000000000000000000000000000000000000000000000000000000000000000")),
    RAND(Bytes.fromHexString("000000000000000000000000000000000000000000000000000000000000013f")),
    MAX_LEGAL_FULL(
        Bytes.fromHexString("0000000000000000000000000000000000000000000000000000000000000200")),
    MAX_LEGAL_SHORT(
        Bytes.fromHexString("00000000000000000000000000000000000000000000000000000000000002")),
    MIN_ILLEGAL_FULL(
        Bytes.fromHexString(
            "0000000000000000000000000000000000000000000000000000000000000201")), // invalid
    MIN_ILLEGAL_SHORT(
        Bytes.fromHexString(
            "00000000000000000000000000000000000000000000000000000000000003")), // invalid
    ILLEGAL_SHORTEST(Bytes.fromHexString("ff")), // invalid
    MAX_WORD(
        Bytes.fromHexString(
            "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff")); // invalid

    private final Bytes w;

    ModexpCallDataWordVariants(Bytes w) {
      this.w = w;
    }

    public boolean isInvalid() {
      return this.getW() == MIN_ILLEGAL_FULL.getW()
          || this.getW() == MIN_ILLEGAL_SHORT.getW()
          || this.getW() == ILLEGAL_SHORTEST.getW()
          || this.getW() == MAX_WORD.getW();
    }
  }
}
