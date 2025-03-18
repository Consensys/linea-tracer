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

    for (var eventTopic : logsMap.entrySet()) {
      int logCount = eventTopic.getValue().get(txCounter);
      if (logCount > 0) {
        for (int i = 0; i < result.getLogs().size(); i++) {
          List<LogTopic> currentLogTopics = result.getLogs().get(i).getTopics();
          for (int j = 0; j < currentLogTopics.size(); j++) {
            String topic = currentLogTopics.get(j).toString();
            if (eventTopic.getKey().toString().equals(topic)) {
              logCount--;
            }
          }
        }
      }
      if (logCount != 0) {
        fail(
            "Log count mismatch for topic: "
                + eventTopic.getKey()
                + " and Tx counter: "
                + txCounter);
      }
    }
    txCounter++;
  }
}
