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

package net.consensys.linea.zktracer.module.hub.fragment.imc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.TraceSubFragment;
import net.consensys.linea.zktracer.module.hub.signals.Exceptions;
import net.consensys.linea.zktracer.module.hub.state.State;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.opcode.OpCodeData;
import net.consensys.linea.zktracer.opcode.gas.BillingRate;
import net.consensys.linea.zktracer.types.EWord;
import org.apache.tuweni.bytes.Bytes;

@RequiredArgsConstructor
public class MxpCall implements TraceSubFragment {

  public final Hub hub;

  @Getter @Setter public boolean mayTriggerNontrivialMmuOperation;

  /** mxpx is short of Memory eXPansion eXception */
  @Getter @Setter public boolean mxpx;

  @Getter @Setter public long memorySizeInWords;
  @Getter @Setter public long gasMxp;

  public static MxpCall build(Hub hub) {
    return new MxpCall(hub);
  }

  static boolean getMemoryExpansionException(Hub hub) {
    return Exceptions.memoryExpansionException(hub.pch().exceptions());
  }

  public OpCodeData getOpCodeData() {
    return this.hub.opCodeData();
  }

  public boolean isDeploys() {
    return getOpCodeData().mnemonic() == OpCode.RETURN & this.hub.currentFrame().isDeployment();
  }

  public long getMemorySizeInWords() {
    return this.hub.messageFrame().memoryWordSize();
  }

  public EWord getOffset1() {
    OpCode opCode = getOpCodeData().mnemonic();
    switch (opCode) {
      case MLOAD,
          MSTORE,
          MSTORE8,
          REVERT,
          RETURN,
          LOG0,
          LOG1,
          LOG2,
          LOG3,
          LOG4,
          SHA3,
          CALLDATACOPY,
          RETURNDATACOPY,
          CODECOPY,
          MCOPY -> {
        return EWord.of(this.hub.messageFrame().getStackItem(0));
      }
      case EXTCODECOPY, CREATE, CREATE2 -> {
        return EWord.of(this.hub.messageFrame().getStackItem(1));
      }
      case DELEGATECALL, STATICCALL -> {
        return EWord.of(this.hub.messageFrame().getStackItem(2));
      }
      case CALL, CALLCODE -> {
        return EWord.of(this.hub.messageFrame().getStackItem(3));
      }
      default -> {
        return EWord.of(0);
      }
    }
  }

  public EWord getOffset2() {
    OpCode opCode = getOpCodeData().mnemonic();
    switch (opCode) {
      case MCOPY -> {
        return EWord.of(this.hub.messageFrame().getStackItem(1));
      }
      case CALL, CALLCODE -> {
        return EWord.of(this.hub.messageFrame().getStackItem(5));
      }
      case DELEGATECALL, STATICCALL -> {
        return EWord.of(this.hub.messageFrame().getStackItem(4));
      }
      default -> {
        return EWord.of(0);
      }
    }
  }

  public EWord getSize1() {
    OpCode opCode = getOpCodeData().mnemonic();
    switch (opCode) {
      case MLOAD, MSTORE -> {
        return EWord.of(32);
      }
      case MSTORE8 -> {
        return EWord.of(1);
      }
      case REVERT, RETURN, LOG0, LOG1, LOG2, LOG3, LOG4, SHA3 -> {
        return EWord.of(this.hub.messageFrame().getStackItem(1));
      }
      case CALLDATACOPY, RETURNDATACOPY, CODECOPY, CREATE, CREATE2, MCOPY -> {
        return EWord.of(this.hub.messageFrame().getStackItem(2));
      }
      case EXTCODECOPY, DELEGATECALL, STATICCALL -> {
        return EWord.of(this.hub.messageFrame().getStackItem(3));
      }
      case CALL, CALLCODE -> {
        return EWord.of(this.hub.messageFrame().getStackItem(4));
      }
      default -> {
        return EWord.of(0);
      }
    }
  }

  public EWord getSize2() {
    OpCode opCode = getOpCodeData().mnemonic();
    switch (opCode) {
      case MCOPY -> {
        return EWord.of(this.hub.messageFrame().getStackItem(2));
      }
      case DELEGATECALL, STATICCALL -> {
        return EWord.of(this.hub.messageFrame().getStackItem(5));
      }
      case CALL, CALLCODE -> {
        return EWord.of(this.hub.messageFrame().getStackItem(6));
      }
      default -> {
        return EWord.of(0);
      }
    }
  }

  public boolean getSize1NonZeroNoMxpx() {
    return !this.mxpx && !getSize1().isZero();
  }

  public boolean getSize2NonZeroNoMxpx() {
    return !this.mxpx && !getSize2().isZero();
  }

  public Bytes getCostBy(BillingRate billingRate) {
    return Bytes.of(
        getOpCodeData().billing().billingRate() == billingRate
            ? getOpCodeData().billing().perUnit().cost()
            : 0);
  }

  public Trace.Hub trace(Trace.Hub trace, State hubState) {
    hubState.incrementMxpStamp();
    return trace
        .pMiscMxpFlag(true)
        .pMiscMxpInst(getOpCodeData().value())
        .pMiscMxpDeploys(isDeploys())
        .pMiscMxpOffset1Hi(getOffset1().hi())
        .pMiscMxpOffset1Lo(getOffset1().lo())
        .pMiscMxpSize1Hi(getSize1().hi())
        .pMiscMxpSize1Lo(getSize1().lo())
        .pMiscMxpOffset2Hi(getOffset2().hi())
        .pMiscMxpOffset2Lo(getOffset2().lo())
        .pMiscMxpSize2Hi(getSize2().hi())
        .pMiscMxpSize2Lo(getSize2().lo())
        .pMiscMxpMtntop(this.mayTriggerNontrivialMmuOperation)
        .pMiscMxpSize1NonzeroNoMxpx(this.getSize1NonZeroNoMxpx())
        .pMiscMxpSize2NonzeroNoMxpx(this.getSize2NonZeroNoMxpx())
        .pMiscMxpMxpx(this.mxpx)
        .pMiscMxpWords(Bytes.ofUnsignedLong(this.memorySizeInWords))
        .pMiscMxpGasMxp(Bytes.ofUnsignedLong(this.gasMxp));
  }
}
