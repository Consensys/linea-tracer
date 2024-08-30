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

package net.consensys.linea.zktracer;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;

import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.testing.ToyExecutionEnvironment;
import org.junit.jupiter.api.Test;

/**
 * Replays are captured on a fully (not snapshot) synchronized Besu node running the plugin:
 *
 * <pre>{@code
 * curl -X POST 'http://localhost:8545'
 * --data '{
 *    "jsonrpc":"2.0",
 *    "method":"rollup_captureConflation",
 *    "params":["296519", "296521"], "id":"1"
 *  }'
 * | jq '.result.capture' -r
 * | gzip > arithmetization/src/test/resources/replays/my-test-case.json.gz
 * }</pre>
 *
 * One can run this command: scripts/capture.pl --start xxx --end yyy --output my-test-case.json.gz
 */
@Slf4j
public class ReplayTests {

  /**
   * Loads a .json or .json.gz replay file generated by the {@link
   * net.consensys.linea.blockcapture.BlockCapturer} and execute it as a test.
   *
   * @param filename the file in resources/replays/ containing the replay
   * @param resultChecking enable checking of transaction results. This should always be enabled.
   *     However until existing problems are resolved with the replay mechanism, it may be useful to
   *     disable this for specific tests on a case-by-case basis.
   */
  public static void replay(String filename, boolean resultChecking) {
    final InputStream fileStream =
        ReplayTests.class.getClassLoader().getResourceAsStream("replays/%s".formatted(filename));
    if (fileStream == null) {
      fail("unable to find %s in replay resources".formatted(filename));
    }

    final InputStream stream;
    try {
      stream = filename.toLowerCase().endsWith("gz") ? new GZIPInputStream(fileStream) : fileStream;
    } catch (IOException e) {
      log.error("while loading {}: {}", filename, e.getMessage());
      throw new RuntimeException(e);
    }
    ToyExecutionEnvironment.builder()
        .resultChecking(resultChecking)
        .build()
        .replay(new BufferedReader(new InputStreamReader(stream)));
  }

  /**
   * Implementation of replay which enables result checking by default.
   *
   * @param filename
   */
  public static void replay(String filename) {
    replay(filename, true);
  }

  public static void replayAt(String filePath) {
    // Convert the relative path to an absolute path
    Path path = Paths.get(filePath).toAbsolutePath();

    if (!Files.exists(path)) {
      fail("unable to find %s in replay resources".formatted(path));
    }

    try (InputStream stream = Files.newInputStream(path);
        GZIPInputStream gzipStream = new GZIPInputStream(stream)) {

      ToyExecutionEnvironment.builder()
          .build()
          .replay(new BufferedReader(new InputStreamReader(gzipStream)), path.toString());

    } catch (IOException e) {
      log.error("while processing {}: {}", filePath, e.getMessage());
      throw new RuntimeException(e);
    }
  }

  public static void replayBulk(String directory) {
    Path dirPath = Paths.get(directory);
    Path conflatedDirPath = Paths.get(directory, "../conflated");
    Path replayedDirPath = Paths.get(directory, "../replayed");
    // Create conflated and replayed directories if they do not exist
    try {
      if (!Files.exists(conflatedDirPath)) {
        Files.createDirectory(conflatedDirPath);
      }
      if (!Files.exists(replayedDirPath)) {
        Files.createDirectory(replayedDirPath);
      }
    } catch (IOException e) {
      log.error(
          "Error creating directories {} or {}: {}",
          conflatedDirPath,
          replayedDirPath,
          e.getMessage());
      throw new RuntimeException(e);
    }

    List<Path> files = new ArrayList<>();
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, "*.json.gz")) {
      for (Path entry : stream) {
        files.add(entry);
      }
    } catch (IOException e) {
      log.error("Error reading directory {}: {}", directory, e.getMessage());
      throw new RuntimeException(e);
    }
    // Sort files before processing
    Collections.sort(files);
    for (Path entry : files) {
      String filePath = entry.toString();
      System.out.println("Replaying file: " + filePath);
      replayAt(filePath);
      // Move the .lt file to conflated directory with .tmp suffix
      Path ltFilePath = Paths.get(filePath.replace(".json.gz", ".lt"));
      if (Files.exists(ltFilePath)) {
        Path targetLtPath = conflatedDirPath.resolve(ltFilePath.getFileName().toString() + ".tmp");
        try {
          Files.move(ltFilePath, targetLtPath, StandardCopyOption.REPLACE_EXISTING);
          Path finalLtPath =
              targetLtPath.resolveSibling(
                  targetLtPath.getFileName().toString().replace(".tmp", ""));
          Files.move(targetLtPath, finalLtPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
          log.error("Error moving file {}: {}", ltFilePath, e.getMessage());
        }
      }

      // Move the .json.gz file to replayed directory
      Path targetJsonPath = replayedDirPath.resolve(entry.getFileName());

      try {
        Files.move(entry, targetJsonPath, StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException e) {
        log.error("Error moving file {}: {}", entry, e.getMessage());
      }
    }
  }

  @Test
  void traceTxStartNotTheSameAsTxPrepare() {
    replay("start-vs-prepare-tx.json.gz");
  }

  @Test
  void fatMxp() {
    replay("2492975-2492977.json.gz");
  }

  /**
   * Run replay with the specific file path of a replay file. The conflated trace will be written to
   * the same directory as the replay file.
   */
  @Test
  void replaySpecificFilePath() {
    replayAt("src/test/resources/replays/2492975-2492977.json.gz");
  }

  /**
   * Run a bulk replay of multiple replay files specified by a directory. The conflated traces will
   * be moved to "conflated" directory once replayed. The replay files will be moved to "replayed"
   * directory once completed. Note: CORSET_VALIDATOR.validate() is disabled by default for
   * replayBulk
   */
  @Test
  void replayMultipleReplayFiles() {
    replayBulk("src/test/resources/replays");
  }

  @Test
  void leoFailingRange() {
    replay("5389571-5389577.json.gz");
  }

  @Test
  void failingMmuModexp() {
    replay("5995162.json.gz");
  }

  @Test
  void failRlpAddress() {
    replay("5995097.json.gz");
  }

  @Test
  void rlprcptManyTopicsWoLogData() {
    replay("6569423.json.gz", false);
  }

  @Test
  void multipleFailingCallToEcrecover() {
    replay("5000544.json.gz");
  }

  @Test
  void incident777zkGethMainnet() {
    replay("7461019-7461030.json.gz");
  }

  @Test
  void issue1006() {
    replay("6032696-6032699.json.gz");
  }

  @Test
  void issue1004() {
    replay("6020023-6020029.json.gz", false);
  }

  @Test
  void block_6110045() {
    // The purpose of this test is to check the mechanism for spotting divergence between the replay
    // tests and mainnet.  Specifically, this replay has transaction result information embedded
    // within it.
    replay("6110045.json.gz");
  }

  @Test
  void failingCreate2() {
    replay("2250197-2250197.json.gz");
  }

  @Test
  void blockHash1() {
    replay("8718090.json.gz");
  }

  @Test
  void blockHash2() {
    replay("8718330.json.gz");
  }

  // TODO: should be replaced by a unit test triggering AnyToRamWithPadding (mixed case) MMU
  // instruction
  @Test
  void negativeNumberOfMmioInstruction() {
    replay("6029454-6029459.json.gz");
  }
}
