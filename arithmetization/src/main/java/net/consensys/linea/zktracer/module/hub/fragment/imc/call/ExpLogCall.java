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

package net.consensys.linea.zktracer.module.hub.fragment.imc.call;

import static net.consensys.linea.zktracer.opcode.gas.GasConstants.G_EXP_BYTE;

import java.math.BigInteger;

import net.consensys.linea.zktracer.module.exp.ExpLogExpParameters;
import net.consensys.linea.zktracer.module.hub.Trace;
import net.consensys.linea.zktracer.module.hub.fragment.TraceSubFragment;
import net.consensys.linea.zktracer.types.EWord;
import org.apache.tuweni.bytes.Bytes;

public record ExpLogCall(EWord exponent) implements TraceSubFragment {
  @Override
  public Trace trace(Trace trace) {
    return trace
        .pMiscellaneousExpFlag(true)
        .pMiscellaneousExpData1(exponent.hi())
        .pMiscellaneousExpData2(exponent.lo())
        .pMiscellaneousExpData5(Bytes.ofUnsignedShort(G_EXP_BYTE.cost() * exponent.byteLength()));
  }

  public ExpLogExpParameters forExp() {
    return new ExpLogExpParameters(
        exponent, BigInteger.valueOf((long) G_EXP_BYTE.cost() * exponent.byteLength()));
  }
}
