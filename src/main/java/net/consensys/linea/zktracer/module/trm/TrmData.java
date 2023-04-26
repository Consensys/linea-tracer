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

import net.consensys.linea.zktracer.bytestheta.BaseBytes;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;

public class TrmData {
  private final BaseBytes arg1;
  private final boolean isPrec;
  private final boolean[] pBit = new boolean[8];
  private final boolean[] ones = new boolean[8];

  public TrmData(final Bytes32 arg1) {
    this.arg1 = BaseBytes.fromBytes32(arg1);
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

  public boolean[] getpBit() {
    return pBit;
  }

  public boolean[] getOnes() {
    return ones;
  }
}
