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

public final class Log extends GasProjection {
  private final MessageFrame frame;
  private long offset = 0;
  private long size = 0;
  private final int numTopics;

  public Log(MessageFrame frame, int numTopics) {
    this.frame = frame;
    this.numTopics = numTopics;
    if (frame.stackSize() > 1) {
      this.offset = clampedToLong(frame.getStackItem(0));
      this.size = clampedToLong(frame.getStackItem(1));
    }
  }

  @Override
  public long staticGas() {
    switch (numTopics) {
      case 0 -> {
        return GlobalConstants.GAS_CONST_G_LOG;
      }
      case 1 -> {
        return GlobalConstants.GAS_CONST_G_LOG + GlobalConstants.GAS_CONST_G_LOG_TOPIC;
      }
      case 2 -> {
        return GlobalConstants.GAS_CONST_G_LOG + 2 * GlobalConstants.GAS_CONST_G_LOG_TOPIC;
      }
      case 3 -> {
        return GlobalConstants.GAS_CONST_G_LOG + 3 * GlobalConstants.GAS_CONST_G_LOG_TOPIC;
      }
      case 4 -> {
        return GlobalConstants.GAS_CONST_G_LOG + 4 * GlobalConstants.GAS_CONST_G_LOG_TOPIC;
      }
      default -> throw new IllegalStateException("Unexpected value: " + numTopics);
    }
  }

  @Override
  public long memoryExpansion() {
    return gc.memoryExpansionGasCost(frame, offset, size);
  }

  @Override
  public long largestOffset() {
    return size == 0 ? 0 : Words.clampedAdd(offset, size);
  }

  @Override
  public long linearPerByte() {
    return linearCost(GlobalConstants.GAS_CONST_G_LOG_DATA, size, 1);
  }
}
