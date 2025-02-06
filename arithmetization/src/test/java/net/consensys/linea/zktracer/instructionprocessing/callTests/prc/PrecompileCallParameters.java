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

public class PrecompileCallParameters {
  public final OpCode call;
  public GasParameter gas;
  public final HashPrecompile prc;
  public int value;
  public final CallOffset cdo;
  public final CallSize cds;
  public final CallOffset rao;
  public final CallSize rac;
  public final RelativeRangePosition relPos;

    public PrecompileCallParameters(OpCode call, GasParameter gas, HashPrecompile prc, int value, CallOffset cdo, CallSize cds, CallOffset rao, CallSize rac, RelativeRangePosition relPos) {
        this.call = call;
        this.gas = gas;
        this.prc = prc;
        this.value = value;
        this.cdo = cdo;
        this.cds = cds;
        this.rao = rao;
        this.rac = rac;
        this.relPos = relPos;
    }

  public PrecompileCallParameters next() {
    return new PrecompileCallParameters(call, gas, prc.next(), value, cdo, cds, rao, rac, relPos);
  }
}
