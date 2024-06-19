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

  public static void replay(String filename) {
    final InputStream fileStream = ReplayTests.class.getClassLoader()
        .getResourceAsStream("replays/%s".formatted(filename));
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
        .build()
        .replay(new BufferedReader(new InputStreamReader(stream)), "src/test/resources/replays/%s".formatted(filename));
  }

  public static void replayBulk(String directory) {
    Path dirPath = Paths.get(directory);
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, "*.json.gz")) {
      for (Path entry : stream) {
        String filename = entry.getFileName().toString();
        System.out.println("Replaying file: " + filename);
        replay(filename);
      }
    } catch (IOException e) {
      log.error("Error reading directory {}: {}", directory, e.getMessage());
      throw new RuntimeException(e);
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

  @Test
  void bulkReplayTest() {
    replayBulk("src/test/resources/replays");
  }
}
