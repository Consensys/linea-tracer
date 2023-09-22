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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.consensys.linea.zktracer.bytes.UnsignedByte;

/**
 * WARNING: This code is generated automatically. Any modifications to this code may be overwritten
 * and could lead to unexpected behavior. Please DO NOT ATTEMPT TO MODIFY this code directly.
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
    @JsonProperty("CN") List<BigInteger> cn,
    @JsonProperty("COMP") List<BigInteger> comp,
    @JsonProperty("COUNTER") List<BigInteger> counter,
    @JsonProperty("DELTA_MXPC") List<BigInteger> deltaMxpc,
    @JsonProperty("MAX_OFFSET") List<BigInteger> maxOffset,
    @JsonProperty("MAX_OFFSET_1") List<BigInteger> maxOffset1,
    @JsonProperty("MAX_OFFSET_2") List<BigInteger> maxOffset2,
    @JsonProperty("MEMORY_EXPANSION_EVENT") List<Boolean> memoryExpansionEvent,
    @JsonProperty("MEMORY_EXPANSION_EXCEPTION") List<Boolean> memoryExpansionException,
    @JsonProperty("MXP_GBYTE") List<BigInteger> mxpGbyte,
    @JsonProperty("MXP_GWORD") List<BigInteger> mxpGword,
    @JsonProperty("MXP_INST") List<BigInteger> mxpInst,
    @JsonProperty("MXP_TYPE_1") List<Boolean> mxpType1,
    @JsonProperty("MXP_TYPE_2") List<Boolean> mxpType2,
    @JsonProperty("MXP_TYPE_3") List<Boolean> mxpType3,
    @JsonProperty("MXP_TYPE_4") List<Boolean> mxpType4,
    @JsonProperty("MXP_TYPE_5") List<Boolean> mxpType5,
    @JsonProperty("MXPC") List<BigInteger> mxpc,
    @JsonProperty("MXPC_NEW") List<BigInteger> mxpcNew,
    @JsonProperty("NOOP") List<Boolean> noop,
    @JsonProperty("OFFSET_1_HI") List<BigInteger> offset1Hi,
    @JsonProperty("OFFSET_1_LO") List<BigInteger> offset1Lo,
    @JsonProperty("OFFSET_2_HI") List<BigInteger> offset2Hi,
    @JsonProperty("OFFSET_2_LO") List<BigInteger> offset2Lo,
    @JsonProperty("RIDICULOUSLY_OUT_OF_BOUND") List<Boolean> ridiculouslyOutOfBound,
    @JsonProperty("SIZE_1_HI") List<BigInteger> size1Hi,
    @JsonProperty("SIZE_1_LO") List<BigInteger> size1Lo,
    @JsonProperty("SIZE_2_HI") List<BigInteger> size2Hi,
    @JsonProperty("SIZE_2_LO") List<BigInteger> size2Lo,
    @JsonProperty("STAMP") List<BigInteger> stamp,
    @JsonProperty("WORDS") List<BigInteger> words,
    @JsonProperty("WORDS_NEW") List<BigInteger> wordsNew) {
  static TraceBuilder builder() {
    return new TraceBuilder();
  }

  static class TraceBuilder {
    private final BitSet filled = new BitSet();

    @JsonProperty("ACC_1")
    private final List<BigInteger> acc1 = new ArrayList<>();

    @JsonProperty("ACC_2")
    private final List<BigInteger> acc2 = new ArrayList<>();

    @JsonProperty("ACC_3")
    private final List<BigInteger> acc3 = new ArrayList<>();

    @JsonProperty("ACC_4")
    private final List<BigInteger> acc4 = new ArrayList<>();

    @JsonProperty("ACC_A")
    private final List<BigInteger> accA = new ArrayList<>();

    @JsonProperty("ACC_Q")
    private final List<BigInteger> accQ = new ArrayList<>();

    @JsonProperty("ACC_W")
    private final List<BigInteger> accW = new ArrayList<>();

    @JsonProperty("BYTE_1")
    private final List<UnsignedByte> byte1 = new ArrayList<>();

    @JsonProperty("BYTE_2")
    private final List<UnsignedByte> byte2 = new ArrayList<>();

    @JsonProperty("BYTE_3")
    private final List<UnsignedByte> byte3 = new ArrayList<>();

    @JsonProperty("BYTE_4")
    private final List<UnsignedByte> byte4 = new ArrayList<>();

    @JsonProperty("BYTE_A")
    private final List<UnsignedByte> byteA = new ArrayList<>();

    @JsonProperty("BYTE_Q")
    private final List<UnsignedByte> byteQ = new ArrayList<>();

    @JsonProperty("BYTE_QQ")
    private final List<BigInteger> byteQq = new ArrayList<>();

    @JsonProperty("BYTE_R")
    private final List<BigInteger> byteR = new ArrayList<>();

    @JsonProperty("BYTE_W")
    private final List<UnsignedByte> byteW = new ArrayList<>();

    @JsonProperty("CN")
    private final List<BigInteger> cn = new ArrayList<>();

    @JsonProperty("COMP")
    private final List<BigInteger> comp = new ArrayList<>();

    @JsonProperty("COUNTER")
    private final List<BigInteger> counter = new ArrayList<>();

    @JsonProperty("DELTA_MXPC")
    private final List<BigInteger> deltaMxpc = new ArrayList<>();

    @JsonProperty("MAX_OFFSET")
    private final List<BigInteger> maxOffset = new ArrayList<>();

    @JsonProperty("MAX_OFFSET_1")
    private final List<BigInteger> maxOffset1 = new ArrayList<>();

    @JsonProperty("MAX_OFFSET_2")
    private final List<BigInteger> maxOffset2 = new ArrayList<>();

    @JsonProperty("MEMORY_EXPANSION_EVENT")
    private final List<Boolean> memoryExpansionEvent = new ArrayList<>();

    @JsonProperty("MEMORY_EXPANSION_EXCEPTION")
    private final List<Boolean> memoryExpansionException = new ArrayList<>();

    @JsonProperty("MXP_GBYTE")
    private final List<BigInteger> mxpGbyte = new ArrayList<>();

    @JsonProperty("MXP_GWORD")
    private final List<BigInteger> mxpGword = new ArrayList<>();

    @JsonProperty("MXP_INST")
    private final List<BigInteger> mxpInst = new ArrayList<>();

    @JsonProperty("MXP_TYPE_1")
    private final List<Boolean> mxpType1 = new ArrayList<>();

    @JsonProperty("MXP_TYPE_2")
    private final List<Boolean> mxpType2 = new ArrayList<>();

    @JsonProperty("MXP_TYPE_3")
    private final List<Boolean> mxpType3 = new ArrayList<>();

    @JsonProperty("MXP_TYPE_4")
    private final List<Boolean> mxpType4 = new ArrayList<>();

    @JsonProperty("MXP_TYPE_5")
    private final List<Boolean> mxpType5 = new ArrayList<>();

    @JsonProperty("MXPC")
    private final List<BigInteger> mxpc = new ArrayList<>();

    @JsonProperty("MXPC_NEW")
    private final List<BigInteger> mxpcNew = new ArrayList<>();

    @JsonProperty("NOOP")
    private final List<Boolean> noop = new ArrayList<>();

    @JsonProperty("OFFSET_1_HI")
    private final List<BigInteger> offset1Hi = new ArrayList<>();

    @JsonProperty("OFFSET_1_LO")
    private final List<BigInteger> offset1Lo = new ArrayList<>();

    @JsonProperty("OFFSET_2_HI")
    private final List<BigInteger> offset2Hi = new ArrayList<>();

    @JsonProperty("OFFSET_2_LO")
    private final List<BigInteger> offset2Lo = new ArrayList<>();

    @JsonProperty("RIDICULOUSLY_OUT_OF_BOUND")
    private final List<Boolean> ridiculouslyOutOfBound = new ArrayList<>();

    @JsonProperty("SIZE_1_HI")
    private final List<BigInteger> size1Hi = new ArrayList<>();

    @JsonProperty("SIZE_1_LO")
    private final List<BigInteger> size1Lo = new ArrayList<>();

    @JsonProperty("SIZE_2_HI")
    private final List<BigInteger> size2Hi = new ArrayList<>();

    @JsonProperty("SIZE_2_LO")
    private final List<BigInteger> size2Lo = new ArrayList<>();

    @JsonProperty("STAMP")
    private final List<BigInteger> stamp = new ArrayList<>();

    @JsonProperty("WORDS")
    private final List<BigInteger> words = new ArrayList<>();

    @JsonProperty("WORDS_NEW")
    private final List<BigInteger> wordsNew = new ArrayList<>();

    private TraceBuilder() {}

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

    public TraceBuilder cn(final BigInteger b) {
      if (filled.get(16)) {
        throw new IllegalStateException("CN already set");
      } else {
        filled.set(16);
      }

      cn.add(b);

      return this;
    }

    public TraceBuilder comp(final BigInteger b) {
      if (filled.get(17)) {
        throw new IllegalStateException("COMP already set");
      } else {
        filled.set(17);
      }

      comp.add(b);

      return this;
    }

    public TraceBuilder counter(final BigInteger b) {
      if (filled.get(18)) {
        throw new IllegalStateException("COUNTER already set");
      } else {
        filled.set(18);
      }

      counter.add(b);

      return this;
    }

    public TraceBuilder deltaMxpc(final BigInteger b) {
      if (filled.get(19)) {
        throw new IllegalStateException("DELTA_MXPC already set");
      } else {
        filled.set(19);
      }

      deltaMxpc.add(b);

      return this;
    }

    public TraceBuilder maxOffset(final BigInteger b) {
      if (filled.get(20)) {
        throw new IllegalStateException("MAX_OFFSET already set");
      } else {
        filled.set(20);
      }

      maxOffset.add(b);

      return this;
    }

    public TraceBuilder maxOffset1(final BigInteger b) {
      if (filled.get(21)) {
        throw new IllegalStateException("MAX_OFFSET_1 already set");
      } else {
        filled.set(21);
      }

      maxOffset1.add(b);

      return this;
    }

    public TraceBuilder maxOffset2(final BigInteger b) {
      if (filled.get(22)) {
        throw new IllegalStateException("MAX_OFFSET_2 already set");
      } else {
        filled.set(22);
      }

      maxOffset2.add(b);

      return this;
    }

    public TraceBuilder memoryExpansionEvent(final Boolean b) {
      if (filled.get(23)) {
        throw new IllegalStateException("MEMORY_EXPANSION_EVENT already set");
      } else {
        filled.set(23);
      }

      memoryExpansionEvent.add(b);

      return this;
    }

    public TraceBuilder memoryExpansionException(final Boolean b) {
      if (filled.get(24)) {
        throw new IllegalStateException("MEMORY_EXPANSION_EXCEPTION already set");
      } else {
        filled.set(24);
      }

      memoryExpansionException.add(b);

      return this;
    }

    public TraceBuilder mxpGbyte(final BigInteger b) {
      if (filled.get(27)) {
        throw new IllegalStateException("MXP_GBYTE already set");
      } else {
        filled.set(27);
      }

      mxpGbyte.add(b);

      return this;
    }

    public TraceBuilder mxpGword(final BigInteger b) {
      if (filled.get(28)) {
        throw new IllegalStateException("MXP_GWORD already set");
      } else {
        filled.set(28);
      }

      mxpGword.add(b);

      return this;
    }

    public TraceBuilder mxpInst(final BigInteger b) {
      if (filled.get(29)) {
        throw new IllegalStateException("MXP_INST already set");
      } else {
        filled.set(29);
      }

      mxpInst.add(b);

      return this;
    }

    public TraceBuilder mxpType1(final Boolean b) {
      if (filled.get(30)) {
        throw new IllegalStateException("MXP_TYPE_1 already set");
      } else {
        filled.set(30);
      }

      mxpType1.add(b);

      return this;
    }

    public TraceBuilder mxpType2(final Boolean b) {
      if (filled.get(31)) {
        throw new IllegalStateException("MXP_TYPE_2 already set");
      } else {
        filled.set(31);
      }

      mxpType2.add(b);

      return this;
    }

    public TraceBuilder mxpType3(final Boolean b) {
      if (filled.get(32)) {
        throw new IllegalStateException("MXP_TYPE_3 already set");
      } else {
        filled.set(32);
      }

      mxpType3.add(b);

      return this;
    }

    public TraceBuilder mxpType4(final Boolean b) {
      if (filled.get(33)) {
        throw new IllegalStateException("MXP_TYPE_4 already set");
      } else {
        filled.set(33);
      }

      mxpType4.add(b);

      return this;
    }

    public TraceBuilder mxpType5(final Boolean b) {
      if (filled.get(34)) {
        throw new IllegalStateException("MXP_TYPE_5 already set");
      } else {
        filled.set(34);
      }

      mxpType5.add(b);

      return this;
    }

    public TraceBuilder mxpc(final BigInteger b) {
      if (filled.get(25)) {
        throw new IllegalStateException("MXPC already set");
      } else {
        filled.set(25);
      }

      mxpc.add(b);

      return this;
    }

    public TraceBuilder mxpcNew(final BigInteger b) {
      if (filled.get(26)) {
        throw new IllegalStateException("MXPC_NEW already set");
      } else {
        filled.set(26);
      }

      mxpcNew.add(b);

      return this;
    }

    public TraceBuilder noop(final Boolean b) {
      if (filled.get(35)) {
        throw new IllegalStateException("NOOP already set");
      } else {
        filled.set(35);
      }

      noop.add(b);

      return this;
    }

    public TraceBuilder offset1Hi(final BigInteger b) {
      if (filled.get(36)) {
        throw new IllegalStateException("OFFSET_1_HI already set");
      } else {
        filled.set(36);
      }

      offset1Hi.add(b);

      return this;
    }

    public TraceBuilder offset1Lo(final BigInteger b) {
      if (filled.get(37)) {
        throw new IllegalStateException("OFFSET_1_LO already set");
      } else {
        filled.set(37);
      }

      offset1Lo.add(b);

      return this;
    }

    public TraceBuilder offset2Hi(final BigInteger b) {
      if (filled.get(38)) {
        throw new IllegalStateException("OFFSET_2_HI already set");
      } else {
        filled.set(38);
      }

      offset2Hi.add(b);

      return this;
    }

    public TraceBuilder offset2Lo(final BigInteger b) {
      if (filled.get(39)) {
        throw new IllegalStateException("OFFSET_2_LO already set");
      } else {
        filled.set(39);
      }

      offset2Lo.add(b);

      return this;
    }

    public TraceBuilder ridiculouslyOutOfBound(final Boolean b) {
      if (filled.get(40)) {
        throw new IllegalStateException("RIDICULOUSLY_OUT_OF_BOUND already set");
      } else {
        filled.set(40);
      }

      ridiculouslyOutOfBound.add(b);

      return this;
    }

    public TraceBuilder size1Hi(final BigInteger b) {
      if (filled.get(41)) {
        throw new IllegalStateException("SIZE_1_HI already set");
      } else {
        filled.set(41);
      }

      size1Hi.add(b);

      return this;
    }

    public TraceBuilder size1Lo(final BigInteger b) {
      if (filled.get(42)) {
        throw new IllegalStateException("SIZE_1_LO already set");
      } else {
        filled.set(42);
      }

      size1Lo.add(b);

      return this;
    }

    public TraceBuilder size2Hi(final BigInteger b) {
      if (filled.get(43)) {
        throw new IllegalStateException("SIZE_2_HI already set");
      } else {
        filled.set(43);
      }

      size2Hi.add(b);

      return this;
    }

    public TraceBuilder size2Lo(final BigInteger b) {
      if (filled.get(44)) {
        throw new IllegalStateException("SIZE_2_LO already set");
      } else {
        filled.set(44);
      }

      size2Lo.add(b);

      return this;
    }

    public TraceBuilder stamp(final BigInteger b) {
      if (filled.get(45)) {
        throw new IllegalStateException("STAMP already set");
      } else {
        filled.set(45);
      }

      stamp.add(b);

      return this;
    }

    public TraceBuilder words(final BigInteger b) {
      if (filled.get(46)) {
        throw new IllegalStateException("WORDS already set");
      } else {
        filled.set(46);
      }

      words.add(b);

      return this;
    }

    public TraceBuilder wordsNew(final BigInteger b) {
      if (filled.get(47)) {
        throw new IllegalStateException("WORDS_NEW already set");
      } else {
        filled.set(47);
      }

      wordsNew.add(b);

      return this;
    }

    public TraceBuilder setAcc1At(final BigInteger b, int i) {
      acc1.set(i, b);

      return this;
    }

    public TraceBuilder setAcc2At(final BigInteger b, int i) {
      acc2.set(i, b);

      return this;
    }

    public TraceBuilder setAcc3At(final BigInteger b, int i) {
      acc3.set(i, b);

      return this;
    }

    public TraceBuilder setAcc4At(final BigInteger b, int i) {
      acc4.set(i, b);

      return this;
    }

    public TraceBuilder setAccAAt(final BigInteger b, int i) {
      accA.set(i, b);

      return this;
    }

    public TraceBuilder setAccQAt(final BigInteger b, int i) {
      accQ.set(i, b);

      return this;
    }

    public TraceBuilder setAccWAt(final BigInteger b, int i) {
      accW.set(i, b);

      return this;
    }

    public TraceBuilder setByte1At(final UnsignedByte b, int i) {
      byte1.set(i, b);

      return this;
    }

    public TraceBuilder setByte2At(final UnsignedByte b, int i) {
      byte2.set(i, b);

      return this;
    }

    public TraceBuilder setByte3At(final UnsignedByte b, int i) {
      byte3.set(i, b);

      return this;
    }

    public TraceBuilder setByte4At(final UnsignedByte b, int i) {
      byte4.set(i, b);

      return this;
    }

    public TraceBuilder setByteAAt(final UnsignedByte b, int i) {
      byteA.set(i, b);

      return this;
    }

    public TraceBuilder setByteQAt(final UnsignedByte b, int i) {
      byteQ.set(i, b);

      return this;
    }

    public TraceBuilder setByteQqAt(final BigInteger b, int i) {
      byteQq.set(i, b);

      return this;
    }

    public TraceBuilder setByteRAt(final BigInteger b, int i) {
      byteR.set(i, b);

      return this;
    }

    public TraceBuilder setByteWAt(final UnsignedByte b, int i) {
      byteW.set(i, b);

      return this;
    }

    public TraceBuilder setCnAt(final BigInteger b, int i) {
      cn.set(i, b);

      return this;
    }

    public TraceBuilder setCompAt(final BigInteger b, int i) {
      comp.set(i, b);

      return this;
    }

    public TraceBuilder setCounterAt(final BigInteger b, int i) {
      counter.set(i, b);

      return this;
    }

    public TraceBuilder setDeltaMxpcAt(final BigInteger b, int i) {
      deltaMxpc.set(i, b);

      return this;
    }

    public TraceBuilder setMaxOffsetAt(final BigInteger b, int i) {
      maxOffset.set(i, b);

      return this;
    }

    public TraceBuilder setMaxOffset1At(final BigInteger b, int i) {
      maxOffset1.set(i, b);

      return this;
    }

    public TraceBuilder setMaxOffset2At(final BigInteger b, int i) {
      maxOffset2.set(i, b);

      return this;
    }

    public TraceBuilder setMemoryExpansionEventAt(final Boolean b, int i) {
      memoryExpansionEvent.set(i, b);

      return this;
    }

    public TraceBuilder setMemoryExpansionExceptionAt(final Boolean b, int i) {
      memoryExpansionException.set(i, b);

      return this;
    }

    public TraceBuilder setMxpGbyteAt(final BigInteger b, int i) {
      mxpGbyte.set(i, b);

      return this;
    }

    public TraceBuilder setMxpGwordAt(final BigInteger b, int i) {
      mxpGword.set(i, b);

      return this;
    }

    public TraceBuilder setMxpInstAt(final BigInteger b, int i) {
      mxpInst.set(i, b);

      return this;
    }

    public TraceBuilder setMxpType1At(final Boolean b, int i) {
      mxpType1.set(i, b);

      return this;
    }

    public TraceBuilder setMxpType2At(final Boolean b, int i) {
      mxpType2.set(i, b);

      return this;
    }

    public TraceBuilder setMxpType3At(final Boolean b, int i) {
      mxpType3.set(i, b);

      return this;
    }

    public TraceBuilder setMxpType4At(final Boolean b, int i) {
      mxpType4.set(i, b);

      return this;
    }

    public TraceBuilder setMxpType5At(final Boolean b, int i) {
      mxpType5.set(i, b);

      return this;
    }

    public TraceBuilder setMxpcAt(final BigInteger b, int i) {
      mxpc.set(i, b);

      return this;
    }

    public TraceBuilder setMxpcNewAt(final BigInteger b, int i) {
      mxpcNew.set(i, b);

      return this;
    }

    public TraceBuilder setNoopAt(final Boolean b, int i) {
      noop.set(i, b);

      return this;
    }

    public TraceBuilder setOffset1HiAt(final BigInteger b, int i) {
      offset1Hi.set(i, b);

      return this;
    }

    public TraceBuilder setOffset1LoAt(final BigInteger b, int i) {
      offset1Lo.set(i, b);

      return this;
    }

    public TraceBuilder setOffset2HiAt(final BigInteger b, int i) {
      offset2Hi.set(i, b);

      return this;
    }

    public TraceBuilder setOffset2LoAt(final BigInteger b, int i) {
      offset2Lo.set(i, b);

      return this;
    }

    public TraceBuilder setRidiculouslyOutOfBoundAt(final Boolean b, int i) {
      ridiculouslyOutOfBound.set(i, b);

      return this;
    }

    public TraceBuilder setSize1HiAt(final BigInteger b, int i) {
      size1Hi.set(i, b);

      return this;
    }

    public TraceBuilder setSize1LoAt(final BigInteger b, int i) {
      size1Lo.set(i, b);

      return this;
    }

    public TraceBuilder setSize2HiAt(final BigInteger b, int i) {
      size2Hi.set(i, b);

      return this;
    }

    public TraceBuilder setSize2LoAt(final BigInteger b, int i) {
      size2Lo.set(i, b);

      return this;
    }

    public TraceBuilder setStampAt(final BigInteger b, int i) {
      stamp.set(i, b);

      return this;
    }

    public TraceBuilder setWordsAt(final BigInteger b, int i) {
      words.set(i, b);

      return this;
    }

    public TraceBuilder setWordsNewAt(final BigInteger b, int i) {
      wordsNew.set(i, b);

      return this;
    }

    public TraceBuilder setAcc1Relative(final BigInteger b, int i) {
      acc1.set(acc1.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setAcc2Relative(final BigInteger b, int i) {
      acc2.set(acc2.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setAcc3Relative(final BigInteger b, int i) {
      acc3.set(acc3.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setAcc4Relative(final BigInteger b, int i) {
      acc4.set(acc4.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setAccARelative(final BigInteger b, int i) {
      accA.set(accA.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setAccQRelative(final BigInteger b, int i) {
      accQ.set(accQ.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setAccWRelative(final BigInteger b, int i) {
      accW.set(accW.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setByte1Relative(final UnsignedByte b, int i) {
      byte1.set(byte1.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setByte2Relative(final UnsignedByte b, int i) {
      byte2.set(byte2.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setByte3Relative(final UnsignedByte b, int i) {
      byte3.set(byte3.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setByte4Relative(final UnsignedByte b, int i) {
      byte4.set(byte4.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setByteARelative(final UnsignedByte b, int i) {
      byteA.set(byteA.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setByteQRelative(final UnsignedByte b, int i) {
      byteQ.set(byteQ.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setByteQqRelative(final BigInteger b, int i) {
      byteQq.set(byteQq.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setByteRRelative(final BigInteger b, int i) {
      byteR.set(byteR.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setByteWRelative(final UnsignedByte b, int i) {
      byteW.set(byteW.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setCnRelative(final BigInteger b, int i) {
      cn.set(cn.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setCompRelative(final BigInteger b, int i) {
      comp.set(comp.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setCounterRelative(final BigInteger b, int i) {
      counter.set(counter.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setDeltaMxpcRelative(final BigInteger b, int i) {
      deltaMxpc.set(deltaMxpc.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setMaxOffsetRelative(final BigInteger b, int i) {
      maxOffset.set(maxOffset.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setMaxOffset1Relative(final BigInteger b, int i) {
      maxOffset1.set(maxOffset1.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setMaxOffset2Relative(final BigInteger b, int i) {
      maxOffset2.set(maxOffset2.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setMemoryExpansionEventRelative(final Boolean b, int i) {
      memoryExpansionEvent.set(memoryExpansionEvent.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setMemoryExpansionExceptionRelative(final Boolean b, int i) {
      memoryExpansionException.set(memoryExpansionException.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setMxpGbyteRelative(final BigInteger b, int i) {
      mxpGbyte.set(mxpGbyte.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setMxpGwordRelative(final BigInteger b, int i) {
      mxpGword.set(mxpGword.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setMxpInstRelative(final BigInteger b, int i) {
      mxpInst.set(mxpInst.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setMxpType1Relative(final Boolean b, int i) {
      mxpType1.set(mxpType1.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setMxpType2Relative(final Boolean b, int i) {
      mxpType2.set(mxpType2.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setMxpType3Relative(final Boolean b, int i) {
      mxpType3.set(mxpType3.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setMxpType4Relative(final Boolean b, int i) {
      mxpType4.set(mxpType4.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setMxpType5Relative(final Boolean b, int i) {
      mxpType5.set(mxpType5.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setMxpcRelative(final BigInteger b, int i) {
      mxpc.set(mxpc.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setMxpcNewRelative(final BigInteger b, int i) {
      mxpcNew.set(mxpcNew.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setNoopRelative(final Boolean b, int i) {
      noop.set(noop.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setOffset1HiRelative(final BigInteger b, int i) {
      offset1Hi.set(offset1Hi.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setOffset1LoRelative(final BigInteger b, int i) {
      offset1Lo.set(offset1Lo.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setOffset2HiRelative(final BigInteger b, int i) {
      offset2Hi.set(offset2Hi.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setOffset2LoRelative(final BigInteger b, int i) {
      offset2Lo.set(offset2Lo.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setRidiculouslyOutOfBoundRelative(final Boolean b, int i) {
      ridiculouslyOutOfBound.set(ridiculouslyOutOfBound.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setSize1HiRelative(final BigInteger b, int i) {
      size1Hi.set(size1Hi.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setSize1LoRelative(final BigInteger b, int i) {
      size1Lo.set(size1Lo.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setSize2HiRelative(final BigInteger b, int i) {
      size2Hi.set(size2Hi.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setSize2LoRelative(final BigInteger b, int i) {
      size2Lo.set(size2Lo.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setStampRelative(final BigInteger b, int i) {
      stamp.set(stamp.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setWordsRelative(final BigInteger b, int i) {
      words.set(words.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setWordsNewRelative(final BigInteger b, int i) {
      wordsNew.set(wordsNew.size() - 1 - i, b);

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

      if (!filled.get(16)) {
        throw new IllegalStateException("CN has not been filled");
      }

      if (!filled.get(17)) {
        throw new IllegalStateException("COMP has not been filled");
      }

      if (!filled.get(18)) {
        throw new IllegalStateException("COUNTER has not been filled");
      }

      if (!filled.get(19)) {
        throw new IllegalStateException("DELTA_MXPC has not been filled");
      }

      if (!filled.get(20)) {
        throw new IllegalStateException("MAX_OFFSET has not been filled");
      }

      if (!filled.get(21)) {
        throw new IllegalStateException("MAX_OFFSET_1 has not been filled");
      }

      if (!filled.get(22)) {
        throw new IllegalStateException("MAX_OFFSET_2 has not been filled");
      }

      if (!filled.get(23)) {
        throw new IllegalStateException("MEMORY_EXPANSION_EVENT has not been filled");
      }

      if (!filled.get(24)) {
        throw new IllegalStateException("MEMORY_EXPANSION_EXCEPTION has not been filled");
      }

      if (!filled.get(27)) {
        throw new IllegalStateException("MXP_GBYTE has not been filled");
      }

      if (!filled.get(28)) {
        throw new IllegalStateException("MXP_GWORD has not been filled");
      }

      if (!filled.get(29)) {
        throw new IllegalStateException("MXP_INST has not been filled");
      }

      if (!filled.get(30)) {
        throw new IllegalStateException("MXP_TYPE_1 has not been filled");
      }

      if (!filled.get(31)) {
        throw new IllegalStateException("MXP_TYPE_2 has not been filled");
      }

      if (!filled.get(32)) {
        throw new IllegalStateException("MXP_TYPE_3 has not been filled");
      }

      if (!filled.get(33)) {
        throw new IllegalStateException("MXP_TYPE_4 has not been filled");
      }

      if (!filled.get(34)) {
        throw new IllegalStateException("MXP_TYPE_5 has not been filled");
      }

      if (!filled.get(25)) {
        throw new IllegalStateException("MXPC has not been filled");
      }

      if (!filled.get(26)) {
        throw new IllegalStateException("MXPC_NEW has not been filled");
      }

      if (!filled.get(35)) {
        throw new IllegalStateException("NOOP has not been filled");
      }

      if (!filled.get(36)) {
        throw new IllegalStateException("OFFSET_1_HI has not been filled");
      }

      if (!filled.get(37)) {
        throw new IllegalStateException("OFFSET_1_LO has not been filled");
      }

      if (!filled.get(38)) {
        throw new IllegalStateException("OFFSET_2_HI has not been filled");
      }

      if (!filled.get(39)) {
        throw new IllegalStateException("OFFSET_2_LO has not been filled");
      }

      if (!filled.get(40)) {
        throw new IllegalStateException("RIDICULOUSLY_OUT_OF_BOUND has not been filled");
      }

      if (!filled.get(41)) {
        throw new IllegalStateException("SIZE_1_HI has not been filled");
      }

      if (!filled.get(42)) {
        throw new IllegalStateException("SIZE_1_LO has not been filled");
      }

      if (!filled.get(43)) {
        throw new IllegalStateException("SIZE_2_HI has not been filled");
      }

      if (!filled.get(44)) {
        throw new IllegalStateException("SIZE_2_LO has not been filled");
      }

      if (!filled.get(45)) {
        throw new IllegalStateException("STAMP has not been filled");
      }

      if (!filled.get(46)) {
        throw new IllegalStateException("WORDS has not been filled");
      }

      if (!filled.get(47)) {
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
      if (!filled.get(16)) {
        cn.add(BigInteger.ZERO);
        this.filled.set(16);
      }
      if (!filled.get(17)) {
        comp.add(BigInteger.ZERO);
        this.filled.set(17);
      }
      if (!filled.get(18)) {
        counter.add(BigInteger.ZERO);
        this.filled.set(18);
      }
      if (!filled.get(19)) {
        deltaMxpc.add(BigInteger.ZERO);
        this.filled.set(19);
      }
      if (!filled.get(20)) {
        maxOffset.add(BigInteger.ZERO);
        this.filled.set(20);
      }
      if (!filled.get(21)) {
        maxOffset1.add(BigInteger.ZERO);
        this.filled.set(21);
      }
      if (!filled.get(22)) {
        maxOffset2.add(BigInteger.ZERO);
        this.filled.set(22);
      }
      if (!filled.get(23)) {
        memoryExpansionEvent.add(false);
        this.filled.set(23);
      }
      if (!filled.get(24)) {
        memoryExpansionException.add(false);
        this.filled.set(24);
      }
      if (!filled.get(27)) {
        mxpGbyte.add(BigInteger.ZERO);
        this.filled.set(27);
      }
      if (!filled.get(28)) {
        mxpGword.add(BigInteger.ZERO);
        this.filled.set(28);
      }
      if (!filled.get(29)) {
        mxpInst.add(BigInteger.ZERO);
        this.filled.set(29);
      }
      if (!filled.get(30)) {
        mxpType1.add(false);
        this.filled.set(30);
      }
      if (!filled.get(31)) {
        mxpType2.add(false);
        this.filled.set(31);
      }
      if (!filled.get(32)) {
        mxpType3.add(false);
        this.filled.set(32);
      }
      if (!filled.get(33)) {
        mxpType4.add(false);
        this.filled.set(33);
      }
      if (!filled.get(34)) {
        mxpType5.add(false);
        this.filled.set(34);
      }
      if (!filled.get(25)) {
        mxpc.add(BigInteger.ZERO);
        this.filled.set(25);
      }
      if (!filled.get(26)) {
        mxpcNew.add(BigInteger.ZERO);
        this.filled.set(26);
      }
      if (!filled.get(35)) {
        noop.add(false);
        this.filled.set(35);
      }
      if (!filled.get(36)) {
        offset1Hi.add(BigInteger.ZERO);
        this.filled.set(36);
      }
      if (!filled.get(37)) {
        offset1Lo.add(BigInteger.ZERO);
        this.filled.set(37);
      }
      if (!filled.get(38)) {
        offset2Hi.add(BigInteger.ZERO);
        this.filled.set(38);
      }
      if (!filled.get(39)) {
        offset2Lo.add(BigInteger.ZERO);
        this.filled.set(39);
      }
      if (!filled.get(40)) {
        ridiculouslyOutOfBound.add(false);
        this.filled.set(40);
      }
      if (!filled.get(41)) {
        size1Hi.add(BigInteger.ZERO);
        this.filled.set(41);
      }
      if (!filled.get(42)) {
        size1Lo.add(BigInteger.ZERO);
        this.filled.set(42);
      }
      if (!filled.get(43)) {
        size2Hi.add(BigInteger.ZERO);
        this.filled.set(43);
      }
      if (!filled.get(44)) {
        size2Lo.add(BigInteger.ZERO);
        this.filled.set(44);
      }
      if (!filled.get(45)) {
        stamp.add(BigInteger.ZERO);
        this.filled.set(45);
      }
      if (!filled.get(46)) {
        words.add(BigInteger.ZERO);
        this.filled.set(46);
      }
      if (!filled.get(47)) {
        wordsNew.add(BigInteger.ZERO);
        this.filled.set(47);
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
          cn,
          comp,
          counter,
          deltaMxpc,
          maxOffset,
          maxOffset1,
          maxOffset2,
          memoryExpansionEvent,
          memoryExpansionException,
          mxpGbyte,
          mxpGword,
          mxpInst,
          mxpType1,
          mxpType2,
          mxpType3,
          mxpType4,
          mxpType5,
          mxpc,
          mxpcNew,
          noop,
          offset1Hi,
          offset1Lo,
          offset2Hi,
          offset2Lo,
          ridiculouslyOutOfBound,
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
