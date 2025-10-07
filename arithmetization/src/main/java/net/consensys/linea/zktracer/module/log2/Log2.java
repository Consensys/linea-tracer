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

package net.consensys.linea.zktracer.module.log2;

import static net.consensys.linea.zktracer.module.ModuleName.LOG2;

import java.util.List;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.container.module.OperationSetModule;
import net.consensys.linea.zktracer.container.stacked.ModuleOperationStackedSet;
import net.consensys.linea.zktracer.module.ModuleName;
import org.apache.tuweni.bytes.Bytes;

@Accessors(fluent = true)
public class Log2 implements OperationSetModule<Log2Operation> {
  @Getter
  private final ModuleOperationStackedSet<Log2Operation> operations =
      new ModuleOperationStackedSet<>();

  @Override
  public ModuleName moduleKey() {
    return LOG2;
  }

  public void callLog2(Bytes word) {
    operations.add(new Log2Operation(word));
  }

  @Override
  public int spillage(Trace trace) {
    return trace.log2().spillage();
  }

  @Override
  public List<Trace.ColumnHeader> columnHeaders(Trace trace) {
    return trace.log2().headers(lineCount());
  }

  @Override
  public void commit(Trace trace) {
    for (Log2Operation op : sortOperations(new Log2Operation.Comparator())) {
      op.trace(trace.log2());
    }
  }
}
