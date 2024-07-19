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

package net.consensys.linea.zktracer.module.ecdata;

import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.testing.BytecodeCompiler;
import net.consensys.linea.zktracer.testing.BytecodeRunner;
import org.apache.tuweni.bytes.Bytes;
import org.junit.jupiter.api.Test;

public class EcPairingrTest {
  @Test
  void testEcPairingWithTrivialPairing() {
    BytecodeCompiler program =
        BytecodeCompiler.newProgram()
            .push(0x20) // retSize
            .push(0) // retOffset
            .push(192) // argSize
            .push(0) // argOffset
            .push(8) // address
            .push(Bytes.fromHexStringLenient("0xFFFFFFFF")) // gas
            .op(OpCode.STATICCALL);

    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    bytecodeRunner.run();
  }

  @Test
  void testEcPairingWithMinimalNonTrivialValidPairing() {
    // small point: (Ax,Ay)
    // large point: (BxRe + i*BxIm, ByRe + i*ByIm)
    BytecodeCompiler program =
        BytecodeCompiler.newProgram()
            // random point in C1
            .push("26d7d8759964ac70b4d5cdf698ad5f70da246752481ea37da637551a60a2a57f") // Ax
            .push("13991eda70bd3bd91c43e7c93f8f89c949bd271ba8f9f5a38bce7248f1f6056b") // Ay
            // random point in G2
            .push("13eb8555f322c3885846df8a505693a0012493a30c34196a529f964a684c0cb2") // BxIm
            .push("18335a998f3db61d3d0b63cd1371789a9a8a5ed4fb1a4adaa20ab9573251a9d0") // BxRe
            .push("20494259608bfb4cd04716ba62e1350ce90d00d8af00a5170f46f59ae72d060c") // ByIm
            .push("257a64fbc5a9cf9c3f7be349d09efa099305fe61f364c31c082ef6a81c815c1d") // ByRe
            .push(0x20) // retSize
            .push(0) // retOffset
            .push(192) // argSize
            .push(0) // argOffset
            .push(8) // address
            .push(Bytes.fromHexStringLenient("0xFFFFFFFF")) // gas
            .op(OpCode.STATICCALL);

    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    bytecodeRunner.run();
  }
}
