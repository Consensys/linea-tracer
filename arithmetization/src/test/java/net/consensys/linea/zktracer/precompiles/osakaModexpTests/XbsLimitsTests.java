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
package net.consensys.linea.zktracer.precompiles.osakaModexpTests;

import static net.consensys.linea.zktracer.Fork.forkPredatesOsaka;
import static net.consensys.linea.zktracer.TraceOsaka.EIP_7823_MODEXP_UPPER_BYTE_SIZE_BOUND;
import static net.consensys.linea.zktracer.opcode.OpCode.*;
import static net.consensys.linea.zktracer.precompiles.osakaModexpTests.XbsValueType.GIBBERISH;
import static net.consensys.linea.zktracer.precompiles.osakaModexpTests.XbsValueType.getListOfInputs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.consensys.linea.reporting.TracerTestBase;
import net.consensys.linea.testing.*;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.crypto.KeyPair;
import org.hyperledger.besu.crypto.SECP256K1;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Hash;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class XbsLimitsTests extends TracerTestBase {

  final KeyPair keyPair = new SECP256K1().generateKeyPair();
  final Address senderAddress =
      Address.extract(Hash.hash(keyPair.getPublicKey().getEncodedBytes()));

  final ToyAccount senderAccount =
      ToyAccount.builder().balance(Wei.fromEth(1900)).nonce(420).address(senderAddress).build();

  /**
   * Returns code calling the <b>MODEXP</b> precompile with the transaction's call data as inputs.
   *
   * @param cds
   * @return
   */
  final BytecodeCompiler modexpCallerCode(int cds) {
    return BytecodeCompiler.newProgram(chainConfig)
        // full copy of call data
        .op(CALLDATASIZE)
        .op(PUSH0)
        .op(PUSH0)
        .op(CALLDATACOPY)
        // call MODEXP precompile
        .push(EIP_7823_MODEXP_UPPER_BYTE_SIZE_BOUND) // r@c
        .push(3 * 32 + 3 * EIP_7823_MODEXP_UPPER_BYTE_SIZE_BOUND) // r@o
        .push(cds) // cds
        .push(0) // cdo
        .push("0000000000000000000000000000000000000005")
        .op(GAS)
        .op(STATICCALL)
        // append 32 JUMPDESTs for sanity
        .op(JUMPDEST, 32);
  }

  final ToyAccount.ToyAccountBuilder receiverAccountBuilder =
      ToyAccount.builder()
          .balance(Wei.ONE)
          .nonce(6)
          .address(Address.fromHexString("11223344aaaaffff000000000000000000000001"));


  @ParameterizedTest
  @MethodSource("fullParametricTestSource")
  public void fullParametricTest(XbsValueType.BbsEbsMbsScenario scenario, String bbsEbsMbsString, TestInfo testInfo) {

      final int cds = scenario.callDataSize();
      String transactionCallData = bbsEbsMbsString + GIBBERISH;

      ToyAccount targetAccount =
              receiverAccountBuilder.code(modexpCallerCode(cds).compile()).build();

      Transaction tx =
              ToyTransaction.builder()
                      .sender(senderAccount)
                      .to(targetAccount)
                      .keyPair(keyPair)
                      .payload(Bytes.fromHexString(transactionCallData))
                      .gasLimit((long) (1 << 24))
                      .build();

      ToyExecutionEnvironmentV2.builder(chainConfig, testInfo)
              .accounts(List.of(senderAccount, targetAccount))
              .transaction(tx)
              .build()
              .run();
  }

  @Test
  public void fullTest(TestInfo testInfo) {

    // goal:
    // create a transaction with call data derived from entries in allParameters
    // do a full copy of the transaction's call data using CALLDATACOPY
    // do a CALL to MODEXP with that call data
    // for sanity reasons: append 32 JUMPDESTs

    // skip test if fork is before Osaka
    if (forkPredatesOsaka(fork)) return;

    for (Map.Entry<XbsValueType.BbsEbsMbsScenario, List<String>> entry : allParameters.entrySet()) {
      XbsValueType.BbsEbsMbsScenario scenario = entry.getKey();
      List<String> parametersList = entry.getValue();

      final int cds = scenario.callDataSize();

      for (String parameter : parametersList) {

        System.out.println("Testing scenario: " + scenario + " with parameters: " + parameter);
        String transactionCallData = parameter + GIBBERISH;

        ToyAccount receiverAccount =
            receiverAccountBuilder.code(modexpCallerCode(cds).compile()).build();

        Transaction tx =
            ToyTransaction.builder()
                .sender(senderAccount)
                .to(receiverAccount)
                .keyPair(keyPair)
                .payload(Bytes.fromHexString(transactionCallData))
                .gasLimit((long) (1 << 24))
                .build();

        ToyExecutionEnvironmentV2.builder(chainConfig, testInfo)
            .accounts(List.of(senderAccount, receiverAccount))
            .transaction(tx)
            .build()
            .run();
      }
    }
  }

  static Map<XbsValueType.BbsEbsMbsScenario, List<String>> allParameters =
      Arrays.stream(XbsValueType.values())
          .flatMap(
              bbsType ->
                  Arrays.stream(XbsValueType.values())
                      .flatMap(
                          ebsType ->
                              Arrays.stream(XbsValueType.values())
                                  .map(
                                      mbsType ->
                                          Map.entry(
                                              new XbsValueType.BbsEbsMbsScenario(
                                                  bbsType, ebsType, mbsType),
                                              getParameters(
                                                  new XbsValueType.BbsEbsMbsScenario(
                                                      bbsType, ebsType, mbsType))))))
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

  static Stream<Arguments> fullParametricTestSource() {

      List<Arguments> arguments = new ArrayList<>();
        for (Map.Entry<XbsValueType.BbsEbsMbsScenario, List<String>> entry : allParameters.entrySet()) {
            XbsValueType.BbsEbsMbsScenario scenario = entry.getKey();
            List<String> parametersList = entry.getValue();

            for (String parameter : parametersList) {
                arguments.add(Arguments.of(scenario, parameter));
            }
        }

        return arguments.stream();
  }

  static List<String> getParameters(XbsValueType.BbsEbsMbsScenario bbsEbsMbsScenario) {

    List<String> parameters = new ArrayList<>();

    for (String bbs : getListOfInputs(bbsEbsMbsScenario.bbsValueType())) {
      for (String ebs : getListOfInputs(bbsEbsMbsScenario.ebsValueType())) {
        for (String mbs : getListOfInputs(bbsEbsMbsScenario.mbsValueType())) {
          parameters.add(bbs + ebs + mbs);
        }
      }
    }

    return parameters;
  }
}
