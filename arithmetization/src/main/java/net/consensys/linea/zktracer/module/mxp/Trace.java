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

package net.consensys.linea.zktracer.module.mxp;

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
  @JsonProperty("ACC_1") List<BigInteger> acc1,
  @JsonProperty("ACC_2") List<BigInteger> acc2,
  @JsonProperty("ACC_3") List<BigInteger> acc3,
  @JsonProperty("ACC_4") List<BigInteger> acc4,
  @JsonProperty("ACC_A") List<BigInteger> accA,
  @JsonProperty("ACC_Q") List<BigInteger> accQ,
  @JsonProperty("ACC_W") List<BigInteger> accW,
  @JsonProperty("BYTE_1") List<UnsignedByte> byte1,
  @JsonProperty("BYTE_2") List<UnsignedByte> byte2,
  @JsonProperty("BYTE_3") List<UnsignedByte> byte3,
  @JsonProperty("BYTE_4") List<UnsignedByte> byte4,
  @JsonProperty("BYTE_A") List<UnsignedByte> byteA,
  @JsonProperty("BYTE_Q") List<UnsignedByte> byteQ,
  @JsonProperty("BYTE_QQ") List<BigInteger> byteQq,
  @JsonProperty("BYTE_R") List<BigInteger> byteR,
  @JsonProperty("BYTE_W") List<UnsignedByte> byteW,
  @JsonProperty("C_MEM") List<BigInteger> cMem,
  @JsonProperty("C_MEM_NEW") List<BigInteger> cMemNew,
  @JsonProperty("CN") List<BigInteger> cn,
  @JsonProperty("COMP") List<Boolean> comp,
  @JsonProperty("CT") List<BigInteger> ct,
  @JsonProperty("DEPLOYS") List<Boolean> deploys,
  @JsonProperty("EXPANDS") List<Boolean> expands,
  @JsonProperty("GAS_MXP") List<BigInteger> gasMxp,
  @JsonProperty("GBYTE") List<BigInteger> gbyte,
  @JsonProperty("GWORD") List<BigInteger> gword,
  @JsonProperty("INST") List<BigInteger> inst,
  @JsonProperty("LIN_COST") List<BigInteger> linCost,
  @JsonProperty("MAX_OFFSET") List<BigInteger> maxOffset,
  @JsonProperty("MAX_OFFSET_1") List<BigInteger> maxOffset1,
  @JsonProperty("MAX_OFFSET_2") List<BigInteger> maxOffset2,
  @JsonProperty("MXP_TYPE_1") List<Boolean> mxpType1,
  @JsonProperty("MXP_TYPE_2") List<Boolean> mxpType2,
  @JsonProperty("MXP_TYPE_3") List<Boolean> mxpType3,
  @JsonProperty("MXP_TYPE_4") List<Boolean> mxpType4,
  @JsonProperty("MXP_TYPE_5") List<Boolean> mxpType5,
  @JsonProperty("MXPX") List<Boolean> mxpx,
  @JsonProperty("NOOP") List<Boolean> noop,
  @JsonProperty("OFFSET_1_HI") List<BigInteger> offset1Hi,
  @JsonProperty("OFFSET_1_LO") List<BigInteger> offset1Lo,
  @JsonProperty("OFFSET_2_HI") List<BigInteger> offset2Hi,
  @JsonProperty("OFFSET_2_LO") List<BigInteger> offset2Lo,
  @JsonProperty("QUAD_COST") List<BigInteger> quadCost,
  @JsonProperty("ROOB") List<Boolean> roob,
  @JsonProperty("SIZE_1_HI") List<BigInteger> size1Hi,
  @JsonProperty("SIZE_1_LO") List<BigInteger> size1Lo,
  @JsonProperty("SIZE_2_HI") List<BigInteger> size2Hi,
  @JsonProperty("SIZE_2_LO") List<BigInteger> size2Lo,
  @JsonProperty("STAMP") List<BigInteger> stamp,
  @JsonProperty("WORDS") List<BigInteger> words,
  @JsonProperty("WORDS_NEW") List<BigInteger> wordsNew) { 
  static TraceBuilder builder(int length) {
    return new TraceBuilder(length);
  }

  public int size() {
      return this.acc1.size();
  }

  public static List<ColumnHeader> headers(int size) {
    return List.of(
      new ColumnHeader("mxp.ACC_1", 32, size),
      new ColumnHeader("mxp.ACC_2", 32, size),
      new ColumnHeader("mxp.ACC_3", 32, size),
      new ColumnHeader("mxp.ACC_4", 32, size),
      new ColumnHeader("mxp.ACC_A", 32, size),
      new ColumnHeader("mxp.ACC_Q", 32, size),
      new ColumnHeader("mxp.ACC_W", 32, size),
      new ColumnHeader("mxp.BYTE_1", 1, size),
      new ColumnHeader("mxp.BYTE_2", 1, size),
      new ColumnHeader("mxp.BYTE_3", 1, size),
      new ColumnHeader("mxp.BYTE_4", 1, size),
      new ColumnHeader("mxp.BYTE_A", 1, size),
      new ColumnHeader("mxp.BYTE_Q", 1, size),
      new ColumnHeader("mxp.BYTE_QQ", 32, size),
      new ColumnHeader("mxp.BYTE_R", 32, size),
      new ColumnHeader("mxp.BYTE_W", 1, size),
      new ColumnHeader("mxp.C_MEM", 32, size),
      new ColumnHeader("mxp.C_MEM_NEW", 32, size),
      new ColumnHeader("mxp.CN", 32, size),
      new ColumnHeader("mxp.COMP", 1, size),
      new ColumnHeader("mxp.CT", 32, size),
      new ColumnHeader("mxp.DEPLOYS", 1, size),
      new ColumnHeader("mxp.EXPANDS", 1, size),
      new ColumnHeader("mxp.GAS_MXP", 32, size),
      new ColumnHeader("mxp.GBYTE", 32, size),
      new ColumnHeader("mxp.GWORD", 32, size),
      new ColumnHeader("mxp.INST", 32, size),
      new ColumnHeader("mxp.LIN_COST", 32, size),
      new ColumnHeader("mxp.MAX_OFFSET", 32, size),
      new ColumnHeader("mxp.MAX_OFFSET_1", 32, size),
      new ColumnHeader("mxp.MAX_OFFSET_2", 32, size),
      new ColumnHeader("mxp.MXP_TYPE_1", 1, size),
      new ColumnHeader("mxp.MXP_TYPE_2", 1, size),
      new ColumnHeader("mxp.MXP_TYPE_3", 1, size),
      new ColumnHeader("mxp.MXP_TYPE_4", 1, size),
      new ColumnHeader("mxp.MXP_TYPE_5", 1, size),
      new ColumnHeader("mxp.MXPX", 1, size),
      new ColumnHeader("mxp.NOOP", 1, size),
      new ColumnHeader("mxp.OFFSET_1_HI", 32, size),
      new ColumnHeader("mxp.OFFSET_1_LO", 32, size),
      new ColumnHeader("mxp.OFFSET_2_HI", 32, size),
      new ColumnHeader("mxp.OFFSET_2_LO", 32, size),
      new ColumnHeader("mxp.QUAD_COST", 32, size),
      new ColumnHeader("mxp.ROOB", 1, size),
      new ColumnHeader("mxp.SIZE_1_HI", 32, size),
      new ColumnHeader("mxp.SIZE_1_LO", 32, size),
      new ColumnHeader("mxp.SIZE_2_HI", 32, size),
      new ColumnHeader("mxp.SIZE_2_LO", 32, size),
      new ColumnHeader("mxp.STAMP", 32, size),
      new ColumnHeader("mxp.WORDS", 32, size),
      new ColumnHeader("mxp.WORDS_NEW", 32, size)
      );
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


    private void writeacc1(final BigInteger b) {
      this.target.put(0 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeacc2(final BigInteger b) {
      this.target.put(32 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeacc3(final BigInteger b) {
      this.target.put(64 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeacc4(final BigInteger b) {
      this.target.put(96 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeaccA(final BigInteger b) {
      this.target.put(128 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeaccQ(final BigInteger b) {
      this.target.put(160 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeaccW(final BigInteger b) {
      this.target.put(192 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writebyte1(final UnsignedByte b) {
      this.target.put(224 * this.length + this.currentLength*1, b.toByte());
    }
    private void writebyte2(final UnsignedByte b) {
      this.target.put(225 * this.length + this.currentLength*1, b.toByte());
    }
    private void writebyte3(final UnsignedByte b) {
      this.target.put(226 * this.length + this.currentLength*1, b.toByte());
    }
    private void writebyte4(final UnsignedByte b) {
      this.target.put(227 * this.length + this.currentLength*1, b.toByte());
    }
    private void writebyteA(final UnsignedByte b) {
      this.target.put(228 * this.length + this.currentLength*1, b.toByte());
    }
    private void writebyteQ(final UnsignedByte b) {
      this.target.put(229 * this.length + this.currentLength*1, b.toByte());
    }
    private void writebyteQq(final BigInteger b) {
      this.target.put(230 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writebyteR(final BigInteger b) {
      this.target.put(262 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writebyteW(final UnsignedByte b) {
      this.target.put(294 * this.length + this.currentLength*1, b.toByte());
    }
    private void writecMem(final BigInteger b) {
      this.target.put(360 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writecMemNew(final BigInteger b) {
      this.target.put(392 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writecn(final BigInteger b) {
      this.target.put(295 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writecomp(final Boolean b) {
      this.target.put(327 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writect(final BigInteger b) {
      this.target.put(328 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writedeploys(final Boolean b) {
      this.target.put(424 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writeexpands(final Boolean b) {
      this.target.put(425 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writegasMxp(final BigInteger b) {
      this.target.put(426 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writegbyte(final BigInteger b) {
      this.target.put(458 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writegword(final BigInteger b) {
      this.target.put(490 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeinst(final BigInteger b) {
      this.target.put(522 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writelinCost(final BigInteger b) {
      this.target.put(554 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writemaxOffset(final BigInteger b) {
      this.target.put(586 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writemaxOffset1(final BigInteger b) {
      this.target.put(618 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writemaxOffset2(final BigInteger b) {
      this.target.put(650 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writemxpType1(final Boolean b) {
      this.target.put(683 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writemxpType2(final Boolean b) {
      this.target.put(684 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writemxpType3(final Boolean b) {
      this.target.put(685 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writemxpType4(final Boolean b) {
      this.target.put(686 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writemxpType5(final Boolean b) {
      this.target.put(687 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writemxpx(final Boolean b) {
      this.target.put(682 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writenoop(final Boolean b) {
      this.target.put(688 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writeoffset1Hi(final BigInteger b) {
      this.target.put(689 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeoffset1Lo(final BigInteger b) {
      this.target.put(721 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeoffset2Hi(final BigInteger b) {
      this.target.put(753 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeoffset2Lo(final BigInteger b) {
      this.target.put(785 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writequadCost(final BigInteger b) {
      this.target.put(817 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeroob(final Boolean b) {
      this.target.put(849 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writesize1Hi(final BigInteger b) {
      this.target.put(850 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writesize1Lo(final BigInteger b) {
      this.target.put(882 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writesize2Hi(final BigInteger b) {
      this.target.put(914 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writesize2Lo(final BigInteger b) {
      this.target.put(946 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writestamp(final BigInteger b) {
      this.target.put(978 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writewords(final BigInteger b) {
      this.target.put(1010 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writewordsNew(final BigInteger b) {
      this.target.put(1042 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }


    public BufferTraceWriter acc1(final BigInteger b) {
      if (filled.get(0)) {
        throw new IllegalStateException("ACC_1 already set");
      } else {
        filled.set(0);
      }

      this.writeacc1(b);

      return this;
    }
    public BufferTraceWriter acc2(final BigInteger b) {
      if (filled.get(1)) {
        throw new IllegalStateException("ACC_2 already set");
      } else {
        filled.set(1);
      }

      this.writeacc2(b);

      return this;
    }
    public BufferTraceWriter acc3(final BigInteger b) {
      if (filled.get(2)) {
        throw new IllegalStateException("ACC_3 already set");
      } else {
        filled.set(2);
      }

      this.writeacc3(b);

      return this;
    }
    public BufferTraceWriter acc4(final BigInteger b) {
      if (filled.get(3)) {
        throw new IllegalStateException("ACC_4 already set");
      } else {
        filled.set(3);
      }

      this.writeacc4(b);

      return this;
    }
    public BufferTraceWriter accA(final BigInteger b) {
      if (filled.get(4)) {
        throw new IllegalStateException("ACC_A already set");
      } else {
        filled.set(4);
      }

      this.writeaccA(b);

      return this;
    }
    public BufferTraceWriter accQ(final BigInteger b) {
      if (filled.get(5)) {
        throw new IllegalStateException("ACC_Q already set");
      } else {
        filled.set(5);
      }

      this.writeaccQ(b);

      return this;
    }
    public BufferTraceWriter accW(final BigInteger b) {
      if (filled.get(6)) {
        throw new IllegalStateException("ACC_W already set");
      } else {
        filled.set(6);
      }

      this.writeaccW(b);

      return this;
    }
    public BufferTraceWriter byte1(final UnsignedByte b) {
      if (filled.get(7)) {
        throw new IllegalStateException("BYTE_1 already set");
      } else {
        filled.set(7);
      }

      this.writebyte1(b);

      return this;
    }
    public BufferTraceWriter byte2(final UnsignedByte b) {
      if (filled.get(8)) {
        throw new IllegalStateException("BYTE_2 already set");
      } else {
        filled.set(8);
      }

      this.writebyte2(b);

      return this;
    }
    public BufferTraceWriter byte3(final UnsignedByte b) {
      if (filled.get(9)) {
        throw new IllegalStateException("BYTE_3 already set");
      } else {
        filled.set(9);
      }

      this.writebyte3(b);

      return this;
    }
    public BufferTraceWriter byte4(final UnsignedByte b) {
      if (filled.get(10)) {
        throw new IllegalStateException("BYTE_4 already set");
      } else {
        filled.set(10);
      }

      this.writebyte4(b);

      return this;
    }
    public BufferTraceWriter byteA(final UnsignedByte b) {
      if (filled.get(11)) {
        throw new IllegalStateException("BYTE_A already set");
      } else {
        filled.set(11);
      }

      this.writebyteA(b);

      return this;
    }
    public BufferTraceWriter byteQ(final UnsignedByte b) {
      if (filled.get(12)) {
        throw new IllegalStateException("BYTE_Q already set");
      } else {
        filled.set(12);
      }

      this.writebyteQ(b);

      return this;
    }
    public BufferTraceWriter byteQq(final BigInteger b) {
      if (filled.get(13)) {
        throw new IllegalStateException("BYTE_QQ already set");
      } else {
        filled.set(13);
      }

      this.writebyteQq(b);

      return this;
    }
    public BufferTraceWriter byteR(final BigInteger b) {
      if (filled.get(14)) {
        throw new IllegalStateException("BYTE_R already set");
      } else {
        filled.set(14);
      }

      this.writebyteR(b);

      return this;
    }
    public BufferTraceWriter byteW(final UnsignedByte b) {
      if (filled.get(15)) {
        throw new IllegalStateException("BYTE_W already set");
      } else {
        filled.set(15);
      }

      this.writebyteW(b);

      return this;
    }
    public BufferTraceWriter cMem(final BigInteger b) {
      if (filled.get(19)) {
        throw new IllegalStateException("C_MEM already set");
      } else {
        filled.set(19);
      }

      this.writecMem(b);

      return this;
    }
    public BufferTraceWriter cMemNew(final BigInteger b) {
      if (filled.get(20)) {
        throw new IllegalStateException("C_MEM_NEW already set");
      } else {
        filled.set(20);
      }

      this.writecMemNew(b);

      return this;
    }
    public BufferTraceWriter cn(final BigInteger b) {
      if (filled.get(16)) {
        throw new IllegalStateException("CN already set");
      } else {
        filled.set(16);
      }

      this.writecn(b);

      return this;
    }
    public BufferTraceWriter comp(final Boolean b) {
      if (filled.get(17)) {
        throw new IllegalStateException("COMP already set");
      } else {
        filled.set(17);
      }

      this.writecomp(b);

      return this;
    }
    public BufferTraceWriter ct(final BigInteger b) {
      if (filled.get(18)) {
        throw new IllegalStateException("CT already set");
      } else {
        filled.set(18);
      }

      this.writect(b);

      return this;
    }
    public BufferTraceWriter deploys(final Boolean b) {
      if (filled.get(21)) {
        throw new IllegalStateException("DEPLOYS already set");
      } else {
        filled.set(21);
      }

      this.writedeploys(b);

      return this;
    }
    public BufferTraceWriter expands(final Boolean b) {
      if (filled.get(22)) {
        throw new IllegalStateException("EXPANDS already set");
      } else {
        filled.set(22);
      }

      this.writeexpands(b);

      return this;
    }
    public BufferTraceWriter gasMxp(final BigInteger b) {
      if (filled.get(23)) {
        throw new IllegalStateException("GAS_MXP already set");
      } else {
        filled.set(23);
      }

      this.writegasMxp(b);

      return this;
    }
    public BufferTraceWriter gbyte(final BigInteger b) {
      if (filled.get(24)) {
        throw new IllegalStateException("GBYTE already set");
      } else {
        filled.set(24);
      }

      this.writegbyte(b);

      return this;
    }
    public BufferTraceWriter gword(final BigInteger b) {
      if (filled.get(25)) {
        throw new IllegalStateException("GWORD already set");
      } else {
        filled.set(25);
      }

      this.writegword(b);

      return this;
    }
    public BufferTraceWriter inst(final BigInteger b) {
      if (filled.get(26)) {
        throw new IllegalStateException("INST already set");
      } else {
        filled.set(26);
      }

      this.writeinst(b);

      return this;
    }
    public BufferTraceWriter linCost(final BigInteger b) {
      if (filled.get(27)) {
        throw new IllegalStateException("LIN_COST already set");
      } else {
        filled.set(27);
      }

      this.writelinCost(b);

      return this;
    }
    public BufferTraceWriter maxOffset(final BigInteger b) {
      if (filled.get(28)) {
        throw new IllegalStateException("MAX_OFFSET already set");
      } else {
        filled.set(28);
      }

      this.writemaxOffset(b);

      return this;
    }
    public BufferTraceWriter maxOffset1(final BigInteger b) {
      if (filled.get(29)) {
        throw new IllegalStateException("MAX_OFFSET_1 already set");
      } else {
        filled.set(29);
      }

      this.writemaxOffset1(b);

      return this;
    }
    public BufferTraceWriter maxOffset2(final BigInteger b) {
      if (filled.get(30)) {
        throw new IllegalStateException("MAX_OFFSET_2 already set");
      } else {
        filled.set(30);
      }

      this.writemaxOffset2(b);

      return this;
    }
    public BufferTraceWriter mxpType1(final Boolean b) {
      if (filled.get(32)) {
        throw new IllegalStateException("MXP_TYPE_1 already set");
      } else {
        filled.set(32);
      }

      this.writemxpType1(b);

      return this;
    }
    public BufferTraceWriter mxpType2(final Boolean b) {
      if (filled.get(33)) {
        throw new IllegalStateException("MXP_TYPE_2 already set");
      } else {
        filled.set(33);
      }

      this.writemxpType2(b);

      return this;
    }
    public BufferTraceWriter mxpType3(final Boolean b) {
      if (filled.get(34)) {
        throw new IllegalStateException("MXP_TYPE_3 already set");
      } else {
        filled.set(34);
      }

      this.writemxpType3(b);

      return this;
    }
    public BufferTraceWriter mxpType4(final Boolean b) {
      if (filled.get(35)) {
        throw new IllegalStateException("MXP_TYPE_4 already set");
      } else {
        filled.set(35);
      }

      this.writemxpType4(b);

      return this;
    }
    public BufferTraceWriter mxpType5(final Boolean b) {
      if (filled.get(36)) {
        throw new IllegalStateException("MXP_TYPE_5 already set");
      } else {
        filled.set(36);
      }

      this.writemxpType5(b);

      return this;
    }
    public BufferTraceWriter mxpx(final Boolean b) {
      if (filled.get(31)) {
        throw new IllegalStateException("MXPX already set");
      } else {
        filled.set(31);
      }

      this.writemxpx(b);

      return this;
    }
    public BufferTraceWriter noop(final Boolean b) {
      if (filled.get(37)) {
        throw new IllegalStateException("NOOP already set");
      } else {
        filled.set(37);
      }

      this.writenoop(b);

      return this;
    }
    public BufferTraceWriter offset1Hi(final BigInteger b) {
      if (filled.get(38)) {
        throw new IllegalStateException("OFFSET_1_HI already set");
      } else {
        filled.set(38);
      }

      this.writeoffset1Hi(b);

      return this;
    }
    public BufferTraceWriter offset1Lo(final BigInteger b) {
      if (filled.get(39)) {
        throw new IllegalStateException("OFFSET_1_LO already set");
      } else {
        filled.set(39);
      }

      this.writeoffset1Lo(b);

      return this;
    }
    public BufferTraceWriter offset2Hi(final BigInteger b) {
      if (filled.get(40)) {
        throw new IllegalStateException("OFFSET_2_HI already set");
      } else {
        filled.set(40);
      }

      this.writeoffset2Hi(b);

      return this;
    }
    public BufferTraceWriter offset2Lo(final BigInteger b) {
      if (filled.get(41)) {
        throw new IllegalStateException("OFFSET_2_LO already set");
      } else {
        filled.set(41);
      }

      this.writeoffset2Lo(b);

      return this;
    }
    public BufferTraceWriter quadCost(final BigInteger b) {
      if (filled.get(42)) {
        throw new IllegalStateException("QUAD_COST already set");
      } else {
        filled.set(42);
      }

      this.writequadCost(b);

      return this;
    }
    public BufferTraceWriter roob(final Boolean b) {
      if (filled.get(43)) {
        throw new IllegalStateException("ROOB already set");
      } else {
        filled.set(43);
      }

      this.writeroob(b);

      return this;
    }
    public BufferTraceWriter size1Hi(final BigInteger b) {
      if (filled.get(44)) {
        throw new IllegalStateException("SIZE_1_HI already set");
      } else {
        filled.set(44);
      }

      this.writesize1Hi(b);

      return this;
    }
    public BufferTraceWriter size1Lo(final BigInteger b) {
      if (filled.get(45)) {
        throw new IllegalStateException("SIZE_1_LO already set");
      } else {
        filled.set(45);
      }

      this.writesize1Lo(b);

      return this;
    }
    public BufferTraceWriter size2Hi(final BigInteger b) {
      if (filled.get(46)) {
        throw new IllegalStateException("SIZE_2_HI already set");
      } else {
        filled.set(46);
      }

      this.writesize2Hi(b);

      return this;
    }
    public BufferTraceWriter size2Lo(final BigInteger b) {
      if (filled.get(47)) {
        throw new IllegalStateException("SIZE_2_LO already set");
      } else {
        filled.set(47);
      }

      this.writesize2Lo(b);

      return this;
    }
    public BufferTraceWriter stamp(final BigInteger b) {
      if (filled.get(48)) {
        throw new IllegalStateException("STAMP already set");
      } else {
        filled.set(48);
      }

      this.writestamp(b);

      return this;
    }
    public BufferTraceWriter words(final BigInteger b) {
      if (filled.get(49)) {
        throw new IllegalStateException("WORDS already set");
      } else {
        filled.set(49);
      }

      this.writewords(b);

      return this;
    }
    public BufferTraceWriter wordsNew(final BigInteger b) {
      if (filled.get(50)) {
        throw new IllegalStateException("WORDS_NEW already set");
      } else {
        filled.set(50);
      }

      this.writewordsNew(b);

      return this;
    }

    public BufferTraceWriter validateRow() {
      if (!filled.get(0)) {
        throw new IllegalStateException("ACC_1 has not been filled");
      }

      if (!filled.get(1)) {
        throw new IllegalStateException("ACC_2 has not been filled");
      }

      if (!filled.get(2)) {
        throw new IllegalStateException("ACC_3 has not been filled");
      }

      if (!filled.get(3)) {
        throw new IllegalStateException("ACC_4 has not been filled");
      }

      if (!filled.get(4)) {
        throw new IllegalStateException("ACC_A has not been filled");
      }

      if (!filled.get(5)) {
        throw new IllegalStateException("ACC_Q has not been filled");
      }

      if (!filled.get(6)) {
        throw new IllegalStateException("ACC_W has not been filled");
      }

      if (!filled.get(7)) {
        throw new IllegalStateException("BYTE_1 has not been filled");
      }

      if (!filled.get(8)) {
        throw new IllegalStateException("BYTE_2 has not been filled");
      }

      if (!filled.get(9)) {
        throw new IllegalStateException("BYTE_3 has not been filled");
      }

      if (!filled.get(10)) {
        throw new IllegalStateException("BYTE_4 has not been filled");
      }

      if (!filled.get(11)) {
        throw new IllegalStateException("BYTE_A has not been filled");
      }

      if (!filled.get(12)) {
        throw new IllegalStateException("BYTE_Q has not been filled");
      }

      if (!filled.get(13)) {
        throw new IllegalStateException("BYTE_QQ has not been filled");
      }

      if (!filled.get(14)) {
        throw new IllegalStateException("BYTE_R has not been filled");
      }

      if (!filled.get(15)) {
        throw new IllegalStateException("BYTE_W has not been filled");
      }

      if (!filled.get(19)) {
        throw new IllegalStateException("C_MEM has not been filled");
      }

      if (!filled.get(20)) {
        throw new IllegalStateException("C_MEM_NEW has not been filled");
      }

      if (!filled.get(16)) {
        throw new IllegalStateException("CN has not been filled");
      }

      if (!filled.get(17)) {
        throw new IllegalStateException("COMP has not been filled");
      }

      if (!filled.get(18)) {
        throw new IllegalStateException("CT has not been filled");
      }

      if (!filled.get(21)) {
        throw new IllegalStateException("DEPLOYS has not been filled");
      }

      if (!filled.get(22)) {
        throw new IllegalStateException("EXPANDS has not been filled");
      }

      if (!filled.get(23)) {
        throw new IllegalStateException("GAS_MXP has not been filled");
      }

      if (!filled.get(24)) {
        throw new IllegalStateException("GBYTE has not been filled");
      }

      if (!filled.get(25)) {
        throw new IllegalStateException("GWORD has not been filled");
      }

      if (!filled.get(26)) {
        throw new IllegalStateException("INST has not been filled");
      }

      if (!filled.get(27)) {
        throw new IllegalStateException("LIN_COST has not been filled");
      }

      if (!filled.get(28)) {
        throw new IllegalStateException("MAX_OFFSET has not been filled");
      }

      if (!filled.get(29)) {
        throw new IllegalStateException("MAX_OFFSET_1 has not been filled");
      }

      if (!filled.get(30)) {
        throw new IllegalStateException("MAX_OFFSET_2 has not been filled");
      }

      if (!filled.get(32)) {
        throw new IllegalStateException("MXP_TYPE_1 has not been filled");
      }

      if (!filled.get(33)) {
        throw new IllegalStateException("MXP_TYPE_2 has not been filled");
      }

      if (!filled.get(34)) {
        throw new IllegalStateException("MXP_TYPE_3 has not been filled");
      }

      if (!filled.get(35)) {
        throw new IllegalStateException("MXP_TYPE_4 has not been filled");
      }

      if (!filled.get(36)) {
        throw new IllegalStateException("MXP_TYPE_5 has not been filled");
      }

      if (!filled.get(31)) {
        throw new IllegalStateException("MXPX has not been filled");
      }

      if (!filled.get(37)) {
        throw new IllegalStateException("NOOP has not been filled");
      }

      if (!filled.get(38)) {
        throw new IllegalStateException("OFFSET_1_HI has not been filled");
      }

      if (!filled.get(39)) {
        throw new IllegalStateException("OFFSET_1_LO has not been filled");
      }

      if (!filled.get(40)) {
        throw new IllegalStateException("OFFSET_2_HI has not been filled");
      }

      if (!filled.get(41)) {
        throw new IllegalStateException("OFFSET_2_LO has not been filled");
      }

      if (!filled.get(42)) {
        throw new IllegalStateException("QUAD_COST has not been filled");
      }

      if (!filled.get(43)) {
        throw new IllegalStateException("ROOB has not been filled");
      }

      if (!filled.get(44)) {
        throw new IllegalStateException("SIZE_1_HI has not been filled");
      }

      if (!filled.get(45)) {
        throw new IllegalStateException("SIZE_1_LO has not been filled");
      }

      if (!filled.get(46)) {
        throw new IllegalStateException("SIZE_2_HI has not been filled");
      }

      if (!filled.get(47)) {
        throw new IllegalStateException("SIZE_2_LO has not been filled");
      }

      if (!filled.get(48)) {
        throw new IllegalStateException("STAMP has not been filled");
      }

      if (!filled.get(49)) {
        throw new IllegalStateException("WORDS has not been filled");
      }

      if (!filled.get(50)) {
        throw new IllegalStateException("WORDS_NEW has not been filled");
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

    @JsonProperty("ACC_1")
    private final List<BigInteger> acc1;
    @JsonProperty("ACC_2")
    private final List<BigInteger> acc2;
    @JsonProperty("ACC_3")
    private final List<BigInteger> acc3;
    @JsonProperty("ACC_4")
    private final List<BigInteger> acc4;
    @JsonProperty("ACC_A")
    private final List<BigInteger> accA;
    @JsonProperty("ACC_Q")
    private final List<BigInteger> accQ;
    @JsonProperty("ACC_W")
    private final List<BigInteger> accW;
    @JsonProperty("BYTE_1")
    private final List<UnsignedByte> byte1;
    @JsonProperty("BYTE_2")
    private final List<UnsignedByte> byte2;
    @JsonProperty("BYTE_3")
    private final List<UnsignedByte> byte3;
    @JsonProperty("BYTE_4")
    private final List<UnsignedByte> byte4;
    @JsonProperty("BYTE_A")
    private final List<UnsignedByte> byteA;
    @JsonProperty("BYTE_Q")
    private final List<UnsignedByte> byteQ;
    @JsonProperty("BYTE_QQ")
    private final List<BigInteger> byteQq;
    @JsonProperty("BYTE_R")
    private final List<BigInteger> byteR;
    @JsonProperty("BYTE_W")
    private final List<UnsignedByte> byteW;
    @JsonProperty("C_MEM")
    private final List<BigInteger> cMem;
    @JsonProperty("C_MEM_NEW")
    private final List<BigInteger> cMemNew;
    @JsonProperty("CN")
    private final List<BigInteger> cn;
    @JsonProperty("COMP")
    private final List<Boolean> comp;
    @JsonProperty("CT")
    private final List<BigInteger> ct;
    @JsonProperty("DEPLOYS")
    private final List<Boolean> deploys;
    @JsonProperty("EXPANDS")
    private final List<Boolean> expands;
    @JsonProperty("GAS_MXP")
    private final List<BigInteger> gasMxp;
    @JsonProperty("GBYTE")
    private final List<BigInteger> gbyte;
    @JsonProperty("GWORD")
    private final List<BigInteger> gword;
    @JsonProperty("INST")
    private final List<BigInteger> inst;
    @JsonProperty("LIN_COST")
    private final List<BigInteger> linCost;
    @JsonProperty("MAX_OFFSET")
    private final List<BigInteger> maxOffset;
    @JsonProperty("MAX_OFFSET_1")
    private final List<BigInteger> maxOffset1;
    @JsonProperty("MAX_OFFSET_2")
    private final List<BigInteger> maxOffset2;
    @JsonProperty("MXP_TYPE_1")
    private final List<Boolean> mxpType1;
    @JsonProperty("MXP_TYPE_2")
    private final List<Boolean> mxpType2;
    @JsonProperty("MXP_TYPE_3")
    private final List<Boolean> mxpType3;
    @JsonProperty("MXP_TYPE_4")
    private final List<Boolean> mxpType4;
    @JsonProperty("MXP_TYPE_5")
    private final List<Boolean> mxpType5;
    @JsonProperty("MXPX")
    private final List<Boolean> mxpx;
    @JsonProperty("NOOP")
    private final List<Boolean> noop;
    @JsonProperty("OFFSET_1_HI")
    private final List<BigInteger> offset1Hi;
    @JsonProperty("OFFSET_1_LO")
    private final List<BigInteger> offset1Lo;
    @JsonProperty("OFFSET_2_HI")
    private final List<BigInteger> offset2Hi;
    @JsonProperty("OFFSET_2_LO")
    private final List<BigInteger> offset2Lo;
    @JsonProperty("QUAD_COST")
    private final List<BigInteger> quadCost;
    @JsonProperty("ROOB")
    private final List<Boolean> roob;
    @JsonProperty("SIZE_1_HI")
    private final List<BigInteger> size1Hi;
    @JsonProperty("SIZE_1_LO")
    private final List<BigInteger> size1Lo;
    @JsonProperty("SIZE_2_HI")
    private final List<BigInteger> size2Hi;
    @JsonProperty("SIZE_2_LO")
    private final List<BigInteger> size2Lo;
    @JsonProperty("STAMP")
    private final List<BigInteger> stamp;
    @JsonProperty("WORDS")
    private final List<BigInteger> words;
    @JsonProperty("WORDS_NEW")
    private final List<BigInteger> wordsNew;

    TraceBuilder(int length) {
      this.acc1 = new ArrayList<>(length);
      this.acc2 = new ArrayList<>(length);
      this.acc3 = new ArrayList<>(length);
      this.acc4 = new ArrayList<>(length);
      this.accA = new ArrayList<>(length);
      this.accQ = new ArrayList<>(length);
      this.accW = new ArrayList<>(length);
      this.byte1 = new ArrayList<>(length);
      this.byte2 = new ArrayList<>(length);
      this.byte3 = new ArrayList<>(length);
      this.byte4 = new ArrayList<>(length);
      this.byteA = new ArrayList<>(length);
      this.byteQ = new ArrayList<>(length);
      this.byteQq = new ArrayList<>(length);
      this.byteR = new ArrayList<>(length);
      this.byteW = new ArrayList<>(length);
      this.cMem = new ArrayList<>(length);
      this.cMemNew = new ArrayList<>(length);
      this.cn = new ArrayList<>(length);
      this.comp = new ArrayList<>(length);
      this.ct = new ArrayList<>(length);
      this.deploys = new ArrayList<>(length);
      this.expands = new ArrayList<>(length);
      this.gasMxp = new ArrayList<>(length);
      this.gbyte = new ArrayList<>(length);
      this.gword = new ArrayList<>(length);
      this.inst = new ArrayList<>(length);
      this.linCost = new ArrayList<>(length);
      this.maxOffset = new ArrayList<>(length);
      this.maxOffset1 = new ArrayList<>(length);
      this.maxOffset2 = new ArrayList<>(length);
      this.mxpType1 = new ArrayList<>(length);
      this.mxpType2 = new ArrayList<>(length);
      this.mxpType3 = new ArrayList<>(length);
      this.mxpType4 = new ArrayList<>(length);
      this.mxpType5 = new ArrayList<>(length);
      this.mxpx = new ArrayList<>(length);
      this.noop = new ArrayList<>(length);
      this.offset1Hi = new ArrayList<>(length);
      this.offset1Lo = new ArrayList<>(length);
      this.offset2Hi = new ArrayList<>(length);
      this.offset2Lo = new ArrayList<>(length);
      this.quadCost = new ArrayList<>(length);
      this.roob = new ArrayList<>(length);
      this.size1Hi = new ArrayList<>(length);
      this.size1Lo = new ArrayList<>(length);
      this.size2Hi = new ArrayList<>(length);
      this.size2Lo = new ArrayList<>(length);
      this.stamp = new ArrayList<>(length);
      this.words = new ArrayList<>(length);
      this.wordsNew = new ArrayList<>(length);
    }

    public int size() {
      if (!filled.isEmpty()) {
        throw new RuntimeException("Cannot measure a trace with a non-validated row.");
      }

      return this.acc1.size();
    }

    public TraceBuilder acc1(final BigInteger b) {
      if (filled.get(0)) {
        throw new IllegalStateException("ACC_1 already set");
      } else {
        filled.set(0);
      }

      acc1.add(b);

      return this;
    }

    public TraceBuilder acc2(final BigInteger b) {
      if (filled.get(1)) {
        throw new IllegalStateException("ACC_2 already set");
      } else {
        filled.set(1);
      }

      acc2.add(b);

      return this;
    }

    public TraceBuilder acc3(final BigInteger b) {
      if (filled.get(2)) {
        throw new IllegalStateException("ACC_3 already set");
      } else {
        filled.set(2);
      }

      acc3.add(b);

      return this;
    }

    public TraceBuilder acc4(final BigInteger b) {
      if (filled.get(3)) {
        throw new IllegalStateException("ACC_4 already set");
      } else {
        filled.set(3);
      }

      acc4.add(b);

      return this;
    }

    public TraceBuilder accA(final BigInteger b) {
      if (filled.get(4)) {
        throw new IllegalStateException("ACC_A already set");
      } else {
        filled.set(4);
      }

      accA.add(b);

      return this;
    }

    public TraceBuilder accQ(final BigInteger b) {
      if (filled.get(5)) {
        throw new IllegalStateException("ACC_Q already set");
      } else {
        filled.set(5);
      }

      accQ.add(b);

      return this;
    }

    public TraceBuilder accW(final BigInteger b) {
      if (filled.get(6)) {
        throw new IllegalStateException("ACC_W already set");
      } else {
        filled.set(6);
      }

      accW.add(b);

      return this;
    }

    public TraceBuilder byte1(final UnsignedByte b) {
      if (filled.get(7)) {
        throw new IllegalStateException("BYTE_1 already set");
      } else {
        filled.set(7);
      }

      byte1.add(b);

      return this;
    }

    public TraceBuilder byte2(final UnsignedByte b) {
      if (filled.get(8)) {
        throw new IllegalStateException("BYTE_2 already set");
      } else {
        filled.set(8);
      }

      byte2.add(b);

      return this;
    }

    public TraceBuilder byte3(final UnsignedByte b) {
      if (filled.get(9)) {
        throw new IllegalStateException("BYTE_3 already set");
      } else {
        filled.set(9);
      }

      byte3.add(b);

      return this;
    }

    public TraceBuilder byte4(final UnsignedByte b) {
      if (filled.get(10)) {
        throw new IllegalStateException("BYTE_4 already set");
      } else {
        filled.set(10);
      }

      byte4.add(b);

      return this;
    }

    public TraceBuilder byteA(final UnsignedByte b) {
      if (filled.get(11)) {
        throw new IllegalStateException("BYTE_A already set");
      } else {
        filled.set(11);
      }

      byteA.add(b);

      return this;
    }

    public TraceBuilder byteQ(final UnsignedByte b) {
      if (filled.get(12)) {
        throw new IllegalStateException("BYTE_Q already set");
      } else {
        filled.set(12);
      }

      byteQ.add(b);

      return this;
    }

    public TraceBuilder byteQq(final BigInteger b) {
      if (filled.get(13)) {
        throw new IllegalStateException("BYTE_QQ already set");
      } else {
        filled.set(13);
      }

      byteQq.add(b);

      return this;
    }

    public TraceBuilder byteR(final BigInteger b) {
      if (filled.get(14)) {
        throw new IllegalStateException("BYTE_R already set");
      } else {
        filled.set(14);
      }

      byteR.add(b);

      return this;
    }

    public TraceBuilder byteW(final UnsignedByte b) {
      if (filled.get(15)) {
        throw new IllegalStateException("BYTE_W already set");
      } else {
        filled.set(15);
      }

      byteW.add(b);

      return this;
    }

    public TraceBuilder cMem(final BigInteger b) {
      if (filled.get(19)) {
        throw new IllegalStateException("C_MEM already set");
      } else {
        filled.set(19);
      }

      cMem.add(b);

      return this;
    }

    public TraceBuilder cMemNew(final BigInteger b) {
      if (filled.get(20)) {
        throw new IllegalStateException("C_MEM_NEW already set");
      } else {
        filled.set(20);
      }

      cMemNew.add(b);

      return this;
    }

    public TraceBuilder cn(final BigInteger b) {
      if (filled.get(16)) {
        throw new IllegalStateException("CN already set");
      } else {
        filled.set(16);
      }

      cn.add(b);

      return this;
    }

    public TraceBuilder comp(final Boolean b) {
      if (filled.get(17)) {
        throw new IllegalStateException("COMP already set");
      } else {
        filled.set(17);
      }

      comp.add(b);

      return this;
    }

    public TraceBuilder ct(final BigInteger b) {
      if (filled.get(18)) {
        throw new IllegalStateException("CT already set");
      } else {
        filled.set(18);
      }

      ct.add(b);

      return this;
    }

    public TraceBuilder deploys(final Boolean b) {
      if (filled.get(21)) {
        throw new IllegalStateException("DEPLOYS already set");
      } else {
        filled.set(21);
      }

      deploys.add(b);

      return this;
    }

    public TraceBuilder expands(final Boolean b) {
      if (filled.get(22)) {
        throw new IllegalStateException("EXPANDS already set");
      } else {
        filled.set(22);
      }

      expands.add(b);

      return this;
    }

    public TraceBuilder gasMxp(final BigInteger b) {
      if (filled.get(23)) {
        throw new IllegalStateException("GAS_MXP already set");
      } else {
        filled.set(23);
      }

      gasMxp.add(b);

      return this;
    }

    public TraceBuilder gbyte(final BigInteger b) {
      if (filled.get(24)) {
        throw new IllegalStateException("GBYTE already set");
      } else {
        filled.set(24);
      }

      gbyte.add(b);

      return this;
    }

    public TraceBuilder gword(final BigInteger b) {
      if (filled.get(25)) {
        throw new IllegalStateException("GWORD already set");
      } else {
        filled.set(25);
      }

      gword.add(b);

      return this;
    }

    public TraceBuilder inst(final BigInteger b) {
      if (filled.get(26)) {
        throw new IllegalStateException("INST already set");
      } else {
        filled.set(26);
      }

      inst.add(b);

      return this;
    }

    public TraceBuilder linCost(final BigInteger b) {
      if (filled.get(27)) {
        throw new IllegalStateException("LIN_COST already set");
      } else {
        filled.set(27);
      }

      linCost.add(b);

      return this;
    }

    public TraceBuilder maxOffset(final BigInteger b) {
      if (filled.get(28)) {
        throw new IllegalStateException("MAX_OFFSET already set");
      } else {
        filled.set(28);
      }

      maxOffset.add(b);

      return this;
    }

    public TraceBuilder maxOffset1(final BigInteger b) {
      if (filled.get(29)) {
        throw new IllegalStateException("MAX_OFFSET_1 already set");
      } else {
        filled.set(29);
      }

      maxOffset1.add(b);

      return this;
    }

    public TraceBuilder maxOffset2(final BigInteger b) {
      if (filled.get(30)) {
        throw new IllegalStateException("MAX_OFFSET_2 already set");
      } else {
        filled.set(30);
      }

      maxOffset2.add(b);

      return this;
    }

    public TraceBuilder mxpType1(final Boolean b) {
      if (filled.get(32)) {
        throw new IllegalStateException("MXP_TYPE_1 already set");
      } else {
        filled.set(32);
      }

      mxpType1.add(b);

      return this;
    }

    public TraceBuilder mxpType2(final Boolean b) {
      if (filled.get(33)) {
        throw new IllegalStateException("MXP_TYPE_2 already set");
      } else {
        filled.set(33);
      }

      mxpType2.add(b);

      return this;
    }

    public TraceBuilder mxpType3(final Boolean b) {
      if (filled.get(34)) {
        throw new IllegalStateException("MXP_TYPE_3 already set");
      } else {
        filled.set(34);
      }

      mxpType3.add(b);

      return this;
    }

    public TraceBuilder mxpType4(final Boolean b) {
      if (filled.get(35)) {
        throw new IllegalStateException("MXP_TYPE_4 already set");
      } else {
        filled.set(35);
      }

      mxpType4.add(b);

      return this;
    }

    public TraceBuilder mxpType5(final Boolean b) {
      if (filled.get(36)) {
        throw new IllegalStateException("MXP_TYPE_5 already set");
      } else {
        filled.set(36);
      }

      mxpType5.add(b);

      return this;
    }

    public TraceBuilder mxpx(final Boolean b) {
      if (filled.get(31)) {
        throw new IllegalStateException("MXPX already set");
      } else {
        filled.set(31);
      }

      mxpx.add(b);

      return this;
    }

    public TraceBuilder noop(final Boolean b) {
      if (filled.get(37)) {
        throw new IllegalStateException("NOOP already set");
      } else {
        filled.set(37);
      }

      noop.add(b);

      return this;
    }

    public TraceBuilder offset1Hi(final BigInteger b) {
      if (filled.get(38)) {
        throw new IllegalStateException("OFFSET_1_HI already set");
      } else {
        filled.set(38);
      }

      offset1Hi.add(b);

      return this;
    }

    public TraceBuilder offset1Lo(final BigInteger b) {
      if (filled.get(39)) {
        throw new IllegalStateException("OFFSET_1_LO already set");
      } else {
        filled.set(39);
      }

      offset1Lo.add(b);

      return this;
    }

    public TraceBuilder offset2Hi(final BigInteger b) {
      if (filled.get(40)) {
        throw new IllegalStateException("OFFSET_2_HI already set");
      } else {
        filled.set(40);
      }

      offset2Hi.add(b);

      return this;
    }

    public TraceBuilder offset2Lo(final BigInteger b) {
      if (filled.get(41)) {
        throw new IllegalStateException("OFFSET_2_LO already set");
      } else {
        filled.set(41);
      }

      offset2Lo.add(b);

      return this;
    }

    public TraceBuilder quadCost(final BigInteger b) {
      if (filled.get(42)) {
        throw new IllegalStateException("QUAD_COST already set");
      } else {
        filled.set(42);
      }

      quadCost.add(b);

      return this;
    }

    public TraceBuilder roob(final Boolean b) {
      if (filled.get(43)) {
        throw new IllegalStateException("ROOB already set");
      } else {
        filled.set(43);
      }

      roob.add(b);

      return this;
    }

    public TraceBuilder size1Hi(final BigInteger b) {
      if (filled.get(44)) {
        throw new IllegalStateException("SIZE_1_HI already set");
      } else {
        filled.set(44);
      }

      size1Hi.add(b);

      return this;
    }

    public TraceBuilder size1Lo(final BigInteger b) {
      if (filled.get(45)) {
        throw new IllegalStateException("SIZE_1_LO already set");
      } else {
        filled.set(45);
      }

      size1Lo.add(b);

      return this;
    }

    public TraceBuilder size2Hi(final BigInteger b) {
      if (filled.get(46)) {
        throw new IllegalStateException("SIZE_2_HI already set");
      } else {
        filled.set(46);
      }

      size2Hi.add(b);

      return this;
    }

    public TraceBuilder size2Lo(final BigInteger b) {
      if (filled.get(47)) {
        throw new IllegalStateException("SIZE_2_LO already set");
      } else {
        filled.set(47);
      }

      size2Lo.add(b);

      return this;
    }

    public TraceBuilder stamp(final BigInteger b) {
      if (filled.get(48)) {
        throw new IllegalStateException("STAMP already set");
      } else {
        filled.set(48);
      }

      stamp.add(b);

      return this;
    }

    public TraceBuilder words(final BigInteger b) {
      if (filled.get(49)) {
        throw new IllegalStateException("WORDS already set");
      } else {
        filled.set(49);
      }

      words.add(b);

      return this;
    }

    public TraceBuilder wordsNew(final BigInteger b) {
      if (filled.get(50)) {
        throw new IllegalStateException("WORDS_NEW already set");
      } else {
        filled.set(50);
      }

      wordsNew.add(b);

      return this;
    }

    public TraceBuilder validateRow() {
      if (!filled.get(0)) {
        throw new IllegalStateException("ACC_1 has not been filled");
      }

      if (!filled.get(1)) {
        throw new IllegalStateException("ACC_2 has not been filled");
      }

      if (!filled.get(2)) {
        throw new IllegalStateException("ACC_3 has not been filled");
      }

      if (!filled.get(3)) {
        throw new IllegalStateException("ACC_4 has not been filled");
      }

      if (!filled.get(4)) {
        throw new IllegalStateException("ACC_A has not been filled");
      }

      if (!filled.get(5)) {
        throw new IllegalStateException("ACC_Q has not been filled");
      }

      if (!filled.get(6)) {
        throw new IllegalStateException("ACC_W has not been filled");
      }

      if (!filled.get(7)) {
        throw new IllegalStateException("BYTE_1 has not been filled");
      }

      if (!filled.get(8)) {
        throw new IllegalStateException("BYTE_2 has not been filled");
      }

      if (!filled.get(9)) {
        throw new IllegalStateException("BYTE_3 has not been filled");
      }

      if (!filled.get(10)) {
        throw new IllegalStateException("BYTE_4 has not been filled");
      }

      if (!filled.get(11)) {
        throw new IllegalStateException("BYTE_A has not been filled");
      }

      if (!filled.get(12)) {
        throw new IllegalStateException("BYTE_Q has not been filled");
      }

      if (!filled.get(13)) {
        throw new IllegalStateException("BYTE_QQ has not been filled");
      }

      if (!filled.get(14)) {
        throw new IllegalStateException("BYTE_R has not been filled");
      }

      if (!filled.get(15)) {
        throw new IllegalStateException("BYTE_W has not been filled");
      }

      if (!filled.get(19)) {
        throw new IllegalStateException("C_MEM has not been filled");
      }

      if (!filled.get(20)) {
        throw new IllegalStateException("C_MEM_NEW has not been filled");
      }

      if (!filled.get(16)) {
        throw new IllegalStateException("CN has not been filled");
      }

      if (!filled.get(17)) {
        throw new IllegalStateException("COMP has not been filled");
      }

      if (!filled.get(18)) {
        throw new IllegalStateException("CT has not been filled");
      }

      if (!filled.get(21)) {
        throw new IllegalStateException("DEPLOYS has not been filled");
      }

      if (!filled.get(22)) {
        throw new IllegalStateException("EXPANDS has not been filled");
      }

      if (!filled.get(23)) {
        throw new IllegalStateException("GAS_MXP has not been filled");
      }

      if (!filled.get(24)) {
        throw new IllegalStateException("GBYTE has not been filled");
      }

      if (!filled.get(25)) {
        throw new IllegalStateException("GWORD has not been filled");
      }

      if (!filled.get(26)) {
        throw new IllegalStateException("INST has not been filled");
      }

      if (!filled.get(27)) {
        throw new IllegalStateException("LIN_COST has not been filled");
      }

      if (!filled.get(28)) {
        throw new IllegalStateException("MAX_OFFSET has not been filled");
      }

      if (!filled.get(29)) {
        throw new IllegalStateException("MAX_OFFSET_1 has not been filled");
      }

      if (!filled.get(30)) {
        throw new IllegalStateException("MAX_OFFSET_2 has not been filled");
      }

      if (!filled.get(32)) {
        throw new IllegalStateException("MXP_TYPE_1 has not been filled");
      }

      if (!filled.get(33)) {
        throw new IllegalStateException("MXP_TYPE_2 has not been filled");
      }

      if (!filled.get(34)) {
        throw new IllegalStateException("MXP_TYPE_3 has not been filled");
      }

      if (!filled.get(35)) {
        throw new IllegalStateException("MXP_TYPE_4 has not been filled");
      }

      if (!filled.get(36)) {
        throw new IllegalStateException("MXP_TYPE_5 has not been filled");
      }

      if (!filled.get(31)) {
        throw new IllegalStateException("MXPX has not been filled");
      }

      if (!filled.get(37)) {
        throw new IllegalStateException("NOOP has not been filled");
      }

      if (!filled.get(38)) {
        throw new IllegalStateException("OFFSET_1_HI has not been filled");
      }

      if (!filled.get(39)) {
        throw new IllegalStateException("OFFSET_1_LO has not been filled");
      }

      if (!filled.get(40)) {
        throw new IllegalStateException("OFFSET_2_HI has not been filled");
      }

      if (!filled.get(41)) {
        throw new IllegalStateException("OFFSET_2_LO has not been filled");
      }

      if (!filled.get(42)) {
        throw new IllegalStateException("QUAD_COST has not been filled");
      }

      if (!filled.get(43)) {
        throw new IllegalStateException("ROOB has not been filled");
      }

      if (!filled.get(44)) {
        throw new IllegalStateException("SIZE_1_HI has not been filled");
      }

      if (!filled.get(45)) {
        throw new IllegalStateException("SIZE_1_LO has not been filled");
      }

      if (!filled.get(46)) {
        throw new IllegalStateException("SIZE_2_HI has not been filled");
      }

      if (!filled.get(47)) {
        throw new IllegalStateException("SIZE_2_LO has not been filled");
      }

      if (!filled.get(48)) {
        throw new IllegalStateException("STAMP has not been filled");
      }

      if (!filled.get(49)) {
        throw new IllegalStateException("WORDS has not been filled");
      }

      if (!filled.get(50)) {
        throw new IllegalStateException("WORDS_NEW has not been filled");
      }


      filled.clear();

      return this;
    }

    public TraceBuilder fillAndValidateRow() {
      if (!filled.get(0)) {
          acc1.add(BigInteger.ZERO);
          this.filled.set(0);
      }
      if (!filled.get(1)) {
          acc2.add(BigInteger.ZERO);
          this.filled.set(1);
      }
      if (!filled.get(2)) {
          acc3.add(BigInteger.ZERO);
          this.filled.set(2);
      }
      if (!filled.get(3)) {
          acc4.add(BigInteger.ZERO);
          this.filled.set(3);
      }
      if (!filled.get(4)) {
          accA.add(BigInteger.ZERO);
          this.filled.set(4);
      }
      if (!filled.get(5)) {
          accQ.add(BigInteger.ZERO);
          this.filled.set(5);
      }
      if (!filled.get(6)) {
          accW.add(BigInteger.ZERO);
          this.filled.set(6);
      }
      if (!filled.get(7)) {
          byte1.add(UnsignedByte.of(0));
          this.filled.set(7);
      }
      if (!filled.get(8)) {
          byte2.add(UnsignedByte.of(0));
          this.filled.set(8);
      }
      if (!filled.get(9)) {
          byte3.add(UnsignedByte.of(0));
          this.filled.set(9);
      }
      if (!filled.get(10)) {
          byte4.add(UnsignedByte.of(0));
          this.filled.set(10);
      }
      if (!filled.get(11)) {
          byteA.add(UnsignedByte.of(0));
          this.filled.set(11);
      }
      if (!filled.get(12)) {
          byteQ.add(UnsignedByte.of(0));
          this.filled.set(12);
      }
      if (!filled.get(13)) {
          byteQq.add(BigInteger.ZERO);
          this.filled.set(13);
      }
      if (!filled.get(14)) {
          byteR.add(BigInteger.ZERO);
          this.filled.set(14);
      }
      if (!filled.get(15)) {
          byteW.add(UnsignedByte.of(0));
          this.filled.set(15);
      }
      if (!filled.get(19)) {
          cMem.add(BigInteger.ZERO);
          this.filled.set(19);
      }
      if (!filled.get(20)) {
          cMemNew.add(BigInteger.ZERO);
          this.filled.set(20);
      }
      if (!filled.get(16)) {
          cn.add(BigInteger.ZERO);
          this.filled.set(16);
      }
      if (!filled.get(17)) {
          comp.add(false);
          this.filled.set(17);
      }
      if (!filled.get(18)) {
          ct.add(BigInteger.ZERO);
          this.filled.set(18);
      }
      if (!filled.get(21)) {
          deploys.add(false);
          this.filled.set(21);
      }
      if (!filled.get(22)) {
          expands.add(false);
          this.filled.set(22);
      }
      if (!filled.get(23)) {
          gasMxp.add(BigInteger.ZERO);
          this.filled.set(23);
      }
      if (!filled.get(24)) {
          gbyte.add(BigInteger.ZERO);
          this.filled.set(24);
      }
      if (!filled.get(25)) {
          gword.add(BigInteger.ZERO);
          this.filled.set(25);
      }
      if (!filled.get(26)) {
          inst.add(BigInteger.ZERO);
          this.filled.set(26);
      }
      if (!filled.get(27)) {
          linCost.add(BigInteger.ZERO);
          this.filled.set(27);
      }
      if (!filled.get(28)) {
          maxOffset.add(BigInteger.ZERO);
          this.filled.set(28);
      }
      if (!filled.get(29)) {
          maxOffset1.add(BigInteger.ZERO);
          this.filled.set(29);
      }
      if (!filled.get(30)) {
          maxOffset2.add(BigInteger.ZERO);
          this.filled.set(30);
      }
      if (!filled.get(32)) {
          mxpType1.add(false);
          this.filled.set(32);
      }
      if (!filled.get(33)) {
          mxpType2.add(false);
          this.filled.set(33);
      }
      if (!filled.get(34)) {
          mxpType3.add(false);
          this.filled.set(34);
      }
      if (!filled.get(35)) {
          mxpType4.add(false);
          this.filled.set(35);
      }
      if (!filled.get(36)) {
          mxpType5.add(false);
          this.filled.set(36);
      }
      if (!filled.get(31)) {
          mxpx.add(false);
          this.filled.set(31);
      }
      if (!filled.get(37)) {
          noop.add(false);
          this.filled.set(37);
      }
      if (!filled.get(38)) {
          offset1Hi.add(BigInteger.ZERO);
          this.filled.set(38);
      }
      if (!filled.get(39)) {
          offset1Lo.add(BigInteger.ZERO);
          this.filled.set(39);
      }
      if (!filled.get(40)) {
          offset2Hi.add(BigInteger.ZERO);
          this.filled.set(40);
      }
      if (!filled.get(41)) {
          offset2Lo.add(BigInteger.ZERO);
          this.filled.set(41);
      }
      if (!filled.get(42)) {
          quadCost.add(BigInteger.ZERO);
          this.filled.set(42);
      }
      if (!filled.get(43)) {
          roob.add(false);
          this.filled.set(43);
      }
      if (!filled.get(44)) {
          size1Hi.add(BigInteger.ZERO);
          this.filled.set(44);
      }
      if (!filled.get(45)) {
          size1Lo.add(BigInteger.ZERO);
          this.filled.set(45);
      }
      if (!filled.get(46)) {
          size2Hi.add(BigInteger.ZERO);
          this.filled.set(46);
      }
      if (!filled.get(47)) {
          size2Lo.add(BigInteger.ZERO);
          this.filled.set(47);
      }
      if (!filled.get(48)) {
          stamp.add(BigInteger.ZERO);
          this.filled.set(48);
      }
      if (!filled.get(49)) {
          words.add(BigInteger.ZERO);
          this.filled.set(49);
      }
      if (!filled.get(50)) {
          wordsNew.add(BigInteger.ZERO);
          this.filled.set(50);
      }

      return this.validateRow();
    }

    public Trace build() {
      if (!filled.isEmpty()) {
        throw new IllegalStateException("Cannot build trace with a non-validated row.");
      }

      return new Trace(
        acc1,
        acc2,
        acc3,
        acc4,
        accA,
        accQ,
        accW,
        byte1,
        byte2,
        byte3,
        byte4,
        byteA,
        byteQ,
        byteQq,
        byteR,
        byteW,
        cMem,
        cMemNew,
        cn,
        comp,
        ct,
        deploys,
        expands,
        gasMxp,
        gbyte,
        gword,
        inst,
        linCost,
        maxOffset,
        maxOffset1,
        maxOffset2,
        mxpType1,
        mxpType2,
        mxpType3,
        mxpType4,
        mxpType5,
        mxpx,
        noop,
        offset1Hi,
        offset1Lo,
        offset2Hi,
        offset2Lo,
        quadCost,
        roob,
        size1Hi,
        size1Lo,
        size2Hi,
        size2Lo,
        stamp,
        words,
        wordsNew);
    }
  }
}
