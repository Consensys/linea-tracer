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
import java.util.List;

import net.consensys.linea.zktracer.module.hub.Hub;
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

  private final List<Pair<Integer, List<PostRollbackDefer>>> rollbackDefers = new ArrayList<>();

  /*
  For every context X that is RESPONSIBLE for triggering a rollback i.e.
  - executes the REVERT opcode or
  - triggers an exceptional halting condition
  should have an entry in the rollbackDefers sub-registry.

      < context_number_of_X, List < RollbackDefers > >

   this list should contain all the stuff that will be undone / make explicit
   at the precise time where the context reverts.

   Note: we could have such a list for EVERY context, with that list being either
   - empty if this context isn't responsible for a rollback
   - nonempty and containing everything susceptible to be rolled back by itself o
     an ancestor

   Choice

   A does a CALL
       B is the callee context
       B does a CALL
           C is the callee context
           C does a CREATE
               D is the createe context
               D is successful
               D exits with exit code 1 (success)
           C resumes execution
           C fucks up
           C now has to undo everything that happened in C and D
           C exits with exit code 0 (failure)
       B resumes execution
       B is successful
       B exits with exit code 1 (success)
   A resumes execution
   A does a CALL
       E executes
       E is successful
       E exits with exit code 1 (success)
   A resumes execution
   A fucks up
   A is responsible for undoing everything done by A, B and E

   for a context you would have the following information
   - SUCCESS / FAILURE
   - ALREADY_ROLLED_BACK
   - better: in stead of saying: 'this context was already rolled back' and remembering
     that decision we simply clear the corresponding list; and we always rollback everything
     in the descendant contexts

   A does  a CALL
      enter B
      B does a CALL
         enter C
         C success, exits with exits code 1
      B resumes
      B succeeds
   A resumes execution
   A fucks up
   A is responsible for reverting all its descendants (B and C)


   Extra difficulty:
   - nice stuff:
      - nonces, warmth, deployments, ... get rolled back with any rollback
      - balance transfers get rolled back with child failure

   A does a CREATE with value v
      A's balance does bal_A -= v
      A's nonce does nonce_A += 1
         B is the createe
         B's balance does bal_B += v
         B's nonce does nonce_B = 1
         B fucks up
         B exits with exit code 0
      bal_A += v
      bal_B -= v
      nonces stay the same ! for A and B ...
   A resumes execution

   if A is rolled back later then its nonce goes back to its initial value "n"
   if A isn't rolled back later then its nonce remains at "n + 1"

   directDescendantSelfRevertRegistry
   - everytime there is a CALL / CREATE we add 'it' (i.e. that operation) to that sub-registry
   - every time a context comes to a halt we need to determine if it
     exited with exit code 1 or 0
   - if the exit code is 0 we need to undo the balance transfer

   In other words we should have a LIST (not a tree) of such CALL's / CREATE's
   We add items to it with every CALL / CREATE
   Everytime we exit a context (special care has to be taken for EOA's / PRECOMPILE's)
   we pop the top item of this list
   and depending on whether or not the exit code is 1 / 0 we "resolve" that item.
   if EXIT_CODE = 1 no resolution is required ( return; )
   if EXIT_CODE = 0 then the balance must be resolved;
      - i.e. we need to resolve a CALL_SECTION or a CREATE_SECTION

   This means that
   - CallSection must implement the DirectDescendantSelfRevertRegistry
   - CreateSection must implement the DirectDescendantSelfRevertRegistry
   */

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
}
