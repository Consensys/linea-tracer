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

package net.consensys.linea.zktracer.module.gas;

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

  private final MappedByteBuffer acc1;
  private final MappedByteBuffer acc2;
  private final MappedByteBuffer byte1;
  private final MappedByteBuffer byte2;
  private final MappedByteBuffer ct;
  private final MappedByteBuffer gasActl;
  private final MappedByteBuffer gasCost;
  private final MappedByteBuffer oogx;
  private final MappedByteBuffer stamp;

  static List<ColumnHeader> headers(int length) {
    return List.of(
        new ColumnHeader("gas.ACC_1", 32, length),
        new ColumnHeader("gas.ACC_2", 32, length),
        new ColumnHeader("gas.BYTE_1", 1, length),
        new ColumnHeader("gas.BYTE_2", 1, length),
        new ColumnHeader("gas.CT", 2, length),
        new ColumnHeader("gas.GAS_ACTL", 8, length),
        new ColumnHeader("gas.GAS_COST", 32, length),
        new ColumnHeader("gas.OOGX", 1, length),
        new ColumnHeader("gas.STAMP", 8, length));
  }

  public Trace(List<MappedByteBuffer> buffers) {
    this.acc1 = buffers.get(0);
    this.acc2 = buffers.get(1);
    this.byte1 = buffers.get(2);
    this.byte2 = buffers.get(3);
    this.ct = buffers.get(4);
    this.gasActl = buffers.get(5);
    this.gasCost = buffers.get(6);
    this.oogx = buffers.get(7);
    this.stamp = buffers.get(8);
  }

  public int size() {
    if (!filled.isEmpty()) {
      throw new RuntimeException("Cannot measure a trace with a non-validated row.");
    }

    return this.currentLine;
  }

  public Trace acc1(final Bytes b) {
    if (filled.get(0)) {
      throw new IllegalStateException("gas.ACC_1 already set");
    } else {
      filled.set(0);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      acc1.put((byte) 0);
    }
    acc1.put(b.toArrayUnsafe());

    return this;
  }

  public Trace acc2(final Bytes b) {
    if (filled.get(1)) {
      throw new IllegalStateException("gas.ACC_2 already set");
    } else {
      filled.set(1);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      acc2.put((byte) 0);
    }
    acc2.put(b.toArrayUnsafe());

    return this;
  }

  public Trace byte1(final UnsignedByte b) {
    if (filled.get(2)) {
      throw new IllegalStateException("gas.BYTE_1 already set");
    } else {
      filled.set(2);
    }

    byte1.put(b.toByte());

    return this;
  }

  public Trace byte2(final UnsignedByte b) {
    if (filled.get(3)) {
      throw new IllegalStateException("gas.BYTE_2 already set");
    } else {
      filled.set(3);
    }

    byte2.put(b.toByte());

    return this;
  }

  public Trace ct(final short b) {
    if (filled.get(4)) {
      throw new IllegalStateException("gas.CT already set");
    } else {
      filled.set(4);
    }

    ct.putShort(b);

    return this;
  }

  public Trace gasActl(final long b) {
    if (filled.get(5)) {
      throw new IllegalStateException("gas.GAS_ACTL already set");
    } else {
      filled.set(5);
    }

    gasActl.putLong(b);

    return this;
  }

  public Trace gasCost(final Bytes b) {
    if (filled.get(6)) {
      throw new IllegalStateException("gas.GAS_COST already set");
    } else {
      filled.set(6);
    }

    final byte[] bs = b.toArrayUnsafe();
    for (int i = bs.length; i < 32; i++) {
      gasCost.put((byte) 0);
    }
    gasCost.put(b.toArrayUnsafe());

    return this;
  }

  public Trace oogx(final Boolean b) {
    if (filled.get(7)) {
      throw new IllegalStateException("gas.OOGX already set");
    } else {
      filled.set(7);
    }

    oogx.put((byte) (b ? 1 : 0));

    return this;
  }

  public Trace stamp(final long b) {
    if (filled.get(8)) {
      throw new IllegalStateException("gas.STAMP already set");
    } else {
      filled.set(8);
    }

    stamp.putLong(b);

    return this;
  }

  public Trace validateRow() {
    if (!filled.get(0)) {
      throw new IllegalStateException("gas.ACC_1 has not been filled");
    }

    if (!filled.get(1)) {
      throw new IllegalStateException("gas.ACC_2 has not been filled");
    }

    if (!filled.get(2)) {
      throw new IllegalStateException("gas.BYTE_1 has not been filled");
    }

    if (!filled.get(3)) {
      throw new IllegalStateException("gas.BYTE_2 has not been filled");
    }

    if (!filled.get(4)) {
      throw new IllegalStateException("gas.CT has not been filled");
    }

    if (!filled.get(5)) {
      throw new IllegalStateException("gas.GAS_ACTL has not been filled");
    }

    if (!filled.get(6)) {
      throw new IllegalStateException("gas.GAS_COST has not been filled");
    }

    if (!filled.get(7)) {
      throw new IllegalStateException("gas.OOGX has not been filled");
    }

    if (!filled.get(8)) {
      throw new IllegalStateException("gas.STAMP has not been filled");
    }

    filled.clear();
    this.currentLine++;

    return this;
  }

  public Trace fillAndValidateRow() {
    if (!filled.get(0)) {
      acc1.position(acc1.position() + 32);
    }

    if (!filled.get(1)) {
      acc2.position(acc2.position() + 32);
    }

    if (!filled.get(2)) {
      byte1.position(byte1.position() + 1);
    }

    if (!filled.get(3)) {
      byte2.position(byte2.position() + 1);
    }

    if (!filled.get(4)) {
      ct.position(ct.position() + 2);
    }

    if (!filled.get(5)) {
      gasActl.position(gasActl.position() + 8);
    }

    if (!filled.get(6)) {
      gasCost.position(gasCost.position() + 32);
    }

    if (!filled.get(7)) {
      oogx.position(oogx.position() + 1);
    }

    if (!filled.get(8)) {
      stamp.position(stamp.position() + 8);
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
