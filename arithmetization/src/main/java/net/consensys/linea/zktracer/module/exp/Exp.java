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

import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.MappedByteBuffer;
import java.util.List;

import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.container.stacked.list.StackedList;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.hub.Hub;
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

  public Exp(Hub hub) {
    this.hub = hub;
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
    if (hub.pch().exceptions().none() && hub.pch().aborts().none()) {
      OpCode opCode = OpCode.of(frame.getCurrentOperation().getOpcode());
      if (opCode.equals(OpCode.EXP)) {
        ExpLogExpParameters expLogExpParameters = extractExpLogParameters(frame);
        this.chunks.add(new ExpChunk(frame, expLogExpParameters));
      } else if (opCode.isCall()) {
        Address target = Words.toAddress(frame.getStackItem(1));
        if (target.equals(Address.MODEXP)) {
          ModexpLogExpParameters modexpLogExpParameters = extractModexpLogParameters(frame);
          if (modexpLogExpParameters != null) {
            this.chunks.add(new ExpChunk(frame, modexpLogExpParameters));
          }
        }
      }
    }
  }

  private ExpLogExpParameters extractExpLogParameters(final MessageFrame frame) {
    EWord exponent = EWord.of(frame.getStackItem(1));
    return new ExpLogExpParameters(exponent, BigInteger.ZERO);
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
    BigInteger ebs = paddedCallData.slice(32, 64).toUnsignedBigInteger();
    BigInteger mbs = paddedCallData.slice(64, 96).toUnsignedBigInteger();

    // Some other module checks if bbs, ebs and msb are <= 512

    if (ebs.signum() == 0) {
      return null;
    }
    // Here ebs != 0

    if (cds.compareTo(BigInteger.valueOf(96).add(bbs)) > 0) {
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

    // lead
    BigInteger lead =
        doublePaddedCallData
            .slice(96 + bbs.intValue(), min(ebs.intValue(), 32))
            .toUnsignedBigInteger();

    // lead_log (same as EYP)
    BigInteger leadLog;
    if (ebs.intValue() <= 32 && lead.signum() == 0) {
      leadLog = BigInteger.ZERO;
    } else if (ebs.intValue() <= 32 && lead.signum() != 0) {
      leadLog = BigInteger.valueOf(log2(lead, RoundingMode.FLOOR));
    } else if (ebs.intValue() > 32 && lead.signum() != 0) {
      leadLog =
          BigInteger.valueOf(8)
              .multiply(ebs.subtract(BigInteger.valueOf(32)))
              .add(BigInteger.valueOf(log2(lead, RoundingMode.FLOOR)));
    } else {
      leadLog = BigInteger.valueOf(8).multiply(ebs.subtract(BigInteger.valueOf(32)));
    }
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
