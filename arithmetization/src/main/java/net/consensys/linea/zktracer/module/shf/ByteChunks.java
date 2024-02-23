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

package net.consensys.linea.zktracer.module.shf;

import org.apache.tuweni.bytes.Bytes;

public record ByteChunks(short ra, short la, short ones) {

  public static ByteChunks fromBytes(final short b, final short mshp) {
    if (mshp > 8) {
      String s = String.format("chunksFromByte given mshp = %d not in {0,1,...,8}", mshp);
      throw new IllegalArgumentException(s);
    }

    final short mshpCmp = (short) (8 - mshp);
    final short ra = (short) Bytes.ofUnsignedShort(b).shiftRight(mshp).toInt();
    final short la = (short) Bytes.ofUnsignedShort(b).shiftLeft(mshpCmp).toInt();
    final short ones = (short) Bytes.of(255).shiftLeft(mshpCmp).toInt();

    return new ByteChunks(ra, la, ones);
  }
}
