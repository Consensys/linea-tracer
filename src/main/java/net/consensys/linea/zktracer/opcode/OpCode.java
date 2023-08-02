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

package net.consensys.linea.zktracer.opcode;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import net.consensys.linea.zktracer.opcode.mxp.MxpSettings;
import net.consensys.linea.zktracer.opcode.stack.Pattern;
import net.consensys.linea.zktracer.opcode.stack.StackSettings;

public enum OpCode {
  STOP(
      0x00,
      EnumSet.of(InstructionFlags.Stop),
      new StackSettings(Pattern.ZeroZero, 0, 0, 0, 0, Gas.gZero, false, false, false, false),
      EnumSet.noneOf(MemoryFlags.class),
      EnumSet.noneOf(ModuleFlags.class),
      new MxpSettings(0, 0, 0)),
  ADD(0x01, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  MUL(0x02, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  SUB(0x03, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  DIV(0x04, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  SDIV(0x05, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  MOD(0x06, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  SMOD(0x07, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  ADDMOD(0x8, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  MULMOD(0x9, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  EXP(0xa, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  SIGNEXTEND(0xb, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  LT(0x10, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  GT(0x11, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  SLT(0x12, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  SGT(0x13, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  EQ(0x14, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  ISZERO(0x15, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  AND(0x16, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  OR(0x17, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  XOR(0x18, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  NOT(0x19, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  BYTE(0x1a, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  SHL(0x1b, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  SHR(0x1c, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  SAR(0x1d, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  SHA3(0x20, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  ADDRESS(0x30, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  BALANCE(0x31, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  ORIGIN(0x32, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  CALLER(0x33, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  CALLVALUE(0x34, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  CALLDATALOAD(0x35, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  CALLDATASIZE(0x36, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  CALLDATACOPY(0x37, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  CODESIZE(0x38, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  CODECOPY(0x39, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  GASPRICE(0x3a, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  EXTCODESIZE(0x3b, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  EXTCODECOPY(0x3c, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  RETURNDATASIZE(0x3d, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  RETURNDATACOPY(0x3e, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  EXTCODEHASH(0x3f, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  BLOCKHASH(0x40, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  COINBASE(0x41, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  TIMESTAMP(0x42, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  NUMBER(0x43, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  DIFFICULTY(0x44, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  GASLIMIT(0x45, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  CHAINID(0x46, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  SELFBALANCE(0x47, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  BASEFEE(0x48, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  POP(0x50, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  MLOAD(0x51, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  MSTORE(0x52, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  MSTORE8(0x53, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  SLOAD(0x54, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  SSTORE(0x55, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  JUMP(0x56, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  JUMPI(0x57, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PC(0x58, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  MSIZE(0x59, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  GAS(0x5a, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  JUMPDEST(0x5b, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH0(0x5f, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH1(0x60, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH2(0x61, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH3(0x62, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH4(0x63, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH5(0x64, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH6(0x65, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH7(0x66, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH8(0x67, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH9(0x68, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH10(0x69, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH11(0x6a, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH12(0x6b, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH13(0x6c, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH14(0x6d, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH15(0x6e, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH16(0x6f, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH17(0x70, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH18(0x71, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH19(0x72, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH20(0x73, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH21(0x74, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH22(0x75, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH23(0x76, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH24(0x77, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH25(0x78, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH26(0x79, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH27(0x7a, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH28(0x7b, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH29(0x7c, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH30(0x7d, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH31(0x7e, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  PUSH32(0x7f, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  DUP1(0x80, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  DUP2(0x81, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  DUP3(0x82, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  DUP4(0x83, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  DUP5(0x84, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  DUP6(0x85, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  DUP7(0x86, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  DUP8(0x87, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  DUP9(0x88, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  DUP10(0x89, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  DUP11(0x8a, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  DUP12(0x8b, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  DUP13(0x8c, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  DUP14(0x8d, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  DUP15(0x8e, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  DUP16(0x8f, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  SWAP1(0x90, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  SWAP2(0x91, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  SWAP3(0x92, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  SWAP4(0x93, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  SWAP5(0x94, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  SWAP6(0x95, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  SWAP7(0x96, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  SWAP8(0x97, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  SWAP9(0x98, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  SWAP10(0x99, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  SWAP11(0x9a, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  SWAP12(0x9b, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  SWAP13(0x9c, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  SWAP14(0x9d, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  SWAP15(0x9e, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  SWAP16(0x9f, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  LOG0(0xa0, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  LOG1(0xa1, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  LOG2(0xa2, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  LOG3(0xa3, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  LOG4(0xa4, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  CREATE(0xf0, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  CALL(0xf1, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  CALLCODE(0xf2, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  RETURN(0xf3, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  DELEGATECALL(0xf4, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  CREATE2(0xf5, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  STATICCALL(0xfa, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  REVERT(0xfd, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  INVALID(0xfe, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings),
  SELFDESTRUCT(0xff, flags, stackSettings, memoryFlags, moduleFlags, mxpSettings);

  public final long value;
  public final EnumSet<InstructionFlags> flags;
  public final StackSettings stackSettings;
  public final EnumSet<MemoryFlags> memoryFlags;
  public final EnumSet<ModuleFlags> moduleFlags;
  public final MxpSettings mxpSettings;

  private static final Map<Long, OpCode> BY_VALUE = new HashMap<>(values().length);

  static {
    for (OpCode o : values()) {
      BY_VALUE.put(o.value, o);
    }
  }

  OpCode(
      final int value,
      EnumSet<InstructionFlags> flags,
      StackSettings stackSettings,
      EnumSet<MemoryFlags> memoryFlags,
      EnumSet<ModuleFlags> moduleFlags,
      MxpSettings mxpSettings) {
    this.value = value;
    this.flags = flags;
    this.stackSettings = stackSettings;
    this.memoryFlags = memoryFlags;
    this.moduleFlags = moduleFlags;
    this.mxpSettings = mxpSettings;
  }

  public static OpCode of(final long value) {
    if (!BY_VALUE.containsKey(value)) {
      throw new AssertionError("No OpCode with value " + value + " is defined.");
    }

    return BY_VALUE.get(value);
  }

  public int numberOfArguments() {
    return switch (this) {
      case STOP -> 0;
      case ADD -> 2;
      case MUL -> 2;
      case SUB -> 2;
      case DIV -> 2;
      case SDIV -> 2;
      case MOD -> 2;
      case SMOD -> 2;
      case ADDMOD -> 3;
      case MULMOD -> 3;
      case EXP -> 2;
      case SIGNEXTEND -> 2;
      case LT -> 2;
      case GT -> 2;
      case SLT -> 2;
      case SGT -> 2;
      case EQ -> 2;
      case ISZERO -> 1;
      case AND -> 2;
      case OR -> 2;
      case XOR -> 2;
      case NOT -> 2;
      case BYTE -> 2;
      case SHL -> 2;
      case SHR -> 2;
      case SAR -> 2;
      case SHA3 -> 2;
      case ADDRESS -> 0;
      case BALANCE -> 1;
      case ORIGIN -> 0;
      case CALLER -> 0;
      case CALLVALUE -> 0;
      case CALLDATALOAD -> 2;
      case CALLDATASIZE -> 0;
      case CALLDATACOPY -> 3;
      case CODESIZE -> 0;
      case CODECOPY -> 3;
      case GASPRICE -> 0;
      case EXTCODESIZE -> 1;
      case EXTCODECOPY -> 4;
      case RETURNDATASIZE -> 0;
      case RETURNDATACOPY -> 3;
      case EXTCODEHASH -> 1;
      case BLOCKHASH -> 1;
      case COINBASE -> 0;
      case TIMESTAMP -> 0;
      case NUMBER -> 0;
      case DIFFICULTY -> 0;
      case GASLIMIT -> 0;
      case CHAINID -> 0;
      case SELFBALANCE -> 0;
      case BASEFEE -> 0;
      case POP -> 0;
      case MLOAD -> 1;
      case MSTORE -> 2;
      case MSTORE8 -> 2;
      case SLOAD -> 1;
      case SSTORE -> 2;
      case JUMP -> 1;
      case JUMPI -> 2;
      case PC -> 0;
      case MSIZE -> 0;
      case GAS -> 0;
      case JUMPDEST -> 0;
      case PUSH0 -> 0;
      case PUSH1 -> 0;
      case PUSH2 -> 0;
      case PUSH3 -> 0;
      case PUSH4 -> 0;
      case PUSH5 -> 0;
      case PUSH6 -> 0;
      case PUSH7 -> 0;
      case PUSH8 -> 0;
      case PUSH9 -> 0;
      case PUSH10 -> 0;
      case PUSH11 -> 0;
      case PUSH12 -> 0;
      case PUSH13 -> 0;
      case PUSH14 -> 0;
      case PUSH15 -> 0;
      case PUSH16 -> 0;
      case PUSH17 -> 0;
      case PUSH18 -> 0;
      case PUSH19 -> 0;
      case PUSH20 -> 0;
      case PUSH21 -> 0;
      case PUSH22 -> 0;
      case PUSH23 -> 0;
      case PUSH24 -> 0;
      case PUSH25 -> 0;
      case PUSH26 -> 0;
      case PUSH27 -> 0;
      case PUSH28 -> 0;
      case PUSH29 -> 0;
      case PUSH30 -> 0;
      case PUSH31 -> 0;
      case PUSH32 -> 0;
      case DUP1 -> 0;
      case DUP2 -> 0;
      case DUP3 -> 0;
      case DUP4 -> 0;
      case DUP5 -> 0;
      case DUP6 -> 0;
      case DUP7 -> 0;
      case DUP8 -> 0;
      case DUP9 -> 0;
      case DUP10 -> 0;
      case DUP11 -> 0;
      case DUP12 -> 0;
      case DUP13 -> 0;
      case DUP14 -> 0;
      case DUP15 -> 0;
      case DUP16 -> 0;
      case SWAP1 -> 0;
      case SWAP2 -> 0;
      case SWAP3 -> 0;
      case SWAP4 -> 0;
      case SWAP5 -> 0;
      case SWAP6 -> 0;
      case SWAP7 -> 0;
      case SWAP8 -> 0;
      case SWAP9 -> 0;
      case SWAP10 -> 0;
      case SWAP11 -> 0;
      case SWAP12 -> 0;
      case SWAP13 -> 0;
      case SWAP14 -> 0;
      case SWAP15 -> 0;
      case SWAP16 -> 0;
      case LOG0 -> 2;
      case LOG1 -> 3;
      case LOG2 -> 4;
      case LOG3 -> 5;
      case LOG4 -> 6;
      case CREATE -> 3;
      case CALL -> 7;
      case CALLCODE -> 7;
      case RETURN -> 2;
      case DELEGATECALL -> 6;
      case CREATE2 -> 4;
      case STATICCALL -> 6;
      case REVERT -> 3;
      case INVALID -> 0;
      case SELFDESTRUCT -> 1;
      default -> {
        throw new RuntimeException("unaccounted opcode");
      }
    };
  }
}
