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

package net.consensys.linea.zktracer.module.hub.fragment.account;

import static net.consensys.linea.zktracer.Trace.Hub.MULTIPLIER___DOM_SUB_STAMPS;

import java.util.Map;
import java.util.Optional;

import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.module.hub.AccountSnapshot;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.DomSubStampsSubFragment;
import net.consensys.linea.zktracer.module.hub.section.halt.EphemeralAccount;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class LondonAccountFragment extends AccountFragment {
  private boolean markedForSelfDestruct;
  private boolean markedForSelfDestructNew;

  public LondonAccountFragment(
      Hub hub,
      AccountSnapshot oldState,
      AccountSnapshot newState,
      Optional<Bytes> addressToTrim,
      DomSubStampsSubFragment domSubStampsSubFragment) {

    super(hub, oldState, newState, addressToTrim, domSubStampsSubFragment);
  }

  @Override
  void traceMarkedForSelfDestruct(Trace.Hub trace) {
    trace
        .pAccountMarkedForSelfdestruct(markedForSelfDestruct)
        .pAccountMarkedForSelfdestructNew(markedForSelfDestructNew);
  }

  @Override
  void traceMarkedForDeletion(Trace.Hub trace) {
    // Those columns appear in Cancun
  }

  @Override
  void traceHadCodeInitially(Trace.Hub trace) {
    // This column appears in Cancun
  }

  @Override
  public void resolveAtEndTransaction(
      Hub hub, WorldView state, Transaction tx, boolean isSuccessful) {
    final Map<EphemeralAccount, Integer> effectiveSelfDestructMap =
        transactionProcessingMetadata.getEffectiveSelfDestructMap();
    final EphemeralAccount ephemeralAccount =
        new EphemeralAccount(oldState().address(), oldState().deploymentNumber());
    if (effectiveSelfDestructMap.containsKey(ephemeralAccount)) {
      final int selfDestructTime = effectiveSelfDestructMap.get(ephemeralAccount);
      markedForSelfDestruct =
          domSubStampsSubFragment().domStamp() > MULTIPLIER___DOM_SUB_STAMPS * selfDestructTime;
      markedForSelfDestructNew = hubStamp >= selfDestructTime;
    } else {
      markedForSelfDestruct = false;
      markedForSelfDestructNew = false;
    }
  }
}
