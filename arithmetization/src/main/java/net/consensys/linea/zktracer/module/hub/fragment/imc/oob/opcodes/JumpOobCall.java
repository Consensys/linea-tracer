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

import static net.consensys.linea.zktracer.Trace.OOB_INST_JUMP;
import static net.consensys.linea.zktracer.Trace.Oob.CT_MAX_JUMP;
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
public class JumpOobCall extends OobCall {
  EWord pcNew;
  BigInteger codeSize;
  boolean jumpGuaranteedException;
  boolean jumpMustBeAttempted;

  public JumpOobCall() {
    super();
  }

  public BigInteger pcNewHi() {
    return pcNew.hiBigInt();
  }

  public BigInteger pcNewLo() {
    return pcNew.loBigInt();
  }

  @Override
  public void setInputData(final MessageFrame frame, Hub hub) {
    setPcNew(EWord.of(frame.getStackItem(0)));
    setCodeSize(BigInteger.valueOf(frame.getCode().getSize()));
  }

  @Override
  public void callExoModules(final Add add, final Mod mod, final Wcp wcp) {
    final OobExoCall validPcNewCall = callToLT(wcp, pcNew, bigIntegerToBytes(codeSize));
    exoCalls.add(validPcNewCall);

    final boolean validPcNew = bytesToBoolean(validPcNewCall.result());

    // Set jumpGuaranteedException
    setJumpGuaranteedException(validPcNew);

    // Set jumpMustBeAttempted
    setJumpMustBeAttempted(validPcNew);
  }

  @Override
  public int ctMax() {
    return CT_MAX_JUMP;
  }

  @Override
  public Trace.Oob trace(Trace.Oob trace) {
    return trace
        .oobInst(OOB_INST_JUMP)
        .isJump(true)
        .data1(bigIntegerToBytes(pcNewHi()))
        .data2(bigIntegerToBytes(pcNewLo()))
        .data5(bigIntegerToBytes(codeSize))
        .data7(booleanToBytes(jumpGuaranteedException))
        .data8(booleanToBytes(jumpMustBeAttempted));
  }

  @Override
  public Trace.Hub trace(Trace.Hub trace) {
    return trace
        .pMiscOobFlag(true)
        .pMiscOobInst(OOB_INST_JUMP)
        .pMiscOobData1(bigIntegerToBytes(pcNewHi()))
        .pMiscOobData2(bigIntegerToBytes(pcNewLo()))
        .pMiscOobData5(bigIntegerToBytes(codeSize))
        .pMiscOobData7(booleanToBytes(jumpGuaranteedException))
        .pMiscOobData8(booleanToBytes(jumpMustBeAttempted));
  }
}
