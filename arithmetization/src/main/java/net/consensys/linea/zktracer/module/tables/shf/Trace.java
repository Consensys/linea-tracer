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

package net.consensys.linea.zktracer.module.tables.shf;

import java.nio.MappedByteBuffer;
import java.util.BitSet;
import java.util.List;

import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.types.UnsignedByte;
import org.apache.tuweni.bytes.Bytes;

/**
 * WARNING: This code is generated automatically.
 *
 * <p>Any modifications to this code may be overwritten and could lead to unexpected behavior.
 * Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
public class Trace {

  private final BitSet filled = new BitSet();
  private int currentLine = 0;

  private final MappedByteBuffer byte1;
  private final MappedByteBuffer iomf;
  private final MappedByteBuffer las;
  private final MappedByteBuffer mshp;
  private final MappedByteBuffer ones;
  private final MappedByteBuffer rap;

  static List<ColumnHeader> headers(int length) {
    return List.of(
        new ColumnHeader("shfRT.BYTE1", 1, length),
        new ColumnHeader("shfRT.IOMF", 1, length),
        new ColumnHeader("shfRT.LAS", 32, length),
        new ColumnHeader("shfRT.MSHP", 2, length),
        new ColumnHeader("shfRT.ONES", 32, length),
        new ColumnHeader("shfRT.RAP", 32, length));
  }

  public Trace(List<MappedByteBuffer> buffers) {
    this.byte1 = buffers.get(0);
    this.iomf = buffers.get(1);
    this.las = buffers.get(2);
    this.mshp = buffers.get(3);
    this.ones = buffers.get(4);
    this.rap = buffers.get(5);
  }

  public int size() {
    if (!filled.isEmpty()) {
      throw new RuntimeException("Cannot measure a trace with a non-validated row.");
    }

    return this.currentLine;
  }

  public Trace byte1(final UnsignedByte b) {
    if (filled.get(0)) {
      throw new IllegalStateException("shfRT.BYTE1 already set");
    } else {
      filled.set(0);
    }

    byte1.put(b.toByte());

    return this;
  }

  public Trace iomf(final Boolean b) {
    if (filled.get(1)) {
      throw new IllegalStateException("shfRT.IOMF already set");
    } else {
      filled.set(1);
    }

    iomf.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace las(final Bytes b) {
    if (filled.get(2)) {
      throw new IllegalStateException("shfRT.LAS already set");
    } else {
      filled.set(2);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      las.put((byte) 0);
    }
    las.put(b.toArrayUnsafe());

    return this;
  }

  public Trace mshp(final short b) {
    if (filled.get(3)) {
      throw new IllegalStateException("shfRT.MSHP already set");
    } else {
      filled.set(3);
    }

    mshp.putShort(b);

    return this;
  }

  public Trace ones(final Bytes b) {
    if (filled.get(4)) {
      throw new IllegalStateException("shfRT.ONES already set");
    } else {
      filled.set(4);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      ones.put((byte) 0);
    }
    ones.put(b.toArrayUnsafe());

    return this;
  }

  public Trace rap(final Bytes b) {
    if (filled.get(5)) {
      throw new IllegalStateException("shfRT.RAP already set");
    } else {
      filled.set(5);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      rap.put((byte) 0);
    }
    rap.put(b.toArrayUnsafe());

    return this;
  }

  public Trace validateRow() {
    if (!filled.get(0)) {
      throw new IllegalStateException("shfRT.BYTE1 has not been filled");
    }

    if (!filled.get(1)) {
      throw new IllegalStateException("shfRT.IOMF has not been filled");
    }

    if (!filled.get(2)) {
      throw new IllegalStateException("shfRT.LAS has not been filled");
    }

    if (!filled.get(3)) {
      throw new IllegalStateException("shfRT.MSHP has not been filled");
    }

    if (!filled.get(4)) {
      throw new IllegalStateException("shfRT.ONES has not been filled");
    }

    if (!filled.get(5)) {
      throw new IllegalStateException("shfRT.RAP has not been filled");
    }

    filled.clear();
    this.currentLine++;

    return this;
  }

  public Trace fillAndValidateRow() {
    if (!filled.get(0)) {
      byte1.position(byte1.position() + 1);
    }

    if (!filled.get(1)) {
      iomf.position(iomf.position() + 1);
    }

    if (!filled.get(2)) {
      las.position(las.position() + 32);
    }

    if (!filled.get(3)) {
      mshp.position(mshp.position() + 2);
    }

    if (!filled.get(4)) {
      ones.position(ones.position() + 32);
    }

    if (!filled.get(5)) {
      rap.position(rap.position() + 32);
    }

    filled.clear();
    this.currentLine++;

    return this;
  }

  public void build() {
    if (!filled.isEmpty()) {
      throw new IllegalStateException("Cannot build trace with a non-validated row.");
    }
  }
}
