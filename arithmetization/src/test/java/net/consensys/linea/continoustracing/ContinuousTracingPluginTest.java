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
package net.consensys.linea.continoustracing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.hyperledger.besu.plugin.BesuContext;
import org.hyperledger.besu.plugin.services.BesuEvents;
import org.hyperledger.besu.plugin.services.PicoCLIOptions;
import org.hyperledger.besu.plugin.services.TraceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import picocli.CommandLine;

@ExtendWith(MockitoExtension.class)
class ContinuousTracingPluginTest {
  private ContinuousTracingPlugin plugin;

  @Mock BesuContext besuContextMock;
  @Mock PicoCLIOptions picoCLIOptionsMock;
  @Mock BesuEvents besuEventsMock;
  @Mock TraceService traceServiceMock;

  @BeforeEach
  void setup() {
    plugin = new ContinuousTracingPlugin();
  }

  @Test
  void shouldAddCLIOptionAndAddBlockListener() {
    when(besuContextMock.getService(PicoCLIOptions.class))
        .thenReturn(Optional.of(picoCLIOptionsMock));

    plugin.register(besuContextMock);

    verify(picoCLIOptionsMock, times(1))
        .addPicoCLIOptions(matches("linea-continuous"), any(ContinuousTracingCliOptions.class));

    // naming convention is enforced by the Besu plugin implementation
    final CommandLine.Model.CommandSpec commandSpec =
        CommandLine.Model.CommandSpec.forAnnotatedObject(ContinuousTracingCliOptions.create());
    for (CommandLine.Model.OptionSpec optionSpec : commandSpec.options()) {
      for (String optionName : optionSpec.names()) {
        assertThat(optionName).startsWith("--plugin-linea-continuous-");
      }
    }

    when(besuContextMock.getService(BesuEvents.class)).thenReturn(Optional.of(besuEventsMock));
    when(besuContextMock.getService(TraceService.class)).thenReturn(Optional.of(traceServiceMock));

    plugin.start();

    verify(besuEventsMock, times(1))
        .addBlockAddedListener(any(ContinuousTracingBlockAddedListener.class));
  }
}
