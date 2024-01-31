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

package net.consensys.linea.zktracer;

import java.math.BigInteger;

import net.consensys.linea.zktracer.types.EWord;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.crypto.altbn128.AltBn128Fq2Point;
import org.hyperledger.besu.crypto.altbn128.AltBn128Point;
import org.hyperledger.besu.crypto.altbn128.Fq;
import org.hyperledger.besu.crypto.altbn128.Fq2;

public class CurveOperations {
  private static final EWord P =
      EWord.ofHexString("0x30644e72e131a029b85045b68181585d97816a916871ca8d3c208c16d87cfd47");
  private static final BigInteger Q =
      new BigInteger("30644E72E131A029B85045B68181585D2833E84879B9709143E1F593F0000001", 16);

  public static BigInteger extractParameter(final Bytes input) {
    if (input.isEmpty()) {
      throw new IllegalArgumentException("EC_DATA input can not be empty");
    }
    return new BigInteger(1, input.toArray());
  }

  public static boolean isOnC1(Bytes xBytes) {
    final BigInteger pX = extractParameter(xBytes.slice(0, 32));
    final BigInteger pY = extractParameter(xBytes.slice(32, 32));

    if (pX.compareTo(Fq.FIELD_MODULUS) >= 0 || pY.compareTo(Fq.FIELD_MODULUS) >= 0) {
      return false;
    }

    AltBn128Point p = new AltBn128Point(Fq.create(pX), Fq.create(pY));

    return p.isOnCurve();
  }

  public static boolean isOnG2(Bytes xBytes) {
    final BigInteger pXIm = extractParameter(xBytes.slice(0, 32));
    final BigInteger pXRe = extractParameter(xBytes.slice(32, 32));
    final BigInteger pYIm = extractParameter(xBytes.slice(64, 32));
    final BigInteger pYRe = extractParameter(xBytes.slice(96, 32));

    final Fq2 pX = Fq2.create(pXRe, pXIm);
    final Fq2 pY = Fq2.create(pYRe, pYIm);

    if (!pX.isValid() || !pY.isValid()) {
      return false;
    }

    final AltBn128Fq2Point p2 = new AltBn128Fq2Point(pX, pY);
    final AltBn128Fq2Point pPowQ = p2.multiply(Q);

    return pPowQ.isInfinity();
  }
}
