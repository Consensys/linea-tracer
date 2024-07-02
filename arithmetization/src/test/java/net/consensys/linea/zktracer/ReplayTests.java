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
import java.util.zip.GZIPInputStream;

import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.zktracer.testing.ToyExecutionEnvironment;
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

  public static void replay(String filePath) {
    final Path path = Paths.get(filePath);
    if (!Files.exists(path)) {
      fail("unable to find %s in replay resources".formatted(filePath));
    }

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
      log.error("Error creating directories {} or {}: {}", conflatedDirPath, replayedDirPath, e.getMessage());
      throw new RuntimeException(e);
    }

    try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, "*.json.gz")) {
      for (Path entry : stream) {
        String filePath = entry.toString();
        System.out.println("Replaying file: " + filePath);
        replay(filePath);

        // Move the .lt file to conflated directory with .tmp suffix
        Path ltFilePath = Paths.get(filePath.replace(".json.gz", ".lt"));
        if (Files.exists(ltFilePath)) {
          Path targetLtPath = conflatedDirPath.resolve(ltFilePath.getFileName().toString() + ".tmp");
          Files.move(ltFilePath, targetLtPath);
          Files.move(targetLtPath, targetLtPath.resolveSibling(targetLtPath.getFileName().toString().replace(".tmp", "")));
        }

        // Move the .json.gz file to replayed directory
        Path targetJsonPath = replayedDirPath.resolve(entry.getFileName());
        Files.move(entry, targetJsonPath);
      }
    } catch (IOException e) {
      log.error("Error reading directory {}: {}", directory, e.getMessage());
      throw new RuntimeException(e);
    }
  }

  @Test
  void traceTxStartNotTheSameAsTxPrepare() {
    replay("src/test/resources/replays/start-vs-prepare-tx.json.gz");
  }

  @Test
  void fatMxp() {
    replay("src/test/resources/replays/2492975-2492977.json.gz");
  }

  @Test
  void bulkReplayTest() {
    replayBulk("/home/ubuntu/zkevm-monorepo/prover/integration_test/replays");
  }

  @Test
  void failingMmuModexp() {
    replay("5995162.json.gz");
  }

  @Test
  void failRlpAddress() {
    replay("5995097.json.gz");
  }
}
