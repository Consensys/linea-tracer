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
package net.consensys.linea.zktracer.instructionprocessing.createTests.advanced;

import static net.consensys.linea.zktracer.instructionprocessing.utilities.MonoOpCodeSmcs.keyPair;
import static net.consensys.linea.zktracer.instructionprocessing.utilities.MonoOpCodeSmcs.userAccount;

import java.util.ArrayList;
import java.util.List;

import net.consensys.linea.UnitTestWatcher;
import net.consensys.linea.testing.*;
import net.consensys.linea.testing.ToyTransaction.ToyTransactionBuilder;
import net.consensys.linea.testing.generated.CustomCreate2;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(UnitTestWatcher.class)
public class InitCodeTests {

  static final Wei defaultBalance = Wei.of(4500L);

  static final String initCodeC =
      "0x6080604052600034905060003390506001820361006c578060008084815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555061010e565b600282036100d9578073ffffffffffffffffffffffffffffffffffffffff16630161e6d76040518163ffffffff1660e01b8152600401600060405180830381600087803b1580156100bc57600080fd5b505af11580156100d0573d6000803e3d6000fd5b5050505061010d565b600382036100f4576100ef61011560201b60201c565b61010c565b6004820361010b5761010a61013360201b60201c565b5b5b5b5b5050610138565b60003090508073ffffffffffffffffffffffffffffffffffffffff16ff5b600080fd5b6103f6806101476000396000f3fe608060405234801561001057600080fd5b50600436106100625760003560e01c80632199ecd71461006757806338f387ef14610071578063545b079a146100a15780635e666e4a146100bd5780636c0c412e146100ed578063983d1ce214610109575b600080fd5b61006f610113565b005b61008b60048036038101906100869190610274565b610131565b60405161009891906102b0565b60405180910390f35b6100bb60048036038101906100b69190610329565b610149565b005b6100d760048036038101906100d29190610274565b6101ac565b6040516100e49190610365565b60405180910390f35b61010760048036038101906101029190610380565b6101df565b005b610111610234565b005b60003090508073ffffffffffffffffffffffffffffffffffffffff16ff5b60016020528060005260406000206000915090505481565b8073ffffffffffffffffffffffffffffffffffffffff16630161e6d76040518163ffffffff1660e01b8152600401600060405180830381600087803b15801561019157600080fd5b505af11580156101a5573d6000803e3d6000fd5b5050505050565b60006020528060005260406000206000915054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b8060008084815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505050565b600080fd5b600080fd5b6000819050919050565b6102518161023e565b811461025c57600080fd5b50565b60008135905061026e81610248565b92915050565b60006020828403121561028a57610289610239565b5b60006102988482850161025f565b91505092915050565b6102aa8161023e565b82525050565b60006020820190506102c560008301846102a1565b92915050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b60006102f6826102cb565b9050919050565b610306816102eb565b811461031157600080fd5b50565b600081359050610323816102fd565b92915050565b60006020828403121561033f5761033e610239565b5b600061034d84828501610314565b91505092915050565b61035f816102eb565b82525050565b600060208201905061037a6000830184610356565b92915050565b6000806040838503121561039757610396610239565b5b60006103a58582860161025f565b92505060206103b685828601610314565b915050925092905056fea2646970667358221220529cb816c89aa211951e64517fe3cf3598883549ef1b6408ad03dcb339e8702964736f6c634300081a0033";

  static final String buildCustomCreate2Address = "789101";
  static final String addressToCallOrStore =
      "0000000000000000000000000000000000000000000000000000000000" + buildCustomCreate2Address;

  ToyAccount customCreate2Account =
      ToyAccount.builder()
          .address(Address.fromHexString("0x" + buildCustomCreate2Address))
          .balance(defaultBalance)
          .nonce(1)
          .code(SmartContractUtils.getSolidityContractRuntimeByteCode(CustomCreate2.class))
          .build();

  static final Long gasLimit = 5000000L;

  @Test
  void deployContractCWithCreate2() {

    Bytes storeInitCodeC = CustomCreate2Payload.storeInitCodeC(initCodeC);
    Bytes storeSalt =
        CustomCreate2Payload.storeSalt(
            "0x1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef");
    Bytes create2WithInitCodeC = CustomCreate2Payload.create2WithInitCodeC();
    Bytes callContractCStoreInMapPayload =
        CustomCreate2Payload.callContractC(
            ContractCPayload.storeInMap(1, "0x0000000000000000000000000000000000001234"), false);
    Bytes callContractCSelfDestructPayload =
        CustomCreate2Payload.callContractC(ContractCPayload.selfDestructOnDemand(), false);
    // 2 - Fails OK
    Bytes create2WithCallBackAfterCreate2 = CustomCreate2Payload.create2WithCallBackAfterCreate2();
    // 3 - Passes because of doCall, should fail
    Bytes callContractCRevertOnPurpose =
        CustomCreate2Payload.callContractC(ContractCPayload.revertOnDemand(), false);
    // 5 - Passes because of doStaticCall, should fail
    // Ajouter un log
    Bytes create2WithStaticCall =
        CustomCreate2Payload.callMyself(CustomCreate2Payload.create2WithInitCodeC(), true);

    List<Transaction> transactions =
        getTransactions(
            customCreate2Account,
            userAccount,
            List.of(
                // preparation
                storeInitCodeC,
                storeSalt,
                // start tx
                create2WithInitCodeC,
                callContractCStoreInMapPayload,
                callContractCSelfDestructPayload,
                create2WithCallBackAfterCreate2),
            List.of(0L, 0L, 0L, 0L, 0L, 0L, 0L));

    /*    List<ToyTransactionBuilder> builders = new ArrayList<>();
    builders.addAll(txBuilders);*/

    ToyExecutionEnvironmentV2.builder()
        .accounts(List.of(userAccount, customCreate2Account))
        .transactions(transactions)
        .transactionProcessingResultValidator(
            TransactionProcessingResultValidator.DEFAULT_VALIDATOR)
        .build()
        .run();
  }

  /// /////////////////////////////////////////////////////////////////////////////////////////////
  /// Common helpers
  List<Transaction> getTransactions(
      ToyAccount to, ToyAccount userAccount, List<Bytes> payloads, List<Long> values) {
    final List<ToyTransactionBuilder> builders = new ArrayList<>();

    for (Bytes payload : payloads) {
      ToyTransactionBuilder builder =
          ToyTransaction.builder()
              .to(to)
              .payload(payload)
              .keyPair(keyPair)
              .gasLimit(gasLimit)
              .value(Wei.of(values.get(payloads.indexOf(payload))));
      builders.add(builder);
    }
    return ToyMultiTransaction.builder().build(builders, userAccount);
  }
}
