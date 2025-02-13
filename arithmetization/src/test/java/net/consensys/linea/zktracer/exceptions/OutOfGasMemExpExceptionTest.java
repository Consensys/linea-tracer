package net.consensys.linea.zktracer.exceptions;

import static net.consensys.linea.zktracer.DynamicGasCostUtils.getGasCostForMessageCall;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.*;
import static net.consensys.linea.zktracer.module.hub.signals.TracedException.OUT_OF_GAS_EXCEPTION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import net.consensys.linea.UnitTestWatcher;
import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;
import org.hyperledger.besu.datatypes.Wei;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@ExtendWith(UnitTestWatcher.class)
public class OutOfGasMemExpExceptionTest {
  /**
   * Trigger out of gas exception for a MSTORE operation with a gas limit that is too low to cover
   * the memory expansion
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 0, 1})
  void outOfGasExceptionMStore(int cornerCase) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        .push(Bytes.fromHexString("0xFF")) // value
        .push(0) // offset
        .op(OpCode.MSTORE);

    Bytes pgCompile = program.compile();
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);

    long gasCost = getGasCostForMessageCall(pgCompile);

    bytecodeRunner.run(gasCost + cornerCase);
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

  /**
   * Trigger out of gas exception for a MSTORE8 operation with a gas limit that is too low to cover
   * the memory expansion
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 0, 1})
  void outOfGasExceptionMStore8(int cornerCase) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        .push(Bytes.fromHexString("0xFFFF")) // value
        .push(0) // offset
        .op(OpCode.MSTORE8);

    Bytes pgCompile = program.compile();
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);

    long gasCost = getGasCostForMessageCall(pgCompile);

    bytecodeRunner.run(gasCost + cornerCase);
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

  /**
   * Trigger out of gas exception for a MLOAD operation with a gas limit that is too low to cover
   * its memory expansion
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 0, 1})
  void outOfGasExceptionMLoad(int cornerCase) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        .push(Bytes.fromHexString("0xFF")) // value
        .push(0) // offset
        .op(OpCode.MSTORE);

    program
        .push(17) // value
        .op(OpCode.MLOAD);

    Bytes pgCompile = program.compile();
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);

    long gasCost = getGasCostForMessageCall(pgCompile);

    bytecodeRunner.run(gasCost + cornerCase);
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

  @ParameterizedTest
  @ValueSource(ints = {-1, 0, 1})
  void outOfGasExceptionCallDataCopy(int cornerCase) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        .push(32) // value
        .push(0) // offset
        .push(0) // offset
        .op(OpCode.CALLDATACOPY);

    Bytes pgCompile = program.compile();
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);

    long gasCost = bytecodeRunner.runOnlyForGasCost(Wei.fromEth(1), 61_000_000L, List.of());

    bytecodeRunner.run(gasCost + cornerCase);
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

  @ParameterizedTest
  @ValueSource(ints = {-1, 0, 1})
  void outOfGasExceptionCodeCopy(int cornerCase) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        .push(Bytes.fromHexString("0xFA")) // value
        .push(0) // offset
        .op(OpCode.MSTORE)
        .push(3) // size
        .push(0) // offset
        .push(32) // destoffset
        .op(OpCode.CODECOPY);

    Bytes pgCompile = program.compile();
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);

    long gasCost = bytecodeRunner.runOnlyForGasCost(Wei.fromEth(1), 61_000_000L, List.of());

    bytecodeRunner.run(gasCost + cornerCase);
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

  @ParameterizedTest
  @ValueSource(ints = {-1, 0, 1})
  void outOfGasExceptionExtCodeCopy(int cornerCase) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        // constructor
        .push(
            Bytes.fromHexString(
                "0x7FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")) // value
        .push(0) // offset
        .op(OpCode.MSTORE)
        .push(
            Bytes.fromHexString(
                "0xFF60005260206000F30000000000000000000000000000000000000000000000")) // value
        .push(32) // offset
        .op(OpCode.MSTORE)
        // Create the contract
        .push(41)
        .push(0)
        .push(0)
        .op(OpCode.CREATE)
        .push(32) // size
        .push(0) // offset
        .push(33) // destoffset
        .op(OpCode.DUP4) //
        .op(OpCode.EXTCODECOPY);

    Bytes pgCompile = program.compile();
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);

    long gasCost = bytecodeRunner.runOnlyForGasCost(Wei.fromEth(1), 61_000_000L, List.of());

    bytecodeRunner.run(gasCost + cornerCase);
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

  @ParameterizedTest
  @ValueSource(ints = {-1, 0, 1})
  void outOfGasExceptionReturn(int cornerCase) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        .push(
            Bytes.fromHexString(
                "0xF00000000000000000000000000000000000000000000000000000000000FFAA")) // value
        .push(0) // offset
        .op(OpCode.MSTORE)
        .push(3) // value
        .push(30) // offset
        .op(OpCode.RETURN);

    Bytes pgCompile = program.compile();
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);

    long gasCost = bytecodeRunner.runOnlyForGasCost(Wei.fromEth(1), 61_000_000L, List.of());

    bytecodeRunner.run(gasCost + cornerCase);
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

  @ParameterizedTest
  @ValueSource(ints = {-1, 0, 1})
  void outOfGasExceptionReturnDataCopy(int cornerCase) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        // constructor
        .push(
            Bytes.fromHexString(
                "0x7F7FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")) // value
        .push(0) // offset
        .op(OpCode.MSTORE)
        .push(
            Bytes.fromHexString(
                "0xFF6000527FFF60005260206000F3000000000000000000000000000000000000")) // value
        .push(32) // offset
        .op(OpCode.MSTORE)
        .push(
            Bytes.fromHexString(
                "0x000000000060205260296000F300000000000000000000000000000000000000")) // value
        .push(64) // offset
        .op(OpCode.MSTORE)
        // Create the contract
        .push(77)
        .push(0)
        .push(0)
        .op(OpCode.CREATE)
        .push(0)
        .push(0)
        .push(0)
        .push(0)
        .op(OpCode.DUP5)
        .push(Bytes.fromHexString("0xFFFFFFFF"))
        .op(OpCode.STATICCALL)
        .op(OpCode.POP)
        .op(OpCode.POP)
        .push(32)
        .push(0)
        .push(65)
        .op(OpCode.RETURNDATACOPY);

    Bytes pgCompile = program.compile();
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);

    long gasCost = bytecodeRunner.runOnlyForGasCost(Wei.fromEth(1), 61_000_000L, List.of());

    bytecodeRunner.run(gasCost + cornerCase);
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

  @ParameterizedTest
  @ValueSource(ints = {-1, 0, 1})
  void outOfGasExceptionRevert(int cornerCase) {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        .push(
            Bytes.fromHexString(
                "0xF00000000000000000000000000000000000000000000000000000000000FFAA")) // value
        .push(0) // offset
        .op(OpCode.MSTORE)
        .push(3) // value
        .push(30) // offset
        .op(OpCode.RETURN);

    Bytes pgCompile = program.compile();
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);

    long gasCost = bytecodeRunner.runOnlyForGasCost(Wei.fromEth(1), 61_000_000L, List.of());

    bytecodeRunner.run(gasCost + cornerCase);
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
}
