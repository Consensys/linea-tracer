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
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import net.consensys.linea.zktracer.bytes.UnsignedByte;

@JsonPropertyOrder({"Trace", "Stamp"})
@SuppressWarnings("unused")
public record MxpTrace(@JsonProperty("Trace") Trace trace, @JsonProperty("Stamp") int stamp) {
  @JsonPropertyOrder({
    "INST",
    "COUNTER",
    "TYPE_MXP",
    "WORDS",
    "WORDS_NEW",
    "OFFSET_1",
    "SIZE_1",
    "OFFSET_2",
    "SIZE_2",
    "MAX_OFFSET_1",
    "MAX_OFFSET_2",
    "MAX_OFFSET",
    "MXPX",
    "ROOB",
    "NOOP",
    "COMP",
    "MXPE",
    "MXPC",
    "MXP_NEW",
    "MXP_DELTA",
    "ACC_1",
    "ACC_2",
    "ACC_3",
    "ACC_4",
    "ACC_A",
    "ACC_W",
    "ACC_Q",
    "BYTE_1",
    "BYTE_2",
    "BYTE_3",
    "BYTE_4",
    "BYTE_A",
    "BYTE_W",
    "BYTE_Q",
    "BYTE_QQ",
    "BYTE_R",
    "MXP_STAMP"
  })
  public record Trace(
      @JsonProperty("INST") List<UnsignedByte> INST,
      @JsonProperty("COUNTER") List<Integer> COUNTER,
      @JsonProperty("TYPE_MXP") List<Integer> TYPE_MXP,
      @JsonProperty("WORDS") List<BigInteger> WORDS,
      @JsonProperty("WORDS_NEW") List<BigInteger> WORDS_NEW,
      @JsonProperty("OFFSET_1") List<BigInteger> OFFSET_1,
      @JsonProperty("SIZE_1") List<BigInteger> SIZE_1,
      @JsonProperty("OFFSET_2") List<BigInteger> OFFSET_2,
      @JsonProperty("SIZE_2") List<BigInteger> SIZE_2,
      @JsonProperty("MAX_OFFSET_1") List<BigInteger> MAX_OFFSET_1,
      @JsonProperty("MAX_OFFSET_2") List<BigInteger> MAX_OFFSET_2,
      @JsonProperty("MAX_OFFSET") List<BigInteger> MAX_OFFSET,
      @JsonProperty("MXPX") List<UnsignedByte> MXPX,
      @JsonProperty("ROOB") List<UnsignedByte> ROOB,
      @JsonProperty("NOOP") List<UnsignedByte> NOOP,
      @JsonProperty("COMP") List<UnsignedByte> COMP,
      @JsonProperty("MXPE") List<UnsignedByte> MXPE,
      @JsonProperty("MXPC") List<BigInteger> MXPC,
      @JsonProperty("MXPC_NEW") List<BigInteger> MXPC_NEW,
      @JsonProperty("MXPC_DELTA") List<BigInteger> MXPC_DELTA,
      @JsonProperty("ACC_1") List<BigInteger> ACC_1,
      @JsonProperty("ACC_2") List<BigInteger> ACC_2,
      @JsonProperty("ACC_3") List<BigInteger> ACC_3,
      @JsonProperty("ACC_4") List<BigInteger> ACC_4,
      @JsonProperty("ACC_A") List<BigInteger> ACC_A,
      @JsonProperty("ACC_W") List<BigInteger> ACC_W,
      @JsonProperty("ACC_Q") List<BigInteger> ACC_Q,
      @JsonProperty("BYTE_1") List<UnsignedByte> BYTE_1,
      @JsonProperty("BYTE_2") List<UnsignedByte> BYTE_2,
      @JsonProperty("BYTE_3") List<UnsignedByte> BYTE_3,
      @JsonProperty("BYTE_4") List<UnsignedByte> BYTE_4,
      @JsonProperty("BYTE_A") List<UnsignedByte> BYTE_A,
      @JsonProperty("BYTE_W") List<UnsignedByte> BYTE_W,
      @JsonProperty("BYTE_Q") List<UnsignedByte> BYTE_Q,
      @JsonProperty("BYTE_QQ") List<UnsignedByte> BYTE_QQ,
      @JsonProperty("BYTE_R") List<UnsignedByte> BYTE_R,
      @JsonProperty("MXP_STAMP") List<Integer> MXP_STAMP) {

    public static class Builder {
      private final List<UnsignedByte> inst = new ArrayList<>();
      private final List<Integer> counter = new ArrayList<>();
      private final List<Integer> typeMxp = new ArrayList<>();
      private final List<BigInteger> words = new ArrayList<>();
      private final List<BigInteger> wordsNew = new ArrayList<>();
      private final List<BigInteger> offset1 = new ArrayList<>();
      private final List<BigInteger> size1 = new ArrayList<>();
      private final List<BigInteger> offset2 = new ArrayList<>();
      private final List<BigInteger> size2 = new ArrayList<>();
      private final List<BigInteger> maxOffset1 = new ArrayList<>();
      private final List<BigInteger> maxOffset2 = new ArrayList<>();
      private final List<BigInteger> maxOffset = new ArrayList<>();
      private final List<UnsignedByte> mxpx = new ArrayList<>();
      private final List<UnsignedByte> roob = new ArrayList<>();
      private final List<UnsignedByte> noop = new ArrayList<>();
      private final List<UnsignedByte> comp = new ArrayList<>();
      private final List<UnsignedByte> mxpe = new ArrayList<>();
      private final List<BigInteger> mxpc = new ArrayList<>();
      private final List<BigInteger> mxpcNew = new ArrayList<>();
      private final List<BigInteger> mxpcDelta = new ArrayList<>();
      private final List<BigInteger> acc1 = new ArrayList<>();
      private final List<BigInteger> acc2 = new ArrayList<>();
      private final List<BigInteger> acc3 = new ArrayList<>();
      private final List<BigInteger> acc4 = new ArrayList<>();
      private final List<BigInteger> accA = new ArrayList<>();
      private final List<BigInteger> accW = new ArrayList<>();
      private final List<BigInteger> accQ = new ArrayList<>();
      private final List<UnsignedByte> byte1 = new ArrayList<>();
      private final List<UnsignedByte> byte2 = new ArrayList<>();
      private final List<UnsignedByte> byte3 = new ArrayList<>();
      private final List<UnsignedByte> byte4 = new ArrayList<>();
      private final List<UnsignedByte> byteA = new ArrayList<>();
      private final List<UnsignedByte> byteW = new ArrayList<>();
      private final List<UnsignedByte> byteQ = new ArrayList<>();
      private final List<UnsignedByte> byteQQ = new ArrayList<>();
      private final List<UnsignedByte> byteR = new ArrayList<>();
      private final List<Integer> mxpStamp = new ArrayList<>();
      private int stamp = 0;

      private Builder() {}

      public static Builder newInstance() {
        return new Builder();
      }

      public Builder appendInst(final UnsignedByte b) {
        inst.add(b);
        return this;
      }

      public Builder appendMxpx(final UnsignedByte b) {
        mxpx.add(b);
        return this;
      }

      public Builder appendCounter(final Integer b) {
        counter.add(b);
        return this;
      }

      public Builder appendMxpStamp(final Integer b) {
        mxpStamp.add(b);
        return this;
      }

      public Builder appendOffset1(final BigInteger b) {
        offset1.add(b);
        return this;
      }

      public Builder appendSize1(final BigInteger b) {
        size1.add(b);
        return this;
      }

      public Builder appendOffset2(final BigInteger b) {
        offset2.add(b);
        return this;
      }

      public Builder appendSize2(final BigInteger b) {
        size2.add(b);
        return this;
      }

      public Builder appendAcc1(final BigInteger b) {
        acc1.add(b);
        return this;
      }

      public Builder appendAcc2(final BigInteger b) {
        acc2.add(b);
        return this;
      }

      public Builder appendAcc3(final BigInteger b) {
        acc3.add(b);
        return this;
      }

      public Builder appendAcc4(final BigInteger b) {
        acc4.add(b);
        return this;
      }

      public Builder appendAccA(final BigInteger b) {
        accA.add(b);
        return this;
      }

      public Builder appendAccW(final BigInteger b) {
        accW.add(b);
        return this;
      }

      public Builder appendAccQ(final BigInteger b) {
        accQ.add(b);
        return this;
      }

      public Builder appendWords(final BigInteger b) {
        words.add(b);
        return this;
      }

      public Builder appendWordsNew(final BigInteger b) {
        wordsNew.add(b);
        return this;
      }

      public Builder appendByte1(final UnsignedByte b) {
        byte1.add(b);
        return this;
      }

      public Builder appendByte2(final UnsignedByte b) {
        byte2.add(b);
        return this;
      }

      public Builder appendByte3(final UnsignedByte b) {
        byte3.add(b);
        return this;
      }

      public Builder appendByte4(final UnsignedByte b) {
        byte4.add(b);
        return this;
      }

      public Builder appendByteA(final UnsignedByte b) {
        byteA.add(b);
        return this;
      }

      public Builder appendByteW(final UnsignedByte b) {
        byteW.add(b);
        return this;
      }

      public Builder appendByteQ(final UnsignedByte b) {
        byteQ.add(b);
        return this;
      }

      public Builder appendByteQQ(final UnsignedByte b) {
        byteQQ.add(b);
        return this;
      }

      public Builder appendByteR(final UnsignedByte b) {
        byteR.add(b);
        return this;
      }

      public Builder setStamp(final int stamp) {
        this.stamp = stamp;
        return this;
      }

      public MxpTrace build() {
        return new MxpTrace(
            new Trace(
                inst,
                counter,
                typeMxp,
                words,
                wordsNew,
                offset1,
                size1,
                offset2,
                size2,
                maxOffset1,
                maxOffset2,
                maxOffset,
                mxpx,
                roob,
                noop,
                comp,
                mxpe,
                mxpc,
                mxpcNew,
                mxpcDelta,
                acc1,
                acc2,
                acc3,
                acc4,
                accA,
                accW,
                accQ,
                byte1,
                byte2,
                byte3,
                byte4,
                byteA,
                byteW,
                byteQ,
                byteQQ,
                byteR,
                mxpStamp),
            stamp);
      }
    }
  }
}
