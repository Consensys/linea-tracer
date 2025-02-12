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
package net.consensys.linea.zktracer.instructionprocessing.callTests.prc.modexp;

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
   * Generates test parameters for the happy path tests of <b>MODEXP</b>.
   *
   * @return Stream of test parameters
   */
  public static Stream<Arguments> happyPathParameterGeneration() {
    List<OpCode> CallOpCodes = List.of(CALL, CALLCODE, DELEGATECALL, STATICCALL);

    List<Arguments> argumentsList = new ArrayList<>();

    for (OpCode opCode : CallOpCodes) { // 4
      // for (GasParameter gas : GasParameter.values()) { // 5
      for (ByteSizeParameter bbs : ByteSizeParameter.values()) { // 4
        for (ByteSizeParameter ebs : ByteSizeParameter.values()) { // 4
          for (ByteSizeParameter mbs : ByteSizeParameter.values()) { // 4
            for (ModexpCallDataSizeParameter cds : ModexpCallDataSizeParameter.values()) { // 9
              for (ReturnAtParameter returnAt : ReturnAtParameter.values()) { // 4
                for (RelativeRangePosition relPos : RelativeRangePosition.values()) { // 2
                  argumentsList.add(
                      Arguments.of(
                          new ModexpCallParameters(
                              opCode,
                              GasParameter.FULL,
                              new ModexpCallDataParameters(bbs, ebs, mbs, cds),
                              returnAt,
                              relPos,
                              false)));

                  argumentsList.add(
                      Arguments.of(
                          new ModexpCallParameters(
                              opCode,
                              GasParameter.FULL,
                              new ModexpCallDataParameters(bbs, ebs, mbs, cds),
                              returnAt,
                              relPos,
                              false)));
                }
              }
            }
          }
        }
      }
    }
    // }
    return argumentsList.stream();
  }
}
