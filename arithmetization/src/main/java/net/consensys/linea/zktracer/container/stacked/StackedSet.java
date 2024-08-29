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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.consensys.linea.zktracer.container.ModuleOperation;
import org.jetbrains.annotations.NotNull;

/**
 * Implements a system of nested sets behaving as a single one, where the current context
 * modification can transparently be dropped.
 *
 * @param <E> the type of elements stored in the set
 */
public class StackedSet<E extends ModuleOperation> {
  private final Set<E> operationSinceBeginningOfTheConflation = new HashSet<>();
  private final Set<E> thisTransactionOperation = new HashSet<>();
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

  public int size() {
    return thisTransactionOperation.size() + operationSinceBeginningOfTheConflation.size();
  }

  public int lineCount() {
    return lineCounter.lineCount();
  }

  public Set<E> getAll() {
    final Set<E> all = new HashSet<>(operationSinceBeginningOfTheConflation);
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
    if (!operationSinceBeginningOfTheConflation.contains(e)) {
      final boolean isNew = thisTransactionOperation.add(e);
      if (isNew) {
        lineCounter.add(e.lineCount());
      }
      return isNew;
    }
    return false;
  }

  public boolean containsAll(@NotNull Collection<?> c) {
    for (var x : c) {
      if (!contains(x)) {
        return false;
      }
    }
    return true;
  }

  public boolean addAll(@NotNull Collection<? extends E> c) {
    boolean r = false;
    for (var x : c) {
      r |= add(x);
    }
    return r;
  }

  public void clear() {
    operationSinceBeginningOfTheConflation.clear();
    thisTransactionOperation.clear();
    lineCounter.clear();
  }
}
