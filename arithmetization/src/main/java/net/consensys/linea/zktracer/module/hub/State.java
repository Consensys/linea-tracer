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

package net.consensys.linea.zktracer.module.hub;

import java.util.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.container.StackedContainer;
import net.consensys.linea.zktracer.module.hub.State.TxState.Stamps;
import net.consensys.linea.zktracer.module.hub.fragment.storage.StorageFragment;
import net.consensys.linea.zktracer.module.hub.signals.PlatformController;
import net.consensys.linea.zktracer.types.EWord;
import org.hyperledger.besu.datatypes.Address;

public class State implements StackedContainer {
  private final Deque<TxState> state = new ArrayDeque<>();

  State() {}

  public TxState current() {
    return this.state.peek();
  }

  public Stamps stamps() {
    return this.current().stamps;
  }

  @Getter @Setter HubProcessingPhase processingPhase;

  @RequiredArgsConstructor
  @EqualsAndHashCode
  @Getter
  public static class StorageSlotIdentifier {
    final Address address;
    final int deploymentNumber;
    final EWord storageKey;
  }

  public void updateOrInsertStorageSlotOccurrence(
      StorageSlotIdentifier slotIdentifier, StorageFragment storageFragment) {
    int size = firstAndLastStorageSlotOccurrences.size();
    HashMap<StorageSlotIdentifier, StorageFragmentPair> current =
        firstAndLastStorageSlotOccurrences.get(size - 1);
    if (current.containsKey(slotIdentifier)) {
      current.get(slotIdentifier).update(storageFragment);
    } else {
      current.put(slotIdentifier, new State.StorageFragmentPair(storageFragment));
    }
  }

  @Getter
  public static class StorageFragmentPair {
    final StorageFragment firstOccurrence;
    @Setter StorageFragment finalOccurrence;

    public StorageFragmentPair(StorageFragment firstOccurrence) {
      this.firstOccurrence = firstOccurrence;
      this.finalOccurrence = firstOccurrence;
    }

    public void update(StorageFragment current) {
      setFinalOccurrence(current);
    }
  }

  // initialized here
  public ArrayList<HashMap<StorageSlotIdentifier, StorageFragmentPair>>
      firstAndLastStorageSlotOccurrences = new ArrayList<>();

  /**
   * @return the current transaction trace elements
   */
  TxTrace currentTxTrace() {
    return this.current().txTrace;
  }

  /**
   * Concretize the traces of all the accumulated transactions.
   *
   * @param hubTrace the trace builder to write to
   * @return the trace builder
   */
  Trace commit(Trace hubTrace) {
    for (Iterator<TxState> it = this.state.descendingIterator(); it.hasNext(); ) {
      final TxState txState = it.next();
      txState.txTrace().commit(hubTrace);
    }
    return hubTrace;
  }

  int txCount() {
    return this.state.size();
  }

  /**
   * @return the cumulated line numbers for all currently traced transactions
   */
  int lineCount() {
    int sum = 0;
    for (TxState s : this.state) {
      sum += s.txTrace.lineCount();
    }
    return sum;
  }

  @Override
  public void enter() {
    if (this.state.isEmpty()) {
      this.state.push(new TxState());
    } else {
      this.state.push(this.current().spinOff());
    }
  }

  @Override
  public void pop() {
    this.state.pop();
  }

  /** Describes the Hub state during a given transaction. */
  @Accessors(fluent = true)
  @Getter
  public static class TxState {
    Stamps stamps;
    TxTrace txTrace;

    TxState() {
      this.stamps = new Stamps();
      this.txTrace = new TxTrace();
    }

    public TxState(Stamps stamps) {
      this.stamps = stamps;
      this.txTrace = new TxTrace();
    }

    TxState spinOff() {
      return new TxState(this.stamps.snapshot());
    }

    /** Stores all the stamps associated to the tracing of a transaction. */
    @Accessors(fluent = true)
    @Getter
    public static class Stamps {
      private int hub = 0;
      private int mmu = 0;
      private int mxp = 0;
      private int hashInfo = 0; // TODO: doesn't exist anymore
      private int log = 0; // TODO: implement this

      public Stamps() {}

      public Stamps(int hubStamp, int mmuStamp, int mxpStamp, int hashInfoStamp) {
        this.hub = hubStamp;
        this.mmu = mmuStamp;
        this.mxp = mxpStamp;
        this.hashInfo = hashInfoStamp;
      }

      public Stamps snapshot() {
        return new Stamps(this.hub, this.mmu, this.mxp, this.hashInfo);
      }

      public void incrementHubStamp() {
        this.hub++;
      }

      void stampSubmodules(final PlatformController platformController) {
        if (platformController.signals().mmu()) {
          this.mmu++;
        }
        if (platformController.signals().mxp()) {
          this.mxp++;
        }
      }
    }
  }
}
