package linea.plugin.acc.test;

import static linea.plugin.acc.test.AbstractPluginTest.getResourcePath;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.web3j.tx.gas.DefaultGasProvider;

public class TestCliOptions {
  private final Properties cliOptions = new Properties();

  private static final String MAX_VALUE = String.valueOf(Integer.MAX_VALUE);

  public TestCliOptions() {
    cliOptions.setProperty("--plugin-linea-max-tx-calldata-size=", MAX_VALUE);
    cliOptions.setProperty("--plugin-linea-max-block-calldata-size=", MAX_VALUE);
    cliOptions.setProperty(
        "--plugin-linea-max-tx-gas-limit=", DefaultGasProvider.GAS_LIMIT.toString());
    cliOptions.setProperty("--plugin-linea-deny-list-path=", getResourcePath("/emptyDenyList.txt"));
    cliOptions.setProperty(
        "--plugin-linea-module-limit-file-path=", getResourcePath("/noModuleLimits.json"));
  }

  public TestCliOptions set(String option, String value) {
    cliOptions.setProperty(option, value);
    return this;
  }

  public List<String> build() {
    List<String> optionsList = new ArrayList<>();
    for (String key : cliOptions.stringPropertyNames()) {
      optionsList.add(key + cliOptions.getProperty(key));
    }
    return optionsList;
  }
}
