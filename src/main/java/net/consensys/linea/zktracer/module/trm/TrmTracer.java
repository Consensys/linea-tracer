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
package net.consensys.linea.zktracer.module.trm;

import static net.consensys.linea.zktracer.module.trm.TrmData.P_BIT_FLIPS_TO_TRUE;

import org.hyperledger.besu.evm.frame.MessageFrame;

import java.math.BigInteger;
import java.util.List;

import net.consensys.linea.zktracer.OpCode;
import net.consensys.linea.zktracer.bytes.UnsignedByte;
import net.consensys.linea.zktracer.module.ModuleTracer;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;

public class TrmTracer implements ModuleTracer {
  private int stamp = 0;

  @Override
  public String jsonKey() {
    return "trm";
  }

  @Override
  public List<OpCode> supportedOpCodes() {
    return List.of(
        OpCode.BALANCE,
        OpCode.EXTCODESIZE,
        OpCode.EXTCODECOPY,
        OpCode.EXTCODEHASH,
        OpCode.CALL,
        OpCode.CALLCODE,
        OpCode.DELEGATECALL,
        OpCode.STATICCALL,
        OpCode.SELFDESTRUCT);
  }

  @Override
  public Object trace(MessageFrame frame) {

    final Bytes32 arg1 = Bytes32.wrap(frame.getStackItem(0));

    final TrmData data = new TrmData(arg1);
    final TrmTrace.Trace.Builder builder = TrmTrace.Trace.Builder.newInstance();
    stamp++;
    for (int ct = 0; ct < maxCounter(data); ct++) {
      builder
          .appendCounter(ct)
          .appendStamp(stamp)
          .appendIsPrec(data.isPrec())
          .appendPbit(data.getPBit(ct))
          .appendByteHi(UnsignedByte.of(data.getArg1().getHigh().get(ct)))
          .appendByteLo(UnsignedByte.of(data.getArg1().getLow().get(ct)))
          .appendArg1Hi(data.getArg1().getHigh().toUnsignedBigInteger())
          .appendArg1Lo(data.getArg1().getLow().toUnsignedBigInteger())
          .appendTrmAddr(data.getTrimmedAddressHi().toUnsignedBigInteger())
          .appendOnes(data.getOnes()[ct]);

      // the accumulator values for i depend on the values for i-1
      data.setAccumulators(ct);
      builder
          // accT(i) = 256 * accT(i-1) + pBit(i) * byteHi(i)
          // same as accHi but only after pBit flips to true
          .appendAccT(
              ct >= P_BIT_FLIPS_TO_TRUE && data.getPBit(ct)
                  ? Bytes.wrap(data.getArg1().getHigh())
                      .slice(P_BIT_FLIPS_TO_TRUE, ct - P_BIT_FLIPS_TO_TRUE + 1)
                      .toUnsignedBigInteger()
                  : BigInteger.ZERO)
          .appendAcc1(Bytes.wrap(data.getArg1().getHigh()).slice(0, ct + 1).toUnsignedBigInteger())
          .appendAcc2(Bytes.wrap(data.getArg1().getLow()).slice(0, ct + 1).toUnsignedBigInteger());
    }
    builder.setStamp(stamp);

    return builder.build();
  }

  private int maxCounter(TrmData data) {
    if (data.getTrimmedAddressHi().isZero()) {
      return 1;
    } else {
      return TrmData.MAX_COUNTER;
    }
  }
}
