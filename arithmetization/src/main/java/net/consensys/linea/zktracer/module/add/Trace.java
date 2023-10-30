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

import com.fasterxml.jackson.annotation.JsonProperty;
import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.types.EWord;
import net.consensys.linea.zktracer.types.UnsignedByte;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * WARNING: This code is generated automatically.
 * Any modifications to this code may be overwritten and could lead to unexpected behavior.
 * Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
public record Trace(
  @JsonProperty("rACC_1") List<BigInteger> rAcc1,
  @JsonProperty("rACC_2") List<BigInteger> rAcc2,
  @JsonProperty("rARG_1_HI") List<BigInteger> rArg1Hi,
  @JsonProperty("rARG_1_LO") List<BigInteger> rArg1Lo,
  @JsonProperty("rARG_2_HI") List<BigInteger> rArg2Hi,
  @JsonProperty("rARG_2_LO") List<BigInteger> rArg2Lo,
  @JsonProperty("rBYTE_1") List<UnsignedByte> rByte1,
  @JsonProperty("rBYTE_2") List<UnsignedByte> rByte2,
  @JsonProperty("rCT") List<BigInteger> rCt,
  @JsonProperty("rINST") List<BigInteger> rInst,
  @JsonProperty("rOVERFLOW") List<Boolean> rOverflow,
  @JsonProperty("rRES_HI") List<BigInteger> rResHi,
  @JsonProperty("rRES_LO") List<BigInteger> rResLo,
  @JsonProperty("rSTAMP") List<BigInteger> rStamp) { 
  static TraceBuilder builder(int length) {
    return new TraceBuilder(length);
  }

  public int size() {
      return this.rAcc1.size();
  }

  public static List<ColumnHeader> headers(int size) {
    return List.of(
      new ColumnHeader("add.ACC_1", 255, size),
      new ColumnHeader("add.ACC_2", 255, size),
      new ColumnHeader("add.ARG_1_HI", 255, size),
      new ColumnHeader("add.ARG_1_LO", 255, size),
      new ColumnHeader("add.ARG_2_HI", 256, size),
      new ColumnHeader("add.ARG_2_LO", 256, size),
      new ColumnHeader("add.BYTE_1", 255, size),
      new ColumnHeader("add.BYTE_2", 255, size),
      new ColumnHeader("add.CT", 255, size),
      new ColumnHeader("add.INST", 255, size),
      new ColumnHeader("add.OVERFLOW", 255, size),
      new ColumnHeader("add.RES_HI", 255, size),
      new ColumnHeader("add.RES_LO", 255, size),
      new ColumnHeader("add.STAMP", 255, size));
  }

  static class BufferTraceWriter {
    private final BitSet filled = new BitSet();
    private final int length;
    private int currentLength = 0;
    private final ByteBuffer target;

    public BufferTraceWriter(ByteBuffer target, int length) {
      this.length = length;
      this.target = target;
    }


    private void writerAcc1(final BigInteger b) {
      this.target.put(0 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writerAcc2(final BigInteger b) {
      this.target.put(32 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writerArg1Hi(final BigInteger b) {
      this.target.put(64 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writerArg1Lo(final BigInteger b) {
      this.target.put(96 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writerArg2Hi(final BigInteger b) {
      this.target.put(128 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writerArg2Lo(final BigInteger b) {
      this.target.put(160 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writerByte1(final UnsignedByte b) {
      this.target.put(192 * this.length + this.currentLength*1, b.toByte());
    }
    private void writerByte2(final UnsignedByte b) {
      this.target.put(193 * this.length + this.currentLength*1, b.toByte());
    }
    private void writerCt(final BigInteger b) {
      this.target.put(194 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writerInst(final BigInteger b) {
      this.target.put(226 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writerOverflow(final Boolean b) {
      this.target.put(258 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writerResHi(final BigInteger b) {
      this.target.put(259 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writerResLo(final BigInteger b) {
      this.target.put(291 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writerStamp(final BigInteger b) {
      this.target.put(323 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }


    public BufferTraceWriter acc1(final BigInteger b) {
      if (filled.get(0)) {
        throw new IllegalStateException("ACC_1 already set");
      } else {
        filled.set(0);
      }

      this.writerAcc1(b);

      return this;
    }
    public BufferTraceWriter acc2(final BigInteger b) {
      if (filled.get(1)) {
        throw new IllegalStateException("ACC_2 already set");
      } else {
        filled.set(1);
      }

      this.writerAcc2(b);

      return this;
    }
    public BufferTraceWriter arg1Hi(final BigInteger b) {
      if (filled.get(2)) {
        throw new IllegalStateException("ARG_1_HI already set");
      } else {
        filled.set(2);
      }

      this.writerArg1Hi(b);

      return this;
    }
    public BufferTraceWriter arg1Lo(final BigInteger b) {
      if (filled.get(3)) {
        throw new IllegalStateException("ARG_1_LO already set");
      } else {
        filled.set(3);
      }

      this.writerArg1Lo(b);

      return this;
    }
    public BufferTraceWriter arg2Hi(final BigInteger b) {
      if (filled.get(4)) {
        throw new IllegalStateException("ARG_2_HI already set");
      } else {
        filled.set(4);
      }

      this.writerArg2Hi(b);

      return this;
    }
    public BufferTraceWriter arg2Lo(final BigInteger b) {
      if (filled.get(5)) {
        throw new IllegalStateException("ARG_2_LO already set");
      } else {
        filled.set(5);
      }

      this.writerArg2Lo(b);

      return this;
    }
    public BufferTraceWriter byte1(final UnsignedByte b) {
      if (filled.get(6)) {
        throw new IllegalStateException("BYTE_1 already set");
      } else {
        filled.set(6);
      }

      this.writerByte1(b);

      return this;
    }
    public BufferTraceWriter byte2(final UnsignedByte b) {
      if (filled.get(7)) {
        throw new IllegalStateException("BYTE_2 already set");
      } else {
        filled.set(7);
      }

      this.writerByte2(b);

      return this;
    }
    public BufferTraceWriter ct(final BigInteger b) {
      if (filled.get(8)) {
        throw new IllegalStateException("CT already set");
      } else {
        filled.set(8);
      }

      this.writerCt(b);

      return this;
    }
    public BufferTraceWriter inst(final BigInteger b) {
      if (filled.get(9)) {
        throw new IllegalStateException("INST already set");
      } else {
        filled.set(9);
      }

      this.writerInst(b);

      return this;
    }
    public BufferTraceWriter overflow(final Boolean b) {
      if (filled.get(10)) {
        throw new IllegalStateException("OVERFLOW already set");
      } else {
        filled.set(10);
      }

      this.writerOverflow(b);

      return this;
    }
    public BufferTraceWriter resHi(final BigInteger b) {
      if (filled.get(11)) {
        throw new IllegalStateException("RES_HI already set");
      } else {
        filled.set(11);
      }

      this.writerResHi(b);

      return this;
    }
    public BufferTraceWriter resLo(final BigInteger b) {
      if (filled.get(12)) {
        throw new IllegalStateException("RES_LO already set");
      } else {
        filled.set(12);
      }

      this.writerResLo(b);

      return this;
    }
    public BufferTraceWriter stamp(final BigInteger b) {
      if (filled.get(13)) {
        throw new IllegalStateException("STAMP already set");
      } else {
        filled.set(13);
      }

      this.writerStamp(b);

      return this;
    }

    public BufferTraceWriter validateRow() {
      if (!filled.get(0)) {
        throw new IllegalStateException("rACC_1 has not been filled");
      }

      if (!filled.get(1)) {
        throw new IllegalStateException("rACC_2 has not been filled");
      }

      if (!filled.get(2)) {
        throw new IllegalStateException("rARG_1_HI has not been filled");
      }

      if (!filled.get(3)) {
        throw new IllegalStateException("rARG_1_LO has not been filled");
      }

      if (!filled.get(4)) {
        throw new IllegalStateException("rARG_2_HI has not been filled");
      }

      if (!filled.get(5)) {
        throw new IllegalStateException("rARG_2_LO has not been filled");
      }

      if (!filled.get(6)) {
        throw new IllegalStateException("rBYTE_1 has not been filled");
      }

      if (!filled.get(7)) {
        throw new IllegalStateException("rBYTE_2 has not been filled");
      }

      if (!filled.get(8)) {
        throw new IllegalStateException("rCT has not been filled");
      }

      if (!filled.get(9)) {
        throw new IllegalStateException("rINST has not been filled");
      }

      if (!filled.get(10)) {
        throw new IllegalStateException("rOVERFLOW has not been filled");
      }

      if (!filled.get(11)) {
        throw new IllegalStateException("rRES_HI has not been filled");
      }

      if (!filled.get(12)) {
        throw new IllegalStateException("rRES_LO has not been filled");
      }

      if (!filled.get(13)) {
        throw new IllegalStateException("rSTAMP has not been filled");
      }


      filled.clear();
      this.currentLength++;

      return this;
    }

    public BufferTraceWriter fillAndValidateRow() {
      this.currentLength++;
      return this;
    }
  }

  static class TraceBuilder {
    private final BitSet filled = new BitSet();

    @JsonProperty("rACC_1")
    private final List<BigInteger> rAcc1;
    @JsonProperty("rACC_2")
    private final List<BigInteger> rAcc2;
    @JsonProperty("rARG_1_HI")
    private final List<BigInteger> rArg1Hi;
    @JsonProperty("rARG_1_LO")
    private final List<BigInteger> rArg1Lo;
    @JsonProperty("rARG_2_HI")
    private final List<BigInteger> rArg2Hi;
    @JsonProperty("rARG_2_LO")
    private final List<BigInteger> rArg2Lo;
    @JsonProperty("rBYTE_1")
    private final List<UnsignedByte> rByte1;
    @JsonProperty("rBYTE_2")
    private final List<UnsignedByte> rByte2;
    @JsonProperty("rCT")
    private final List<BigInteger> rCt;
    @JsonProperty("rINST")
    private final List<BigInteger> rInst;
    @JsonProperty("rOVERFLOW")
    private final List<Boolean> rOverflow;
    @JsonProperty("rRES_HI")
    private final List<BigInteger> rResHi;
    @JsonProperty("rRES_LO")
    private final List<BigInteger> rResLo;
    @JsonProperty("rSTAMP")
    private final List<BigInteger> rStamp;

    TraceBuilder(int length) {
      this.rAcc1 = new ArrayList<>(length);
      this.rAcc2 = new ArrayList<>(length);
      this.rArg1Hi = new ArrayList<>(length);
      this.rArg1Lo = new ArrayList<>(length);
      this.rArg2Hi = new ArrayList<>(length);
      this.rArg2Lo = new ArrayList<>(length);
      this.rByte1 = new ArrayList<>(length);
      this.rByte2 = new ArrayList<>(length);
      this.rCt = new ArrayList<>(length);
      this.rInst = new ArrayList<>(length);
      this.rOverflow = new ArrayList<>(length);
      this.rResHi = new ArrayList<>(length);
      this.rResLo = new ArrayList<>(length);
      this.rStamp = new ArrayList<>(length);
    }

    public int size() {
      if (!filled.isEmpty()) {
        throw new RuntimeException("Cannot measure a trace with a non-validated row.");
      }

      return this.rAcc1.size();
    }

    public TraceBuilder acc1(final BigInteger b) {
      if (filled.get(0)) {
        throw new IllegalStateException("ACC_1 already set");
      } else {
        filled.set(0);
      }

      rAcc1.add(b);

      return this;
    }

    public TraceBuilder acc2(final BigInteger b) {
      if (filled.get(1)) {
        throw new IllegalStateException("ACC_2 already set");
      } else {
        filled.set(1);
      }

      rAcc2.add(b);

      return this;
    }

    public TraceBuilder arg1Hi(final BigInteger b) {
      if (filled.get(2)) {
        throw new IllegalStateException("ARG_1_HI already set");
      } else {
        filled.set(2);
      }

      rArg1Hi.add(b);

      return this;
    }

    public TraceBuilder arg1Lo(final BigInteger b) {
      if (filled.get(3)) {
        throw new IllegalStateException("ARG_1_LO already set");
      } else {
        filled.set(3);
      }

      rArg1Lo.add(b);

      return this;
    }

    public TraceBuilder arg2Hi(final BigInteger b) {
      if (filled.get(4)) {
        throw new IllegalStateException("ARG_2_HI already set");
      } else {
        filled.set(4);
      }

      rArg2Hi.add(b);

      return this;
    }

    public TraceBuilder arg2Lo(final BigInteger b) {
      if (filled.get(5)) {
        throw new IllegalStateException("ARG_2_LO already set");
      } else {
        filled.set(5);
      }

      rArg2Lo.add(b);

      return this;
    }

    public TraceBuilder byte1(final UnsignedByte b) {
      if (filled.get(6)) {
        throw new IllegalStateException("BYTE_1 already set");
      } else {
        filled.set(6);
      }

      rByte1.add(b);

      return this;
    }

    public TraceBuilder byte2(final UnsignedByte b) {
      if (filled.get(7)) {
        throw new IllegalStateException("BYTE_2 already set");
      } else {
        filled.set(7);
      }

      rByte2.add(b);

      return this;
    }

    public TraceBuilder ct(final BigInteger b) {
      if (filled.get(8)) {
        throw new IllegalStateException("CT already set");
      } else {
        filled.set(8);
      }

      rCt.add(b);

      return this;
    }

    public TraceBuilder inst(final BigInteger b) {
      if (filled.get(9)) {
        throw new IllegalStateException("INST already set");
      } else {
        filled.set(9);
      }

      rInst.add(b);

      return this;
    }

    public TraceBuilder overflow(final Boolean b) {
      if (filled.get(10)) {
        throw new IllegalStateException("OVERFLOW already set");
      } else {
        filled.set(10);
      }

      rOverflow.add(b);

      return this;
    }

    public TraceBuilder resHi(final BigInteger b) {
      if (filled.get(11)) {
        throw new IllegalStateException("RES_HI already set");
      } else {
        filled.set(11);
      }

      rResHi.add(b);

      return this;
    }

    public TraceBuilder resLo(final BigInteger b) {
      if (filled.get(12)) {
        throw new IllegalStateException("RES_LO already set");
      } else {
        filled.set(12);
      }

      rResLo.add(b);

      return this;
    }

    public TraceBuilder stamp(final BigInteger b) {
      if (filled.get(13)) {
        throw new IllegalStateException("STAMP already set");
      } else {
        filled.set(13);
      }

      rStamp.add(b);

      return this;
    }

    public TraceBuilder validateRow() {
      if (!filled.get(0)) {
        throw new IllegalStateException("rACC_1 has not been filled");
      }

      if (!filled.get(1)) {
        throw new IllegalStateException("rACC_2 has not been filled");
      }

      if (!filled.get(2)) {
        throw new IllegalStateException("rARG_1_HI has not been filled");
      }

      if (!filled.get(3)) {
        throw new IllegalStateException("rARG_1_LO has not been filled");
      }

      if (!filled.get(4)) {
        throw new IllegalStateException("rARG_2_HI has not been filled");
      }

      if (!filled.get(5)) {
        throw new IllegalStateException("rARG_2_LO has not been filled");
      }

      if (!filled.get(6)) {
        throw new IllegalStateException("rBYTE_1 has not been filled");
      }

      if (!filled.get(7)) {
        throw new IllegalStateException("rBYTE_2 has not been filled");
      }

      if (!filled.get(8)) {
        throw new IllegalStateException("rCT has not been filled");
      }

      if (!filled.get(9)) {
        throw new IllegalStateException("rINST has not been filled");
      }

      if (!filled.get(10)) {
        throw new IllegalStateException("rOVERFLOW has not been filled");
      }

      if (!filled.get(11)) {
        throw new IllegalStateException("rRES_HI has not been filled");
      }

      if (!filled.get(12)) {
        throw new IllegalStateException("rRES_LO has not been filled");
      }

      if (!filled.get(13)) {
        throw new IllegalStateException("rSTAMP has not been filled");
      }


      filled.clear();

      return this;
    }

    public TraceBuilder fillAndValidateRow() {
      if (!filled.get(0)) {
          rAcc1.add(BigInteger.ZERO);
          this.filled.set(0);
      }
      if (!filled.get(1)) {
          rAcc2.add(BigInteger.ZERO);
          this.filled.set(1);
      }
      if (!filled.get(2)) {
          rArg1Hi.add(BigInteger.ZERO);
          this.filled.set(2);
      }
      if (!filled.get(3)) {
          rArg1Lo.add(BigInteger.ZERO);
          this.filled.set(3);
      }
      if (!filled.get(4)) {
          rArg2Hi.add(BigInteger.ZERO);
          this.filled.set(4);
      }
      if (!filled.get(5)) {
          rArg2Lo.add(BigInteger.ZERO);
          this.filled.set(5);
      }
      if (!filled.get(6)) {
          rByte1.add(UnsignedByte.of(0));
          this.filled.set(6);
      }
      if (!filled.get(7)) {
          rByte2.add(UnsignedByte.of(0));
          this.filled.set(7);
      }
      if (!filled.get(8)) {
          rCt.add(BigInteger.ZERO);
          this.filled.set(8);
      }
      if (!filled.get(9)) {
          rInst.add(BigInteger.ZERO);
          this.filled.set(9);
      }
      if (!filled.get(10)) {
          rOverflow.add(false);
          this.filled.set(10);
      }
      if (!filled.get(11)) {
          rResHi.add(BigInteger.ZERO);
          this.filled.set(11);
      }
      if (!filled.get(12)) {
          rResLo.add(BigInteger.ZERO);
          this.filled.set(12);
      }
      if (!filled.get(13)) {
          rStamp.add(BigInteger.ZERO);
          this.filled.set(13);
      }

      return this.validateRow();
    }

    public Trace build() {
      if (!filled.isEmpty()) {
        throw new IllegalStateException("Cannot build trace with a non-validated row.");
      }

      return new Trace(
        rAcc1,
        rAcc2,
        rArg1Hi,
        rArg1Lo,
        rArg2Hi,
        rArg2Lo,
        rByte1,
        rByte2,
        rCt,
        rInst,
        rOverflow,
        rResHi,
        rResLo,
        rStamp);
    }
  }
}
