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
package net.consensys.linea.zktracer.instructionprocessing.callTests.prc;

import net.consensys.linea.zktracer.opcode.OpCode;

public class ModexpCallParameters {

  public final OpCode call;
  public final GasParameter gas;
  public final ModexpCallDataParameters callData;
  public final ReturnAtParameter returnAt;
  public final RelativeRangePosition relPos;
  public final boolean willRevert;

  public ModexpCallParameters(
      OpCode call,
      GasParameter gas,
      ModexpCallDataParameters callData,
      ReturnAtParameter returnAt,
      RelativeRangePosition relPos,
      boolean willRevert) {
    this.call = call;
    this.gas = gas;
    this.callData = callData;
    this.returnAt = returnAt;
    this.relPos = relPos;
    this.willRevert = willRevert;
  }
}
