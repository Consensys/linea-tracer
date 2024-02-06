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

import static net.consensys.linea.zktracer.module.UtilCalculator.allButOneSixtyFourth;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.Trace;
import net.consensys.linea.zktracer.module.hub.fragment.TraceFragment;
import net.consensys.linea.zktracer.module.hub.fragment.TraceSubFragment;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.ExpCall;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.MxpCall;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.StpCall;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.mmu.MmuCall;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.oob.OobCall;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.oob.opcodes.Call;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.oob.opcodes.CallDataLoad;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.oob.opcodes.Create;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.oob.opcodes.DeploymentReturn;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.oob.opcodes.ExceptionalCall;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.oob.opcodes.Jump;
import net.consensys.linea.zktracer.module.hub.fragment.misc.call.oob.opcodes.SStore;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.opcode.gas.GasConstants;
import net.consensys.linea.zktracer.types.EWord;
import org.hyperledger.besu.evm.account.Account;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.internal.Words;
import org.hyperledger.besu.evm.worldstate.WorldView;

/**
 * IMCFragments embed data required for Inter-Module Communication, i.e. data that are required to
 * correctly trigger other modules from the Hub.
 */
public class ImcFragment implements TraceFragment {
  /**
   * the list of modules to trigger withing this fragment. TODO: a method to ensure there is never a
   * call to the same module twice should be implemented
   */
  private final List<TraceSubFragment> moduleCalls = new ArrayList<>();

  /** This class should only be instantiated through specialized static methods. */
  private ImcFragment() {}

  private ImcFragment(final Hub hub) {
    if (hub.pch().signals().mxp()) {
      this.moduleCalls.add(MxpCall.build(hub));
    }

    if (hub.pch().signals().exp()) {
      this.moduleCalls.add(new ExpCall(EWord.of(hub.messageFrame().getStackItem(1))));
    }
  }

  /**
   * Create an empty ImcFragment to be filled with specialized methods.
   *
   * @return an empty ImcFragment
   */
  public static ImcFragment empty() {
    return new ImcFragment();
  }

  /**
   * Create an ImcFragment to be used in the transaction initialization phase.
   *
   * @param hub the execution context
   * @return the ImcFragment for the TxInit phase
   */
  public static ImcFragment forTxInit(final Hub hub) {
    return ImcFragment.empty().callMmu(MmuCall.txInit(hub));
  }

  /**
   * Create an ImcFragment to be used when executing a *CALL.
   *
   * @param hub the execution context
   * @param callerAccount the caller account
   * @param calledAccount the (maybe non-existing) called account
   * @return the ImcFragment for the *CALL
   */
  public static ImcFragment forCall(
      Hub hub, Account callerAccount, Optional<Account> calledAccount) {
    final ImcFragment r = new ImcFragment(hub);

    if (hub.pch().signals().oob()) {
      switch (hub.opCode()) {
        case CALL, STATICCALL, DELEGATECALL, CALLCODE -> {
          if (hub.opCode().equals(OpCode.CALL) && hub.pch().exceptions().any()) {
            r.moduleCalls.add(new ExceptionalCall(EWord.of(hub.messageFrame().getStackItem(2))));
          } else {
            r.moduleCalls.add(
                new Call(
                    EWord.of(hub.messageFrame().getStackItem(2)),
                    EWord.of(callerAccount.getBalance()),
                    hub.callStack().depth(),
                    hub.pch().aborts().snapshot()));
          }
        }
        default -> throw new IllegalArgumentException("unexpected opcode for IMC/CALL");
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

      r.moduleCalls.add(
          new StpCall(
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

  public static ImcFragment forCreate(
      Hub hub, Account creatorAccount, Optional<Account> createeAccount) {
    final ImcFragment r = new ImcFragment(hub);

    if (hub.pch().signals().oob()) {
      switch (hub.currentFrame().opCode()) {
        case CREATE, CREATE2 -> {
          r.moduleCalls.add(
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

  public static ImcFragment forOpcode(Hub hub, MessageFrame frame) {
    final ImcFragment r = new ImcFragment(hub);

    if (hub.pch().signals().mmu()) {
      switch (hub.opCode()) {
        case SHA3 -> r.moduleCalls.add(MmuCall.sha3(hub));
        case CALLDATALOAD -> r.moduleCalls.add(MmuCall.callDataLoad(hub));
        case CALLDATACOPY -> r.moduleCalls.add(MmuCall.callDataCopy(hub));
        case CODECOPY -> r.moduleCalls.add(MmuCall.codeCopy(hub));
        case EXTCODECOPY -> r.moduleCalls.add(MmuCall.extCodeCopy(hub));
        case RETURNDATACOPY -> r.moduleCalls.add(MmuCall.returnDataCopy(hub));
        case MLOAD -> r.moduleCalls.add(MmuCall.mload(hub));
        case MSTORE -> r.moduleCalls.add(MmuCall.mstore(hub));
        case MSTORE8 -> r.moduleCalls.add(MmuCall.mstore8(hub));
        case LOG0, LOG1, LOG2, LOG3, LOG4 -> r.moduleCalls.add(MmuCall.log(hub));
        case CREATE -> r.moduleCalls.add(MmuCall.create(hub));
        case RETURN -> r.moduleCalls.add(
            hub.currentFrame().underDeployment()
                ? MmuCall.returnFromDeployment(hub)
                : MmuCall.returnFromCall(hub));
        case CREATE2 -> r.moduleCalls.add(MmuCall.create2(hub));
        case REVERT -> r.moduleCalls.add(MmuCall.revert(hub));
      }
    }

    if (hub.pch().signals().oob()) {
      switch (hub.opCode()) {
        case JUMP, JUMPI -> r.moduleCalls.add(new Jump(hub, frame));
        case CALLDATALOAD -> r.moduleCalls.add(CallDataLoad.build(hub, frame));
        case SSTORE -> {
          r.moduleCalls.add(new SStore(frame.getRemainingGas()));
        }
        case RETURN -> {
          if (hub.currentFrame().underDeployment()) {
            r.moduleCalls.add(new DeploymentReturn(EWord.of(frame.getStackItem(1))));
          }
        }
        default -> throw new IllegalArgumentException("unexpected opcode for OoB");
      }
    }

    return r;
  }

  public ImcFragment callOob(OobCall f) {
    this.moduleCalls.add(f);
    return this;
  }

  public ImcFragment callMmu(MmuCall f) {
    this.moduleCalls.add(f);
    return this;
  }

  @Override
  public Trace trace(Trace trace) {
    trace.peekAtMiscellaneous(true);

    for (TraceSubFragment subFragment : this.moduleCalls) {
      subFragment.trace(trace);
    }

    return trace.fillAndValidateRow();
  }

  @Override
  public void postConflationRetcon(Hub hub, WorldView state) {
    for (TraceSubFragment f : this.moduleCalls) {
      if (f instanceof MmuCall mmuCall) {
        mmuCall.postConflationRetcon(hub);
      }
    }
  }
}
