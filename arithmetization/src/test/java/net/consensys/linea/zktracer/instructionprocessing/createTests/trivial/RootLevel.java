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
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.zktracer.instructionprocessing.createTests.*;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
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

        BytecodeCompiler program = BytecodeCompiler.newProgram();
        genericCreate(program, createType, valueParameter, offsetParameter, SizeParameter.ZERO, salt01);

        if (revert) {
            program
                    .push(0)
                    .push(0)
                    .op(REVERT);
        }

        run(program);
    }

    @ParameterizedTest
    @EnumSource(WhenToTestParameter.class)
    void rootLevelCreate2AndExtCodeHash(WhenToTestParameter when) {

        int storageKey = 0;
        BytecodeCompiler program = BytecodeCompiler.newProgram();
        precomputeCreate2DeploymentAddress(program, salt01);
        storeAt(program, storageKey);

        if (when == BEFORE || when == BEFORE_AND_AFTER) {
            loadFromStorage(program, storageKey);
            program.op(EXTCODEHASH); // we expect to see 0
        }

        genericCreate(program, CreateType.CREATE2, ValueParameter.ONE, OffsetParameter.ZERO, SizeParameter.ZERO, salt01);

        if (when == BEFORE || when == BEFORE_AND_AFTER) {
            loadFromStorage(program, storageKey);
            program.op(EQ); // we expect to have the result be true here
            loadFromStorage(program, storageKey);
            program.op(EXTCODEHASH); // we expect to see KECCAK(( ))
        }

        run(program);
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

    public static void genericCreate(BytecodeCompiler program, CreateType type, ValueParameter valueParameter, OffsetParameter offsetParameter, SizeParameter sizeParameter, String salt) {

        if (type == CreateType.CREATE2) program.push(salt);

        switch (sizeParameter) {
            case ZERO -> program.push(0);
            case TWELVE -> program.push(12);
            case THIRTEEN -> program.push(13);
            case FOURTEEN -> program.push(14);
            case THIRTY_TWO -> program.push(0x20);
            case MSIZE -> program.op(MSTORE);
        }
        switch (offsetParameter) {
            case ZERO -> program.push(0);
            case THREE -> program.push(0);
            case SIXTEEN -> program.push(0x10);
            case SIXTEEN_BYTE_INT -> program.push("abe1245ffff123a87000a543eff12aaa");
            case THIRTY_TWO_BYTE_INT -> program.push("abe1245ffff123a87000a543eff12aaa09987582714266effffefaabc76aa758");
            case MAX -> program.push("ff".repeat(32));
        }
        switch (valueParameter) {
            case ZERO -> program.push(0);
            case ONE -> program.push(1);
            case SELFBALANCE -> program.op(SELFBALANCE);
            case SELFBALANCE_PLUS_ONE -> program.op(SELFBALANCE).push(1).op(ADD);
        }
        switch (type) {
            case CREATE -> program.op(CREATE);
            case CREATE2 -> program.op(OpCode.CREATE2);
        }
    }

    public static void precomputeCreate2DeploymentAddress(BytecodeCompiler program, String salt) {
        program
                .push(0xff)
                .push(0)
                .op(MSTORE8); // (255)
        program
                .op(OpCode.ADDRESS)
                .push(12 * 8)
                .op(SHL)
                .push(1)
                .op(MSTORE); // store address left shifted by 12 bytes
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
                .op(AND); // computes deployment address
    }

    public static void storeAt(BytecodeCompiler program, int storageKey) {
        program
                .push(storageKey)
                .op(SSTORE);
    }

    public static void loadFromStorage(BytecodeCompiler program, int storageKey) {
        program
                .push(storageKey)
                .op(SLOAD);
    }

    private void run(BytecodeCompiler program) {
        BytecodeRunner.of(program).run();
    }

}
