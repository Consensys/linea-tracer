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

package net.consensys.linea.zktracer.module.oob;

import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

@Getter
public class PrcModexpPricingParameters implements OobParameters {

  BigInteger callGas;
  @Setter BigInteger returnGas;
  BigInteger exponentLog;
  BigInteger maxMbsBbs;
  BigInteger returnAtCapacity;
  boolean returnAtCapacityISZERO;

  public PrcModexpPricingParameters(
      BigInteger callGas,
      BigInteger exponentLog,
      BigInteger maxMbsBbs,
      BigInteger returnAtCapacity,
      boolean returnAtCapacityISZERO) {
    this.callGas = callGas;
    this.exponentLog = exponentLog;
    this.maxMbsBbs = maxMbsBbs;
    this.returnAtCapacity = returnAtCapacity;
    this.returnAtCapacityISZERO = returnAtCapacityISZERO;
  }

  @Override
  public Trace trace(Trace trace) {
    return trace
        .data1(bigIntegerToBytes(callGas))
        .data2(bigIntegerToBytes(returnGas))
        .data3(bigIntegerToBytes(exponentLog))
        .data4(bigIntegerToBytes(maxMbsBbs))
        .data5(bigIntegerToBytes(returnAtCapacity))
        .data6(returnAtCapacityISZERO ? ONE : ZERO);
  }
}
