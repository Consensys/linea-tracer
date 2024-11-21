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
package net.consensys.linea.zktracer.instructionprocessing.callTests.prc.sha2;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.hyperledger.besu.datatypes.Address;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static net.consensys.linea.zktracer.instructionprocessing.utilities.Calls.appendCall;

public class GasTests {

    @ParameterizedTest
    @EnumSource(value = OpCode.class, names = {"CALL", "CALLCODE", "DELEGATECALL", "STATICCALL"})
    void sha2ProvidedWithLittleToNoneGasTest(OpCode callOpCode) {

        BytecodeCompiler program = BytecodeCompiler.newProgram();
        appendCall(program, callOpCode, 0, Address.fromHexString(Address.SHA256.toHexString()), 1_000_000, 0, 32 * 10, 0, 32);

        BytecodeRunner.of(program).run();
    }


    @ParameterizedTest
    @EnumSource(value = OpCode.class, names = {"CALL", "CALLCODE", "DELEGATECALL", "STATICCALL"})
    void sha2ProvidedWithPlentifulGasTest(OpCode callOpCode) {

        BytecodeCompiler program = BytecodeCompiler.newProgram();
        appendCall(program, callOpCode, 1_000_000, Address.fromHexString(Address.SHA256.toHexString()), 1_000_000, 0, 32 * 10, 0, 32);

        BytecodeRunner.of(program).run();
    }
}
