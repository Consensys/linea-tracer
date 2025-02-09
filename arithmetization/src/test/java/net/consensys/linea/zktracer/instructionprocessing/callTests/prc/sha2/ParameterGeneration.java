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

import static net.consensys.linea.zktracer.opcode.OpCode.*;
import static net.consensys.linea.zktracer.opcode.OpCode.STATICCALL;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import net.consensys.linea.zktracer.instructionprocessing.callTests.prc.*;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.junit.jupiter.params.provider.Arguments;

public class ParameterGeneration {

  /**
   * Generates test parameters for the happy path tests.
   *
   * @return Stream of test parameters
   */
  public static Stream<Arguments> happyPathHashPrecompileParameters() {
    List<OpCode> CallOpCodes = List.of(CALL, CALLCODE, DELEGATECALL, STATICCALL);

    List<Arguments> argumentsList = new ArrayList<>();
    for (OpCode callOpcode : CallOpCodes) {
      for (GasParameter gas : GasParameter.values()) {
        for (HashPrecompile precompile : HashPrecompile.values()) {
          for (CallOffset cdo : CallOffset.values()) {
            for (CallSize cds : CallSize.values()) {
              for (CallOffset rao : CallOffset.values()) {
                for (CallSize rac : CallSize.values()) {
                  for (RelativeRangePosition relPos : RelativeRangePosition.values()) {
                    argumentsList.add(
                        Arguments.of(
                            new PrecompileCallParameters(
                                callOpcode, gas, precompile, 1, cdo, cds, rao, rac, relPos)));
                  }
                }
              }
            }
          }
        }
      }
    }
    return argumentsList.stream();
  }
}
