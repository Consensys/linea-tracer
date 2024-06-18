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

package net.consensys.linea.zktracer.module.hub.fragment.imc.call.oob;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.module.hub.Trace;
import net.consensys.linea.zktracer.module.hub.fragment.TraceSubFragment;
import org.apache.tuweni.bytes.Bytes;

/** This interface defines the API required to execute a call to the OOB module. */
@Accessors(fluent = true)
@Setter
public abstract class OobCall implements TraceSubFragment {
  // TODO: check if we need 0 instead of EMPTY
  Bytes data1 = Bytes.EMPTY;
  Bytes data2 = Bytes.EMPTY;
  Bytes data3 = Bytes.EMPTY;
  Bytes data4 = Bytes.EMPTY;
  Bytes data5 = Bytes.EMPTY;
  Bytes data6 = Bytes.EMPTY;
  Bytes data7 = Bytes.EMPTY;
  Bytes data8 = Bytes.EMPTY;

  /** The instruction to trigger in the OOB for this call. */
  public abstract int oobInstruction();

  @Override
  public Trace trace(Trace trace) {
    return trace
        .pMiscOobFlag(true)
        .pMiscOobData1(data1)
        .pMiscOobData2(data2)
        .pMiscOobData3(data3)
        .pMiscOobData4(data4)
        .pMiscOobData5(data5)
        .pMiscOobData6(data6)
        .pMiscOobData7(data7)
        .pMiscOobData8(data8)
        .pMiscOobInst(this.oobInstruction());
  }
}
