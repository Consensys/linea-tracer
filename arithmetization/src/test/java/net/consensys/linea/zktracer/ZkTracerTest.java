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

package net.consensys.linea.zktracer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import net.consensys.linea.zktracer.module.add.Add;
import net.consensys.linea.zktracer.module.hub.Hub;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;

public class ZkTracerTest {

  @Test
  public void createNewTracer() {
    final ZkTracer zkTracer = new ZkTracer();
    assertThat(zkTracer.isExtendedTracing()).isTrue();
  }

  @Test
  void writeToFileTest(@TempDir Path tempDir) throws Exception {
    ZkTracer zkTracer = new ZkTracer();

    // Mock the Hub and other modules as needed
    Hub mockHub = Mockito.mock(Hub.class);
    Add mockAdd = Mockito.mock(Add.class);

    // Prepare test data and expectations
    when(mockHub.getModulesToTrace()).thenReturn(List.of(mockAdd));

    // Specify the file to write to
    Path testFile = tempDir.resolve("testTrace.lt");

    // Actual test
    zkTracer.writeToFile(testFile);

    // Verify the file was created
    assertTrue(Files.exists(testFile));

    // Verify the file content or size as expected, depending on what writeToFile should do
    assertNotEquals(0, Files.size(testFile));
  }
}
