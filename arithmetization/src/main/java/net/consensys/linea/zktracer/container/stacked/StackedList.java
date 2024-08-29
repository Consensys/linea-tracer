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

import net.consensys.linea.zktracer.container.ModuleOperation;
import org.jetbrains.annotations.NotNull;

/**
 * Implements a system of nested lists behaving as a single one, where the current context
 * modification can transparently be dropped.
 *
 * @param <E> the type of elements stored in the list
 */
public class StackedList<E extends ModuleOperation> {
  private final List<E> operationSinceBeginningOfTheConflation = new ArrayList<>();
  private final List<E> thisTransactionOperation = new ArrayList<>();
  private final CountOnlyOperation lineCounter = new CountOnlyOperation();

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
    return getAll().get(index);
  }

  public List<E> getAll() {
    final List<E> all = new ArrayList<>(operationSinceBeginningOfTheConflation);
    all.addAll(thisTransactionOperation);
    return all;
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
}
