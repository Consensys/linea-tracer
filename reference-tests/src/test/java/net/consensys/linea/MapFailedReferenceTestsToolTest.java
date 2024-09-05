package net.consensys.linea;

import static net.consensys.linea.MapFailedReferenceTestsTool.getModuleToConstraints;
import static net.consensys.linea.MapFailedReferenceTestsTool.mapAndStoreFailedReferenceTest;
import static net.consensys.linea.MapFailedReferenceTestsTool.readFailedTestsOutput;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.consensys.linea.zktracer.json.JsonConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MapFailedReferenceTestsToolTest {

  public static final String TEST_OUTPUT_JSON = "MapFailedReferenceTestsToolTest.json";
  JsonConverter jsonConverter = JsonConverter.builder().build();

  @BeforeEach
  void setup() {
    File outputJsonFile = new File(TEST_OUTPUT_JSON);
    if (outputJsonFile.exists()) {
      outputJsonFile.delete();
    }
  }

  @Test
  void multipleModulesAreStoredCorrectly() {
    String testName = "test1";

    String module1 = "blockdata";
    String constraint1 = module1 + ".value-constraints";

    String module2 = "txndata";
    String constraint2 = module2 + "-into-blockdata";

    List<String> constraints = List.of(constraint1, constraint2);
    List<String> modules = List.of(module1, module2);

    String dummyEvent = createDummyLogEventMessage(constraints);

    mapAndStoreFailedReferenceTest(testName, List.of(dummyEvent), TEST_OUTPUT_JSON);

    String jsonString = readFailedTestsOutput(TEST_OUTPUT_JSON);
    List<ModuleToConstraints> constraintToFailingTests =
        getModuleToConstraints(jsonString, jsonConverter);

    assertThat(constraintToFailingTests.size()).isEqualTo(modules.size());
    assertThat(constraintToFailingTests.get(0).moduleName()).isEqualTo(module1);
    assertThat(constraintToFailingTests.get(1).moduleName()).isEqualTo(module2);
  }

  @Test
  void multipleConstraintsInSameModuleAreStoredCorrectly() {
    String testName = "test1";

    String module1 = "blockdata";
    String constraint1 = module1 + ".value-constraints";

    String constraint2 = module1 + ".horizontal-byte-dec";

    List<String> constraints = List.of(constraint1, constraint2);

    String dummyEvent = createDummyLogEventMessage(constraints);

    mapAndStoreFailedReferenceTest(testName, List.of(dummyEvent), TEST_OUTPUT_JSON);

    String jsonString = readFailedTestsOutput(TEST_OUTPUT_JSON);
    List<ModuleToConstraints> constraintToFailingTests =
        getModuleToConstraints(jsonString, jsonConverter);

    assertThat(constraintToFailingTests.size()).isEqualTo(1);

    Map<String, List<String>> failedConstraints = constraintToFailingTests.getFirst().constraints();
    assertThat(failedConstraints.size()).isEqualTo(constraints.size());
    assertThat(failedConstraints.get("value-constraints")).isEqualTo(List.of("test1"));
    assertThat(failedConstraints.get("horizontal-byte-dec")).isEqualTo(List.of("test1"));
  }

  @Test
  void multipleFailedTestsWithSameConstraintAreStoredCorrectly() {
    String testName1 = "test1";
    String testName2 = "test2";
    String testName3 = "test3";

    String module1 = "blockdata";
    String constraint1 = module1 + ".value-constraints";

    List<String> constraints = List.of(constraint1);
    List<String> modules = List.of(module1);
    List<String> tests = List.of(testName1, testName2, testName3);

    String dummyEvent = createDummyLogEventMessage(constraints);

    mapAndStoreFailedReferenceTest(testName1, List.of(dummyEvent), TEST_OUTPUT_JSON);
    mapAndStoreFailedReferenceTest(testName2, List.of(dummyEvent), TEST_OUTPUT_JSON);
    mapAndStoreFailedReferenceTest(testName3, List.of(dummyEvent), TEST_OUTPUT_JSON);
    mapAndStoreFailedReferenceTest(testName3, List.of(dummyEvent), TEST_OUTPUT_JSON);
    mapAndStoreFailedReferenceTest(testName3, List.of(dummyEvent), TEST_OUTPUT_JSON);

    String jsonString = readFailedTestsOutput(TEST_OUTPUT_JSON);
    List<ModuleToConstraints> constraintToFailingTests =
        getModuleToConstraints(jsonString, jsonConverter);

    assertThat(constraintToFailingTests.size()).isEqualTo(modules.size());
    List<String> failedTests =
        constraintToFailingTests.getFirst().constraints().get("value-constraints");
    assertThat(failedTests.size()).isEqualTo(tests.size());
  }

  private String createDummyLogEventMessage(List<String> failedConstraints) {
    String prefix = "constraints failed: ";
    String concatenatedConstraints =
        failedConstraints.stream()
            .map(
                s -> s + (failedConstraints.indexOf(s) == failedConstraints.size() - 1 ? "" : ", "))
            .collect(Collectors.joining());

    return prefix + concatenatedConstraints;
  }
}
