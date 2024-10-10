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

import static net.consensys.linea.ReferenceTestOutcomeRecorderTool.*;

import lombok.extern.slf4j.Slf4j;
import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.LauncherSessionListener;

import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

@Slf4j
public class ReferenceTestOutcomeWriter implements LauncherSessionListener {

//  @Override
//  public void launcherSessionOpened(LauncherSession session) {
//    String fileDirectory = setFileDirectory();
//    log.info("Results summary will be written to {}", fileDirectory);
//  }

  @Override
  public void launcherSessionClosed(LauncherSession session) {
    try {
      String directory =  setFileDirectory();
      log.info("Reference test will be written to file {}\\{}", directory, JSON_OUTPUT_FILENAME);
      writeToJsonFile().get();
      log.info("Reference test results written to file {}", JSON_OUTPUT_FILENAME);
      log.info("Path exists: {}, file exist: {}", Paths.get(directory).toFile().exists(), Paths.get(directory).resolve(JSON_OUTPUT_FILENAME).toFile().exists());
    } catch (InterruptedException | ExecutionException e) {
      log.error("Error while writing results");
      throw new RuntimeException("Error while writing results", e);
    }
  }
}
