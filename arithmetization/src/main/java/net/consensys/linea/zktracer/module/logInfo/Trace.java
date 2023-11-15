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

package net.consensys.linea.zktracer.module.logInfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.consensys.linea.zktracer.types.UnsignedByte;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * WARNING: This code is generated automatically.
 * Any modifications to this code may be overwritten and could lead to unexpected behavior.
 * Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
public record Trace(
  @JsonProperty("ABS_LOG_NUM") List<BigInteger> absLogNum,
  @JsonProperty("ABS_LOG_NUM_MAX") List<BigInteger> absLogNumMax,
  @JsonProperty("ABS_TXN_NUM") List<BigInteger> absTxnNum,
  @JsonProperty("ABS_TXN_NUM_MAX") List<BigInteger> absTxnNumMax,
  @JsonProperty("ADDR_HI") List<BigInteger> addrHi,
  @JsonProperty("ADDR_LO") List<BigInteger> addrLo,
  @JsonProperty("CT") List<BigInteger> ct,
  @JsonProperty("CT_MAX") List<BigInteger> ctMax,
  @JsonProperty("DATA_HI") List<BigInteger> dataHi,
  @JsonProperty("DATA_LO") List<BigInteger> dataLo,
  @JsonProperty("DATA_SIZE") List<BigInteger> dataSize,
  @JsonProperty("INST") List<BigInteger> inst,
  @JsonProperty("IS_LOG_X_0") List<Boolean> isLogX0,
  @JsonProperty("IS_LOG_X_1") List<Boolean> isLogX1,
  @JsonProperty("IS_LOG_X_2") List<Boolean> isLogX2,
  @JsonProperty("IS_LOG_X_3") List<Boolean> isLogX3,
  @JsonProperty("IS_LOG_X_4") List<Boolean> isLogX4,
  @JsonProperty("PHASE") List<BigInteger> phase,
  @JsonProperty("TOPIC_HI_1") List<BigInteger> topicHi1,
  @JsonProperty("TOPIC_HI_2") List<BigInteger> topicHi2,
  @JsonProperty("TOPIC_HI_3") List<BigInteger> topicHi3,
  @JsonProperty("TOPIC_HI_4") List<BigInteger> topicHi4,
  @JsonProperty("TOPIC_LO_1") List<BigInteger> topicLo1,
  @JsonProperty("TOPIC_LO_2") List<BigInteger> topicLo2,
  @JsonProperty("TOPIC_LO_3") List<BigInteger> topicLo3,
  @JsonProperty("TOPIC_LO_4") List<BigInteger> topicLo4,
  @JsonProperty("TXN_EMITS_LOGS") List<Boolean> txnEmitsLogs) { 
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
    @JsonProperty("ABS_TXN_NUM")
    private final List<BigInteger> absTxnNum;
    @JsonProperty("ABS_TXN_NUM_MAX")
    private final List<BigInteger> absTxnNumMax;
    @JsonProperty("ADDR_HI")
    private final List<BigInteger> addrHi;
    @JsonProperty("ADDR_LO")
    private final List<BigInteger> addrLo;
    @JsonProperty("CT")
    private final List<BigInteger> ct;
    @JsonProperty("CT_MAX")
    private final List<BigInteger> ctMax;
    @JsonProperty("DATA_HI")
    private final List<BigInteger> dataHi;
    @JsonProperty("DATA_LO")
    private final List<BigInteger> dataLo;
    @JsonProperty("DATA_SIZE")
    private final List<BigInteger> dataSize;
    @JsonProperty("INST")
    private final List<BigInteger> inst;
    @JsonProperty("IS_LOG_X_0")
    private final List<Boolean> isLogX0;
    @JsonProperty("IS_LOG_X_1")
    private final List<Boolean> isLogX1;
    @JsonProperty("IS_LOG_X_2")
    private final List<Boolean> isLogX2;
    @JsonProperty("IS_LOG_X_3")
    private final List<Boolean> isLogX3;
    @JsonProperty("IS_LOG_X_4")
    private final List<Boolean> isLogX4;
    @JsonProperty("PHASE")
    private final List<BigInteger> phase;
    @JsonProperty("TOPIC_HI_1")
    private final List<BigInteger> topicHi1;
    @JsonProperty("TOPIC_HI_2")
    private final List<BigInteger> topicHi2;
    @JsonProperty("TOPIC_HI_3")
    private final List<BigInteger> topicHi3;
    @JsonProperty("TOPIC_HI_4")
    private final List<BigInteger> topicHi4;
    @JsonProperty("TOPIC_LO_1")
    private final List<BigInteger> topicLo1;
    @JsonProperty("TOPIC_LO_2")
    private final List<BigInteger> topicLo2;
    @JsonProperty("TOPIC_LO_3")
    private final List<BigInteger> topicLo3;
    @JsonProperty("TOPIC_LO_4")
    private final List<BigInteger> topicLo4;
    @JsonProperty("TXN_EMITS_LOGS")
    private final List<Boolean> txnEmitsLogs;

    private TraceBuilder(int length) {
      this.absLogNum = new ArrayList<>(length);
      this.absLogNumMax = new ArrayList<>(length);
      this.absTxnNum = new ArrayList<>(length);
      this.absTxnNumMax = new ArrayList<>(length);
      this.addrHi = new ArrayList<>(length);
      this.addrLo = new ArrayList<>(length);
      this.ct = new ArrayList<>(length);
      this.ctMax = new ArrayList<>(length);
      this.dataHi = new ArrayList<>(length);
      this.dataLo = new ArrayList<>(length);
      this.dataSize = new ArrayList<>(length);
      this.inst = new ArrayList<>(length);
      this.isLogX0 = new ArrayList<>(length);
      this.isLogX1 = new ArrayList<>(length);
      this.isLogX2 = new ArrayList<>(length);
      this.isLogX3 = new ArrayList<>(length);
      this.isLogX4 = new ArrayList<>(length);
      this.phase = new ArrayList<>(length);
      this.topicHi1 = new ArrayList<>(length);
      this.topicHi2 = new ArrayList<>(length);
      this.topicHi3 = new ArrayList<>(length);
      this.topicHi4 = new ArrayList<>(length);
      this.topicLo1 = new ArrayList<>(length);
      this.topicLo2 = new ArrayList<>(length);
      this.topicLo3 = new ArrayList<>(length);
      this.topicLo4 = new ArrayList<>(length);
      this.txnEmitsLogs = new ArrayList<>(length);
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

    public TraceBuilder absTxnNum(final BigInteger b) {
      if (filled.get(2)) {
        throw new IllegalStateException("ABS_TXN_NUM already set");
      } else {
        filled.set(2);
      }

      absTxnNum.add(b);

      return this;
    }

    public TraceBuilder absTxnNumMax(final BigInteger b) {
      if (filled.get(3)) {
        throw new IllegalStateException("ABS_TXN_NUM_MAX already set");
      } else {
        filled.set(3);
      }

      absTxnNumMax.add(b);

      return this;
    }

    public TraceBuilder addrHi(final BigInteger b) {
      if (filled.get(4)) {
        throw new IllegalStateException("ADDR_HI already set");
      } else {
        filled.set(4);
      }

      addrHi.add(b);

      return this;
    }

    public TraceBuilder addrLo(final BigInteger b) {
      if (filled.get(5)) {
        throw new IllegalStateException("ADDR_LO already set");
      } else {
        filled.set(5);
      }

      addrLo.add(b);

      return this;
    }

    public TraceBuilder ct(final BigInteger b) {
      if (filled.get(6)) {
        throw new IllegalStateException("CT already set");
      } else {
        filled.set(6);
      }

      ct.add(b);

      return this;
    }

    public TraceBuilder ctMax(final BigInteger b) {
      if (filled.get(7)) {
        throw new IllegalStateException("CT_MAX already set");
      } else {
        filled.set(7);
      }

      ctMax.add(b);

      return this;
    }

    public TraceBuilder dataHi(final BigInteger b) {
      if (filled.get(8)) {
        throw new IllegalStateException("DATA_HI already set");
      } else {
        filled.set(8);
      }

      dataHi.add(b);

      return this;
    }

    public TraceBuilder dataLo(final BigInteger b) {
      if (filled.get(9)) {
        throw new IllegalStateException("DATA_LO already set");
      } else {
        filled.set(9);
      }

      dataLo.add(b);

      return this;
    }

    public TraceBuilder dataSize(final BigInteger b) {
      if (filled.get(10)) {
        throw new IllegalStateException("DATA_SIZE already set");
      } else {
        filled.set(10);
      }

      dataSize.add(b);

      return this;
    }

    public TraceBuilder inst(final BigInteger b) {
      if (filled.get(11)) {
        throw new IllegalStateException("INST already set");
      } else {
        filled.set(11);
      }

      inst.add(b);

      return this;
    }

    public TraceBuilder isLogX0(final Boolean b) {
      if (filled.get(12)) {
        throw new IllegalStateException("IS_LOG_X_0 already set");
      } else {
        filled.set(12);
      }

      isLogX0.add(b);

      return this;
    }

    public TraceBuilder isLogX1(final Boolean b) {
      if (filled.get(13)) {
        throw new IllegalStateException("IS_LOG_X_1 already set");
      } else {
        filled.set(13);
      }

      isLogX1.add(b);

      return this;
    }

    public TraceBuilder isLogX2(final Boolean b) {
      if (filled.get(14)) {
        throw new IllegalStateException("IS_LOG_X_2 already set");
      } else {
        filled.set(14);
      }

      isLogX2.add(b);

      return this;
    }

    public TraceBuilder isLogX3(final Boolean b) {
      if (filled.get(15)) {
        throw new IllegalStateException("IS_LOG_X_3 already set");
      } else {
        filled.set(15);
      }

      isLogX3.add(b);

      return this;
    }

    public TraceBuilder isLogX4(final Boolean b) {
      if (filled.get(16)) {
        throw new IllegalStateException("IS_LOG_X_4 already set");
      } else {
        filled.set(16);
      }

      isLogX4.add(b);

      return this;
    }

    public TraceBuilder phase(final BigInteger b) {
      if (filled.get(17)) {
        throw new IllegalStateException("PHASE already set");
      } else {
        filled.set(17);
      }

      phase.add(b);

      return this;
    }

    public TraceBuilder topicHi1(final BigInteger b) {
      if (filled.get(18)) {
        throw new IllegalStateException("TOPIC_HI_1 already set");
      } else {
        filled.set(18);
      }

      topicHi1.add(b);

      return this;
    }

    public TraceBuilder topicHi2(final BigInteger b) {
      if (filled.get(19)) {
        throw new IllegalStateException("TOPIC_HI_2 already set");
      } else {
        filled.set(19);
      }

      topicHi2.add(b);

      return this;
    }

    public TraceBuilder topicHi3(final BigInteger b) {
      if (filled.get(20)) {
        throw new IllegalStateException("TOPIC_HI_3 already set");
      } else {
        filled.set(20);
      }

      topicHi3.add(b);

      return this;
    }

    public TraceBuilder topicHi4(final BigInteger b) {
      if (filled.get(21)) {
        throw new IllegalStateException("TOPIC_HI_4 already set");
      } else {
        filled.set(21);
      }

      topicHi4.add(b);

      return this;
    }

    public TraceBuilder topicLo1(final BigInteger b) {
      if (filled.get(22)) {
        throw new IllegalStateException("TOPIC_LO_1 already set");
      } else {
        filled.set(22);
      }

      topicLo1.add(b);

      return this;
    }

    public TraceBuilder topicLo2(final BigInteger b) {
      if (filled.get(23)) {
        throw new IllegalStateException("TOPIC_LO_2 already set");
      } else {
        filled.set(23);
      }

      topicLo2.add(b);

      return this;
    }

    public TraceBuilder topicLo3(final BigInteger b) {
      if (filled.get(24)) {
        throw new IllegalStateException("TOPIC_LO_3 already set");
      } else {
        filled.set(24);
      }

      topicLo3.add(b);

      return this;
    }

    public TraceBuilder topicLo4(final BigInteger b) {
      if (filled.get(25)) {
        throw new IllegalStateException("TOPIC_LO_4 already set");
      } else {
        filled.set(25);
      }

      topicLo4.add(b);

      return this;
    }

    public TraceBuilder txnEmitsLogs(final Boolean b) {
      if (filled.get(26)) {
        throw new IllegalStateException("TXN_EMITS_LOGS already set");
      } else {
        filled.set(26);
      }

      txnEmitsLogs.add(b);

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
        throw new IllegalStateException("ABS_TXN_NUM has not been filled");
      }

      if (!filled.get(3)) {
        throw new IllegalStateException("ABS_TXN_NUM_MAX has not been filled");
      }

      if (!filled.get(4)) {
        throw new IllegalStateException("ADDR_HI has not been filled");
      }

      if (!filled.get(5)) {
        throw new IllegalStateException("ADDR_LO has not been filled");
      }

      if (!filled.get(6)) {
        throw new IllegalStateException("CT has not been filled");
      }

      if (!filled.get(7)) {
        throw new IllegalStateException("CT_MAX has not been filled");
      }

      if (!filled.get(8)) {
        throw new IllegalStateException("DATA_HI has not been filled");
      }

      if (!filled.get(9)) {
        throw new IllegalStateException("DATA_LO has not been filled");
      }

      if (!filled.get(10)) {
        throw new IllegalStateException("DATA_SIZE has not been filled");
      }

      if (!filled.get(11)) {
        throw new IllegalStateException("INST has not been filled");
      }

      if (!filled.get(12)) {
        throw new IllegalStateException("IS_LOG_X_0 has not been filled");
      }

      if (!filled.get(13)) {
        throw new IllegalStateException("IS_LOG_X_1 has not been filled");
      }

      if (!filled.get(14)) {
        throw new IllegalStateException("IS_LOG_X_2 has not been filled");
      }

      if (!filled.get(15)) {
        throw new IllegalStateException("IS_LOG_X_3 has not been filled");
      }

      if (!filled.get(16)) {
        throw new IllegalStateException("IS_LOG_X_4 has not been filled");
      }

      if (!filled.get(17)) {
        throw new IllegalStateException("PHASE has not been filled");
      }

      if (!filled.get(18)) {
        throw new IllegalStateException("TOPIC_HI_1 has not been filled");
      }

      if (!filled.get(19)) {
        throw new IllegalStateException("TOPIC_HI_2 has not been filled");
      }

      if (!filled.get(20)) {
        throw new IllegalStateException("TOPIC_HI_3 has not been filled");
      }

      if (!filled.get(21)) {
        throw new IllegalStateException("TOPIC_HI_4 has not been filled");
      }

      if (!filled.get(22)) {
        throw new IllegalStateException("TOPIC_LO_1 has not been filled");
      }

      if (!filled.get(23)) {
        throw new IllegalStateException("TOPIC_LO_2 has not been filled");
      }

      if (!filled.get(24)) {
        throw new IllegalStateException("TOPIC_LO_3 has not been filled");
      }

      if (!filled.get(25)) {
        throw new IllegalStateException("TOPIC_LO_4 has not been filled");
      }

      if (!filled.get(26)) {
        throw new IllegalStateException("TXN_EMITS_LOGS has not been filled");
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
          absTxnNum.add(BigInteger.ZERO);
          this.filled.set(2);
      }
      if (!filled.get(3)) {
          absTxnNumMax.add(BigInteger.ZERO);
          this.filled.set(3);
      }
      if (!filled.get(4)) {
          addrHi.add(BigInteger.ZERO);
          this.filled.set(4);
      }
      if (!filled.get(5)) {
          addrLo.add(BigInteger.ZERO);
          this.filled.set(5);
      }
      if (!filled.get(6)) {
          ct.add(BigInteger.ZERO);
          this.filled.set(6);
      }
      if (!filled.get(7)) {
          ctMax.add(BigInteger.ZERO);
          this.filled.set(7);
      }
      if (!filled.get(8)) {
          dataHi.add(BigInteger.ZERO);
          this.filled.set(8);
      }
      if (!filled.get(9)) {
          dataLo.add(BigInteger.ZERO);
          this.filled.set(9);
      }
      if (!filled.get(10)) {
          dataSize.add(BigInteger.ZERO);
          this.filled.set(10);
      }
      if (!filled.get(11)) {
          inst.add(BigInteger.ZERO);
          this.filled.set(11);
      }
      if (!filled.get(12)) {
          isLogX0.add(false);
          this.filled.set(12);
      }
      if (!filled.get(13)) {
          isLogX1.add(false);
          this.filled.set(13);
      }
      if (!filled.get(14)) {
          isLogX2.add(false);
          this.filled.set(14);
      }
      if (!filled.get(15)) {
          isLogX3.add(false);
          this.filled.set(15);
      }
      if (!filled.get(16)) {
          isLogX4.add(false);
          this.filled.set(16);
      }
      if (!filled.get(17)) {
          phase.add(BigInteger.ZERO);
          this.filled.set(17);
      }
      if (!filled.get(18)) {
          topicHi1.add(BigInteger.ZERO);
          this.filled.set(18);
      }
      if (!filled.get(19)) {
          topicHi2.add(BigInteger.ZERO);
          this.filled.set(19);
      }
      if (!filled.get(20)) {
          topicHi3.add(BigInteger.ZERO);
          this.filled.set(20);
      }
      if (!filled.get(21)) {
          topicHi4.add(BigInteger.ZERO);
          this.filled.set(21);
      }
      if (!filled.get(22)) {
          topicLo1.add(BigInteger.ZERO);
          this.filled.set(22);
      }
      if (!filled.get(23)) {
          topicLo2.add(BigInteger.ZERO);
          this.filled.set(23);
      }
      if (!filled.get(24)) {
          topicLo3.add(BigInteger.ZERO);
          this.filled.set(24);
      }
      if (!filled.get(25)) {
          topicLo4.add(BigInteger.ZERO);
          this.filled.set(25);
      }
      if (!filled.get(26)) {
          txnEmitsLogs.add(false);
          this.filled.set(26);
      }

      return this.validateRow();
    }

    public Trace build() {
      if (!filled.isEmpty()) {
        throw new IllegalStateException("Cannot build trace with a non-validated row.");
      }

      return new Trace(
        absLogNum,
        absLogNumMax,
        absTxnNum,
        absTxnNumMax,
        addrHi,
        addrLo,
        ct,
        ctMax,
        dataHi,
        dataLo,
        dataSize,
        inst,
        isLogX0,
        isLogX1,
        isLogX2,
        isLogX3,
        isLogX4,
        phase,
        topicHi1,
        topicHi2,
        topicHi3,
        topicHi4,
        topicLo1,
        topicLo2,
        topicLo3,
        topicLo4,
        txnEmitsLogs);
    }
  }
}
