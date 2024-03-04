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

package net.consensys.linea.zktracer.module.oob;

import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;
import static net.consensys.linea.zktracer.types.Conversions.booleanToBytes;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

@Getter
public class PrcModexpXbsParameters implements OobParameters {

  private BigInteger xbsHi;
  private BigInteger xbsLo;
  private BigInteger ybsLo;
  private boolean computeMax;
  @Setter private BigInteger maxXbsYbs;
  @Setter private boolean xbsNonZero;

  public PrcModexpXbsParameters(
      BigInteger xbsHi, BigInteger xbsLo, BigInteger ybsLo, boolean computeMax) {
    this.xbsHi = xbsHi;
    this.xbsLo = xbsLo;
    this.ybsLo = ybsLo;
    this.computeMax = computeMax;
  }

  @Override
  public Trace trace(Trace trace) {
    return trace
        .data1(bigIntegerToBytes(xbsHi))
        .data2(bigIntegerToBytes(xbsLo))
        .data3(bigIntegerToBytes(ybsLo))
        .data4(booleanToBytes(computeMax))
        .data5(ZERO)
        .data6(ZERO)
        .data7(bigIntegerToBytes(maxXbsYbs))
        .data8(booleanToBytes(xbsNonZero));
  }
}
