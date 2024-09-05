package net.consensys.linea;

import java.util.List;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

public class ReferenceTestWatcher implements TestWatcher {

  public static final String JSON_OUTPUT_PATH = "failedTests.json";
  ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

  public ReferenceTestWatcher() {
    Logger logger = (Logger) org.slf4j.LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
    listAppender.setContext(logger.getLoggerContext());
    listAppender.start();
    logger.addAppender(listAppender);
  }

  @Override
  public void testFailed(ExtensionContext context, Throwable cause) {
    String testName = context.getDisplayName().split(": ")[1];
    List<String> logEventMessages =
        listAppender.list.stream().map(ILoggingEvent::getMessage).toList();

    MapFailedReferenceTestsTool.mapAndStoreFailedReferenceTest(
        testName, logEventMessages, JSON_OUTPUT_PATH);
  }
}
