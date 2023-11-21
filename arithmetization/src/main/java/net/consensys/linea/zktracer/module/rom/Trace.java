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

package net.consensys.linea.zktracer.module.rom;

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
    @JsonProperty("rom.ACC") List<BigInteger> acc,
    @JsonProperty("rom.CODE_FRAGMENT_INDEX") List<BigInteger> codeFragmentIndex,
    @JsonProperty("rom.CODE_FRAGMENT_INDEX_INFTY") List<BigInteger> codeFragmentIndexInfty,
    @JsonProperty("rom.CODE_SIZE") List<BigInteger> codeSize,
    @JsonProperty("rom.CODESIZE_REACHED") List<Boolean> codesizeReached,
    @JsonProperty("rom.COUNTER") List<BigInteger> counter,
    @JsonProperty("rom.COUNTER_MAX") List<BigInteger> counterMax,
    @JsonProperty("rom.COUNTER_PUSH") List<BigInteger> counterPush,
    @JsonProperty("rom.INDEX") List<BigInteger> index,
    @JsonProperty("rom.IS_PUSH") List<Boolean> isPush,
    @JsonProperty("rom.IS_PUSH_DATA") List<Boolean> isPushData,
    @JsonProperty("rom.LIMB") List<BigInteger> limb,
    @JsonProperty("rom.nBYTES") List<BigInteger> nBytes,
    @JsonProperty("rom.nBYTES_ACC") List<BigInteger> nBytesAcc,
    @JsonProperty("rom.OPCODE") List<UnsignedByte> opcode,
    @JsonProperty("rom.PADDED_BYTECODE_BYTE") List<UnsignedByte> paddedBytecodeByte,
    @JsonProperty("rom.PROGRAMME_COUNTER") List<BigInteger> programmeCounter,
    @JsonProperty("rom.PUSH_FUNNEL_BIT") List<Boolean> pushFunnelBit,
    @JsonProperty("rom.PUSH_PARAMETER") List<BigInteger> pushParameter,
    @JsonProperty("rom.PUSH_VALUE_ACC") List<BigInteger> pushValueAcc,
    @JsonProperty("rom.PUSH_VALUE_HIGH") List<BigInteger> pushValueHigh,
    @JsonProperty("rom.PUSH_VALUE_LOW") List<BigInteger> pushValueLow,
    @JsonProperty("rom.VALID_JUMP_DESTINATION") List<Boolean> validJumpDestination) {
  static TraceBuilder builder(int length) {
    return new TraceBuilder(length);
  }

  public int size() {
    return this.acc.size();
  }

  public static List<ColumnHeader> headers(int size) {
    return List.of(
        new ColumnHeader("rom.ACC", 32, size),
        new ColumnHeader("rom.CODE_FRAGMENT_INDEX", 32, size),
        new ColumnHeader("rom.CODE_FRAGMENT_INDEX_INFTY", 32, size),
        new ColumnHeader("rom.CODE_SIZE", 32, size),
        new ColumnHeader("rom.CODESIZE_REACHED", 1, size),
        new ColumnHeader("rom.COUNTER", 32, size),
        new ColumnHeader("rom.COUNTER_MAX", 32, size),
        new ColumnHeader("rom.COUNTER_PUSH", 32, size),
        new ColumnHeader("rom.INDEX", 32, size),
        new ColumnHeader("rom.IS_PUSH", 1, size),
        new ColumnHeader("rom.IS_PUSH_DATA", 1, size),
        new ColumnHeader("rom.LIMB", 32, size),
        new ColumnHeader("rom.nBYTES", 32, size),
        new ColumnHeader("rom.nBYTES_ACC", 32, size),
        new ColumnHeader("rom.OPCODE", 1, size),
        new ColumnHeader("rom.PADDED_BYTECODE_BYTE", 1, size),
        new ColumnHeader("rom.PROGRAMME_COUNTER", 32, size),
        new ColumnHeader("rom.PUSH_FUNNEL_BIT", 1, size),
        new ColumnHeader("rom.PUSH_PARAMETER", 32, size),
        new ColumnHeader("rom.PUSH_VALUE_ACC", 32, size),
        new ColumnHeader("rom.PUSH_VALUE_HIGH", 32, size),
        new ColumnHeader("rom.PUSH_VALUE_LOW", 32, size),
        new ColumnHeader("rom.VALID_JUMP_DESTINATION", 1, size));
  }

  static class TraceBuilder {
    private final BitSet filled = new BitSet();
    private int currentLine = 0;

    private MappedByteBuffer acc;
    private MappedByteBuffer codeFragmentIndex;
    private MappedByteBuffer codeFragmentIndexInfty;
    private MappedByteBuffer codeSize;
    private MappedByteBuffer codesizeReached;
    private MappedByteBuffer counter;
    private MappedByteBuffer counterMax;
    private MappedByteBuffer counterPush;
    private MappedByteBuffer index;
    private MappedByteBuffer isPush;
    private MappedByteBuffer isPushData;
    private MappedByteBuffer limb;
    private MappedByteBuffer nBytes;
    private MappedByteBuffer nBytesAcc;
    private MappedByteBuffer opcode;
    private MappedByteBuffer paddedBytecodeByte;
    private MappedByteBuffer programmeCounter;
    private MappedByteBuffer pushFunnelBit;
    private MappedByteBuffer pushParameter;
    private MappedByteBuffer pushValueAcc;
    private MappedByteBuffer pushValueHigh;
    private MappedByteBuffer pushValueLow;
    private MappedByteBuffer validJumpDestination;

    private TraceBuilder(int length) {}

    public int size() {
      if (!filled.isEmpty()) {
        throw new RuntimeException("Cannot measure a trace with a non-validated row.");
      }

      return this.currentLine;
    }

    public void setBuffers(List<MappedByteBuffer> buffers) {
      this.acc = buffers.get(0);
      this.codeFragmentIndex = buffers.get(1);
      this.codeFragmentIndexInfty = buffers.get(2);
      this.codeSize = buffers.get(3);
      this.codesizeReached = buffers.get(4);
      this.counter = buffers.get(5);
      this.counterMax = buffers.get(6);
      this.counterPush = buffers.get(7);
      this.index = buffers.get(8);
      this.isPush = buffers.get(9);
      this.isPushData = buffers.get(10);
      this.limb = buffers.get(11);
      this.nBytes = buffers.get(12);
      this.nBytesAcc = buffers.get(13);
      this.opcode = buffers.get(14);
      this.paddedBytecodeByte = buffers.get(15);
      this.programmeCounter = buffers.get(16);
      this.pushFunnelBit = buffers.get(17);
      this.pushParameter = buffers.get(18);
      this.pushValueAcc = buffers.get(19);
      this.pushValueHigh = buffers.get(20);
      this.pushValueLow = buffers.get(21);
      this.validJumpDestination = buffers.get(22);
    }

    public void releaseBuffers() {
      this.acc.force();
      this.codeFragmentIndex.force();
      this.codeFragmentIndexInfty.force();
      this.codeSize.force();
      this.codesizeReached.force();
      this.counter.force();
      this.counterMax.force();
      this.counterPush.force();
      this.index.force();
      this.isPush.force();
      this.isPushData.force();
      this.limb.force();
      this.nBytes.force();
      this.nBytesAcc.force();
      this.opcode.force();
      this.paddedBytecodeByte.force();
      this.programmeCounter.force();
      this.pushFunnelBit.force();
      this.pushParameter.force();
      this.pushValueAcc.force();
      this.pushValueHigh.force();
      this.pushValueLow.force();
      this.validJumpDestination.force();
    }

    public TraceBuilder acc(final BigInteger b) {
      if (filled.get(0)) {
        throw new IllegalStateException("rom.ACC already set");
      } else {
        filled.set(0);
      }

      acc.put(b.toByteArray());

      return this;
    }

    public TraceBuilder codeFragmentIndex(final BigInteger b) {
      if (filled.get(2)) {
        throw new IllegalStateException("rom.CODE_FRAGMENT_INDEX already set");
      } else {
        filled.set(2);
      }

      codeFragmentIndex.put(b.toByteArray());

      return this;
    }

    public TraceBuilder codeFragmentIndexInfty(final BigInteger b) {
      if (filled.get(3)) {
        throw new IllegalStateException("rom.CODE_FRAGMENT_INDEX_INFTY already set");
      } else {
        filled.set(3);
      }

      codeFragmentIndexInfty.put(b.toByteArray());

      return this;
    }

    public TraceBuilder codeSize(final BigInteger b) {
      if (filled.get(4)) {
        throw new IllegalStateException("rom.CODE_SIZE already set");
      } else {
        filled.set(4);
      }

      codeSize.put(b.toByteArray());

      return this;
    }

    public TraceBuilder codesizeReached(final Boolean b) {
      if (filled.get(1)) {
        throw new IllegalStateException("rom.CODESIZE_REACHED already set");
      } else {
        filled.set(1);
      }

      codesizeReached.put((byte) (b ? 1 : 0));

      return this;
    }

    public TraceBuilder counter(final BigInteger b) {
      if (filled.get(5)) {
        throw new IllegalStateException("rom.COUNTER already set");
      } else {
        filled.set(5);
      }

      counter.put(b.toByteArray());

      return this;
    }

    public TraceBuilder counterMax(final BigInteger b) {
      if (filled.get(6)) {
        throw new IllegalStateException("rom.COUNTER_MAX already set");
      } else {
        filled.set(6);
      }

      counterMax.put(b.toByteArray());

      return this;
    }

    public TraceBuilder counterPush(final BigInteger b) {
      if (filled.get(7)) {
        throw new IllegalStateException("rom.COUNTER_PUSH already set");
      } else {
        filled.set(7);
      }

      counterPush.put(b.toByteArray());

      return this;
    }

    public TraceBuilder index(final BigInteger b) {
      if (filled.get(8)) {
        throw new IllegalStateException("rom.INDEX already set");
      } else {
        filled.set(8);
      }

      index.put(b.toByteArray());

      return this;
    }

    public TraceBuilder isPush(final Boolean b) {
      if (filled.get(9)) {
        throw new IllegalStateException("rom.IS_PUSH already set");
      } else {
        filled.set(9);
      }

      isPush.put((byte) (b ? 1 : 0));

      return this;
    }

    public TraceBuilder isPushData(final Boolean b) {
      if (filled.get(10)) {
        throw new IllegalStateException("rom.IS_PUSH_DATA already set");
      } else {
        filled.set(10);
      }

      isPushData.put((byte) (b ? 1 : 0));

      return this;
    }

    public TraceBuilder limb(final BigInteger b) {
      if (filled.get(11)) {
        throw new IllegalStateException("rom.LIMB already set");
      } else {
        filled.set(11);
      }

      limb.put(b.toByteArray());

      return this;
    }

    public TraceBuilder nBytes(final BigInteger b) {
      if (filled.get(21)) {
        throw new IllegalStateException("rom.nBYTES already set");
      } else {
        filled.set(21);
      }

      nBytes.put(b.toByteArray());

      return this;
    }

    public TraceBuilder nBytesAcc(final BigInteger b) {
      if (filled.get(22)) {
        throw new IllegalStateException("rom.nBYTES_ACC already set");
      } else {
        filled.set(22);
      }

      nBytesAcc.put(b.toByteArray());

      return this;
    }

    public TraceBuilder opcode(final UnsignedByte b) {
      if (filled.get(12)) {
        throw new IllegalStateException("rom.OPCODE already set");
      } else {
        filled.set(12);
      }

      opcode.put(b.toByte());

      return this;
    }

    public TraceBuilder paddedBytecodeByte(final UnsignedByte b) {
      if (filled.get(13)) {
        throw new IllegalStateException("rom.PADDED_BYTECODE_BYTE already set");
      } else {
        filled.set(13);
      }

      paddedBytecodeByte.put(b.toByte());

      return this;
    }

    public TraceBuilder programmeCounter(final BigInteger b) {
      if (filled.get(14)) {
        throw new IllegalStateException("rom.PROGRAMME_COUNTER already set");
      } else {
        filled.set(14);
      }

      programmeCounter.put(b.toByteArray());

      return this;
    }

    public TraceBuilder pushFunnelBit(final Boolean b) {
      if (filled.get(15)) {
        throw new IllegalStateException("rom.PUSH_FUNNEL_BIT already set");
      } else {
        filled.set(15);
      }

      pushFunnelBit.put((byte) (b ? 1 : 0));

      return this;
    }

    public TraceBuilder pushParameter(final BigInteger b) {
      if (filled.get(16)) {
        throw new IllegalStateException("rom.PUSH_PARAMETER already set");
      } else {
        filled.set(16);
      }

      pushParameter.put(b.toByteArray());

      return this;
    }

    public TraceBuilder pushValueAcc(final BigInteger b) {
      if (filled.get(17)) {
        throw new IllegalStateException("rom.PUSH_VALUE_ACC already set");
      } else {
        filled.set(17);
      }

      pushValueAcc.put(b.toByteArray());

      return this;
    }

    public TraceBuilder pushValueHigh(final BigInteger b) {
      if (filled.get(18)) {
        throw new IllegalStateException("rom.PUSH_VALUE_HIGH already set");
      } else {
        filled.set(18);
      }

      pushValueHigh.put(b.toByteArray());

      return this;
    }

    public TraceBuilder pushValueLow(final BigInteger b) {
      if (filled.get(19)) {
        throw new IllegalStateException("rom.PUSH_VALUE_LOW already set");
      } else {
        filled.set(19);
      }

      pushValueLow.put(b.toByteArray());

      return this;
    }

    public TraceBuilder validJumpDestination(final Boolean b) {
      if (filled.get(20)) {
        throw new IllegalStateException("rom.VALID_JUMP_DESTINATION already set");
      } else {
        filled.set(20);
      }

      validJumpDestination.put((byte) (b ? 1 : 0));

      return this;
    }

    public TraceBuilder validateRow() {
      if (!filled.get(0)) {
        throw new IllegalStateException("rom.ACC has not been filled");
      }

      if (!filled.get(2)) {
        throw new IllegalStateException("rom.CODE_FRAGMENT_INDEX has not been filled");
      }

      if (!filled.get(3)) {
        throw new IllegalStateException("rom.CODE_FRAGMENT_INDEX_INFTY has not been filled");
      }

      if (!filled.get(4)) {
        throw new IllegalStateException("rom.CODE_SIZE has not been filled");
      }

      if (!filled.get(1)) {
        throw new IllegalStateException("rom.CODESIZE_REACHED has not been filled");
      }

      if (!filled.get(5)) {
        throw new IllegalStateException("rom.COUNTER has not been filled");
      }

      if (!filled.get(6)) {
        throw new IllegalStateException("rom.COUNTER_MAX has not been filled");
      }

      if (!filled.get(7)) {
        throw new IllegalStateException("rom.COUNTER_PUSH has not been filled");
      }

      if (!filled.get(8)) {
        throw new IllegalStateException("rom.INDEX has not been filled");
      }

      if (!filled.get(9)) {
        throw new IllegalStateException("rom.IS_PUSH has not been filled");
      }

      if (!filled.get(10)) {
        throw new IllegalStateException("rom.IS_PUSH_DATA has not been filled");
      }

      if (!filled.get(11)) {
        throw new IllegalStateException("rom.LIMB has not been filled");
      }

      if (!filled.get(21)) {
        throw new IllegalStateException("rom.nBYTES has not been filled");
      }

      if (!filled.get(22)) {
        throw new IllegalStateException("rom.nBYTES_ACC has not been filled");
      }

      if (!filled.get(12)) {
        throw new IllegalStateException("rom.OPCODE has not been filled");
      }

      if (!filled.get(13)) {
        throw new IllegalStateException("rom.PADDED_BYTECODE_BYTE has not been filled");
      }

      if (!filled.get(14)) {
        throw new IllegalStateException("rom.PROGRAMME_COUNTER has not been filled");
      }

      if (!filled.get(15)) {
        throw new IllegalStateException("rom.PUSH_FUNNEL_BIT has not been filled");
      }

      if (!filled.get(16)) {
        throw new IllegalStateException("rom.PUSH_PARAMETER has not been filled");
      }

      if (!filled.get(17)) {
        throw new IllegalStateException("rom.PUSH_VALUE_ACC has not been filled");
      }

      if (!filled.get(18)) {
        throw new IllegalStateException("rom.PUSH_VALUE_HIGH has not been filled");
      }

      if (!filled.get(19)) {
        throw new IllegalStateException("rom.PUSH_VALUE_LOW has not been filled");
      }

      if (!filled.get(20)) {
        throw new IllegalStateException("rom.VALID_JUMP_DESTINATION has not been filled");
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
