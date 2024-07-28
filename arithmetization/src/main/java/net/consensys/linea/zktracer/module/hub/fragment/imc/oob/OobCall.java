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

package net.consensys.linea.zktracer.module.hub.fragment.imc.oob;

import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.module.hub.fragment.TraceSubFragment;
import org.apache.tuweni.bytes.Bytes;

/** This interface defines the API required to execute a call to the OOB module. */
@RequiredArgsConstructor
public abstract class OobCall implements TraceSubFragment {
  // TODO: move these constants somewhere else
  public Bytes ZERO = Bytes.EMPTY;
  public Bytes ONE = Bytes.of(1);

  final OobInstruction oobInstruction;

  public int oobInstructionValue() {
    return oobInstruction.getValue();
  }

  public abstract net.consensys.linea.zktracer.module.oob.Trace trace(
      net.consensys.linea.zktracer.module.oob.Trace trace);
}
