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

package net.consensys.linea.zktracer.module.exp;

import static com.google.common.math.BigIntegerMath.log2;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static net.consensys.linea.zktracer.ZkTracer.gasCalculator;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.MappedByteBuffer;
import java.util.List;

import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.container.stacked.list.StackedList;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.types.EWord;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.internal.Words;

public class Exp implements Module {
  /** A list of the operations to trace */
  private final StackedList<ExpChunk> chunks = new StackedList<>();

  private final Hub hub;
  private final Wcp wcp;

  public Exp(Hub hub, Wcp wcp) {
    this.hub = hub;
    this.wcp = wcp;
  }

  @Override
  public String moduleKey() {
    return "EXP";
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
    return this.chunks.lineCount();
  }

  @Override
  public List<ColumnHeader> columnsHeaders() {
    return Trace.headers(this.lineCount());
  }

  @Override
  public void tracePreOpcode(MessageFrame frame) {
    OpCode opCode = OpCode.of(frame.getCurrentOperation().getOpcode());
    if (opCode.equals(OpCode.EXP)) {
      if (!hub.pch().exceptions().stackUnderflow()) {
        ExpLogExpParameters expLogExpParameters = extractExpLogParameters(frame);
        this.chunks.add(new ExpChunk(frame, wcp, expLogExpParameters));
      }
    }
    if (hub.pch().exceptions().none() && hub.pch().aborts().none()) {
      if (opCode.isCall()) {
        Address target = Words.toAddress(frame.getStackItem(1));
        if (target.equals(Address.MODEXP)) {
          ModexpLogExpParameters modexpLogExpParameters = extractModexpLogParameters(frame);
          if (modexpLogExpParameters != null) {
            this.chunks.add(new ExpChunk(frame, wcp, modexpLogExpParameters));
          }
        }
      }
    }
  }

  private ExpLogExpParameters extractExpLogParameters(final MessageFrame frame) {
    EWord exponent = EWord.of(frame.getStackItem(1));
    // From SpuriousDragonGasCalculator.java (necessary to keep only dynamic cost and throw away the
    // static one)
    final long EXP_OPERATION_BASE_GAS_COST = 10L;
    final int numBytes = (exponent.bitLength() + 7) / 8;
    BigInteger dynCost =
        exponent.isZero()
            ? BigInteger.ZERO
            : BigInteger.valueOf(
                gasCalculator.expOperationGasCost(numBytes) - EXP_OPERATION_BASE_GAS_COST);
    return new ExpLogExpParameters(exponent, dynCost);
  }

  private ModexpLogExpParameters extractModexpLogParameters(final MessageFrame frame) {
    OpCode opCode = OpCode.of(frame.getCurrentOperation().getOpcode());
    // DELEGATECALL, STATICCALL cases
    int cdoIndex = 2;
    int cdsIndex = 3;
    // CALL, CALLCODE cases
    if (opCode == OpCode.CALL || opCode == OpCode.CALLCODE) {
      cdoIndex = 3;
      cdsIndex = 4;
    }
    BigInteger cdo = EWord.of(frame.getStackItem(cdoIndex)).toUnsignedBigInteger();
    BigInteger cds = EWord.of(frame.getStackItem(cdsIndex)).toUnsignedBigInteger();

    // mxp should ensure that the hi part of cds is 0

    if (cds.signum() == 0) {
      return null;
    }
    // Here cds != 0

    Bytes unpaddedCallData = frame.shadowReadMemory(cdo.longValue(), cds.longValue());

    // pad unpaddedCallData to 96 (this is probably not necessary)
    Bytes paddedCallData =
        cds.intValue() < 96
            ? Bytes.concatenate(unpaddedCallData, Bytes.repeat((byte) 0, 96 - cds.intValue()))
            : unpaddedCallData;

    BigInteger bbs = paddedCallData.slice(0, 32).toUnsignedBigInteger();
    BigInteger ebs = paddedCallData.slice(32, 32).toUnsignedBigInteger();
    BigInteger mbs = paddedCallData.slice(64, 32).toUnsignedBigInteger();

    // Some other module checks if bbs, ebs and msb are <= 512 (@Francois)

    if (ebs.signum() == 0) {
      return null;
    }
    // Here ebs != 0

    if (cds.compareTo(BigInteger.valueOf(96).add(bbs)) <= 0) {
      return null;
    }

    // pad paddedCallData to 96 + bbs + 32
    Bytes doublePaddedCallData =
        cds.intValue() < 96 + bbs.intValue() + 32
            ? Bytes.concatenate(
                paddedCallData, Bytes.repeat((byte) 0, 96 + bbs.intValue() + 32 - cds.intValue()))
            : paddedCallData;

    // raw_lead
    EWord rawLead = EWord.of(doublePaddedCallData.slice(96 + bbs.intValue(), 32));

    // cds_cutoff
    int cdsCutoff = min(max(cds.intValue() - (96 + ebs.intValue()), 0), 32);
    // ebs_cutoff
    int ebsCutoff = min(ebs.intValue(), 32);
    // min_cutoff
    int minCutoff = min(cdsCutoff, ebsCutoff);

    BigInteger mask = new BigInteger("FF".repeat(minCutoff), 16);
    if (minCutoff < 32) {
      // 32 - minCutoff is the shift distance in bytes, but we need bits
      mask = mask.shiftLeft(8 * (32 - minCutoff));
    }

    // trim (keep only minCutoff bytes of rawLead)
    BigInteger trim = rawLead.toUnsignedBigInteger().and(mask);

    // lead (keep only minCutoff bytes of rawLead and potentially pad to ebsCutoff with 0's)
    BigInteger lead = trim.shiftRight(8 * (32 - ebsCutoff));

    // lead_log (same as EYP)
    BigInteger leadLog =
        lead.signum() == 0 ? BigInteger.ZERO : BigInteger.valueOf(log2(lead, RoundingMode.FLOOR));
    return new ModexpLogExpParameters(rawLead, cdsCutoff, ebsCutoff, leadLog, lead);
  }

  @Override
  public void commit(List<MappedByteBuffer> buffers) {
    final Trace trace = new Trace(buffers);
    for (int i = 0; i < this.chunks.size(); i++) {
      ExpChunk expChunk = this.chunks.get(i);
      expChunk.traceComputation(i + 1, trace);
      expChunk.traceMacro(i + 1, trace);
      expChunk.tracePreprocessing(i + 1, trace);
    }
  }
}
