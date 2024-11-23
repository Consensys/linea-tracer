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
package net.consensys.linea.zktracer.instructionprocessing.ZeroSizeTests;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static com.google.common.base.Preconditions.checkArgument;

public class ReturnRevertArguments {

    public String hugeOffset = "ff".repeat(32);

    @ParameterizedTest
    @EnumSource(
        value = OpCode.class,
        names = {"RETURN", "REVERT"})
    void rootContextMessageCall(OpCode opCode) {
        BytecodeCompiler program = zeroSizeReturnOrRevert(opCode);
        BytecodeRunner.of(program.compile()).run();
    }

    private BytecodeCompiler zeroSizeReturnOrRevert(OpCode opCode) {
        checkArgument(opCode == OpCode.RETURN || opCode == OpCode.REVERT);

        BytecodeCompiler program = BytecodeCompiler.newProgram();
        program
                .push(0) // zero size
                .push(hugeOffset) // huge offset
                .op(opCode);
        return program;
    }
}
