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

package net.consensys.linea.zktracer.exceptions;

import static net.consensys.linea.zktracer.module.hub.signals.TracedException.RETURN_DATA_COPY_FAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import net.consensys.linea.UnitTestWatcher;
import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.testing.ToyAccount;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Wei;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(UnitTestWatcher.class)
public class MultiExceptionTest {

  @Test
  void RdcxAndOogxExceptionReturnDataCopy() {
    BytecodeCompiler programWithoutRdcx = BytecodeCompiler.newProgram();
    BytecodeCompiler program = BytecodeCompiler.newProgram();
    BytecodeCompiler programRdcx = BytecodeCompiler.newProgram();
    BytecodeCompiler postRdcxrogram = BytecodeCompiler.newProgram();

    final ToyAccount returnDataProviderAccount =
        ToyAccount.builder()
            .balance(Wei.fromEth(1))
            .nonce(10)
            .address(Address.fromHexString("c0de"))
            // Constructor that returns 32 FF
            .code(
                Bytes.fromHexString(
                    "7fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff60005260206000f3"))
            .build();

    programWithoutRdcx
        // 1. Execute static call
        .push(0) // byte size of return data
        .push(0) // retOffset
        .push(0) // byte size calldata
        .push(0) // argsOffset
        .push("c0de") // Address of 'return data provider' account
        .op(OpCode.GAS) // gas
        .op(OpCode.STATICCALL)
        // 2. Clean the stack
        .op(OpCode.POP)
        .op(OpCode.RETURNDATASIZE);

    program.concatenate(programWithoutRdcx);

    postRdcxrogram
        .push(0) // offset
        .push(65) // destoffset, trigger mem expansion
        .op(OpCode.RETURNDATACOPY);

    programRdcx
        // 4. Doubly exceptional return data copy
        .push(1)
        .op(OpCode.ADD); // size = RDS + 1, which will trigger the `returnDataCopyException`

    programWithoutRdcx.concatenate(postRdcxrogram);
    BytecodeRunner bytecodeRunnerWithoutRdcx = BytecodeRunner.of(programWithoutRdcx.compile());

    long gasCost = bytecodeRunnerWithoutRdcx.runOnlyForGasCost(List.of(returnDataProviderAccount));

    int cornerCase = -1;
    long gasCostWithRdcx =
        gasCost
            + 3 // Push
            + 3 // ADD
            + cornerCase; // trigger oogx

    program.concatenate(programRdcx);
    program.concatenate(postRdcxrogram);
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    bytecodeRunner.run(gasCostWithRdcx, List.of(returnDataProviderAccount));

    // Rdcx happens before Oogx
    assertEquals(
        RETURN_DATA_COPY_FAULT,
        bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
  }
}
