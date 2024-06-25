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

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.module.hub.DeploymentExceptions;
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
/** A TraceSection gather the trace lines linked to a single operation */
public abstract class TraceSection {
  private final Hub hub;
  public final CommonFragmentValues commonValues;
  @Getter List<TraceFragment> fragments = new ArrayList<>();
  @Getter private int stackHeight = 0; // To delete
  @Getter private int stackHeightNew = 0; // To delete

  /** Count the stack lines */
  @Getter private int stackRowsCounter = 0;

  /** Count the non-stack lines */
  public int nonStackRowCounter = 0;

  public int numberOfNonStackRows = -1;
  @Getter @Setter private TxTrace parentTrace;

  /** Default creator for an empty section. */
  public TraceSection(Hub hub) {
    this.hub = hub;
    this.commonValues = new CommonFragmentValues(hub);
  }

  /// **
  // * A TraceLine stores the information required to generate a trace line.
  // *
  // * @param common data required to trace shared columns
  // * @param specific data required to trace perspective-specific columns
  // */
  // public record TraceLine(CommonFragment common, TraceFragment specific) {
  //  /**
  //   * Trace the line in the given trace builder.
  //   *
  //   * @param trace where to trace the line
  //   * @return the trace builder
  //   */
  //  public Trace trace(Trace trace, int stackInt, int stackHeight) {
  //    Preconditions.checkNotNull(common);
  //    Preconditions.checkNotNull(specific);
  //
  //    specific.trace(trace); // Warn: need to be called before common.trace to update stamps
  //    common.trace(trace, stackInt, stackHeight);
  //
  //    return trace.fillAndValidateRow();
  //  }
  // }

  /// **
  // * Fill the columns shared by all type of lines.
  // *
  // * @return a {@link CommonFragment} representing the shared columns
  // */
  // private CommonFragment traceCommon(Hub hub, CallFrame frame) {
  //  return CommonFragment.fromHub(
  //      hub, frame, this.stackRowsCounter == 2, this.nonStackRowCounter,
  // this.numberOfNonStackRows);
  // }

  /**
   * Add a fragment to the section.
   *
   * @param fragment the fragment to insert
   */
  public final void addFragment(TraceFragment fragment) {
    Preconditions.checkArgument(!(fragment instanceof CommonFragment));
    this.fragments.add(fragment);
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
  public final void addFragmentsWithoutStack(TraceFragment... fragments) {
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
  public final void addFragmentsAndStack(Hub hub, TraceFragment... fragments) {
    this.addStack(hub);
    this.addFragmentsWithoutStack(fragments);
  }

  /**
   * This method is called when the TraceSection is finished, to build required information
   * post-hoc.
   */
  public void seal() {
    commonValues.numberOfNonStackRows(
        (int) this.fragments.stream().filter(l -> !(l instanceof StackFragment)).count());
    commonValues.TLI(
        (int) this.fragments.stream().filter(l -> (l instanceof StackFragment)).count() == 2);
    commonValues.codeFragmentIndex(
        this.commonValues.hubProcessingPhase == HubProcessingPhase.TX_EXEC
            ? this.hub.getCfiByMetaData(
                this.commonValues.callFrame().byteCodeAddress(),
                this.commonValues.callFrame().codeDeploymentNumber(),
                this.commonValues.callFrame().isDeployment())
            : 0);
  }

  public final boolean hasReverted() {
    return this.commonValues.callFrame().hasReverted();
  }

  public final long refundDelta() {
    return this.commonValues.refundDelta();
  }

  /**
   * Update the stack fragments of the section with the provided {@link DeploymentExceptions}.
   *
   * @param contEx the computed exceptions
   */
  public void setContextExceptions(DeploymentExceptions contEx) {
    for (TraceFragment fragment : this.fragments) {
      if (fragment instanceof StackFragment) {
        ((StackFragment) fragment).contextExceptions(contEx);
      }
    }
  }

  private List<TraceFragment> makeStackFragments(final Hub hub, CallFrame f) {
    List<TraceFragment> r = new ArrayList<>(2);
    if (f.pending().lines().isEmpty()) {
      for (int i = 0; i < (f.opCodeData().stackSettings().twoLinesInstruction() ? 2 : 1); i++) {
        r.add(
            StackFragment.prepare(
                hub,
                f.stack().snapshot(),
                new StackLine().asStackOperations(),
                hub.pch().exceptions().snapshot(),
                hub.pch().abortingConditions().snapshot(),
                Hub.GAS_PROJECTOR.of(f.frame(), f.opCode()),
                f.isDeployment(),
                f.willRevert()));
      }
    } else {
      for (StackLine line : f.pending().lines()) {
        r.add(
            StackFragment.prepare(
                hub,
                f.stack().snapshot(),
                line.asStackOperations(),
                hub.pch().exceptions().snapshot(),
                hub.pch().abortingConditions().snapshot(),
                Hub.GAS_PROJECTOR.of(f.frame(), f.opCode()),
                f.isDeployment(),
                f.willRevert()));
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
}
