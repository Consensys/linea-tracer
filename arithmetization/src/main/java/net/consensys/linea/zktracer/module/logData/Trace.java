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

package net.consensys.linea.zktracer.module.logData;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * WARNING: This code is generated automatically. Any modifications to this code may be overwritten
 * and could lead to unexpected behavior. Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
public record Trace(
    @JsonProperty("ABS_LOG_NUM") List<BigInteger> absLogNum,
    @JsonProperty("ABS_LOG_NUM_MAX") List<BigInteger> absLogNumMax,
    @JsonProperty("INDEX") List<BigInteger> index,
    @JsonProperty("LIMB") List<BigInteger> limb,
    @JsonProperty("LOGS_DATA") List<Boolean> logsData,
    @JsonProperty("SIZE_ACC") List<BigInteger> sizeAcc,
    @JsonProperty("SIZE_LIMB") List<BigInteger> sizeLimb,
    @JsonProperty("SIZE_TOTAL") List<BigInteger> sizeTotal) {
  static TraceBuilder builder(int length) {
    return new TraceBuilder(length);
  }

  public int size() {
    return this.absLogNum.size();
  }

  static class TraceBuilder {
    private final BitSet filled = new BitSet();

    @JsonProperty("ABS_LOG_NUM")
    private final List<BigInteger> absLogNum;

    @JsonProperty("ABS_LOG_NUM_MAX")
    private final List<BigInteger> absLogNumMax;

    @JsonProperty("INDEX")
    private final List<BigInteger> index;

    @JsonProperty("LIMB")
    private final List<BigInteger> limb;

    @JsonProperty("LOGS_DATA")
    private final List<Boolean> logsData;

    @JsonProperty("SIZE_ACC")
    private final List<BigInteger> sizeAcc;

    @JsonProperty("SIZE_LIMB")
    private final List<BigInteger> sizeLimb;

    @JsonProperty("SIZE_TOTAL")
    private final List<BigInteger> sizeTotal;

    private TraceBuilder(int length) {
      this.absLogNum = new ArrayList<>(length);
      this.absLogNumMax = new ArrayList<>(length);
      this.index = new ArrayList<>(length);
      this.limb = new ArrayList<>(length);
      this.logsData = new ArrayList<>(length);
      this.sizeAcc = new ArrayList<>(length);
      this.sizeLimb = new ArrayList<>(length);
      this.sizeTotal = new ArrayList<>(length);
    }

    public int size() {
      if (!filled.isEmpty()) {
        throw new RuntimeException("Cannot measure a trace with a non-validated row.");
      }

      return this.absLogNum.size();
    }

    public TraceBuilder absLogNum(final BigInteger b) {
      if (filled.get(0)) {
        throw new IllegalStateException("ABS_LOG_NUM already set");
      } else {
        filled.set(0);
      }

      absLogNum.add(b);

      return this;
    }

    public TraceBuilder absLogNumMax(final BigInteger b) {
      if (filled.get(1)) {
        throw new IllegalStateException("ABS_LOG_NUM_MAX already set");
      } else {
        filled.set(1);
      }

      absLogNumMax.add(b);

      return this;
    }

    public TraceBuilder index(final BigInteger b) {
      if (filled.get(2)) {
        throw new IllegalStateException("INDEX already set");
      } else {
        filled.set(2);
      }

      index.add(b);

      return this;
    }

    public TraceBuilder limb(final BigInteger b) {
      if (filled.get(3)) {
        throw new IllegalStateException("LIMB already set");
      } else {
        filled.set(3);
      }

      limb.add(b);

      return this;
    }

    public TraceBuilder logsData(final Boolean b) {
      if (filled.get(4)) {
        throw new IllegalStateException("LOGS_DATA already set");
      } else {
        filled.set(4);
      }

      logsData.add(b);

      return this;
    }

    public TraceBuilder sizeAcc(final BigInteger b) {
      if (filled.get(5)) {
        throw new IllegalStateException("SIZE_ACC already set");
      } else {
        filled.set(5);
      }

      sizeAcc.add(b);

      return this;
    }

    public TraceBuilder sizeLimb(final BigInteger b) {
      if (filled.get(6)) {
        throw new IllegalStateException("SIZE_LIMB already set");
      } else {
        filled.set(6);
      }

      sizeLimb.add(b);

      return this;
    }

    public TraceBuilder sizeTotal(final BigInteger b) {
      if (filled.get(7)) {
        throw new IllegalStateException("SIZE_TOTAL already set");
      } else {
        filled.set(7);
      }

      sizeTotal.add(b);

      return this;
    }

    public TraceBuilder validateRow() {
      if (!filled.get(0)) {
        throw new IllegalStateException("ABS_LOG_NUM has not been filled");
      }

      if (!filled.get(1)) {
        throw new IllegalStateException("ABS_LOG_NUM_MAX has not been filled");
      }

      if (!filled.get(2)) {
        throw new IllegalStateException("INDEX has not been filled");
      }

      if (!filled.get(3)) {
        throw new IllegalStateException("LIMB has not been filled");
      }

      if (!filled.get(4)) {
        throw new IllegalStateException("LOGS_DATA has not been filled");
      }

      if (!filled.get(5)) {
        throw new IllegalStateException("SIZE_ACC has not been filled");
      }

      if (!filled.get(6)) {
        throw new IllegalStateException("SIZE_LIMB has not been filled");
      }

      if (!filled.get(7)) {
        throw new IllegalStateException("SIZE_TOTAL has not been filled");
      }

      filled.clear();

      return this;
    }

    public TraceBuilder fillAndValidateRow() {
      if (!filled.get(0)) {
        absLogNum.add(BigInteger.ZERO);
        this.filled.set(0);
      }
      if (!filled.get(1)) {
        absLogNumMax.add(BigInteger.ZERO);
        this.filled.set(1);
      }
      if (!filled.get(2)) {
        index.add(BigInteger.ZERO);
        this.filled.set(2);
      }
      if (!filled.get(3)) {
        limb.add(BigInteger.ZERO);
        this.filled.set(3);
      }
      if (!filled.get(4)) {
        logsData.add(false);
        this.filled.set(4);
      }
      if (!filled.get(5)) {
        sizeAcc.add(BigInteger.ZERO);
        this.filled.set(5);
      }
      if (!filled.get(6)) {
        sizeLimb.add(BigInteger.ZERO);
        this.filled.set(6);
      }
      if (!filled.get(7)) {
        sizeTotal.add(BigInteger.ZERO);
        this.filled.set(7);
      }

      return this.validateRow();
    }

    public Trace build() {
      if (!filled.isEmpty()) {
        throw new IllegalStateException("Cannot build trace with a non-validated row.");
      }

      return new Trace(
          absLogNum, absLogNumMax, index, limb, logsData, sizeAcc, sizeLimb, sizeTotal);
    }
  }
}
