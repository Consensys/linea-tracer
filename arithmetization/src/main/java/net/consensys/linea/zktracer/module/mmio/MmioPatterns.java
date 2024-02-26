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

import static net.consensys.linea.zktracer.module.mmio.Trace.LLARGE;

import net.consensys.linea.zktracer.types.Bytes16;
import org.apache.tuweni.bytes.Bytes;

public class MmioPatterns {

  static Bytes[] isolateSuffix(final Bytes16 input, final boolean[] flag) {
    Bytes[] output = new Bytes[LLARGE];

    output[0] = flag[0] ? Bytes.of(input.get(0)) : Bytes.EMPTY;

    for (int ct = 1; ct < LLARGE; ct++) {
      output[ct] =
          flag[ct] ? Bytes.concatenate(output[ct - 1], Bytes.of(input.get(ct))) : output[ct - 1];
    }

    return output;
  }

  static Bytes[] isolatePrefix(final Bytes16 input, final boolean[] flag) {
    Bytes[] output = new Bytes[LLARGE];

    output[0] = flag[0] ? Bytes.EMPTY : Bytes.of(input.get(0));

    for (int ct = 1; ct < LLARGE; ct++) {
      output[ct] =
          flag[ct] ? output[ct - 1] : Bytes.concatenate(output[ct - 1], Bytes.of(input.get(ct)));
    }

    return output;
  }

  static Bytes[] isolateChunk(
      final Bytes16 input, final boolean[] startFlag, final boolean[] endFlag) {
    Bytes[] output = new Bytes[LLARGE];

    output[0] = startFlag[0] ? Bytes.of(input.get(0)) : Bytes.EMPTY;

    for (int ct = 1; ct < LLARGE; ct++) {
      if (startFlag[ct]) {
        output[ct] =
            endFlag[ct]
                ? output[ct - 1]
                : Bytes.concatenate(output[ct - 1], Bytes.of(input.get(ct)));
      } else {
        output[ct] = Bytes.EMPTY;
      }
    }

    return output;
  }

  static Bytes[] power(final boolean[] flag) {
    Bytes[] output = new Bytes[LLARGE];

    output[0] = Bytes.of(1);

    for (int ct = 1; ct < LLARGE; ct++) {
      output[ct] = flag[ct] ? output[ct - 1].shiftLeft(8) : output[ct - 1];
    }
    return output;
  }

  static Bytes[] antiPower(final boolean[] flag) {
    Bytes[] output = new Bytes[LLARGE];

    output[0] = flag[0] ? Bytes.of(256) : Bytes.of(1);

    for (short ct = 1; ct < LLARGE; ct++) {
      output[ct] = flag[ct] ? output[ct - 1] : output[ct - 1].shiftLeft(8);
    }
    return output;
  }

  static boolean plateau(int m, int counter) {
    return counter >= m;
  }

  public static Bytes16 onePartialToOne(
      final Bytes16 source,
      final Bytes16 target,
      final short sourceByteOffset,
      final short targetByteOffset,
      final short size) {
    return Bytes16.leftPad(
        Bytes.concatenate(target.slice(0, targetByteOffset), source.slice(sourceByteOffset, size)));
  }

  public static Bytes16 onePartialToTwoOutputOne(
      final Bytes16 source,
      final Bytes16 target1,
      final short sourceByteOffset,
      final short targetByteOffset) {
    return (Bytes16)
        Bytes.concatenate(
            target1.slice(0, targetByteOffset),
            source.slice(sourceByteOffset, LLARGE - targetByteOffset));
  }

  public static Bytes16 onePartialToTwoOutputTwo(
      final Bytes16 source,
      final Bytes16 target2,
      final short sourceByteOffset,
      final short targetByteOffset,
      final short size) {
    final short numberOfBytesFromSourceToFirstTarget = (short) (LLARGE - targetByteOffset);
    final short numberOfBytesFromSourceToSecondTarget =
        (short) (size - numberOfBytesFromSourceToFirstTarget);
    return (Bytes16)
        Bytes.concatenate(
            source.slice(
                sourceByteOffset + numberOfBytesFromSourceToFirstTarget,
                numberOfBytesFromSourceToSecondTarget),
            target2.slice(
                numberOfBytesFromSourceToSecondTarget,
                LLARGE - numberOfBytesFromSourceToSecondTarget));
  }

  public static Bytes16 oneToOnePadded(
      final Bytes16 source, final short sourceByteoffset, final short size) {
    return Bytes16.leftPad(source.slice(sourceByteoffset, size));
  }

  public static Bytes16 twoToOnePadded(
      final Bytes16 source1,
      final Bytes16 source2,
      final short sourceByteOffset,
      final short size) {
    final short numberOfBytesFromFirstSource = (short) (LLARGE - sourceByteOffset);

    return Bytes16.leftPad(
        Bytes.concatenate(
            source1.slice(sourceByteOffset, numberOfBytesFromFirstSource),
            source2.slice(0, size - numberOfBytesFromFirstSource)));
  }

  public static Bytes16 twoPartialToOne(
      final Bytes16 source1,
      final Bytes16 source2,
      final Bytes16 target,
      final short sourceByteOffset,
      final short targetByteOffset,
      final short size) {
    final short numberByteFromFirstSource = (short) (LLARGE - sourceByteOffset);
    final short numberByteFromSecondSource = (short) (size - numberByteFromFirstSource);
    return (Bytes16)
        Bytes.concatenate(
            target.slice(0, targetByteOffset),
            source1.slice(sourceByteOffset, numberByteFromFirstSource),
            source2.slice(0, numberByteFromSecondSource),
            target.slice(targetByteOffset + size));
  }

  public static Bytes16 excision(
      final Bytes16 target, final short targetByteOffset, final short size) {
    return (Bytes16)
        Bytes.concatenate(
            target.slice(0, targetByteOffset),
            Bytes.repeat((byte) 0, size),
            target.slice(targetByteOffset + size));
  }
}
