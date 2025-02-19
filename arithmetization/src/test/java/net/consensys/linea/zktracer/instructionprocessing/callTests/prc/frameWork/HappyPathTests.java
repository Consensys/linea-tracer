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
package net.consensys.linea.zktracer.instructionprocessing.callTests.prc.frameWork;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.zktracer.instructionprocessing.callTests.prc.CodeExecutionMethods;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static net.consensys.linea.zktracer.instructionprocessing.callTests.Utilities.revertWith;
import static net.consensys.linea.zktracer.instructionprocessing.callTests.prc.CodeExecutionMethods.*;
import static net.consensys.linea.zktracer.instructionprocessing.callTests.prc.CodeExecutionMethods.runCreateDeployingForeignCodeAndCallIntoIt;

public abstract class HappyPathTests<T extends CallParameters> {

    /**
     * <b>CONTRACT_DEPLOYMENT_TRANSACTION</b> case.
     *
     * <p>See {@link CodeExecutionMethods} for documentation and context.
     *
     * @param params
     */
    @ParameterizedTest
    @MethodSource("happyPathParameterGeneration")
    public void deploymentTransactionTest(T params) {

        BytecodeCompiler txInitCode = params.happyPathWipeReturnDataHappyPathProgram();
        if (params.willRevert()) revertWith(txInitCode, 0, 0);

        runDeploymentTransactionWithProvidedCodeAsInitCode(txInitCode);
    }

    /**
     * <b>MESSAGE_CALL_FROM_ROOT</b> case.
     *
     * <p>See {@link CodeExecutionMethods} for documentation and context.
     */
    @ParameterizedTest
    @MethodSource("happyPathParameterGeneration")
    public void messageCallFromRootTest(T params) {
        BytecodeCompiler chadPrcEnjoyerCode = params.happyPathWipeReturnDataHappyPathProgram();
        runMessageCallToAccountEndowedWithProvidedCode(chadPrcEnjoyerCode, params.willRevert());
    }

    /**
     * <b>DURING_DEPLOYMENT</b> case.
     *
     * <p>See {@link CodeExecutionMethods} for documentation and context.
     *
     * <p>The {@link CodeExecutionMethods#root} contract fully copies the code of the account whose
     * address is in the {@link CodeExecutionMethods#transaction} call data. This account is the
     * {@link CodeExecutionMethods#chadPrcEnjoyer}. That code is then used as the initialization code
     * of a <b>CREATE</b>. The whole operation optionally <b>REVERT</b>'s.
     *
     * @param params
     */
    @ParameterizedTest
    @MethodSource("happyPathParameterGeneration")
    public void happyPathDuringCreate(T params) {
            BytecodeCompiler foreignCode = params.happyPathWipeReturnDataHappyPathProgram();
            runForeignByteCodeAsInitCode(foreignCode, params.willRevert());
    }

    /**
     * <b>AFTER_DEPLOYMENT</b> case.
     *
     * <p>See {@link CodeExecutionMethods} for documentation and context.
     */
    @ParameterizedTest
    @MethodSource("happyPathParameterGeneration")
    public void happyPathAfterCreate(T params) {
            BytecodeCompiler chadPrcEnjoyerCode = params.happyPathWipeReturnDataHappyPathProgram();
            runCreateDeployingForeignCodeAndCallIntoIt(chadPrcEnjoyerCode, params.willRevert());
    }

}
