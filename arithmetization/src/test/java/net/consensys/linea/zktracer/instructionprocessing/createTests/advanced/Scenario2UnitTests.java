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
import org.hyperledger.besu.ethereum.core.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

// Recap : CustomCreate2 is a contract with multiple methods to deploy ContractC using CREATE2
// opcode
// See more details in AllScenariiInitCodeTests

/*

SCENARIO 2 - STATIC CALL A CREATE2

Attempt to static call a create2 deployment of ContractC

     STATICCALL
   -------------->  - CREATE2

 */

public class Scenario2UnitTests extends TracerTestBase {

  /*
  SCENARIO 2 -
  Attempt to static call a create2 deployment
  TXSTATUS : Successful
  LOGS: 1 StaticCallMyselfFail
  Note : transactions are sent with value 2 as will be done in the final AllScenarii test
  Note 2 : used in ScenariiTriggeredFromRoot
   */
  @Test
  void deployScenario2(TestInfo testInfo) {
    Map<String, List<Integer>> logsTopicMap = new HashMap<>();

    // Tx status
    List<Integer> txStatuses = List.of(1);

    // Tx logs to check in validator
    logsTopicMap.put(staticCallMyselfFailEvent, List.of(1));

    // Instantiate validator
    TransactionProcessingResultValidator txValidator =
        new SmartContractTestValidator(txStatuses, logsTopicMap, new HashMap<>());

    List<Transaction> transactions =
        getTransactions(
            customCreate2Account, userAccount, List.of(create2WithStaticCall), List.of(2L));

    final ToyExecutionEnvironmentV2 toyExecutionEnvironmentV2 =
        ToyExecutionEnvironmentV2.builder(chainConfig, testInfo)
            .accounts(List.of(userAccount, customCreate2Account))
            .transactions(transactions)
            .transactionProcessingResultValidator(txValidator)
            .build();
    toyExecutionEnvironmentV2.run();
  }

  /*
  SCENARIO 2 - WITH NESTED CALL TO SCENARIO 1
  Through a call, after create2 four times call, attempt to static call a create2 deployment
  TXSTATUS : Successful
  LOGS: 1 CallMyselfFail, 1 StaticCallMyselfFail
  Note 2 : used in ScenariiNestedCalls
  */
  @Test
  void deployScenario2Nested(TestInfo testInfo) {
    Map<String, List<Integer>> logsTopicMap = new HashMap<>();
    // Tx status
    List<Integer> txStatuses = List.of(1);

    // Tx logs to check in validator
    // 1 from scenario 1 nested
    logsTopicMap.put(callMyselfFail, List.of(1));
    // 1 from scenario 2's static call
    logsTopicMap.put(staticCallMyselfFailEvent, List.of(1));

    // Instantiate validator
    TransactionProcessingResultValidator txValidator =
        new SmartContractTestValidator(txStatuses, logsTopicMap, new HashMap<>());

    List<Transaction> transactions =
        getTransactions(
            customCreate2Account,
            userAccount,
            List.of(callMyselfWithCreate2WithStaticCall_nested),
            List.of(2L));

    final ToyExecutionEnvironmentV2 toyExecutionEnvironmentV2 =
        ToyExecutionEnvironmentV2.builder(chainConfig, testInfo)
            .accounts(List.of(userAccount, customCreate2Account))
            .transactions(transactions)
            .transactionProcessingResultValidator(txValidator)
            .build();
    toyExecutionEnvironmentV2.run();
  }
}
