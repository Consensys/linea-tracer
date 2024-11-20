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
package net.consensys.linea.zktracer.instructionprocessing.selfdestructTests.repeatedly;

import net.consensys.linea.testing.*;
import net.consensys.linea.zktracer.instructionprocessing.selfdestructTests.SeveralSelfDestructsInARowModifyingStorageTests;
import net.consensys.linea.zktracer.instructionprocessing.selfdestructTests.Type;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.TransactionType;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.ethereum.core.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static net.consensys.linea.zktracer.instructionprocessing.selfdestructTests.SeveralSelfDestructsInARowModifyingStorageTests.keyPair;
import static net.consensys.linea.zktracer.instructionprocessing.selfdestructTests.Type.*;
import static net.consensys.linea.zktracer.instructionprocessing.utilities.Calls.*;

/**
 * We consider the case of 3 successful SELFDESTRUCT's in a row at the same selfDestructorAddress. The selfDestructorAddress parameter decides whether the SELFDESTRUCT targets
 * the same selfDestructorAddress or not. We consider the reverted vs unreverted cases.
 *
 */
public class NormalCaseTests {

    private ToyAccount userAccount = SeveralSelfDestructsInARowModifyingStorageTests.userAccount;
    private ToyAccount toAccount;
    BytecodeCompiler toAccountCode = BytecodeCompiler.newProgram();
    private ToyAccount selfDestructorAccount;

    private void buildToAccount() {
        toAccount = ToyAccount.builder()
                .address(Address.fromHexString("0x1234567890"))
                .balance(Wei.fromEth(2))
                .code(toAccountCode.compile())
                .nonce(23)
                .build();
    }

    private Transaction transaction() {
        return ToyTransaction.builder()
                        .sender(userAccount)
                        .to(toAccount)
                        .transactionType(TransactionType.FRONTIER)
                        .gasLimit(500_000L)
                        .value(Wei.ONE)
                        .keyPair(keyPair)
                        .build();
    }

    private void run(Type type) {
        selfDestructorAccount = basicSelfDestructor(type);
        buildToAccount();
        ToyExecutionEnvironmentV2.builder().accounts(List.of(userAccount, toAccount, selfDestructorAccount)).transaction(transaction()).build().run();
    }

    /**
     * The root contract CALL's the selfdestructor thrice, each time providing him with new balance.
     */
    @ParameterizedTest
    @EnumSource(Type.class)
    public void sameAccountSelfDestructsThrice(Type type) {

        appendCall(toAccountCode, OpCode.CALL, 100_000, Type.selfDestructorAddress, 12, 0, 4, 0, 0);
        appendCall(toAccountCode, OpCode.CALL, 100_000, Type.selfDestructorAddress, 19, 0, 3, 0, 0);
        appendCall(toAccountCode, OpCode.CALL, 100_000, Type.selfDestructorAddress, 26, 0, 2, 0, 0);

        run(type);
    }

    @ParameterizedTest
    @EnumSource(Type.class)
    public void sameAccountSelfDestructsThriceReverted(Type type) {

        appendCall(toAccountCode, OpCode.CALL, 100_000, Type.selfDestructorAddress, 12, 0, 4, 0, 0);
        appendCall(toAccountCode, OpCode.CALL, 100_000, Type.selfDestructorAddress, 19, 0, 3, 0, 0);
        appendCall(toAccountCode, OpCode.CALL, 100_000, Type.selfDestructorAddress, 26, 0, 2, 0, 0);
        appendRevert(toAccountCode, 0 ,0);

        selfDestructorAccount = basicSelfDestructor(type);
        buildToAccount();
        ToyExecutionEnvironmentV2.builder().accounts(List.of(userAccount, toAccount, selfDestructorAccount)).build().run();
    }

    /**
     * DELEGATECALL induces SELFDESTRUCT in the caller. A second DELEGATECALL does it again.
     *
     * <p> The second SELFDESTRUCT should go through due to DELEGATECALL not transferring value.
     */
    @ParameterizedTest
    @EnumSource(Type.class)
    public void calleeInducesSelfDestructInCallerViaDelegateCall(Type type) {

        appendCall(toAccountCode, OpCode.DELEGATECALL, 100_000, Type.selfDestructorAddress, 12, 0, 4, 0, 0);
        appendCall(toAccountCode, OpCode.DELEGATECALL, 100_000, Type.selfDestructorAddress, 19, 0, 3, 0, 0);

        run(type);
    }

    @ParameterizedTest
    @EnumSource(Type.class)
    public void calleeInducesSelfDestructInCallerViaDelegateCallReverted(Type type) {
        
        appendCall(toAccountCode, OpCode.DELEGATECALL, 100_000, Type.selfDestructorAddress, 12, 0, 4, 0, 0);
        appendCall(toAccountCode, OpCode.DELEGATECALL, 100_000, Type.selfDestructorAddress, 19, 0, 3, 0, 0);
        appendRevert(toAccountCode, 0 ,0);

        run(type);
    }

    /**
     * The second call should abort due to not having any funds left.
     */
    @Test
    public void calleeInducesSelfDestructInCallerViaCallCode() {
        appendCall(toAccountCode, OpCode.CALLCODE, 100_000, Type.selfDestructorAddress, 12, 0, 4, 0, 0);
        appendCall(toAccountCode, OpCode.CALLCODE, 100_000, Type.selfDestructorAddress, 19, 0, 3, 0, 0);
    }
    @Test
    public void calleeInducesSelfDestructInCallerViaCallCodeReverted() {
        appendCall(toAccountCode, OpCode.CALLCODE, 100_000, Type.selfDestructorAddress, 12, 0, 4, 0, 0);
        appendCall(toAccountCode, OpCode.CALLCODE, 100_000, Type.selfDestructorAddress, 19, 0, 3, 0, 0);
        appendRevert(toAccountCode, 0 ,0);
    }

    /**
     * DELEGATECALL into account that induces SELFDESTRUCT in caller followed by the caller
     * CALL'ing the callee again inducing SELFDESTRUCT in callee this time.
     *
     * <p> Both reverted and unreverted versions</p>
     */
    @Test
    public void callerThenCalleeSelfDestruct() {
        appendCall(toAccountCode, OpCode.DELEGATECALL, 100_000, Type.selfDestructorAddress, 12, 0, 4, 0, 0);
        appendCall(toAccountCode, OpCode.CALL, 100_000, Type.selfDestructorAddress, 0, 0, 3, 0, 0);
    }
    @Test
    public void callerThenCalleeSelfDestructReverted() {
        appendCall(toAccountCode, OpCode.DELEGATECALL, 100_000, Type.selfDestructorAddress, 12, 0, 4, 0, 0);
        appendCall(toAccountCode, OpCode.CALL, 100_000, Type.selfDestructorAddress, 0, 0, 3, 0, 0);
    }

    /**
     * CALL into account which SELFDESTRUCT's followed by the caller
     * DELEGATECALL'ing the callee again inducing SELFDESTRUCT in caller this time.
     *
     * <p> Both reverted and unreverted versions</p>
     */
    @Test
    public void calleeThenCallerSelfDestruct() {
        appendCall(toAccountCode, OpCode.CALL, 100_000, Type.selfDestructorAddress, 25, 0, 3, 0, 0);
        appendCall(toAccountCode, OpCode.DELEGATECALL, 100_000, Type.selfDestructorAddress, 12, 0, 4, 0, 0);
    }
    @Test
    public void calleeThenCallerSelfDestructReverted() {
        appendCall(toAccountCode, OpCode.CALL, 100_000, Type.selfDestructorAddress, 25, 0, 3, 0, 0);
        appendCall(toAccountCode, OpCode.DELEGATECALL, 100_000, Type.selfDestructorAddress, 12, 0, 4, 0, 0);
        appendRevert(toAccountCode, 0 ,0);
    }
}
