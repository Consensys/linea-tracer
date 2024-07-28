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

package net.consensys.linea.zktracer.module.hub.fragment.imc.call.oob.opcodes;

import static net.consensys.linea.zktracer.module.txndata.Trace.OOB_INST_CDL;
import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;
import static net.consensys.linea.zktracer.types.Conversions.booleanToBytes;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;
import net.consensys.linea.zktracer.module.hub.Trace;
import net.consensys.linea.zktracer.module.hub.fragment.imc.call.oob.OobCall;
import net.consensys.linea.zktracer.types.EWord;

@Getter
@Setter
public class CallDataLoadOobCall implements OobCall {
  EWord offset;
  BigInteger cds;
  boolean cdlOutOfBounds;

  public BigInteger offsetHi() {
    return offset.hiBigInt();
  }

  public BigInteger offsetLo() {
    return offset.loBigInt();
  }

  @Override
  public int oobInstruction() {
    return OOB_INST_CDL;
  }

  @Override
  public net.consensys.linea.zktracer.module.oob.Trace trace(
      net.consensys.linea.zktracer.module.oob.Trace trace) {
    return trace
        .data1(bigIntegerToBytes(offsetHi()))
        .data2(bigIntegerToBytes(offsetLo()))
        .data3(ZERO)
        .data4(ZERO)
        .data5(bigIntegerToBytes(cds))
        .data6(ZERO)
        .data7(booleanToBytes(cdlOutOfBounds))
        .data8(ZERO);
  }

  @Override
  public Trace trace(Trace trace) {
    return trace
        .pMiscOobFlag(true)
        .pMiscOobInst(oobInstruction())
        .pMiscOobData1(bigIntegerToBytes(offsetHi()))
        .pMiscOobData2(bigIntegerToBytes(offsetLo()))
        .pMiscOobData3(ZERO)
        .pMiscOobData4(ZERO)
        .pMiscOobData5(bigIntegerToBytes(cds))
        .pMiscOobData6(ZERO)
        .pMiscOobData7(booleanToBytes(cdlOutOfBounds))
        .pMiscOobData8(ZERO);
  }
}
