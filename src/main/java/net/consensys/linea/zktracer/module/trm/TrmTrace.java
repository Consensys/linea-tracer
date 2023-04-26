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
package net.consensys.linea.zktracer.module.trm;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import net.consensys.linea.zktracer.bytes.UnsignedByte;

@JsonPropertyOrder({"Trace", "Stamp"})
@SuppressWarnings("unused")
public record TrmTrace(
    @JsonProperty("Trace") TrmTrace.Trace trace, @JsonProperty("Stamp") int stamp) {
  @JsonPropertyOrder({
    "ACC_HI", // ACC_HI
    "ACC_LO", // ACC_LO
    "ADDR_HI",
    "ADDR_LO",
    "TRM_ADDR_HI",
    "BYTE_HI", // BYTE_HI
    "BYTE_LO", // BYTE_LO
    "CT",
    "IS_PREC",
    "PBIT", // BINARY PLATEAU CONSTRAINT
    "ACC_T", // ACC_T
    "ONES", // [[1]]
    "TRM_STAMP"
  })
  @SuppressWarnings("unused")
  public record Trace(
      @JsonProperty("ACC_HI") List<BigInteger> ACC_HI,
      @JsonProperty("ACC_LO") List<BigInteger> ACC_LO,
      @JsonProperty("ADDR_HI") List<BigInteger> ADDR_HI,
      @JsonProperty("ADDR_LO") List<BigInteger> ADDR_LO,
      @JsonProperty("TRM_ADDR_HI") List<BigInteger> TRM_ADDR_HI,
      @JsonProperty("BYTE_HI") List<UnsignedByte> BYTE_HI,
      @JsonProperty("BYTE_LO") List<UnsignedByte> BYTE_LO,
      @JsonProperty("CT") List<Integer> COUNTER,
      @JsonProperty("IS_PREC") List<Boolean> IS_PREC,
      @JsonProperty("PBIT") List<Boolean> PBIT,
      @JsonProperty("ACC_T") List<BigInteger> ACC_T,
      @JsonProperty("ONES") List<Boolean> ONES,
      @JsonProperty("TRM_STAMP") List<Integer> TRM_STAMP) {

    public static class Builder {
      private final List<BigInteger> acc1 = new ArrayList<>();
      private final List<BigInteger> acc2 = new ArrayList<>();
      private final List<BigInteger> arg1Hi = new ArrayList<>();
      private final List<BigInteger> arg1Lo = new ArrayList<>();
      private final List<BigInteger> trmAddr = new ArrayList<>();
      private final List<UnsignedByte> byteHi = new ArrayList<>();
      private final List<UnsignedByte> byteLo = new ArrayList<>();
      private final List<Integer> counter = new ArrayList<>();
      private final List<Boolean> isPrec = new ArrayList<>();
      private final List<Boolean> pbit = new ArrayList<>();
      private final List<BigInteger> accT = new ArrayList<>();
      private final List<Boolean> ones = new ArrayList<>();
      private final List<Integer> trmStamp = new ArrayList<>();
      private int stamp = 0;

      private Builder() {}

      public static TrmTrace.Trace.Builder newInstance() {
        return new TrmTrace.Trace.Builder();
      }

      public TrmTrace.Trace.Builder appendAcc1(final BigInteger b) {
        acc1.add(b);
        return this;
      }

      public TrmTrace.Trace.Builder appendAcc2(final BigInteger b) {
        acc2.add(b);
        return this;
      }

      public TrmTrace.Trace.Builder appendArg1Hi(final BigInteger b) {
        arg1Hi.add(b);
        return this;
      }

      public TrmTrace.Trace.Builder appendArg1Lo(final BigInteger b) {
        arg1Lo.add(b);
        return this;
      }

      public TrmTrace.Trace.Builder appendTrmAddr(final BigInteger b) {
        trmAddr.add(b);
        return this;
      }

      public TrmTrace.Trace.Builder appendByte1(final UnsignedByte b) {
        byteHi.add(b);
        return this;
      }

      public TrmTrace.Trace.Builder appendByte2(final UnsignedByte b) {
        byteLo.add(b);
        return this;
      }

      public TrmTrace.Trace.Builder appendCounter(final Integer b) {
        counter.add(b);
        return this;
      }

      public TrmTrace.Trace.Builder appendIsPrec(final Boolean b) {
        isPrec.add(b);
        return this;
      }

      public TrmTrace.Trace.Builder appendPbit(final Boolean b) {
        pbit.add(b);
        return this;
      }

      public TrmTrace.Trace.Builder appendAccT(final BigInteger b) {
        accT.add(b);
        return this;
      }

      public TrmTrace.Trace.Builder appendOnes(final Boolean b) {
        ones.add(b);
        return this;
      }

      public TrmTrace.Trace.Builder appendStamp(final Integer b) {
        trmStamp.add(b);
        return this;
      }

      public TrmTrace.Trace.Builder setStamp(final int stamp) {
        this.stamp = stamp;
        return this;
      }

      public TrmTrace build() {
        return new TrmTrace(
            new TrmTrace.Trace(
                acc1, acc2, arg1Hi, arg1Lo, trmAddr, byteHi, byteLo, counter, isPrec, pbit, accT,
                ones, trmStamp),
            stamp);
      }
    }
  }
}
