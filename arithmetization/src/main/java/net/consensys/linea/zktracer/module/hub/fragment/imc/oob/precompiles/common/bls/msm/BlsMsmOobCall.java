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

package net.consensys.linea.zktracer.module.hub.fragment.imc.oob.precompiles.common.bls.msm;

import static net.consensys.linea.zktracer.Trace.PRC_BLS_MULTIPLICATION_MULTIPLIER;
import static net.consensys.linea.zktracer.module.oob.OobExoCall.callToGT;
import static net.consensys.linea.zktracer.module.oob.OobExoCall.callToIsZero;
import static net.consensys.linea.zktracer.module.oob.OobExoCall.callToLT;
import static net.consensys.linea.zktracer.module.oob.OobExoCall.callToMOD;
import static net.consensys.linea.zktracer.module.oob.OobExoCall.noCall;
import static net.consensys.linea.zktracer.types.Conversions.bytesToBoolean;

import java.math.BigInteger;

import net.consensys.linea.zktracer.module.add.Add;
import net.consensys.linea.zktracer.module.hub.fragment.imc.oob.precompiles.common.CommonPrecompileOobCall;
import net.consensys.linea.zktracer.module.hub.fragment.scenario.PrecompileScenarioFragment;
import net.consensys.linea.zktracer.module.mod.Mod;
import net.consensys.linea.zktracer.module.oob.OobExoCall;
import net.consensys.linea.zktracer.module.wcp.Wcp;
import org.apache.tuweni.bytes.Bytes;

public abstract class BlsMsmOobCall extends CommonPrecompileOobCall {
  protected BlsMsmOobCall(BigInteger calleeGas) {
    super(calleeGas);
  }

  long precompileCost;

  abstract long minMsmSize();

  abstract PrecompileScenarioFragment.PrecompileFlag getPrecompileFlag();

  abstract long maxDiscount();

  abstract long msmMultiplicationCost();

  @Override
  public void callExoModules(Add add, Mod mod, Wcp wcp) {
    super.callExoModules(add, mod, wcp);

    // row i + 2
    final OobExoCall remaninderCall =
        callToMOD(mod, getCds().toBytes(), Bytes.ofUnsignedLong(minMsmSize()));
    exoCalls.add(remaninderCall);
    final Bytes remainder = remaninderCall.result();

    // row i + 3
    final OobExoCall cdsIsMultipleOfMinMsmSizeCall = callToIsZero(wcp, remainder);
    exoCalls.add(cdsIsMultipleOfMinMsmSizeCall);
    final boolean cdsIsMultipleOfMinMsmSize =
        bytesToBoolean(cdsIsMultipleOfMinMsmSizeCall.result());

    final long numInputs = getCds().toLong() / minMsmSize();

    Boolean numInputsLeq128 = null;
    // i + 4
    if (!cdsIsMultipleOfMinMsmSize) {
      exoCalls.add(noCall());
    } else {
      final OobExoCall numInputsGt128Call =
          callToGT(wcp, Bytes.ofUnsignedLong(numInputs), Bytes.ofUnsignedInt(128));
      exoCalls.add(cdsIsMultipleOfMinMsmSizeCall);
      numInputsLeq128 = !bytesToBoolean(numInputsGt128Call.result());
    }

    // i + 5
    Long discount = null;
    if (!cdsIsMultipleOfMinMsmSize) {
      exoCalls.add(noCall());
    } else {

      if (numInputsLeq128) {
        // TODO
        /*
        final OobExoCall discountCall = callToBlsRefTable(refTable, getPrecompileFlag(), numInputs);
        exoCalls.add(discountCall);
        discount = bytesToLong(discountCall.result());
         */
      } else {
        exoCalls.add(noCall());
        discount = maxDiscount();
      }
    }

    // i + 6
    boolean sufficientGas = false;
    if (!cdsIsMultipleOfMinMsmSize) {
      exoCalls.add(noCall());
    } else {
      precompileCost =
          numInputs * msmMultiplicationCost() * discount / PRC_BLS_MULTIPLICATION_MULTIPLIER;
      final OobExoCall insufficientGasCall =
          callToLT(wcp, getCalleeGas(), Bytes.ofUnsignedLong(precompileCost));
      exoCalls.add(insufficientGasCall);
      sufficientGas = !bytesToBoolean(insufficientGasCall.result());
    }

    // Set hubSuccess
    final boolean hubSuccess = !isCdsIsZero() && cdsIsMultipleOfMinMsmSize && sufficientGas;
    setHubSuccess(hubSuccess);

    // Set returnGas
    final BigInteger returnGas =
        hubSuccess
            ? getCalleeGas().toUnsignedBigInteger().subtract(BigInteger.valueOf(precompileCost))
            : BigInteger.ZERO;
    setReturnGas(returnGas);
  }
}
