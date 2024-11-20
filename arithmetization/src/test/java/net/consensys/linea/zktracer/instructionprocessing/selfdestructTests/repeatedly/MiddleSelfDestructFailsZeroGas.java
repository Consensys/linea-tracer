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
package net.consensys.linea.zktracer.instructionprocessing.selfdestructTests.thriceInARowTests;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.hyperledger.besu.datatypes.Address;

import static net.consensys.linea.zktracer.instructionprocessing.utilities.Calls.simpleCall;
import static net.consensys.linea.zktracer.opcode.OpCode.REVERT;

public class MiddleSelfDestructFailsZeroGas {
    /**
     * Like the other one except that the second CALL fails at the start with zero gas. Reverted an non-reverted versions.
     */
    private BytecodeCompiler threeSelfDestructsSecondFailsWithZeroGas(Address address) {
        BytecodeCompiler program = BytecodeCompiler.newProgram();
        simpleCall(program, OpCode.CALL, 100_000, address, 12, 0, 4, 0, 0);
        simpleCall(program, OpCode.CALL, 0, address, 19, 0, 3, 0, 0);
        simpleCall(program, OpCode.CALL, 100_000, address, 26, 0, 2, 0, 0);
        return program;
    }
    private BytecodeCompiler threeSelfDestructsSecondFailsWithZeroGasThenRevert(Address address) {
        BytecodeCompiler multipleCalls = threeSelfDestructsSecondFailsWithZeroGas(address);
        multipleCalls.push(32).push(0).op(REVERT);
        return multipleCalls;
    }
}
