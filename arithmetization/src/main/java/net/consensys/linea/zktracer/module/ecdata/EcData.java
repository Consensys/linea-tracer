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
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.container.stacked.set.StackedSet;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.ext.Ext;
import net.consensys.linea.zktracer.module.hub.Hub;
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
import org.hyperledger.besu.evm.frame.MessageFrame;

@RequiredArgsConstructor
public class EcData implements Module {
  public static final Set<Address> EC_PRECOMPILES =
      Set.of(Address.ECREC, Address.ALTBN128_ADD, Address.ALTBN128_MUL, Address.ALTBN128_PAIRING);

  @Getter private final StackedSet<EcDataOperation> operations = new StackedSet<>();
  private final Hub hub;
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
  public void tracePreOpcode(MessageFrame frame) {
    /*
    final Address target = Words.toAddress(frame.getStackItem(1));
    if (!EC_PRECOMPILES.contains(target)) {
      return;
    }
    final MemorySpan callDataSource = hub.transients().op().callDataSegment();

    if (target.equals(Address.ALTBN128_PAIRING)
        && (callDataSource.isEmpty() || callDataSource.length() % 192 != 0)) {
      return;
    }

    final Bytes data = hub.transients().op().callData();

    this.ecDataOperation =
        EcDataOperation.of(
            this.wcp, this.ext, hub.precompileId(), addressToPrecompileFlag(target), data);
    this.operations.add(ecDataOperation);
    */
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

    List<EcDataOperation> sortedOperations =
        this.operations.stream().sorted(Comparator.comparingLong(EcDataOperation::id)).toList();

    for (EcDataOperation op : sortedOperations) {
      // TODO: temporary hack, we should have only successful EcData Operations, will be fixed in PR
      // #748
      if (op.successBit()) {
        stamp++;
        op.trace(trace, stamp, previousId);
        previousId = op.id();
      }
    }
  }

  public void callEcdata(
      PrecompileScenarioFragment.PrecompileFlag precompileFlag, Bytes callData, Bytes returnData) {
    this.ecDataOperation =
        EcDataOperation.of(
            this.wcp, this.ext, hub.precompileId(), precompileFlag, callData, returnData);
    this.operations.add(ecDataOperation);

    switch (ecDataOperation.precompileFlag()) {
      case PRC_ECADD -> ecAddEffectiveCall.addPrecompileLimit(1);
      case PRC_ECMUL -> ecMulEffectiveCall.addPrecompileLimit(1);
      case PRC_ECRECOVER -> ecRecoverEffectiveCall.addPrecompileLimit(1);
      case PRC_ECPAIRING -> {
        // TODO @Lorenzo
      }
      default -> throw new IllegalArgumentException("Operation not supported by EcData");
    }
  }
}
