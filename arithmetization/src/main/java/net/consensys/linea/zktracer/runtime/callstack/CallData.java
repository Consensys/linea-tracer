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

package net.consensys.linea.zktracer.runtime.callstack;

import static com.google.common.base.Preconditions.checkArgument;

import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.types.MemoryRange;
import net.consensys.linea.zktracer.types.Range;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.evm.frame.MessageFrame;

@Accessors(fluent = true)
public class CallData extends MemoryRange {
  private static final CallData EMPTY = new CallData(0, 0, 0, Bytes.EMPTY);

  public static CallData empty(long callDataContextNumber) {
    return new CallData(callDataContextNumber, 0, 0, Bytes.EMPTY);
  }

  public static CallData empty() {
    return EMPTY;
  }

  public CallData(final long callDataContextNumber, final long callDataOffset, final long callDataSize, final Bytes data) {
    super(callDataContextNumber, callDataOffset, callDataSize, data);
  }
}
