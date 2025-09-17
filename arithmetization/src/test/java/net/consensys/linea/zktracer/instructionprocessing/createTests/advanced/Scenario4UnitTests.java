package net.consensys.linea.zktracer.instructionprocessing.createTests.advanced;

import static net.consensys.linea.zktracer.instructionprocessing.createTests.advanced.ScenarioUtils.*;
import static net.consensys.linea.zktracer.instructionprocessing.utilities.MonoOpCodeSmcs.userAccount;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.consensys.linea.reporting.TracerTestBase;
import net.consensys.linea.testing.ToyExecutionEnvironmentV2;
import net.consensys.linea.testing.TransactionProcessingResultValidator;
import net.consensys.linea.zktracer.instructionprocessing.utilities.SmartContractTestValidator;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

// Recap : CustomCreate2 is a contract with multiple methods to deploy ContractC using CREATE2
// opcode
// See more details in AllScenariiInitCodeTests

/// ////////////////////////////////////////////
// SCENARIO 4 - ATTEMPT CREATE2 AFTER A CREATE2
/// ////////////////////////////////////////////
// Attempts a create2 after a successful create2, by calling ContractC that does a callback to
// CustomCreate2 to deploy

public class Scenario4UnitTests extends TracerTestBase {

  // SCENARIO 4 -
  // TXSTATUS : Successful
  // LOGS: 1 ContractCreated, 1 CallCreate2WithInitCodeC_noValue
  // Note: transaction is sent with value 2
  @Test
  void deployScenario4(TestInfo testInfo) {
    Map<String, List<Integer>> logsTopicMap = new HashMap<>();
    Map<String, List<Bytes>> logsDataMap = new HashMap<>();
    List<Integer> txStatuses = List.of(1, 1, 1);

    logsTopicMap.put(contractCreatedEvent, List.of(0, 0, 1));
    // Call to attempt a redeployment after the successful one
    logsTopicMap.put(callCreate2WithInitCodeC_noValue_Event, List.of(0, 0, 1));

    // Instantiate validator
    TransactionProcessingResultValidator create2OneTxValidator =
        new SmartContractTestValidator(txStatuses, logsTopicMap, logsDataMap);

    List<Transaction> transactions =
        getTransactions(
            customCreate2Account,
            userAccount,
            List.of(storeInitCodeC, storeSalt, create2WithCallCtoCallback_noValue),
            List.of(0L, 0L, 2L));

    final ToyExecutionEnvironmentV2 toyExecutionEnvironmentV2 =
        ToyExecutionEnvironmentV2.builder(chainConfig, testInfo)
            .accounts(List.of(userAccount, customCreate2Account))
            .transactions(transactions)
            .transactionProcessingResultValidator(create2OneTxValidator)
            .build();
    toyExecutionEnvironmentV2.run();
  }

  // SCENARIO 4 - WITH NESTED CALLS TO SCENARIO 1 AND 2 AND 3
  // TXSTATUS : Successful
  // LOGS: 1 CallMyselfFail, 1 ContractCreated, 1 CallCreate2WithInitCodeC_noValue
  // Note: transaction is sent with value 2
  @Test
  void deployScenario4Nested(TestInfo testInfo) {
    Map<String, List<Integer>> logsTopicMap = new HashMap<>();
    Map<String, List<Bytes>> logsDataMap = new HashMap<>();
    List<Integer> txStatuses = List.of(1, 1, 1);

    // 1 from the nested call
    logsTopicMap.put(callMyselfFail, List.of(0, 0, 1));
    // 1 from the scenario 4
    logsTopicMap.put(contractCreatedEvent, List.of(0, 0, 1));
    // Call to attempt a redeployment after the successful one
    logsTopicMap.put(callCreate2WithInitCodeC_noValue_Event, List.of(0, 0, 1));

    // Instantiate validator
    TransactionProcessingResultValidator txValidator =
        new SmartContractTestValidator(txStatuses, logsTopicMap, logsDataMap);

    List<Transaction> transactions =
        getTransactions(
            customCreate2Account,
            userAccount,
            List.of(
                storeInitCodeC,
                storeSalt,
                callMyselfWithCreate2WithCallCtoCallback_noValueAndNested),
            List.of(0L, 0L, 2L));

    final ToyExecutionEnvironmentV2 toyExecutionEnvironmentV2 =
        ToyExecutionEnvironmentV2.builder(chainConfig, testInfo)
            .accounts(List.of(userAccount, customCreate2Account))
            .transactions(transactions)
            .transactionProcessingResultValidator(txValidator)
            .build();
    toyExecutionEnvironmentV2.run();
  }
}
