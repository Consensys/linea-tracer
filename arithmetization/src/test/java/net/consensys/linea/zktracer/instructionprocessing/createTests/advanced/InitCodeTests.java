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
      "0x6080604052348015600f57600080fd5b506103798061001f6000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c80632199ecd71461005c578063545b079a146100665780635e666e4a146100825780636c0c412e146100b2578063983d1ce2146100ce575b600080fd5b6100646100d8565b005b610080600480360381019061007b9190610249565b6100f6565b005b61009c600480360381019061009791906102ac565b610159565b6040516100a991906102e8565b60405180910390f35b6100cc60048036038101906100c79190610303565b61018c565b005b6100d66101e1565b005b60003090508073ffffffffffffffffffffffffffffffffffffffff16ff5b8073ffffffffffffffffffffffffffffffffffffffff16630161e6d76040518163ffffffff1660e01b8152600401600060405180830381600087803b15801561013e57600080fd5b505af1158015610152573d6000803e3d6000fd5b5050505050565b60006020528060005260406000206000915054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b8060008084815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505050565b600080fd5b600080fd5b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000610216826101eb565b9050919050565b6102268161020b565b811461023157600080fd5b50565b6000813590506102438161021d565b92915050565b60006020828403121561025f5761025e6101e6565b5b600061026d84828501610234565b91505092915050565b6000819050919050565b61028981610276565b811461029457600080fd5b50565b6000813590506102a681610280565b92915050565b6000602082840312156102c2576102c16101e6565b5b60006102d084828501610297565b91505092915050565b6102e28161020b565b82525050565b60006020820190506102fd60008301846102d9565b92915050565b6000806040838503121561031a576103196101e6565b5b600061032885828601610297565b925050602061033985828601610234565b915050925092905056fea2646970667358221220e83ad410ca8f7041f461ba4698a5e5deb62dd45f2388d6f06ecc3bc1d95e63e864736f6c634300081a0033";

  static final String buildCustomCreate2Address = "789101";
  // Update
  static final String initCodeCWithConstructorBase =
      "0x608060405234801561001057600080fd5b506040516105bd3803806105bd833981810160405281019061003291906101f5565b60018203610090578060008084815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550610132565b600282036100fd578073ffffffffffffffffffffffffffffffffffffffff1663f047c0c46040518163ffffffff1660e01b8152600401600060405180830381600087803b1580156100e057600080fd5b505af11580156100f4573d6000803e3d6000fd5b50505050610131565b600382036101185761011361013960201b60201c565b610130565b6004820361012f5761012e61015760201b60201c565b5b5b5b5b5050610235565b60003090508073ffffffffffffffffffffffffffffffffffffffff16ff5b600080fd5b600080fd5b6000819050919050565b61017481610161565b811461017f57600080fd5b50565b6000815190506101918161016b565b92915050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b60006101c282610197565b9050919050565b6101d2816101b7565b81146101dd57600080fd5b50565b6000815190506101ef816101c9565b92915050565b6000806040838503121561020c5761020b61015c565b5b600061021a85828601610182565b925050602061022b858286016101e0565b9150509250929050565b610379806102446000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c80632199ecd71461005c578063545b079a146100665780635e666e4a146100825780636c0c412e146100b2578063983d1ce2146100ce575b600080fd5b6100646100d8565b005b610080600480360381019061007b9190610249565b6100f6565b005b61009c600480360381019061009791906102ac565b610159565b6040516100a991906102e8565b60405180910390f35b6100cc60048036038101906100c79190610303565b61018c565b005b6100d66101e1565b005b60003090508073ffffffffffffffffffffffffffffffffffffffff16ff5b8073ffffffffffffffffffffffffffffffffffffffff16630161e6d76040518163ffffffff1660e01b8152600401600060405180830381600087803b15801561013e57600080fd5b505af1158015610152573d6000803e3d6000fd5b5050505050565b60006020528060005260406000206000915054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b8060008084815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505050565b600080fd5b600080fd5b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000610216826101eb565b9050919050565b6102268161020b565b811461023157600080fd5b50565b6000813590506102438161021d565b92915050565b60006020828403121561025f5761025e6101e6565b5b600061026d84828501610234565b91505092915050565b6000819050919050565b61028981610276565b811461029457600080fd5b50565b6000813590506102a681610280565b92915050565b6000602082840312156102c2576102c16101e6565b5b60006102d084828501610297565b91505092915050565b6102e28161020b565b82525050565b60006020820190506102fd60008301846102d9565b92915050565b6000806040838503121561031a576103196101e6565b5b600061032885828601610297565b925050602061033985828601610234565b915050925092905056fea264697066735822122071dc44d6f79c309ad88fe404d2520e19e70dc177f7ba636a2f8ccb17d0b2bfa864736f6c634300081a0033";
  static final String selector2 =
      "0000000000000000000000000000000000000000000000000000000000000002";
  static final String selector1 =
      "0000000000000000000000000000000000000000000000000000000000000001";
  static final String addressToCallOrStore =
      "0000000000000000000000000000000000000000000000000000000000" + buildCustomCreate2Address;
  static final String initCodeCWithImmediateCallBack =
      initCodeCWithConstructorBase + selector2 + addressToCallOrStore;
  static final String initCodeCWithStorage =
      initCodeCWithConstructorBase + selector1 + addressToCallOrStore;

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
    Bytes storeInitCodeCWithImmediateCallBack =
        CustomCreate2Payload.storeInitCodeCWithImmediateCallBack(initCodeCWithImmediateCallBack);
    Bytes storeInitCodeCWithStorage =
        CustomCreate2Payload.storeInitCodeCWithImmediateCallBack(initCodeCWithStorage);
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
    // 4 - Fails, should pass with empty code => debug
    Bytes create2WithImmediateCallBack = CustomCreate2Payload.create2WithImmediateCallBack();
    // 5 - Passes because of doStaticCall, should fail
    Bytes create2WithStaticCall =
        CustomCreate2Payload.callMyself(CustomCreate2Payload.create2WithInitCodeC(), true);

    List<ToyTransactionBuilder> txBuilders =
        getTransactionBuilders(
            customCreate2Account,
            List.of(
                storeInitCodeC,
                storeInitCodeCWithImmediateCallBack,
                storeSalt,
                create2WithInitCodeC,
                callContractCStoreInMapPayload,
                callContractCSelfDestructPayload,
                create2WithInitCodeC));

    /*    List<ToyTransactionBuilder> builders = new ArrayList<>();
    builders.addAll(txBuilders);*/

    List<Transaction> transactions = getTransactions(userAccount, txBuilders);

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
  List<ToyTransactionBuilder> getTransactionBuilders(ToyAccount to, List<Bytes> payloads) {
    final List<ToyTransactionBuilder> builders = new ArrayList<>();

    for (Bytes payload : payloads) {
      ToyTransactionBuilder builder =
          ToyTransaction.builder().to(to).payload(payload).keyPair(keyPair).gasLimit(gasLimit);
      builders.add(builder);
    }
    return builders;
  }

  List<Transaction> getTransactions(ToyAccount userAccount, List<ToyTransactionBuilder> builders) {
    return ToyMultiTransaction.builder().build(builders, userAccount);
  }
}
