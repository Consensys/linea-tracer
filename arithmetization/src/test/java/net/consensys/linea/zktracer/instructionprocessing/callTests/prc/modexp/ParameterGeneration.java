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
package net.consensys.linea.zktracer.instructionprocessing.callTests.prc.modexp;

import net.consensys.linea.zktracer.instructionprocessing.callTests.prc.*;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.junit.jupiter.params.provider.Arguments;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static net.consensys.linea.zktracer.opcode.OpCode.*;
import static net.consensys.linea.zktracer.opcode.OpCode.STATICCALL;

public class ParameterGeneration {

    /**
     * Generates test parameters for the happy path tests of <b>MODEXP</b>.
     *
     * @return Stream of test parameters
     */
    public static Stream<Arguments> happyPathParameterGeneration() {
        List<OpCode> CallOpCodes = List.of(CALL, CALLCODE, DELEGATECALL, STATICCALL);

        List<Arguments> argumentsList = new ArrayList<>();

        for (OpCode opCode : CallOpCodes) {
            for (GasParameter gas : GasParameter.values()) {
                for (ByteSizeParameter bbs : ByteSizeParameter.values()) {
                    for (ByteSizeParameter ebs : ByteSizeParameter.values()) {
                        for (ByteSizeParameter mbs : ByteSizeParameter.values()) {
                            for (ModexpCallDataSizeParameter cds : ModexpCallDataSizeParameter.values()) {
                                for (ReturnAtParameter returnAt : ReturnAtParameter.values()) {
                                    for (RelativeRangePosition relPos : RelativeRangePosition.values()) {
                                        argumentsList.add(
                                                Arguments.of(
                                                        new ModexpCallParameters(
                                                                opCode,
                                                                gas,
                                                                new CallDataParameter(
                                                                        bbs,
                                                                        ebs,
                                                                        mbs,
                                                                        cds
                                                                ),
                                                                returnAt,
                                                                relPos,
                                                                false
                                                        )
                                                )
                                        );

                                        argumentsList.add(
                                                Arguments.of(
                                                        new ModexpCallParameters(
                                                                opCode,
                                                                gas,
                                                                new CallDataParameter(
                                                                        bbs,
                                                                        ebs,
                                                                        mbs,
                                                                        cds
                                                                ),
                                                                returnAt,
                                                                relPos,
                                                                false
                                                        )
                                                )
                                        );
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return argumentsList.stream();
    }
}
