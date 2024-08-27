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

package net.consensys.linea.zktracer.module.ecdata;

import java.nio.MappedByteBuffer;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.container.stacked.list.StackedList;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.ext.Ext;
import net.consensys.linea.zktracer.module.hub.fragment.scenario.PrecompileScenarioFragment;
import net.consensys.linea.zktracer.module.limits.precompiles.EcAddEffectiveCall;
import net.consensys.linea.zktracer.module.limits.precompiles.EcMulEffectiveCall;
import net.consensys.linea.zktracer.module.limits.precompiles.EcPairingFinalExponentiations;
import net.consensys.linea.zktracer.module.limits.precompiles.EcPairingG2MembershipCalls;
import net.consensys.linea.zktracer.module.limits.precompiles.EcPairingMillerLoops;
import net.consensys.linea.zktracer.module.limits.precompiles.EcRecoverEffectiveCall;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;

@RequiredArgsConstructor
public class EcData implements Module {
  public static final Set<Address> EC_PRECOMPILES =
      Set.of(Address.ECREC, Address.ALTBN128_ADD, Address.ALTBN128_MUL, Address.ALTBN128_PAIRING);

  @Getter private final StackedList<EcDataOperation> operations = new StackedList<>();
  private final Wcp wcp;
  private final Ext ext;

  private final EcAddEffectiveCall ecAddEffectiveCall;
  private final EcMulEffectiveCall ecMulEffectiveCall;
  private final EcRecoverEffectiveCall ecRecoverEffectiveCall;

  private final EcPairingG2MembershipCalls ecPairingG2MembershipCalls;
  private final EcPairingMillerLoops ecPairingMillerLoops;
  private final EcPairingFinalExponentiations ecPairingFinalExponentiations;

  @Getter private EcDataOperation ecDataOperation;

  @Override
  public String moduleKey() {
    return "EC_DATA";
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

  @Override
  public void commit(List<MappedByteBuffer> buffers) {
    final Trace trace = new Trace(buffers);
    int stamp = 0;
    long previousId = 0;
    for (EcDataOperation op : operations) {
      stamp++;
      op.trace(trace, stamp, previousId);
      previousId = op.id();
    }
  }

  public void callEcData(
      final int id,
      final PrecompileScenarioFragment.PrecompileFlag precompileFlag,
      final Bytes callData,
      final Bytes returnData) {
    this.ecDataOperation =
        EcDataOperation.of(this.wcp, this.ext, id, precompileFlag, callData, returnData);
    this.operations.add(ecDataOperation);

    switch (ecDataOperation.precompileFlag()) {
      case PRC_ECADD -> ecAddEffectiveCall.addPrecompileLimit(1);
      case PRC_ECMUL -> ecMulEffectiveCall.addPrecompileLimit(1);
      case PRC_ECRECOVER -> ecRecoverEffectiveCall.addPrecompileLimit(1);
      case PRC_ECPAIRING -> {
        // TODO: @Olivier @Lorenzo: review

        // ecPairingG2MembershipCalls case
        // NOTE: see EC_DATA specs Figure 3.5 for a graphical representation of this case analysis
        if (!ecDataOperation.internalChecksPassed()) {
          ecPairingG2MembershipCalls.addPrecompileLimit(0);
          // The circuit is never invoked in the case of internal checks failing
        }
        // NOTE: the && of the conditions may seem not necessary since in the specs
        // !internalChecksPassed => !notOnG2AccMax
        // however, in EcDataOperation implementation the notOnG2AccMax takes into consideration
        // only large points G2 membership
        // , and it has to be && with internalChecksPassed to compute the actual
        // NOT_ON_G2_ACC_MAX to trace
        if (ecDataOperation.internalChecksPassed() && ecDataOperation.notOnG2AccMax()) {
          ecPairingG2MembershipCalls.addPrecompileLimit(1);
          // The circuit is invoked only once if there is at least one point predicted to be not on G2
        }
        if (ecDataOperation.internalChecksPassed()
            && !ecDataOperation.notOnG2AccMax()
            && ecDataOperation.overallTrivialPairing().getLast()) {
          ecPairingG2MembershipCalls.addPrecompileLimit(0);
          // The circuit is never invoked in the case of a trivial pairing
        }
        if (ecDataOperation.internalChecksPassed()
            && !ecDataOperation.notOnG2AccMax()
            && !ecDataOperation.overallTrivialPairing().getLast()) {
          ecPairingG2MembershipCalls.addPrecompileLimit(
              ecDataOperation.circuitSelectorG2MembershipCounter());
          // The circuit is invoked as many times as there are points predicted to be on G2
        }

        final boolean predictedToBeValidSetOfPairings =
            ecDataOperation.circuitSelectorEcPairingCounter() > 0;

        // ecPairingMillerLoops case
        // NOTE: the number of Miller Loops is equal to the number of pairings in a predicted to be
        // valid set
        // , 0 otherwise
        ecPairingMillerLoops.addPrecompileLimit(
            predictedToBeValidSetOfPairings ? ecDataOperation.totalPairings() : 0);

        // ecPairingFinalExponentiation case
        // NOTE: the number of final exponentiation is just one for a predicted to be valid set
        // of pairings, 0 otherwise
        ecPairingFinalExponentiations.addPrecompileLimit(
            predictedToBeValidSetOfPairings ? 1 : 0); // See https://eprint.iacr.org/2008/490.pdf
      }
      default -> throw new IllegalArgumentException("Operation not supported by EcData");
    }
  }
}
