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

import static com.google.common.base.Preconditions.checkArgument;
import static net.consensys.linea.zktracer.module.hub.signals.TracedException.OUT_OF_GAS_EXCEPTION;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.exp.ExpCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.exp.ExplogExpCall;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;

public class ExpSection extends TraceSection {
  public ExpSection(Hub hub) {
    // 1 + 1     (stack, misc)
    // 1 + 1 + 1 (stack, misc, context)
    super(hub, Exceptions.none(hub.pch().exceptions()) ? (short) 3 : (short) 2);

    final ExpCall expCall = new ExplogExpCall();
    final ImcFragment miscFragment = ImcFragment.empty(hub).callExp(expCall);
    this.addStackAndFragments(hub, miscFragment);

    final short exceptions = hub.pch().exceptions();
    if (Exceptions.any(exceptions)) {
      checkArgument(Exceptions.outOfGasException(exceptions));
      commonValues.setTracedException(OUT_OF_GAS_EXCEPTION);
    }
  }
}
