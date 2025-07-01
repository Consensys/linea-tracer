/*
 * Copyright ConsenSys Inc.
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

package net.consensys.linea.zktracer.module.rlpUtils;

import static net.consensys.linea.zktracer.Trace.LLARGE;
import static net.consensys.linea.zktracer.Trace.RLP_UTILS_INST_BYTES32;
import static net.consensys.linea.zktracer.Trace.Rlputils.CT_MAX_INST_BYTES32;
import static net.consensys.linea.zktracer.module.rlpUtils.WcpExoCall.callToGeq;

import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;

public class InstructionBytes32 extends RlpUtilsCall {
  private final Bytes32 input1;

  public InstructionBytes32(Bytes32 input1) {
    super(CT_MAX_INST_BYTES32);
    this.input1 = input1;
  }

  @Override
  protected void compute(Wcp wcp) {
    wcpCalls.add(callToGeq(wcp, input1, Bytes32.ZERO));
  }

  @Override
  public void traceRlpTxn(Trace.Rlptxn trace) {
    trace
        .pCmpRlpUtilsFlag(true)
        .pCmpInst(RLP_UTILS_INST_BYTES32)
        .pCmpExoData1(data1())
        .pCmpExoData2(data2());
  }

  @Override
  protected void traceMacro(Trace.Rlputils trace) {
    trace
        .iomf(true)
        .macro(true)
        .pMacroInst(RLP_UTILS_INST_BYTES32)
        .isByte32(true)
        .pMacroData1(data1())
        .pMacroData2(data2())
        .fillAndValidateRow();
  }

  @Override
  protected void traceCompt(Trace.Rlputils trace, short ct) {
    trace.iomf(true).compt(true).isByte32(true);
    wcpCalls.getFirst().traceWcpCall(trace);
    trace.fillAndValidateRow();
  }

  @Override
  protected int computeLineCount() {
    return 1 + CT_MAX_INST_BYTES32 + 1;
  }

  private Bytes data1() {
    return input1.slice(0, LLARGE);
  }

  private Bytes data2() {
    return input1.slice(LLARGE, LLARGE);
  }

  private int wcpCtMax() {
    return Math.max(data1().trimLeadingZeros().size(), data2().trimLeadingZeros().size()) - 1;
  }
}
