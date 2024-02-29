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

public record PrcModexpCdsParameters(
    BigInteger cds,
    boolean cdsGT0,
    boolean cdsGT32,
    boolean cdsGT64,
    boolean cdsLT32,
    boolean cdsLT64,
    boolean cdsLT96)
    implements OobParameters {

  @Override
  public Trace trace(Trace trace) {
    return trace
        .data1(bigIntegerToBytes(cds))
        .data2(cdsGT0 ? ONE : ZERO)
        .data3(cdsGT32 ? ONE : ZERO)
        .data4(cdsGT64 ? ONE : ZERO)
        .data5(cdsLT32 ? ONE : ZERO)
        .data6(cdsLT32 ? ONE : ZERO);
    // oobEvent1 is set to cdsLT96 in set PrcModexpCds
  }
}
