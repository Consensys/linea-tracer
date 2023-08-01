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

package net.consensys.linea.zktracer.module.alu.mod;

import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Singular;
import net.consensys.linea.zktracer.bytes.UnsignedByte;
import net.consensys.linea.zktracer.module.RowValidator;

@Builder
record Trace(
    @Singular("acc12") @JsonProperty("ACC_1_2") List<BigInteger> acc12,
    @Singular("acc13") @JsonProperty("ACC_1_3") List<BigInteger> acc13,
    @Singular("acc22") @JsonProperty("ACC_2_2") List<BigInteger> acc22,
    @Singular("acc23") @JsonProperty("ACC_2_3") List<BigInteger> acc23,
    @Singular("accB0") @JsonProperty("ACC_B_0") List<BigInteger> accB0,
    @Singular("accB1") @JsonProperty("ACC_B_1") List<BigInteger> accB1,
    @Singular("accB2") @JsonProperty("ACC_B_2") List<BigInteger> accB2,
    @Singular("accB3") @JsonProperty("ACC_B_3") List<BigInteger> accB3,
    @Singular("accDelta0") @JsonProperty("ACC_DELTA_0") List<BigInteger> accDelta0,
    @Singular("accDelta1") @JsonProperty("ACC_DELTA_1") List<BigInteger> accDelta1,
    @Singular("accDelta2") @JsonProperty("ACC_DELTA_2") List<BigInteger> accDelta2,
    @Singular("accDelta3") @JsonProperty("ACC_DELTA_3") List<BigInteger> accDelta3,
    @Singular("accH0") @JsonProperty("ACC_H_0") List<BigInteger> accH0,
    @Singular("accH1") @JsonProperty("ACC_H_1") List<BigInteger> accH1,
    @Singular("accH2") @JsonProperty("ACC_H_2") List<BigInteger> accH2,
    @Singular("accQ0") @JsonProperty("ACC_Q_0") List<BigInteger> accQ0,
    @Singular("accQ1") @JsonProperty("ACC_Q_1") List<BigInteger> accQ1,
    @Singular("accQ2") @JsonProperty("ACC_Q_2") List<BigInteger> accQ2,
    @Singular("accQ3") @JsonProperty("ACC_Q_3") List<BigInteger> accQ3,
    @Singular("accR0") @JsonProperty("ACC_R_0") List<BigInteger> accR0,
    @Singular("accR1") @JsonProperty("ACC_R_1") List<BigInteger> accR1,
    @Singular("accR2") @JsonProperty("ACC_R_2") List<BigInteger> accR2,
    @Singular("accR3") @JsonProperty("ACC_R_3") List<BigInteger> accR3,
    @Singular("arg1Hi") @JsonProperty("ARG_1_HI") List<BigInteger> arg1Hi,
    @Singular("arg1Lo") @JsonProperty("ARG_1_LO") List<BigInteger> arg1Lo,
    @Singular("arg2Hi") @JsonProperty("ARG_2_HI") List<BigInteger> arg2Hi,
    @Singular("arg2Lo") @JsonProperty("ARG_2_LO") List<BigInteger> arg2Lo,
    @Singular("byte12") @JsonProperty("BYTE_1_2") List<UnsignedByte> byte12,
    @Singular("byte13") @JsonProperty("BYTE_1_3") List<UnsignedByte> byte13,
    @Singular("byte22") @JsonProperty("BYTE_2_2") List<UnsignedByte> byte22,
    @Singular("byte23") @JsonProperty("BYTE_2_3") List<UnsignedByte> byte23,
    @Singular("byteB0") @JsonProperty("BYTE_B_0") List<UnsignedByte> byteB0,
    @Singular("byteB1") @JsonProperty("BYTE_B_1") List<UnsignedByte> byteB1,
    @Singular("byteB2") @JsonProperty("BYTE_B_2") List<UnsignedByte> byteB2,
    @Singular("byteB3") @JsonProperty("BYTE_B_3") List<UnsignedByte> byteB3,
    @Singular("byteDelta0") @JsonProperty("BYTE_DELTA_0") List<UnsignedByte> byteDelta0,
    @Singular("byteDelta1") @JsonProperty("BYTE_DELTA_1") List<UnsignedByte> byteDelta1,
    @Singular("byteDelta2") @JsonProperty("BYTE_DELTA_2") List<UnsignedByte> byteDelta2,
    @Singular("byteDelta3") @JsonProperty("BYTE_DELTA_3") List<UnsignedByte> byteDelta3,
    @Singular("byteH0") @JsonProperty("BYTE_H_0") List<UnsignedByte> byteH0,
    @Singular("byteH1") @JsonProperty("BYTE_H_1") List<UnsignedByte> byteH1,
    @Singular("byteH2") @JsonProperty("BYTE_H_2") List<UnsignedByte> byteH2,
    @Singular("byteQ0") @JsonProperty("BYTE_Q_0") List<UnsignedByte> byteQ0,
    @Singular("byteQ1") @JsonProperty("BYTE_Q_1") List<UnsignedByte> byteQ1,
    @Singular("byteQ2") @JsonProperty("BYTE_Q_2") List<UnsignedByte> byteQ2,
    @Singular("byteQ3") @JsonProperty("BYTE_Q_3") List<UnsignedByte> byteQ3,
    @Singular("byteR0") @JsonProperty("BYTE_R_0") List<UnsignedByte> byteR0,
    @Singular("byteR1") @JsonProperty("BYTE_R_1") List<UnsignedByte> byteR1,
    @Singular("byteR2") @JsonProperty("BYTE_R_2") List<UnsignedByte> byteR2,
    @Singular("byteR3") @JsonProperty("BYTE_R_3") List<UnsignedByte> byteR3,
    @Singular("cmp1") @JsonProperty("CMP_1") List<Boolean> cmp1,
    @Singular("cmp2") @JsonProperty("CMP_2") List<Boolean> cmp2,
    @Singular("ct") @JsonProperty("CT") List<BigInteger> ct,
    @Singular("decOutput") @JsonProperty("DEC_OUTPUT") List<Boolean> decOutput,
    @Singular("decSigned") @JsonProperty("DEC_SIGNED") List<Boolean> decSigned,
    @Singular("inst") @JsonProperty("INST") List<BigInteger> inst,
    @Singular("msb1") @JsonProperty("MSB_1") List<Boolean> msb1,
    @Singular("msb2") @JsonProperty("MSB_2") List<Boolean> msb2,
    @Singular("oli") @JsonProperty("OLI") List<Boolean> oli,
    @Singular("resHi") @JsonProperty("RES_HI") List<BigInteger> resHi,
    @Singular("resLo") @JsonProperty("RES_LO") List<BigInteger> resLo,
    @Singular("stamp") @JsonProperty("STAMP") List<BigInteger> stamp) {
  static class TraceBuilder {
    public void validateRow(int rowIndex) {
      RowValidator.validate(Trace.class, this.build(), rowIndex);
    }
  }
}
