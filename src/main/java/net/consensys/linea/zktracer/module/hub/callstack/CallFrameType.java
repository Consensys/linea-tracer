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

package net.consensys.linea.zktracer.module.hub.callstack;

import net.consensys.linea.zktracer.opcode.OpCode;

public enum CallFrameType {
  /** Executing deployment code */
  InitCode,
  /** Executing standard contract */
  Standard,
  /** Within a delegate call */
  Delegate,
  /** Within a static call */
  Static,
  /** Within a call code */
  CallCode,
  /** The bedrock context */
  Root;

  /**
   * Returns the kind of {@link CallFrameType} context that an opcode will create; throws if the
   * opcode does not create a new context.
   *
   * @param opCode a context-changing {@link OpCode}
   * @return the associated {@link CallFrameType}
   */
  public CallFrameType ofOpCode(OpCode opCode) {
    if (this.isStatic()) {
      return Static;
    } else {
      return switch (opCode) {
        case CREATE, CREATE2 -> InitCode;
        case DELEGATECALL -> Delegate;
        case CALLCODE -> CallCode;
        case STATICCALL -> Static;
        default -> {
          throw new IllegalStateException(String.valueOf(opCode));
        }
      };
    }
  }

  public boolean isStatic() {
    return this == Static;
  }
}
