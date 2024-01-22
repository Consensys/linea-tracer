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

package net.consensys.linea.zktracer.module.mmio;

import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.units.bigints.UInt256;

class MmioPatterns {
  static UInt256 isolateChunk(
      UInt256 acc, UnsignedByte b, boolean startFlag, boolean endFlag, int counter) {
    if (startFlag) {
      if (counter == 0) {
        return acc.add(UInt256.valueOf(b.toInteger()));
      }

      if (!endFlag) {
        return acc.multiply(UInt256.valueOf(256))
            .add(UInt256.valueOf(b != null ? b.toInteger() : 0));
      }
    }

    return acc;
  }

  static UInt256 isolateSuffix(UInt256 acc, boolean flag, UnsignedByte b) {
    if (flag) {
      return acc.multiply(UInt256.valueOf(256)).add(UInt256.valueOf(b != null ? b.toInteger() : 0));
    }

    return acc;
  }

  static UInt256 isolatePrefix(UInt256 acc, boolean flag, UnsignedByte b) {
    if (!flag) {
      return acc.multiply(UInt256.valueOf(256)).add(UInt256.valueOf(b != null ? b.toInteger() : 0));
    }

    return acc;
  }

  static boolean plateau(int m, int counter) {
    return counter >= m;
  }

  static UInt256 power(UInt256 pow, boolean b, int counter) {
    if (counter == 0) {
      return UInt256.valueOf(1);
    }

    if (b) {
      return pow.multiply(UInt256.valueOf(256));
    }

    return pow;
  }
}
