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
import net.consensys.linea.zktracer.types.EWord;
import org.hyperledger.besu.evm.frame.MessageFrame;

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

  public static EWord[] getSizesAndOffsets(MessageFrame frame) {
    OpCode opCode = OpCode.of(frame.getCurrentOperation().getOpcode());
    EWord[] result = new EWord[4];
    switch (opCode) {
      case MSIZE -> {}
      case MLOAD -> {
        result[1] = EWord.of(frame.getStackItem(0));
      }
      case MSTORE -> {
        result[1] = EWord.of(frame.getStackItem(0));
        result[0] = EWord.of(32);
      }
      case MSTORE8 -> {
        result[1] = EWord.of(frame.getStackItem(0));
        result[0] = EWord.of(1);
      }
      case REVERT, RETURN, LOG0, LOG1, LOG2, LOG3, LOG4, SHA3 -> {
        result[1] = EWord.of(frame.getStackItem(0));
        result[0] = EWord.of(frame.getStackItem(1));
      }
      case CALLDATACOPY, RETURNDATACOPY, CODECOPY -> {
        result[1] = EWord.of(frame.getStackItem(0));
        result[0] = EWord.of(frame.getStackItem(2));
      }
      case EXTCODECOPY -> {
        result[1] = EWord.of(frame.getStackItem(1));
        result[0] = EWord.of(frame.getStackItem(3));
      }
      case CREATE, CREATE2 -> {
        result[1] = EWord.of(frame.getStackItem(1));
        result[0] = EWord.of(frame.getStackItem(2));
      }
      case MCOPY -> {
        result[1] = EWord.of(frame.getStackItem(0));
        result[3] = EWord.of(frame.getStackItem(1));
        result[2] = EWord.of(frame.getStackItem(2));
      }
      case CALL, CALLCODE -> {
        result[1] = EWord.of(frame.getStackItem(3));
        result[0] = EWord.of(frame.getStackItem(4));
        result[3] = EWord.of(frame.getStackItem(5));
        result[2] = EWord.of(frame.getStackItem(6));
      }
      case DELEGATECALL, STATICCALL -> {
        result[1] = EWord.of(frame.getStackItem(2));
        result[0] = EWord.of(frame.getStackItem(3));
        result[3] = EWord.of(frame.getStackItem(4));
        result[2] = EWord.of(frame.getStackItem(5));
      }
      default -> throw new IllegalStateException("Unexpected value: " + opCode);
    }
    return result;
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
