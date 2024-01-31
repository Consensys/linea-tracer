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

package net.consensys.linea.zktracer.module.hub.fragment.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.Signals;
import net.consensys.linea.zktracer.module.hub.Trace;
import net.consensys.linea.zktracer.module.hub.defer.PostExecDefer;
import net.consensys.linea.zktracer.module.hub.fragment.TraceFragment;
import net.consensys.linea.zktracer.module.hub.fragment.TraceSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.ExpSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.MmuSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.MxpSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.oob.Call;
import net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.oob.CalldataloadSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.oob.Create;
import net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.oob.DeploymentReturn;
import net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.oob.ExceptionalCall;
import net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.oob.JumpSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.oob.SStore;
import net.consensys.linea.zktracer.module.hub.subsection.PrecompileScenario;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.EWord;
import org.hyperledger.besu.evm.account.Account;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.operation.Operation;

public class MiscFragment implements TraceFragment, PostExecDefer {
  private final Signals signals;
  private final List<TraceSubFragment> subFragments = new ArrayList<>();

  private MiscFragment(final Hub hub, final MessageFrame frame) {
    this(hub, frame, hub.pch().signals().snapshot());
  }

  private MiscFragment(final Hub hub, final MessageFrame frame, final Signals signals) {
    this.signals = signals;

    if (this.signals.mmu()) {
      this.subFragments.add(new MmuSubFragment(hub, frame));
    }

    if (this.signals.mxp()) {
      this.subFragments.add(MxpSubFragment.build(hub));
    }

    if (this.signals.exp()) {
      this.subFragments.add(new ExpSubFragment(EWord.of(frame.getStackItem(1))));
    }
  }

  public static MiscFragment fromCall(Hub hub, MessageFrame frame, Account callerAccount) {
    final MiscFragment r = new MiscFragment(hub, frame);

    if (r.signals.oob()) {
      switch (hub.currentFrame().opCode()) {
        case CALL, STATICCALL, DELEGATECALL, CALLCODE -> {
          if (hub.currentFrame().opCode().equals(OpCode.CALL) && hub.pch().exceptions().any()) {
            r.subFragments.add(new ExceptionalCall(EWord.of(frame.getStackItem(2))));
          } else {
            r.subFragments.add(
                new Call(
                    EWord.of(frame.getStackItem(2)),
                    EWord.of(callerAccount.getBalance()),
                    hub.callStack().depth(),
                    hub.pch().aborts().snapshot()));
          }
        }
        default -> throw new IllegalArgumentException("unexpected opcode for OoB");
      }
    }

    return r;
  }

  public static MiscFragment fromCall(
      Hub hub, MessageFrame frame, Account creatorAccount, Optional<Account> createeAccount) {
    final MiscFragment r = new MiscFragment(hub, frame);

    if (r.signals.oob()) {
      switch (hub.currentFrame().opCode()) {
        case CREATE, CREATE2 -> {
          r.subFragments.add(
              new Create(
                  hub.pch().aborts().snapshot(),
                  hub.pch().failures().snapshot(),
                  EWord.of(frame.getStackItem(0)),
                  EWord.of(creatorAccount.getBalance()),
                  createeAccount.map(Account::getNonce).orElse(0L),
                  createeAccount.map(Account::hasCode).orElse(false),
                  hub.callStack().depth()));
        }
        default -> throw new IllegalArgumentException("unexpected opcode for OoB");
      }
    }

    return r;
  }

  public static MiscFragment fromOpcode(Hub hub, MessageFrame frame) {
    final MiscFragment r = new MiscFragment(hub, frame);

    if (r.signals.oob()) {
      switch (hub.currentFrame().opCode()) {
        case JUMP, JUMPI -> r.subFragments.add(new JumpSubFragment(hub, frame));
        case CALLDATALOAD -> r.subFragments.add(CalldataloadSubFragment.build(hub, frame));
        case SSTORE -> {
          r.subFragments.add(new SStore(frame.getRemainingGas()));
        }
        case RETURN -> {
          if (hub.currentFrame().underDeployment()) {
            r.subFragments.add(new DeploymentReturn(EWord.of(frame.getStackItem(1))));
          }
        }
        default -> throw new IllegalArgumentException("unexpected opcode for OoB");
      }
    }

    return r;
  }

  public static MiscFragment forPrecompile(
      final Hub hub,
      final MessageFrame frame,
      final PrecompileScenario scenario,
      TraceSubFragment... subFragments) {
    final MiscFragment r = new MiscFragment(hub, frame, Signals.fromPrecompile(scenario));

    if (r.signals.oob()) {}

    r.subFragments.addAll(List.of(subFragments));

    return r;
  }

  @Override
  public Trace trace(Trace trace) {
    trace
        .peekAtMiscellaneous(true)
        .pMiscellaneousMmuFlag(this.signals.mmu())
        .pMiscellaneousMxpFlag(this.signals.mxp())
        .pMiscellaneousOobFlag(this.signals.oob())
        .pMiscellaneousStpFlag(this.signals.stp())
        .pMiscellaneousExpFlag(this.signals.exp());

    for (TraceSubFragment subFragment : this.subFragments) {
      subFragment.trace(trace);
    }

    return trace;
  }

  @Override
  public void runPostExec(Hub hub, MessageFrame frame, Operation.OperationResult operationResult) {
    for (TraceSubFragment f : this.subFragments) {
      if (f instanceof MmuSubFragment mmuSubFragment) {
        mmuSubFragment.runPostExec(hub, frame, operationResult);
      }
    }
  }
}
