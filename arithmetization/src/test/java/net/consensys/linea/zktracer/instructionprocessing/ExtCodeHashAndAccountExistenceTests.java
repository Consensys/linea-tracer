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
package net.consensys.linea.zktracer.instructionprocessing;

import static net.consensys.linea.zktracer.opcode.OpCode.*;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import org.apache.tuweni.bytes.Bytes;
import org.junit.jupiter.api.Test;

public class ExtCodeHashAndAccountExistenceTests {
  /**
   * EXTCODEHASH targets a precompile (which is DEAD initially) CALL the same precompile
   * transferring 1 Wei in the process. EXTCODEHASH targets the same precompile (which now isn't
   * DEAD anymore)
   */
  @Test
  void extcodehashForPrecompileBeforeAndAfterTransfer() {
    final Bytes bytecode =
        BytecodeCompiler.newProgram()
            .push(1)
            .op(DUP1)
            .op(EXTCODEHASH) // will return 0
            .op(POP)
            .op(EXTCODESIZE) // will return 0
            /* next phase */
            .push(0)
            .push(0)
            .push(0)
            .push(0)
            .push(1) // value
            .push(1) // address
            .push(3000) // gas for ECRECOVER
            .op(CALL)
            .push(1)
            .op(DUP1)
            .op(EXTCODEHASH) // will return KECCAK(())
            .op(POP)
            .op(EXTCODESIZE) // will return 0
            .compile();
    BytecodeRunner.of(bytecode).run();
  }

  /** same as above with ECRECOVER swapped for some random address (nice!) */
  @Test
  void extcodehashForRandomAddressBeforeAndAfterTransfer() {
    final Bytes bytecode =
        BytecodeCompiler.newProgram()
            .push(69)
            .op(DUP1)
            .op(EXTCODEHASH) // will return 0
            .op(POP)
            .op(EXTCODESIZE) // will return 0
            /* next phase */
            .push(0)
            .push(0)
            .push(0)
            .push(0)
            .push(1) // value
            .push(69) // address
            .push(0) // gas: none required, immediate STOP
            .op(CALL)
            .push(69)
            .op(DUP1)
            .op(EXTCODEHASH) // will return KECCAK(())
            .op(POP)
            .op(EXTCODESIZE) // will return 0
            .compile();
    BytecodeRunner.of(bytecode).run();
  }

  /**
   * Deployment happens at 0x6ff1019c622e4641f86f4bb7232b7901b8d20db6 The first EXTCODECOPY returns
   * 0, the second one (during deployment) returns KECCAK(()) and the final one returns KECCAK(())
   * again (no deployment occurred)
   */
  @Test
  void extcodehashBeforeDuringAndAfterTrivialDeployment() {
    final Bytes bytecode =
        BytecodeCompiler.newProgram()
            .push("6ff1019c622e4641f86f4bb7232b7901b8d20db6")
            .op(DUP1)
            .op(EXTCODEHASH) // will return 0
            .op(POP)
            .op(EXTCODESIZE) // will return 0
            /* next phase */
            .push(0) // size
            .push(0) // offset
            .push(0) // value
            .op(CREATE)
            .op(DUP1)
            .op(EXTCODEHASH) // will return KECCAK(())
            .op(POP)
            .op(EXTCODESIZE) // will return 0
            .compile();
    BytecodeRunner.of(bytecode).run();
  }

  /**
   * Deployment happens at 0x6ff1019c622e4641f86f4bb7232b7901b8d20db6 The first EXTCODECOPY returns
   * 0, the second one (during deployment) returns KECCAK(()) and the final one returns KECCAK(())
   * again (no deployment occurred)
   */
  @Test
  void extcodehashBeforeDuringAndAfterDeploymentDeployingEmtpyByteCode() {
    final Bytes bytecode =
        BytecodeCompiler.newProgram()
            .push("6ff1019c622e4641f86f4bb7232b7901b8d20db6")
            .op(DUP1)
            .op(EXTCODEHASH) // will return 0
            .op(POP)
            .op(EXTCODESIZE) // will return 0
            /* next phase */
            .push("30803f3b00000000000000000000000000000000000000000000000000000000")
            .push(0)
            .op(MSTORE)
            .push(4) // size
            .push(0) // offset
            .push(0) // value
            .op(CREATE) // the EXTCODEHASH will return KECCAK(())
            .op(CREATE)
            .op(DUP1)
            .op(EXTCODEHASH) // will return KECCAK(())
            .op(POP)
            .op(EXTCODESIZE) // will return 0
            .compile();
    BytecodeRunner.of(bytecode).run();
  }

  /**
   * Deployment happens at 0x6ff1019c622e4641f86f4bb7232b7901b8d20db6 The first EXTCODECOPY returns
   * 0, the second one (during deployment) returns KECCAK(()) and the final one returns KECCAK(0x00)
   * ≡ 0xbc36789e7a1e281436464229828f817d6612f7b477d66591ff96a9e064bcc98a
   *
   * <p>The initialization code is ADDRESS DUP1 EXTCODEHASH EXTCODESIZE PUSH1 0x01 PUSH1 0x00 RETURN
   */
  @Test
  void extcodehashBeforeDuringAndAfterDeploymentDeployingSingleZeroByte() {
    final Bytes bytecode =
        BytecodeCompiler.newProgram()
            .push("6ff1019c622e4641f86f4bb7232b7901b8d20db6")
            .op(EXTCODEHASH) // will return 0
            .op(POP)
            /* next phase */
            .push("30803f3b60016000F30000000000000000000000000000000000000000000000")
            .push(0)
            .op(MSTORE)
            .push(9) // size
            .push(0) // offset
            .push(0) // value
            .op(CREATE) // during deployment EXTCODEHASH will return KECCAK(())
            .op(CREATE)
            .op(DUP1)
            .op(EXTCODEHASH) // deployment success leaves address on the stack; will return //
            // KECCAK(0x00)
            .op(POP)
            .op(EXTCODESIZE) // will return 1
            .compile();
    BytecodeRunner.of(bytecode).run();
  }

  @Test
  void extcodehashOfOneself() {
    final Bytes bytecode =
        BytecodeCompiler.newProgram()
            .op(ADDRESS)
            .op(DUP1)
            .op(EXTCODEHASH) // will ???
            .op(POP)
            .op(EXTCODESIZE) // will return 6
            .op(JUMPDEST) //
            .compile();
    BytecodeRunner.of(bytecode).run();
  }
}
