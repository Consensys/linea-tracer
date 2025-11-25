package net.consensys.linea.zktracer.lt;

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
import java.nio.ByteBuffer;

import net.consensys.linea.zktracer.Trace;

/**
 * Provides a simple encoding of column data, where each element is stored directly in place. This
 * is suitable for the v1 file format.
 */
public class RawColumn implements Trace.Column {
  private final String name;
  private final int bitWidth;
  private final int byteWidth;
  private final long longMax;
  private final ByteBuffer buffer;

  public RawColumn(String name, int bitwidth, int length) {
    this.name = name;
    this.bitWidth = bitwidth;
    this.byteWidth = Util.byteWidth(bitwidth);
    this.longMax = 1L << bitwidth;
    this.buffer = ByteBuffer.allocate(length * byteWidth);
  }

  @Override
  public void write(boolean value) {
    this.buffer.put((byte) (value ? 1 : 0));
  }

  @Override
  public void write(long value) {
    // Sanity check
    if (longMax <= value) {
      throw new IllegalArgumentException(name + " has invalid value (" + value + ")");
    }
    //
    switch (byteWidth) {
      case 8:
        this.buffer.put((byte) (value >> 56));
      case 7:
        this.buffer.put((byte) (value >> 48));
      case 6:
        this.buffer.put((byte) (value >> 40));
      case 5:
        this.buffer.put((byte) (value >> 32));
      case 4:
        this.buffer.put((byte) (value >> 24));
      case 3:
        this.buffer.put((byte) (value >> 16));
      case 2:
        this.buffer.put((byte) (value >> 8));
      case 1:
        this.buffer.put((byte) value);
        break;
      default:
        throw new IllegalArgumentException(name + " has invalid width (" + byteWidth + "bytes)");
    }
  }

  /**
   * Write element bytes
   *
   * @param bytes stored in big-endian form and already trimmed.
   */
  @Override
  public void write(byte[] bytes) {
    final int n = Util.bitLengthOf(bytes);
    // Sanity check
    if (n > bitWidth || bytes.length > byteWidth) {
      throw new IllegalArgumentException(name + " has invalid width (" + n + " bits)");
    }
    // Write padding (if necessary)
    for (int i = bytes.length; i < byteWidth; i++) {
      buffer.put((byte) 0);
    }
    // Write data
    for (int i = 0; i != bytes.length; i++) {
      buffer.put(bytes[i]);
    }
  }

  /**
   * Access the underling array of bytes for this column.
   *
   * @return
   */
  public byte[] toBytes() {
    return buffer.array();
  }
}
