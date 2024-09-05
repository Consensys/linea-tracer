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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.zktracer.json.JsonConverter;

@Slf4j
public class MapFailedReferenceTestsTool {

  public static void mapAndStoreFailedReferenceTest(
      String testName, List<String> logEventMessages, String jsonOutputPath) {
    Set<String> failedConstraints = getFailedConstraints(logEventMessages);
    String jsonString = readFailedTestsOutput(jsonOutputPath);
    JsonConverter jsonConverter = JsonConverter.builder().build();

    List<ModuleToConstraints> constraintToFailingTests =
        getModuleToConstraints(jsonString, jsonConverter);

    mapFailedConstraintsToTestsToModule(failedConstraints, constraintToFailingTests, testName);

    jsonString = jsonConverter.toJson(constraintToFailingTests);
    writeToJsonFile(jsonString, jsonOutputPath);
  }

  private static void mapFailedConstraintsToTestsToModule(
      Set<String> failedConstraints,
      List<ModuleToConstraints> constraintToFailingTests,
      String testName) {
    for (String constraint : failedConstraints) {
      String moduleName = getModuleFromFailedConstraint(constraint);
      if (constraintToFailingTests.stream()
          .filter(mapping -> mapping.equals(moduleName))
          .toList()
          .isEmpty()) {
        constraintToFailingTests.add(new ModuleToConstraints(moduleName, new HashMap<>()));
      }

      ModuleToConstraints moduleMapping =
          constraintToFailingTests.stream()
              .filter(mapping -> mapping.equals(moduleName))
              .toList()
              .getFirst();

      String cleanedConstraintName = constraint;
      if (cleanedConstraintName.contains(".")) {
        cleanedConstraintName = cleanedConstraintName.split("\\.")[1];
      }
      Set<String> failedTests =
          new HashSet<>(
              moduleMapping
                  .constraints()
                  .getOrDefault(cleanedConstraintName, Collections.emptyList()));
      failedTests.add(testName);
      moduleMapping.constraints().put(cleanedConstraintName, failedTests.stream().toList());
    }
  }

  public static List<ModuleToConstraints> getModuleToConstraints(
      String jsonString, JsonConverter jsonConverter) {
    List<ModuleToConstraints> constraintToFailingTests = new ArrayList<>();
    if (!jsonString.isEmpty()) {
      constraintToFailingTests =
          new ArrayList<>(
              Arrays.asList(jsonConverter.fromJson(jsonString, ModuleToConstraints[].class)));
    }
    return constraintToFailingTests;
  }

  private static Set<String> getFailedConstraints(List<String> logEventMessages) {
    Set<String> failedConstraints = new HashSet<>();
    for (String eventMessage : logEventMessages) {
      failedConstraints.addAll(
          extractFailedConstraintsFromException(eventMessage.replaceAll("\u001B\\[[;\\d]*m", "")));
    }
    return failedConstraints;
  }

  @Synchronized
  public static String readFailedTestsOutput(String filePath) {
    Path path = Paths.get(filePath);
    String jsonString = "";
    try {
      jsonString = new String(Files.readAllBytes(path));
    } catch (IOException e) {
      log.info(
          "Failed to read json output, could be first time running: %s".formatted(e.getMessage()));
      try {
        Files.createFile(path);
        log.info("Created a new file at: %s".formatted(filePath));
      } catch (IOException ex) {
        log.error("Failed to create a new file at: %s".formatted(filePath), ex);
      }
    }
    return jsonString;
  }

  @Synchronized
  public static void writeToJsonFile(String jsonString, String filePath) {
    try (FileWriter file = new FileWriter(filePath)) {
      file.write(jsonString);
    } catch (Exception e) {
      log.error("Error - Failed to write failed test output: %s".formatted(e.getMessage()));
    }
  }

  private static List<String> extractFailedConstraintsFromException(String message) {
    List<String> exceptionCauses = extractionExceptionCauses(message);
    Set<String> failedConstraints = new HashSet<>();
    for (String causes : exceptionCauses) {
      failedConstraints.addAll(extractSeparateConstraints(causes));
    }
    return failedConstraints.stream().toList();
  }

  private static List<String> extractionExceptionCauses(String message) {
    Pattern pattern = Pattern.compile("constraints failed: (.+)");
    Matcher matcher = pattern.matcher(message);

    List<String> exceptionCauses = new ArrayList<>();

    while (matcher.find()) {
      String cause = matcher.group(1);
      exceptionCauses.add(cause);
    }

    return exceptionCauses;
  }

  private static List<String> extractSeparateConstraints(String exceptionCauses) {
    String[] parts = exceptionCauses.split(", ");
    return new ArrayList<>(Arrays.asList(parts));
  }

  private static String getModuleFromFailedConstraint(String failedConstraint) {
    if (failedConstraint.contains(".")) {
      return failedConstraint.split("\\.")[0];
    } else {
      return failedConstraint.split("-")[0];
    }
  }
}
