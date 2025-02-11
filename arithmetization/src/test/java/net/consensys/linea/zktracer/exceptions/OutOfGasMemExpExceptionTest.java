package net.consensys.linea.zktracer.exceptions;

import static net.consensys.linea.zktracer.DynamicGasCostUtils.getGasCostForMessageCall;
import static net.consensys.linea.zktracer.module.constants.GlobalConstants.*;
import static net.consensys.linea.zktracer.module.hub.signals.TracedException.OUT_OF_GAS_EXCEPTION;
import static org.junit.jupiter.api.Assertions.assertEquals;

import net.consensys.linea.UnitTestWatcher;
import net.consensys.linea.testing.BytecodeCompiler;
import net.consensys.linea.testing.BytecodeRunner;
import net.consensys.linea.zktracer.opcode.OpCode;
import org.apache.tuweni.bytes.Bytes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(UnitTestWatcher.class)
public class OutOfGasMemExpExceptionTest {
  /**
   * Trigger out of gas exception for a MSTORE operation with a gas limit that is too low to cover
   * the memory expansion
   */
  @Test
  void outOfGasExceptionMStore() {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        .push(Bytes.fromHexString("0xFF")) // value
        .push(0) // offset
        .op(OpCode.MSTORE);

    Bytes pgCompile = program.compile();
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);

    long gasCostMinusOne = getGasCostForMessageCall(pgCompile) - 1;

    bytecodeRunner.run(gasCostMinusOne);
    assertEquals(
        OUT_OF_GAS_EXCEPTION,
        bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
  }

  /**
   * Trigger out of gas exception for a MSTORE8 operation with a gas limit that is too low to cover
   * the memory expansion
   */
  @Test
  void outOfGasExceptionMStore8() {
    BytecodeCompiler program = BytecodeCompiler.newProgram();

    program
        .push(Bytes.fromHexString("0xFFFF")) // value
        .push(0) // offset
        .op(OpCode.MSTORE8);

    Bytes pgCompile = program.compile();
    BytecodeRunner bytecodeRunner = BytecodeRunner.of(pgCompile);

    long gasCostMinusOne = getGasCostForMessageCall(pgCompile) - 1;

    bytecodeRunner.run(gasCostMinusOne);
    assertEquals(
        OUT_OF_GAS_EXCEPTION,
        bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
  }

  /**
   * Trigger out of gas exception for a MLOAD operation with a gas limit that is too low to cover
   * its memory expansion
   */
  @Test
  void outOfGasExceptionMLoad() {
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

    long gasCostMinusOne = getGasCostForMessageCall(pgCompile) - 1;

    bytecodeRunner.run(gasCostMinusOne);
    assertEquals(
        OUT_OF_GAS_EXCEPTION,
        bytecodeRunner.getHub().previousTraceSection().commonValues.tracedException());
  }
}
