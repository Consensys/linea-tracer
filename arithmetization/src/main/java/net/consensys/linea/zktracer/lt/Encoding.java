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
package net.consensys.linea.zktracer.lt;

import java.nio.ByteBuffer;

public record Encoding(int encoding, byte[] data) {
  public static final byte ENCODING_ZERO = 0;
  public static final byte ENCODING_STATIC = 1;
  public static final byte ENCODING_POOL = 2;

  /**
   * Construct a static encoding for a given set of column data.
   *
   * @param buffer
   * @return
   */
  public static Encoding of(long[] buffer) {
    int encoding;
    byte[] data;
    long maxValue = maxValue(buffer);
    //
    if (maxValue == 0) {
      encoding = encoding(Encoding.ENCODING_ZERO, 0);
      data = encodeU0(buffer);
    } else if (maxValue < 2L) {
      encoding = encoding(Encoding.ENCODING_STATIC, 1);
      data = encodeU1(buffer);
    } else if (maxValue < 256L) {
      encoding = encoding(Encoding.ENCODING_STATIC, 8);
      data = encodeU8(buffer);
    } else if (maxValue < 65536L) {
      encoding = encoding(Encoding.ENCODING_STATIC, 16);
      data = encodeU16(buffer);
    } else if (maxValue < 4294967296L) {
      encoding = encoding(Encoding.ENCODING_STATIC, 32);
      data = encodeU32(buffer);
    } else {
      throw new IllegalArgumentException("column data too large (" + maxValue + ")");
    }
    //
    return new Encoding(encoding, data);
  }

  private static long maxValue(long[] data) {
    long max = 0;

    for (int i = 0; i < data.length; i++) {
      max = Math.max(max, data[i]);
    }

    return max;
  }

  /**
   * Construct a pooled encoding for a given set of column data.
   *
   * @param buffer
   * @return
   */
  public static Encoding ofPool(int[] buffer, int bitwidth) {
    int encoding = encoding(ENCODING_POOL, bitwidth);
    byte[] data = encodeU32(buffer);
    return new Encoding(encoding, data);
  }

  private static int encoding(byte opcode, int operand) {
    int encoding = (opcode & 0xff) << 24;
    return encoding | (operand & 0xfff);
  }

  private static byte[] encodeU0(long[] buffer) {
    final ByteBuffer buf = ByteBuffer.allocate(4);
    buf.putInt(buffer.length);
    return buf.array();
  }

  /**
   * U1 encoding is given a special bit representation for efficiency.
   *
   * @param buffer
   * @return
   */
  private static byte[] encodeU1(long[] buffer) {
    // Determine how many bytes required
    int n = Util.byteWidth(buffer.length);
    final byte[] bytes = new byte[n + 1];
    int bitIndex = 0;
    int byteIndex = 0;
    //
    for (int i = 0; i != buffer.length; i++) {
      // Check whether bit set
      if (buffer[i] != 0) {
        int bit = 1 << bitIndex;
        int ith = (bytes[byteIndex] & 0xff) | bit;
        // Assign updated byte
        bytes[byteIndex] = (byte) ith;
      }
      // Increment indices
      if (++bitIndex == 8) {
        bitIndex = 0;
        byteIndex++;
      }
    }
    // Mark unused bits
    bytes[n] = (byte) ((n * 8) - buffer.length);
    // Done
    return bytes;
  }

  private static byte[] encodeU8(long[] buffer) {
    final byte[] bytes = new byte[buffer.length];
    //
    for (int i = 0; i != buffer.length; i++) {
      bytes[i] = (byte) buffer[i];
    }
    //
    return bytes;
  }

  private static byte[] encodeU16(long[] buffer) {
    final byte[] bytes = new byte[buffer.length * 2];
    //
    for (int i = 0; i != buffer.length; i++) {
      final long ith = buffer[i];
      bytes[i << 1] = (byte) (ith >> 8);
      bytes[(i << 1) + 1] = (byte) ith;
    }
    //
    return bytes;
  }

  private static byte[] encodeU32(int[] buffer) {
    final byte[] bytes = new byte[buffer.length * 4];
    //
    for (int i = 0; i != buffer.length; i++) {
      final int ith = buffer[i];
      bytes[i << 2] = (byte) (ith >> 24);
      bytes[(i << 2) + 1] = (byte) (ith >> 16);
      bytes[(i << 2) + 2] = (byte) (ith >> 8);
      bytes[(i << 2) + 3] = (byte) ith;
    }
    //
    return bytes;
  }

  private static byte[] encodeU32(long[] buffer) {
    final byte[] bytes = new byte[buffer.length * 4];
    //
    for (int i = 0; i != buffer.length; i++) {
      final long ith = buffer[i];
      bytes[i << 2] = (byte) (ith >> 24);
      bytes[(i << 2) + 1] = (byte) (ith >> 16);
      bytes[(i << 2) + 2] = (byte) (ith >> 8);
      bytes[(i << 2) + 3] = (byte) ith;
    }
    //
    return bytes;
  }
}
