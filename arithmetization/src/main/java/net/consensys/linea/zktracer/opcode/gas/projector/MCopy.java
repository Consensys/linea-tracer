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

package net.consensys.linea.zktracer.opcode.gas.projector;

import static org.hyperledger.besu.evm.internal.Words.clampedToLong;

import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.gascalculator.GasCalculator;

public final class MCopy extends DataCopy {
  private long sourceOffset = 0;

  public MCopy(GasCalculator gc, MessageFrame frame) {
    super(gc, frame);
    if (frame.stackSize() > 2) {
      sourceOffset = clampedToLong(frame.getStackItem(1));
    }
  }

  @Override
  protected long offset() {
    return Math.max(sourceOffset, targetOffset);
  }
}
