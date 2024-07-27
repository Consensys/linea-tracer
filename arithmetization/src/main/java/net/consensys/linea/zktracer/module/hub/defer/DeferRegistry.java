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

import java.util.*;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import net.consensys.linea.zktracer.runtime.callstack.CallStack;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.operation.Operation;
import org.hyperledger.besu.evm.worldstate.WorldView;

/**
 * Stores different categories of actions whose execution must be deferred later in the normal
 * transaction execution process.
 */
// TODO: fix naming and implement the missing interfaces
public class DeferRegistry
    implements PostOpcodeDefer,
        ImmediateContextEntryDefer,
        ContextExitDefer,
        PostRollbackDefer,
        PostTransactionDefer,
        PostConflationDefer {

  /** A list of actions deferred until the end of the current opcode execution */
  private final List<PostOpcodeDefer> postOpcodeDefers = new ArrayList<>();

  /** A list of actions deferred to the immediate entry into a child or parent context */
  private final List<ImmediateContextEntryDefer> immediateContextEntryDefers = new ArrayList<>();

  /** A list of actions deferred to the end of a given context */
  private final Map<Integer, List<ContextExitDefer>> contextExitDefers = new HashMap<>();

  /** A list of actions deferred to the end of the current opcode execution */
  private final Map<CallFrame, List<ContextReEntryDefer>> contextReEntryDefers = new HashMap<>();

  /** A list of actions deferred to the end of the current transaction */
  private final List<PostTransactionDefer> postTransactionDefers = new ArrayList<>();

  /** A list of actions deferred until the end of the current conflation execution */
  private final List<PostConflationDefer> postConflationDefers = new ArrayList<>();

  /**
   * A collection of actions whose execution is deferred to a hypothetical future rollback. This
   * collection maps a context to all actions that would have to be done if that execution context
   * were to be rolled back.
   */
  private final Map<CallFrame, List<PostRollbackDefer>> rollbackDefers = new HashMap<>();

  /** Schedule an action to be executed after the completion of the current opcode. */
  public void scheduleForImmediateContextEntry(ImmediateContextEntryDefer defer) {
    this.immediateContextEntryDefers.add(defer);
  }

  /** Schedule an action to be executed after the completion of the current opcode. */
  public void scheduleForPostExecution(PostOpcodeDefer defer) {
    this.postOpcodeDefers.add(defer);
  }

  /** Schedule an action to be executed at the exit of a given context. */
  public void scheduleForContextExit(ContextExitDefer defer, Integer callFrameId) {
    if (!contextExitDefers.containsKey(callFrameId)) {
      contextExitDefers.put(callFrameId, new ArrayList<>());
    }
    contextExitDefers.get(callFrameId).add(defer);
  }

  /** Schedule an action to be executed at the end of the current transaction. */
  public void scheduleForPostTransaction(PostTransactionDefer defer) {
    this.postTransactionDefers.add(defer);
  }

  /** Schedule an action to be executed at the end of the current transaction. */
  public void scheduleForPostConflation(PostConflationDefer defer) {
    this.postConflationDefers.add(defer);
  }

  /** Schedule an action to be executed at the re-entry in the current context. */
  public void scheduleForContextReEntry(ContextReEntryDefer defer, CallFrame callFrame) {
    if (!contextReEntryDefers.containsKey(callFrame)) {
      contextReEntryDefers.put(callFrame, new ArrayList<>());
    }
    contextReEntryDefers.get(callFrame).add(defer);
  }

  public void scheduleForPostRollback(PostRollbackDefer defer, CallFrame callFrame) {
    if (!rollbackDefers.containsKey(callFrame)) {
      rollbackDefers.put(callFrame, new ArrayList<>());
    }
    rollbackDefers.get(callFrame).add(defer);
  }

  /**
   * Trigger the execution of the actions deferred to the end of the transaction.
   *
   * @param hub the {@link Hub} context
   * @param world a {@link WorldView} on the state
   * @param tx the current {@link Transaction}
   */
  // TODO: should use the TransactionProcessingMetadata

  // TODO add docs to understand why we do two rounds of resolving (due to AccountFragment created
  // at endTx which are too deferEndTx)
  @Override
  public void resolvePostTransaction(
      Hub hub, WorldView world, Transaction tx, boolean isSuccessful) {
    final List<PostTransactionDefer> postTransactionDefersFirstRound =
        new ArrayList<>(this.postTransactionDefers);
    this.postTransactionDefers.clear();
    for (PostTransactionDefer defer : postTransactionDefersFirstRound) {
      defer.resolvePostTransaction(hub, world, tx, isSuccessful);
    }
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
  @Override
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
  @Override
  public void resolvePostExecution(Hub hub, MessageFrame frame, Operation.OperationResult result) {
    for (PostOpcodeDefer defer : this.postOpcodeDefers) {
      defer.resolvePostExecution(hub, frame, result);
    }
    this.postOpcodeDefers.clear();
  }

  /**
   * Trigger the execution of the actions deferred to the re-entry in the current context.
   *
   * @param hub the {@link Hub} context
   * @param callFrame the {@link CallFrame} of the transaction
   */
  public void resolveAtContextReEntry(Hub hub, CallFrame callFrame) {
    if (this.contextReEntryDefers.containsKey(callFrame)) {
      for (ContextReEntryDefer defer : this.contextReEntryDefers.get(callFrame)) {
        defer.resolveAtContextReEntry(hub, callFrame);
      }
      this.contextReEntryDefers.remove(callFrame);
    }
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
  @Override
  public void resolvePostRollback(
      final Hub hub, final MessageFrame messageFrame, CallFrame currentCallFrame) {

    // roll back current context
    for (PostRollbackDefer defer : hub.defers().rollbackDefers.get(currentCallFrame)) {
      defer.resolvePostRollback(hub, messageFrame, currentCallFrame);
    }
    hub.defers().rollbackDefers.get(currentCallFrame).clear();

    // recursively roll back child call frames
    final CallStack callStack = hub.callStack();
    currentCallFrame.childFrames().stream()
        .map(callStack::getById)
        .forEach(childCallFrame -> resolvePostRollback(hub, messageFrame, childCallFrame));
  }

  @Override
  public void resolveUponEnteringChildContext(Hub hub) {
    for (ImmediateContextEntryDefer defer : this.immediateContextEntryDefers) {
      defer.resolveUponEnteringChildContext(hub);
    }
    this.immediateContextEntryDefers.clear();
  }

  @Override
  public void resolveUponExitingContext(Hub hub, CallFrame callFrame) {
    final Integer frameId = callFrame.id();
    if (contextExitDefers.containsKey(frameId)) {
      for (ContextExitDefer defers : contextExitDefers.get(frameId)) {
        defers.resolveUponExitingContext(hub, callFrame);
      }
      contextExitDefers.remove(frameId);
    }
  }
}
