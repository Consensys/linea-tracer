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
import net.consensys.linea.zktracer.module.hub.Trace;
import net.consensys.linea.zktracer.module.hub.fragment.TraceFragment;
import net.consensys.linea.zktracer.module.hub.fragment.TraceSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.ExpSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.MxpSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.StpSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.mmu.GenericMmuSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.oob.Blake2fPrecompileFirstSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.oob.Blake2fPrecompileSecondSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.oob.Call;
import net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.oob.CalldataloadSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.oob.Create;
import net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.oob.DeploymentReturn;
import net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.oob.ExceptionalCall;
import net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.oob.GenericOobSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.oob.JumpSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.misc.subfragment.oob.SStore;
import net.consensys.linea.zktracer.module.hub.precompiles.PrecompileInvocation;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.opcode.gas.GasConstants;
import net.consensys.linea.zktracer.types.EWord;
import org.hyperledger.besu.evm.account.Account;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.internal.Words;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class MiscFragment implements TraceFragment {
  private final List<TraceSubFragment> subFragments = new ArrayList<>();

  private MiscFragment() {}

  private MiscFragment(final Hub hub) {
    if (hub.pch().signals().mxp()) {
      this.subFragments.add(MxpSubFragment.build(hub));
    }

    if (hub.pch().signals().exp()) {
      this.subFragments.add(new ExpSubFragment(EWord.of(hub.messageFrame().getStackItem(1))));
    }
  }

  public static MiscFragment empty() {
    return new MiscFragment();
  }

  public static MiscFragment forTxInit(final Hub hub) {
    return MiscFragment.empty().withMmu(GenericMmuSubFragment.txInit(hub));
  }

  public static MiscFragment forCall(
      Hub hub, Account callerAccount, Optional<Account> calledAccount) {
    final MiscFragment r = new MiscFragment(hub);

    if (hub.pch().signals().oob()) {
      switch (hub.opCode()) {
        case CALL, STATICCALL, DELEGATECALL, CALLCODE -> {
          if (hub.opCode().equals(OpCode.CALL) && hub.pch().exceptions().any()) {
            r.subFragments.add(new ExceptionalCall(EWord.of(hub.messageFrame().getStackItem(2))));
          } else {
            r.subFragments.add(
                new Call(
                    EWord.of(hub.messageFrame().getStackItem(2)),
                    EWord.of(callerAccount.getBalance()),
                    hub.callStack().depth(),
                    hub.pch().aborts().snapshot()));
          }
        }
        default -> throw new IllegalArgumentException("unexpected opcode for OoB");
      }
    }

    if (hub.pch().signals().stp()) {
      final long gas = Words.clampedToLong(hub.messageFrame().getStackItem(0));
      EWord value = EWord.ZERO;
      if (hub.opCode().isAnyOf(OpCode.CALL, OpCode.CALLCODE)) {
        value = EWord.of(hub.messageFrame().getStackItem(2));
      }

      final long stipend = value.isZero() ? 0 : GasConstants.G_CALL_STIPEND.cost();
      final long upfrontCost = Hub.gp.of(hub.messageFrame(), hub.opCode()).total();

      r.subFragments.add(
          new StpSubFragment(
              hub.opCode().byteValue(),
              EWord.of(gas),
              value,
              calledAccount.isPresent(),
              calledAccount
                  .map(a -> hub.messageFrame().isAddressWarm(a.getAddress()))
                  .orElse(false),
              hub.pch().exceptions().outOfGas(),
              upfrontCost,
              Math.max(
                  Words.unsignedMin(
                      allButOneSixtyFourth(hub.messageFrame().getRemainingGas() - upfrontCost),
                      gas),
                  0),
              stipend));
    }

    return r;
  }

  public static long allButOneSixtyFourth(final long value) {
    return value - value / 64;
  }

  public static MiscFragment forCreate(
      Hub hub, Account creatorAccount, Optional<Account> createeAccount) {
    final MiscFragment r = new MiscFragment(hub);

    if (hub.pch().signals().oob()) {
      switch (hub.currentFrame().opCode()) {
        case CREATE, CREATE2 -> {
          r.subFragments.add(
              new Create(
                  hub.pch().aborts().snapshot(),
                  hub.pch().failures().snapshot(),
                  EWord.of(hub.messageFrame().getStackItem(0)),
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

  public static MiscFragment forOpcode(Hub hub, MessageFrame frame) {
    final MiscFragment r = new MiscFragment(hub);

    if (hub.pch().signals().mmu()) {
      switch (hub.opCode()) {
        case SHA3 -> r.subFragments.add(GenericMmuSubFragment.sha3(hub));
        case CALLDATALOAD -> r.subFragments.add(GenericMmuSubFragment.callDataLoad(hub));
        case CALLDATACOPY -> r.subFragments.add(GenericMmuSubFragment.callDataCopy(hub));
        case CODECOPY -> r.subFragments.add(GenericMmuSubFragment.codeCopy(hub));
        case EXTCODECOPY -> r.subFragments.add(GenericMmuSubFragment.extCodeCopy(hub));
        case RETURNDATACOPY -> r.subFragments.add(GenericMmuSubFragment.returnDataCopy(hub));
        case MLOAD -> r.subFragments.add(GenericMmuSubFragment.mload(hub));
        case MSTORE -> r.subFragments.add(GenericMmuSubFragment.mstore(hub));
        case MSTORE8 -> r.subFragments.add(GenericMmuSubFragment.mstore8(hub));
        case LOG0, LOG1, LOG2, LOG3, LOG4 -> r.subFragments.add(GenericMmuSubFragment.log(hub));
        case CREATE -> r.subFragments.add(GenericMmuSubFragment.create(hub));
        case RETURN -> r.subFragments.add(
            hub.currentFrame().underDeployment()
                ? GenericMmuSubFragment.returnFromDeployment(hub)
                : GenericMmuSubFragment.returnFromCall(hub));
        case CREATE2 -> r.subFragments.add(GenericMmuSubFragment.create2(hub));
        case REVERT -> r.subFragments.add(GenericMmuSubFragment.revert(hub));
      }
    }

    if (hub.pch().signals().oob()) {
      switch (hub.opCode()) {
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

  public MiscFragment withOob(GenericOobSubFragment f) {
    this.subFragments.add(f);
    return this;
  }

  public MiscFragment withOobForPrecompile(final Hub hub, final PrecompileInvocation p, int i) {
    switch (p.precompile()) {
      case EC_RECOVER,
          EC_PAIRING,
          EC_ADD,
          EC_MUL,
          IDENTITY,
          RIPEMD_160,
          SHA2_256 -> throw new IllegalArgumentException("should be called without an index");
      case BLAKE2F -> {
        if (i == 0) {
          this.subFragments.add(new Blake2fPrecompileFirstSubFragment(p));
        } else if (i == 1) {
          this.subFragments.add(new Blake2fPrecompileSecondSubFragment(p));
        } else {
          throw new IllegalArgumentException("invalid i");
        }
      }
      case MODEXP -> {}
    }

    return this;
  }

  public MiscFragment withMmu(GenericMmuSubFragment f) {
    this.subFragments.add(f);
    return this;
  }

  @Override
  public Trace trace(Trace trace) {
    trace.peekAtMiscellaneous(true);

    for (TraceSubFragment subFragment : this.subFragments) {
      subFragment.trace(trace);
    }

    return trace.fillAndValidateRow();
  }

  @Override
  public void postConflationRetcon(Hub hub, WorldView state) {
    for (TraceSubFragment f : this.subFragments) {
      if (f instanceof GenericMmuSubFragment mmuSubFragment) {
        mmuSubFragment.postConflationRetcon(hub);
      }
    }
  }
}
