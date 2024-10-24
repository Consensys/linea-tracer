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

import static net.consensys.linea.zktracer.module.hub.signals.TracedException.STATIC_FAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.testing.ToyAccount;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Wei;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class StaticExceptionTest {

  @ParameterizedTest
  @ValueSource(ints = {0, 1})
  void staticExceptionDueToCallWithNonZeroValueTest(int value) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();
    program
        .push(0) // return data size
        .push(0) // return data offset
        .push(0) // call data size
        .push(0) // call data offset
        .push("ca11ee") // address
        .push(1000) // gas
        .op(OpCode.STATICCALL);

    BytecodeCompiler calleeProgram = BytecodeCompiler.newProgram();
    calleeProgram
        .push(0) // return data size
        .push(0) // return data offset
        .push(0) // call data size
        .push(0) // call data offset
        .push(value) // value
        .push("ca11ee") // address
        .push(1000) // gas
        .op(OpCode.CALL);

    final ToyAccount calleeAccount =
        ToyAccount.builder()
            .balance(Wei.fromEth(1))
            .nonce(10)
            .address(Address.fromHexString("ca11ee"))
            .code(calleeProgram.compile())
            .build();

    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());

    bytecodeRunner.run(List.of(calleeAccount));

    if (value != 0) {
      assertEquals(
          STATIC_FAULT,
          bytecodeRunner.getHub().previousTraceSection(2).commonValues.tracedException());
    }
  }

  @Test
  void staticExceptionDueToSStoreTest() {
    BytecodeCompiler program = BytecodeCompiler.newProgram();
    program
        .push(0) // return data size
        .push(0) // return data offset
        .push(0) // call data size
        .push(0) // call data offset
        .push("ca11ee") // address
        .push(1000) // gas
        .op(OpCode.STATICCALL);

    BytecodeCompiler calleeProgram = BytecodeCompiler.newProgram();
    calleeProgram.push(0).push(0).op(OpCode.SSTORE);

    final ToyAccount calleeAccount =
        ToyAccount.builder()
            .balance(Wei.fromEth(1))
            .nonce(10)
            .address(Address.fromHexString("ca11ee"))
            .code(calleeProgram.compile())
            .build();

    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());

    bytecodeRunner.run(List.of(calleeAccount));

    assertEquals(
        STATIC_FAULT,
        bytecodeRunner.getHub().previousTraceSection(2).commonValues.tracedException());
  }

  @Test
  void staticExceptionDueToSelfDestructTest() {
    BytecodeCompiler program = BytecodeCompiler.newProgram();
    program
        .push(0) // return data size
        .push(0) // return data offset
        .push(0) // call data size
        .push(0) // call data offset
        .push("ca11ee") // address
        .push(1000) // gas
        .op(OpCode.STATICCALL);

    BytecodeCompiler calleeProgram = BytecodeCompiler.newProgram();
    calleeProgram.push(0).op(OpCode.SELFDESTRUCT);

    final ToyAccount calleeAccount =
        ToyAccount.builder()
            .balance(Wei.fromEth(1))
            .nonce(10)
            .address(Address.fromHexString("ca11ee"))
            .code(calleeProgram.compile())
            .build();

    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());

    bytecodeRunner.run(List.of(calleeAccount));

    assertEquals(
        STATIC_FAULT,
        bytecodeRunner.getHub().previousTraceSection(2).commonValues.tracedException());
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 1, 2, 3, 4})
  void staticExceptionDueToLogTest(int numberOfTopics) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();
    program
        .push(0) // return data size
        .push(0) // return data offset
        .push(0) // call data size
        .push(0) // call data offset
        .push("ca11ee") // address
        .push(1000) // gas
        .op(OpCode.STATICCALL);

    BytecodeCompiler calleeProgram = BytecodeCompiler.newProgram();
    for (int i = 0; i < numberOfTopics; i++) {
      calleeProgram.push(0);
    }
    calleeProgram.push(0).push(0).op(OpCode.of(OpCode.LOG0.getData().value() + numberOfTopics));

    final ToyAccount calleeAccount =
        ToyAccount.builder()
            .balance(Wei.fromEth(1))
            .nonce(10)
            .address(Address.fromHexString("ca11ee"))
            .code(calleeProgram.compile())
            .build();

    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());

    bytecodeRunner.run(List.of(calleeAccount));

    assertEquals(
        STATIC_FAULT,
        bytecodeRunner.getHub().previousTraceSection(2).commonValues.tracedException());
  }

  @ParameterizedTest
  @ValueSource(strings = {"CREATE", "CREATE2"})
  void staticExceptionDueToCreateTest(String opCodeName) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();
    program
        .push(0) // return data size
        .push(0) // return data offset
        .push(0) // call data size
        .push(0) // call data offset
        .push("ca11ee") // address
        .push(1000) // gas
        .op(OpCode.STATICCALL);

    BytecodeCompiler calleeProgram = BytecodeCompiler.newProgram();
    if (opCodeName.equals("CREATE2")) {
      calleeProgram.push(0);
    }
    calleeProgram
        .push(0)
        .push(0)
        .push(0)
        .op(opCodeName.equals("CREATE") ? OpCode.CREATE : OpCode.CREATE2);

    final ToyAccount calleeAccount =
        ToyAccount.builder()
            .balance(Wei.fromEth(1))
            .nonce(10)
            .address(Address.fromHexString("ca11ee"))
            .code(calleeProgram.compile())
            .build();

    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());

    bytecodeRunner.run(List.of(calleeAccount));

    assertEquals(
        STATIC_FAULT,
        bytecodeRunner.getHub().previousTraceSection(2).commonValues.tracedException());
  }
}
