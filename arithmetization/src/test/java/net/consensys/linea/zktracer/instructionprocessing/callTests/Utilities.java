/*
 * Copyright Consensys Software Inc.
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
package net.consensys.linea.zktracer.instructionprocessing.callTests;

import static com.google.common.base.Preconditions.checkArgument;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.WORD_SIZE;
import static net.consensys.linea.zktracer.opcode.OpCode.*;

import java.util.List;

import net.consensys.linea.UnitTestWatcher;
import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.hyperledger.besu.datatypes.Address;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(UnitTestWatcher.class)
public class Utilities {

  public static final String fullEoaAddress = "000000000000000000000000abcdef0123456789";
  public static final String toTrim12 = "aaaaaaaaaaaaaaaaaaaaaaaa";
  public static final String untrimmedEoaAddress = toTrim12 + fullEoaAddress;
  public static final String eoaAddress = "c0ffeef00d";
  public static final String eoaAddress2 = "badbeef";

  public static void fullGasCall(
      BytecodeCompiler program,
      OpCode callOpcode,
      Address to,
      int value,
      int cdo,
      int cds,
      int rao,
      int rac) {
    program.push(rac).push(rao).push(cds).push(cdo);
    if (callOpcode.callHasValueArgument()) {
      program.push(value);
    }
    program.push(to).op(GAS).op(callOpcode).op(POP);
  }

  public static void simpleCall(
      BytecodeCompiler program,
      OpCode callOpcode,
      int gas,
      Address to,
      int value,
      int cdo,
      int cds,
      int rao,
      int rac) {
    program.push(rac).push(rao).push(cds).push(cdo);
    if (callOpcode.callHasValueArgument()) {
      program.push(value);
    }
    program.push(to).push(gas).op(callOpcode);
  }

  public static void callCaller(
      BytecodeCompiler program,
      OpCode callOpcode,
      int gas,
      int value,
      int cdo,
      int cds,
      int rao,
      int rac) {
    program.push(rac).push(rao).push(cds).push(cdo);
    if (callOpcode.callHasValueArgument()) {
      program.push(value);
    }
    program.op(CALLER).push(gas).op(callOpcode);
  }

  public static void appendInsufficientBalanceCall(
      BytecodeCompiler program,
      OpCode callOpcode,
      int gas,
      Address to,
      int cdo,
      int cds,
      int rao,
      int rac) {
    checkArgument(callOpcode.callHasValueArgument());
    program
        .push(rac)
        .push(rao)
        .push(cds)
        .push(cdo)
        .op(BALANCE)
        .push(1)
        .op(ADD) // puts balance + 1 on the stack
        .push(to)
        .push(gas)
        .op(callOpcode);
  }

  /**
   * Pushing, in order: h, v, r, s; values produce a valid signature.
   *
   * @param program
   */
  public static void validEcrecoverData(BytecodeCompiler program) {
    program
        .push("279d94621558f755796898fc4bd36b6d407cae77537865afe523b79c74cc680b")
        .push(0)
        .op(MSTORE)
        .push("1b")
        .push(32)
        .op(MSTORE)
        .push("c2ff96feed8749a5ad1c0714f950b5ac939d8acedbedcbc2949614ab8af06312")
        .push(64)
        .op(MSTORE)
        .push("1feecd50adc6273fdd5d11c6da18c8cfe14e2787f5a90af7c7c1328e7d0a2c42")
        .push(96)
        .op(MSTORE);
  }

  /**
   * Populates memory with 6 words of data, namely
   *
   * <p><b>0x aa ... aa bb ... bb cc ... cc dd ... dd ee ... ee ff ... ff</b>
   *
   * <p>starting at offset 0. This provides 192 = 6*32 nonzero bytes in RAM.
   */
  public static void populateMemory(BytecodeCompiler program) {
    populateMemory(program, 6, 0);
  }

  public static void fullReturnDataCopyAt(BytecodeCompiler program, int targetOffset) {
    program.op(RETURNDATASIZE).push(0).push(targetOffset);
  }

  public static void partialReturnDataCopyAt(BytecodeCompiler program, int targetOffset) {
    pushRdsOverArgOntoTheStack(program, 2); // source size   ≡ rds/2
    pushRdsOverArgOntoTheStack(program, 3); // source offset ≡ rds/3
    program.push(targetOffset);
  }

  public static void loadFirstReturnDataWordOntoStack(BytecodeCompiler program, int offset) {
    squashMemoryWordAtOffset(program, offset);
    pushMinOfRdsAnd32OntoStack(program); // leaves min(RDS, 32) on the stack
    program.push(0).push(offset).op(RETURNDATACOPY).push(offset).op(MLOAD);
  }

  public static void pushMinOfRdsAnd32OntoStack(BytecodeCompiler program) {
    program
        .push(WORD_SIZE)
        .op(RETURNDATASIZE)
        .op(LT) // stack: [ c | ... [, where c ≡ [RDS < 32]
        .op(DUP1)
        .push(1)
        .op(SUB) // stack: [ d | c | ... [, where d ≡ [RDS ≥ 32]
        .push(WORD_SIZE)
        .op(MUL) // stack: [ (d ? 32 : 0) | c | ... [
        .op(SWAP1) // stack: [ c | (d ? 32 : 0) | ... [
        .op(RETURNDATASIZE)
        .op(MUL) // stack: [ (c ? RDS : 0) | (d ? 32 : 0) | ... [
        .op(ADD) // stack: [ min(RDS, 32) | ... [
    ;
  }

  public static void squashMemoryWordAtOffset(BytecodeCompiler program, int offset) {
    program.push(0).push(offset).op(MSTORE);
  }

  public static void pushRdsOverArgOntoTheStack(BytecodeCompiler program, int arg) {
    program.push(arg).op(RETURNDATASIZE).op(DIV);
  }

  /**
   * {@link #populateMemory} populates memory with <b>nWords</b> words chosen cyclically from 6 EVM
   * words obtained by repeating the strings "aa", "bb", "cc", "dd", "ee", "ff" 32 times each.
   *
   * @param program
   * @param nWords
   */
  public static void populateMemory(BytecodeCompiler program, int nWords, int offset) {
    List<String> abcdef = List.of("aa", "bb", "cc", "dd", "ee", "ff");
    for (int i = 0; i < nWords; i++) {
      program
          .push(abcdef.get(i % abcdef.size()).repeat(WORD_SIZE)) // value, a 32 byte word
          .push(offset + i * WORD_SIZE) // offset
          .op(MSTORE);
    }
  }
}
