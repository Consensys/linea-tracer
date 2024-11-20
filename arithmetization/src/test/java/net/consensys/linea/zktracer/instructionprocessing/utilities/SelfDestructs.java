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
package net.consensys.linea.zktracer.instructionprocessing.selfdestructTests;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.zktracer.opcode.OpCode;

import static net.consensys.linea.zktracer.instructionprocessing.callTests.Utilities.eoaAddress;
import static net.consensys.linea.zktracer.opcode.OpCode.*;
import static org.hyperledger.besu.datatypes.Address.ECREC;

public class Utilities {

    public static BytecodeCompiler simpleSelfDestruct(BytecodeCompiler program) {
        return program
                .push(eoaAddress) // will be cold
                .op(OpCode.SELFDESTRUCT);
    }

    // will have to be tested in conjunction with DELEGATECALL and CALLCODE
    public static BytecodeCompiler selfReferentialSelfDestruct(BytecodeCompiler program) {
        return program
                .op(ADDRESS) // one self, thus already warm
                .op(OpCode.SELFDESTRUCT);
    }

    // will have to be tested in conjunction with DELEGATECALL and CALLCODE
    public static BytecodeCompiler recipientIsCallerSelfDestruct(BytecodeCompiler program) {
        return program
                .op(CALLER) // warm caller;
                .op(OpCode.SELFDESTRUCT);
    }

    // will have to be tested in conjunction with DELEGATECALL and CALLCODE
    public static BytecodeCompiler recipientIsOriginSelfDestruct(BytecodeCompiler program) {
        return program
                .op(ORIGIN) // warm origin;
                .op(OpCode.SELFDESTRUCT);
    }

    // will have to be tested in conjunction with DELEGATECALL and CALLCODE
    public static int recipientIsPrecompileSelfDestruct(BytecodeCompiler program) {
        ProgramIncrement increment = new ProgramIncrement(program);
        program
                .push(ECREC) // precompiles are warm by default
                .op(OpCode.SELFDESTRUCT);
        return increment.sizeDelta();
    }

    public static int createValueFromContextParameters(BytecodeCompiler program) {
        ProgramIncrement increment = new ProgramIncrement(program);

        program
                .push(256)
                .op(CALLDATASIZE)
                .push(5003)
                .op(ADD)
                .op(CALLVALUE)
                .push(1789)
                .op(ADD).op(MUL)
                .op(MOD);

        return increment.sizeDelta();
    }

    public static BytecodeCompiler storgageTouchingSelfDestructor() {

        BytecodeCompiler selfDestructor = BytecodeCompiler.newProgram();
        selfDestructor.push(0);
        selfDestructor.op(SLOAD).op(POP);
        selfDestructor.push(1);
        selfDestructor.op(SLOAD).op(POP);
        Utilities.createValueFromContextParameters(selfDestructor);
        selfDestructor.op(DUP1);
        selfDestructor.push(0).op(SSTORE);
        selfDestructor.push(1).op(ADD);
        selfDestructor.push(1).op(SSTORE);
        Utilities.simpleSelfDestruct(selfDestructor);

       return selfDestructor;
    }
}
