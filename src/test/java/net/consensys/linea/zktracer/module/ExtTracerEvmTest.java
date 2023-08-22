/*
 * Copyright ConsenSys AG.
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
package net.consensys.linea.zktracer.module;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import net.consensys.linea.zktracer.ZkTracer;
import net.consensys.linea.zktracer.module.mul.MulUtilsTest;
import net.consensys.linea.zktracer.testutils.TestMessageFrameBuilder;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.evm.Code;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.internal.ReturnStack;
import org.junit.jupiter.api.Test;

class ExtTracerEvmTest {
  private static final ZkTracer TRACER = new ZkTracer();

  @Test
  void testSpecificMulMod() {
    final Code mockCode = mock(Code.class);

    int[] cs = new int[] {1, 1, 137, 21};
    int[] bs = new int[] {251, 12, 1, 1};
    int[] as = new int[] {115, 4, 9, 13};

    List<Bytes> instructions = new ArrayList<>();

    for (int i = 0; i < as.length - 1; i++) {
      instructions.add(MulUtilsTest.mulMod(as[i], bs[i], cs[i]));
    }

    final Bytes code = Bytes.concatenate(instructions);
    when(mockCode.getBytes()).thenReturn(code);

    MessageFrame messageFrame =
        new TestMessageFrameBuilder()
            .code(mockCode)
            .pc(1)
            .initialGas(10L)
            .pushStackItem(Bytes.EMPTY)
            .pushStackItem(Bytes.EMPTY)
            .build();

    assertThat(messageFrame.getSection()).isEqualTo(0);
    assertThat(messageFrame.getPC()).isEqualTo(1);
    assertThat(messageFrame.returnStackSize()).isEqualTo(1);
    assertThat(messageFrame.peekReturnStack()).isEqualTo(new ReturnStack.ReturnStackItem(0, 0, 0));
  }
}
