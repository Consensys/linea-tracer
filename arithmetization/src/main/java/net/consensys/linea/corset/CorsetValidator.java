/*
 * Copyright Consensys Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package net.consensys.linea.corset;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

@Slf4j
public class CorsetValidator {
  public record Result(boolean isValid, File traceFile, String corsetOutput) {}

  private static final String ZK_EVM_RELATIVE_PATH = "/zkevm-constraints/zkevm.bin";

  /** Specifies the default zkEVM.bin file to use (including its path). */
  private String defaultZkEvm = null;
  /** Specifies the Corset binary to use (including its path). */
  private String corsetBin;

  /**
   * Specifies whether field arithmetic should be used. To best reflect the prover, this should be
   * enabled.
   */
  @Getter @Setter private boolean fieldArithmetic = true;

  /**
   * Specifies whether constraints should be expanded. For example, normalisation expressions are
   * expanded into columns containing the multiplicative inverse, etc. To best reflect the prover,
   * this should be enabled. Also, the effect of this option is limited unless field arithmetic is
   * also enabled.
   */
  @Getter @Setter private boolean expansion = true;

  /**
   * Specifies whether expansion of "auto constraints" should be performed. This is the final step
   * of expansion taking us to the lowest level. To best reflect the prover, this should be enabled.
   * However, it is not enabled by default because this imposes a high performance overhead.
   */
  @Getter @Setter private boolean autoConstraints = false;

  public CorsetValidator() {
    initCorset();
    initDefaultZkEvm();
  }

  public Result validate(final Path filename) throws RuntimeException {
    if (defaultZkEvm == null) {
      throw new IllegalArgumentException("Default zkevm.bin not set.");
    }
    return validate(filename, defaultZkEvm);
  }

  public Result validate(final Path filename, final String zkEvmBin) throws RuntimeException {
    final Process corsetValidationProcess;
    try {
      List<String> options = buildOptions(filename, zkEvmBin);
      corsetValidationProcess =
          new ProcessBuilder(options)
              .redirectInput(ProcessBuilder.Redirect.INHERIT)
              .redirectErrorStream(true)
              .start();
    } catch (IOException e) {
      log.error("Corset validation has thrown an exception: %s".formatted(e.getMessage()));
      throw new RuntimeException(e);
    }

    final String corsetOutput;
    try {
      corsetOutput =
          IOUtils.toString(corsetValidationProcess.getInputStream(), Charset.defaultCharset());
    } catch (IOException e) {
      log.error(
          "Error while catching output corsetValidationProcess: %s".formatted(e.getMessage()));
      throw new RuntimeException(e);
    }

    try {
      corsetValidationProcess.waitFor(5, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      log.error("Timeout while validating trace file: %s".formatted(e.getMessage()));
      throw new RuntimeException(e);
    }

    if (corsetValidationProcess.exitValue() != 0) {
      log.error("Validation failed: %s".formatted(corsetOutput));
      return new Result(false, filename.toFile(), corsetOutput);
    }

    return new Result(true, filename.toFile(), corsetOutput);
  }

  private void initCorset() {
    final Process whichCorsetProcess;

    try {
      whichCorsetProcess = Runtime.getRuntime().exec(new String[] {"which", "corset"});
    } catch (IOException e) {
      log.error("Error while searching for corset: %s".formatted(e.getMessage()));
      throw new RuntimeException(e);
    }

    final String whichCorsetProcessOutput;
    try {
      whichCorsetProcessOutput =
          IOUtils.toString(whichCorsetProcess.getInputStream(), Charset.defaultCharset());
    } catch (IOException e) {
      log.error("Error while catching output whichCorsetProcess: %s".formatted(e.getMessage()));
      throw new RuntimeException(e);
    }

    try {
      whichCorsetProcess.waitFor(5, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      log.error("Timeout while searching for corset: %s".formatted(e.getMessage()));
      throw new RuntimeException(e);
    }

    if (whichCorsetProcess.exitValue() == 0) {
      corsetBin = whichCorsetProcessOutput.trim();
      return;
    }

    log.warn("Could not find corset executable: %s".formatted(whichCorsetProcessOutput));

    final String homePath = System.getenv("HOME");
    corsetBin = homePath + "/.cargo/bin/corset";
    log.warn("Trying to use default corset path: %s".formatted(corsetBin));

    if (!Files.isExecutable(Path.of(corsetBin))) {
      throw new RuntimeException("Corset is not executable: %s".formatted(corsetBin));
    }
  }

  private void initDefaultZkEvm() {
    final String currentDir;

    try {
      currentDir = Path.of(".").toRealPath().toString();
    } catch (final IOException e) {
      log.error("Error while getting current directory: %s".formatted(e.getMessage()));
      throw new RuntimeException(e);
    }

    final String zkEvmBinInCurrentDir = currentDir + ZK_EVM_RELATIVE_PATH;
    if (new File(zkEvmBinInCurrentDir).exists()) {
      defaultZkEvm = zkEvmBinInCurrentDir;
      return;
    }

    final String zkEvmBinInDirAbove = currentDir + "/.." + ZK_EVM_RELATIVE_PATH;
    if (new File(zkEvmBinInDirAbove).exists()) {
      defaultZkEvm = zkEvmBinInDirAbove;
      return;
    }

    log.warn("Could not find default path for zkevm.bin");
  }

  /**
   * Construct the list of options to be used when running Corset.
   *
   * @return
   */
  private List<String> buildOptions(Path filename, String zkEvmBin) {
    ArrayList<String> options = new ArrayList<>();
    // Specify corset binary
    options.add(corsetBin);
    // Specify corset "check" command.
    options.add("check");
    // Specify corset trace file to use
    options.add("-T");
    options.add(filename.toAbsolutePath().toString());
    // Specify reporting options where:
    //
    // -q Decrease logging verbosity
    // -r detail failing constraint
    // -d dim unimportant expressions for failing constraints
    // -s display original source along with compiled form
    options.add("-qrds");
    // Enable field arithmetic (if applicable)
    if (fieldArithmetic) {
      options.add("-N");
    }
    // Enable expansion (if applicable)
    if (expansion) {
      options.add("-eeee");
    }
    // Enable auto constraints (if applicable)
    if (autoConstraints) {
      options.add("--auto-constraints");
      options.add("nhood,sorts");
    }
    // Specify number of threads to use.
    options.add("-t");
    options.add(determineNumberOfThreads());
    // Specify the zkevm.bin file.
    options.add(zkEvmBin);
    // Done
    return options;
  }

  /**
   * Determine the number of threads to use when checking constraints. The default is "2", but this
   * can be overriden using an environment variable <code>CORSET_THREADS</code>.
   *
   * @return
   */
  private String determineNumberOfThreads() {
    return Optional.ofNullable(System.getenv("CORSET_THREADS")).orElse("2");
  }
}
