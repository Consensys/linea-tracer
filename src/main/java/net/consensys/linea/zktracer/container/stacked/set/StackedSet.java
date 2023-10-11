/*
 * Copyright ConsenSys AG.
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

package net.consensys.linea.zktracer.container.stacked.set;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import net.consensys.linea.zktracer.container.StackedContainer;
import org.jetbrains.annotations.NotNull;

/**
 * Implements a system of nested sets behaving as a single on, where the current context
 * modification can transparently be dropped.
 *
 * @param <E> the type of elements stored in the set
 */
public class StackedSet<E> implements StackedContainer, java.util.Set<E> {
  private final Stack<Set<E>> sets = new Stack<>();

  @Override
  public void enter() {
    this.sets.push(new HashSet<>());
  }

  @Override
  public void pop() {
    this.sets.pop();
  }

  @Override
  public int size() {
    return this.sets.stream().mapToInt(Set::size).sum();
  }

  @Override
  public boolean isEmpty() {
    for (Set<E> set : this.sets) {
      if (!set.isEmpty()) {
        return false;
      }
    }

    return true;
  }

  @Override
  public boolean contains(Object o) {
    for (Set<E> set : this.sets) {
      if (set.contains(o)) {
        return true;
      }
    }
    return false;
  }

  @NotNull
  @Override
  public Iterator<E> iterator() {
    return new StackedSetIterator<>(this.sets);
  }

  @NotNull
  @Override
  public Object[] toArray() {
    throw new RuntimeException("toArray not supported");
  }

  @NotNull
  @Override
  public <T> T[] toArray(@NotNull T[] a) {
    throw new RuntimeException("toArray not supported");
  }

  @Override
  public boolean add(E e) {
    // An element should *never* be duplicated, for it would appear twice in the iterator.
    if (this.contains(e)) {
      return false;
    }

    return this.sets.peek().add(e);
  }

  @Override
  public boolean remove(Object o) {
    throw new RuntimeException("remove not supported");
  }

  @Override
  public boolean containsAll(@NotNull Collection<?> c) {
    for (var x : c) {
      if (!this.contains(x)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean addAll(@NotNull Collection<? extends E> c) {
    boolean r = false;
    for (var x : c) {
      r |= this.sets.peek().add(x);
    }
    return r;
  }

  @Override
  public boolean retainAll(@NotNull Collection<?> c) {
    throw new RuntimeException("retainAll not supported");
  }

  @Override
  public boolean removeAll(@NotNull Collection<?> c) {
    throw new RuntimeException("removeAll not supported");
  }

  @Override
  public void clear() {
    this.sets.clear();
  }

  /** This class acts as an {@link Iterator} over a StackedSet. */
  private static class StackedSetIterator<E> implements Iterator<E> {
    private final List<Iterator<E>> iters = new ArrayList<>();

    StackedSetIterator(List<Set<E>> sets) {
      for (Set<E> set : sets) {
        this.iters.add(set.iterator());
      }
    }

    @Override
    public boolean hasNext() {
      for (Iterator<E> iter : iters) {
        if (iter.hasNext()) {
          return true;
        }
      }
      return false;
    }

    @Override
    public E next() {
      for (Iterator<E> iter : iters) {
        if (iter.hasNext()) {
          return iter.next();
        }
      }
      return null;
    }
  }
}
