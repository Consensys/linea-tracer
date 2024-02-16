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

package net.consensys.linea.zktracer.module.exp;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.math.BigInteger;

import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.testing.BytecodeCompiler;
import net.consensys.linea.zktracer.testing.BytecodeRunner;
import net.consensys.linea.zktracer.testing.EvmExtension;
import org.apache.tuweni.bytes.Bytes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(EvmExtension.class)
public class ExpTest {

  @Test
  void TestExpLogSingleCase() {
    BytecodeCompiler program = BytecodeCompiler.newProgram();
    program.push(2).push(10).op(OpCode.EXP);
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    bytecodeRunner.run();
  }

  @Test
  void TestModexpLogSingleCase() {
    BytecodeCompiler program = BytecodeCompiler.newProgram();
    program
        .push(1) // bbs
        .push(0)
        .op(OpCode.MSTORE)
        .push(1) // ebs
        .push(0x20)
        .op(OpCode.MSTORE)
        .push(1) // mbs
        .push(0x40)
        .op(OpCode.MSTORE)
        .push(
            Bytes.fromHexStringLenient(
                "0x08090A0000000000000000000000000000000000000000000000000000000000")) // b, e, m
        .push(0x60)
        .op(OpCode.MSTORE);

    program
        .push(1) // retSize
        .push(0x9f) // retOffset
        .push(0x63) // argSize (cds)
        .push(0) // argOffset (cdo)
        .push(5) // address
        .push(Bytes.fromHexStringLenient("0xFFFFFFFF")) // gas
        .op(OpCode.STATICCALL);
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    bytecodeRunner.run();
  }

  @Test
  void TestExpLogFFBlockCase() {
    for (int k = 0; k <= 32; k++) {
      Bytes exponent = Bytes.fromHexString(FFBlock(k));
      BytecodeCompiler program = BytecodeCompiler.newProgram();
      program.push(exponent).push(10).op(OpCode.EXP);
      BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
      bytecodeRunner.run();
    }
  }

  @Test
  void TestExpLogFFAtCase() {
    for (int k = 1; k <= 32; k++) {
      Bytes exponent = Bytes.fromHexString(FFAt(k));
      BytecodeCompiler program = BytecodeCompiler.newProgram();
      program.push(exponent).push(10).op(OpCode.EXP);
      BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
      bytecodeRunner.run();
    }
  }

  // Generates 128, 64, 2, 1 as LD
  int[] LDIndeces = new int[] {1, 2, 7, 8};
  int[] C = new int[] {1, 2, 10, 15, 16, 17, 20, 31, 32};

  @Test
  void TestModexpLogFFBlockWithLDCase() {
    for (int ebsCutoff : C) {
      for (int cdsCutoff : C) {
        for (int k : C) {
          for (int LDIndex : LDIndeces) {
            Bytes wordAfterBase = Bytes.fromHexStringLenient(FFBlockWithLD(k, LDIndex));
            BytecodeCompiler program =
                initProgramInvokingModexp(ebsCutoff, cdsCutoff, wordAfterBase);
            BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
            bytecodeRunner.run();
          }
        }
      }
    }
  }

  @Test
  void TestModexpLogLDAtCase() {
    for (int ebsCutoff : C) {
      for (int cdsCutoff : C) {
        for (int k : C) {
          for (int LDIndex : LDIndeces) {
            Bytes wordAfterBase = Bytes.fromHexStringLenient(LDAt(k, LDIndex));
            BytecodeCompiler program =
                initProgramInvokingModexp(ebsCutoff, cdsCutoff, wordAfterBase);
            BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
            bytecodeRunner.run();
          }
        }
      }
    }
  }

  @Test
  void TestModexpLogZerosCase() {
    for (int ebsCutoff : C) {
      for (int cdsCutoff : C) {
        Bytes wordAfterBase =
            Bytes.fromHexStringLenient(
                "0000000000000000000000000000000000000000000000000000000000000000");
        BytecodeCompiler program = initProgramInvokingModexp(ebsCutoff, cdsCutoff, wordAfterBase);
        BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
        bytecodeRunner.run();
      }
    }
  }

  BytecodeCompiler initProgramInvokingModexp(int ebsCutoff, int cdsCutoff, Bytes wordAfterBase) {
    int bbs = 0;
    int minimalValidEbs = ebsCutoff;
    int mbs = 0;
    int minimalValidCds = cdsCutoff + 96 + bbs;
    System.out.println("ebsCutoff: " + ebsCutoff);
    System.out.println("cdsCutoff: " + cdsCutoff);
    System.out.println("minimalValidEbs: " + minimalValidEbs);
    System.out.println("minimalValidCds: " + minimalValidCds);
    System.out.println("wordAfterBase: " + wordAfterBase);

    BytecodeCompiler program = BytecodeCompiler.newProgram();
    program
        .push(bbs) // bbs
        .push(0)
        .op(OpCode.MSTORE)
        .push(minimalValidEbs) // ebs
        .push(32)
        .op(OpCode.MSTORE)
        .push(mbs) // mbs
        .push(64)
        .op(OpCode.MSTORE)
        .push(wordAfterBase) // e
        .push(96 + bbs)
        .op(OpCode.MSTORE);

    program
        .push(512) // retSize
        .push(minimalValidCds) // retOffset
        .push(minimalValidCds) // argSize (cds)
        .push(0) // argOffset (cdo)
        .push(5) // address
        .push(Bytes.fromHexStringLenient("0xFFFFFFFF")) // gas
        .op(OpCode.STATICCALL);
    System.out.println(program.compile());
    return program;
  }

  @Test
  void TestExpUtils() {
    // ExpLog case
    System.out.println("FFBlock");
    for (int k = 0; k <= 32; k++) {
      System.out.println(FFBlock(k));
    }

    System.out.println("FFAt");
    for (int k = 1; k <= 32; k++) {
      System.out.println(FFAt(k));
    }

    // ModexpLog case
    System.out.println("FFBlockWithLD");
    for (int k : C) {
      for (int LDIndex : LDIndeces) {
        System.out.println(FFBlockWithLD(k, LDIndex));
      }
    }

    System.out.println("LDAt");
    for (int k : C) {
      for (int LDIndex : LDIndeces) {
        System.out.println(LDAt(k, LDIndex));
      }
    }

    for (int ebsCutoff : C) {
      for (int cdsCutoff : C) {
        int bbs = 0;
        int minimalValidEbs = ebsCutoff;
        int minimalValidCds = cdsCutoff + 96 + bbs;
        assert (ebsCutoff == min(minimalValidEbs, 32));
        assert (cdsCutoff == min(max(minimalValidCds - (96 + bbs), 0), 32));
        System.out.println(
            "minimalValidEbs: " + minimalValidEbs + ", minimalValidCds: " + minimalValidCds);
        System.out.println("ebsCutoff: " + ebsCutoff + ", cdsCutoff: " + cdsCutoff);
        System.out.println("###");
      }
    }
  }

  public static String FFBlock(int k) {
    if (k < 0 || k > 32) {
      throw new IllegalArgumentException("k must be between 0 and 32");
    }
    return "00".repeat(32 - k) + "ff".repeat(k);
  }

  public static String FFAt(int k) {
    if (k < 1 || k > 32) {
      throw new IllegalArgumentException("k must be between 1 and 32");
    }
    return "00".repeat(k - 1) + "ff" + "00".repeat(32 - k);
  }

  public static String FFBlockWithLD(int k, int LDIndex) {
    if (k < 1 || k > 32) {
      throw new IllegalArgumentException("k must be between 1 and 32");
    }
    if (LDIndex < 1 || LDIndex > 8) {
      throw new IllegalArgumentException("LDIndex must be between 1 and 8");
    }
    String LD =
        new BigInteger("0".repeat(LDIndex - 1) + "1" + "0".repeat(8 - LDIndex), 2).toString(16);
    if (k < 32) {
      return "00".repeat(32 - k - 1) + (LD.length() == 1 ? "0" + LD : LD) + "ff".repeat(k);
    } else {
      return "ff".repeat(k);
    }
  }

  public static String LDAt(int k, int LDIndex) {
    if (k < 1 || k > 32) {
      throw new IllegalArgumentException("k must be between 1 and 32");
    }
    if (LDIndex < 1 || LDIndex > 8) {
      throw new IllegalArgumentException("LDIndex must be between 1 and 8");
    }
    String LD =
        new BigInteger("0".repeat(LDIndex - 1) + "1" + "0".repeat(8 - LDIndex), 2).toString(16);
    return "00".repeat(k - 1) + (LD.length() == 1 ? "0" + LD : LD) + "00".repeat(32 - k);
  }
}
