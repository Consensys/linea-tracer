/*
 * Copyright contributors to Hyperledger Besu
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

package net.consensys.linea.zktracer.module.stp;

import java.math.BigInteger;
import java.util.List;

import net.consensys.linea.zktracer.OpCode;
import net.consensys.linea.zktracer.bytes.Bytes16;
import net.consensys.linea.zktracer.module.Module;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.evm.frame.MessageFrame;

public abstract class Stp implements Module {

  @Override
  public String jsonKey() {
    return "stp";
  }

  private int stamp = 0;

  public List<OpCode> supportedOpCodes() {
    return List.of(OpCode.CALL, OpCode.CALLCODE, OpCode.SAR);
  }

  final Trace.TraceBuilder builder = Trace.builder();

  @Override
  public void trace(MessageFrame frame) {
    final OpCode opCode = OpCode.of(frame.getCurrentOperation().getOpcode());
    final Bytes32 gas = Bytes32.wrap(frame.getStackItem(0));
    final Bytes32 arg2 = Bytes32.wrap(frame.getStackItem(1));
    final Bytes16 gasHi = Bytes16.wrap(gas.slice(0, 16));
    final Bytes16 gasLo = Bytes16.wrap(gas.slice(16));
    final BigInteger remainingGas = BigInteger.valueOf(frame.getRemainingGas());
    boolean cctv = isCCTV(opCode);
    boolean call = isCall(opCode);

    stamp++;
    for (int ct = 0; ct < 4; ct++) {
      builder
          .stamp(BigInteger.valueOf(stamp))
          .ct(BigInteger.valueOf(ct))
          .inst(BigInteger.valueOf(opCode.value))
          .extType(!isCall(opCode))
          .cctv(Boolean.valueOf(cctv))
          .gasActl(remainingGas)
          .gasCost(BigInteger.valueOf(0)) // todo
          .gasStpd(BigInteger.valueOf(0)) // todo
          .gasHi(gasHi.toUnsignedBigInteger())
          .gasLo(gasLo.toUnsignedBigInteger())
          .valueHi(BigInteger.valueOf(0)) // todo
          .valueLo(BigInteger.valueOf(0)) // todo
          .wcpFlag(Boolean.valueOf(true)) // todo
          .modFlag(Boolean.valueOf(true)) // todo
          .exoInst(BigInteger.valueOf(0)) // todo
          .arg1Hi(BigInteger.valueOf(0)) // todo
          .arg1Lo(BigInteger.valueOf(0)) // todo
          .arg2Lo(BigInteger.valueOf(0)) // todo;
      ;
    }
  }

  private boolean isCCTV(OpCode opCode) {
    return switch (opCode) {
      case CALL -> true;
      case CALLCODE -> true;
      case DELEGATECALL -> false;
      case STATICCALL -> false;
      default -> throw new RuntimeException("Stipend module was given the wrong opcode");
    };
  }

  /**
   * Returns true if opCode is a call Returns false if opCode is a create Throws an exception
   * otherwise
   *
   * @param opCode the opcode
   */
  private boolean isCall(OpCode opCode) {
    return switch (opCode) {
      case CREATE -> false;
      case CREATE2 -> false;
      case CALL -> true;
      case CALLCODE -> true;
      case DELEGATECALL -> true;
      case STATICCALL -> true;
      default -> throw new RuntimeException("Stipend module was given the wrong opcode");
    };
  }
}
