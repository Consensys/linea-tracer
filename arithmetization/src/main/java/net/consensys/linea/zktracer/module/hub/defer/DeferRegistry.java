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

package net.consensys.linea.zktracer.module.hub.defer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import net.consensys.linea.zktracer.runtime.callstack.CallStack;
import org.apache.commons.lang3.tuple.Pair;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.operation.Operation;
import org.hyperledger.besu.evm.worldstate.WorldView;

/**
 * Stores different categories of actions whose execution must be deferred later in the normal
 * transaction execution process.
 */
public class DeferRegistry {
  /** A list of actions deferred until the end of the current conflation execution */
  private final List<PostConflationDefer> postConflationDefers = new ArrayList<>();

  /** A list of actions deferred until the end of the current transaction */
  private final List<PostTransactionDefer> postTransactionDefers = new ArrayList<>();

  /** A list of actions deferred until the end of the current opcode execution */
  private final List<PostExecDefer> postExecDefers = new ArrayList<>();

  /** A list of actions deferred until the end of the current opcode execution */
  private final List<ReEnterContextDefer> reEntryDefers = new ArrayList<>();

  /** A list of actions deferred until the end of the current opcode execution */
  private final List<Pair<Integer, NextContextDefer>> contextReentry = new ArrayList<>();

  /**
   * A collection of actions whose execution is deferred to a hypothetical future rollback. This
   * collection maps a context to all actions that would have to be done if that execution context
   * were to be rolled back.
   */
  private final Map<CallFrame, List<PostRollbackDefer>> rollbackDefers = new HashMap<>();

  /** Schedule an action to be executed after the completion of the current opcode. */
  public void scheduleForContextReEntry(NextContextDefer defer, int frameId) {
    this.contextReentry.add(Pair.of(frameId, defer));
  }

  /** Schedule an action to be executed after the completion of the current opcode. */
  public void schedulePostExecution(PostExecDefer defer) {
    this.postExecDefers.add(defer);
  }

  /** Schedule an action to be executed at the end of the current transaction. */
  public void schedulePostTransaction(PostTransactionDefer defer) {
    this.postTransactionDefers.add(defer);
  }

  /** Schedule an action to be executed at the end of the current transaction. */
  public void schedulePostConflation(PostConflationDefer defer) {
    this.postConflationDefers.add(defer);
  }

  /** Schedule an action to be executed at the re-entry in the current context. */
  public void reEntry(ReEnterContextDefer defer) {
    this.reEntryDefers.add(defer);
  }

  public void scheduleForPostRollback(PostRollbackDefer defer, CallFrame callFrame) {
    this.rollbackDefers.get(callFrame).add(defer);
  }

  /**
   * Trigger the execution of the actions deferred to the next context.
   *
   * @param hub a {@link Hub} context
   * @param frame the new context {@link MessageFrame}
   */
  public void resolveWithNextContext(Hub hub, MessageFrame frame) {
    for (Pair<Integer, NextContextDefer> defer : this.contextReentry) {
      if (hub.currentFrame().parentFrame() == defer.getLeft()) {
        defer.getRight().resolveWithNextContext(hub, frame);
      }
    }
    // TODO: we clear everything ?!? not just the one that was just resolved ?
    this.contextReentry.clear();
  }

  /**
   * Trigger the execution of the actions deferred to the end of the transaction.
   *
   * @param hub the {@link Hub} context
   * @param world a {@link WorldView} on the state
   * @param tx the current {@link Transaction}
   */
  // TODO: should use the TransactionProcessingMetadata
  public void resolvePostTransaction(
      Hub hub, WorldView world, Transaction tx, boolean isSuccessful) {
    for (PostTransactionDefer defer : this.postTransactionDefers) {
      defer.resolvePostTransaction(hub, world, tx, isSuccessful);
    }
    this.postTransactionDefers.clear();
  }

  /**
   * Trigger the execution of the actions deferred to the end of the conflation.
   *
   * @param hub the {@link Hub} context
   */
  public void resolvePostConflation(Hub hub, WorldView world) {
    for (PostConflationDefer defer : this.postConflationDefers) {
      defer.resolvePostConflation(hub, world);
    }
    this.postConflationDefers.clear();
  }

  /**
   * Trigger the execution of the actions deferred to the end of the current instruction execution.
   *
   * @param hub the {@link Hub} context
   * @param frame the {@link MessageFrame} of the transaction
   * @param result the {@link Operation.OperationResult} of the transaction
   */
  public void resolvePostExecution(Hub hub, MessageFrame frame, Operation.OperationResult result) {
    for (PostExecDefer defer : this.postExecDefers) {
      defer.resolvePostExecution(hub, frame, result);
    }
    this.postExecDefers.clear();
  }

  /**
   * Trigger the execution of the actions deferred to the re-entry in the current context.
   *
   * @param hub the {@link Hub} context
   * @param frame the {@link MessageFrame} of the transaction
   */
  public void resolveAtReEntry(Hub hub, MessageFrame frame) {
    for (ReEnterContextDefer defer : this.reEntryDefers) {
      defer.resolveAtContextReEntry(hub, frame);
    }
    // TODO: make sure this is correct;
    //  it used to be this.postExecDefers.clear()
    //  ... obvious mistake ?
    this.reEntryDefers.clear();
    // TODO: how would us clearing the reentryDefers not
    //  fail in case you have e.g. A calls B calls C ?
    //  When we 're-enter' B after finishing with C wouldn't
    //  we try to resolve the re-entry business of A ? Which
    //  is too early ... ?
    //  Note: this is used for CREATE's, specifically for when
    //  deployment terminates and we re-enter the creator context ...
    //  But what if we have nested CREATE's ?
    //  A creates B, and during deployment B creates C ... ?!
  }

  /**
   * Should be invoked when precisely after a rollback was acted upon in terms of rolling back
   * modifications to WORLD STATE and ACCRUED STATE but the caller (or creator), if present, hasn't
   * resumed execution yet, and if there isn't one because this is the root context of the
   * transaction, we haven't entered the "transaction finalization phase."
   *
   * <p>Note that the "current messageFrame" is expected to STILL BE the messageFrame responsible
   * for the rollback.
   */
  public void resolvePostRollback(
      final Hub hub, final MessageFrame messageFrame, CallFrame currentCallFrame) {

    // roll back current context
    for (PostRollbackDefer defer : hub.defers().rollbackDefers.get(currentCallFrame)) {
      defer.resolvePostRollback(hub, messageFrame, currentCallFrame);
    }
    hub.defers().rollbackDefers.get(currentCallFrame).clear();

    // recursively roll back child call frames
    CallStack callStack = hub.callStack();
    currentCallFrame.childFrames().stream()
        .map(callStack::getById)
        .forEach(childCallFrame -> resolvePostRollback(hub, messageFrame, childCallFrame));
  }
}
