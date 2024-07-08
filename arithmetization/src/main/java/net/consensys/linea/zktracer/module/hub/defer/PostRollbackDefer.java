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
package net.consensys.linea.zktracer.module.hub.defer;

import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.runtime.callstack.CallFrame;
import org.hyperledger.besu.evm.frame.MessageFrame;

// import org.hyperledger.besu.evm.worldstate.WorldView;

public interface PostRollbackDefer {
  /**
   * @param messageFrame access point to world state & accrued state
   * @param callFrame reference to call frame whose actions are to be undone
   */
  public void resolvePostRollback(Hub hub, MessageFrame messageFrame, CallFrame callFrame);
}
