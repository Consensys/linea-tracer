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

package net.consensys.linea.zktracer.opcode.gas.projector;

import static net.consensys.linea.zktracer.module.constants.GlobalConstants.GAS_CONST_G_COPY;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.WORD_SIZE;
import static org.hyperledger.besu.evm.internal.Words.clampedToLong;

import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.internal.Words;

@Slf4j
public final class DataCopy extends GasProjection {
  private final MessageFrame frame;
  private long destOffset = 0;
  private long len = 0;

  public DataCopy(MessageFrame frame, OpCode opCode) {
    this.frame = frame;
    if (frame.stackSize() > 2) {
      destOffset = clampedToLong(frame.getStackItem(0));
      len = clampedToLong(frame.getStackItem(2));
    }
  }

  @Override
  public long staticGas() {
    return gc.getVeryLowTierGasCost();
  }

  @Override
  public long memoryExpansion() {
    return gc.memoryExpansionGasCost(frame, destOffset, len);
  }

  @Override
  public long linearPerWord() {
    return linearCost(GAS_CONST_G_COPY, len, WORD_SIZE);
  }

  @Override
  public long largestOffset() {
    return len == 0 ? 0 : Words.clampedAdd(destOffset, len);
  }
}
