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
  public static final byte ENCODING_CONSTANT = 0;
  public static final byte ENCODING_STATIC_DENSE = 1;
  public static final byte ENCODING_STATIC_SPARSE = 2;
  public static final byte ENCODING_POOL_CONSTANT = 3;
  public static final byte ENCODING_POOL8_DENSE = 4;
  public static final byte ENCODING_POOL16_DENSE = 5;
  public static final byte ENCODING_POOL32_DENSE = 6;
  public static final byte ENCODING_POOL8_SPARSE = 7;
  public static final byte ENCODING_POOL16_SPARSE = 8;
  public static final byte ENCODING_POOL32_SPARSE = 9;

  /**
   * Construct a static encoding for a given set of column data.  This is where values are encoded directly in the
   * resulting byte array (i.e. rather than being encoded as indexes into the heap).
   *
   * @param buffer Column data
   * @return Encoded column data
   */
  public static Encoding of(int[] buffer) {
    int encoding;
    byte[] data;
    long maxValue = Util.maxValue(buffer);
    long minValue = Util.minValue(buffer);
    int nblocks = Util.approxUniqueElements(buffer);
    //
    if ((buffer.length == 0 || maxValue == minValue) && maxValue <= 0xFF_FFFF) {
      encoding = encoding(Encoding.ENCODING_CONSTANT, (int) maxValue);
      data = encodeU0(buffer.length);
    } else if (maxValue < 2L) {
      // Check for sparse encoding
      if (preferSparse(nblocks, buffer.length, 1, 8)) {
        encoding = encoding(Encoding.ENCODING_STATIC_SPARSE, 1);
        data = encodeU8Sparse32(nblocks, buffer);
      } else {
        encoding = encoding(Encoding.ENCODING_STATIC_DENSE, 1);
        data = encodeU1(buffer);
      }
    } else if (maxValue < 4L) {
      // Check for sparse encoding
      if (preferSparse(nblocks, buffer.length, 2, 8)) {
        encoding = encoding(Encoding.ENCODING_STATIC_SPARSE, 2);
        data = encodeU8Sparse32(nblocks, buffer);
      } else {
        encoding = encoding(Encoding.ENCODING_STATIC_DENSE, 2);
        data = encodeU2(buffer);
      }
    } else if (maxValue < 16L) {
      // Check for sparse encoding
      if (preferSparse(nblocks, buffer.length, 4, 8)) {
        encoding = encoding(Encoding.ENCODING_STATIC_SPARSE, 4);
        data = encodeU8Sparse32(nblocks, buffer);
      } else {
        encoding = encoding(Encoding.ENCODING_STATIC_DENSE, 4);
        data = encodeU4(buffer);
      }
    } else if (maxValue < 256L) {
      // Check for sparse encoding
      if (preferSparse(nblocks, buffer.length, 8, 8)) {
        encoding = encoding(Encoding.ENCODING_STATIC_SPARSE, 8);
        data = encodeU8Sparse32(nblocks, buffer);
      } else {
        encoding = encoding(Encoding.ENCODING_STATIC_DENSE, 8);
        data = encodeU8Dense(buffer);
      }
    } else if (maxValue < 65536L) {
      // Check for sparse encoding
      if (preferSparse(nblocks, buffer.length, 16, 16)) {
        encoding = encoding(Encoding.ENCODING_STATIC_SPARSE, 16);
        data = encodeU16Sparse32(nblocks, buffer);
      } else {
        encoding = encoding(Encoding.ENCODING_STATIC_DENSE, 16);
        data = encodeU16Dense(buffer);
      }
    } else {
      // Check for sparse encoding
      if (preferSparse(nblocks, buffer.length, 32, 32)) {
        encoding = encoding(Encoding.ENCODING_STATIC_SPARSE, 32);
        data = encodeU32Sparse32(nblocks, buffer);
      } else {
        encoding = encoding(Encoding.ENCODING_STATIC_DENSE, 32);
        data = encodeU32Dense(buffer);
      }
    }
    //
    return new Encoding(encoding, data);
  }

  /**
   * Construct a pooled encoding for a given set of column data. This is where values are encoded in the resulting
   * array as indexes into the heap (i.e. rather than being encoded directly into the byte array).  The benefit of
   * this encoding arises in two ways: (1) when there are lots of repeated large (e.g. u128) values; (2) when we have
   * a large column (e.g. u128) which contains many small items.  Furthermore, since the heap is shared across all
   * column in the encoded trace, this encoding benefits when the same value appears in multiple columns, etc.
   *
   * @param buffer Column data
   * @return Encoded column data
   */
  public static Encoding of(int[] buffer, int bitwidth, BytesHeap heap) {
    int encoding;
    byte[] data;
    long maxValue = Util.maxValue(buffer);
    long minValue = Util.minValue(buffer);
    int nblocks = Util.approxUniqueElements(buffer);
    //
    if ((buffer.length == 0 || maxValue == minValue) && maxValue <= 0xFF_FFFF) {
      if (buffer.length == 0 || maxValue == 0) {
        // NOTE: index 0 already represents actual 0
        encoding = encoding(Encoding.ENCODING_CONSTANT, 0);
      } else {
        encoding = encoding(Encoding.ENCODING_POOL_CONSTANT, (int) maxValue);
      }
      data = encodeU0(buffer.length);
    } else if (maxValue < 256L && preferSparse(nblocks, buffer.length, 8, 8)) {
      encoding = encoding(Encoding.ENCODING_POOL8_SPARSE, bitwidth);
      data = encodeU8Sparse32(nblocks, buffer);
    } else if (maxValue < 256L) {
      data = encodeU8Dense(buffer);
      encoding = encoding(ENCODING_POOL8_DENSE, bitwidth);
    } else if (maxValue < 65536L && preferSparse(nblocks, buffer.length, 16, 16)) {
      encoding = encoding(Encoding.ENCODING_POOL16_SPARSE, bitwidth);
      data = encodeU16Sparse32(nblocks, buffer);
    } else if (maxValue < 65536L) {
      data = encodeU16Dense(buffer);
      encoding = encoding(ENCODING_POOL16_DENSE, bitwidth);
    } else if (preferSparse(nblocks, buffer.length, 32, 32)) {
      encoding = encoding(Encoding.ENCODING_POOL32_SPARSE, bitwidth);
      data = encodeU32Sparse32(nblocks, buffer);
    } else {
      encoding = encoding(ENCODING_POOL32_DENSE, bitwidth);
      data = encodeU32Dense(buffer);
    }
    //

    return new Encoding(encoding, data);
  }

  /**
   * Check whether or not to use a sparse representation of the given data. The decision is made
   * simply by calculating the size of both the sparse and dense representations, and choosing the
   * smallest. In doing this calculation, we have to consider the width of values used in each
   * representation (e.g. because values can be wider when used in the sparse representation).
   *
   * @param nblocks Identifies the number of entries required for the sparse representation.
   * @param length Identifies the number of actual rows in the column.
   * @param denseWidth Identifies the width of elements in the dense representation.
   * @param sparseWidth Identifies the width of elements in the sparse representation.
   * @return true if the sparse representation is smallest, false otherwise.
   */
  private static boolean preferSparse(int nblocks, int length, int denseWidth, int sparseWidth) {
    int sparseLayout = (nblocks * (32 + sparseWidth));
    int denseLayout = length * denseWidth;
    return sparseLayout < denseLayout;
  }

  /**
   * Construct an "encoding identifier" from a given opcode and operand.
   *
   * @param opcode
   * @param operand
   * @return
   */
  private static int encoding(byte opcode, int operand) {
    int encoding = (opcode & 0xff) << 24;
    return encoding | (operand & 0xff_ffff);
  }

  /**
   * Encode an array of constant values. This amounts simply to writing the length (i.e. number of
   * rows) into the buffer, and nothing else.
   *
   * @param length Number of rows in the column.
   * @return byte encoding of the data
   */
  private static byte[] encodeU0(int length) {
    final ByteBuffer buf = ByteBuffer.allocate(4);
    buf.putInt(length);
    return buf.array();
  }

  /**
   * U1 encoding is given a special bit-level representation for efficiency. Specifically, 8
   * elements are packed into each byte of the resulting data. Furthermore, the number of unused
   * elements is stored in an additional last byte.
   *
   * @param buffer Contains only values in {0,1}.
   * @return byte encoding of the data
   */
  private static byte[] encodeU1(int[] buffer) {
    // Determine how many bytes required
    int n = Util.byteWidth(buffer.length);
    final byte[] bytes = new byte[n + 1];
    int bitIndex = 0;
    int byteIndex = 0;
    //
    for (int i = 0; i != buffer.length; i++) {
      int bit = (0x1 & buffer[i]) << bitIndex;
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
   * U2 encoding is given a special bit-level representation for efficiency. Specifically, 4
   * elements are packed into each byte of the resulting data. Furthermore, the number of unused
   * elements is stored in an additional last byte.
   *
   * @param buffer Contains only values in {0,1,2,3}.
   * @return byte encoding of the data
   */
  private static byte[] encodeU2(int[] buffer) {
    // Determine how many bytes required
    int n = Util.byteWidth(buffer.length * 2);
    final byte[] bytes = new byte[n + 1];
    int bitIndex = 0;
    int byteIndex = 0;
    //
    for (int i = 0; i != buffer.length; i++) {
      int bits = (0x3 & buffer[i]) << bitIndex;
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
    bytes[n] = (byte) ((n * 4) - buffer.length);
    // Done
    return bytes;
  }

  /**
   * U4 encoding is given a special bit-level representation for efficiency. Specifically, 2
   * elements are packed into each byte of the resulting data. Furthermore, the number of unused
   * elements is stored in an additional last byte.
   *
   * @param buffer Contains only values in {0,1,...14,15}.
   * @return byte encoding of the data
   */
  private static byte[] encodeU4(int[] buffer) {
    // Determine how many bytes required
    int n = Util.byteWidth(buffer.length * 4);
    final byte[] bytes = new byte[n + 1];
    int bitIndex = 0;
    int byteIndex = 0;
    //
    for (int i = 0; i != buffer.length; i++) {
      int bits = (0xf & buffer[i]) << bitIndex;
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
    bytes[n] = (byte) ((n * 2) - buffer.length);
    // Done
    return bytes;
  }

  /**
   * Encode a given set of byte values using a "dense encoding" where each value is stored
   * consecutively.
   *
   * @param buffer Contains only values in {0,1,...254,255}.
   * @return byte encoding of the data
   */
  private static byte[] encodeU8Dense(int[] buffer) {
    final byte[] bytes = new byte[buffer.length];
    //
    for (int i = 0; i != buffer.length; i++) {
      bytes[i] = (byte) buffer[i];
    }
    //
    return bytes;
  }

  /**
   * Encode a given set of byte values using a "sparse encoding" consisting of tuples (u8 value, u32
   * n), where each represents n copies of the given value.
   *
   * @param buffer Contains only values in {0,1,...65534,65535}.
   * @return byte encoding of the data
   */
  private static byte[] encodeU8Sparse32(int nblocks, int[] buffer) {
    final ByteBuffer bytes = ByteBuffer.allocate(nblocks * 5);
    //
    if (nblocks > 0) {
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

  /**
   * Encode a given set of 16bit values using a "dense encoding" where each value is stored
   * consecutively.
   *
   * @param buffer Contains only values in {0,1,...65534,65535}.
   * @return byte encoding of the data
   */
  private static byte[] encodeU16Dense(int[] buffer) {
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

  /**
   * Encode a given set of 16bit values using a "sparse encoding" consisting of tuples (u16 value,
   * u32 n), where each represents n copies of the given value.
   *
   * @param buffer Contains only values in {0,1,...65534,65535}.
   * @return byte encoding of the data
   */
  private static byte[] encodeU16Sparse32(int nblocks, int[] buffer) {
    final ByteBuffer bytes = ByteBuffer.allocate(nblocks * 6);
    //
    if (nblocks > 0) {
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

  /**
   * Encode a given set of 32bit values using a "dense encoding" where each value is stored
   * consecutively.
   *
   * @param buffer Contains values to be encoded.
   * @return byte encoding of the data
   */
  private static byte[] encodeU32Dense(int[] buffer) {
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

  /**
   * Encode a given set of 32bit values using a "sparse encoding" consisting of tuples (u32 value,
   * u32 n), where each represents n copies of the given value.
   *
   * @param buffer Contains only values in {0,1,...2^31}.
   * @return byte encoding of the data
   */
  private static byte[] encodeU32Sparse32(int nblocks, int[] buffer) {
    final ByteBuffer bytes = ByteBuffer.allocate(nblocks * 8);
    //
    if (nblocks > 0) {
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
}
