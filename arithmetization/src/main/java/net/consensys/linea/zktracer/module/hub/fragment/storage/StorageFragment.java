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

package net.consensys.linea.zktracer.module.hub.fragment.storage;

import static net.consensys.linea.zktracer.types.AddressUtils.highPart;
import static net.consensys.linea.zktracer.types.AddressUtils.lowPart;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.consensys.linea.zktracer.module.hub.State;
import net.consensys.linea.zktracer.module.hub.Trace;
import net.consensys.linea.zktracer.module.hub.fragment.DomSubStampsSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.TraceFragment;
import net.consensys.linea.zktracer.types.EWord;
import org.hyperledger.besu.datatypes.Address;

import java.util.HashMap;

@RequiredArgsConstructor
@Getter
public final class StorageFragment implements TraceFragment {
  private final State hubState;
  @Setter private StorageFragmentType type;
  private final Address address;
  private final int deploymentNumber;
  private final EWord key;
  private final EWord valOrig;
  private final EWord valCurr;
  private final EWord valNext;
  private final boolean oldWarmth;
  private final boolean newWarmth;
  private final DomSubStampsSubFragment domSubStampsSubFragment;
  private final int blockNumber;

  public Trace trace(Trace trace) {

    HashMap<State.EphemeralStorageSlotIdentifier, State.StorageFragmentPair> current = hubState.firstAndLastStorageSlotOccurrences.get(blockNumber);
    State.EphemeralStorageSlotIdentifier storageSlotIdentifier = new State.EphemeralStorageSlotIdentifier(address, deploymentNumber, key);
    Preconditions.checkArgument(current.containsKey(storageSlotIdentifier));

    final boolean isFirstOccurrence = current.get(storageSlotIdentifier).getFirstOccurrence() == this;
    final boolean isFinalOccurrence = current.get(storageSlotIdentifier).getFinalOccurrence() == this;

    return trace
        .peekAtStorage(true)
        .pStorageAddressHi(highPart(address))
        .pStorageAddressLo(lowPart(address))
        .pStorageDeploymentNumber(deploymentNumber)
        .pStorageStorageKeyHi(key.hi())
        .pStorageStorageKeyLo(key.lo())
        .pStorageValueOrigHi(valOrig.hi())
        .pStorageValueOrigLo(valOrig.lo())
        .pStorageValueCurrHi(valCurr.hi())
        .pStorageValueCurrLo(valCurr.lo())
        .pStorageValueNextHi(valNext.hi())
        .pStorageValueNextLo(valNext.lo())
        .pStorageWarmth(oldWarmth)
        .pStorageWarmthNew(newWarmth)
        .pStorageValueOrigIsZero(valOrig.isZero())
        .pStorageValueCurrIsOrig(valCurr.equals(valOrig))
        .pStorageValueCurrIsZero(valCurr.isZero())
        .pStorageValueNextIsCurr(valNext == valOrig)
        .pStorageValueNextIsZero(valNext.isZero())
        .pStorageValueNextIsOrig(valNext == valOrig)
            .pStorageUnconstrainedFirst(isFirstOccurrence)
    .pStorageUnconstrainedFinal(isFinalOccurrence);
  }
}
