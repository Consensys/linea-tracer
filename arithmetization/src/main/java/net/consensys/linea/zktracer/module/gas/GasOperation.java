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

package net.consensys.linea.zktracer.module.gas;

import static com.google.common.base.Preconditions.checkArgument;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.EVM_INST_LT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.WCP_INST_LEQ;
import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;
import static net.consensys.linea.zktracer.types.Utils.initArray;

import java.math.BigInteger;

import lombok.EqualsAndHashCode;
import net.consensys.linea.zktracer.container.ModuleOperation;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import net.consensys.linea.zktracer.types.UnsignedByte;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class GasOperation extends ModuleOperation {
  @EqualsAndHashCode.Include GasCall gasCall;
  private final BigInteger[] wcpArg1Lo;
  private final BigInteger[] wcpArg2Lo;
  private final UnsignedByte[] wcpInst;
  private final boolean[] wcpRes;
  private final int ctMax;

  public GasOperation(GasCall gasCall, Wcp wcp) {
    this.gasCall = gasCall;
    ctMax = compareGasActualAndGasCost() ? 2 : 1;

    // init arrays
    wcpArg1Lo = initArray(BigInteger.ZERO, ctMax + 1);
    wcpArg2Lo = initArray(BigInteger.ZERO, ctMax + 1);
    wcpInst = initArray(UnsignedByte.of(0), ctMax + 1);
    wcpRes = new boolean[ctMax + 1];

    // row 0
    wcpArg1Lo[0] = BigInteger.ZERO;
    wcpArg2Lo[0] = gasCall.getGasActual();
    wcpInst[0] = UnsignedByte.of(WCP_INST_LEQ);
    wcpRes[0] = true;
    checkArgument(wcp.callLEQ(0, gasCall.getGasActual().longValue()));

    // row 1
    wcpArg1Lo[1] = BigInteger.ZERO;
    wcpArg2Lo[1] = gasCall.getGasCost();
    wcpInst[1] = UnsignedByte.of(WCP_INST_LEQ);
    wcpRes[1] = true;
    checkArgument(wcp.callLEQ(0, gasCall.getGasCost().longValue()));

    // row 2
    if (compareGasActualAndGasCost()) {
      wcpArg1Lo[2] = gasCall.getGasActual();
      wcpArg2Lo[2] = gasCall.getGasCost();
      wcpInst[2] = UnsignedByte.of(EVM_INST_LT);
      wcpRes[2] = gasCall.isOogx();
      checkArgument(
          gasCall.isOogx()
              == wcp.callLT(gasCall.getGasActual().longValue(), gasCall.getGasCost().longValue()));
    }
  }

  private boolean compareGasActualAndGasCost() {
    return !gasCall.isXahoy() || gasCall.isOogx();
  }

  @Override
  protected int computeLineCount() {
    return ctMax + 1;
  }

  public void trace(Trace trace) {
    for (short i = 0; i < ctMax + 1; i++) {
      trace
          .inputsAndOutputsAreMeaningful(true)
          .first(i == 0)
          .ct(i)
          .ctMax(ctMax)
          .gasActual(gasCall.getGasActual().longValue())
          .gasCost(bigIntegerToBytes(gasCall.getGasCost()))
          .exceptionsAhoy(gasCall.isXahoy())
          .outOfGasException(gasCall.isOogx())
          .wcpArg1Lo(bigIntegerToBytes(wcpArg1Lo[i]))
          .wcpArg2Lo(bigIntegerToBytes(wcpArg2Lo[i]))
          .wcpInst(wcpInst[i])
          .wcpRes(wcpRes[i])
          .validateRow();
    }
  }
}
