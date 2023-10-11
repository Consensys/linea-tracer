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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

class StackedSetIterator<E> implements Iterator<E> {
  private final List<Iterator<E>> iters = new ArrayList<>(10);

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
