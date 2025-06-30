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

package net.consensys.linea.zktracer.module.mxp;

import static net.consensys.linea.zktracer.Trace.GAS_CONST_G_MEMORY;
import static org.hyperledger.besu.evm.internal.Words.clampedAdd;
import static org.hyperledger.besu.evm.internal.Words.clampedMultiply;

import net.consensys.linea.zktracer.opcode.OpCode;

public class MxpUtils {
  public static boolean isSingleOffsetOpcode(OpCode opCode) {
    return opCode == OpCode.MLOAD
        || opCode == OpCode.MSTORE
        || opCode == OpCode.MSTORE8
        || opCode == OpCode.REVERT
        || opCode == OpCode.RETURN
        || opCode.isLog()
        || opCode == OpCode.SHA3
        || opCode.isCopy()
        || opCode.isCreate();
  }

  public static boolean isDoubleOffsetOpcode(OpCode opCode) {
    return opCode == OpCode.MCOPY || opCode.isCall();
  }

  public static boolean isWordPricingOpcode(OpCode opCode) {
    return opCode == OpCode.SHA3 || opCode.isCopy() || opCode.isCreate() || opCode == OpCode.MCOPY;
  }

  public static boolean isBytePricingOpcode(OpCode opCode) {
    return opCode == OpCode.MSIZE
        || opCode == OpCode.MLOAD
        || opCode == OpCode.MSTORE
        || opCode == OpCode.MSTORE8
        || opCode == OpCode.REVERT
        || opCode == OpCode.RETURN
        || opCode.isLog()
        || opCode.isCall();
  }

  // This is a copy and past from FrontierGasCalculator.java
  public static long memoryCost(final long length) {
    final long lengthSquare = clampedMultiply(length, length);
    final long base =
        (lengthSquare == Long.MAX_VALUE)
            ? clampedMultiply(length / 512, length)
            : lengthSquare / 512;
    return clampedAdd(clampedMultiply(GAS_CONST_G_MEMORY, length), base);
  }
}
