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

package net.consensys.linea.zktracer.module.trm;

import static net.consensys.linea.zktracer.module.constants.GlobalConstants.LLARGE;

import java.nio.MappedByteBuffer;
import java.util.List;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.container.module.OperationSetModule;
import net.consensys.linea.zktracer.container.stacked.ModuleOperationStackedSet;
import net.consensys.linea.zktracer.types.EWord;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.datatypes.Address;

@Getter
@Accessors(fluent = true)
public class Trm implements OperationSetModule<TrmOperation> {
  private final ModuleOperationStackedSet<TrmOperation> operations =
      new ModuleOperationStackedSet<>();

  static final int MAX_CT = LLARGE;
  static final int PIVOT_BIT_FLIPS_TO_TRUE = 12;

  @Override
  public String moduleKey() {
    return "TRM";
  }

  public Address callTrimming(Bytes32 rawHash) {
    operations.add(new TrmOperation(EWord.of(rawHash)));
    return Address.extract(rawHash);
  }

  public Address callTrimming(Bytes addressToTrim) {
    final Bytes32 addressPadded = Bytes32.leftPad(addressToTrim);
    return callTrimming(addressPadded);
  }

  @Override
  public List<ColumnHeader> columnsHeaders() {
    return Trace.headers(this.lineCount());
  }

  @Override
  public void commit(List<MappedByteBuffer> buffers) {
    final Trace trace = new Trace(buffers);

    int stamp = 0;
    for (TrmOperation operation : operations.getAll()) {
      operation.trace(trace, ++stamp);
    }
  }
}
