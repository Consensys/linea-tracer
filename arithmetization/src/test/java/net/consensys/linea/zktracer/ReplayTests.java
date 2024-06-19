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
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, "*.json.gz")) {
      for (Path entry : stream) {
        String filePath = entry.toString();
        System.out.println("Replaying file: " + filePath);
        replay(filePath);
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
    replayBulk("src/test/resources/replays2");
  }
}
