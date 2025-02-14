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
package net.consensys.linea.zktracer.instructionprocessing.callTests.prc.ecmul;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.zktracer.instructionprocessing.callTests.prc.GasParameter;
import net.consensys.linea.zktracer.instructionprocessing.callTests.prc.ReturnAtParameter;
import net.consensys.linea.zktracer.opcode.OpCode;

import static net.consensys.linea.zktracer.instructionprocessing.callTests.prc.CodeExecutionMethods.codeHolder1;
import static net.consensys.linea.zktracer.instructionprocessing.callTests.prc.CodeExecutionMethods.codeHolder2;

public class CallParameters {
  public final OpCode call;
  public final GasParameter gas;
  public final MemoryContentsParameter memoryContent;
  public final CallDataSizeParameter cds;
  public final ReturnAtParameter returnAt;
  public final boolean willRevert;

  public CallParameters(
      OpCode call,
      GasParameter gas,
      MemoryContentsParameter memoryContent,
      CallDataSizeParameter cds,
      ReturnAtParameter returnAt,
      boolean willRevert) {
    this.call = call;
    this.gas = gas;
    this.memoryContent = memoryContent;
    this.cds = cds;
    this.returnAt = returnAt;
    this.willRevert = willRevert;
  }

  public void switchVariants() {
    memoryContent.switchVariants();
  }

  public void setCodeOfHolderAccounts() {

    BytecodeCompiler code1 = this.memoryContent.memoryContents();
    this.switchVariants();

    BytecodeCompiler code2 = this.memoryContent.memoryContents();

    codeHolder1.code(code1.compile());
    codeHolder2.code(code2.compile());
  }
}
