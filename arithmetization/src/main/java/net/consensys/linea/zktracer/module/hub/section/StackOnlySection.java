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

package net.consensys.linea.zktracer.module.hub.section;

import static net.consensys.linea.zktracer.module.hub.signals.Exceptions.OUT_OF_GAS_EXCEPTION;
import static net.consensys.linea.zktracer.module.hub.signals.Exceptions.STACK_OVERFLOW;
import static net.consensys.linea.zktracer.module.hub.signals.Exceptions.STACK_UNDERFLOW;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;

public class StackOnlySection extends TraceSection {
  public StackOnlySection(Hub hub) {
    super(hub, (short) (hub.opCode().getData().stackSettings().twoLineInstruction() ? 2 : 1));

    final short exceptions = hub.pch().exceptions();

    if (Exceptions.stackUnderflow(exceptions)) {
      commonValues.setTracedException(STACK_UNDERFLOW);
      return;
    }
    if (Exceptions.stackOverflow(exceptions)) {
      commonValues.setTracedException(STACK_OVERFLOW);
      return;
    }
    if (Exceptions.outOfGasException(exceptions)) {
      commonValues.setTracedException(OUT_OF_GAS_EXCEPTION);
      return;
    }
    this.addStack(hub);
  }
}
