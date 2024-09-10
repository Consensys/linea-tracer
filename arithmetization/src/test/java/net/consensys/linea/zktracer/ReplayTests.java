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
import org.junit.jupiter.api.Tag;
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
@Tag("replay")
@Tag("nightly")
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

  /**
   * Run replay with the specific file path of a replay file. The conflated trace will be written to
   * the same directory as the replay file. Usage: replayAt("/path/to/your/star-end.json.gz");
   */
  public static void replayAt(String filePath) {
    final Path path = Paths.get(filePath);
    final InputStream stream;
    try {
      stream = Files.newInputStream(path);
    } catch (IOException e) {
      log.error("while loading {}: {}", filePath, e.getMessage());
      throw new RuntimeException(e);
    }

    try (GZIPInputStream gzipStream = new GZIPInputStream(stream)) {
      ToyExecutionEnvironment.builder()
          .build()
          .replay(new BufferedReader(new InputStreamReader(gzipStream)), filePath);
    } catch (IOException e) {
      log.error("while processing {}: {}", filePath, e.getMessage());
      throw new RuntimeException(e);
    }
  }

  public static void bulkReplay(String directory) {
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
    replay("2492975-2492977.json.gz", false);
  }

  /**
   * bulk-replay of multiple replay files specified by a directory. The conflated traces will be
   * moved to "conflated" directory once replayed. The replay files will be moved to "replayed"
   * directory once completed. Note: CORSET_VALIDATOR.validate() is disabled by default for
   * bulkReplay. Usage: bulkReplay("/path/to/your/directory");
   */
  @Test
  void bulkReplay() {
    // bulkReplay("./src/test/resources/replays");
    bulkReplay("");
  }

  @Test
  void leoFailingRange() {
    replay("5389571-5389577.json.gz");
  }

  // @Disabled
  @Test
  void failingMmuModexp() {
    replay("5995162.json.gz");
  }

  // @Disabled
  @Test
  void failRlpAddress() {
    replay("5995097.json.gz");
  }

  // @Disabled
  @Test
  void rlprcptManyTopicsWoLogData() {
    replay("6569423.json.gz", false);
  }

  // @Disabled
  @Test
  void multipleFailingCallToEcrecover() {
    replay("5000544.json.gz");
  }

  // @Disabled
  @Test
  void incident777zkGethMainnet() {
    replay("7461019-7461030.json.gz");
  }

  // @Disabled
  @Test
  void issue1006() {
    replay("6032696-6032699.json.gz");
  }

  // @Disabled
  @Test
  void issue1004() {
    replay("6020023-6020029.json.gz", false);
  }

  // @Disabled
  @Test
  void block_6110045() {
    // The purpose of this test is to check the mechanism for spotting divergence between the replay
    // tests and mainnet.  Specifically, this replay has transaction result information embedded
    // within it.
    replay("6110045.json.gz");
  }

  // @Disabled
  @Test
  void failingCreate2() {
    replay("2250197-2250197.json.gz");
  }

  // @Disabled
  @Test
  void blockHash1() {
    replay("8718090.json.gz");
  }

  // @Disabled
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

  @Test
  void simpleSelfDestruct() {
    replay("50020-50029.json.gz", false);
  }

  // TODO: should be replaced by a unit test triggering a failed CREATE2
  @Test
  void failedCreate2() {
    replay("41640-41649.json.gz");
  }

  // Leo's range split up 5104800-5104883
  ///////////////////////////////////////
  // @Disabled
  @Test
  void test_5104800_5104809() {
    replay("5104800-5104809.json.gz");
  }

  // @Disabled
  @Test
  void test_5104810_5104819() {
    replay("5104810-5104819.json.gz");
  }

  // @Disabled
  @Test
  void test_5104820_5104829() {
    replay("5104820-5104829.json.gz", false);
  }

  // @Disabled
  @Test
  void test_5104830_5104839() {
    replay("5104830-5104839.json.gz");
  }

  // @Disabled
  @Test
  void test_5104840_5104849() {
    replay("5104840-5104849.json.gz", false);
  }

  // @Disabled
  @Test
  void test_5104850_5104859() {
    replay("5104850-5104859.json.gz");
  }

  // @Disabled
  @Test
  void test_5104860_5104869() {
    replay("5104860-5104869.json.gz");
  }

  // @Disabled
  @Test
  void test_5104870_5104879() {
    replay("5104870-5104879.json.gz");
  }

  // @Disabled
  @Test
  void test_5104880_5104883() {
    replay("5104880-5104883.json.gz");
  }

  // Leo's range split up 5105646-5105728
  ///////////////////////////////////////
  // @Disabled
  @Test
  void test_5105646_5105649() {
    replay("5105646-5105649.json.gz", false);
  }

  // @Disabled
  @Test
  void test_5105650_5105659() {
    replay("5105650-5105659.json.gz", false);
  }

  // @Disabled
  @Test
  void test_5105660_5105669() {
    replay("5105660-5105669.json.gz", false);
  }

  // @Disabled
  @Test
  void test_5105670_5105679() {
    replay("5105670-5105679.json.gz", false);
  }

  // @Disabled
  @Test
  void test_5105680_5105689() {
    replay("5105680-5105689.json.gz", false);
  }

  // @Disabled
  @Test
  void test_5105690_5105699() {
    replay("5105690-5105699.json.gz", false);
  }

  // @Disabled
  @Test
  void test_5105700_5105709() {
    replay("5105700-5105709.json.gz", false);
  }

  // @Disabled
  @Test
  void test_5105710_5105719() {
    replay("5105710-5105719.json.gz", false);
  }

  // @Disabled
  @Test
  void test_5105720_5105728() {
    replay("5105720-5105728.json.gz", false);
  }

  // Leo's range split up 5106538-5106638
  // @Disabled
  @Test
  void test_5106538_5106539() {
    replay("5106538-5106539.json.gz");
  }

  // @Disabled
  @Test
  void test_5106540_5106549() {
    replay("5106540-5106549.json.gz", false);
  }

  // @Disabled
  @Test
  void test_5106550_5106559() {
    replay("5106550-5106559.json.gz", false);
  }

  // @Disabled
  @Test
  void test_5106560_5106569() {
    replay("5106560-5106569.json.gz", false);
  }

  // @Disabled
  @Test
  void test_5106570_5106579() {
    replay("5106570-5106579.json.gz");
  }

  // @Disabled
  @Test
  void test_5106580_5106589() {
    replay("5106580-5106589.json.gz");
  }

  // @Disabled
  @Test
  void test_5106590_5106599() {
    replay("5106590-5106599.json.gz");
  }

  // @Disabled
  @Test
  void test_5106600_5106609() {
    replay("5106600-5106609.json.gz");
  }

  // @Disabled
  @Test
  void test_5106610_5106619() {
    replay("5106610-5106619.json.gz", false);
  }

  // @Disabled
  @Test
  void test_5106620_5106629() {
    replay("5106620-5106629.json.gz", false);
  }

  // @Disabled
  @Test
  void test_5106630_5106638() {
    replay("5106630-5106638.json.gz", false);
  }

  // Leo's range split up 5118361-5118389
  ///////////////////////////////////////
  // @Disabled
  @Test
  void test_5118361_5118369() {
    replay("5118361-5118369.json.gz");
  }

  // @Disabled
  @Test
  void test_5118370_5118379() {
    replay("5118370-5118379.json.gz", false);
  }

  // @Disabled
  @Test
  void test_5118380_5118389() {
    replay("5118380-5118389.json.gz", false);
  }

  // Florian's ranges
  ///////////////////
  // @Disabled
  @Test
  void test_6871261_6871263() {
    replay("6871261-6871263.json.gz", false);
  }

  // @Disabled
  @Test
  void test_6930360_6930360() {
    replay("6930360-6930360.json.gz");
  }

  // @Disabled
  @Test
  void test_7040245_7040246() {
    replay("7040245-7040246.json.gz");
  }

  // @Disabled
  @Test
  void test_7037321_7037321() {
    replay("7037321-7037321.json.gz");
  }

  // @Disabled
  @Test
  void test_7037237_7037243() {
    replay("7037237-7037243.json.gz", false);
  }

  // @Disabled
  @Test
  void test_7037244_7037244() {
    replay("7037244-7037244.json.gz");
  }

  // @Disabled
  @Test
  void test_7032685_7032688() {
    replay("7032685-7032688.json.gz");
  }

  // @Disabled
  @Test
  void test_7032397_7032402() {
    replay("7032397-7032402.json.gz");
  }

  /**
   * TODO: should be replace by a unit test triggering a STATICCALL to a precompile, without enough
   * remaining gas if the precompile was considered COLD
   */
  @Test
  void hotOrColdPrecompile() {
    replay("2019510-2019519.json.gz");
  }

  // TODO: should be replace by a unit test triggering a CALLDATACOPY in a ROOT context of a
  // deployment transaction
  @Test
  void callDataCopyCnNotFound() {
    replay("67050-67059.json.gz");
  }

  /**
   * TODO: should be replace by a unit test triggering a RETURN during a deployment transaction,
   * where we run OOG when need to pay the gas cost of the code deposit
   */
  @Test
  void returnOogxForCodeDepositCost() {
    replay("1002387.json.gz");
  }
}
