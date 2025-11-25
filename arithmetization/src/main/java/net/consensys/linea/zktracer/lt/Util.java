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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import net.consensys.linea.zktracer.Trace;

public class Util {

  /**
   * Write header information for the trace file.
   *
   * @param headers Column headers.*
   */
  public static byte[] getColumnHeaderBytes(Trace.ColumnHeader[] headers) throws IOException {
    ByteBuffer buffer = ByteBuffer.allocate(getColumnHeadersSize(headers));
    // Write column count as uint32
    buffer.putInt(countHeaders(headers));
    // Write column headers one-by-one
    for (Trace.ColumnHeader h : headers) {
      if (h != null) {
        buffer.putShort((short) h.name().length());
        buffer.put(h.name().getBytes());
        buffer.put((byte) Util.byteWidth(h.bitwidth()));
        buffer.putInt(h.length());
      }
    }
    //
    return buffer.array();
  }

  /**
   * Precompute the size of the trace file in order to memory map the buffers.
   *
   * @param headers Set of headers for the columns being written.
   * @return Number of bytes requires for the trace file header.
   */
  private static int getColumnHeadersSize(Trace.ColumnHeader[] headers) {
    int nBytes = 4; // column count

    for (Trace.ColumnHeader header : headers) {
      if (header != null) {
        nBytes += 2; // name length
        nBytes += header.name().length();
        nBytes += 1; // byte per element
        nBytes += 4; // element count
      }
    }

    return nBytes;
  }

  /**
   * Counter number of active (i.e. non-null) headers. A header can be null if it represents a
   * column in a module which is not activated for this trace.
   */
  private static int countHeaders(Trace.ColumnHeader[] headers) {
    int count = 0;
    for (Trace.ColumnHeader h : headers) {
      if (h != null) {
        count++;
      }
    }
    return count;
  }

  /**
   * Convert a given bitwidth into a bytewidth. For example, a bitwidth of 1 becomes a bytewidth of
   * 1 whilst a bitwidth of 9 becomes a bytewidth of 2, etc.
   *
   * @param bitwidth
   * @return
   */
  static int byteWidth(int bitwidth) {
    int byteWidth = bitwidth / 8;
    //
    if ((bitwidth % 8) != 0) {
      byteWidth++;
    }
    //
    return byteWidth;
  }

  /**
   * Determine the minimal number of bits required to store the value held in a given set of bytes (assuming a big
   * endian layout).  For example, a value of 0x0AFF (binary 0b00001010_11111111). has a bit length of 12.
   *
   * @param bytes the bytes (stored in big endian form) whose bitlength is being determined.
   * @return
   */
  static int bitLengthOf(byte[] bytes) {
    int n = 0;
    // Skip forward
    while (n < bytes.length && bytes[n] == 0) {
      n++;
    }
    // Determine width
    if (n == bytes.length) {
      return 0;
    } else {
      // n > 0
      int m = bytes.length - n - 1;
      return bitLengthOf(bytes[n]) + (m * 8);
    }
  }

  /**
   * Determine the minimal number of bits required to store the value held in a given byte.  For example, 0x0A
   * (binary 0b00001010) has a bit length of 4.
   *
   * @param b the byte whose bitlength is being determined.
   *
   * @return
   */
  static int bitLengthOf(byte b) {
    // Convert into unsigned representation
    int val = b & 0xff;
    // NOTE: we could further improve performance by turning this into one big lookup table.
    if (val >= 16) {
      return bits[val >> 4] + 4;
    } else {
      return bits[val];
    }
  }

  private static final int[] bits = {0, 1, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4};
}
