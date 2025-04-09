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
package net.consensys.linea.zktracer.exceptions;

import static net.consensys.linea.zktracer.module.hub.signals.TracedException.OUT_OF_GAS_EXCEPTION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.testing.ToyAccount;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Wei;

public class ExceptionUtils {

  public static Bytes address1 =
      Bytes.fromHexString("0x1FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
  public static Bytes address2 =
      Bytes.fromHexString("0x2FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
  public static Bytes address3 =
      Bytes.fromHexString("0x3FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
  public static Bytes address4 =
      Bytes.fromHexString("0x4FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
  public static Bytes salt =
      Bytes.fromHexString("0x1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef");

  static void assertEqualsOutOfGasIfCornerCaseMinusOneElseAssertNotEquals(
      int cornerCase, BytecodeRunner bytecodeRunner) {
    if (cornerCase == -1) {
      assertEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    } else {
      assertNotEquals(
          OUT_OF_GAS_EXCEPTION,
          bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
    }
  }

  public static ToyAccount getAccountForCodeAddress(Bytes bytecode) {
    return ToyAccount.builder()
        .balance(Wei.fromEth(1))
        .nonce(10)
        .address(Address.fromHexString("c0de"))
        .code(bytecode)
        .build();
  }

  public static BytecodeCompiler getPgStaticCallToCodeAddress(int gas) {
    return BytecodeCompiler.newProgram()
        .push(0) // byte size of return data
        .push(0) // retOffset
        .push(0) // byte size calldata
        .push(0) // argsOffset
        .push("c0de") // Address of account
        .push(gas) // gas
        .op(OpCode.STATICCALL);
  }

  public static BytecodeCompiler getPgStaticCallToCodeAccount() {
    return BytecodeCompiler.newProgram()
        .push(0) // byte size of return data
        .push(0) // retOffset
        .push(0) // byte size calldata
        .push(0) // argsOffset
        .push("c0de") // Address of account
        .op(OpCode.GAS) // gas
        .op(OpCode.STATICCALL);
  }

  public static BytecodeCompiler getProgramRDC(boolean withRDCX, boolean withMXPX) {
    // if withMXPX, we set an offset to trigger MXPX else regular MXP
    Bytes offsetRDC =
        withMXPX
            ? Bytes.fromHexStringLenient("0xFFFFFFFF")
            : Bytes.ofUnsignedLong(65).trimLeadingZeros();
    // 1. Execute static call
    BytecodeCompiler programStartWithStaticCall = getPgStaticCallToCodeAccount();
    // 2. Clean the stack
    programStartWithStaticCall.op(OpCode.POP).op(OpCode.RETURNDATASIZE);
    // if withRDCX is true, we add the code to trigger the exception
    // 3. Trigger exceptional return data copy
    if (withRDCX) {
      programStartWithStaticCall
          .push(1)
          .op(OpCode.ADD); // size = RDS + 1, which will trigger the `returnDataCopyException`
    }
    programStartWithStaticCall
        .push(0) // offset
        .push(offsetRDC) // destoffset, trigger mem expansion
        .op(OpCode.RETURNDATACOPY);
    // Bytes.fromHexStringLenient("0xFFFFFFFF")
    return programStartWithStaticCall;
  }

  public static BytecodeCompiler getPgPushInitCodeToMem() {
    Bytes initCodePart1 =
        Bytes.fromHexString("0x7F7EFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
    Bytes initCodePart2 =
        Bytes.fromHexString("0xFF60005260206000F30000000000000000000000000000000000000000000000");

    return BytecodeCompiler.newProgram()
        .push(initCodePart1) // value
        .push(0) // offset
        .op(OpCode.MSTORE)
        .push(initCodePart2) // value
        .push(32) // offset
        .op(OpCode.MSTORE);
  }
}
