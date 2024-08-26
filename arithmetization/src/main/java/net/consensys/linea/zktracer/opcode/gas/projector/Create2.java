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

import static org.hyperledger.besu.evm.internal.Words.clampedToLong;

import net.consensys.linea.zktracer.module.constants.GlobalConstants;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.internal.Words;

public final class Create2 extends GasProjection {
  private final MessageFrame frame;
  private long initCodeOffset = 0;
  private long initCodeLength = 0;

  public Create2(MessageFrame frame) {
    this.frame = frame;
    if (frame.stackSize() > 2) {
      this.initCodeOffset = clampedToLong(frame.getStackItem(1));
      this.initCodeLength = clampedToLong(frame.getStackItem(2));
    }
  }

  @Override
  public long staticGas() {
    return GlobalConstants.GAS_CONST_G_CREATE;
  }

  @Override
  public long memoryExpansion() {
    return gc.memoryExpansionGasCost(frame, initCodeOffset, initCodeLength);
  }

  @Override
  public long largestOffset() {
    return initCodeLength == 0 ? 0 : Words.clampedAdd(initCodeOffset, initCodeLength);
  }

  @Override
  public long linearPerWord() {
    return linearCost(GlobalConstants.GAS_CONST_G_KECCAK_256_WORD, initCodeLength, 32);
  }

  @Override
  public long rawStipend() {
    long currentGas = frame.getRemainingGas();
    long gasCost = this.staticGas() + this.memoryExpansion() + this.linearPerWord();

    if (gasCost > currentGas) {
      return 0;
    } else {
      return currentGas - currentGas / 64;
    }
  }

  @Override
  public long messageSize() {
    return this.initCodeLength;
  }
}
