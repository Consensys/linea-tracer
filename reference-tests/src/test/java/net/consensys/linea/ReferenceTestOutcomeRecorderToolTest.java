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
package net.consensys.linea;

import static net.consensys.linea.BlockchainReferenceTestJson.readBlockchainReferenceTestsOutput;
import static net.consensys.linea.ReferenceTestOutcomeRecorderTool.getBlockchainReferenceTestOutcome;
import static net.consensys.linea.ReferenceTestOutcomeRecorderTool.mapAndStoreFailedReferenceTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReferenceTestOutcomeRecorderToolTest {

  public static final String TEST_OUTPUT_JSON = "MapFailedReferenceTestsToolTest.json";
  private final String prefix1 = "constraints failed: ";
  private final String prefix2 = "failing constraint ";

  @BeforeEach
  void setup() {
    File outputJsonFile = new File(TEST_OUTPUT_JSON);
    if (outputJsonFile.exists()) {
      outputJsonFile.delete();
    }
  }

  @Test
  void multipleModulesAreStoredCorrectly() {
    String testName = "test1";

    String module1 = "blockdata";
    String constraint1 = module1 + ".value-constraints";

    String module2 = "txndata";
    String constraint2 = module2 + "-into-blockdata";

    List<String> modules = List.of(module1, module2);

    String dummyEvent = createDummyLogEventMessage(List.of(constraint1), prefix1);
    String dummyEvent2 = createDummyLogEventMessage(List.of(constraint2), prefix2);

    mapAndStoreFailedReferenceTest(testName, List.of(dummyEvent, dummyEvent2), TEST_OUTPUT_JSON);

    readBlockchainReferenceTestsOutput(TEST_OUTPUT_JSON)
        .thenApply(
            jsonString -> {
              BlockchainReferenceTestOutcome blockchainReferenceTestOutcome =
                  getBlockchainReferenceTestOutcome(jsonString);

              List<ModuleToConstraints> modulesToConstraints =
                  blockchainReferenceTestOutcome.modulesToConstraints();

              assertThat(modulesToConstraints.size()).isEqualTo(modules.size());
              assertThat(modulesToConstraints.get(0).moduleName()).isEqualTo(module1);
              assertThat(modulesToConstraints.get(1).moduleName()).isEqualTo(module2);
              return null;
            });
  }

  @Test
  void multipleConstraintsInSameModuleAreStoredCorrectly() {
    String testName = "test1";

    String module1 = "blockdata";
    String constraint1 = module1 + ".value-constraints";
    String constraint2 = module1 + ".horizontal-byte-dec";

    List<String> constraints = List.of(constraint1, constraint2);

    String dummyEvent = createDummyLogEventMessage(constraints, prefix1);

    mapAndStoreFailedReferenceTest(testName, List.of(dummyEvent), TEST_OUTPUT_JSON);

    readBlockchainReferenceTestsOutput(TEST_OUTPUT_JSON)
        .thenCompose(
            jsonString -> {
              BlockchainReferenceTestOutcome blockchainReferenceTestOutcome =
                  getBlockchainReferenceTestOutcome(jsonString);

              List<ModuleToConstraints> modulesToConstraints =
                  blockchainReferenceTestOutcome.modulesToConstraints();

              assertThat(modulesToConstraints.size()).isEqualTo(1);

              Map<String, Set<String>> failedConstraints =
                  modulesToConstraints.getFirst().constraints();
              assertThat(failedConstraints.size()).isEqualTo(constraints.size());
              assertThat(failedConstraints.get("value-constraints")).isEqualTo(Set.of("test1"));
              assertThat(failedConstraints.get("horizontal-byte-dec")).isEqualTo(Set.of("test1"));
              return null;
            });
  }

  @Test
  void multipleFailedTestsWithSameConstraintAreStoredCorrectly() {
    String testName1 = "test1";
    String testName2 = "test2";
    String testName3 = "test3";

    String module1 = "blockdata";
    String constraint1 = module1 + ".value-constraints";

    List<String> constraints = List.of(constraint1);
    List<String> modules = List.of(module1);
    List<String> tests = List.of(testName1, testName2, testName3);

    String dummyEvent = createDummyLogEventMessage(constraints, prefix1);

    mapAndStoreFailedReferenceTest(testName1, List.of(dummyEvent), TEST_OUTPUT_JSON);
    mapAndStoreFailedReferenceTest(testName2, List.of(dummyEvent), TEST_OUTPUT_JSON);
    mapAndStoreFailedReferenceTest(testName3, List.of(dummyEvent), TEST_OUTPUT_JSON);
    mapAndStoreFailedReferenceTest(testName3, List.of(dummyEvent), TEST_OUTPUT_JSON);
    mapAndStoreFailedReferenceTest(testName3, List.of(dummyEvent), TEST_OUTPUT_JSON);

    readBlockchainReferenceTestsOutput(TEST_OUTPUT_JSON)
        .thenCompose(
            jsonString -> {
              BlockchainReferenceTestOutcome blockchainReferenceTestOutcome =
                  getBlockchainReferenceTestOutcome(jsonString);

              List<ModuleToConstraints> modulesToConstraints =
                  blockchainReferenceTestOutcome.modulesToConstraints();

              assertThat(modulesToConstraints.size()).isEqualTo(modules.size());
              Set<String> failedTests =
                  modulesToConstraints.getFirst().constraints().get("value-constraints");
              assertThat(failedTests.size()).isEqualTo(tests.size());
              return null;
            });
  }

  private String createDummyLogEventMessage(List<String> failedConstraints, String prefix) {
    String concatenatedConstraints =
        failedConstraints.stream()
            .map(
                s -> s + (failedConstraints.indexOf(s) == failedConstraints.size() - 1 ? "" : ", "))
            .collect(Collectors.joining());

    return prefix + concatenatedConstraints;
  }
}
