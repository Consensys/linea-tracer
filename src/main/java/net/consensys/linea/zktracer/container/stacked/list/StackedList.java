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

package net.consensys.linea.zktracer.container.stacked.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.jetbrains.annotations.NotNull;

public class StackedList<E> implements List<E> {
  private final List<List<E>> lists = new ArrayList<>();

  @Override
  public int size() {
    this.lists.iterator()
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public boolean contains(Object o) {
    return false;
  }

  @NotNull
  @Override
  public Iterator<E> iterator() {
    return null;
  }

  @NotNull
  @Override
  public Object[] toArray() {
    return new Object[0];
  }

  @NotNull
  @Override
  public <T> T[] toArray(@NotNull T[] a) {
    return null;
  }

  @Override
  public boolean add(E e) {
    return false;
  }

  @Override
  public boolean remove(Object o) {
    return false;
  }

  @Override
  public boolean containsAll(@NotNull Collection<?> c) {
    return false;
  }

  @Override
  public boolean addAll(@NotNull Collection<? extends E> c) {
    return false;
  }

  @Override
  public boolean addAll(int index, @NotNull Collection<? extends E> c) {
    return false;
  }

  @Override
  public boolean removeAll(@NotNull Collection<?> c) {
    return false;
  }

  @Override
  public boolean retainAll(@NotNull Collection<?> c) {
    return false;
  }

  @Override
  public void clear() {

  }

  @Override
  public E get(int index) {
    return null;
  }

  @Override
  public E set(int index, E element) {
    return null;
  }

  @Override
  public void add(int index, E element) {

  }

  @Override
  public E remove(int index) {
    return null;
  }

  @Override
  public int indexOf(Object o) {
    return 0;
  }

  @Override
  public int lastIndexOf(Object o) {
    return 0;
  }

  @NotNull
  @Override
  public ListIterator<E> listIterator() {
    return null;
  }

  @NotNull
  @Override
  public ListIterator<E> listIterator(int index) {
    return null;
  }

  @NotNull
  @Override
  public List<E> subList(int fromIndex, int toIndex) {
    return null;
  }
}
