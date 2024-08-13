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

package net.consensys.linea.zktracer.module.mod;

import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.testing.BytecodeCompiler;
import net.consensys.linea.zktracer.testing.BytecodeRunner;
import org.apache.tuweni.units.bigints.UInt256;
import org.junit.jupiter.api.Test;

public class ModTest {
  @Test
  void testSignedSmod() {
    BytecodeRunner.of(
            BytecodeCompiler.newProgram()
                .push(UInt256.MAX_VALUE)
                .push(UInt256.MAX_VALUE)
                .op(OpCode.SMOD)
                .op(OpCode.POP)
                .push(0)
                .push(UInt256.MAX_VALUE)
                .op(OpCode.MOD)
                .op(OpCode.POP)
                .push(UInt256.MAX_VALUE)
                .push(UInt256.MAX_VALUE)
                .op(OpCode.MOD)
                .op(OpCode.POP)
                .push(UInt256.MAX_VALUE)
                .push(0)
                .op(OpCode.SDIV)
                .op(OpCode.POP)
                .push(0)
                .push(0)
                .op(OpCode.DIV)
                .op(OpCode.POP)
                .push(UInt256.valueOf(0xffff))
                .push(UInt256.valueOf(0xffffffffL))
                .op(OpCode.DIV)
                .op(OpCode.POP)
                .compile())
        .run();
  }
}
