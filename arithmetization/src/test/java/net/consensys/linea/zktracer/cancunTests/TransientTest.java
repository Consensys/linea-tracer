/*
 * Copyright ConsenSys Inc.
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

package net.consensys.linea.zktracer.cancunTests;

import static net.consensys.linea.zktracer.Fork.CANCUN;
import static net.consensys.linea.zktracer.opcode.OpCode.TLOAD;
import static net.consensys.linea.zktracer.opcode.OpCode.TSTORE;
import static net.consensys.linea.zktracer.opcode.OpCodes.loadOpcodes;

import net.consensys.linea.reporting.TracerTestBase;
import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import org.junit.jupiter.api.Test;

public class TransientTest extends TracerTestBase {

  @Test
  void testSmallZeroAdd() {
    loadOpcodes(CANCUN);
    BytecodeRunner.of(
            BytecodeCompiler.newProgram(testInfo)
                .push(1) // value
                .push(2) // storage key
                .op(TSTORE)
                .push(2) // storage key
                .op(TLOAD)
                .compile())
        .run(testInfo);
  }
}
