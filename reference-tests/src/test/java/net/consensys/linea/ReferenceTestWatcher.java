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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.opentest4j.AssertionFailedError;

import static net.consensys.linea.TestState.*;
import static net.consensys.linea.testing.ExecutionEnvironment.CORSET_VALIDATION_RESULT;

public class ReferenceTestWatcher implements TestWatcher {
  private static final String ASSERTION_FAILED = "ASSERTION_FAILED";
  private static final String UNCATEGORIZED_EXCEPTION = "UNCATEGORIZED_EXCEPTION";

  @Override
  public void testFailed(ExtensionContext context, Throwable cause) {
    String testName = context.getDisplayName().split(": ")[1];

    Map<String, Set<String>> logEventMessages = new HashMap<>();
    if (cause != null && cause instanceof AssertionFailedError){
      if(((AssertionFailedError) cause).getActual() != null){
        if(cause.getMessage().contains(CORSET_VALIDATION_RESULT)){
          String constraints = cause.getMessage().replaceFirst(CORSET_VALIDATION_RESULT, "");
          logEventMessages = ReferenceTestOutcomeRecorderTool.extractConstraints(constraints);
        } else {
          logEventMessages.put(ASSERTION_FAILED, Set.of(cause.getMessage()));
        }
      } else {
        logEventMessages.put(UNCATEGORIZED_EXCEPTION, Set.of(cause.getMessage()));
      }
    }

    ReferenceTestOutcomeRecorderTool.mapAndStoreTestResult(
            testName, FAILED, logEventMessages);
  }

  @Override
  public void testSuccessful(ExtensionContext context) {
    String testName = context.getDisplayName().split(": ")[1];
    ReferenceTestOutcomeRecorderTool.mapAndStoreTestResult(
            testName, SUCCESS, Map.of());
  }

  @Override
  public void testDisabled(ExtensionContext context, Optional<String> reason) {
    String testName = context.getDisplayName().split(": ")[1];
    ReferenceTestOutcomeRecorderTool.mapAndStoreTestResult(
            testName, DISABLED, Map.of());
  }
  @Override
  public void testAborted(ExtensionContext context, Throwable cause) {
    String testName = context.getDisplayName().split(": ")[1];
    ReferenceTestOutcomeRecorderTool.mapAndStoreTestResult(
            testName, ABORTED, Map.of());
  }

}
