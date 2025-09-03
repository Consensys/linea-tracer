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

package net.consensys.linea.zktracer.module.blsdata;

import java.util.List;

import net.consensys.linea.UnitTestWatcher;
import net.consensys.linea.reporting.TracerTestBase;
import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.testing.ToyAccount;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Wei;
import org.hyperledger.besu.evm.precompile.KZGPointEvalPrecompiledContract;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(UnitTestWatcher.class)
public class PointEvaluationTest extends TracerTestBase {

  @BeforeAll
  static void setup() {
    // Initialize KZG native library before running tests
    KZGPointEvalPrecompiledContract.init();
  }

  @Test
  void testPointEvaluation() {
    BytecodeCompiler program = BytecodeCompiler.newProgram(testInfo);

    // TODO: extract method for that
    final Address codeOwnerAddress = Address.fromHexString("0xC0DE");
    final ToyAccount codeOwnerAccount =
        ToyAccount.builder()
            .balance(Wei.of(0))
            .nonce(1)
            .address(codeOwnerAddress)
            .code(
                Bytes.fromHexString(
                    "010657f37554c781402a22917dee2f75def7ab966d7b770905398eba3c444014"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "0000000000000000000000000000000000000000000000000000000000000000"
                        + "c00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                        + "c00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"))
            .build();

    /* source: https://github.com/ethereum/execution-spec-tests/blob/1983444bbe1a471886ef7c0e82253ffe2a4053e1/tests/cancun/eip4844_blobs/point_evaluation_vectors/go_kzg_4844_verify_kzg_proof.json#L312-L321
               and Ivo
    {
    "input": {
      "versioned_hash": "010657f37554c781402a22917dee2f75def7ab966d7b770905398eba3c444014",
      "z": "0000000000000000000000000000000000000000000000000000000000000000",
      "y": "0000000000000000000000000000000000000000000000000000000000000000",
       "commitment": "c00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
      "proof": "c00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
    },
    "output": true,
    "name": "verify_kzg_proof_case_correct_proof_c3d4322ec17fe7cd"
     }
     */

    // First place the parameters in memory
    // Copy to targetOffset the code of codeOwnerAccount
    program
        .push(codeOwnerAddress)
        .op(OpCode.EXTCODESIZE) // size
        .push(0) // offset
        .push(0) // targetOffset
        .push(codeOwnerAddress) // address
        .op(OpCode.EXTCODECOPY);

    // Do the call
    program
        .push(64) // retSize
        .push(256) // retOffset
        .push(192) // argSize
        .push(0) // argOffset
        .push(10) // address
        .push(Bytes.fromHexStringLenient("0xFFFFFFFF")) // gas
        .op(OpCode.STATICCALL);
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    bytecodeRunner.run(List.of(codeOwnerAccount), testInfo);
  }
}
