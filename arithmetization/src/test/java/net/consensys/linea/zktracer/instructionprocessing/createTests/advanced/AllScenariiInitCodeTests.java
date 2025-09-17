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

import static net.consensys.linea.zktracer.Fork.isPostCancun;
import static net.consensys.linea.zktracer.instructionprocessing.createTests.advanced.ScenarioUtils.*;
import static net.consensys.linea.zktracer.instructionprocessing.utilities.MonoOpCodeSmcs.userAccount;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.UnitTestWatcher;
import net.consensys.linea.reporting.TracerTestBase;
import net.consensys.linea.testing.*;
import net.consensys.linea.zktracer.instructionprocessing.utilities.SmartContractTestValidator;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

@Slf4j
@ExtendWith(UnitTestWatcher.class)
public class AllScenariiInitCodeTests extends TracerTestBase {

  // This suite aims at testing advanced CREATE2 scenarii using smart contracts
  // ** CustomCreate2 **
  // CustomCreate2 is the smart contract used to pilot the deployment of a complex initCodeC
  // CustomCreate2 stores an initCodeC and a salt used for subsequent deployments
  // CustomCreate2 has different create2 methods to have deployment scenarii within the same
  // transaction
  // CustomCreate2 can CALL/STATICCALL itself and contractC
  // ** initCodeC **
  // initCodeC can be piloted by CustomCreate2 via the value passed in the transaction
  // value 1 Wei enables a storage modification in ContractC
  // value 2 Wei calls CustomCreate2 back to trigger an immediate redeployment of ContractC
  // value 3 Wei triggers a self-destruct of ContractC
  // value 4 Wei triggers a revert on demand
  // ** ContractC **
  // ContractC can modify storage, revert on demand, self-destruct on demand, and call back
  // CustomCreate2 to trigger a redeployment of ContractC

  // In this test, we test Scenario 1 to 5 in two ways
  // - triggered Calls from a root context
  // - triggered nested Calls going from Scenario 5 to 1, with 1 being executed first
  // The 5 scenarii are unit tested in separate tests for clarity

  @Test
  void deployContractCWithCreate2(TestInfo testInfo) {

    Map<String, List<Integer>> logsTopicMap = new HashMap<>();
    Map<String, List<Bytes>> logsDataMap = new HashMap<>();

    // List all logs expected from each topic
    // In the last transaction, we attempt to redeploy contractC at the same address (see detailed
    // description below). However, as the previous self-destruct was not done in the same tx as the
    // 1st deployment, the re-deployment now fails.
    int lastTxIsContractCreatedEvent = isPostCancun(fork) ? 0 : 1;
    logsTopicMap.put(
        contractCreatedEvent, List.of(0, 0, 1, 0, 0, 0, 0, 0, lastTxIsContractCreatedEvent));
    logsTopicMap.put(storeInMapEvent, List.of(0, 0, 0, 1, 0, 0, 0, 0, 1));
    logsTopicMap.put(staticCallMyselfFailEvent, List.of(0, 0, 0, 0, 0, 0, 0, 1, 0));
    logsTopicMap.put(callCreate2WithInitCodeC_withValue_Event, List.of(0, 0, 1, 0, 0, 0, 0, 0, 0));
    // List data expected for each topic
    Bytes lastTxContractCreatedEvent =
        isPostCancun(fork) ? Bytes.EMPTY : expectedContractCAddressLogData;
    logsDataMap.put(
        contractCreatedEvent,
        List.of(
            Bytes.EMPTY,
            Bytes.EMPTY,
            expectedContractCAddressLogData,
            Bytes.EMPTY,
            Bytes.EMPTY,
            Bytes.EMPTY,
            Bytes.EMPTY,
            Bytes.EMPTY,
            lastTxContractCreatedEvent));
    logsDataMap.put(
        storeInMapEvent,
        List.of(
            Bytes.EMPTY,
            Bytes.EMPTY,
            Bytes.EMPTY,
            Bytes.EMPTY,
            Bytes.fromHexString(
                "0x0000000000000000000000000000000000000000000000000000000000000001"),
            Bytes.EMPTY,
            Bytes.EMPTY,
            Bytes.EMPTY,
            Bytes.EMPTY,
            Bytes.EMPTY,
            Bytes.fromHexString(
                "0x0000000000000000000000000000000000000000000000000000000000000000")));
    // List status expected per transaction
    // 0 is FAILED
    // 1 is SUCCESSFUL
    int lastTxStatus = isPostCancun(fork) ? 0 : 1;
    List<Integer> txStatuses = List.of(1, 1, 1, 1, 1, 0, 0, 1, lastTxStatus);

    // Instantiate validator
    TransactionProcessingResultValidator create2Validator =
        new SmartContractTestValidator(txStatuses, logsTopicMap, logsDataMap);

    List<Transaction> transactions =
        getTransactions(
            customCreate2Account,
            userAccount,
            // The list below contains `Bytes` payloads
            // Every item in that list will define a unique transaction having it as its payload.
            List.of(
                // CustomCreate2 initialization
                storeInitCodeC,
                storeSalt,
                // SCENARIO 1 - Deploy ContractC through CustomCreate2, modify storage,
                // self-destruct ContractC
                // ContractC is deployed
                // ContractC Storage is modified
                // ContractC is self-destructed
                // TXSTATUS : Successfull
                // LOGS: 1 CalledCreate2WithInitCodeC + 1 ContractCreated
                // Note : 1 CREATE2 opcode called
                create2WithInitCodeC,
                callContractCStoreInMapPayload,
                callContractCSelfDestructPayload,
                // SCENARIO 2 - Deploy ContractC and attempt redeployment after in same transaction
                // Transaction reverts, nothing is deployed
                // TXSTATUS : Failed
                // LOGS: no logs
                // Note : 2 CREATE2 opcode called
                create2WithCallBackAfterCreate2_noValue,
                // SCENARIO 3 - Deploy ContractC and the deployment attempts redeployment
                // The ContractC deployment is done with value 2 - this value pilots the initcode so
                // immediate redeployment is attempted
                // While deploying ContractC adds STOP opcode after immediate redeployment attempt
                // has failed
                // ContractC is deployed with empty bytecode
                // Call ContractC to modify storage
                // Revert on demand
                // Transaction reverts
                // TXSTATUS : Failed
                // LOGS: no logs
                // Note : 2 CREATE2 opcode called
                create2CallC_withRevert,
                // SCENARIO 4 - Attempt ContractC deployment with a staticCall
                // TXSTATUS : Successfull
                // LOGS: 1 StaticCallMyselfFail
                // Note 1 : no CalledCreate2WithInitCodeCEvent as attempt fails prior
                // Note : 1 CREATE2 opcode called
                create2WithStaticCall,
                // SCENARIO 5 - Four ContractC deployment attempts : (1) with max value, (2)
                // acceptable value, (3) max value and
                // (4) acceptable value
                // (1) is aborted
                // (2) results in deployment of ContractC
                // (3) is aborted
                // (4) fails as it's a collision with attempt (2)
                // TXSTATUS : Successfull
                // LOGS: 1 ContractCreated
                // Note : 4 CREATE2 opcode called
                create2FourTimes_noRevert),
            // Values to pilot initCode : as many as there are transactions
            List.of(0L, 0L, 0L, 0L, 0L, 0L, 2L, 0L, 0L));

    final ToyExecutionEnvironmentV2 toyExecutionEnvironmentV2 =
        ToyExecutionEnvironmentV2.builder(chainConfig, testInfo)
            .accounts(List.of(userAccount, customCreate2Account))
            .transactions(transactions)
            .transactionProcessingResultValidator(create2Validator)
            .build();
    toyExecutionEnvironmentV2.run();

    // Final check on the deployment number of ContractC

    // PRE-CANCUN FORK
    // At start, deploymentNumber = 0
    // transaction 3 - create2WithInitCodeC : deploymentNumber ++
    // - Deploys contract C with non empty code
    // - deploymentNumber = 1
    // transaction 5 - callContractCSelfDestructPayload : deploymentNumber ++
    // - Self-destruct successful increments deployment number
    // - deploymentNumber = 2
    // transaction 6 - create2WithCallBackAfterCreate2 : deploymentNumber ++
    // - First create2 is successful so increments the deployment number, second create2 makes the
    // whole transaction revert
    // - deploymentNumber = 3
    // transaction 7 - create2CallCAndRevert : deploymentNumber ++
    // - Create2 deploys contractC with empty bytecode (thus the deployment number increment) and
    // reverts
    // - deploymentNumber = 4
    // transaction 9 - create2FourTimes : deploymentNumber ++
    // - Only one create2 is successful so increments the deployment number by 1
    // - deploymentNumber = 5

    // POST-CANCUN FORK
    // At start, deploymentNumber = 0
    // transaction 3 - create2WithInitCodeC : deploymentNumber ++
    // - Deploys contract C with non empty code
    // - deploymentNumber = 1
    int deploymentNumber =
        toyExecutionEnvironmentV2
            .getHub()
            .transients()
            .conflation()
            .deploymentInfo()
            .deploymentNumber(Address.fromHexString(expectedContractCAddress.toString()));
    int expectedDeploymentNumber = isPostCancun(fork) ? 1 : 5;
    assertEquals(expectedDeploymentNumber, deploymentNumber);
  }

  // Play Scenario from 1 to 5 with Calls from root context
  // The scenarii should end with a successful self-destruct of ContractC
  // We send another final transaction to attempt a redeployment of ContractC at the same address
  // - pre-Cancun : this should be successful
  // - post-Cancun : this should fail as the self-destruct did not happen in the same transaction as
  // the deployment
  @Test
  void deployContractCWithCreate2ScenariiTriggeredFromRoot(TestInfo testInfo) {
    // Payload preparation
    Bytes advancedCreateScenariiTriggeredFromRoot =
        CustomCreate2Payload.advancedCreateScenariiTriggeredFromRoot(initCodeC, salt);

    Map<String, List<Integer>> logsTopicMap = new HashMap<>();
    Map<String, List<Bytes>> logsDataMap = new HashMap<>();

    List<Integer> txStatuses = List.of(1, 1);

    // 1 in scenario 1
    // 1 in scenario 3
    logsTopicMap.put(callMyselfFail, List.of(2, 0));
    // 1 in scenario 2
    logsTopicMap.put(staticCallMyselfFailEvent, List.of(1, 0));
    // 1 in scenario 4
    logsTopicMap.put(contractCreatedEvent, List.of(1, 1));
    logsTopicMap.put(callCreate2WithInitCodeC_noValue_Event, List.of(1, 0));
    // 1 in scenario 5
    logsTopicMap.put(storeInMapEvent, List.of(1, 0));
    logsTopicMap.put(selfDestructEvent, List.of(1, 0));
    // In the last create2 from the last transaction, to see if selfdestruct from previous
    // transaction has worked
    logsTopicMap.put(callCreate2WithInitCodeC_withValue_Event, List.of(0, 1));

    logsDataMap.put(
        contractCreatedEvent,
        List.of(expectedContractCAddressLogData, expectedContractCAddressLogData));
    // The storeInMap call uses key 1
    logsDataMap.put(
        storeInMapEvent,
        List.of(
            Bytes.fromHexString(
                "0x0000000000000000000000000000000000000000000000000000000000000001"),
            Bytes.EMPTY));

    // Instantiate validator
    TransactionProcessingResultValidator txValidator =
        new SmartContractTestValidator(txStatuses, logsTopicMap, logsDataMap);

    List<Transaction> transactions =
        getTransactions(
            customCreate2Account,
            userAccount,
            List.of(advancedCreateScenariiTriggeredFromRoot, create2WithInitCodeC_withValue),
            List.of(2L /* Msg.value is 2 for Scenario 4*/, 0L));

    final ToyExecutionEnvironmentV2 toyExecutionEnvironmentV2 =
        ToyExecutionEnvironmentV2.builder(chainConfig, testInfo)
            .accounts(List.of(userAccount, customCreate2Account))
            .transactions(transactions)
            .transactionProcessingResultValidator(txValidator)
            .build();
    toyExecutionEnvironmentV2.run();
  }

  // Trigger Scenario 5 to 1 in nested Calls
  @Test
  void deployContractCWithCreate2ScenariiNestedCalls(TestInfo testInfo) {
    // Payload preparation
    Bytes advancedCreateScenariiNestedCalls =
        CustomCreate2Payload.advancedCreateScenariiNestedCalls(initCodeC, salt);

    Map<String, List<Integer>> logsTopicMap = new HashMap<>();
    Map<String, List<Bytes>> logsDataMap = new HashMap<>();

    List<Integer> txStatuses = List.of(1, 1);

    // 1 in call to scenario 3
    logsTopicMap.put(callMyselfFail, List.of(1, 0));
    // 1 in scenario 4
    logsTopicMap.put(contractCreatedEvent, List.of(1, 1));
    logsTopicMap.put(callCreate2WithInitCodeC_noValue_Event, List.of(1, 0));
    // 1 in scenario 5
    logsTopicMap.put(storeInMapEvent, List.of(1, 0));
    logsTopicMap.put(selfDestructEvent, List.of(1, 0));
    // In the last create2 from the last transaction, to see if selfdestruct from previous
    // transaction has worked
    logsTopicMap.put(callCreate2WithInitCodeC_withValue_Event, List.of(0, 1));

    logsDataMap.put(
        contractCreatedEvent,
        List.of(expectedContractCAddressLogData, expectedContractCAddressLogData));
    // The storeInMap call uses key 1
    logsDataMap.put(
        storeInMapEvent,
        List.of(
            Bytes.fromHexString(
                "0x0000000000000000000000000000000000000000000000000000000000000001"),
            Bytes.EMPTY));

    // Instantiate validator
    TransactionProcessingResultValidator txValidator =
        new SmartContractTestValidator(txStatuses, logsTopicMap, logsDataMap);

    List<Transaction> transactions =
        getTransactions(
            customCreate2Account,
            userAccount,
            List.of(advancedCreateScenariiNestedCalls, create2WithInitCodeC_withValue),
            List.of(2L /* Msg.value is 2 for Scenario 4*/, 0L));

    final ToyExecutionEnvironmentV2 toyExecutionEnvironmentV2 =
        ToyExecutionEnvironmentV2.builder(chainConfig, testInfo)
            .accounts(List.of(userAccount, customCreate2Account))
            .transactions(transactions)
            .transactionProcessingResultValidator(txValidator)
            .build();
    toyExecutionEnvironmentV2.run();
  }
}
