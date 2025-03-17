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

import static com.google.common.base.Preconditions.checkArgument;
import static net.consensys.linea.zktracer.instructionprocessing.utilities.MonoOpCodeSmcs.keyPair;
import static net.consensys.linea.zktracer.instructionprocessing.utilities.MonoOpCodeSmcs.userAccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.consensys.linea.UnitTestWatcher;
import net.consensys.linea.testing.*;
import net.consensys.linea.testing.ToyTransaction.ToyTransactionBuilder;
import net.consensys.linea.testing.generated.ContractC;
import net.consensys.linea.testing.generated.CustomCreate2;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.web3j.abi.EventEncoder;

@ExtendWith(UnitTestWatcher.class)
public class InitCodeTests {

  static final Wei defaultBalance = Wei.of(4500L);

  static final String initCodeC =
      "0x6080604052600034905060003390506001820361006c578060008084815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550610169565b60028203610134578073ffffffffffffffffffffffffffffffffffffffff16630161e6d76040518163ffffffff1660e01b8152600401600060405180830381600087803b1580156100bc57600080fd5b505af19250505080156100cd575060015b61012f576100d96101a0565b806308c379a0036100fa57506100ed61023d565b806100f857506100fc565b005b505b3d8060008114610128576040519150601f19603f3d011682016040523d82523d6000602084013e61012d565b606091505b005b610168565b6003820361014f5761014a61017060201b60201c565b610167565b600482036101665761016561018e60201b60201c565b5b5b5b5b50506102cd565b60003090508073ffffffffffffffffffffffffffffffffffffffff16ff5b600080fd5b60008160e01c9050919050565b600060033d11156101bf5760046000803e6101bc600051610193565b90505b90565b6000604051905090565b6000601f19601f8301169050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b610215826101cc565b810181811067ffffffffffffffff82111715610234576102336101dd565b5b80604052505050565b600060443d106102ca5761024f6101c2565b60043d036004823e80513d602482011167ffffffffffffffff821117156102775750506102ca565b808201805167ffffffffffffffff81111561029557505050506102ca565b80602083010160043d0385018111156102b25750505050506102ca565b6102c18260200185018661020c565b82955050505050505b90565b610379806102dc6000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c80632199ecd71461005c578063545b079a146100665780635e666e4a146100825780636c0c412e146100b2578063983d1ce2146100ce575b600080fd5b6100646100d8565b005b610080600480360381019061007b9190610249565b6100f6565b005b61009c600480360381019061009791906102ac565b610159565b6040516100a991906102e8565b60405180910390f35b6100cc60048036038101906100c79190610303565b61018c565b005b6100d66101e1565b005b60003090508073ffffffffffffffffffffffffffffffffffffffff16ff5b8073ffffffffffffffffffffffffffffffffffffffff16630161e6d76040518163ffffffff1660e01b8152600401600060405180830381600087803b15801561013e57600080fd5b505af1158015610152573d6000803e3d6000fd5b5050505050565b60006020528060005260406000206000915054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b8060008084815260200190815260200160002060006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505050565b600080fd5b600080fd5b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000610216826101eb565b9050919050565b6102268161020b565b811461023157600080fd5b50565b6000813590506102438161021d565b92915050565b60006020828403121561025f5761025e6101e6565b5b600061026d84828501610234565b91505092915050565b6000819050919050565b61028981610276565b811461029457600080fd5b50565b6000813590506102a681610280565b92915050565b6000602082840312156102c2576102c16101e6565b5b60006102d084828501610297565b91505092915050565b6102e28161020b565b82525050565b60006020820190506102fd60008301846102d9565b92915050565b6000806040838503121561031a576103196101e6565b5b600061032885828601610297565b925050602061033985828601610234565b915050925092905056fea26469706673582212206ec99c584c0741a86513352d079749b97bdb3ca175e5fbaac3ead911faac581964736f6c634300081a0033";

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
    Bytes storeSalt2 =
        CustomCreate2Payload.storeSalt(
            "0x1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdee");
    Bytes create2WithInitCodeC = CustomCreate2Payload.create2WithInitCodeC();
    Bytes callContractCStoreInMapPayload =
        CustomCreate2Payload.callContractC(
            ContractCPayload.storeInMap(1, "0x0000000000000000000000000000000000001234"), false);
    Bytes callContractCSelfDestructPayload =
        CustomCreate2Payload.callContractC(ContractCPayload.selfDestructOnDemand(), false);
    // 2 - Fails OK
    Bytes create2WithCallBackAfterCreate2 = CustomCreate2Payload.create2WithCallBackAfterCreate2();
    // 3 - Passes because of doCall, emits log
    Bytes callContractCRevertOnPurpose =
        CustomCreate2Payload.callContractC(ContractCPayload.revertOnDemand(), false);
    // 5 - Passes because of doStaticCall, emits log
    // Bug to check, always StaticCallMyself
    Bytes create2WithStaticCall =
        CustomCreate2Payload.callMyself(CustomCreate2Payload.create2WithInitCodeC(), true);
    Bytes create2FourTimes = CustomCreate2Payload.create2FourTimes();

    // prepare the transaction validator
    String contractCreatedEvent = EventEncoder.encode(CustomCreate2.CONTRACTCREATED_EVENT);
    String staticCallMyselfFailEvent =
        EventEncoder.encode(CustomCreate2.STATICCALLMYSELFFAIL_EVENT);
    String immediateRedeploymentFailEvent =
        EventEncoder.encode(ContractC.IMMEDIATEREDEPLOYMENTFAIL_EVENT);
    Map<String, List<Integer>> logsMap = new HashMap<>();
    logsMap.put(contractCreatedEvent, List.of(0, 0, 1, 0, 0, 0, 1, 0, 0, 1));
    logsMap.put(staticCallMyselfFailEvent, List.of(0, 0, 0, 0, 0, 0, 0, 0, 1, 0));
    logsMap.put(immediateRedeploymentFailEvent, List.of(0, 0, 0, 0, 0, 0, 1, 0, 0, 0));
    TransactionProcessingResultValidator create2Validator = new Create2TestValidator(logsMap);

    List<Transaction> transactions =
        getTransactions(
            customCreate2Account,
            userAccount,
            List.of(
                // CustomCreate2 initialization
                storeInitCodeC,
                storeSalt,
                // Flow 1 - ContractC deploy, modify storage, self-destruct
                // ContractC is deployed
                // Storage is modified
                // ContractC is self-destructed
                // Logs: 1 ContractCreated
                create2WithInitCodeC,
                callContractCStoreInMapPayload,
                callContractCSelfDestructPayload,
                // Flow 2 - Deploy ContractC and try deployment again after in same transaction
                // Transaction reverts, nothing is deployed
                // Logs: none
                create2WithCallBackAfterCreate2,
                // Flow 3 - Deploy ContractC and deployment attempts redeployment
                // ContractC deployment attempt with value 2, so immediate redeployment attempted
                // ContractC is deployed with empty bytecode
                // Logs: 1 ContractCreated + 1 ImmediateRedeploymentFail
                create2WithInitCodeC,
                // Flow 4 - ContractC deployed with staticCall
                // Logs: 1 StaticCallMyselfFail
                storeSalt2, // change salt as C is deployed at address
                create2WithStaticCall,
                // Flow 5 - ContractC deployed with max value, acceptable value, max value,
                // acceptable value
                // First attempt is aborted
                // Second attempt is successful
                // Third attempt  is aborted
                // Fourth attempt fails
                // Logs: 1 ContractCreated
                create2FourTimes),
            List.of(0L, 0L, 0L, 0L, 0L, 0L, 2L, 0L, 0L, 0L));

    ToyExecutionEnvironmentV2.builder()
        .accounts(List.of(userAccount, customCreate2Account))
        .transactions(transactions)
        .transactionProcessingResultValidator(create2Validator)
        .build()
        .run();
  }

  /// /////////////////////////////////////////////////////////////////////////////////////////////
  /// Common helpers
  List<Transaction> getTransactions(
      ToyAccount to, ToyAccount userAccount, List<Bytes> payloads, List<Long> values) {

    checkArgument(payloads.size() == values.size());
    final List<ToyTransactionBuilder> builders = new ArrayList<>();

    for (int i = 0; i < payloads.size(); i++) {
      ToyTransactionBuilder builder =
          ToyTransaction.builder()
              .to(to)
              .payload(payloads.get(i))
              .keyPair(keyPair)
              .gasLimit(gasLimit)
              .value(Wei.of(values.get(i)));
      builders.add(builder);
    }
    return ToyMultiTransaction.builder().build(builders, userAccount);
  }
}
