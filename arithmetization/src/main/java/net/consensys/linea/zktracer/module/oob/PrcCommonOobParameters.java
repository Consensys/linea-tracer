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

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

@Getter
public class PrcCommonOobParameters implements OobParameters {

  BigInteger callGas;
  @Setter BigInteger remainingGas;
  BigInteger cds;
  boolean cdsISZERO;
  BigInteger returnAtCapacity;
  boolean returnAtCapacityISZERO;

  public PrcCommonOobParameters(
      BigInteger callGas,
      BigInteger cds,
      boolean cdsISZERO,
      BigInteger returnAtCapacity,
      boolean returnAtCapacityISZERO) {
    this.callGas = callGas;
    this.cds = cds;
    this.cdsISZERO = cdsISZERO;
    this.returnAtCapacity = returnAtCapacity;
    this.returnAtCapacityISZERO = returnAtCapacityISZERO;
  }

  @Override
  public Trace trace(Trace trace) {
    return trace
        .incomingData1(bigIntegerToBytes(callGas))
        .incomingData2(bigIntegerToBytes(remainingGas))
        .incomingData3(bigIntegerToBytes(cds))
        .incomingData4(cdsISZERO ? ONE : ZERO)
        .incomingData5(bigIntegerToBytes(returnAtCapacity))
        .incomingData6(returnAtCapacityISZERO ? ONE : ZERO);
  }
}
