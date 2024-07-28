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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.defer.ContextExitDefer;
import net.consensys.linea.zktracer.module.hub.defer.ContextReEntryDefer;
import net.consensys.linea.zktracer.module.hub.defer.PostRollbackDefer;
import net.consensys.linea.zktracer.module.hub.defer.PostTransactionDefer;
import net.consensys.linea.zktracer.module.hub.fragment.TraceFragment;
import net.consensys.linea.zktracer.module.hub.fragment.scenario.PrecompileScenarioFragment;
import net.consensys.linea.zktracer.module.hub.section.call.CallSection;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import net.consensys.linea.zktracer.types.MemorySpan;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.worldstate.WorldView;

/** Note: {@link PrecompileSubsection}'s are created at child context entry by the call section */
@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public class PrecompileSubsection
    implements ContextExitDefer, ContextReEntryDefer, PostRollbackDefer, PostTransactionDefer {

  public final CallSection callSection;

  /** List of fragments of the precompile specific subsection */
  private final List<TraceFragment> fragments;

  /** The (potentially empty) call data of the precompile call */
  final Bytes callData;

  /** The input data for the precompile */
  private final MemorySpan callDataMemorySpan;

  /** Where the caller wants the precompile return data to be stored */
  private final MemorySpan parentReturnDataTarget;

  /** The (potentially empty) return data of the precompile call */
  @Setter Bytes returnData;

  /** Leftover gas of the caller */
  final long callerGas;

  /** Available gas of the callee */
  final long calleeGas;

  /** The gas to return to the caller context */
  long returnGas;

  boolean successBit;

  final PrecompileScenarioFragment precompileScenarioFragment;

  /** A snapshot of the caller's memory before the execution of the precompile */
  final Bytes callerMemorySnapshot;

  /**
   * Default creator specifying the max number of rows the precompile processing subsection can
   * contain.
   */
  public PrecompileSubsection(final Hub hub, final CallSection callSection) {
    this.callSection = callSection;
    fragments = new ArrayList<>(maxNumberOfLines());
    callDataMemorySpan = hub.currentFrame().callDataInfo().memorySpan();
    callData = hub.messageFrame().getInputData();
    parentReturnDataTarget = hub.currentFrame().parentReturnDataTarget();
    callerGas = hub.callStack().parent().frame().getRemainingGas();
    calleeGas = hub.messageFrame().getRemainingGas();

    final MessageFrame callerFrame = hub.callStack().parent().frame();
    callerMemorySnapshot = extractContiguousLimbsFromMemory(callerFrame, callDataMemorySpan);

    precompileScenarioFragment =
        new PrecompileScenarioFragment(this, PRC_UNDEFINED_SCENARIO, PRC_UNDEFINED);
    fragments.add(precompileScenarioFragment);
  }

  protected short maxNumberOfLines() {
    return 0;
  }
  ;

  public void resolveUponExitingContext(Hub hub, CallFrame frame) {
    returnGas = frame.frame().getRemainingGas();
  }

  @Override
  public void resolveAtContextReEntry(Hub hub, CallFrame frame) {
    successBit = bytesToBoolean(hub.messageFrame().getStackItem(0));
    returnData = frame.frame().getReturnData();

    if (successBit) {
      hub.defers().scheduleForPostRollback(this, frame);
      precompileScenarioFragment.setScenario(PRC_SUCCESS_WONT_REVERT);
    }
  }

  @Override
  public void resolvePostRollback(Hub hub, MessageFrame messageFrame, CallFrame callFrame) {

    precompileScenarioFragment.setScenario(PRC_SUCCESS_WILL_REVERT);
  }

  @Override
  public void resolvePostTransaction(
      Hub hub, WorldView state, Transaction tx, boolean isSuccessful) {}
}
