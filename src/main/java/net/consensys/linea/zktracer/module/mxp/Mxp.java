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

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.consensys.linea.zktracer.bytes.Bytes16;
import net.consensys.linea.zktracer.bytes.UnsignedByte;
import net.consensys.linea.zktracer.bytestheta.BaseBytes;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.opcode.OpCodeData;
import net.consensys.linea.zktracer.opcode.OpCodes;
import net.consensys.linea.zktracer.opcode.gas.MxpType;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.apache.tuweni.units.bigints.UInt256;
import org.hyperledger.besu.evm.frame.MessageFrame;

/** Implementation of a {@link Module} for memory expansion. */
public class Mxp implements Module {
  public static final UInt256 TWO_POW_128 = UInt256.ONE.shiftLeft(128);
  public static final UInt256 TWO_POW_32 = UInt256.ONE.shiftLeft(32);

  public static final String MXP_JSON_KEY = "mxp";
  final Trace.TraceBuilder trace = Trace.builder();
  private int stamp = 0;

  /** A set of the operations to trace */
  private final Map<OpCode, Map<Bytes32, Bytes32>> chunks = new HashMap<>();

  private MxpType typeMxp;

  @Override
  public String jsonKey() {
    return MXP_JSON_KEY;
  }

  @Override
  public final List<OpCode> supportedOpCodes() {
    return List.of(OpCode.MLOAD, OpCode.MSTORE, OpCode.MSTORE8);
  }

  @Override
  public void trace(MessageFrame frame) {
    final OpCodeData opCodeData = OpCodes.of(frame.getCurrentOperation().getOpcode());
    final OpCode opCode = opCodeData.mnemonic();

    // create a data object to do the work
    final MxpData mxpData = new MxpData(opCodeData);

    // sanity check
    //    sanityCheck(opCode, scope, mxpData);

    final Bytes32 arg1 = Bytes32.leftPad(frame.getStackItem(0));
    final Bytes16 arg1Hi = Bytes16.wrap(arg1.slice(0, 16));
    final Bytes32 arg1Lo = Bytes32.leftPad(arg1.slice(16));

    Bytes32 arg2 = Bytes32.ZERO;
    if (opCode.getData().numberOfArguments() > 1) {
      arg2 = Bytes32.leftPad(frame.getStackItem(1));
    }
    final Bytes16 arg2Hi = Bytes16.wrap(arg2.slice(0, 16));
    final Bytes16 arg2Lo = Bytes16.wrap(arg2.slice(16));

    this.typeMxp = opCode.getData().billing().type();

    stamp++;
    // TODO what if no arg2
    this.chunks
        .computeIfAbsent(OpCode.of(frame.getCurrentOperation().getOpcode()), x -> new HashMap<>())
        .put(arg1, arg2);
    BaseBytes off1 =
        BaseBytes.fromBytes32(Bytes32.leftPad(Bytes.wrap(mxpData.getOffset1().toByteArray())));
    BaseBytes off2 =
        BaseBytes.fromBytes32(Bytes32.leftPad(Bytes.wrap(mxpData.getOffset2().toByteArray())));
    BaseBytes s1 =
        BaseBytes.fromBytes32(Bytes32.leftPad(Bytes.wrap(mxpData.getSize1().toByteArray())));
    BaseBytes s2 =
        BaseBytes.fromBytes32(Bytes32.leftPad(Bytes.wrap(mxpData.getSize2().toByteArray())));
    Bytes32 acc1Bytes32 = Bytes32.leftPad(Bytes.wrap(mxpData.getAcc1().toByteArray()));
    Bytes32 acc2Bytes32 = Bytes32.leftPad(Bytes.wrap(mxpData.getAcc2().toByteArray()));
    Bytes32 acc3Bytes32 = Bytes32.leftPad(Bytes.wrap(mxpData.getAcc3().toByteArray()));
    Bytes32 acc4Bytes32 = Bytes32.leftPad(Bytes.wrap(mxpData.getAcc4().toByteArray()));

    for (int i = 0; i < 16; i++) {

      trace
          .ct(BigInteger.valueOf(i))
          .inst(BigInteger.valueOf(opCodeData.value()))
          .offset1Hi(off1.getHigh().toUnsignedBigInteger())
          .offset1Lo(off1.getLow().toUnsignedBigInteger())
          .offset2Hi(off2.getHigh().toUnsignedBigInteger())
          .offset2Lo(off2.getLow().toUnsignedBigInteger())
          .size1Hi(s1.getHigh().toUnsignedBigInteger())
          .size1Lo(s1.getLow().toUnsignedBigInteger())
          .size2Hi(s2.getHigh().toUnsignedBigInteger())
          .size2Lo(s2.getLow().toUnsignedBigInteger())
          .mxpx(mxpData.isMxpx())
          .roob(mxpData.isRoob())
          .noop(mxpData.isNoop())
          //              .comp(mxpData.isComp()) // TODO comp is boolean
          .acc1(acc1Bytes32.slice(0, 1 + i).toUnsignedBigInteger())
          .acc2(acc2Bytes32.slice(0, 1 + i).toUnsignedBigInteger())
          .acc3(acc3Bytes32.slice(0, 1 + i).toUnsignedBigInteger())
          .acc4(acc4Bytes32.slice(0, 1 + i).toUnsignedBigInteger())
          //              .accA()
          .byte1(UnsignedByte.of(arg1Hi.get(i)))
          .byte2(UnsignedByte.of(arg1Lo.get(i)))
          .byte3(UnsignedByte.of(arg2Hi.get(i)))
          .byte4(UnsignedByte.of(arg2Lo.get(i)))
          .stamp(BigInteger.valueOf(stamp))
          .validateRow();
    }
  }

  @Override
  public Object commit() {
    for (Map.Entry<OpCode, Map<Bytes32, Bytes32>> op : this.chunks.entrySet()) {
      OpCode opCode = op.getKey();
      for (Map.Entry<Bytes32, Bytes32> args : op.getValue().entrySet()) {
        // TODO implement this with dynamic args
        //        this.traceOperation(opCode, args.getKey(), args.getValue());
      }
    }
    return new MxpTrace(trace.build());
  }

  //  public void sanityCheck(OpCode op, ScopeContext scope, MxpData data) {
  ////    boolean gasExceptionInGeth = err instanceof ErrOutOfGas || err instanceof
  // ErrGasUintOverflow;
  //    if (data.roob || data.mxpx) {
  //      if (!gasExceptionInGeth) {
  //        throw new RuntimeException("MXP: data.roob || data.mxpx should lead to a gas error in
  // geth");
  //      }
  //      return;
  //    }
  //
  //    // The following code needs the correct implementation of the missing methods and classes
  //    // ...
  //  }
}
