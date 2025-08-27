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
      final Bytes returnData,
      final boolean successBit) {
    blsOperation = BlsOperation.of(wcp, id, precompileFlag, callData, returnData, successBit);
    operations.add(blsOperation);

    // TODO: compute precompile limits
    if (blsOperation.mint()) {
      // every limit is 0
    }

    switch (blsOperation.precompileFlag()) {
      case PRC_POINT_EVALUATION -> {
        if (blsOperation.wnon()) {
          // blsPointEvaluationEffectiveCalls.updateTally(1);
        }
        if (blsOperation.mext()) {
          // blsPointEvaluationFailureCalls.updateTally(1);
        }
      }
      case PRC_BLS_G1_ADD -> {
        if (blsOperation.wnon()) {
          // blsG1AddEffectiveCalls.updateTally(1);
        }
        if (blsOperation.mext()) {
          // blsC1MembershipCalls.updateTally(1);
        }
      }
      case PRC_BLS_G1_MSM -> {
        if (blsOperation.wnon()) {
          // blsBlsG1MsmEffectiveCalls.updateTally(1);
        }
        if (blsOperation.mext()) {
          // blsG1MembershipCalls.updateTally(1);
        }
      }
      case PRC_BLS_G2_ADD -> {
        if (blsOperation.wnon()) {
          // blsG2AddEffectiveCalls.updateTally(1);
        }
        if (blsOperation.mext()) {
          // blsC2MembershipCalls.updateTally(1);
        }
      }
      case PRC_BLS_G2_MSM -> {
        if (blsOperation.wnon()) {
          // blsBlsG2MsmEffectiveCalls.updateTally(1);
        }
        if (blsOperation.mext()) {
          // blsG2MembershipCalls.updateTally(1);
        }
      }
      case PRC_BLS_PAIRING_CHECK -> {
        if (blsOperation.wtrv() || blsOperation.wnon()) {
           /*
          G1  | G2  | Circuit
          P   | inf | G1 membership
          inf | Q   | G2 membership
          inf | inf | none
          */

          // blsG1MembershipCalls.updateTally(blsOperation.trivialPopDueToG2PointCounter());
          // blsG2MembershipCalls.updateTally(blsOperation.trivialPopDueToG1PointCounter());
        }
        if (blsOperation.wtrv()) {
          // blsPairingCheckMillerLoops.updateTally(0);
          // blsPairingCheckFinalExponentiations.updateTally(0);
        }
        if (blsOperation.wnon()) {
          // blsPairingCheckMillerLoops.updateTally(blsOperation.nontrivialPopCounter());
          // blsPairingCheckFinalExponentiations.updateTally(1);
        }
        if (blsOperation.mext()) {
          if (blsOperation.firstPointNotInSubgroupIsSmall()) {
            // blsG1MembershipCalls.updateTally(1);
            // blsG2MembershipCalls.updateTally(0);
          } else {
            // blsG1MembershipCalls.updateTally(0);
            // blsG2MembershipCalls.updateTally(1);
          }
        }
      }
      case PRC_BLS_MAP_FP_TO_G1 -> {
        if (blsOperation.wnon()) {
          // blsBlsG1MapFpToG1EffectiveCalls.updateTally(1);
        }
      }
      case PRC_BLS_MAP_FP2_TO_G2 -> {
        if (blsOperation.wnon()) {
          // blsBlsG1MapFp2ToG2EffectiveCalls.updateTally(1);
        }
      }
    }
  }
}
