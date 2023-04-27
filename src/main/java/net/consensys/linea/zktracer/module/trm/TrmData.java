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

import java.util.Arrays;

import net.consensys.linea.zktracer.bytestheta.BaseBytes;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;

public class TrmData {
  public static final int MAX_COUNTER = 16;
  public static final int P_BIT_FLIPS_TO_TRUE = 12;

  private final BaseBytes arg1;
  private final Bytes trimmedAddressHi;
  private final boolean isPrec;
  // (9 - arg1.Hi[15]) in binary form IFF is_precompile. otherwise, all zeros
  private boolean[] ones = new boolean[MAX_COUNTER];

  public TrmData(final Bytes32 arg1) {
    Arrays.fill(ones, false);
    this.arg1 = BaseBytes.fromBytes32(arg1);
    // trimmed version of the high part of the address arg
    this.trimmedAddressHi = this.arg1.getHigh().trimLeadingZeros();
    this.isPrec = getIsPrec(); // also sets ones
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
      // this means we set the ones ie the difference, in binary
      int b = leastSignificantBytes.get(leastSignificantBytes.size() - 1);
      ones = getOnesFromDiff(9 - b);
      return true;
    }
    return false;
  }

  public boolean[] getOnesFromDiff(int diff) {
    boolean[] bitDecomposition = new boolean[MAX_COUNTER];
    char[] binaryDiff = Integer.toBinaryString(diff).toCharArray();
    for (int i = 1; i <= binaryDiff.length; i++) {
      bitDecomposition[MAX_COUNTER - i] = '0' != binaryDiff[binaryDiff.length - i];
    }
    return bitDecomposition;
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

  public Bytes getTrimmedAddressHi() {
    return trimmedAddressHi;
  }

  public boolean[] getOnes() {
    return ones;
  }
}
