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

package net.consensys.linea.zktracer.module.hub.fragment.imc.call.oob.precompiles;

import static net.consensys.linea.zktracer.module.constants.GlobalConstants.OOB_INST_MODEXP_EXTRACT;
import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;
import static net.consensys.linea.zktracer.types.Conversions.booleanToBytes;

import java.math.BigInteger;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.consensys.linea.zktracer.module.hub.Trace;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.oob.OobCall;
import net.consensys.linea.zktracer.module.hub.precompiles.PrecompileInvocation;

@Getter
@Setter
@RequiredArgsConstructor
public class ModexpExtractOobCall implements OobCall {

  final PrecompileInvocation p;
  BigInteger cds;
  BigInteger bbs;
  BigInteger ebs;
  BigInteger mbs;

  boolean extractBase;
  boolean extractExponent;
  boolean extractModulus;

  @Override
  public int oobInstruction() {
    return OOB_INST_MODEXP_EXTRACT;
  }

  @Override
  public net.consensys.linea.zktracer.module.oob.Trace trace(
      net.consensys.linea.zktracer.module.oob.Trace trace) {
    return trace
        .data1(ZERO)
        .data2(bigIntegerToBytes(cds))
        .data3(bigIntegerToBytes(bbs))
        .data4(bigIntegerToBytes(ebs))
        .data5(bigIntegerToBytes(mbs))
        .data6(booleanToBytes(extractBase))
        .data7(booleanToBytes(extractExponent))
        .data8(booleanToBytes(extractModulus));
  }

  @Override
  public Trace trace(Trace trace) {
    return trace
        .pMiscOobFlag(true)
        .pMiscOobInst(oobInstruction())
        .pMiscOobData1(ZERO)
        .pMiscOobData2(bigIntegerToBytes(cds))
        .pMiscOobData3(bigIntegerToBytes(bbs))
        .pMiscOobData4(bigIntegerToBytes(ebs))
        .pMiscOobData5(bigIntegerToBytes(mbs))
        .pMiscOobData6(booleanToBytes(extractBase))
        .pMiscOobData7(booleanToBytes(extractExponent))
        .pMiscOobData8(booleanToBytes(extractModulus));
  }
}
