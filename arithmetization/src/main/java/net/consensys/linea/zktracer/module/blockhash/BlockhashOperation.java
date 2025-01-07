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

package net.consensys.linea.zktracer.module.blockhash;

import static net.consensys.linea.zktracer.module.blockhash.Trace.BLOCKHASH_DEPTH;
import static net.consensys.linea.zktracer.module.blockhash.Trace.nROWS_PRPRC;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EVM_INST_EQ;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EVM_INST_LT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.LLARGE;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.WCP_INST_LEQ;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;

@Accessors(fluent = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class BlockhashOperation extends ModuleOperation {
  @Getter @EqualsAndHashCode.Include private final short relBlock;
  @Getter @EqualsAndHashCode.Include private final Bytes32 blockhashArg;
  private final long absBlock;
  @Getter private final Bytes32 blockhashRes;
  private final Wcp wcp;

  private final Bytes[] exoArg1Hi = new Bytes[nROWS_PRPRC];
  private final Bytes[] exoArg1Lo = new Bytes[nROWS_PRPRC];
  private final Bytes[] exoArg2Hi = new Bytes[nROWS_PRPRC];
  private final Bytes[] exoArg2Lo = new Bytes[nROWS_PRPRC];
  private final long[] exoInst = new long[nROWS_PRPRC];
  private final boolean[] exoRes = new boolean[nROWS_PRPRC];

  public BlockhashOperation(
      final short relBlock,
      final long absBlock,
      final Bytes32 blockhashArg,
      final Bytes32 blockhashRes,
      final Wcp wcp) {
    this.relBlock = relBlock;
    this.absBlock = absBlock;
    this.blockhashArg = blockhashArg;
    this.blockhashRes = blockhashRes;
    this.wcp = wcp;
  }

  void triggerHandlePreprocessing(Bytes32 prevBlockhashArg) {
    handlePreprocessing(
        prevBlockhashArg.slice(0, LLARGE),
        prevBlockhashArg.slice(LLARGE, LLARGE),
        blockhashArg.slice(0, LLARGE),
        blockhashArg.slice(LLARGE, LLARGE));
  }

  private void handlePreprocessing(
      Bytes prevBHArgHi, Bytes prevBHArgLo, Bytes currBHArgHi, Bytes curBHArgLo) {
    // row i + 1
    wcpCallToLEQ(1, prevBHArgHi, prevBHArgLo, currBHArgHi, curBHArgLo);

    // row i + 2
    boolean sameBHArg = wcpCallToEQ(2, prevBHArgHi, prevBHArgLo, currBHArgHi, curBHArgLo);

    // row i + 3
    boolean res3 =
        wcpCallToLEQ(3, Bytes.of(0), Bytes.of(256), Bytes.of(0), Bytes.ofUnsignedLong(absBlock));
    long minimalReachable = 0;
    if (res3) {
      minimalReachable = absBlock - 256;
    }

    // row i + 4
    boolean upperBoundOk =
        wcpCallToLT(4, currBHArgHi, curBHArgLo, Bytes.of(0), Bytes.ofUnsignedLong(absBlock));

    // row i + 5
    boolean lowerBoundOk =
        wcpCallToLEQ(
            5, Bytes.of(0), Bytes.ofUnsignedLong(minimalReachable), currBHArgHi, curBHArgLo);
  }

  @Override
  protected int computeLineCount() {
    return BLOCKHASH_DEPTH;
  }

  public void traceMacro(Trace trace, final Bytes32 blockhashVal) {
    trace
        .iomf(true)
        .macro(true)
        .ct(0)
        .ctMax(0)
        .pMacroRelBlock(relBlock)
        .pMacroAbsBlock(absBlock)
        .pMacroBlockhashValHi(blockhashVal.slice(0, LLARGE))
        .pMacroBlockhashValLo(blockhashVal.slice(LLARGE, LLARGE))
        .pMacroBlockhashArgHi(blockhashArg.slice(0, LLARGE))
        .pMacroBlockhashArgLo(blockhashArg.slice(LLARGE, LLARGE))
        .pMacroBlockhashResHi(blockhashRes.slice(0, LLARGE))
        .pMacroBlockhashResLo(blockhashRes.slice(LLARGE, LLARGE))
        .fillAndValidateRow();
  }

  public void tracePreprocessing(Trace trace) {
    for (int ct = 0; ct < nROWS_PRPRC; ct++) {
      trace
          .iomf(true)
          .prprc(true)
          .ct(ct)
          .ctMax(nROWS_PRPRC - 1)
          .pPreprocessingExoInst(exoInst[ct])
          .pPreprocessingExoArg1Hi(exoArg1Hi[ct])
          .pPreprocessingExoArg1Lo(exoArg1Lo[ct])
          .pPreprocessingExoArg2Hi(exoArg2Hi[ct])
          .pPreprocessingExoArg2Lo(exoArg2Lo[ct])
          .pPreprocessingExoRes(exoRes[ct])
          .fillAndValidateRow();
    }
  }

  // WCP calls
  private boolean wcpCallToLT(
      int w, Bytes exoArg1Hi, Bytes exoArg1Lo, Bytes exoArg2Hi, Bytes exoArg2Lo) {
    this.exoInst[w] = EVM_INST_LT;
    this.exoArg1Hi[w] = exoArg1Hi;
    this.exoArg1Lo[w] = exoArg1Lo;
    this.exoArg2Hi[w] = exoArg2Hi;
    this.exoArg2Lo[w] = exoArg2Lo;
    this.exoRes[w] =
        wcp.callLT(
            Bytes.concatenate(exoArg1Hi, exoArg1Lo), Bytes.concatenate(exoArg2Hi, exoArg2Lo));
    return this.exoRes[w];
  }

  private boolean wcpCallToLEQ(
      int w, Bytes exoArg1Hi, Bytes exoArg1Lo, Bytes exoArg2Hi, Bytes exoArg2Lo) {
    this.exoInst[w] = WCP_INST_LEQ;
    this.exoArg1Hi[w] = exoArg1Hi;
    this.exoArg1Lo[w] = exoArg1Lo;
    this.exoArg2Hi[w] = exoArg2Hi;
    this.exoArg2Lo[w] = exoArg2Lo;
    this.exoRes[w] =
        wcp.callLEQ(
            Bytes.concatenate(exoArg1Hi, exoArg1Lo), Bytes.concatenate(exoArg2Hi, exoArg2Lo));
    return this.exoRes[w];
  }

  private boolean wcpCallToEQ(
      int w, Bytes exoArg1Hi, Bytes exoArg1Lo, Bytes exoArg2Hi, Bytes exoArg2Lo) {
    this.exoInst[w] = EVM_INST_EQ;
    this.exoArg1Hi[w] = exoArg1Hi;
    this.exoArg1Lo[w] = exoArg1Lo;
    this.exoArg2Hi[w] = exoArg2Hi;
    this.exoArg2Lo[w] = exoArg2Lo;
    this.exoRes[w] =
        wcp.callEQ(
            Bytes.concatenate(exoArg1Hi, exoArg1Lo), Bytes.concatenate(exoArg2Hi, exoArg2Lo));
    return this.exoRes[w];
  }
}
