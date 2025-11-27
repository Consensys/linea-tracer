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
import java.util.Arrays;

public record Encoding(int encoding, byte[] data) {
  public static final byte ENCODING_CONSTANT = 0;
  public static final byte ENCODING_STATIC = 1;
  public static final byte ENCODING_STATIC_SPARSE = 2;
  public static final byte ENCODING_POOL_CONSTANT = 3;
  public static final byte ENCODING_POOL8 = 4;
  public static final byte ENCODING_POOL16 = 5;
  public static final byte ENCODING_POOL32 = 6;
  public static final byte ENCODING_POOL8_SPARSE = 7;
  public static final byte ENCODING_POOL16_SPARSE = 8;
  public static final byte ENCODING_POOL32_SPARSE = 9;
  /**
   * Construct a static encoding for a given set of column data.
   *
   * @param buffer
   * @return
   */
  public static Encoding of(long[] buffer) {
    int encoding;
    byte[] data;
    long maxValue = Util.maxValue(buffer);
    long minValue = Util.minValue(buffer);
    int nblocks = Util.approxUniqueElements(buffer);
    //
    if ((buffer.length == 0 || maxValue == minValue) && maxValue <= 0xFF_FFFF) {
      encoding = encoding(Encoding.ENCODING_CONSTANT, (int) maxValue);
      data = encodeU0(buffer.length);
    } else if (maxValue < 2L && preferSparse(nblocks,buffer.length,1,8)) {
      encoding = encoding(Encoding.ENCODING_STATIC_SPARSE, 1);
      data = encodeU8sparse(nblocks,buffer);
    } else if (maxValue < 2L) {
      encoding = encoding(Encoding.ENCODING_STATIC, 1);
      data = encodeU1(buffer);
    } else if (maxValue < 4L && preferSparse(nblocks,buffer.length,2,8)) {
      encoding = encoding(Encoding.ENCODING_STATIC_SPARSE, 2);
      data = encodeU8sparse(nblocks,buffer);
    } else if (maxValue < 4L) {
      encoding = encoding(Encoding.ENCODING_STATIC, 2);
      data = encodeU2(buffer);
    } else if (maxValue < 16L && preferSparse(nblocks,buffer.length,4,8)) {
      encoding = encoding(Encoding.ENCODING_STATIC_SPARSE, 4);
      data = encodeU8sparse(nblocks,buffer);
    } else if (maxValue < 16L) {
      encoding = encoding(Encoding.ENCODING_STATIC, 4);
      data = encodeU4(buffer);
    } else if (maxValue < 256L && preferSparse(nblocks,buffer.length,8,8)) {
      encoding = encoding(Encoding.ENCODING_STATIC_SPARSE, 8);
      data = encodeU8sparse(nblocks,buffer);
    } else if (maxValue < 256L) {
      encoding = encoding(Encoding.ENCODING_STATIC, 8);
      data = encodeU8(buffer);
    } else if (maxValue < 65536L && preferSparse(nblocks,buffer.length,16,16)) {
      encoding = encoding(Encoding.ENCODING_STATIC_SPARSE, 16);
      data = encodeU16sparse(nblocks,buffer);
    } else if (maxValue < 65536L) {
      encoding = encoding(Encoding.ENCODING_STATIC, 16);
      data = encodeU16(buffer);
    } else if (maxValue < 4294967296L && preferSparse(nblocks,buffer.length,32,32)) {
      encoding = encoding(Encoding.ENCODING_STATIC_SPARSE, 32);
      data = encodeU32sparse(nblocks,buffer);
    } else if (maxValue < 4294967296L) {
      encoding = encoding(Encoding.ENCODING_STATIC, 32);
      data = encodeU32(buffer);
    } else {
      throw new IllegalArgumentException("column data too large (" + maxValue + ")");
    }
    //
    return new Encoding(encoding, data);
  }

  /**
   * Construct a pooled encoding for a given set of column data.
   *
   * @param buffer
   * @return
   */
  public static Encoding of(int[] buffer, int bitwidth, BytesHeap heap) {
    int encoding;
    byte[] data;
    int maxValue = Util.maxValue(buffer);
    int minValue = Util.minValue(buffer);
    int nblocks = Util.approxUniqueElements(buffer);
    //
    if ((buffer.length == 0 || maxValue == minValue) && maxValue <= 0xFF_FFFF) {
      if(maxValue == 0) {
        // NOTE: index 0 already represents actual 0
         encoding = encoding(Encoding.ENCODING_CONSTANT, 0);
      } else {
        encoding = encoding(Encoding.ENCODING_POOL_CONSTANT, maxValue);
      }
      data = encodeU0(buffer.length);
    } else if (maxValue < 256L && preferSparse(nblocks,buffer.length,8,8)) {
      encoding = encoding(Encoding.ENCODING_POOL8_SPARSE, bitwidth);
      data = encodeU8sparse(nblocks,buffer);
    } else if (maxValue < 256L) {
      data = encodeU8(buffer);
      encoding = encoding(ENCODING_POOL8, bitwidth);
    } else if (maxValue < 65536L && preferSparse(nblocks,buffer.length,16,16)) {
      encoding = encoding(Encoding.ENCODING_POOL16_SPARSE, bitwidth);
      data = encodeU16sparse(nblocks,buffer);
    } else if (maxValue < 65536L) {
      data = encodeU16(buffer);
      encoding = encoding(ENCODING_POOL16, bitwidth);
    } else if (preferSparse(nblocks,buffer.length,32,32)) {
      encoding = encoding(Encoding.ENCODING_POOL32_SPARSE, bitwidth);
      data = encodeU32sparse(nblocks,buffer);
    } else {
      encoding = encoding(ENCODING_POOL32, bitwidth);
      data = encodeU32(buffer);
    }
    //

    return new Encoding(encoding, data);
  }

  private static boolean preferSparse(int nblocks, int length, int denseWidth, int sparseWidth) {
    int sparseLayout = (nblocks * (32 + sparseWidth));
    int denseLayout = length * denseWidth;
    return sparseLayout < denseLayout;
  }

  private static int encoding(byte opcode, int operand) {
    int encoding = (opcode & 0xff) << 24;
    return encoding | (operand & 0xff_ffff);
  }

  private static byte[] encodeU0(int length) {
    final ByteBuffer buf = ByteBuffer.allocate(4);
    buf.putInt(length);
    return buf.array();
  }

  /**
   * U1 encoding is given a special bit-level representation for efficiency.
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
      int bit = (int) (0x1 & buffer[i]) << bitIndex;
      int ith = (bytes[byteIndex] & 0xff) | bit;
      // Assign updated byte
      bytes[byteIndex] = (byte) ith;
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

  /**
   * U2 encoding is given a special bit-level representation for efficiency.
   *
   * @param buffer
   * @return
   */
  private static byte[] encodeU2(long[] buffer) {
    // Determine how many bytes required
    int n = Util.byteWidth(buffer.length * 2);
    final byte[] bytes = new byte[n + 1];
    int bitIndex = 0;
    int byteIndex = 0;
    //
    for (int i = 0; i != buffer.length; i++) {
      int bits = (int) (0x3 & buffer[i]) << bitIndex;
      int ith = (bytes[byteIndex] & 0xff) | bits;
      // Assign updated byte
      bytes[byteIndex] = (byte) ith;
      // Increment indices
      bitIndex += 2;
      // Check overflow
      if (bitIndex == 8) {
        bitIndex = 0;
        byteIndex++;
      }
    }
    // Mark unused bits
    bytes[n] = (byte) ((n * 8) - buffer.length);
    // Done
    return bytes;
  }

  /**
   * U2 encoding is given a special bit-level representation for efficiency.
   *
   * @param buffer
   * @return
   */
  private static byte[] encodeU4(long[] buffer) {
    // Determine how many bytes required
    int n = Util.byteWidth(buffer.length * 4);
    final byte[] bytes = new byte[n + 1];
    int bitIndex = 0;
    int byteIndex = 0;
    //
    for (int i = 0; i != buffer.length; i++) {
      int bits = (int) (0x15 & buffer[i]) << bitIndex;
      int ith = (bytes[byteIndex] & 0xff) | bits;
      // Assign updated byte
      bytes[byteIndex] = (byte) ith;
      // Increment indices
      bitIndex += 4;
      // Check overflow
      if (bitIndex == 8) {
        bitIndex = 0;
        byteIndex++;
      }
    }
    // Mark unused bits
    bytes[n] = (byte) ((n * 8) - buffer.length);
    // Done
    return bytes;
  }

  private static byte[] encodeU8(int[] buffer) {
    final byte[] bytes = new byte[buffer.length];
    //
    for (int i = 0; i != buffer.length; i++) {
      bytes[i] = (byte) buffer[i];
    }
    //
    return bytes;
  }

  private static byte[] encodeU8sparse(int nblocks, long[] buffer) {
    final ByteBuffer bytes = ByteBuffer.allocate(nblocks * 5);
    //
    if(nblocks > 0) {
      long last = buffer[0];
      int lastIndex = 0;
      bytes.put((byte) (last & 0xff));
      //
      for (int i = 1; i < buffer.length; ++i) {
        if (buffer[i] != last) {
          bytes.putInt(i - lastIndex);
          last = buffer[i];
          lastIndex = i;
          bytes.put((byte) (last & 0xff));
        }
      }
      //
      bytes.putInt(buffer.length - lastIndex);
    }
    //
    return bytes.array();
  }

  private static byte[] encodeU8sparse(int nblocks, int[] buffer) {
    final ByteBuffer bytes = ByteBuffer.allocate(nblocks * 5);
    //
    if(nblocks > 0) {
      int last = buffer[0];
      int lastIndex = 0;
      bytes.put((byte) (last & 0xff));
      //
      for (int i = 1; i < buffer.length; ++i) {
        if (buffer[i] != last) {
          bytes.putInt(i - lastIndex);
          last = buffer[i];
          lastIndex = i;
          bytes.put((byte) (last & 0xff));
        }
      }
      //
      bytes.putInt(buffer.length - lastIndex);
    }
    //
    return bytes.array();
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

  private static byte[] encodeU16(int[] buffer) {
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

  private static byte[] encodeU16sparse(int nblocks, long[] buffer) {
    final ByteBuffer bytes = ByteBuffer.allocate(nblocks * 6);
    //
    if(nblocks > 0) {
      long last = buffer[0];
      int lastIndex = 0;
      bytes.putShort((short) (last & 0xffff));
      //
      for (int i = 1; i < buffer.length; ++i) {
        if (buffer[i] != last) {
          bytes.putInt(i - lastIndex);
          last = buffer[i];
          lastIndex = i;
          bytes.putShort((short) (last & 0xffff));
        }
      }
      //
      bytes.putInt(buffer.length - lastIndex);
    }
    //
    return bytes.array();
  }

  private static byte[] encodeU16sparse(int nblocks, int[] buffer) {
    final ByteBuffer bytes = ByteBuffer.allocate(nblocks * 6);
    //
    if(nblocks > 0) {
      int last = buffer[0];
      int lastIndex = 0;
      bytes.putShort((short) (last & 0xffff));
      //
      for (int i = 1; i < buffer.length; ++i) {
        if (buffer[i] != last) {
          bytes.putInt(i - lastIndex);
          last = buffer[i];
          lastIndex = i;
          bytes.putShort((short) (last & 0xffff));
        }
      }
      //
      bytes.putInt(buffer.length - lastIndex);
    }
    //
    return bytes.array();
  }

  private static byte[] encodeU32sparse(int nblocks, long[] buffer) {
    final ByteBuffer bytes = ByteBuffer.allocate(nblocks * 8);
    //
    if(nblocks > 0) {
      long last = buffer[0];
      int lastIndex = 0;
      bytes.putInt((int) (last & 0xffff_ffffL));
      //
      for (int i = 1; i < buffer.length; ++i) {
        if (buffer[i] != last) {
          bytes.putInt(i - lastIndex);
          last = buffer[i];
          lastIndex = i;
          bytes.putInt((int) (last & 0xffff_ffffL));
        }
      }
      //
      bytes.putInt(buffer.length - lastIndex);
    }
    //
    return bytes.array();
  }

  private static byte[] encodeU32sparse(int nblocks, int[] buffer) {
    final ByteBuffer bytes = ByteBuffer.allocate(nblocks * 8);
    //
    if(nblocks > 0) {
      int last = buffer[0];
      int lastIndex = 0;
      bytes.putInt(last);
      //
      for (int i = 1; i < buffer.length; ++i) {
        if (buffer[i] != last) {
          bytes.putInt(i - lastIndex);
          last = buffer[i];
          lastIndex = i;
          bytes.putInt(last);
        }
      }
      //
      bytes.putInt(buffer.length - lastIndex);
    }
    //
    return bytes.array();
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
