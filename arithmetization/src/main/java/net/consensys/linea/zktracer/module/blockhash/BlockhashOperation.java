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

import static net.consensys.linea.zktracer.module.blockhash.Trace.nROWS_PRPRC;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.LLARGE;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.container.ModuleOperation;
import org.apache.tuweni.bytes.Bytes32;

@Accessors(fluent = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@RequiredArgsConstructor
public class BlockhashOperation extends ModuleOperation {
  @Getter @EqualsAndHashCode.Include private final short relBlock;
  @Getter @EqualsAndHashCode.Include private final Bytes32 blockhashArg;
  private final long absBlock;
  @Getter private final Bytes32 blockhashRes;

  @Override
  protected int computeLineCount() {
    return 1;
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
      trace.iomf(true).prprc(true).ct(ct).ctMax(nROWS_PRPRC - 1).fillAndValidateRow();
    }
  }
}
