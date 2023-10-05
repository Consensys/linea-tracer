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
    @JsonProperty("ADDRESS_HI") List<BigInteger> addressHi,
    @JsonProperty("ADDRESS_LO") List<BigInteger> addressLo,
    @JsonProperty("CODE_FRAGMENT_INDEX") List<BigInteger> codeFragmentIndex,
    @JsonProperty("CODESIZE") List<BigInteger> codesize,
    @JsonProperty("CODESIZE_REACHED") List<Boolean> codesizeReached,
    @JsonProperty("COUNTER") List<BigInteger> counter,
    @JsonProperty("LIMB") List<BigInteger> limb,
    @JsonProperty("PADDED_BYTECODE_BYTE") List<UnsignedByte> paddedBytecodeByte,
    @JsonProperty("PROGRAMME_COUNTER") List<BigInteger> programmeCounter) {
  static TraceBuilder builder() {
    return new TraceBuilder();
  }

  static class TraceBuilder {
    private final BitSet filled = new BitSet();

    @JsonProperty("ADDRESS_HI")
    private final List<BigInteger> addressHi = new ArrayList<>();

    @JsonProperty("ADDRESS_LO")
    private final List<BigInteger> addressLo = new ArrayList<>();

    @JsonProperty("CODE_FRAGMENT_INDEX")
    private final List<BigInteger> codeFragmentIndex = new ArrayList<>();

    @JsonProperty("CODESIZE")
    private final List<BigInteger> codesize = new ArrayList<>();

    @JsonProperty("CODESIZE_REACHED")
    private final List<Boolean> codesizeReached = new ArrayList<>();

    @JsonProperty("COUNTER")
    private final List<BigInteger> counter = new ArrayList<>();

    @JsonProperty("LIMB")
    private final List<BigInteger> limb = new ArrayList<>();

    @JsonProperty("PADDED_BYTECODE_BYTE")
    private final List<UnsignedByte> paddedBytecodeByte = new ArrayList<>();

    @JsonProperty("PROGRAMME_COUNTER")
    private final List<BigInteger> programmeCounter = new ArrayList<>();

    private TraceBuilder() {}

    public int size() {
      if (!filled.isEmpty()) {
        throw new RuntimeException("Cannot measure a trace with a non-validated row.");
      }

      return this.addressHi.size();
    }

    public TraceBuilder addressHi(final BigInteger b) {
      if (filled.get(0)) {
        throw new IllegalStateException("ADDRESS_HI already set");
      } else {
        filled.set(0);
      }

      addressHi.add(b);

      return this;
    }

    public TraceBuilder addressLo(final BigInteger b) {
      if (filled.get(1)) {
        throw new IllegalStateException("ADDRESS_LO already set");
      } else {
        filled.set(1);
      }

      addressLo.add(b);

      return this;
    }

    public TraceBuilder codeFragmentIndex(final BigInteger b) {
      if (filled.get(4)) {
        throw new IllegalStateException("CODE_FRAGMENT_INDEX already set");
      } else {
        filled.set(4);
      }

      codeFragmentIndex.add(b);

      return this;
    }

    public TraceBuilder codesize(final BigInteger b) {
      if (filled.get(2)) {
        throw new IllegalStateException("CODESIZE already set");
      } else {
        filled.set(2);
      }

      codesize.add(b);

      return this;
    }

    public TraceBuilder codesizeReached(final Boolean b) {
      if (filled.get(3)) {
        throw new IllegalStateException("CODESIZE_REACHED already set");
      } else {
        filled.set(3);
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

    public TraceBuilder limb(final BigInteger b) {
      if (filled.get(6)) {
        throw new IllegalStateException("LIMB already set");
      } else {
        filled.set(6);
      }

      limb.add(b);

      return this;
    }

    public TraceBuilder paddedBytecodeByte(final UnsignedByte b) {
      if (filled.get(7)) {
        throw new IllegalStateException("PADDED_BYTECODE_BYTE already set");
      } else {
        filled.set(7);
      }

      paddedBytecodeByte.add(b);

      return this;
    }

    public TraceBuilder programmeCounter(final BigInteger b) {
      if (filled.get(8)) {
        throw new IllegalStateException("PROGRAMME_COUNTER already set");
      } else {
        filled.set(8);
      }

      programmeCounter.add(b);

      return this;
    }

    public TraceBuilder validateRow() {
      if (!filled.get(0)) {
        throw new IllegalStateException("ADDRESS_HI has not been filled");
      }

      if (!filled.get(1)) {
        throw new IllegalStateException("ADDRESS_LO has not been filled");
      }

      if (!filled.get(4)) {
        throw new IllegalStateException("CODE_FRAGMENT_INDEX has not been filled");
      }

      if (!filled.get(2)) {
        throw new IllegalStateException("CODESIZE has not been filled");
      }

      if (!filled.get(3)) {
        throw new IllegalStateException("CODESIZE_REACHED has not been filled");
      }

      if (!filled.get(5)) {
        throw new IllegalStateException("COUNTER has not been filled");
      }

      if (!filled.get(6)) {
        throw new IllegalStateException("LIMB has not been filled");
      }

      if (!filled.get(7)) {
        throw new IllegalStateException("PADDED_BYTECODE_BYTE has not been filled");
      }

      if (!filled.get(8)) {
        throw new IllegalStateException("PROGRAMME_COUNTER has not been filled");
      }

      filled.clear();

      return this;
    }

    public TraceBuilder fillAndValidateRow() {
      if (!filled.get(0)) {
        addressHi.add(BigInteger.ZERO);
        this.filled.set(0);
      }
      if (!filled.get(1)) {
        addressLo.add(BigInteger.ZERO);
        this.filled.set(1);
      }
      if (!filled.get(4)) {
        codeFragmentIndex.add(BigInteger.ZERO);
        this.filled.set(4);
      }
      if (!filled.get(2)) {
        codesize.add(BigInteger.ZERO);
        this.filled.set(2);
      }
      if (!filled.get(3)) {
        codesizeReached.add(false);
        this.filled.set(3);
      }
      if (!filled.get(5)) {
        counter.add(BigInteger.ZERO);
        this.filled.set(5);
      }
      if (!filled.get(6)) {
        limb.add(BigInteger.ZERO);
        this.filled.set(6);
      }
      if (!filled.get(7)) {
        paddedBytecodeByte.add(UnsignedByte.of(0));
        this.filled.set(7);
      }
      if (!filled.get(8)) {
        programmeCounter.add(BigInteger.ZERO);
        this.filled.set(8);
      }

      return this.validateRow();
    }

    public Trace build() {
      if (!filled.isEmpty()) {
        throw new IllegalStateException("Cannot build trace with a non-validated row.");
      }

      return new Trace(
          addressHi,
          addressLo,
          codeFragmentIndex,
          codesize,
          codesizeReached,
          counter,
          limb,
          paddedBytecodeByte,
          programmeCounter);
    }
  }
}
