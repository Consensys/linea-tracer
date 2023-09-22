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

import net.consensys.linea.zktracer.bytes.UnsignedByte;
import net.consensys.linea.zktracer.container.stacked.list.StackedList;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.opcode.OpCodeData;
import net.consensys.linea.zktracer.opcode.OpCodes;
import net.consensys.linea.zktracer.opcode.gas.BillingRate;
import net.consensys.linea.zktracer.opcode.gas.MxpType;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.evm.frame.MessageFrame;

/** Implementation of a {@link Module} for memory expansion. */
public class Mxp implements Module {
  public static final String MXP_JSON_KEY = "mxp";
  final Trace.TraceBuilder trace = Trace.builder();
  private int stamp = 0;

  private Hub hub;

  /** A set of the operations to trace */
  private final StackedList<Chunk> chunks = new StackedList<>();

  @Override
  public String jsonKey() {
    return MXP_JSON_KEY;
  }

  public Mxp(Hub hub) {
    this.hub = hub;
  }

  // TODO: update tests and eliminate this constructor
  public Mxp() {}

  // TODO: use the one defined here instead zktracer/bytes/conversions.java
  public static Bytes bigIntegerToBytes(BigInteger big) {
    byte[] byteArray;
    byteArray = big.toByteArray();
    Bytes bytes;
    if (byteArray[0] == 0) {
      Bytes tmp = Bytes.wrap(byteArray);
      bytes = Bytes.wrap(tmp.slice(1, tmp.size() - 1));
    } else {
      bytes = Bytes.wrap(byteArray);
    }
    return bytes;
  }

  @Override
  public void tracePreOpcode(MessageFrame frame) { // This will be renamed to tracePreOp
    final OpCodeData opCodeData = OpCodes.of(frame.getCurrentOperation().getOpcode());

    // create a data object to do the work
    final MxpData mxpData = new MxpData(opCodeData, frame, hub);

    // sanity check
    //    sanityCheck(opCode, scope, mxpData);

    stamp++;

    Bytes32 acc1Bytes32 = Bytes32.leftPad(bigIntegerToBytes(mxpData.getAcc1()));
    Bytes32 acc2Bytes32 = Bytes32.leftPad(bigIntegerToBytes(mxpData.getAcc2()));
    Bytes32 acc3Bytes32 = Bytes32.leftPad(bigIntegerToBytes(mxpData.getAcc3()));
    Bytes32 acc4Bytes32 = Bytes32.leftPad(bigIntegerToBytes(mxpData.getAcc4()));
    Bytes32 accABytes32 = Bytes32.leftPad(bigIntegerToBytes(mxpData.getAccA()));
    Bytes32 accWBytes32 = Bytes32.leftPad(bigIntegerToBytes(mxpData.getAccW()));
    Bytes32 accQBytes32 = Bytes32.leftPad(bigIntegerToBytes(mxpData.getAccQ()));

    int maxCt = mxpData.maxCt();
    int maxCtComplement = 32 - maxCt;

    for (int i = 0; i < maxCt; i++) {

      trace
          .stamp(BigInteger.valueOf(stamp))
          .cn(BigInteger.valueOf(hub.currentFrame().contextNumber()))
          .ct(BigInteger.valueOf(i))
          .roob(mxpData.isRoob())
          .noop(mxpData.isNoop())
          .mxpx(mxpData.isMxpx())
          .inst(BigInteger.valueOf(opCodeData.value()))
          .mxpType1(opCodeData.billing().type() == MxpType.TYPE_1)
          .mxpType2(opCodeData.billing().type() == MxpType.TYPE_2)
          .mxpType3(opCodeData.billing().type() == MxpType.TYPE_3)
          .mxpType4(opCodeData.billing().type() == MxpType.TYPE_4)
          .mxpType5(opCodeData.billing().type() == MxpType.TYPE_5)
          .gword(
              BigInteger.valueOf(
                  opCodeData.billing().billingRate() == BillingRate.BY_WORD
                      ? opCodeData.billing().perUnit().cost()
                      : 0))
          .gbyte(
              BigInteger.valueOf(
                  opCodeData.billing().billingRate() == BillingRate.BY_BYTE
                      ? opCodeData.billing().perUnit().cost()
                      : 0))
          .deploys(mxpData.isDeploys())
          .offset1Hi(mxpData.getOffset1().hiBigInt())
          .offset1Lo(mxpData.getOffset1().loBigInt())
          .offset2Hi(mxpData.getOffset2().hiBigInt())
          .offset2Lo(mxpData.getOffset2().loBigInt())
          .size1Hi(mxpData.getSize1().hiBigInt())
          .size1Lo(mxpData.getSize1().loBigInt())
          .size2Hi(mxpData.getSize2().hiBigInt())
          .size2Lo(mxpData.getSize2().loBigInt())
          .maxOffset1(mxpData.getMaxOffset1())
          .maxOffset2(mxpData.getMaxOffset2())
          .maxOffset(mxpData.getMaxOffset())
          .comp(mxpData.isComp())
          .acc1(acc1Bytes32.slice(maxCtComplement, 1 + i).toUnsignedBigInteger())
          .acc2(acc2Bytes32.slice(maxCtComplement, 1 + i).toUnsignedBigInteger())
          .acc3(acc3Bytes32.slice(maxCtComplement, 1 + i).toUnsignedBigInteger())
          .acc4(acc4Bytes32.slice(maxCtComplement, 1 + i).toUnsignedBigInteger())
          .accA(accABytes32.slice(maxCtComplement, 1 + i).toUnsignedBigInteger())
          .accW(accWBytes32.slice(maxCtComplement, 1 + i).toUnsignedBigInteger())
          .accQ(accQBytes32.slice(maxCtComplement, 1 + i).toUnsignedBigInteger())
          .byte1(UnsignedByte.of(acc1Bytes32.get(maxCtComplement + i)))
          .byte2(UnsignedByte.of(acc2Bytes32.get(maxCtComplement + i)))
          .byte3(UnsignedByte.of(acc3Bytes32.get(maxCtComplement + i)))
          .byte4(UnsignedByte.of(acc4Bytes32.get(maxCtComplement + i)))
          .byteA(UnsignedByte.of(accABytes32.get(maxCtComplement + i)))
          .byteW(UnsignedByte.of(accWBytes32.get(maxCtComplement + i)))
          .byteQ(UnsignedByte.of(accQBytes32.get(maxCtComplement + i)))
          .byteQq(mxpData.getByteQQ()[i].toBigInteger())
          .byteR(mxpData.getByteR()[i].toBigInteger())
          .words(BigInteger.valueOf(mxpData.getWords()))
          .wordsNew(
              BigInteger.valueOf(mxpData.getWordsNew())) // Could (should?) be set in tracePostOp?
          .cMem(BigInteger.valueOf(mxpData.getCMem())) // Returns current memory size in EVM words
          .cMemNew(BigInteger.valueOf(mxpData.getCMemNew()))
          .quadCost(BigInteger.valueOf(mxpData.getQuadCost()))
          .linCost(BigInteger.valueOf(mxpData.getLinCost()))
          .gasMxp(BigInteger.valueOf(mxpData.getQuadCost() + mxpData.getEffectiveLinCost()))
          .expands(mxpData.isExpands())
          .validateRow();
    }
  }

  @Override
  public void enterTransaction() {
    this.chunks.enter();
  }

  @Override
  public void popTransaction() {
    this.chunks.pop();
  }

  @Override
  public int lineCount() {
    throw new RuntimeException("not yet implemented"); // TODO: fixme @Lorenzo
  }

  @Override
  public Object commit() {
    // TODO: @lorenzo
    return new MxpTrace(trace.build());
  }

  @Override
  public void tracePostOp(MessageFrame frame) { // This is paired with tracePreOp
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
