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

package net.consensys.linea.zktracer.module.bin;

import lombok.experimental.Accessors;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes32;

@Accessors(fluent = true)
public class BinOperation {
  private final OpCode opCode;
  private final Bytes32 arg1;
  private final Bytes32 arg2;

  public BinOperation(OpCode opCode, Bytes32 arg1, Bytes32 arg2) {
    this.opCode = opCode;
    this.arg1 = arg1;
    this.arg2 = arg2;
  }

  public OpCode opCode() {
    return this.opCode;
  }

  public Bytes32 arg1() {
    return this.arg1;
  }

  public Bytes32 arg2() {
    return this.arg2;
  }
}
