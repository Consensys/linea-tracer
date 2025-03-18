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

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.consensys.linea.testing.TransactionProcessingResultValidator;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.hyperledger.besu.ethereum.processing.TransactionProcessingResult;
import org.hyperledger.besu.evm.log.LogTopic;

@RequiredArgsConstructor
public class Create2TestValidator implements TransactionProcessingResultValidator {
  @NonNull Map<String, List<Integer>> logsMap;
  int txCounter = 0;

  @Override
  public void accept(Transaction transaction, TransactionProcessingResult result) {
    TransactionProcessingResultValidator.EMPTY_VALIDATOR.accept(transaction, result);
    System.out.println("Number of logs: " + result.getLogs().size());
    int totalLogsMapPerTx = 0;
    for (var logsMapEntry : logsMap.entrySet()) {
      int logsMaplogCount = logsMapEntry.getValue().get(txCounter);
      String logsMapTopic = logsMapEntry.getKey();
      totalLogsMapPerTx = totalLogsMapPerTx + logsMaplogCount;

      if (logsMaplogCount > 0) {
        for (int i = 0; i < result.getLogs().size(); i++) {
          List<LogTopic> txLogsTopics = result.getLogs().get(i).getTopics();
          for (int j = 0; j < txLogsTopics.size(); j++) {
            String txLogsTopic = txLogsTopics.get(j).toString();
            if (logsMapTopic.toString().equals(txLogsTopic)) {
              logsMaplogCount--;
            }
          }
        }
      }

      // Check that all logs we've listed are the same as the logs in the result
      if (logsMaplogCount != 0) {
        fail("Log count mismatch for topic: " + logsMapTopic + " and Tx counter: " + txCounter);
      }
    }

    // Check that we have listed all the logs in LogsMap
    assertEquals(totalLogsMapPerTx, result.getLogs().size());

    txCounter++;
  }
}
