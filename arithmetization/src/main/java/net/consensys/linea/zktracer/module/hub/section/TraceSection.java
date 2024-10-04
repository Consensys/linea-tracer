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

import static com.google.common.base.Preconditions.*;
import static net.consensys.linea.zktracer.module.hub.HubProcessingPhase.TX_EXEC;
import static net.consensys.linea.zktracer.module.hub.HubProcessingPhase.TX_FINL;
import static net.consensys.linea.zktracer.module.hub.HubProcessingPhase.TX_INIT;
import static net.consensys.linea.zktracer.module.hub.HubProcessingPhase.TX_SKIP;
import static net.consensys.linea.zktracer.module.hub.HubProcessingPhase.TX_WARM;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.HubProcessingPhase;
import net.consensys.linea.zktracer.module.hub.Trace;
import net.consensys.linea.zktracer.module.hub.TxTrace;
import net.consensys.linea.zktracer.module.hub.fragment.StackFragment;
import net.consensys.linea.zktracer.module.hub.fragment.TraceFragment;
import net.consensys.linea.zktracer.module.hub.fragment.common.CommonFragment;
import net.consensys.linea.zktracer.module.hub.fragment.common.CommonFragmentValues;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import net.consensys.linea.zktracer.runtime.stack.StackLine;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.evm.internal.Words;

@Accessors(fluent = true)
/* A TraceSection gather the trace lines linked to a single operation */
public class TraceSection {
  private final Hub hub;
  public final CommonFragmentValues commonValues;
  @Getter List<TraceFragment> fragments;
  @Getter @Setter private TxTrace parentTrace;
  /* A link to the previous section */
  @Setter public TraceSection previousSection = null;
  /* A link to the next section */
  @Setter public TraceSection nextSection = null;

  /** Default creator specifying the max number of rows the section can contain. */
  public TraceSection(final Hub hub, final short maxNumberOfLines) {
    this.hub = hub;
    hub.state().stamps().incrementHubStamp();
    commonValues = new CommonFragmentValues(hub);
    fragments = new ArrayList<>(maxNumberOfLines);
    hub.addTraceSection(this);
  }

  /**
   * Add a fragment to the section.
   *
   * @param fragment the fragment to insert
   */
  public final void addFragment(TraceFragment fragment) {
    checkArgument(!(fragment instanceof CommonFragment));
    fragments.add(fragment);
  }

  /**
   * Add the fragments containing the stack lines.
   *
   * @param hub the execution context
   */
  public final void addStack(Hub hub) {
    for (var stackFragment : this.makeStackFragments(hub, hub.currentFrame())) {
      this.addFragment(stackFragment);
    }
  }

  /**
   * Add several fragments within this section for the specified fragments.
   *
   * @param fragments the fragments to add to the section
   */
  public final void addFragments(TraceFragment... fragments) {
    for (TraceFragment f : fragments) {
      this.addFragment(f);
    }
  }

  /**
   * Add several fragments within this section for the specified fragments.
   *
   * @param fragments the fragments to add to the section
   */
  public final void addFragments(List<TraceFragment> fragments) {
    for (TraceFragment f : fragments) {
      this.addFragment(f);
    }
  }

  /**
   * Insert Stack fragments related to the current state of the stack, then insert the provided
   * fragments in a single swoop.
   *
   * @param hub the execution context
   * @param fragments the fragments to insert
   */
  public final void addStackAndFragments(Hub hub, TraceFragment... fragments) {
    this.addStack(hub);
    this.addFragments(fragments);
  }

  /** This method is called at commit time, to build required information post-hoc. */
  public void seal() {
    final HubProcessingPhase currentPhase = commonValues.hubProcessingPhase;

    commonValues.numberOfNonStackRows(
        (int) fragments.stream().filter(l -> !(l instanceof StackFragment)).count());
    commonValues.TLI(
        (int) fragments.stream().filter(l -> (l instanceof StackFragment)).count() == 2);
    commonValues.codeFragmentIndex(
        currentPhase == TX_EXEC
            ? hub.getCfiByMetaData(
                commonValues.callFrame().byteCodeAddress(),
                commonValues.callFrame().byteCodeDeploymentNumber(),
                commonValues.callFrame().isDeployment())
            : 0);
    commonValues.contextNumberNew(computeContextNumberNew());

    commonValues.gasRefund(
        currentPhase == TX_SKIP || currentPhase == TX_WARM || currentPhase == TX_INIT
            ? 0
            : previousSection.commonValues.gasRefundNew);
    commonValues.gasRefundNew(commonValues.gasRefund + commonValues.refundDelta);

    /* If the logStamp hasn't been set (either by being first section of the tx, or by the LogSection), set it to the previous section logStamp */
    if (commonValues.logStamp == -1) {
      commonValues.logStamp(previousSection.commonValues.logStamp);
    }
  }

  private int computeContextNumberNew() {
    final HubProcessingPhase currentPhase = commonValues.hubProcessingPhase;
    if (currentPhase == TX_WARM || currentPhase == TX_FINL || currentPhase == TX_SKIP) {
      return 0;
    }
    return nextSection.commonValues.hubProcessingPhase == TX_EXEC
        ? nextSection.commonValues.callFrame().contextNumber()
        : 0;
  }

  private List<TraceFragment> makeStackFragments(final Hub hub, CallFrame f) {
    final List<TraceFragment> r = new ArrayList<>(2);
    if (f.pending().lines().isEmpty()) {
      for (int i = 0; i < (f.opCodeData().stackSettings().twoLineInstruction() ? 2 : 1); i++) {
        r.add(
            StackFragment.prepare(
                hub,
                f.stack().snapshot(),
                new StackLine().asStackOperations(),
                hub.pch().exceptions(),
                hub.pch().abortingConditions().snapshot(),
                Hub.GAS_PROJECTOR.of(f.frame(), f.opCode()),
                f.isDeployment(),
                f.willRevert(),
                commonValues));
      }
    } else {
      for (StackLine line : f.pending().lines()) {
        r.add(
            StackFragment.prepare(
                hub,
                f.stack().snapshot(),
                line.asStackOperations(),
                hub.pch().exceptions(),
                hub.pch().abortingConditions().snapshot(),
                Hub.GAS_PROJECTOR.of(f.frame(), f.opCode()),
                f.isDeployment(),
                f.willRevert(),
                commonValues));
      }
    }
    return r;
  }

  public void triggerHashInfo(Bytes hash) {
    for (TraceFragment fragment : this.fragments()) {
      if (fragment instanceof StackFragment) {
        ((StackFragment) fragment).hashInfoFlag = true;
        ((StackFragment) fragment).hash = hash;
      }
    }
  }

  public void triggerJumpDestinationVetting(Hub hub) {
    final int pcNew = Words.clampedToInt(hub.messageFrame().getStackItem(0));
    final boolean invalidJumpDestination = hub.messageFrame().getCode().isJumpDestInvalid(pcNew);

    for (TraceFragment fragment : this.fragments()) {
      if (fragment instanceof StackFragment) {
        ((StackFragment) fragment).jumpDestinationVettingRequired(true);
        ((StackFragment) fragment).validJumpDestination(invalidJumpDestination);
      }
    }
  }

  public void trace(Trace hubTrace) {
    int stackLineCounter = -1;
    int nonStackLineCounter = 0;

    for (TraceFragment specificFragment : fragments()) {
      if (specificFragment instanceof StackFragment) {
        stackLineCounter++;
      } else {
        nonStackLineCounter++;
      }

      specificFragment.trace(hubTrace);
      final CommonFragment commonFragment =
          new CommonFragment(commonValues, stackLineCounter, nonStackLineCounter);
      commonFragment.trace(hubTrace);
      hubTrace.fillAndValidateRow();
    }
  }

  public int hubStamp() {
    return commonValues.hubStamp;
  }

  public int revertStamp() {
    return commonValues.callFrame().revertStamp();
  }
}
