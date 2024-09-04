package net.consensys.linea;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import lombok.Synchronized;
import net.consensys.linea.zktracer.json.JsonConverter;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.slf4j.LoggerFactory;

public class ReferenceTestWatcher implements TestWatcher {

  private static final org.slf4j.Logger log = LoggerFactory.getLogger(ReferenceTestWatcher.class);
  ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

  public ReferenceTestWatcher() {
    Logger logger = (Logger) org.slf4j.LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
    listAppender.setContext(logger.getLoggerContext());
    listAppender.start();
    logger.addAppender(listAppender);
  }

  @Override
  public void testFailed(ExtensionContext context, Throwable cause) {
    Set<String> failedConstraints = getFailedConstraints();

    String jsonOutputPath = "failedTests.json";
    String jsonString = readFailedTestsOutput(jsonOutputPath);
    JsonConverter jsonConverter = JsonConverter.builder().build();
    String testName = context.getDisplayName().split(": ")[1];

    List<ModuleToConstraints> constraintToFailingTests =
        getModuleToConstraints(jsonString, jsonConverter);

    mapFailedConstraintsToTestsToModule(failedConstraints, constraintToFailingTests, testName);

    jsonString = jsonConverter.toJson(constraintToFailingTests);
    writeToJsonFile(jsonString, jsonOutputPath);
  }

  private void mapFailedConstraintsToTestsToModule(
      Set<String> failedConstraints,
      List<ModuleToConstraints> constraintToFailingTests,
      String testName
  ) {
    for (String constraint : failedConstraints) {
      String moduleName = getModuleFromFailedConstraint(constraint);
      if (constraintToFailingTests.stream()
          .filter(mapping -> mapping.equals(moduleName))
          .toList()
          .isEmpty()) {
        constraintToFailingTests.add(new ModuleToConstraints(moduleName, new HashMap<>()));
      }

      ModuleToConstraints moduleMapping =
          constraintToFailingTests.stream()
              .filter(mapping -> mapping.equals(moduleName))
              .toList()
              .getFirst();

      String cleanedConstraintName = constraint;
      if (cleanedConstraintName.contains(".")) {
        cleanedConstraintName = cleanedConstraintName.split("\\.")[1];
      }
      List<String> failedTests =
          new ArrayList<>(
              moduleMapping.getConstraints().getOrDefault(cleanedConstraintName, Collections.emptyList()));
      failedTests.add(testName);
      moduleMapping.getConstraints().put(cleanedConstraintName, failedTests);
    }
  }

  private static List<ModuleToConstraints> getModuleToConstraints(
      String jsonString, JsonConverter jsonConverter) {
    List<ModuleToConstraints> constraintToFailingTests = new ArrayList<>();
    if (!jsonString.isEmpty()) {
      constraintToFailingTests =
          new ArrayList<>(
              Arrays.asList(jsonConverter.fromJson(jsonString, ModuleToConstraints[].class)));
    }
    return constraintToFailingTests;
  }

  private @NotNull Set<String> getFailedConstraints() {
    Set<String> failedConstraints = new HashSet<>();
    List<ILoggingEvent> logEvents = listAppender.list;
    // Process the latest log events here
    for (ILoggingEvent logEvent : logEvents) {
      failedConstraints.addAll(
          extractFailedConstraintsFromException(
              logEvent.getMessage().replaceAll("\u001B\\[[;\\d]*m", "")));
    }
    return failedConstraints;
  }

  private static String readFailedTestsOutput(String filePath) {
    Path path = Paths.get(filePath);
    String jsonString = "";
    try {
      jsonString = new String(Files.readAllBytes(path));
    } catch (IOException e) {
      log.info(
          "Failed to read json output, could be first time running: %s".formatted(e.getMessage()));
      try {
        Files.createFile(path);
        log.info("Created a new file at: %s".formatted(filePath));
      } catch (IOException ex) {
        log.error("Failed to create a new file at: %s".formatted(filePath), ex);
      }
    }
    return jsonString;
  }

  @Synchronized
  private void writeToJsonFile(String jsonString, String filePath) {
    // Write the JSON string to a file
    try (FileWriter file = new FileWriter(filePath)) {
      file.write(jsonString);
    } catch (Exception e) {
      log.error("Error - Failed to write failed test output: %s".formatted(e.getMessage()));
    }
  }

  private Map<String, String> mapFailedConstraintsToTest(
      String testName, Set<String> failedConstraints) {
    Map<String, String> failedConstrainsMap = new HashMap<>();
    failedConstraints.forEach(constraint -> failedConstrainsMap.put(constraint, testName));

    return failedConstrainsMap;
  }

  private List<String> extractFailedConstraintsFromException(String message) {
    List<String> exceptionCauses = extractionExceptionCauses(message);
    Set<String> failedConstraints = new HashSet<>();
    for (String causes : exceptionCauses) {
      failedConstraints.addAll(extractSeparateConstraints(causes));
    }
    return failedConstraints.stream().toList();
  }

  private static List<String> extractionExceptionCauses(String message) {
    Pattern pattern = Pattern.compile("constraints failed: (.+)");
    Matcher matcher = pattern.matcher(message);

    List<String> exceptionCauses = new ArrayList<>();

    while (matcher.find()) {
      String cause = matcher.group(1);
      exceptionCauses.add(cause);
    }

    return exceptionCauses;
  }

  private List<String> extractSeparateConstraints(String exceptionCauses) {
    String[] parts = exceptionCauses.split(", ");
    return new ArrayList<>(Arrays.asList(parts));
  }

  private String getModuleFromFailedConstraint(String failedConstraint) {
    if (failedConstraint.contains(".")) {
      return failedConstraint.split("\\.")[0];
    } else {
      return failedConstraint.split("-")[0];
    }
  }
}
