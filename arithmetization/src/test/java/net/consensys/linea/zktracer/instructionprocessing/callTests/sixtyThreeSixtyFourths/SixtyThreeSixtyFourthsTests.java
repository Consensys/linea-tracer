package net.consensys.linea.zktracer.instructionprocessing.callTests.sixtyThreeSixtyFourths;

import static com.google.common.base.Preconditions.checkArgument;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.GAS_CONST_G_CALL_VALUE;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.GAS_CONST_G_NEW_ACCOUNT;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.GAS_CONST_G_WARM_ACCESS;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.PRC_BLAKE2F_SIZE;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.WORD_SIZE;
import static net.consensys.linea.zktracer.module.hub.signals.TracedException.OUT_OF_GAS_EXCEPTION;
import static net.consensys.linea.zktracer.opcode.OpCode.CALL;
import static net.consensys.linea.zktracer.opcode.OpCode.MLOAD;
import static net.consensys.linea.zktracer.opcode.OpCode.MSTORE8;
import static net.consensys.linea.zktracer.opcode.OpCode.POP;
import static org.hyperledger.besu.datatypes.Address.ALTBN128_ADD;
import static org.hyperledger.besu.datatypes.Address.ALTBN128_MUL;
import static org.hyperledger.besu.datatypes.Address.ALTBN128_PAIRING;
import static org.hyperledger.besu.datatypes.Address.BLAKE2B_F_COMPRESSION;
import static org.hyperledger.besu.datatypes.Address.ECREC;
import static org.hyperledger.besu.datatypes.Address.ID;
import static org.hyperledger.besu.datatypes.Address.MODEXP;
import static org.hyperledger.besu.datatypes.Address.RIPEMD160;
import static org.hyperledger.besu.datatypes.Address.SHA256;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.testing.ToyAccount;
import net.consensys.linea.zktracer.module.constants.GlobalConstants;
import net.consensys.linea.zktracer.module.oob.OobOperation;
import net.consensys.linea.zktracer.opcode.OpCode;
import net.consensys.linea.zktracer.precompiles.PrecompileUtils;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.hyperledger.besu.datatypes.Address;
import org.hyperledger.besu.datatypes.Wei;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SixtyThreeSixtyFourthsTests {

  /*
  Cases to cover:

  * value = 0
  If precompileGasCost >= 2300 then we are interested in:
   * value = 1, targetAddressExists = false
   * value = 1, targetAddressExists = true

  Note: BLAKE2F and MODEXP requires a non-zero non-trivial input to have a cost greater than 2300.
  Other precompiles only require a proper call data size at most.
  */

  final Bytes INFINITE_GAS = Bytes.fromHexString("ff".repeat(32));

  // BLAKE2F specific parameters
  final int rLeadingByte = 0x09;
  final int r = rLeadingByte << 8;

  // MODEXP specific parameters
  int bbs = 2;
  int ebs = 6;
  int mbs = 128;
  int exponentLog = 0;
  List<ToyAccount> additionalAccounts = new ArrayList<>();
  Address codeOwnerAddress = Address.fromHexString("0xC0DE");

  // Cost of preCallProgram in different scenarios:
  // (address, transfersValue) -> gasCost
  final Map<Address, Map<Boolean, Long>> preCallProgramGasMap =
      Stream.of(
              ECREC,
              SHA256,
              RIPEMD160,
              ID,
              MODEXP,
              ALTBN128_ADD,
              ALTBN128_MUL,
              ALTBN128_PAIRING,
              BLAKE2B_F_COMPRESSION)
          .collect(
              Collectors.toMap(
                  address -> address,
                  address ->
                      new HashMap<>() {
                        {
                          put(
                              false,
                              BytecodeRunner.of(preCallProgram(address, false, false, 0))
                                  .runOnlyForGasCost(additionalAccounts));
                          put(
                              true,
                              BytecodeRunner.of(preCallProgram(address, false, true, 0))
                                  .runOnlyForGasCost(additionalAccounts));
                        }
                      }));

  // Note: transferValue = false and cds = 0 as we are interested only in the cost of the
  // corresponding PUSHes here

  @ParameterizedTest
  @MethodSource("fixedCostLTStipendTestSource")
  void fixedCostLTStipendTest(long gasLimit, boolean insufficientGasForPrecompileExpected) {
    // Only ECADD falls in this scenario and whenever transferValue = true, gas is enough
    // Thus, we only test the case in which transferValue = false

    final BytecodeCompiler program = BytecodeCompiler.newProgram();

    program.immediate(preCallProgram(ALTBN128_ADD, false, false, 0)).op(CALL);

    final BytecodeRunner bytecodeRunner = BytecodeRunner.of(program);
    bytecodeRunner.run(gasLimit);

    // insufficientGasForPrecompileExpected = true  =>
    // targetCalleeGas = 63/64 * (252 - 100) = 150
    // insufficientGasForPrecompileExpected = false =>
    // targetCalleeGas = 63/64 * (251 - 100) = 149

    assertNotEquals(
        OUT_OF_GAS_EXCEPTION,
        bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());

    final boolean insufficientGasForPrecompileActual =
        bytecodeRunner.getHub().oob().operations().stream()
            .anyMatch(OobOperation::isInsufficientGasForPrecompile);

    assertEquals(insufficientGasForPrecompileExpected, insufficientGasForPrecompileActual);
  }

  Stream<Arguments> fixedCostLTStipendTestSource() {
    List<Arguments> arguments = new ArrayList<>();
    final long targetCalleeGas = PrecompileUtils.getECADDCost();
    for (int cornerCase : List.of(0, -1)) {
      final long gasLimit =
          getGasLimit(
              targetCalleeGas + cornerCase,
              false,
              false,
              preCallProgramGasMap.get(ALTBN128_ADD).get(false));
      arguments.add(Arguments.of(gasLimit, cornerCase == -1));
    }
    return arguments.stream();
  }

  @ParameterizedTest
  @MethodSource("costGEQStipendTest")
  void costGEQStipendTest(
      Address address,
      long gasLimit,
      boolean insufficientGasForPrecompileExpected,
      boolean transfersValue,
      boolean targetAddressExists,
      int cds) {
    final BytecodeCompiler program = BytecodeCompiler.newProgram();
    // ECREC, SHA256, RIPEMD160, ID, ALTBN128_MUL and ALTBN128_PAIRING fall in this scenario

    program.immediate(preCallProgram(address, transfersValue, targetAddressExists, cds)).op(CALL);

    final BytecodeRunner bytecodeRunner = BytecodeRunner.of(program);
    bytecodeRunner.run(gasLimit, additionalAccounts);

    // ECMUL case:

    // transferValue = false, targetAddressExists = false

    // insufficientGasForPrecompileExpected = true  =>
    // targetCalleeGas = 63/64 * (6195 - 100) = 6000
    // insufficientGasForPrecompileExpected = false =>
    // targetCalleeGas = 63/64 * (6194 - 100) = 5999

    // transfersValue = true, targetAddressExists = false

    // insufficientGasForPrecompileExpected = true  =>
    // targetCalleeGas = 63/64 * (37858 - (100 + 9000 + 25000)) + 2300 = 6000
    // insufficientGasForPrecompileExpected = false =>
    // targetCalleeGas = 63/64 * (37857 - (100 + 9000 + 25000)) + 2300 = 5999

    // transfersValue = true, targetAddressExists = true

    // insufficientGasForPrecompileExpected = true  =>
    // targetCalleeGas = 63/64 * (12858 - (100 + 9000)) + 2300 = 6000
    // insufficientGasForPrecompileExpected = false =>
    // targetCalleeGas = 63/64 * (12857 - (100 + 9000)) + 2300 = 5999

    assertNotEquals(
        OUT_OF_GAS_EXCEPTION,
        bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());

    final boolean insufficientGasForPrecompileActual =
        bytecodeRunner.getHub().oob().operations().stream()
            .anyMatch(OobOperation::isInsufficientGasForPrecompile);

    assertEquals(insufficientGasForPrecompileExpected, insufficientGasForPrecompileActual);
  }

  Stream<Arguments> costGEQStipendTest() {
    List<Arguments> arguments = new ArrayList<>();
    for (Address address :
        List.of(
            ECREC,
            SHA256,
            RIPEMD160,
            ID,
            MODEXP,
            ALTBN128_MUL,
            ALTBN128_PAIRING,
            BLAKE2B_F_COMPRESSION)) {
      final int cds = getCallDataSize(address);
      final long targetCalleeGas =
          address == BLAKE2B_F_COMPRESSION
              ? PrecompileUtils.getBLAKE2FCost(r)
              : address == MODEXP
                  ? PrecompileUtils.getMODEXPCost(bbs, mbs, exponentLog)
                  : PrecompileUtils.getPrecompileCost(address, cds);
      for (int cornerCase : List.of(0, -1)) {
        for (boolean transfersValue : List.of(true, false)) {
          for (boolean targetAddressExists : List.of(true, false)) {
            if (!transfersValue && targetAddressExists) {
              continue; // no need to test this case
            }
            final long gasLimit =
                getGasLimit(
                    targetCalleeGas + cornerCase,
                    transfersValue,
                    targetAddressExists,
                    preCallProgramGasMap.get(address).get(targetAddressExists));
            arguments.add(
                Arguments.of(
                    address, gasLimit, cornerCase == -1, transfersValue, targetAddressExists, cds));
          }
        }
      }
    }
    return arguments.stream();
  }

  // Support methods
  Bytes preCallProgram(
      Address address, boolean transfersValue, boolean targetAddressExists, int cds) {
    return BytecodeCompiler.newProgram()
        .immediate(expandMemoryTo1024Words())
        .immediate(
            targetAddressExists
                ? call(
                    INFINITE_GAS,
                    address,
                    address == BLAKE2B_F_COMPRESSION
                        ? PRC_BLAKE2F_SIZE
                        : 0, // For BLAKE2F we need a meaningful cds for the call to succeed
                    true)
                : Bytes.EMPTY)
        .immediate(address == MODEXP ? storeModexpInput(bbs, mbs, ebs) : Bytes.EMPTY)
        .immediate(address == BLAKE2B_F_COMPRESSION ? storeBlake2fInput(rLeadingByte) : Bytes.EMPTY)
        .immediate(pushCallArguments(INFINITE_GAS, address, cds, transfersValue))
        .compile();
  }

  Bytes expandMemoryTo1024Words() {
    return BytecodeCompiler.newProgram().push(1024 * WORD_SIZE).op(MLOAD).op(POP).compile();
  }

  Bytes call(Bytes gas, Address address, int cds, boolean transfersValue) {
    return BytecodeCompiler.newProgram()
        .immediate(pushCallArguments(gas, address, cds, transfersValue))
        .op(CALL)
        .compile();
  }

  Bytes pushCallArguments(Bytes gas, Address address, int cds, boolean transfersValue) {
    return BytecodeCompiler.newProgram()
        .push(0) // returnAtCapacity
        .push(0) // returnAtOffset
        .push(cds) // callDataSize
        .push(0) // callDataOffset
        .push(transfersValue ? 1 : 0) // value
        .push(address) // address
        .push(gas) // gas
        .compile();
  }

  // TODO: the two methods below are essentially duplicates of the ones in
  //  LowGasStipendPrecompileCallTests. Consider unifying them in a common class.
  Bytes storeBlake2fInput(int rLeadingByte) {
    return BytecodeCompiler.newProgram().push(rLeadingByte).push(2).op(MSTORE8).compile();
  }

  private Bytes storeModexpInput(int bbs, int mbs, int ebs) {
    final Bytes32 bbsPadded = Bytes32.leftPad(Bytes.of(bbs));
    final Bytes32 ebsPadded = Bytes32.leftPad(Bytes.of(ebs));
    final Bytes32 mbsPadded = Bytes32.leftPad(Bytes.of(mbs));
    final Bytes bem =
        Bytes.fromHexString("0x" + "aa".repeat(bbs) + "ff".repeat(ebs) + "bb".repeat(mbs));
    final Bytes modexpInput = Bytes.concatenate(bbsPadded, ebsPadded, mbsPadded, bem);

    // codeOwnerAccount owns the bytecode that will be given as input to MODEXP through EXTCODECOPY
    final ToyAccount codeOwnerAccount =
        ToyAccount.builder()
            .balance(Wei.of(0))
            .nonce(1)
            .address(codeOwnerAddress)
            .code(modexpInput)
            .build();
    additionalAccounts = List.of(codeOwnerAccount);

    // This is computed here for convenience, and it is used for pricing the MODEXP precompile
    exponentLog =
        Math.max(OobOperation.computeExponentLog(modexpInput, 96 + bbs + ebs + mbs, bbs, ebs), 1);

    // Copy to targetOffset the code of codeOwnerAccount
    return BytecodeCompiler.newProgram()
        .push(codeOwnerAddress)
        .op(OpCode.EXTCODESIZE) // size
        .push(0) // offset
        .push(0) // targetOffset
        .push(codeOwnerAddress) // address
        .op(OpCode.EXTCODECOPY)
        .compile();
  }

  long getGasLimit(
      long targetCalleeGas,
      boolean transfersValue,
      boolean targetAddressExists,
      long preCallProgramGas) {
    /* gasLimit = preCallProgramGasCost + gasPreCall
    /  63/64 * (gasPreCall - gasUpFront) + stipend = targetCalleeGas
    /  x = gasPreCall - gasUpFront
    /  k = x / 64 (integer division)
    /  l = x - 64 * k
    /  63 * k + l + stipend = targetCalleeGas
    / find gasLimit going backwards
    */
    final long stipend = transfersValue ? GlobalConstants.GAS_CONST_G_CALL_STIPEND : 0;
    checkArgument(targetCalleeGas >= stipend);
    final long l = (targetCalleeGas - stipend) % 63;
    final long k = (targetCalleeGas - stipend - l) / 63;
    checkArgument(targetCalleeGas == 63 * k + l + stipend);
    final long gasUpfront = getUpfrontGasCost(transfersValue, targetAddressExists);
    final long gasPreCall = (targetCalleeGas - stipend) * 64 / 63 + gasUpfront;
    return preCallProgramGas + gasPreCall; // gasLimit
  }

  long getUpfrontGasCost(boolean transfersValue, boolean targetAddressExists) {
    // GAS_CONST_G_WARM_ACCESS = 100
    // GAS_CONST_G_COLD_ACCOUNT_ACCESS = 2600
    // GAS_CONST_G_CALL_VALUE = 9000
    // GAS_CONST_G_NEW_ACCOUNT = 25000
    return GAS_CONST_G_WARM_ACCESS
        + (transfersValue
            ? GAS_CONST_G_CALL_VALUE + (targetAddressExists ? 0 : GAS_CONST_G_NEW_ACCOUNT)
            : 0);
  }

  int getCallDataSize(Address address) {
    if (address == SHA256 || address == RIPEMD160 || address == ID) {
      return 1024 * WORD_SIZE; // Ensures cost is greater than stipend
    } else if (address == MODEXP) {
      return 96 + bbs + ebs + mbs; // Ensures cost is greater than stipend with non-zero non-trivial
    } else if (address == BLAKE2B_F_COMPRESSION) {
      return PRC_BLAKE2F_SIZE; // Ensures cost is greater than stipend with non-zero non-trivial input
    } else {
      return 0;
    }
  }
}
