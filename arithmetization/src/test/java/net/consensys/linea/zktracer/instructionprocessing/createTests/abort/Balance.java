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
package net.consensys.linea.zktracer.instructionprocessing.createTests.abort;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.zktracer.instructionprocessing.createTests.CreateType;
import net.consensys.linea.zktracer.instructionprocessing.createTests.OffsetParameter;
import net.consensys.linea.zktracer.instructionprocessing.createTests.SizeParameter;
import net.consensys.linea.zktracer.instructionprocessing.createTests.ValueParameter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static net.consensys.linea.zktracer.instructionprocessing.createTests.SizeParameter.*;
import static net.consensys.linea.zktracer.instructionprocessing.createTests.trivial.RootLevel.*;
import static net.consensys.linea.zktracer.opcode.OpCode.SHA3;

public class Balance {

    @ParameterizedTest
    @MethodSource("rootLevelInsufficientBalanceParameters")
    void rootLevelInsfficientBalanceEmptyCreateOpcodeTest(CreateType createType, SizeParameter sizeParameter, OffsetParameter offsetParameter) {

        BytecodeCompiler program = BytecodeCompiler.newProgram();

        if (sizeParameter == MSIZE) {
            program.push(512).push(0).op(SHA3); // purely to expand memory
        }

        genericCreate(program, createType, ValueParameter.SELFBALANCE_PLUS_ONE, offsetParameter, sizeParameter, salt01);

        run(program);
    }

    /**
     * {@link #rootLevelInsufficientBalanceParameters} excludes ''large sizes'': we are interested in unexceptional but aborted
     * CREATE(2)'s.
     *
     */
    private static Stream<Arguments> rootLevelInsufficientBalanceParameters() {

        List<Arguments> arguments = new ArrayList<>();
        List<SizeParameter> sizeParameters = List.of(ZERO, TWELVE, THIRTEEN, FOURTEEN, THIRTY_TWO, MSIZE);

        for (CreateType createType : CreateType.values()) {
            for (OffsetParameter offsetParameter : OffsetParameter.values()) {
                for (SizeParameter sizeParameter : sizeParameters) {
                    arguments.add(Arguments.of(createType, sizeParameter, offsetParameter));
                }
            }
        }

        return arguments.stream();
    }
}
