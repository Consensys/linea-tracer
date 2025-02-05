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
package net.consensys.linea.zktracer.instructionprocessing.callTests.prc.sha2;

import static net.consensys.linea.zktracer.instructionprocessing.callTests.Utilities.*;
import static net.consensys.linea.zktracer.instructionprocessing.callTests.prc.RelativeRangePosition.OVERLAP;
import static net.consensys.linea.zktracer.instructionprocessing.utilities.MonoOpCodeSmcs.accounts;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.WORD_SIZE;
import static net.consensys.linea.zktracer.opcode.OpCode.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.zktracer.instructionprocessing.callTests.prc.CallOffset;
import net.consensys.linea.zktracer.instructionprocessing.callTests.prc.CallSize;
import net.consensys.linea.zktracer.instructionprocessing.callTests.prc.HashPrecompile;
import net.consensys.linea.zktracer.instructionprocessing.callTests.prc.RelativeRangePosition;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.hyperledger.besu.datatypes.Address;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * <b>Happy path</b> tests for <b>SHA2-256</b>, <b>RIPEMD-160</b> and <b>IDENTITY</b>, i.e. the
 * present tests we will only consider calls to these precompiles where
 *
 * <p>- the precompile is provided with sufficient gas (ensuring <b>scenario/PRC_SUCCESS</b>)
 *
 * <p>- nothing <b>REVERT</b>s (thus value only matters in terms of pricing)
 *
 * <p>To avoid trivialities we pre-populate memory with nonzero values. We force
 * interactions between the <b>call data</b> range, the <b>return at</b> range and <b>return
 * data</b> ranges in the <b>OVERLAP</b> case.
 *
 * <p>After the call we interact with return data via <b>RETURNDATA[SIZE/COPY]</b> and
 * <b>MLOAD</b>.
 */
public class HappyPathHashPrecompileTests {

  @ParameterizedTest
  @MethodSource("happyPathHashPrecompileParameters")
  public void happyPathHashPrecompileTests(
      OpCode callOpcode,
      HashPrecompile precompile,
      CallOffset cdo,
      CallSize cds,
      CallOffset rao,
      CallSize rac,
      RelativeRangePosition relPos) {

    BytecodeCompiler program = BytecodeCompiler.newProgram();

    populateMemory(program);
    appendHashPrecompileCall(program, callOpcode, precompile, cdo, cds, rao, rac, relPos);
    copyHalfOfReturnDataOmittingTheFirstThirdOfIt(program, relPos == OVERLAP ? 4 : 4 * WORD_SIZE);
    loadFirstReturnDataWordOntoStack(program, relPos == OVERLAP ? 15 : 5 * WORD_SIZE);
    BytecodeRunner.of(program.compile()).run(accounts);
  }

  private static Stream<Arguments> happyPathHashPrecompileParameters() {
    List<OpCode> CallOpCodes = List.of(CALL, CALLCODE, DELEGATECALL, STATICCALL);

    List<Arguments> argumentsList = new ArrayList<>();
    for (CallSize rac : CallSize.values()) {
      for (CallOffset rao : CallOffset.values()) {
        for (CallSize cds : CallSize.values()) {
          for (CallOffset cdo : CallOffset.values()) {
            for (RelativeRangePosition relPos : RelativeRangePosition.values()) {
              for (HashPrecompile precompile : HashPrecompile.values()) {
                for (OpCode callOpcode : CallOpCodes) {
                  argumentsList.add(
                      Arguments.of(callOpcode, precompile, cdo, cds, rao, rac, relPos));
                }
              }
            }
          }
        }
      }
    }
    return argumentsList.stream();
  }

  public void appendHashPrecompileCall(
      BytecodeCompiler program,
      OpCode callOpcode,
      HashPrecompile hashPrecompile,
      CallOffset cdo,
      CallSize cds,
      CallOffset rao,
      CallSize rac,
      RelativeRangePosition relativeRangePosition) {

    // call data can occupy up to 52 bytes; it fits into the first 2 words
    switch (cdo) {
      case ALIGNED -> program.push(0);
      case MISALIGNED -> program.push(13);
      case INFINITY -> program.push("ff".repeat(32));
    }

    switch (cds) {
      case ZERO -> program.push(0);
      case WORD -> program.push(WORD_SIZE);
      case OTHER -> program.push(39);
    }

    // if DISJOINT the "return at range" lives among words 3 and 4 or RAM
    switch (rao) {
      case ALIGNED -> program.push(relativeRangePosition == OVERLAP ? 0 : 2 * WORD_SIZE);
      case MISALIGNED -> program.push((relativeRangePosition == OVERLAP ? 0 : 2 * WORD_SIZE) + 4);
      case INFINITY -> program.push("ff".repeat(32));
    }

    switch (rac) {
      case ZERO -> program.push(0);
      case WORD -> program.push(WORD_SIZE);
      case OTHER -> program.push(58);
    }

    Address prcAddress =
        switch (hashPrecompile) {
          case SHA256 -> Address.SHA256;
          case RIPEMD160 -> Address.RIPEMD160;
          case IDENTITY -> Address.ID;
        };
    program.push(prcAddress);

    if (callOpcode.callHasValueArgument()) {
      program.push(1);
    }

    program.op(GAS);
    program.op(callOpcode);
  }
}
