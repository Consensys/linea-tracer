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

import static net.consensys.linea.zktracer.module.hub.fragment.scenario.PrecompileScenarioFragment.PrecompileFlag.PRC_UNDEFINED;
import static net.consensys.linea.zktracer.module.hub.fragment.scenario.PrecompileScenarioFragment.PrecompileScenario.*;
import static net.consensys.linea.zktracer.runtime.callstack.CallFrame.extractContiguousLimbsFromMemory;
import static net.consensys.linea.zktracer.types.Conversions.bytesToBoolean;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.defer.*;
import net.consensys.linea.zktracer.module.hub.fragment.TraceFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.scenario.PrecompileScenarioFragment;
import net.consensys.linea.zktracer.module.hub.section.call.CallSection;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import net.consensys.linea.zktracer.types.MemorySpan;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.evm.frame.MessageFrame;

/** Note: {@link PrecompileSubsection}'s are created at child context entry by the call section */
@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public class PrecompileSubsection
    implements ImmediateContextEntryDefer,
        ContextExitDefer,
        ContextReEntryDefer,
        PostRollbackDefer {

  public final CallSection callSection;

  /** List of fragments of the precompile specific subsection */
  private final List<TraceFragment> fragments;

  /** The (potentially empty) call data of the precompile call */
  public Bytes callData;

  /** The input data for the precompile */
  public MemorySpan callDataMemorySpan;

  /** Where the caller wants the precompile return data to be stored */
  public MemorySpan parentReturnDataTarget;

  /** The (potentially empty) return data of the precompile call */
  @Setter public Bytes returnData;

  /** Leftover gas of the caller */
  long callerGas;

  /** Available gas of the callee */
  long calleeGas;

  /** The gas to return to the caller context */
  long returnGas;

  boolean successBit;

  public final PrecompileScenarioFragment precompileScenarioFragment;
  public final ImcFragment firstImcFragment;

  /** A snapshot of the caller's memory before the execution of the precompile */
  public Bytes callerMemorySnapshot;

  /**
   * Default creator specifying the max number of rows the precompile processing subsection can
   * contain.
   */
  public PrecompileSubsection(final Hub hub, final CallSection callSection) {
    this.callSection = callSection;
    fragments = new ArrayList<>(maxNumberOfLines());

    precompileScenarioFragment =
        new PrecompileScenarioFragment(this, PRC_SUCCESS_WONT_REVERT, PRC_UNDEFINED);
    fragments.add(precompileScenarioFragment);

    firstImcFragment = ImcFragment.empty(hub);
    fragments().add(firstImcFragment);
  }

  protected short maxNumberOfLines() {
    return 0;
  }

  @Override
  public void resolveUponEnteringChildContext(Hub hub) {
    callerGas = hub.callStack().parent().frame().getRemainingGas();
    calleeGas = hub.messageFrame().getRemainingGas();
    callDataMemorySpan = hub.currentFrame().callDataInfo().memorySpan();
    callData = hub.messageFrame().getInputData();
    parentReturnDataTarget = hub.currentFrame().returnDataTargetInCaller();

    final MessageFrame callerFrame = hub.callStack().parent().frame();
    callerMemorySnapshot = extractContiguousLimbsFromMemory(callerFrame, callDataMemorySpan);
  }

  public void resolveUponExitingContext(Hub hub, CallFrame callFrame) {
    returnGas = callFrame.frame().getRemainingGas();
  }

  @Override
  public void resolveAtContextReEntry(Hub hub, CallFrame frame) {
    successBit = bytesToBoolean(hub.messageFrame().getStackItem(0));
    returnData = frame.frame().getReturnData();

    if (successBit) {
      hub.defers().scheduleForPostRollback(this, frame);
    }
  }

  @Override
  public void resolvePostRollback(Hub hub, MessageFrame messageFrame, CallFrame callFrame) {

    // only successful PRC calls should enter here
    Preconditions.checkArgument(
        precompileScenarioFragment.getScenario() == PRC_SUCCESS_WONT_REVERT);

    precompileScenarioFragment.setScenario(PRC_SUCCESS_WILL_REVERT);
  }

  public int exoModuleOperationId() {
    return this.callSection.hubStamp() + 1;
  }

  public int returnDataContextNumber() {
    return exoModuleOperationId();
  }
}
