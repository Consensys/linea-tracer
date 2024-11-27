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
package net.consensys.linea.zktracer.instructionprocessing.createTests.trivial;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.zktracer.instructionprocessing.createTests.*;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static net.consensys.linea.zktracer.instructionprocessing.createTests.WhenToTestParameter.BEFORE;
import static net.consensys.linea.zktracer.instructionprocessing.createTests.WhenToTestParameter.BEFORE_AND_AFTER;
import static net.consensys.linea.zktracer.opcode.OpCode.*;

public class RootLevel {

    public String salt01 = "5a1701";
    public String salt02 = "5a1702";

    @ParameterizedTest
    @MethodSource("createParametersForEmptyCreates")
    void rootLevelCreateTests(
            CreateType createType,
            ValueParameter valueParameter,
            OffsetParameter offsetParameter,
            boolean revert) {

    }

    void rootLevelCreate2AndExtCodeHash(
            WhenToTestParameter when
    ) {

        int storageKey = 0;
        BytecodeCompiler program = BytecodeCompiler.newProgram();
        precomputeAndStoreCreate2DeploymentAddress(program, salt01, storageKey);

        if (when == BEFORE || when == BEFORE_AND_AFTER) {
        }
    }

    private static Stream<Arguments> createParametersForEmptyCreates() {

        final List<ValueParameter> valueParameters = List.of(ValueParameter.ZERO, ValueParameter.ONE);

        List<Arguments> arguments = new ArrayList<>();

        for (CreateType createType : CreateType.values()) {
            for (ValueParameter valueParameter : valueParameters) {
                for (OffsetParameter offsetParameter : OffsetParameter.values()) {
                    arguments.add(Arguments.of(createType, valueParameter, offsetParameter, true));
                    arguments.add(Arguments.of(createType, valueParameter, offsetParameter, false));
                }
            }
        }

        return arguments.stream();
    }

    public static void precomputeAndStoreCreate2DeploymentAddress(BytecodeCompiler program, String salt, int storageKey) {
        program
                .push(0xff)
                .push(0)
                .op(MSTORE8); // (255)
        program
                .op(OpCode.ADDRESS)
                .push(8 * 12)
                .op(SHL)
                .push(1)
                .op(MSTORE);
        program
                .push(salt)
                .push(21)
                .op(MSTORE); // salt
        program
                .push(0)
                .push(0)
                .op(SHA3)
                .push(53)
                .op(MSTORE); // init code hash = KECCAK(( ))
        program
                .push(85) // 1 + 20 + 32 + 32
                .push(0)
                .op(SHA3); // extract raw address
        program
                .push("000000000000000000000000ffffffffffffffffffffffffffffffffffffffff")
                .op(AND) // computes deployment address
                .push(storageKey)
                .op(SSTORE); // SSTORE address at key = 0
    }
}
