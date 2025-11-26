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
package net.consensys.linea.zktracer.precompiles.modexp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.consensys.linea.reporting.TracerTestBase;
import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.testing.ToyAccount;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.commons.math3.util.Pair;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Wei;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ModexpEIP7883Tests extends TracerTestBase {

  // See https://github.com/Consensys/linea-tracer/issues/2496
  static final List<Pair<Integer, Integer>> bbsMbsPairs =
      List.of(Pair.create(0, 0), Pair.create(0, 3), Pair.create(21, 23), Pair.create(56, 55));

  static final List<Integer> ebss = List.of(0, 1, 16, 27, 32, 39, 173);

  // Pre-computed exponents for each ebs value
  static final Map<Integer, List<String>> ebsToExponentLeadingWords =
      ebss.stream()
          .collect(
              Collectors.toMap(
                  ebsItem -> ebsItem,
                  ebsItem -> {
                    final int minEbs32 = Math.min(ebsItem, 32);
                    List<String> exponentLeadingWords = new ArrayList<>();
                    for (int z = 0; z <= 8 * minEbs32; z++) {
                      exponentLeadingWords.add("0".repeat(8 * minEbs32 - z) + "1".repeat(z));
                    }
                    return exponentLeadingWords;
                  }));

  // Support method to compute cds given bbs, ebs, mbs
  static List<Integer> cdss(Integer bbs, Integer ebs, Integer mbs) {
    List<Integer> cds = new ArrayList<>();
    for (Integer extra : List.of(ebs / 2, ebs, ebs + mbs)) {
      cds.add(bbs + extra);
    }
    return cds;
  }

  @ParameterizedTest
  @MethodSource("modexpEIP7883TestSource")
  void modexpEIP7883Test(int bbs, int ebs, int mbs, int cds, String exponentLeadingWord) {
    // TODO

  }

  private void modexpEIP7883TestBody(int bbs, int ebs, int mbs, int cds, String exponentLeadingWord, TestInfo testInfo) {
    final Bytes input = Bytes.fromHexString("...");

    BytecodeCompiler program = BytecodeCompiler.newProgram(chainConfig);

    final Address codeOwnerAddress = Address.fromHexString("0xC0DE");
    final ToyAccount codeOwnerAccount =
      ToyAccount.builder()
        .balance(Wei.of(0))
        .nonce(1)
        .address(codeOwnerAddress)
        .code(input)
        .build();

    // First place the parameters in memory
    // Copy to targetOffset the code of codeOwnerAccount
    program
      .push(codeOwnerAddress)
      .op(OpCode.EXTCODESIZE) // size
      .push(0) // offset
      .push(0) // targetOffset
      .push(codeOwnerAddress) // address
      .op(OpCode.EXTCODECOPY);

    // Do the call
    program
      .push(mbs) // retSize
      .push(input.size()) // retOffset
      .push(input.size()) // argSize
      .push(0) // argOffset
      .push(Address.MODEXP) // address
      .push(Bytes.fromHexStringLenient("0xFFFFFFFF")) // gas
      .op(OpCode.STATICCALL)
      .op(OpCode.RETURNDATASIZE)
      .op(OpCode.JUMPDEST, 32);

    BytecodeRunner bytecodeRunner = BytecodeRunner.of(program.compile());
    bytecodeRunner.run(List.of(codeOwnerAccount), chainConfig, testInfo);
  }

  static Stream<Arguments> modexpEIP7883TestSource() {
    List<Arguments> arguments = new ArrayList<>();
    for (Pair<Integer, Integer> bbsMbs : bbsMbsPairs) {
      Integer bbs = bbsMbs.getFirst();
      Integer mbs = bbsMbs.getSecond();
      for (Integer ebs : ebss) {
        List<Integer> cdss = cdss(bbs, ebs, mbs);
        List<String> exponentLeadsForEbs = ebsToExponentLeadingWords.get(ebs);
          for (Integer cds : cdss) {
            for (String exponentLeadForEbs : exponentLeadsForEbs) {
            arguments.add(Arguments.of(bbs, ebs, mbs, cds, exponentLeadForEbs));
          }
        }
      }
    }
    return arguments.stream();
  }
}
