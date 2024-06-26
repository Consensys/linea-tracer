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

package net.consensys.linea.zktracer.module.hub.fragment.imc.call;

import java.math.BigInteger;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.Trace;
import net.consensys.linea.zktracer.module.hub.fragment.TraceSubFragment;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.opcode.gas.GasConstants;
import net.consensys.linea.zktracer.types.EWord;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.account.Account;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.internal.Words;

@Getter
@Setter
@Accessors(fluent = true)
public class StpCall implements TraceSubFragment {
  final Hub hub;
  final long memoryExpansionGas;
  OpCode opCode;
  long gasActual;
  EWord gas; // for CALL's only
  EWord value;
  boolean exists;
  boolean warm;
  long upfrontGasCost;
  boolean outOfGasException;
  long gasPaidOutOfPocket;
  long stipend;

  public StpCall(Hub hub, long memoryExpansionGas) {
    this.hub = hub;
    this.memoryExpansionGas = memoryExpansionGas;
    this.opCode = hub.opCode();
    this.gasActual = hub.messageFrame().getRemainingGas();
    Preconditions.checkArgument(this.opCode.isCall() || this.opCode.isCreate());
  }

  public StpCall stpCallForCalls(Hub hub, long memoryExpansionGas) {
    // set up hub, memoryExpansionGas, opCode and gasActual;
    StpCall stpCall = new StpCall(hub, memoryExpansionGas);
    MessageFrame frame = hub.messageFrame();

    final boolean instructionCanTransferValue = stpCall.opCode.callCanTransferValue();
    final boolean isCALL = stpCall.opCode.equals(OpCode.CALL);
    final Address to = Words.toAddress(frame.getStackItem(1));
    Account toAccount = frame.getWorldUpdater().getAccount(to);
    stpCall.gas = EWord.of(frame.getStackItem(0));
    stpCall.value = (instructionCanTransferValue) ? EWord.of(frame.getStackItem(2)) : EWord.ZERO;
    stpCall.exists = toAccount != null ? !toAccount.isEmpty() : false;
    stpCall.warm = frame.isAddressWarm(to);
    stpCall.upfrontGasCost = memoryExpansionGas;

    final boolean nonzeroValueTransfer = !value.isZero();
    final boolean callWouldLeadToAccountCreation =
        isCALL && nonzeroValueTransfer && !stpCall.exists;
    if (nonzeroValueTransfer) {
      stpCall.upfrontGasCost += GasConstants.G_CALL_VALUE.cost();
    }
    if (stpCall.warm) {
      stpCall.upfrontGasCost += GasConstants.G_WARM_ACCESS.cost();
    } else {
      stpCall.upfrontGasCost += GasConstants.G_COLD_ACCOUNT_ACCESS.cost();
    }
    if (callWouldLeadToAccountCreation) {
      stpCall.upfrontGasCost += GasConstants.G_NEW_ACCOUNT.cost();
    }

    stpCall.outOfGasException = stpCall.gasActual < stpCall.upfrontGasCost;
    stpCall.gasPaidOutOfPocket = stpCall.computeGasPaidOutOfPocketForCalls();
    stpCall.stipend = !outOfGasException && nonzeroValueTransfer ? GasConstants.G_CALL_STIPEND.cost() : 0;

    return stpCall;
  }

  private long computeGasPaidOutOfPocketForCalls() {
    if (outOfGasException) {
      return gasPaidOutOfPocket = 0;
    } else {
      long gasMinusUpfront = gasActual - upfrontGasCost;
      long oneSixtyFourths = gasMinusUpfront >> 6;
      long maxGasAllowance = gasMinusUpfront - oneSixtyFourths;
      return
              gas().toUnsignedBigInteger().compareTo(BigInteger.valueOf(maxGasAllowance)) > 0
                      ? maxGasAllowance
                      : gas.toLong();
    }
  }

  public StpCall stpCallForCreates(Hub hub, long memoryExpansionGas) {
    StpCall stpCall = new StpCall(hub, memoryExpansionGas);
    MessageFrame frame = hub.messageFrame();

    stpCall.gas = EWord.ZERO; // irrelevant
    stpCall.value = EWord.of(frame.getStackItem(0));
    stpCall.exists = false; // irrelevant
    stpCall.warm = false; // irrelevant
    stpCall.upfrontGasCost = GasConstants.G_CREATE.cost() + stpCall.memoryExpansionGas;
    stpCall.outOfGasException = stpCall.gasActual < stpCall.upfrontGasCost;
    stpCall.gasPaidOutOfPocket = stpCall.computeGasPaidOutOfPocketForCreates();
    stpCall.stipend = 0; // irrelevant
    return stpCall;
  }

  private long computeGasPaidOutOfPocketForCreates() {
    if (outOfGasException) {
      return 0;
    } else {
      long gasMinusUpfrontCost = gasActual - upfrontGasCost;
      return gasMinusUpfrontCost - (gasMinusUpfrontCost >> 6);
    }
  }

  @Override
  public Trace trace(Trace trace) {
    return trace
        .pMiscStpFlag(true)
        .pMiscStpInstruction(opCode.byteValue())
        .pMiscStpGasHi(gas.hi())
        .pMiscStpGasLo(gas.lo())
        .pMiscStpValueHi(value.hi())
        .pMiscStpValueLo(value.lo())
        .pMiscStpExists(exists)
        .pMiscStpWarmth(warm)
        .pMiscStpOogx(outOfGasException)
        .pMiscStpGasUpfrontGasCost(Bytes.ofUnsignedLong(upfrontGasCost))
        .pMiscStpGasPaidOutOfPocket(Bytes.ofUnsignedLong(gasPaidOutOfPocket))
        .pMiscStpGasStipend(stipend);
  }
}
