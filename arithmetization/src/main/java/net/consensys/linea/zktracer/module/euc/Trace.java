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

package net.consensys.linea.zktracer.module.euc;

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

  private final MappedByteBuffer ceil;
  private final MappedByteBuffer ct;
  private final MappedByteBuffer ctMax;
  private final MappedByteBuffer dividend;
  private final MappedByteBuffer divisor;
  private final MappedByteBuffer divisorByte;
  private final MappedByteBuffer done;
  private final MappedByteBuffer iomf;
  private final MappedByteBuffer quotient;
  private final MappedByteBuffer quotientByte;
  private final MappedByteBuffer remainder;
  private final MappedByteBuffer remainderByte;

  static List<ColumnHeader> headers(int length) {
    return List.of(
        new ColumnHeader("euc.CEIL", 32, length),
        new ColumnHeader("euc.CT", 2, length),
        new ColumnHeader("euc.CT_MAX", 2, length),
        new ColumnHeader("euc.DIVIDEND", 32, length),
        new ColumnHeader("euc.DIVISOR", 32, length),
        new ColumnHeader("euc.DIVISOR_BYTE", 1, length),
        new ColumnHeader("euc.DONE", 1, length),
        new ColumnHeader("euc.IOMF", 1, length),
        new ColumnHeader("euc.QUOTIENT", 32, length),
        new ColumnHeader("euc.QUOTIENT_BYTE", 1, length),
        new ColumnHeader("euc.REMAINDER", 32, length),
        new ColumnHeader("euc.REMAINDER_BYTE", 1, length));
  }

  public Trace(List<MappedByteBuffer> buffers) {
    this.ceil = buffers.get(0);
    this.ct = buffers.get(1);
    this.ctMax = buffers.get(2);
    this.dividend = buffers.get(3);
    this.divisor = buffers.get(4);
    this.divisorByte = buffers.get(5);
    this.done = buffers.get(6);
    this.iomf = buffers.get(7);
    this.quotient = buffers.get(8);
    this.quotientByte = buffers.get(9);
    this.remainder = buffers.get(10);
    this.remainderByte = buffers.get(11);
  }

  public int size() {
    if (!filled.isEmpty()) {
      throw new RuntimeException("Cannot measure a trace with a non-validated row.");
    }

    return this.currentLine;
  }

  public Trace ceil(final Bytes b) {
    if (filled.get(0)) {
      throw new IllegalStateException("euc.CEIL already set");
    } else {
      filled.set(0);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      ceil.put((byte) 0);
    }
    ceil.put(b.toArrayUnsafe());

    return this;
  }

  public Trace ct(final short b) {
    if (filled.get(1)) {
      throw new IllegalStateException("euc.CT already set");
    } else {
      filled.set(1);
    }

    ct.putShort(b);

    return this;
  }

  public Trace ctMax(final short b) {
    if (filled.get(2)) {
      throw new IllegalStateException("euc.CT_MAX already set");
    } else {
      filled.set(2);
    }

    ctMax.putShort(b);

    return this;
  }

  public Trace dividend(final Bytes b) {
    if (filled.get(3)) {
      throw new IllegalStateException("euc.DIVIDEND already set");
    } else {
      filled.set(3);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      dividend.put((byte) 0);
    }
    dividend.put(b.toArrayUnsafe());

    return this;
  }

  public Trace divisor(final Bytes b) {
    if (filled.get(4)) {
      throw new IllegalStateException("euc.DIVISOR already set");
    } else {
      filled.set(4);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      divisor.put((byte) 0);
    }
    divisor.put(b.toArrayUnsafe());

    return this;
  }

  public Trace divisorByte(final UnsignedByte b) {
    if (filled.get(5)) {
      throw new IllegalStateException("euc.DIVISOR_BYTE already set");
    } else {
      filled.set(5);
    }

    divisorByte.put(b.toByte());

    return this;
  }

  public Trace done(final Boolean b) {
    if (filled.get(6)) {
      throw new IllegalStateException("euc.DONE already set");
    } else {
      filled.set(6);
    }

    done.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace iomf(final Boolean b) {
    if (filled.get(7)) {
      throw new IllegalStateException("euc.IOMF already set");
    } else {
      filled.set(7);
    }

    iomf.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace quotient(final Bytes b) {
    if (filled.get(8)) {
      throw new IllegalStateException("euc.QUOTIENT already set");
    } else {
      filled.set(8);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      quotient.put((byte) 0);
    }
    quotient.put(b.toArrayUnsafe());

    return this;
  }

  public Trace quotientByte(final UnsignedByte b) {
    if (filled.get(9)) {
      throw new IllegalStateException("euc.QUOTIENT_BYTE already set");
    } else {
      filled.set(9);
    }

    quotientByte.put(b.toByte());

    return this;
  }

  public Trace remainder(final Bytes b) {
    if (filled.get(10)) {
      throw new IllegalStateException("euc.REMAINDER already set");
    } else {
      filled.set(10);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      remainder.put((byte) 0);
    }
    remainder.put(b.toArrayUnsafe());

    return this;
  }

  public Trace remainderByte(final UnsignedByte b) {
    if (filled.get(11)) {
      throw new IllegalStateException("euc.REMAINDER_BYTE already set");
    } else {
      filled.set(11);
    }

    remainderByte.put(b.toByte());

    return this;
  }

  public Trace validateRow() {
    if (!filled.get(0)) {
      throw new IllegalStateException("euc.CEIL has not been filled");
    }

    if (!filled.get(1)) {
      throw new IllegalStateException("euc.CT has not been filled");
    }

    if (!filled.get(2)) {
      throw new IllegalStateException("euc.CT_MAX has not been filled");
    }

    if (!filled.get(3)) {
      throw new IllegalStateException("euc.DIVIDEND has not been filled");
    }

    if (!filled.get(4)) {
      throw new IllegalStateException("euc.DIVISOR has not been filled");
    }

    if (!filled.get(5)) {
      throw new IllegalStateException("euc.DIVISOR_BYTE has not been filled");
    }

    if (!filled.get(6)) {
      throw new IllegalStateException("euc.DONE has not been filled");
    }

    if (!filled.get(7)) {
      throw new IllegalStateException("euc.IOMF has not been filled");
    }

    if (!filled.get(8)) {
      throw new IllegalStateException("euc.QUOTIENT has not been filled");
    }

    if (!filled.get(9)) {
      throw new IllegalStateException("euc.QUOTIENT_BYTE has not been filled");
    }

    if (!filled.get(10)) {
      throw new IllegalStateException("euc.REMAINDER has not been filled");
    }

    if (!filled.get(11)) {
      throw new IllegalStateException("euc.REMAINDER_BYTE has not been filled");
    }

    filled.clear();
    this.currentLine++;

    return this;
  }

  public Trace fillAndValidateRow() {
    if (!filled.get(0)) {
      ceil.position(ceil.position() + 32);
    }

    if (!filled.get(1)) {
      ct.position(ct.position() + 2);
    }

    if (!filled.get(2)) {
      ctMax.position(ctMax.position() + 2);
    }

    if (!filled.get(3)) {
      dividend.position(dividend.position() + 32);
    }

    if (!filled.get(4)) {
      divisor.position(divisor.position() + 32);
    }

    if (!filled.get(5)) {
      divisorByte.position(divisorByte.position() + 1);
    }

    if (!filled.get(6)) {
      done.position(done.position() + 1);
    }

    if (!filled.get(7)) {
      iomf.position(iomf.position() + 1);
    }

    if (!filled.get(8)) {
      quotient.position(quotient.position() + 32);
    }

    if (!filled.get(9)) {
      quotientByte.position(quotientByte.position() + 1);
    }

    if (!filled.get(10)) {
      remainder.position(remainder.position() + 32);
    }

    if (!filled.get(11)) {
      remainderByte.position(remainderByte.position() + 1);
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
