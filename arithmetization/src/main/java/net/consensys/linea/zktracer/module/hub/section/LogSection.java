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

package net.consensys.linea.zktracer.module.hub.section;

import java.util.Optional;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.defer.PostTransactionDefer;
import net.consensys.linea.zktracer.module.hub.fragment.ContextFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.ImcFragment;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.MxpCall;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.mmu.MmuCall;
import net.consensys.linea.zktracer.runtime.LogInvocation;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class LogSection implements PostTransactionDefer {

  final LogCommonSection sectionPrequel;
  final ContextFragment finalContextRow;

  final boolean mxpX;
  final boolean oogX;

  Optional<ImcFragment> miscFragment = Optional.empty();
  Optional<MxpCall> mxpSubFragment = Optional.empty();
  Optional<LogInvocation> logData = Optional.empty();

  public LogSection(Hub hub) {
    this.sectionPrequel = new LogCommonSection(hub, ContextFragment.readCurrentContextData(hub));
    hub.addTraceSection(sectionPrequel);
    this.finalContextRow = ContextFragment.executionProvidesEmptyReturnData(hub);

    this.mxpX = hub.pch().exceptions().memoryExpansionException();
    this.oogX = hub.pch().exceptions().outOfGasException();

    if (hub.currentFrame().frame().isStatic()) {
      // static exception
      sectionPrequel.addFragmentsWithoutStack(finalContextRow);
    } else {
      logData = Optional.of(new LogInvocation(hub));
      mxpSubFragment = Optional.of(MxpCall.build(hub));
      miscFragment = Optional.of(ImcFragment.empty(hub)); // TODO: .callMxp(mxpSubFragment.get()));
      hub.defers().postTx(this);
    }
  }

  @Override
  public void runPostTx(Hub hub, WorldView state, Transaction tx, boolean isSuccessful) {
    if (logData.isPresent()) {
      final boolean mmuTrigger = !this.logData.get().reverted() && this.logData.get().size != 0;
      if (mmuTrigger) {
        miscFragment.get().callMmu(MmuCall.LogX(hub, this.logData.get()));
      }
      this.sectionPrequel.addFragment(miscFragment.get());
      if (mxpX || oogX) {
        this.sectionPrequel.addFragment(finalContextRow);
      }
    }
  }

  public static class LogCommonSection extends TraceSection {
    public LogCommonSection(Hub hub, ContextFragment fragment) {
      super(hub);
      this.addFragmentsAndStack(hub, fragment);
    }
  }
}
