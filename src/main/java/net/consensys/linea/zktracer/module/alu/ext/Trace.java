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

package net.consensys.linea.zktracer.module.alu.ext;

import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Singular;
import net.consensys.linea.zktracer.bytes.UnsignedByte;
import net.consensys.linea.zktracer.module.RowValidator;

@Builder
record Trace(
    @Singular("accA0") @JsonProperty("ACC_A_0") List<BigInteger> accA0,
    @Singular("accA1") @JsonProperty("ACC_A_1") List<BigInteger> accA1,
    @Singular("accA2") @JsonProperty("ACC_A_2") List<BigInteger> accA2,
    @Singular("accA3") @JsonProperty("ACC_A_3") List<BigInteger> accA3,
    @Singular("accB0") @JsonProperty("ACC_B_0") List<BigInteger> accB0,
    @Singular("accB1") @JsonProperty("ACC_B_1") List<BigInteger> accB1,
    @Singular("accB2") @JsonProperty("ACC_B_2") List<BigInteger> accB2,
    @Singular("accB3") @JsonProperty("ACC_B_3") List<BigInteger> accB3,
    @Singular("accC0") @JsonProperty("ACC_C_0") List<BigInteger> accC0,
    @Singular("accC1") @JsonProperty("ACC_C_1") List<BigInteger> accC1,
    @Singular("accC2") @JsonProperty("ACC_C_2") List<BigInteger> accC2,
    @Singular("accC3") @JsonProperty("ACC_C_3") List<BigInteger> accC3,
    @Singular("accDelta0") @JsonProperty("ACC_DELTA_0") List<BigInteger> accDelta0,
    @Singular("accDelta1") @JsonProperty("ACC_DELTA_1") List<BigInteger> accDelta1,
    @Singular("accDelta2") @JsonProperty("ACC_DELTA_2") List<BigInteger> accDelta2,
    @Singular("accDelta3") @JsonProperty("ACC_DELTA_3") List<BigInteger> accDelta3,
    @Singular("accH0") @JsonProperty("ACC_H_0") List<BigInteger> accH0,
    @Singular("accH1") @JsonProperty("ACC_H_1") List<BigInteger> accH1,
    @Singular("accH2") @JsonProperty("ACC_H_2") List<BigInteger> accH2,
    @Singular("accH3") @JsonProperty("ACC_H_3") List<BigInteger> accH3,
    @Singular("accH4") @JsonProperty("ACC_H_4") List<BigInteger> accH4,
    @Singular("accH5") @JsonProperty("ACC_H_5") List<BigInteger> accH5,
    @Singular("accI0") @JsonProperty("ACC_I_0") List<BigInteger> accI0,
    @Singular("accI1") @JsonProperty("ACC_I_1") List<BigInteger> accI1,
    @Singular("accI2") @JsonProperty("ACC_I_2") List<BigInteger> accI2,
    @Singular("accI3") @JsonProperty("ACC_I_3") List<BigInteger> accI3,
    @Singular("accI4") @JsonProperty("ACC_I_4") List<BigInteger> accI4,
    @Singular("accI5") @JsonProperty("ACC_I_5") List<BigInteger> accI5,
    @Singular("accI6") @JsonProperty("ACC_I_6") List<BigInteger> accI6,
    @Singular("accJ0") @JsonProperty("ACC_J_0") List<BigInteger> accJ0,
    @Singular("accJ1") @JsonProperty("ACC_J_1") List<BigInteger> accJ1,
    @Singular("accJ2") @JsonProperty("ACC_J_2") List<BigInteger> accJ2,
    @Singular("accJ3") @JsonProperty("ACC_J_3") List<BigInteger> accJ3,
    @Singular("accJ4") @JsonProperty("ACC_J_4") List<BigInteger> accJ4,
    @Singular("accJ5") @JsonProperty("ACC_J_5") List<BigInteger> accJ5,
    @Singular("accJ6") @JsonProperty("ACC_J_6") List<BigInteger> accJ6,
    @Singular("accJ7") @JsonProperty("ACC_J_7") List<BigInteger> accJ7,
    @Singular("accQ0") @JsonProperty("ACC_Q_0") List<BigInteger> accQ0,
    @Singular("accQ1") @JsonProperty("ACC_Q_1") List<BigInteger> accQ1,
    @Singular("accQ2") @JsonProperty("ACC_Q_2") List<BigInteger> accQ2,
    @Singular("accQ3") @JsonProperty("ACC_Q_3") List<BigInteger> accQ3,
    @Singular("accQ4") @JsonProperty("ACC_Q_4") List<BigInteger> accQ4,
    @Singular("accQ5") @JsonProperty("ACC_Q_5") List<BigInteger> accQ5,
    @Singular("accQ6") @JsonProperty("ACC_Q_6") List<BigInteger> accQ6,
    @Singular("accQ7") @JsonProperty("ACC_Q_7") List<BigInteger> accQ7,
    @Singular("accR0") @JsonProperty("ACC_R_0") List<BigInteger> accR0,
    @Singular("accR1") @JsonProperty("ACC_R_1") List<BigInteger> accR1,
    @Singular("accR2") @JsonProperty("ACC_R_2") List<BigInteger> accR2,
    @Singular("accR3") @JsonProperty("ACC_R_3") List<BigInteger> accR3,
    @Singular("arg1Hi") @JsonProperty("ARG_1_HI") List<BigInteger> arg1Hi,
    @Singular("arg1Lo") @JsonProperty("ARG_1_LO") List<BigInteger> arg1Lo,
    @Singular("arg2Hi") @JsonProperty("ARG_2_HI") List<BigInteger> arg2Hi,
    @Singular("arg2Lo") @JsonProperty("ARG_2_LO") List<BigInteger> arg2Lo,
    @Singular("arg3Hi") @JsonProperty("ARG_3_HI") List<BigInteger> arg3Hi,
    @Singular("arg3Lo") @JsonProperty("ARG_3_LO") List<BigInteger> arg3Lo,
    @Singular("bit1") @JsonProperty("BIT_1") List<Boolean> bit1,
    @Singular("bit2") @JsonProperty("BIT_2") List<Boolean> bit2,
    @Singular("bit3") @JsonProperty("BIT_3") List<Boolean> bit3,
    @Singular("byteA0") @JsonProperty("BYTE_A_0") List<UnsignedByte> byteA0,
    @Singular("byteA1") @JsonProperty("BYTE_A_1") List<UnsignedByte> byteA1,
    @Singular("byteA2") @JsonProperty("BYTE_A_2") List<UnsignedByte> byteA2,
    @Singular("byteA3") @JsonProperty("BYTE_A_3") List<UnsignedByte> byteA3,
    @Singular("byteB0") @JsonProperty("BYTE_B_0") List<UnsignedByte> byteB0,
    @Singular("byteB1") @JsonProperty("BYTE_B_1") List<UnsignedByte> byteB1,
    @Singular("byteB2") @JsonProperty("BYTE_B_2") List<UnsignedByte> byteB2,
    @Singular("byteB3") @JsonProperty("BYTE_B_3") List<UnsignedByte> byteB3,
    @Singular("byteC0") @JsonProperty("BYTE_C_0") List<UnsignedByte> byteC0,
    @Singular("byteC1") @JsonProperty("BYTE_C_1") List<UnsignedByte> byteC1,
    @Singular("byteC2") @JsonProperty("BYTE_C_2") List<UnsignedByte> byteC2,
    @Singular("byteC3") @JsonProperty("BYTE_C_3") List<UnsignedByte> byteC3,
    @Singular("byteDelta0") @JsonProperty("BYTE_DELTA_0") List<UnsignedByte> byteDelta0,
    @Singular("byteDelta1") @JsonProperty("BYTE_DELTA_1") List<UnsignedByte> byteDelta1,
    @Singular("byteDelta2") @JsonProperty("BYTE_DELTA_2") List<UnsignedByte> byteDelta2,
    @Singular("byteDelta3") @JsonProperty("BYTE_DELTA_3") List<UnsignedByte> byteDelta3,
    @Singular("byteH0") @JsonProperty("BYTE_H_0") List<UnsignedByte> byteH0,
    @Singular("byteH1") @JsonProperty("BYTE_H_1") List<UnsignedByte> byteH1,
    @Singular("byteH2") @JsonProperty("BYTE_H_2") List<UnsignedByte> byteH2,
    @Singular("byteH3") @JsonProperty("BYTE_H_3") List<UnsignedByte> byteH3,
    @Singular("byteH4") @JsonProperty("BYTE_H_4") List<UnsignedByte> byteH4,
    @Singular("byteH5") @JsonProperty("BYTE_H_5") List<UnsignedByte> byteH5,
    @Singular("byteI0") @JsonProperty("BYTE_I_0") List<UnsignedByte> byteI0,
    @Singular("byteI1") @JsonProperty("BYTE_I_1") List<UnsignedByte> byteI1,
    @Singular("byteI2") @JsonProperty("BYTE_I_2") List<UnsignedByte> byteI2,
    @Singular("byteI3") @JsonProperty("BYTE_I_3") List<UnsignedByte> byteI3,
    @Singular("byteI4") @JsonProperty("BYTE_I_4") List<UnsignedByte> byteI4,
    @Singular("byteI5") @JsonProperty("BYTE_I_5") List<UnsignedByte> byteI5,
    @Singular("byteI6") @JsonProperty("BYTE_I_6") List<UnsignedByte> byteI6,
    @Singular("byteJ0") @JsonProperty("BYTE_J_0") List<UnsignedByte> byteJ0,
    @Singular("byteJ1") @JsonProperty("BYTE_J_1") List<UnsignedByte> byteJ1,
    @Singular("byteJ2") @JsonProperty("BYTE_J_2") List<UnsignedByte> byteJ2,
    @Singular("byteJ3") @JsonProperty("BYTE_J_3") List<UnsignedByte> byteJ3,
    @Singular("byteJ4") @JsonProperty("BYTE_J_4") List<UnsignedByte> byteJ4,
    @Singular("byteJ5") @JsonProperty("BYTE_J_5") List<UnsignedByte> byteJ5,
    @Singular("byteJ6") @JsonProperty("BYTE_J_6") List<UnsignedByte> byteJ6,
    @Singular("byteJ7") @JsonProperty("BYTE_J_7") List<UnsignedByte> byteJ7,
    @Singular("byteQ0") @JsonProperty("BYTE_Q_0") List<UnsignedByte> byteQ0,
    @Singular("byteQ1") @JsonProperty("BYTE_Q_1") List<UnsignedByte> byteQ1,
    @Singular("byteQ2") @JsonProperty("BYTE_Q_2") List<UnsignedByte> byteQ2,
    @Singular("byteQ3") @JsonProperty("BYTE_Q_3") List<UnsignedByte> byteQ3,
    @Singular("byteQ4") @JsonProperty("BYTE_Q_4") List<UnsignedByte> byteQ4,
    @Singular("byteQ5") @JsonProperty("BYTE_Q_5") List<UnsignedByte> byteQ5,
    @Singular("byteQ6") @JsonProperty("BYTE_Q_6") List<UnsignedByte> byteQ6,
    @Singular("byteQ7") @JsonProperty("BYTE_Q_7") List<UnsignedByte> byteQ7,
    @Singular("byteR0") @JsonProperty("BYTE_R_0") List<UnsignedByte> byteR0,
    @Singular("byteR1") @JsonProperty("BYTE_R_1") List<UnsignedByte> byteR1,
    @Singular("byteR2") @JsonProperty("BYTE_R_2") List<UnsignedByte> byteR2,
    @Singular("byteR3") @JsonProperty("BYTE_R_3") List<UnsignedByte> byteR3,
    @Singular("cmp") @JsonProperty("CMP") List<Boolean> cmp,
    @Singular("ct") @JsonProperty("CT") List<BigInteger> ct,
    @Singular("inst") @JsonProperty("INST") List<BigInteger> inst,
    @Singular("ofH") @JsonProperty("OF_H") List<Boolean> ofH,
    @Singular("ofI") @JsonProperty("OF_I") List<Boolean> ofI,
    @Singular("ofJ") @JsonProperty("OF_J") List<Boolean> ofJ,
    @Singular("ofRes") @JsonProperty("OF_RES") List<Boolean> ofRes,
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
