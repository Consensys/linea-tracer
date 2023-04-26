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
package net.consensys.linea.zktracer.module.trm;

import java.math.BigInteger;

import net.consensys.linea.zktracer.bytestheta.BaseBytes;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;

public class TrmData {
  public static final int MAX_COUNTER = 16;

  private final BaseBytes arg1;
  private final Bytes trimmedAddressHi;
  private final boolean isPrec;
  // (9 - arg1.Hi[15]) in binary form IFF is_precompile. otherwise, all zeros
  private final boolean[] ones = new boolean[MAX_COUNTER];
  private final BigInteger[] accT = new BigInteger[MAX_COUNTER];
  private final BigInteger[] accHi = new BigInteger[MAX_COUNTER];
  private final BigInteger[] accLo = new BigInteger[MAX_COUNTER];

  public TrmData(final Bytes32 arg1) {
    this.arg1 = BaseBytes.fromBytes32(arg1);
    // trimmed version of the high part of the address arg
    this.trimmedAddressHi = this.arg1.getHigh().trimLeadingZeros();
    this.isPrec = getIsPrec();
  }

  public boolean getIsPrec() {
    if (!arg1.getHigh().isZero()) {
      return false;
    }
    Bytes leastSignificantBytes = arg1.getLow().trimLeadingZeros();
    if (leastSignificantBytes.isZero()) {
      return false;
    }
    if (leastSignificantBytes.size() > 1) {
      return false;
    }
    // single byte
    // if 01 - 09 then return true
    if (leastSignificantBytes.compareTo(Bytes.fromHexString("0x09")) <= 0) {
      return true;
    }
    return false;
  }

  public BaseBytes getArg1() {
    return arg1;
  }

  public boolean isPrec() {
    return isPrec;
  }

  public boolean getPBit(int i) {
    // zero for the first 12 rows
    return i > 11;
  }

  public boolean[] getOnes() {
    return ones;
  }

  public Bytes getTrimmedAddressHi() {
    return trimmedAddressHi;
  }

  public void setAccumulators(final int i) {
    if (i == 0) {
      accHi[i] = BigInteger.valueOf(arg1.getHigh().get(i));
      accLo[i] = BigInteger.valueOf(arg1.getLow().get(i));
      accT[i] = BigInteger.ZERO;
    } else {
      accHi[i] =
          accHi[i - 1]
              .multiply(BigInteger.valueOf(256))
              .add(BigInteger.valueOf(arg1.getHigh().get(i)));
      accLo[i] =
          accLo[i - 1]
              .multiply(BigInteger.valueOf(256))
              .add(BigInteger.valueOf(arg1.getLow().get(i)));
      accT[i] =
          accT[i - 1]
              .multiply(BigInteger.valueOf(256))
              .add(BigInteger.valueOf((getPBit(i) ? arg1.getHigh().get(i) : 0)));
    }
  }

  public BigInteger[] getAccT() {
    return accT;
  }

  public BigInteger[] getAccHi() {
    return accHi;
  }

  public BigInteger[] getAccLo() {
    return accLo;
  }
}
