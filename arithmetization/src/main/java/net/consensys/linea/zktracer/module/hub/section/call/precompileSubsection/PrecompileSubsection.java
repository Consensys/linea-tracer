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

import static net.consensys.linea.zktracer.module.hub.fragment.scenario.PrecompileScenarioFragment.PrecompileFlag.*;
import static net.consensys.linea.zktracer.module.hub.fragment.scenario.PrecompileScenarioFragment.PrecompileScenario.*;
import static net.consensys.linea.zktracer.runtime.callstack.CallFrame.extractContiguousLimbsFromMemory;
import static net.consensys.linea.zktracer.types.Conversions.bytesToBoolean;
import static net.consensys.linea.zktracer.types.Utils.rightPadTo;

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
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import net.consensys.linea.zktracer.types.MemorySpan;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.internal.Words;

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
  public final List<TraceFragment> fragments;

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

  /** The boolean pushed onto the caller's stack when it resumes execution */
  boolean callSuccess;

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

    final PrecompileScenarioFragment.PrecompileFlag precompileFlag =
        addressToPrecompileFlag(callSection.precompileAddress.orElseThrow());

    precompileScenarioFragment =
        new PrecompileScenarioFragment(this, PRC_SUCCESS_WONT_REVERT, precompileFlag);
    fragments.add(precompileScenarioFragment);

    firstImcFragment = ImcFragment.empty(hub);
    fragments.add(firstImcFragment);

    final OpCode opCode = hub.opCode();
    final long offset =
        Words.clampedToLong(
            opCode.callCanTransferValue()
                ? hub.messageFrame().getStackItem(3)
                : hub.messageFrame().getStackItem(2));
    final long length =
        Words.clampedToLong(
            opCode.callCanTransferValue()
                ? hub.messageFrame().getStackItem(4)
                : hub.messageFrame().getStackItem(3));
    callDataMemorySpan = new MemorySpan(offset, length);
    callerMemorySnapshot =
        extractContiguousLimbsFromMemory(hub.currentFrame().frame(), callDataMemorySpan);
    final int lengthToExtract = (int) Math.min(length, callerMemorySnapshot.size() - offset);
    callData = rightPadTo(callerMemorySnapshot.slice((int) offset, lengthToExtract), (int) length);
  }

  protected short maxNumberOfLines() {
    return 0;
  }

  @Override
  public void resolveUponImmediateContextEntry(Hub hub) {
    // Sanity check
    Preconditions.checkArgument(
        callDataMemorySpan.equals(hub.currentFrame().callDataInfo().memorySpan()));
    Preconditions.checkArgument(callData.equals(hub.messageFrame().getInputData()));
    final MessageFrame callerFrame = hub.callStack().parent().frame();
    Preconditions.checkArgument(
        callerMemorySnapshot.equals(
            extractContiguousLimbsFromMemory(callerFrame, callDataMemorySpan)));

    callerGas = hub.callStack().parent().frame().getRemainingGas();
    calleeGas = hub.messageFrame().getRemainingGas();
    parentReturnDataTarget = hub.currentFrame().returnDataTargetInCaller();

    // TODO: @françois: I've added this here as I believe that we will want
    //  to resolve at context exit in all cases and my impression is that
    //  we weren't doing so currently.
    //  This actually raises the question if we should pre-emptively schedule
    //  everything for ContextExit as soon as it has been scheduled for
    //  ContextEntry ...
    hub.defers().scheduleForContextExit(this, hub.currentFrame().id());
  }

  public void resolveUponExitingContext(Hub hub, CallFrame callFrame) {
    returnGas = callFrame.frame().getRemainingGas();
  }

  @Override
  public void resolveAtContextReEntry(Hub hub, CallFrame frame) {
    callSuccess = bytesToBoolean(hub.messageFrame().getStackItem(0));
    returnData = frame.frame().getReturnData();

    if (callSuccess) {
      hub.defers().scheduleForPostRollback(this, frame);
    }
  }

  public void sanityCheck() {
    if (callSuccess) {
      Preconditions.checkArgument(precompileScenarioFragment.scenario.isSuccess());
    } else {
      Preconditions.checkArgument(precompileScenarioFragment.scenario.isFailure());
    }
  }

  @Override
  public void resolvePostRollback(Hub hub, MessageFrame messageFrame, CallFrame callFrame) {

    // only successful PRC calls should enter here
    Preconditions.checkArgument(precompileScenarioFragment.scenario() == PRC_SUCCESS_WONT_REVERT);

    precompileScenarioFragment.scenario(PRC_SUCCESS_WILL_REVERT);
  }

  public int exoModuleOperationId() {
    return this.callSection.hubStamp() + 1;
  }

  public int returnDataContextNumber() {
    return exoModuleOperationId();
  }

  public PrecompileScenarioFragment.PrecompileFlag flag() {
    return precompileScenarioFragment.flag;
  }

  public void setScenario(PrecompileScenarioFragment.PrecompileScenario scenario) {
    this.precompileScenarioFragment.scenario(scenario);
  }
}
