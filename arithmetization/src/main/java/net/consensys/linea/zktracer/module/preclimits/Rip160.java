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

package net.consensys.linea.zktracer.module.preclimits;

import java.util.Stack;

import lombok.extern.slf4j.Slf4j;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.ModuleTrace;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.internal.Words;

@Slf4j
public final class Rip160 implements Module {
  private final Stack<Integer> counts = new Stack<Integer>(); // TODO

  @Override
  public String jsonKey() {
    return null;
  }

  private final int precompileBaseGasFee = 600;
  private final int precompileGasFeePerEWord = 120;
  private final int ripmd160BlockSize = 64 * 8; // TODO
  private final int ripmd160MaxDataLengthBitLength =
      64; // The length of the data to be hashed is 2**64 maximum. //TODO
  private final int ripmd160NbPaddedOne = 1; // TODO

  @Override
  public void enterTransaction() {
    counts.push(0);
  }

  @Override
  public void popTransaction() {
    counts.pop();
  }

  @Override
  public void tracePreOpcode(MessageFrame frame) {
    final OpCode opCode = OpCode.of(frame.getCurrentOperation().getOpcode());

    switch (opCode) {
      case CALL, STATICCALL, DELEGATECALL, CALLCODE -> {
        final Address target = Words.toAddress(frame.getStackItem(1));
        if (target == Address.SHA256) {
          long dataByteLength = 0;
          switch (opCode) {
            case CALL, CALLCODE -> dataByteLength = Words.clampedToLong(frame.getStackItem(4));
            case DELEGATECALL, STATICCALL -> dataByteLength =
                Words.clampedToLong(frame.getStackItem(3));
          }
          final int blockCount =
              (int)
                      (dataByteLength * 8
                          + ripmd160NbPaddedOne
                          + ripmd160MaxDataLengthBitLength
                          + (ripmd160BlockSize - 1))
                  / ripmd160BlockSize; // TODO

          final long wordCount = (dataByteLength + 31) / 32;
          final long gasPaid = Words.clampedToLong(frame.getStackItem(0));
          final long gasNeeded = precompileBaseGasFee + precompileGasFeePerEWord * wordCount;

          if (gasNeeded <= gasPaid) {
            this.counts.push(this.counts.pop() + blockCount); // TODO
          }
        }
      }
      default -> {}
    }
  }

  @Override
  public int lineCount() {
    return this.counts.stream().mapToInt(x -> x).sum();
  }

  @Override
  public ModuleTrace commit() {
    return null;
  }
}
