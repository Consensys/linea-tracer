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

import lombok.extern.slf4j.Slf4j;
import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.LauncherSessionListener;

import static net.consensys.linea.ReferenceTestOutcomeRecorderTool.*;

@Slf4j
public class ReferenceTestOutcomeWritter implements LauncherSessionListener {

  @Override
  public void launcherSessionOpened(LauncherSession session) {
    String fileDirectory = setFileDirectory();
    log.info("Results summary will be written to {}", fileDirectory);
  }

  @Override
  public void launcherSessionClosed(LauncherSession session) {
    writeToJsonFile();
    log.info("Reference test results written to file {}", JSON_OUTPUT_FILENAME);
  }
}
