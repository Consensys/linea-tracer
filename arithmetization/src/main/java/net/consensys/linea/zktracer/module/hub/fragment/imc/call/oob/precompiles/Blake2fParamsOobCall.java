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

import static net.consensys.linea.zktracer.module.constants.GlobalConstants.OOB_INST_BLAKE_PARAMS;
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
public class Blake2fParamsOobCall implements OobCall {

  final PrecompileInvocation p;

  BigInteger callGas;
  BigInteger blakeR;
  BigInteger blakeF;

  boolean ramSuccess;
  BigInteger returnGas;

  @Override
  public int oobInstruction() {
    return OOB_INST_BLAKE_PARAMS;
  }

  @Override
  public net.consensys.linea.zktracer.module.oob.Trace trace(
      net.consensys.linea.zktracer.module.oob.Trace trace) {
    return trace
        .data1(bigIntegerToBytes(callGas))
        .data2(ZERO)
        .data3(ZERO)
        .data4(booleanToBytes(ramSuccess)) // Set after the constructor
        .data5(bigIntegerToBytes(returnGas)) // Set after the constructor
        .data6(bigIntegerToBytes(blakeR))
        .data7(bigIntegerToBytes(blakeF))
        .data8(ZERO);
  }

  @Override
  public Trace trace(Trace trace) {
    return trace
        .pMiscOobFlag(true)
        .pMiscOobInst(oobInstruction())
        .pMiscOobData1(bigIntegerToBytes(callGas))
        .pMiscOobData2(ZERO)
        .pMiscOobData3(ZERO)
        .pMiscOobData4(booleanToBytes(ramSuccess)) // Set after the constructor
        .pMiscOobData5(bigIntegerToBytes(returnGas)) // Set after the constructor
        .pMiscOobData6(bigIntegerToBytes(blakeR))
        .pMiscOobData7(bigIntegerToBytes(blakeF))
        .pMiscOobData8(ZERO);
  }
}
