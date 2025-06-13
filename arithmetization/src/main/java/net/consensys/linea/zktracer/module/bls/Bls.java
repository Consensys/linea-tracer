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

package net.consensys.linea.zktracer.module.bls;

import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.container.module.OperationListModule;
import net.consensys.linea.zktracer.container.stacked.ModuleOperationStackedList;
import net.consensys.linea.zktracer.module.hub.fragment.scenario.PrecompileScenarioFragment;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;

@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public class Bls implements OperationListModule<BlsOperation> {
  public static final Set<Address> BLS_PRECOMPILES =
      Set.of(
          Address.KZG_POINT_EVAL,
          Address.BLS12_G1ADD,
          Address.BLS12_G1MULTIEXP,
          Address.BLS12_G2ADD,
          Address.BLS12_G2MULTIEXP,
          Address.BLS12_PAIRING,
          Address.BLS12_MAP_FP_TO_G1,
          Address.BLS12_MAP_FP2_TO_G2);

  private final ModuleOperationStackedList<BlsOperation> operations =
      new ModuleOperationStackedList<>();

  private final Wcp wcp;

  @Getter private BlsOperation blsOperation;

  @Override
  public String moduleKey() {
    return "BLS";
  }

  @Override
  public List<Trace.ColumnHeader> columnHeaders(Trace trace) {
    return trace.bls().headers(this.lineCount());
  }

  @Override
  public int spillage(Trace trace) {
    return trace.bls().spillage();
  }

  @Override
  public void commit(Trace trace) {
    int stamp = 0;
    long previousId = 0;
    for (BlsOperation op : operations.getAll()) {
      op.trace(trace.bls(), ++stamp, previousId);
      previousId = op.id();
    }
  }

  public void callBls(
      final int id,
      final PrecompileScenarioFragment.PrecompileFlag precompileFlag,
      final Bytes callData,
      final Bytes returnData) {
    blsOperation = BlsOperation.of(wcp, id, precompileFlag, callData, returnData);
    operations.add(blsOperation);
  }
}
