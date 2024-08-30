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

package net.consensys.linea.zktracer.module.mxp;

import java.nio.MappedByteBuffer;
import java.util.List;

import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.container.stacked.StackedList;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.hub.fragment.imc.MxpCall;

/** Implementation of a {@link Module} for memory expansion. */
public class Mxp implements Module {
  /** A list of the operations to trace */
  private final StackedList<MxpOperation> operations = new StackedList<>();

  @Override
  public String moduleKey() {
    return "MXP";
  }

  public Mxp() {}

  @Override
  public void enterTransaction() {
    operations.enter();
  }

  @Override
  public void popTransaction() {
    operations.pop();
  }

  @Override
  public int lineCount() {
    return operations.lineCount();
  }

  @Override
  public List<ColumnHeader> columnsHeaders() {
    return Trace.headers(this.lineCount());
  }

  @Override
  public void commit(List<MappedByteBuffer> buffers) {
    final Trace trace = new Trace(buffers);
    int stamp = 0;
    for (MxpOperation mxpOperation : operations.getAll()) {
      mxpOperation.trace(++stamp, trace);
    }
  }

  public void call(MxpCall mxpCall) {
    operations.add(new MxpOperation(mxpCall));
  }
}
