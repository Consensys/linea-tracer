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

import static net.consensys.linea.zktracer.module.constants.GlobalConstants.OOB_INST_RDC;
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
public class ReturnDataCopyOobCall implements OobCall {
  EWord offset;
  EWord size;
  BigInteger rds;
  boolean rdcx;

  public BigInteger offsetHi() {
    return offset.hiBigInt();
  }

  public BigInteger offsetLo() {
    return offset.loBigInt();
  }

  public BigInteger sizeHi() {
    return size.hiBigInt();
  }

  public BigInteger sizeLo() {
    return size.loBigInt();
  }

  @Override
  public int oobInstruction() {
    return OOB_INST_RDC;
  }

  @Override
  public net.consensys.linea.zktracer.module.oob.Trace trace(
      net.consensys.linea.zktracer.module.oob.Trace trace) {
    return trace
        .data1(bigIntegerToBytes(offsetHi()))
        .data2(bigIntegerToBytes(offsetLo()))
        .data3(bigIntegerToBytes(sizeHi()))
        .data4(bigIntegerToBytes(sizeLo()))
        .data5(bigIntegerToBytes(rds))
        .data6(ZERO)
        .data7(booleanToBytes(rdcx))
        .data8(ZERO);
  }

  @Override
  public Trace trace(Trace trace) {
    return trace
        .pMiscOobFlag(true)
        .pMiscOobInst(oobInstruction())
        .pMiscOobData1(bigIntegerToBytes(offsetHi()))
        .pMiscOobData2(bigIntegerToBytes(offsetLo()))
        .pMiscOobData3(bigIntegerToBytes(sizeHi()))
        .pMiscOobData4(bigIntegerToBytes(sizeLo()))
        .pMiscOobData5(bigIntegerToBytes(rds))
        .pMiscOobData6(ZERO)
        .pMiscOobData7(booleanToBytes(rdcx))
        .pMiscOobData8(ZERO);
  }
}
