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

package net.consensys.linea.zktracer.module.limits.precompiles;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.module.Module;
import org.apache.commons.lang3.NotImplementedException;

public class AbstractCallCounter implements Module {
  private final Deque<Integer> callCount = new ArrayDeque<>();

  @Override
  public String moduleKey() {
    throw new NotImplementedException("must be implemented by derived class");
  }

  @Override
  public void enterTransaction() {
    this.callCount.push(0);
  }

  @Override
  public void popTransaction() {
    this.callCount.pop();
  }

  @Override
  public int lineCount() {
    return this.callCount.stream().mapToInt(x -> x).sum();
  }

  public void tick() {
    this.callCount.push(this.callCount.pop() + 1);
  }

  @Override
  public List<ColumnHeader> columnsHeaders() {
    throw new IllegalStateException("should never be called");
  }
}
