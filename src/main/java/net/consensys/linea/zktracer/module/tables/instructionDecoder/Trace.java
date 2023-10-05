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

package net.consensys.linea.zktracer.module.tables.instructionDecoder;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.consensys.linea.zktracer.bytes.UnsignedByte;

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
  @JsonProperty("ADD_FLAG") List<BigInteger> addFlag,
  @JsonProperty("ADD_MOD_FLAG") List<BigInteger> addModFlag,
  @JsonProperty("ALPHA") List<BigInteger> alpha,
  @JsonProperty("ALU_ADD_INST") List<BigInteger> aluAddInst,
  @JsonProperty("ALU_EXT_INST") List<BigInteger> aluExtInst,
  @JsonProperty("ALU_MOD_INST") List<BigInteger> aluModInst,
  @JsonProperty("ALU_MUL_INST") List<BigInteger> aluMulInst,
  @JsonProperty("ARITHMETIC_INST") List<BigInteger> arithmeticInst,
  @JsonProperty("BINARY_INST") List<BigInteger> binaryInst,
  @JsonProperty("CALL_FLAG") List<BigInteger> callFlag,
  @JsonProperty("CALLDATA_FLAG") List<BigInteger> calldataFlag,
  @JsonProperty("CALLDATACOPY_FLAG") List<BigInteger> calldatacopyFlag,
  @JsonProperty("DELTA") List<BigInteger> delta,
  @JsonProperty("DIV_FLAG") List<BigInteger> divFlag,
  @JsonProperty("EXODATA_IS_SOURCE") List<BigInteger> exodataIsSource,
  @JsonProperty("EXOOP_FLAG") List<BigInteger> exoopFlag,
  @JsonProperty("EXP_FLAG") List<BigInteger> expFlag,
  @JsonProperty("FLAG_1") List<BigInteger> flag1,
  @JsonProperty("FLAG_2") List<BigInteger> flag2,
  @JsonProperty("FLAG_3") List<BigInteger> flag3,
  @JsonProperty("HASH_INST") List<BigInteger> hashInst,
  @JsonProperty("INST") List<BigInteger> inst,
  @JsonProperty("INST_PARAM") List<BigInteger> instParam,
  @JsonProperty("INVALID_INSTRUCTION") List<BigInteger> invalidInstruction,
  @JsonProperty("JUMP_FLAG") List<BigInteger> jumpFlag,
  @JsonProperty("LOG_INST") List<BigInteger> logInst,
  @JsonProperty("MEMORY_EXPANSION_FLAG") List<BigInteger> memoryExpansionFlag,
  @JsonProperty("MOD_FLAG") List<BigInteger> modFlag,
  @JsonProperty("MUL_FLAG") List<BigInteger> mulFlag,
  @JsonProperty("MUL_MOD_FLAG") List<BigInteger> mulModFlag,
  @JsonProperty("MXP_GBYTE") List<BigInteger> mxpGbyte,
  @JsonProperty("MXP_GWORD") List<BigInteger> mxpGword,
  @JsonProperty("MXP_INST") List<BigInteger> mxpInst,
  @JsonProperty("MXP_TYPE_1") List<BigInteger> mxpType1,
  @JsonProperty("MXP_TYPE_2") List<BigInteger> mxpType2,
  @JsonProperty("MXP_TYPE_3") List<BigInteger> mxpType3,
  @JsonProperty("MXP_TYPE_4") List<BigInteger> mxpType4,
  @JsonProperty("MXP_TYPE_5") List<BigInteger> mxpType5,
  @JsonProperty("NB_ADDED") List<BigInteger> nbAdded,
  @JsonProperty("NB_REMOVED") List<BigInteger> nbRemoved,
  @JsonProperty("NON_STATIC_FLAG") List<BigInteger> nonStaticFlag,
  @JsonProperty("OP") List<BigInteger> op,
  @JsonProperty("PUSH_FLAG") List<BigInteger> pushFlag,
  @JsonProperty("RAM_INST") List<BigInteger> ramInst,
  @JsonProperty("RETURN_FLAG") List<BigInteger> returnFlag,
  @JsonProperty("RETURNDATA_FLAG") List<BigInteger> returndataFlag,
  @JsonProperty("REVERT_FLAG") List<BigInteger> revertFlag,
  @JsonProperty("ROM_FLAG") List<BigInteger> romFlag,
  @JsonProperty("SDIV_FLAG") List<BigInteger> sdivFlag,
  @JsonProperty("SHIFT_INST") List<BigInteger> shiftInst,
  @JsonProperty("SIZE") List<BigInteger> size,
  @JsonProperty("SMOD_FLAG") List<BigInteger> smodFlag,
  @JsonProperty("SPECIAL_PC_UPDATE") List<BigInteger> specialPcUpdate,
  @JsonProperty("STACK_PATTERN") List<BigInteger> stackPattern,
  @JsonProperty("STATIC_GAS") List<BigInteger> staticGas,
  @JsonProperty("STOP_FLAG") List<BigInteger> stopFlag,
  @JsonProperty("STORAGE_INST") List<BigInteger> storageInst,
  @JsonProperty("SUB_FLAG") List<BigInteger> subFlag,
  @JsonProperty("TWO_LINES_INSTRUCTION") List<BigInteger> twoLinesInstruction,
  @JsonProperty("WARMTH_FLAG") List<BigInteger> warmthFlag,
  @JsonProperty("WORD_COMPARISON_INST") List<BigInteger> wordComparisonInst) { 
  static TraceBuilder builder() {
    return new TraceBuilder();
  }

  static class TraceBuilder {
    private final BitSet filled = new BitSet();

    @JsonProperty("ADD_FLAG")
    private final List<BigInteger> addFlag = new ArrayList<>();
    @JsonProperty("ADD_MOD_FLAG")
    private final List<BigInteger> addModFlag = new ArrayList<>();
    @JsonProperty("ALPHA")
    private final List<BigInteger> alpha = new ArrayList<>();
    @JsonProperty("ALU_ADD_INST")
    private final List<BigInteger> aluAddInst = new ArrayList<>();
    @JsonProperty("ALU_EXT_INST")
    private final List<BigInteger> aluExtInst = new ArrayList<>();
    @JsonProperty("ALU_MOD_INST")
    private final List<BigInteger> aluModInst = new ArrayList<>();
    @JsonProperty("ALU_MUL_INST")
    private final List<BigInteger> aluMulInst = new ArrayList<>();
    @JsonProperty("ARITHMETIC_INST")
    private final List<BigInteger> arithmeticInst = new ArrayList<>();
    @JsonProperty("BINARY_INST")
    private final List<BigInteger> binaryInst = new ArrayList<>();
    @JsonProperty("CALL_FLAG")
    private final List<BigInteger> callFlag = new ArrayList<>();
    @JsonProperty("CALLDATA_FLAG")
    private final List<BigInteger> calldataFlag = new ArrayList<>();
    @JsonProperty("CALLDATACOPY_FLAG")
    private final List<BigInteger> calldatacopyFlag = new ArrayList<>();
    @JsonProperty("DELTA")
    private final List<BigInteger> delta = new ArrayList<>();
    @JsonProperty("DIV_FLAG")
    private final List<BigInteger> divFlag = new ArrayList<>();
    @JsonProperty("EXODATA_IS_SOURCE")
    private final List<BigInteger> exodataIsSource = new ArrayList<>();
    @JsonProperty("EXOOP_FLAG")
    private final List<BigInteger> exoopFlag = new ArrayList<>();
    @JsonProperty("EXP_FLAG")
    private final List<BigInteger> expFlag = new ArrayList<>();
    @JsonProperty("FLAG_1")
    private final List<BigInteger> flag1 = new ArrayList<>();
    @JsonProperty("FLAG_2")
    private final List<BigInteger> flag2 = new ArrayList<>();
    @JsonProperty("FLAG_3")
    private final List<BigInteger> flag3 = new ArrayList<>();
    @JsonProperty("HASH_INST")
    private final List<BigInteger> hashInst = new ArrayList<>();
    @JsonProperty("INST")
    private final List<BigInteger> inst = new ArrayList<>();
    @JsonProperty("INST_PARAM")
    private final List<BigInteger> instParam = new ArrayList<>();
    @JsonProperty("INVALID_INSTRUCTION")
    private final List<BigInteger> invalidInstruction = new ArrayList<>();
    @JsonProperty("JUMP_FLAG")
    private final List<BigInteger> jumpFlag = new ArrayList<>();
    @JsonProperty("LOG_INST")
    private final List<BigInteger> logInst = new ArrayList<>();
    @JsonProperty("MEMORY_EXPANSION_FLAG")
    private final List<BigInteger> memoryExpansionFlag = new ArrayList<>();
    @JsonProperty("MOD_FLAG")
    private final List<BigInteger> modFlag = new ArrayList<>();
    @JsonProperty("MUL_FLAG")
    private final List<BigInteger> mulFlag = new ArrayList<>();
    @JsonProperty("MUL_MOD_FLAG")
    private final List<BigInteger> mulModFlag = new ArrayList<>();
    @JsonProperty("MXP_GBYTE")
    private final List<BigInteger> mxpGbyte = new ArrayList<>();
    @JsonProperty("MXP_GWORD")
    private final List<BigInteger> mxpGword = new ArrayList<>();
    @JsonProperty("MXP_INST")
    private final List<BigInteger> mxpInst = new ArrayList<>();
    @JsonProperty("MXP_TYPE_1")
    private final List<BigInteger> mxpType1 = new ArrayList<>();
    @JsonProperty("MXP_TYPE_2")
    private final List<BigInteger> mxpType2 = new ArrayList<>();
    @JsonProperty("MXP_TYPE_3")
    private final List<BigInteger> mxpType3 = new ArrayList<>();
    @JsonProperty("MXP_TYPE_4")
    private final List<BigInteger> mxpType4 = new ArrayList<>();
    @JsonProperty("MXP_TYPE_5")
    private final List<BigInteger> mxpType5 = new ArrayList<>();
    @JsonProperty("NB_ADDED")
    private final List<BigInteger> nbAdded = new ArrayList<>();
    @JsonProperty("NB_REMOVED")
    private final List<BigInteger> nbRemoved = new ArrayList<>();
    @JsonProperty("NON_STATIC_FLAG")
    private final List<BigInteger> nonStaticFlag = new ArrayList<>();
    @JsonProperty("OP")
    private final List<BigInteger> op = new ArrayList<>();
    @JsonProperty("PUSH_FLAG")
    private final List<BigInteger> pushFlag = new ArrayList<>();
    @JsonProperty("RAM_INST")
    private final List<BigInteger> ramInst = new ArrayList<>();
    @JsonProperty("RETURN_FLAG")
    private final List<BigInteger> returnFlag = new ArrayList<>();
    @JsonProperty("RETURNDATA_FLAG")
    private final List<BigInteger> returndataFlag = new ArrayList<>();
    @JsonProperty("REVERT_FLAG")
    private final List<BigInteger> revertFlag = new ArrayList<>();
    @JsonProperty("ROM_FLAG")
    private final List<BigInteger> romFlag = new ArrayList<>();
    @JsonProperty("SDIV_FLAG")
    private final List<BigInteger> sdivFlag = new ArrayList<>();
    @JsonProperty("SHIFT_INST")
    private final List<BigInteger> shiftInst = new ArrayList<>();
    @JsonProperty("SIZE")
    private final List<BigInteger> size = new ArrayList<>();
    @JsonProperty("SMOD_FLAG")
    private final List<BigInteger> smodFlag = new ArrayList<>();
    @JsonProperty("SPECIAL_PC_UPDATE")
    private final List<BigInteger> specialPcUpdate = new ArrayList<>();
    @JsonProperty("STACK_PATTERN")
    private final List<BigInteger> stackPattern = new ArrayList<>();
    @JsonProperty("STATIC_GAS")
    private final List<BigInteger> staticGas = new ArrayList<>();
    @JsonProperty("STOP_FLAG")
    private final List<BigInteger> stopFlag = new ArrayList<>();
    @JsonProperty("STORAGE_INST")
    private final List<BigInteger> storageInst = new ArrayList<>();
    @JsonProperty("SUB_FLAG")
    private final List<BigInteger> subFlag = new ArrayList<>();
    @JsonProperty("TWO_LINES_INSTRUCTION")
    private final List<BigInteger> twoLinesInstruction = new ArrayList<>();
    @JsonProperty("WARMTH_FLAG")
    private final List<BigInteger> warmthFlag = new ArrayList<>();
    @JsonProperty("WORD_COMPARISON_INST")
    private final List<BigInteger> wordComparisonInst = new ArrayList<>();

    TraceBuilder() {}

    public int size() {
      if (!filled.isEmpty()) {
        throw new RuntimeException("Cannot measure a trace with a non-validated row.");
      }

      return this.addFlag.size();
    }

    public TraceBuilder addFlag(final BigInteger b) {
      if (filled.get(0)) {
        throw new IllegalStateException("ADD_FLAG already set");
      } else {
        filled.set(0);
      }

      addFlag.add(b);

      return this;
    }

    public TraceBuilder addModFlag(final BigInteger b) {
      if (filled.get(1)) {
        throw new IllegalStateException("ADD_MOD_FLAG already set");
      } else {
        filled.set(1);
      }

      addModFlag.add(b);

      return this;
    }

    public TraceBuilder alpha(final BigInteger b) {
      if (filled.get(2)) {
        throw new IllegalStateException("ALPHA already set");
      } else {
        filled.set(2);
      }

      alpha.add(b);

      return this;
    }

    public TraceBuilder aluAddInst(final BigInteger b) {
      if (filled.get(3)) {
        throw new IllegalStateException("ALU_ADD_INST already set");
      } else {
        filled.set(3);
      }

      aluAddInst.add(b);

      return this;
    }

    public TraceBuilder aluExtInst(final BigInteger b) {
      if (filled.get(4)) {
        throw new IllegalStateException("ALU_EXT_INST already set");
      } else {
        filled.set(4);
      }

      aluExtInst.add(b);

      return this;
    }

    public TraceBuilder aluModInst(final BigInteger b) {
      if (filled.get(5)) {
        throw new IllegalStateException("ALU_MOD_INST already set");
      } else {
        filled.set(5);
      }

      aluModInst.add(b);

      return this;
    }

    public TraceBuilder aluMulInst(final BigInteger b) {
      if (filled.get(6)) {
        throw new IllegalStateException("ALU_MUL_INST already set");
      } else {
        filled.set(6);
      }

      aluMulInst.add(b);

      return this;
    }

    public TraceBuilder arithmeticInst(final BigInteger b) {
      if (filled.get(7)) {
        throw new IllegalStateException("ARITHMETIC_INST already set");
      } else {
        filled.set(7);
      }

      arithmeticInst.add(b);

      return this;
    }

    public TraceBuilder binaryInst(final BigInteger b) {
      if (filled.get(8)) {
        throw new IllegalStateException("BINARY_INST already set");
      } else {
        filled.set(8);
      }

      binaryInst.add(b);

      return this;
    }

    public TraceBuilder callFlag(final BigInteger b) {
      if (filled.get(11)) {
        throw new IllegalStateException("CALL_FLAG already set");
      } else {
        filled.set(11);
      }

      callFlag.add(b);

      return this;
    }

    public TraceBuilder calldataFlag(final BigInteger b) {
      if (filled.get(10)) {
        throw new IllegalStateException("CALLDATA_FLAG already set");
      } else {
        filled.set(10);
      }

      calldataFlag.add(b);

      return this;
    }

    public TraceBuilder calldatacopyFlag(final BigInteger b) {
      if (filled.get(9)) {
        throw new IllegalStateException("CALLDATACOPY_FLAG already set");
      } else {
        filled.set(9);
      }

      calldatacopyFlag.add(b);

      return this;
    }

    public TraceBuilder delta(final BigInteger b) {
      if (filled.get(12)) {
        throw new IllegalStateException("DELTA already set");
      } else {
        filled.set(12);
      }

      delta.add(b);

      return this;
    }

    public TraceBuilder divFlag(final BigInteger b) {
      if (filled.get(13)) {
        throw new IllegalStateException("DIV_FLAG already set");
      } else {
        filled.set(13);
      }

      divFlag.add(b);

      return this;
    }

    public TraceBuilder exodataIsSource(final BigInteger b) {
      if (filled.get(14)) {
        throw new IllegalStateException("EXODATA_IS_SOURCE already set");
      } else {
        filled.set(14);
      }

      exodataIsSource.add(b);

      return this;
    }

    public TraceBuilder exoopFlag(final BigInteger b) {
      if (filled.get(15)) {
        throw new IllegalStateException("EXOOP_FLAG already set");
      } else {
        filled.set(15);
      }

      exoopFlag.add(b);

      return this;
    }

    public TraceBuilder expFlag(final BigInteger b) {
      if (filled.get(16)) {
        throw new IllegalStateException("EXP_FLAG already set");
      } else {
        filled.set(16);
      }

      expFlag.add(b);

      return this;
    }

    public TraceBuilder flag1(final BigInteger b) {
      if (filled.get(17)) {
        throw new IllegalStateException("FLAG_1 already set");
      } else {
        filled.set(17);
      }

      flag1.add(b);

      return this;
    }

    public TraceBuilder flag2(final BigInteger b) {
      if (filled.get(18)) {
        throw new IllegalStateException("FLAG_2 already set");
      } else {
        filled.set(18);
      }

      flag2.add(b);

      return this;
    }

    public TraceBuilder flag3(final BigInteger b) {
      if (filled.get(19)) {
        throw new IllegalStateException("FLAG_3 already set");
      } else {
        filled.set(19);
      }

      flag3.add(b);

      return this;
    }

    public TraceBuilder hashInst(final BigInteger b) {
      if (filled.get(20)) {
        throw new IllegalStateException("HASH_INST already set");
      } else {
        filled.set(20);
      }

      hashInst.add(b);

      return this;
    }

    public TraceBuilder inst(final BigInteger b) {
      if (filled.get(21)) {
        throw new IllegalStateException("INST already set");
      } else {
        filled.set(21);
      }

      inst.add(b);

      return this;
    }

    public TraceBuilder instParam(final BigInteger b) {
      if (filled.get(22)) {
        throw new IllegalStateException("INST_PARAM already set");
      } else {
        filled.set(22);
      }

      instParam.add(b);

      return this;
    }

    public TraceBuilder invalidInstruction(final BigInteger b) {
      if (filled.get(23)) {
        throw new IllegalStateException("INVALID_INSTRUCTION already set");
      } else {
        filled.set(23);
      }

      invalidInstruction.add(b);

      return this;
    }

    public TraceBuilder jumpFlag(final BigInteger b) {
      if (filled.get(24)) {
        throw new IllegalStateException("JUMP_FLAG already set");
      } else {
        filled.set(24);
      }

      jumpFlag.add(b);

      return this;
    }

    public TraceBuilder logInst(final BigInteger b) {
      if (filled.get(25)) {
        throw new IllegalStateException("LOG_INST already set");
      } else {
        filled.set(25);
      }

      logInst.add(b);

      return this;
    }

    public TraceBuilder memoryExpansionFlag(final BigInteger b) {
      if (filled.get(26)) {
        throw new IllegalStateException("MEMORY_EXPANSION_FLAG already set");
      } else {
        filled.set(26);
      }

      memoryExpansionFlag.add(b);

      return this;
    }

    public TraceBuilder modFlag(final BigInteger b) {
      if (filled.get(27)) {
        throw new IllegalStateException("MOD_FLAG already set");
      } else {
        filled.set(27);
      }

      modFlag.add(b);

      return this;
    }

    public TraceBuilder mulFlag(final BigInteger b) {
      if (filled.get(28)) {
        throw new IllegalStateException("MUL_FLAG already set");
      } else {
        filled.set(28);
      }

      mulFlag.add(b);

      return this;
    }

    public TraceBuilder mulModFlag(final BigInteger b) {
      if (filled.get(29)) {
        throw new IllegalStateException("MUL_MOD_FLAG already set");
      } else {
        filled.set(29);
      }

      mulModFlag.add(b);

      return this;
    }

    public TraceBuilder mxpGbyte(final BigInteger b) {
      if (filled.get(30)) {
        throw new IllegalStateException("MXP_GBYTE already set");
      } else {
        filled.set(30);
      }

      mxpGbyte.add(b);

      return this;
    }

    public TraceBuilder mxpGword(final BigInteger b) {
      if (filled.get(31)) {
        throw new IllegalStateException("MXP_GWORD already set");
      } else {
        filled.set(31);
      }

      mxpGword.add(b);

      return this;
    }

    public TraceBuilder mxpInst(final BigInteger b) {
      if (filled.get(32)) {
        throw new IllegalStateException("MXP_INST already set");
      } else {
        filled.set(32);
      }

      mxpInst.add(b);

      return this;
    }

    public TraceBuilder mxpType1(final BigInteger b) {
      if (filled.get(33)) {
        throw new IllegalStateException("MXP_TYPE_1 already set");
      } else {
        filled.set(33);
      }

      mxpType1.add(b);

      return this;
    }

    public TraceBuilder mxpType2(final BigInteger b) {
      if (filled.get(34)) {
        throw new IllegalStateException("MXP_TYPE_2 already set");
      } else {
        filled.set(34);
      }

      mxpType2.add(b);

      return this;
    }

    public TraceBuilder mxpType3(final BigInteger b) {
      if (filled.get(35)) {
        throw new IllegalStateException("MXP_TYPE_3 already set");
      } else {
        filled.set(35);
      }

      mxpType3.add(b);

      return this;
    }

    public TraceBuilder mxpType4(final BigInteger b) {
      if (filled.get(36)) {
        throw new IllegalStateException("MXP_TYPE_4 already set");
      } else {
        filled.set(36);
      }

      mxpType4.add(b);

      return this;
    }

    public TraceBuilder mxpType5(final BigInteger b) {
      if (filled.get(37)) {
        throw new IllegalStateException("MXP_TYPE_5 already set");
      } else {
        filled.set(37);
      }

      mxpType5.add(b);

      return this;
    }

    public TraceBuilder nbAdded(final BigInteger b) {
      if (filled.get(38)) {
        throw new IllegalStateException("NB_ADDED already set");
      } else {
        filled.set(38);
      }

      nbAdded.add(b);

      return this;
    }

    public TraceBuilder nbRemoved(final BigInteger b) {
      if (filled.get(39)) {
        throw new IllegalStateException("NB_REMOVED already set");
      } else {
        filled.set(39);
      }

      nbRemoved.add(b);

      return this;
    }

    public TraceBuilder nonStaticFlag(final BigInteger b) {
      if (filled.get(40)) {
        throw new IllegalStateException("NON_STATIC_FLAG already set");
      } else {
        filled.set(40);
      }

      nonStaticFlag.add(b);

      return this;
    }

    public TraceBuilder op(final BigInteger b) {
      if (filled.get(41)) {
        throw new IllegalStateException("OP already set");
      } else {
        filled.set(41);
      }

      op.add(b);

      return this;
    }

    public TraceBuilder pushFlag(final BigInteger b) {
      if (filled.get(42)) {
        throw new IllegalStateException("PUSH_FLAG already set");
      } else {
        filled.set(42);
      }

      pushFlag.add(b);

      return this;
    }

    public TraceBuilder ramInst(final BigInteger b) {
      if (filled.get(43)) {
        throw new IllegalStateException("RAM_INST already set");
      } else {
        filled.set(43);
      }

      ramInst.add(b);

      return this;
    }

    public TraceBuilder returnFlag(final BigInteger b) {
      if (filled.get(45)) {
        throw new IllegalStateException("RETURN_FLAG already set");
      } else {
        filled.set(45);
      }

      returnFlag.add(b);

      return this;
    }

    public TraceBuilder returndataFlag(final BigInteger b) {
      if (filled.get(44)) {
        throw new IllegalStateException("RETURNDATA_FLAG already set");
      } else {
        filled.set(44);
      }

      returndataFlag.add(b);

      return this;
    }

    public TraceBuilder revertFlag(final BigInteger b) {
      if (filled.get(46)) {
        throw new IllegalStateException("REVERT_FLAG already set");
      } else {
        filled.set(46);
      }

      revertFlag.add(b);

      return this;
    }

    public TraceBuilder romFlag(final BigInteger b) {
      if (filled.get(47)) {
        throw new IllegalStateException("ROM_FLAG already set");
      } else {
        filled.set(47);
      }

      romFlag.add(b);

      return this;
    }

    public TraceBuilder sdivFlag(final BigInteger b) {
      if (filled.get(48)) {
        throw new IllegalStateException("SDIV_FLAG already set");
      } else {
        filled.set(48);
      }

      sdivFlag.add(b);

      return this;
    }

    public TraceBuilder shiftInst(final BigInteger b) {
      if (filled.get(49)) {
        throw new IllegalStateException("SHIFT_INST already set");
      } else {
        filled.set(49);
      }

      shiftInst.add(b);

      return this;
    }

    public TraceBuilder size(final BigInteger b) {
      if (filled.get(50)) {
        throw new IllegalStateException("SIZE already set");
      } else {
        filled.set(50);
      }

      size.add(b);

      return this;
    }

    public TraceBuilder smodFlag(final BigInteger b) {
      if (filled.get(51)) {
        throw new IllegalStateException("SMOD_FLAG already set");
      } else {
        filled.set(51);
      }

      smodFlag.add(b);

      return this;
    }

    public TraceBuilder specialPcUpdate(final BigInteger b) {
      if (filled.get(52)) {
        throw new IllegalStateException("SPECIAL_PC_UPDATE already set");
      } else {
        filled.set(52);
      }

      specialPcUpdate.add(b);

      return this;
    }

    public TraceBuilder stackPattern(final BigInteger b) {
      if (filled.get(53)) {
        throw new IllegalStateException("STACK_PATTERN already set");
      } else {
        filled.set(53);
      }

      stackPattern.add(b);

      return this;
    }

    public TraceBuilder staticGas(final BigInteger b) {
      if (filled.get(54)) {
        throw new IllegalStateException("STATIC_GAS already set");
      } else {
        filled.set(54);
      }

      staticGas.add(b);

      return this;
    }

    public TraceBuilder stopFlag(final BigInteger b) {
      if (filled.get(55)) {
        throw new IllegalStateException("STOP_FLAG already set");
      } else {
        filled.set(55);
      }

      stopFlag.add(b);

      return this;
    }

    public TraceBuilder storageInst(final BigInteger b) {
      if (filled.get(56)) {
        throw new IllegalStateException("STORAGE_INST already set");
      } else {
        filled.set(56);
      }

      storageInst.add(b);

      return this;
    }

    public TraceBuilder subFlag(final BigInteger b) {
      if (filled.get(57)) {
        throw new IllegalStateException("SUB_FLAG already set");
      } else {
        filled.set(57);
      }

      subFlag.add(b);

      return this;
    }

    public TraceBuilder twoLinesInstruction(final BigInteger b) {
      if (filled.get(58)) {
        throw new IllegalStateException("TWO_LINES_INSTRUCTION already set");
      } else {
        filled.set(58);
      }

      twoLinesInstruction.add(b);

      return this;
    }

    public TraceBuilder warmthFlag(final BigInteger b) {
      if (filled.get(59)) {
        throw new IllegalStateException("WARMTH_FLAG already set");
      } else {
        filled.set(59);
      }

      warmthFlag.add(b);

      return this;
    }

    public TraceBuilder wordComparisonInst(final BigInteger b) {
      if (filled.get(60)) {
        throw new IllegalStateException("WORD_COMPARISON_INST already set");
      } else {
        filled.set(60);
      }

      wordComparisonInst.add(b);

      return this;
    }

    public TraceBuilder setAddFlagAt(final BigInteger b, int i) {
      addFlag.set(i, b);

      return this;
    }

    public TraceBuilder setAddModFlagAt(final BigInteger b, int i) {
      addModFlag.set(i, b);

      return this;
    }

    public TraceBuilder setAlphaAt(final BigInteger b, int i) {
      alpha.set(i, b);

      return this;
    }

    public TraceBuilder setAluAddInstAt(final BigInteger b, int i) {
      aluAddInst.set(i, b);

      return this;
    }

    public TraceBuilder setAluExtInstAt(final BigInteger b, int i) {
      aluExtInst.set(i, b);

      return this;
    }

    public TraceBuilder setAluModInstAt(final BigInteger b, int i) {
      aluModInst.set(i, b);

      return this;
    }

    public TraceBuilder setAluMulInstAt(final BigInteger b, int i) {
      aluMulInst.set(i, b);

      return this;
    }

    public TraceBuilder setArithmeticInstAt(final BigInteger b, int i) {
      arithmeticInst.set(i, b);

      return this;
    }

    public TraceBuilder setBinaryInstAt(final BigInteger b, int i) {
      binaryInst.set(i, b);

      return this;
    }

    public TraceBuilder setCallFlagAt(final BigInteger b, int i) {
      callFlag.set(i, b);

      return this;
    }

    public TraceBuilder setCalldataFlagAt(final BigInteger b, int i) {
      calldataFlag.set(i, b);

      return this;
    }

    public TraceBuilder setCalldatacopyFlagAt(final BigInteger b, int i) {
      calldatacopyFlag.set(i, b);

      return this;
    }

    public TraceBuilder setDeltaAt(final BigInteger b, int i) {
      delta.set(i, b);

      return this;
    }

    public TraceBuilder setDivFlagAt(final BigInteger b, int i) {
      divFlag.set(i, b);

      return this;
    }

    public TraceBuilder setExodataIsSourceAt(final BigInteger b, int i) {
      exodataIsSource.set(i, b);

      return this;
    }

    public TraceBuilder setExoopFlagAt(final BigInteger b, int i) {
      exoopFlag.set(i, b);

      return this;
    }

    public TraceBuilder setExpFlagAt(final BigInteger b, int i) {
      expFlag.set(i, b);

      return this;
    }

    public TraceBuilder setFlag1At(final BigInteger b, int i) {
      flag1.set(i, b);

      return this;
    }

    public TraceBuilder setFlag2At(final BigInteger b, int i) {
      flag2.set(i, b);

      return this;
    }

    public TraceBuilder setFlag3At(final BigInteger b, int i) {
      flag3.set(i, b);

      return this;
    }

    public TraceBuilder setHashInstAt(final BigInteger b, int i) {
      hashInst.set(i, b);

      return this;
    }

    public TraceBuilder setInstAt(final BigInteger b, int i) {
      inst.set(i, b);

      return this;
    }

    public TraceBuilder setInstParamAt(final BigInteger b, int i) {
      instParam.set(i, b);

      return this;
    }

    public TraceBuilder setInvalidInstructionAt(final BigInteger b, int i) {
      invalidInstruction.set(i, b);

      return this;
    }

    public TraceBuilder setJumpFlagAt(final BigInteger b, int i) {
      jumpFlag.set(i, b);

      return this;
    }

    public TraceBuilder setLogInstAt(final BigInteger b, int i) {
      logInst.set(i, b);

      return this;
    }

    public TraceBuilder setMemoryExpansionFlagAt(final BigInteger b, int i) {
      memoryExpansionFlag.set(i, b);

      return this;
    }

    public TraceBuilder setModFlagAt(final BigInteger b, int i) {
      modFlag.set(i, b);

      return this;
    }

    public TraceBuilder setMulFlagAt(final BigInteger b, int i) {
      mulFlag.set(i, b);

      return this;
    }

    public TraceBuilder setMulModFlagAt(final BigInteger b, int i) {
      mulModFlag.set(i, b);

      return this;
    }

    public TraceBuilder setMxpGbyteAt(final BigInteger b, int i) {
      mxpGbyte.set(i, b);

      return this;
    }

    public TraceBuilder setMxpGwordAt(final BigInteger b, int i) {
      mxpGword.set(i, b);

      return this;
    }

    public TraceBuilder setMxpInstAt(final BigInteger b, int i) {
      mxpInst.set(i, b);

      return this;
    }

    public TraceBuilder setMxpType1At(final BigInteger b, int i) {
      mxpType1.set(i, b);

      return this;
    }

    public TraceBuilder setMxpType2At(final BigInteger b, int i) {
      mxpType2.set(i, b);

      return this;
    }

    public TraceBuilder setMxpType3At(final BigInteger b, int i) {
      mxpType3.set(i, b);

      return this;
    }

    public TraceBuilder setMxpType4At(final BigInteger b, int i) {
      mxpType4.set(i, b);

      return this;
    }

    public TraceBuilder setMxpType5At(final BigInteger b, int i) {
      mxpType5.set(i, b);

      return this;
    }

    public TraceBuilder setNbAddedAt(final BigInteger b, int i) {
      nbAdded.set(i, b);

      return this;
    }

    public TraceBuilder setNbRemovedAt(final BigInteger b, int i) {
      nbRemoved.set(i, b);

      return this;
    }

    public TraceBuilder setNonStaticFlagAt(final BigInteger b, int i) {
      nonStaticFlag.set(i, b);

      return this;
    }

    public TraceBuilder setOpAt(final BigInteger b, int i) {
      op.set(i, b);

      return this;
    }

    public TraceBuilder setPushFlagAt(final BigInteger b, int i) {
      pushFlag.set(i, b);

      return this;
    }

    public TraceBuilder setRamInstAt(final BigInteger b, int i) {
      ramInst.set(i, b);

      return this;
    }

    public TraceBuilder setReturnFlagAt(final BigInteger b, int i) {
      returnFlag.set(i, b);

      return this;
    }

    public TraceBuilder setReturndataFlagAt(final BigInteger b, int i) {
      returndataFlag.set(i, b);

      return this;
    }

    public TraceBuilder setRevertFlagAt(final BigInteger b, int i) {
      revertFlag.set(i, b);

      return this;
    }

    public TraceBuilder setRomFlagAt(final BigInteger b, int i) {
      romFlag.set(i, b);

      return this;
    }

    public TraceBuilder setSdivFlagAt(final BigInteger b, int i) {
      sdivFlag.set(i, b);

      return this;
    }

    public TraceBuilder setShiftInstAt(final BigInteger b, int i) {
      shiftInst.set(i, b);

      return this;
    }

    public TraceBuilder setSizeAt(final BigInteger b, int i) {
      size.set(i, b);

      return this;
    }

    public TraceBuilder setSmodFlagAt(final BigInteger b, int i) {
      smodFlag.set(i, b);

      return this;
    }

    public TraceBuilder setSpecialPcUpdateAt(final BigInteger b, int i) {
      specialPcUpdate.set(i, b);

      return this;
    }

    public TraceBuilder setStackPatternAt(final BigInteger b, int i) {
      stackPattern.set(i, b);

      return this;
    }

    public TraceBuilder setStaticGasAt(final BigInteger b, int i) {
      staticGas.set(i, b);

      return this;
    }

    public TraceBuilder setStopFlagAt(final BigInteger b, int i) {
      stopFlag.set(i, b);

      return this;
    }

    public TraceBuilder setStorageInstAt(final BigInteger b, int i) {
      storageInst.set(i, b);

      return this;
    }

    public TraceBuilder setSubFlagAt(final BigInteger b, int i) {
      subFlag.set(i, b);

      return this;
    }

    public TraceBuilder setTwoLinesInstructionAt(final BigInteger b, int i) {
      twoLinesInstruction.set(i, b);

      return this;
    }

    public TraceBuilder setWarmthFlagAt(final BigInteger b, int i) {
      warmthFlag.set(i, b);

      return this;
    }

    public TraceBuilder setWordComparisonInstAt(final BigInteger b, int i) {
      wordComparisonInst.set(i, b);

      return this;
    }

    public TraceBuilder setAddFlagRelative(final BigInteger b, int i) {
      addFlag.set(addFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setAddModFlagRelative(final BigInteger b, int i) {
      addModFlag.set(addModFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setAlphaRelative(final BigInteger b, int i) {
      alpha.set(alpha.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setAluAddInstRelative(final BigInteger b, int i) {
      aluAddInst.set(aluAddInst.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setAluExtInstRelative(final BigInteger b, int i) {
      aluExtInst.set(aluExtInst.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setAluModInstRelative(final BigInteger b, int i) {
      aluModInst.set(aluModInst.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setAluMulInstRelative(final BigInteger b, int i) {
      aluMulInst.set(aluMulInst.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setArithmeticInstRelative(final BigInteger b, int i) {
      arithmeticInst.set(arithmeticInst.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setBinaryInstRelative(final BigInteger b, int i) {
      binaryInst.set(binaryInst.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setCallFlagRelative(final BigInteger b, int i) {
      callFlag.set(callFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setCalldataFlagRelative(final BigInteger b, int i) {
      calldataFlag.set(calldataFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setCalldatacopyFlagRelative(final BigInteger b, int i) {
      calldatacopyFlag.set(calldatacopyFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setDeltaRelative(final BigInteger b, int i) {
      delta.set(delta.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setDivFlagRelative(final BigInteger b, int i) {
      divFlag.set(divFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setExodataIsSourceRelative(final BigInteger b, int i) {
      exodataIsSource.set(exodataIsSource.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setExoopFlagRelative(final BigInteger b, int i) {
      exoopFlag.set(exoopFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setExpFlagRelative(final BigInteger b, int i) {
      expFlag.set(expFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setFlag1Relative(final BigInteger b, int i) {
      flag1.set(flag1.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setFlag2Relative(final BigInteger b, int i) {
      flag2.set(flag2.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setFlag3Relative(final BigInteger b, int i) {
      flag3.set(flag3.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setHashInstRelative(final BigInteger b, int i) {
      hashInst.set(hashInst.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setInstRelative(final BigInteger b, int i) {
      inst.set(inst.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setInstParamRelative(final BigInteger b, int i) {
      instParam.set(instParam.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setInvalidInstructionRelative(final BigInteger b, int i) {
      invalidInstruction.set(invalidInstruction.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setJumpFlagRelative(final BigInteger b, int i) {
      jumpFlag.set(jumpFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setLogInstRelative(final BigInteger b, int i) {
      logInst.set(logInst.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setMemoryExpansionFlagRelative(final BigInteger b, int i) {
      memoryExpansionFlag.set(memoryExpansionFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setModFlagRelative(final BigInteger b, int i) {
      modFlag.set(modFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setMulFlagRelative(final BigInteger b, int i) {
      mulFlag.set(mulFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setMulModFlagRelative(final BigInteger b, int i) {
      mulModFlag.set(mulModFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setMxpGbyteRelative(final BigInteger b, int i) {
      mxpGbyte.set(mxpGbyte.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setMxpGwordRelative(final BigInteger b, int i) {
      mxpGword.set(mxpGword.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setMxpInstRelative(final BigInteger b, int i) {
      mxpInst.set(mxpInst.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setMxpType1Relative(final BigInteger b, int i) {
      mxpType1.set(mxpType1.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setMxpType2Relative(final BigInteger b, int i) {
      mxpType2.set(mxpType2.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setMxpType3Relative(final BigInteger b, int i) {
      mxpType3.set(mxpType3.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setMxpType4Relative(final BigInteger b, int i) {
      mxpType4.set(mxpType4.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setMxpType5Relative(final BigInteger b, int i) {
      mxpType5.set(mxpType5.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setNbAddedRelative(final BigInteger b, int i) {
      nbAdded.set(nbAdded.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setNbRemovedRelative(final BigInteger b, int i) {
      nbRemoved.set(nbRemoved.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setNonStaticFlagRelative(final BigInteger b, int i) {
      nonStaticFlag.set(nonStaticFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setOpRelative(final BigInteger b, int i) {
      op.set(op.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setPushFlagRelative(final BigInteger b, int i) {
      pushFlag.set(pushFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setRamInstRelative(final BigInteger b, int i) {
      ramInst.set(ramInst.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setReturnFlagRelative(final BigInteger b, int i) {
      returnFlag.set(returnFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setReturndataFlagRelative(final BigInteger b, int i) {
      returndataFlag.set(returndataFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setRevertFlagRelative(final BigInteger b, int i) {
      revertFlag.set(revertFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setRomFlagRelative(final BigInteger b, int i) {
      romFlag.set(romFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setSdivFlagRelative(final BigInteger b, int i) {
      sdivFlag.set(sdivFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setShiftInstRelative(final BigInteger b, int i) {
      shiftInst.set(shiftInst.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setSizeRelative(final BigInteger b, int i) {
      size.set(size.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setSmodFlagRelative(final BigInteger b, int i) {
      smodFlag.set(smodFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setSpecialPcUpdateRelative(final BigInteger b, int i) {
      specialPcUpdate.set(specialPcUpdate.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setStackPatternRelative(final BigInteger b, int i) {
      stackPattern.set(stackPattern.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setStaticGasRelative(final BigInteger b, int i) {
      staticGas.set(staticGas.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setStopFlagRelative(final BigInteger b, int i) {
      stopFlag.set(stopFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setStorageInstRelative(final BigInteger b, int i) {
      storageInst.set(storageInst.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setSubFlagRelative(final BigInteger b, int i) {
      subFlag.set(subFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setTwoLinesInstructionRelative(final BigInteger b, int i) {
      twoLinesInstruction.set(twoLinesInstruction.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setWarmthFlagRelative(final BigInteger b, int i) {
      warmthFlag.set(warmthFlag.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder setWordComparisonInstRelative(final BigInteger b, int i) {
      wordComparisonInst.set(wordComparisonInst.size() - 1 - i, b);

      return this;
    }

    public TraceBuilder validateRow() {
      if (!filled.get(0)) {
        throw new IllegalStateException("ADD_FLAG has not been filled");
      }

      if (!filled.get(1)) {
        throw new IllegalStateException("ADD_MOD_FLAG has not been filled");
      }

      if (!filled.get(2)) {
        throw new IllegalStateException("ALPHA has not been filled");
      }

      if (!filled.get(3)) {
        throw new IllegalStateException("ALU_ADD_INST has not been filled");
      }

      if (!filled.get(4)) {
        throw new IllegalStateException("ALU_EXT_INST has not been filled");
      }

      if (!filled.get(5)) {
        throw new IllegalStateException("ALU_MOD_INST has not been filled");
      }

      if (!filled.get(6)) {
        throw new IllegalStateException("ALU_MUL_INST has not been filled");
      }

      if (!filled.get(7)) {
        throw new IllegalStateException("ARITHMETIC_INST has not been filled");
      }

      if (!filled.get(8)) {
        throw new IllegalStateException("BINARY_INST has not been filled");
      }

      if (!filled.get(11)) {
        throw new IllegalStateException("CALL_FLAG has not been filled");
      }

      if (!filled.get(10)) {
        throw new IllegalStateException("CALLDATA_FLAG has not been filled");
      }

      if (!filled.get(9)) {
        throw new IllegalStateException("CALLDATACOPY_FLAG has not been filled");
      }

      if (!filled.get(12)) {
        throw new IllegalStateException("DELTA has not been filled");
      }

      if (!filled.get(13)) {
        throw new IllegalStateException("DIV_FLAG has not been filled");
      }

      if (!filled.get(14)) {
        throw new IllegalStateException("EXODATA_IS_SOURCE has not been filled");
      }

      if (!filled.get(15)) {
        throw new IllegalStateException("EXOOP_FLAG has not been filled");
      }

      if (!filled.get(16)) {
        throw new IllegalStateException("EXP_FLAG has not been filled");
      }

      if (!filled.get(17)) {
        throw new IllegalStateException("FLAG_1 has not been filled");
      }

      if (!filled.get(18)) {
        throw new IllegalStateException("FLAG_2 has not been filled");
      }

      if (!filled.get(19)) {
        throw new IllegalStateException("FLAG_3 has not been filled");
      }

      if (!filled.get(20)) {
        throw new IllegalStateException("HASH_INST has not been filled");
      }

      if (!filled.get(21)) {
        throw new IllegalStateException("INST has not been filled");
      }

      if (!filled.get(22)) {
        throw new IllegalStateException("INST_PARAM has not been filled");
      }

      if (!filled.get(23)) {
        throw new IllegalStateException("INVALID_INSTRUCTION has not been filled");
      }

      if (!filled.get(24)) {
        throw new IllegalStateException("JUMP_FLAG has not been filled");
      }

      if (!filled.get(25)) {
        throw new IllegalStateException("LOG_INST has not been filled");
      }

      if (!filled.get(26)) {
        throw new IllegalStateException("MEMORY_EXPANSION_FLAG has not been filled");
      }

      if (!filled.get(27)) {
        throw new IllegalStateException("MOD_FLAG has not been filled");
      }

      if (!filled.get(28)) {
        throw new IllegalStateException("MUL_FLAG has not been filled");
      }

      if (!filled.get(29)) {
        throw new IllegalStateException("MUL_MOD_FLAG has not been filled");
      }

      if (!filled.get(30)) {
        throw new IllegalStateException("MXP_GBYTE has not been filled");
      }

      if (!filled.get(31)) {
        throw new IllegalStateException("MXP_GWORD has not been filled");
      }

      if (!filled.get(32)) {
        throw new IllegalStateException("MXP_INST has not been filled");
      }

      if (!filled.get(33)) {
        throw new IllegalStateException("MXP_TYPE_1 has not been filled");
      }

      if (!filled.get(34)) {
        throw new IllegalStateException("MXP_TYPE_2 has not been filled");
      }

      if (!filled.get(35)) {
        throw new IllegalStateException("MXP_TYPE_3 has not been filled");
      }

      if (!filled.get(36)) {
        throw new IllegalStateException("MXP_TYPE_4 has not been filled");
      }

      if (!filled.get(37)) {
        throw new IllegalStateException("MXP_TYPE_5 has not been filled");
      }

      if (!filled.get(38)) {
        throw new IllegalStateException("NB_ADDED has not been filled");
      }

      if (!filled.get(39)) {
        throw new IllegalStateException("NB_REMOVED has not been filled");
      }

      if (!filled.get(40)) {
        throw new IllegalStateException("NON_STATIC_FLAG has not been filled");
      }

      if (!filled.get(41)) {
        throw new IllegalStateException("OP has not been filled");
      }

      if (!filled.get(42)) {
        throw new IllegalStateException("PUSH_FLAG has not been filled");
      }

      if (!filled.get(43)) {
        throw new IllegalStateException("RAM_INST has not been filled");
      }

      if (!filled.get(45)) {
        throw new IllegalStateException("RETURN_FLAG has not been filled");
      }

      if (!filled.get(44)) {
        throw new IllegalStateException("RETURNDATA_FLAG has not been filled");
      }

      if (!filled.get(46)) {
        throw new IllegalStateException("REVERT_FLAG has not been filled");
      }

      if (!filled.get(47)) {
        throw new IllegalStateException("ROM_FLAG has not been filled");
      }

      if (!filled.get(48)) {
        throw new IllegalStateException("SDIV_FLAG has not been filled");
      }

      if (!filled.get(49)) {
        throw new IllegalStateException("SHIFT_INST has not been filled");
      }

      if (!filled.get(50)) {
        throw new IllegalStateException("SIZE has not been filled");
      }

      if (!filled.get(51)) {
        throw new IllegalStateException("SMOD_FLAG has not been filled");
      }

      if (!filled.get(52)) {
        throw new IllegalStateException("SPECIAL_PC_UPDATE has not been filled");
      }

      if (!filled.get(53)) {
        throw new IllegalStateException("STACK_PATTERN has not been filled");
      }

      if (!filled.get(54)) {
        throw new IllegalStateException("STATIC_GAS has not been filled");
      }

      if (!filled.get(55)) {
        throw new IllegalStateException("STOP_FLAG has not been filled");
      }

      if (!filled.get(56)) {
        throw new IllegalStateException("STORAGE_INST has not been filled");
      }

      if (!filled.get(57)) {
        throw new IllegalStateException("SUB_FLAG has not been filled");
      }

      if (!filled.get(58)) {
        throw new IllegalStateException("TWO_LINES_INSTRUCTION has not been filled");
      }

      if (!filled.get(59)) {
        throw new IllegalStateException("WARMTH_FLAG has not been filled");
      }

      if (!filled.get(60)) {
        throw new IllegalStateException("WORD_COMPARISON_INST has not been filled");
      }


      filled.clear();

      return this;
    }

    public TraceBuilder fillAndValidateRow() {
      if (!filled.get(0)) {
          addFlag.add(BigInteger.ZERO);
          this.filled.set(0);
      }
      if (!filled.get(1)) {
          addModFlag.add(BigInteger.ZERO);
          this.filled.set(1);
      }
      if (!filled.get(2)) {
          alpha.add(BigInteger.ZERO);
          this.filled.set(2);
      }
      if (!filled.get(3)) {
          aluAddInst.add(BigInteger.ZERO);
          this.filled.set(3);
      }
      if (!filled.get(4)) {
          aluExtInst.add(BigInteger.ZERO);
          this.filled.set(4);
      }
      if (!filled.get(5)) {
          aluModInst.add(BigInteger.ZERO);
          this.filled.set(5);
      }
      if (!filled.get(6)) {
          aluMulInst.add(BigInteger.ZERO);
          this.filled.set(6);
      }
      if (!filled.get(7)) {
          arithmeticInst.add(BigInteger.ZERO);
          this.filled.set(7);
      }
      if (!filled.get(8)) {
          binaryInst.add(BigInteger.ZERO);
          this.filled.set(8);
      }
      if (!filled.get(11)) {
          callFlag.add(BigInteger.ZERO);
          this.filled.set(11);
      }
      if (!filled.get(10)) {
          calldataFlag.add(BigInteger.ZERO);
          this.filled.set(10);
      }
      if (!filled.get(9)) {
          calldatacopyFlag.add(BigInteger.ZERO);
          this.filled.set(9);
      }
      if (!filled.get(12)) {
          delta.add(BigInteger.ZERO);
          this.filled.set(12);
      }
      if (!filled.get(13)) {
          divFlag.add(BigInteger.ZERO);
          this.filled.set(13);
      }
      if (!filled.get(14)) {
          exodataIsSource.add(BigInteger.ZERO);
          this.filled.set(14);
      }
      if (!filled.get(15)) {
          exoopFlag.add(BigInteger.ZERO);
          this.filled.set(15);
      }
      if (!filled.get(16)) {
          expFlag.add(BigInteger.ZERO);
          this.filled.set(16);
      }
      if (!filled.get(17)) {
          flag1.add(BigInteger.ZERO);
          this.filled.set(17);
      }
      if (!filled.get(18)) {
          flag2.add(BigInteger.ZERO);
          this.filled.set(18);
      }
      if (!filled.get(19)) {
          flag3.add(BigInteger.ZERO);
          this.filled.set(19);
      }
      if (!filled.get(20)) {
          hashInst.add(BigInteger.ZERO);
          this.filled.set(20);
      }
      if (!filled.get(21)) {
          inst.add(BigInteger.ZERO);
          this.filled.set(21);
      }
      if (!filled.get(22)) {
          instParam.add(BigInteger.ZERO);
          this.filled.set(22);
      }
      if (!filled.get(23)) {
          invalidInstruction.add(BigInteger.ZERO);
          this.filled.set(23);
      }
      if (!filled.get(24)) {
          jumpFlag.add(BigInteger.ZERO);
          this.filled.set(24);
      }
      if (!filled.get(25)) {
          logInst.add(BigInteger.ZERO);
          this.filled.set(25);
      }
      if (!filled.get(26)) {
          memoryExpansionFlag.add(BigInteger.ZERO);
          this.filled.set(26);
      }
      if (!filled.get(27)) {
          modFlag.add(BigInteger.ZERO);
          this.filled.set(27);
      }
      if (!filled.get(28)) {
          mulFlag.add(BigInteger.ZERO);
          this.filled.set(28);
      }
      if (!filled.get(29)) {
          mulModFlag.add(BigInteger.ZERO);
          this.filled.set(29);
      }
      if (!filled.get(30)) {
          mxpGbyte.add(BigInteger.ZERO);
          this.filled.set(30);
      }
      if (!filled.get(31)) {
          mxpGword.add(BigInteger.ZERO);
          this.filled.set(31);
      }
      if (!filled.get(32)) {
          mxpInst.add(BigInteger.ZERO);
          this.filled.set(32);
      }
      if (!filled.get(33)) {
          mxpType1.add(BigInteger.ZERO);
          this.filled.set(33);
      }
      if (!filled.get(34)) {
          mxpType2.add(BigInteger.ZERO);
          this.filled.set(34);
      }
      if (!filled.get(35)) {
          mxpType3.add(BigInteger.ZERO);
          this.filled.set(35);
      }
      if (!filled.get(36)) {
          mxpType4.add(BigInteger.ZERO);
          this.filled.set(36);
      }
      if (!filled.get(37)) {
          mxpType5.add(BigInteger.ZERO);
          this.filled.set(37);
      }
      if (!filled.get(38)) {
          nbAdded.add(BigInteger.ZERO);
          this.filled.set(38);
      }
      if (!filled.get(39)) {
          nbRemoved.add(BigInteger.ZERO);
          this.filled.set(39);
      }
      if (!filled.get(40)) {
          nonStaticFlag.add(BigInteger.ZERO);
          this.filled.set(40);
      }
      if (!filled.get(41)) {
          op.add(BigInteger.ZERO);
          this.filled.set(41);
      }
      if (!filled.get(42)) {
          pushFlag.add(BigInteger.ZERO);
          this.filled.set(42);
      }
      if (!filled.get(43)) {
          ramInst.add(BigInteger.ZERO);
          this.filled.set(43);
      }
      if (!filled.get(45)) {
          returnFlag.add(BigInteger.ZERO);
          this.filled.set(45);
      }
      if (!filled.get(44)) {
          returndataFlag.add(BigInteger.ZERO);
          this.filled.set(44);
      }
      if (!filled.get(46)) {
          revertFlag.add(BigInteger.ZERO);
          this.filled.set(46);
      }
      if (!filled.get(47)) {
          romFlag.add(BigInteger.ZERO);
          this.filled.set(47);
      }
      if (!filled.get(48)) {
          sdivFlag.add(BigInteger.ZERO);
          this.filled.set(48);
      }
      if (!filled.get(49)) {
          shiftInst.add(BigInteger.ZERO);
          this.filled.set(49);
      }
      if (!filled.get(50)) {
          size.add(BigInteger.ZERO);
          this.filled.set(50);
      }
      if (!filled.get(51)) {
          smodFlag.add(BigInteger.ZERO);
          this.filled.set(51);
      }
      if (!filled.get(52)) {
          specialPcUpdate.add(BigInteger.ZERO);
          this.filled.set(52);
      }
      if (!filled.get(53)) {
          stackPattern.add(BigInteger.ZERO);
          this.filled.set(53);
      }
      if (!filled.get(54)) {
          staticGas.add(BigInteger.ZERO);
          this.filled.set(54);
      }
      if (!filled.get(55)) {
          stopFlag.add(BigInteger.ZERO);
          this.filled.set(55);
      }
      if (!filled.get(56)) {
          storageInst.add(BigInteger.ZERO);
          this.filled.set(56);
      }
      if (!filled.get(57)) {
          subFlag.add(BigInteger.ZERO);
          this.filled.set(57);
      }
      if (!filled.get(58)) {
          twoLinesInstruction.add(BigInteger.ZERO);
          this.filled.set(58);
      }
      if (!filled.get(59)) {
          warmthFlag.add(BigInteger.ZERO);
          this.filled.set(59);
      }
      if (!filled.get(60)) {
          wordComparisonInst.add(BigInteger.ZERO);
          this.filled.set(60);
      }

      return this.validateRow();
    }

    public Trace build() {
      if (!filled.isEmpty()) {
        throw new IllegalStateException("Cannot build trace with a non-validated row.");
      }

      return new Trace(
        addFlag,
        addModFlag,
        alpha,
        aluAddInst,
        aluExtInst,
        aluModInst,
        aluMulInst,
        arithmeticInst,
        binaryInst,
        callFlag,
        calldataFlag,
        calldatacopyFlag,
        delta,
        divFlag,
        exodataIsSource,
        exoopFlag,
        expFlag,
        flag1,
        flag2,
        flag3,
        hashInst,
        inst,
        instParam,
        invalidInstruction,
        jumpFlag,
        logInst,
        memoryExpansionFlag,
        modFlag,
        mulFlag,
        mulModFlag,
        mxpGbyte,
        mxpGword,
        mxpInst,
        mxpType1,
        mxpType2,
        mxpType3,
        mxpType4,
        mxpType5,
        nbAdded,
        nbRemoved,
        nonStaticFlag,
        op,
        pushFlag,
        ramInst,
        returnFlag,
        returndataFlag,
        revertFlag,
        romFlag,
        sdivFlag,
        shiftInst,
        size,
        smodFlag,
        specialPcUpdate,
        stackPattern,
        staticGas,
        stopFlag,
        storageInst,
        subFlag,
        twoLinesInstruction,
        warmthFlag,
        wordComparisonInst);
    }
  }
}
