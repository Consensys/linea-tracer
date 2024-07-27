/*
 * Copyright ConsenSys Inc.
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

package net.consensys.linea.zktracer.module.hub.section.call.precompileSubsection;

import static net.consensys.linea.zktracer.module.hub.fragment.scenario.PrecompileScenarioFragment.PrecompileFlag.PRC_SHA2_256;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.module.hub.section.call.CallSection;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import org.hyperledger.besu.datatypes.Transaction;
import org.hyperledger.besu.evm.worldstate.WorldView;

public class Sha2SubSection extends PrecompileSubsection {
  public Sha2SubSection(Hub hub, CallSection callSection) {
    super(hub, callSection);
    precompileScenarioFragment.setFlag(PRC_SHA2_256);
  }

  // 4 = 1 + 3 (scenario row + up to 3 miscellaneous fragments)
  @Override
  protected short maxNumberOfLines() {
    return (short) (successBit ? 4 : 2);
  }

  @Override
  public void resolveUponExitingContext(Hub hub, CallFrame frame) {
    // TODO
  }

  @Override
  public void resolveAtContextReEntry(Hub hub, CallFrame frame) {
    super.resolveAtContextReEntry(hub, frame);
  }

  @Override
  public void resolvePostTransaction(
      Hub hub, WorldView state, Transaction tx, boolean isSuccessful) {}
}
