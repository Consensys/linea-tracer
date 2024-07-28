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

package net.consensys.linea.zktracer.module.ecdata;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

public class EcPairingSingleForScenarioTestWatcher implements TestWatcher {
  private static final Path SUCCESSFUL_FILE_PATH;
  private static final Path FAILED_FILE_PATH;

  static {
    String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    try {
      SUCCESSFUL_FILE_PATH =
          Files.createTempFile(
              "test_ec_pairing_single_for_scenario_successful_" + timestamp, ".csv");
      FAILED_FILE_PATH =
          Files.createTempFile("test_ec_pairing_single_for_scenario_failed_" + timestamp, ".csv");

      try (FileWriter writerPassed = new FileWriter(SUCCESSFUL_FILE_PATH.toFile());
          FileWriter writerFailed = new FileWriter(FAILED_FILE_PATH.toFile())) {
        writerPassed.append("Ax,Ay,BxIm,BxRe,ByIm,ByRe\n");
        writerFailed.append("Ax,Ay,BxIm,BxRe,ByIm,ByRe\n");
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void testSuccessful(ExtensionContext context) {
    System.out.println(
        "EcPairingSingleForScenarioTestWatcher\nSUCCESSFUL_FILE_PATH = " + SUCCESSFUL_FILE_PATH);
    logResult(context, SUCCESSFUL_FILE_PATH);
  }

  @Override
  public void testFailed(ExtensionContext context, Throwable cause) {
    System.out.println(
        "EcPairingSingleForScenarioTestWatcher\nFAILED_FILE_PATH = " + FAILED_FILE_PATH);
    logResult(context, FAILED_FILE_PATH);
  }

  private void logResult(ExtensionContext context, Path filePath) {
    Optional<Method> testMethod = context.getTestMethod();
    if (testMethod.isPresent()
        && (testMethod.get().getName().equals("testEcPairingSingleForScenarioUsingMethodSource")
            || testMethod.get().getName().equals("testEcPairingSingleForScenarioUsingCsv"))) {
      String params = context.getDisplayName().replaceAll("\\[\\d+] ", "");
      try (FileWriter writer = new FileWriter(filePath.toFile(), true)) {
        writer.append(params).append("\n");
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
