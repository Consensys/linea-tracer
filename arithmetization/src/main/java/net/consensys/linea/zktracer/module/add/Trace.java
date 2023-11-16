/*
 * Copyright ConsenSys AG.
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

package net.consensys.linea.zktracer.module.add;

import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.util.BitSet;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.types.UnsignedByte;

/**
 * WARNING: This code is generated automatically. Any modifications to this code may be overwritten
 * and could lead to unexpected behavior. Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
public record Trace(
    @JsonProperty("add.ACC_1") List<BigInteger> acc1,
    @JsonProperty("add.ACC_2") List<BigInteger> acc2,
    @JsonProperty("add.ARG_1_HI") List<BigInteger> arg1Hi,
    @JsonProperty("add.ARG_1_LO") List<BigInteger> arg1Lo,
    @JsonProperty("add.ARG_2_HI") List<BigInteger> arg2Hi,
    @JsonProperty("add.ARG_2_LO") List<BigInteger> arg2Lo,
    @JsonProperty("add.BYTE_1") List<UnsignedByte> byte1,
    @JsonProperty("add.BYTE_2") List<UnsignedByte> byte2,
    @JsonProperty("add.CT") List<BigInteger> ct,
    @JsonProperty("add.INST") List<BigInteger> inst,
    @JsonProperty("add.OVERFLOW") List<Boolean> overflow,
    @JsonProperty("add.RES_HI") List<BigInteger> resHi,
    @JsonProperty("add.RES_LO") List<BigInteger> resLo,
    @JsonProperty("add.STAMP") List<BigInteger> stamp) {
  static TraceBuilder builder(int length) {
    return new TraceBuilder(length);
  }

  public int size() {
    return this.acc1.size();
  }

  public static List<ColumnHeader> headers(int size) {
    return List.of(
        new ColumnHeader("add.ACC_1", 32, size),
        new ColumnHeader("add.ACC_2", 32, size),
        new ColumnHeader("add.ARG_1_HI", 32, size),
        new ColumnHeader("add.ARG_1_LO", 32, size),
        new ColumnHeader("add.ARG_2_HI", 32, size),
        new ColumnHeader("add.ARG_2_LO", 32, size),
        new ColumnHeader("add.BYTE_1", 1, size),
        new ColumnHeader("add.BYTE_2", 1, size),
        new ColumnHeader("add.CT", 32, size),
        new ColumnHeader("add.INST", 32, size),
        new ColumnHeader("add.OVERFLOW", 1, size),
        new ColumnHeader("add.RES_HI", 32, size),
        new ColumnHeader("add.RES_LO", 32, size),
        new ColumnHeader("add.STAMP", 32, size));
  }

  static class TraceBuilder {
    private final BitSet filled = new BitSet();
    private int currentLine = 0;

    private MappedByteBuffer acc1;
    private MappedByteBuffer acc2;
    private MappedByteBuffer arg1Hi;
    private MappedByteBuffer arg1Lo;
    private MappedByteBuffer arg2Hi;
    private MappedByteBuffer arg2Lo;
    private MappedByteBuffer byte1;
    private MappedByteBuffer byte2;
    private MappedByteBuffer ct;
    private MappedByteBuffer inst;
    private MappedByteBuffer overflow;
    private MappedByteBuffer resHi;
    private MappedByteBuffer resLo;
    private MappedByteBuffer stamp;

    TraceBuilder(int length) {}

    public int size() {
      if (!filled.isEmpty()) {
        throw new RuntimeException("Cannot measure a trace with a non-validated row.");
      }

      return this.currentLine;
    }

    public void setBuffers(List<MappedByteBuffer> buffers) {
      this.acc1 = buffers.get(0);
      this.acc2 = buffers.get(1);
      this.arg1Hi = buffers.get(2);
      this.arg1Lo = buffers.get(3);
      this.arg2Hi = buffers.get(4);
      this.arg2Lo = buffers.get(5);
      this.byte1 = buffers.get(6);
      this.byte2 = buffers.get(7);
      this.ct = buffers.get(8);
      this.inst = buffers.get(9);
      this.overflow = buffers.get(10);
      this.resHi = buffers.get(11);
      this.resLo = buffers.get(12);
      this.stamp = buffers.get(13);
    }

    public TraceBuilder acc1(final BigInteger b) {
      if (filled.get(0)) {
        throw new IllegalStateException("add.ACC_1 already set");
      } else {
        filled.set(0);
      }

      acc1.put(b.toByteArray());

      return this;
    }

    public TraceBuilder acc2(final BigInteger b) {
      if (filled.get(1)) {
        throw new IllegalStateException("add.ACC_2 already set");
      } else {
        filled.set(1);
      }

      acc2.put(b.toByteArray());

      return this;
    }

    public TraceBuilder arg1Hi(final BigInteger b) {
      if (filled.get(2)) {
        throw new IllegalStateException("add.ARG_1_HI already set");
      } else {
        filled.set(2);
      }

      arg1Hi.put(b.toByteArray());

      return this;
    }

    public TraceBuilder arg1Lo(final BigInteger b) {
      if (filled.get(3)) {
        throw new IllegalStateException("add.ARG_1_LO already set");
      } else {
        filled.set(3);
      }

      arg1Lo.put(b.toByteArray());

      return this;
    }

    public TraceBuilder arg2Hi(final BigInteger b) {
      if (filled.get(4)) {
        throw new IllegalStateException("add.ARG_2_HI already set");
      } else {
        filled.set(4);
      }

      arg2Hi.put(b.toByteArray());

      return this;
    }

    public TraceBuilder arg2Lo(final BigInteger b) {
      if (filled.get(5)) {
        throw new IllegalStateException("add.ARG_2_LO already set");
      } else {
        filled.set(5);
      }

      arg2Lo.put(b.toByteArray());

      return this;
    }

    public TraceBuilder byte1(final UnsignedByte b) {
      if (filled.get(6)) {
        throw new IllegalStateException("add.BYTE_1 already set");
      } else {
        filled.set(6);
      }

      byte1.put(b.toByte());

      return this;
    }

    public TraceBuilder byte2(final UnsignedByte b) {
      if (filled.get(7)) {
        throw new IllegalStateException("add.BYTE_2 already set");
      } else {
        filled.set(7);
      }

      byte2.put(b.toByte());

      return this;
    }

    public TraceBuilder ct(final BigInteger b) {
      if (filled.get(8)) {
        throw new IllegalStateException("add.CT already set");
      } else {
        filled.set(8);
      }

      ct.put(b.toByteArray());

      return this;
    }

    public TraceBuilder inst(final BigInteger b) {
      if (filled.get(9)) {
        throw new IllegalStateException("add.INST already set");
      } else {
        filled.set(9);
      }

      inst.put(b.toByteArray());

      return this;
    }

    public TraceBuilder overflow(final Boolean b) {
      if (filled.get(10)) {
        throw new IllegalStateException("add.OVERFLOW already set");
      } else {
        filled.set(10);
      }

      overflow.put((byte) (b ? 1 : 0));

      return this;
    }

    public TraceBuilder resHi(final BigInteger b) {
      if (filled.get(11)) {
        throw new IllegalStateException("add.RES_HI already set");
      } else {
        filled.set(11);
      }

      resHi.put(b.toByteArray());

      return this;
    }

    public TraceBuilder resLo(final BigInteger b) {
      if (filled.get(12)) {
        throw new IllegalStateException("add.RES_LO already set");
      } else {
        filled.set(12);
      }

      resLo.put(b.toByteArray());

      return this;
    }

    public TraceBuilder stamp(final BigInteger b) {
      if (filled.get(13)) {
        throw new IllegalStateException("add.STAMP already set");
      } else {
        filled.set(13);
      }

      stamp.put(b.toByteArray());

      return this;
    }

    public TraceBuilder validateRow() {
      if (!filled.get(0)) {
        throw new IllegalStateException("add.ACC_1 has not been filled");
      }

      if (!filled.get(1)) {
        throw new IllegalStateException("add.ACC_2 has not been filled");
      }

      if (!filled.get(2)) {
        throw new IllegalStateException("add.ARG_1_HI has not been filled");
      }

      if (!filled.get(3)) {
        throw new IllegalStateException("add.ARG_1_LO has not been filled");
      }

      if (!filled.get(4)) {
        throw new IllegalStateException("add.ARG_2_HI has not been filled");
      }

      if (!filled.get(5)) {
        throw new IllegalStateException("add.ARG_2_LO has not been filled");
      }

      if (!filled.get(6)) {
        throw new IllegalStateException("add.BYTE_1 has not been filled");
      }

      if (!filled.get(7)) {
        throw new IllegalStateException("add.BYTE_2 has not been filled");
      }

      if (!filled.get(8)) {
        throw new IllegalStateException("add.CT has not been filled");
      }

      if (!filled.get(9)) {
        throw new IllegalStateException("add.INST has not been filled");
      }

      if (!filled.get(10)) {
        throw new IllegalStateException("add.OVERFLOW has not been filled");
      }

      if (!filled.get(11)) {
        throw new IllegalStateException("add.RES_HI has not been filled");
      }

      if (!filled.get(12)) {
        throw new IllegalStateException("add.RES_LO has not been filled");
      }

      if (!filled.get(13)) {
        throw new IllegalStateException("add.STAMP has not been filled");
      }

      filled.clear();
      this.currentLine++;

      return this;
    }

    public TraceBuilder fillAndValidateRow() {
      filled.clear();
      this.currentLine++;

      return this;
    }

    public Trace build() {
      if (!filled.isEmpty()) {
        throw new IllegalStateException("Cannot build trace with a non-validated row.");
      }
      return null;
    }
  }
}
