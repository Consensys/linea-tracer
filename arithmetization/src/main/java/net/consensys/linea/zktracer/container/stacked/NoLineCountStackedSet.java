/*
 * Copyright ConsenSys Inc.
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

package net.consensys.linea.zktracer.container.stacked;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
public class NoLineCountStackedSet<E> {
  private final Set<E> operationsCommitedToTheConflation;
  private final Set<E> operationsInTransaction;

  public NoLineCountStackedSet() {
    operationsCommitedToTheConflation = new HashSet<>();
    operationsInTransaction = new HashSet<>();
  }

  /** Prefer this constructor as we preallocate more needed memory */
  public NoLineCountStackedSet(
      final int expectedConflationNumberOperations, final int expectedTransactionNumberOperations) {
    operationsCommitedToTheConflation = new HashSet<>(expectedConflationNumberOperations);
    operationsInTransaction = new HashSet<>(expectedTransactionNumberOperations);
  }

  /**
   * Upon entering a new transaction, the set of operations generated by the previous transaction
   * {@link StackedSet#operationsInTransaction()} (which is empty if no such transaction exists) is
   * added to the set of committed operations {@link
   * StackedSet#operationsCommitedToTheConflation()}. {@link StackedSet#operationsInTransaction()}
   * is further reset to be empty.
   */
  public void enter() {
    operationsCommitedToTheConflation().addAll(operationsInTransaction());
    operationsInTransaction().clear();
  }

  public void pop() {
    operationsInTransaction().clear();
  }

  public boolean add(E e) {
    if (!operationsCommitedToTheConflation().contains(e)) {
      return operationsInTransaction().add(e);
    }
    return false;
  }
}
