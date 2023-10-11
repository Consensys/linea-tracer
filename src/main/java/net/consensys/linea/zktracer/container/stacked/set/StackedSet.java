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

import net.consensys.linea.zktracer.container.StackedState;
import org.jetbrains.annotations.NotNull;

public class StackedSet<E> implements StackedState, java.util.Set<E> {
  private final List<Set<E>> sets = new ArrayList<>();
  private int head = -1;

  private Set<E> top() {
    return this.sets.get(head);
  }

  @Override
  public void push() {
    head += 1;
    if (head + 1 > this.sets.size()) {
      this.sets.add(new HashSet<>());
    } else {
      this.sets.set(head, new HashSet<>());
    }
  }

  @Override
  public void pop() {
    if (head >= 0) {
      head -= 1;
    }
  }

  @Override
  public int size() {
    var size = 0;
    for (int i = 0; i < head; i++) {
      size += this.sets.get(i).size();
    }
    return size;
  }

  @Override
  public boolean isEmpty() {
    for (int i = 0; i < head; i++) {
      if (!this.sets.get(i).isEmpty()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean contains(Object o) {
    for (int i = head; i >= 0; i--) {
      if (this.sets.get(i).contains(o)) {
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
    if (this.contains(e)) {
      return false;
    }

    return this.top().add(e);
  }

  @Override
  public boolean remove(Object o) {
    for (int i = head; i >= 0; i--) {
      if (this.sets.get(i).remove(o)) {
        return true;
      }
    }
    return false;
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
      r = r || this.top().add(x);
    }
    return r;
  }

  @Override
  public boolean retainAll(@NotNull Collection<?> c) {
    throw new RuntimeException("retainAll not supported");
  }

  @Override
  public boolean removeAll(@NotNull Collection<?> c) {
    boolean r = false;
    for (var x : c) {
      r = r || this.remove(x);
    }
    return r;
  }

  @Override
  public void clear() {
    this.head = -1;
  }
}
