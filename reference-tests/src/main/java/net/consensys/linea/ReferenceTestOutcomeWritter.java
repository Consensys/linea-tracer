package net.consensys.linea;

import lombok.extern.slf4j.Slf4j;
import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.LauncherSessionListener;

import static net.consensys.linea.ReferenceTestOutcomeRecorderTool.JSON_OUTPUT_FILENAME;
import static net.consensys.linea.ReferenceTestOutcomeRecorderTool.writeToJsonFile;

@Slf4j
public class ReferenceTestOutcomeWritter implements LauncherSessionListener {

  @Override
  public void launcherSessionClosed(LauncherSession session) {
    writeToJsonFile();
    log.info("Reference test results written to file {}", JSON_OUTPUT_FILENAME);
  }
}
