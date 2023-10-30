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

package net.consensys.linea.zktracer.module.rom;

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
  @JsonProperty("ACC") List<BigInteger> acc,
  @JsonProperty("CODE_FRAGMENT_INDEX") List<BigInteger> codeFragmentIndex,
  @JsonProperty("CODE_FRAGMENT_INDEX_INFTY") List<BigInteger> codeFragmentIndexInfty,
  @JsonProperty("CODE_SIZE") List<BigInteger> codeSize,
  @JsonProperty("CODESIZE_REACHED") List<Boolean> codesizeReached,
  @JsonProperty("COUNTER") List<BigInteger> counter,
  @JsonProperty("COUNTER_MAX") List<BigInteger> counterMax,
  @JsonProperty("COUNTER_PUSH") List<BigInteger> counterPush,
  @JsonProperty("INDEX") List<BigInteger> index,
  @JsonProperty("IS_PUSH") List<Boolean> isPush,
  @JsonProperty("IS_PUSH_DATA") List<Boolean> isPushData,
  @JsonProperty("LIMB") List<BigInteger> limb,
  @JsonProperty("nBYTES") List<BigInteger> nBytes,
  @JsonProperty("nBYTES_ACC") List<BigInteger> nBytesAcc,
  @JsonProperty("OPCODE") List<UnsignedByte> opcode,
  @JsonProperty("PADDED_BYTECODE_BYTE") List<UnsignedByte> paddedBytecodeByte,
  @JsonProperty("PROGRAMME_COUNTER") List<BigInteger> programmeCounter,
  @JsonProperty("PUSH_FUNNEL_BIT") List<Boolean> pushFunnelBit,
  @JsonProperty("PUSH_PARAMETER") List<BigInteger> pushParameter,
  @JsonProperty("PUSH_VALUE_ACC") List<BigInteger> pushValueAcc,
  @JsonProperty("PUSH_VALUE_HIGH") List<BigInteger> pushValueHigh,
  @JsonProperty("PUSH_VALUE_LOW") List<BigInteger> pushValueLow,
  @JsonProperty("VALID_JUMP_DESTINATION") List<Boolean> validJumpDestination) { 
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
      new ColumnHeader("rom.VALID_JUMP_DESTINATION", 1, size)
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


    private void writeacc(final BigInteger b) {
      this.target.put(0 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writecodeFragmentIndex(final BigInteger b) {
      this.target.put(33 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writecodeFragmentIndexInfty(final BigInteger b) {
      this.target.put(65 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writecodeSize(final BigInteger b) {
      this.target.put(97 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writecodesizeReached(final Boolean b) {
      this.target.put(32 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writecounter(final BigInteger b) {
      this.target.put(129 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writecounterMax(final BigInteger b) {
      this.target.put(161 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writecounterPush(final BigInteger b) {
      this.target.put(193 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeindex(final BigInteger b) {
      this.target.put(225 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeisPush(final Boolean b) {
      this.target.put(257 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writeisPushData(final Boolean b) {
      this.target.put(258 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writelimb(final BigInteger b) {
      this.target.put(259 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writenBytes(final BigInteger b) {
      this.target.put(455 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writenBytesAcc(final BigInteger b) {
      this.target.put(487 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writeopcode(final UnsignedByte b) {
      this.target.put(291 * this.length + this.currentLength*1, b.toByte());
    }
    private void writepaddedBytecodeByte(final UnsignedByte b) {
      this.target.put(292 * this.length + this.currentLength*1, b.toByte());
    }
    private void writeprogrammeCounter(final BigInteger b) {
      this.target.put(293 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writepushFunnelBit(final Boolean b) {
      this.target.put(325 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }
    private void writepushParameter(final BigInteger b) {
      this.target.put(326 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writepushValueAcc(final BigInteger b) {
      this.target.put(358 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writepushValueHigh(final BigInteger b) {
      this.target.put(390 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writepushValueLow(final BigInteger b) {
      this.target.put(422 * this.length + this.currentLength*32, EWord.of(b).toArray());
    }
    private void writevalidJumpDestination(final Boolean b) {
      this.target.put(454 * this.length + this.currentLength*1, (byte) (b ? 1 : 0));
    }


    public BufferTraceWriter acc(final BigInteger b) {
      if (filled.get(0)) {
        throw new IllegalStateException("ACC already set");
      } else {
        filled.set(0);
      }

      this.writeacc(b);

      return this;
    }
    public BufferTraceWriter codeFragmentIndex(final BigInteger b) {
      if (filled.get(2)) {
        throw new IllegalStateException("CODE_FRAGMENT_INDEX already set");
      } else {
        filled.set(2);
      }

      this.writecodeFragmentIndex(b);

      return this;
    }
    public BufferTraceWriter codeFragmentIndexInfty(final BigInteger b) {
      if (filled.get(3)) {
        throw new IllegalStateException("CODE_FRAGMENT_INDEX_INFTY already set");
      } else {
        filled.set(3);
      }

      this.writecodeFragmentIndexInfty(b);

      return this;
    }
    public BufferTraceWriter codeSize(final BigInteger b) {
      if (filled.get(4)) {
        throw new IllegalStateException("CODE_SIZE already set");
      } else {
        filled.set(4);
      }

      this.writecodeSize(b);

      return this;
    }
    public BufferTraceWriter codesizeReached(final Boolean b) {
      if (filled.get(1)) {
        throw new IllegalStateException("CODESIZE_REACHED already set");
      } else {
        filled.set(1);
      }

      this.writecodesizeReached(b);

      return this;
    }
    public BufferTraceWriter counter(final BigInteger b) {
      if (filled.get(5)) {
        throw new IllegalStateException("COUNTER already set");
      } else {
        filled.set(5);
      }

      this.writecounter(b);

      return this;
    }
    public BufferTraceWriter counterMax(final BigInteger b) {
      if (filled.get(6)) {
        throw new IllegalStateException("COUNTER_MAX already set");
      } else {
        filled.set(6);
      }

      this.writecounterMax(b);

      return this;
    }
    public BufferTraceWriter counterPush(final BigInteger b) {
      if (filled.get(7)) {
        throw new IllegalStateException("COUNTER_PUSH already set");
      } else {
        filled.set(7);
      }

      this.writecounterPush(b);

      return this;
    }
    public BufferTraceWriter index(final BigInteger b) {
      if (filled.get(8)) {
        throw new IllegalStateException("INDEX already set");
      } else {
        filled.set(8);
      }

      this.writeindex(b);

      return this;
    }
    public BufferTraceWriter isPush(final Boolean b) {
      if (filled.get(9)) {
        throw new IllegalStateException("IS_PUSH already set");
      } else {
        filled.set(9);
      }

      this.writeisPush(b);

      return this;
    }
    public BufferTraceWriter isPushData(final Boolean b) {
      if (filled.get(10)) {
        throw new IllegalStateException("IS_PUSH_DATA already set");
      } else {
        filled.set(10);
      }

      this.writeisPushData(b);

      return this;
    }
    public BufferTraceWriter limb(final BigInteger b) {
      if (filled.get(11)) {
        throw new IllegalStateException("LIMB already set");
      } else {
        filled.set(11);
      }

      this.writelimb(b);

      return this;
    }
    public BufferTraceWriter nBytes(final BigInteger b) {
      if (filled.get(21)) {
        throw new IllegalStateException("nBYTES already set");
      } else {
        filled.set(21);
      }

      this.writenBytes(b);

      return this;
    }
    public BufferTraceWriter nBytesAcc(final BigInteger b) {
      if (filled.get(22)) {
        throw new IllegalStateException("nBYTES_ACC already set");
      } else {
        filled.set(22);
      }

      this.writenBytesAcc(b);

      return this;
    }
    public BufferTraceWriter opcode(final UnsignedByte b) {
      if (filled.get(12)) {
        throw new IllegalStateException("OPCODE already set");
      } else {
        filled.set(12);
      }

      this.writeopcode(b);

      return this;
    }
    public BufferTraceWriter paddedBytecodeByte(final UnsignedByte b) {
      if (filled.get(13)) {
        throw new IllegalStateException("PADDED_BYTECODE_BYTE already set");
      } else {
        filled.set(13);
      }

      this.writepaddedBytecodeByte(b);

      return this;
    }
    public BufferTraceWriter programmeCounter(final BigInteger b) {
      if (filled.get(14)) {
        throw new IllegalStateException("PROGRAMME_COUNTER already set");
      } else {
        filled.set(14);
      }

      this.writeprogrammeCounter(b);

      return this;
    }
    public BufferTraceWriter pushFunnelBit(final Boolean b) {
      if (filled.get(15)) {
        throw new IllegalStateException("PUSH_FUNNEL_BIT already set");
      } else {
        filled.set(15);
      }

      this.writepushFunnelBit(b);

      return this;
    }
    public BufferTraceWriter pushParameter(final BigInteger b) {
      if (filled.get(16)) {
        throw new IllegalStateException("PUSH_PARAMETER already set");
      } else {
        filled.set(16);
      }

      this.writepushParameter(b);

      return this;
    }
    public BufferTraceWriter pushValueAcc(final BigInteger b) {
      if (filled.get(17)) {
        throw new IllegalStateException("PUSH_VALUE_ACC already set");
      } else {
        filled.set(17);
      }

      this.writepushValueAcc(b);

      return this;
    }
    public BufferTraceWriter pushValueHigh(final BigInteger b) {
      if (filled.get(18)) {
        throw new IllegalStateException("PUSH_VALUE_HIGH already set");
      } else {
        filled.set(18);
      }

      this.writepushValueHigh(b);

      return this;
    }
    public BufferTraceWriter pushValueLow(final BigInteger b) {
      if (filled.get(19)) {
        throw new IllegalStateException("PUSH_VALUE_LOW already set");
      } else {
        filled.set(19);
      }

      this.writepushValueLow(b);

      return this;
    }
    public BufferTraceWriter validJumpDestination(final Boolean b) {
      if (filled.get(20)) {
        throw new IllegalStateException("VALID_JUMP_DESTINATION already set");
      } else {
        filled.set(20);
      }

      this.writevalidJumpDestination(b);

      return this;
    }

    public BufferTraceWriter validateRow() {
      if (!filled.get(0)) {
        throw new IllegalStateException("ACC has not been filled");
      }

      if (!filled.get(2)) {
        throw new IllegalStateException("CODE_FRAGMENT_INDEX has not been filled");
      }

      if (!filled.get(3)) {
        throw new IllegalStateException("CODE_FRAGMENT_INDEX_INFTY has not been filled");
      }

      if (!filled.get(4)) {
        throw new IllegalStateException("CODE_SIZE has not been filled");
      }

      if (!filled.get(1)) {
        throw new IllegalStateException("CODESIZE_REACHED has not been filled");
      }

      if (!filled.get(5)) {
        throw new IllegalStateException("COUNTER has not been filled");
      }

      if (!filled.get(6)) {
        throw new IllegalStateException("COUNTER_MAX has not been filled");
      }

      if (!filled.get(7)) {
        throw new IllegalStateException("COUNTER_PUSH has not been filled");
      }

      if (!filled.get(8)) {
        throw new IllegalStateException("INDEX has not been filled");
      }

      if (!filled.get(9)) {
        throw new IllegalStateException("IS_PUSH has not been filled");
      }

      if (!filled.get(10)) {
        throw new IllegalStateException("IS_PUSH_DATA has not been filled");
      }

      if (!filled.get(11)) {
        throw new IllegalStateException("LIMB has not been filled");
      }

      if (!filled.get(21)) {
        throw new IllegalStateException("nBYTES has not been filled");
      }

      if (!filled.get(22)) {
        throw new IllegalStateException("nBYTES_ACC has not been filled");
      }

      if (!filled.get(12)) {
        throw new IllegalStateException("OPCODE has not been filled");
      }

      if (!filled.get(13)) {
        throw new IllegalStateException("PADDED_BYTECODE_BYTE has not been filled");
      }

      if (!filled.get(14)) {
        throw new IllegalStateException("PROGRAMME_COUNTER has not been filled");
      }

      if (!filled.get(15)) {
        throw new IllegalStateException("PUSH_FUNNEL_BIT has not been filled");
      }

      if (!filled.get(16)) {
        throw new IllegalStateException("PUSH_PARAMETER has not been filled");
      }

      if (!filled.get(17)) {
        throw new IllegalStateException("PUSH_VALUE_ACC has not been filled");
      }

      if (!filled.get(18)) {
        throw new IllegalStateException("PUSH_VALUE_HIGH has not been filled");
      }

      if (!filled.get(19)) {
        throw new IllegalStateException("PUSH_VALUE_LOW has not been filled");
      }

      if (!filled.get(20)) {
        throw new IllegalStateException("VALID_JUMP_DESTINATION has not been filled");
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

    @JsonProperty("ACC")
    private final List<BigInteger> acc;
    @JsonProperty("CODE_FRAGMENT_INDEX")
    private final List<BigInteger> codeFragmentIndex;
    @JsonProperty("CODE_FRAGMENT_INDEX_INFTY")
    private final List<BigInteger> codeFragmentIndexInfty;
    @JsonProperty("CODE_SIZE")
    private final List<BigInteger> codeSize;
    @JsonProperty("CODESIZE_REACHED")
    private final List<Boolean> codesizeReached;
    @JsonProperty("COUNTER")
    private final List<BigInteger> counter;
    @JsonProperty("COUNTER_MAX")
    private final List<BigInteger> counterMax;
    @JsonProperty("COUNTER_PUSH")
    private final List<BigInteger> counterPush;
    @JsonProperty("INDEX")
    private final List<BigInteger> index;
    @JsonProperty("IS_PUSH")
    private final List<Boolean> isPush;
    @JsonProperty("IS_PUSH_DATA")
    private final List<Boolean> isPushData;
    @JsonProperty("LIMB")
    private final List<BigInteger> limb;
    @JsonProperty("nBYTES")
    private final List<BigInteger> nBytes;
    @JsonProperty("nBYTES_ACC")
    private final List<BigInteger> nBytesAcc;
    @JsonProperty("OPCODE")
    private final List<UnsignedByte> opcode;
    @JsonProperty("PADDED_BYTECODE_BYTE")
    private final List<UnsignedByte> paddedBytecodeByte;
    @JsonProperty("PROGRAMME_COUNTER")
    private final List<BigInteger> programmeCounter;
    @JsonProperty("PUSH_FUNNEL_BIT")
    private final List<Boolean> pushFunnelBit;
    @JsonProperty("PUSH_PARAMETER")
    private final List<BigInteger> pushParameter;
    @JsonProperty("PUSH_VALUE_ACC")
    private final List<BigInteger> pushValueAcc;
    @JsonProperty("PUSH_VALUE_HIGH")
    private final List<BigInteger> pushValueHigh;
    @JsonProperty("PUSH_VALUE_LOW")
    private final List<BigInteger> pushValueLow;
    @JsonProperty("VALID_JUMP_DESTINATION")
    private final List<Boolean> validJumpDestination;

    TraceBuilder(int length) {
      this.acc = new ArrayList<>(length);
      this.codeFragmentIndex = new ArrayList<>(length);
      this.codeFragmentIndexInfty = new ArrayList<>(length);
      this.codeSize = new ArrayList<>(length);
      this.codesizeReached = new ArrayList<>(length);
      this.counter = new ArrayList<>(length);
      this.counterMax = new ArrayList<>(length);
      this.counterPush = new ArrayList<>(length);
      this.index = new ArrayList<>(length);
      this.isPush = new ArrayList<>(length);
      this.isPushData = new ArrayList<>(length);
      this.limb = new ArrayList<>(length);
      this.nBytes = new ArrayList<>(length);
      this.nBytesAcc = new ArrayList<>(length);
      this.opcode = new ArrayList<>(length);
      this.paddedBytecodeByte = new ArrayList<>(length);
      this.programmeCounter = new ArrayList<>(length);
      this.pushFunnelBit = new ArrayList<>(length);
      this.pushParameter = new ArrayList<>(length);
      this.pushValueAcc = new ArrayList<>(length);
      this.pushValueHigh = new ArrayList<>(length);
      this.pushValueLow = new ArrayList<>(length);
      this.validJumpDestination = new ArrayList<>(length);
    }

    public int size() {
      if (!filled.isEmpty()) {
        throw new RuntimeException("Cannot measure a trace with a non-validated row.");
      }

      return this.acc.size();
    }

    public TraceBuilder acc(final BigInteger b) {
      if (filled.get(0)) {
        throw new IllegalStateException("ACC already set");
      } else {
        filled.set(0);
      }

      acc.add(b);

      return this;
    }

    public TraceBuilder codeFragmentIndex(final BigInteger b) {
      if (filled.get(2)) {
        throw new IllegalStateException("CODE_FRAGMENT_INDEX already set");
      } else {
        filled.set(2);
      }

      codeFragmentIndex.add(b);

      return this;
    }

    public TraceBuilder codeFragmentIndexInfty(final BigInteger b) {
      if (filled.get(3)) {
        throw new IllegalStateException("CODE_FRAGMENT_INDEX_INFTY already set");
      } else {
        filled.set(3);
      }

      codeFragmentIndexInfty.add(b);

      return this;
    }

    public TraceBuilder codeSize(final BigInteger b) {
      if (filled.get(4)) {
        throw new IllegalStateException("CODE_SIZE already set");
      } else {
        filled.set(4);
      }

      codeSize.add(b);

      return this;
    }

    public TraceBuilder codesizeReached(final Boolean b) {
      if (filled.get(1)) {
        throw new IllegalStateException("CODESIZE_REACHED already set");
      } else {
        filled.set(1);
      }

      codesizeReached.add(b);

      return this;
    }

    public TraceBuilder counter(final BigInteger b) {
      if (filled.get(5)) {
        throw new IllegalStateException("COUNTER already set");
      } else {
        filled.set(5);
      }

      counter.add(b);

      return this;
    }

    public TraceBuilder counterMax(final BigInteger b) {
      if (filled.get(6)) {
        throw new IllegalStateException("COUNTER_MAX already set");
      } else {
        filled.set(6);
      }

      counterMax.add(b);

      return this;
    }

    public TraceBuilder counterPush(final BigInteger b) {
      if (filled.get(7)) {
        throw new IllegalStateException("COUNTER_PUSH already set");
      } else {
        filled.set(7);
      }

      counterPush.add(b);

      return this;
    }

    public TraceBuilder index(final BigInteger b) {
      if (filled.get(8)) {
        throw new IllegalStateException("INDEX already set");
      } else {
        filled.set(8);
      }

      index.add(b);

      return this;
    }

    public TraceBuilder isPush(final Boolean b) {
      if (filled.get(9)) {
        throw new IllegalStateException("IS_PUSH already set");
      } else {
        filled.set(9);
      }

      isPush.add(b);

      return this;
    }

    public TraceBuilder isPushData(final Boolean b) {
      if (filled.get(10)) {
        throw new IllegalStateException("IS_PUSH_DATA already set");
      } else {
        filled.set(10);
      }

      isPushData.add(b);

      return this;
    }

    public TraceBuilder limb(final BigInteger b) {
      if (filled.get(11)) {
        throw new IllegalStateException("LIMB already set");
      } else {
        filled.set(11);
      }

      limb.add(b);

      return this;
    }

    public TraceBuilder nBytes(final BigInteger b) {
      if (filled.get(21)) {
        throw new IllegalStateException("nBYTES already set");
      } else {
        filled.set(21);
      }

      nBytes.add(b);

      return this;
    }

    public TraceBuilder nBytesAcc(final BigInteger b) {
      if (filled.get(22)) {
        throw new IllegalStateException("nBYTES_ACC already set");
      } else {
        filled.set(22);
      }

      nBytesAcc.add(b);

      return this;
    }

    public TraceBuilder opcode(final UnsignedByte b) {
      if (filled.get(12)) {
        throw new IllegalStateException("OPCODE already set");
      } else {
        filled.set(12);
      }

      opcode.add(b);

      return this;
    }

    public TraceBuilder paddedBytecodeByte(final UnsignedByte b) {
      if (filled.get(13)) {
        throw new IllegalStateException("PADDED_BYTECODE_BYTE already set");
      } else {
        filled.set(13);
      }

      paddedBytecodeByte.add(b);

      return this;
    }

    public TraceBuilder programmeCounter(final BigInteger b) {
      if (filled.get(14)) {
        throw new IllegalStateException("PROGRAMME_COUNTER already set");
      } else {
        filled.set(14);
      }

      programmeCounter.add(b);

      return this;
    }

    public TraceBuilder pushFunnelBit(final Boolean b) {
      if (filled.get(15)) {
        throw new IllegalStateException("PUSH_FUNNEL_BIT already set");
      } else {
        filled.set(15);
      }

      pushFunnelBit.add(b);

      return this;
    }

    public TraceBuilder pushParameter(final BigInteger b) {
      if (filled.get(16)) {
        throw new IllegalStateException("PUSH_PARAMETER already set");
      } else {
        filled.set(16);
      }

      pushParameter.add(b);

      return this;
    }

    public TraceBuilder pushValueAcc(final BigInteger b) {
      if (filled.get(17)) {
        throw new IllegalStateException("PUSH_VALUE_ACC already set");
      } else {
        filled.set(17);
      }

      pushValueAcc.add(b);

      return this;
    }

    public TraceBuilder pushValueHigh(final BigInteger b) {
      if (filled.get(18)) {
        throw new IllegalStateException("PUSH_VALUE_HIGH already set");
      } else {
        filled.set(18);
      }

      pushValueHigh.add(b);

      return this;
    }

    public TraceBuilder pushValueLow(final BigInteger b) {
      if (filled.get(19)) {
        throw new IllegalStateException("PUSH_VALUE_LOW already set");
      } else {
        filled.set(19);
      }

      pushValueLow.add(b);

      return this;
    }

    public TraceBuilder validJumpDestination(final Boolean b) {
      if (filled.get(20)) {
        throw new IllegalStateException("VALID_JUMP_DESTINATION already set");
      } else {
        filled.set(20);
      }

      validJumpDestination.add(b);

      return this;
    }

    public TraceBuilder validateRow() {
      if (!filled.get(0)) {
        throw new IllegalStateException("ACC has not been filled");
      }

      if (!filled.get(2)) {
        throw new IllegalStateException("CODE_FRAGMENT_INDEX has not been filled");
      }

      if (!filled.get(3)) {
        throw new IllegalStateException("CODE_FRAGMENT_INDEX_INFTY has not been filled");
      }

      if (!filled.get(4)) {
        throw new IllegalStateException("CODE_SIZE has not been filled");
      }

      if (!filled.get(1)) {
        throw new IllegalStateException("CODESIZE_REACHED has not been filled");
      }

      if (!filled.get(5)) {
        throw new IllegalStateException("COUNTER has not been filled");
      }

      if (!filled.get(6)) {
        throw new IllegalStateException("COUNTER_MAX has not been filled");
      }

      if (!filled.get(7)) {
        throw new IllegalStateException("COUNTER_PUSH has not been filled");
      }

      if (!filled.get(8)) {
        throw new IllegalStateException("INDEX has not been filled");
      }

      if (!filled.get(9)) {
        throw new IllegalStateException("IS_PUSH has not been filled");
      }

      if (!filled.get(10)) {
        throw new IllegalStateException("IS_PUSH_DATA has not been filled");
      }

      if (!filled.get(11)) {
        throw new IllegalStateException("LIMB has not been filled");
      }

      if (!filled.get(21)) {
        throw new IllegalStateException("nBYTES has not been filled");
      }

      if (!filled.get(22)) {
        throw new IllegalStateException("nBYTES_ACC has not been filled");
      }

      if (!filled.get(12)) {
        throw new IllegalStateException("OPCODE has not been filled");
      }

      if (!filled.get(13)) {
        throw new IllegalStateException("PADDED_BYTECODE_BYTE has not been filled");
      }

      if (!filled.get(14)) {
        throw new IllegalStateException("PROGRAMME_COUNTER has not been filled");
      }

      if (!filled.get(15)) {
        throw new IllegalStateException("PUSH_FUNNEL_BIT has not been filled");
      }

      if (!filled.get(16)) {
        throw new IllegalStateException("PUSH_PARAMETER has not been filled");
      }

      if (!filled.get(17)) {
        throw new IllegalStateException("PUSH_VALUE_ACC has not been filled");
      }

      if (!filled.get(18)) {
        throw new IllegalStateException("PUSH_VALUE_HIGH has not been filled");
      }

      if (!filled.get(19)) {
        throw new IllegalStateException("PUSH_VALUE_LOW has not been filled");
      }

      if (!filled.get(20)) {
        throw new IllegalStateException("VALID_JUMP_DESTINATION has not been filled");
      }


      filled.clear();

      return this;
    }

    public TraceBuilder fillAndValidateRow() {
      if (!filled.get(0)) {
          acc.add(BigInteger.ZERO);
          this.filled.set(0);
      }
      if (!filled.get(2)) {
          codeFragmentIndex.add(BigInteger.ZERO);
          this.filled.set(2);
      }
      if (!filled.get(3)) {
          codeFragmentIndexInfty.add(BigInteger.ZERO);
          this.filled.set(3);
      }
      if (!filled.get(4)) {
          codeSize.add(BigInteger.ZERO);
          this.filled.set(4);
      }
      if (!filled.get(1)) {
          codesizeReached.add(false);
          this.filled.set(1);
      }
      if (!filled.get(5)) {
          counter.add(BigInteger.ZERO);
          this.filled.set(5);
      }
      if (!filled.get(6)) {
          counterMax.add(BigInteger.ZERO);
          this.filled.set(6);
      }
      if (!filled.get(7)) {
          counterPush.add(BigInteger.ZERO);
          this.filled.set(7);
      }
      if (!filled.get(8)) {
          index.add(BigInteger.ZERO);
          this.filled.set(8);
      }
      if (!filled.get(9)) {
          isPush.add(false);
          this.filled.set(9);
      }
      if (!filled.get(10)) {
          isPushData.add(false);
          this.filled.set(10);
      }
      if (!filled.get(11)) {
          limb.add(BigInteger.ZERO);
          this.filled.set(11);
      }
      if (!filled.get(21)) {
          nBytes.add(BigInteger.ZERO);
          this.filled.set(21);
      }
      if (!filled.get(22)) {
          nBytesAcc.add(BigInteger.ZERO);
          this.filled.set(22);
      }
      if (!filled.get(12)) {
          opcode.add(UnsignedByte.of(0));
          this.filled.set(12);
      }
      if (!filled.get(13)) {
          paddedBytecodeByte.add(UnsignedByte.of(0));
          this.filled.set(13);
      }
      if (!filled.get(14)) {
          programmeCounter.add(BigInteger.ZERO);
          this.filled.set(14);
      }
      if (!filled.get(15)) {
          pushFunnelBit.add(false);
          this.filled.set(15);
      }
      if (!filled.get(16)) {
          pushParameter.add(BigInteger.ZERO);
          this.filled.set(16);
      }
      if (!filled.get(17)) {
          pushValueAcc.add(BigInteger.ZERO);
          this.filled.set(17);
      }
      if (!filled.get(18)) {
          pushValueHigh.add(BigInteger.ZERO);
          this.filled.set(18);
      }
      if (!filled.get(19)) {
          pushValueLow.add(BigInteger.ZERO);
          this.filled.set(19);
      }
      if (!filled.get(20)) {
          validJumpDestination.add(false);
          this.filled.set(20);
      }

      return this.validateRow();
    }

    public Trace build() {
      if (!filled.isEmpty()) {
        throw new IllegalStateException("Cannot build trace with a non-validated row.");
      }

      return new Trace(
        acc,
        codeFragmentIndex,
        codeFragmentIndexInfty,
        codeSize,
        codesizeReached,
        counter,
        counterMax,
        counterPush,
        index,
        isPush,
        isPushData,
        limb,
        nBytes,
        nBytesAcc,
        opcode,
        paddedBytecodeByte,
        programmeCounter,
        pushFunnelBit,
        pushParameter,
        pushValueAcc,
        pushValueHigh,
        pushValueLow,
        validJumpDestination);
    }
  }
}
