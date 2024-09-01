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

package net.consensys.linea.zktracer.container.module;

import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.container.stacked.StackedList;
import org.hyperledger.besu.evm.worldstate.WorldView;

/**
 * A {@link StatefullModule} is a {@link Module} that contains ordered {@link ModuleOperation} where
 * we can keep trace of duplicates.
 */
public interface StatefullModule<E extends ModuleOperation> extends Module {
  StackedList<E> operations();

  @Override
  default void enterTransaction() {
    operations().enter();
  }

  @Override
  default void popTransaction() {
    operations().pop();
  }

  @Override
  default int lineCount() {
    return operations().lineCount();
  }

  @Override
  default void traceEndConflation(final WorldView state) {
    operations().finishConflation();
  }
}
