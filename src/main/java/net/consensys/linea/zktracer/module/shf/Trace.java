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

package net.consensys.linea.zktracer.module.shf;

import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Singular;
import net.consensys.linea.zktracer.bytes.UnsignedByte;
import net.consensys.linea.zktracer.module.RowValidator;

@Builder
record Trace(
    @Singular("acc1") @JsonProperty("ACC_1") List<BigInteger> acc1,
    @Singular("acc2") @JsonProperty("ACC_2") List<BigInteger> acc2,
    @Singular("acc3") @JsonProperty("ACC_3") List<BigInteger> acc3,
    @Singular("acc4") @JsonProperty("ACC_4") List<BigInteger> acc4,
    @Singular("acc5") @JsonProperty("ACC_5") List<BigInteger> acc5,
    @Singular("arg1Hi") @JsonProperty("ARG_1_HI") List<BigInteger> arg1Hi,
    @Singular("arg1Lo") @JsonProperty("ARG_1_LO") List<BigInteger> arg1Lo,
    @Singular("arg2Hi") @JsonProperty("ARG_2_HI") List<BigInteger> arg2Hi,
    @Singular("arg2Lo") @JsonProperty("ARG_2_LO") List<BigInteger> arg2Lo,
    @Singular("bit1") @JsonProperty("BIT_1") List<Boolean> bit1,
    @Singular("bit2") @JsonProperty("BIT_2") List<Boolean> bit2,
    @Singular("bit3") @JsonProperty("BIT_3") List<Boolean> bit3,
    @Singular("bit4") @JsonProperty("BIT_4") List<Boolean> bit4,
    @Singular("bitB3") @JsonProperty("BIT_B_3") List<Boolean> bitB3,
    @Singular("bitB4") @JsonProperty("BIT_B_4") List<Boolean> bitB4,
    @Singular("bitB5") @JsonProperty("BIT_B_5") List<Boolean> bitB5,
    @Singular("bitB6") @JsonProperty("BIT_B_6") List<Boolean> bitB6,
    @Singular("bitB7") @JsonProperty("BIT_B_7") List<Boolean> bitB7,
    @Singular("bits") @JsonProperty("BITS") List<Boolean> bits,
    @Singular("byte1") @JsonProperty("BYTE_1") List<UnsignedByte> byte1,
    @Singular("byte2") @JsonProperty("BYTE_2") List<UnsignedByte> byte2,
    @Singular("byte3") @JsonProperty("BYTE_3") List<UnsignedByte> byte3,
    @Singular("byte4") @JsonProperty("BYTE_4") List<UnsignedByte> byte4,
    @Singular("byte5") @JsonProperty("BYTE_5") List<UnsignedByte> byte5,
    @Singular("counter") @JsonProperty("COUNTER") List<BigInteger> counter,
    @Singular("inst") @JsonProperty("INST") List<BigInteger> inst,
    @Singular("isData") @JsonProperty("IS_DATA") List<Boolean> isData,
    @Singular("known") @JsonProperty("KNOWN") List<Boolean> known,
    @Singular("leftAlignedSuffixHigh") @JsonProperty("LEFT_ALIGNED_SUFFIX_HIGH")
        List<BigInteger> leftAlignedSuffixHigh,
    @Singular("leftAlignedSuffixLow") @JsonProperty("LEFT_ALIGNED_SUFFIX_LOW")
        List<BigInteger> leftAlignedSuffixLow,
    @Singular("low3") @JsonProperty("LOW_3") List<BigInteger> low3,
    @Singular("microShiftParameter") @JsonProperty("MICRO_SHIFT_PARAMETER")
        List<BigInteger> microShiftParameter,
    @Singular("neg") @JsonProperty("NEG") List<Boolean> neg,
    @Singular("oneLineInstruction") @JsonProperty("ONE_LINE_INSTRUCTION")
        List<Boolean> oneLineInstruction,
    @Singular("ones") @JsonProperty("ONES") List<BigInteger> ones,
    @Singular("resHi") @JsonProperty("RES_HI") List<BigInteger> resHi,
    @Singular("resLo") @JsonProperty("RES_LO") List<BigInteger> resLo,
    @Singular("rightAlignedPrefixHigh") @JsonProperty("RIGHT_ALIGNED_PREFIX_HIGH")
        List<BigInteger> rightAlignedPrefixHigh,
    @Singular("rightAlignedPrefixLow") @JsonProperty("RIGHT_ALIGNED_PREFIX_LOW")
        List<BigInteger> rightAlignedPrefixLow,
    @Singular("shb3Hi") @JsonProperty("SHB_3_HI") List<BigInteger> shb3Hi,
    @Singular("shb3Lo") @JsonProperty("SHB_3_LO") List<BigInteger> shb3Lo,
    @Singular("shb4Hi") @JsonProperty("SHB_4_HI") List<BigInteger> shb4Hi,
    @Singular("shb4Lo") @JsonProperty("SHB_4_LO") List<BigInteger> shb4Lo,
    @Singular("shb5Hi") @JsonProperty("SHB_5_HI") List<BigInteger> shb5Hi,
    @Singular("shb5Lo") @JsonProperty("SHB_5_LO") List<BigInteger> shb5Lo,
    @Singular("shb6Hi") @JsonProperty("SHB_6_HI") List<BigInteger> shb6Hi,
    @Singular("shb6Lo") @JsonProperty("SHB_6_LO") List<BigInteger> shb6Lo,
    @Singular("shb7Hi") @JsonProperty("SHB_7_HI") List<BigInteger> shb7Hi,
    @Singular("shb7Lo") @JsonProperty("SHB_7_LO") List<BigInteger> shb7Lo,
    @Singular("shiftDirection") @JsonProperty("SHIFT_DIRECTION") List<Boolean> shiftDirection,
    @Singular("shiftStamp") @JsonProperty("SHIFT_STAMP") List<BigInteger> shiftStamp) {
  static class TraceBuilder {
    public void validateRow(int rowIndex) {
      RowValidator.validate(Trace.class, this.build(), rowIndex);
    }
  }
}
