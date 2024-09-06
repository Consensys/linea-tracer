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

package net.consensys.linea.zktracer.module.gas;

import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.util.List;

import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.container.module.Module;
import net.consensys.linea.zktracer.container.stacked.StackedSet;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.common.CommonFragmentValues;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;

public class Gas implements Module {
  /** A list of the operations to trace */
  private final StackedSet<GasOperation> operations = new StackedSet<>();

  // TODO: why a stackedList of GasOperation? It should be a StateLess module no ?

  @Override
  public String moduleKey() {
    return "GAS";
  }

  @Override
  public void enterTransaction() {
    this.operations.enter();
  }

  @Override
  public void popTransaction() {
    this.operations.pop();
  }

  @Override
  public int lineCount() {
    return this.operations.lineCount();
  }

  @Override
  public List<ColumnHeader> columnsHeaders() {
    return null;
  }

  @Override
  public void commit(List<MappedByteBuffer> buffers) {
    final Trace trace = new Trace(buffers);
    int stamp = 0;
    for (GasOperation gasOperation : this.operations.getAll()) {
      gasOperation.trace(++stamp, trace);
    }
  }

  public void call(Hub hub, CommonFragmentValues common) {
    final GasCall gasCall = new GasCall();
    gasCall.setGasActual(BigInteger.valueOf(common.gasActual));
    gasCall.setGasCost(BigInteger.valueOf(common.gasCost));
    gasCall.setXahoy(common.exceptionAhoy);
    gasCall.setOogx(Exceptions.outOfGasException(hub.pch().exceptions()));
    // TODO: this is false, see issue #1118
    //  we may have to defer post opcode (or post execution) the setting of oogx
    //  because we will only know which exception, if any, to trace once we have
    //  processed the instruction in its relevant XxxSection
    this.operations.add(new GasOperation(gasCall));
  }
}
