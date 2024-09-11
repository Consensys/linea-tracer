/*
 * Copyright ConsenSys Inc.
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

package net.consensys.linea.zktracer.module.hub.section;

import static com.google.common.base.Preconditions.checkArgument;
import static net.consensys.linea.zktracer.opcode.InstructionFamily.INVALID;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import net.consensys.linea.zktracer.module.hub.signals.TracedException;
import net.consensys.linea.zktracer.opcode.OpCode;

public class EarlyExceptionSection extends TraceSection {
  public EarlyExceptionSection(Hub hub) {
    super(hub, (short) (hub.opCode().getData().stackSettings().twoLineInstruction() ? 2 : 1));

    this.addStack(hub);

    final short exceptions = hub.pch().exceptions();

    final OpCode opCode = hub.opCode();
    if (Exceptions.stackUnderflow(exceptions)) {
      checkArgument(opCode.mayTriggerStackUnderflow());
      commonValues.setTracedException(TracedException.STACK_UNDERFLOW);
      return;
    }

    if (Exceptions.stackOverflow(exceptions)) {
      checkArgument(opCode.mayTriggerStackOverflow());
      commonValues.setTracedException(TracedException.STACK_OVERFLOW);
      return;
    }

    if (hub.opCode().getData().instructionFamily() == INVALID) {
      checkArgument(Exceptions.invalidOpcode(exceptions));
      commonValues.setTracedException(TracedException.INVALID_OPCODE);
    }
  }
}
