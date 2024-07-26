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
package net.consensys.linea.zktracer.module.hub.section.call.precompileSubsection;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.defer.PostTransactionDefer;
import net.consensys.linea.zktracer.module.hub.defer.ReEnterContextDefer;
import net.consensys.linea.zktracer.module.hub.fragment.TraceFragment;
import net.consensys.linea.zktracer.module.hub.section.call.CallSection;
import net.consensys.linea.zktracer.types.MemorySpan;

@RequiredArgsConstructor
@Accessors(fluent = true)
public abstract class PrecompileSubsection implements ReEnterContextDefer, PostTransactionDefer {
  @Getter List<TraceFragment> fragments;
  /* The input data for the precompile */
  final MemorySpan callDataMemorySpan;

  /* Where the caller wants the precompile return data to be stored */
  final MemorySpan parentReturnDataTarget;

  /* Leftover gas of the caller */
  final long callerGas;

  /* Available gas of the callee */
  final long calleeGas;

  /* The intrinsic cost of the precompile */
  long precompileCost;

  /**
   * Default creator specifying the max number of rows the precompile processing subsection can
   * contain.
   */
  public PrecompileSubsection(final Hub hub, final CallSection callSection) {
    fragments = new ArrayList<>(maxNumberOfLines());
    callDataMemorySpan = hub.currentFrame().callDataInfo().memorySpan();
    parentReturnDataTarget = hub.currentFrame().parentReturnDataTarget();
    callerGas = hub.callStack().parent().frame().getRemainingGas();
    calleeGas = hub.messageFrame().getRemainingGas();
  }

  private long returnGas() {
    return calleeGas - precompileCost;
  }

  protected abstract short maxNumberOfLines();
}
