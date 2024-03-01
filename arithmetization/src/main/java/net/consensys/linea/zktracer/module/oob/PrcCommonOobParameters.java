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
public class PrcCommonOobParameters implements OobParameters {

  BigInteger callGas;
  BigInteger cds;
  BigInteger returnAtCapacity;
  @Setter boolean success;
  @Setter BigInteger returnGas;
  boolean extractCallData;
  boolean emptyCallData;
  boolean returnAtCapacityNonZero;

  public PrcCommonOobParameters(
      BigInteger callGas,
      BigInteger cds,
      BigInteger returnAtCapacity,
      boolean returnAtCapacityNonZero) {
    this.callGas = callGas;
    this.cds = cds;
    this.returnAtCapacity = returnAtCapacity;
    this.returnAtCapacityNonZero = returnAtCapacityNonZero;
  }

  @Override
  public Trace trace(Trace trace) {
    boolean extractCallData = success && cds.signum() != 0;
    boolean emptyCallData = success && cds.signum() == 0;
    return trace
        .data1(bigIntegerToBytes(callGas))
        .data2(bigIntegerToBytes(cds))
        .data3(bigIntegerToBytes(returnAtCapacity))
        .data4(booleanToBytes(success)) // Not set in the constructor
        .data5(bigIntegerToBytes(returnGas)) // Not set in the constructor
        .data6(booleanToBytes(extractCallData)) // Derived from other parameters
        .data7(booleanToBytes(emptyCallData)) // Derived from other parameters
        .data8(booleanToBytes(returnAtCapacityNonZero));
  }
}
