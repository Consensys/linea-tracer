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

public record PrcModexpXbsParameters(
    BigInteger cds,
    BigInteger bbs,
    boolean bbsISZERO,
    boolean callDataExtendesBeyondBase,
    boolean byLessThanAnEVMWord,
    BigInteger NCallDataBytes)
    implements OobParameters {

  @Override
  public Trace trace(Trace trace) {
    return trace
        .data1(bigIntegerToBytes(cds))
        .data2(bigIntegerToBytes(bbs))
        .data3(bbsISZERO ? ONE : ZERO)
        .data4(callDataExtendesBeyondBase ? ONE : ZERO)
        .data5(byLessThanAnEVMWord ? ONE : ZERO)
        .data6(bigIntegerToBytes(NCallDataBytes));
  }
}
