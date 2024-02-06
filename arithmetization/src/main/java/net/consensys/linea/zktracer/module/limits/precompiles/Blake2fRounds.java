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

package net.consensys.linea.zktracer.module.limits.precompiles;

import java.nio.MappedByteBuffer;
import java.util.List;
import java.util.Stack;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.consensys.linea.zktracer.ColumnHeader;
import net.consensys.linea.zktracer.module.Module;
import net.consensys.linea.zktracer.module.blake2fmodexpdata.Blake2fComponents;
import net.consensys.linea.zktracer.module.blake2fmodexpdata.Blake2fModexpData;
import net.consensys.linea.zktracer.module.blake2fmodexpdata.Blake2fModexpDataOperation;
import net.consensys.linea.zktracer.module.hub.Hub;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.internal.Words;

@RequiredArgsConstructor
public final class Blake2fRounds implements Module {
  private static final int BLAKE2f_INPUT_SIZE = 213;
  private final Hub hub;

  @Getter private final Blake2fModexpData data = new Blake2fModexpData();

  private int lastDataCallHubStamp = 0;

  private final Stack<Integer> counts = new Stack<>();

  @Override
  public String moduleKey() {
    return "PRECOMPILE_BLAKE2F_ROUNDS";
  }

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
    final OpCode opCode = hub.opCode();

    if (opCode.isAnyOf(OpCode.CALL, OpCode.STATICCALL, OpCode.DELEGATECALL, OpCode.CALLCODE)) {
      final Address target = Words.toAddress(frame.getStackItem(1));
      if (target.equals(Address.BLAKE2B_F_COMPRESSION)) {
        long length = 0;
        long offset = 0;
        switch (opCode) {
          case CALL, CALLCODE -> {
            length = Words.clampedToLong(frame.getStackItem(4));
            offset = Words.clampedToLong(frame.getStackItem(3));
          }
          case DELEGATECALL, STATICCALL -> {
            length = Words.clampedToLong(frame.getStackItem(3));
            offset = Words.clampedToLong(frame.getStackItem(2));
          }
        }

        final Bytes inputData = frame.shadowReadMemory(offset, length);

        if (length == BLAKE2f_INPUT_SIZE) {
          final int f = inputData.get(BLAKE2f_INPUT_SIZE - 1);
          if (f == 0 || f == 1) {
            final Bytes r = inputData.slice(0, 4); // The number of round is equal to the gas to pay
            final Bytes data = inputData.slice(4, BLAKE2f_INPUT_SIZE - 4);

            final long gasPaid = Words.clampedToLong(frame.getStackItem(0));
            final int rInt = r.toInt();
            if (gasPaid >= rInt) {
              this.lastDataCallHubStamp =
                  this.data.call(
                      new Blake2fModexpDataOperation(
                          hub.stamp(),
                          lastDataCallHubStamp,
                          null,
                          new Blake2fComponents(inputData, data, r, Bytes.of(f))));
              this.counts.push(this.counts.pop() + rInt);
            }
          }
        }
      }
    }
  }

  @Override
  public int lineCount() {
    return this.counts.stream().mapToInt(x -> x).sum();
  }

  @Override
  public List<ColumnHeader> columnsHeaders() {
    throw new UnsupportedOperationException("should never be called");
  }

  @Override
  public void commit(List<MappedByteBuffer> buffers) {
    throw new UnsupportedOperationException("should never be called");
  }
}
