/*
 * Copyright ConsenSys AG.
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
package net.consensys.linea.zktracer.module.mxp;

import org.hyperledger.besu.evm.frame.MessageFrame;

import java.util.List;

import net.consensys.linea.zktracer.OpCode;
import net.consensys.linea.zktracer.bytes.UnsignedByte;
import net.consensys.linea.zktracer.module.ModuleTracer;
import org.apache.tuweni.bytes.Bytes32;

public class MxpTracer implements ModuleTracer {
  public static final String MXP_JSON_KEY = "mxp";
  private static final int LIMB_SIZE = 16;
  private int stamp = 0;

  @Override
  public String jsonKey() {
    return MXP_JSON_KEY;
  }

  @Override
  public List<OpCode> supportedOpCodes() {
    return List.of(OpCode.MLOAD, OpCode.MSTORE, OpCode.MSTORE8);
  }

  @Override
  public Object trace(final MessageFrame frame) {
    final OpCode opCode = OpCode.of(frame.getCurrentOperation().getOpcode());
    final Bytes32 arg1 = Bytes32.wrap(frame.getStackItem(0));
    final Bytes32 arg2 = Bytes32.wrap(frame.getStackItem(1));

    final MxpData data = new MxpData(opCode, arg1, arg2);
    final MxpTrace.Trace.Builder builder = MxpTrace.Trace.Builder.newInstance();

    stamp++;
    for (int ct = 0; ct < LIMB_SIZE; ct++) {
      builder
          .appendMxpStamp(stamp)
          .appendCounter(ct)
          .appendInst(UnsignedByte.of(opCode.value))
          .appendOffset1(data.getOffset1().toUnsignedBigInteger())
          .appendSize1(data.getSize1().toUnsignedBigInteger())
          .appendOffset2(data.getOffset2().toUnsignedBigInteger())
          .appendSize2(data.getSize2().toUnsignedBigInteger());
      //          .appendByte1(UnsignedByte.of(data.getArg1Hi().get(ct)))
      //          .appendByte2(UnsignedByte.of(data.getArg1Lo().get(ct)))
      //          .appendByte3(UnsignedByte.of(data.getArg2Hi().get(ct)))
      //          .appendByte4(UnsignedByte.of(data.getArg2Lo().get(ct)))
      //          .appendByteA(UnsignedByte.of(data.getAdjHi().get(ct)))
      //          .appendByteW(UnsignedByte.of(data.getAdjLo().get(ct)))
      //          .appendAcc1(data.getacc().slice(0, 1 + ct).toUnsignedBigInteger())
      //          .appendAcc2(data.getArg1Lo().slice(0, 1 + ct).toUnsignedBigInteger())
      //          .appendAcc3(data.getArg2Hi().slice(0, 1 + ct).toUnsignedBigInteger())
      //          .appendAcc4(data.getArg2Lo().slice(0, 1 + ct).toUnsignedBigInteger())
      //          .appendAccA(data.getAdjHi().slice(0, 1 + ct).toUnsignedBigInteger())
      //          .appendAccW(data.getAdjLo().slice(0, 1 + ct).toUnsignedBigInteger());
    }

    builder.setStamp(stamp);
    return builder.build();
  }
}
