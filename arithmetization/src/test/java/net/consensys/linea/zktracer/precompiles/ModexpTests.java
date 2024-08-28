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
package net.consensys.linea.zktracer.precompiles;

import static net.consensys.linea.zktracer.module.blake2fmodexpdata.BlakeModexpDataOperation.MODEXP_COMPONENT_BYTE_SIZE;
import static net.consensys.linea.zktracer.module.hub.precompiles.ModexpMetadata.BASE_MIN_OFFSET;
import static net.consensys.linea.zktracer.module.hub.precompiles.ModexpMetadata.BBS_MIN_OFFSET;
import static net.consensys.linea.zktracer.module.hub.precompiles.ModexpMetadata.EBS_MIN_OFFSET;
import static net.consensys.linea.zktracer.module.hub.precompiles.ModexpMetadata.MBS_MIN_OFFSET;

import java.util.List;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;
import org.junit.jupiter.api.Test;

public class ModexpTests {

  // some 10 decimal digit primes in the range [256 ** 3, 256 ** 4[
  // * 1081914797 ≡ 0x407CB5AD
  // * 1086173677 ≡ 0x40BDB1ED
  // * 1219462969 ≡ 0x48AF8739
  // * 2198809297 ≡ 0x830F2AD1
  // * 3752945107 ≡ 0xDFB165D3
  // * 3772857469 ≡ 0xE0E13C7D
  // * 3952396501 ≡ 0xEB94C8D5
  // * 4171332133 ≡ 0xF8A17A25

  @Test
  void basicModexpTest() {
    final Bytes bytecode =
        BytecodeCompiler.newProgram()
            .push(0)
            .push(0)
            .push(0)
            .push(0)
            .push(0)
            .push(0x05) // address
            .push(0xffff) // gas
            .op(OpCode.CALL)
            .op(OpCode.POP)
            .compile();
    BytecodeRunner.of(bytecode).run();
  }

  @Test
  void basicNonTrivialModexpTest() {
    final int base = 2;
    final int exp = 5;
    final int mod = 7;
    final Bytes bytecode =
        BytecodeCompiler.newProgram()
            .push(1)
            .push(BBS_MIN_OFFSET)
            .op(OpCode.MSTORE)
            .push(1)
            .push(EBS_MIN_OFFSET)
            .op(OpCode.MSTORE)
            .push(1)
            .push(MBS_MIN_OFFSET)
            .op(OpCode.MSTORE)
            .push(base)
            .push(BASE_MIN_OFFSET)
            .op(OpCode.MSTORE8)
            .push(exp)
            .push(BASE_MIN_OFFSET + 1)
            .op(OpCode.MSTORE8)
            .push(mod)
            .push(BASE_MIN_OFFSET + 2)
            .op(OpCode.MSTORE8)
            .push(MODEXP_COMPONENT_BYTE_SIZE) // retLength
            .push(0) // retOffset
            .push(BASE_MIN_OFFSET + 3) // argLength
            .push(0) // argOffset
            .push(0) // value
            .push(0x05) // address
            .push(0xffff) // gas
            .op(OpCode.CALL)
            .op(OpCode.POP)
            .compile();
    BytecodeRunner.of(bytecode).run();
  }

  @Test
  void testUnpaddedModexp() {

    String hexBase = "407CB5AD";
    String hexExpn = "40BDB1ED";
    String hexModl = "48AF8739";

    BytecodeCompiler program =
        preparingBaseExponentAndModulusForModexpAndRunningVariousModexps(hexBase, hexExpn, hexModl);

    BytecodeRunner.of(program.compile()).run();
  }

  @Test
  void testPaddedModexp() {

    String hexBase = "00407CB5AD";
    String hexExpn = "40BDB1ED";
    String hexModl = "000048AF8739";

    BytecodeCompiler program =
        preparingBaseExponentAndModulusForModexpAndRunningVariousModexps(hexBase, hexExpn, hexModl);

    BytecodeRunner.of(program.compile()).run();
  }

  int byteSize(String hexString) {
    return (hexString.length() + 1) / 2;
  }

  BytecodeCompiler preparingBaseExponentAndModulusForModexpAndRunningVariousModexps(
      String hexBase, String hexExpn, String hexModl) {

    BytecodeCompiler program = preparingBaseExponentAndModulusForModexp(hexBase, hexExpn, hexModl);

    int bbs = byteSize(hexBase);
    int ebs = byteSize(hexExpn);
    int mbs = byteSize(hexModl);

    List<Integer> returnAtCapacityList = List.of(0, 1, mbs - 1, mbs, mbs + 1, 31, 32);
    List<Integer> callDataSizeList =
        List.of(
            0,
            1,
            31,
            32,
            33,
            63,
            64,
            65,
            95,
            96,
            97,
            96 + (bbs - 1),
            96 + bbs,
            96 + bbs + 1,
            96 + bbs + (ebs - 1),
            96 + bbs + ebs,
            96 + bbs + ebs + 1,
            96 + bbs + ebs + (mbs - 1),
            96 + bbs + ebs + mbs,
            96 + bbs + ebs + mbs + 1);

    for (int returnAtCapacity : returnAtCapacityList) {
      for (int callDataSize : callDataSizeList) {
        appendParametrizedModexpCall(program, returnAtCapacity, callDataSize);
      }
    }

    return program;
  }

  void appendParametrizedModexpCall(
      BytecodeCompiler program, int returnAtCapacity, int callDataSize) {
    program
        .push(returnAtCapacity) // r@c
        .push(Bytes.fromHexString("0100")) // r@o
        .push(callDataSize) // cds
        .push(Bytes.fromHexString("")) // cdo
        .push(0x04) // address (here: MODEXP)
        .push(Bytes.fromHexString("ffff")) // gas
        .op(OpCode.DELEGATECALL)
        .op(OpCode.POP);
  }

  BytecodeCompiler preparingBaseExponentAndModulusForModexp(
      String hexBase, String hexExpn, String hexModl) {

    int bbs = byteSize(hexBase);
    int ebs = byteSize(hexExpn);
    int mbs = byteSize(hexModl);
    int baseOffset = 64 + bbs;
    int expnOffset = 64 + bbs + ebs;
    int modlOffset = 64 + bbs + ebs + mbs;
    return BytecodeCompiler.newProgram()
        .push(byteSize(hexBase))
        .push("00")
        .op(OpCode.MSTORE) // this sets bbs = 4
        .push(byteSize(hexExpn))
        .push("20")
        .op(OpCode.MSTORE) // this sets ebs = 4
        .push(byteSize(hexModl))
        .push("40")
        .op(OpCode.MSTORE) // this sets mbs = 4
        // to read call data 32 + 32 + 32 + 4 + 4 + 4 = 108 bytes are sufficient
        .push(hexBase)
        .push(baseOffset)
        .op(OpCode.MSTORE) // this sets the base
        .push(hexExpn)
        .push(expnOffset)
        .op(OpCode.MSTORE) // this sets the exponent
        .push(hexModl)
        .push(modlOffset)
        .op(OpCode.MSTORE) // this sets the modulus
    ;
  }

  @Test
  void variationsOnEmptyCalls() {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    List<Integer> callDataSizeList = List.of(0, 1, 31, 32, 33, 63, 64, 65, 95, 96, 97, 128);
    for (int callDataSize : callDataSizeList) {
      appendAllZeroCallDataModexpCalls(program, callDataSize);
    }

    BytecodeRunner.of(program.compile()).run();
  }

  void appendAllZeroCallDataModexpCalls(BytecodeCompiler program, int callDataSize) {
    program
        .push(Bytes.fromHexString("0200")) // rds 0x0200 ≡ 512 in decimal
        .push(Bytes.fromHexString("0200")) // rdo
        .push(callDataSize) // cds
        .push(Bytes.fromHexString("00")) // cdo
        .push(0x04) // i.e. MODEXP
        .push(Bytes.fromHexString("ffff")) // gas
        .op(OpCode.STATICCALL)
        .op(OpCode.POP);
  }
}
