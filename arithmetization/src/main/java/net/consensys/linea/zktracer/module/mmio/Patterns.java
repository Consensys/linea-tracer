/*
 * Copyright ConsenSys Inc.
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

import net.consensys.linea.zktracer.types.Bytes16;

public class Patterns {
  public static Bytes16 isolateSuffix(Bytes16 inputLimb, int trigger) {
    final byte[] limbByte = inputLimb.toArray();
    byte[] outputByte = new byte[0];
    for (int i = 0; i < 16; i++) {
      if (i >= trigger) {
        outputByte[i] = limbByte[i];
      } else {
        outputByte[i] = 0;
      }
    }
    return Bytes16.wrap(outputByte);
  }

  public static Bytes16 isolatePrefix(Bytes16 inputLimb, int trigger) {
    final byte[] limbByte = inputLimb.toArray();
    byte[] outputByte = new byte[0];
    for (int i = 0; i < 16; i++) {
      if (i < trigger) {
        outputByte[i] = limbByte[i];
      } else {
        outputByte[i] = 0;
      }
    }
    return Bytes16.wrap(outputByte);
  }

  public static Bytes16 isolateChunk(Bytes16 inputLimb, int from, int to) {
    final byte[] limbByte = inputLimb.toArray();
    byte[] outputByte = new byte[0];
    for (int i = 0; i < 16; i++) {
      if (i >= from && i < to) {
        outputByte[i] = limbByte[i];
      } else {
        outputByte[i] = 0;
      }
    }
    return Bytes16.wrap(outputByte);
  }
}
