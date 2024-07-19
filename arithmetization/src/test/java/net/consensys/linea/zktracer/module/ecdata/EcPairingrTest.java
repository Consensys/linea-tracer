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

package net.consensys.linea.zktracer.module.ecdata;

import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.testing.BytecodeCompiler;
import net.consensys.linea.zktracer.testing.BytecodeRunner;
import org.apache.tuweni.bytes.Bytes;
import org.junit.jupiter.api.Test;

public class EcPairingrTest {
  @Test
  void testEcPairingWithTrivialPairing() {
    BytecodeCompiler program =
        BytecodeCompiler.newProgram()
            .push(0x20) // retSize
            .push(0) // retOffset
            .push(192) // argSize
            .push(0) // argOffset
            .push(8) // address
            .push(Bytes.fromHexStringLenient("0xFFFFFFFF")) // gas
            .op(OpCode.STATICCALL);

    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    bytecodeRunner.run();
  }
}
