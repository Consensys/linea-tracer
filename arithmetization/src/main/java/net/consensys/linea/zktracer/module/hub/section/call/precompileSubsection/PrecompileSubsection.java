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

import static com.google.common.base.Preconditions.*;
import static net.consensys.linea.zktracer.module.hub.Hub.newIdentifierFromStamp;
import static net.consensys.linea.zktracer.module.hub.fragment.scenario.PrecompileScenarioFragment.PrecompileFlag.*;
import static net.consensys.linea.zktracer.module.hub.fragment.scenario.PrecompileScenarioFragment.PrecompileScenario.*;
import static net.consensys.linea.zktracer.types.Conversions.bytesToBoolean;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.defer.*;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.TraceFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.scenario.PrecompileScenarioFragment;
import net.consensys.linea.zktracer.module.hub.section.call.CallSection;
import net.consensys.linea.zktracer.runtime.callstack.CallDataInfo;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import net.consensys.linea.zktracer.runtime.callstack.ReturnDataInfo;
import net.consensys.linea.zktracer.types.MemorySpan;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.evm.frame.MessageFrame;

/** Note: {@link PrecompileSubsection}'s are created at child context entry by the call section */
@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public class PrecompileSubsection
    implements ContextEntryDefer, ContextExitDefer, ContextReEntryDefer, PostRollbackDefer {

  /** Contains among others the call data */
  public final CallSection callSection;

  /** List of fragments of the precompile specific subsection */
  public final List<TraceFragment> fragments;

  /** ReturnDataInfo produced by the precompile */
  public ReturnDataInfo returnDataInfo;

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

    final MessageFrame messageFrame = hub.messageFrame();

    hub.defers().scheduleForContextEntry(this); // gas & input data, ...
    hub.defers().scheduleForContextExit(this, hub.callStack().futureId());
    hub.defers().scheduleForContextReEntry(this, hub.currentFrame()); // success bit & return data

    final PrecompileScenarioFragment.PrecompileFlag precompileFlag =
        addressToPrecompileFlag(callSection.precompileAddress.orElseThrow());

    precompileScenarioFragment =
        new PrecompileScenarioFragment(this, PRC_SUCCESS_WONT_REVERT, precompileFlag);
    fragments.add(precompileScenarioFragment);

    firstImcFragment = ImcFragment.empty(hub);
    fragments.add(firstImcFragment);
  }

  protected short maxNumberOfLines() {
    return 0;
  }

  @Override
  public void resolveUponContextEntry(Hub hub) {
    // Sanity check
    checkState(getCallDataSpan().equals(hub.currentFrame().callDataInfo().memorySpan()));
    checkState(getCallData().equals(hub.messageFrame().getInputData()));
    checkState(callSection.getReturnAtMemorySpan().equals(hub.currentFrame().returnDataTargetInCaller()));

    callerGas = hub.callStack().parentFrame().frame().getRemainingGas();
    calleeGas = hub.messageFrame().getRemainingGas();
  }

  public void resolveUponContextExit(Hub hub, CallFrame callFrame) {
    returnGas = callFrame.frame().getRemainingGas();
  }

  @Override
  public void resolveAtContextReEntry(Hub hub, CallFrame callFrame) {
    callSuccess = bytesToBoolean(callFrame.frame().getStackItem(0));
    Bytes returnData = callFrame.frame().getReturnData();

    // TODO: from @Olivier to @Olivier: the 513 will (intentionally) blow up,
    //  it must be replaced with mbs.
    final int returnerCn = exoModuleOperationId();
    ReturnDataInfo returnDataInfo =
            (precompileScenarioFragment.flag == PRC_MODEXP)
                    ? ReturnDataInfo.forModExp(returnData, 513, returnerCn)
                    : ReturnDataInfo.standard(returnData, returnerCn);

    callFrame.returnDataInfo(returnDataInfo);

    if (callSuccess) {
      hub.defers().scheduleForPostRollback(this, callFrame);
      callSection.setFinalContextFragment(
          ContextFragment.updateReturnData(hub, returnDataInfo));
    } else {
      callSection.setFinalContextFragment(ContextFragment.nonExecutionProvidesEmptyReturnData(hub));
    }
  }

  public void sanityCheck() {
    if (callSuccess) {
      checkArgument(precompileScenarioFragment.scenario.isSuccess());
    } else {
      checkArgument(precompileScenarioFragment.scenario.isFailure());
    }
  }

  @Override
  public void resolveUponRollback(Hub hub, MessageFrame messageFrame, CallFrame callFrame) {

    // only successful PRC calls should enter here
    checkArgument(precompileScenarioFragment.scenario() == PRC_SUCCESS_WONT_REVERT);

    precompileScenarioFragment.scenario(PRC_SUCCESS_WILL_REVERT);
  }

  public int exoModuleOperationId() {
    return newIdentifierFromStamp(callSection.hubStamp());
  }

  public int getReturnDataContextNumber() {
    return (int) returnDataInfo.getReturnDataContextNumber();
  }

  public PrecompileScenarioFragment.PrecompileFlag flag() {
    return precompileScenarioFragment.flag;
  }

  public void setScenario(PrecompileScenarioFragment.PrecompileScenario scenario) {
    precompileScenarioFragment.scenario(scenario);
  }

  public CallDataInfo getCallDataInfo() {
    return callSection.getCallDataInfo();
  }

  public Bytes getCallData() {
    return getCallDataInfo().data();
  }

  public MemorySpan getCallDataSpan() {
    return getCallDataInfo().memorySpan();
  }

  public Bytes getReturnData() {
    return returnDataInfo.getData();
  }

  public long cdo() {
    return callSection.getCallDataInfo().cdo();
  }

  public long cds() {
    return callSection.getCallDataInfo().cds();
  }

  public long rao() {
    return callSection.getReturnAtMemorySpan().offset();
  }

  public long rac() {
    return callSection.getReturnAtMemorySpan().length();
  }

  public long rdo() {
    return returnDataInfo().rdo();
  }

  public long rds() {
    return returnDataInfo().rds();
  }
}
