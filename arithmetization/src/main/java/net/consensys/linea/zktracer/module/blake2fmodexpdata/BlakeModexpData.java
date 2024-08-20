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

package net.consensys.linea.zktracer.module.blake2fmodexpdata;

import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_BLAKE_DATA;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_BLAKE_RESULT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_MODEXP_BASE;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_MODEXP_EXPONENT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_MODEXP_MODULUS;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PHASE_MODEXP_RESULT;

import java.nio.MappedByteBuffer;
import java.util.List;

import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.container.stacked.list.StackedList;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.hub.precompiles.ModExpMetadata;
import net.consensys.linea.zktracer.module.limits.precompiles.BlakeEffectiveCall;
import net.consensys.linea.zktracer.module.limits.precompiles.BlakeRounds;
import net.consensys.linea.zktracer.module.limits.precompiles.ModexpEffectiveCall;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import org.apache.tuweni.bytes.Bytes;

@RequiredArgsConstructor
public class BlakeModexpData implements Module {
  private final Wcp wcp;
  private final ModexpEffectiveCall modexpEffectiveCall;
  private final BlakeEffectiveCall blakeEffectiveCall;
  private final BlakeRounds blakeRounds;

  private final StackedList<BlakeModexpDataOperation> operations = new StackedList<>();
  private long previousID = 0;

  @Override
  public String moduleKey() {
    return "BLAKE_MODEXP_DATA";
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
    return Trace.headers(this.lineCount());
  }

  public void callModexp(final ModExpMetadata modexpMetaData, final int operationID) {
    operations.add(new BlakeModexpDataOperation(modexpMetaData, operationID));
    modexpEffectiveCall.addPrecompileLimit(1);
    callWcpForIdCheck(operationID);
  }

  public void callBlake(final BlakeComponents blakeComponents, final int operationID) {
    operations.add(new BlakeModexpDataOperation(blakeComponents, operationID));
    blakeEffectiveCall.addPrecompileLimit(1);
    blakeRounds.addPrecompileLimit(blakeComponents.r().toInt());
    callWcpForIdCheck(operationID);
  }

  private void callWcpForIdCheck(final int operationID) {
    wcp.callLT(previousID, operationID);
    previousID = operationID;
  }

  @Override
  public void commit(List<MappedByteBuffer> buffers) {
    Trace trace = new Trace(buffers);
    int stamp = 0;
    for (BlakeModexpDataOperation o : operations) {
      stamp++;
      o.trace(trace, stamp);
    }
  }

  // TODO: this should die, the MMU just have to read in MmuCall
  public Bytes getInputDataByIdAndPhase(final int id, final int phase) {
    final BlakeModexpDataOperation op = getOperationById(id);
    return switch (phase) {
      case PHASE_MODEXP_BASE -> op.modexpMetaData.get().base();
      case PHASE_MODEXP_EXPONENT -> op.modexpMetaData.get().exp();
      case PHASE_MODEXP_MODULUS -> op.modexpMetaData.get().mod();
      case PHASE_MODEXP_RESULT -> Bytes.EMPTY; // TODO
      case PHASE_BLAKE_DATA -> op.blake2fComponents.get().data();
      case PHASE_BLAKE_RESULT -> Bytes.EMPTY; // TODO
      default -> throw new IllegalStateException("Unexpected value: " + phase);
    };
  }

  private BlakeModexpDataOperation getOperationById(final int id) {
    for (BlakeModexpDataOperation operation : this.operations) {
      if (id == operation.id) {
        return operation;
      }
    }
    throw new RuntimeException("BlakeModexpOperation not found");
  }
}
