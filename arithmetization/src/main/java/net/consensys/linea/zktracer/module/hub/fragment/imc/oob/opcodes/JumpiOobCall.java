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

package net.consensys.linea.zktracer.module.hub.fragment.imc.oob.opcodes;

import static net.consensys.linea.zktracer.Trace.OOB_INST_JUMPI;
import static net.consensys.linea.zktracer.Trace.Oob.CT_MAX_JUMPI;
import static net.consensys.linea.zktracer.module.oob.OobExoCall.callToIsZero;
import static net.consensys.linea.zktracer.module.oob.OobExoCall.callToLT;
import static net.consensys.linea.zktracer.types.Conversions.*;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;
import net.consensys.linea.zktracer.Trace;
import net.consensys.linea.zktracer.module.add.Add;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.fragment.imc.oob.OobCall;
import net.consensys.linea.zktracer.module.mod.Mod;
import net.consensys.linea.zktracer.module.oob.OobExoCall;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.types.EWord;
import org.hyperledger.besu.evm.frame.MessageFrame;

@Getter
@Setter
public class JumpiOobCall extends OobCall {
  EWord pcNew;
  EWord jumpCondition;
  BigInteger codeSize;
  boolean jumpNotAttempted;
  boolean jumpGuanranteedException;
  boolean jumpMustBeAttempted;

  public JumpiOobCall() {
    super();
  }

  public BigInteger pcNewHi() {
    return pcNew.hiBigInt();
  }

  public BigInteger pcNewLo() {
    return pcNew.loBigInt();
  }

  public BigInteger jumpConditionHi() {
    return jumpCondition.hiBigInt();
  }

  public BigInteger jumpConditionLo() {
    return jumpCondition.loBigInt();
  }

  @Override
  public void setInputData(MessageFrame frame, Hub hub) {
    setPcNew(EWord.of(frame.getStackItem(0)));
    setJumpCondition(EWord.of(frame.getStackItem(1)));
    setCodeSize(BigInteger.valueOf(frame.getCode().getSize()));
  }

  @Override
  public void callExoModules(Add add, Mod mod, Wcp wcp) {
    // row i
    final OobExoCall validPcNewCall = callToLT(wcp, pcNew, bigIntegerToBytes(codeSize));
    final boolean validPcNew = bytesToBoolean(validPcNewCall.result());
    exoCalls.add(validPcNewCall);

    // row i + 1
    final OobExoCall jumpCondIsZeroCall = callToIsZero(wcp, jumpCondition);
    final boolean jumpCondIsZero = bytesToBoolean(jumpCondIsZeroCall.result());
    exoCalls.add(jumpCondIsZeroCall);

    setJumpNotAttempted(jumpCondIsZero);
    setJumpGuanranteedException(!jumpCondIsZero && !validPcNew);
    setJumpMustBeAttempted(!jumpCondIsZero && validPcNew);
  }

  @Override
  public int ctMax() {
    return CT_MAX_JUMPI;
  }

  @Override
  public Trace.Hub trace(Trace.Hub trace) {
    return trace
        .pMiscOobFlag(true)
        .pMiscOobInst(OOB_INST_JUMPI)
        .pMiscOobData1(bigIntegerToBytes(pcNewHi()))
        .pMiscOobData2(bigIntegerToBytes(pcNewLo()))
        .pMiscOobData3(bigIntegerToBytes(jumpConditionHi()))
        .pMiscOobData4(bigIntegerToBytes(jumpConditionLo()))
        .pMiscOobData5(bigIntegerToBytes(codeSize))
        .pMiscOobData6(booleanToBytes(jumpNotAttempted))
        .pMiscOobData7(booleanToBytes(jumpGuanranteedException))
        .pMiscOobData8(booleanToBytes(jumpMustBeAttempted));
  }

  @Override
  public Trace.Oob trace(Trace.Oob trace) {
    return trace
        .isJumpi(true)
        .oobInst(OOB_INST_JUMPI)
        .data1(bigIntegerToBytes(pcNewHi()))
        .data2(bigIntegerToBytes(pcNewLo()))
        .data3(bigIntegerToBytes(jumpConditionHi()))
        .data4(bigIntegerToBytes(jumpConditionLo()))
        .data5(bigIntegerToBytes(codeSize))
        .data6(booleanToBytes(jumpNotAttempted))
        .data7(booleanToBytes(jumpGuanranteedException))
        .data8(booleanToBytes(jumpMustBeAttempted));
  }
}
