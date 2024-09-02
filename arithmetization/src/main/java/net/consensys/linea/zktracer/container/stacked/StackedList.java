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

package net.consensys.linea.zktracer.container.stacked;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.container.ModuleOperation;
import org.jetbrains.annotations.NotNull;

/**
 * Implements a system of pseudo-stacked squashed List where {@link
 * operationSinceBeginningOfTheConflation} represents the List of all operations since the beginning
 * of the conflation and {@link thisTransactionOperation} represents the operations added by the
 * last transaction. We can pop only the operations added by last transaction. The line counting is
 * done by a separate {@link CountOnlyOperation}.
 *
 * @param <E> the type of elements stored in the set
 */
@Accessors(fluent = true)
public class StackedList<E extends ModuleOperation> {
  private final List<E> operationSinceBeginningOfTheConflation;
  @Getter private final List<E> thisTransactionOperation;
  private final CountOnlyOperation lineCounter = new CountOnlyOperation();
  private boolean conflationFinished = false;

  public StackedList() {
    operationSinceBeginningOfTheConflation = new ArrayList<>();
    thisTransactionOperation = new ArrayList<>();
  }

  /** Prefer this constructor as we preallocate more needed memory */
  public StackedList(
      final int expectedConflationNumberOperations, final int expectedTransactionNumberOperations) {
    operationSinceBeginningOfTheConflation = new ArrayList<>(expectedConflationNumberOperations);
    thisTransactionOperation = new ArrayList<>(expectedTransactionNumberOperations);
  }

  /**
   * when we enter a transaction, the previous transaction is definitely added to the block and
   * can't be pop
   */
  public void enter() {
    operationSinceBeginningOfTheConflation.addAll(thisTransactionOperation);
    thisTransactionOperation.clear();
    lineCounter.enter();
  }

  public void pop() {
    thisTransactionOperation.clear();
    lineCounter.pop();
  }

  public E getFirst() {
    return operationSinceBeginningOfTheConflation.isEmpty()
        ? thisTransactionOperation.getFirst()
        : operationSinceBeginningOfTheConflation.getFirst();
  }

  public E getLast() {
    return thisTransactionOperation.isEmpty()
        ? operationSinceBeginningOfTheConflation.getLast()
        : thisTransactionOperation.getLast();
  }

  public int size() {
    return thisTransactionOperation.size() + operationSinceBeginningOfTheConflation.size();
  }

  public int lineCount() {
    return lineCounter.lineCount();
  }

  public E get(int index) {
    if (index < operationSinceBeginningOfTheConflation.size()) {
      return operationSinceBeginningOfTheConflation.get(index);
    } else {
      return thisTransactionOperation.get(index - operationSinceBeginningOfTheConflation.size());
    }
  }

  public List<E> getAll() {
    Preconditions.checkState(conflationFinished, "Conflation not finished");
    return operationSinceBeginningOfTheConflation;
  }

  public boolean isEmpty() {
    return size() == 0;
  }

  public boolean contains(Object o) {
    return thisTransactionOperation.contains(o)
        || operationSinceBeginningOfTheConflation.contains(o);
  }

  public boolean add(E e) {
    lineCounter.add(e.lineCount());
    return thisTransactionOperation.add(e);
  }

  public boolean addAll(@NotNull Collection<? extends E> c) {
    boolean r = false;
    for (var x : c) {
      r |= this.add(x);
    }
    return r;
  }

  public void clear() {
    operationSinceBeginningOfTheConflation.clear();
    thisTransactionOperation.clear();
    lineCounter.clear();
  }

  public void finishConflation() {
    conflationFinished = true;
    operationSinceBeginningOfTheConflation.addAll(thisTransactionOperation);
    thisTransactionOperation.clear();
    lineCounter.enter(); // this is not mandatory but it is more consistent
  }
}
