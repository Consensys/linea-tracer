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

package net.consensys.linea.zktracer.module.exp;

import static java.lang.Math.min;
import static net.consensys.linea.zktracer.types.Conversions.bigIntegerToBytes;

import java.math.BigInteger;

import net.consensys.linea.zktracer.types.EWord;
import org.apache.tuweni.bytes.Bytes;

public record ModexpLogExpParameters(
    EWord rawLead, int cdsCutoff, int ebsCutoff, BigInteger leadLog, Bytes trim, Bytes lead) {
  public static ModexpLogExpParameters build(
      EWord rawLead, int cdsCutoff, int ebsCutoff, BigInteger leadLog) {
    final int minCutoff = min(cdsCutoff, ebsCutoff);

    BigInteger mask = new BigInteger("FF".repeat(minCutoff), 16);
    if (minCutoff < 32) {
      // 32 - minCutoff is the shift distance in bytes, but we need bits
      mask = mask.shiftLeft(8 * (32 - minCutoff));
    }

    // trim (keep only minCutoff bytes of rawLead)
    final BigInteger trim = rawLead.toUnsignedBigInteger().and(mask);

    // lead (keep only minCutoff bytes of rawLead and potentially pad to ebsCutoff with 0's)
    final BigInteger lead = trim.shiftRight(8 * (32 - ebsCutoff));
    return new ModexpLogExpParameters(
        rawLead, cdsCutoff, ebsCutoff, leadLog, bigIntegerToBytes(trim), bigIntegerToBytes(lead));
  }

  public BigInteger rawLeadHi() {
    return rawLead.hiBigInt();
  }

  public BigInteger rawLeadLo() {
    return rawLead.loBigInt();
  }

  public BigInteger trimHi() {
    return EWord.of(trim).hiBigInt();
  }

  public BigInteger trimLo() {
    return EWord.of(trim).loBigInt();
  }
}
