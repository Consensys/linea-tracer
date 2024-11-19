package net.consensys.linea;

import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.LauncherSessionListener;

import static net.consensys.linea.reporting.TestOutcomeWriterTool.writeToJsonFile;

public class UnitTestOutcomeWriter  implements LauncherSessionListener {

  public static final String FILE_NAME = "UnitTestsResults.json";

  @Override
  public void launcherSessionClosed(LauncherSession session) {
    writeToJsonFile(FILE_NAME);
  }

}
