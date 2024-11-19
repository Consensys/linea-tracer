package net.consensys.linea;

import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.reporting.TestOutcomeWriterTool;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.util.Optional;

@Slf4j
public class UnitTestWatcher implements TestWatcher {

  private String FAILED = "FAILED";

  @Override
  public void testFailed(ExtensionContext context, Throwable cause) {
    String testName = context.getDisplayName().split(": ")[1];
    log.info("Adding failure for {}", testName);
    TestOutcomeWriterTool.addFailure(FAILED, cause.getMessage(), testName);
    log.info("Failure added for {}", testName);
  }

  @Override
  public void testSuccessful(ExtensionContext context) {
    TestOutcomeWriterTool.addSuccess();
  }

  @Override
  public void testDisabled(ExtensionContext context, Optional<String> reason) {
    TestOutcomeWriterTool.addSkipped();
  }

  @Override
  public void testAborted(ExtensionContext context, Throwable cause) {
    TestOutcomeWriterTool.addAborted();
  }
}