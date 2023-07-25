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

package net.consensys.linea.zktracer.module.stp;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * WARNING: This code is generated automatically. Any modifications to this code may be overwritten
 * and could lead to unexpected behavior. Please DO NOT ATTEMPT TO MODIFY this code directly.
 */
record Trace(
    @JsonProperty("ARG1_HI") List<BigInteger> arg1Hi,
    @JsonProperty("ARG1_LO") List<BigInteger> arg1Lo,
    @JsonProperty("ARG2_LO") List<BigInteger> arg2Lo,
    @JsonProperty("CCTV") List<Boolean> cctv,
    @JsonProperty("CT") List<BigInteger> ct,
    @JsonProperty("EXO_INST") List<BigInteger> exoInst,
    @JsonProperty("EXT_TYPE") List<Boolean> extType,
    @JsonProperty("GAS_ACTL") List<BigInteger> gasActl,
    @JsonProperty("GAS_COST") List<BigInteger> gasCost,
    @JsonProperty("GAS_HI") List<BigInteger> gasHi,
    @JsonProperty("GAS_LO") List<BigInteger> gasLo,
    @JsonProperty("GAS_STPD") List<BigInteger> gasStpd,
    @JsonProperty("INST") List<BigInteger> inst,
    @JsonProperty("MOD_FLAG") List<Boolean> modFlag,
    @JsonProperty("RES_LO") List<BigInteger> resLo,
    @JsonProperty("STAMP") List<BigInteger> stamp,
    @JsonProperty("VALUE_HI") List<BigInteger> valueHi,
    @JsonProperty("VALUE_LO") List<BigInteger> valueLo,
    @JsonProperty("WCP_FLAG") List<Boolean> wcpFlag,
    @JsonProperty("ZERO_CONST") List<BigInteger> zeroConst) {
  static TraceBuilder builder() {
    return new TraceBuilder();
  }

  static class TraceBuilder {
    private final BitSet filled = new BitSet();

    @JsonProperty("ARG1_HI")
    private final List<BigInteger> arg1Hi = new ArrayList<>();

    @JsonProperty("ARG1_LO")
    private final List<BigInteger> arg1Lo = new ArrayList<>();

    @JsonProperty("ARG2_LO")
    private final List<BigInteger> arg2Lo = new ArrayList<>();

    @JsonProperty("CCTV")
    private final List<Boolean> cctv = new ArrayList<>();

    @JsonProperty("CT")
    private final List<BigInteger> ct = new ArrayList<>();

    @JsonProperty("EXO_INST")
    private final List<BigInteger> exoInst = new ArrayList<>();

    @JsonProperty("EXT_TYPE")
    private final List<Boolean> extType = new ArrayList<>();

    @JsonProperty("GAS_ACTL")
    private final List<BigInteger> gasActl = new ArrayList<>();

    @JsonProperty("GAS_COST")
    private final List<BigInteger> gasCost = new ArrayList<>();

    @JsonProperty("GAS_HI")
    private final List<BigInteger> gasHi = new ArrayList<>();

    @JsonProperty("GAS_LO")
    private final List<BigInteger> gasLo = new ArrayList<>();

    @JsonProperty("GAS_STPD")
    private final List<BigInteger> gasStpd = new ArrayList<>();

    @JsonProperty("INST")
    private final List<BigInteger> inst = new ArrayList<>();

    @JsonProperty("MOD_FLAG")
    private final List<Boolean> modFlag = new ArrayList<>();

    @JsonProperty("RES_LO")
    private final List<BigInteger> resLo = new ArrayList<>();

    @JsonProperty("STAMP")
    private final List<BigInteger> stamp = new ArrayList<>();

    @JsonProperty("VALUE_HI")
    private final List<BigInteger> valueHi = new ArrayList<>();

    @JsonProperty("VALUE_LO")
    private final List<BigInteger> valueLo = new ArrayList<>();

    @JsonProperty("WCP_FLAG")
    private final List<Boolean> wcpFlag = new ArrayList<>();

    @JsonProperty("ZERO_CONST")
    private final List<BigInteger> zeroConst = new ArrayList<>();

    private TraceBuilder() {}

    TraceBuilder arg1Hi(final BigInteger b) {
      if (filled.get(13)) {
        throw new IllegalStateException("ARG1_HI already set");
      } else {
        filled.set(13);
      }

      arg1Hi.add(b);

      return this;
    }

    TraceBuilder arg1Lo(final BigInteger b) {
      if (filled.get(17)) {
        throw new IllegalStateException("ARG1_LO already set");
      } else {
        filled.set(17);
      }

      arg1Lo.add(b);

      return this;
    }

    TraceBuilder arg2Lo(final BigInteger b) {
      if (filled.get(15)) {
        throw new IllegalStateException("ARG2_LO already set");
      } else {
        filled.set(15);
      }

      arg2Lo.add(b);

      return this;
    }

    TraceBuilder cctv(final Boolean b) {
      if (filled.get(8)) {
        throw new IllegalStateException("CCTV already set");
      } else {
        filled.set(8);
      }

      cctv.add(b);

      return this;
    }

    TraceBuilder ct(final BigInteger b) {
      if (filled.get(6)) {
        throw new IllegalStateException("CT already set");
      } else {
        filled.set(6);
      }

      ct.add(b);

      return this;
    }

    TraceBuilder exoInst(final BigInteger b) {
      if (filled.get(12)) {
        throw new IllegalStateException("EXO_INST already set");
      } else {
        filled.set(12);
      }

      exoInst.add(b);

      return this;
    }

    TraceBuilder extType(final Boolean b) {
      if (filled.get(9)) {
        throw new IllegalStateException("EXT_TYPE already set");
      } else {
        filled.set(9);
      }

      extType.add(b);

      return this;
    }

    TraceBuilder gasActl(final BigInteger b) {
      if (filled.get(14)) {
        throw new IllegalStateException("GAS_ACTL already set");
      } else {
        filled.set(14);
      }

      gasActl.add(b);

      return this;
    }

    TraceBuilder gasCost(final BigInteger b) {
      if (filled.get(0)) {
        throw new IllegalStateException("GAS_COST already set");
      } else {
        filled.set(0);
      }

      gasCost.add(b);

      return this;
    }

    TraceBuilder gasHi(final BigInteger b) {
      if (filled.get(18)) {
        throw new IllegalStateException("GAS_HI already set");
      } else {
        filled.set(18);
      }

      gasHi.add(b);

      return this;
    }

    TraceBuilder gasLo(final BigInteger b) {
      if (filled.get(10)) {
        throw new IllegalStateException("GAS_LO already set");
      } else {
        filled.set(10);
      }

      gasLo.add(b);

      return this;
    }

    TraceBuilder gasStpd(final BigInteger b) {
      if (filled.get(4)) {
        throw new IllegalStateException("GAS_STPD already set");
      } else {
        filled.set(4);
      }

      gasStpd.add(b);

      return this;
    }

    TraceBuilder inst(final BigInteger b) {
      if (filled.get(3)) {
        throw new IllegalStateException("INST already set");
      } else {
        filled.set(3);
      }

      inst.add(b);

      return this;
    }

    TraceBuilder modFlag(final Boolean b) {
      if (filled.get(1)) {
        throw new IllegalStateException("MOD_FLAG already set");
      } else {
        filled.set(1);
      }

      modFlag.add(b);

      return this;
    }

    TraceBuilder resLo(final BigInteger b) {
      if (filled.get(2)) {
        throw new IllegalStateException("RES_LO already set");
      } else {
        filled.set(2);
      }

      resLo.add(b);

      return this;
    }

    TraceBuilder stamp(final BigInteger b) {
      if (filled.get(19)) {
        throw new IllegalStateException("STAMP already set");
      } else {
        filled.set(19);
      }

      stamp.add(b);

      return this;
    }

    TraceBuilder valueHi(final BigInteger b) {
      if (filled.get(11)) {
        throw new IllegalStateException("VALUE_HI already set");
      } else {
        filled.set(11);
      }

      valueHi.add(b);

      return this;
    }

    TraceBuilder valueLo(final BigInteger b) {
      if (filled.get(16)) {
        throw new IllegalStateException("VALUE_LO already set");
      } else {
        filled.set(16);
      }

      valueLo.add(b);

      return this;
    }

    TraceBuilder wcpFlag(final Boolean b) {
      if (filled.get(5)) {
        throw new IllegalStateException("WCP_FLAG already set");
      } else {
        filled.set(5);
      }

      wcpFlag.add(b);

      return this;
    }

    TraceBuilder zeroConst(final BigInteger b) {
      if (filled.get(7)) {
        throw new IllegalStateException("ZERO_CONST already set");
      } else {
        filled.set(7);
      }

      zeroConst.add(b);

      return this;
    }

    TraceBuilder setArg1HiAt(final BigInteger b, int i) {
      arg1Hi.set(i, b);

      return this;
    }

    TraceBuilder setArg1LoAt(final BigInteger b, int i) {
      arg1Lo.set(i, b);

      return this;
    }

    TraceBuilder setArg2LoAt(final BigInteger b, int i) {
      arg2Lo.set(i, b);

      return this;
    }

    TraceBuilder setCctvAt(final Boolean b, int i) {
      cctv.set(i, b);

      return this;
    }

    TraceBuilder setCtAt(final BigInteger b, int i) {
      ct.set(i, b);

      return this;
    }

    TraceBuilder setExoInstAt(final BigInteger b, int i) {
      exoInst.set(i, b);

      return this;
    }

    TraceBuilder setExtTypeAt(final Boolean b, int i) {
      extType.set(i, b);

      return this;
    }

    TraceBuilder setGasActlAt(final BigInteger b, int i) {
      gasActl.set(i, b);

      return this;
    }

    TraceBuilder setGasCostAt(final BigInteger b, int i) {
      gasCost.set(i, b);

      return this;
    }

    TraceBuilder setGasHiAt(final BigInteger b, int i) {
      gasHi.set(i, b);

      return this;
    }

    TraceBuilder setGasLoAt(final BigInteger b, int i) {
      gasLo.set(i, b);

      return this;
    }

    TraceBuilder setGasStpdAt(final BigInteger b, int i) {
      gasStpd.set(i, b);

      return this;
    }

    TraceBuilder setInstAt(final BigInteger b, int i) {
      inst.set(i, b);

      return this;
    }

    TraceBuilder setModFlagAt(final Boolean b, int i) {
      modFlag.set(i, b);

      return this;
    }

    TraceBuilder setResLoAt(final BigInteger b, int i) {
      resLo.set(i, b);

      return this;
    }

    TraceBuilder setStampAt(final BigInteger b, int i) {
      stamp.set(i, b);

      return this;
    }

    TraceBuilder setValueHiAt(final BigInteger b, int i) {
      valueHi.set(i, b);

      return this;
    }

    TraceBuilder setValueLoAt(final BigInteger b, int i) {
      valueLo.set(i, b);

      return this;
    }

    TraceBuilder setWcpFlagAt(final Boolean b, int i) {
      wcpFlag.set(i, b);

      return this;
    }

    TraceBuilder setZeroConstAt(final BigInteger b, int i) {
      zeroConst.set(i, b);

      return this;
    }

    TraceBuilder setArg1HiRelative(final BigInteger b, int i) {
      arg1Hi.set(arg1Hi.size() - 1 - i, b);

      return this;
    }

    TraceBuilder setArg1LoRelative(final BigInteger b, int i) {
      arg1Lo.set(arg1Lo.size() - 1 - i, b);

      return this;
    }

    TraceBuilder setArg2LoRelative(final BigInteger b, int i) {
      arg2Lo.set(arg2Lo.size() - 1 - i, b);

      return this;
    }

    TraceBuilder setCctvRelative(final Boolean b, int i) {
      cctv.set(cctv.size() - 1 - i, b);

      return this;
    }

    TraceBuilder setCtRelative(final BigInteger b, int i) {
      ct.set(ct.size() - 1 - i, b);

      return this;
    }

    TraceBuilder setExoInstRelative(final BigInteger b, int i) {
      exoInst.set(exoInst.size() - 1 - i, b);

      return this;
    }

    TraceBuilder setExtTypeRelative(final Boolean b, int i) {
      extType.set(extType.size() - 1 - i, b);

      return this;
    }

    TraceBuilder setGasActlRelative(final BigInteger b, int i) {
      gasActl.set(gasActl.size() - 1 - i, b);

      return this;
    }

    TraceBuilder setGasCostRelative(final BigInteger b, int i) {
      gasCost.set(gasCost.size() - 1 - i, b);

      return this;
    }

    TraceBuilder setGasHiRelative(final BigInteger b, int i) {
      gasHi.set(gasHi.size() - 1 - i, b);

      return this;
    }

    TraceBuilder setGasLoRelative(final BigInteger b, int i) {
      gasLo.set(gasLo.size() - 1 - i, b);

      return this;
    }

    TraceBuilder setGasStpdRelative(final BigInteger b, int i) {
      gasStpd.set(gasStpd.size() - 1 - i, b);

      return this;
    }

    TraceBuilder setInstRelative(final BigInteger b, int i) {
      inst.set(inst.size() - 1 - i, b);

      return this;
    }

    TraceBuilder setModFlagRelative(final Boolean b, int i) {
      modFlag.set(modFlag.size() - 1 - i, b);

      return this;
    }

    TraceBuilder setResLoRelative(final BigInteger b, int i) {
      resLo.set(resLo.size() - 1 - i, b);

      return this;
    }

    TraceBuilder setStampRelative(final BigInteger b, int i) {
      stamp.set(stamp.size() - 1 - i, b);

      return this;
    }

    TraceBuilder setValueHiRelative(final BigInteger b, int i) {
      valueHi.set(valueHi.size() - 1 - i, b);

      return this;
    }

    TraceBuilder setValueLoRelative(final BigInteger b, int i) {
      valueLo.set(valueLo.size() - 1 - i, b);

      return this;
    }

    TraceBuilder setWcpFlagRelative(final Boolean b, int i) {
      wcpFlag.set(wcpFlag.size() - 1 - i, b);

      return this;
    }

    TraceBuilder setZeroConstRelative(final BigInteger b, int i) {
      zeroConst.set(zeroConst.size() - 1 - i, b);

      return this;
    }

    TraceBuilder validateRow() {
      if (!filled.get(13)) {
        throw new IllegalStateException("ARG1_HI has not been filled");
      }

      if (!filled.get(17)) {
        throw new IllegalStateException("ARG1_LO has not been filled");
      }

      if (!filled.get(15)) {
        throw new IllegalStateException("ARG2_LO has not been filled");
      }

      if (!filled.get(8)) {
        throw new IllegalStateException("CCTV has not been filled");
      }

      if (!filled.get(6)) {
        throw new IllegalStateException("CT has not been filled");
      }

      if (!filled.get(12)) {
        throw new IllegalStateException("EXO_INST has not been filled");
      }

      if (!filled.get(9)) {
        throw new IllegalStateException("EXT_TYPE has not been filled");
      }

      if (!filled.get(14)) {
        throw new IllegalStateException("GAS_ACTL has not been filled");
      }

      if (!filled.get(0)) {
        throw new IllegalStateException("GAS_COST has not been filled");
      }

      if (!filled.get(18)) {
        throw new IllegalStateException("GAS_HI has not been filled");
      }

      if (!filled.get(10)) {
        throw new IllegalStateException("GAS_LO has not been filled");
      }

      if (!filled.get(4)) {
        throw new IllegalStateException("GAS_STPD has not been filled");
      }

      if (!filled.get(3)) {
        throw new IllegalStateException("INST has not been filled");
      }

      if (!filled.get(1)) {
        throw new IllegalStateException("MOD_FLAG has not been filled");
      }

      if (!filled.get(2)) {
        throw new IllegalStateException("RES_LO has not been filled");
      }

      if (!filled.get(19)) {
        throw new IllegalStateException("STAMP has not been filled");
      }

      if (!filled.get(11)) {
        throw new IllegalStateException("VALUE_HI has not been filled");
      }

      if (!filled.get(16)) {
        throw new IllegalStateException("VALUE_LO has not been filled");
      }

      if (!filled.get(5)) {
        throw new IllegalStateException("WCP_FLAG has not been filled");
      }

      if (!filled.get(7)) {
        throw new IllegalStateException("ZERO_CONST has not been filled");
      }

      filled.clear();

      return this;
    }

    public Trace build() {
      if (!filled.isEmpty()) {
        throw new IllegalStateException("Cannot build trace with a non-validated row.");
      }

      return new Trace(
          arg1Hi, arg1Lo, arg2Lo, cctv, ct, exoInst, extType, gasActl, gasCost, gasHi, gasLo,
          gasStpd, inst, modFlag, resLo, stamp, valueHi, valueLo, wcpFlag, zeroConst);
    }
  }
}
