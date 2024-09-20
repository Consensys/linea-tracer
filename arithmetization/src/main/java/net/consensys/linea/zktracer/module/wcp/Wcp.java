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

package net.consensys.linea.zktracer.module.wcp;

import static net.consensys.linea.zktracer.module.wcp.WcpOperation.EQbv;
import static net.consensys.linea.zktracer.module.wcp.WcpOperation.GEQbv;
import static net.consensys.linea.zktracer.module.wcp.WcpOperation.GTbv;
import static net.consensys.linea.zktracer.module.wcp.WcpOperation.ISZERObv;
import static net.consensys.linea.zktracer.module.wcp.WcpOperation.LEQbv;
import static net.consensys.linea.zktracer.module.wcp.WcpOperation.LTbv;
import static net.consensys.linea.zktracer.module.wcp.WcpOperation.SGTbv;
import static net.consensys.linea.zktracer.module.wcp.WcpOperation.SLTbv;

import java.nio.MappedByteBuffer;
import java.util.List;

import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.container.module.Module;
import net.consensys.linea.zktracer.container.stacked.CountOnlyOperation;
import net.consensys.linea.zktracer.container.stacked.StackedSet;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.worldstate.WorldView;

@RequiredArgsConstructor
public class Wcp implements Module {

  private final StackedSet<WcpOperation> ltOperations = new StackedSet<>();
  private final StackedSet<WcpOperation> leqOperations = new StackedSet<>();
  private final StackedSet<WcpOperation> gtOperations = new StackedSet<>();
  private final StackedSet<WcpOperation> geqOperations = new StackedSet<>();
  private final StackedSet<WcpOperation> sltOperations = new StackedSet<>();
  private final StackedSet<WcpOperation> sgtOperations = new StackedSet<>();
  private final StackedSet<WcpOperation> eqOperations = new StackedSet<>();
  private final StackedSet<WcpOperation> isZeroOperations = new StackedSet<>();

  /**
   * For perf, we split the WcpOperations into different StackedSet in order to - have smaller
   * set,thus faster to check for equality - remove the opcode value when checking the equality
   */
  private final List<StackedSet<WcpOperation>> operations =
      List.of(
          ltOperations,
          leqOperations,
          gtOperations,
          geqOperations,
          sltOperations,
          sgtOperations,
          eqOperations,
          isZeroOperations);

  /** count the number of rows that could be added after the sequencer counts the number of line */
  public final CountOnlyOperation additionalRows = new CountOnlyOperation();

  @Override
  public String moduleKey() {
    return "WCP";
  }

  @Override
  public void enterTransaction() {
    for (StackedSet<WcpOperation> operationsSet : operations) {
      operationsSet.enter();
    }
    additionalRows.enter();
  }

  @Override
  public void popTransaction() {
    for (StackedSet<WcpOperation> operationsSet : operations) {
      operationsSet.pop();
    }
    additionalRows.pop();
  }

  @Override
  public void tracePreOpcode(final MessageFrame frame) {
    final OpCode opCode = OpCode.of(frame.getCurrentOperation().getOpcode());
    final Bytes32 arg1 = Bytes32.leftPad(frame.getStackItem(0));
    final Bytes32 arg2 =
        (opCode != OpCode.ISZERO) ? Bytes32.leftPad(frame.getStackItem(1)) : Bytes32.ZERO;

    switch (opCode) {
      case LT -> ltOperations.add(new WcpOperation(LTbv, arg1, arg2));
      case GT -> gtOperations.add(new WcpOperation(GTbv, arg1, arg2));
      case SLT -> sltOperations.add(new WcpOperation(SLTbv, arg1, arg2));
      case SGT -> sgtOperations.add(new WcpOperation(SGTbv, arg1, arg2));
      case EQ -> eqOperations.add(new WcpOperation(EQbv, arg1, arg2));
      case ISZERO -> isZeroOperations.add(new WcpOperation(ISZERObv, arg1, Bytes32.ZERO));
      default -> throw new UnsupportedOperationException("Not given a WCP EVM Opcode");
    }
  }

  @Override
  public List<ColumnHeader> columnsHeaders() {
    return Trace.headers(this.lineCount());
  }

  @Override
  public void commit(List<MappedByteBuffer> buffers) {
    final Trace trace = new Trace(buffers);

    int stamp = 0;
    for (StackedSet<WcpOperation> operationsSet : operations) {
      for (WcpOperation operation : operationsSet.getAll()) {
        operation.trace(trace, ++stamp);
      }
    }
  }

  @Override
  public int lineCount() {
    final int count = operations.stream().mapToInt(StackedSet::lineCount).sum();
    return ltOperations.conflationFinished() ? count : count + additionalRows.lineCount();
  }

  public boolean callLT(final Bytes32 arg1, final Bytes32 arg2) {
    ltOperations.add(new WcpOperation(LTbv, arg1, arg2));
    return arg1.compareTo(arg2) < 0;
  }

  public boolean callLT(final Bytes arg1, final Bytes arg2) {
    return callLT(Bytes32.leftPad(arg1), Bytes32.leftPad(arg2));
  }

  public boolean callLT(final long arg1, final long arg2) {
    return callLT(Bytes.ofUnsignedLong(arg1), Bytes.ofUnsignedLong(arg2));
  }

  public boolean callGT(final Bytes32 arg1, final Bytes32 arg2) {
    gtOperations.add(new WcpOperation(GTbv, arg1, arg2));
    return arg1.compareTo(arg2) > 0;
  }

  public boolean callGT(final Bytes arg1, final Bytes arg2) {
    return callGT(Bytes32.leftPad(arg1), Bytes32.leftPad(arg2));
  }

  public boolean callGT(final int arg1, final int arg2) {
    return callGT(Bytes.ofUnsignedLong(arg1), Bytes.ofUnsignedLong(arg2));
  }

  public boolean callEQ(final Bytes32 arg1, final Bytes32 arg2) {
    eqOperations.add(new WcpOperation(EQbv, arg1, arg2));
    return arg1.compareTo(arg2) == 0;
  }

  public boolean callEQ(final Bytes arg1, final Bytes arg2) {
    return callEQ(Bytes32.leftPad(arg1), Bytes32.leftPad(arg2));
  }

  public boolean callISZERO(final Bytes32 arg1) {
    isZeroOperations.add(new WcpOperation(ISZERObv, arg1, Bytes32.ZERO));
    return arg1.isZero();
  }

  public boolean callISZERO(final Bytes arg1) {
    return callISZERO(Bytes32.leftPad(arg1));
  }

  public boolean callLEQ(final Bytes32 arg1, final Bytes32 arg2) {
    leqOperations.add(new WcpOperation(LEQbv, arg1, arg2));
    return arg1.compareTo(arg2) <= 0;
  }

  public boolean callLEQ(final long arg1, final long arg2) {
    return callLEQ(Bytes.ofUnsignedLong(arg1), Bytes.ofUnsignedLong(arg2));
  }

  public boolean callLEQ(final Bytes arg1, final Bytes arg2) {
    return callLEQ(Bytes32.leftPad(arg1), Bytes32.leftPad(arg2));
  }

  public boolean callGEQ(final Bytes32 arg1, final Bytes32 arg2) {
    geqOperations.add(new WcpOperation(GEQbv, arg1, arg2));
    return arg1.compareTo(arg2) >= 0;
  }

  public boolean callGEQ(final Bytes arg1, final Bytes arg2) {
    return callGEQ(Bytes32.leftPad(arg1), Bytes32.leftPad(arg2));
  }

  @Override
  public void traceEndConflation(final WorldView state) {
    for (StackedSet<WcpOperation> operationsSet : operations) {
      operationsSet.finishConflation();
    }
  }
}
