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

import java.io.FileWriter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.zktracer.json.JsonConverter;

@Slf4j
public class ReferenceTestOutcomeRecorderTool {

  public static final String JSON_OUTPUT_FILENAME = "failedBlockchainReferenceTests.json";
  public static JsonConverter jsonConverter = JsonConverter.builder().build();
  public static volatile BlockchainReferenceTestOutcome testOutcomes =
      new BlockchainReferenceTestOutcome(
          new AtomicInteger(0),
          new AtomicInteger(0),
          new AtomicInteger(0),
          new AtomicInteger(0),
          new ConcurrentHashMap<>());

  public static void mapAndStoreTestResult(
      String testName, TestState success, Map<String, Set<String>> failedConstraints) {
    switch (success) {
      case FAILED -> {
        testOutcomes.failedCounter().incrementAndGet();
        for (Map.Entry<String, Set<String>> failedConstraint : failedConstraints.entrySet()) {
          String moduleName = failedConstraint.getKey();
          for (String constraint : failedConstraint.getValue()) {
            ConcurrentMap<String, ConcurrentSkipListSet<String>> constraintsToTests =
                testOutcomes
                    .modulesToConstraintsToTests()
                    .computeIfAbsent(moduleName, m -> new ConcurrentHashMap<>());
            ConcurrentSkipListSet<String> failingTests =
                constraintsToTests.computeIfAbsent(constraint, m -> new ConcurrentSkipListSet<>());
            int size = failingTests.size();
            failingTests.add(testName);
            if (failingTests.size() == size) {
              log.warn("Duplicate name found... {}", failedConstraint);
            }
          }
        }
      }
      case SUCCESS -> testOutcomes.successCounter().incrementAndGet();
      case ABORTED -> testOutcomes.abortedCounter().incrementAndGet();
      case DISABLED -> testOutcomes.disabledCounter().incrementAndGet();
    }
  }

  @Synchronized
  public static BlockchainReferenceTestOutcome parseBlockchainReferenceTestOutcome(
      String jsonString) {
    if (!jsonString.isEmpty()) {
      BlockchainReferenceTestOutcome blockchainReferenceTestOutcome =
          jsonConverter.fromJson(jsonString, BlockchainReferenceTestOutcome.class);
      return blockchainReferenceTestOutcome;
    }
    throw new RuntimeException("invalid JSON");
  }

  public static Map<String, Set<String>> extractConstraints(String message) {
    String cleaned =
        message
            .substring(message.indexOf("constraints failed:") + "constraints failed:".length())
            .replaceAll(("\\[[0-9]+m"), " ")
            .replace(']', ' ')
            .trim();
    String[] constraints = cleaned.split(",");

    Map<String, Set<String>> pairs = new HashMap<>();
    for (int i = 0; i < constraints.length; i++) {
      String[] pair;
      if (constraints[i].contains("-into-")) {
        pair = constraints[i].split("-into-");
      } else {
        pair = constraints[i].split("\\.");
      }
      pairs.computeIfAbsent(pair[0].trim(), p -> new HashSet<>()).add(pair[1].trim());
    }

    return pairs;
  }

  @Synchronized
  public static CompletableFuture<Void> writeToJsonFile() {
    return writeToJsonFile(JSON_OUTPUT_FILENAME);
  }

  @Synchronized
  public static CompletableFuture<Void> writeToJsonFile(String name) {
    String fileDirectory = setFileDirectory();
    String jsonString = jsonConverter.toJson(testOutcomes);
    log.info(jsonString);
    return CompletableFuture.runAsync(
        () -> {
          try (FileWriter file = new FileWriter(fileDirectory + name)) {
            file.write(jsonString);
          } catch (Exception e) {
            log.error("Error - Failed to write failed test output: %s".formatted(e.getMessage()));
          }
        });
  }

  static String setFileDirectory() {
    String jsonDirectory = System.getenv("FAILED_TEST_JSON_DIRECTORY");
    if (jsonDirectory == null || jsonDirectory.isEmpty()) {
      return "../tmp/local/";
    }
    return jsonDirectory;
  }
}
